package view.params;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

/*
 * Панель детальной информации о параметре
 */
public class ParamDescPanel {
    @Getter
    private static final Pane pane;
    @Getter
    private static final Label descLabel;

    static {
        pane = new Pane();
        pane.setPrefSize(150, 150);
        pane.setLayoutX(705);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:black;");
        pane.setVisible(false);

        descLabel = new Label();
        descLabel.setLayoutX(5);
        descLabel.setLayoutY(5);
        descLabel.setMaxWidth(140);
        descLabel.setWrapText(true);
        pane.getChildren().add(descLabel);
        ParamPanel.getPane().getChildren().add(pane);
    }

    public static void showDetailPanel(String desc, HBox box) {
        descLabel.setText(desc);
        if (ParamPanel.getSkillsPane().getChildren().contains(box)) {
            pane.setLayoutY(box.getLayoutY() + 20 + ParamPanel.getScrollPane().getLayoutY() -
                    ParamPanel.getScrollPane().getVvalue());
            pane.setLayoutX(box.getLayoutX() + ParamPanel.getScrollPane().getLayoutX());
        } else {
            pane.setLayoutY(box.getLayoutY() + 20);
            pane.setLayoutX(box.getLayoutX());
        }
        pane.setMaxHeight(descLabel.getHeight() + 10);
        pane.setVisible(true);
    }

    public static void hideDetailPanel() {
        pane.setVisible(false);
    }
}
