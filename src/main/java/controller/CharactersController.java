package controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import model.editor.TileInfo;
import model.editor.TileTypeEnum;
import model.editor.items.BodyPartEnum;
import model.entity.DirectionEnum;
import model.entity.map.ClosableCellInfo;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import model.entity.player.Player;
import view.CodeLockPanel;
import view.Editor;
import view.Game;
import view.inventory.InventoryPanel;
import view.inventory.PlayerIndicatorsPanel;
import view.params.ParamPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static game.GameParams.mapSize;
import static game.GameParams.tileSize;

/**
 * Действия персонажей
 */
public class CharactersController {
    private static final Random random = new Random();

    /**
     * Увеличить навык
     *
     * @param skillId     идентификатор навыка
     * @param skillPoints на сколько пунктов увеличить
     */
    public static void increaseSkill(int skillId, int skillPoints) {
        var skills = Game.getMap().getPlayer().getParams().getSkills();

        skills.get(skillId).setRealValue(skills.get(skillId).getRealValue() + skillPoints);
        if (skills.get(skillId).getRealValue() > 100) {
            skills.get(skillId).setRealValue(100);
        } else {
            skills.get(skillId).setCurrentValue(skills.get(skillId).getCurrentValue() + skillPoints);
        }

        Game.showMessage(String.format(
                Game.getText("INCREASE_SKILL_MESSAGE"),
                Game.getText(ParamPanel.getSkillsNames().get(skillId) + "_PARAM_NAME"),
                skills.get(skillId).getCurrentValue().toString()),
                Color.GREEN);
    }

    /**
     * Добавить опыт для увеличения навыка
     *
     * @param skillId   идентификатор навыка
     * @param expPoints сколько опыта нужно добавить
     */
    public static void addSkillExp(int skillId, int expPoints) {
        var skill = Game.getMap().getPlayer().getParams().getSkills().get(skillId);
        skill.setExperience(skill.getExperience() + expPoints);
        while (skill.getExperience() >= skill.getRealValue() * 10) {
            skill.setExperience(skill.getExperience() - skill.getRealValue() * 10);
            increaseSkill(skillId, 1);
        }
    }

    /**
     * Подобрать предметы с тайла
     *
     * @param itemsList - подбираемые предметы
     * @param x         - координата х предметов на карте
     * @param y         - координата y предметов на карте
     */
    public static void pickUpItems(List<Items> itemsList, int x, int y) {
        var player = Game.getMap().getPlayer();
        var mapCellInfo = Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y];

