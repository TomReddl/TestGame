package sample.entity;

import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<List<TileInfo>> getTiles() {
        return tiles;
    }

    public void setTiles(List<List<TileInfo>> tiles) {
        this.tiles = tiles;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
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

    public void saveMap(Map map, String name) {
        try {
            FileOutputStream outputStream = new FileOutputStream("src/Data/World/" + name + ".wld");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            // сохраняем карту в файл
            objectOutputStream.writeObject(map);
            //закрываем поток и освобождаем ресурсы
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map loadMap(String name) {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/Data/World/" + name + ".wld");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Map) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }
}
