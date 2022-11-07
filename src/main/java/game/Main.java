package game;

import controller.CharactersController;
import controller.MapController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import view.Editor;
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

        Game.getRoot().setOnMousePressed(event -> MapController.drawMapStart(event.getX(), event.getY(), event.getButton() == MouseButton.SECONDARY));
        Game.getRoot().setOnMouseReleased(event -> MapController.drawMapEnd());
        Game.getRoot().setOnMouseMoved(event -> MapController.mapMouseMove(event.getX(), event.getY()));
        Game.getRoot().setOnMouseDragged(event -> MapController.mapMouseDragged(event.getX(), event.getY(), event.getButton() == MouseButton.SECONDARY));
        primaryStage.setScene(scene);
        primaryStage.show();
        Editor.getCanvas().requestFocus();

        CharactersController.setPlayerStartItems(player);

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
