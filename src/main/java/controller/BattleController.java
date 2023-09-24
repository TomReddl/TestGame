package controller;

import javafx.scene.paint.Color;
import model.editor.TileTypeEnum;
import model.editor.items.WeaponInfo;
import model.entity.BloodTypeEnum;
import model.entity.GameModeEnum;
import model.entity.battle.DamageTypeEnum;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import model.entity.player.Player;
import view.Editor;
import view.Game;
import view.menu.MainMenu;

import java.util.List;

/**
 * Боевые действия
 */
public class BattleController {

    public static final int baseFireDamage = 20; // базовый урон от горения
    public static final int baseAcidRainDamage = 3; // базовый урон от кислотного дождя
    public static final int baseAcidPollutionDamage = 3; // базовый урон от кислотного загрязнения

    /**
     * Персонаж атакует существо
     *
     * @param player   - персонаж
     * @param weapon   - оружие, которым он атакует
     * @param creature - атакуемое существо
     */
    public static void attackCreature(Player player, Items weapon, Creature creature) {
        WeaponInfo weaponInfo = ((WeaponInfo) weapon.getInfo());
        boolean isKilled = applyDamageToCreature(weaponInfo.getDamage(), DamageTypeEnum.valueOf(weaponInfo.getDamageType()), creature);
        if (isKilled) {
            if (Game.getMap().getPlayer() == player) { // выводим сообщение, только если атаковал текущий выбранный персонаж
                Game.showMessage(creature.getInfo().getName() + " " + Game.getText("KILLED"), Color.GREEN);
            }
        }
        ItemsController.damageItem(weapon, 1, Game.getMap().getPlayer().getInventory(), Game.getMap().getPlayer());
    }

    /**
     * Наносит урон существу
     *
     * @param damagePoints - урон
     * @param damageType   - тип наносимого урона
     * @param creature     - существо
     * @return true- существо убито
     */
    public static boolean applyDamageToCreature(int damagePoints, DamageTypeEnum damageType, Creature creature) {
        creature.setHealth(creature.getHealth() - damagePoints);
        setBloodSplatter(damagePoints, damageType, creature);
        if (creature.getHealth() <= 0) {
            creature.setAlive(false);
            return true;
        }
        return false;
    }

    /**
     * Установить брызки крови при ударе по существу
     */
    private static void setBloodSplatter(int damagePoints, DamageTypeEnum damageType, Creature creature) {
        switch (damageType) {
            case CUTTING_DAMAGE: {
                if (damagePoints >= 10) {
                    setPollution(creature);
                }
                break;
            }
            case PIERCING_DAMAGE: {
                if (damagePoints >= 15) {
                    setPollution(creature);
                }
                break;
            }
            case CRUSHING_DAMAGE: {
                if (damagePoints >= 20) {
                    setPollution(creature);
                }
                break;
            }
        }
    }

    /**
     * Установить брызги на точку карты
     *
     * @param creature - существо
     */
    private static void setPollution(Creature creature) {
        BloodTypeEnum bloodType = BloodTypeEnum.valueOf(creature.getInfo().getBloodType());
        MapCellInfo mapCellInfo = Game.getMap().getTiles()[creature.getXPos()][creature.getYPos()];
        if (mapCellInfo.getPollutionId() == 0) {
            mapCellInfo.setPollutionId(bloodType.getPollution1Id());
        } else if (mapCellInfo.getPollutionId() == bloodType.getPollution1Id()) {
            mapCellInfo.setPollutionId(bloodType.getPollution2Id());
        } else if (mapCellInfo.getPollutionId() == bloodType.getPollution2Id()) {
            mapCellInfo.setPollutionId(bloodType.getPollution3Id());
        }
    }

    /**
     * Наносит урон персонажу игрока
     *
     * @param damagePoints - урон
     */
    public static void applyDamageToPlayer(int damagePoints, DamageTypeEnum damageType) {
        var player = Game.getMap().getPlayer();
        var playerHealth = player.getParams().getIndicators().get(0).getCurrentValue();
        playerHealth = playerHealth - damagePoints;
        // при нанесение урона на земле остается кровь
        if (damageType.isBloody()) {
            Game.getMap().getTiles()[player.getXPosition()][player.getYPosition()].setPollutionId(1);
            MapController.drawPlayerTile();
        }
        if (playerHealth <= 0) {
            killPlayer();
        } else {
            player.getParams().getIndicators().get(0).setCurrentValue(playerHealth);
        }
    }

