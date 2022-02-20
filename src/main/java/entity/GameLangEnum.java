package entity;

import lombok.Getter;

public enum GameLangEnum {
    RU("Русский"),
    EN("English");

    @Getter
    private final String desc;

    GameLangEnum(String desc) {
        this.desc = desc;
    }

    public static GameLangEnum getGameLangByCode(String desc) {
        for (GameLangEnum lang : GameLangEnum.values()) {
            if (lang.getDesc().equals(desc)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Некорректный язык: " + desc);
    }
}
