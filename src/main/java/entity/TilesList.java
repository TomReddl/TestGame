package entity;

import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

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
        tile1Count = Objects.requireNonNull(new File("src/main/resources/graphics/tiles").list()).length;
        tile2Count = Objects.requireNonNull(new File("src/main/resources/graphics/tiles2").list()).length;
        tiles1 = JsonUtils.getTiles();

        for (int i = 0; i < tile2Count; i++) {
            tiles2.add(new Tile());
            tiles2.get(i).setId(i);
            tiles2.get(i).setPassability(Boolean.FALSE);
        }
        tiles2.get(0).setPassability(Boolean.TRUE);
    }
}
