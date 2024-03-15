package view.dialog;

import controller.ItemsController;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Getter;
import lombok.Setter;
import model.entity.dialogs.Answer;
import model.entity.dialogs.Dialog;
import model.entity.dialogs.Phrase;
import model.entity.map.Items;
import model.entity.player.Player;
import view.Game;
import view.inventory.BookPanel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Панель отображения диалога в игре
 */
public class GameDialogPanel {
    @Getter
    private final Pane pane;

    private final Pane scrollContentPane;
    @Getter
    private final ScrollPane scrollPane;
    private final TextFlow textFlow;

    private final Pane answersScrollContentPane;
    @Getter
    private final ScrollPane answersScrollPane;
    private List<Label> answers = new ArrayList<>();

    @Setter
    private Dialog dialog;
    private Phrase selectedPhrase;
    private int npcId;

    private Font font;

    public GameDialogPanel() {
        pane = new Pane();
        pane.setPrefSize(550, 360);
        pane.setLayoutX(30);
        pane.setLayoutY(50);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        scrollContentPane = new Pane();
        scrollContentPane.setPrefSize(500, 190);
        scrollContentPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(10);
        scrollPane.setPrefSize(520, 200);
        scrollPane.setMinWidth(500);
        scrollPane.setContent(scrollContentPane);
        scrollPane.setStyle("-fx-border-color:grey;");
        pane.getChildren().add(scrollPane);

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

        Text text = new Text(Game.getText("DIALOG_ID"));
        text.setFill(Color.web("#4a3710"));
        text.setFont(font);
        text.setWrappingWidth(170);

        textFlow = new TextFlow();
        textFlow.setLayoutX(10);
        textFlow.setLayoutY(10);
        textFlow.getChildren().addAll(text);
        scrollContentPane.getChildren().add(textFlow);

        answersScrollContentPane = new Pane();
        answersScrollContentPane.setPrefSize(500, 90);
        answersScrollContentPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        answersScrollPane = new ScrollPane();
        answersScrollPane.setLayoutX(10);
        answersScrollPane.setLayoutY(220);
        answersScrollPane.setPrefSize(520, 100);
        answersScrollPane.setMinWidth(500);
        answersScrollPane.setContent(answersScrollContentPane);
        answersScrollPane.setStyle("-fx-border-color:grey;");
        pane.getChildren().add(answersScrollPane);

        Game.getRoot().getChildren().add(pane);
    }

    private void closeDialog() {
        pane.setVisible(false);
    }

    public void showPanel(Integer npcId) {
        if (!pane.isVisible()) {
            pane.setVisible(true);
        }
        this.npcId = npcId;
        if (dialog == null) {
            dialog = new Dialog();
            selectedPhrase = new Phrase();
            dialog.getPhrases().put("", selectedPhrase);
            ((Text) textFlow.getChildren().get(0)).setText("");
        } else {
            setPhrase(npcId, dialog.getPhrases().get("1"));
        }
    }

