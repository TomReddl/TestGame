package model.entity.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.editor.TileInfo;
import model.editor.TileTypeEnum;
import view.Game;

import java.io.Serializable;
import java.util.List;

/**
 * Данные о точке на карте мира
 */
@Getter
@Setter
@NoArgsConstructor
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
    @JsonIgnore
    public TileTypeEnum getTile1Type() {
        return getTile1Info().getType() == null ? TileTypeEnum.NONE : TileTypeEnum.valueOf(getTile1Info().getType());
    }

    // получить тип верхнего тайла
    @JsonIgnore
    public TileTypeEnum getTile2Type() {
        return getTile2Info().getType() == null ? TileTypeEnum.NONE : TileTypeEnum.valueOf(getTile2Info().getType());
    }

    // получить информацию о нижнем тайле
    @JsonIgnore
    public TileInfo getTile1Info() {
        return Game.getEditor().getTiles1().get(tile1Id);
    }

    // получить информацию о верхнем тайле
    @JsonIgnore
    public TileInfo getTile2Info() {
        return Game.getEditor().getTiles2().get(tile2Id);
    }
}
