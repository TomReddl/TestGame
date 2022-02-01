package entity;

/*
* Тип предмета
* */
public enum ItemType {
  EAT("Еда"),
  CLOTHES("Одежда"),
  WEAPON("Оружие"),
  RESOURCE("Ресурс"),
  BOOK("Книга"),
  TREASURE("Сокровище"),
  INGREDIENT("Ингридиент"),
  POTION("Зелье"),
  TRASH("Мусор");

  private final String desc;

  ItemType(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  public static ItemType getItemTypeByCode(String desc) {
    for (ItemType item : ItemType.values()) {
      if (item.getDesc().equals(desc)) {
        return item;
      }
    }
    throw new IllegalArgumentException("Некорректный тип предмета: " + desc);
  }
}
