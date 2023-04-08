package controller;

import javafx.util.Pair;
import lombok.Getter;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesInfo;
import model.editor.items.EdibleInfo;
import model.editor.items.WeaponInfo;
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import model.entity.player.ParamsInfo;
import model.entity.player.Player;
import view.AlchemyLaboratoryPanel;
import view.AlchemyPanel;
import view.Game;
import view.inventory.*;
import view.menu.GameMenuPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Действия с предметами
 */
public class ItemsController {

    // Виды действий с предметами
    public enum ItemActionType {
        TO_PLAYER("Переместить предметы в инвентарь игрока"),
        TO_CONTAINER("Переместить предметы в контейнер"),
        DROP("Выбросить предметы изх инвентаря персонажа");

        @Getter
        private final String desc;

        ItemActionType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * Повредить предмет. Если предмет хрупкий, то он уничтожается при 0 прочности
     *
     * @param item         - повреждаемый предмет
     * @param damagePoints - сила повреждения
     * @param inventory    - инвентарь, в котором лежит предмет
     * @param player       - персонаж
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

    /**
     * Найти предмет в инвентаре по itemTypeId
     *
     * @param itemTypeId - идентификатор типа предмета
     * @param inventory  - инвентарь, в котором ищется предмет
     * @return найденный предмет, или null
     */
    public static Items findItemInInventory(Integer itemTypeId, List<Items> inventory) {
        if (itemTypeId != null) {
            for (Items i : inventory) {
                if (i.getTypeId() == itemTypeId) {
                    return i;
                }
            }
        }
        return null;
    }

    /**
     * Найти лучшие инструменты для взлома в инвентаре
     *
     * @param inventory - инвентарь, в котором ищется предмет
     * @return найденный предмет, или null
     */
    public static Items findPicklockInInventory(List<Items> inventory) {
        Items pickLock = null;
        for (Items i : inventory) {
            if (i.getInfo().getTypes().contains(ItemTypeEnum.PICKLOCK)) {
                if (pickLock == null) {
                    pickLock = i;
                } else {
                    pickLock = Integer.parseInt(i.getInfo().getParams().get("skillBonus")) > Integer.parseInt(pickLock.getInfo().getParams().get("skillBonus")) ? i : pickLock;
                }
            }
        }
        return pickLock;
    }

    /**
     * Найти лучшие сапёрные инструменты в инвентаре
     *
     * @param inventory - инвентарь, в котором ищется предмет
     * @return - найденный предмет, или null
     */
    public static Items findSapperToolsInInventory(List<Items> inventory) {
        Items sapperTool = null;
        for (Items i : inventory) {
            if (i.getInfo().getTypes().contains(ItemTypeEnum.SAPPER_TOOL)) {
                if (sapperTool == null) {
                    sapperTool = i;
                } else {
                    sapperTool = Integer.parseInt(i.getInfo().getParams().get("skillBonus")) > Integer.parseInt(sapperTool.getInfo().getParams().get("skillBonus")) ? i : sapperTool;
                }
            }
        }
        return sapperTool;
    }

    /**
     * Добавить предмет в инвентарь
     * ВАЖНО: метод создает новый объект items, а не добавляет в инвентарь переданный ему объект!
     *
     * @param item      - добавляемый предмет
     * @param inventory - инвентарь, в который добавляем
     * @param player    - персонаж
     * @return добавленный предмет, если удалось добавить, null, если не удалось добавить
     */
    public static Items addItem(Items item, List<Items> inventory, Player player) {
        return addItem(item, item.getCount(), inventory, player);
    }

