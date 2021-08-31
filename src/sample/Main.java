package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import sample.entity.Map;
import sample.entity.Player;
import sample.entity.TilesList;

public class Main extends Application {
    private Player player;
    private Map map;
    private TilesList tilesList;
    private ImageView border;

    private int selectTile = 0;

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
        Canvas canvas = new Canvas(900, 700);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);

        canvas.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.D) {
                if (tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition() + 1).get(player.getyPosition()).getTile1Id()).isPassability()) {
                    player.setxPosition(player.getxPosition() + 1);
                    player.getImage().setX(player.getImage().getX() + 40);
                }
            }
            if (code == KeyCode.A) {
                if (tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition() - 1).get(player.getyPosition()).getTile1Id()).isPassability()) {
                    player.setxPosition(player.getxPosition() - 1);
                    player.getImage().setX(player.getImage().getX() - 40);
                }
            }
            if (code == KeyCode.S) {
                if (tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition()).get(player.getyPosition() + 1).getTile1Id()).isPassability()) {
                    player.setyPosition(player.getyPosition() + 1);
                    player.getImage().setY(player.getImage().getY() + 40);
                }
            }
            if (code == KeyCode.W) {
                if (tilesList.getTiles1().get(map.getTiles().get(
                        player.getxPosition()).get(player.getyPosition() - 1).getTile1Id()).isPassability()) {
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
                Canvas canvas2 = ((Canvas) (root.getChildren().get(0)));
                GraphicsContext gc2 = canvas2.getGraphicsContext2D();
                ImageView image = new ImageView(selectTile + ".png");
                gc2.drawImage(image.getImage(), ((((int) x)) / 40) * 40, ((((int) y)) / 40) * 40);
                root.getChildren().set(0, canvas);

                map.getTiles().get(((((int) x)) / 40)).get(((((int) y)) / 40)).setTile1Id(selectTile);
            }
        });
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        canvas.requestFocus();
    }

    private void drawShapes(GraphicsContext gc) {
        ImageView image = new javafx.scene.image.ImageView("1.png");
        ImageView image3 = new javafx.scene.image.ImageView("2.png");
        for (int x = 0; x < 600; x += 40) {
            for (int y = 0; y < 600; y += 40) {
                gc.drawImage(image.getImage(), x, y);
            }
        }
        gc.drawImage(image3.getImage(), 80, 80);
    }

    private void drawTiles(Group root) {
        for (int i = 0; i < 27; i++) {
            ImageView tile = new ImageView(i + ".png");
            tile.setX(640 + (i/13)*45);
            tile.setY(10 + (i) * 45 - (i / 13)*585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                selectTile = tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getId();
                border.setX(tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getImage().getX()-1);
                border.setY(tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getImage().getY()-1);
            });

            tilesList.getTiles1().get(i).setImage(tile);
            root.getChildren().add(tile);
        }
        border = new javafx.scene.image.ImageView("Border.png");
        border.setX(tilesList.getTiles1().get(0).getImage().getX()-1);
        border.setY(tilesList.getTiles1().get(0).getImage().getY()-1);
        root.getChildren().add(border);
    }
}
