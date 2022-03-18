package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import model.editor.items.ItemInfo;
import model.entity.map.Items;
import view.Game;

/*
 * Панель детальной информации о предмете
 */
public class ItemDetailPanel {
    @Getter
    private static final Pane pane;
    @Getter
    private static final Label descLabel;
    @Getter
    private static Items selectItem;

    static {
        pane = new Pane();
        pane.setPrefSize(300, 300);
        pane.setLayoutX(705);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(Boolean.FALSE);

        descLabel = new Label();
        descLabel.setLayoutX(5);
        descLabel.setLayoutY(5);
        descLabel.setMaxWidth(290);
        descLabel.setWrapText(Boolean.TRUE);
        pane.getChildren().add(descLabel);
    }

    public static void showDetailPanel(Items item, double y) {
        selectItem = item;
        descLabel.setText(item.getInfo().getDesc());
        pane.setLayoutY(y + 65);
        pane.setVisible(Boolean.TRUE);
    }

    public static void hideDetailPanel() {
        selectItem = null;
        pane.setVisible(Boolean.FALSE);
    }
}
