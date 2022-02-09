package entity;

public enum GameModeEnum {
    MAIN_MENU("Главное меню"),
    EDITOR("Редактор"),
    GAME("Игра"),
    GAME_MENU("Меню в игре");

    private final String desc;

    GameModeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
