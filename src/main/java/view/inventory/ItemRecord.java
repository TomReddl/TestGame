package view.inventory;

import model.editor.ItemInfo;
import model.entity.map.Items;
import view.Game;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import static view.params.GameParams.tileSize;

/*
 * Строка с информацией о предмете для отображения в инвентаре
 */
public class ItemRecord {
    @Getter
    private Pane pane;
    private ImageView icon;
    private Label nameLabel;
    private Label typeLabel;
    private Label weightLabel;
    private Label volumeLabel;
    private Label priceLabel;

    public ItemRecord(int index, Items items) {
        ItemInfo itemInfo = Game.getEditor().getItems().get(items.getTypeId());
        pane = new Pane();
        pane.setPrefSize(480, 40);
        pane.setBackground(new Background(new BackgroundFill(
                (((index & 1) == 0) ? Color.WHITESMOKE : Color.GAINSBORO), CornerRadii.EMPTY, Insets.EMPTY)));

        icon = new ImageView(itemInfo.getIcon().getImage());
        pane.getChildren().add(icon);

        var nameText = items.getCount() > 1 ?
                (itemInfo.getName() + " (" + items.getCount() + ")") :
                itemInfo.getName();
        nameLabel = new Label(nameText);
        nameLabel.setLayoutX(tileSize + 5);
        nameLabel.setLayoutY(10);
        pane.getChildren().add(nameLabel);

        typeLabel = new Label(itemInfo.getTypes().get(0).getDesc());
        typeLabel.setLayoutX(230);
        typeLabel.setLayoutY(10);
        pane.getChildren().add(typeLabel);

        weightLabel = new Label(itemInfo.getWeight().toString());
        weightLabel.setLayoutX(350);
        weightLabel.setLayoutY(10);
        pane.getChildren().add(weightLabel);

        volumeLabel = new Label(itemInfo.getVolume().toString());
        volumeLabel.setLayoutX(400);
        volumeLabel.setLayoutY(10);
        pane.getChildren().add(volumeLabel);

        priceLabel = new Label(itemInfo.getPrice().toString());
        priceLabel.setLayoutX(450);
        priceLabel.setLayoutY(10);
        pane.getChildren().add(priceLabel);
    }
}
