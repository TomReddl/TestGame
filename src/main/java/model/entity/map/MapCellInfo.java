package model.entity.map;

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
    private int pollutionId; // загрязнение на тайле
    private int zoneId; // к какой зоне принадлежит тайл
    private Integer npcId; // если на тайле стоит NPC, здесь отображается его id
    private Integer creatureId; // если на тайле стоит существо, здесь отображается его id
    private List<Items> items; // лежащие на тайле предметы
}
