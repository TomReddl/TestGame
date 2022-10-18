package controller;

import javafx.util.Pair;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesInfo;
import model.editor.items.EdibleInfo;
import model.editor.items.WeaponInfo;
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import model.entity.player.ParamsInfo;
import model.entity.player.Player;
import view.Game;
import view.inventory.BookPanel;
import view.inventory.InventoryPanel;
import view.inventory.ItemDetailPanel;
import view.inventory.PlayerIndicatorsPanel;
import view.menu.GameMenuPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/*
 * Действия с предметами
 */
public class ItemsController {

    /**
     * Повредить предмет. Если предмет хрупкий, то он уничтожается при 0 прочности
     *
     * @param item         - повреждаемый предмет
     * @param damagePoints - сила повреждения
     */
    public static void damageItem(Items item, int damagePoints, List<Items> inventory, Player player) {
        item.setCurrentStrength(item.getCurrentStrength() - damagePoints);
        if (item.getCurrentStrength() <= 0) {
            if (item.getInfo().getFragile()) {
                deleteItem(item, 1, inventory, player);
                Game.showMessage(String.format(Game.getText("ITEM_BROKEN"), item.getInfo().getName()));
            } else {
                item.setCurrentStrength(0);
                Game.showMessage(String.format(Game.getText("ITEM_DESTROYED"), item.getInfo().getName()));
            }
        }
    }

    // найти предмет в инвентаре по itemTypeId
    public static Items findItemInInventory(Integer itemId, List<Items> inventory) {
        if (itemId != null) {
            for (Items i : inventory) {
                if (i.getTypeId() == itemId) {
                    return i;
                }
            }
        }
        return null;
    }

    // найти лучшие инструменты для взлома в инвентаре
    public static Items findPicklockInInventory(List<Items> inventory) {
        Items pickLock = null;
        for (Items i : inventory) {
            if (i.getInfo().getTypes().contains(ItemTypeEnum.PICKLOCK)) {
                if (pickLock == null) {
                    pickLock = i;
                } else {
                    pickLock = Integer.parseInt(i.getInfo().getParams().get(0)) > Integer.parseInt(pickLock.getInfo().getParams().get(0)) ? i : pickLock;
                }
            }
        }
        return pickLock;
    }

    // найти лучшие сапёрные инструменты в инвентаре
    public static Items findSapperToolsInInventory(List<Items> inventory) {
        Items sapperTool = null;
        for (Items i : inventory) {
            if (i.getInfo().getTypes().contains(ItemTypeEnum.SAPPER_TOOL)) {
                if (sapperTool == null) {
                    sapperTool = i;
                } else {
                    sapperTool = Integer.parseInt(i.getInfo().getParams().get(0)) > Integer.parseInt(sapperTool.getInfo().getParams().get(0)) ? i : sapperTool;
                }
            }
        }
        return sapperTool;
    }

    /*
     * Добавить предмет в инвентарь
     */
    public static Boolean addItem(Items item, List<Items> inventory, Player player) {
        if (!canAddItem(item, inventory, player)) {
            return false;
        }
        boolean found = false;
        Items i = findItemInInventory(item.getTypeId(), inventory);
        if (i != null && item.getInfo().getStackable()) {
            i.setCount(i.getCount() + item.getCount());
            found = true;
        }

        if (!found) {
            inventory.add(item);
        }
        if (player != null) {
            player.setCurrentVolume(getCurrVolume(inventory));
            player.setCurrentWeight(getCurrWeight(inventory));
        }

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();

        InventoryPanel inventoryPanel = player != null ? Game.getInventory() : Game.getContainerInventory();
        inventoryPanel.filterInventoryTabs(inventoryPanel.getTabPane().getSelectionModel().getSelectedItem());

        return true;
    }

