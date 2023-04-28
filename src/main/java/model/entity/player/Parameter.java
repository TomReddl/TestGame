package model.entity.player;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Параметр персонажа
 */
@Getter
@Setter
public class Parameter implements Serializable {
    private String strId; // идентификатор параметра
    private Integer realValue; // значение параметра без учета модификаторов
    private Integer currentValue; // текущее значение параметра
    private Integer experience; // опыт до повышения навыка
}
