package model.entity.map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.editor.TileInfo;
import model.editor.TileTypeEnum;
import model.editor.items.ItemInfo;
import model.entity.GameModeEnum;
import model.entity.player.Player;
import view.Editor;
import view.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static view.params.GameParams.*;

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

    public void drawTile(int Xpos, int YPos, int x, int y) {
        drawBottomLayer(Xpos, YPos, x, y);
        drawUpperLayer(Xpos, YPos, x, y);
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

    public void interactionWithMap(int Xpos, int YPos, int x, int y) {
        Editor editor = Game.getEditor();
        TileInfo tileInfo = editor.getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id());
        if (tileInfo.getType() != null) {
            switch (TileTypeEnum.valueOf(tileInfo.getType())) {
                case DOOR: {
                    tiles[Xpos + x][YPos + y].setTile2Id(Integer.parseInt(tileInfo.getParams().get(0)));
                    break;
                }
            }
            Game.getMap().drawTile(Xpos, YPos, x, y);
        }
    }

    // отрисовка нижнего уровня тайла
    private void drawBottomLayer(int Xpos, int YPos, int x, int y) {
        Editor editor = Game.getEditor();
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();

        var tileInfo1 = editor.getTiles1().get(tiles[Xpos + x][YPos + y].getTile1Id());
        var tileInfo2 = editor.getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id());

        gc.drawImage(tileInfo1.getImage().getImage(),
                x * tileSize, y * tileSize);
        gc.drawImage(tileInfo2.getImage().getImage(),
                x * tileSize, y * tileSize);

        // загрязнение на тайле
        if (tiles[Xpos + x][YPos + y].getPollutionId() != 0) {
            gc.drawImage(editor.getPollutions().get(tiles[Xpos + x][YPos + y].getPollutionId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // предметы
        if (tileInfo2.getType() == null || !(tileInfo2.getType().equals(TileTypeEnum.CONTAINER.toString()))) {
            if (tiles[Xpos + x][YPos + y].getItems() != null) {
                if (tiles[Xpos + x][YPos + y].getItems().size() == 1) {
                    gc.drawImage(editor.getItems().get(
                            tiles[Xpos + x][YPos + y].getItems().get(0).getTypeId()).getImage().getImage(),
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

        // NPC
        if (tiles[Xpos + x][YPos + y].getNpcId() != null) {
            gc.drawImage(editor.getNpcs().get(npcList.get(
                    tiles[Xpos + x][YPos + y].getNpcId()).getNpcTypeId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // существа
        if (tiles[Xpos + x][YPos + y].getCreatureId() != null) {
            gc.drawImage(editor.getCreatures().get(
                    creaturesList.get(tiles[Xpos + x][YPos + y].getCreatureId()).getCreatureTypeId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // персонаж игрока
        if ((Game.getGameMode().equals(GameModeEnum.GAME) ||
                Game.getGameMode().equals(GameModeEnum.GAME_MENU)) &&
                (player.getXViewPos() == x) && (player.getYViewPos() == y)) {
            Player.drawPlayerImage(player);
        }

        // если у тайла есть верхний уровень, рисуем его поверх персонажа или NPC
        if (editor.getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).isTwoLayer()) {
            gc.drawImage(editor.getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).getUpLayerImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // Если включено отображение зон, то рисуем их поверх всего
        if (Game.getGameMode().equals(GameModeEnum.EDITOR) &&
                editor.isShowZones() &&
                (tiles[Xpos + x][YPos + y].getZoneId() != 0)) {
            gc.drawImage(editor.getZones().get(tiles[Xpos + x][YPos + y].getZoneId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }
    }
}
