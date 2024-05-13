package view.inventory;

import controller.ItemsController;
import controller.MoneyController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import lombok.Getter;
import model.entity.map.Items;
import view.Game;

import static game.GameParams.headerSize;
import static game.GameParams.screenSizeX;

/**
 * Панель выбора количества предметов
 */
public class ItemCountPanel {
    @Getter
    private static final Pane pane;
    private static final Slider slider;
    private static final Button selectButton;
    @Getter
    private static final Button backButton;
    private static final Label selectCountLabel;
    private static ItemsController.ItemActionType currentAction;
    private static Items currentItem;

    static {
        pane = new Pane();
        pane.setPrefSize(200, 100);
        pane.setLayoutX(765);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:gray;");
        pane.setVisible(false);
        Game.getRoot().getChildren().add(pane);

        slider = new Slider();
        slider.setLayoutY(10);
        slider.setLayoutX(10);
        slider.setMajorTickUnit(1.0);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setMin(1);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                changeValue(newVal));
        pane.getChildren().add(slider);

        selectCountLabel = new Label();
        selectCountLabel.setLayoutY(10);
        selectCountLabel.setLayoutX(150);
        pane.getChildren().add(selectCountLabel);

        selectButton = new Button(Game.getText("OK"));
        selectButton.setLayoutX(30);
        selectButton.setLayoutY(50);
        selectButton.setOnAction(event -> selectCount());
        pane.getChildren().add(selectButton);

        backButton = new Button(Game.getText("BACK"));
        backButton.setLayoutY(50);
        backButton.setLayoutX(70);
        backButton.setOnAction(event -> hide());
        pane.getChildren().add(backButton);
    }

    /**
     * Показать панель выбора количества предметов
     *
     * @param action - действие с предметом
     * @param item   - предмет
     */
    public static void show(ItemsController.ItemActionType action, Items item) {
        Robot robot = new Robot();
        double x = robot.getMousePosition().getX() - Game.getStage().getX();
        if (x + pane.getWidth() > screenSizeX) {
            x = screenSizeX - pane.getWidth();
        }
        pane.setLayoutX(x);
        pane.setLayoutY(robot.getMousePosition().getY() - Game.getStage().getY() - headerSize);
        currentItem = item;
        currentAction = action;
        slider.setMax(item.getCount());
        slider.setValue(1);
        pane.setVisible(true);
    }

    public static void selectCount() {
        hide();
        if (currentAction.equals(ItemsController.ItemActionType.TO_CONTAINER)) {
            ItemsController.addItemsToContainerFromPlayer(currentItem, (int) slider.getValue(), Game.getContainerInventory().getItems());
        } else if (currentAction.equals(ItemsController.ItemActionType.TO_PLAYER)) {
            ItemsController.addItemsToPlayerFromContainer(currentItem, (int) slider.getValue(), Game.getContainerInventory().getItems());
        } else if (currentAction.equals(ItemsController.ItemActionType.DROP)) {
            ItemDetailPanel.setSelectItem(currentItem);
            ItemsController.dropItems(Game.getMap().getPlayersSquad().getSelectedCharacter(), (int) slider.getValue());
        } else if (currentAction.equals(ItemsController.ItemActionType.BUY)) {
            MoneyController.buyItems(Game.getMap().getPlayersSquad().getSelectedCharacter(), Game.getMap().getCharacterList().get(Game.getContainerInventory().getCharacterId()), currentItem, (int) slider.getValue());
        } else if (currentAction.equals(ItemsController.ItemActionType.CELL)) {
            MoneyController.cellItems(Game.getMap().getPlayersSquad().getSelectedCharacter(), Game.getMap().getCharacterList().get(Game.getContainerInventory().getCharacterId()), currentItem, (int) slider.getValue());
        }
    }

    public static void hide() {
        pane.setVisible(false);
    }

    private static void changeValue(Number newVal) {
        slider.setValue(newVal.intValue());
        selectCountLabel.setText(String.valueOf(newVal.intValue()));
    }
}
