package view.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import model.entity.dialogs.Answer;
import model.entity.dialogs.Dialog;
import model.entity.dialogs.Phrase;
import model.entity.character.Character;
import view.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Панель редактирования диалога с неигровым персонажем
 */
public class DialogPanel {
    @Getter
    private final Pane pane;
    private final Label dialogIdLabel;
    private final TextField dialogIdEdit;
    private final Label dialogPhaseLabel;
    private final TextField dialogPhaseEdit;
    private final Label phraseLabel;
    private final TextField phraseEdit;
    private final Label textLabel;
    private final TextArea textEdit;
    private final Label scriptLabel;
    private final TextField scriptEdit;
    private final Label answersLabel;
    private final Label phrasesLabel;

    private final Pane scrollContentPane;
    @Getter
    private final ScrollPane scrollPane;
    private final Pane answersScrollContentPane;
    @Getter
    private final ScrollPane answersScrollPane;

    @Getter
    private final Button addPhraseButton;
    @Getter
    private final Button saveDialogButton;
    private final Button closeDialogButton;
    @Getter
    private final Button addAnswerButton;

    private List<AnswerPanel> answerPanels = new ArrayList<>();
    private final List<Label> phraseLabels = new ArrayList<>();

    private Dialog dialog;
    private String characterId;
    private Phrase selectedPhrase;

    public DialogPanel() {
        pane = new Pane();
        pane.setPrefSize(550, 500);
        pane.setLayoutX(30);
        pane.setLayoutY(50);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        dialogIdLabel = new Label();
        dialogIdLabel.setLayoutX(10);
        dialogIdLabel.setLayoutY(10);
        dialogIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        dialogIdLabel.setText(Game.getText("DIALOG_ID"));
        pane.getChildren().add(dialogIdLabel);

        dialogIdEdit = new TextField();
        dialogIdEdit.setLayoutX(120);
        dialogIdEdit.setLayoutY(10);
        dialogIdEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(dialogIdEdit);

        dialogPhaseLabel = new Label();
        dialogPhaseLabel.setLayoutX(10);
        dialogPhaseLabel.setLayoutY(40);
        dialogPhaseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        dialogPhaseLabel.setText(Game.getText("DIALOG_PHASE"));
        pane.getChildren().add(dialogPhaseLabel);

        dialogPhaseEdit = new TextField();
        dialogPhaseEdit.setLayoutX(120);
        dialogPhaseEdit.setLayoutY(40);
        dialogPhaseEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(dialogPhaseEdit);

        phraseLabel = new Label();
        phraseLabel.setLayoutX(10);
        phraseLabel.setLayoutY(70);
        phraseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        phraseLabel.setText(Game.getText("PHRASE_ID"));
        pane.getChildren().add(phraseLabel);

        phraseEdit = new TextField();
        phraseEdit.setLayoutX(120);
        phraseEdit.setLayoutY(70);
        phraseEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(phraseEdit);

        textLabel = new Label();
        textLabel.setLayoutX(10);
        textLabel.setLayoutY(100);
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        textLabel.setText(Game.getText("PHRASE_TEXT"));
        pane.getChildren().add(textLabel);

        textEdit = new TextArea();
        textEdit.setLayoutX(10);
        textEdit.setLayoutY(120);
        textEdit.setPrefWidth(260);
        textEdit.setPrefHeight(100);
        textEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(textEdit);

        scriptLabel = new Label();
        scriptLabel.setLayoutX(10);
        scriptLabel.setLayoutY(230);
        scriptLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        scriptLabel.setText(Game.getText("SCRIPT"));
        pane.getChildren().add(scriptLabel);

        scriptEdit = new TextField();
        scriptEdit.setLayoutX(120);
        scriptEdit.setLayoutY(230);
        scriptEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(scriptEdit);

        answersLabel = new Label();
        answersLabel.setLayoutX(10);
        answersLabel.setLayoutY(260);
        answersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        answersLabel.setText(Game.getText("ANSWERS"));
        pane.getChildren().add(answersLabel);

        phrasesLabel = new Label();
        phrasesLabel.setLayoutX(290);
        phrasesLabel.setLayoutY(10);
        phrasesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        phrasesLabel.setText(Game.getText("PHRASES"));
        pane.getChildren().add(phrasesLabel);

        scrollContentPane = new Pane();
        scrollContentPane.setPrefSize(200, 400);
        scrollContentPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(290);
        scrollPane.setLayoutY(30);
        scrollPane.setPrefSize(250, 250);
        scrollPane.setMaxHeight(240);
        scrollPane.setMinWidth(200);
        scrollPane.setContent(scrollContentPane);
        scrollPane.setStyle("-fx-border-color:grey;");
        pane.getChildren().add(scrollPane);

        answersScrollContentPane = new Pane();
        answersScrollContentPane.setPrefSize(200, 500);
        answersScrollContentPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        addAnswerButton = new Button(Game.getText("ADD_ANSWER"));
        addAnswerButton.setLayoutX(10);
        addAnswerButton.setLayoutY(10);
        addAnswerButton.setPrefWidth(150);
        addAnswerButton.setOnAction(event -> addAnswer());
        answersScrollContentPane.getChildren().add(addAnswerButton);

        answersScrollPane = new ScrollPane();
        answersScrollPane.setLayoutX(10);
        answersScrollPane.setLayoutY(290);
        answersScrollPane.setPrefSize(250, 250);
        answersScrollPane.setMaxHeight(160);
        answersScrollPane.setMinWidth(530);
        answersScrollPane.setContent(answersScrollContentPane);
        answersScrollPane.setStyle("-fx-border-color:grey;");
        pane.getChildren().add(answersScrollPane);

        addPhraseButton = new Button(Game.getText("SAVE_PHRASE"));
        addPhraseButton.setLayoutX(10);
        addPhraseButton.setLayoutY(460);
        addPhraseButton.setPrefWidth(150);
        addPhraseButton.setOnAction(event -> savePhrase());
        pane.getChildren().add(addPhraseButton);

        saveDialogButton = new Button(Game.getText("SAVE_DIALOG"));
        saveDialogButton.setLayoutX(170);
        saveDialogButton.setLayoutY(460);
        saveDialogButton.setPrefWidth(150);
        saveDialogButton.setOnAction(event -> saveDialog());
        pane.getChildren().add(saveDialogButton);

        closeDialogButton = new Button(Game.getText("CLOSE_DIALOG"));
        closeDialogButton.setLayoutX(330);
        closeDialogButton.setLayoutY(460);
        closeDialogButton.setPrefWidth(150);
        closeDialogButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeDialogButton);

