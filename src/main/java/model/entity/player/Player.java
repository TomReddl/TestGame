package model.entity.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesInfo;
import model.editor.items.WeaponInfo;
import model.entity.DirectionEnum;
import model.entity.ItemTypeEnum;
import model.entity.map.Items;
import view.Game;
import view.inventory.InventoryPanel;
import view.inventory.ItemDetailPanel;
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

    @JsonProperty("legacy")
    private List<Parameter> legacies = new ArrayList<>(); // наследия персонажа
    @JsonProperty("characteristics")
    private List<Parameter> characteristics = new ArrayList<>(); // характеристики персонажа
    @JsonProperty("skills")
    private List<Parameter> skills = new ArrayList<>(); // навыки персонажа

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

    public Player() {
        image = new ImageView("/graphics/characters/32.png");
        image.setVisible(Boolean.FALSE);
        direction = DirectionEnum.RIGHT;

        Items items = new Items();
        items.setTypeId(1);
        items.setCount(3);
        inventory.add(items);

        Items items2 = new Items();
        items2.setTypeId(3);
        items2.setCount(1);
        inventory.add(items2);

        Items items3 = new Items();
        items3.setTypeId(11);
        items3.setCount(10);
        inventory.add(items3);

        for (int i = 0; i < 6; i++) {
            Parameter legacy = new Parameter();
            legacy.setCurrentValue(16);
            legacy.setRealValue(16);
            legacies.add(legacy);

            Parameter characteristic = new Parameter();
            characteristic.setCurrentValue(50);
            characteristic.setRealValue(50);
            characteristics.add(characteristic);
        }

        for (int i = 0; i < 24; i++) {
            Parameter skill = new Parameter();
            skill.setCurrentValue(10);
            skill.setRealValue(10);
            skills.add(skill);
        }

        for (BodyPartEnum partEnum : BodyPartEnum.values()) {
            wearingItems.add(new Pair<>(partEnum, null));
        }

        maxVolume = getMaximumVolume();
        currentVolume = getCurrVolume(this.inventory);
        maxWeight = getMaximumWeight();
        currentWeight = getCurrWeight();
    }

    private BigDecimal getCurrWeight() {
        int totalWeight = 0;
        for (Items item : this.getInventory()) {
            totalWeight += item.getInfo().getWeight() * item.getCount();
        }
        return BigDecimal.valueOf(totalWeight).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal getMaximumWeight() {
        return BigDecimal.valueOf(this.getCharacteristics().get(3).getCurrentValue() * 3);
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
        currentWeight = getCurrWeight();

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();

        Game.getInventory().filterInventoryTabs(Game.getInventory().getTabPane().getSelectionModel().getSelectedItem());

        return Boolean.TRUE;
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
     * экипирует wearingItem на персонажа
     */
    public boolean equipItem(Items wearingItem) {
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

        Game.getInventory().drawItems(Boolean.FALSE,
                Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());

        drawPlayerImage(Game.getMap().getPlayer());

        currentVolume = getCurrVolume(this.inventory);
        maxVolume = getMaximumVolume();

        InventoryPanel.setVolumeText();

        return Boolean.TRUE;
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
        if (GameMenuPanel.getPane().isVisible()) {
            ItemDetailPanel.getSelectItem().setEquipment(Boolean.FALSE);

            Game.getMap().addItemOnMap(Game.getMap().getPlayer().getXPosition(), Game.getMap().getPlayer().getYPosition(), ItemDetailPanel.getSelectItem());

            Game.getMap().getPlayer().getInventory().remove(ItemDetailPanel.getSelectItem());

            Game.getInventory().drawItems(Boolean.FALSE,
                    Game.getInventory().getTabPane().getSelectionModel().getSelectedItem().getText());

            currentVolume = getCurrVolume(this.inventory);
            currentWeight = getCurrWeight();

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
}
