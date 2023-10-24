package model.entity;

import lombok.Getter;
import lombok.Setter;
import view.Game;

/**
 * Размер инкрустата
 */
public enum InlayerSizeEnum {
    SMALL(Game.getText("SMALL"), 1),
    MEDIUM(Game.getText("MEDIUM"), 2),
    LARGE(Game.getText("LARGE"), 3),
    GREAT(Game.getText("GREAT"), 4);

    @Getter
    @Setter
    private String desc;

    @Getter
    private int size;

    InlayerSizeEnum(String desc, int size) {
        this.desc = desc;
        this.size = size;
    }
}
