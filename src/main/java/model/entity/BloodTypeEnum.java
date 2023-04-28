package model.entity;

import lombok.Getter;
import view.Game;

/**
 * Тип крови существа
 */
public enum BloodTypeEnum {
    BLOOD(Game.getPollutionsText("0"), 1, 2, 3),
    OIL(Game.getPollutionsText("2"), 7, 8, 9),
    MUCUS(Game.getPollutionsText("3"), 10, 11, 12),
    ACID(Game.getPollutionsText("4"), 13, 14, 15),
    DIRT(Game.getPollutionsText("5"), 16, 17, 18);

    @Getter
    private final String desc;
    @Getter
    private final int pollution1Id; // id первого уровня загрязнения при проливании крови на землю
    @Getter
    private final int pollution2Id; // id второго уровня
    @Getter
    private final int pollution3Id; // id третьего уровня

    BloodTypeEnum(String desc, int one, int two, int three) {
        this.desc = desc;
        this.pollution1Id = one;
        this.pollution2Id = two;
        this.pollution3Id = three;
    }
}
