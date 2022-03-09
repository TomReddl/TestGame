package model.entity.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import view.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int typeId; // идентификатор данных о предмете в списке ItemsList
    @JsonProperty("count")
    private int count; // количество предметов в стеке

    public Items(int typeId, int id) {
        this.typeId = typeId;
        this.id = id;
        this.count = 1;
    }

    public static Comparator<Items> compareByName = (o1, o2) -> {
        var itemInfo1 = Game.getEditor().getItems().get(o1.getTypeId());
        var itemInfo2 = Game.getEditor().getItems().get(o2.getTypeId());
        return itemInfo1.getName().compareTo(itemInfo2.getName());
    };
}
