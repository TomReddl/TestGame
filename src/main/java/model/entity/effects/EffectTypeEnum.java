package model.entity.effects;

import lombok.Getter;
import view.Game;

/**
 * Типы эффектов
 */
public enum EffectTypeEnum {
    POTION(Game.getText("POTION_EFFECT_TYPE")),   // зелье
    ITEM(Game.getText("ITEM_EFFECT_TYPE")),       // предмет
    DISEASE(Game.getText("DISEASE_EFFECT_TYPE")), // болезнь
    ABILITY(Game.getText("ABILITY_EFFECT_TYPE")); // способность

    @Getter
    private final String name;

    EffectTypeEnum(String name) {
        this.name = name;
    }
}
