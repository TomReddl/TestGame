package model.editor;

import lombok.Getter;

/**
 * Типы тайлов
 */
public enum  TileTypeEnum {
    NONE("Нет типа"),
    DOOR("Дверь"),
    CONTAINER("Контейнер"),
    MOVABLE("Перемещаемый"), // этот тип тайлов можно толкать
    WALL("Стена"),
    BED("Кровать"),
    CLOCK("Часы"),
    PLANT("Растение"),
    WATER("Вода"),
    EARTH("Земля"),
    ORE("Руда"),
    CRAFTING_PLACE("Место для крафта");

    @Getter
    private final String desc;

    TileTypeEnum(String desc) {
        this.desc = desc;
    }
}
