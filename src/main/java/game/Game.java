package game;

import editor.Editor;
import editor.ItemInfo;
import editor.TileInfo;
import entity.GameModeEnum;
import entity.ItemTypeEnum;
import entity.map.Map;
import javafx.scene.Group;
import javafx.scene.control.Tab;
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
    static {
        loadTranslations();
    }

    @Getter
    private static Properties properties;
    private static Properties itemsProperties;
    private static Properties tiles1Properties;
    private static Properties tiles2Properties;

    @Getter
    private static final Group root = new Group();
    @Getter
    private static final Editor editor = new Editor(root);
    @Getter
    @Setter
    private static Map map = new Map();
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

    public static void loadTranslations() {
        properties = new Properties();
        itemsProperties = new Properties();
        tiles1Properties = new Properties();
        tiles2Properties = new Properties();
        try {
            InputStream in = new FileInputStream("src/main/resources/text/editor_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            properties.load(in);
            in.close();

            in = new FileInputStream("src/main/resources/text/items_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            itemsProperties.load(in);
            in.close();

            in = new FileInputStream("src/main/resources/text/tiles1_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            tiles1Properties.load(in);
            in.close();

            in = new FileInputStream("src/main/resources/text/tiles2_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            tiles2Properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fillTranslations() {
        editor.getTabPane().getTabs().get(0).setText(Game.getText("TILES"));
        editor.getTabPane().getTabs().get(1).setText(Game.getText("OBJECTS"));
        editor.getTabPane().getTabs().get(2).setText(Game.getText("CHARACTERS"));
        editor.getTabPane().getTabs().get(3).setText(Game.getText("CREATURES"));
        editor.getTabPane().getTabs().get(4).setText(Game.getText("ITEMS"));
        editor.getTabPane().getTabs().get(5).setText(Game.getText("POLLUTIONS"));
        editor.getTabPane().getTabs().get(6).setText(Game.getText("ZONES"));
        editor.getSearchItemTF().setPromptText(Game.getText("ITEM_NAME"));
        editor.getShowZonesLabel().setText(Game.getText("SHOW_ZONES"));

        int i = 0;
        for (ItemTypeEnum itemType : ItemTypeEnum.values()) {
            itemType.setDesc(Game.getText(itemType.name()));
            editor.getItemsTabPane().getTabs().get(i++).setText(itemType.getDesc());
        }

        for (TileInfo tileInfo : editor.getTiles1()) {
            tileInfo.setName(Game.getTiles1Text(tileInfo.getId() + "NAME"));
            tileInfo.setDesc(Game.getTiles1Text(tileInfo.getId() + "DESC"));
        }
        for (TileInfo tileInfo : editor.getTiles2()) {
            tileInfo.setName(Game.getTiles2Text(tileInfo.getId() + "NAME"));
            tileInfo.setDesc(Game.getTiles2Text(tileInfo.getId() + "DESC"));
        }
        for (ItemInfo itemInfo : editor.getItemsList()) {
            itemInfo.setName(Game.getItemsText(itemInfo.getId() + "NAME"));
            itemInfo.setDesc(Game.getItemsText(itemInfo.getId() + "DESC"));
            var itemName = itemInfo.getName();
            itemName = itemName.length() > 8 ? itemName.substring(0, 8) : itemName;
            itemInfo.getNameLabel().setText(itemName);
        }

        mainMenu.getSettingsPanel().getLangLabel().setText(Game.getText("LANG"));
        mainMenu.getSettingsPanel().getSaveButton().setText(Game.getText("SAVE"));
        mainMenu.getSettingsPanel().getBackButton().setText(Game.getText("BACK"));

        mainMenu.getContinueButton().setText(Game.getText("CONTINUE"));
        mainMenu.getNewGameButton().setText(Game.getText("NEW_GAME"));
        mainMenu.getLoadGameButton().setText(Game.getText("LOAD_GAME"));
        mainMenu.getEditorButton().setText(Game.getText("EDITOR"));
        mainMenu.getSettingsButton().setText(Game.getText("SETTINGS"));
        mainMenu.getExitButton().setText(Game.getText("EXIT"));
    }

    public static String getText(String strId) {
        return properties.getProperty(strId);
    }

    public static String getItemsText(String strId) {
        return itemsProperties.getProperty(strId);
    }

    public static String getTiles1Text(String strId) {
        return tiles1Properties.getProperty(strId);
    }

    public static String getTiles2Text(String strId) {
        return tiles2Properties.getProperty(strId);
    }
}
