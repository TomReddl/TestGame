package game;

import controller.utils.JsonUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import model.entity.DirectionEnum;
import model.entity.GameModeEnum;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.NPC;
import model.entity.player.Player;
import view.Game;
import view.inventory.PlayerStatsPanel;
import view.menu.MainMenu;

import java.util.ArrayList;
import java.util.List;

import static view.params.GameParams.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(Game.getRoot());
        primaryStage.setTitle("Game");
        primaryStage.setResizable(Boolean.FALSE);
        primaryStage.setMaximized(Boolean.FALSE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/gui/Icon.png")));

        var player = Game.getMap().getPlayer();

        player.setPlayerStartItems(player);

        Game.getInventory().filterInventoryTabs(Game.getInventory().getTabPane().getSelectionModel().getSelectedItem());

        Game.getMap().drawCurrentMap();

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            switch (Game.getGameMode()) {
                case GAME: {
                    Game.hideMessage();
                    switch (code) {
                        case D: {
                            if (player.isOverloaded()) {
                                Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                            } else {
                                heroMoveRight(player);
                            }
                            break;
                        }
                        case A: {
                            if (player.isOverloaded()) {
                                Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                            } else {
                                heroMoveLeft(player);
                            }
                            break;
                        }
                        case S: {
                            if (player.isOverloaded()) {
                                Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                            } else {
                                heroMoveDown(player);
                            }
                            break;
                        }
                        case W: {
                            if (player.isOverloaded()) {
                                Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                            } else {
                                heroMoveUp(player);
                            }
                            break;
                        }
                        case ESCAPE: {
                            MainMenu.getPane().setVisible(Boolean.TRUE);
                            Game.setGameMode(GameModeEnum.GAME_MENU);
                            break;
                        }
                        case I: {
                            Game.getGameMenu().showGameMenuPanel("0");
                            break;
                        }
                        case C: {
                            player.dropSelectItems();
                            break;
                        }
                        case P: {
                            Game.getGameMenu().showGameMenuPanel("1");
                            break;
                        }
                        case E: {
                            Robot robot = new Robot();
                            int x = ((int) (robot.getMousePosition().getX() - primaryStage.getX()) / tileSize);
                            int y = ((int) (robot.getMousePosition().getY() - primaryStage.getY() - headerSize) / tileSize);
                            if (isReachable(player, x, y)) {
                                List<Items> itemsList = Game.getMap().getTiles()[player.getXMapPos() + x]
                                        [player.getYMapPos() + y].getItems();
                                if (itemsList != null) {
                                    List<Items> removeList = new ArrayList<>();
                                    for (Items items : itemsList) {
                                        if (Game.getMap().getPlayer().addItem(items)) {
                                            removeList.add(items);
                                        } else {
                                            break;
                                        }
                                    }
                                    Game.getMap().getTiles()[player.getXMapPos() + x]
                                            [player.getYMapPos() + y].getItems().removeAll(removeList);
                                    if (Game.getMap().getTiles()[player.getXMapPos() + x]
                                            [player.getYMapPos() + y].getItems().size() == 0) {
                                        Game.getMap().getTiles()[player.getXMapPos() + x]
                                                [player.getYMapPos() + y].setItems(null);
                                    } else {
                                        Game.showMessage(Game.getText("ERROR_INVENTORY_SPACE"));
                                    }
                                    Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(), x, y);
                                } else {
                                    Game.getMap().interactionWithMap(player.getXMapPos(), player.getYMapPos(), x, y);
                                }
                            }
                        }
                    }
                    break;
                }
                case EDITOR: {
                    switch (code) {
                        case W: {
                            player.setYMapPos(Math.max(player.getYMapPos() - viewSize, 0));
                            Game.getMap().drawCurrentMap();
                            break;
                        }
                        case S: {
                            player.setYMapPos(Math.min(player.getYMapPos() + viewSize, mapSize - viewSize));
                            Game.getMap().drawCurrentMap();
                            break;
                        }
                        case D: {
                            player.setXMapPos(Math.min(player.getXMapPos() + viewSize, mapSize - viewSize));
                            Game.getMap().drawCurrentMap();
                            break;
                        }
                        case A: {
                            player.setXMapPos(Math.max(player.getXMapPos() - viewSize, 0));
                            Game.getMap().drawCurrentMap();
                            break;
                        }
                        case ESCAPE: {
                            MainMenu.getPane().setVisible(Boolean.FALSE);
                            Game.setGameMode(GameModeEnum.GAME_MENU);
                            break;
                        }
                        case Z: {
                            Game.getEditor().setShowZones(!Game.getEditor().isShowZones());
                            Game.getEditor().getShowZonesCheckBox().setSelected(Game.getEditor().isShowZones());
                            Game.getMap().drawCurrentMap();
                            break;
                        }
                    }
                    break;
                }
                case GAME_MENU: {
                    switch (code) {
                        case ESCAPE: {
                            Game.setGameMode(GameModeEnum.GAME);
                            break;
                        }
                    }
                    break;
                }
            }
        });

        Label mapNameLabel = new Label(Game.getText("MAP_NAME"));
        mapNameLabel.setLayoutX(5);
        mapNameLabel.setLayoutY(10);
        Game.getEditor().getButtonsPane().getChildren().add(mapNameLabel);

        TextField mapNameTextField = new TextField();
        mapNameTextField.setLayoutX(105);
        mapNameTextField.setLayoutY(5);
        mapNameTextField.setText(Game.getMap().getMapName());
        mapNameTextField.setFocusTraversable(Boolean.FALSE);
        mapNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d|\\.*")) {
                mapNameTextField.setText(newValue.replaceAll("[^\\d|.]", ""));
            }
        });
        Game.getEditor().getButtonsPane().getChildren().add(mapNameTextField);

        ImageView saveMapImage = new ImageView("/graphics/gui/SaveMap.png");
        saveMapImage.setLayoutX(260);
        saveMapImage.setLayoutY(5);
        saveMapImage.setOnMousePressed(event -> JsonUtils.saveMap(mapNameTextField.getText(), Game.getMap()));
        Game.getEditor().getButtonsPane().getChildren().add(saveMapImage);

        ImageView loadMapImage = new ImageView("/graphics/gui/LoadMap.png");
        loadMapImage.setLayoutX(295);
        loadMapImage.setLayoutY(5);
        loadMapImage.setOnMousePressed(event -> {
            Game.setMap(JsonUtils.loadMap(mapNameTextField.getText()));
            Game.getMap().drawCurrentMap();
        });
        Game.getEditor().getButtonsPane().getChildren().add(loadMapImage);

        ImageView startTestGameImage = new ImageView("/graphics/gui/StartTestGame.png");
        startTestGameImage.setLayoutX(330);
        startTestGameImage.setLayoutY(5);
        startTestGameImage.setOnMousePressed(event -> Game.setGameMode(GameModeEnum.GAME));
        Game.getEditor().getButtonsPane().getChildren().add(startTestGameImage);

        Game.getStopTestGameImage().setLayoutX(5);
        Game.getStopTestGameImage().setLayoutY(610);
        Game.getStopTestGameImage().setOnMousePressed(event -> Game.setGameMode(GameModeEnum.EDITOR));
        Game.getStopTestGameImage().setVisible(Boolean.FALSE);
        Game.getRoot().getChildren().add(Game.getStopTestGameImage());

        Game.getRoot().setOnMousePressed(event -> drawTileOnMap(event.getX(), event.getY(), Game.getEditor().getCanvas()));
        Game.getRoot().setOnMouseDragged(event -> drawTileOnMap(event.getX(), event.getY(), Game.getEditor().getCanvas()));
        Game.getRoot().setOnMouseMoved(event -> showMapPointInfo(event.getX(), event.getY(), Game.getEditor().getMapInfoLabel()));
        primaryStage.setScene(scene);
        primaryStage.show();
        Game.getEditor().getCanvas().requestFocus();
    }

    private Boolean isReachable(Player player, double x, double y) {
        return (Math.abs(player.getXPosition() - (player.getXMapPos() + x)) < 2) &&
                (Math.abs(player.getYPosition() - (player.getYMapPos() + y)) < 2);
    }

    private void heroMoveUp(Player player) {
        Game.getMap().getPlayer().setDirection(DirectionEnum.UP);
        if (player.getYPosition() > 0 && (Game.getEditor().getTiles1().get(Game.getMap().getTiles()[
                player.getXPosition()][player.getYPosition() - 1].getTile1Id()).isPassability()) &&
                (Game.getEditor().getTiles2().get(Game.getMap().getTiles()[
                        player.getXPosition()][player.getYPosition() - 1].getTile2Id()).isPassability())) {
            if (player.getYPosition() > 0) {
                player.setYPosition(player.getYPosition() - 1);
            }
            if (player.getYMapPos() > 0 && player.getYPosition() - 3 < player.getYMapPos()) {
                player.setYMapPos(player.getYMapPos() - 1);
                Game.getMap().drawCurrentMap();
            } else {
                player.setYViewPos(player.getYViewPos() - 1);
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos() + 1);
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos());
            }
        }
    }

    private void heroMoveDown(Player player) {
        Game.getMap().getPlayer().setDirection(DirectionEnum.DOWN);
        if (player.getYPosition() < mapSize && (Game.getEditor().getTiles1().get(Game.getMap().getTiles()[
                player.getXPosition()][player.getYPosition() + 1].getTile1Id()).isPassability()) &&
                (Game.getEditor().getTiles2().get(Game.getMap().getTiles()[
                        player.getXPosition()][player.getYPosition() + 1].getTile2Id()).isPassability())) {
            if (player.getYPosition() < mapSize - viewSize) {
                player.setYPosition(player.getYPosition() + 1);
            }
            if (player.getYPosition() + 3 > player.getYMapPos() + 12) {
                player.setYMapPos(player.getYMapPos() + 1);
                Game.getMap().drawCurrentMap();
            } else {
                player.setYViewPos(player.getYViewPos() + 1);
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos() - 1);
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos());
            }
        }
    }

    private void heroMoveLeft(Player player) {
        Game.getMap().getPlayer().setDirection(DirectionEnum.LEFT);
        if (player.getXPosition() > 0 && (Game.getEditor().getTiles1().get(Game.getMap().getTiles()[
                player.getXPosition() - 1][player.getYPosition()].getTile1Id()).isPassability()) &&
                (Game.getEditor().getTiles2().get(Game.getMap().getTiles()[
                        player.getXPosition() - 1][player.getYPosition()].getTile2Id()).isPassability())) {
            if (player.getXPosition() > 0) {
                player.setXPosition(player.getXPosition() - 1);
            }
            if (player.getXMapPos() > 0 && player.getXPosition() - 3 < player.getXMapPos()) {
                player.setXMapPos(player.getXMapPos() - 1);
                Game.getMap().drawCurrentMap();
            } else {
                player.setXViewPos(player.getXViewPos() - 1);
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos() + 1, player.getYViewPos());
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos());
            }
        }
    }

    private void heroMoveRight(Player player) {
        Game.getMap().getPlayer().setDirection(DirectionEnum.RIGHT);
        if (player.getXPosition() < mapSize && (Game.getEditor().getTiles1()
                .get(Game.getMap().getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile1Id())
                .isPassability()) &&
                (Game.getEditor().getTiles2()
                        .get(Game.getMap().getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile2Id())
                        .isPassability())) {
            if (player.getXPosition() < mapSize - viewSize) {
                player.setXPosition(player.getXPosition() + 1);
            }
            if (player.getXPosition() + 3 > player.getXMapPos() + 12) {
                player.setXMapPos(player.getXMapPos() + 1);
                Game.getMap().drawCurrentMap();
            } else {
                player.setXViewPos(player.getXViewPos() + 1);
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos() - 1, player.getYViewPos());
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos());
            }
        }
    }

    private void showMapPointInfo(double x, double y, Label mapInfoLabel) {
        var player = Game.getMap().getPlayer();
        if (x < viewSize * tileSize && y < viewSize * tileSize) {
            int xPos = player.getXMapPos() + (((int) x) / tileSize);
            int yPos = player.getYMapPos() + (((int) y) / tileSize);
            mapInfoLabel.setText(
                    "X: " + xPos + ", Y: " + yPos + ". " +
                            Game.getEditor().getTiles1().get(Game.getMap().getTiles()[xPos][yPos].getTile1Id()).getDesc() +
                            (Game.getMap().getTiles()[xPos][yPos].getTile2Id() == 0 ? "" : ", " +
                                    Game.getEditor().getTiles2().get(Game.getMap().getTiles()[xPos][yPos].getTile2Id()).getDesc().toLowerCase()));
        } else {
            mapInfoLabel.setText("");
        }
    }

    public void drawTileOnMap(double x, double y, Canvas canvas) {
        if (Game.getGameMode().equals(GameModeEnum.EDITOR)) {
            var player = Game.getMap().getPlayer();
            if (x < viewSize * tileSize && y < viewSize * tileSize) {
                var tileX = (((int) x) / tileSize);
                var tileY = (((int) y) / tileSize);
                switch (Game.getEditor().getSelectedType()) {
                    case GROUND: {
                        Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                [player.getYMapPos() + tileY].setTile1Id(Game.getEditor().getSelectTile());
                        break;
                    }
                    case OBJECT: {
                        Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                [player.getYMapPos() + tileY].setTile2Id(Game.getEditor().getSelectTile());
                        break;
                    }
                    case NPC: {
                        if (Game.getEditor().getSelectTile() == 0) {
                            Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                    [player.getYMapPos() + tileY].setNpcId(null);
                        } else if (Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                [player.getYMapPos() + tileY].getNpcId() == null) {
                            Game.getMap().getNpcList().add(new NPC(Game.getEditor().getSelectTile(), Game.getMap().getNpcList().size(),
                                    player.getXMapPos() + tileX,
                                    player.getYMapPos() + tileY));
                            Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                    [player.getYMapPos() + tileY].
                                    setNpcId(Game.getMap().getNpcList().get(Game.getMap().getNpcList().size() - 1).getId());
                        }
                        break;
                    }
                    case CREATURE: {
                        if (Game.getEditor().getSelectTile() == 0) {
                            Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                    [player.getYMapPos() + tileY].setCreatureId(null);
                        } else if (Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                [player.getYMapPos() + tileY].getCreatureId() == null) {
                            Game.getMap().getCreaturesList().add(new Creature(Game.getEditor().getSelectTile(), Game.getMap().getCreaturesList().size(),
                                    player.getXMapPos() + tileX,
                                    player.getYMapPos() + tileY));

                            Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                    [player.getYMapPos() + tileY].
                                    setCreatureId(Game.getMap().getCreaturesList().get(Game.getMap().getCreaturesList().size() - 1).getId());
                        }
                        break;
                    }
                    case ITEM: {
                        Game.getMap().addItemOnMap(
                                player.getXMapPos() + tileX,
                                player.getYMapPos() + tileY,
                                new Items(Game.getEditor().getSelectTile(), 1));
                        break;
                    }
                    case POLLUTION: {
                        Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                [player.getYMapPos() + tileY].setPollutionId(Game.getEditor().getSelectTile());
                        break;
                    }
                    case ZONE: {
                        Game.getMap().getTiles()[player.getXMapPos() + tileX]
                                [player.getYMapPos() + tileY].setZoneId(Game.getEditor().getSelectTile());
                        break;
                    }
                }

                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(), tileX, tileY);

                canvas.requestFocus();
            }
        }
    }
}
