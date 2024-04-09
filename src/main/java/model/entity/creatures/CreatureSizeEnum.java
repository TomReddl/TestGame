package model.entity.creatures;

import lombok.Getter;
import view.Game;

/**
 * Размеры существ
 */
public enum CreatureSizeEnum {
    TINY(Game.getGameText("TINY"), 1),              // Крошечный (10+ см., например- мышь)
    SMALL(Game.getGameText("SMALL"), 2),            // Маленький (1+ метр, например- собака)
    MEDIUM(Game.getGameText("MEDIUM"), 3),          // Средний (2+ метра, например- человек)
    LARGE(Game.getGameText("LARGE"), 4),            // Большой (3+ метра, например- брамин)
    HUGE(Game.getGameText("HUGE"), 5),              // Огромный (5+ метров, например- акула)
    GARGANTUAN(Game.getGameText("GARGANTUAN"), 6);  // Громадный (15+ метров, например- титан)
    @Getter
    private final String desc;
    @Getter
    private final int value;

    CreatureSizeEnum(String desc, int value) {
        this.desc = desc;
        this.value = value;
    }
}
