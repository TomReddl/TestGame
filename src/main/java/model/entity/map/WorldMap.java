package model.entity.map;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.entity.player.Player;

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
    List<NPC> npcList = new ArrayList<>();
    List<Creature> creaturesList = new ArrayList<>();
    private String mapName;
    private Player player = new Player();
    private Pair<WeatherEnum, Integer> currentWeather; // текущая погода и ее сила
    private Map<WeatherEnum, Integer> accessibleWeathers = new HashMap<>(); // доступная погода и вероятность ее наступления

    public WorldMap() {
        accessibleWeathers.put(WeatherEnum.CLEAR, 1);
        currentWeather = new Pair(WeatherEnum.CLEAR, 1);
        mapName = "1";
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                var taleInfo = new MapCellInfo();
                taleInfo.setTile1Id(0);
                taleInfo.setTile2Id(0);
                tiles[i][j] = taleInfo;
            }
        }
        tiles[1][1].setTile2Id(249); // алхимический стол для тестирования
    }
}
