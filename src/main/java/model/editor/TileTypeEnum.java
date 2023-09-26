package model.editor;

import lombok.Getter;

/**
 * Типы тайлов
 */
public enum  TileTypeEnum {
    NONE("Нет типа"),
    DOOR("Дверь"),
    CONTAINER("Контейнер"),
    DUMMY("Манекен"), // надетые на манекен предметы отображаются на нем
    MOVABLE("Перемещаемый"), // этот тип тайлов можно толкать
    WALL("Стена"),
    BED("Кровать"),
    CLOCK("Часы"),
    PLANT("Растение"), // с тайлов этого типа можно собрать урожай рукой
    WATER("Вода"),
    EARTH("Земля"),
    ORE("Руда"),
    WOOD("Дерево"),
    CROPS("Посевы"), // тип тайла, который можно собрать косой
    ALCHEMY_TABLE("Алхимический стол"),
    ALCHEMY_LABORATORY("Алхимическая лаборатория"),
    INGREDIENTS_COMBINER("Объединитель ингредиентов"),
    DUPLICATOR("Дубликатор ингредиентов"),
    CRAFTING_PLACE("Место для крафта");

    @Getter
    private final String desc;

    TileTypeEnum(String desc) {
        this.desc = desc;
    }
}
