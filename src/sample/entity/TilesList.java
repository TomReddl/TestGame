package sample.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TilesList {
    private List<Tile> tiles1 = new ArrayList<>();
    private List<Tile> tiles2 = new ArrayList<>();
    private int tile1Count; // количество тайлов нижнего уровня, равно числу картинок в папке src/Data/Graphics/Tiles
    private int tile2Count;

    public List<Tile> getTiles1() {
        return tiles1;
    }

    public void setTiles1(List<Tile> tiles1) {
        this.tiles1 = tiles1;
    }

    public List<Tile> getTiles2() {
        return tiles2;
    }

    public void setTiles2(List<Tile> tiles2) {
        this.tiles2 = tiles2;
    }

    public TilesList() {
        tile1Count = new File("src/Data/Graphics/Tiles/").list().length;
        tile2Count = new File("src/Data/Graphics/Tiles2/").list().length;
        for (int i = 0; i < tile1Count; i++) {
            tiles1.add(new Tile());
            tiles1.get(i).setId(i);
            tiles1.get(i).setPassability(Boolean.FALSE);
        }
        tiles1.get(0).setPassability(Boolean.TRUE);
        tiles1.get(1).setPassability(Boolean.TRUE);
        tiles1.get(14).setPassability(Boolean.TRUE);
        tiles1.get(23).setPassability(Boolean.TRUE);
        tiles1.get(24).setPassability(Boolean.TRUE);
        tiles1.get(25).setPassability(Boolean.TRUE);
        tiles1.get(26).setPassability(Boolean.TRUE);

        for (int i = 0; i < tile2Count; i++) {
            tiles2.add(new Tile());
            tiles2.get(i).setId(i);
            tiles2.get(i).setPassability(Boolean.FALSE);
        }
        tiles2.get(0).setPassability(Boolean.TRUE);
    }

    public int getTile1Count() {
        return tile1Count;
    }

    public void setTile1Count(int tile1Count) {
        this.tile1Count = tile1Count;
    }

    public int getTile2Count() {
        return tile2Count;
    }

    public void setTile2Count(int tile2Count) {
        this.tile2Count = tile2Count;
    }

}
