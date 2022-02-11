package game;

import editor.Editor;
import entity.DirectionEnum;
import entity.GameModeEnum;
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
import lombok.Getter;
import lombok.Setter;
import menu.MainMenu;
import utils.JsonUtils;

import java.util.ArrayList;

public class Main extends Application {
    private final Group root = new Group();
    private final Editor editor = new Editor(root);
    private Map map= new Map();
    private final MainMenu mainMenu = new MainMenu(root);
    @Getter
    @Setter
    private static GameModeEnum gameMode = GameModeEnum.EDITOR;
    private final ImageView stopTestGameImage = new ImageView("/graphics/gui/StopTestGame.png");

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
        map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);

        editor.getCanvas().setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (gameMode == GameModeEnum.GAME) {
                switch (code) {
                    case D: {
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
                                map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                            } else {
                                player.setXViewPos(player.getXViewPos() + 1);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos() - 1, player.getYViewPos(), editor);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos(), player.getYViewPos(), editor);
                            }
                        }
                        break;
                    }
                    case A: {
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
                                map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                            } else {
                                player.setXViewPos(player.getXViewPos() - 1);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos() + 1, player.getYViewPos(), editor);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos(), player.getYViewPos(), editor);
                            }
                        }
                        break;
                    }
                    case S: {
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
                                map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                            } else {
                                player.setYViewPos(player.getYViewPos() + 1);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos(), player.getYViewPos() - 1, editor);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos(), player.getYViewPos(), editor);
                            }
                        }
                        break;
                    }
                    case W: {
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
                                map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                            } else {
                                player.setYViewPos(player.getYViewPos() - 1);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos(), player.getYViewPos() + 1, editor);
                                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                                        player.getXViewPos(), player.getYViewPos(), editor);
                            }
                        }
                        break;
                    }
                    case ESCAPE: {
                        mainMenu.getMenuPane().setVisible(Boolean.TRUE);
                        setGameMode(GameModeEnum.GAME_MENU);
                        break;
                    }
                }
            } else if (gameMode.equals(GameModeEnum.EDITOR)) {
                switch (code) {
                    case W: {
                        player.setYMapPos(Math.max(player.getYMapPos() - 15, 0));
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                        break;
                    }
                    case S: {
                        player.setYMapPos(Math.min(player.getYMapPos() + 15, 285));
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                        break;
                    }
                    case D: {
                        player.setXMapPos(Math.min(player.getXMapPos() + 15, 285));
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                        break;
                    }
                    case A: {
                        player.setXMapPos(Math.max(player.getXMapPos() - 15, 0));
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
                        break;
                    }
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
            map.drawMap(player.getXMapPos(), player.getYMapPos(), editor);
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

    private void showMapPointInfo(double x, double y, Label mapInfoLabel) {
        var player = map.getPlayer();
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
        var player = map.getPlayer();
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
                case POLLUTION: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].setPollutionId(editor.getSelectTile());
                    break;
                }
                case ZONE: {
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].setZoneId(editor.getSelectTile());
                    break;
                }
            }

            map.drawTile(player.getXMapPos(), player.getYMapPos(), ((((int) x)) / 40), ((((int) y)) / 40), editor);

            root.getChildren().set(0, canvas);
            canvas.requestFocus();
        }
    }

    public void setGameMode(GameModeEnum mode) {
        gameMode = mode;
        var player = map.getPlayer();
        switch (gameMode) {
            case MAIN_MENU: {
                mainMenu.getMenuPane().setVisible(Boolean.TRUE);
                break;
            }
            case EDITOR: {
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), editor);
                editor.getTabPane().setVisible(Boolean.TRUE);
                editor.getButtonsPane().setVisible(Boolean.TRUE);
                stopTestGameImage.setVisible(Boolean.FALSE);
                mainMenu.getMenuPane().setVisible(Boolean.FALSE);
                break;
            }
            case GAME: {
                map.drawTile(player.getXMapPos(), player.getYMapPos(),
                        player.getXViewPos(), player.getYViewPos(), editor);
                editor.getTabPane().setVisible(Boolean.FALSE);
                editor.getButtonsPane().setVisible(Boolean.FALSE);
                stopTestGameImage.setVisible(Boolean.TRUE);
                mainMenu.getMenuPane().setVisible(Boolean.FALSE);
                break;
            }
            case GAME_MENU: {
                break;
            }
        }
    }
}
