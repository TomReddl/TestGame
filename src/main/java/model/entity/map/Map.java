package model.entity.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.entity.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static game.GameParams.mapSize;

/**
 * Карта мира со всеми персонажами и предметами на ней
 */
@Getter
@Setter
@Slf4j
public class Map implements Serializable {
    private MapCellInfo[][] tiles = new MapCellInfo[mapSize][mapSize];
    List<NPC> npcList = new ArrayList<>();
    List<Creature> creaturesList = new ArrayList<>();
    private String mapName;
    private Player player = new Player();

    public Map() {
        mapName = "1";
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                var taleInfo = new MapCellInfo();
                taleInfo.setTile1Id(0);
                taleInfo.setTile2Id(0);
                tiles[i][j] = taleInfo;
            }
        }
    }
}
