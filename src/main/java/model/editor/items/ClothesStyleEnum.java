package model.editor.items;

import lombok.Getter;
import lombok.Setter;
import view.Game;

/**
 * Стили одежды
 */
public enum ClothesStyleEnum {
    COMMON(Game.getText("COMMON_STYLE")),       // обычный
    FRONTIER(Game.getText("FRONTIER_STYLE")),   // фронтир
    CATTGAR(Game.getText("CATTGAR_STYLE")),     // Каттгар
    NALHAIM(Game.getText("NALHAIM_STYLE")),     // Налхейм
    RASHAN(Game.getText("RASHAN_STYLE")),       // Рашанский халифат
    BEGGAR(Game.getText("BEGGAR_STYLE")),       // нищий
    HOLIDAY(Game.getText("HOLIDAY_STYLE")),     // праздничный
    NOBLEMAN(Game.getText("NOBLEMAN_STYLE"));   // знать

    @Getter
    @Setter
    private String desc;

    ClothesStyleEnum(String desc) {
        this.desc = desc;
    }
}
