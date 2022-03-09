package view.menu;

import model.entity.GameLangEnum;
import view.Game;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import view.params.GameParams;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameSettingsPanel {
    private final Pane pane = new Pane();
    @Getter
    private final Label langLabel = new Label(Game.getText("LANG"));
    private final ComboBox<String> langCB = new ComboBox();
    @Getter
    private final Button saveButton = new Button(Game.getText("SAVE"));
    @Getter
    private final Button backButton = new Button(Game.getText("BACK"));

    public GameSettingsPanel() {
        langLabel.setLayoutX(20);
        langLabel.setLayoutY(5);
        pane.getChildren().add(langLabel);

        langCB.setLayoutX(20);
        langCB.setLayoutY(30);
        langCB.setPrefWidth(100);
        langCB.getItems().addAll(Stream.of(GameLangEnum.values())
                .map(GameLangEnum::getDesc)
                .collect(Collectors.toList()));
        pane.getChildren().add(langCB);

        saveButton.setLayoutX(20);
        saveButton.setLayoutY(60);
        saveButton.setPrefWidth(100);
        saveButton.setOnAction(event ->
                saveLang(GameLangEnum.getGameLangByCode(langCB.getSelectionModel().getSelectedItem())));
        pane.getChildren().add(saveButton);

        backButton.setLayoutX(20);
        backButton.setLayoutY(90);
        backButton.setPrefWidth(100);
        backButton.setOnAction(event -> closeSettingsPanel());
        pane.getChildren().add(backButton);

        pane.setLayoutX(200);
        pane.setLayoutY(200);
        pane.setPrefSize(140, 210);
        pane.setVisible(Boolean.FALSE);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        Game.getRoot().getChildren().add(pane);
    }

    private void closeSettingsPanel() {
        pane.setVisible(Boolean.FALSE);
        MainMenu.getPane().setVisible(Boolean.TRUE);
    }

    private void saveLang(GameLangEnum lang) {
        GameParams.lang = lang;

        Path props = Paths.get("game.properties");
        try {
            Writer PropWriter =
                    Files.newBufferedWriter(props);
            Properties AppProps = new Properties();
            AppProps.setProperty("lang", lang.name());
            AppProps.store(PropWriter,
                    "Game properties");
            PropWriter.close();
        } catch (IOException Ex) {
            System.out.println("IO Exception :" +
                    Ex.getMessage());
        }
        Game.loadTranslations();
        Game.fillTranslations();
    }

    public void viewSettingsPanel() {
        MainMenu.getPane().setVisible(Boolean.FALSE);

        langCB.getSelectionModel().select(GameParams.lang.getDesc());
        pane.setVisible(Boolean.TRUE);
    }
}
