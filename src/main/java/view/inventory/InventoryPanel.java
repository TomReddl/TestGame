package view.inventory;

import controller.ItemsController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;
import model.editor.TileTypeEnum;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesInfo;
import model.entity.ItemTypeEnum;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import model.entity.player.Character;
import view.Game;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static game.GameParams.tileSize;

/**
 * Панель инвентаря
 */
public class InventoryPanel {

    // Тип инвентаря
    public enum InventoryTypeEnum {
        PLAYER, // инвентарь персонажа игрока
        CONTAINER, // инвентарь контейнера
        CHARACTER; // инвентарь неигрового персонажа
    }

    // Режим открытия инвентаря
    public enum ShowModeEnum {
        DEFAULT, // обычное открытие инвентаря
        SELECT_FOR_POTION_CRAFT, // выбор предмета для зельеварения
        SELECT_FOR_POTION_EXPLORE, // выбор предмета для исследования
        SELECT_FOR_COMBINER, // выбор предмета для объединения
        SELECT_FOR_DUPLICATOR, // выбор предмета для дублирования
        SELECT_INLAYER_FOR_ENCHANT, // выбор инкрустата для зачарования
        SELECT_INLAYER_FOR_DUPLICATOR, // выбор инкрустата для дублирования
        SELECT_ITEM_FOR_ENCHANT; // выбор предмета для зачарования
    }

    // Тип сортировки
    public enum SortType {
        NAME("Имя"),
        PRICE("Цена"),
        WEIGHT("Вес"),
        VOLUME("Объем");

        @Getter
        private final String desc;

        SortType(String desc) {
            this.desc = desc;
        }
    }

    @Getter
    private final TabPane tabPane = new TabPane();
    @Getter
    private final Map<String, Tab> tabHolder = new HashMap<>();
    @Getter
    private final ScrollPane scrollPane = new ScrollPane();
    private final Pane pane = new Pane();
    private final Pane outerPane = new Pane();
    private final Label itemNameLabel = new Label(Game.getText("NAME"));
    private final ImageView sortNameImage = new ImageView("/graphics/gui/Sort2.png");
    private final ImageView sortWeightImage = new ImageView("/graphics/gui/Sort1.png");
    private final ImageView sortVolumeImage = new ImageView("/graphics/gui/Sort1.png");
    private final ImageView sortPriceImage = new ImageView("/graphics/gui/Sort1.png");
    private final Button takeAllButton = new Button(Game.getText("TAKE_ALL"));
    private final Button storeTrashButton = new Button(Game.getText("STORE_ALL_TRASH"));
    @Getter
    private final Button butcherButton = new Button(Game.getText("BUTCHER")); // Кнопка "Разделать тушу" для трупа существа
    @Getter
    private final Label typeLabel = new Label(Game.getText("TYPE"));
    @Getter
    private final Label weightLabel = new Label(Game.getText("WEIGHT") + " " + Game.getText("KG"));
    @Getter
    private final Label volumeLabel = new Label(Game.getText("VOLUME") + " " + Game.getText("LITERS"));
    @Getter
    private final Label priceLabel = new Label(Game.getText("PRICE"));
    private static final Label totalWeightLabel = new Label(Game.getText("TOTAL_WEIGHT"));
    private final Label totalVolumeLabel = new Label(Game.getText("TOTAL_VOLUME"));

    private boolean descending = false;
    @Getter
    @Setter
    private List<Items> items = Game.getMap().getPlayersSquad().getSelectedCharacter().getInventory();
    @Getter
    @Setter
    private Integer characterId = null; // id персонажа, чей инвентарь открыт
    @Getter
    private int x; // координаты нужны для панели инвентаря контейнера, чтобы перерисовывать тайл, если игрок забирает с него все предметы
    @Getter
    private int y;
    @Getter
    private InventoryTypeEnum inventoryType; // текущий тип инвентаря
    @Getter
    private ShowModeEnum showMode; // тип открытия инвентаря

    private static final DecimalFormat formatter = new DecimalFormat("###,###.###");

