package model.entity.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import model.editor.items.*;
import model.entity.DirectionEnum;
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import view.Game;
import view.inventory.BookPanel;
import view.inventory.InventoryPanel;
import view.inventory.ItemDetailPanel;
import view.inventory.PlayerStatsPanel;
import view.menu.GameMenuPanel;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static view.params.GameParams.tileSize;

@Getter
@Setter
public class Player implements Serializable {
    @JsonIgnore
    private ImageView image;
    @JsonProperty("xPosition")
    private int xPosition; // координаты персонажа на карте
    @JsonProperty("yPosition")
    private int yPosition;
    @JsonProperty("xMapPos")
    private int xMapPos; // сдвиг области отрисовки карты от начала координат
    @JsonProperty("yMapPos")
    private int yMapPos;
    @JsonProperty("xViewPos")
    private int xViewPos; // положение персонажа в отрисованной области
    @JsonProperty("yViewPos")
    private int yViewPos;
    @JsonProperty("direction")
    private DirectionEnum direction; // направление движения персонажа

    @JsonProperty("params")
    @Getter
    private ParamsInfo params = new ParamsInfo(); // параметры персонажа

    @JsonProperty("params")
    @Getter
    private KnowledgeInfo knowledgeInfo = new KnowledgeInfo(); // Известная персонажу информация

    @JsonProperty("inventory")
    private List<Items> inventory = new ArrayList<>(); // вещи в рюкзаке персонажа
    private List<Pair<BodyPartEnum, Items>> wearingItems = new ArrayList<>(); // надетые предметы

    @JsonIgnore
    @Getter
    private static int baseVolume = 40000;
    @JsonIgnore
    private BigDecimal maxVolume;
    @JsonIgnore
    private BigDecimal currentVolume;
    @JsonIgnore
    private BigDecimal maxWeight;
    @JsonIgnore
    private BigDecimal currentWeight;
    private ClothesStyleEnum style;

    public Player() {
        image = new ImageView("/graphics/characters/32.png");
        image.setVisible(Boolean.FALSE);
        direction = DirectionEnum.RIGHT;

        for (BodyPartEnum partEnum : BodyPartEnum.values()) {
            wearingItems.add(new Pair<>(partEnum, null));
        }
    }

