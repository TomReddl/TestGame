package model.editor.items;

import lombok.Getter;
import lombok.Setter;
import model.entity.effects.EffectParams;

import java.util.List;

/*
 * Информация об оружии
 * */
@Setter
@Getter
public class WeaponInfo extends ItemInfo {
    private Integer damage; // базовый урон
    private Integer maxStrength; // Максимальная прочность
    private Integer skill; // навык, отвечающий за владение данным оружием
    private Boolean oneHand; // одноручное оружие
    private List<EffectParams> effects; // эффекты
}
