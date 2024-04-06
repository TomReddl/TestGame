package controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import model.editor.TileInfo;
import model.editor.TileTypeEnum;
import model.editor.items.BodyPartEnum;
import model.entity.DirectionEnum;
import model.entity.Event;
import model.entity.GameCalendar;
import model.entity.ItemTypeEnum;
import model.entity.battle.DamageTypeEnum;
import model.entity.effects.EffectParams;
import model.entity.map.*;
import model.entity.player.GenderEnum;
import model.entity.player.ParamsInfo;
import model.entity.player.Character;
import view.CodeLockPanel;
import view.Editor;
import view.Game;
import view.SelectTimePanel;
import view.inventory.InventoryPanel;
import view.inventory.PlayerIndicatorsPanel;

import java.io.File;
import java.util.*;

import static controller.ItemsController.engineeringWateringCan;
import static controller.ItemsController.lighterId;
import static game.GameParams.*;

/**
 * Действия персонажей
 */
public class CharactersController {
    private static final Random random = new Random();

    /**
     * Рост волос
     */
    public static void growingHair() {
        var hairLength = Game.getMap().getSelecterCharacter().getHairLength();
        if (hairLength < 3) {
            Game.getMap().getSelecterCharacter().setHairLength(hairLength + 1);
        }
        if (Game.getMap().getSelecterCharacter().getGender().equals(GenderEnum.MALE)) { // борода растет только у мужчин
            var beardLength = Game.getMap().getSelecterCharacter().getBeardLength();
            if (beardLength < 3) {
                Game.getMap().getSelecterCharacter().setBeardLength(beardLength + 1);
            }
        }
    }

