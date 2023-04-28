package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;

/**
 * Панель крафта. Применяется для множества тайлов (наковальни, плавильни, столярные мастерские, ткацкие станки и т.д.)
 */
public class CraftPanel {
    @Getter
    private final Pane pane;
    @Getter
    private final Label nameLabel; // название дубликатора

    public CraftPanel() {
        pane = new Pane();
        pane.setPrefSize(230, 210);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        nameLabel = new Label();
        nameLabel.setLayoutX(10);
        nameLabel.setLayoutY(10);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nameLabel.setText(Editor.getTiles2().get(251).getName());
        pane.getChildren().add(nameLabel);

        Game.getRoot().getChildren().add(pane);
    }

    public void showPanel(boolean visible) {
        pane.setVisible(visible);
    }
}
