package model.entity.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.editor.items.ItemInfo;
import view.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

/*
* * Информация о предмете
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

    public Items(int typeId, int id) {
        this.typeId = typeId;
        this.id = id;
        this.count = 1;
    }

    public static Comparator<Items> compareByName = Comparator.comparing(o -> o.getInfo().getName());

    public ItemInfo getInfo() {
        return Game.getEditor().getItems().get(getTypeId());
    }

    public static BigDecimal getFormatedItemValue(Long value) {
        return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }
}
