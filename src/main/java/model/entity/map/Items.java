package model.entity.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.editor.items.ItemInfo;
import model.entity.effects.EffectParams;
import view.Editor;
import view.inventory.InventoryPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Информация о предмете
 */
@Getter
@Setter
@NoArgsConstructor
public class Items {
    @JsonProperty("id")
    private int id; // идентификатор данного конкретного предмета
    @JsonProperty("typeId")
    private int typeId; // идентификатор типа предмета
    @JsonProperty("count")
    private int count; // количество предметов в стеке
    @JsonProperty("equipment")
    private boolean equipment; // признак экипированного предмета
    @JsonProperty("currentStrength")
    private int currentStrength; // Текущая прочность
    @JsonProperty("params")
    private Map<String, String> params; // дополнительные параметры
    @JsonProperty("name")
    private String name; // кастомное название предмета
    @JsonProperty("price")
    private Long price; // кастомная цена предмета
    @JsonProperty("effects")
    private List<EffectParams> effects; // кастомные эффекты предмета
    @JsonProperty("unselectable")
    private boolean unselectable; // предмет нельзя подобрать (например, активированная взрывчатка)
    @JsonProperty("inlayerId")
    private Integer inlayerId; // идентификатор инкрустата (для зачаровываемых предметов)

    public Items(int typeId, int count) {
        this.typeId = typeId;
        this.count = count;
        this.inlayerId = this.getInfo().getInlayerId();
        this.setCurrentStrength(this.getInfo().getMaxStrength());
        this.setParams(this.getInfo().getParams());
        if (this.getInfo().getParams() != null &&
                this.getInfo().getParams().get("capacity") != null) {
            this.getParams().put("currentCapacity", "0");
        }
    }

    public Items(Items anotherItem, int count) {
        this.id = anotherItem.getId();
        this.typeId = anotherItem.getTypeId();
        this.count = count;
        this.equipment = anotherItem.isEquipment();
        this.currentStrength = anotherItem.getCurrentStrength();
        this.params = anotherItem.getParams();
        this.name = anotherItem.getName();
        this.effects = anotherItem.getEffects();
        this.price = anotherItem.getPrice();
        this.unselectable = anotherItem.isUnselectable();
        this.inlayerId = anotherItem.getInlayerId();
    }

    private static Comparator<Items> compareByName = Comparator.comparing(o -> o.getInfo().getName());

    private static Comparator<Items> compareByWeight = Comparator.comparing(o -> o.getInfo().getWeight());

    private static Comparator<Items> compareByVolume = Comparator.comparing(o -> o.getInfo().getVolume());

    private static Comparator<Items> compareByPrice = Comparator.comparing(o -> o.getInfo().getPrice());

    public static HashMap<InventoryPanel.SortType, Comparator<Items>> comparators = new HashMap<>();

    static {
        comparators.put(InventoryPanel.SortType.NAME, compareByName);
        comparators.put(InventoryPanel.SortType.WEIGHT, compareByWeight);
        comparators.put(InventoryPanel.SortType.VOLUME, compareByVolume);
        comparators.put(InventoryPanel.SortType.PRICE, compareByPrice);
    }

    @JsonIgnore
    public ItemInfo getInfo() {
        return Editor.getItems().get(getTypeId());
    }

    public Long getPrice() {
        return price != null ? price : getInfo().getPrice();
    }

    public String getName() {
        return name != null ? name : getInfo().getName();
    }

    public List<EffectParams> getEffects() {
        return effects != null ? effects : getInfo().getEffects();
    }

    public static BigDecimal getFormatedItemValue(Long value) {
        return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }
}
