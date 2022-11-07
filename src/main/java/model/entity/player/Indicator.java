package model.entity.player;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Показатель персонажа
 */
@Getter
@Setter
public class Indicator implements Serializable {
    private Integer currentValue; // текущее значение
    private Integer minValue; // минимальное значение
    private Integer maxValue; // максимальное значение
}
