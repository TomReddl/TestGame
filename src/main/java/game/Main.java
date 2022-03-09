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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.editor.ItemInfo;
import model.entity.DirectionEnum;
import model.entity.GameModeEnum;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.NPC;
import model.entity.player.Player;
import view.Game;
import view.menu.MainMenu;

import java.util.ArrayList;
import java.util.List;

import static view.params.GameParams.*;

public class Main extends Application {
    public static Game game = new Game();

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
        Game.getMap().drawCurrentMap();

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            switch (Game.getGameMode()) {
                case GAME: {
                    switch (code) {
                        case D: {
                            heroMoveRight(player);
                            break;
                        }
                        case A: {
                            heroMoveLeft(player);
                            break;
                        }
                        case S: {
                            heroMoveDown(player);
                            break;
                        }
                        case W: {
                            heroMoveUp(player);
                            break;
                        }
                        case ESCAPE: {
                            MainMenu.getPane().setVisible(Boolean.TRUE);
                            Game.setGameMode(GameModeEnum.GAME_MENU);
                            break;
                        }
                        case I: {
                            Game.getInventory().showInventory();
                            break;
                        }
                        case P: {
                            Game.getParams().getPane().setVisible(
                                    !Game.getParams().getPane().isVisible()
                            );
                            break;
                        }
                        case E: {
                            Robot robot = new Robot();
                            int x = ((int) (robot.getMousePosition().getX() - primaryStage.getX()) / tileSize);
                            int y = ((int) (robot.getMousePosition().getY() - primaryStage.getY() - headerSize) / tileSize);
                            if ((Math.abs(player.getXPosition() - (player.getXMapPos() + x)) < 2) &&
                                    (Math.abs(player.getYPosition() - (player.getYMapPos() + y)) < 2)) {
                                List<Items> itemsList = Game.getMap().getTiles()[player.getXMapPos() + x]
                                        [player.getYMapPos() + y].getItems();
                                if (itemsList != null) {
                                    for (Items items : itemsList) {
                                        Player.addItem(items, player.getInventory());
                                    }
                                    Game.getMap().getTiles()[player.getXMapPos() + x]
                                            [player.getYMapPos() + y].setItems(null);
                                    Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                                            x, y, Game.getEditor());
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
        Game.getStopTestGameImage().setId("Game.getStopTestGameImage()");
        Game.getRoot().getChildren().add(Game.getStopTestGameImage());

        Game.getRoot().setOnMousePressed(event -> drawTileOnMap(event.getX(), event.getY(), Game.getEditor().getCanvas()));
        Game.getRoot().setOnMouseDragged(event -> drawTileOnMap(event.getX(), event.getY(), Game.getEditor().getCanvas()));
        Game.getRoot().setOnMouseMoved(event -> showMapPointInfo(event.getX(), event.getY(), Game.getEditor().getMapInfoLabel()));
        primaryStage.setScene(scene);
        primaryStage.show();
        Game.getEditor().getCanvas().requestFocus();
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
                        player.getXViewPos(), player.getYViewPos() + 1, Game.getEditor());
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), Game.getEditor());
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
                        player.getXViewPos(), player.getYViewPos() - 1, Game.getEditor());
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), Game.getEditor());
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
                        player.getXViewPos() + 1, player.getYViewPos(), Game.getEditor());
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), Game.getEditor());
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
                        player.getXViewPos() - 1, player.getYViewPos(), Game.getEditor());
                Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), Game.getEditor());
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
        var player = Game.getMap().getPlayer();
        if (x < viewSize * tileSize && y < viewSize * tileSize) {
            switch (Game.getEditor().getSelectedType()) {
                case GROUND: {
                    Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setTile1Id(Game.getEditor().getSelectTile());
                    break;
                }
                case OBJECT: {
                    Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setTile2Id(Game.getEditor().getSelectTile());
                    break;
                }
                case NPC: {
                    if (Game.getEditor().getSelectTile() == 0) {
                        Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setNpcId(null);
                    } else if (Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].getNpcId() == null) {
                        Game.getMap().getNpcList().add(new NPC(Game.getEditor().getSelectTile(), Game.getMap().getNpcList().size(),
                                player.getXMapPos() + ((((int) x)) / tileSize),
                                player.getYMapPos() + ((((int) y)) / tileSize)));
                        Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].
                                setNpcId(Game.getMap().getNpcList().get(Game.getMap().getNpcList().size() - 1).getId());
                    }
                    break;
                }
                case CREATURE: {
                    if (Game.getEditor().getSelectTile() == 0) {
                        Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setCreatureId(null);
                    } else if (Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].getCreatureId() == null) {
                        Game.getMap().getCreaturesList().add(new Creature(Game.getEditor().getSelectTile(), Game.getMap().getCreaturesList().size(),
                                player.getXMapPos() + ((((int) x)) / tileSize),
                                player.getYMapPos() + ((((int) y)) / tileSize)));

                        Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].
                                setCreatureId(Game.getMap().getCreaturesList().get(Game.getMap().getCreaturesList().size() - 1).getId());
                    }
                    break;
                }
                case ITEM: {
                    if (Game.getEditor().getSelectTile() == 0) {
                        Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setItems(null);
                    } else if (Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].getItems() == null) {
                        Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setItems(new ArrayList<>());
                        Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].getItems().add(new Items(Game.getEditor().getSelectTile(), 1));
                    } else {
                        ItemInfo itemInfo = Game.getEditor().getItems().get(Game.getEditor().getSelectTile());
                        boolean found = false;
                        if (itemInfo.getStackable()) {
                            for (Items items : Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                    [player.getYMapPos() + ((((int) y)) / tileSize)].getItems()) {
                                if (items.getTypeId() == Game.getEditor().getSelectTile()) {
                                    items.setCount(items.getCount() + 1);
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                    [player.getYMapPos() + ((((int) y)) / tileSize)].getItems().add(new Items(Game.getEditor().getSelectTile(), 1));
                        }
                    }
                    break;
                }
                case POLLUTION: {
                    Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setPollutionId(Game.getEditor().getSelectTile());
                    break;
                }
                case ZONE: {
                    Game.getMap().getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setZoneId(Game.getEditor().getSelectTile());
                    break;
                }
            }

            Game.getMap().drawTile(player.getXMapPos(), player.getYMapPos(), ((((int) x)) / tileSize), ((((int) y)) / tileSize), Game.getEditor());

            Game.getRoot().getChildren().set(0, canvas);
            canvas.requestFocus();
        }
    }
}
