package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class TilesList {
    private List<Tile> tiles1 = new ArrayList<>();
    private List<Tile> tiles2 = new ArrayList<>();
    private int tile1Count; // количество тайлов нижнего уровня, равно числу картинок в папке src/Data/Graphics/Tiles
    private int tile2Count;

    public TilesList() {
        tile1Count = Objects.requireNonNull(new File("src/main/resources/Graphics/Tiles").list()).length;
        tile2Count = Objects.requireNonNull(new File("src/main/resources/Graphics/Tiles2").list()).length;
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
}
