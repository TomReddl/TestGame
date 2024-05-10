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
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import model.entity.character.Character;
import view.inventory.InventoryPanel;

import static controller.ItemsController.biomassId;

/**
 * Панель дубликатора ингредиентов. Позволяет создать копию ингредиента
 */
public class DuplicatorPanel {
    @Getter
    private final Pane pane;
    @Getter
    private final Label nameLabel; // название дубликатора
    private final Label infoLabel; // подсказка для игрока
    @Getter
    private final ImageView ingredientImage; // изображение выбранного ингредиента
    @Getter
    private final Label ingredientCountLabel; // количество ингредиентов
    @Getter
    private final ImageView ingredientBackgroundImage; // фон для ингредиента
    @Getter
    private final ImageView biomassImage; // изображение расходника (биомассы)
    @Getter
    private final Label biomassCountLabel; // количество биомассы
    @Getter
    private final ImageView biomassBackgroundImage; // фон для расходника (биомассы)
    @Getter
    private final Button duplicateButton; // кнопка "Дублировать"
    private final Button closeButton;

    @Getter
    @Setter
    private Items selectedIngredient = new Items(); // выбранный ингредиент
    @Getter
    @Setter
    private Items biomass = new Items(); // биомасса

    private final int timeToDuplicate = 30; // время дублирования ингредиента

    public DuplicatorPanel() {
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
        infoLabel.setText(Game.getText("DUPLICATOR_HINT"));
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(210);
        pane.getChildren().add(infoLabel);

        ingredientBackgroundImage = new ImageView("/graphics/gui/ingredientBackground.png");
        ingredientBackgroundImage.setLayoutX(40);
        ingredientBackgroundImage.setLayoutY(106);
        ingredientBackgroundImage.setOnMouseClicked(event -> addIngredient(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(ingredientBackgroundImage);

        ingredientImage = new ImageView();
        ingredientImage.setLayoutX(44);
        ingredientImage.setLayoutY(110);
        ingredientImage.setOnMouseClicked(event -> addIngredient(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(ingredientImage);

        ingredientCountLabel = new Label();
        ingredientCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ingredientCountLabel.setLayoutX(45);
        ingredientCountLabel.setLayoutY(137);
        pane.getChildren().add(ingredientCountLabel);

        biomassBackgroundImage = new ImageView("/graphics/gui/ingredientBackground.png");
        biomassBackgroundImage.setLayoutX(90);
        biomassBackgroundImage.setLayoutY(106);
        pane.getChildren().add(biomassBackgroundImage);

        biomassImage = new ImageView();
        biomassImage.setLayoutX(94);
        biomassImage.setLayoutY(110);
        pane.getChildren().add(biomassImage);

        biomassCountLabel = new Label();
        biomassCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        biomassCountLabel.setLayoutX(95);
        biomassCountLabel.setLayoutY(137);
        pane.getChildren().add(biomassCountLabel);

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
        selectedIngredient = new Items();
        ingredientImage.setImage(null);
        duplicateButton.setDisable(true);
        pane.setVisible(false);
        ingredientCountLabel.setText("");
        biomassCountLabel.setText("");
        Game.getMap().getPlayersSquad().getSelectedCharacter().setInteractMapPoint(null);
    }

    /**
     * Дублировать ингредиент
     */
    private void duplicate() {
        if (selectedIngredient.getTypeId() != 0 && biomass.getTypeId() != 0) {
            Character character = Game.getMap().getPlayersSquad().getSelectedCharacter();
            TimeController.tic(timeToDuplicate);
            // проверяем, на месте ли наш стол, а то за время дублирования он мог куда-то деться
            String tileType = character.getInteractMapPoint().getTile2Info().getType();
            if (tileType == null || !TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.DUPLICATOR)) {
                Game.showMessage(character.getInteractMapPoint().getTile2Info().getName() + Game.getText("DESTROYED"));
                closePanel();
            } else {
                ItemsController.addItem(selectedIngredient, 1, character.getInventory(), character);
                ingredientCountLabel.setText(String.valueOf(selectedIngredient.getCount()));
                ItemsController.deleteItem(biomass, 1, character.getInventory(), character);
                if (biomass.getCount() <= 0) {
                    biomass = new Items();
                    biomassImage.setImage(null);
                    biomassCountLabel.setText("");
                    setDuplicateButtonDisabled();
                } else {
                    biomassCountLabel.setText(String.valueOf(biomass.getCount()));
                }
                CharactersController.addSkillExp("POTIONS", 5);
            }
        } else {
            Game.showMessage(Game.getText("DUPLICATE_IMPOSSIBLE"));
        }
    }

    private void addIngredient(boolean isRightMouse) {
        if (isRightMouse) { // правой кнопкой стираем предмет
            selectedIngredient = new Items();
            ingredientImage.setImage(null);
            duplicateButton.setDisable(true);
            ingredientCountLabel.setText("");
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.INGREDIENT.getDesc(), InventoryPanel.ShowModeEnum.SELECT_FOR_DUPLICATOR);
        }
    }

    public void showPanel() {
        pane.setVisible(true);
        biomass = ItemsController.findItemInInventory(biomassId, Game.getMap().getPlayersSquad().getSelectedCharacter().getInventory());
        if (biomass != null) {
            biomassImage.setImage(biomass.getInfo().getIcon().getImage());
            biomassCountLabel.setText(String.valueOf(biomass.getCount()));
        }
        setDuplicateButtonDisabled();
    }

    /**
     * Установить доступность кнопки "Дублировать"
     */
    public void setDuplicateButtonDisabled() {
        duplicateButton.setDisable(biomass == null || selectedIngredient.getTypeId() == 0);
    }
}