    /*
     * Добавить все предметы из инвентаря контейнера в инвентарь персонажа (кнопка "Взять всё")
     */
    public static void takeAllItems() {
        List<Items> inventory = Game.getMap().getPlayer().getInventory();
        List<Items> containerInventory = Game.getContainerInventory().getItems();
        while (containerInventory.size() > 0) {
            Items item = containerInventory.get(0);
            if (addItem(item, inventory, Game.getMap().getPlayer())) {
                deleteItem(item, item.getCount(), containerInventory, null);
            } else {
                break;
            }
        }
    }

    /*
     * удалить item из инвентаря персонажа.
     * если count = 0, удалить все предметы
     */
    public static Boolean deleteItem(Items item, int count, List<Items> inventory, Player player) {
        boolean found = false;
        for (Items i : inventory) {
            if (item.getTypeId() == i.getTypeId()) {
                if (item.getInfo().getStackable() && count > 0) {
                    i.setCount(i.getCount() - count);
                    if (i.getCount() <= 0) {
                        inventory.remove(i);
                    }
                } else {
                    inventory.remove(i);
                }
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        if (player != null) {
            player.setCurrentVolume(getCurrVolume(inventory));
            player.setCurrentWeight(getCurrWeight(inventory));
        }

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();

        InventoryPanel inventoryPanel = player != null ? Game.getInventory() : Game.getContainerInventory();
        inventoryPanel.filterInventoryTabs(inventoryPanel.getTabPane().getSelectionModel().getSelectedItem());

        return true;
    }

    /*
     * Выкинуть выбранный предмет из инвентаря на карту
     */
    public static void dropSelectItems(Player player) {
        var selectedItem = ItemDetailPanel.getSelectItem();
        if (GameMenuPanel.getPane().isVisible() && player.getInventory().contains(selectedItem)) {
            if (selectedItem.isEquipment()) {
                ItemsController.equipItem(selectedItem, player);
            }

            Game.getMap().addItemOnMap(Game.getMap().getPlayer().getXPosition(), Game.getMap().getPlayer().getYPosition(), selectedItem);

            Game.getMap().getPlayer().getInventory().remove(selectedItem);

            Game.getInventory().drawItems(InventoryPanel.SortType.NAME, false,
                    Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());

            player.setCurrentVolume(getCurrVolume(player.getInventory()));
            player.setCurrentWeight(getCurrWeight(player.getInventory()));

            InventoryPanel.setWeightText();
            InventoryPanel.setVolumeText();

            Game.getInventory().filterInventoryTabs(Game.getInventory().getTabPane().getSelectionModel().getSelectedItem());
        }
    }

    /*
     * Хватит ли объема в инвентаре, чтобы добавить новый предмет
     */
    private static Boolean canAddItem(Items item, List<Items> inventory, Player player) {
        return player == null || (getCurrVolume(inventory).
                add(BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP)))
                .compareTo(getMaximumVolume(player)) < 1;
    }

    /*
     * Хватит ли объема в инвентаре, чтобы экипировать/снять предмет
     */
    private static Boolean canEquipItem(Items item, Pair<BodyPartEnum, Items> bodyPart, List<Items> inventory, Player player) {
        var itemVolume = BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
        var itemAddVolume = BigDecimal.ZERO;

        var equipItemVolume = BigDecimal.ZERO;
        var equipItemAddVolume = BigDecimal.ZERO;

        if (((ClothesInfo) item.getInfo()).getBodyPart().equals(BodyPartEnum.BACKPACK.name()) ||
                ((ClothesInfo) item.getInfo()).getBodyPart().equals(BodyPartEnum.BELT.name())) {
            itemAddVolume = BigDecimal.valueOf(Long.parseLong(item.getInfo().getParams().get(0))).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
        }
        if (bodyPart.getValue() != null) {
            equipItemVolume = BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
            if (bodyPart.getKey().equals(BodyPartEnum.BACKPACK) ||
                    bodyPart.getKey().equals(BodyPartEnum.BELT)) {
                equipItemAddVolume = BigDecimal.valueOf(Long.parseLong(bodyPart.getValue().getInfo().getParams().get(0))).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
            }
        }

        return (getCurrVolume(inventory)
                .add(itemVolume.negate())
                .add(equipItemVolume)
                .compareTo(getMaximumVolume(player)
                        .add(itemAddVolume)
                        .add(equipItemAddVolume.negate())) < 1);
    }

    /*
     * клик на предмет item
     */
    public static void clickItem(Items item, Player player) {
        if (Game.getContainerInventory().getTabPane().isVisible()) {
            shiftItem(item);
        } else {
            useItem(item, player);
        }
    }

    //переместить предмет
    private static void shiftItem(Items item) {
        var player = Game.getMap().getPlayer();
        List<Items> inventory = player.getInventory();
        List<Items> containerInventory = Game.getContainerInventory().getItems();
        if (item.getCount() == 1) {
            if (containerInventory.contains(item)) {
                if (addItem(item, inventory, Game.getMap().getPlayer())) {
                    deleteItem(item, 1, containerInventory, null);
                    // если остался 1 или 0 предметов, перерисовываем тайл карты
                    if (containerInventory.size() <= 1) {
                        int x = Game.getContainerInventory().getX();
                        int y = Game.getContainerInventory().getY();
                        if (containerInventory.size() == 0) {
                            Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y].setItems(null);
                        }
                        Game.getMap().drawTile(player, x, y);
                    }
                }
            } else {
                if (addItem(item, containerInventory, null)) {
                    deleteItem(item, 1, inventory, Game.getMap().getPlayer());
                }
            }
        } else {

        }
    }

