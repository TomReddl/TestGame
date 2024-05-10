package view;

import controller.CharactersController;
import controller.ItemsController;
import controller.TimeController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;
import model.editor.TileInfo;
import model.entity.InlayerSizeEnum;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectParams;
import model.entity.map.Items;
import model.entity.character.Character;
import view.inventory.InventoryPanel;

import java.util.List;

/**
 * Панель зачарования/снятия зачарования с предмета
 */
public class EnchantmentPanel {
    @Getter
    private final Pane pane;
    @Getter
    private final Label enchantmentNameLabel; // название зачарователя
    private final Label infoLabel; // подсказка для игрока
    @Getter
    private final ImageView itemImage; // изображение предмета для зачарования
    @Getter
    private final ImageView itemBackgroundImage; // фон для предмета для зачарования
    @Getter
    private final ImageView inlayerImage; // изображение инкрустата
    @Getter
    private final ImageView inlayerBackgroundImage; // фон для инкрустата
    @Getter
    private final Button enchantButton; // кнопка "Зачаровать"
    private final Button closeButton;
    private final TextField nameEdit = new TextField();
    private final ImageView enchantmentImage; // изображение иконки зачарованного предмета

    @Getter
    @Setter
    private Items item = new Items(); // предмет для зачарования
    @Getter
    @Setter
    private Items inlayer = new Items(); // инкрустат

    private final int timeToEnchant = 5; // время зачарования

    private int tableLevel = 0; // уровень стола для зачарования

