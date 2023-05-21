package model.entity.effects;

import lombok.Getter;
import lombok.Setter;
import model.entity.map.Items;

/**
 * Параметры эффектов
 */
@Getter
@Setter
public class EffectParams {
    private String strId; // идентификатор эффекта
    private Integer power; // сила эффекта
    private Integer durability; // продолжительность эффекта
    private Items baseItem; // предмет, дающий эффект
    private EffectTypeEnum type; // тип эффекта

    public EffectParams() {
    }

    public EffectParams(String strId) {
        this.strId = strId;
    }
}
