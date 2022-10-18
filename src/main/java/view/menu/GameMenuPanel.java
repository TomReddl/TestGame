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
import model.entity.map.Items;
import view.Game;
import view.params.ParamPanel;

import java.util.List;

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
        pane.setPrefSize(550, 455);
        pane.setLayoutX(210);
        pane.setLayoutY(5);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        closeMenuButton.setLayoutX(550);
        closeMenuButton.setOnMousePressed(event -> setPanelsVisible(false));
        closeMenuButton.setVisible(true);
        pane.getChildren().add(closeMenuButton);

        tabPane.setPrefSize(550, 490);
        tabPane.setMinHeight(450);
        tabPane.setLayoutX(0);
        tabPane.setLayoutY(0);
        tabPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        var tab = new Tab(Game.getText("INVENTORY"));
        tab.setClosable(false);
        tab.setContent(Game.getInventory().getTabPane());
        tab.setId("0");
        tabPane.getTabs().add(tab);

        tab = new Tab(Game.getText("PARAMS"));
        tab.setClosable(false);
        tab.setContent(ParamPanel.getPane());
        tab.setId("1");
        tabPane.getTabs().add(tab);

        pane.getChildren().add(tabPane);
        root.getChildren().add(pane);
    }

    // Показать панель внутриигрового меню (инвентарь, параметры, индикаторы персонажа)
    public void showGameMenuPanel(String tabName) {
        if (!GameMenuPanel.getPane().isVisible()) {
            setPanelsVisible(true);
        } else if (GameMenuPanel.getTabPane().getSelectionModel().getSelectedItem().getId().equals(tabName)) {
            setPanelsVisible(false);
        }
        GameMenuPanel.getTabPane().getSelectionModel().select(Integer.parseInt(tabName));
    }

    public void showContainerInventory(List<Items> itemsList, int x, int y) {
        showGameMenuPanel("0");
        Game.getContainerInventory().show(itemsList, x, y);
    }

    private void setPanelsVisible(Boolean show) {
        GameMenuPanel.getPane().setVisible(show);
        if (show) {
            Game.getInventory().show(Game.getMap().getPlayer().getInventory(), 0, 0);
            Game.getParams().refreshParamsValueViews();
        } else {
            Game.getInventory().hide();
            Game.getContainerInventory().hide();
        }
    }
}
