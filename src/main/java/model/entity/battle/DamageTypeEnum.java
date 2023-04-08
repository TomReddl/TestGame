package model.entity.battle;

import lombok.Getter;
import view.Game;

/**
 * Тип урона
 */
public enum DamageTypeEnum {
    PIERCING_DAMAGE(Game.getText("PIERCING_DAMAGE")),     // Колющий урон
    CUTTING_DAMAGE(Game.getText("CUTTING_DAMAGE")),       // Режущий урон
    FIRE_DAMAGE(Game.getText("FIRE_DAMAGE")),             // Урон огнем
    ACID_DAMAGE(Game.getText("ACID_DAMAGE")),             // Урон кислотой
    CRUSHING_DAMAGE(Game.getText("CRUSHING_DAMAGE"));     // Дробящий урон

    @Getter
    private final String desc;

    DamageTypeEnum(String desc) {
        this.desc = desc;
    }
}
