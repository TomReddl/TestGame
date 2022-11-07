package model.editor.items;

import lombok.Getter;
import lombok.Setter;
import view.Game;

/**
 * Принадлежность одежды к полу
 */
public enum ClothesGenderEnum {
    MALE(Game.getText("MALE_CLOTHES_GENDER")),       // Мужская
    FEMALE(Game.getText("FEMALE_CLOTHES_GENDER")),   // Женская
    UNISEX(Game.getText("UNISEX_CLOTHES_GENDER"));   // Унисекс

    @Getter
    @Setter
    private String desc;

    ClothesGenderEnum(String desc) {
        this.desc = desc;
    }
}
