package model.entity;

import view.Game;
import lombok.Getter;
import lombok.Setter;

/*
* Тип предмета
* */
public enum ItemTypeEnum {
  ALL(Game.getText("ALL")),
  EAT(Game.getText("EAT")),
  CLOTHES(Game.getText("CLOTHES")),
  WEAPON(Game.getText("WEAPON")),
  RESOURCE(Game.getText("RESOURCE")),
  BOOK(Game.getText("BOOK")),
  TREASURE(Game.getText("TREASURE")),
  INGREDIENT(Game.getText("INGREDIENT")),
  POTION(Game.getText("POTION")),
  COMMON(Game.getText("COMMON")),
  TRASH(Game.getText("TRASH"));

  @Getter
  @Setter
  private String desc;

  ItemTypeEnum(String desc) {
    this.desc = desc;
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
