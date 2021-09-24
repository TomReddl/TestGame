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
    private List<Tile> groundTiles;
    private List<Tile> gameObjects = new ArrayList<>();
    private int groundCounts; // количество тайлов нижнего уровня, равно числу картинок в папке src/Data/Graphics/Tiles
    private int tile2Count;

    public TilesList() {
        tile2Count = Objects.requireNonNull(new File("src/main/resources/graphics/tiles2").list()).length;
        groundTiles = JsonUtils.getTiles();
        groundCounts = groundTiles.size();

        for (int i = 0; i < tile2Count; i++) {
            gameObjects.add(new Tile());
            gameObjects.get(i).setId(i);
            gameObjects.get(i).setPassability(Boolean.FALSE);
        }
        gameObjects.get(0).setPassability(Boolean.TRUE);
    }
}
