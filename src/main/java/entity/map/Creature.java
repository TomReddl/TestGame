package entity.map;

import lombok.Getter;
import lombok.Setter;

/*
 * * Информация о существе
 */
@Getter
@Setter
public class Creature {
    private int id; // идентификатор данного конкретного существа
    private int creatureTypeId; // идентификатор данных о существе из списка CreatureList
    private int xPos;
    private int yPos;
    private boolean isAlive;

    public Creature(int creatureTypeId, int id, int xPos, int yPos) {
        this.creatureTypeId = creatureTypeId;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