    /**
     * Нажатие на карту в игре
     *
     * @param x            - координата X места нажатия
     * @param y            - координата Y места нажатия
     * @param isRightMouse - признак нажатия ПКМ
     */
    public static void gameMapClick(double x, double y, boolean isRightMouse) {
        var tileX = (((int) x) / tileSize);
        var tileY = (((int) y) / tileSize);
        var player = Game.getMap().getSelecterCharacter();
        var showOres = false;
        var showEmptiness = false;
        var applyDamage = false;
        if (MapController.isReachable(player, tileX, tileY)) {
            MapCellInfo mapCellInfo = Game.getMap().getTiles()[player.getXMapPos() + tileX]
                    [player.getYMapPos() + tileY];
            var itemInRightHand = player.getWearingItems().get(BodyPartEnum.RIGHT_ARM.ordinal()).values().iterator().next();

            if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.TOOL)) { //если в руках инструмент
                if (itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.WATERING_CAN)) { // если в руках лейка
                    watering(itemInRightHand, mapCellInfo, tileX, tileY, player);
                } else if (itemInRightHand.getTypeId() == lighterId) { // если в руках зажигалка
                    if (mapCellInfo.getTile2Id() == 256) { // поджигаем костёр
                        mapCellInfo.setTile2Id(257);
                    } else {
                        MapController.setFire(mapCellInfo);
                    }
                } else if (itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.METAL_DETECTOR)) { // если в руках металлоискатель
                    showOres = true;
                } else if (itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.ECHOLOCATOR)) { // если в руках эхолокатор
                    showEmptiness = true;
                }
            } else if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.AXE)) { // если в руках топор
                String tileType = mapCellInfo.getTile2Info().getType();
                if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.WOOD)) {
                    int woodId = (mapCellInfo.getTile2Info().getParams() != null && mapCellInfo.getTile2Info().getParams().get("wood") != null) ? // у дерева может быть особая древесина
                            Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("wood")) :
                            ItemsController.woodId;

                    BattleController.applyDamageToMapCell(mapCellInfo, itemInRightHand);
                    applyDamage = true;
                    tileType = mapCellInfo.getTile2Info().getType();
                    if (tileType == null || !TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.WOOD)) {
                        ItemsController.addItem(new Items(woodId, 2), player.getInventory(), player); // если срубили дерево, добавляем бревна в инвентарь
                    }
                }
            } else if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.PICKAXE)) { // если в руках кирка
                String tileType = mapCellInfo.getTile2Info().getType();
                if (tileType != null && (TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.ORE) || TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CRYSTAL))) {
                    int oreId = (mapCellInfo.getTile2Info().getParams() != null && mapCellInfo.getTile2Info().getParams().get("ore") != null) ? // у каждой рудной жилы своя руда
                            Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("ore")) :
                            ItemsController.stoneId;

                    BattleController.applyDamageToMapCell(mapCellInfo, itemInRightHand);
                    applyDamage = true;
                    tileType = mapCellInfo.getTile2Info().getType();
                    if (tileType == null || !(TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.ORE) || TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CRYSTAL))) {
                        ItemsController.addItem(new Items(oreId, 5), player.getInventory(), player); // если добыли руду, добавляем ее в инвентарь
                    }
                }
            } else if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.SCYTHE)) { // если в руках коса
                String tileType = mapCellInfo.getTile2Info().getType();
                if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CROPS)) {
                    int i = 1;
                    List<Pair<Integer, Integer>> harvests = new ArrayList<>();
                    while (mapCellInfo.getTile2Info().getParams().get("harvestId" + i) != null) {
                        harvests.add(new Pair(Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("harvestId" + i)),
                                Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("harvestCount" + i))));
                        i++;
                    }
                    BattleController.applyDamageToMapCell(mapCellInfo, itemInRightHand);
                    applyDamage = true;
                    tileType = mapCellInfo.getTile2Info().getType();
                    if (tileType == null || !TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CROPS)) {
                        for (Pair<Integer, Integer> harvest : harvests) {
                            ItemsController.addItem(new Items(harvest.getKey(), harvest.getValue()), player.getInventory(), player); // если скосили посевы, добавляем в инвентарь
                            i++;
                        }
                    }
                }
            } else if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.SEED)) { // если в руках семена
                if ((mapCellInfo.getTile1Id() == MapController.getDugUpGroundId() ||
                        mapCellInfo.getTile1Id() == MapController.getDryGround()||
                        mapCellInfo.getTile1Id() == MapController.getWetGround()) &&
                        (mapCellInfo.getTile2Id() == 0)) { // сажать можно только во вскопанную землю
                    ItemsController.deleteItem(itemInRightHand, 1, player.getInventory(), player);
                    if (itemInRightHand.getParams() != null && itemInRightHand.getParams().get("plantId") != null) {
                        mapCellInfo.setTile2Id(Integer.parseInt(itemInRightHand.getParams().get("plantId")));
                    } else {
                        System.out.println("Для предмета с id ==" + itemInRightHand.getInfo().getId() + " не задан параметр plantId");
                    }
                }
            } else if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.SHOVEL)) { // если в руках лопата
                String tileType = mapCellInfo.getTile1Info().getType();
                if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.EARTH) && mapCellInfo.getTile2Id() == 0) {
                    mapCellInfo.setTile1Id(MapController.getDugUpGroundId());
                }
            } else if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.EXPLOSIVES)) { // если в руках взрывчатка
                MapController.addItemOnMap(mapCellInfo.getX(), mapCellInfo.getY(), new Items(itemInRightHand.getTypeId(), 1));
                Event event = new Event();
                event.setTime(Integer.parseInt(itemInRightHand.getParams().get("time")) + GameCalendar.getCurrentDate().getTic());
                event.setParams(itemInRightHand.getParams());
                event.setX(mapCellInfo.getX());
                event.setY(mapCellInfo.getY());
                event.setId("EXPLORE");
                TimeController.addEvent(event);
                ItemsController.deleteItem(itemInRightHand, 1, player.getInventory(), player);
            }

            if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.WEAPON) && !applyDamage) { // если в руках оружие
                if (mapCellInfo.getCreatureId() != null) {
                    Creature creature = Game.getMap().getCreaturesList().get(mapCellInfo.getCreatureId());
                    if (creature.isAlive()) {
                        BattleController.attackCreature(player, itemInRightHand, creature);
                    }
                } else if (mapCellInfo.getCharacterId() != null) {
                    Character character = Game.getMap().getCharacterList().get(mapCellInfo.getCharacterId());
                    if (character.isAlive()) {
                        BattleController.attackCharacter(player, itemInRightHand, character);
                    }
                } else {
                    BattleController.applyDamageToMapCell(mapCellInfo, itemInRightHand);
                }
            }

            TimeController.tic(true); // нажатие на карту занимает 1 тик
            if (showOres) {
                MapController.drawOres(itemInRightHand.getInfo().getId());
            }
            if (showEmptiness) {
                MapController.drawEmptiness();
            }
        }
    }

    /**
     * Полив
     *
     * @param itemInRightHand
     * @param mapCellInfo
     * @param tileX
     * @param tileY
     * @param character
     */
    private static void watering(Items itemInRightHand, MapCellInfo mapCellInfo, int tileX, int tileY, Character character) {
        wateringMapCell(itemInRightHand, mapCellInfo, character);
        if (itemInRightHand.getTypeId() == 111) { // Лейка с широкой насадкой поливает сразу 4 тайла
            try {
                wateringMapCell(itemInRightHand, Game.getMap().getTiles()[character.getXMapPos() + tileX - 1]
                        [character.getYMapPos() + tileY], character);
                MapController.drawTile(character, tileX - 1, tileY);
            } catch (Exception ignored) {
            }
            try {
                wateringMapCell(itemInRightHand, Game.getMap().getTiles()[character.getXMapPos() + tileX]
                        [character.getYMapPos() + tileY - 1], character);
                MapController.drawTile(character, tileX, tileY - 1);
            } catch (Exception ignored) {
            }
            try {
                wateringMapCell(itemInRightHand, Game.getMap().getTiles()[character.getXMapPos() + tileX - 1]
                        [character.getYMapPos() + tileY - 1], character);
                MapController.drawTile(character, tileX - 1, tileY - 1);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Полив конкретного тайла карты
     *
     * @param itemInRightHand - предмет, который персонаж держит в руке
     * @param mapCellInfo     - точка на карте, которую нужно полить
     * @param character          - персонаж
     */
    private static void wateringMapCell(Items itemInRightHand, MapCellInfo mapCellInfo, Character character) {
        int currentCapacity = Integer.parseInt(itemInRightHand.getParams().get("currentCapacity"));
        if (mapCellInfo.getTile1Type().equals(TileTypeEnum.WATER)) {
            itemInRightHand.getParams().put("currentCapacity", itemInRightHand.getParams().get("capacity"));
            Game.showMessage(Game.getText("WATERING_CAN_FILLED"), Color.GREEN);
        } else {
            int consumption = itemInRightHand.getTypeId() == engineeringWateringCan ? 100 : 500; // расход воды для инженерной лейки ниже
            if (currentCapacity >= consumption) {
                if (mapCellInfo.getFireId() != 0) {
                    mapCellInfo.setFireId(mapCellInfo.getFireId() - 1); // тушим огонь
                } else if (mapCellInfo.getTile1Type().equals(TileTypeEnum.EARTH)) {
                    if (mapCellInfo.getTile1Id() == MapController.getDugUpGroundId() ||
                            mapCellInfo.getTile1Id() == MapController.getDryGround()) {
                        mapCellInfo.setTile1Id(MapController.getWetGround());
                    }
                } else {
                    mapCellInfo.setPollutionId(5); // если поливаем не землю, образуется лужа
                }
                itemInRightHand.getParams().put("currentCapacity", String.valueOf(currentCapacity - consumption));
            } else {
                Game.showMessage(Game.getText("ERROR_EMPTY"));
            }
        }
        character.setCurrentWeight(ItemsController.getCurrWeight(character.getInventory())); // обновляем вес лейки
    }

    /**
     * Увеличить навык
     *
     * @param skillId     идентификатор навыка
     * @param skillPoints на сколько пунктов увеличить
     */
    public static void increaseSkill(String skillId, int skillPoints) {
        var skills = Game.getMap().getSelecterCharacter().getParams().getSkills();

        skills.get(skillId).setRealValue(skills.get(skillId).getRealValue() + skillPoints);
        if (skills.get(skillId).getRealValue() > 100) {
            skills.get(skillId).setRealValue(100);
        } else {
            skills.get(skillId).setCurrentValue(skills.get(skillId).getCurrentValue() + skillPoints);
        }

        Game.showMessage(String.format(
                Game.getText("INCREASE_SKILL_MESSAGE"),
                Game.getText(skillId + "_PARAM_NAME"),
                skills.get(skillId).getCurrentValue().toString()),
                Color.GREEN);
    }

    /**
     * Добавить опыт для увеличения навыка
     *
     * @param skillId   идентификатор навыка
     * @param expPoints сколько опыта нужно добавить
     */
    public static void addSkillExp(String skillId, int expPoints) {
        Character character = Game.getMap().getSelecterCharacter();
        for (EffectParams effect : character.getAppliedEffects()) {
            if (effect.getStrId().equals("ADD_EXP")) { // если на персонаже эффект "Увеличенный опыт", добавляем его к каждому получению опыта
                expPoints += effect.getPower();
            }
        }
        var skill = Game.getMap().getSelecterCharacter().getParams().getSkills().get(skillId);
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
        TimeController.tic(true);
        var player = Game.getMap().getSelecterCharacter();
        var mapCellInfo = Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y];

        if (isClosedCell(mapCellInfo)) {
            closableInteract(mapCellInfo);
        } else {
            if (mapCellInfo.getTile2Type().equals(TileTypeEnum.CONTAINER)
                    || mapCellInfo.getTile2Type().equals(TileTypeEnum.MANNEQUIN) ||
                    (itemsList != null && (itemsList.size() > 1 || itemsList.get(0).getCount() > 1))) {
                // показываем вторую панель инвентаря, если на тайле лежит больше 1 предмета, или если это контейнер/манекен
                String subtype = mapCellInfo.getTile2Info().getParams() != null ? mapCellInfo.getTile2Info().getParams().get("subtype") : "";
                boolean isTrashCan = subtype != null && subtype.equals("trashCan");
                Game.getGameMenu().showContainerInventory(itemsList, x, y, isTrashCan ? "trashCan" : "", null);
            } else if (itemsList != null) {
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
        var player = Game.getMap().getSelecterCharacter();
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
                case CLOCK: {
                    Game.getTimeLabel().setText(TimeController.getCurrentDataStr(true));
                    break;
                }
                case BED: {
                    SelectTimePanel.show(SelectTimePanel.TimeSkipType.SLEEP, mapCellInfo);
                    break;
                }
                case BATH: {
                    washInBath(mapCellInfo);
                    break;
                }
                case PLANT: {
                    harvest(mapCellInfo);
                    break;
                }
                case ALCHEMY_TABLE: {
                    Game.getEditor().getAlchemyPanel().showPanel(true, tileInfo.getName(), Integer.parseInt(tileInfo.getParams().get("level")));
                    break;
                }
                case ALCHEMY_LABORATORY: {
                    Game.getEditor().getAlchemyLaboratoryPanel().showPanel();
                    break;
                }
                case INGREDIENTS_COMBINER: {
                    Game.getEditor().getCombinerPanel().showPanel();
                    break;
                }
                case DUPLICATOR: {
                    Game.getEditor().getDuplicatorPanel().showPanel();
                    break;
                }
                case CRAFTING_PLACE: {
                    Game.getEditor().getCraftPanel().showPanel(tileInfo);
                    break;
                }
                case JEWELRY_TABLE: {
                    Game.getEditor().getEnchantmentPanel().showPanel(tileInfo);
                    break;
                }
                case INLAYER_DUPLICATOR: {
                    Game.getEditor().getInlayerDuplicatorPanel().showPanel();
                    break;
                }
            }
            MapController.drawTile(player, x, y);
        }

        TileInfo tile1Info = Editor.getTiles1().get(mapCellInfo.getTile1Id());
        if (tile1Info.getType() != null) {
            switch (TileTypeEnum.valueOf(tile1Info.getType())) {
                case WATER: {
                    var itemInRightHand = player.getWearingItems().get(BodyPartEnum.RIGHT_ARM.ordinal()).values().iterator().next();
                    if (itemInRightHand != null && itemInRightHand.getInfo().getTypes().contains(ItemTypeEnum.BOTTLE)) {
                        ItemsController.deleteItem(itemInRightHand, 1, player.getInventory(), player);
                        Items bottleOfWater = new Items(117, 1);
                        ItemsController.addItem(bottleOfWater, player.getInventory(), player);
                        Game.showMessage(Game.getText("BOTTLE_FILLED"), Color.GREEN);
                    }
                    break;
                }
            }
        }
        TimeController.tic(true);
    }

    /**
     * Мытье в ванне
     *
     * @param mapCellInfo - точка на карте
     */
    public static void washInBath(MapCellInfo mapCellInfo) {
        Character character = Game.getMap().getSelecterCharacter();
        ParamsInfo params = character.getParams();
        int currentCleanness = params.getIndicators().get(4).getCurrentValue(); // текущее значение чистоты
        if (currentCleanness < 70) {
            currentCleanness = 70; // без мочалки и мыла можно довести чистоту только до 70
        }

        if (currentCleanness < 85) {
            Items washcloth = ItemsController.findItemInInventory(204, character.getInventory());
            if (washcloth != null) {
                currentCleanness = 85; // с мочалкой можно довести уровень чистоты до 85
            }
        }
        if (currentCleanness < 100) {
            Items soap = ItemsController.findItemInInventory(206, character.getInventory()); // сначала ищем в инвентаре душистое мыло
            if (soap != null) {
                currentCleanness = 100;
                EffectsController.applyEffects(soap, character); // вешаем на персонажа эффект мыла
                ItemsController.deleteItem(soap, 1, character.getInventory(), character); // мыло тратися при мытье
            } else {
                soap = ItemsController.findItemInInventory(205, character.getInventory()); // ищем обычное мыло
                if (soap != null) {
                    currentCleanness = 100;
                    ItemsController.deleteItem(soap, 1, character.getInventory(), character);
                }
            }
        }
        PlayerIndicatorsPanel.setIndicatorValue(4,  currentCleanness);
        Game.showMessage(Game.getGameText("CHARACTER_WASHED"));
    }

    /**
     * Сбор урожая с тайла
     *
     * @param mapCellInfo - точка на карте
     */
    public static void harvest(MapCellInfo mapCellInfo) {
        if (mapCellInfo.getTile2Info().getParams() != null &&
                mapCellInfo.getTile2Info().getParams().get("harvestId1") != null) {
            var player = Game.getMap().getSelecterCharacter();
            int i = 1;
            List<Pair<Integer, Integer>> harvests = new ArrayList<>();
            while (mapCellInfo.getTile2Info().getParams().get("harvestId" + i) != null) {
                harvests.add(new Pair(Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("harvestId" + i)),
                        mapCellInfo.getTile2Info().getParams().get("harvestCount" + i) != null ? Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("harvestCount" + i)) : 1));
                i++;
            }

            for (Pair<Integer, Integer> harvest : harvests) {
                Items harvestItem = new Items(harvest.getKey(), harvest.getValue());
                ItemsController.addItem(
                        harvestItem,
                        player.getInventory(),
                        player);
                i++;
            }

            mapCellInfo.setTile2Id(Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("harvestedId")));
        }
    }

    /**
     * Взаимодействие с запираемым тайлом
     *
     * @param mapCellInfo - точка на карте
     */
    private static void closableInteract(MapCellInfo mapCellInfo) {
        TileInfo tileInfo = mapCellInfo.getTile2Info();
        var player = Game.getMap().getSelecterCharacter();
        var closableCellInfo = (ClosableCellInfo) mapCellInfo;
        if (closableCellInfo.isLocked()) {
            if (closableCellInfo.isCodeLock()) {
                CodeLockPanel.showPanel(closableCellInfo);
            } else {
                var doorKey = ItemsController.findItemInInventory(closableCellInfo.getKeyId(), player.getInventory());
                if (doorKey != null) {
                    closableCellInfo.setTile2Id(Integer.parseInt(tileInfo.getParams().get("anotherDoorState")));
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
                    BattleController.applyDamageToCharacter(closableCellInfo.getTrapLevel(), DamageTypeEnum.PIERCING_DAMAGE, Game.getMap().getSelecterCharacter());
                } else {
                    trapDeactivation(closableCellInfo, sapperTools);
                }
            } else {
                if (tileInfo.getParams() != null) {
                    closableCellInfo.setTile2Id(Integer.parseInt(tileInfo.getParams().get("anotherDoorState")));
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
        var hackingSkill = Game.getMap().getSelecterCharacter().getParams().getSkills().get("LOCKPICKING").getCurrentValue(); // уровень навыка
        var dexterity = Game.getMap().getSelecterCharacter().getParams().getCharacteristics().get(2).getCurrentValue(); // уровень ловкости
        var skillBonus = Integer.parseInt(hackingTools.getInfo().getParams().get("skillBonus")); // бонус к навыку от инструментов инструментов
        var lockLevel = closableCellInfo.getLockLevel(); // уровень замка
        var hackChance = hackingSkill * 1.5 + dexterity * 0.25 + skillBonus * 1.5 - lockLevel; // формула рассчета вероятности взлома замка
        if (hackChance < 0) {
            Game.showMessage(Game.getText("CANT_HACK")); // если шанс взлома меньше 0, вообще не пытаемся взломать
        } else {
            ItemsController.damageItem(hackingTools, 1, Game.getMap().getSelecterCharacter().getInventory(), Game.getMap().getSelecterCharacter());
            if (random.nextInt(100) < hackChance) {
                closableCellInfo.setLocked(false);
                Game.showMessage(
                        String.format(Game.getText("OPENED"), hackingTools.getInfo().getName()),
                        Color.GREEN);
                addSkillExp("LOCKPICKING", lockLevel +
                        (hackingSkill < lockLevel ? lockLevel - hackingSkill : 0));
            } else {
                Game.showMessage(Game.getText("HACK_FAILED"));
                addSkillExp("LOCKPICKING", 1);
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
        var hackingSkill = Game.getMap().getSelecterCharacter().getParams().getSkills().get("LOCKPICKING").getCurrentValue(); // уровень навыка
        var dexterity = Game.getMap().getSelecterCharacter().getParams().getCharacteristics().get(2).getCurrentValue(); // уровень ловкости
        var skillBonus = Integer.parseInt(sapperTools.getInfo().getParams().get("skillBonus")); // бонус инструментов
        var trapLevel = closableCellInfo.getTrapLevel(); // уровень замка
        var defuseChance = hackingSkill * 1.5 + dexterity * 0.25 + skillBonus * 1.5 - trapLevel; // формула рассчета вероятности взлома замка
        if (defuseChance < 0) {
            Game.showMessage(Game.getText("CANT_DEFUSE")); // если шанс взлома меньше 0, вообще не пытаемся взломать
        } else {
            ItemsController.damageItem(sapperTools, 1, Game.getMap().getSelecterCharacter().getInventory(), Game.getMap().getSelecterCharacter());
            if (random.nextInt(100) < defuseChance) {
                closableCellInfo.setTrap(false);
                Game.showMessage(
                        String.format(Game.getText("DEFUSED"), sapperTools.getInfo().getName()),
                        Color.GREEN);
                addSkillExp("LOCKPICKING", (trapLevel + trapLevel / 2) +
                        (hackingSkill < trapLevel ? trapLevel - hackingSkill : 0));
            } else {
                Game.showMessage(Game.getText("DEFUSE_FAILED"));
                addSkillExp("LOCKPICKING", 1);
            }
        }
    }

    /**
     * Может ли персонаж при движении сдвинуть лежащий на карте тайл 2 уровня (например, каменный шар)
     *
     * @return true, если персонаж может сдвинуть тайл
     */
    private static boolean canMoveTile2() {
        var player = Game.getMap().getSelecterCharacter();
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
                    player.getXViewPos() + xPlus * 2, player.getYViewPos() + yPlus * 2);
        }
        return canMove;
    }

    /**
     * Движение персонажа вверх
     *
     * @param character - персонаж
     */
    public static void heroMoveUp(Character character) {
        character.setDirection(DirectionEnum.UP);
        if (character.getYPosition() > 0 && tilePassability(character.getXPosition(), character.getYPosition() - 1, character)) {
            if (character.getYPosition() > 0) {
                character.setYPosition(character.getYPosition() - 1);
            }
            if (character.getYMapPos() > 0 && character.getYPosition() - 3 < character.getYMapPos()) {
                character.setYMapPos(character.getYMapPos() - 1);
            } else {
                character.setYViewPos(character.getYViewPos() - 1);
            }
            TimeController.tic(true);
        }
    }

    /**
     * Движение персонажа вниз
     *
     * @param character - персонаж
     */
    public static void heroMoveDown(Character character) {
        character.setDirection(DirectionEnum.DOWN);
        if (character.getYPosition() < (mapSize - 1) && tilePassability(character.getXPosition(), character.getYPosition() + 1, character)) {
            if (character.getYPosition() < mapSize) {
                character.setYPosition(character.getYPosition() + 1);
            }
            if (character.getYMapPos() < 285 && character.getYPosition() + 3 > character.getYMapPos() + 12) {
                character.setYMapPos(character.getYMapPos() + 1);
            } else {
                character.setYViewPos(character.getYViewPos() + 1);
            }
            TimeController.tic(true);
        }
    }

    /**
     * Движение персонажа влево
     *
     * @param character - персонаж
     */
    public static void heroMoveLeft(Character character) {
        character.setDirection(DirectionEnum.LEFT);
        if (character.getXPosition() > 0 && tilePassability(character.getXPosition() - 1, character.getYPosition(), character)) {
            if (character.getXPosition() > 0) {
                character.setXPosition(character.getXPosition() - 1);
            }
            if (character.getXMapPos() > 0 && character.getXPosition() - 3 < character.getXMapPos()) {
                character.setXMapPos(character.getXMapPos() - 1);
            } else {
                character.setXViewPos(character.getXViewPos() - 1);
            }
            TimeController.tic(true);
        }
    }

    /**
     * Движение персонажа вправо
     *
     * @param character - персонаж
     */
    public static void heroMoveRight(Character character) {
        character.setDirection(DirectionEnum.RIGHT);
        if (character.getXPosition() < (mapSize - 1) && tilePassability(character.getXPosition() + 1, character.getYPosition(), character)) {
            if (character.getXPosition() < mapSize) {
                character.setXPosition(character.getXPosition() + 1);
            }
            if (character.getXMapPos() < 285 && character.getXPosition() + 3 > character.getXMapPos() + 12) {
                character.setXMapPos(character.getXMapPos() + 1);
            } else {
                character.setXViewPos(character.getXViewPos() + 1);
            }
            TimeController.tic(true);
        }
    }

    /**
     * Проверка проходимости тайла, на который хочет переместиться персонаж
     * @param x
     * @param y
     * @param character
     * @return
     */
    private static boolean tilePassability(int x, int y, Character character) {
        return  (Game.getMap().getTiles()[x][y].getTile1Info().isPassability() ||
                (character.getAppliedEffects().stream().anyMatch(i -> i.getStrId().equals("WATER_WALK")) &&
                        Game.getMap().getTiles()[x][y].getTile1Type().equals(TileTypeEnum.WATER))) &&
                (Game.getMap().getTiles()[x][y].getTile2Info().isPassability() || canMoveTile2());
    }

    /**
     * Нарисовать персонажа игрока и все экипированные на нем предметы
     *
     * @param character - персонаж
     */
    public static void drawPlayerImage(Character character) {
        var playerImage = character.getImage().getImage();

        var isLeft = character.getDirection().equals(DirectionEnum.LEFT) ||
                character.getDirection().equals(DirectionEnum.UP);
        drawImg(character, playerImage, isLeft);
        if (character.getGender().equals(GenderEnum.MALE)) {
            if (character.getBeardLength() > 0) { // рисуем бороду персонажа
                var image = new Image("/graphics/characters/body/beard" + character.getHairColor().name() + character.getBeardLength() + ".png");
                drawImg(character, image, isLeft);
            }
        }
        if (character.getHairLength() > 0) { // рисуем волосы персонажа
            var image = new Image("/graphics/characters/body/hair" + character.getHairColor().name() + character.getHairLength() + ".png");
            drawImg(character, image, isLeft);
        }

        for (Map<BodyPartEnum, Items> bodyPart : character.getWearingItems()) {
            Items items = bodyPart.values().iterator().next();
            if (items != null) {
                var path = "/graphics/items/" + items.getTypeId() + "doll.png";
                var f = new File("/" + Character.class.getProtectionDomain().getCodeSource().getLocation().getPath() + path);
                if (f.exists()) {
                    var image = new Image(path);
                    drawImg(character, image, isLeft);
                }
            }
        }
        /*var sp = new SnapshotParameters();
        WritableImage writableImage = new WritableImage(40, 40);
        writableImage = gc.getCanvas().snapshot(sp, writableImage);*/
    }

    private static void drawImg(Character character, Image img, boolean isLeft) {
        GraphicsContext gc = Editor.getCanvas().getGraphicsContext2D();
        if (isLeft) {
            double width = img.getWidth();
            double height = img.getHeight();
            gc.drawImage(img,
                    0, 0, tileSize, tileSize,
                    character.getXViewPos() * tileSize + tileSize,
                    character.getYViewPos() * tileSize - (height - tileSize),
                    -width, height);
        } else {
            gc.drawImage(img,
                    character.getXViewPos() * tileSize, character.getYViewPos() * tileSize - (img.getHeight() - tileSize));
        }
    }

    /**
     * Перегружен ли персонаж
     *
     * @return true, если персонаж перегружен
     */
    public static Boolean isOverloaded(Character character) {
        return character.getCurrentWeight().compareTo(character.getMaxWeight()) > 0;
    }

    /**
     * Устанавливает стартовые настройки персонажа (знания и предметы в инвентаре)
     *
     * @param character - персонаж
     */
    public static void setPlayerStartParams(Character character) {
        character.getKnowledgeRecipes().add(1);
        character.getKnowledgeRecipes().add(2);
        character.getKnowledgeRecipes().add(3);

        ItemsController.addItem(new Items(1, 2), character.getInventory(), character);
        ItemsController.addItem(new Items(3, 1), character.getInventory(), character);
        ItemsController.addItem(new Items(11, 10), character.getInventory(), character);
        ItemsController.addItem(new Items(10, 1), character.getInventory(), character);
        ItemsController.addItem(new Items(113, 3), character.getInventory(), character);
        ItemsController.addItem(new Items(114, 3), character.getInventory(), character);
        ItemsController.addItem(new Items(115, 3), character.getInventory(), character);
        ItemsController.addItem(new Items(117, 3), character.getInventory(), character);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(23, 1),
                        character.getInventory(),
                        character),
                character);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(25, 1),
                        character.getInventory(),
                        character),
                character);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(26, 1),
                        character.getInventory(),
                        character),
                character);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(20, 1),
                        character.getInventory(),
                        character),
                character);

        ItemsController.equipItem(
                ItemsController.addItem(new Items(21, 1),
                        character.getInventory(),
                        character),
                character);

        var items6 = new Items(22, 1);
        ItemsController.addItem(items6, character.getInventory(), character);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items6.getTypeId(), character.getInventory()),
                character);

        var items7 = new Items(30, 1);
        items7.setCurrentStrength(0);
        ItemsController.addItem(items7, character.getInventory(), character);

        PlayerIndicatorsPanel.setClothesStyle(character.getWearingItems());

        character.setMaxVolume(ItemsController.getMaximumVolume(character));
        character.setCurrentVolume(ItemsController.getCurrVolume(character.getInventory()));
        character.setMaxWeight(ItemsController.getMaximumWeight(character));
        character.setCurrentWeight(ItemsController.getCurrWeight(character.getInventory()));

        InventoryPanel.setWeightText();
        Game.getInventory().setVolumeText();
        Game.hideMessage();
    }

    /**
     * Изменение голода и жажды персонажа с течением времени
     *
     * @param character - персонаж
     */
    public static void changeHungerAndThirst(Character character) {
        ParamsInfo params = character.getParams();
        PlayerIndicatorsPanel.setIndicatorValue(2, params.getIndicators().get(2).getCurrentValue() - 5);
        PlayerIndicatorsPanel.setIndicatorValue(3, params.getIndicators().get(3).getCurrentValue() - 5);
    }

    /**
     * Убывание чистоты персонажа с течением времени
     *
     * @param character - персонаж
     */
    public static void changeCleanness(Character character) {
        ParamsInfo params = character.getParams();
        PlayerIndicatorsPanel.setIndicatorValue(4, params.getIndicators().get(4).getCurrentValue() - 5);
    }

    /**
     * Найти все видимые персонажем точки на карте
     *
     * @param character - персонаж
     */
    public static boolean[][] findVisibleMapPoints(Character character) {
        int viewX = character.getXViewPos();
        int viewY = character.getYViewPos();
        int width = viewSize;
        int height = viewSize;
        boolean[][] result = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == viewX && j == viewY) {
                    result[i][j] = true;
                } else {
                    boolean visible = true;
                    int dx = i - viewX;
                    int dy = j - viewY;
                    int steps = Math.max(Math.abs(dx), Math.abs(dy));
                    for (int k = 1; k < steps; k++) {
                        int px = viewX + k * dx / steps;
                        int py = viewY + k * dy / steps;
                        if (!Game.getMap().getTiles()[character.getXMapPos() + px][character.getYMapPos() + py].isVisibly()) {
                            visible = false;
                            break;
                        }
                    }
                    result[i][j] = visible;
                }
            }
        }
        return result;
    }

    /**
     * Проверяет, наложен ли на персонажа эффект "слепота"
     * @param character
     * @return
     */
    public static boolean isBlind(Character character) {
        for (EffectParams effect : character.getAppliedEffects()) {
            if (effect.getStrId().equals("BLIND")) {
                return true;
            }
        }
        return false;
    }
}