    public InventoryPanel(double XPos, double YPos, InventoryTypeEnum type) {
        inventoryType = type;
        tabPane.setLayoutX(XPos);
        tabPane.setLayoutY(YPos);
        tabPane.setPrefSize(550, 450);
        tabPane.setMinHeight(350);
        tabPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        tabPane.setVisible(false);

        for (ItemTypeEnum itemType : ItemTypeEnum.getItemTypesForFilter()) {
            var tab = new Tab(itemType.getDesc());
            tab.setClosable(false);
            tabHolder.put(itemType.getDesc(), tab);
        }

        if (inventoryType.equals(InventoryTypeEnum.PLAYER)) {
            var tab = new Tab(Game.getText("EQUIPPED"));
            tab.setClosable(false);
            tabHolder.put(Game.getText("EQUIPPED"), tab);
        }

        filterInventoryTabs(tabPane.getSelectionModel().getSelectedItem());

        pane.setLayoutX(190);
        pane.setPrefSize(480, 350);

        outerPane.setLayoutX(190);
        outerPane.setPrefSize(480, 50);

        itemNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        itemNameLabel.setLayoutX(tileSize + 5);
        itemNameLabel.setLayoutY(10);
        pane.getChildren().add(itemNameLabel);

        sortNameImage.setX(100);
        sortNameImage.setY(10);
        sortNameImage.setOnMouseClicked(event -> {
            descending = !descending;
            sortNameImage.setImage(descending ? new Image("/graphics/gui/Sort1.png") :
                    new Image("/graphics/gui/Sort2.png"));
            drawItems(SortType.NAME, descending, tabPane.getSelectionModel().getSelectedItem().getText());
        });
        pane.getChildren().add(sortNameImage);

        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        typeLabel.setLayoutX(210);
        typeLabel.setLayoutY(10);
        pane.getChildren().add(typeLabel);

        weightLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        weightLabel.setLayoutX(330);
        weightLabel.setLayoutY(10);
        pane.getChildren().add(weightLabel);

        sortWeightImage.setX(370);
        sortWeightImage.setY(10);
        sortWeightImage.setOnMouseClicked(event -> {
            descending = !descending;
            sortWeightImage.setImage(descending ? new Image("/graphics/gui/Sort1.png") :
                    new Image("/graphics/gui/Sort2.png"));
            drawItems(SortType.WEIGHT, descending, tabPane.getSelectionModel().getSelectedItem().getText());
        });
        pane.getChildren().add(sortWeightImage);

        volumeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        volumeLabel.setLayoutX(400);
        volumeLabel.setLayoutY(10);
        pane.getChildren().add(volumeLabel);

        sortVolumeImage.setX(470);
        sortVolumeImage.setY(10);
        sortVolumeImage.setOnMouseClicked(event -> {
            descending = !descending;
            sortVolumeImage.setImage(descending ? new Image("/graphics/gui/Sort1.png") :
                    new Image("/graphics/gui/Sort2.png"));
            drawItems(SortType.VOLUME, descending, tabPane.getSelectionModel().getSelectedItem().getText());
        });
        pane.getChildren().add(sortVolumeImage);

        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        priceLabel.setLayoutX(485);
        priceLabel.setLayoutY(10);
        pane.getChildren().add(priceLabel);

        sortPriceImage.setX(515);
        sortPriceImage.setY(10);
        sortPriceImage.setOnMouseClicked(event -> {
            descending = !descending;
            sortPriceImage.setImage(descending ? new Image("/graphics/gui/Sort1.png") :
                    new Image("/graphics/gui/Sort2.png"));
            drawItems(SortType.PRICE, descending, tabPane.getSelectionModel().getSelectedItem().getText());
        });
        pane.getChildren().add(sortPriceImage);

        scrollPane.setPrefSize(400, 400);
        scrollPane.setMaxHeight(400);
        scrollPane.setMinWidth(550);
        scrollPane.setContent(pane);
        scrollPane.setStyle("-fx-border-color:black;");

        outerPane.getChildren().add(scrollPane);

        totalVolumeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        totalVolumeLabel.setLayoutY(405);
        totalVolumeLabel.setLayoutX(250);
        outerPane.getChildren().add(totalVolumeLabel);

        if (inventoryType.equals(InventoryTypeEnum.PLAYER)) {
            totalWeightLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            totalWeightLabel.setLayoutY(405);
            totalWeightLabel.setLayoutX(10);
            outerPane.getChildren().add(totalWeightLabel);
        } else {
            scrollPane.setMaxHeight(380);
            takeAllButton.setLayoutX(10);
            takeAllButton.setLayoutY(395);
            takeAllButton.setOnAction(event -> ItemsController.takeAllItems());
            outerPane.getChildren().add(takeAllButton);

            storeTrashButton.setLayoutX(90);
            storeTrashButton.setLayoutY(395);
            storeTrashButton.setOnAction(event -> ItemsController.storeTrash());
            storeTrashButton.setVisible(false);
            outerPane.getChildren().add(storeTrashButton);

            butcherButton.setLayoutX(90);
            butcherButton.setLayoutY(395);
            butcherButton.setOnAction(event -> ItemsController.butcheringCreature());
            butcherButton.setVisible(false);
            outerPane.getChildren().add(butcherButton);
        }

        tabPane.getTabs().get(0).setContent(outerPane);

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, tab1, tab2) -> {
                    if (tab1 != null) {
                        tab1.setContent(null);
                    }
                    if (tab2 != null) {
                        tab2.setContent(outerPane);
                        drawItems(SortType.NAME, descending, tabPane.getSelectionModel().getSelectedItem().getText());
                    }
                }
        );

        if (inventoryType.equals(InventoryTypeEnum.CONTAINER)) {
            tabPane.setVisible(false);
            Game.getRoot().getChildren().add(tabPane);
        }
    }

    public static void setWeightText() {
        var currentWeight = Game.getMap().getPlayersSquad().getSelectedCharacter().getCurrentWeight();
        var maxWeight = Game.getMap().getPlayersSquad().getSelectedCharacter().getMaxWeight();
        if (currentWeight != null && maxWeight != null) {
            totalWeightLabel.setTextFill(currentWeight.compareTo(maxWeight) > 0 ? Color.web("#FF0000") : Color.web("#000000"));
            totalWeightLabel.setText(Game.getText("TOTAL_WEIGHT") + " " + formatter.format(currentWeight) + "/" +
                    formatter.format(maxWeight) + " " +
                    Game.getText("KG"));
        }
    }

    public void setVolumeText() {
        var currentVolume = ItemsController.getCurrVolume(items);
        BigDecimal maxVolume = null;
        MapCellInfo interactMapPoint = Game.getMap().getPlayersSquad().getSelectedCharacter().getInteractMapPoint();
        if (inventoryType.equals(InventoryTypeEnum.PLAYER)) {
            maxVolume = Game.getMap().getPlayersSquad().getSelectedCharacter().getMaxVolume();
        } else if (interactMapPoint != null) {
            String tileType = interactMapPoint.getTile2Info().getType();
            if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CONTAINER)) {
                maxVolume = BigDecimal.valueOf(Integer.parseInt(interactMapPoint.getTile2Info().getParams().get("volume"))).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);;
            }
        }
        if (currentVolume != null && maxVolume != null) {
            totalVolumeLabel.setTextFill(currentVolume.compareTo(maxVolume) > 0 ? Color.web("#FF0000") : Color.web("#000000"));
            totalVolumeLabel.setText(Game.getText("TOTAL_VOLUME") + " " + formatter.format(currentVolume) + "/" +
                    formatter.format(maxVolume) + " " +
                    Game.getText("LITERS"));
        }
    }

    public void drawItems(SortType sort, Boolean descending, String selectType) {
        var comparator = Items.comparators.get(sort);
        int i = 0;
        if (Game.getText("EQUIPPED").equals(selectType)) {
            pane.getChildren().remove(pane.getChildren().indexOf(sortPriceImage) + 1, pane.getChildren().size());

            var wearingItems = items.stream()
                    .filter(Items::isEquipment).sorted(descending ? comparator.reversed() : comparator)
                    .collect(Collectors.toList());
            List<String> nudeBodyParts = BodyPartEnum.getCodes();
            for (Items items : wearingItems) {
                nudeBodyParts.remove(((ClothesInfo) items.getInfo()).getBodyPart());
                var itemRecord = new ItemRecord(items, selectType, this);
                itemRecord.getPane().setLayoutY(++i * tileSize);
                pane.getChildren().add(itemRecord.getPane());
            }

            for (String bodyPart : nudeBodyParts) {
                var itemRecord = new ItemRecord(bodyPart);
                itemRecord.getPane().setLayoutY(++i * tileSize);
                pane.getChildren().add(itemRecord.getPane());
            }
        } else {
            ItemTypeEnum type = null;
            ItemTypeEnum secondType = null;
            if (ItemTypeEnum.WEARABLE.getDesc().equals(selectType)) {
                type = ItemTypeEnum.CLOTHES;
                secondType = ItemTypeEnum.WEAPON;
            } else {
                type = ItemTypeEnum.getItemTypeByCode(selectType);
            }
            pane.getChildren().remove(pane.getChildren().indexOf(sortPriceImage) + 1, pane.getChildren().size());

            if (items != null) {
                items.sort(
                        descending ? comparator.reversed() : comparator);
                for (Items items : items) {
                    List<ItemTypeEnum> types = items.getInfo().getTypes();
                    if (types != null && (types.contains(type) || (secondType != null && types.contains(secondType)) || ItemTypeEnum.ALL.equals(type))) {
                        var itemRecord = new ItemRecord(items, selectType, this);
                        itemRecord.getPane().setLayoutY(++i * tileSize);
                        pane.getChildren().add(itemRecord.getPane());
                    }
                }
            }
        }
        pane.setMinHeight(40 * (i + 1));
    }

    public void refreshInventory() {
        drawItems(SortType.NAME, false,
                tabPane.getSelectionModel().getSelectedItem().getText());
        setWeightText();
        setVolumeText();
    }

    public void show(List<Items> itemsList, int x, int y, ShowModeEnum showMode, String type, Integer characterId) {
        if (type.equals("character")) {
            inventoryType = InventoryTypeEnum.CHARACTER;
        } else if (type.equals("")) {
            inventoryType = InventoryTypeEnum.CONTAINER;
        } else {
            inventoryType = InventoryTypeEnum.PLAYER;
        }
        this.characterId = characterId;
        this.x = x;
        this.y = y;
        items = itemsList;
        refreshInventory();
        tabPane.setVisible(true);
        storeTrashButton.setVisible(type.equals("trashCan"));
        Creature creature = Game.getMap().getPlayersSquad().getSelectedCharacter().getInteractCreature();
        butcherButton.setVisible(type.equals("creature") && creature != null && !creature.isButchering());
        this.showMode = showMode;
        totalVolumeLabel.setVisible(false);
        MapCellInfo interactMapPoint = Game.getMap().getPlayersSquad().getSelectedCharacter().getInteractMapPoint();
        if (interactMapPoint != null) {
            String tileType = interactMapPoint.getTile2Info().getType();
            if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CONTAINER)) {
                totalVolumeLabel.setVisible(true);
                Game.getContainerInventory().setVolumeText();
            }
        } else if (inventoryType.equals(InventoryTypeEnum.PLAYER)) {
            totalVolumeLabel.setVisible(true);
        }
    }

    public void hide() {
        tabPane.setVisible(false);
    }

    /*
     * Скрывает вкладки типов предметов в инвентаре, если у персонажа нет предметов данного типа
     */
    public void filterInventoryTabs(Tab selectTab) {
        tabPane.getTabs().clear();

        for (ItemTypeEnum itemType : ItemTypeEnum.getItemTypesForFilter()) {
            var found = false;
            for (Items items : items) {
                List<ItemTypeEnum> types = items.getInfo().getTypes();
                if (types != null && (types.contains(itemType) || ItemTypeEnum.ALL.equals(itemType))) {
                    found = true;
                    break;
                }
            }

            if (found) {
                tabPane.getTabs().add(tabHolder.get(itemType.getDesc()));
            }
        }
        if (tabPane.getTabs().isEmpty()) {
            tabPane.getTabs().add(0, tabHolder.get(ItemTypeEnum.ALL.getDesc()));
        }
        if (inventoryType.equals(InventoryTypeEnum.PLAYER)) {
            tabPane.getTabs().add(1, tabHolder.get(Game.getText("EQUIPPED")));
        }
        if (tabPane.getTabs().contains(selectTab)) {
            tabPane.getSelectionModel().select(selectTab);
        } else {
            tabPane.getSelectionModel().select(0);
        }
    }

    public List<Items> getItems() {
        if (inventoryType.equals(InventoryTypeEnum.CONTAINER)) {
            if (items == null) {
                Character character = Game.getMap().getPlayersSquad().getSelectedCharacter();
                items = new ArrayList<>();
                Game.getMap().getTiles()[character.getXMapPos() + Game.getContainerInventory().getX()][character.getYMapPos() + Game.getContainerInventory().getY()].setItems(items);
            }
        }
        return items;
    }
}
