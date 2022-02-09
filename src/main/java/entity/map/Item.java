package entity.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* * Информация о предмете
*/
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @JsonProperty("id")
    private int id; // идентификатор данного конкретного предмета
    @JsonProperty("typeId")
    private int typeId; // идентификатор данных о предмете в списке ItemsList
    @JsonProperty("count")
    private int count; // количество предметов в стеке

    public Item(int typeId, int id) {
        this.typeId = typeId;
        this.id = id;
    }
}
