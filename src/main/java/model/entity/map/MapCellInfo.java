package model.entity.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.editor.TileInfo;
import model.editor.TileTypeEnum;
import view.Editor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Данные о точке на карте мира
 */
@Getter
@Setter
@NoArgsConstructor
public class MapCellInfo implements Serializable {
    private int tile1Id;
    private int tile2Id;
    private int tile1Strength; // текущая прочность нижнего тайла
    private int tile2Strength; // текущая прочность верхнего тайла
    private int pollutionId; // загрязнение на тайле
    private int fireId; // огонь на тайле
    private int zoneId; // к какой зоне принадлежит тайл
    private int roofId; // id крыши тайла, если она есть
    private Integer characterId; // если на тайле стоит персонаж, здесь отображается его id
    private Integer creatureId; // если на тайле стоит существо, здесь отображается его id
    private List<Items> items; // лежащие на тайле предметы
    private String desc; // кастомное описание тайла
    private int x; // координата x на карте мира
    private int y; // координата y на карте мира
    @JsonProperty("params")
    private Map<String, String> params; // дополнительные параметры

    public MapCellInfo(MapCellInfo oldInfo) {
        this.tile1Id = oldInfo.getTile1Id();
        this.tile1Strength = oldInfo.getTile1Strength();
        this.tile2Strength = oldInfo.getTile2Strength();
        this.tile2Id = oldInfo.getTile2Id();
        this.pollutionId = oldInfo.getFireId();
        this.fireId = oldInfo.getPollutionId();
        this.zoneId = oldInfo.getZoneId();
        this.characterId = oldInfo.getCharacterId();
        this.creatureId = oldInfo.getCreatureId();
        this.items = oldInfo.getItems();
        this.desc = oldInfo.getDesc();
        this.x = oldInfo.getX();
        this.y = oldInfo.getY();
        this.params = oldInfo.getParams();
    }

    /**
     * Получить тип нижнего тайла
     */
    @JsonIgnore
    public TileTypeEnum getTile1Type() {
        return getTile1Info().getType() == null ? TileTypeEnum.NONE : TileTypeEnum.valueOf(getTile1Info().getType());
    }

    /**
     * Получить тип верхнего тайла
     */
    @JsonIgnore
    public TileTypeEnum getTile2Type() {
        return getTile2Info().getType() == null ? TileTypeEnum.NONE : TileTypeEnum.valueOf(getTile2Info().getType());
    }

    /**
     * Получить информацию о нижнем тайле
     */
    @JsonIgnore
    public TileInfo getTile1Info() {
        return Editor.getTiles1().get(tile1Id);
    }

    /**
     * Получить информацию о верхнем тайле
     */
    @JsonIgnore
    public TileInfo getTile2Info() {
        return Editor.getTiles2().get(tile2Id);
    }

    /**
     * Видимость сквозь тайл
     */
    @JsonIgnore
    public boolean isVisibly() {
        return Editor.getTiles1().get(tile1Id).isVisibly() && Editor.getTiles2().get(tile2Id).isVisibly();
    }
}
