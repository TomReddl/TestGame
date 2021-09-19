package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TileInfo implements Serializable {
    private int tile1Id;
    private int tile2Id;
    private Integer npcId; // если на тайле стоит NPC, здесь отображается его id
    private Integer creatureId; // если на тайле стоит существо, здесь отображается его id
}
