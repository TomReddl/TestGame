package view;

import controller.CharactersController;
import controller.MapController;
import controller.utils.JsonUtils;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import model.editor.ConstructionInfo;
import model.editor.*;
import model.editor.items.ItemInfo;
import model.editor.items.RecipeInfo;
import model.entity.GameModeEnum;
import model.entity.ItemTypeEnum;
import model.entity.map.WeatherEnum;
import model.entity.player.Character;
import view.dialog.DialogPanel;
import view.dialog.GameDialogPanel;
import view.player.SquadPanel;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import static game.GameParams.*;

/**
 * Игровой редактор
 */
public class Editor {
    @Getter
    private static final TabPane tabPane = new TabPane();
    @Getter
    private static final Pane buttonsPane = new Pane();
    private static ImageView border;
    private static final Pane pane1 = new Pane();
    private static final Pane pane2 = new Pane();
    private static final Pane pane3 = new Pane();
    private static final Pane pane4 = new Pane();
    private static final Pane pane5 = new Pane();
    private static final Pane pane6 = new Pane();
    private static final Pane pane7 = new Pane();
    private static final Pane pane8 = new Pane();
    private static final Pane pane9 = new Pane();
    private static final Pane pane10 = new Pane();
    @Getter
    private static final Pane itemsPane = new Pane();
    private static final TextField searchTile1TF = new TextField();
    private static final TextField searchTile2TF = new TextField();
    private static final TextField searchNPCTF = new TextField();
    private static final TextField searchCreatureTF = new TextField();
    @Getter
    private static final TextField searchItemTF = new TextField();
    @Getter
    private static final TabPane itemsTabPane = new TabPane();
    @Getter
    private static final Label showZonesLabel = new Label(Game.getText("SHOW_ZONES"));
    @Getter
    private static final Label showRoofsLabel = new Label(Game.getText("SHOW_ROOFS"));
    @Getter
    @Setter
    private static boolean showRoofs = false;
    @Setter
    @Getter
    private static CheckBox showRoofsCheckBox = new CheckBox();
    @Getter
    private static int selectTile = 0;
    @Getter
    @Setter
    private static boolean showZones = false;
    @Setter
    @Getter
    private static CheckBox showZonesCheckBox = new CheckBox();
    @Getter
    private static EditorObjectType selectedType = EditorObjectType.GROUND;
    @Setter
    @Getter
    private static List<TileInfo> tiles1 = JsonUtils.getTiles1();
    @Setter
    @Getter
    private static List<TileInfo> tiles2 = JsonUtils.getTiles2();
    @Setter
    @Getter
    private static List<CharacterInfo> characters = JsonUtils.getCharacters();
    @Setter
    @Getter
    private static List<CreatureInfo> creatures = JsonUtils.getCreatures();
    @Setter
    @Getter
    private static List<ItemInfo> items = JsonUtils.getItems();
    @Setter
    @Getter
    private static List<PollutionInfo> pollutions = JsonUtils.getPollutions();
    @Setter
    @Getter
    private static List<ZoneInfo> zones = JsonUtils.getZones();
    @Setter
    @Getter
    private static List<RoofInfo> roofs = JsonUtils.getRoofs();
    @Setter
    @Getter
    private static List<ConstructionInfo> constructions = JsonUtils.getConstructions();
    @Setter
    @Getter
    private static List<RecipeInfo> recipes = JsonUtils.getRecipes();
    @Setter
    @Getter
    private static Canvas canvas = new Canvas(screenSizeX, screenSizeY); // размеры игрового окна
    @Setter
    @Getter
    private static Label mapInfoLabel = new Label("");
    @Getter
    private static final List<Image> fires = new ArrayList<>();

    private final ComboBox<WeatherEnum> weatherCB = new ComboBox();

    private final Map<String, Pair<CheckBox, Slider>> weatherSliders = new HashMap<>(); // мапа <id погоды, пара: чекбокс и слайдер для этой погоды>
    private static final ImageView selectConstructionImage = new ImageView("/graphics/gui/SelectConstruction.png"); // Выбрать конструкцию для сохранения
    @Getter
    private static final TextField firstConstructionPointTX = new TextField();
    private static final Label firstConstructionPointLabel = new Label(Game.getText("FIRST_POINT"));
    @Getter
    private static final TextField secondConstructionPointTX = new TextField();
    private static final Label secondConstructionPointLabel = new Label(Game.getText("SECOND_POINT"));
    @Getter
    private static final TextField nameConstructionTX = new TextField();
    private static final Label nameConstructionPointLabel = new Label(Game.getText("NAME_POINT"));
    private static final ImageView saveConstructionImage = new ImageView("/graphics/gui/SaveMap.png");

