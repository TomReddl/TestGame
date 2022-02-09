package entity;

/*
* Тип предмета
* */
public enum ItemTypeEnum {
  ALL("Все"),
  EAT("Еда"),
  CLOTHES("Одежда"),
  WEAPON("Оружие"),
  RESOURCE("Ресурсы"),
  BOOK("Книги"),
  TREASURE("Сокровища"),
  INGREDIENT("Ингридиенты"),
  POTION("Зелья"),
  COMMON("Бытовые"),
  TRASH("Мусор");

  private final String desc;

  ItemTypeEnum(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  public static ItemTypeEnum getItemTypeByCode(String desc) {
    for (ItemTypeEnum item : ItemTypeEnum.values()) {
      if (item.getDesc().equals(desc)) {
        return item;
      }
    }
    throw new IllegalArgumentException("Некорректный тип предмета: " + desc);
  }
}
