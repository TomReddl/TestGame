package model.entity.player;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
 * Параметр персонажа
 */
@Getter
@Setter
public class Parameter implements Serializable {
    private Integer realValue;
    private Integer currentValue;
    private Integer experience;
}
