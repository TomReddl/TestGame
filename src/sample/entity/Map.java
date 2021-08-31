package sample.entity;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private List<List<TileInfo>> tiles = new ArrayList<>();

    public Map() {
        for (int i = 0; i < 15; i++) {
            tiles.add(new ArrayList<>());
            for (int j = 0; j < 15; j++) {
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
}
