package entity.old;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TileInfo implements Serializable {
    private int tile1Id;
    private int tile2Id;
}
