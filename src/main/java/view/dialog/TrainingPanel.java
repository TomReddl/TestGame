package view.dialog;

import controller.MoneyController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import view.inventory.BookPanel;
import view.params.ParamPanel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Панель для тренировки навыков у учителя
 */
public class TrainingPanel {
    @Getter
    private final Pane pane;
    private final Label titleLabel;
    private final Pane scrollContentPane;
    @Getter
    private final ScrollPane scrollPane;
    private final Button closeButton = new Button(Game.getText("CLOSE"));
    private final Label charactersMoneyLabel; // деньги персонажа

    private Font font;

    public TrainingPanel() {
        font = Font.font("Arial", FontWeight.BOLD, 16);
        InputStream fontStream = BookPanel.class.getResourceAsStream("/fonts/KingthingsPetrock.ttf");
        if (fontStream != null) {
            try {
                Font.loadFont(fontStream, 36);
                font = Font.font("Kingthings Petrock", FontWeight.BOLD, 16);
                fontStream.close();
            } catch (IOException ex) {
                throw new RuntimeException("Не удалось загрузить шрифт для книг=%s" + ex.getMessage());
            }
        }

        pane = new Pane();
        pane.setPrefSize(400, 210);
        pane.setLayoutX(100);
        pane.setLayoutY(100);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:gray;");
        pane.setVisible(false);

        titleLabel = new Label(Game.getText("SKILL_TRAINING"));
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(10);
        titleLabel.setFont(font);
        titleLabel.setTextFill(Color.web("#4a3710"));
        pane.getChildren().add(titleLabel);

        scrollContentPane = new Pane();
        scrollContentPane.setPrefSize(360, 120);
        scrollContentPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(30);
        scrollPane.setPrefSize(380, 140);
        scrollPane.setMinWidth(200);
        scrollPane.setContent(scrollContentPane);
        scrollPane.setStyle("-fx-border-color:grey;");
        pane.getChildren().add(scrollPane);

        closeButton.setLayoutX(10);
        closeButton.setLayoutY(175);
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeButton);

        charactersMoneyLabel = new Label(Game.getText("CHARACTERS_MONEY"));
        charactersMoneyLabel.setLayoutX(120);
        charactersMoneyLabel.setLayoutY(175);
        charactersMoneyLabel.setFont(font);
        charactersMoneyLabel.setTextFill(Color.web("#4a3710"));
        pane.getChildren().add(charactersMoneyLabel);

        Game.getRoot().getChildren().add(pane);
    }

    public void showPanel(Character character) {
        pane.setVisible(true);
        titleLabel.setText(Game.getText("SKILL_TRAINING") + " " + character.getName());
        int i = 0;
        for (String trainingSkill : character.getInfo().getTrainableSkills()) {
            String skillName = ParamPanel.getSkillsNames().get(Integer.parseInt(trainingSkill));
            Label label = new Label(Game.getText("TRAINING_SKILL_NAME") + " \"" +
                    Game.getText(skillName + "_PARAM_NAME") + "\" " +
                    Game.getText("UP_TO") + " " + (character.getParams().getSkills().get(skillName).getRealValue() + 1) + " " +
                    Game.getText("UNITS") + " (" + MoneyController.getTrainingPrice(character, Game.getMap().getPlayersSquad().getSelectedCharacter(), skillName) + " " + Game.getText("SHAD") + ")");
            label.setTextFill(Color.web("#4a3710"));
            label.setFont(font);
            label.setLayoutX(10);
            label.setLayoutY(10 + i*15);
            i++;
            scrollContentPane.getChildren().add(label);
        }
        charactersMoneyLabel.setText(Game.getText("CHARACTERS_MONEY") + " " + MoneyController.getMoneyCount(Game.getMap().getPlayersSquad().getSelectedCharacter().getInventory()) + " " + Game.getText("SHAD"));
    }

    private void closePanel() {
        pane.setVisible(false);
    }
}
