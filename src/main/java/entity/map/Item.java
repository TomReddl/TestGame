package entity.map;

import lombok.Getter;
import lombok.Setter;

/*
* * Информация о предмете
*/
@Getter
@Setter
public class Item {
    private int id; // идентификатор данного конкретного предмета
    private int typeId; // идентификатор данных о предмете в списке ItemsList
    private int count; // количество предметов в стеке

    public Item(int typeId, int id) {
        this.typeId = typeId;
        this.id = id;
    }
}
