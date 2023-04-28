package model.entity.battle;

import lombok.Getter;
import view.Game;

/**
 * Тип урона
 */
public enum DamageTypeEnum {
    PIERCING_DAMAGE(Game.getText("PIERCING_DAMAGE"), true),     // Колющий урон
    CUTTING_DAMAGE(Game.getText("CUTTING_DAMAGE"), true),       // Режущий урон
    FIRE_DAMAGE(Game.getText("FIRE_DAMAGE"), false),            // Урон огнем
    ACID_DAMAGE(Game.getText("ACID_DAMAGE"), false),            // Урон кислотой
    CRUSHING_DAMAGE(Game.getText("CRUSHING_DAMAGE"), true);     // Дробящий урон

    @Getter
    private final String desc;
    @Getter
    private final boolean bloody; // кровоточит ли рана, нанесенная этим типом урона

    DamageTypeEnum(String desc, boolean bloody) {
        this.desc = desc;
        this.bloody = bloody;
    }
}
