package model.entity.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesStyleEnum;
import model.entity.DirectionEnum;
import model.entity.effects.EffectParams;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import view.Game;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация о персонаже игрока
 */
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

    @JsonProperty("params")
    @Getter
    private ParamsInfo params = new ParamsInfo(); // параметры персонажа

    @JsonProperty("knowledgeInfo")
    @Getter
    private KnowledgeInfo knowledgeInfo = new KnowledgeInfo(); // Известная персонажу информация

    @JsonProperty("inventory")
    private List<Items> inventory = new ArrayList<>(); // предметы в инвентаре персонажа
    private List<Pair<BodyPartEnum, Items>> wearingItems = new ArrayList<>(); // надетые предметы

    @JsonProperty("appliedEffects")
    private List<EffectParams> appliedEffects; // примененные эффекты

    private List<Integer> knowledgeRecipes = new ArrayList<>(); // известные персонажу рецепты крафта

    @JsonIgnore
    @Getter
    private static int baseVolume = 40000;
    @JsonIgnore
    @Getter
    private static int baseWeight = 50; // Базовый переносимый вес (кг)
    @JsonIgnore
    @Setter
    private BigDecimal maxVolume;
    @JsonIgnore
    @Setter
    private BigDecimal currentVolume;
    @JsonIgnore
    @Setter
    private BigDecimal maxWeight;
    @JsonIgnore
    @Setter
    private BigDecimal currentWeight;
    private ClothesStyleEnum style;

    @JsonIgnore
    @Getter
    @Setter
    private MapCellInfo interactMapPoint; // Точка на карте, с которой взаимодействует персонаж

    public Player() {
        image = new ImageView("/graphics/characters/32.png");
        image.setVisible(false);
        direction = DirectionEnum.RIGHT;
        hairLength = 1;
        beardLength = 0;
        hairColor = HairColorEnum.Brown;
        gender = GenderEnum.MALE;
        name = "Путник";

        for (BodyPartEnum partEnum : BodyPartEnum.values()) {
            wearingItems.add(new Pair<>(partEnum, null));
        }

        appliedEffects = new ArrayList<>();
    }

    /**
     * Получить существо, с которым взаимодействует персонаж
     * @return
     */
    public Creature getInteractCreature() {
        if (interactMapPoint != null && interactMapPoint.getCreatureId() != null) {
            return Game.getMap().getCreaturesList().get(interactMapPoint.getCreatureId());
        }
        return null;
    }
}
