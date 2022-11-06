package controller;

import model.entity.GameModeEnum;
import view.Game;
import view.menu.MainMenu;

/**
 * Боевые действия
 */
public class BattleController {

    /**
     * Наносит урон персонажу игрока
     *
     * @param damagePoints - урон
     */
    public static void applyDamageToPlayer(int damagePoints) {
        var player = Game.getMap().getPlayer();
        var playerHealth = player.getParams().getIndicators().get(0).getCurrentValue();
        playerHealth = playerHealth - damagePoints;
        // при нанесение урона на земле остается кровь
        Game.getMap().getTiles()[player.getXPosition()][player.getYPosition()].setPollutionId(3);
        MapController.drawTile(player, player.getXViewPos(), player.getYViewPos());
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
}
