package controller;

import model.editor.TileTypeEnum;
import model.entity.GameModeEnum;
import model.entity.battle.DamageTypeEnum;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import view.Game;
import view.menu.MainMenu;

import java.util.List;

/**
 * Боевые действия
 */
public class BattleController {

    public static final int baseFireDamage = 20; // базовый урон от горения

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
        if (!damageType.equals(DamageTypeEnum.FIRE_DAMAGE)) {
            Game.getMap().getTiles()[player.getXPosition()][player.getYPosition()].setPollutionId(3);
            MapController.drawTile(player, player.getXViewPos(), player.getYViewPos());
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
     * @param mapCellInfo - точка
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
                    mapCellInfo.setTile2Id(150); // обломки
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
                }
            }
        } else if (mapCellInfo.getTile2Info().getStrength() == 0 && mapCellInfo.getTile2Info().isBurn() &&
                damageType.equals(DamageTypeEnum.FIRE_DAMAGE) && damagePoints == baseFireDamage * 4) {
                mapCellInfo.setTile2Id(0);
        } else if (mapCellInfo.getTile1Info().getStrength() == 0 && mapCellInfo.getTile1Info().isBurn() &&
                damageType.equals(DamageTypeEnum.FIRE_DAMAGE) && damagePoints == baseFireDamage * 4) {
            mapCellInfo.setTile1Id(MapController.getBurntGround());
        }

        // Предметы, лежащие не в контейнере могут сгореть
        if ((mapCellInfo.getTile2Info().getType() == null || !TileTypeEnum.valueOf(mapCellInfo.getTile2Info().getType()).equals(TileTypeEnum.CONTAINER)) &&
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
