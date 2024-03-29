package view.inventory;

import controller.EffectsController;
import controller.ItemsController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesInfo;
import model.editor.items.ItemInfo;
import model.editor.items.WeaponInfo;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectInfo;
import model.entity.map.Items;
import view.Game;

import static game.GameParams.tileSize;

/**
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
    private ImageView enchantmentImage;

    public ItemRecord(String bodyPart) {
        pane = new Pane();
        pane.setPrefSize(550, 40);
        pane.setBackground(new Background(new BackgroundFill((Color.WHITESMOKE), CornerRadii.EMPTY, Insets.EMPTY)));

        nameLabel = new Label(String.format(Game.getText("EMPTY_BODY_SLOT"), BodyPartEnum.valueOf(bodyPart).getDesc()));
        nameLabel.setTextFill(Color.web("#6c6c6c"));
        nameLabel.setLayoutX(tileSize + 5);
        nameLabel.setLayoutY(10);
        pane.getChildren().add(nameLabel);
    }

    public ItemRecord(Items items, String selectType, InventoryPanel inventoryPanel) {
        ItemInfo itemInfo = items.getInfo();
        pane = new Pane();
        pane.setPrefSize(550, 40);
        pane.setBackground(new Background(new BackgroundFill(
                ((items.isEquipment()) ? Color.GAINSBORO : Color.WHITESMOKE), CornerRadii.EMPTY, Insets.EMPTY)));

        if (items.getInfo().getTypes().contains(ItemTypeEnum.POTION) && items.getEffects() != null && !items.getEffects().isEmpty()) {
            EffectInfo effectInfo = EffectsController.getEffects().get(items.getEffects().get(0).getStrId());
            Image image = EffectsController.getPotionImages().get(effectInfo.getPotionColor());
            if (image != null) {
                icon = new ImageView(image);
            }
        }
        if (icon == null) {
            icon = new ImageView(itemInfo.getIcon().getImage());
        }

        pane.getChildren().add(icon);

        if ((itemInfo.getTypes().contains(ItemTypeEnum.WEAPON) || itemInfo.getTypes().contains(ItemTypeEnum.CLOTHES))
                && items.getCurrentStrength() ==0) {
            brokenItemIcon = new ImageView("/graphics/gui/BrokenItem.png");
            brokenItemIcon.setX(25);
            brokenItemIcon.setY(25);
            pane.getChildren().add(brokenItemIcon);
        }

        if (items.getInlayerId() != null && items.getInlayerId() != 0) {
            enchantmentImage = new ImageView("/graphics/gui/Enchantment.png");
            pane.getChildren().add(enchantmentImage);
        }

        var itemName = items.getName();
        var cutName = itemName.length() > 25 ? itemName.substring(0, 22).concat("... ") : itemName;
        var nameText = items.getCount() > 1 ?
                (cutName + " (" + items.getCount() + ")") : cutName;
        nameLabel = new Label(nameText);
        nameLabel.setLayoutX(tileSize + 5);
        nameLabel.setLayoutY(10);
        pane.getChildren().add(nameLabel);

        if (ItemTypeEnum.WEAPON.equals(ItemTypeEnum.getItemTypeByCode(selectType))) {
            typeLabel = new Label(((WeaponInfo) itemInfo).getDamage().toString());
            inventoryPanel.getTypeLabel().setText(Game.getText("DAMAGE_INVENTORY"));
        } else if (ItemTypeEnum.CLOTHES.equals(ItemTypeEnum.getItemTypeByCode(selectType))) {
            typeLabel = new Label(((ClothesInfo) itemInfo).getArmor().toString());
            inventoryPanel.getTypeLabel().setText(Game.getText("ARMOR_INVENTORY"));
        } else {
            typeLabel = new Label(itemInfo.getTypes().get(0).getDesc());
            inventoryPanel.getTypeLabel().setText(Game.getText("TYPE"));
        }
        typeLabel.setLayoutX(inventoryPanel.getTypeLabel().getLayoutX());
        typeLabel.setLayoutY(10);
        pane.getChildren().add(typeLabel);

        Long weight = itemInfo.getWeight();
        if (items.getInfo().getParams() != null &&
                items.getInfo().getParams().get("currentCapacity") != null) {
            // нужно учитывать вес содержимого предметов
            weight += Integer.parseInt(items.getParams().get("currentCapacity"));
        }
        weightLabel = new Label(Items.getFormatedItemValue(weight).toString());
        weightLabel.setLayoutX(inventoryPanel.getWeightLabel().getLayoutX());
        weightLabel.setLayoutY(10);
        pane.getChildren().add(weightLabel);

        volumeLabel = new Label(Items.getFormatedItemValue(itemInfo.getVolume()).toString());
        volumeLabel.setLayoutX(inventoryPanel.getVolumeLabel().getLayoutX());
        volumeLabel.setLayoutY(10);
        pane.getChildren().add(volumeLabel);

        priceLabel = new Label(items.getPrice().toString());
        priceLabel.setLayoutX(inventoryPanel.getPriceLabel().getLayoutX());
        priceLabel.setLayoutY(10);
        pane.getChildren().add(priceLabel);

        pane.setOnMouseEntered(event -> ItemDetailPanel.showDetailPanel(items, pane.getLayoutY()));
        pane.setOnMouseExited(event -> ItemDetailPanel.hideDetailPanel());
        pane.setOnMouseClicked(event -> ItemsController.clickItem(items, Game.getMap().getPlayer()));
    }
}