        if (isClosedCell(mapCellInfo)) {
            closableInteract(mapCellInfo);
        } else {
            if (itemsList.size() > 1 || itemsList.get(0).getCount() > 1 || mapCellInfo.getTile2Type().equals(TileTypeEnum.CONTAINER)) {
                // показываем вторую панель инвентаря, если на тайле лежит больше 1 предмета, или если это контейнер
                Game.getGameMenu().showContainerInventory(itemsList, x, y);
            } else {
                List<Items> removeList = new ArrayList<>();
                for (Items items : itemsList) {
                    if (ItemsController.addItem(items, player.getInventory(), player) != null) {
                        removeList.add(items);
                    } else {
                        break;
                    }
                }
                mapCellInfo.getItems().removeAll(removeList);
                if (mapCellInfo.getItems().size() == 0) {
                    mapCellInfo.setItems(null);
                } else {
                    Game.showMessage(Game.getText("ERROR_INVENTORY_SPACE"));
                }
                MapController.drawTile(player, x, y);
            }
        }
    }

    /**
     * Тайл карты является запертым контейнером, или контейнером с ловушкой
     *
     * @param mapCellInfo - точка на карте
     *
     * @return true, если точка является запертым контейнером
     */
    private static boolean isClosedCell(MapCellInfo mapCellInfo) {
        TileInfo tileInfo = mapCellInfo.getTile2Info();
        if (tileInfo.getType() != null && TileTypeEnum.valueOf(tileInfo.getType()).equals(TileTypeEnum.CONTAINER)) {
            var closableCellInfo = (ClosableCellInfo) mapCellInfo;
            return closableCellInfo.isLocked() || closableCellInfo.isTrap();
        }
        return false;
    }

    /**
     * Взаимодействие персонажа с картой
     *
     * @param x - координата х предметов на карте
     * @param y - координата y предметов на карте
     */
    public static void interactionWithMap(int x, int y) {
        var player = Game.getMap().getPlayer();
        int Xpos = player.getXMapPos();
        int YPos = player.getYMapPos();
        var mapCellInfo = Game.getMap().getTiles()[Xpos + x][YPos + y];
        TileInfo tileInfo = Editor.getTiles2().get(mapCellInfo.getTile2Id());
        if (tileInfo.getType() != null) {
            switch (TileTypeEnum.valueOf(tileInfo.getType())) {
                case DOOR:
                case CONTAINER: {
                    closableInteract(mapCellInfo);
                    break;
                }
            }
            MapController.drawTile(player, x, y);
        }
    }

    /**
     * Взаимодействие с запираемым тайлом
     *
     * @param mapCellInfo - точка на карте
     */
    private static void closableInteract(MapCellInfo mapCellInfo) {
        TileInfo tileInfo = mapCellInfo.getTile2Info();
        var player = Game.getMap().getPlayer();
        var closableCellInfo = (ClosableCellInfo) mapCellInfo;
        if (closableCellInfo.isLocked()) {
            if (closableCellInfo.isCodeLock()) {
                CodeLockPanel.showPanel(closableCellInfo);
            } else {
                var doorKey = ItemsController.findItemInInventory(closableCellInfo.getKeyId(), player.getInventory());
                if (doorKey != null) {
                    closableCellInfo.setTile2Id(Integer.parseInt(tileInfo.getParams().get(0)));
                    closableCellInfo.setLocked(false);
                    Game.showMessage(
                            String.format(Game.getText("OPENED"), doorKey.getInfo().getName()),
                            Color.GREEN);
                } else {
                    Items pickLock = ItemsController.findPicklockInInventory(player.getInventory());
                    if (pickLock == null) {
                        Game.showMessage(Game.getText("CLOSED"));
                    } else {
                        hackLock(closableCellInfo, pickLock);
                    }
                }
            }
        } else {
            if (closableCellInfo.isTrap()) {
                Items sapperTools = ItemsController.findSapperToolsInInventory(player.getInventory());
                if (sapperTools == null) {
                    closableCellInfo.setTrap(false);
                    Game.showMessage(Game.getText("TRAP_TRIGGERED"));
                    BattleController.applyDamageToPlayer(closableCellInfo.getTrapLevel());
                } else {
                    trapDeactivation(closableCellInfo, sapperTools);
                }
            } else {
                if (tileInfo.getParams() != null) {
                    closableCellInfo.setTile2Id(Integer.parseInt(tileInfo.getParams().get(0)));
                }
            }
        }
    }

    /**
     * Взлом замка
     *
     * @param closableCellInfo - точка на карте с замком
     * @param hackingTools     - инструменты для взлома
     */
    private static void hackLock(ClosableCellInfo closableCellInfo, Items hackingTools) {
        var hackingSkill = Game.getMap().getPlayer().getParams().getSkills().get(11).getCurrentValue(); // уровень навыка
        var dexterity = Game.getMap().getPlayer().getParams().getCharacteristics().get(2).getCurrentValue(); // уровень ловкости
        var hackingToolsLevel = Integer.parseInt(hackingTools.getInfo().getParams().get(0)); // уровень инструментов
        var lockLevel = closableCellInfo.getLockLevel(); // уровень замка
        var hackChance = hackingSkill * 1.5 + dexterity * 0.25 + hackingToolsLevel * 1.5 - lockLevel; // формула рассчета вероятности взлома замка
        if (hackChance < 0) {
            Game.showMessage(Game.getText("CANT_HACK")); // если шанс взлома меньше 0, вообще не пытаемся взломать
        } else {
            ItemsController.damageItem(hackingTools, 1, Game.getMap().getPlayer().getInventory(), Game.getMap().getPlayer());
            if (random.nextInt(100) < hackChance) {
                closableCellInfo.setLocked(false);
                Game.showMessage(
                        String.format(Game.getText("OPENED"), hackingTools.getInfo().getName()),
                        Color.GREEN);
                addSkillExp(11, lockLevel +
                        (hackingSkill < lockLevel ? lockLevel - hackingSkill : 0));
            } else {
                Game.showMessage(Game.getText("HACK_FAILED"));
                addSkillExp(11, 1);
            }
        }
    }

    /**
     * Обезвреживание ловушки
     *
     * @param closableCellInfo - точка на карте с ловушкой
     * @param sapperTools      - сапёрные инструменты
     */
    private static void trapDeactivation(ClosableCellInfo closableCellInfo, Items sapperTools) {
        var hackingSkill = Game.getMap().getPlayer().getParams().getSkills().get(11).getCurrentValue(); // уровень навыка
        var dexterity = Game.getMap().getPlayer().getParams().getCharacteristics().get(2).getCurrentValue(); // уровень ловкости
        var hackingToolsLevel = Integer.parseInt(sapperTools.getInfo().getParams().get(0)); // уровень инструментов
        var trapLevel = closableCellInfo.getTrapLevel(); // уровень замка
        var defuseChance = hackingSkill * 1.5 + dexterity * 0.25 + hackingToolsLevel * 1.5 - trapLevel; // формула рассчета вероятности взлома замка
        if (defuseChance < 0) {
            Game.showMessage(Game.getText("CANT_DEFUSE")); // если шанс взлома меньше 0, вообще не пытаемся взломать
        } else {
            ItemsController.damageItem(sapperTools, 1, Game.getMap().getPlayer().getInventory(), Game.getMap().getPlayer());
            if (random.nextInt(100) < defuseChance) {
                closableCellInfo.setTrap(false);
                Game.showMessage(
                        String.format(Game.getText("DEFUSED"), sapperTools.getInfo().getName()),
                        Color.GREEN);
                addSkillExp(11, (trapLevel + trapLevel / 2) +
                        (hackingSkill < trapLevel ? trapLevel - hackingSkill : 0));
            } else {
                Game.showMessage(Game.getText("DEFUSE_FAILED"));
                addSkillExp(11, 1);
            }
        }
    }

    /**
     * Может ли персонаж при движении сдвинуть лежащий на карте тайл 2 уровня (например, каменный шар)
     *
     * @return true, если персонаж может сдвинуть тайл
     */
    private static boolean canMoveTile2() {
        var player = Game.getMap().getPlayer();
        int x = player.getXPosition();
        int y = player.getYPosition();

        int xPlus = 0;
        int yPlus = 0;

        switch (player.getDirection()) {
            case UP: {
                y = --y;
                yPlus = -1;
                break;
            }
            case DOWN: {
                y = ++y;
                yPlus = 1;
                break;
            }
            case LEFT: {
                x = --x;
                xPlus = -1;
                break;
            }
            case RIGHT: {
                x = ++x;
                xPlus = 1;
                break;
            }
        }
        boolean canMove;
        var tileType = Game.getMap().getTiles()[x][y].getTile2Info().getType();
        try {
            canMove = (tileType != null && tileType.equals(TileTypeEnum.MOVABLE.toString())) &&
                    Game.getMap().getTiles()[x + xPlus][y + yPlus].getTile1Info().isPassability() &&
                    (Game.getMap().getTiles()[x + xPlus][y + yPlus].getTile2Id() == 0);
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }

        if (canMove) {
            Game.getMap().getTiles()[x + xPlus][y + yPlus].setTile2Id(Game.getMap().getTiles()[x][y].getTile2Id());
            Game.getMap().getTiles()[x][y].setTile2Id(0);
            MapController.drawTile(player,
                    player.getXViewPos() + xPlus*2, player.getYViewPos() + yPlus*2);
        }
        return canMove;
    }

    /**
     * Движение персонажа вверх
     *
     * @param player - персонаж
     */
    public static void heroMoveUp(Player player) {
        player.setDirection(DirectionEnum.UP);
        if (player.getYPosition() > 0 && (Game.getMap().getTiles()[player.getXPosition()][player.getYPosition() - 1].getTile1Info().isPassability()) &&
                (Game.getMap().getTiles()
                        [player.getXPosition()][player.getYPosition() - 1].getTile2Info().isPassability() ||
                        canMoveTile2())) {
            if (player.getYPosition() > 0) {
                player.setYPosition(player.getYPosition() - 1);
            }
            if (player.getYMapPos() > 0 && player.getYPosition() - 3 < player.getYMapPos()) {
                player.setYMapPos(player.getYMapPos() - 1);
                MapController.drawCurrentMap();
            } else {
                player.setYViewPos(player.getYViewPos() - 1);
                MapController.drawCurrentMap();
            }
        }
    }

    /**
     * Движение персонажа вниз
     *
     * @param player - персонаж
     */
    public static void heroMoveDown(Player player) {
        player.setDirection(DirectionEnum.DOWN);
        if (player.getYPosition() < (mapSize-1) && (Game.getMap().getTiles()[player.getXPosition()][player.getYPosition() + 1].getTile1Info().isPassability()) &&
                (Game.getMap().getTiles()
                        [player.getXPosition()][player.getYPosition() + 1].getTile2Info().isPassability() ||
                        canMoveTile2())) {
            if (player.getYPosition() < mapSize) {
                player.setYPosition(player.getYPosition() + 1);
            }
            if (player.getYMapPos() < 285 && player.getYPosition() + 3 > player.getYMapPos() + 12) {
                player.setYMapPos(player.getYMapPos() + 1);
                MapController.drawCurrentMap();
            } else {
                player.setYViewPos(player.getYViewPos() + 1);
                MapController.drawCurrentMap();
            }
        }
    }

    /**
     * Движение персонажа влево
     *
     * @param player - персонаж
     */
    public static void heroMoveLeft(Player player) {
        player.setDirection(DirectionEnum.LEFT);
        if (player.getXPosition() > 0 && Game.getMap().getTiles()[player.getXPosition() - 1][player.getYPosition()].getTile1Info().isPassability() &&
                (Game.getMap().getTiles()
                        [player.getXPosition() - 1][player.getYPosition()].getTile2Info().isPassability() ||
                        canMoveTile2())) {
            if (player.getXPosition() > 0) {
                player.setXPosition(player.getXPosition() - 1);
            }
            if (player.getXMapPos() > 0 && player.getXPosition() - 3 < player.getXMapPos()) {
                player.setXMapPos(player.getXMapPos() - 1);
                MapController.drawCurrentMap();
            } else {
                player.setXViewPos(player.getXViewPos() - 1);
                MapController.drawCurrentMap();
            }
        }
    }

    /**
     * Движение персонажа вправо
     *
     * @param player - персонаж
     */
    public static void heroMoveRight(Player player) {
        player.setDirection(DirectionEnum.RIGHT);
        if (player.getXPosition() < (mapSize-1) && Game.getMap().getTiles()
                [player.getXPosition() + 1][player.getYPosition()].getTile1Info().isPassability() &&
                (Game.getMap().getTiles()
                        [player.getXPosition() + 1][player.getYPosition()].getTile2Info().isPassability() ||
                        canMoveTile2())) {
            if (player.getXPosition() < mapSize) {
                player.setXPosition(player.getXPosition() + 1);
            }
            if (player.getXMapPos() < 285 && player.getXPosition() + 3 > player.getXMapPos() + 12) {
                player.setXMapPos(player.getXMapPos() + 1);
                MapController.drawCurrentMap();
            } else {
                player.setXViewPos(player.getXViewPos() + 1);
                MapController.drawCurrentMap();
            }
        }
    }

    /**
     * Нарисовать персонажа игрока и все экипированные на нем предметы
     *
     * @param player - персонаж
     */
    public static void drawPlayerImage(Player player) {
        GraphicsContext gc = Game.getEditor().getCanvas().getGraphicsContext2D();
        var img = player.getImage().getImage();
        double width = img.getWidth();
        double height = img.getHeight();

        var isLeft = player.getDirection().equals(DirectionEnum.LEFT) ||
                player.getDirection().equals(DirectionEnum.UP);
        if (isLeft) {
            gc.drawImage(img,
                    0, 0, tileSize, tileSize,
                    player.getXViewPos() * tileSize + tileSize,
                    player.getYViewPos() * tileSize,
                    -width, height);
        } else {
            gc.drawImage(img,
                    player.getXViewPos() * tileSize, player.getYViewPos() * tileSize);
        }

        for (Pair<BodyPartEnum, Items> bodyPart : player.getWearingItems()) {
            if (bodyPart.getValue() != null) {
                var path = "/graphics/items/" + bodyPart.getValue().getTypeId() + "doll.png";
                var f = new File("/" + Player.class.getProtectionDomain().getCodeSource().getLocation().getPath() + path);
                if (f.exists()) {
                    var image = new Image(path);
                    if (isLeft) {
                        gc.drawImage(image,
                                0, 0, tileSize, tileSize,
                                player.getXViewPos() * tileSize + tileSize,
                                player.getYViewPos() * tileSize,
                                -width, height);
                    } else {
                        gc.drawImage(image,
                                player.getXViewPos() * tileSize, player.getYViewPos() * tileSize);
                    }
                }
            }
        }
        /*var sp = new SnapshotParameters();
        WritableImage writableImage = new WritableImage(40, 40);
        writableImage = gc.getCanvas().snapshot(sp, writableImage);*/
    }

    /**
     * Перегружен ли персонаж
     *
     * @return true, если персонаж перегружен
     */
    public static Boolean isOverloaded(Player player) {
        return player.getCurrentWeight().compareTo(player.getMaxWeight()) > 0;
    }

    /**
     * Добавить в инвентарь персонажа стартовые предметы
     *
     * @param player - персонаж
     */
    public static void setPlayerStartItems(Player player) {
        ItemsController.addItem(new Items(1, 2), player.getInventory(), player);
        ItemsController.addItem(new Items(3, 1), player.getInventory(), player);
        ItemsController.addItem(new Items(11, 10), player.getInventory(), player);
        ItemsController.addItem(new Items(10, 1), player.getInventory(), player);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(23, 1),
                        player.getInventory(),
                        player),
                player);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(25, 1),
                        player.getInventory(),
                        player),
                player);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(26, 1),
                        player.getInventory(),
                        player),
                player);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(20, 1),
                        player.getInventory(),
                        player),
                player);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(21, 1),
                        player.getInventory(),
                        player),
                player);

        var items6 = new Items(22, 1);
        ItemsController.addItem(items6, player.getInventory(), player);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items6.getTypeId(), player.getInventory()),
                player);

        var items7 = new Items(30, 1);
        items7.setCurrentStrength(0);
        ItemsController.addItem(items7, player.getInventory(), player);

        PlayerIndicatorsPanel.setClothesStyle(player.getWearingItems());

        player.setMaxVolume(ItemsController.getMaximumVolume(player));
        player.setCurrentVolume(ItemsController.getCurrVolume(player.getInventory()));
        player.setMaxWeight(ItemsController.getMaximumWeight(player));
        player.setCurrentWeight(ItemsController.getCurrWeight(player.getInventory()));

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();
    }
}
