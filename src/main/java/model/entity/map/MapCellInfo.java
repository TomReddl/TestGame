package model.entity.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.editor.TileInfo;
import model.editor.TileTypeEnum;
import view.Game;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// Данные о точке на карте мира
public class MapCellInfo implements Serializable {
    private int tile1Id;
    private int tile2Id;
    private int pollutionId; // загрязнение на тайле
    private int zoneId; // к какой зоне принадлежит тайл
    private Integer npcId; // если на тайле стоит NPC, здесь отображается его id
    private Integer creatureId; // если на тайле стоит существо, здесь отображается его id
    private List<Items> items; // лежащие на тайле предметы
    private String desc; // кастомное описание тайла

    public MapCellInfo(MapCellInfo oldInfo) {
        this.tile1Id = oldInfo.getTile1Id();
        this.tile2Id = oldInfo.getTile2Id();
        this.pollutionId = oldInfo.getPollutionId();
        this.zoneId = oldInfo.getZoneId();
        this.npcId = oldInfo.getNpcId();
        this.creatureId = oldInfo.getCreatureId();
        this.items = oldInfo.getItems();
        this.desc = oldInfo.getDesc();
    }

    // получить тип нижнего тайла
    public TileTypeEnum getTile1Type() {
        return TileTypeEnum.valueOf(getTile1Info().getType());
    }

    // получить тип верхнего тайла
    public TileTypeEnum getTile2Type() {
        return TileTypeEnum.valueOf(getTile2Info().getType());
    }

    // получить информацию о нижнем тайле
    public TileInfo getTile1Info() {
        return Game.getEditor().getTiles1().get(tile1Id);
    }

    // получить информацию о верхнем тайле
    public TileInfo getTile2Info() {
        return Game.getEditor().getTiles2().get(tile2Id);
    }
}
