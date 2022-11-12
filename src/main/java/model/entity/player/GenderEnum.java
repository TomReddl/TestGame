package model.entity.player;

import lombok.Getter;

/**
 * Пол персонажа
 */
public enum GenderEnum {
    MALE("Мужчина"),
    FEMALE("Женщина"),
    ANDROGYNE("Нет пола");

    @Getter
    private final String desc;

    GenderEnum(String desc) {
        this.desc = desc;
    }
}
