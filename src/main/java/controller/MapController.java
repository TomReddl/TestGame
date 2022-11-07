package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import model.editor.TileInfo;
import model.editor.TileTypeEnum;
import model.entity.GameModeEnum;
import model.entity.map.*;
import model.entity.player.Player;
import view.Editor;
import view.Game;
import view.TileEditPanel;
import view.inventory.BookPanel;
import view.inventory.ItemCountPanel;
import view.menu.MainMenu;

import java.util.ArrayList;
import java.util.List;

import static game.GameParams.*;

/**
 * Действия с картой
 */
public class MapController {
    private static ImageView bag = new ImageView("/graphics/items/bag.png");

    /**
     * Движение мыши по карте без нажатых кнопок
     *
     * @param x - координата x мыши в пикселях от слевого верхнего угла игрового окна
     * @param y - координата y мыши в пикселях от слевого верхнего угла игрового окна
     */
    public static void mapMouseMove(double x, double y) {
        if (x < viewSize * tileSize && y < viewSize * tileSize) {
            var tileX = (((int) x) / tileSize);
            var tileY = (((int) y) / tileSize);
            if (tileX != Game.getXMapInfoPos() || tileY != Game.getYMapInfoPos()) {
                MapController.showMapPointInfo(x, y, Game.getEditor().getMapInfoLabel());

                Game.setXMapInfoPos(Game.getMap().getPlayer().getXMapPos() + (((int) x) / tileSize));
                Game.setYMapInfoPos(Game.getMap().getPlayer().getYMapPos() + (((int) y) / tileSize));
            }
        }
    }

    /**
     * Движение мыши по карте с нажатой кнопкой
     *
     * @param x            - координата x мыши в пикселях от слевого верхнего угла игрового окна
     * @param y            - координата y мыши в пикселях от слевого верхнего угла игрового окна
     * @param isRightMouse - нажата ПКМ
     */
    public static void mapMouseDragged(double x, double y, boolean isRightMouse) {
        if (canMouseDraw(x, y)) {
            var tileX = (((int) x) / tileSize);
            var tileY = (((int) y) / tileSize);
            if (tileX != Game.getXMapInfoPos() || tileY != Game.getYMapInfoPos()) {
                MapController.drawTileOnMap(x, y,
                        isRightMouse, Game.getEditor().getCanvas());

                Game.setXMapInfoPos(Game.getMap().getPlayer().getXMapPos() + (((int) x) / tileSize));
                Game.setYMapInfoPos(Game.getMap().getPlayer().getYMapPos() + (((int) y) / tileSize));
            }
        }
    }

    /**
     * Проверка возможности рисовать карту
     *
     * @param x - координата x
     * @param y - координата y
     * @return true, если доступно рисование карты
     */
    private static boolean canMouseDraw(double x, double y) {
        return Game.getGameMode().equals(GameModeEnum.EDITOR) &&
                !Game.getInventory().getTabPane().isVisible() &&
                !ItemCountPanel.getPane().isVisible() &&
                !TileEditPanel.getPane().isVisible() &&
                Game.isDrawingMap() &&
                x < viewSize * tileSize &&
                y < viewSize * tileSize;
    }

