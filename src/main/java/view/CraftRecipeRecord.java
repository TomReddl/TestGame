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
import model.editor.items.ItemInfo;
import model.editor.items.RecipeInfo;
import model.entity.map.Items;

/**
 * Строка с информацией о рецепте создания предмета
 */
public class CraftRecipeRecord {
    @Getter
    private final Pane pane;
    private final Label itemName; // название предмета
    private final ImageView icon; // иконка создаваемого предмета

    public CraftRecipeRecord(RecipeInfo recipeInfo) {
        pane = new Pane();
        pane.setPrefSize(550, 40);
        pane.setBackground(new Background(new BackgroundFill((Color.WHITESMOKE), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setOnMouseEntered(event -> showRequiredItemsInfo(recipeInfo));
        pane.setOnMouseExited(event -> hideRequiredItemsInfo());

        icon = new ImageView(Editor.getItems().get(recipeInfo.getItemId()).getImage().getImage());
        icon.setLayoutX(10);
        icon.setLayoutY(0);
        pane.getChildren().add(icon);

        itemName = new Label(Editor.getItems().get(recipeInfo.getItemId()).getName() +
                (recipeInfo.getItemCount() != null ? " (" + recipeInfo.getItemCount() + ")" : ""));
        itemName.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        itemName.setLayoutX(60);
        itemName.setLayoutY(15);
        itemName.setTextFill(ItemsController.getCraftElements(recipeInfo, Game.getMap().getPlayersSquad().getSelectedCharacter()) != null ? Color.BLACK : Color.web("#A9A9A9"));
        pane.getChildren().add(itemName);

        pane.setOnMouseClicked(event -> ItemsController.craftItem(recipeInfo, Game.getMap().getPlayersSquad().getSelectedCharacter()));
    }

    /**
     * Показать информацию о требуемых для крафта ингредиентах
     * @param recipeInfo - рецепт крафта
     */
    private void showRequiredItemsInfo(RecipeInfo recipeInfo) {
        String text = Game.getText("REQUIRED") + ": ";
        for (String itemTypeId : recipeInfo.getElements().keySet()) {
            ItemInfo element = Editor.getItems().get(Integer.parseInt(itemTypeId));
            Items findElement = ItemsController.findItemInInventory(Integer.parseInt(itemTypeId), Game.getMap().getPlayersSquad().getSelectedCharacter().getInventory());
            String elementCount = findElement != null ? " (" + Game.getText("IN_STOCK").toLowerCase() + ": " + findElement.getCount() + ")" : "";
            text = text.concat("\n" + element.getName() + ": " + recipeInfo.getElements().get(itemTypeId) + elementCount);
        }
        Game.getEditor().getCraftPanel().getRequiredItemsLabel().setText(text);
    }

    private void hideRequiredItemsInfo() {
        Game.getEditor().getCraftPanel().getRequiredItemsLabel().setText("");
    }
}
