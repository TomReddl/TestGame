package model.entity.effects;

import lombok.Getter;
import lombok.Setter;

/*
 * Эффект
 */
@Getter
@Setter
public class EffectInfo {
    private String name;
    private String desc;

    public EffectInfo(String name) {
        this.name = name;
    }
}
