package model.entity;

import lombok.Getter;
import lombok.Setter;
import view.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Тип предмета
 */
public enum ItemTypeEnum {
    ALL(Game.getText("ALL")), // Все
    EAT(Game.getText("EAT")), // Еда
    CLOTHES(Game.getText("CLOTHES")), // Одежда
    WEAPON(Game.getText("WEAPON")), // Оружие
    RESOURCE(Game.getText("RESOURCE")), // Русурсы
    BOOK(Game.getText("BOOK")), // Книги
    TREASURE(Game.getText("TREASURE")), // Сокровища
    INGREDIENT(Game.getText("INGREDIENT")), // Ингридиенты
    POTION(Game.getText("POTION")), // Зелья
    COMMON(Game.getText("COMMON")), // Обычные
    KEY(Game.getText("KEY")), // Ключи
    TOOL(Game.getText("TOOL")), // Инструменты
    TRASH(Game.getText("TRASH")), // Мусор
    PICKLOCK(Game.getText("PICKLOCK")), // Отмычки
    SAPPER_TOOL(Game.getText("SAPPER_TOOL")), // Инструменты для обезвреживания ловушек
    BUILDING_TOOL(Game.getText("BUILDING_TOOL")), // Строительные инструменты
    PICKAXE(Game.getText("PICKAXE")), // Кирки
    AXE(Game.getText("AXE")), // Топоры
    SHOVEL(Game.getText("SHOVEL")), // Лопаты
    SCYTHE(Game.getText("SCYTHE")), // Косы
    WATERING_CAN(Game.getText("WATERING_CAN")), // Лейки
    BOTTLE(Game.getText("BOTTLE")), // Бутылка
    BROOM(Game.getText("BROOM")), // Метла
    METAL_DETECTOR(Game.getText("METAL_DETECTOR")), // Металлоискатель
    ECHOLOCATOR(Game.getText("ECHOLOCATOR")), // Эхолокатор
    CLOCK(Game.getText("CLOCK")), // Часы
    FOLDABLE(Game.getText("FOLDABLE")), // Раскладываемый
    EXPLOSIVES(Game.getText("EXPLOSIVES")), // Взрывчатка
    SEED(Game.getText("SEED")); // Семена растений

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
        return null;
    }

    // получить список типов предметов, используемых в фильтре инвентаря
    public static List<ItemTypeEnum> getItemTypesForFilter() {
      List<ItemTypeEnum> list = new ArrayList<>();
      list.add(ItemTypeEnum.ALL);
      list.add(ItemTypeEnum.EAT);
      list.add(ItemTypeEnum.CLOTHES);
      list.add(ItemTypeEnum.WEAPON);
      list.add(ItemTypeEnum.RESOURCE);
      list.add(ItemTypeEnum.BOOK);
      list.add(ItemTypeEnum.TREASURE);
      list.add(ItemTypeEnum.INGREDIENT);
      list.add(ItemTypeEnum.POTION);
      list.add(ItemTypeEnum.KEY);
      list.add(ItemTypeEnum.TOOL);
      list.add(ItemTypeEnum.TRASH);
      return list;
    }
}
