import entity.map.Creature;
import entity.map.Map;
import entity.map.NPC;
import entity.Player;
import gui.Editor;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {
    private Editor editor;
    private Player player;
    private Map map;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        player = new Player();
        map = new Map();
        editor = new Editor();

        primaryStage.setTitle("Game");
        Group root = new Group();
        Canvas canvas = new Canvas(1020, 680); // размеры игрового окна

        canvas.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.D) {
                if (player.getXPosition() < 299 && (editor.getTilesList().getGroundTiles()
                        .get(map.getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile1Id())
                        .isPassability()) &&
                        (editor.getTilesList().getGameObjects()
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
                if (player.getXPosition() > 0 && (editor.getTilesList().getGroundTiles().get(map.getTiles()[
                        player.getXPosition() - 1][player.getYPosition()].getTile1Id()).isPassability()) &&
                        (editor.getTilesList().getGameObjects().get(map.getTiles()[
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
                if (player.getYPosition() < 299 && (editor.getTilesList().getGroundTiles().get(map.getTiles()[
                        player.getXPosition()][player.getYPosition() + 1].getTile1Id()).isPassability()) &&
                        (editor.getTilesList().getGameObjects().get(map.getTiles()[
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
                if (player.getYPosition() > 0 && (editor.getTilesList().getGroundTiles().get(map.getTiles()[
                        player.getXPosition()][player.getYPosition() - 1].getTile1Id()).isPassability()) &&
                        (editor.getTilesList().getGameObjects().get(map.getTiles()[
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
        });

        root.getChildren().add(canvas);
        root.getChildren().add(player.getImage());
        editor.drawTiles(root);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);

        Label mapNameLabel = new Label("Название карты:");
        mapNameLabel.setLayoutX(5);
        mapNameLabel.setLayoutY(610);
        root.getChildren().add(mapNameLabel);

        TextField mapNameTextField = new TextField();
        mapNameTextField.setLayoutX(105);
        mapNameTextField.setLayoutY(605);
        mapNameTextField.setText(map.getMapName());
        root.getChildren().add(mapNameTextField);

        ImageView saveMapImage = new ImageView("/graphics/gui/SaveMap.png");
        saveMapImage.setLayoutX(260);
        saveMapImage.setLayoutY(605);
        saveMapImage.setOnMousePressed(event -> {
            map.saveMap(mapNameTextField.getText());
        });
        root.getChildren().add(saveMapImage);

        ImageView loadMapImage = new ImageView("/graphics/gui/LoadMap.png");
        loadMapImage.setLayoutX(295);
        loadMapImage.setLayoutY(605);
        loadMapImage.setOnMousePressed(event -> {
            map = map.loadMap(mapNameTextField.getText());
            map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, editor);
        });
        root.getChildren().add(loadMapImage);

        root.setOnMousePressed(event -> drawTileOnMap(event.getX(), event.getY(), root, canvas));
        root.setOnMouseDragged(event -> drawTileOnMap(event.getX(), event.getY(), root, canvas));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        canvas.requestFocus();
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
                    map.getNpcList().add(new NPC(editor.getSelectTile(), map.getNpcList().size(),
                            player.getXMapPos() + ((((int) x)) / 40),
                            player.getYMapPos() + ((((int) y)) / 40)));
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].
                            setNpcId(map.getNpcList().get(map.getNpcList().size()-1).getId());
                    break;
                }
                case CREATURE: {
                    map.getCreaturesList().add(new Creature(editor.getSelectTile(), map.getCreaturesList().size(),
                            player.getXMapPos() + ((((int) x)) / 40),
                            player.getYMapPos() + ((((int) y)) / 40)));

                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].
                            setCreatureId(map.getCreaturesList().get(map.getCreaturesList().size()-1).getId());
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
}
