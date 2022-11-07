package view;

import controller.MapController;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.editor.items.ItemInfo;
import model.editor.TileInfo;
import model.entity.GameModeEnum;
import model.entity.ItemTypeEnum;
import model.entity.map.Map;
import view.inventory.InventoryPanel;
import view.inventory.PlayerIndicatorsPanel;
import view.menu.GameMenuPanel;
import view.params.ParamPanel;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import view.menu.MainMenu;
import game.GameParams;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Класс, в котором хранится основная игровая информация
 */
public class Game {
    @Getter
    private static final Properties properties = new Properties();
    private static final Properties itemsProperties = new Properties();
    private static final Properties tiles1Properties = new Properties();
    private static final Properties tiles2Properties = new Properties();
    private static final Properties NPCProperties = new Properties();
    private static final Properties creaturesProperties = new Properties();
    private static final Properties effectsProperties = new Properties();

    @Getter
    private static final List<Label> messageLabels = new ArrayList<>();
    @Getter
    private static final Group root = new Group();
    @Getter
    @Setter
    public static Stage stage;

    static {
        for (int i = 0; i<4; i++) {
            Label label = new Label("");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            label.setLayoutX(50);
            label.setLayoutY(610);
            label.setVisible(false);
            label.setTextFill(Color.RED);

            messageLabels.add(label);
            root.getChildren().add(label);
        }

        loadTranslations();
    }

    @Getter
    private static final ImageView stopTestGameImage = new ImageView("/graphics/gui/StopTestGame.png");
    @Getter
    @Setter
    private static Map map = new Map();
    @Getter
    private static final Editor editor = new Editor();
    @Getter
    private static final MainMenu mainMenu = new MainMenu();
    @Getter
    private static final InventoryPanel inventory = new InventoryPanel(210, 5, InventoryPanel.InventoryTypeEnum.PLAYER);
    @Getter
    private static final InventoryPanel containerInventory = new InventoryPanel(800, 35, InventoryPanel.InventoryTypeEnum.CONTAINER);
    @Getter
    private static final ParamPanel params = new ParamPanel(root);
    @Getter
    private static final GameMenuPanel gameMenu = new GameMenuPanel(root);
    @Getter
    private static GameModeEnum gameMode = GameModeEnum.MAIN_MENU;
    @Getter
    @Setter
    private static int xMapInfoPos; // координата текущей точки карты, инофрмация о которой отображается в информационном лэйбле
    @Getter
    @Setter
    private static int yMapInfoPos;
    @Getter
    @Setter
    private static boolean isDrawingMap;
    @Getter
    @Setter
    private static boolean isShiftPressed; // нажата клавиша shift

    public static void setGameMode(GameModeEnum mode) {
        gameMode = mode;
        switch (gameMode) {
            case MAIN_MENU: {
                MainMenu.getPane().setVisible(true);
                MainMenu.getPane().setLayoutX(0);
                MainMenu.getPane().setLayoutY(0);
                MainMenu.getBackgroundImage().setVisible(true);
                editor.getMapInfoLabel().setVisible(false);
                break;
            }
            case EDITOR: {
                MapController.drawCurrentMap();
                editor.getTabPane().setVisible(true);
                editor.getButtonsPane().setVisible(true);
                stopTestGameImage.setVisible(false);
                MainMenu.getPane().setVisible(false);
                editor.getMapInfoLabel().setVisible(true);
                PlayerIndicatorsPanel.showPanel(false);
                GameMenuPanel.getPane().setVisible(false);
                break;
            }
            case GAME: {
                MapController.drawCurrentMap();
                editor.getTabPane().setVisible(false);
                editor.getButtonsPane().setVisible(false);
                stopTestGameImage.setVisible(true);
                MainMenu.getPane().setVisible(false);
                editor.getMapInfoLabel().setVisible(true);
                break;
            }
            case GAME_MENU: {
                MainMenu.getPane().setLayoutX(MainMenu.getGameMenuPosX());
                MainMenu.getPane().setLayoutY(MainMenu.getGameMenuPosY());
                MainMenu.getBackgroundImage().setVisible(false);
                break;
            }
        }
    }

    public static void loadTranslations() {
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

            in = new FileInputStream("src/main/resources/text/NPC_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            NPCProperties.load(in);
            in.close();

            in = new FileInputStream("src/main/resources/text/creatures_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            creaturesProperties.load(in);
            in.close();

            in = new FileInputStream("src/main/resources/text/effects_" +
                    GameParams.lang.toString().toLowerCase() + ".properties");
            effectsProperties.load(in);
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
        for (ItemTypeEnum itemType : ItemTypeEnum.getItemTypesForFilter()) {
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
        for (ItemInfo itemInfo : editor.getItems()) {
            itemInfo.setName(Game.getItemsText(itemInfo.getId() + "NAME"));
            itemInfo.setDesc(Game.getItemsText(itemInfo.getId() + "DESC"));
        }

        MainMenu.getSettingsPanel().getLangLabel().setText(Game.getText("LANG"));
        MainMenu.getSettingsPanel().getSaveButton().setText(Game.getText("SAVE"));
        MainMenu.getSettingsPanel().getBackButton().setText(Game.getText("BACK"));

        MainMenu.getContinueButton().setText(Game.getText("CONTINUE"));
        MainMenu.getNewGameButton().setText(Game.getText("NEW_GAME"));
        MainMenu.getLoadGameButton().setText(Game.getText("LOAD_GAME"));
        MainMenu.getEditorButton().setText(Game.getText("EDITOR"));
        MainMenu.getSettingsButton().setText(Game.getText("SETTINGS"));
        MainMenu.getExitButton().setText(Game.getText("EXIT"));
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

    public static String getCreaturesText(String strId) {
        return creaturesProperties.getProperty(strId);
    }

    public static String getNPCText(String strId) {
        return NPCProperties.getProperty(strId);
    }

    public static String getEffectText(String strId) {
        return effectsProperties.getProperty(strId);
    }

    // отобразить сообщение игроку
    public static void showMessage(String message, Color... color) {
        Label messageLabel = null;
        int i = -1;
        for (Label label : messageLabels) {
            i++;
            if (!label.isVisible()) {
                messageLabel = label;

                break;
            }
        }
        if (color.length > 0) {
            messageLabel.setTextFill(color[0]);
        } else {
            messageLabel.setTextFill(Color.RED);
        }
        messageLabel.setLayoutY(610 + i*20);
        messageLabel.setVisible(true);
        messageLabel.setText(message);
    }

    public static void hideMessage() {
        for (Label label : messageLabels) {
            label.setVisible(false);
        }
    }
}
