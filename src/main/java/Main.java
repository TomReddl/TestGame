import entity.Map;
import entity.Player;
import entity.TilesList;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private Player player;
    private Map map;
    private TilesList tilesList;
    private ImageView border;
    Pane pane = new Pane();
    Pane pane2 = new Pane();

    private int selectTile = 0;
    private String selectedType = "tile1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        player = new Player();
        map = new Map();
        tilesList = new TilesList();

        primaryStage.setTitle("Game");
        Group root = new Group();
        Canvas canvas = new Canvas(1000, 700);

        canvas.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.D) {
                if (player.getXPosition() < 299 && (tilesList.getTiles1()
                        .get(map.getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile1Id())
                        .isPassability()) &&
                        (tilesList.getTiles2()
                                .get(map.getTiles()[player.getXPosition() + 1][player.getYPosition()].getTile2Id())
                                .isPassability())) {
                    if (player.getXPosition() < 285) {
                        player.setXPosition(player.getXPosition() + 1);
                    }
                    if (player.getXPosition() + 3 > player.getXMapPos() + 12) {
                        player.setXMapPos(player.getXMapPos() + 1);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, tilesList);
                    } else {
                        player.getImage().setX(player.getImage().getX() + 40);
                    }
                }
            }
            if (code == KeyCode.A) {
                if (player.getXPosition() > 0 && (tilesList.getTiles1().get(map.getTiles()[
                        player.getXPosition() - 1][player.getYPosition()].getTile1Id()).isPassability()) &&
                        (tilesList.getTiles2().get(map.getTiles()[
                                player.getXPosition() - 1][player.getYPosition()].getTile2Id()).isPassability())) {
                    if (player.getXPosition() > 0) {
                        player.setXPosition(player.getXPosition() - 1);
                    }
                    if (player.getXMapPos() > 0 && player.getXPosition() - 3 < player.getXMapPos()) {
                        player.setXMapPos(player.getXMapPos() - 1);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, tilesList);
                    } else {
                        player.getImage().setX(player.getImage().getX() - 40);
                    }
                }
            }
            if (code == KeyCode.S) {
                if (player.getYPosition() < 299 && (tilesList.getTiles1().get(map.getTiles()[
                        player.getXPosition()][player.getYPosition() + 1].getTile1Id()).isPassability()) &&
                        (tilesList.getTiles2().get(map.getTiles()[
                                player.getXPosition()][player.getYPosition() + 1].getTile2Id()).isPassability())) {
                    if (player.getYPosition() < 285) {
                        player.setYPosition(player.getYPosition() + 1);
                    }
                    if (player.getYPosition() + 3 > player.getYMapPos() + 12) {
                        player.setYMapPos(player.getYMapPos() + 1);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, tilesList);
                    } else {
                        player.getImage().setY(player.getImage().getY() + 40);
                    }
                }
            }
            if (code == KeyCode.W) {
                if (player.getYPosition() > 0 && (tilesList.getTiles1().get(map.getTiles()[
                        player.getXPosition()][player.getYPosition() - 1].getTile1Id()).isPassability()) &&
                        (tilesList.getTiles2().get(map.getTiles()[
                                player.getXPosition()][player.getYPosition() - 1].getTile2Id()).isPassability())) {
                    if (player.getYPosition() > 0) {
                        player.setYPosition(player.getYPosition() - 1);
                    }
                    if (player.getYMapPos() > 0 && player.getYPosition() - 3 < player.getYMapPos()) {
                        player.setYMapPos(player.getYMapPos() - 1);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, tilesList);
                    } else {
                        player.getImage().setY(player.getImage().getY() - 40);
                    }
                }
            }
        });

        root.getChildren().add(canvas);
        root.getChildren().add(player.getImage());
        drawTiles(root);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, tilesList);

        Label mapNameLabel = new Label("Название карты:");
        mapNameLabel.setLayoutX(5);
        mapNameLabel.setLayoutY(610);
        root.getChildren().add(mapNameLabel);

        TextField mapNameTextField = new TextField();
        mapNameTextField.setLayoutX(105);
        mapNameTextField.setLayoutY(605);
        mapNameTextField.setText(map.getMapName());
        root.getChildren().add(mapNameTextField);

        ImageView saveMapImage = new ImageView("/Graphics/GUI/SaveMap.png");
        saveMapImage.setLayoutX(260);
        saveMapImage.setLayoutY(605);
        saveMapImage.setOnMousePressed(event -> {
            map.saveMap(mapNameTextField.getText());
        });
        root.getChildren().add(saveMapImage);

        ImageView loadMapImage = new ImageView("/Graphics/GUI/LoadMap.png");
        loadMapImage.setLayoutX(295);
        loadMapImage.setLayoutY(605);
        loadMapImage.setOnMousePressed(event -> {
            map = map.loadMap(mapNameTextField.getText());
            map.drawMap(player.getXMapPos(), player.getYMapPos(), gc, tilesList);
        });
        root.getChildren().add(loadMapImage);

        root.setOnMousePressed(event -> drawTileOnMap(event.getX(), event.getY(), root, canvas));
        root.setOnMouseDragged(event -> drawTileOnMap(event.getX(), event.getY(), root, canvas));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        canvas.requestFocus();
    }

    private void drawTileOnMap(double x, double y, Group root, Canvas canvas) {
        if (x < 600 && y < 600) {
            if ("tile1".equals(selectedType)) {
                map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                        [player.getYMapPos() + ((((int) y)) / 40)].setTile1Id(selectTile);
            } else {
                map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                        [player.getYMapPos() + ((((int) y)) / 40)].setTile2Id(selectTile);
            }

            Canvas canvas2 = ((Canvas) (root.getChildren().get(0)));
            GraphicsContext gc2 = canvas2.getGraphicsContext2D();
            ImageView image = new ImageView("/Graphics/Tiles/" +
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].getTile1Id() + ".png");
            gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);

            ImageView image2 = new ImageView("/Graphics/Tiles2/" +
                    map.getTiles()[player.getXMapPos() + ((((int) x)) / 40)]
                            [player.getYMapPos() + ((((int) y)) / 40)].getTile2Id() + ".png");
            gc2.drawImage(image2.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);
            root.getChildren().set(0, canvas);
            canvas.requestFocus();
        }
    }

    /*
    * Метод перерисовывает тайлы вокруг текущего, нужно для автоматической дорисовки правильных блоков стен
     */
    private void drawTilesAround() {

    }

    private void drawTiles(Group root) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(630);
        scrollPane.setPrefSize(180, 600);
        for (int i = 0; i < tilesList.getTile1Count(); i++) {
            ImageView tile = new ImageView("/Graphics/Tiles/" + i + ".png");
            tile.setX(5 + (i / 13) * 45);
            tile.setY(5 + (i) * 45 - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                if (pane2.getChildren().contains(border)) {
                    pane2.getChildren().remove(border);
                    pane.getChildren().add(border);
                }
                selectTile = tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getId();
                selectedType = "tile1";
                border.setX(tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tilesList.getTiles1().get(i).setImage(tile);
            pane.getChildren().add(tile);
        }
        border = new javafx.scene.image.ImageView("/Graphics/GUI/Border.png");
        border.setX(tilesList.getTiles1().get(0).getImage().getX() - 1);
        border.setY(tilesList.getTiles1().get(0).getImage().getY() - 1);
        pane.getChildren().add(border);
        scrollPane.setContent(pane);
        root.getChildren().add(scrollPane);

        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setLayoutX(820);
        scrollPane2.setPrefSize(180, 600);

        for (int i = 0; i < tilesList.getTile2Count(); i++) {
            ImageView tile = new ImageView("/Graphics/Tiles2/" + i + ".png");
            tile.setX(5 + (i / 13) * 45);
            tile.setY(5 + (i) * 45 - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                if (pane.getChildren().contains(border)) {
                    pane.getChildren().remove(border);
                    pane2.getChildren().add(border);
                }
                selectTile = tilesList.getTiles2().get(Integer.parseInt(tile.getId())).getId();
                selectedType = "tile2";
                border.setX(tilesList.getTiles2().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tilesList.getTiles2().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tilesList.getTiles2().get(i).setImage(tile);
            pane2.getChildren().add(tile);
        }
        scrollPane2.setContent(pane2);
        root.getChildren().add(scrollPane2);
    }
}
