package entity.map;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
// Данные о точке на карте мира
public class MapCellInfo implements Serializable {
    private int tile1Id;
    private int tile2Id;
    private Integer npcId; // если на тайле стоит NPC, здесь отображается его id
    private Integer creatureId; // если на тайле стоит существо, здесь отображается его id
    private List<Item> items; // лежащие на тайле предметы
}
