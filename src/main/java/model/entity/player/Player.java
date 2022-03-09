package model.entity.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.editor.ItemInfo;
import model.entity.DirectionEnum;
import model.entity.map.Items;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import view.Game;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player implements Serializable {
    @JsonIgnore
    private ImageView image;
    @JsonProperty("xPosition")
    private int xPosition; // координаты персонажа на карте
    @JsonProperty("yPosition")
    private int yPosition;
    @JsonProperty("xMapPos")
    private int xMapPos; // сдвиг области отрисовки карты от начала координат
    @JsonProperty("yMapPos")
    private int yMapPos;
    @JsonProperty("xViewPos")
    private int xViewPos; // положение персонажа в отрисованной области
    @JsonProperty("yViewPos")
    private int yViewPos;
    @JsonProperty("direction")
    private DirectionEnum direction; // направление движения персонажа
    @JsonProperty("inventory")
    private List<Items> inventory = new ArrayList<>(); // вещи в рюкзаке персонажа
    @JsonProperty("legacy")
    private List<Parameter> legacies = new ArrayList<>(); // наследия персонажа
    @JsonProperty("characteristics")
    private List<Parameter> characteristics = new ArrayList<>(); // характеристики персонажа
    @JsonProperty("skills")
    private List<Parameter> skills = new ArrayList<>(); // навыки персонажа

    public Player() {
        image = new ImageView("/graphics/characters/31.png");
        image.setVisible(Boolean.FALSE);
        direction = DirectionEnum.RIGHT;

        Items items = new Items();
        items.setTypeId(1);
        items.setCount(3);
        inventory.add(items);

        Items items2 = new Items();
        items2.setTypeId(3);
        items2.setCount(1);
        inventory.add(items2);

        Items items3 = new Items();
        items3.setTypeId(11);
        items3.setCount(10);
        inventory.add(items3);

        for (int i = 0; i < 6; i++) {
            Parameter legacy = new Parameter();
            legacy.setCurrentValue(16);
            legacy.setRealValue(16);
            legacies.add(legacy);

            Parameter characteristic = new Parameter();
            characteristic.setCurrentValue(50);
            characteristic.setRealValue(50);
            characteristics.add(characteristic);
        }

        for (int i = 0; i < 24; i++) {
            Parameter skill = new Parameter();
            skill.setCurrentValue(10);
            skill.setRealValue(10);
            skills.add(skill);
        }
    }

    public static BigDecimal getCurrentWeight(List<Items> inventory) {
        int totalWeight = 0;
        for (Items item : inventory) {
            ItemInfo itemInfo = Game.getEditor().getItems().get(item.getTypeId());
            totalWeight += itemInfo.getWeight() * item.getCount();
        }
        return BigDecimal.valueOf(totalWeight).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal getMaxWeight(Parameter power) {
        return BigDecimal.valueOf(power.getCurrentValue() * 3);
    }

    public static BigDecimal getCurrentVolume(List<Items> inventory) {
        int totalVolume = 0;
        for (Items item : inventory) {
            ItemInfo itemInfo = Game.getEditor().getItems().get(item.getTypeId());
            totalVolume += itemInfo.getVolume() * item.getCount();
        }
        return BigDecimal.valueOf(totalVolume).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal getMaxVolume() {
        return BigDecimal.valueOf(100000).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    public static void addItem(Items item, List<Items> inventory) {
        boolean found = false;
        for (Items i : inventory) {
            ItemInfo itemInfo = Game.getEditor().getItems().get(item.getTypeId());
            if (item.getTypeId() == i.getTypeId() && itemInfo.getStackable()) {
                i.setCount(i.getCount() + item.getCount());
                found = true;
                break;
            }
        }
        if (!found) {
            inventory.add(item);
        }
    }
}
