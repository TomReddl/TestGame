package entity;

import lombok.Getter;

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
