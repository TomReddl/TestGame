package controller;

import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import model.entity.player.Character;
import view.Game;

import java.math.BigDecimal;
import java.util.*;

/**
 * Действия, связанные с торговлей
 */
public class TradeController {

    private static Map<String, Set<ItemTypeEnum>> tradableItems = new HashMap<>(); // торгуемые предметы

    static {
        tradableItems.put("resources", Set.of(ItemTypeEnum.RESOURCE));
    }

    /**
     * Купить предмет
     *
     * @param buyer   - персонаж, который покупает
     * @param celler  - персонаж, который продает
     * @param item    - покупаемый предмет
     * @param count   - количество покупаемого предмета (если null, то покупают всё имеющееся количество)
     */
    public static void buyItems(Character buyer, Character celler, Items item, Integer count) {
        count = count != null ? count : item.getCount();
        var totalPrice = celler.getCellCoefficient().multiply(BigDecimal.valueOf(count)).multiply(BigDecimal.valueOf(item.getPrice())).intValue();
        var moneyCount= getMoneyCount(buyer.getInventory());
        if (moneyCount >= totalPrice) {
            ItemsController.addItemsToPlayerFromContainer(item, count, celler.getInventory());
            for (Items items : getMoneyForPay(totalPrice, buyer.getInventory())) {
                ItemsController.addItemsToContainerFromPlayer(items, items.getCount(), buyer.getInventory());
            }
        } else if (Game.getMap().getPlayersSquad().getSelectedCharacter().equals(buyer)) {
            Game.showMessage(String.format(Game.getGameText("NOT_ENOUGH_MONEY"), item.getName()));
        }
    }

    /**
     * Продать предмет
     *
     * @param celler  - персонаж, который продает
     * @param buyer   - персонаж, который покупает
     * @param item    - продаваемый предмет
     * @param count   - количество продаваемого предмета (если null, то продают всё имеющееся количество)
     */
    public static void cellItems(Character celler, Character buyer, Items item, Integer count) {
        count = count != null ? count : item.getCount();
        var totalPrice = buyer.getBuyCoefficient().multiply(BigDecimal.valueOf(count)).multiply(BigDecimal.valueOf(item.getPrice())).intValue();
        var moneyCount= getMoneyCount(buyer.getInventory());
        if (moneyCount >= totalPrice) {
            ItemsController.addItemsToContainerFromPlayer(item, count, buyer.getInventory());
            for (Items items : getMoneyForPay(totalPrice, buyer.getInventory())) {
                ItemsController.addItemsToPlayerFromContainer(items, items.getCount(), buyer.getInventory());
            }
        } else if (Game.getMap().getPlayersSquad().getSelectedCharacter().equals(celler)) {
            Game.showMessage(String.format(Game.getGameText("NOT_ENOUGH_MONEY"), item.getName()));
        }
    }

    /**
     * Доступен ли предмет для продажи
     *
     * @param items - предмет
     * @return true, если предмет должен отображаться в окне инвентаря продавца при торговле
     */
    public static boolean isItemAvailableForTrade(Items items) {
        if (items.isEquipment()) {
            return false; // экипированные предметы не продаются
        }
        var celler = Game.getMap().getCharacterList().get(Game.getContainerInventory().getCharacterId());
        if (celler != null) {
            String cellersTradableItems = celler.getInfo().getTradableItems();
            return cellersTradableItems != null && tradableItems.get(cellersTradableItems).contains(items.getInfo().getTypes().get(0));
        }
        return false;
    }

    /**
     * Получить количество денег в инвентаре
     * @param inventory - инвентарь, в котором ищем деньги
     * @return - сумма денег в шадах
     */
    public static int getMoneyCount(List<Items> inventory) {
        var moneyCount = 0;
        var money1 = ItemsController.findItemInInventory(371, inventory);
        if (money1 != null) {
            moneyCount =+ money1.getCount();
        }
        var money10 = ItemsController.findItemInInventory(372, inventory);
        if (money10 != null) {
            moneyCount =+ money10.getCount();
        }
        var money50 = ItemsController.findItemInInventory(373, inventory);
        if (money50 != null) {
            moneyCount =+ money50.getCount();
        }
        var money100 = ItemsController.findItemInInventory(374, inventory);
        if (money100 != null) {
            moneyCount =+ money100.getCount();
        }
        var money1000 = ItemsController.findItemInInventory(375, inventory);
        if (money1000 != null) {
            moneyCount =+ money1000.getCount();
        }

        return moneyCount;
    }

    /**
     * Получить деньги для оплаты покупки
     * @param price      - цена
     * @param inventory  - инвентарь, в котором ищем деньги
     * @return
     */
    public static List<Items> getMoneyForPay(int price, List<Items> inventory) {
        List<Items> result = new ArrayList<>();
        var money1000 = ItemsController.findItemInInventory(375, inventory);
        var money1000count = money1000 != null ? money1000.getCount() : 0;
        var money100 = ItemsController.findItemInInventory(374, inventory);
        var money100count = money100 != null ? money100.getCount() : 0;
        var money50 = ItemsController.findItemInInventory(373, inventory);
        var money50count = money50 != null ? money50.getCount() : 0;
        var money10 = ItemsController.findItemInInventory(372, inventory);
        var money10count = money10 != null ? money10.getCount() : 0;
        var money1 = ItemsController.findItemInInventory(371, inventory);
        var money1count = money1 != null ? money1.getCount() : 0;

        if (money1count >= price) {
            result.add(new Items(371, price));
        } else if (money10count*10 >= price) {
            result.add(new Items(372, price / 10));
        } else if (money50count*50 >= price) {
            result.add(new Items(373, price / 50));
        } else if (money100count*100 >= price) {
            result.add(new Items(374, price / 100));
        } else if (money100count*1000 >= price) {
            result.add(new Items(375, price / 1000));
        }
        return result;
    }
}
