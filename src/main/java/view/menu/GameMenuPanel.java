package view.menu;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import view.Game;
import view.params.ParamPanel;

/*
 * Панель игрового меню, объединяет панели инвентаря и параметров персонажа
 */
public class GameMenuPanel {
    @Getter
    private static final Pane pane = new Pane();
    @Getter
    private static final TabPane tabPane = new TabPane();
    private static final ImageView closeMenuButton = new ImageView("/graphics/gui/Close.png");

    public GameMenuPanel(Group root) {
        pane.setPrefSize(490, 455);
        pane.setLayoutX(210);
        pane.setLayoutY(5);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(Boolean.FALSE);

        closeMenuButton.setLayoutX(490);
        closeMenuButton.setOnMousePressed(event -> setPanelsVisible(Boolean.FALSE));
        closeMenuButton.setVisible(Boolean.TRUE);
        pane.getChildren().add(closeMenuButton);

        tabPane.setPrefSize(490, 490);
        tabPane.setMinHeight(450);
        tabPane.setLayoutX(0);
        tabPane.setLayoutY(0);
        tabPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        var tab = new Tab(Game.getText("INVENTORY"));
        tab.setClosable(Boolean.FALSE);
        tab.setContent(Game.getInventory().getTabPane());
        tab.setId("0");
        tabPane.getTabs().add(tab);

        tab = new Tab(Game.getText("PARAMS"));
        tab.setClosable(Boolean.FALSE);
        tab.setContent(ParamPanel.getPane());
        tab.setId("1");
        tabPane.getTabs().add(tab);

        pane.getChildren().add(tabPane);
        root.getChildren().add(pane);
    }

    public void showGameMenuPanel(String tabName) {
        if (!GameMenuPanel.getPane().isVisible()) {
            setPanelsVisible(Boolean.TRUE);
        } else if (GameMenuPanel.getTabPane().getSelectionModel().getSelectedItem().getId().equals(tabName)) {
            setPanelsVisible(Boolean.FALSE);
        }
        GameMenuPanel.getTabPane().getSelectionModel().select(Integer.parseInt(tabName));
    }

    private void setPanelsVisible(Boolean show) {
        GameMenuPanel.getPane().setVisible(show);
        Game.getInventory().show(show);
        Game.getParams().show(show);
    }
}
