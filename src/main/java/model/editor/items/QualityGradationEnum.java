package model.editor.items;

import lombok.Getter;
import view.Game;

/**
 * Градация качества предмета
 */
public enum QualityGradationEnum {
    UNSUCCESSFUL(Game.getText("UNSUCCESSFUL"), 0.5),
    CHEAP(Game.getText("CHEAP"), 0.75),
    NORMAL(Game.getText("NORMAL"), 1),
    QUALITY(Game.getText("QUALITY"), 1.5),
    EXPERT(Game.getText("EXPERT"), 2),
    LEGENDARY(Game.getText("LEGENDARY"), 4);

    @Getter
    private final String name;
    @Getter
    private final double qualityLevel;

    QualityGradationEnum(String name, double qualityLevel) {
        this.name = name;
        this.qualityLevel = qualityLevel;
    }
}
