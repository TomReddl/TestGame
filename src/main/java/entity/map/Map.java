package entity.map;

import gui.Editor;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class Map implements Serializable {
    private MapCellInfo[][] tiles = new MapCellInfo[300][300];
    List<NPC> npcList = new ArrayList<>();
    List<Creature> creaturesList = new ArrayList<>();
    private String mapName;

    public Map() {
        mapName = "1";
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                var taleInfo = new MapCellInfo();
                taleInfo.setTile1Id(1);
                taleInfo.setTile1Id(0);
                tiles[i][j] = taleInfo;
            }
        }
    }

    public void drawMap(int Xpos, int YPos, GraphicsContext gc, Editor editor) {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                gc.drawImage(editor.getTilesList().getTiles1().get(tiles[Xpos + x][YPos + y].getTile1Id()).getImage().getImage(),
                        x * 40, y * 40);
                gc.drawImage(editor.getTilesList().getTiles2().get(tiles[Xpos + x][YPos + y].getTile2Id()).getImage().getImage(),
                        x * 40, y * 40);
            }
        }

        for (int x = 0; x < 15; x++) { // NPC и существа рисуются в отдельном цикле, чтобы быть поверх уже отрисованной карты
            for (int y = 0; y < 15; y++) {
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
            }
        }
    }

    public void saveMap(String name) {
        try (var outputStream = new FileOutputStream("src/main/resources/world/" + name + ".wld");
             var objectOutputStream = new ObjectOutputStream(outputStream);) {
            objectOutputStream.writeObject(this);
        } catch (Exception ex) {
            log.error("can not save the map, name={}", name, ex);
            throw new RuntimeException("can not save the map");
        }
    }

    public Map loadMap(String name) {
        try (var fileInputStream = new FileInputStream("src/main/resources/world/" + name + ".wld");
             var objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (Map) objectInputStream.readObject();
        } catch (Exception ex) {
            log.error("can not load the map, name={}", name, ex);
            throw new RuntimeException("can not load the map");
        }
    }
}
