package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import model.editor.items.ClothesInfo;
import model.editor.items.ItemInfo;
import model.editor.items.WeaponInfo;
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import view.Game;

import static view.params.GameParams.tileSize;

/*
 * Строка с информацией о предмете для отображения в инвентаре
 */
public class ItemRecord {
    @Getter
    private Pane pane;
    private ImageView icon;
    private ImageView brokenItemIcon; // иконка сломанного предмета
    private Label nameLabel;
    private Label typeLabel;
    private Label weightLabel;
    private Label volumeLabel;
    private Label priceLabel;

    public ItemRecord(Items items, String selectType) {
        ItemInfo itemInfo = items.getInfo();
        pane = new Pane();
        pane.setPrefSize(550, 40);
        pane.setBackground(new Background(new BackgroundFill(
                ((items.isEquipment()) ? Color.GAINSBORO : Color.WHITESMOKE), CornerRadii.EMPTY, Insets.EMPTY)));

        icon = new ImageView(itemInfo.getIcon().getImage());
        pane.getChildren().add(icon);

        if ((itemInfo.getTypes().contains(ItemTypeEnum.WEAPON) || itemInfo.getTypes().contains(ItemTypeEnum.CLOTHES))
                && items.getCurrentStrength() ==0) {
            brokenItemIcon = new ImageView("/graphics/gui/BrokenItem.png");
            brokenItemIcon.setX(25);
            brokenItemIcon.setY(25);
            pane.getChildren().add(brokenItemIcon);
        }

        var cutName = itemInfo.getName().length() > 28 ? itemInfo.getName().substring(0, 25).concat("... ") : itemInfo.getName();
        var nameText = items.getCount() > 1 ?
                (cutName + " (" + items.getCount() + ")") : cutName;
        nameLabel = new Label(nameText);
        nameLabel.setLayoutX(tileSize + 5);
        nameLabel.setLayoutY(10);
        pane.getChildren().add(nameLabel);

        if (ItemTypeEnum.WEAPON.equals(ItemTypeEnum.getItemTypeByCode(selectType))) {
            typeLabel = new Label(((WeaponInfo) itemInfo).getDamage().toString());
            InventoryPanel.getTypeLabel().setText(Game.getText("DAMAGE"));
        } else if (ItemTypeEnum.CLOTHES.equals(ItemTypeEnum.getItemTypeByCode(selectType))) {
            typeLabel = new Label(((ClothesInfo) itemInfo).getArmor().toString());
            InventoryPanel.getTypeLabel().setText(Game.getText("ARMOR"));
        } else {
            typeLabel = new Label(itemInfo.getTypes().get(0).getDesc());
            InventoryPanel.getTypeLabel().setText(Game.getText("TYPE"));
        }
        typeLabel.setLayoutX(InventoryPanel.getTypeLabel().getLayoutX());
        typeLabel.setLayoutY(10);
        pane.getChildren().add(typeLabel);

        weightLabel = new Label(Items.getFormatedItemValue(itemInfo.getWeight()).toString());
        weightLabel.setLayoutX(InventoryPanel.getWeightLabel().getLayoutX());
        weightLabel.setLayoutY(10);
        pane.getChildren().add(weightLabel);

        volumeLabel = new Label(Items.getFormatedItemValue(itemInfo.getVolume()).toString());
        volumeLabel.setLayoutX(InventoryPanel.getVolumeLabel().getLayoutX());
        volumeLabel.setLayoutY(10);
        pane.getChildren().add(volumeLabel);

        priceLabel = new Label(itemInfo.getPrice().toString());
        priceLabel.setLayoutX(InventoryPanel.getPriceLabel().getLayoutX());
        priceLabel.setLayoutY(10);
        pane.getChildren().add(priceLabel);

        pane.setOnMouseEntered(event -> ItemDetailPanel.showDetailPanel(items, pane.getLayoutY()));
        pane.setOnMouseExited(event -> ItemDetailPanel.hideDetailPanel());
        pane.setOnMouseClicked(event -> Game.getMap().getPlayer().useItem(items));
    }
}