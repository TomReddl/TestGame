package model.entity.effects;

import lombok.Getter;
import view.Game;

/**
 * Типы эффектов
 */
public enum EffectTypeEnum {
    POTION(Game.getText("POTION_EFFECT_TYPE")),
    ITEM(Game.getText("ITEM_EFFECT_TYPE")),
    DISEASE(Game.getText("DISEASE_EFFECT_TYPE")),
    ABILITY(Game.getText("ABILITY_EFFECT_TYPE"));

    @Getter
    private final String name;

    EffectTypeEnum(String name) {
        this.name = name;
    }
}
