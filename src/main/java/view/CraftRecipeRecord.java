package view;

import controller.ItemsController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import model.editor.items.RecipeInfo;


/**
 * Строка с информацией о рецепте создания предмета
 */
public class CraftRecipeRecord {
    @Getter
    private Pane pane;
    private Label itemName; // название предмета
    private ImageView icon; // иконка создаваемого предмета

    public CraftRecipeRecord(RecipeInfo recipeInfo) {
        pane = new Pane();
        pane.setPrefSize(550, 40);
        pane.setBackground(new Background(new BackgroundFill((Color.WHITESMOKE), CornerRadii.EMPTY, Insets.EMPTY)));

        icon = new ImageView(Editor.getItems().get(recipeInfo.getItemId()).getImage().getImage());
        icon.setLayoutX(10);
        icon.setLayoutY(0);
        pane.getChildren().add(icon);

        itemName = new Label(Editor.getItems().get(recipeInfo.getItemId()).getName());
        itemName.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        itemName.setLayoutX(60);
        itemName.setLayoutY(15);
        itemName.setTextFill(ItemsController.getCraftElements(recipeInfo, Game.getMap().getPlayer()) != null ? Color.BLACK : Color.web("#A9A9A9"));
        pane.getChildren().add(itemName);

        pane.setOnMouseClicked(event -> ItemsController.craftItem(recipeInfo, Game.getMap().getPlayer()));
    }
}
