package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import view.Game;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static view.params.GameParams.tileSize;

/*
 * Инвентарь
 */
public class InventoryPanel {
    @Getter
    private final TabPane tabPane = new TabPane();
    private final Map<String, Tab> tabHolder = new HashMap<>();
    private final ScrollPane scrollPane = new ScrollPane();
    private final Pane pane = new Pane();
    private final Pane outerPane = new Pane();
    private final Label itemNameLabel = new Label(Game.getText("NAME"));
    private final ImageView sortNameImage = new ImageView("/graphics/gui/Sort1.png");
    private final ImageView sortWeightImage = new ImageView("/graphics/gui/Sort1.png");
    private final ImageView sortVolumeImage = new ImageView("/graphics/gui/Sort1.png");
    private final ImageView sortPriceImage = new ImageView("/graphics/gui/Sort1.png");
    @Getter
    private static final Label typeLabel = new Label(Game.getText("TYPE"));
    @Getter
    private static final Label weightLabel = new Label(Game.getText("WEIGHT") + " " + Game.getText("LITERS"));
    @Getter
    private static final Label volumeLabel = new Label(Game.getText("VOLUME") + " " + Game.getText("KG"));
    @Getter
    private static final Label priceLabel = new Label(Game.getText("PRICE"));
    private static final Label totalWeightLabel = new Label(Game.getText("TOTAL_WEIGHT"));
    private static final Label totalVolumeLabel = new Label(Game.getText("TOTAL_VOLUME"));

    private Boolean descending = Boolean.TRUE;
    private List<Items> inventory = Game.getMap().getPlayer().getInventory();

    private static final DecimalFormat formatter = new DecimalFormat("###,###.###");

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

    public InventoryPanel(Group root) {
        tabPane.setLayoutX(210);
        tabPane.setLayoutY(5);
        tabPane.setPrefSize(550, 450);
        tabPane.setMinHeight(350);
        tabPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        for (ItemTypeEnum itemType : ItemTypeEnum.values()) {
            var tab = new Tab(itemType.getDesc());
            tab.setClosable(Boolean.FALSE);
            tabHolder.put(itemType.getDesc(), tab);
        }

        var tab = new Tab(Game.getText("EQUIPPED"));
        tab.setClosable(Boolean.FALSE);
        tabHolder.put(Game.getText("EQUIPPED"), tab);

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
        priceLabel.setLayoutX(500);
        priceLabel.setLayoutY(10);
        pane.getChildren().add(priceLabel);

        sortPriceImage.setX(530);
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

        totalWeightLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        totalWeightLabel.setLayoutY(405);
        totalWeightLabel.setLayoutX(10);
        setWeightText();
        outerPane.getChildren().add(totalWeightLabel);

        totalVolumeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        totalVolumeLabel.setLayoutY(405);
        totalVolumeLabel.setLayoutX(180);
        setVolumeText();
        outerPane.getChildren().add(totalVolumeLabel);

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

        root.getChildren().add(ItemDetailPanel.getPane());
        root.getChildren().add(PlayerStatsPanel.getPane());

        drawItems(SortType.NAME, descending, tabPane.getSelectionModel().getSelectedItem().getText());
    }

    public static void setWeightText() {
        var currentWeight = Game.getMap().getPlayer().getCurrentWeight();
        var maxWeight = Game.getMap().getPlayer().getMaxWeight();
        totalWeightLabel.setTextFill(currentWeight.compareTo(maxWeight) > 0 ? Color.web("#FF0000") : Color.web("#000000"));
        totalWeightLabel.setText(Game.getText("TOTAL_WEIGHT") + " " + formatter.format(currentWeight) + "/" +
                formatter.format(maxWeight) + " " +
                Game.getText("KG"));
    }

    public static void setVolumeText() {
        var currentVolume = Game.getMap().getPlayer().getCurrentVolume();
        var maxVolume = Game.getMap().getPlayer().getMaxVolume();
        totalVolumeLabel.setTextFill(currentVolume.compareTo(maxVolume) > 0 ? Color.web("#FF0000") : Color.web("#000000"));
        totalVolumeLabel.setText(Game.getText("TOTAL_VOLUME") + " " + formatter.format(currentVolume) + "/" +
                formatter.format(maxVolume) + " " +
                Game.getText("LITERS"));
    }

    public void drawItems(SortType sort, Boolean descending, String selectType) {
        var comparator = Items.comparators.get(sort);
        if (Game.getText("EQUIPPED").equals(selectType)) {
            pane.getChildren().remove(pane.getChildren().indexOf(sortPriceImage) + 1, pane.getChildren().size());
            int i = 0;

            var wearingItems = inventory.stream().filter(t -> t.isEquipment()).collect(Collectors.toList());
            wearingItems.sort(
                    descending ? comparator : comparator.reversed());
            for (Items items : wearingItems) {
                var itemRecord = new ItemRecord(items);
                itemRecord.getPane().setLayoutY(++i * tileSize);
                pane.getChildren().add(itemRecord.getPane());
            }
        } else {
            ItemTypeEnum type = ItemTypeEnum.getItemTypeByCode(selectType);
            pane.getChildren().remove(pane.getChildren().indexOf(sortPriceImage) + 1, pane.getChildren().size());
            int i = 0;

            inventory.sort(
                    descending ? comparator : comparator.reversed());
            for (Items items : inventory) {
                List<ItemTypeEnum> types = items.getInfo().getTypes();
                if (types != null && (types.contains(type) || ItemTypeEnum.ALL.equals(type))) {
                    var itemRecord = new ItemRecord(items);
                    itemRecord.getPane().setLayoutY(++i * tileSize);
                    pane.getChildren().add(itemRecord.getPane());
                }
            }
        }
        pane.setMinHeight(40 * inventory.size());
    }

    public void show(Boolean show) {
        Game.getInventory().drawItems(SortType.NAME, Boolean.FALSE,
                Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());

        setWeightText();
        setVolumeText();

        PlayerStatsPanel.getPane().setVisible(show);
    }

    /*
     * Скрывает вкладки типов предметов в инвентаре, если у персонажа нет предметов данного типа
     */
    public void filterInventoryTabs(Tab selectTab) {
        tabPane.getTabs().clear();

        for (ItemTypeEnum itemType : ItemTypeEnum.values()) {
            boolean found = Boolean.FALSE;
            for (Items items : inventory) {
                List<ItemTypeEnum> types = items.getInfo().getTypes();
                if (types != null && (types.contains(itemType) || ItemTypeEnum.ALL.equals(itemType))) {
                    found = Boolean.TRUE;
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
        tabPane.getTabs().add(1, tabHolder.get(Game.getText("EQUIPPED")));
        if (tabPane.getTabs().contains(selectTab)) {
            tabPane.getSelectionModel().select(selectTab);
        } else {
            tabPane.getSelectionModel().select(0);
        }
    }
}
