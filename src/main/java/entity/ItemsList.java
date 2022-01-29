package entity;

import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

import java.util.List;

/*
 * Список предметов в редакторе
 * */
@Setter
@Getter
public class ItemsList {
    private List<ItemInfo> items;
    private int itemsCount;

    public ItemsList() {
        items = JsonUtils.getItems();
        itemsCount = items.size();
    }
}
