package entity.map;

import editor.Editor;
import entity.GameModeEnum;
import entity.Player;
import game.Main;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import utils.JsonUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
* Карта мира со всеми персонажами и предметами на ней
* */
@Getter
@Setter
@Slf4j
public class Map implements Serializable {
    private MapCellInfo[][] tiles = new MapCellInfo[300][300];
    List<NPC> npcList = new ArrayList<>();
    List<Creature> creaturesList = new ArrayList<>();
    private String mapName;
    private Player player = new Player();

    public Map() {
        mapName = "1";
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                var taleInfo = new MapCellInfo();
                taleInfo.setTile1Id(0);
                taleInfo.setTile2Id(0);
                tiles[i][j] = taleInfo;
            }
        }
    }

    public void drawMap(int Xpos, int YPos, Editor editor) {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                drawBottomLayer(editor, Xpos, YPos, x, y);
            }
        }

        for (int x = 0; x < 15; x++) { // NPC и существа рисуются в отдельном цикле, чтобы быть поверх уже отрисованной карты
            for (int y = 0; y < 15; y++) {
                drawUpperLayer(editor, Xpos, YPos, x, y);
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
        gc.drawImage(editor.getTilesList().getTiles1().get(tiles[Xpos + x][YPos + y].getTile1Id()).getImage().getImage(),
                x * 40, y * 40);
        gc.drawImage(editor.getTilesList().getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).getImage().getImage(),
                x * 40, y * 40);

        // загрязнение на тайле
        if (tiles[Xpos + x][YPos + y].getPollutionId() != 0) {
            gc.drawImage(editor.getPollutionList().getPollutions().get(tiles[Xpos + x][YPos + y].getPollutionId()).getImage().getImage(),
                    x * 40, y * 40);
        }

        if (tiles[Xpos + x][YPos + y].getItems() != null) {
            gc.drawImage(editor.getItemsList().getItems().get(
                    tiles[Xpos + x][YPos + y].getItems().get(0).getTypeId()).getImage().getImage(),
                    x * 40, y * 40);
        }
    }

    // отрисовка верхнего уровня тайла
    private void drawUpperLayer(Editor editor, int Xpos, int YPos, int x, int y) {
        GraphicsContext gc = editor.getCanvas().getGraphicsContext2D();
        if (tiles[Xpos + x][YPos + y].getNpcId() != null) {
            gc.drawImage(editor.getNpcList().getNpc().get(npcList.get(
                    tiles[Xpos + x][YPos + y].getNpcId()).getNpcTypeId()).getImage().getImage(),
                    x * 40, y * 40);
        }
        if (tiles[Xpos + x][YPos + y].getCreatureId() != null) {
            gc.drawImage(editor.getCreatureList().getCreatures().get(
                    creaturesList.get(tiles[Xpos + x][YPos + y].getCreatureId()).getCreatureTypeId()).getImage().getImage(),
                    x * 40, y * 40);
        }

        if ((Main.getGameMode().equals(GameModeEnum.GAME) ||
                Main.getGameMode().equals(GameModeEnum.GAME_MENU)) &&
                (player.getXViewPos() == x) && (player.getYViewPos() == y)) {
            gc.drawImage(player.getImage().getImage(),
                    player.getXViewPos() * 40, player.getYViewPos() * 40);
        }

        // если у тайла есть верхний уровень, рисуем его поверх персонажа или NPC
        if (editor.getTilesList().getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).isTwoLayer()) {
            gc.drawImage(editor.getTilesList().getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).getUpLayerImage().getImage(),
                    x * 40, y * 40);
        }
    }
}
