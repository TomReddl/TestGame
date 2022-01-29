package entity.map;

import lombok.Getter;
import lombok.Setter;

/*
 * * Информация о NPC
 */
@Getter
@Setter
public class NPC {
    private int id; // идентификатор данного конкретного NPC
    private int npcTypeId; // идентификатор данных об NPC из списка NPCList
    private int xPos;
    private int yPos;
    private boolean isAlive;

    public NPC(int npcTypeId, int id, int xPos, int yPos) {
        this.npcTypeId = npcTypeId;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
