package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import static view.params.GameParams.tileSize;

/*
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
        pane.setVisible(Boolean.FALSE);

        textLabel = new Label();
        textLabel.setMaxWidth(90);
        textLabel.setLayoutX(5);
        textLabel.setWrapText(Boolean.TRUE);
        pane.getChildren().add(textLabel);
        Game.getEditor().getItemsPane().getChildren().add(pane);
    }

    public static void showPopover(String text, ImageView imageView) {
        if (!pane.isVisible()) {
            textLabel.setText(text);
            pane.setLayoutY(imageView.getY() + tileSize);
            pane.setLayoutX(imageView.getX());

            pane.setPrefSize(100, textLabel.getHeight());
            pane.setVisible(Boolean.TRUE);
        }
    }

    public static void hidePopover() {
        pane.setVisible(Boolean.FALSE);
    }

    /*
     * Установить родительскую панель
     */
    public static void setPane(Pane parentPane) {
        if (!parentPane.getChildren().contains(pane)) {
            parentPane.getChildren().add(pane);
        }
    }
}
