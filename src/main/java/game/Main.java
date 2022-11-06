package game;

import controller.MapController;
import controller.utils.JsonUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.entity.GameModeEnum;
import view.Game;

import java.io.File;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(Game.getRoot());
        primaryStage.setTitle("Game");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/gui/Icon.png")));

        var player = Game.getMap().getPlayer();

        Game.getInventory().filterInventoryTabs(Game.getInventory().getTabPane().getSelectionModel().getSelectedItem());

        MapController.drawCurrentMap();

        scene.setOnKeyReleased(event -> MapController.onKeyReleased(event.getCode(), primaryStage));
        scene.setOnKeyPressed(event -> MapController.onKeyTyped(event.getCode()));

        Label mapNameLabel = new Label(Game.getText("MAP_NAME"));
        mapNameLabel.setLayoutX(5);
        mapNameLabel.setLayoutY(10);
        Game.getEditor().getButtonsPane().getChildren().add(mapNameLabel);

        TextField mapNameTextField = new TextField();
        mapNameTextField.setLayoutX(105);
        mapNameTextField.setLayoutY(5);
        mapNameTextField.setText(Game.getMap().getMapName());
        mapNameTextField.setFocusTraversable(false);
        mapNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d|\\.*")) {
                mapNameTextField.setText(newValue.replaceAll("[^\\d|.]", ""));
            }
        });
        Game.getEditor().getButtonsPane().getChildren().add(mapNameTextField);

        ImageView saveMapImage = new ImageView("/graphics/gui/SaveMap.png");
        saveMapImage.setLayoutX(260);
        saveMapImage.setLayoutY(5);
        saveMapImage.setOnMousePressed(event -> JsonUtils.saveMap(mapNameTextField.getText(), Game.getMap()));
        Game.getEditor().getButtonsPane().getChildren().add(saveMapImage);

        ImageView loadMapImage = new ImageView("/graphics/gui/LoadMap.png");
        loadMapImage.setLayoutX(295);
        loadMapImage.setLayoutY(5);
        loadMapImage.setOnMousePressed(event -> {
            Game.setMap(JsonUtils.loadMap(mapNameTextField.getText()));
            MapController.drawCurrentMap();
        });
        Game.getEditor().getButtonsPane().getChildren().add(loadMapImage);

        ImageView startTestGameImage = new ImageView("/graphics/gui/StartTestGame.png");
        startTestGameImage.setLayoutX(330);
        startTestGameImage.setLayoutY(5);
        startTestGameImage.setOnMousePressed(event -> Game.setGameMode(GameModeEnum.GAME));
        Game.getEditor().getButtonsPane().getChildren().add(startTestGameImage);

        Game.getStopTestGameImage().setLayoutX(5);
        Game.getStopTestGameImage().setLayoutY(610);
        Game.getStopTestGameImage().setOnMousePressed(event -> Game.setGameMode(GameModeEnum.EDITOR));
        Game.getStopTestGameImage().setVisible(false);
        Game.getRoot().getChildren().add(Game.getStopTestGameImage());

        Game.getRoot().setOnMousePressed(event -> MapController.drawMapStart(event.getX(), event.getY(), event.getButton() == MouseButton.SECONDARY));
        Game.getRoot().setOnMouseReleased(event -> MapController.drawMapEnd());
        Game.getRoot().setOnMouseMoved(event -> MapController.mapMouseMove(event.getX(), event.getY()));
        Game.getRoot().setOnMouseDragged(event -> MapController.mapMouseDragged(event.getX(), event.getY(), event.getButton() == MouseButton.SECONDARY));
        primaryStage.setScene(scene);
        primaryStage.show();
        Game.getEditor().getCanvas().requestFocus();

        player.setPlayerStartItems(player);

        File f = new File("src/main/resources/sound/mainTheme.mp3");
        Media media = new Media(f.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        try {
            mediaPlayer.setAutoPlay(true);
        } finally {
            mediaPlayer.dispose();
        }
    }
}
