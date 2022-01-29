package entity;

import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

import java.util.List;

/*
 * Список тайлов в редакторе
 * */
@Setter
@Getter
public class TilesList {
    private List<TileInfo> tiles1;
    private List<TileInfo> tiles2;
    private int tiles1Count;
    private int tiles2Count;

    public TilesList() {
        tiles1 = JsonUtils.getTiles1();
        tiles2 = JsonUtils.getTiles2();
        tiles1Count = tiles1.size();
        tiles2Count = tiles2.size();
    }
}
