package game;

import editor.Editor;
import entity.GameModeEnum;
import entity.map.Map;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import menu.MainMenu;

public class Game {
    @Getter
    @Setter
    private static final Group root = new Group();
    @Getter
    @Setter
    private static final Editor editor = new Editor(root);
    @Getter
    @Setter
    private static final Map map= new Map();
    @Getter
    @Setter
    private static final MainMenu mainMenu = new MainMenu(root);
    @Getter
    private static GameModeEnum gameMode = GameModeEnum.MAIN_MENU;
    @Getter
    @Setter
    private static final ImageView stopTestGameImage = new ImageView("/graphics/gui/StopTestGame.png");

    public void setGameMode(GameModeEnum mode) {
        gameMode = mode;
        var player = map.getPlayer();
        switch (gameMode) {
            case MAIN_MENU: {
                mainMenu.getMenuPane().setVisible(Boolean.TRUE);
                mainMenu.getMenuPane().setLayoutX(0);
                mainMenu.getMenuPane().setLayoutY(0);
                mainMenu.getBackgroundImage().setVisible(Boolean.TRUE);
                break;
            }
            case EDITOR: {
                map.drawCurrentMap();
                editor.getTabPane().setVisible(Boolean.TRUE);
                editor.getButtonsPane().setVisible(Boolean.TRUE);
                stopTestGameImage.setVisible(Boolean.FALSE);
                mainMenu.getMenuPane().setVisible(Boolean.FALSE);
                break;
            }
            case GAME: {
                map.drawCurrentMap();
                editor.getTabPane().setVisible(Boolean.FALSE);
                editor.getButtonsPane().setVisible(Boolean.FALSE);
                stopTestGameImage.setVisible(Boolean.TRUE);
                mainMenu.getMenuPane().setVisible(Boolean.FALSE);
                break;
            }
            case GAME_MENU: {
                mainMenu.getMenuPane().setLayoutX(mainMenu.getGameMenuPosX());
                mainMenu.getMenuPane().setLayoutY(mainMenu.getGameMenuPosY());
                mainMenu.getBackgroundImage().setVisible(Boolean.FALSE);
                break;
            }
        }
    }
}
