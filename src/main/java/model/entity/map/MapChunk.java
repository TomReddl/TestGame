package model.entity.map;

import controller.EffectsController;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.entity.effects.EffectParams;
import model.entity.character.Character;
import model.entity.character.Squad;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static game.GameParams.mapSize;

/**
 * Часть карты мира со всеми персонажами, существами и предметами на ней, а также настройками, свойстенными этой части карты
 */
@Getter
@Setter
@Slf4j
public class MapChunk implements Serializable {
    private MapCellInfo[][] tiles = new MapCellInfo[mapSize][mapSize];
    Map<String, Character> characterList = new HashMap<>();
    Map<String, Creature> creaturesList = new HashMap<>();
    private String mapName;
    private int worldPosX; // координата карты по горизонтали в мире
    private int worldPosY; // координата карты по вертикали в мире
    private Squad playersSquad = new Squad(); // отряд игрока
    private Map<WeatherEnum, Integer> currentWeather = new HashMap<>(); // текущая погода и ее сила
    private Map<WeatherEnum, Integer> accessibleWeathers = new HashMap<>(); // доступная погода и вероятность ее наступления
    private Map<Integer, EffectParams> additionalEffect = new HashMap<>(); // дополнительные эффекты ингредиентов (генерируются случайно при старте новой игры)

    public MapChunk() {
        Character mainCharacter = new Character(32, 0, 0, 0, 0);
        characterList.put(mainCharacter.getId(), mainCharacter);
        playersSquad.setMainCharacter(mainCharacter);
        playersSquad.setName("альфа");
        Fraction playerSquadFraction = new Fraction();
        playerSquadFraction.setId("playerSquad");
        playerSquadFraction.setName("бродяги фронтира");
        playersSquad.setFraction(playerSquadFraction);
        playersSquad.setSelectedCharacter(mainCharacter);
        playersSquad.getCharacters().put(mainCharacter.getId(), mainCharacter);
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
