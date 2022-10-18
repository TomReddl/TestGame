package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import view.Game;

/*
 * Панель выбора количества предметов
 */
public class ItemCountPanel {
    @Getter
    private static final Pane pane;

    private static final Slider slider;
    @Getter
    private static final Button selectButton;
    @Getter
    private static final Button backButton;

    static {
        pane = new Pane();
        pane.setPrefSize(200, 200);
        pane.setLayoutX(765);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);
        Game.getRoot().getChildren().add(pane);

        slider = new Slider();
        pane.getChildren().add(slider);

        selectButton = new Button(Game.getText("OK"));
        pane.getChildren().add(selectButton);

        backButton = new Button(Game.getText("BACK"));
        pane.getChildren().add(backButton);
    }
}
