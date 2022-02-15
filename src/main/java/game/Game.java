package game;

import editor.Editor;
import entity.GameModeEnum;
import entity.map.Map;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import menu.MainMenu;
import params.GameParams;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Game {
    @Getter
    private static final Properties properties;
    private static final Properties itemsProperties;
    static {
        properties = new Properties();
        itemsProperties = new Properties();
        try {
            InputStream in = new FileInputStream("src/main/resources/text/editor_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            properties.load(in);
            in.close();

            in = new FileInputStream("src/main/resources/text/items_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            itemsProperties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Getter
    private static final Group root = new Group();
    @Getter
    private static final Editor editor = new Editor(root);
    @Getter
    @Setter
    private static Map map= new Map();
    @Getter
    private static final MainMenu mainMenu = new MainMenu(root);
    @Getter
    private static GameModeEnum gameMode = GameModeEnum.MAIN_MENU;
    @Getter
    private static final ImageView stopTestGameImage = new ImageView("/graphics/gui/StopTestGame.png");

    public static void setGameMode(GameModeEnum mode) {
        gameMode = mode;
        switch (gameMode) {
            case MAIN_MENU: {
                MainMenu.getPane().setVisible(Boolean.TRUE);
                MainMenu.getPane().setLayoutX(0);
                MainMenu.getPane().setLayoutY(0);
                mainMenu.getBackgroundImage().setVisible(Boolean.TRUE);
                break;
            }
            case EDITOR: {
                map.drawCurrentMap();
                editor.getTabPane().setVisible(Boolean.TRUE);
                editor.getButtonsPane().setVisible(Boolean.TRUE);
                stopTestGameImage.setVisible(Boolean.FALSE);
                MainMenu.getPane().setVisible(Boolean.FALSE);
                break;
            }
            case GAME: {
                map.drawCurrentMap();
                editor.getTabPane().setVisible(Boolean.FALSE);
                editor.getButtonsPane().setVisible(Boolean.FALSE);
                stopTestGameImage.setVisible(Boolean.TRUE);
                MainMenu.getPane().setVisible(Boolean.FALSE);
                break;
            }
            case GAME_MENU: {
                MainMenu.getPane().setLayoutX(mainMenu.getGameMenuPosX());
                MainMenu.getPane().setLayoutY(mainMenu.getGameMenuPosY());
                mainMenu.getBackgroundImage().setVisible(Boolean.FALSE);
                break;
            }
        }
    }

    public static String getText(String strId) {
        return properties.getProperty(strId);
    }

    public static String getItemsText(String strId) {
        return itemsProperties.getProperty(strId);
    }
}
