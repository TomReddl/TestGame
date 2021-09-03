package sample.entity;

import javafx.scene.canvas.GraphicsContext;
import lombok.Generated;
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
    private List<List<TileInfo>> tiles = new ArrayList<>();
    private String mapName;

    public Map() {
        mapName = "1";
        for (int i = 0; i < 300; i++) {
            tiles.add(new ArrayList<>());
            for (int j = 0; j < 300; j++) {
                tiles.get(i).add(new TileInfo());
                tiles.get(i).get(j).setTile1Id(1);
                tiles.get(i).get(j).setTile2Id(0);
            }
        }
    }

    public void drawMap(int Xpos, int YPos, GraphicsContext gc, TilesList tilesList) {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                gc.drawImage(tilesList.getTiles1().get(tiles.get(Xpos + x).get(YPos + y).getTile1Id()).getImage().getImage(),
                        x * 40, y * 40);
                gc.drawImage(tilesList.getTiles2().get(tiles.get(Xpos + x).get(YPos + y).getTile2Id()).getImage().getImage(),
                        x * 40, y * 40);
            }
        }
    }

    public void saveMap(String name) {
        try (var outputStream = new FileOutputStream("src/Data/World/" + name + ".wld");
             var objectOutputStream = new ObjectOutputStream(outputStream);) {
            objectOutputStream.writeObject(this);
        } catch (Exception ex) {
            log.error("can not load the map, name={}", name, ex);
            throw new RuntimeException("can not load the map");
        }
    }

    public Map loadMap(String name) {
        try (var fileInputStream = new FileInputStream("src/Data/World/" + name + ".wld");
             var objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (Map) objectInputStream.readObject();
        } catch (Exception ex) {
            log.error("can not load the map, name={}", name, ex);
            throw new RuntimeException("can not load the map");
        }
    }
}
