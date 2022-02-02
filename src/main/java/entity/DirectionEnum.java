package entity;

public enum DirectionEnum {
    UP("Вверх"),
    DOWN("Вниз"),
    LEFT("Влево"),
    RIGHT("Вправо"),
    NONE("Нет");

    private final String desc;

    DirectionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
