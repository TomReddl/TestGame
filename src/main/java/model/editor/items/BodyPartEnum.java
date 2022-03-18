package model.editor.items;

import lombok.Getter;

/*
 * Список частей тела, на которые можно надеть какую-то одежду
 */
public enum BodyPartEnum {
    UNDERWEAR("Нижнее белье"),
    SHIRT("Рубашка"),
    HEAD("Голова"),
    FACE("Лицо"),
    NECKLACE("Ожерелье"),
    TORSO("Торс"),
    LEGS("Ноги"),
    SHOES("Обувь"),
    BELT("Пояс"),
    BACKPACK("Рюкзак"),
    GLOVES("Перчатки"),
    LEFT_ARM("Левая рука"),
    RIGHT_ARM("Правая рука");

    @Getter
    private final String desc;

    BodyPartEnum(String desc) {
        this.desc = desc;
    }
}
