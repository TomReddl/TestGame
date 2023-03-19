package model.entity.player;

import lombok.Getter;

/**
 * Цвет волос
 */
public enum HairColorEnum {
    Brown("Коричневый"),
    Ginger("Рыжий");

    @Getter
    private final String desc;

    HairColorEnum(String desc) {
        this.desc = desc;
    }
}
