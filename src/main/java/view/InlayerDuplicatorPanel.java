package view;

import controller.CharactersController;
import controller.ItemsController;
import controller.TimeController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import model.editor.TileTypeEnum;
import model.entity.InlayerSizeEnum;
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import model.entity.player.Player;
import view.inventory.InventoryPanel;

import static controller.ItemsController.inlayerBlankId;

/**
 * Панель дубликатора инкрустатов. Позволяет создать копию инкрустата (не выше большого размера) при наличии болванки инкрустата
 */
public class InlayerDuplicatorPanel {
    @Getter
    private final Pane pane;
    @Getter
    private final Label nameLabel; // название дубликатора
    private final Label infoLabel; // подсказка для игрока
    @Getter
    private final ImageView inlayerImage; // изображение выбранного инкрустата
    @Getter
    private final Label inlayerCountLabel; // количество инкрустатов
    @Getter
    private final ImageView inlayerBackgroundImage; // фон для инкрустата
    @Getter
    private final ImageView blankImage; // изображение расходника (болванки)
    @Getter
    private final Label blankCountLabel; // количество болванок
    @Getter
    private final ImageView blankBackgroundImage; // фон для расходника (болванки)
    @Getter
    private final Button duplicateButton; // кнопка "Дублировать"
    private final Button closeButton;

    @Getter
    @Setter
    private Items selectedInlayer = new Items(); // выбранный инкрустат
    @Getter
    @Setter
    private Items blank = new Items(); // болванка

    private final int timeToDuplicate = 15; // время дублирования инкрустата

    public InlayerDuplicatorPanel() {
        pane = new Pane();
        pane.setPrefSize(230, 210);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        nameLabel = new Label();
        nameLabel.setLayoutX(10);
        nameLabel.setLayoutY(10);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nameLabel.setText(Editor.getTiles2().get(251).getName());
        pane.getChildren().add(nameLabel);

        infoLabel = new Label();
        infoLabel.setLayoutX(10);
        infoLabel.setLayoutY(30);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        infoLabel.setText(Game.getText("INLAYER_DUPLICATOR_HINT"));
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(210);
        pane.getChildren().add(infoLabel);

        inlayerBackgroundImage = new ImageView("/graphics/gui/inlayerBackground.png");
        inlayerBackgroundImage.setLayoutX(40);
        inlayerBackgroundImage.setLayoutY(106);
        inlayerBackgroundImage.setOnMouseClicked(event -> addInlayer(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(inlayerBackgroundImage);

        inlayerImage = new ImageView();
        inlayerImage.setLayoutX(44);
        inlayerImage.setLayoutY(110);
        inlayerImage.setOnMouseClicked(event -> addInlayer(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(inlayerImage);

        inlayerCountLabel = new Label();
        inlayerCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        inlayerCountLabel.setLayoutX(45);
        inlayerCountLabel.setLayoutY(137);
        pane.getChildren().add(inlayerCountLabel);

        blankBackgroundImage = new ImageView("/graphics/gui/inlayerBackground.png");
        blankBackgroundImage.setLayoutX(90);
        blankBackgroundImage.setLayoutY(106);
        pane.getChildren().add(blankBackgroundImage);

        blankImage = new ImageView();
        blankImage.setLayoutX(94);
        blankImage.setLayoutY(110);
        pane.getChildren().add(blankImage);

        blankCountLabel = new Label();
        blankCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        blankCountLabel.setLayoutX(95);
        blankCountLabel.setLayoutY(137);
        pane.getChildren().add(blankCountLabel);

        duplicateButton = new Button(Game.getText("DUPLICATE"));
        duplicateButton.setLayoutX(10);
        duplicateButton.setLayoutY(180);
        duplicateButton.setPrefWidth(100);
        duplicateButton.setOnAction(event -> duplicate());
        duplicateButton.setDisable(true);
        pane.getChildren().add(duplicateButton);

        closeButton = new Button(Game.getText("CLOSE"));
        closeButton.setLayoutX(120);
        closeButton.setLayoutY(180);
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeButton);

        Game.getRoot().getChildren().add(pane);
    }

    private void closePanel() {
        selectedInlayer = new Items();
        inlayerImage.setImage(null);
        duplicateButton.setDisable(true);
        pane.setVisible(false);
        inlayerCountLabel.setText("");
        blankCountLabel.setText("");
        Game.getMap().getPlayer().setInteractMapPoint(null);
    }

    /**
     * Дублировать инкрустат
     */
    private void duplicate() {
        if (selectedInlayer.getTypeId() != 0 && blank.getTypeId() != 0) {
            if (selectedInlayer.getParams().get("inlayerSize").equals(InlayerSizeEnum.GREAT.name())) {
                Game.showMessage(Game.getText("DUPLICATE_GREAT_IMPOSSIBLE")); // великие инкрустаты нельзя дублировать
            } else {
                Player player = Game.getMap().getPlayer();
                TimeController.tic(timeToDuplicate);
                // проверяем, на месте ли наш стол, а то за время дублирования он мог куда-то деться
                String tileType = player.getInteractMapPoint().getTile2Info().getType();
                if (tileType == null || !TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.INLAYER_DUPLICATOR)) {
                    Game.showMessage(player.getInteractMapPoint().getTile2Info().getName() + Game.getText("DESTROYED"));
                    closePanel();
                } else {
                    Items doubleInlayer = new Items(inlayerBlankId, 1);
                    doubleInlayer.setPrice(selectedInlayer.getPrice());
                    doubleInlayer.setName(selectedInlayer.getName());
                    doubleInlayer.setEffects(selectedInlayer.getEffects());
                    ItemsController.addItem(doubleInlayer, player.getInventory(), player);
                    inlayerCountLabel.setText(String.valueOf(selectedInlayer.getCount()));
                    ItemsController.deleteItem(blank, 1, player.getInventory(), player);
                    if (blank.getCount() <= 0) {
                        blank = new Items();
                        blankImage.setImage(null);
                        blankCountLabel.setText("");
                        setDuplicateButtonDisabled();
                    } else {
                        blankCountLabel.setText(String.valueOf(blank.getCount()));
                    }
                    CharactersController.addSkillExp("ENCHANTMENT", 5);
                }
            }
        } else {
            Game.showMessage(Game.getText("DUPLICATE_IMPOSSIBLE"));
        }
    }

    private void addInlayer(boolean isRightMouse) {
        if (isRightMouse) { // правой кнопкой стираем предмет
            selectedInlayer = new Items();
            inlayerImage.setImage(null);
            duplicateButton.setDisable(true);
            inlayerCountLabel.setText("");
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.ENCHANTMENT.getDesc(), InventoryPanel.ShowModeEnum.SELECT_INLAYER_FOR_DUPLICATOR);
        }
    }

    public void showPanel() {
        pane.setVisible(true);
        blank = ItemsController.findItemInInventory(inlayerBlankId, Game.getMap().getPlayer().getInventory());
        if (blank != null) {
            blankImage.setImage(blank.getInfo().getIcon().getImage());
            blankCountLabel.setText(String.valueOf(blank.getCount()));
        }
        setDuplicateButtonDisabled();
    }

    /**
     * Установить доступность кнопки "Дублировать"
     */
    public void setDuplicateButtonDisabled() {
        duplicateButton.setDisable(blank == null || selectedInlayer.getTypeId() == 0);
    }
}
