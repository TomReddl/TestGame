package view;

import controller.TimeController;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.Date;
import java.util.TimerTask;

import static game.GameParams.tileSize;
import static game.GameParams.viewSize;

/**
 * Панель для компонентов контроля времени в игре
 */
public class TimeControlPanel {
    @Getter
    private final Pane pane;
    @Getter
    private final ImageView borderImage;
    @Getter
    private final ImageView stopTimeImage;
    @Getter
    private final ImageView startTimeImage;
    @Getter
    private final ImageView startTimeX2Image;
    @Getter
    private final ImageView startTimeX3Image;

    TimeControlPanel() {
        pane = new Pane();
        pane.setPrefSize(210, 60);
        pane.setLayoutX(10 + tileSize * viewSize);
        pane.setLayoutY(90);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:gray;");
        pane.setVisible(false);

        borderImage = new ImageView("/graphics/gui/TimeBorder.png");
        borderImage.setFitWidth(tileSize + 4);
        borderImage.setFitHeight(tileSize + 4);
        borderImage.setPreserveRatio(true);
        borderImage.setSmooth(true);
        borderImage.setCache(true);
        borderImage.setLayoutX(8);
        borderImage.setLayoutY(8);
        pane.getChildren().add(borderImage);

        stopTimeImage = new ImageView("/graphics/gui/stopTime.png");
        stopTimeImage.setFitWidth(tileSize);
        stopTimeImage.setFitHeight(tileSize);
        stopTimeImage.setPreserveRatio(true);
        stopTimeImage.setSmooth(true);
        stopTimeImage.setCache(true);
        stopTimeImage.setLayoutX(10);
        stopTimeImage.setLayoutY(10);
        stopTimeImage.setOnMouseClicked(event -> stopTime());
        stopTimeImage.setOnMouseEntered(event -> Editor.showHint(Game.getGameText("STOP_TIME")));
        stopTimeImage.setOnMouseExited(event -> Editor.hideHint());
        pane.getChildren().add(stopTimeImage);

        startTimeImage = new ImageView("/graphics/gui/startTime.png");
        startTimeImage.setFitWidth(tileSize);
        startTimeImage.setFitHeight(tileSize);
        startTimeImage.setPreserveRatio(true);
        startTimeImage.setSmooth(true);
        startTimeImage.setCache(true);
        startTimeImage.setLayoutX(60);
        startTimeImage.setLayoutY(10);
        startTimeImage.setOnMouseClicked(event -> startTime(2, startTimeImage.getLayoutX()));
        startTimeImage.setOnMouseEntered(event -> Editor.showHint(Game.getGameText("START_TIME")));
        startTimeImage.setOnMouseExited(event -> Editor.hideHint());
        pane.getChildren().add(startTimeImage);

        startTimeX2Image = new ImageView("/graphics/gui/startTimeX2.png");
        startTimeX2Image.setFitWidth(tileSize);
        startTimeX2Image.setFitHeight(tileSize);
        startTimeX2Image.setPreserveRatio(true);
        startTimeX2Image.setSmooth(true);
        startTimeX2Image.setCache(true);
        startTimeX2Image.setLayoutX(110);
        startTimeX2Image.setLayoutY(10);
        startTimeX2Image.setOnMouseClicked(event -> startTime(2, startTimeX2Image.getLayoutX()));
        startTimeX2Image.setOnMouseEntered(event -> Editor.showHint(Game.getGameText("START_TIME_X2")));
        startTimeX2Image.setOnMouseExited(event -> Editor.hideHint());
        pane.getChildren().add(startTimeX2Image);

        startTimeX3Image = new ImageView("/graphics/gui/startTimeX3.png");
        startTimeX3Image.setFitWidth(tileSize);
        startTimeX3Image.setFitHeight(tileSize);
        startTimeX3Image.setPreserveRatio(true);
        startTimeX3Image.setSmooth(true);
        startTimeX3Image.setCache(true);
        startTimeX3Image.setLayoutX(160);
        startTimeX3Image.setLayoutY(10);
        startTimeX3Image.setOnMouseClicked(event -> startTime(1, startTimeX3Image.getLayoutX()));
        startTimeX3Image.setOnMouseEntered(event -> Editor.showHint(Game.getGameText("START_TIME_X3")));
        startTimeX3Image.setOnMouseExited(event -> Editor.hideHint());
        pane.getChildren().add(startTimeX3Image);

        Game.getRoot().getChildren().add(pane);
    }

    /**
     * Поставить течение времени на паузу
     */
    public void stopTime() {
        if (TimeController.getTask() != null) {
            borderImage.setLayoutX(stopTimeImage.getLayoutX() - 2);
            TimeController.getTask().cancel();
        }
    }

    private void startTime(int speed, double xPos) {
        if (TimeController.getTask() != null) {
            TimeController.getTask().cancel();
        }
        TimeController.setTask(getTask());
        borderImage.setLayoutX(xPos - 2);
        TimeController.getTimer().schedule(TimeController.getTask(), new Date(), TimeController.getBaseTicTime() * speed);

    }

    private TimerTask getTask() {
        return new TimerTask() {
            public void run() {
                TimeController.tic(true);
            }
        };
    }

    /**
     * Запуск нормальной скорости течения времени
     */
    public void startNormalTime() {
        startTime(3, startTimeImage.getLayoutX());
    }
}
