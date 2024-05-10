package view.player;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import model.entity.character.Character;
import view.Game;

import java.util.ArrayList;
import java.util.List;

import static game.GameParams.tileSize;
import static game.GameParams.viewSize;

/**
 * Панель для отображения информации об отряде игрока
 */
public class SquadPanel {
    @Getter
    private final Pane pane;

    private final Label squadNameLabel; // название отряда
    private final Label fractionLabel; // фракция, к которой принадлежит отряд
    private final Pane innerPane;
    private final ScrollPane scrollPane;
    @Getter
    private final List<SquadMemberPanel> squadMemberPanelList = new ArrayList<>();

    public SquadPanel() {
        pane = new Pane();
        pane.setPrefSize(340, 305);
        pane.setLayoutX(10 + tileSize * viewSize);
        pane.setLayoutY(170);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:gray;");
        pane.setVisible(false);

        squadNameLabel = new Label();
        squadNameLabel.setLayoutX(10);
        squadNameLabel.setLayoutY(10);
        squadNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        squadNameLabel.setText(Game.getGameText("SQUAD_NAME") + ": " + Game.getMap().getPlayersSquad().getName());
        pane.getChildren().add(squadNameLabel);

        fractionLabel = new Label();
        fractionLabel.setLayoutX(10);
        fractionLabel.setLayoutY(30);
        fractionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        fractionLabel.setText(Game.getGameText("FRACTION") + ": " + Game.getMap().getPlayersSquad().getFraction().getName());
        pane.getChildren().add(fractionLabel);

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

        Game.getRoot().getChildren().add(pane);

        drawSquadMembersPanels();
    }

    /**
     * Отрисовать карточки членов отряда
     */
    public void drawSquadMembersPanels() {
        innerPane.getChildren().remove(0, innerPane.getChildren().size());

        int i = 0;
        for (Character character : Game.getMap().getPlayersSquad().getCharacters().values()) {
            var characterRecord = new SquadMemberPanel(character);
            squadMemberPanelList.add(characterRecord);
            characterRecord.getPane().setLayoutY(i++ * characterRecord.getPane().getPrefHeight() + 10);
            innerPane.getChildren().add(characterRecord.getPane());
        }
    }
}