    public EnchantmentPanel() {
        pane = new Pane();
        pane.setPrefSize(270, 220);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        enchantmentNameLabel = new Label();
        enchantmentNameLabel.setLayoutX(10);
        enchantmentNameLabel.setLayoutY(10);
        enchantmentNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(enchantmentNameLabel);

        infoLabel = new Label();
        infoLabel.setLayoutX(10);
        infoLabel.setLayoutY(30);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        infoLabel.setText(Game.getText("ENCHANTMENT_HINT"));
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(210);
        pane.getChildren().add(infoLabel);

        itemBackgroundImage = new ImageView("/graphics/gui/enchantmentBackground.png");
        itemBackgroundImage.setLayoutX(60);
        itemBackgroundImage.setLayoutY(106);
        itemBackgroundImage.setOnMouseClicked(event -> addItem(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(itemBackgroundImage);

        enchantmentImage = new ImageView("/graphics/gui/Enchantment.png");
        enchantmentImage.setLayoutX(64);
        enchantmentImage.setLayoutY(110);
        enchantmentImage.setVisible(false);
        pane.getChildren().add(enchantmentImage);

        itemImage = new ImageView();
        itemImage.setLayoutX(64);
        itemImage.setLayoutY(110);
        itemImage.setOnMouseClicked(event -> addItem(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(itemImage);

        inlayerBackgroundImage = new ImageView("/graphics/gui/inlayerBackground.png");
        inlayerBackgroundImage.setLayoutX(160);
        inlayerBackgroundImage.setLayoutY(106);
        inlayerBackgroundImage.setOnMouseClicked(event -> addInlayer(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(inlayerBackgroundImage);

        inlayerImage = new ImageView();
        inlayerImage.setLayoutX(164);
        inlayerImage.setLayoutY(110);
        inlayerImage.setOnMouseClicked(event -> addInlayer(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(inlayerImage);

        nameEdit.setLayoutX(40);
        nameEdit.setLayoutY(160);
        nameEdit.setPrefWidth(190);
        nameEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(nameEdit);

        enchantButton = new Button(Game.getText("ENCHANT"));
        enchantButton.setLayoutX(10);
        enchantButton.setLayoutY(190);
        enchantButton.setPrefWidth(120);
        enchantButton.setOnAction(event -> enchantItem());
        enchantButton.setDisable(true);
        pane.getChildren().add(enchantButton);

        closeButton = new Button(Game.getText("CLOSE"));
        closeButton.setLayoutX(140);
        closeButton.setLayoutY(190);
        closeButton.setPrefWidth(120);
        closeButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeButton);

        Game.getRoot().getChildren().add(pane);
    }

    public void showPanel(TileInfo tileInfo) {
        enchantmentNameLabel.setText(tileInfo.getName());
        tableLevel = Integer.parseInt(tileInfo.getParams().get("level"));
        pane.setVisible(true);
    }

    private void closePanel() {
        clearPanel();
        enchantButton.setDisable(true);
        pane.setVisible(false);
        Game.getMap().getPlayersSquad().getSelectedCharacter().setInteractMapPoint(null);
    }

    private void clearPanel() {
        item = new Items();
        itemImage.setImage(null);
        inlayer = new Items();
        inlayerImage.setImage(null);
        nameEdit.setText("");
        tableLevel = 0;
    }

    /**
     * Зачаровать предмет/излечь инкрустат из зачарованного предмета
     */
    private void enchantItem() {
        if (item.getTypeId() != 0 && inlayer.getTypeId() != 0) {
            Character character = Game.getMap().getPlayersSquad().getSelectedCharacter();
            if (item.getInlayerId() == null || item.getInlayerId() == 0) { // зачаровываем
                Integer inlayerSize = (item.getParams() != null && item.getParams().get("inlayerSize") != null) ? InlayerSizeEnum.valueOf(item.getParams().get("inlayerSize")).getSize() : null;
                if (inlayerSize == null) {
                    Game.showMessage(Game.getText("CAN_NOT_ENCAHT")); // нельзя зачаровать предмет, если у него нет слота под инкрустат
                } else if (tableLevel + 1 < inlayerSize) {
                    Game.showMessage(Game.getText("WRONG_TABLE_SIZE")); // если стол не подходит, выводим ошибку
                } else if (inlayerSize >= InlayerSizeEnum.valueOf(inlayer.getParams().get("inlayerSize")).getSize()) {
                    setItemEffectsAndPrice(item, inlayer, character);
                    ItemsController.deleteItem(inlayer, 1, character.getInventory(), character);
                    clearPanel();
                    Game.showMessage(Game.getText("SUCCESS_ENCHANTED"), Color.GREEN);
                    setEnchantButtonDisabled();
                    CharactersController.addSkillExp("ENCHANTMENT", 10*inlayerSize);
                    TimeController.tic(timeToEnchant); // инкрустация занимает время
                } else {
                    Game.showMessage(Game.getText("WRONG_INLAYER_SIZE"));
                }
            } else if (item.getInlayerId() != null) {  // извлекаем инкрустат
                var extractedInlayer = new Items(item.getInlayerId(), 1);
                ItemsController.addItem(extractedInlayer, character.getInventory(), character);
                ItemsController.deleteItem(item, 1, character.getInventory(), character);
                clearPanel();
                Game.showMessage(Game.getText("SUCCESS_EXTRACT"), Color.GREEN);
                setEnchantButtonDisabled();
                int inlayerSize = (extractedInlayer.getParams() != null && extractedInlayer.getParams().get("inlayerSize") != null) ? InlayerSizeEnum.valueOf(extractedInlayer.getParams().get("inlayerSize")).getSize() : 0;
                CharactersController.addSkillExp("ENCHANTMENT", 5*inlayerSize);
                TimeController.tic(timeToEnchant);
            }
        }
    }

    /**
     * Установливает силу зачарования и цену зачарованного предмета в зависимости от навыка ENCHANTMENT
     * @param item    - предмет, который зачаровываем
     * @param inlayer - инкрустат
     * @param character  - персонаж, который зачаровывает
     */
    private void setItemEffectsAndPrice(Items item, Items inlayer, Character character) {
        int enchantmentSkill = character.getParams().getSkills().get("ENCHANTMENT").getCurrentValue();
        List<EffectParams> effects = inlayer.getEffects();
        for (EffectParams effect : effects) {
            Integer power = effect.getPower();
            if (power != null && power != 0) {
                effect.setPower(power / 2 + power * (enchantmentSkill / 50));
            }
        }
        item.setPrice((item.getPrice() + inlayer.getPrice()) * (1 + (enchantmentSkill / 100))); // цена предмета меняется при зачаровании
        item.setEffects(effects);
        item.setInlayerId(inlayer.getTypeId());
        item.setName(nameEdit.getText());
    }

    private void addItem(boolean isRightMouse) {
        if (isRightMouse) { // правой кнопкой стираем предмет
            item = new Items();
            itemImage.setImage(null);
            setEnchantButtonDisabled();
            nameEdit.setText("");
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.WEARABLE.getDesc(), InventoryPanel.ShowModeEnum.SELECT_ITEM_FOR_ENCHANT);
        }
    }

    private void addInlayer(boolean isRightMouse) {
        if (isRightMouse) { // правой кнопкой стираем предмет
            inlayer = new Items();
            inlayerImage.setImage(null);
            enchantButton.setDisable(true);
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.ENCHANTMENT.getDesc(), InventoryPanel.ShowModeEnum.SELECT_INLAYER_FOR_ENCHANT);
        }
    }

    /**
     * Установить доступность кнопки "Зачаровать"
     */
    public void setEnchantButtonDisabled() {
        enchantButton.setDisable(item.getTypeId() == 0 || inlayer.getTypeId() == 0 || inlayer.getEffects() == null);
        if (item.getTypeId() != 0) {
            nameEdit.setText(item.getTypeId() == 0 ? "" : item.getName());
            if (item.getInlayerId() != null && item.getInlayerId() != 0) {
                enchantButton.setText(Game.getText("EXTRACT_INLAYER"));
                enchantmentImage.setVisible(true);
                infoLabel.setText(Game.getText("DISENCHANTMENT_HINT"));
            } else {
                enchantButton.setText(Game.getText("ENCHANT"));
                enchantmentImage.setVisible(false);
                infoLabel.setText(Game.getText("ENCHANTMENT_HINT"));
            }
        }
    }
}
