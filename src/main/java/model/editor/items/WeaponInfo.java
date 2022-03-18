package model.editor.items;

import lombok.Getter;
import lombok.Setter;

/*
 * Информация об оружии
 * */
@Setter
@Getter
public class WeaponInfo extends ItemInfo {
    private Integer damage; // базовый урон
    private Integer maxStrength; // прочность
    private Integer skill; // навык, отвечающий за владение данным оружием
    private Boolean oneHand; // одноручное оружие
}
