package sample.entity;

import java.util.ArrayList;
import java.util.List;

public class TilesList {
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

    private List<Tile> tiles1 = new ArrayList<>();
    private List<Tile> tiles2 = new ArrayList<>();

    public TilesList() {
        for (int i = 0; i < 27; i++) {
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

        for (int i = 0; i < 12; i++) {
            tiles2.add(new Tile());
            tiles2.get(i).setId(i);
            tiles2.get(i).setPassability(Boolean.FALSE);
        }
        tiles2.get(0).setPassability(Boolean.TRUE);
    }

}
