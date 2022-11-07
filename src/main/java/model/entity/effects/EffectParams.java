package model.entity.effects;

import lombok.Getter;
import lombok.Setter;

/**
 * Параметры эффектов
 */
@Getter
@Setter
public class EffectParams {
    private String strId; // идентификатор эффекта
    private Integer power; // сила эффекта
    private Integer durability; // продолжительность эффекта
}