    /*
     * применить предмет item
     */
    private static void useItem(Items item, Player player) {
        List<Items> inventory = player.getInventory();
        if (item.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES) ||
                item.getInfo().getTypes().contains(ItemTypeEnum.WEAPON)) {
            equipItem(item, player);
        } else if (item.getInfo().getTypes().contains(ItemTypeEnum.EAT) ||
                item.getInfo().getTypes().contains(ItemTypeEnum.POTION) ||
                item.getInfo().getTypes().contains(ItemTypeEnum.INGREDIENT)) {
            eatItem(item, inventory, player);
        } else if (item.getInfo().getTypes().contains(ItemTypeEnum.BOOK)) {
            BookPanel.showBookPanel(item.getTypeId());
        }
    }

    /*
     * съесть item
     */
    public static void eatItem(Items item, List<Items> inventory, Player player) {
        ParamsInfo params = player.getParams();
        if ((((EdibleInfo) item.getInfo()).getHunger().compareTo(params.getIndicators().get(2).getMaxValue() - params.getIndicators().get(2).getCurrentValue()) < 0) &&
                (((EdibleInfo) item.getInfo()).getThirst().compareTo(params.getIndicators().get(3).getMaxValue() - params.getIndicators().get(3).getCurrentValue()) < 0)) {

            PlayerIndicatorsPanel.setIndicatorValue(2, params.getIndicators().get(2).getCurrentValue() + ((EdibleInfo) item.getInfo()).getHunger());
            PlayerIndicatorsPanel.setIndicatorValue(3, params.getIndicators().get(3).getCurrentValue() + ((EdibleInfo) item.getInfo()).getThirst());

            deleteItem(item, 1, inventory, player);

            //если после съедения предмета что-то остается, добавляем это в инвентарь
            if (((EdibleInfo) item.getInfo()).getRemainder() != null) {
                addItem(new Items(((EdibleInfo) item.getInfo()).getRemainder(), 1), inventory, player);
            }
        } else {
            Game.showMessage(Game.getText("ERROR_CANT_EAT_ITEM"));
        }
    }

    /*
     * экипирует/снимает wearingItem
     */
    public static boolean equipItem(Items wearingItem, Player player) {
        List<Pair<BodyPartEnum, Items>> wearingItems = player.getWearingItems();
        List<Items> inventory = player.getInventory();
        if (wearingItem.getCurrentStrength() > 0) {
            if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES)) {
                var index = BodyPartEnum.valueOf(((ClothesInfo) wearingItem.getInfo()).getBodyPart()).ordinal();
                var bodyPart = wearingItems.get(index);
                if (canEquipItem(wearingItem, bodyPart, inventory, player)) {
                    wearingItem.setEquipment(!wearingItem.isEquipment());

                    if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                        wearingItems.set(index, new Pair<>(bodyPart.getKey(), null));
                    } else {
                        if (bodyPart.getValue() != null) {
                            bodyPart.getValue().setEquipment(false);
                        }
                        wearingItems.set(index, new Pair<>(bodyPart.getKey(), wearingItem));
                    }
                } else {
                    Game.showMessage(Game.getText("ERROR_CANT_REMOVE_ITEM"));
                    return false;
                }
            } else if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.WEAPON)) {
                var weapon = (WeaponInfo) wearingItem.getInfo();
                wearingItem.setEquipment(!wearingItem.isEquipment());
                var bodyPart = wearingItems.get(BodyPartEnum.RIGHT_ARM.ordinal());
                if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                    wearingItems.set(BodyPartEnum.RIGHT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                } else {
                    if (bodyPart.getValue() != null) {
                        bodyPart.getValue().setEquipment(false);
                    }
                    wearingItems.set(BodyPartEnum.RIGHT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), wearingItem));
                }

                bodyPart = wearingItems.get(BodyPartEnum.LEFT_ARM.ordinal());
                if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                    wearingItems.set(BodyPartEnum.LEFT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                } else {
                    if (bodyPart.getValue() != null) {
                        if (!((WeaponInfo) bodyPart.getValue().getInfo()).getOneHand()) {
                            bodyPart.getValue().setEquipment(false);
                        }
                        if (!weapon.getOneHand()) {
                            wearingItems.set(BodyPartEnum.LEFT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), wearingItem));
                        } else {
                            wearingItems.set(BodyPartEnum.LEFT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                        }
                    }
                }
            }

            if (GameMenuPanel.getPane().isVisible()) {
                Game.getInventory().drawItems(InventoryPanel.SortType.NAME, false,
                        Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());
            }

            // перерисовываем весь тайл с персонажем, чтобы не оставалось артефактов от снятых предметов одежды
            Game.getMap().drawTile(player, player.getXViewPos(), player.getYViewPos());

            player.setCurrentVolume(getCurrVolume(player.getInventory()));
            player.setCurrentWeight(getCurrWeight(player.getInventory()));

            InventoryPanel.setVolumeText();

            return true;
        } else {
            Game.showMessage(Game.getText("ERROR_CANT_EQUIP_BROKEN_ITEM"));
            return false;
        }
    }

    public static BigDecimal getCurrWeight(List<Items> inventory) {
        int totalWeight = 0;
        for (Items item : inventory) {
            totalWeight += item.getInfo().getWeight() * item.getCount();
        }
        return BigDecimal.valueOf(totalWeight).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal getMaximumWeight(Player player) {
        return BigDecimal.valueOf(player.getParams().getCharacteristics().get(3).getCurrentValue() * 3);
    }

    public static BigDecimal getCurrVolume(List<Items> inventory) {
        int totalVolume = 0;
        for (Items item : inventory) {
            if (!item.isEquipment()) { // объем надетых вещей не учитывается при подсчете свободного места в инвентаре
                totalVolume += item.getInfo().getVolume() * item.getCount();
            }
        }
        return BigDecimal.valueOf(totalVolume).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    /*
     * Получить максимальный объем переносимых персонажем вещей. Рюкзак и пояс увеличивают макс. объем
     */
    public static BigDecimal getMaximumVolume(Player player) {
        var belt = player.getWearingItems().get(BodyPartEnum.BELT.ordinal()).getValue();
        var backpack = player.getWearingItems().get(BodyPartEnum.BACKPACK.ordinal()).getValue();
        var maxVol = Player.getBaseVolume() +
                (belt != null ? Integer.parseInt(belt.getInfo().getParams().get(0)) : 0) +
                (backpack != null ? Integer.parseInt(backpack.getInfo().getParams().get(0)) : 0);
        return BigDecimal.valueOf(maxVol).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }
}
