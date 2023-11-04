package model.editor;

import lombok.Getter;

/**
 * Типы тайлов
 */
public enum  TileTypeEnum {
    NONE("Нет типа"),
    DOOR("Дверь"),
    CONTAINER("Контейнер"),
    MANNEQUIN("Манекен"), // надетые на манекен предметы отображаются на нем
    MOVABLE("Перемещаемый"), // этот тип тайлов можно толкать
    WALL("Стена"),
    BED("Кровать"),
    BATH("Ванна"),
    CLOCK("Часы"),
    PLANT("Растение"), // с тайлов этого типа можно собрать урожай рукой
    WATER("Вода"),
    EARTH("Земля"),
    ORE("Руда"),
    CRYSTAL("Кристалл"),
    WOOD("Дерево"),
    CROPS("Посевы"), // тип тайла, который можно собрать косой
    ALCHEMY_TABLE("Алхимический стол"),
    ALCHEMY_LABORATORY("Алхимическая лаборатория"),
    INGREDIENTS_COMBINER("Объединитель ингредиентов"),
    DUPLICATOR("Дубликатор ингредиентов"),
    INLAYER_DUPLICATOR("Дубликатор инкрустатов"),
    JEWELRY_TABLE("Стол для зачарования"),
    CRAFTING_PLACE("Место для крафта"),
    TRAINING_DUMMY("Тренировочный манекен");

    @Getter
    private final String desc;

    TileTypeEnum(String desc) {
        this.desc = desc;
    }
}
