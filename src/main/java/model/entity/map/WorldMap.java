package model.entity.map;

import controller.EffectsController;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.entity.effects.EffectParams;
import model.entity.player.Character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static game.GameParams.mapSize;

/**
 * Карта мира со всеми персонажами и предметами на ней
 */
@Getter
@Setter
@Slf4j
public class WorldMap implements Serializable {
    private MapCellInfo[][] tiles = new MapCellInfo[mapSize][mapSize];
    List<Character> characterList = new ArrayList<>();
    List<Creature> creaturesList = new ArrayList<>();
    private String mapName;
    private int worldPosX; // координата карты по горизонтали в мире
    private int worldPosY; // координата карты по вертикали в мире
    private Character selecterCharacter = new Character(32, 1, 0, 0, 0, 0);
    private Map<WeatherEnum, Integer> currentWeather = new HashMap<>(); // текущая погода и ее сила
    private Map<WeatherEnum, Integer> accessibleWeathers = new HashMap<>(); // доступная погода и вероятность ее наступления
    private Map<Integer, EffectParams> additionalEffect = new HashMap<>(); // дополнительные эффекты ингредиентов (генерируются случайно при старте новой игры)

    public WorldMap() {
        selecterCharacter.setActiveCharacter(true);
        accessibleWeathers.put(WeatherEnum.CLEAR, 1);
        currentWeather.put(WeatherEnum.CLEAR, 1);
        mapName = "1.1";
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                var taleInfo = new MapCellInfo();
                taleInfo.setTile1Id(0);
                taleInfo.setTile2Id(0);
                taleInfo.setX(i);
                taleInfo.setY(j);
                tiles[i][j] = taleInfo;
            }
        }
        tiles[1][1].setTile2Id(260); // плавильня для тестирования

        EffectsController.putAdditionalEffects(additionalEffect);
    }
}