    @Getter
    private final AlchemyPanel alchemyPanel;
    @Getter
    private final AlchemyLaboratoryPanel alchemyLaboratoryPanel;
    @Getter
    private final CombinerPanel combinerPanel;
    @Getter
    private final DuplicatorPanel duplicatorPanel;
    @Getter
    private final CraftPanel craftPanel;
    @Getter
    private final EnchantmentPanel enchantmentPanel;
    @Getter
    private final InlayerDuplicatorPanel inlayerDuplicatorPanel;
    @Getter
    private final DialogPanel dialogPanel;
    @Getter
    private final GameDialogPanel gameDialogPanel;
    @Getter
    private final TimeControlPanel timeControlPanel;
    @Getter
    private final SquadPanel squadPanel;

    public Editor() {
        Game.getRoot().getChildren().add(canvas);
        drawTiles();
        drawEditorButtons();
        alchemyPanel = new AlchemyPanel();
        alchemyLaboratoryPanel = new AlchemyLaboratoryPanel();
        combinerPanel = new CombinerPanel();
        duplicatorPanel = new DuplicatorPanel();
        craftPanel = new CraftPanel();
        enchantmentPanel = new EnchantmentPanel();
        inlayerDuplicatorPanel = new InlayerDuplicatorPanel();
        dialogPanel = new DialogPanel();
        gameDialogPanel = new GameDialogPanel();
        timeControlPanel = new TimeControlPanel();
        squadPanel = new SquadPanel();
    }

