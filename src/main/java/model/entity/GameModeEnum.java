package model.entity;

import lombok.Getter;

/**
 * Режимы игры
 */
public enum GameModeEnum {
    MAIN_MENU("Главное меню"),
    EDITOR("Редактор"),
    GAME("Игра"),
    GAME_MENU("Меню в игре");

    @Getter
    private final String desc;

    GameModeEnum(String desc) {
        this.desc = desc;
    }
}
