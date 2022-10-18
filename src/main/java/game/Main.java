package game;

import controller.CharactersController;
import controller.ItemsController;
import controller.utils.JsonUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import model.editor.TileTypeEnum;
import model.entity.GameModeEnum;
import model.entity.map.*;
import model.entity.player.Player;
import view.Game;
import view.TileEditPanel;
import view.inventory.BookPanel;
import view.menu.MainMenu;

import java.io.File;
import java.util.List;

import static game.GameParams.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(Game.getRoot());
        primaryStage.setTitle("Game");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
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
                            if (BookPanel.getPane().isVisible()) {
                                BookPanel.showNextPage();
                            } else {
                                if (player.isOverloaded()) {
                                    Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                                } else {
                                    CharactersController.heroMoveRight(player);
                                }
                            }
                            break;
                        }
                        case A: {
                            if (BookPanel.getPane().isVisible()) {
                                BookPanel.showPreviousPage();
                            } else {
                                if (player.isOverloaded()) {
                                    Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                                } else {
                                    CharactersController.heroMoveLeft(player);
                                }
                            }
                            break;
                        }
                        case S: {
                            if (player.isOverloaded()) {
                                Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                            } else {
                                CharactersController.heroMoveDown(player);
                            }
                            break;
                        }
                        case W: {
                            if (player.isOverloaded()) {
                                Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                            } else {
                                CharactersController.heroMoveUp(player);
                            }
                            break;
                        }
                        case ESCAPE: {
                            MainMenu.getPane().setVisible(true);
                            Game.setGameMode(GameModeEnum.GAME_MENU);
                            break;
                        }
                        case I: {
                            Game.getGameMenu().showGameMenuPanel("0");
                            BookPanel.closeBookPanel();
                            break;
                        }
                        case C: {
                            ItemsController.dropSelectItems(player);
                            break;
                        }
                        case P: {
                            Game.getGameMenu().showGameMenuPanel("1");
                            BookPanel.closeBookPanel();
                            break;
                        }
                        case E: {
                            if (BookPanel.getPane().isVisible()) {
                                BookPanel.closeBookPanel();
                            } else {
                                Robot robot = new Robot();
                                int x = ((int) (robot.getMousePosition().getX() - primaryStage.getX()) / tileSize);
                                int y = ((int) (robot.getMousePosition().getY() - primaryStage.getY() - headerSize) / tileSize);
                                if (isReachable(player, x, y)) {
                                    List<Items> itemsList = Game.getMap().getTiles()[player.getXMapPos() + x]
                                            [player.getYMapPos() + y].getItems();
                                    if (itemsList != null) {
                                        CharactersController.pickUpItems(itemsList, x, y);
                                    } else {
                                        CharactersController.interactionWithMap(x, y);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case EDITOR: {
                    if (code == KeyCode.ESCAPE) {
                        MainMenu.getPane().setVisible(false);
                        Game.setGameMode(GameModeEnum.GAME_MENU);
                        break;
                    }
                    if (isEditorButtonPressEnabled()) {
                        switch (code) {
                            case E: {
                                Robot robot = new Robot();
                                int x = ((int) (robot.getMousePosition().getX() - primaryStage.getX()) / tileSize);
                                int y = ((int) (robot.getMousePosition().getY() - primaryStage.getY() - headerSize) / tileSize);
                                MapCellInfo cellInfo = Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y];
                                TileEditPanel.showPanel(cellInfo);
                                break;
                            }
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
                            case Z: {
                                Game.getEditor().setShowZones(!Game.getEditor().isShowZones());
                                Game.getEditor().getShowZonesCheckBox().setSelected(Game.getEditor().isShowZones());
                                Game.getMap().drawCurrentMap();
                                break;
                            }
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
        mapNameTextField.setFocusTraversable(false);
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
        Game.getStopTestGameImage().setVisible(false);
        Game.getRoot().getChildren().add(Game.getStopTestGameImage());

        Game.getRoot().setOnMouseClicked(event -> drawTileOnMap(event.getX(), event.getY(), Game.getEditor().getCanvas()));
        Game.getRoot().setOnMouseDragged(event -> drawTileOnMap(event.getX(), event.getY(), Game.getEditor().getCanvas()));
        Game.getRoot().setOnMouseMoved(event -> showMapPointInfo(event.getX(), event.getY(), Game.getEditor().getMapInfoLabel()));
        primaryStage.setScene(scene);
        primaryStage.show();
        Game.getEditor().getCanvas().requestFocus();

        File f = new File("src/main/resources/sound/mainTheme.mp3");
        Media media = new Media(f.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        try {
            mediaPlayer.setAutoPlay(true);
        } finally {
            mediaPlayer.dispose();
        }
    }

    //
    private boolean isEditorButtonPressEnabled() {
        return !TileEditPanel.getPane().isVisible();
    }

    private Boolean isReachable(Player player, double x, double y) {
        return (Math.abs(player.getXPosition() - (player.getXMapPos() + x)) < 2) &&
                (Math.abs(player.getYPosition() - (player.getYMapPos() + y)) < 2);
    }

    private void showMapPointInfo(double x, double y, Label mapInfoLabel) {
        var player = Game.getMap().getPlayer();
        if (x < viewSize * tileSize && y < viewSize * tileSize) {
            int xPos = player.getXMapPos() + (((int) x) / tileSize);
            int yPos = player.getYMapPos() + (((int) y) / tileSize);
            var mapCellInfo = Game.getMap().getTiles()[xPos][yPos];
            mapInfoLabel.setText(
                    "X: " + xPos + ", Y: " + yPos + ". " +
                            mapCellInfo.getTile1Info().getDesc() +
                            (mapCellInfo.getTile2Id() == 0 ? "" : ", " +
                                    mapCellInfo.getTile2Info().getDesc().toLowerCase()) +
                            (mapCellInfo.getDesc() != null ? "\n" + mapCellInfo.getDesc() : ""));

            if (mapCellInfo instanceof ClosableCellInfo) {
                var closableCellInfo = (ClosableCellInfo) mapCellInfo;
                if (closableCellInfo.isLocked()) {
                    if (closableCellInfo.isCodeLock()) {
                        mapInfoLabel.setText(mapInfoLabel.getText() + "\n" + Game.getText("IS_LOCKED"));
                    } else {
                        mapInfoLabel.setText(mapInfoLabel.getText() + "\n" +
                                String.format(Game.getText("LOCKED"), closableCellInfo.getLockLevel()));
                    }
                }
                if (closableCellInfo.isTrap()) {
                    mapInfoLabel.setText(mapInfoLabel.getText() + "\n" + Game.getText("TRAPPED"));
                }
            }
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
                MapCellInfo mapCellInfo = Game.getMap().getTiles()[player.getXMapPos() + tileX]
                        [player.getYMapPos() + tileY];
                switch (Game.getEditor().getSelectedType()) {
                    case GROUND:
                    case OBJECT: {
                        mapCellInfo = getNewMapCellInfo(mapCellInfo);
                        break;
                    }
                    case NPC: {
                        if (Game.getEditor().getSelectTile() == 0) {
                            mapCellInfo.setNpcId(null);
                        } else if (mapCellInfo.getNpcId() == null) {
                            Game.getMap().getNpcList().add(new NPC(Game.getEditor().getSelectTile(), Game.getMap().getNpcList().size(),
                                    player.getXMapPos() + tileX,
                                    player.getYMapPos() + tileY));
                            mapCellInfo.setNpcId(Game.getMap().getNpcList().get(Game.getMap().getNpcList().size() - 1).getId());
                        }
                        break;
                    }
                    case CREATURE: {
                        if (Game.getEditor().getSelectTile() == 0) {
                            mapCellInfo.setCreatureId(null);
                        } else if (mapCellInfo.getCreatureId() == null) {
                            Game.getMap().getCreaturesList().add(new Creature(Game.getEditor().getSelectTile(), Game.getMap().getCreaturesList().size(),
                                    player.getXMapPos() + tileX,
                                    player.getYMapPos() + tileY));

                            mapCellInfo.setCreatureId(Game.getMap().getCreaturesList().get(Game.getMap().getCreaturesList().size() - 1).getId());
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
                        mapCellInfo.setPollutionId(Game.getEditor().getSelectTile());
                        break;
                    }
                    case ZONE: {
                        mapCellInfo.setZoneId(Game.getEditor().getSelectTile());
                        break;
                    }
                }

                Game.getMap().getTiles()[player.getXMapPos() + tileX][player.getYMapPos() + tileY] = mapCellInfo;

                Game.getMap().drawTile(player, tileX, tileY);

                canvas.requestFocus();
            }
        }
    }

    // получаем новый MapCellInfo при смене тайла карты
    private MapCellInfo getNewMapCellInfo(MapCellInfo info) {
        switch (Game.getEditor().getSelectedType()) {
            case GROUND: {
                info.setTile1Id(Game.getEditor().getSelectTile());
                break;
            }
            case OBJECT: {
                var newType = Game.getEditor().getTiles2().get(Game.getEditor().getSelectTile()).getType();
                var oldType = info.getTile2Info().getType();
                info.setTile2Id(Game.getEditor().getSelectTile());

                if (newType == null && (oldType != null)) {
                    info = new MapCellInfo(info);
                } else if (newType != null && (TileTypeEnum.valueOf(newType).equals(TileTypeEnum.DOOR) ||
                        TileTypeEnum.valueOf(newType).equals(TileTypeEnum.CONTAINER))) {
                    info = new ClosableCellInfo(info);
                }
            }
        }

        return info;
    }
}
