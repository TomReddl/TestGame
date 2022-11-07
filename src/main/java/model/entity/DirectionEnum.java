package model.entity;

import lombok.Getter;

/**
 * Направления движения персонажа
 */
public enum DirectionEnum {
    UP("Вверх"),
    DOWN("Вниз"),
    LEFT("Влево"),
    RIGHT("Вправо"),
    NONE("Нет");

    @Getter
    private final String desc;

    DirectionEnum(String desc) {
        this.desc = desc;
    }
}
