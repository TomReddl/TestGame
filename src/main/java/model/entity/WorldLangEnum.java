package model.entity;

import lombok.Getter;
import view.Game;

/**
 * Языки, существующие в мире игры
 */
public enum WorldLangEnum {
    ELDER_CATTGAR(Game.getText("ELDER_CATTGAR")), // древне-каттгарский
    CATTGAR(Game.getText("CATTGAR")), // каттгарский
    SABBARITH(Game.getText("SABBARITH")), // саббаритский
    NALHAIM(Game.getText("NALHAIM")); // налхеймский

    @Getter
    private final String desc;

    WorldLangEnum(String desc) {
        this.desc = desc;
    }

    public static WorldLangEnum getLangByCode(String desc) {
        for (WorldLangEnum lang : WorldLangEnum.values()) {
            if (lang.getDesc().equals(desc)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Некорректный язык: " + desc);
    }
}
