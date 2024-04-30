package view;

import controller.ItemsController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import model.editor.TileInfo;
import model.editor.items.RecipeInfo;

import java.util.Comparator;
import java.util.stream.Collectors;

import static game.GameParams.tileSize;

/**
 * Панель крафта. Применяется для множества тайлов (наковальни, плавильни, столярные мастерские, ткацкие станки и т.д.)
 */
public class CraftPanel {
    @Getter
    private final Pane pane;
    private final ScrollPane scrollPane;
    private final Pane innerPane;
    @Getter
    private final Label nameLabel; // название стола
    @Getter
    private final Label requiredItemsLabel; // необходимые предметы
    private final TextField itemNameEdit; // поле для поиска предмета по названию
    private final Button closeButton;
    private final CheckBox showOnlyAvailable = new CheckBox(); // чекбокс "Показывать только доступные рецепты"

    public CraftPanel() {
        pane = new Pane();
        pane.setPrefSize(550, 310);
        pane.setLayoutX(30);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        innerPane = new Pane();
        innerPane.setPrefSize(290, 200);
        innerPane.setLayoutX(150);
        innerPane.setLayoutY(150);
        innerPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(60);
        scrollPane.setPrefSize(310, 210);
        scrollPane.setMaxHeight(210);
        scrollPane.setMinWidth(310);
        scrollPane.setStyle("-fx-border-color:black;");
        scrollPane.setContent(innerPane);

        pane.getChildren().add(scrollPane);

        nameLabel = new Label();
        nameLabel.setLayoutX(10);
        nameLabel.setLayoutY(10);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(nameLabel);

        requiredItemsLabel = new Label();
        requiredItemsLabel.setLayoutX(330);
        requiredItemsLabel.setLayoutY(60);
        requiredItemsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(requiredItemsLabel);

        itemNameEdit = new TextField();
        itemNameEdit.setLayoutX(10);
        itemNameEdit.setLayoutY(30);
        itemNameEdit.setPromptText(Game.getText("ITEM_NAME"));
        pane.getChildren().add(itemNameEdit);

        showOnlyAvailable.setLayoutX(170);
        showOnlyAvailable.setLayoutY(35);
        showOnlyAvailable.setText(Game.getText("ONLY_AVAILABLE"));
        showOnlyAvailable.setOnAction(event -> showPanel(Game.getMap().getPlayersSquad().getSelectedCharacter().getInteractMapPoint().getTile2Info()));
        pane.getChildren().add(showOnlyAvailable);

        closeButton = new Button(Game.getText("CLOSE"));
        closeButton.setLayoutX(10);
        closeButton.setLayoutY(280);
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeButton);

        Game.getRoot().getChildren().add(pane);
    }

    /**
     * Закрыть панель крафта
     */
    private void closePanel() {
        pane.setVisible(false);
        Game.getMap().getPlayersSquad().getSelectedCharacter().setInteractMapPoint(null);
    }

    /**
     * Отобразить панель и доступные персонажу рецепты
     */
    public void showPanel(TileInfo tileInfo) {
        if (!pane.isVisible()) {
            itemNameEdit.setText("");
            nameLabel.setText(tileInfo.getName());
            pane.setVisible(true);
        }
        if (innerPane.getChildren().size() > 0) {
            innerPane.getChildren().remove(0, innerPane.getChildren().size());
        }
        int i = 0;
        for (RecipeInfo recipeInfo : Editor.getRecipes().stream().filter(
                showOnlyAvailable.isSelected() ?
                        recipe -> isRecipeVisibly(tileInfo, recipe) && (ItemsController.getCraftElements(recipe, Game.getMap().getPlayersSquad().getSelectedCharacter()) != null) :
                        recipe -> isRecipeVisibly(tileInfo, recipe)).
                sorted(Comparator.comparing(o -> ItemsController.getCraftElements(o, Game.getMap().getPlayersSquad().getSelectedCharacter()) == null))
                .collect(Collectors.toList())) {
            var recipeRecord = new CraftRecipeRecord(recipeInfo);
            recipeRecord.getPane().setLayoutY(i++ * tileSize);
            innerPane.getChildren().add(recipeRecord.getPane());
        }
        innerPane.setMinHeight(40 * (i + 1));
    }

    /**
     * Виден ли рецепт в списке рецептов для крафта
     *
     * @param tileInfo   - тайл со столом для крафта
     * @param recipeInfo - рецепт
     * @return - true, если рецепт доступен на данном столе
     */
    private boolean isRecipeVisibly(TileInfo tileInfo, RecipeInfo recipeInfo) {
        return (Game.getMap().getPlayersSquad().getSelectedCharacter().getKnowledgeRecipes().contains(recipeInfo.getRecipeId()) ||   // персонаж знает рецепт
                (recipeInfo.getGroup() != null && recipeInfo.getGroup().equals("well-known"))) &&       // или это общеизвестный рецепт
                recipeInfo.getCraftingPlace().name().equals(tileInfo.getParams().get("subtype")) &&     // тип стола подходит для рецепта
                recipeInfo.getLevel() <= Integer.parseInt(tileInfo.getParams().get("level"));           // уровень стола подходит для рецепта
    }
}
