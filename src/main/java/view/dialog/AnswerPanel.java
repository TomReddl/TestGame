package view.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;
import view.Game;

/**
 * Панель редактирования ответа на фразу персонажа в диалоге
 */
public class AnswerPanel {
    @Getter
    private final Pane pane;
    private final Label textLabel;
    @Getter
    private final TextField textEdit;
    private final Label visiblyConditionLabel;
    @Getter
    private final TextField visiblyConditionEdit;
    private final Label nextPhraseConditionLabel;
    @Getter
    private final TextField nextPhraseConditionEdit;
    private final Label scriptLabel;
    @Getter
    private final TextField scriptEdit;
    private final Button deleteAnswerButton;
    private final Button goToPhraseButton;
    @Getter
    @Setter
    private int id;

    @Getter
    private static final int paneHeight = 140; // высота панели ответа

    public AnswerPanel(int id) {
        this.id = id;
        pane = new Pane();
        pane.setPrefSize(530, paneHeight);
        pane.setBackground(new Background(new BackgroundFill((Color.WHITESMOKE), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:grey;");

        textLabel = new Label();
        textLabel.setLayoutX(10);
        textLabel.setLayoutY(10);
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        textLabel.setText(Game.getText("ANSWER_TEXT"));
        pane.getChildren().add(textLabel);

        textEdit = new TextField();
        textEdit.setLayoutX(160);
        textEdit.setLayoutY(10);
        textEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(textEdit);

        visiblyConditionLabel = new Label();
        visiblyConditionLabel.setLayoutX(10);
        visiblyConditionLabel.setLayoutY(40);
        visiblyConditionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        visiblyConditionLabel.setText(Game.getText("ANSWER_VISIBLY_CONDITION"));
        pane.getChildren().add(visiblyConditionLabel);

        visiblyConditionEdit = new TextField();
        visiblyConditionEdit.setLayoutX(160);
        visiblyConditionEdit.setLayoutY(40);
        visiblyConditionEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(visiblyConditionEdit);

        nextPhraseConditionLabel = new Label();
        nextPhraseConditionLabel.setLayoutX(10);
        nextPhraseConditionLabel.setLayoutY(70);
        nextPhraseConditionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nextPhraseConditionLabel.setText(Game.getText("NEXT_PHRASE_CONDITION"));
        pane.getChildren().add(nextPhraseConditionLabel);

        nextPhraseConditionEdit = new TextField();
        nextPhraseConditionEdit.setLayoutX(160);
        nextPhraseConditionEdit.setLayoutY(70);
        nextPhraseConditionEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nextPhraseConditionEdit.setOnKeyTyped(event -> nextPhraseConditionEdit());
        pane.getChildren().add(nextPhraseConditionEdit);

        scriptLabel = new Label();
        scriptLabel.setLayoutX(10);
        scriptLabel.setLayoutY(100);
        scriptLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        scriptLabel.setText(Game.getText("SCRIPT"));
        pane.getChildren().add(scriptLabel);

        scriptEdit = new TextField();
        scriptEdit.setLayoutX(160);
        scriptEdit.setLayoutY(100);
        scriptEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        scriptEdit.setOnKeyTyped(event -> nextPhraseConditionEdit());
        pane.getChildren().add(scriptEdit);

        deleteAnswerButton = new Button(Game.getText("DELETE_ANSWER"));
        deleteAnswerButton.setLayoutX(320);
        deleteAnswerButton.setLayoutY(10);
        deleteAnswerButton.setPrefWidth(150);
        deleteAnswerButton.setOnAction(event -> Game.getEditor().getDialogPanel().deleteAnswer(this));
        pane.getChildren().add(deleteAnswerButton);

        goToPhraseButton = new Button(Game.getText("GO_TO_PHRASE"));
        goToPhraseButton.setLayoutX(320);
        goToPhraseButton.setLayoutY(70);
        goToPhraseButton.setPrefWidth(150);
        goToPhraseButton.setVisible(false);
        goToPhraseButton.setOnAction(event -> Game.getEditor().getDialogPanel().phraseLabelClick(nextPhraseConditionEdit.getText()));
        pane.getChildren().add(goToPhraseButton);
    }

    private void nextPhraseConditionEdit() {
        goToPhraseButton.setVisible(!nextPhraseConditionEdit.getText().trim().equals(""));
    }
}
