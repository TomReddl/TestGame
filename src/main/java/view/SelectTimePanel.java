package view;

import controller.TimeController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import model.entity.GameCalendar;
import view.inventory.ItemCountPanel;

import static game.GameParams.tileSize;
import static game.GameParams.viewSize;

/**
 * Панель выбора времени ожидания
 */
public class SelectTimePanel {
    @Getter
    private static final Pane pane;
    private static final Slider slider;
    private static final Button selectButton;
    @Getter
    private static final Button backButton;
    private static final Label selectTimeLabel;
    private static TimeSkipType timeSkipType;

    // Виды пропуска времени
    public enum TimeSkipType {
        WAIT(Game.getGameText("WAIT")),
        SLEEP(Game.getGameText("SLEEP"));

        @Getter
        private final String desc;

        TimeSkipType(String desc) {
            this.desc = desc;
        }
    }

    static {
        pane = new Pane();
        pane.setPrefSize(300, 80);
        pane.setLayoutX(tileSize*(viewSize / 2) - 150);
        pane.setLayoutY(tileSize*(viewSize / 2) - 50);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:gray;");
        pane.setVisible(false);
        Game.getRoot().getChildren().add(pane);

        slider = new Slider();
        slider.setLayoutY(10);
        slider.setLayoutX(10);
        slider.setPrefWidth(250);
        slider.setMajorTickUnit(1.0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.setMin(1);
        slider.setBlockIncrement(1);
        slider.setMax(24);
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                changeValue(newVal));
        pane.getChildren().add(slider);

        selectTimeLabel = new Label();
        selectTimeLabel.setLayoutY(10);
        selectTimeLabel.setLayoutX(270);
        pane.getChildren().add(selectTimeLabel);

        selectButton = new Button(Game.getText("OK"));
        selectButton.setLayoutX(80);
        selectButton.setLayoutY(40);
        selectButton.setOnAction(event -> selectCount());
        pane.getChildren().add(selectButton);

        backButton = new Button(Game.getText("BACK"));
        backButton.setLayoutY(40);
        backButton.setLayoutX(150);
        backButton.setOnAction(event -> hide());
        pane.getChildren().add(backButton);
    }

    /**
     * Показать панель выбора количества предметов
     *
     * @param type - тип пропуска времени (ожидание/сон)
     */
    public static void show(TimeSkipType type) {
        timeSkipType = type;
        if (!Game.getInventory().getTabPane().isVisible() && !ItemCountPanel.getPane().isVisible()) {
            selectButton.setText(timeSkipType.getDesc());
            selectTimeLabel.setText("1 " + Game.getGameText("HOUR"));
            slider.setValue(1);
            pane.setVisible(true);
        }
    }

    public static void selectCount() {
        hide();
        TimeController.tic(((int) slider.getValue()) * GameCalendar.getTicsInHour());
    }

    public static void hide() {
        pane.setVisible(false);
    }

    private static void changeValue(Number newVal) {
        slider.setValue(newVal.intValue());
        selectTimeLabel.setText(newVal.intValue() + " " + Game.getGameText("HOUR"));
    }
}
