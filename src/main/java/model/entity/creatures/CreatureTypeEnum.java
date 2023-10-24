package model.entity.creatures;

import lombok.Getter;
import view.Game;

/**
 * Типы существ
 */
public enum CreatureTypeEnum {
    INSECT(Game.getText("INSECT")),           // Насекомое
    REPTILE(Game.getText("REPTILE")),         // Рептилия
    MOLD(Game.getText("MOLD")),               // Плесневение
    HUMANOID(Game.getText("HUMANOID")),       // Гуманоид
    ABERRATION(Game.getText("ABERRATION"));   // Аберрация
    @Getter
    private final String desc;

    CreatureTypeEnum(String desc) {
        this.desc = desc;
    }
}