    private BigDecimal getCurrWeight(List<Items> inventory) {
        int totalWeight = 0;
        for (Items item : inventory) {
            totalWeight += item.getInfo().getWeight() * item.getCount();
        }
        return BigDecimal.valueOf(totalWeight).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal getMaximumWeight() {
        return BigDecimal.valueOf(params.getCharacteristics().get(3).getCurrentValue() * 3);
    }

    private BigDecimal getCurrVolume(List<Items> inventory) {
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
    private BigDecimal getMaximumVolume() {
        var belt = this.getWearingItems().get(BodyPartEnum.BELT.ordinal()).getValue();
        var backpack = this.getWearingItems().get(BodyPartEnum.BACKPACK.ordinal()).getValue();
        var maxVol = baseVolume +
                (belt != null ? Integer.parseInt(belt.getInfo().getParams().get(0)) : 0) +
                (backpack != null ? Integer.parseInt(backpack.getInfo().getParams().get(0)) : 0);
        return BigDecimal.valueOf(maxVol).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    /*
     * Добавить предмет в инвентарь игроку
     */
    public Boolean addItem(Items item) {
        if (!canAddItem(item)) {
            return Boolean.FALSE;
        }
        boolean found = false;
        for (Items i : this.inventory) {
            if (item.getTypeId() == i.getTypeId() && item.getInfo().getStackable()) {
                i.setCount(i.getCount() + item.getCount());
                found = true;
                break;
            }
        }
        if (!found) {
            inventory.add(item);
        }
        currentVolume = getCurrVolume(this.inventory);
        currentWeight = getCurrWeight(this.inventory);

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();

        Game.getInventory().filterInventoryTabs(Game.getInventory().getTabPane().getSelectionModel().getSelectedItem());

        return Boolean.TRUE;
    }

    /*
     * удалить item из инвентаря персонажа.
     * если count = 0, удалить все предметы
     */
    public Boolean deleteItem(Items item, int count) {
        boolean found = false;
        for (Items i : this.inventory) {
            if (item.getTypeId() == i.getTypeId()) {
                if (item.getInfo().getStackable() && count > 0) {
                    i.setCount(i.getCount() - count);
                    if (i.getCount() <= 0) {
                        this.inventory.remove(i);
                    }
                } else {
                    this.inventory.remove(i);
                }
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        currentVolume = getCurrVolume(this.inventory);
        currentWeight = getCurrWeight(this.inventory);

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();

        Game.getInventory().filterInventoryTabs(Game.getInventory().getTabPane().getSelectionModel().getSelectedItem());

        return true;
    }

    /*
     * Хватит ли объема в инвентаре, чтобы добавить новый предмет
     */
    private Boolean canAddItem(Items item) {
        return (getCurrVolume(this.inventory).
                add(BigDecimal.valueOf(item.getInfo().getVolume()).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP)))
                .compareTo(getMaximumVolume()) < 1;
    }

    /*
     * Хватит ли объема в инвентаре, чтобы экипировать/снять предмет
     */
    private Boolean canEquipItem(Items item, Pair<BodyPartEnum, Items> bodyPart) {
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

        return (getCurrVolume(this.inventory)
                .add(itemVolume.negate())
                .add(equipItemVolume)
                .compareTo(getMaximumVolume()
                        .add(itemAddVolume)
                        .add(equipItemAddVolume.negate())) < 1);
    }

    /*
     * применить предмет item
     */
    public void useItem(Items item) {
        if (item.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES) ||
                item.getInfo().getTypes().contains(ItemTypeEnum.WEAPON)) {
            equipItem(item);
        } else if (item.getInfo().getTypes().contains(ItemTypeEnum.EAT) ||
                item.getInfo().getTypes().contains(ItemTypeEnum.POTION) ||
                item.getInfo().getTypes().contains(ItemTypeEnum.INGREDIENT)) {
            eatItem(item);
        } else if (item.getInfo().getTypes().contains(ItemTypeEnum.BOOK)) {
            BookPanel.showBookPanel(item.getTypeId());
        }
    }

    /*
     * съесть item
     */
    public void eatItem(Items item) {
        if ((((EdibleInfo) item.getInfo()).getHunger().compareTo(params.getIndicators().get(2).getMaxValue() - params.getIndicators().get(2).getCurrentValue()) < 0) &&
                (((EdibleInfo) item.getInfo()).getThirst().compareTo(params.getIndicators().get(3).getMaxValue() - params.getIndicators().get(3).getCurrentValue()) < 0)) {
            params.getIndicators().get(2).setCurrentValue(params.getIndicators().get(2).getCurrentValue() + ((EdibleInfo) item.getInfo()).getHunger());
            if (params.getIndicators().get(2).getCurrentValue() > params.getIndicators().get(2).getMaxValue()) {
                params.getIndicators().get(2).setCurrentValue(params.getIndicators().get(2).getMaxValue());
            }
            PlayerStatsPanel.getHungerValueLabel().setText(params.getIndicators().get(2).getCurrentValue()  + "/" + params.getIndicators().get(2).getMaxValue());

            params.getIndicators().get(3).setCurrentValue(params.getIndicators().get(3).getCurrentValue() + ((EdibleInfo) item.getInfo()).getThirst());
            if (params.getIndicators().get(3).getCurrentValue() > params.getIndicators().get(3).getMaxValue()) {
                params.getIndicators().get(3).setCurrentValue(params.getIndicators().get(3).getMaxValue());
            }
            PlayerStatsPanel.getThirstValueLabel().setText(params.getIndicators().get(3).getCurrentValue()  + "/" + params.getIndicators().get(3).getMaxValue());

            deleteItem(item, 1);

            //если после съедения предмета что-то остается, добавляем это в инвентарь
            if (((EdibleInfo) item.getInfo()).getRemainder() != null) {
                addItem(new Items(((EdibleInfo) item.getInfo()).getRemainder(), 1));
            }
        } else {
            Game.showMessage(Game.getText("ERROR_CANT_EAT_ITEM"));
        }
    }

    /*
     * экипирует/снимает wearingItem
     */
    public boolean equipItem(Items wearingItem) {
        if (wearingItem.getCurrentStrength() > 0) {
            if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.CLOTHES)) {
                var index = BodyPartEnum.valueOf(((ClothesInfo) wearingItem.getInfo()).getBodyPart()).ordinal();
                var bodyPart = wearingItems.get(index);
                if (canEquipItem(wearingItem, bodyPart)) {
                    wearingItem.setEquipment(!wearingItem.isEquipment());

                    if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                        wearingItems.set(index, new Pair<>(bodyPart.getKey(), null));
                    } else {
                        if (bodyPart.getValue() != null) {
                            bodyPart.getValue().setEquipment(Boolean.FALSE);
                        }
                        wearingItems.set(index, new Pair<>(bodyPart.getKey(), wearingItem));
                    }
                } else {
                    Game.showMessage(Game.getText("ERROR_CANT_REMOVE_ITEM"));
                    return Boolean.FALSE;
                }
            } else if (wearingItem.getInfo().getTypes().contains(ItemTypeEnum.WEAPON)) {
                var weapon = (WeaponInfo) wearingItem.getInfo();
                wearingItem.setEquipment(!wearingItem.isEquipment());
                var bodyPart = wearingItems.get(BodyPartEnum.RIGHT_ARM.ordinal());
                if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                    wearingItems.set(BodyPartEnum.RIGHT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                } else {
                    if (bodyPart.getValue() != null) {
                        bodyPart.getValue().setEquipment(Boolean.FALSE);
                    }
                    wearingItems.set(BodyPartEnum.RIGHT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), wearingItem));
                }

                bodyPart = wearingItems.get(BodyPartEnum.LEFT_ARM.ordinal());
                if (bodyPart.getValue() != null && bodyPart.getValue().equals(wearingItem)) {
                    wearingItems.set(BodyPartEnum.LEFT_ARM.ordinal(), new Pair<>(bodyPart.getKey(), null));
                } else {
                    if (bodyPart.getValue() != null) {
                        if (!((WeaponInfo) bodyPart.getValue().getInfo()).getOneHand()) {
                            bodyPart.getValue().setEquipment(Boolean.FALSE);
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
                Game.getInventory().drawItems(InventoryPanel.SortType.NAME, Boolean.FALSE,
                        Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());
            }

            drawPlayerImage(Game.getMap().getPlayer());

            currentVolume = getCurrVolume(this.inventory);
            maxVolume = getMaximumVolume();

            InventoryPanel.setVolumeText();

            return Boolean.TRUE;
        } else {
            Game.showMessage(Game.getText("ERROR_CANT_EQUIP_BROKEN_ITEM"));
            return Boolean.FALSE;
        }
    }

    /*
     * Рисует персонажа игрока и все экипированные на нем предметы
     */
    public static void drawPlayerImage(Player player) {
        GraphicsContext gc = Game.getEditor().getCanvas().getGraphicsContext2D();
        var img = player.getImage().getImage();
        double width = img.getWidth();
        double height = img.getHeight();

        var isLeft = player.getDirection().equals(DirectionEnum.LEFT) ||
                player.getDirection().equals(DirectionEnum.UP);
        if (isLeft) {
            gc.drawImage(img,
                    0, 0, tileSize, tileSize,
                    player.getXViewPos() * tileSize + tileSize,
                    player.getYViewPos() * tileSize,
                    -width, height);
        } else {
            gc.drawImage(img,
                    player.getXViewPos() * tileSize, player.getYViewPos() * tileSize);
        }

        for (Pair<BodyPartEnum, Items> bodyPart : player.getWearingItems()) {
            if (bodyPart.getValue() != null) {
                var path = "/graphics/items/" + bodyPart.getValue().getTypeId() + "doll.png";
                var f = new File("/" + Player.class.getProtectionDomain().getCodeSource().getLocation().getPath() + path);
                if (f.exists()) {
                    var image = new Image(path);
                    if (isLeft) {
                        gc.drawImage(image,
                                0, 0, tileSize, tileSize,
                                player.getXViewPos() * tileSize + tileSize,
                                player.getYViewPos() * tileSize,
                                -width, height);
                    } else {
                        gc.drawImage(image,
                                player.getXViewPos() * tileSize, player.getYViewPos() * tileSize);
                    }
                }
            }
        }
        /*var sp = new SnapshotParameters();
        WritableImage writableImage = new WritableImage(40, 40);
        writableImage = gc.getCanvas().snapshot(sp, writableImage);*/
    }

    /*
     * Выкинуть выбранный предмет из инвентаря на карту
     */
    public void dropSelectItems() {
        var selectedItem = ItemDetailPanel.getSelectItem();
        if (GameMenuPanel.getPane().isVisible()) {
            if (selectedItem.isEquipment()) {
                equipItem(selectedItem);
            }

            Game.getMap().addItemOnMap(Game.getMap().getPlayer().getXPosition(), Game.getMap().getPlayer().getYPosition(), selectedItem);

            Game.getMap().getPlayer().getInventory().remove(selectedItem);

            Game.getInventory().drawItems(InventoryPanel.SortType.NAME, Boolean.FALSE,
                    Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());

            currentVolume = getCurrVolume(this.inventory);
            currentWeight = getCurrWeight(this.inventory);

            InventoryPanel.setWeightText();
            InventoryPanel.setVolumeText();

            Game.getInventory().filterInventoryTabs(Game.getInventory().getTabPane().getSelectionModel().getSelectedItem());
        }
    }

    /*
     * Перегружен ли персонаж
     */
    public Boolean isOverloaded() {
        return currentWeight.compareTo(maxWeight) > 0;
    }

    /*
     * Добавить в инвентарь персонажа стартовые предметы
     */
    public void setPlayerStartItems(Player player) {
        player.addItem(new Items(1, 2));
        player.addItem(new Items(3, 1));
        player.addItem(new Items(11, 10));
        player.addItem(new Items(10, 1));

        var items = new Items(23, 1);
        player.addItem(items);
        player.equipItem(items);

        var items2 = new Items(25, 1);
        player.addItem(items2);
        player.equipItem(items2);

        var items3 = new Items(26, 1);
        player.addItem(items3);
        player.equipItem(items3);

        var items4 = new Items(20, 1);
        player.addItem(items4);
        player.equipItem(items4);

        var items5 = new Items(21, 1);
        player.addItem(items5);
        player.equipItem(items5);

        var items6 = new Items(22, 1);
        player.addItem(items6);
        player.equipItem(items6);

        var items7 = new Items(30, 1);
        items7.setCurrentStrength(0);
        player.addItem(items7);

        PlayerStatsPanel.setClothesStyle(player.getWearingItems());

        maxVolume = getMaximumVolume();
        currentVolume = getCurrVolume(this.inventory);
        maxWeight = getMaximumWeight();
        currentWeight = getCurrWeight(this.inventory);

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();
    }
}
