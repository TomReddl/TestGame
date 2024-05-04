package model.entity.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.ItemsController;
import controller.utils.generation.CharacterGenerator;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import model.editor.CharacterInfo;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesStyleEnum;
import model.entity.DirectionEnum;
import model.entity.dialogs.Dialog;
import model.entity.effects.EffectParams;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import view.Editor;
import view.Game;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static game.GameParams.viewSize;

/**
 * Информация о персонаже
 */
@Getter
@Setter
public class Character implements Serializable {
    @JsonIgnore
    private ImageView image;
    @JsonProperty("xPosition")
    private int xPosition; // координаты персонажа на карте
    @JsonProperty("yPosition")
    private int yPosition;
    @JsonProperty("xViewPos")
    private int xViewPos; // положение персонажа в отрисованной области
    @JsonProperty("yViewPos")
    private int yViewPos;
    @JsonProperty("direction")
    private DirectionEnum direction; // направление движения персонажа
    @JsonProperty("name")
    private String name; // имя
    @JsonProperty("gender")
    private GenderEnum gender; // пол
    @JsonProperty("hairColor")
    private HairColorEnum hairColor; // цвет волос
    @JsonProperty("hairLength")
    private int hairLength; // длина волос
    @JsonProperty("beardLength")
    private int beardLength; // длина бороды
    @JsonProperty("npcTypeId")
    private int characterTypeId; // идентификатор данных о персонаже из списка characterList
    @JsonProperty("xPos")
    private int xPos;
    @JsonProperty("yPos")
    private int yPos;
    @JsonProperty("id")
    private String id; // идентификатор данного конкретного персонажа
    @JsonProperty("isAlive")
    private boolean isAlive;

    @JsonProperty("params")
    private ParamsInfo params = new ParamsInfo(); // параметры персонажа

    @JsonProperty("knowledgeInfo")
    private KnowledgeInfo knowledgeInfo = new KnowledgeInfo(); // Известная персонажу информация

    @JsonProperty("inventory")
    private List<Items> inventory = new ArrayList<>(); // предметы в инвентаре персонажа
    private List<Map<BodyPartEnum, Items>> wearingItems = new ArrayList<>(); // надетые предметы

    @JsonProperty("appliedEffects")
    private List<EffectParams> appliedEffects; // примененные эффекты

    private List<Integer> knowledgeRecipes = new ArrayList<>(); // известные персонажу рецепты крафта

    private Dialog dialog; // диалог с персонажем

    @JsonProperty("cellCoefficient")
    private BigDecimal cellCoefficient; // коэффициент цен при продаже предметов
    @JsonProperty("buyCoefficient")
    private BigDecimal buyCoefficient; // коэффициент цен при покупке предметов

    @JsonIgnore
    private boolean[][] visiblyPoints = new boolean[viewSize][viewSize]; // точки на карте, которые персонаж видит в данный момент

    @JsonIgnore
    @Getter
    private static int baseVolume = 40000;
    @JsonIgnore
    @Getter
    private static int baseWeight = 50; // Базовый переносимый вес (кг)
    private BigDecimal maxVolume;
    private BigDecimal currentVolume;
    private BigDecimal maxWeight;
    private BigDecimal currentWeight;
    private ClothesStyleEnum style;
    private MapCellInfo interactMapPoint; // Точка на карте, с которой взаимодействует персонаж

    public Character() {
        image = new ImageView("/graphics/characters/32.png");
        image.setVisible(false);
        for (BodyPartEnum partEnum : BodyPartEnum.values()) {
            Map<BodyPartEnum, Items> map = new HashMap();
            map.put(partEnum, null);
            wearingItems.add(map);
        }
    }

    public Character(int characterTypeId, int xPosition, int yPosition, int xPos, int yPos) {
        this.characterTypeId = characterTypeId;
        this.id = UUID.randomUUID().toString();
        this.xPos = xPos;
        this.yPos = yPos;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.isAlive = true;
        image = new ImageView("/graphics/characters/32.png");
        image.setVisible(false);
        direction = DirectionEnum.RIGHT;
        hairLength = 1;
        beardLength = 0;
        hairColor = HairColorEnum.Brown;
        gender = GenderEnum.MALE;
        this.name = CharacterGenerator.generateName(characterTypeId);
        cellCoefficient = BigDecimal.valueOf(1.4);
        buyCoefficient = BigDecimal.valueOf(0.6);

        for (BodyPartEnum partEnum : BodyPartEnum.values()) {
            Map<BodyPartEnum, Items> map = new HashMap();
            map.put(partEnum, null);
            wearingItems.add(map);
        }

        Map<String, String> items = Editor.getCharacters().get(characterTypeId).getItems();
        if (items != null) {
            for (String key : items.keySet()) {
                Items addedItem = new Items(Integer.parseInt(key), Integer.parseInt(Editor.getCharacters().get(characterTypeId).getItems().get(key)));
                ItemsController.addItemToCharacter(addedItem, addedItem.getCount(), this);
            }
        }

        appliedEffects = new ArrayList<>();
    }

    /**
     * Получить существо, с которым взаимодействует персонаж
     * @return
     */
    @JsonIgnore
    public Creature getInteractCreature() {
        if (interactMapPoint != null && interactMapPoint.getCreatureId() != null) {
            return Game.getMap().getCreaturesList().get(interactMapPoint.getCreatureId());
        }
        return null;
    }

    @JsonIgnore
    public CharacterInfo getInfo() {
        return Editor.getCharacters().get(characterTypeId);
    }

    @JsonIgnore
    public int getXMapPos() {
        return Game.getMap().getPlayersSquad().getXMapPos();
    }

    @JsonIgnore
    public int getYMapPos() {
        return Game.getMap().getPlayersSquad().getYMapPos();
    }

    @JsonIgnore
    public void setXMapPos(int xMapPos) {
        Game.getMap().getPlayersSquad().setXMapPos(xMapPos);
    }

    @JsonIgnore
    public void setYMapPos(int yMapPos) {
        Game.getMap().getPlayersSquad().setYMapPos(yMapPos);
    }
}
