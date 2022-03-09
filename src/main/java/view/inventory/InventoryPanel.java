package view.inventory;

import model.editor.ItemInfo;
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import model.entity.player.Player;
import view.Game;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static view.params.GameParams.tileSize;

/*
 * Инвентарь
 */
public class InventoryPanel {
    @Getter
    private final TabPane tabPane = new TabPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final Pane pane = new Pane();
    private final Pane outerPane = new Pane();
    private final Label itemNameLabel = new Label(Game.getText("NAME"));
    private final ImageView sortImage = new ImageView("/graphics/gui/Sort1.png");
    private final Label typeLabel = new Label(Game.getText("TYPE"));
    private final Label weightLabel = new Label(Game.getText("WEIGHT"));
    private final Label volumeLabel = new Label(Game.getText("VOLUME"));
    private final Label priceLabel = new Label(Game.getText("PRICE"));
    private final Label totalWeightLabel = new Label(Game.getText("TOTAL_WEIGHT"));
    private final Label totalVolumeLabel = new Label(Game.getText("TOTAL_VOLUME"));

    private Boolean descending = Boolean.TRUE;
    private List<Items> inventory = Game.getMap().getPlayer().getInventory();

    private final DecimalFormat formatter = new DecimalFormat("###,###.###");

    public InventoryPanel(Group root) {
        tabPane.setLayoutX(5);
        tabPane.setLayoutY(35);
        tabPane.setPrefSize(490, 455);
        tabPane.setMinHeight(450);
        tabPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        for (ItemTypeEnum itemType : ItemTypeEnum.values()) {
            Tab tab = new Tab(itemType.getDesc());
            tab.setClosable(Boolean.FALSE);
            tabPane.getTabs().add(tab);
        }

        pane.setLayoutX(190);
        pane.setPrefSize(480, 350);

        outerPane.setLayoutX(190);
        outerPane.setPrefSize(480, 350);

        itemNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        itemNameLabel.setLayoutX(tileSize + 5);
        itemNameLabel.setLayoutY(10);
        pane.getChildren().add(itemNameLabel);

        sortImage.setX(100);
        sortImage.setY(10);
        sortImage.setOnMouseClicked(event -> {
            descending = !descending;
            sortImage.setImage(descending ? new Image("/graphics/gui/Sort1.png") :
                    new Image("/graphics/gui/Sort2.png"));
            pane.getChildren().set(1, sortImage);
            drawItems(descending, tabPane.getSelectionModel().getSelectedItem().getText());
        });
        pane.getChildren().add(sortImage);

        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        typeLabel.setLayoutX(230);
        typeLabel.setLayoutY(10);
        pane.getChildren().add(typeLabel);

        weightLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        weightLabel.setLayoutX(350);
        weightLabel.setLayoutY(10);
        pane.getChildren().add(weightLabel);

        volumeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        volumeLabel.setLayoutX(400);
        volumeLabel.setLayoutY(10);
        pane.getChildren().add(volumeLabel);

        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        priceLabel.setLayoutX(450);
        priceLabel.setLayoutY(10);
        pane.getChildren().add(priceLabel);

        drawItems(descending, tabPane.getSelectionModel().getSelectedItem().getText());

        scrollPane.setPrefSize(400, 400);
        scrollPane.setMaxHeight(400);
        scrollPane.setMinWidth(490);
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
        tabPane.setVisible(Boolean.FALSE);

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, tab1, tab2) -> {
                    tab1.setContent(null);
                    tab2.setContent(outerPane);
                    drawItems(descending, tabPane.getSelectionModel().getSelectedItem().getText());
                }
        );

        root.getChildren().add(tabPane);
    }

    private void setWeightText() {
        BigDecimal currentWeight = Player.getCurrentWeight(inventory);
        BigDecimal maxWeight = Player.getMaxWeight(Game.getMap().getPlayer().getCharacteristics().get(0));
        totalWeightLabel.setTextFill(currentWeight.compareTo(maxWeight) > 0 ? Color.web("#FF0000") : Color.web("#000000") );
        totalWeightLabel.setText(Game.getText("TOTAL_WEIGHT") + " " + formatter.format(currentWeight) + "/" +
                formatter.format(maxWeight) + " " +
                Game.getText("KG"));
    }

    private void setVolumeText() {
        BigDecimal currentVolume = Player.getCurrentVolume(inventory);
        BigDecimal maxVolume = Player.getMaxVolume();
        totalVolumeLabel.setTextFill(currentVolume.compareTo(maxVolume) > 0 ? Color.web("#FF0000") : Color.web("#000000") );
        totalVolumeLabel.setText(Game.getText("TOTAL_VOLUME") + " " + formatter.format(currentVolume) + "/" +
                formatter.format(maxVolume) + " " +
                Game.getText("LITERS"));
    }

    public void drawItems(Boolean descending, String itemType) {
        ItemTypeEnum type = ItemTypeEnum.getItemTypeByCode(itemType);
        pane.getChildren().remove(6, pane.getChildren().size());
        int i = 0;
        inventory.sort(
                descending ? Items.compareByName : Items.compareByName.reversed());
        for (Items items : inventory) {
            ItemInfo itemInfo = Game.getEditor().getItems().get(items.getTypeId());
            List<ItemTypeEnum> types = itemInfo.getTypes();
            if (types != null && (types.contains(type) || ItemTypeEnum.ALL.equals(type))) {
                var itemRecord = new ItemRecord(i++, items);
                itemRecord.getPane().setLayoutY(i * tileSize);
                pane.getChildren().add(itemRecord.getPane());
            }
        }
    }

    public void showInventory() {
        Game.getInventory().getTabPane().setVisible(
                !Game.getInventory().getTabPane().isVisible());
        Game.getInventory().drawItems(Boolean.FALSE,
                Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());

        setWeightText();
        setVolumeText();
    }
}