    /**
     * Убивает персонажа игрока
     */
    public static void killPlayer() {
        MainMenu.getPane().setVisible(true);
        Game.setGameMode(GameModeEnum.GAME_MENU);
        Game.showMessage(Game.getText("PLAYER_DIED"));
    }

    /**
     * Наносит урон точке на карте
     *
     * @param mapCellInfo  - точка
     * @param damagePoints - урон
     */
    public static void applyDamageToMapCell(MapCellInfo mapCellInfo, int damagePoints, DamageTypeEnum damageType) {
        if (mapCellInfo.getTile2Info().getStrength() > 0) {
            mapCellInfo.setTile2Strength(mapCellInfo.getTile2Strength() - damagePoints);
            if (mapCellInfo.getTile2Strength() <= 0) {
                mapCellInfo.setTile2Strength(0);
                if (damageType.equals(DamageTypeEnum.FIRE_DAMAGE)) {
                    mapCellInfo.setTile2Id(0);
                } else {
                    String tile2Type = mapCellInfo.getTile2Info().getType();
                    if (tile2Type != null && TileTypeEnum.valueOf(tile2Type).equals(TileTypeEnum.WOOD)) {
                        mapCellInfo.setTile2Id(3); // пень
                        mapCellInfo.setTile2Strength(Editor.getTiles2().get(3).getStrength());
                    } else if (tile2Type != null && TileTypeEnum.valueOf(tile2Type).equals(TileTypeEnum.ORE)) {
                        mapCellInfo.setTile2Id(241); // скала
                        mapCellInfo.setTile2Strength(Editor.getTiles2().get(241).getStrength());
                    } else if (tile2Type != null && TileTypeEnum.valueOf(tile2Type).equals(TileTypeEnum.CROPS)) {
                        mapCellInfo.setTile2Id(Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("harvestedId"))); // для урожая берем то, что указано в параметрах
                    } else if (mapCellInfo.getTile2Id() == 241) {
                        MapController.addItemOnMap(mapCellInfo.getX(), mapCellInfo.getY(), new Items(ItemsController.stoneId, 10));
                        mapCellInfo.setTile2Id(0);
                    } else {
                        mapCellInfo.setTile2Id(150); // обломки
                        mapCellInfo.setTile2Strength(Editor.getTiles2().get(150).getStrength());
                    }
                }
            }
        } else if (mapCellInfo.getTile1Info().getStrength() > 0) {
            mapCellInfo.setTile1Strength(mapCellInfo.getTile1Strength() - damagePoints);
            if (mapCellInfo.getTile1Strength() <= 0) {
                mapCellInfo.setTile1Strength(0);
                if (damageType.equals(DamageTypeEnum.FIRE_DAMAGE)) {
                    mapCellInfo.setTile1Id(MapController.getBurntGround());
                } else {
                    mapCellInfo.setTile1Id(37); // руины
                    mapCellInfo.setTile1Strength(Editor.getTiles1().get(37).getStrength());
                }
            }
        } else if (mapCellInfo.getTile2Info().getStrength() == 0 && mapCellInfo.getTile2Info().isBurn() &&
                damageType.equals(DamageTypeEnum.FIRE_DAMAGE) && damagePoints == baseFireDamage * 4) {
            mapCellInfo.setTile2Id(0);
        } else if (mapCellInfo.getTile1Info().getStrength() == 0 && mapCellInfo.getTile1Info().isBurn() &&
                damageType.equals(DamageTypeEnum.FIRE_DAMAGE) && damagePoints == baseFireDamage * 4) {
            mapCellInfo.setTile1Id(MapController.getBurntGround());
        }

        // Предметы, лежащие не в контейнере могут сгореть или быть уничтожены взрывом
        if (((mapCellInfo.getTile2Info().getType() == null || !TileTypeEnum.valueOf(mapCellInfo.getTile2Info().getType()).equals(TileTypeEnum.CONTAINER)) &&
                (damageType.equals(DamageTypeEnum.FIRE_DAMAGE) || damageType.equals(DamageTypeEnum.EXPLOSIVE_DAMAGE))) &&
                mapCellInfo.getItems() != null) {
            List<Items> items = mapCellInfo.getItems();
            for (Items item : items) {
                if (item.getInfo().isBurn()) {
                    item.setCurrentStrength(item.getCurrentStrength() - damagePoints);
                    if (item.getCurrentStrength() <= 0) {
                        items.remove(item);
                    }
                }
            }
            if (items.size() > 0) {
                mapCellInfo.setItems(items);
            } else {
                mapCellInfo.setItems(null);
            }
        }
    }
}
