package controller;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import lombok.Getter;
import model.editor.TileTypeEnum;
import model.editor.items.*;
import model.entity.ItemTypeEnum;
import model.entity.battle.DamageTypeEnum;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import model.entity.player.Parameter;
import model.entity.player.ParamsInfo;
import model.entity.player.Character;
import view.*;
import view.inventory.*;
import view.menu.GameMenuPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Действия с предметами
 */
public class ItemsController {
    private static final Random random = new Random();
    public static final int engineeringWateringCan = 110; // id инженерной лейки
    public static final int lighterId = 112; // id защигалки
    public static final int bottleOfWaterId = 117; // id бутылки с водой
    public static final int biomassId = 120; // идентификатор биомассы
    public static final int inlayerBlankId = 257; // идентификатор болванки инкрустата
    public static final int mutantIngredientId = 121; // идентификатор мутантного ингредиента
    public static final int woodId = 5; // идентификатор обычной древесины
    public static final int stoneId = 171; // идентификатор камня

    private static final int defaultTimeToCraft = 20; // дефолтное время крафта

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
     * @param character       - персонаж
     */
    public static void damageItem(Items item, int damagePoints, List<Items> inventory, Character character) {
        item.setCurrentStrength(item.getCurrentStrength() - damagePoints);
        if (item.getCurrentStrength() <= 0) {
            if (item.getInfo().getFragile()) {
                deleteItem(item, 1, inventory, character);
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
     * Найти предмет в инвентаре, аналогичный переданному
     *
     * @param item - предмет, подобный которому ищем
     * @param inventory  - инвентарь, в котором ищется предмет
     * @return найденный предмет, или null
     */
    public static Items findItemInInventory(Items item, List<Items> inventory) {
        if (item != null) {
            for (Items i : inventory) {
                if (i.getTypeId() == item.getTypeId() &&
                        i.getName().equals(item.getName()) &&
                i.getParams() == item.getParams() &&
                i.getEffects() == item.getEffects() &&
                i.getPrice().equals(item.getPrice()) &&
                i.getCurrentStrength() == item.getCurrentStrength()) {
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
     * @param character    - персонаж
     * @return добавленный предмет, если удалось добавить, null, если не удалось добавить
     */
    public static Items addItem(Items item, List<Items> inventory, Character character) {
        return addItem(item, item.getCount(), inventory, character);
    }

    /**
     * Добавить указанное количество предметов в инвентарь
     * ВАЖНО: метод создает новый объект items, а не добавляет в инвентарь переданный ему объект!
     *
     * @param item      - добавляемый предмет
     * @param count     - количество добавляемых предметов
     * @param inventory - инвентарь, в который добавляем
     * @param character    - персонаж
     * @return добавленный предмет, если удалось добавить, null, если не удалось добавить
     */
    public static Items addItem(Items item, int count, List<Items> inventory, Character character) {
        if (count == 0) {
            return null;
        }
        item = new Items(item, count);
        if (!canAddItem(item, inventory, character)) {
            return null;
        }
        boolean found = false;
        Items i = findItemInInventory(item, inventory);
        if (i != null && item.getInfo().getStackable()) {
            i.setCount(i.getCount() + item.getCount());
            found = true;
        }

        if (!found) {
            inventory.add(item);
        }
        if (character != null) {
            character.setCurrentVolume(getCurrVolume(inventory));
            character.setCurrentWeight(getCurrWeight(inventory));
        }

        InventoryPanel.setWeightText();
        Game.getInventory().setVolumeText();

        InventoryPanel inventoryPanel = character != null ? Game.getInventory() : Game.getContainerInventory();
        inventoryPanel.filterInventoryTabs(inventoryPanel.getTabPane().getSelectionModel().getSelectedItem());

        if (character != null && character.isActiveCharacter()) {
            Game.showMessage(String.format(Game.getGameText("ITEM_ADDED"), item.getName(), count), Color.GREEN);
        }

        return item;
    }

    /**
     * Добавить предмет в инвентарь NPC. Если предмет можно экипировать, то он сразу экипируется
     * @param item      - добавляемый предмет
     * @param count     - количество
     * @param character - неигровой персонаж, в инвентарь которого добавляем предмет
     */
    public static void addItemToCharacter(Items item, int count, Character character) {
        Items addedItem = addItem(item, count, character.getInventory(), character);
        equipItem(addedItem, character);
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
     * Переложить весь мусор из инвентаря персонажа в мусорку (кнопка "Складировать весь мусор")
     */
    public static void storeTrash() {
        List<Items> playerInventory = Game.getMap().getSelecterCharacter().getInventory().stream().filter(item -> item.getInfo().getTypes().contains(ItemTypeEnum.TRASH)).collect(Collectors.toList());
        while (playerInventory.size() > 0) {
            Items item = playerInventory.get(0);
            addItemsToContainerFromPlayer(item, item.getCount(), Game.getContainerInventory().getItems());
            playerInventory.remove(item);
        }
    }

    /**
     * Разделать тушу существа
     */
    public static void butcheringCreature() {
        Items itemInRightHand = Game.getMap().getSelecterCharacter().getWearingItems().get(BodyPartEnum.RIGHT_ARM.ordinal()).getValue();
        if (itemInRightHand != null && ((WeaponInfo) itemInRightHand.getInfo()).getDamageType().equals(DamageTypeEnum.CUTTING_DAMAGE.name())) {
            Creature creature = Game.getMap().getSelecterCharacter().getInteractCreature();
            if (creature != null && !creature.isAlive() && creature.getInfo().getOrgans() != null) {
                TimeController.tic(60);
                ItemsController.damageItem(itemInRightHand, 10, Game.getMap().getSelecterCharacter().getInventory(), Game.getMap().getSelecterCharacter());
                creature.setButchering(true);
                Game.getContainerInventory().getButcherButton().setVisible(false);
                for (String key : creature.getInfo().getOrgans().keySet()) {
                    addItem(new Items(Integer.parseInt(key), getButcheringItemsCount(creature.getInfo().getOrgans().get(key))), creature.getInventory(), Game.getMap().getSelecterCharacter());
                }
                Game.getContainerInventory().refreshInventory();
                MapController.drawCurrentMap();
            }
        } else {
            Game.showMessage(Game.getText("NOT_BUTCHERING"));
        }
    }

    /**
     * Получить случайно число органов при разделке из строки
     *
     * @param countStr строка с количеством органов. Может содержать интервал вида "1-5"
     * @return количество органов
     */
    private static int getButcheringItemsCount(String countStr) {
        if (countStr.contains("-")) {
            String[] parts = countStr.split("-");
            int min = Integer.parseInt(parts[0]);
            int max = Integer.parseInt(parts[1]);
            return (int) (Math.random() * (max - min + 1)) + min;
        }
        return Integer.parseInt(countStr);
    }

    /**
     * Удалить item из инвентаря
     * если count = 0, удалить все предметы
     *
     * @param item      - предмет, который необходимо удалить
     * @param count     - количество удаляемых предметов
     * @param inventory - инвентарь, из которого удаляем
     * @param character    - персонаж
     * @return true, если предмет удален
     */
    public static boolean deleteItem(Items item, int count, List<Items> inventory, Character character) {
        if (item != null) {
            for (Items i : inventory) {
                if (i.getTypeId() == item.getTypeId()) {
                    if (item.getInfo().getStackable() && count > 0) {
                        int itemCount = i.getCount() - count;
                        if (itemCount > 0) {
                            i.setCount(itemCount);
                        } else {
                            if (i.isEquipment()) {
                                ItemsController.equipItem(i, character);
                            }
                            i.setCount(0);
                            inventory.remove(i);
                        }
                    } else {
                        if (i.isEquipment()) {
                            ItemsController.equipItem(i, character);
                        }
                        inventory.remove(i);
                    }
                    if (character != null) {
                        character.setCurrentVolume(getCurrVolume(inventory));
                        character.setCurrentWeight(getCurrWeight(inventory));
                    }
                    InventoryPanel.setWeightText();
                    Game.getInventory().setVolumeText();
                    InventoryPanel inventoryPanel = character != null ? Game.getInventory() : Game.getContainerInventory();
                    inventoryPanel.filterInventoryTabs(inventoryPanel.getTabPane().getSelectionModel().getSelectedItem());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Выкинуть выбранный предмет из инвентаря на карту, или выбрать количество предметов для выбрасывания
     *
     * @param character - персонаж, из чьего инвентаря выбрасывается предмет
     */
    public static void dropSelectItems(Character character) {
        if (!ItemCountPanel.getPane().isVisible()) {
            var selectedItem = ItemDetailPanel.getSelectItem();
            if (GameMenuPanel.getPane().isVisible() && character.getInventory().contains(selectedItem)) {
                if (selectedItem.getCount() == 1 || Game.isShiftPressed()) {
                    dropItems(character, selectedItem.getCount());
                } else {
                    ItemCountPanel.show(ItemActionType.DROP, selectedItem);
                }
            }
        }
    }

    /**
     * Выкинуть указанное число предметов из инвентаря на карту
     *
     * @param character - персонаж, из чьего инвентаря выбрасывается предмет
     * @param count  - количество предметов, которые нужно выкинуть
     */
    public static void dropItems(Character character, int count) {
        var selectedItem = ItemDetailPanel.getSelectItem();
        if (selectedItem.isEquipment()) {
            ItemsController.equipItem(selectedItem, character);
        }

        var x = character.getXPosition();
        var y = character.getYPosition();
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
     * @param character - персонаж
     * @return true, если в инвентаре персонажа достаточно объема, чтобы добавить новый предмет
     */
    private static Boolean canAddItem(Items item, List<Items> inventory, Character character) {
        return character == null || (getCurrVolume(inventory).
                add(BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP)))
                .compareTo(getMaximumVolume(character)) < 1;
    }

    /**
     * Хватит ли объема в инвентаре, чтобы экипировать/снять предмет
     *
     * @param item     - предмет
     * @param bodyPart - часть тела, на которую экипируется предмет
     * @param character   - персонаж
     * @return true, если в инвентаре персонажа достаточно объема, чтобы экипировать/снять предмет
     */
    private static Boolean canEquipItem(Items item, Pair<BodyPartEnum, Items> bodyPart, Character character) {
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

        return (getCurrVolume(character.getInventory())
                .add(itemVolume.negate())
                .add(equipItemVolume)
                .compareTo(getMaximumVolume(character)
                        .add(itemAddVolume)
                        .add(equipItemAddVolume.negate())) < 1);
    }

    /**
     * Клик на предмет item
     *
     * @param item   - предмет
     * @param character - персонаж
     */
    public static void clickItem(Items item, Character character) {
        if (!ItemCountPanel.getPane().isVisible()) {
            if (Game.getContainerInventory().getTabPane().isVisible()) {
                shiftItem(item);
            } else {
                useItem(item, character);
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
        var player = Game.getMap().getSelecterCharacter();
        List<Items> inventory = player.getInventory();
        if (addItem(item, count, inventory, Game.getMap().getSelecterCharacter()) != null) {
            if (Game.getContainerInventory().getInventoryType().equals(InventoryPanel.InventoryTypeEnum.CHARACTER)) {
                if (item.isEquipment()) {
                    equipItem(item, Game.getMap().getCharacterList().get(Game.getContainerInventory().getCharacterId()));
                    int x = Game.getContainerInventory().getX();
                    int y = Game.getContainerInventory().getY();
                    MapController.drawTile(player, x, y);
                }
            }
            deleteItem(item, count, containerInventory, null);
            if (item.isEquipment()) {
                item.setEquipment(!item.isEquipment());
            }
            if (!Game.getContainerInventory().getInventoryType().equals(InventoryPanel.InventoryTypeEnum.CHARACTER)) {
                // если остался 1 или 0 предметов, перерисовываем тайл карты. Также перерисовываем всегда для манекена
                MapCellInfo mapCellInfo = player.getInteractMapPoint();
                String tileType = mapCellInfo.getTile2Info().getType();
                if (containerInventory.size() <= 1 || (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.MANNEQUIN))) {
                    int x = Game.getContainerInventory().getX();
                    int y = Game.getContainerInventory().getY();
                    if (containerInventory.size() == 0) {
                        Game.getMap().getTiles()[player.getXMapPos() + x][player.getYMapPos() + y].setItems(null);
                    }
                    MapController.drawTile(player, x, y);
                }
                if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CONTAINER)) {
                    Game.getContainerInventory().setVolumeText();
                }
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
        Character character = Game.getMap().getSelecterCharacter();
        List<Items> inventory = character.getInventory();
        MapCellInfo mapCellInfo = character.getInteractMapPoint();
        String tileType = null;
        if (mapCellInfo != null) {
            tileType = mapCellInfo.getTile2Info().getType();
        }
        boolean canAdd = true;
        if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.MANNEQUIN)) {
            count = 1; // на манекен можно повесить лишь 1 экземпляр каждого вида одежды
            if (item.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES)) {
                if (((ClothesInfo) item.getInfo()).getGender().equals(mapCellInfo.getTile2Info().getParams().get("gender")) ||
                        ((ClothesInfo) item.getInfo()).getGender().equals(ClothesGenderEnum.UNISEX.name())) {
                    for (Items i : containerInventory) {
                        if (i.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES) && ((ClothesInfo) i.getInfo()).getBodyPart().equals(((ClothesInfo) item.getInfo()).getBodyPart())) {
                            canAdd = false;
                            break;
                        }
                    }
                } else {
                    canAdd = false;
                    Game.showMessage(Game.getText("WRONG_GENDER"));
                }
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.WEAPON)) {
                for (Items i : containerInventory) {
                    if (i.getInfo().getTypes().contains(ItemTypeEnum.WEAPON)) {
                        canAdd = false;
                        break;
                    }
                }
            } else {
                canAdd = false;
            }
        }
        if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CONTAINER)) {
            int totalVolume = 0;
            for (Items i : containerInventory) {
                totalVolume += i.getInfo().getVolume() * i.getCount();
            }
            if (totalVolume + item.getInfo().getVolume() * count > Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("volume"))) {
                canAdd = false;
                Game.showMessage(Game.getText("NOT_ENOUGH_VOLUME"));
            }
        }
        if (canAdd && addItem(item, count, containerInventory, null) != null) {
            deleteItem(item, count, inventory, character);
        }
        if (canAdd && tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.MANNEQUIN)) {
            MapController.drawTile(character, mapCellInfo.getX() - character.getXMapPos(), mapCellInfo.getY() - character.getYMapPos()); // для манекена всегда перерисовываем тайл
        }
        if (canAdd && tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.CONTAINER)) {
            Game.getContainerInventory().setVolumeText();
        }
    }

    /**
     * Применить предмет item
     *
     * @param item   - применяемый предмет
     * @param character - персонаж
     */
    private static void useItem(Items item, Character character) {
        if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_FOR_POTION_CRAFT)) { // выбор предмета для панели алхимического стола
            AlchemyPanel alchemyPanel = Game.getEditor().getAlchemyPanel();
            List<Items> selectedIngredients = alchemyPanel.getSelectedIngredients();
            if (item.getTypeId() == mutantIngredientId && selectedIngredients.stream().anyMatch(i -> i.getTypeId() == mutantIngredientId)) {
                Game.showMessage(Game.getText("MAX_ONE_MUTANT"));
            } else {
                if (item.getInfo().getTypes().contains(ItemTypeEnum.INGREDIENT) && !selectedIngredients.contains(item)) {
                    alchemyPanel.getSelectedIngredients().set(alchemyPanel.getIndex(), item);
                    alchemyPanel.getSelectedIngredientsCount().get(alchemyPanel.getIndex()).setText(String.valueOf(item.getCount()));
                    alchemyPanel.getIngredientImages().get(alchemyPanel.getIndex()).setImage(item.getInfo().getIcon().getImage());
                    Game.getGameMenu().showGameMenuPanel("0");
                } else if (bottleOfWaterId == item.getTypeId()) {
                    alchemyPanel.setSelectedBottle(item);
                    alchemyPanel.getSelectedBottlesCount().setText(String.valueOf(item.getCount()));
                    alchemyPanel.getBottleImage().setImage(item.getInfo().getIcon().getImage());
                    Game.getGameMenu().showGameMenuPanel("0");
                }
                Game.getEditor().getAlchemyPanel().setCookButtonDisabled();
                Game.getEditor().getAlchemyPanel().setEffectsText();
                Game.getEditor().getAlchemyPanel().setCookAllButtonVisibly();
            }
        } else if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_FOR_POTION_EXPLORE)) {
            AlchemyLaboratoryPanel laboratoryPanel = Game.getEditor().getAlchemyLaboratoryPanel();
            Game.getGameMenu().showGameMenuPanel("0");
            laboratoryPanel.setSelectedIngredient(item);
            laboratoryPanel.getExploreButton().setDisable(false);
            laboratoryPanel.getIngredientImage().setImage(item.getInfo().getIcon().getImage());
        } else if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_ITEM_FOR_ENCHANT)) {
            EnchantmentPanel enchantmentPanel = Game.getEditor().getEnchantmentPanel();
            Game.getGameMenu().showGameMenuPanel("0");
            enchantmentPanel.setItem(item);
            enchantmentPanel.getItemImage().setImage(item.getInfo().getIcon().getImage());
            enchantmentPanel.setEnchantButtonDisabled();
        } else if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_INLAYER_FOR_ENCHANT)) {
            EnchantmentPanel enchantmentPanel = Game.getEditor().getEnchantmentPanel();
            Game.getGameMenu().showGameMenuPanel("0");
            enchantmentPanel.setInlayer(item);
            enchantmentPanel.getInlayerImage().setImage(item.getInfo().getIcon().getImage());
            enchantmentPanel.setEnchantButtonDisabled();
        } else if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_INLAYER_FOR_DUPLICATOR)) {
            InlayerDuplicatorPanel inlayerDuplicatorPanel = Game.getEditor().getInlayerDuplicatorPanel();
            Game.getGameMenu().showGameMenuPanel("0");
            inlayerDuplicatorPanel.setSelectedInlayer(item);
            inlayerDuplicatorPanel.getInlayerImage().setImage(item.getInfo().getIcon().getImage());
            inlayerDuplicatorPanel.setDuplicateButtonDisabled();
        } else if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_FOR_COMBINER)) {
            if (item.getTypeId() != mutantIngredientId) { // мутантный ингредиент нельзя комбинировать
                CombinerPanel combinerPanel = Game.getEditor().getCombinerPanel();
                Game.getGameMenu().showGameMenuPanel("0");
                if (combinerPanel.getIndex() == 1) {
                    combinerPanel.setFirstIngredient(item);
                    combinerPanel.getFirstIngredientImage().setImage(item.getInfo().getIcon().getImage());
                } else if (combinerPanel.getIndex() == 2) {
                    combinerPanel.setSecondIngredient(item);
                    combinerPanel.getSecondIngredientImage().setImage(item.getInfo().getIcon().getImage());
                }
                combinerPanel.setCombineChance();
                combinerPanel.setCombineButtonDisabled();
            } else {
                Game.showMessage(Game.getText("CANT_COMBINE_MUTANT"));
            }
        } else if (Game.getInventory().getShowMode().equals(InventoryPanel.ShowModeEnum.SELECT_FOR_DUPLICATOR)) {
            if (item.getTypeId() != mutantIngredientId) { // мутантный ингредиент нельзя копировать
                DuplicatorPanel duplicatorPanel = Game.getEditor().getDuplicatorPanel();
                Game.getGameMenu().showGameMenuPanel("0");
                duplicatorPanel.setSelectedIngredient(item);
                duplicatorPanel.getIngredientImage().setImage(item.getInfo().getIcon().getImage());
                duplicatorPanel.setDuplicateButtonDisabled();
                duplicatorPanel.getIngredientCountLabel().setText(String.valueOf(item.getCount()));
            } else {
                Game.showMessage(Game.getText("CANT_COPY_MUTANT"));
            }
        } else {
            if (item.getInfo().getTypes().contains(ItemTypeEnum.EAT) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.POTION) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.INGREDIENT)) {
                eatItem(item, character);
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.WEAPON) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.TOOL) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.BOTTLE) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.EXPLOSIVES) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.SEED)) {
                equipItem(item, character);
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.BOOK)) {
                BookPanel.showBookPanel(item.getTypeId());
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.CLOCK)) {
                Game.getTimeLabel().setText(TimeController.getCurrentDataStr(true));
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.FOLDABLE)) {
                setFoldableItem(item);
            }
        }
    }

    /**
     * Разложить складываемый предмет
     *
     * @param items - предмет
     */
    private static void setFoldableItem(Items items) {
        Character character = Game.getMap().getSelecterCharacter();
        MapCellInfo mapCellInfo = Game.getMap().getTiles()[character.getXPosition()][character.getYPosition()];
        if (mapCellInfo.getTile2Id() == 0) {
            mapCellInfo.setTile2Id(Integer.parseInt(items.getInfo().getParams().get("tileId"))); // устанавливаем предмет, если клетка не занята
            ItemsController.deleteItem(items, 1, character.getInventory(), character);
            MapController.drawPlayerTile();
            TimeController.tic(10);
        }
    }

    /**
     * Cъесть предмет
     *
     * @param item   - предмет, который персонаж собирается съесть
     * @param character - персонаж
     */
    public static void eatItem(Items item, Character character) {
        var inventory = character.getInventory();
        ParamsInfo params = character.getParams();
        if ((((EdibleInfo) item.getInfo()).getHunger().compareTo(params.getIndicators().get(2).getMaxValue() - params.getIndicators().get(2).getCurrentValue()) < 0) &&
                (((EdibleInfo) item.getInfo()).getThirst().compareTo(params.getIndicators().get(3).getMaxValue() - params.getIndicators().get(3).getCurrentValue()) < 0)) {

            PlayerIndicatorsPanel.setIndicatorValue(2, params.getIndicators().get(2).getCurrentValue() + ((EdibleInfo) item.getInfo()).getHunger());
            PlayerIndicatorsPanel.setIndicatorValue(3, params.getIndicators().get(3).getCurrentValue() + ((EdibleInfo) item.getInfo()).getThirst());

            if (item.getInfo().getTypes().contains(ItemTypeEnum.POTION)) {
                EffectsController.applyEffects(item, character);
            }

            deleteItem(item, 1, inventory, character);

            //если после съедения предмета что-то остается, добавляем это в инвентарь
            if (((EdibleInfo) item.getInfo()).getRemainder() != null) {
                addItem(new Items(((EdibleInfo) item.getInfo()).getRemainder(), 1), inventory, character);
            }
        } else {
            Game.showMessage(Game.getText("ERROR_CANT_EAT_ITEM"));
        }
    }

    /**
     * Экипирует/снимает предмет
     *
     * @param wearingItem - экипируемый предмет
     * @param character      - персонаж
     * @return true, если удалось экипировать/снять предмет
     */
    public static boolean equipItem(Items wearingItem, Character character) {
        List<Pair<BodyPartEnum, Items>> wearingItems = character.getWearingItems();
        if (wearingItem.getCurrentStrength() > 0) {
            if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES)) {
                var index = BodyPartEnum.valueOf(((ClothesInfo) wearingItem.getInfo()).getBodyPart()).ordinal();
                var bodyPart = wearingItems.get(index);
                if (canEquipItem(wearingItem, bodyPart, character)) {
                    wearingItem.setEquipment(!wearingItem.isEquipment());
                    if (wearingItem.isEquipment()) {
                        EffectsController.applyEffects(wearingItem, character);
                    } else {
                        EffectsController.removeEffects(wearingItem, character);
                    }

                    if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                        wearingItems.set(index, new Pair<>(bodyPart.getKey(), null));
                    } else {
                        if (bodyPart.getValue() != null) {
                            bodyPart.getValue().setEquipment(false);
                            EffectsController.removeEffects(bodyPart.getValue(), character);
                        }
                        wearingItems.set(index, new Pair<>(bodyPart.getKey(), wearingItem));
                    }
                } else {
                    if (character.isActiveCharacter()) {
                        Game.showMessage(Game.getText("ERROR_CANT_REMOVE_ITEM"));
                    }
                    return false;
                }
            } else if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.WEAPON)) {
                var weapon = (WeaponInfo) wearingItem.getInfo();
                wearingItem.setEquipment(!wearingItem.isEquipment());
                if (wearingItem.isEquipment()) {
                    EffectsController.applyEffects(wearingItem, character);
                } else {
                    EffectsController.removeEffects(wearingItem, character);
                }

                var bodyPart = wearingItems.get(BodyPartEnum.RIGHT_ARM.ordinal());
                if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                    wearingItems.set(BodyPartEnum.RIGHT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                } else {
                    if (bodyPart.getValue() != null) {
                        bodyPart.getValue().setEquipment(false);
                        EffectsController.removeEffects(bodyPart.getValue(), character);
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
                            EffectsController.removeEffects(bodyPart.getValue(), character);
                        }
                        if (!weapon.getOneHand()) {
                            wearingItems.set(BodyPartEnum.LEFT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), wearingItem));
                        } else {
                            wearingItems.set(BodyPartEnum.LEFT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                        }
                    }
                }
            } else if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.TOOL) ||
                    wearingItem.getInfo().getTypes().contains(ItemTypeEnum.BOTTLE) ||
                    wearingItem.getInfo().getTypes().contains(ItemTypeEnum.SEED) ||
                    wearingItem.getInfo().getTypes().contains(ItemTypeEnum.EXPLOSIVES)) {
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
            MapController.drawPlayerTile();

            character.setCurrentVolume(getCurrVolume(character.getInventory()));
            character.setCurrentWeight(getCurrWeight(character.getInventory()));

            Game.getInventory().setVolumeText();

            return true;
        } else {
            if (character.isActiveCharacter()) {
                Game.showMessage(Game.getText("ERROR_CANT_EQUIP_BROKEN_ITEM"));
            }
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
     * @param character - персонаж
     * @return максимальная масса, которую может переносить персонаж
     */
    public static BigDecimal getMaximumWeight(Character character) {
        return BigDecimal.valueOf(character.getParams().getCharacteristics().get(3).getCurrentValue() * 3 + Character.getBaseWeight());
    }

    /**
     * Получить заполненный объем в инвентаре
     *
     * @param inventory - инвентарь
     * @return заполненный объем инвентаря
     */
    public static BigDecimal getCurrVolume(List<Items> inventory) {
        if (inventory == null) {
            return BigDecimal.ZERO;
        }
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
     * @param character - персонаж
     * @return максимальный переносимый персонажем объем предметов
     */
    public static BigDecimal getMaximumVolume(Character character) {
        var belt = character.getWearingItems().get(BodyPartEnum.BELT.ordinal()).getValue();
        var backpack = character.getWearingItems().get(BodyPartEnum.BACKPACK.ordinal()).getValue();
        var maxVol = Character.getBaseVolume() +
                (belt != null ? Integer.parseInt(belt.getInfo().getParams().get("addVolume")) : 0) +
                (backpack != null ? Integer.parseInt(backpack.getInfo().getParams().get("addVolume")) : 0);
        return BigDecimal.valueOf(maxVol).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    /**
     * Получить качество скрафченного предмета исходя из уровня навыка
     *
     * @param param - навык, на основании которого создается предмет
     * @return
     */
    public static QualityGradationEnum getQuality(Parameter param) {
        int currentValue = param.getCurrentValue();
        int result = random.nextInt(currentValue) + 10;
        if (result > 100) {
            return QualityGradationEnum.LEGENDARY;
        } else if (result > 80) {
            return QualityGradationEnum.EXPERT;
        } else if (result > 60) {
            return QualityGradationEnum.QUALITY;
        } else if (result > 40) {
            return QualityGradationEnum.NORMAL;
        } else if (result > 20) {
            return QualityGradationEnum.CHEAP;
        }
        return QualityGradationEnum.UNSUCCESSFUL;
    }

    /**
     * Создать предмет
     *
     * @param recipeInfo- создаваемый предмет
     * @param character - персонаж, который создает предмет
     */
    public static void craftItem(RecipeInfo recipeInfo, Character character) {
        List<Items> craftElements = getCraftElements(recipeInfo, character);
        if (craftElements == null) {
            Game.showMessage(Game.getGameText("NO_ITEMS_TO_CRAFT"));
        } else {
            for (Items element : craftElements) {
                ItemsController.deleteItem(element,
                        recipeInfo.getElements().get(String.valueOf(element.getTypeId())),
                        character.getInventory(),
                        character);
            }
            ItemsController.addItem(new Items(recipeInfo.getItemId(), recipeInfo.getItemCount() != null ? recipeInfo.getItemCount() : 1), character.getInventory(), character);
            Integer exp = recipeInfo.getExp();
            CharactersController.addSkillExp(recipeInfo.getSkillId(), exp != null ? exp : 10);
            if (Game.getMap().getSelecterCharacter().equals(character)) {
                Integer timeToCraft = recipeInfo.getTimeToCraft();
                TimeController.tic(timeToCraft != null ? timeToCraft : defaultTimeToCraft);
                Game.showMessage(String.format(Game.getGameText("ITEM_CREATED"), Editor.getItems().get(recipeInfo.getItemId()).getName()),
                        Color.GREEN);
            }
            Game.getEditor().getCraftPanel().showPanel(Game.getMap().getSelecterCharacter().getInteractMapPoint().getTile2Info()); // перерисовываем панель крафта на случай, если изменился список доступных рецептов
        }
    }

    /**
     * Получить список ингредиентов, необходимых для крафта
     *
     * @param recipeInfo - рецепт
     * @param character     - персонаж, в инвентаре которого ищем предметы
     * @return список ингредиентов, или null, если не все ингредиенты есть в инвентаре персонажа
     */
    public static List<Items> getCraftElements(RecipeInfo recipeInfo, Character character) {
        List<Items> craftElements = new ArrayList<>();
        for (String key : recipeInfo.getElements().keySet()) {
            var element = ItemsController.findItemInInventory(Integer.parseInt(key), character.getInventory());
            if (element == null) {
                return null;
            } else {
                craftElements.add(element);
            }
        }
        return craftElements;
    }
}