    private void drawEditorButtons() {
        Label mapNameLabel = new Label(Game.getText("MAP_NAME"));
        mapNameLabel.setLayoutX(5);
        mapNameLabel.setLayoutY(10);
        buttonsPane.getChildren().add(mapNameLabel);

        TextField mapNameTextField = new TextField();
        mapNameTextField.setLayoutX(105);
        mapNameTextField.setLayoutY(5);
        mapNameTextField.setMaxWidth(100);
        mapNameTextField.setText(Game.getMap().getMapName());
        mapNameTextField.setFocusTraversable(false);
        Pattern pattern = Pattern.compile("\\d{1,3}.\\d{1,3}");
        TextFormatter<?> formatter = new TextFormatter<>(change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        });
        mapNameTextField.setTextFormatter(formatter);
        mapNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d|\\.*")) {
                mapNameTextField.setText(newValue.replaceAll("[^\\d|.]", ""));
            }
        });
        buttonsPane.getChildren().add(mapNameTextField);

        ImageView generateMapImage = new ImageView("/graphics/gui/GenerateMap.png");
        generateMapImage.setLayoutX(225);
        generateMapImage.setLayoutY(5);
        generateMapImage.setOnMousePressed(event -> MapController.generateMap());
        generateMapImage.setOnMouseEntered(event -> showHint(Game.getText("GENERATE_MAP")));
        generateMapImage.setOnMouseExited(event -> hideHint());
        buttonsPane.getChildren().add(generateMapImage);

        ImageView saveMapImage = new ImageView("/graphics/gui/SaveMap.png");
        saveMapImage.setLayoutX(260);
        saveMapImage.setLayoutY(5);
        saveMapImage.setOnMousePressed(event -> JsonUtils.saveMap(mapNameTextField.getText(), Game.getMap()));
        saveMapImage.setOnMouseEntered(event -> showHint(Game.getText("SAVE_MAP")));
        saveMapImage.setOnMouseExited(event -> hideHint());
        buttonsPane.getChildren().add(saveMapImage);

        ImageView loadMapImage = new ImageView("/graphics/gui/LoadMap.png");
        loadMapImage.setLayoutX(295);
        loadMapImage.setLayoutY(5);
        loadMapImage.setOnMousePressed(event -> {
            Game.setMap(JsonUtils.loadMap(mapNameTextField.getText()));
            MapController.drawCurrentMap();
            CharactersController.setPlayerStartParams(Game.getMap().getPlayersSquad().getSelectedCharacter());
        });
        loadMapImage.setOnMouseEntered(event -> showHint(Game.getText("LOAD_MAP")));
        loadMapImage.setOnMouseExited(event -> hideHint());
        buttonsPane.getChildren().add(loadMapImage);

        ImageView startTestGameImage = new ImageView("/graphics/gui/StartTestGame.png");
        startTestGameImage.setLayoutX(330);
        startTestGameImage.setLayoutY(5);
        startTestGameImage.setOnMousePressed(event -> Game.setGameMode(GameModeEnum.GAME));
        startTestGameImage.setOnMouseEntered(event -> showHint(Game.getText("START_TEST_GAME")));
        startTestGameImage.setOnMouseExited(event -> hideHint());
        buttonsPane.getChildren().add(startTestGameImage);

        Game.getStopTestGameImage().setLayoutX(5);
        Game.getStopTestGameImage().setLayoutY(610);
        Game.getStopTestGameImage().setOnMousePressed(event -> Game.setGameMode(GameModeEnum.EDITOR));
        Game.getStopTestGameImage().setVisible(false);
        Game.getStopTestGameImage().setOnMouseEntered(event -> showHint(Game.getText("STOP_TEST_GAME")));
        Game.getStopTestGameImage().setOnMouseExited(event -> hideHint());
        Game.getRoot().getChildren().add(Game.getStopTestGameImage());
    }

    public static void showHint(String hint) {
        mapInfoLabel.setText(hint);
    }

    public static void hideHint() {
        mapInfoLabel.setText("");
    }

    private void drawTiles() {
        Group root = Game.getRoot();
        buttonsPane.setLayoutX(5);
        buttonsPane.setLayoutY(610);
        root.getChildren().add(buttonsPane);

        mapInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        mapInfoLabel.setLayoutX(380);
        mapInfoLabel.setLayoutY(620);
        mapInfoLabel.setVisible(false);
        root.getChildren().add(mapInfoLabel);

        tabPane.setLayoutX(630);
        tabPane.setPrefSize(460, 620);
        tabPane.getTabs().add(new Tab(Game.getText("TILES")));
        tabPane.getTabs().add(new Tab(Game.getText("OBJECTS")));
        tabPane.getTabs().add(new Tab(Game.getText("CHARACTERS")));
        tabPane.getTabs().add(new Tab(Game.getText("CREATURES")));
        tabPane.getTabs().add(new Tab(Game.getText("ITEMS")));
        tabPane.getTabs().add(new Tab(Game.getText("POLLUTIONS")));
        tabPane.getTabs().add(new Tab(Game.getText("ZONES")));
        tabPane.getTabs().add(new Tab(Game.getText("WEATHER")));
        tabPane.getTabs().add(new Tab(Game.getText("ROOFS")));
        tabPane.getTabs().add(new Tab(Game.getText("CONSTRUCTIONS")));
        tabPane.getTabs().get(0).setClosable(false);
        tabPane.getTabs().get(1).setClosable(false);
        tabPane.getTabs().get(2).setClosable(false);
        tabPane.getTabs().get(3).setClosable(false);
        tabPane.getTabs().get(4).setClosable(false);
        tabPane.getTabs().get(5).setClosable(false);
        tabPane.getTabs().get(6).setClosable(false);
        tabPane.getTabs().get(7).setClosable(false);
        tabPane.getTabs().get(8).setClosable(false);
        tabPane.getTabs().get(9).setClosable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(5);
        scrollPane.setPrefSize(180, 600);

        searchTile1TF.setLayoutX(5);
        searchTile1TF.setLayoutY(5);
        searchTile1TF.setPromptText(Game.getText("TILE_NAME"));
        searchTile1TF.setOnAction(event -> filterTiles(searchTile1TF.getText(), tiles1));
        pane1.getChildren().add(searchTile1TF);

        fires.add(new Image("/graphics/tiles2/0.png"));
        for (int i = 1; i < 4; i++) {
            fires.add(new Image("/graphics/fire/fire" + i + ".png"));
        }

        for (int i = 0; i < tiles1.size(); i++) {
            ImageView tile = new ImageView("/graphics/tiles/" + i + ".png");
            tile.setX(5 + (i / 12) * (tileSize + 5));
            tile.setY(35 + (i) * (tileSize + 5) - (i / 12) * 540);
            tile.setId(String.valueOf(i));
            int finalI = i;
            tile.setOnMouseEntered(event -> Popover.showPopover(finalI + " " + Game.getTiles1Text(finalI + "NAME"), tile.getX(), tile.getY() + tileSize));
            tile.setOnMouseExited(event -> Popover.hidePopover());
            tile.setOnMouseClicked(event -> {
                setBorder(pane1);
                selectTile = tiles1.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.GROUND;
                border.setX(tiles1.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tiles1.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tiles1.get(i).setImage(tile);
            tiles1.get(i).setName(Game.getTiles1Text(i + "NAME"));
            tiles1.get(i).setDesc(Game.getTiles1Text(i + "DESC"));
            pane1.getChildren().add(tile);
        }
        border = new ImageView("/graphics/gui/Border.png");
        border.setX(tiles1.get(0).getImage().getX() - 1);
        border.setY(tiles1.get(0).getImage().getY() - 1);
        pane1.getChildren().add(border);
        pane1.setOnMouseEntered(event -> Popover.setPane(pane1));
        scrollPane.setContent(pane1);
        tabPane.getTabs().get(0).setContent(scrollPane);

        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setLayoutX(190);
        scrollPane2.setPrefSize(180, 600);

        searchTile2TF.setLayoutX(5);
        searchTile2TF.setLayoutY(5);
        searchTile2TF.setPromptText(Game.getText("TILE_NAME"));
        searchTile2TF.setOnAction(event -> filterTiles(searchTile2TF.getText(), tiles2));
        pane2.getChildren().add(searchTile2TF);

        for (int i = 0; i < tiles2.size(); i++) {
            ImageView tile = new ImageView("/graphics/tiles2/" + i + ".png");
            tile.setFitWidth(tileSize);
            tile.setFitHeight(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 12) * (tileSize + 5));
            tile.setY(35 + (i) * (tileSize + 5) - (i / 12) * 540);
            tile.setId(String.valueOf(i));
            int finalI = i;
            tile.setOnMouseEntered(event -> Popover.showPopover(finalI + " " + Game.getTiles2Text(finalI + "NAME"), tile.getX(), tile.getY() + tileSize));
            tile.setOnMouseExited(event -> Popover.hidePopover());
            tile.setOnMouseClicked(event -> {
                setBorder(pane2);
                selectTile = tiles2.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.OBJECT;
                border.setX(tiles2.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tiles2.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tiles2.get(i).setImage(tile);
            tiles2.get(i).setName(Game.getTiles2Text(i + "NAME"));
            tiles2.get(i).setDesc(Game.getTiles2Text(i + "DESC"));

            if (tiles2.get(i).isTwoLayer()) {
                tiles2.get(i).setUpLayerImage(tile);
            }
            pane2.getChildren().add(tile);
        }
        pane2.setOnMouseEntered(event -> Popover.setPane(pane2));
        scrollPane2.setContent(pane2);
        tabPane.getTabs().get(1).setContent(scrollPane2);

        ScrollPane scrollPane3 = new ScrollPane();
        scrollPane3.setLayoutX(190);
        scrollPane3.setPrefSize(180, 600);

        searchNPCTF.setLayoutX(5);
        searchNPCTF.setLayoutY(5);
        searchNPCTF.setPromptText(Game.getText("NPC_NAME"));
        searchNPCTF.setOnAction(event -> filterCharacters(searchNPCTF.getText()));
        pane3.getChildren().add(searchNPCTF);

        for (int i = 0; i < characters.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/characters/" + i + ".png");
            }
            tile.setX(5 + (i / 12) * (tileSize + 5));
            tile.setY(35 + (i) * (tileSize + 5) - (i / 12) * 540);
            tile.setId(String.valueOf(i));
            int finalI = i;
            tile.setOnMouseEntered(event -> Popover.showPopover(finalI + " " + Game.getNPCText(finalI + "NAME"), tile.getX(), tile.getY() + tileSize));
            tile.setOnMouseExited(event -> Popover.hidePopover());
            tile.setOnMouseClicked(event -> {
                setBorder(pane3);
                selectTile = characters.get(Integer.parseInt(tile.getId())).getImageId();
                selectedType = EditorObjectType.CHARACTER;
                border.setX(characters.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(characters.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            characters.get(i).setImage(tile);
            characters.get(i).setName(Game.getNPCText(i + "NAME"));
            characters.get(i).setDesc(Game.getNPCText(i + "DESC"));
            pane3.getChildren().add(tile);
        }
        pane3.setOnMouseEntered(event -> Popover.setPane(pane3));
        scrollPane3.setContent(pane3);
        tabPane.getTabs().get(2).setContent(scrollPane3);

        ScrollPane scrollPane4 = new ScrollPane();
        scrollPane4.setLayoutX(190);
        scrollPane4.setPrefSize(180, 600);

        searchCreatureTF.setLayoutX(5);
        searchCreatureTF.setLayoutY(5);
        searchCreatureTF.setPromptText(Game.getText("CREATURE_NAME"));
        searchCreatureTF.setOnAction(event -> filterCreatures(searchCreatureTF.getText()));
        pane4.getChildren().add(searchCreatureTF);

        for (int i = 0; i < creatures.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/creatures/" + i + ".png");
            }
            tile.setFitWidth(tileSize);
            tile.setFitHeight(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 12) * (tileSize + 5));
            tile.setY(35 + (i) * (tileSize + 5) - (i / 12) * 540);
            tile.setId(String.valueOf(i));
            int finalI = i;
            tile.setOnMouseEntered(event -> Popover.showPopover(finalI + " " + Game.getCreaturesText(finalI + "NAME"), tile.getX(), tile.getY() + tileSize));
            tile.setOnMouseExited(event -> Popover.hidePopover());
            tile.setOnMouseClicked(event -> {
                setBorder(pane4);
                selectTile = creatures.get(Integer.parseInt(tile.getId())).getImageId();
                selectedType = EditorObjectType.CREATURE;
                border.setX(creatures.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(creatures.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            creatures.get(i).setImage(tile);

            creatures.get(i).setName(Game.getCreaturesText(i + "NAME"));
            creatures.get(i).setDesc(Game.getCreaturesText(i + "DESC"));

            pane4.getChildren().add(tile);
        }
        pane4.setOnMouseEntered(event -> Popover.setPane(pane4));
        scrollPane4.setContent(pane4);
        tabPane.getTabs().get(3).setContent(scrollPane4);

        ScrollPane scrollPane5 = new ScrollPane();
        scrollPane5.setLayoutX(190);
        scrollPane5.setPrefSize(180, 600);

        searchItemTF.setLayoutX(5);
        searchItemTF.setLayoutY(5);
        searchItemTF.setPromptText(Game.getText("ITEM_NAME"));
        searchItemTF.setOnAction(event -> filterItems(
                itemsTabPane.getSelectionModel().getSelectedItem().getText(),
                searchItemTF.getText()));
        pane5.getChildren().add(searchItemTF);


        itemsTabPane.setLayoutX(5);
        itemsTabPane.setLayoutY(35);
        itemsTabPane.setPrefSize(430, 530);
        for (ItemTypeEnum itemType : ItemTypeEnum.getItemTypesForFilter()) {
            Tab tab = new Tab(itemType.getDesc());
            tab.setClosable(false);
            itemsTabPane.getTabs().add(tab);
        }
        itemsTabPane.getTabs().get(0).setContent(scrollPane5);
        itemsTabPane.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, tab, t1) -> {
                    tab.setContent(null);
                    t1.setContent(scrollPane5);
                    filterItems(t1.getText(), searchItemTF.getText());
                }
        );
        pane5.getChildren().add(itemsTabPane);

        for (int i = 0; i < items.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                var path = "/graphics/items/icons/" + i + ".png";
                var f = new File("/" + Character.class.getProtectionDomain().getCodeSource().getLocation().getPath() + path);
                if (f.exists()) {
                    tile = new ImageView(path);
                } else {
                    tile = new ImageView(MapController.getBag().getImage()); // если нет иконки предмета, берем вместо нее изображение мешка
                }
            }
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 10) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 10) * 450);
            tile.setId(String.valueOf(i));
            int finalI = i;
            tile.setOnMouseEntered(event -> Popover.showPopover(finalI + " " + Game.getItemsText(finalI + "NAME"), tile.getX(), tile.getY() + tileSize));
            tile.setOnMouseExited(event -> Popover.hidePopover());
            tile.setOnMouseClicked(event -> {
                setBorder(itemsPane);
                selectTile = items.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.ITEM;
                border.setX(items.get(Integer.parseInt(tile.getId())).getIcon().getX() - 1);
                border.setY(items.get(Integer.parseInt(tile.getId())).getIcon().getY() - 1);
            });

            ItemInfo itemInfo = items.get(i);
            itemInfo.setIcon(tile);
            itemInfo.setName(Game.getItemsText(i + "NAME"));
            itemInfo.setDesc(Game.getItemsText(i + "DESC"));
            if (itemInfo.getTypes() == null) {
                itemInfo.setTypes(Collections.singletonList(ItemTypeEnum.COMMON));
            }
            if (i > 0) {
                itemInfo.setImage(new ImageView("/graphics/items/" + i + ".png"));
            }
            itemsPane.getChildren().add(itemInfo.getIcon());
        }
        itemsPane.setOnMouseEntered(event -> Popover.setPane(itemsPane));
        scrollPane5.setContent(itemsPane);
        tabPane.getTabs().get(4).setContent(pane5);

        ScrollPane scrollPane6 = new ScrollPane();
        scrollPane6.setLayoutX(190);
        scrollPane6.setPrefSize(180, 600);

        for (int i = 0; i < pollutions.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/pollutions/" + i + ".png");
                int finalI = (i-1) / 3;
                int finalIndex = i;
                tile.setOnMouseEntered(event -> Popover.showPopover(finalIndex + " " + Game.getPollutionsText(String.valueOf(finalI)), tile.getX(), tile.getY() + tileSize));
                tile.setOnMouseExited(event -> Popover.hidePopover());
            }
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane6);
                selectTile = pollutions.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.POLLUTION;
                border.setX(pollutions.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(pollutions.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            pollutions.get(i).setImage(tile);
            pane6.getChildren().add(tile);
        }
        pane6.setOnMouseEntered(event -> Popover.setPane(pane6));
        scrollPane6.setContent(pane6);
        tabPane.getTabs().get(5).setContent(scrollPane6);

        ScrollPane scrollPane7 = new ScrollPane();
        scrollPane7.setLayoutX(190);
        scrollPane7.setPrefSize(180, 600);

        showZonesCheckBox.setLayoutX(5);
        showZonesCheckBox.setLayoutY(5);
        showZonesCheckBox.setOnAction(event -> changeShowZones());

        HBox box = new HBox();
        box.setLayoutX(5);
        box.setLayoutY(5);
        box.getChildren().addAll(showZonesCheckBox, showZonesLabel);
        box.setSpacing(5);

        pane7.getChildren().add(box);

        for (int i = 0; i < zones.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/zones/" + i + ".png");
            }
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(35 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane7);
                selectTile = zones.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.ZONE;
                border.setX(zones.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(zones.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            zones.get(i).setImage(tile);
            pane7.getChildren().add(tile);
        }
        scrollPane7.setContent(pane7);
        tabPane.getTabs().get(6).setContent(scrollPane7);

        Label weathersLabel = new Label(Game.getText("ACCESSIBLE_WEATHER"));
        weathersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        weathersLabel.setLayoutX(5);
        weathersLabel.setLayoutY(5);
        pane8.getChildren().add(weathersLabel);

        int y = 25;
        for (WeatherEnum weatherType : WeatherEnum.values()) {
            CheckBox checkBox = new CheckBox();
            checkBox.setLayoutX(5);
            checkBox.setLayoutY(5);
            checkBox.setId(weatherType.name());
            checkBox.setOnAction(event -> changeAccessibleWeather(checkBox.getId(), checkBox.isSelected()));
            checkBox.setSelected(weatherType.equals(WeatherEnum.CLEAR));

            Label label = new Label(weatherType.getDesc());
            label.setLayoutX(25);
            label.setLayoutY(y);

            Slider slider = new Slider();
            slider.setLayoutY(y);
            slider.setId(weatherType.name());
            slider.setLayoutX(200);
            slider.setPrefWidth(100);
            slider.setMajorTickUnit(1.0);
            slider.setShowTickMarks(true);
            slider.setSnapToTicks(true);
            slider.setMin(1);
            slider.setBlockIncrement(1);
            slider.setMax(10);
            slider.setDisable(!checkBox.isSelected());
            slider.valueProperty().addListener((obs, oldVal, newVal) ->
                    changeSliderValue(slider.getId(), newVal));

            weatherSliders.put(weatherType.name(), new Pair<>(checkBox, slider));

            HBox hBox = new HBox();
            hBox.setLayoutX(5);
            hBox.setLayoutY(y);
            hBox.getChildren().addAll(checkBox, slider, label);
            hBox.setSpacing(5);

            pane8.getChildren().add(hBox);
            y += 20;
        }

        Label currentWeathersLabel = new Label(Game.getText("CURRENT_WEATHER"));
        currentWeathersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        currentWeathersLabel.setLayoutX(5);
        currentWeathersLabel.setLayoutY(y);
        pane8.getChildren().add(currentWeathersLabel);

        y += 20;

        weatherCB.setLayoutX(5);
        weatherCB.setLayoutY(y);
        weatherCB.setPrefWidth(200);
        weatherCB.getItems().addAll(getAccessibleWeathers());
        weatherCB.setOnAction(event -> setWeather(weatherCB.getSelectionModel().getSelectedItem()));
        weatherCB.setVisibleRowCount(5);
        weatherCB.getSelectionModel().selectFirst();
        pane8.getChildren().add(weatherCB);

        tabPane.getTabs().get(7).setContent(pane8);

        showRoofsCheckBox.setLayoutX(5);
        showRoofsCheckBox.setLayoutY(5);
        showRoofsCheckBox.setOnAction(event -> changeRoofsZones());

        HBox box2 = new HBox();
        box2.setLayoutX(5);
        box2.setLayoutY(5);
        box2.getChildren().addAll(showRoofsCheckBox, showRoofsLabel);
        box2.setSpacing(5);

        pane9.getChildren().add(box2);

        ScrollPane scrollPane8 = new ScrollPane();
        scrollPane8.setLayoutX(190);
        scrollPane8.setPrefSize(180, 600);

        for (int i = 0; i < roofs.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/roofs/" + i + ".png");
            }
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(35 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane9);
                selectTile = roofs.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.ROOF;
                border.setX(roofs.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(roofs.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            roofs.get(i).setImage(tile);
            pane9.getChildren().add(tile);
        }
        scrollPane8.setContent(pane9);
        tabPane.getTabs().get(8).setContent(scrollPane8);

        selectConstructionImage.setLayoutX(5);
        selectConstructionImage.setLayoutY(5);
        selectConstructionImage.setOnMouseClicked(event -> selectConstruction());

        firstConstructionPointLabel.setLayoutY(15);
        firstConstructionPointLabel.setLayoutX(50);
        pane10.getChildren().add(firstConstructionPointLabel);

        firstConstructionPointTX.setLayoutX(80);
        firstConstructionPointTX.setLayoutY(15);
        firstConstructionPointTX.setPrefWidth(40);
        pane10.getChildren().add(firstConstructionPointTX);

        secondConstructionPointLabel.setLayoutY(15);
        secondConstructionPointLabel.setLayoutX(130);
        pane10.getChildren().add(secondConstructionPointLabel);

        secondConstructionPointTX.setLayoutX(155);
        secondConstructionPointTX.setLayoutY(15);
        secondConstructionPointTX.setPrefWidth(40);
        pane10.getChildren().add(secondConstructionPointTX);

        nameConstructionPointLabel.setLayoutY(15);
        nameConstructionPointLabel.setLayoutX(205);
        pane10.getChildren().add(nameConstructionPointLabel);

        nameConstructionTX.setLayoutX(260);
        nameConstructionTX.setLayoutY(15);
        nameConstructionTX.setPrefWidth(80);
        nameConstructionTX.setText("1");
        pane10.getChildren().add(nameConstructionTX);

        saveConstructionImage.setLayoutX(350);
        saveConstructionImage.setLayoutY(15);
        saveConstructionImage.setOnMouseClicked(event -> saveConstruction());
        pane10.getChildren().add(saveConstructionImage);

        pane10.getChildren().add(selectConstructionImage);

        ScrollPane scrollPane9 = new ScrollPane();
        scrollPane9.setLayoutX(190);
        scrollPane9.setPrefSize(180, 600);

        for (int i = 0; i < constructions.size(); i++) {
            ImageView tile = new ImageView("/graphics/gui/Construction.png");
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(50 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane10);
                selectTile = Integer.parseInt(tile.getId());
                selectedType = EditorObjectType.CONSTRUCTION;
                border.setX(constructions.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(constructions.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });
            int finalI = i;
            tile.setOnMouseEntered(event -> Popover.showPopover(finalI + " " + constructions.get(finalI).getName(), tile.getX(), tile.getY() + tileSize));
            tile.setOnMouseExited(event -> Popover.hidePopover());

            constructions.get(i).setImage(tile);
            pane10.getChildren().add(tile);
        }
        pane10.setOnMouseEntered(event -> Popover.setPane(pane10));
        scrollPane9.setContent(pane10);
        tabPane.getTabs().get(9).setContent(scrollPane9);

        root.getChildren().add(tabPane);
    }

    private static void changeSliderValue(String sliderId, Number newVal) {
        Game.getMap().getAccessibleWeathers().put(WeatherEnum.valueOf(sliderId), newVal.intValue());
    }

    // установить текущую погоду
    private void setWeather(WeatherEnum selected) {
        Map<WeatherEnum, Integer> weather = new HashMap<>();
        weather.put(selected, 6);
        Game.getMap().setCurrentWeather(weather);
    }

    // Получить доступные для текущей карты погоды
    private List<WeatherEnum> getAccessibleWeathers() {
        return Game.getMap().getAccessibleWeathers() != null ? new ArrayList<>(Game.getMap().getAccessibleWeathers().keySet()) :
                Collections.emptyList();
    }

    // Сохранить доступную для текущей карты погоду
    private void changeAccessibleWeather(String checkBoxId, boolean checked) {
        weatherSliders.get(checkBoxId).getValue().setDisable(!checked);
        if (checked) {
            Game.getMap().getAccessibleWeathers().put(WeatherEnum.valueOf(checkBoxId), (int) weatherSliders.get(checkBoxId).getValue().getValue());
        } else {
            Game.getMap().getAccessibleWeathers().remove(WeatherEnum.valueOf(checkBoxId));
        }
        WeatherEnum currentWeather = Game.getMap().getCurrentWeather().keySet().iterator().next();
        weatherCB.getItems().clear();
        weatherCB.getItems().addAll(getAccessibleWeathers());
        if (weatherCB.getItems().contains(currentWeather)) {
            setWeather(currentWeather);
            weatherCB.getSelectionModel().select(currentWeather);
        }
    }

    private void changeShowZones() {
        showZones = !showZones;
        MapController.drawCurrentMap();
    }

    private void changeRoofsZones() {
        showRoofs = !showRoofs;
        MapController.drawCurrentMap();
    }

    /**
     * Нажатие на кнопку выбора области для сохранения конструкции
     */
    private void selectConstruction() {
        firstConstructionPointTX.setText("");
        secondConstructionPointTX.setText("");
        selectedType = EditorObjectType.CONSTRUCTION_COORDINATES;
    }

    /**
     * Сохранить конструкцию для дальнейшего использования
     */
    private void saveConstruction() {
        if (!firstConstructionPointTX.getText().equals("") && !secondConstructionPointTX.getText().equals("")) {
            String[] firstCoordinates = firstConstructionPointTX.getText().split("\\.");
            int firstX = Integer.parseInt(firstCoordinates[0]);
            int firstY = Integer.parseInt(firstCoordinates[1]);
            String[] secondCoordinates = secondConstructionPointTX.getText().split("\\.");
            int secondX = Integer.parseInt(secondCoordinates[0]);
            int secondY = Integer.parseInt(secondCoordinates[1]);
            ConstructionInfo construction = new ConstructionInfo(firstX, firstY, secondX, secondY);
            construction.setName(nameConstructionTX.getText());
            for (int i = firstX; i <= secondX; i++) {
                for (int j = firstY; j <= secondY; j++) {
                    construction.getTiles()[i-firstX][j-firstY] = Game.getMap().getTiles()[i][j];
                    String creatureId = Game.getMap().getTiles()[i][j].getCreatureId();
                    String characterId = Game.getMap().getTiles()[i][j].getCharacterId();
                    if (creatureId != null) {
                        construction.getCreaturesList().put(creatureId, Game.getMap().getCreaturesList().get(creatureId));
                    }
                    if (characterId != null) {
                        construction.getCharacterList().put(characterId, Game.getMap().getCharacterList().get(characterId));
                    }
                }
            }
            constructions.add(construction);
            JsonUtils.saveConstructions(constructions);
        }
    }

    private void setBorder(Pane pane) {
        border.setVisible(true);
        if (pane1.getChildren().contains(border)) {
            pane1.getChildren().remove(border);
        } else if (pane2.getChildren().contains(border)) {
            pane2.getChildren().remove(border);
        } else if (pane3.getChildren().contains(border)) {
            pane3.getChildren().remove(border);
        } else if (pane4.getChildren().contains(border)) {
            pane4.getChildren().remove(border);
        } else if (itemsPane.getChildren().contains(border)) {
            itemsPane.getChildren().remove(border);
        } else if (pane6.getChildren().contains(border)) {
            pane6.getChildren().remove(border);
        } else if (pane7.getChildren().contains(border)) {
            pane7.getChildren().remove(border);
        } else if (pane9.getChildren().contains(border)) {
            pane9.getChildren().remove(border);
        } else pane10.getChildren().remove(border);
        pane.getChildren().add(border);
    }

    // Фильтрует предметы в редакторе при поиске по названию
    private void filterItems(String itemType, String searchString) {
        ItemTypeEnum type = ItemTypeEnum.getItemTypeByCode(itemType);
        int i = 1;
        boolean visible;
        for (ItemInfo itemInfo : items) {
            ImageView itemTile = itemInfo.getIcon();
            List<ItemTypeEnum> types = itemInfo.getTypes();
            if (types != null) {
                visible = (types.contains(type) || ItemTypeEnum.ALL.equals(type)) &&
                        ((itemInfo.getDesc().toLowerCase().contains(searchString.toLowerCase())) ||
                                (itemInfo.getName().toLowerCase().contains(searchString.toLowerCase())));
                itemTile.setVisible(visible);
                if (itemTile.isVisible()) {
                    itemTile.setX(5 + (i / 10) * (tileSize + 5));
                    itemTile.setY(5 + (i) * (tileSize + 5) - (i / 10) * 450);
                    i++;
                }
            }
            if (selectedType.equals(EditorObjectType.ITEM)) {
                ImageView icon = items.get(selectTile).getIcon();
                if (icon.isVisible()) {
                    border.setVisible(true);
                    border.setX(icon.getX() - 1);
                    border.setY(icon.getY() - 1);
                } else {
                    border.setVisible(false);
                }
            }
        }
        itemsPane.setMaxWidth(45 + (i / 10) * (tileSize + 5));
    }

    // Фильтрует тайлы в редакторе при поиске по названию
    private void filterTiles(String searchString, List<TileInfo> tiles) {
        int i = 0;
        boolean visible;
        for (TileInfo tileInfo : tiles) {
            ImageView tile = tileInfo.getImage();
            visible = ((tileInfo.getDesc().toLowerCase().contains(searchString.toLowerCase())) ||
                    (tileInfo.getName().toLowerCase().contains(searchString.toLowerCase())));
            tile.setVisible(visible);
            if (tile.isVisible()) {
                tile.setX(5 + (i / 12) * (tileSize + 5));
                tile.setY(35 + (i) * (tileSize + 5) - (i / 12) * 540);
                i++;
            }
        }
    }

    // Фильтрует персонажей в редакторе при поиске по имени
    private void filterCharacters(String searchString) {
        int i = 0;
        boolean visible;
        for (CharacterInfo characterInfo : characters) {
            ImageView npcTile = characterInfo.getImage();
            visible = (characterInfo.getImageId() == 0 || (characterInfo.getDesc().toLowerCase().contains(searchString.toLowerCase())) ||
                    (characterInfo.getName().toLowerCase().contains(searchString.toLowerCase())));
            npcTile.setVisible(visible);
            if (npcTile.isVisible()) {
                npcTile.setX(5 + (i / 12) * (tileSize + 5));
                npcTile.setY(35 + (i) * (tileSize + 5) - (i / 12) * 540);
                i++;
            }
        }
    }

    // Фильтрует существ в редакторе при поиске по имени
    private void filterCreatures(String searchString) {
        int i = 0;
        boolean visible;
        for (CreatureInfo creatureInfo : creatures) {
            ImageView creatureTile = creatureInfo.getImage();
            visible = (creatureInfo.getImageId() == 0 || (creatureInfo.getDesc().toLowerCase().contains(searchString.toLowerCase())) ||
                    (creatureInfo.getName().toLowerCase().contains(searchString.toLowerCase())));
            creatureTile.setVisible(visible);
            if (creatureTile.isVisible()) {
                creatureTile.setX(5 + (i / 12) * (tileSize + 5));
                creatureTile.setY(35 + (i) * (tileSize + 5) - (i / 12) * 540);
                i++;
            }
        }
    }

}
