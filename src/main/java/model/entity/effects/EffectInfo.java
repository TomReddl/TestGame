package model.entity.effects;

import lombok.Getter;
import lombok.Setter;

/**
 * Информация об эффекте
 */
@Getter
@Setter
public class EffectInfo {
    private String name;
    private String desc;
    private String potionColor; // цвет бутылки зелья

    public EffectInfo(String name) {
        this.name = name;
    }

    public EffectInfo(String name, String potionColor) {
        this.name = name;
        this.potionColor = potionColor;
    }
}
