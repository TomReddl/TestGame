package view.dialog;

import controller.CharactersController;
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
import model.entity.GameCalendar;
import model.entity.character.Character;
import model.entity.character.Parameter;
import view.Game;
import view.inventory.BookPanel;

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
        pane.setPrefSize(460, 210);
        pane.setLayoutX(70);
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
        scrollPane.setPrefSize(440, 140);
        scrollPane.setMinWidth(260);
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

    public void showPanel(Character teacher) {
        pane.setVisible(true);
        Character student = Game.getMap().getPlayersSquad().getSelectedCharacter();
        titleLabel.setText(Game.getText("SKILL_TRAINING") + " " + teacher.getName());
        int i = 0;
        scrollContentPane.getChildren().clear();
        int studentMoney = MoneyController.getMoneyCount(Game.getMap().getPlayersSquad().getSelectedCharacter().getInventory());
        for (String trainingSkill : teacher.getInfo().getTrainableSkills()) {
            Parameter studentSkill = student.getParams().getSkills().get(trainingSkill);
            Parameter teacherSkill = teacher.getParams().getSkills().get(trainingSkill);
            Parameter teacherTrainingSkill = teacher.getParams().getSkills().get("TRAINING");
            int trainingPrice = MoneyController.getTrainingPrice(teacher, student, trainingSkill);
            int trainingTime = CharactersController.getTrainTime(student, student.getParams().getSkills().get(trainingSkill));
            String impossibilityReason = "";
            boolean skillLessMax = studentSkill.getRealValue() < 100;
            if (skillLessMax) {
                impossibilityReason = Game.getGameText("SKILL_MAX");
            }
            boolean skillTeacherLessStudent = studentSkill.getRealValue() <= teacherSkill.getCurrentValue();
            if (skillTeacherLessStudent) {
                impossibilityReason = Game.getGameText("SKILL_TEACHER_LESS_STUDENT");
            }
            boolean skillTrainTeacherLessStudent = studentSkill.getRealValue() <= teacherTrainingSkill.getCurrentValue();
            if (skillTrainTeacherLessStudent) {
                impossibilityReason = Game.getGameText("SKILL_TRAIN_TRACHER_LESS_STUDENT");
            }
            boolean enoughMoney = studentMoney >= trainingPrice;
            if (!enoughMoney) {
                impossibilityReason = Game.getGameText("NOT_ENOUGH_MONEY");
            }
            boolean canTrain = skillLessMax && skillTeacherLessStudent && skillTrainTeacherLessStudent && enoughMoney;
                Label label = new Label(Game.getText("TRAINING_SKILL_NAME") + " \"" +
                        Game.getText(trainingSkill + "_PARAM_NAME") + "\" " +
                        Game.getText("UP_TO") + " " + (studentSkill.getRealValue() + 1) + " " +
                        Game.getText("UNITS") + " (" + trainingPrice + " " + Game.getText("SHAD") + ", " +
                        (trainingTime / GameCalendar.getTicsInHour()) + " " + Game.getGameText("HOUR") + ")");
                label.setTextFill(canTrain ? Color.web("#4a3710") : Color.GRAY);
                label.setFont(font);
                label.setOnMouseEntered(event -> labelMouseEntered(label, canTrain));
                label.setOnMouseExited(event -> labelMouseExited(label, canTrain));
            String finalImpossibilityReason = impossibilityReason.toLowerCase();
            label.setOnMouseClicked(event -> CharactersController.trainSkill(canTrain, teacher, student, trainingSkill, trainingPrice, trainingTime, finalImpossibilityReason));
                label.setLayoutX(10);
                label.setLayoutY(10 + i * 15);
                i++;
                scrollContentPane.getChildren().add(label);
        }
        if (scrollContentPane.getChildren().size() == 0) {
            pane.setVisible(false);
        }
        charactersMoneyLabel.setText(Game.getText("CHARACTERS_MONEY") + " " + studentMoney + " " + Game.getText("SHAD"));
    }

    public void closePanel() {
        pane.setVisible(false);
    }

    private void labelMouseEntered(Label label, boolean canTrain) {
        if (canTrain) {
            label.setTextFill(Color.RED);
        }
    }

    private void labelMouseExited(Label label, boolean canTrain) {
        if (canTrain) {
            label.setTextFill(Color.web("#4a3710"));
        }
    }
}
