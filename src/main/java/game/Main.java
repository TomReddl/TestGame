package game;

import editor.Editor;
import entity.DirectionEnum;
import entity.GameModeEnum;
import entity.Player;
import entity.map.Creature;
import entity.map.Item;
import entity.map.Map;
import entity.map.NPC;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import menu.MainMenu;
import utils.JsonUtils;

import java.util.ArrayList;

import static params.GameParams.*;

public class Main extends Application {
    public static Game game = new Game();

    private final Group root = game.getRoot();
    private final Editor editor = game.getEditor();
    private Map map = game.getMap();
    private final MainMenu mainMenu = game.getMainMenu();
    private final ImageView stopTestGameImage = game.getStopTestGameImage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game");
        primaryStage.setResizable(Boolean.FALSE);
        primaryStage.setMaximized(Boolean.FALSE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/gui/Icon.png")));

        var player = map.getPlayer();
        map.drawCurrentMap();

        editor.getCanvas().setOnKeyReleased(event -> {
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
                            mainMenu.getMenuPane().setVisible(Boolean.TRUE);
                            game.setGameMode(GameModeEnum.GAME_MENU);
                            break;
                        }
                    }
                    break;
                }
                case EDITOR: {
                    switch (code) {
                        case W: {
                            player.setYMapPos(Math.max(player.getYMapPos() - viewSize, 0));
                            map.drawCurrentMap();
                            break;
                        }
                        case S: {
                            player.setYMapPos(Math.min(player.getYMapPos() + viewSize, mapSize-viewSize));
                            map.drawCurrentMap();
                            break;
                        }
                        case D: {
                            player.setXMapPos(Math.min(player.getXMapPos() + viewSize, mapSize-viewSize));
                            map.drawCurrentMap();
                            break;
                        }
                        case A: {
                            player.setXMapPos(Math.max(player.getXMapPos() - viewSize, 0));
                            map.drawCurrentMap();
                            break;
                        }
                        case ESCAPE: {
                            mainMenu.getMenuPane().setVisible(Boolean.FALSE);
                            game.setGameMode(GameModeEnum.GAME_MENU);
                            break;
                        }
                    }
                    break;
                }
                case GAME_MENU: {
                    switch (code) {
                        case ESCAPE: {
                            game.setGameMode(GameModeEnum.GAME);
                            break;
                        }
                    }
                    break;
                }
            }
        });

        Label mapNameLabel = new Label("Название карты:");
        mapNameLabel.setLayoutX(5);
        mapNameLabel.setLayoutY(10);
        editor.getButtonsPane().getChildren().add(mapNameLabel);

        TextField mapNameTextField = new TextField();
        mapNameTextField.setLayoutX(105);
        mapNameTextField.setLayoutY(5);
        mapNameTextField.setText(map.getMapName());
        editor.getButtonsPane().getChildren().add(mapNameTextField);

        ImageView saveMapImage = new ImageView("/graphics/gui/SaveMap.png");
        saveMapImage.setLayoutX(260);
        saveMapImage.setLayoutY(5);
        saveMapImage.setOnMousePressed(event -> JsonUtils.saveMap(mapNameTextField.getText(), map));
        editor.getButtonsPane().getChildren().add(saveMapImage);

        ImageView loadMapImage = new ImageView("/graphics/gui/LoadMap.png");
        loadMapImage.setLayoutX(295);
        loadMapImage.setLayoutY(5);
        loadMapImage.setOnMousePressed(event -> {
            map = JsonUtils.loadMap(mapNameTextField.getText());
            map.drawCurrentMap();
        });
        editor.getButtonsPane().getChildren().add(loadMapImage);

        ImageView startTestGameImage = new ImageView("/graphics/gui/StartTestGame.png");
        startTestGameImage.setLayoutX(330);
        startTestGameImage.setLayoutY(5);
        startTestGameImage.setOnMousePressed(event -> game.setGameMode(GameModeEnum.GAME));
        editor.getButtonsPane().getChildren().add(startTestGameImage);

        stopTestGameImage.setLayoutX(5);
        stopTestGameImage.setLayoutY(610);
        stopTestGameImage.setOnMousePressed(event -> game.setGameMode(GameModeEnum.EDITOR));
        stopTestGameImage.setVisible(Boolean.FALSE);
        stopTestGameImage.setId("stopTestGameImage");
        root.getChildren().add(stopTestGameImage);

        Label mapInfoLabel = new Label("");
        mapInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        mapInfoLabel.setLayoutX(380);
        mapInfoLabel.setLayoutY(610);
        root.getChildren().add(mapInfoLabel);

        root.setOnMousePressed(event -> drawTileOnMap(event.getX(), event.getY(), root, editor.getCanvas()));
        root.setOnMouseDragged(event -> drawTileOnMap(event.getX(), event.getY(), root, editor.getCanvas()));
        root.setOnMouseMoved(event -> showMapPointInfo(event.getX(), event.getY(), mapInfoLabel));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        editor.getCanvas().requestFocus();
    }

    private void heroMoveUp(Player player) {
        map.getPlayer().setDirection(DirectionEnum.UP);
        if (player.getYPosition() > 0 && (editor.getTiles1().get(map.getTiles()[
                player.getXPosition()][player.getYPosition() - 1].getTile1Id()).isPassability()) &&
                (editor.getTiles2().get(map.getTiles()[
                        player.getXPosition()][player.getYPosition() - 1].getTile2Id()).isPassability())) {
            if (player.getYPosition() > 0) {
                player.setYPosition(player.getYPosition() - 1);
            }
            if (player.getYMapPos() > 0 && player.getYPosition() - 3 < player.getYMapPos()) {
                player.setYMapPos(player.getYMapPos() - 1);
                map.drawCurrentMap();
            } else {
                player.setYViewPos(player.getYViewPos() - 1);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos() + 1, editor);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), editor);
            }
        }
    }

    private void heroMoveDown(Player player) {
        map.getPlayer().setDirection(DirectionEnum.DOWN);
        if (player.getYPosition() < mapSize && (editor.getTiles1().get(map.getTiles()[
                player.getXPosition()][player.getYPosition() + 1].getTile1Id()).isPassability()) &&
                (editor.getTiles2().get(map.getTiles()[
                        player.getXPosition()][player.getYPosition() + 1].getTile2Id()).isPassability())) {
            if (player.getYPosition() < mapSize-viewSize) {
                player.setYPosition(player.getYPosition() + 1);
            }
            if (player.getYPosition() + 3 > player.getYMapPos() + 12) {
                player.setYMapPos(player.getYMapPos() + 1);
                map.drawCurrentMap();
            } else {
                player.setYViewPos(player.getYViewPos() + 1);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos() - 1, editor);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), editor);
            }
        }
    }

    private void heroMoveLeft(Player player) {
        map.getPlayer().setDirection(DirectionEnum.LEFT);
        if (player.getXPosition() > 0 && (editor.getTiles1().get(map.getTiles()[
                player.getXPosition() - 1][player.getYPosition()].getTile1Id()).isPassability()) &&
                (editor.getTiles2().get(map.getTiles()[
                        player.getXPosition() - 1][player.getYPosition()].getTile2Id()).isPassability())) {
            if (player.getXPosition() > 0) {
                player.setXPosition(player.getXPosition() - 1);
            }
            if (player.getXMapPos() > 0 && player.getXPosition() - 3 < player.getXMapPos()) {
                player.setXMapPos(player.getXMapPos() - 1);
                map.drawCurrentMap();
            } else {
                player.setXViewPos(player.getXViewPos() - 1);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos() + 1, player.getYViewPos(), editor);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), editor);
            }
        }
    }

    private void heroMoveRight(Player player) {
        map.getPlayer().setDirection(DirectionEnum.RIGHT);
        if (player.getXPosition() < mapSize && (editor.getTiles1()
                .get(map.getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile1Id())
                .isPassability()) &&
                (editor.getTiles2()
                        .get(map.getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile2Id())
                        .isPassability())) {
            if (player.getXPosition() < mapSize-viewSize) {
                player.setXPosition(player.getXPosition() + 1);
            }
            if (player.getXPosition() + 3 > player.getXMapPos() + 12) {
                player.setXMapPos(player.getXMapPos() + 1);
                map.drawCurrentMap();
            } else {
                player.setXViewPos(player.getXViewPos() + 1);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos() - 1, player.getYViewPos(), editor);
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), editor);
            }
        }
    }

    private void showMapPointInfo(double x, double y, Label mapInfoLabel) {
        var player = map.getPlayer();
        if (x < viewSize*tileSize && y < viewSize*tileSize) {
            int xPos = player.getXMapPos() + (((int) x) / tileSize);
            int yPos = player.getYMapPos() + (((int) y) / tileSize);
            mapInfoLabel.setText(
                    "X: " + xPos + ", Y: " + yPos + ". " +
                            editor.getTiles1().get(map.getTiles()[xPos][yPos].getTile1Id()).getDesc() +
                            (map.getTiles()[xPos][yPos].getTile2Id() == 0 ? "" : ", " +
                                    editor.getTiles2().get(map.getTiles()[xPos][yPos].getTile2Id()).getDesc().toLowerCase()));
        } else {
            mapInfoLabel.setText("");
        }
    }

    public void drawTileOnMap(double x, double y, Group root, Canvas canvas) {
        var player = map.getPlayer();
        if (x < viewSize*tileSize && y < viewSize*tileSize) {
            switch (editor.getSelectedType()) {
                case GROUND: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setTile1Id(editor.getSelectTile());
                    break;
                }
                case OBJECT: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setTile2Id(editor.getSelectTile());
                    break;
                }
                case NPC: {
                    if (editor.getSelectTile() == 0) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setNpcId(null);
                    } else if (map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].getNpcId() == null) {
                        map.getNpcList().add(new NPC(editor.getSelectTile(), map.getNpcList().size(),
                                player.getXMapPos() + ((((int) x)) / tileSize),
                                player.getYMapPos() + ((((int) y)) / tileSize)));
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].
                                setNpcId(map.getNpcList().get(map.getNpcList().size() - 1).getId());
                    }
                    break;
                }
                case CREATURE: {
                    if (editor.getSelectTile() == 0) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setCreatureId(null);
                    } else if (map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].getCreatureId() == null) {
                        map.getCreaturesList().add(new Creature(editor.getSelectTile(), map.getCreaturesList().size(),
                                player.getXMapPos() + ((((int) x)) / tileSize),
                                player.getYMapPos() + ((((int) y)) / tileSize)));

                        map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].
                                setCreatureId(map.getCreaturesList().get(map.getCreaturesList().size() - 1).getId());
                    }
                    break;
                }
                case ITEM: {
                    if (editor.getSelectTile() == 0) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setItems(null);
                    } else if (map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].getItems() == null) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].setItems(new ArrayList<>());
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                                [player.getYMapPos() + ((((int) y)) / tileSize)].getItems().add(new Item(editor.getSelectTile(), 1));
                    }
                    break;
                }
                case POLLUTION: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setPollutionId(editor.getSelectTile());
                    break;
                }
                case ZONE: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / tileSize)]
                            [player.getYMapPos() + ((((int) y)) / tileSize)].setZoneId(editor.getSelectTile());
                    break;
                }
            }

            map.drawTile(player.getXMapPos(), player.getYMapPos(), ((((int) x)) / tileSize), ((((int) y)) / tileSize), editor);

            root.getChildren().set(0, canvas);
            canvas.requestFocus();
        }
    }
}
