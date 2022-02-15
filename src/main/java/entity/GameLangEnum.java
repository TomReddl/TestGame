package entity;

public enum GameLangEnum {
    RU("Русский"),
    EN("English");

    private final String desc;

    GameLangEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
