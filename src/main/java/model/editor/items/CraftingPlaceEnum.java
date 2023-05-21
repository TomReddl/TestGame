package model.editor.items;

import lombok.Getter;

/**
 * Виды столов для крафта
 */
public enum CraftingPlaceEnum {
    foundry("Плавильня"),
    cookingTable("Кулинарный стол"),
    jewelryTable("Ювелирный стол"),
    loom("Ткацкий станок"),
    joinersTable("Столярный стол"),
    workbench("Верстак"),
    anvil("Наковальня");

    @Getter
    private final String desc;

    CraftingPlaceEnum(String desc) {
        this.desc = desc;
    }
}
