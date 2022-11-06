package model.entity.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.ItemsController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesStyleEnum;
import model.entity.DirectionEnum;
import model.entity.map.Items;
import view.Game;
import view.inventory.InventoryPanel;
import view.inventory.PlayerIndicatorsPanel;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static game.GameParams.tileSize;

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

    @JsonProperty("knowledgeInfo")
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
        image.setVisible(false);
        direction = DirectionEnum.RIGHT;

        for (BodyPartEnum partEnum : BodyPartEnum.values()) {
            wearingItems.add(new Pair<>(partEnum, null));
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
     * Перегружен ли персонаж
     */
    public Boolean isOverloaded() {
        return currentWeight.compareTo(maxWeight) > 0;
    }

    /*
     * Добавить в инвентарь персонажа стартовые предметы
     */
    public void setPlayerStartItems(Player player) {
        ItemsController.addItem(new Items(1, 2), player.getInventory(), player);
        ItemsController.addItem(new Items(3, 1), player.getInventory(), player);
        ItemsController.addItem(new Items(11, 10), player.getInventory(), player);
        ItemsController.addItem(new Items(10, 1), player.getInventory(), player);

        var items = new Items(23, 1);
        ItemsController.addItem(items, player.getInventory(), player);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items.getTypeId(), player.getInventory()),
                player);

        var items2 = new Items(25, 1);
        ItemsController.addItem(items2, player.getInventory(), player);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items2.getTypeId(), player.getInventory()),
                player);

        var items3 = new Items(26, 1);
        ItemsController.addItem(items3, player.getInventory(), player);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items3.getTypeId(), player.getInventory()),
                player);

        var items4 = new Items(20, 1);
        ItemsController.addItem(items4, player.getInventory(), player);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items4.getTypeId(), player.getInventory()),
                player);

        var items5 = new Items(21, 1);
        ItemsController.addItem(items5, player.getInventory(), player);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items5.getTypeId(), player.getInventory()),
                player);

        var items6 = new Items(22, 1);
        ItemsController.addItem(items6, player.getInventory(), player);
        ItemsController.equipItem(
                ItemsController.findItemInInventory(items6.getTypeId(), player.getInventory()),
                player);

        var items7 = new Items(30, 1);
        items7.setCurrentStrength(0);
        ItemsController.addItem(items7, player.getInventory(), player);

        PlayerIndicatorsPanel.setClothesStyle(player.getWearingItems());

        maxVolume = ItemsController.getMaximumVolume(this);
        currentVolume = ItemsController.getCurrVolume(this.inventory);
        maxWeight = ItemsController.getMaximumWeight(this);
        currentWeight = ItemsController.getCurrWeight(this.inventory);

        InventoryPanel.setWeightText();
        InventoryPanel.setVolumeText();
    }
}
