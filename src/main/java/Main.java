import entity.DirectionEnum;
import entity.GameModeEnum;
import entity.Player;
import entity.map.Creature;
import entity.map.Item;
import entity.map.Map;
import entity.map.NPC;
import editor.Editor;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    private Editor editor;
    private Player player;
    private Map map;
    private GameModeEnum gameMode;
    private final ImageView stopTestGameImage = new ImageView("/graphics/gui/StopTestGame.png");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        player = new Player();
        map = new Map();
        editor = new Editor();
        setGameMode(GameModeEnum.EDITOR);

        primaryStage.setTitle("Game");
        Group root = new Group();
        Canvas canvas = new Canvas(1020, 680); // размеры игрового окна

        canvas.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (gameMode == GameModeEnum.GAME) {
                if (code == KeyCode.D) {
                    player.setDirection(DirectionEnum.RIGHT);
                    player.getImage().setScaleX(1);
                    if (player.getXPosition() < 299 && (editor.getTilesList().getTiles1()
                            .get(map.getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile1Id())
                            .isPassability()) &&
                            (editor.getTilesList().getTiles2()
                                    .get(map.getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile2Id())
                                    .isPassability())) {
                        if (player.getXPosition() < 285) {
                            player.setXPosition(player.getXPosition() + 1);
                        }
                        if (player.getXPosition() + 3 > player.getXMapPos() + 12) {
                            player.setXMapPos(player.getXMapPos() + 1);
                            GraphicsContext gc = canvas.getGraphicsContext2D();
                            map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);
                        } else {
                            player.getImage().setX(player.getImage().getX() + 40);
                        }
                    }
                }
                if (code == KeyCode.A) {
                    player.setDirection(DirectionEnum.LEFT);
                    player.getImage().setScaleX(-1);
                    if (player.getXPosition() > 0 && (editor.getTilesList().getTiles1().get(map.getTiles()[
                            player.getXPosition() - 1][player.getYPosition()].getTile1Id()).isPassability()) &&
                            (editor.getTilesList().getTiles2().get(map.getTiles()[
                                    player.getXPosition() - 1][player.getYPosition()].getTile2Id()).isPassability())) {
                        if (player.getXPosition() > 0) {
                            player.setXPosition(player.getXPosition() - 1);
                        }
                        if (player.getXMapPos() > 0 && player.getXPosition() - 3 < player.getXMapPos()) {
                            player.setXMapPos(player.getXMapPos() - 1);
                            GraphicsContext gc = canvas.getGraphicsContext2D();
                            map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);
                        } else {
                            player.getImage().setX(player.getImage().getX() - 40);
                        }
                    }
                }
                if (code == KeyCode.S) {
                    player.setDirection(DirectionEnum.DOWN);
                    if (player.getYPosition() < 299 && (editor.getTilesList().getTiles1().get(map.getTiles()[
                            player.getXPosition()][player.getYPosition() + 1].getTile1Id()).isPassability()) &&
                            (editor.getTilesList().getTiles2().get(map.getTiles()[
                                    player.getXPosition()][player.getYPosition() + 1].getTile2Id()).isPassability())) {
                        if (player.getYPosition() < 285) {
                            player.setYPosition(player.getYPosition() + 1);
                        }
                        if (player.getYPosition() + 3 > player.getYMapPos() + 12) {
                            player.setYMapPos(player.getYMapPos() + 1);
                            GraphicsContext gc = canvas.getGraphicsContext2D();
                            map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);
                        } else {
                            player.getImage().setY(player.getImage().getY() + 40);
                        }
                    }
                }
                if (code == KeyCode.W) {
                    player.setDirection(DirectionEnum.UP);
                    if (player.getYPosition() > 0 && (editor.getTilesList().getTiles1().get(map.getTiles()[
                            player.getXPosition()][player.getYPosition() - 1].getTile1Id()).isPassability()) &&
                            (editor.getTilesList().getTiles2().get(map.getTiles()[
                                    player.getXPosition()][player.getYPosition() - 1].getTile2Id()).isPassability())) {
                        if (player.getYPosition() > 0) {
                            player.setYPosition(player.getYPosition() - 1);
                        }
                        if (player.getYMapPos() > 0 && player.getYPosition() - 3 < player.getYMapPos()) {
                            player.setYMapPos(player.getYMapPos() - 1);
                            GraphicsContext gc = canvas.getGraphicsContext2D();
                            map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);
                        } else {
                            player.getImage().setY(player.getImage().getY() - 40);
                        }
                    }
                }
            }
        });

        root.getChildren().add(canvas);
        root.getChildren().add(player.getImage());
        editor.drawTiles(root);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);

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
        saveMapImage.setOnMousePressed(event -> {
            map.saveMap(mapNameTextField.getText());
        });
        editor.getButtonsPane().getChildren().add(saveMapImage);

        ImageView loadMapImage = new ImageView("/graphics/gui/LoadMap.png");
        loadMapImage.setLayoutX(295);
        loadMapImage.setLayoutY(5);
        loadMapImage.setOnMousePressed(event -> {
            map = map.loadMap(mapNameTextField.getText());
            map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);
        });
        editor.getButtonsPane().getChildren().add(loadMapImage);

        ImageView startTestGameImage = new ImageView("/graphics/gui/StartTestGame.png");
        startTestGameImage.setLayoutX(330);
        startTestGameImage.setLayoutY(5);
        startTestGameImage.setOnMousePressed(event -> setGameMode(GameModeEnum.GAME));
        editor.getButtonsPane().getChildren().add(startTestGameImage);

        stopTestGameImage.setLayoutX(5);
        stopTestGameImage.setLayoutY(610);
        stopTestGameImage.setOnMousePressed(event -> setGameMode(GameModeEnum.EDITOR));
        stopTestGameImage.setVisible(Boolean.FALSE);
        root.getChildren().add(stopTestGameImage);

        Label mapInfoLabel = new Label("");
        mapInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        mapInfoLabel.setLayoutX(380);
        mapInfoLabel.setLayoutY(610);
        root.getChildren().add(mapInfoLabel);

        root.setOnMousePressed(event -> drawTileOnMap(event.getX(), event.getY(), root, canvas));
        root.setOnMouseDragged(event -> drawTileOnMap(event.getX(), event.getY(), root, canvas));
        root.setOnMouseMoved(event -> showMapPointInfo(event.getX(), event.getY(), mapInfoLabel));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        canvas.requestFocus();
    }

    private void showMapPointInfo(double x, double y, Label mapInfoLabel) {
        if (x < 600 && y < 600) {
            int xPos = player.getXMapPos() + (((int) x) / 40);
            int yPos = player.getYMapPos() + (((int) y) / 40);
            mapInfoLabel.setText(
               "X: " + xPos + ", Y: " + yPos + ". " +
               editor.getTilesList().getTiles1().get(map.getTiles()[xPos][yPos].getTile1Id()).getDesc() +
               (map.getTiles()[xPos][yPos].getTile2Id() == 0 ? "" : ", " +
               editor.getTilesList().getTiles2().get(map.getTiles()[xPos][yPos].getTile2Id()).getDesc().toLowerCase()));
        } else {
            mapInfoLabel.setText("");
        }
    }

    public void drawTileOnMap(double x, double y, Group root, Canvas canvas) {
        if (x < 600 && y < 600) {
            switch (editor.getSelectedType()) {
                case GROUND: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].setTile1Id(editor.getSelectTile());
                    break;
                }
                case OBJECT: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].setTile2Id(editor.getSelectTile());
                    break;
                }
                case NPC: {
                    if (editor.getSelectTile() == 0) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].setNpcId(null);
                    } else if (map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].getNpcId() == null) {
                        map.getNpcList().add(new NPC(editor.getSelectTile(), map.getNpcList().size(),
                                player.getXMapPos() + ((((int) x)) / 40),
                                player.getYMapPos() + ((((int) y)) / 40)));
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].
                                setNpcId(map.getNpcList().get(map.getNpcList().size() - 1).getId());
                    }
                    break;
                }
                case CREATURE: {
                    if (editor.getSelectTile() == 0) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].setCreatureId(null);
                    } else if (map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].getCreatureId() == null) {
                        map.getCreaturesList().add(new Creature(editor.getSelectTile(), map.getCreaturesList().size(),
                                player.getXMapPos() + ((((int) x)) / 40),
                                player.getYMapPos() + ((((int) y)) / 40)));

                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].
                                setCreatureId(map.getCreaturesList().get(map.getCreaturesList().size() - 1).getId());
                    }
                    break;
                }
                case ITEM: {
                    if (editor.getSelectTile() == 0) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].setItems(null);
                    } else if (map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].getItems() == null) {
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].setItems(new ArrayList<>());
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].getItems().add(new Item(editor.getSelectTile(), 1));
                    }
                    break;
                }
            }


            Canvas canvas2 = ((Canvas) (root.getChildren().get(0)));
            GraphicsContext gc2 = canvas2.getGraphicsContext2D();
            ImageView image = new ImageView("/graphics/tiles/" +
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].getTile1Id() + ".png");
            gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);

            image = new ImageView("/graphics/tiles2/" +
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].getTile2Id() + ".png");
            gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);

            if (map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                    [player.getYMapPos() + ((((int) y)) / 40)].getItems() != null) {
                image = new ImageView("/graphics/items/" +
                        map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].getItems().get(0).getTypeId() + ".png");
                gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);
            }

            if (map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                    [player.getYMapPos() + ((((int) y)) / 40)].getNpcId() != null) {
                image = new ImageView("/graphics/characters/" +
                        map.getNpcList().get(map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].getNpcId()).getNpcTypeId() + ".png");
                gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);
            }

            if (map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                    [player.getYMapPos() + ((((int) y)) / 40)].getCreatureId() != null) {
                image = new ImageView("/graphics/creatures/" +
                        map.getCreaturesList().get(map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                                [player.getYMapPos() + ((((int) y)) / 40)].getCreatureId()).getCreatureTypeId() + ".png");
                gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);
            }

            root.getChildren().set(0, canvas);
            canvas.requestFocus();
        }
    }

    private void setGameMode(GameModeEnum mode) {
        gameMode = mode;
        switch (gameMode) {
            case MENU: {
                break;
            }
            case EDITOR: {
                player.getImage().setVisible(Boolean.FALSE);
                editor.getTabPane().setVisible(Boolean.TRUE);
                editor.getButtonsPane().setVisible(Boolean.TRUE);
                stopTestGameImage.setVisible(Boolean.FALSE);
                break;
            }
            case GAME: {
                player.getImage().setVisible(Boolean.TRUE);
                editor.getTabPane().setVisible(Boolean.FALSE);
                editor.getButtonsPane().setVisible(Boolean.FALSE);
                stopTestGameImage.setVisible(Boolean.TRUE);
                break;
            }
            case PAUSE: {
                break;
            }
        }
    }
}
