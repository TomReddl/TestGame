package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import model.entity.map.ClosableCellInfo;

import java.util.ArrayList;
import java.util.List;

/*
 * Панель открытия кодового замка
 */
public class CodeLockPanel {
    @Getter
    private static final Pane pane;
    private static final Label descLabel = new Label();
    private static final Button closeButton = new Button(Game.getText("CLOSE"));

    private static ClosableCellInfo currentCellInfo = null;
    private static final List<CodeButton> codeButtons = new ArrayList<>();


    private static class CodeButton {
        private final ImageView charImage;
        private final ImageView upImage;
        private final ImageView downImage;
        private final Label charLabel;

        public CodeButton(int index, double XPos, double YPos) {
            charImage = new ImageView("/graphics/gui/locks/keyBackground.png");
            upImage = new ImageView("/graphics/gui/locks/keyUp.png");
            downImage = new ImageView("/graphics/gui/locks/keyDown.png");

            charLabel = new Label("A");
            charLabel.setFont(Font.font("Kingthings Petrock", FontWeight.BOLD, 24));
            charLabel.setTextFill(Color.web("#4a3710"));

            charImage.setLayoutX(XPos);
            charImage.setLayoutY(YPos);

            upImage.setLayoutX(XPos);
            upImage.setLayoutY(YPos - 20);
            upImage.setOnMouseClicked(event -> upChar(index));

            downImage.setLayoutX(XPos);
            downImage.setLayoutY(YPos + charImage.getImage().getHeight());
            downImage.setOnMouseClicked(event -> downChar(index));

            charLabel.setLayoutX(XPos + 10);
            charLabel.setLayoutY(YPos + 5);

            pane.getChildren().add(charImage);
            pane.getChildren().add(upImage);
            pane.getChildren().add(downImage);
            pane.getChildren().add(charLabel);
        }
    }

    static {
        pane = new Pane();
        pane.setPrefSize(280, 170);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        descLabel.setLayoutX(20);
        descLabel.setLayoutY(5);
        descLabel.setFont(Font.font("Kingthings Petrock", FontWeight.BOLD, 16));
        descLabel.setTextFill(Color.web("#4a3710"));
        pane.getChildren().add(descLabel);

        for (int i = 0; i < 7; i++) {
            codeButtons.add(new CodeButton(i,20 + i * 35, 50));
        }

        closeButton.setLayoutX(20);
        closeButton.setLayoutY(130);
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> hidePanel());
        closeButton.setFont(Font.font("Kingthings Petrock", FontWeight.BOLD, 16));
        closeButton.setTextFill(Color.web("#4a3710"));
        pane.getChildren().add(closeButton);

        Game.getRoot().getChildren().add(pane);
    }

    public static void showPanel(ClosableCellInfo cellInfo) {
        currentCellInfo = cellInfo;
        descLabel.setText(currentCellInfo.getCodeHint());

        for (int i = 0; i < 7; i++) {
            var visible = i < currentCellInfo.getCodeForLock().length();
            codeButtons.get(i).charImage.setVisible(visible);
            codeButtons.get(i).charLabel.setVisible(visible);
            codeButtons.get(i).charLabel.setText(currentCellInfo.getCharsForLock().substring(0, 1));
            codeButtons.get(i).downImage.setVisible(visible);
            codeButtons.get(i).upImage.setVisible(visible);
        }
        pane.setVisible(true);
    }

    private static void hidePanel() {
        pane.setVisible(false);
    }

    // прокрутка колеса с символом вверх
    private static void upChar(int index) {
        var currentChar = codeButtons.get(index).charLabel.getText().charAt(0);
        var currentCharIndex = currentCellInfo.getCharsForLock().indexOf(currentChar);
        var newCharIndex = Math.max(--currentCharIndex, 0);
        codeButtons.get(index).charLabel.setText(
                String.valueOf(currentCellInfo.getCharsForLock().charAt(newCharIndex)));
        checkIsOpen();
    }

    // прокрутка колеса с символом вниз
    private static void downChar(int index) {
        var currentChar = codeButtons.get(index).charLabel.getText().charAt(0);
        var currentCharIndex = currentCellInfo.getCharsForLock().indexOf(currentChar);
        var newCharIndex = currentCharIndex < currentCellInfo.getCharsForLock().length()-1 ? ++currentCharIndex : currentCharIndex;
        codeButtons.get(index).charLabel.setText(
                String.valueOf(currentCellInfo.getCharsForLock().charAt(newCharIndex)));
        checkIsOpen();
    }

    // проверяет, подходит ли код для замка
    private static void checkIsOpen() {
        String code = "";
        for (int i = 0; i < 7; i++) {
            if (codeButtons.get(i).charLabel.isVisible()) {
                code = code.concat(codeButtons.get(i).charLabel.getText());
            }
        }
        if (code.equals(currentCellInfo.getCodeForLock())) {
            currentCellInfo.setLocked(false);
            hidePanel();
            Game.showMessage(Game.getText("OPENED_BY_CODE"), Color.GREEN);
        }
    }
}
