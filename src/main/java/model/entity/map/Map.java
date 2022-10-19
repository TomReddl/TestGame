package model.entity.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.editor.TileTypeEnum;
import model.entity.GameModeEnum;
import model.entity.player.Player;
import view.Editor;
import view.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static game.GameParams.*;

/*
 * Карта мира со всеми персонажами и предметами на ней
 * */
@Getter
@Setter
@Slf4j
public class Map implements Serializable {
    private MapCellInfo[][] tiles = new MapCellInfo[mapSize][mapSize];
    List<NPC> npcList = new ArrayList<>();
    List<Creature> creaturesList = new ArrayList<>();
    private String mapName;
    private Player player = new Player();
    @JsonIgnore
    private ImageView bag = new ImageView("/graphics/items/bag.png");

    public Map() {
        mapName = "1";
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                var taleInfo = new MapCellInfo();
                taleInfo.setTile1Id(0);
                taleInfo.setTile2Id(0);
                tiles[i][j] = taleInfo;
            }
        }
    }

    public void drawCurrentMap() {
        drawMap(player.getXMapPos(), player.getYMapPos());
    }

    public void drawMap(int Xpos, int YPos) {
        for (int x = 0; x < viewSize; x++) {
            for (int y = 0; y < viewSize; y++) {
                drawBottomLayer(Xpos, YPos, x, y);
            }
        }

        for (int x = 0; x < viewSize; x++) { // NPC и существа рисуются в отдельном цикле, чтобы быть поверх уже отрисованной карты
            for (int y = 0; y < viewSize; y++) {
                drawUpperLayer(Xpos, YPos, x, y);
            }
        }
    }

    /** Нарисовать тайл
     *
     * @param x -координата тайла по горизонтали
     * @param y -координата тайла по вертикали
     */
    public void drawTile(Player player, int x, int y) {
        drawBottomLayer(player.getXMapPos(), player.getYMapPos(), x, y);
        drawUpperLayer(player.getXMapPos(), player.getYMapPos(), x, y);
    }

    public void addItemOnMap(int x, int y, Items addedItems) {
        if (addedItems.getTypeId() == 0) {
            Game.getMap().getTiles()[x][y].setItems(null);
        } else if (Game.getMap().getTiles()[x][y].getItems() == null) {
            Game.getMap().getTiles()[x][y].setItems(new ArrayList<>());
            Game.getMap().getTiles()[x][y].getItems().add(addedItems);
        } else {
            boolean found = false;
            if (addedItems.getInfo().getStackable()) {
                for (Items items : Game.getMap().getTiles()[x][y].getItems()) {
                    if (items.getTypeId() == addedItems.getTypeId()) {
                        items.setCount(items.getCount() + 1);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                Game.getMap().getTiles()[x][y].getItems().add(addedItems);
            }
        }
    }

    // отрисовка нижнего уровня тайла
    private void drawBottomLayer(int Xpos, int YPos, int x, int y) {
        Editor editor = Game.getEditor();
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();
        var mapCellInfo = tiles[Xpos + x][YPos + y];

        var tileInfo1 = editor.getTiles1().get(mapCellInfo.getTile1Id());
        var tileInfo2 = editor.getTiles2().get(mapCellInfo.getTile2Id());

        gc.drawImage(tileInfo1.getImage().getImage(),
                x * tileSize, y * tileSize);
        gc.drawImage(tileInfo2.getImage().getImage(),
                x * tileSize, y * tileSize);

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
    private void drawUpperLayer(int Xpos, int YPos, int x, int y) {
        Editor editor = Game.getEditor();
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();
        var mapCellInfo = tiles[Xpos + x][YPos + y];

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
            Player.drawPlayerImage(player);
        }

        // если у тайла есть верхний уровень, рисуем его поверх персонажа или NPC
        if (editor.getTiles2().get(mapCellInfo.getTile2Id()).isTwoLayer()) {
            gc.drawImage(editor.getTiles2().get(mapCellInfo.getTile2Id()).getUpLayerImage().getImage(),
                    x * tileSize, y * tileSize);
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
