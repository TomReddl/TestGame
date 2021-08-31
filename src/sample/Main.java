package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.entity.Map;
import sample.entity.Player;
import sample.entity.TilesList;

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
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);

        canvas.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.D) {
                if ((tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition() + 1).get(player.getyPosition()).getTile1Id()).isPassability()) &&
                        (tilesList.getTiles2().get(map.getTiles().get(
                                player.getxPosition() + 1).get(player.getyPosition()).getTile2Id()).isPassability())) {
                    player.setxPosition(player.getxPosition() + 1);
                    player.getImage().setX(player.getImage().getX() + 40);
                }
            }
            if (code == KeyCode.A) {
                if ((tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition() - 1).get(player.getyPosition()).getTile1Id()).isPassability()) &&
                        (tilesList.getTiles2().get(map.getTiles().get(
                                player.getxPosition() - 1).get(player.getyPosition()).getTile2Id()).isPassability())) {
                    player.setxPosition(player.getxPosition() - 1);
                    player.getImage().setX(player.getImage().getX() - 40);
                }
            }
            if (code == KeyCode.S) {
                if ((tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition()).get(player.getyPosition() + 1).getTile1Id()).isPassability()) &&
                        (tilesList.getTiles2().get(map.getTiles().get(
                                player.getyPosition() + 1).get(player.getyPosition()).getTile2Id()).isPassability())) {
                    player.setyPosition(player.getyPosition() + 1);
                    player.getImage().setY(player.getImage().getY() + 40);
                }
            }
            if (code == KeyCode.W) {
                if ((tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition()).get(player.getyPosition() - 1).getTile1Id()).isPassability()) &&
                        (tilesList.getTiles2().get(map.getTiles().get(
                                player.getyPosition() - 1).get(player.getyPosition()).getTile2Id()).isPassability())) {
                    player.setyPosition(player.getyPosition() - 1);
                    player.getImage().setY(player.getImage().getY() - 40);
                }
            }
        });

        root.getChildren().add(canvas);
        root.getChildren().add(player.getImage());
        drawTiles(root);
        root.setOnMousePressed(event -> {
            double x = event.getX();
            double y = event.getY();
            if (x <= 640 && y <= 640) {
                if ("tile1".equals(selectedType)) {
                    map.getTiles().get(((((int) x)) / 40)).get(((((int) y)) / 40)).setTile1Id(selectTile);
                } else {
                    map.getTiles().get(((((int) x)) / 40)).get(((((int) y)) / 40)).setTile2Id(selectTile);
                }

                Canvas canvas2 = ((Canvas) (root.getChildren().get(0)));
                GraphicsContext gc2 = canvas2.getGraphicsContext2D();
                ImageView image = new ImageView("/Data/Graphics/Tiles/" +
                        map.getTiles().get(((((int) x)) / 40)).get(((((int) y)) / 40)).getTile1Id() + ".png");
                gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);

                ImageView image2 = new ImageView("/Data/Graphics/Tiles2/" +
                        map.getTiles().get(((((int) x)) / 40)).get(((((int) y)) / 40)).getTile2Id() + ".png");
                gc2.drawImage(image2.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);
                root.getChildren().set(0, canvas);
                canvas.requestFocus();
            }
        });
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        canvas.requestFocus();
    }

    private void drawShapes(GraphicsContext gc) {
        ImageView image = new javafx.scene.image.ImageView("Data/Graphics/Tiles/1.png");
        ImageView image3 = new javafx.scene.image.ImageView("Data/Graphics/Tiles/2.png");
        for (int x = 0; x < 600; x += 40) {
            for (int y = 0; y < 600; y += 40) {
                gc.drawImage(image.getImage(), x, y);
            }
        }
        gc.drawImage(image3.getImage(), 80, 80);
    }

    private void drawTiles(Group root) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(630);
        scrollPane.setPrefSize(180, 600);

        for (int i = 0; i < 27; i++) {
            ImageView tile = new ImageView("/Data/Graphics/Tiles/" + i + ".png");
            tile.setX(5 + (i / 13) * 45);
            tile.setY(10 + (i) * 45 - (i / 13) * 585);
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
        border = new javafx.scene.image.ImageView("Border.png");
        border.setX(tilesList.getTiles1().get(0).getImage().getX() - 1);
        border.setY(tilesList.getTiles1().get(0).getImage().getY() - 1);
        pane.getChildren().add(border);
        scrollPane.setContent(pane);
        root.getChildren().add(scrollPane);

        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setLayoutX(820);
        scrollPane2.setPrefSize(180, 600);

        for (int i = 0; i < 4; i++) {
            ImageView tile = new ImageView("/Data/Graphics/Tiles2/" + i + ".png");
            tile.setX(5 + (i / 13) * 45);
            tile.setY(10 + (i) * 45 - (i / 13) * 585);
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
