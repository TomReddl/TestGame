package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Кастомное всплывающее окно с текстом для элементов javaFX, не поддерживающих подсказки
 */
public class Popover {
    @Getter
    private static final Pane pane;
    @Getter
    private static final Label textLabel;

    static {
        pane = new Pane();
        pane.setPrefSize(100, 20);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:black;");
        pane.setVisible(false);

        textLabel = new Label();
        textLabel.setMaxWidth(90);
        textLabel.setLayoutX(5);
        textLabel.setWrapText(true);
        pane.getChildren().add(textLabel);
        Editor.getItemsPane().getChildren().add(pane);
    }

    /**
     * Показать окно всплывающей подсказки
     */
    public static void showPopover(String text, double XPos, double Ypos) {
        if (!pane.isVisible()) {
            textLabel.setText(text);
            pane.setLayoutX(XPos);
            pane.setLayoutY(Ypos);

            //TODO исправить установку высоты поповера
            pane.setPrefSize(100, textLabel.getHeight());
            pane.setVisible(true);
        }
    }

    public static void hidePopover() {
        pane.setVisible(false);
    }

    /**
     * Установить родительскую панель
     */
    public static void setPane(Pane parentPane) {
        if (!parentPane.getChildren().contains(pane)) {
            parentPane.getChildren().add(pane);
        }
    }
}
