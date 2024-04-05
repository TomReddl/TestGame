package view;

import controller.CharactersController;
import controller.EffectsController;
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
import model.entity.player.Character;
import view.inventory.InventoryPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Панель алхимической лаборатории. Позволяет исследовать первый эффект ингредиента
 */
public class AlchemyLaboratoryPanel {
    @Getter
    private final Pane pane;
    @Getter
    private final Label laboratoryNameLabel; // название лаборатории
    private final Label infoLabel; // подсказка для игрока
    @Getter
    private final ImageView ingredientImage; // изображение выбранного ингредиента
    @Getter
    private final ImageView backgroundImage; // фон для выбора ингредиента
    @Getter
    private final Button exploreButton; // кнопка "Исследовать"
    private final Button closeButton;

    @Getter
    @Setter
    private Items selectedIngredient = new Items(); // выбранный для исследования ингредиент

    private final int timeToExplore = 30; // время исследования ингредиента

    public AlchemyLaboratoryPanel() {
        pane = new Pane();
        pane.setPrefSize(230, 180);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        laboratoryNameLabel = new Label();
        laboratoryNameLabel.setLayoutX(10);
        laboratoryNameLabel.setLayoutY(10);
        laboratoryNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        laboratoryNameLabel.setText(Editor.getTiles2().get(250).getName());
        pane.getChildren().add(laboratoryNameLabel);

        infoLabel = new Label();
        infoLabel.setLayoutX(10);
        infoLabel.setLayoutY(30);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        infoLabel.setText(Game.getText("ALCHEMY_LABORATORY_HINT"));
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(210);
        pane.getChildren().add(infoLabel);

        backgroundImage = new ImageView("/graphics/gui/ingredientBackground.png");
        backgroundImage.setLayoutX(90);
        backgroundImage.setLayoutY(85);
        backgroundImage.setOnMouseClicked(event -> addIngredient(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(backgroundImage);

        ingredientImage = new ImageView();
        ingredientImage.setLayoutX(94);
        ingredientImage.setLayoutY(89);
        ingredientImage.setOnMouseClicked(event -> addIngredient(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(ingredientImage);

        exploreButton = new Button(Game.getText("EXPLORE_INGREDIENT"));
        exploreButton.setLayoutX(10);
        exploreButton.setLayoutY(140);
        exploreButton.setPrefWidth(100);
        exploreButton.setOnAction(event -> exploreEffect());
        exploreButton.setDisable(true);
        pane.getChildren().add(exploreButton);

        closeButton = new Button(Game.getText("CLOSE"));
        closeButton.setLayoutX(120);
        closeButton.setLayoutY(140);
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeButton);

        Game.getRoot().getChildren().add(pane);
    }

    private void closePanel() {
        selectedIngredient = new Items();
        ingredientImage.setImage(null);
        exploreButton.setDisable(true);
        pane.setVisible(false);
        Game.getMap().getSelecterCharacter().setInteractMapPoint(null);
    }

    /**
     * Исследовать ингредиент.
     * Если персонаж не знает ни одного эффекта выбранного ингредиента, он открывает для себя первый эффект,
     * при этом сам ингредиент уничтожается
     */
    private void exploreEffect() {
        if (selectedIngredient.getTypeId() != 0) {
            List<String> effects = Game.getMap().getSelecterCharacter().getKnowledgeInfo().getKnowEffects().get(selectedIngredient.getTypeId());
            if (effects == null) {
                Character character = Game.getMap().getSelecterCharacter();
                TimeController.tic(timeToExplore);
                // проверяем, на месте ли наш стол, а то за время исследования он мог сгореть
                String tileType = character.getInteractMapPoint().getTile2Info().getType();
                if (tileType == null || !TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.ALCHEMY_LABORATORY)) {
                    Game.showMessage(character.getInteractMapPoint().getTile2Info().getName() + Game.getText("DESTROYED"));
                    closePanel();
                } else {
                    effects = new ArrayList<>();
                    String newEffect = selectedIngredient.getEffects().get(0).getStrId();
                    effects.add(newEffect);
                    Game.getMap().getSelecterCharacter().getKnowledgeInfo().getKnowEffects().put(selectedIngredient.getTypeId(), effects);
                    Game.showMessage(Game.getText("NEW_EFFECT_EXPLORED") + ": " +
                            EffectsController.getEffects().get(newEffect).getName(), Color.GREEN);

                    ItemsController.deleteItem(selectedIngredient, 1, character.getInventory(), character);
                    selectedIngredient = new Items();
                    ingredientImage.setImage(null);
                    exploreButton.setDisable(true);
                    CharactersController.addSkillExp("POTIONS", 20);
                }
            } else {
                Game.showMessage(Game.getText("NOT_EXPLODED_EFFECTS"));
            }
        }
    }

    private void addIngredient(boolean isRightMouse) {
        if (isRightMouse) { // правой кнопкой стираем предмет
            selectedIngredient = new Items();
            ingredientImage.setImage(null);
            exploreButton.setDisable(true);
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.INGREDIENT.getDesc(), InventoryPanel.ShowModeEnum.SELECT_FOR_POTION_EXPLORE);
        }
    }

    public void showPanel() {
        pane.setVisible(true);
    }
}