        Game.getRoot().getChildren().add(pane);
    }

    private void closePanel() {
        pane.setVisible(false);
    }

    public void showPanel(Character character) {
        if (!pane.isVisible()) {
            pane.setVisible(true);
        }
        this.dialog = character.getDialog();
        characterId = character.getId();
        if (this.dialog == null) {
            this.dialog = new Dialog();
            selectedPhrase = new Phrase();
           // dialog.getPhrases().put("", selectedPhrase);
        }
    }

    /**
     * Сохранить диалог
     */
    private void saveDialog() {
        Game.getMap().getCharacterList().get(characterId).setDialog(dialog);
    }

    /**
     * Сохранить фразу
     */
    private void savePhrase() {
        if (!phraseEdit.getText().trim().equals("") && !textEdit.getText().trim().equals("")) {
            Label newPhraseLabel = null;
            Optional<Label> optionalLabel = phraseLabels.stream().filter(e -> e.getId().equals(phraseEdit.getText())).findFirst();
            if (optionalLabel.isPresent()) {
                newPhraseLabel = optionalLabel.get();
            } else {
                newPhraseLabel = addNewPhrase(phraseEdit.getText());
            }
            setPhraseFields(dialog.getPhrases().get(phraseEdit.getText()));
            newPhraseLabel.setText(phraseEdit.getText() + " " + textEdit.getText());
        }
    }

    private Label addNewPhrase(String id) {
        Phrase phrase = new Phrase();
        Label newPhraseLabel = new Label();
        newPhraseLabel.setId(id);
        newPhraseLabel.setLayoutX(10);
        newPhraseLabel.setLayoutY(10 + phraseLabels.size() * 30);
        final Label label = newPhraseLabel;
        newPhraseLabel.setOnMouseClicked(event -> phraseLabelClick(label.getId()));
        scrollContentPane.getChildren().add(newPhraseLabel);
        phraseLabels.add(newPhraseLabel);
        dialog.getPhrases().put(id, phrase);
        return newPhraseLabel;
    }

    public void phraseLabelClick(String id) {
        selectedPhrase = dialog.getPhrases().get(id);
        if (selectedPhrase == null) {
            addNewPhrase(id);
            phraseEdit.setText(id);
            textEdit.setText("");
            scriptEdit.setText("");
            answersScrollContentPane.getChildren().remove(1, answersScrollContentPane.getChildren().size());
            addAnswerButton.setLayoutY(10);
        } else {
            phraseEdit.setText(selectedPhrase.getId());
            textEdit.setText(selectedPhrase.getText());
            scriptEdit.setText(selectedPhrase.getScript());
            answersScrollContentPane.getChildren().remove(1, answersScrollContentPane.getChildren().size());
            answerPanels = new ArrayList<>();
            int i = 0;
            for (Answer answer : selectedPhrase.getAnswers()) {
                var answerPanel = new AnswerPanel(i);
                answerPanel.getPane().setLayoutY(i * AnswerPanel.getPaneHeight());
                answerPanels.add(answerPanel);
                answerPanel.getTextEdit().setText(answer.getText());
                answerPanel.getNextPhraseConditionEdit().setText(answer.getNextPhraseCondition());
                answerPanel.getVisiblyConditionEdit().setText(answer.getVisiblyCondition());
                answersScrollContentPane.getChildren().add(answerPanel.getPane());
                i++;
            }
        }
    }

    /**
     * Установить поля фразы
     * @param phrase
     */
    private void setPhraseFields(Phrase phrase) {
        phrase.setId(phraseEdit.getText());
        phrase.setText(textEdit.getText());
        phrase.setScript(scriptEdit.getText());
        for (AnswerPanel answerPanel : answerPanels) {
            Answer answer = new Answer();
            answer.setText(answerPanel.getTextEdit().getText());
            answer.setVisiblyCondition(answerPanel.getVisiblyConditionEdit().getText());
            answer.setNextPhraseCondition(answerPanel.getNextPhraseConditionEdit().getText());
            phrase.getAnswers().add(answer);
        }
    }

    /**
     * Добавить ответ
     */
    private void addAnswer() {
        if (selectedPhrase == null) {
            selectedPhrase = new Phrase();
        }
        if (selectedPhrase.getAnswers() == null) {
            selectedPhrase.setAnswers(new ArrayList<>());
        }
        selectedPhrase.getAnswers().add(new Answer());
        var answerPanel = new AnswerPanel(answerPanels.size());
        answerPanel.getPane().setLayoutY(answerPanels.size() * AnswerPanel.getPaneHeight());
        answerPanels.add(answerPanel);
        answersScrollContentPane.getChildren().add(answerPanel.getPane());
        addAnswerButton.setLayoutY(answerPanels.size() * AnswerPanel.getPaneHeight() + 10);
    }

    /**
     * Удалить ответ
     */
    void deleteAnswer(AnswerPanel deletedAnswerPanel) {
        selectedPhrase.getAnswers().remove(deletedAnswerPanel.getId());
        answerPanels.remove(deletedAnswerPanel);
        int i = 0;
        for (AnswerPanel answerPanel : answerPanels) {
            answerPanel.setId(i+1);
            answerPanel.getPane().setLayoutY(i * AnswerPanel.getPaneHeight());
            i++;
        }
        addAnswerButton.setLayoutY(answerPanels.size() * AnswerPanel.getPaneHeight() + 10);
    }
}