    /**
     * Показать текстовую подсказку к точке карты
     *
     * @param x            - координата x точки
     * @param y            - координата y точки
     * @param mapInfoLabel - лэйбл для показа подсказки
     */
    public static void showMapPointInfo(double x, double y, Label mapInfoLabel) {
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

    /**
     * Отрисовка тайла карты при его изменении
     *
     * @param x            - координата x точки
     * @param y            - координата y точки
     * @param isRightMouse - признак нажатия ПКМ (стирает объект текущего выбранного типа)
     * @param canvas       - канва, в которой рисуется тайл
     */
    public static void drawTileOnMap(double x, double y, boolean isRightMouse, Canvas canvas) {
        if (Game.getGameMode().equals(GameModeEnum.EDITOR)) {
            var tileX = (((int) x) / tileSize);
            var tileY = (((int) y) / tileSize);
            var player = Game.getMap().getPlayer();
            MapCellInfo mapCellInfo = Game.getMap().getTiles()[player.getXMapPos() + tileX]
                    [player.getYMapPos() + tileY];
            switch (Game.getEditor().getSelectedType()) {
                case GROUND:
                case OBJECT: {
                    mapCellInfo = getNewMapCellInfo(mapCellInfo, isRightMouse);
                    break;
                }
                case NPC: {
                    if (isRightMouse || Game.getEditor().getSelectTile() == 0) {
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
                    if (isRightMouse || Game.getEditor().getSelectTile() == 0) {
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
                    addItemOnMap(
                            player.getXMapPos() + tileX,
                            player.getYMapPos() + tileY,
                            new Items(isRightMouse ? 0 : Game.getEditor().getSelectTile(), Game.isShiftPressed() ? 10 : 1));
                    break;
                }
                case POLLUTION: {
                    mapCellInfo.setPollutionId(isRightMouse ? 0 : Game.getEditor().getSelectTile());
                    break;
                }
                case ZONE: {
                    mapCellInfo.setZoneId(isRightMouse ? 0 : Game.getEditor().getSelectTile());
                    break;
                }
            }

            Game.getMap().getTiles()[player.getXMapPos() + tileX][player.getYMapPos() + tileY] = mapCellInfo;
            drawTilesAround(player.getXMapPos() + tileX, player.getYMapPos() + tileY);
            canvas.requestFocus();
        }
    }

    // Перерисовать тайлы вокруг текущего
    private static void drawTilesAround(int XPos, int YPos) {
        if (XPos > 0) {
            drawWallTile(XPos - 1, YPos);
        }
        if (YPos > 0) {
            drawWallTile(XPos, YPos - 1);
        }
        drawWallTile(XPos, YPos);
        if (YPos < 299) {
            drawWallTile(XPos, YPos + 1);
        }
        if (XPos < 299) {
            drawWallTile(XPos + 1, YPos);
        }
    }

    // Перерисовывает тайл стены на основе окружающих тайлов
    private static void drawWallTile(int XPos, int YPos) {
        if (isWall(XPos, YPos)) {
            if (!isWall(XPos, YPos + 1) &&
                    !isWall(XPos - 1, YPos) &&
                    !isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "8"));
            } else if (!isWall(XPos, YPos + 1) &&
                    isWall(XPos - 1, YPos) &&
                    isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "1"));
            } else if (!isWall(XPos, YPos + 1) &&
                    !isWall(XPos - 1, YPos) &&
                    isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "2"));
            } else if (!isWall(XPos, YPos + 1) &&
                    isWall(XPos - 1, YPos) &&
                    !isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "3"));
            } else if (isWall(XPos, YPos + 1) &&
                    !isWall(XPos - 1, YPos) &&
                    !isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "4"));
            } else if (isWall(XPos, YPos + 1) &&
                    isWall(XPos - 1, YPos) &&
                    !isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "5"));
            } else if (isWall(XPos, YPos + 1) &&
                    !isWall(XPos - 1, YPos) &&
                    isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "6"));
            } else if (isWall(XPos, YPos + 1) &&
                    isWall(XPos - 1, YPos) &&
                    isWall(XPos + 1, YPos)) {
                Game.getMap().getTiles()[XPos][YPos].setTile1Id(getWallVariantId(XPos, YPos, "7"));
            }
        }
        var player = Game.getMap().getPlayer();
        drawTile(player, XPos - player.getXMapPos(), YPos - player.getYMapPos());
    }

    /**
     * Является ли тайл карты стеной
     *
     * @param XPos - координата x (горизонталь)
     * @param YPos - координата y (вертикаль)
     *
     * @return true, если тайл является стеной
     */
    private static boolean isWall(int XPos, int YPos) {
        if (XPos > -1 && YPos > -1 && XPos < 300 && YPos < 300) {
            var type = Game.getMap().getTiles()[XPos][YPos].getTile1Info().getType();
            return type != null && TileTypeEnum.valueOf(type).equals(TileTypeEnum.WALL);
        }
        return false;
    }

    /**
     * Получить вариант тайла стены, сочетающийся с соседними стенами
     *
     * @param XPos - координата x (горизонталь)
     * @param YPos - координата y (вертикаль)
     *
     * @return id нижнего тайла, сочетающегося с соседними стенами
     */
    private static int getWallVariantId(int XPos, int YPos, String variant) {
        TileInfo wallTileInfo = Game.getEditor().getTiles1().get(Game.getMap().getTiles()[XPos][YPos].getTile1Id());
        for (TileInfo tileInfo : Game.getEditor().getTiles1()) {
            if (tileInfo.getType() != null && TileTypeEnum.valueOf(tileInfo.getType()).equals(TileTypeEnum.WALL)) {
                if ((tileInfo.getParams().get(0).equals(wallTileInfo.getParams().get(0))) &&
                        (tileInfo.getParams().get(1).equals(variant))) {
                    return tileInfo.getId();
                }
            }
        }
        return wallTileInfo.getId();
    }

    /**
     * Получить новый MapCellInfo при смене тайла карты
     *
     * @param mapCellInfo  - информация о точке на карте карты
     * @param isRightMouse - признак нажатия ПКМ
     *
     * @return новый MapCellInfo, если была нажата ЛКМ, текущий MapCellInfo со стертым объектом, если была нажата ПКМ
     */
    private static MapCellInfo getNewMapCellInfo(MapCellInfo mapCellInfo, boolean isRightMouse) {
        switch (Game.getEditor().getSelectedType()) {
            case GROUND: {
                mapCellInfo.setTile1Id(isRightMouse ? 0 : Game.getEditor().getSelectTile());
                break;
            }
            case OBJECT: {
                var newType = Game.getEditor().getTiles2().get(isRightMouse ? 0 : Game.getEditor().getSelectTile()).getType();
                var oldType = mapCellInfo.getTile2Info().getType();
                mapCellInfo.setTile2Id(isRightMouse ? 0 : Game.getEditor().getSelectTile());

                if (newType == null && (oldType != null)) {
                    mapCellInfo = new MapCellInfo(mapCellInfo);
                } else if (newType != null && (TileTypeEnum.valueOf(newType).equals(TileTypeEnum.DOOR) ||
                        TileTypeEnum.valueOf(newType).equals(TileTypeEnum.CONTAINER))) {
                    mapCellInfo = new ClosableCellInfo(mapCellInfo);
                }
            }
        }

        return mapCellInfo;
    }

    /**
     * Нажата и отпущена кнопка клавиатуры
     *
     * @param code  - код нажатой кнопки
     * @param stage
     */
    public static void onKeyReleased(KeyCode code, Stage stage) {
        if (code == KeyCode.SHIFT) {
            Game.setShiftPressed(false);
        } else {
            switch (Game.getGameMode()) {
                case GAME: {
                    onGameKeyReleased(code, stage);
                    break;
                }
                case EDITOR: {
                    onEditorKeyReleased(code, stage);
                    break;
                }
                case GAME_MENU: {
                    onGameMenuKeyReleased(code);
                    break;
                }
            }
        }
    }

    /**
     * Нажата кнопка клавиатуры (срабатывает сразу при нажатии, до того, как кнопка была отпущена)
     *
     * @param code  - код нажатой кнопки
     */
    public static void onKeyTyped(KeyCode code) {
        switch (Game.getGameMode()) {
            case GAME:
            case EDITOR:
                {
                MapController.onEditorKeyTyped(code);
                break;
            }
        }
    }

    /**
     * Обработка нажатия кнопки клавиатуры в режиме меню
     *
     * @param code - код нажатой кнопки
     */
    private static void onGameMenuKeyReleased(KeyCode code) {
        switch (code) {
            case ESCAPE: {
                Game.setGameMode(GameModeEnum.GAME);
                break;
            }
        }
    }

    /**
     * Обработка нажатия кнопки клавиатуры в режиме редактора
     *
     * @param code  - код нажатой кнопки
     */
    public static void onEditorKeyTyped(KeyCode code) {
        if (code == KeyCode.SHIFT) {
            Game.setShiftPressed(true);
        }
    }

    /**
     * Обработка нажатия кнопки клавиатуры в режиме редактора
     *
     * @param code  - код нажатой кнопки
     * @param stage
     */
    private static void onEditorKeyReleased(KeyCode code, Stage stage) {
        var player = Game.getMap().getPlayer();
        if (code == KeyCode.ESCAPE) {
            MainMenu.getPane().setVisible(false);
            Game.setGameMode(GameModeEnum.GAME_MENU);
        } else if (!TileEditPanel.getPane().isVisible()) {
            switch (code) {
                case E: {
                    Robot robot = new Robot();
                    int x = ((int) (robot.getMousePosition().getX() - stage.getX()) / tileSize);
                    int y = ((int) (robot.getMousePosition().getY() - stage.getY() - headerSize) / tileSize);
                    MapCellInfo cellInfo = Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y];
                    TileEditPanel.showPanel(cellInfo);
                    break;
                }
                case W: {
                    player.setYMapPos(Math.max(player.getYMapPos() - viewSize, 0));
                    MapController.drawCurrentMap();
                    break;
                }
                case S: {
                    player.setYMapPos(Math.min(player.getYMapPos() + viewSize, mapSize - viewSize));
                    MapController.drawCurrentMap();
                    break;
                }
                case D: {
                    player.setXMapPos(Math.min(player.getXMapPos() + viewSize, mapSize - viewSize));
                    MapController.drawCurrentMap();
                    break;
                }
                case A: {
                    player.setXMapPos(Math.max(player.getXMapPos() - viewSize, 0));
                    MapController.drawCurrentMap();
                    break;
                }
                case Z: {
                    Game.getEditor().setShowZones(!Game.getEditor().isShowZones());
                    Game.getEditor().getShowZonesCheckBox().setSelected(Game.getEditor().isShowZones());
                    MapController.drawCurrentMap();
                    break;
                }
            }
        }
    }

    /**
     * Обработка нажатия кнопки клавиатуры в режиме игры
     *
     * @param code  - код нажатой кнопки
     * @param stage
     */
    private static void onGameKeyReleased(KeyCode code, Stage stage) {
        var player = Game.getMap().getPlayer();
        Game.hideMessage();
        switch (code) {
            case D: {
                if (BookPanel.getPane().isVisible()) {
                    BookPanel.showNextPage();
                } else {
                    if (CharactersController.isOverloaded(player)) {
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
                    if (CharactersController.isOverloaded(player)) {
                        Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                    } else {
                        CharactersController.heroMoveLeft(player);
                    }
                }
                break;
            }
            case S: {
                if (CharactersController.isOverloaded(player)) {
                    Game.showMessage(Game.getText("ERROR_OVERLOADED"));
                } else {
                    CharactersController.heroMoveDown(player);
                }
                break;
            }
            case W: {
                if (CharactersController.isOverloaded(player)) {
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
                    int x = ((int) (robot.getMousePosition().getX() - stage.getX()) / tileSize);
                    int y = ((int) (robot.getMousePosition().getY() - stage.getY() - headerSize) / tileSize);
                    if (isReachable(player, x, y)) {
                        var mapCellInfo = Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y];
                        List<Items> itemsList = mapCellInfo.getItems();
                        if (itemsList != null || mapCellInfo.getTile2Type().equals(TileTypeEnum.CONTAINER)) {
                            CharactersController.pickUpItems(itemsList, x, y);
                        } else {
                            CharactersController.interactionWithMap(x, y);
                        }
                    }
                }
            }
        }
    }

    /**
     * Проверка досягаемости точки на карте для персонажа
     *
     * @param player - игрок, для которого проверяется досягаемость
     * @param x      - координата x точки
     * @param y      - координата x точки
     *
     * @return true, если персонаж может дотянуться до точки
     */
    public static boolean isReachable(Player player, double x, double y) {
        return (Math.abs(player.getXPosition() - (player.getXMapPos() + x)) < 2) &&
                (Math.abs(player.getYPosition() - (player.getYMapPos() + y)) < 2);
    }

    public static void drawMapStart(double x, double y, boolean isRightMouse) {
        if (x < viewSize * tileSize && y < viewSize * tileSize) {
            Game.setDrawingMap(true);
            if (canMouseDraw(x, y)) {
                MapController.drawTileOnMap(x, y, isRightMouse, Game.getEditor().getCanvas());
            }
        }
    }

    public static void drawMapEnd() {
        Game.setDrawingMap(false);
    }

    /**
     * Отрисовать текущую область карты
     */
    public static void drawCurrentMap() {
        var player = Game.getMap().getPlayer();
        drawMap(player.getXMapPos(), player.getYMapPos());
    }

    /**
     * Отрисовать область карты
     *
     * @param XPos - координата x левого верхнего угла отрисовываемой области
     * @param YPos - координата y левого верхнего угла отрисовываемой области
     */
    public static void drawMap(int XPos, int YPos) {
        for (int x = 0; x < viewSize; x++) {
            for (int y = 0; y < viewSize; y++) {
                drawBottomLayer(XPos, YPos, x, y);
            }
        }

        for (int x = 0; x < viewSize; x++) { // NPC и существа рисуются в отдельном цикле, чтобы быть поверх уже отрисованной карты
            for (int y = 0; y < viewSize; y++) {
                drawUpperLayer(XPos, YPos, x, y);
            }
        }
    }

    /**
     * Нарисовать тайл
     *
     * @param player -персонаж игрока
     * @param x      -координата тайла по горизонтали
     * @param y      -координата тайла по вертикали
     */
    public static void drawTile(Player player, int x, int y) {
        if (x > -1 && y > -1 && x < viewSize && y < viewSize) {
            drawBottomLayer(player.getXMapPos(), player.getYMapPos(), x, y);
            drawUpperLayer(player.getXMapPos(), player.getYMapPos(), x, y);
        }
    }

    /**
     * Добавить предмет на карту
     *
     * @param x          - координата тайла по горизонтали
     * @param y          - координата тайла по вертикали
     * @param addedItem - добавляемый предмет
     */
    public static void addItemOnMap(int x, int y, Items addedItem) {
        if (addedItem.getTypeId() == 0) {
            Game.getMap().getTiles()[x][y].setItems(null);
        } else if (Game.getMap().getTiles()[x][y].getItems() == null) {
            Game.getMap().getTiles()[x][y].setItems(new ArrayList<>());
            Game.getMap().getTiles()[x][y].getItems().add(addedItem);
        } else {
            boolean found = false;
            if (addedItem.getInfo().getStackable()) {
                for (Items items : Game.getMap().getTiles()[x][y].getItems()) {
                    if (items.getTypeId() == addedItem.getTypeId()) {
                        items.setCount(items.getCount() + addedItem.getCount());
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                Game.getMap().getTiles()[x][y].getItems().add(addedItem);
            }
        }
    }

    // отрисовка нижнего уровня тайла
    private static void drawBottomLayer(int Xpos, int YPos, int x, int y) {
        MapCellInfo[][] tiles = Game.getMap().getTiles();
        Editor editor = Game.getEditor();
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();
        var mapCellInfo = tiles[Xpos + x][YPos + y];

        var tileInfo1 = editor.getTiles1().get(mapCellInfo.getTile1Id());
        var tileInfo2 = editor.getTiles2().get(mapCellInfo.getTile2Id());

        gc.drawImage(tileInfo1.getImage().getImage(),
                x * tileSize, y * tileSize);
        gc.drawImage(tileInfo2.getImage().getImage(),
                x * tileSize, y * tileSize - (tileInfo2.getImage().getImage().getHeight() - tileSize));

        // загрязнение на тайле
        if (mapCellInfo.getPollutionId() != 0) {
            gc.drawImage(editor.getPollutions().get(mapCellInfo.getPollutionId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // предметы
        if (tileInfo2.getType() == null || !(tileInfo2.getType().equals(TileTypeEnum.CONTAINER.toString()))) {
            if (mapCellInfo.getItems() != null) {
                if (mapCellInfo.getItems().size() == 1) {
                    gc.drawImage(editor.getItems().get(
                            mapCellInfo.getItems().get(0).getTypeId()).getImage().getImage(),
                            x * tileSize, y * tileSize);
                } else { // если на тайле больше 1 предмета, рисуем мешок
                    gc.drawImage(bag.getImage(),
                            x * tileSize, y * tileSize);
                }
            }
        }
    }

    // отрисовка верхнего уровня тайла
    private static void drawUpperLayer(int Xpos, int YPos, int x, int y) {
        MapCellInfo[][] tiles = Game.getMap().getTiles();
        var player = Game.getMap().getPlayer();
        List<NPC> npcList = Game.getMap().getNpcList();
        List<Creature> creaturesList = Game.getMap().getCreaturesList();
        Editor editor = Game.getEditor();
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();
        var mapCellInfo = tiles[Xpos + x][YPos + y];

        var tileInfo2 = editor.getTiles2().get(mapCellInfo.getTile2Id());

        // NPC
        if (mapCellInfo.getNpcId() != null) {
            gc.drawImage(editor.getNpcs().get(npcList.get(
                    mapCellInfo.getNpcId()).getNpcTypeId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // существа
        if (mapCellInfo.getCreatureId() != null) {
            gc.drawImage(editor.getCreatures().get(
                    creaturesList.get(mapCellInfo.getCreatureId()).getCreatureTypeId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // персонаж игрока
        if ((Game.getGameMode().equals(GameModeEnum.GAME) ||
                Game.getGameMode().equals(GameModeEnum.GAME_MENU)) &&
                (player.getXViewPos() == x) && (player.getYViewPos() == y)) {
            CharactersController.drawPlayerImage(player);
        }

        // если у тайла есть верхний уровень или его высота больше размера тайла, рисуем его поверх персонажа или NPC
        Image tile2Img = tileInfo2.getImage().getImage();
        if (tileInfo2.isTwoLayer() || (tile2Img.getHeight() > tileSize)) {
            gc.drawImage(tile2Img, x * tileSize, y * tileSize - (tile2Img.getHeight() - tileSize));
        }

        // Если включено отображение зон, то рисуем их поверх всего
        if (Game.getGameMode().equals(GameModeEnum.EDITOR) &&
                editor.isShowZones() &&
                (mapCellInfo.getZoneId() != 0)) {
            gc.drawImage(editor.getZones().get(mapCellInfo.getZoneId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }
    }
}
