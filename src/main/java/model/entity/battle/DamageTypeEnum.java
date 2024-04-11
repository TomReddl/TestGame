package model.entity.battle;

import lombok.Getter;
import view.Game;

/**
 * Тип урона
 */
public enum DamageTypeEnum {
    PIERCING_DAMAGE(Game.getText("PIERCING_DAMAGE"), true),     // Колющий урон
    CUTTING_DAMAGE(Game.getText("CUTTING_DAMAGE"), true),       // Режущий урон
    CRUSHING_DAMAGE(Game.getText("CRUSHING_DAMAGE"), true),     // Дробящий урон
    EXPLOSIVE_DAMAGE(Game.getText("EXPLOSIVE_DAMAGE"), false),  // Урон взрывом
    FIRE_DAMAGE(Game.getText("FIRE_DAMAGE"), false),            // Урон огнем
    ELECTRIC_DAMAGE(Game.getText("ELECTRIC_DAMAGE"), false),    // Урон электричеством
    FROST_DAMAGE(Game.getText("FROST_DAMAGE"), false),          // Урон холодом
    ACID_DAMAGE(Game.getText("ACID_DAMAGE"), false),            // Урон кислотой
    ROT_DAMAGE(Game.getText("ROT_DAMAGE"), false);              // Урон гнилью

    @Getter
    private final String desc;
    @Getter
    private final boolean bloody; // кровоточит ли рана, нанесенная этим типом урона

    DamageTypeEnum(String desc, boolean bloody) {
        this.desc = desc;
        this.bloody = bloody;
    }
}