    /**
     * Установить фразу
     *
     * @param npcId  - id персонажа
     * @param phrase - фраза
     */
    private void setPhrase(Integer npcId, Phrase phrase) {
        if (phrase != null) {
            selectedPhrase = phrase;
            applyPhraseScript(selectedPhrase.getScript());
            ((Text) textFlow.getChildren().get(0)).setText(Game.getMap().getNpcList().get(npcId).getName() + ": " + replaceDialogText(selectedPhrase.getText()));
            int i = 0;
            answers.clear();
            answersScrollContentPane.getChildren().clear();
            for (Answer answer : selectedPhrase.getAnswers()) {
                if (checkAnswerVisibly(answer.getVisiblyCondition())) {
                    Label answerLabel = new Label();
                    answerLabel.setFont(font);
                    answerLabel.setTextFill(Color.web("#4a3710"));
                    answerLabel.setLayoutX(5);
                    answerLabel.setOnMouseEntered(event -> onAnswerMouseEnter(answerLabel));
                    answerLabel.setOnMouseExited(event -> onAnswerMouseExited(answerLabel));
                    answerLabel.setOnMouseClicked(event -> setPhrase(npcId, dialog.getPhrases().get(applyNextPhraseCondition(answer.getNextPhraseCondition()))));
                    answerLabel.setLayoutY(5 + i * 20);
                    answerLabel.setText(replaceDialogText(answer.getText()));
                    answersScrollContentPane.getChildren().add(answerLabel);
                    answers.add(answerLabel);
                    i++;
                }
            }
            Label exitDialogLabel = new Label();
            exitDialogLabel.setFont(font);
            exitDialogLabel.setTextFill(Color.web("#4a3710"));
            exitDialogLabel.setLayoutX(5);
            exitDialogLabel.setOnMouseEntered(event -> onAnswerMouseEnter(exitDialogLabel));
            exitDialogLabel.setOnMouseExited(event -> onAnswerMouseExited(exitDialogLabel));
            exitDialogLabel.setOnMouseClicked(event -> closeDialog());
            exitDialogLabel.setLayoutY(5 + i * 20);
            exitDialogLabel.setText("[" + Game.getText("CLOSE_DIALOG") + "]");
            answersScrollContentPane.getChildren().add(exitDialogLabel);
            answers.add(exitDialogLabel);
        }
    }

    private void onAnswerMouseEnter(Label answerLabel) {
        answerLabel.setTextFill(Color.RED);
    }

    private void onAnswerMouseExited(Label answerLabel) {
        answerLabel.setTextFill(Color.web("#4a3710"));
    }

    /**
     * Подстановка значений в текст диалога
     * @param text - текст диалога
     * @return - обработанный текст диалога
     */
    private String replaceDialogText(String text) {
        text = text.replaceAll("%playerName", Game.getMap().getPlayer().getName());
        text = text.replaceAll("%NPCName", Game.getMap().getNpcList().get(npcId).getName());

        return text;
    }

    /**
     * Проверить условие видимости ответа в диалоге
     * @param condition - условие
     * @return - true, если ответ видим в диалоге, false, если не виден
     */
    private boolean checkAnswerVisibly(String condition) {
        if (condition == null || condition.equals("")) {
            return true;
        } else {
            String[] words = condition.split(" ");
            boolean result = true;
            for (int i = 0; i < words.length; i++) {
                String word = words[i++];
                switch (word) {
                    case "playerHasItem": {
                        word = words[i++];
                        result = ItemsController.findItemInInventory(Integer.parseInt(word), Game.getMap().getPlayer().getInventory()) != null;
                    }
                }
            }
            return result;
        }
    }

    /**
     * Применить условие для выбора следующей фразы в диалоге
     * @param condition
     * @return
     */
    private String applyNextPhraseCondition(String condition) {
        if (condition == null || condition.equals("")) {
            return "";
        } else {
            try {
                Integer.parseInt(condition);
                return condition;
            } catch(NumberFormatException ignored){
            }
            String[] words = condition.split(" ");
            try {
                for (int i = 0; i < words.length; i++) {
                    String word = words[i++];
                    switch (word) {
                        case "playerHasItem": {
                            word = words[i++];
                            if (ItemsController.findItemInInventory(Integer.parseInt(word), Game.getMap().getPlayer().getInventory()) != null) {
                                return words[i];
                            } else {
                                i++;
                                return words[++i];
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка при определении id следующей фразы в диалоге: " + e);
            }
        }
        return "";
    }

    /**
     * Применить скрипт, срабатывающий при выборе фразы
     * @param script - скрипт
     */
    private void applyPhraseScript(String script) {
        if (script != null && !script.equals("")) {
            String[] words = script.split(" ");
            try {
                Player player = Game.getMap().getPlayer();
                for (int i = 0; i < words.length; i++) {
                    String word = words[i++];
                    switch (word) {
                        case "playerAddItem": {
                            ItemsController.addItem(new Items(Integer.parseInt(words[i++]), Integer.parseInt(words[i++])), player.getInventory(), player);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка при применении скрипта в диалоге: " + e);
            }
        }
    }
}