    /**
     * Добавить указанное количество предметов в инвентарь
     * ВАЖНО: метод создает новый объект items, а не добавляет в инвентарь переданный ему объект!
     *
     * @param item      - добавляемый предмет
     * @param count     - количество добавляемых предметов
     * @param inventory - инвентарь, в который добавляем
     * @param player    - персонаж
     * @return добавленный предмет, если удалось добавить, null, если не удалось добавить
     */
    public static Items addItem(Items item, int count, List<Items> inventory, Player player) {
        item = new Items(item, count);
        if (!canAddItem(item, inventory, player)) {
            return null;
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

        return item;
    }

    /**
     * Добавить все предметы из инвентаря контейнера в инвентарь персонажа (кнопка "Взять всё")
     */
    public static void takeAllItems() {
        List<Items> containerInventory = Game.getContainerInventory().getItems();
        while (containerInventory.size() > 0) {
            Items item = containerInventory.get(0);
            if (!addItemsToPlayerFromContainer(item, item.getCount(), containerInventory)) {
                break;
            }
        }
    }

    /**
     * Удалить item из инвентаря
     * если count = 0, удалить все предметы
     *
     * @param item      - предмет, который необходимо удалить
     * @param count     - количество удаляемых предметов
     * @param inventory - инвентарь, из которого удаляем
     * @param player    - персонаж
     * @return true, если предмет удален
     */
    public static boolean deleteItem(Items item, int count, List<Items> inventory, Player player) {
        for (Items i : inventory) {
            if (i.getTypeId() == item.getTypeId()) {
                if (item.getInfo().getStackable() && count > 0) {
                    int itemCount = i.getCount() - count;
                    if (itemCount > 0) {
                        i.setCount(itemCount);
                    } else {
                        if (i.isEquipment()) {
                            ItemsController.equipItem(i, player);
                        }
                        inventory.remove(i);
                    }
                } else {
                    if (i.isEquipment()) {
                        ItemsController.equipItem(i, player);
                    }
                    inventory.remove(i);
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
        }
        return false;
    }

    /**
     * Выкинуть выбранный предмет из инвентаря на карту, или выбрать количество предметов для выбрасывания
     *
     * @param player - персонаж, из чьего инвентаря выбрасывается предмет
     */
    public static void dropSelectItems(Player player) {
        if (!ItemCountPanel.getPane().isVisible()) {
            var selectedItem = ItemDetailPanel.getSelectItem();
            if (GameMenuPanel.getPane().isVisible() && player.getInventory().contains(selectedItem)) {
                if (selectedItem.getCount() == 1 || Game.isShiftPressed()) {
                    dropItems(player, selectedItem.getCount());
                } else {
                    ItemCountPanel.show(ItemActionType.DROP, selectedItem);
                }
            }
        }
    }

    /**
     * Выкинуть указанное число предметов из инвентаря на карту
     *
     * @param player - персонаж, из чьего инвентаря выбрасывается предмет
     * @param count  - количество предметов, которые нужно выкинуть
     */
    public static void dropItems(Player player, int count) {
        var selectedItem = ItemDetailPanel.getSelectItem();
        if (selectedItem.isEquipment()) {
            ItemsController.equipItem(selectedItem, player);
        }

        var x = player.getXPosition();
        var y = player.getYPosition();
        List<Items> containerInventory;
        if (Game.getMap().getTiles()[x][y].getItems() == null) {
            containerInventory = new ArrayList<>();
            Game.getMap().getTiles()[x][y].setItems(containerInventory);
        } else {
            containerInventory = Game.getMap().getTiles()[x][y].getItems();
        }

        addItemsToContainerFromPlayer(selectedItem, count, containerInventory);
    }

    /**
     * Хватит ли объема в инвентаре, чтобы добавить новый предмет
     *
     * @param item      - предмет, для которого проводится проверка
     * @param inventory - инвентарь, для которого проводится проверка
     * @param player    - персонаж
     * @return true, если в инвентаре персонажа достаточно объема, чтобы добавить новый предмет
     */
    private static Boolean canAddItem(Items item, List<Items> inventory, Player player) {
        return player == null || (getCurrVolume(inventory).
                add(BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP)))
                .compareTo(getMaximumVolume(player)) < 1;
    }

    /**
     * Хватит ли объема в инвентаре, чтобы экипировать/снять предмет
     *
     * @param item     - предмет
     * @param bodyPart - часть тела, на которую экипируется предмет
     * @param player   - персонаж
     * @return true, если в инвентаре персонажа достаточно объема, чтобы экипировать/снять предмет
     */
    private static Boolean canEquipItem(Items item, Pair<BodyPartEnum, Items> bodyPart, Player player) {
        var itemVolume = BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
        var itemAddVolume = BigDecimal.ZERO;

        var equipItemVolume = BigDecimal.ZERO;
        var equipItemAddVolume = BigDecimal.ZERO;

        if (((ClothesInfo) item.getInfo()).getBodyPart().equals(BodyPartEnum.BACKPACK.name()) ||
                ((ClothesInfo) item.getInfo()).getBodyPart().equals(BodyPartEnum.BELT.name())) {
            itemAddVolume = BigDecimal.valueOf(Long.parseLong(item.getInfo().getParams().get("addVolume"))).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
        }
        if (bodyPart.getValue() != null) {
            equipItemVolume = BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
            if (bodyPart.getKey().equals(BodyPartEnum.BACKPACK) ||
                    bodyPart.getKey().equals(BodyPartEnum.BELT)) {
                equipItemAddVolume = BigDecimal.valueOf(Long.parseLong(bodyPart.getValue().getInfo().getParams().get("addVolume"))).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
            }
        }

        return (getCurrVolume(player.getInventory())
                .add(itemVolume.negate())
                .add(equipItemVolume)
                .compareTo(getMaximumVolume(player)
                        .add(itemAddVolume)
                        .add(equipItemAddVolume.negate())) < 1);
    }

    /**
     * Клик на предмет item
     *
     * @param item   - предмет
     * @param player - персонаж
     */
    public static void clickItem(Items item, Player player) {
        if (!ItemCountPanel.getPane().isVisible()) {
            if (Game.getContainerInventory().getTabPane().isVisible()) {
                shiftItem(item);
            } else {
                useItem(item, player);
            }
        }
    }

    /**
     * Переместить предмет из инвентаря персонажа в контейнер, или обратно
     *
     * @param item - предмет
     */
    private static void shiftItem(Items item) {
        List<Items> containerInventory = Game.getContainerInventory().getItems();
        if (item.getCount() == 1 || Game.isShiftPressed()) {
            if (containerInventory.contains(item)) {
                addItemsToPlayerFromContainer(item, item.getCount(), containerInventory);
            } else {
                addItemsToContainerFromPlayer(item, item.getCount(), containerInventory);
            }
        } else {
            ItemActionType action = containerInventory.contains(item) ? ItemActionType.TO_PLAYER : ItemActionType.TO_CONTAINER;
            ItemCountPanel.show(action, item);
        }
    }

    /**
     * Переместить предметы из контейнера в инвентарь персонажа
     *
     * @param item               - предмет, который нужно переместить
     * @param count              - количество перемещаемых предметов
     * @param containerInventory - инвентарь, из которого перемещаются предметы
     * @return true, если удалось переместить предметы
     */
    public static boolean addItemsToPlayerFromContainer(Items item, int count, List<Items> containerInventory) {
        var player = Game.getMap().getPlayer();
        List<Items> inventory = player.getInventory();
        if (addItem(item, count, inventory, Game.getMap().getPlayer()) != null) {
            deleteItem(item, count, containerInventory, null);
            // если остался 1 или 0 предметов, перерисовываем тайл карты
            if (containerInventory.size() <= 1) {
                int x = Game.getContainerInventory().getX();
                int y = Game.getContainerInventory().getY();
                if (containerInventory.size() == 0) {
                    Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y].setItems(null);
                }
                MapController.drawTile(player, x, y);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Переместить предметы из инвентаря персонажа в контейнер
     *
     * @param item               - предмет, который нужно переместить
     * @param count              - количество перемещаемых предметов
     * @param containerInventory - инвентарь, в который перемещаются предметы
     */
    public static void addItemsToContainerFromPlayer(Items item, int count, List<Items> containerInventory) {
        List<Items> inventory = Game.getMap().getPlayer().getInventory();
        if (addItem(item, count, containerInventory, null) != null) {
            deleteItem(item, count, inventory, Game.getMap().getPlayer());
        }
    }

    /**
     * Применить предмет item
     *
     * @param item   - применяемый предмет
     * @param player - персонаж
     */
    private static void useItem(Items item, Player player) {
        if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_FOR_POTION_CRAFT)) { // выбор предмета для панели алхимического стола
            AlchemyPanel alchemyPanel = Game.getEditor().getAlchemyPanel();
            Game.getGameMenu().showGameMenuPanel("0");
            if (item.getInfo().getTypes().contains(ItemTypeEnum.INGREDIENT)) {
                alchemyPanel.getSelectedIngredients().set(alchemyPanel.getIndex(), item);
                alchemyPanel.getSelectedIngredientsCount().get(alchemyPanel.getIndex()).setText(String.valueOf(item.getCount()));
                alchemyPanel.getIngredientImages().get(alchemyPanel.getIndex()).setImage(item.getInfo().getIcon().getImage());
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.BOTTLE)) {
                alchemyPanel.setSelectedBottle(item);
                alchemyPanel.getSelectedBottlesCount().setText(String.valueOf(item.getCount()));
                alchemyPanel.getBottleImage().setImage(item.getInfo().getIcon().getImage());
            }
            Game.getEditor().getAlchemyPanel().setCookButtonEnabled();
            Game.getEditor().getAlchemyPanel().setEffectsText();
        } else if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_FOR_POTION_EXPLORE)) {
            AlchemyLaboratoryPanel laboratoryPanel = Game.getEditor().getAlchemyLaboratoryPanel();
            Game.getGameMenu().showGameMenuPanel("0");
            laboratoryPanel.setSelectedIngredient(item);
            laboratoryPanel.getExploreButton().setDisable(false);
            laboratoryPanel.getIngredientImage().setImage(item.getInfo().getIcon().getImage());
        } else {
            if (item.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.WEAPON) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.TOOL) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.BOTTLE)) {
                equipItem(item, player);
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.EAT) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.POTION) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.INGREDIENT)) {
                eatItem(item, player);
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.BOOK)) {
                BookPanel.showBookPanel(item.getTypeId());
            }
        }
    }

    /**
     * Cъесть предмет
     *
     * @param item   - предмет, который персонаж собирается съесть
     * @param player - персонаж
     */
    public static void eatItem(Items item, Player player) {
        var inventory = player.getInventory();
        ParamsInfo params = player.getParams();
        if ((((EdibleInfo) item.getInfo()).getHunger().compareTo(params.getIndicators().get(2).getMaxValue() - params.getIndicators().get(2).getCurrentValue()) < 0) &&
                (((EdibleInfo) item.getInfo()).getThirst().compareTo(params.getIndicators().get(3).getMaxValue() - params.getIndicators().get(3).getCurrentValue()) < 0)) {

            PlayerIndicatorsPanel.setIndicatorValue(2, params.getIndicators().get(2).getCurrentValue() + ((EdibleInfo) item.getInfo()).getHunger());
            PlayerIndicatorsPanel.setIndicatorValue(3, params.getIndicators().get(3).getCurrentValue() + ((EdibleInfo) item.getInfo()).getThirst());

            if (item.getInfo().getTypes().contains(ItemTypeEnum.POTION)) {
                EffectController.applyEffects(item, player);
            }

            deleteItem(item, 1, inventory, player);

            //если после съедения предмета что-то остается, добавляем это в инвентарь
            if (((EdibleInfo) item.getInfo()).getRemainder() != null) {
                addItem(new Items(((EdibleInfo) item.getInfo()).getRemainder(), 1), inventory, player);
            }
        } else {
            Game.showMessage(Game.getText("ERROR_CANT_EAT_ITEM"));
        }
    }

    /**
     * Экипирует/снимает предмет
     *
     * @param wearingItem - экипируемый предмет
     * @param player      - персонаж
     * @return true, если удалось экипировать/снять предмет
     */
    public static boolean equipItem(Items wearingItem, Player player) {
        List<Pair<BodyPartEnum, Items>> wearingItems = player.getWearingItems();
        if (wearingItem.getCurrentStrength() > 0) {
            if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES)) {
                var index = BodyPartEnum.valueOf(((ClothesInfo) wearingItem.getInfo()).getBodyPart()).ordinal();
                var bodyPart = wearingItems.get(index);
                if (canEquipItem(wearingItem, bodyPart, player)) {
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
            } else if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.TOOL) ||
                    wearingItem.getInfo().getTypes().contains(ItemTypeEnum.BOTTLE)) {
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
                        wearingItems.set(BodyPartEnum.LEFT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                    }
                }
            }

            if (GameMenuPanel.getPane().isVisible()) {
                Game.getInventory().drawItems(InventoryPanel.SortType.NAME, false,
                        Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());
            }

            // перерисовываем весь тайл с персонажем, чтобы не оставалось артефактов от снятых предметов одежды
            MapController.drawTile(player, player.getXViewPos(), player.getYViewPos());

            player.setCurrentVolume(getCurrVolume(player.getInventory()));
            player.setCurrentWeight(getCurrWeight(player.getInventory()));

            InventoryPanel.setVolumeText();

            return true;
        } else {
            Game.showMessage(Game.getText("ERROR_CANT_EQUIP_BROKEN_ITEM"));
            return false;
        }
    }

    /**
     * Получить суммарный вес предметов в инвентаре
     *
     * @param inventory - инвентарь
     * @return суммарный вес всех предметов в инвентаре
     */
    public static BigDecimal getCurrWeight(List<Items> inventory) {
        int totalWeight = 0;
        for (Items item : inventory) {
            totalWeight += item.getInfo().getWeight() * item.getCount();
            if (item.getInfo().getParams() != null &&
                    item.getInfo().getParams().get("currentCapacity") != null) {
                // нужно учитывать вес содержимого предметов
                totalWeight += Integer.parseInt(item.getParams().get("currentCapacity"));
            }
        }
        return BigDecimal.valueOf(totalWeight).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    /**
     * Получить максимальный переносимый персонажем вес (в килограммах)
     *
     * @param player - персонаж
     * @return максимальная масса, которую может переносить персонаж
     */
    public static BigDecimal getMaximumWeight(Player player) {
        return  BigDecimal.valueOf(player.getParams().getCharacteristics().get(3).getCurrentValue() * 3 + Player.getBaseWeight());
    }

    /**
     * Получить заполненный объем в инвентаре
     *
     * @param inventory - инвентарь
     * @return заполненный объем инвентаря
     */
    public static BigDecimal getCurrVolume(List<Items> inventory) {
        int totalVolume = 0;
        for (Items item : inventory) {
            if (!item.isEquipment()) { // объем надетых вещей не учитывается при подсчете свободного места в инвентаре
                totalVolume += item.getInfo().getVolume() * item.getCount();
            }
        }
        return BigDecimal.valueOf(totalVolume).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    /**
     * Получить максимальный объем переносимых персонажем вещей. Рюкзак и пояс увеличивают макс. объем
     *
     * @param player - персонаж
     * @return максимальный переносимый персонажем объем предметов
     */
    public static BigDecimal getMaximumVolume(Player player) {
        var belt = player.getWearingItems().get(BodyPartEnum.BELT.ordinal()).getValue();
        var backpack = player.getWearingItems().get(BodyPartEnum.BACKPACK.ordinal()).getValue();
        var maxVol = Player.getBaseVolume() +
                (belt != null ? Integer.parseInt(belt.getInfo().getParams().get("addVolume")) : 0) +
                (backpack != null ? Integer.parseInt(backpack.getInfo().getParams().get("addVolume")) : 0);
        return BigDecimal.valueOf(maxVol).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }
}
