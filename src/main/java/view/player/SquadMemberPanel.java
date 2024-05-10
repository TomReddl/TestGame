package view.player;

import controller.MapController;
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
import model.entity.character.Character;
import view.Game;

/**
 * Панель с информацией о члене отряда.
 */
public class SquadMemberPanel {
    @Getter
    private final Pane pane;
    @Getter
    private final ImageView characterImage; // изображение выбранной бутылки
    @Getter
    private final Label characterNameLabel; // имя персонажа

    public SquadMemberPanel(Character character) {
        pane = new Pane();
        pane.setPrefSize(260, 100);
        pane.setLayoutX(10);
        pane.setLayoutY(10);
        pane.setId(String.valueOf(character.getId()));
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setOnMouseClicked(event -> setSelectedCharacter());
        pane.setStyle("-fx-border-color:gray;");

        characterImage = new ImageView(character.getImage().getImage());
        characterImage.setLayoutX(10);
        characterImage.setLayoutY(10);
        pane.getChildren().add(characterImage);

        characterNameLabel = new Label();
        characterNameLabel.setLayoutX(10);
        characterNameLabel.setLayoutY(50);
        characterNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        characterNameLabel.setText(character.getName());
        pane.getChildren().add(characterNameLabel);
    }

    /**
     * Выбрать активного персонажа в отряде
     */
    private void setSelectedCharacter() {
        Character character = Game.getMap().getCharacterList().get(Integer.parseInt(pane.getId()));
        Game.getMap().getPlayersSquad().setSelectedCharacter(character);
        Game.getMap().getPlayersSquad().setXMapPos(Math.max(0, character.getXPosition() - 4));
        Game.getMap().getPlayersSquad().setYMapPos(Math.max(0, character.getYPosition() - 4));

        for (SquadMemberPanel squadMemberPanel : Game.getEditor().getSquadPanel().getSquadMemberPanelList()) {
            squadMemberPanel.getPane().setStyle("-fx-border-color:gray;");
        }
        pane.setStyle("-fx-border-color:gold; -fx-border-width:3px");

        MapController.drawCurrentMap();
    }
}
