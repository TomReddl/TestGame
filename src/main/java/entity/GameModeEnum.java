package entity;

public enum GameModeEnum {
    MENU("Меню"),
    EDITOR("Редактор"),
    GAME("Игра"),
    PAUSE("Пауза");

    private final String desc;

    GameModeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
