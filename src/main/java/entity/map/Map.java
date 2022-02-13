package entity.map;

import editor.Editor;
import entity.DirectionEnum;
import entity.GameModeEnum;
import entity.Player;
import game.Game;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static params.GameParams.*;

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
                drawBottomLayer(Game.getEditor(), Xpos, YPos, x, y);
            }
        }

        for (int x = 0; x < viewSize; x++) { // NPC и существа рисуются в отдельном цикле, чтобы быть поверх уже отрисованной карты
            for (int y = 0; y < viewSize; y++) {
                drawUpperLayer(Game.getEditor(), Xpos, YPos, x, y);
            }
        }
    }

    public void drawTile(int Xpos, int YPos, int x, int y, Editor editor) {
        drawBottomLayer(editor, Xpos, YPos, x, y);
        drawUpperLayer(editor, Xpos, YPos, x, y);
    }

    // отрисовка нижнего уровня тайла
    private void drawBottomLayer(Editor editor, int Xpos, int YPos, int x, int y) {
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();
        gc.drawImage(editor.getTiles1().get(tiles[Xpos + x][YPos + y].getTile1Id()).getImage().getImage(),
                x * tileSize, y * tileSize);
        gc.drawImage(editor.getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).getImage().getImage(),
                x * tileSize, y * tileSize);

        // загрязнение на тайле
        if (tiles[Xpos + x][YPos + y].getPollutionId() != 0) {
            gc.drawImage(editor.getPollutionList().get(tiles[Xpos + x][YPos + y].getPollutionId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        if (tiles[Xpos + x][YPos + y].getItems() != null) {
            gc.drawImage(editor.getItemsList().get(
                    tiles[Xpos + x][YPos + y].getItems().get(0).getTypeId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }
    }

    // отрисовка верхнего уровня тайла
    private void drawUpperLayer(Editor editor, int Xpos, int YPos, int x, int y) {
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();
        if (tiles[Xpos + x][YPos + y].getNpcId() != null) {
            gc.drawImage(editor.getNpcList().get(npcList.get(
                    tiles[Xpos + x][YPos + y].getNpcId()).getNpcTypeId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }
        if (tiles[Xpos + x][YPos + y].getCreatureId() != null) {
            gc.drawImage(editor.getCreatureList().get(
                    creaturesList.get(tiles[Xpos + x][YPos + y].getCreatureId()).getCreatureTypeId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        if ((Game.getGameMode().equals(GameModeEnum.GAME) ||
                Game.getGameMode().equals(GameModeEnum.GAME_MENU)) &&
                (player.getXViewPos() == x) && (player.getYViewPos() == y)) {
            var img = player.getImage().getImage();
            double width = img.getWidth();
            double height = img.getHeight();
            if (player.getDirection().equals(DirectionEnum.LEFT) ||
                    player.getDirection().equals(DirectionEnum.UP)) {
                gc.drawImage(img,
                        0, 0, tileSize, tileSize,
                        player.getXViewPos() * tileSize+tileSize,
                        player.getYViewPos() * tileSize,
                        -width, height);
            } else {
                gc.drawImage(player.getImage().getImage(),
                        player.getXViewPos() * tileSize, player.getYViewPos() * tileSize);
            }
        }

        // если у тайла есть верхний уровень, рисуем его поверх персонажа или NPC
        if (editor.getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).isTwoLayer()) {
            gc.drawImage(editor.getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).getUpLayerImage().getImage(),
                    x * tileSize, y * tileSize);
        }

        // Если включено отображение зон, то рисуем их поверх всего
        if (editor.isShowZones() && (tiles[Xpos + x][YPos + y].getZoneId() != 0)) {
            gc.drawImage(editor.getZonesList().get(tiles[Xpos + x][YPos + y].getZoneId()).getImage().getImage(),
                    x * tileSize, y * tileSize);
        }
    }
}
