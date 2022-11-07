package model.entity.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Информация о NPC
 */
@Getter
@Setter
@NoArgsConstructor
public class NPC implements Serializable {
    @JsonProperty("id")
    private int id; // идентификатор данного конкретного NPC
    @JsonProperty("npcTypeId")
    private int npcTypeId; // идентификатор данных об NPC из списка NPCList
    @JsonProperty("xPos")
    private int xPos;
    @JsonProperty("yPos")
    private int yPos;
    @JsonProperty("isAlive")
    private boolean isAlive;

    public NPC(int npcTypeId, int id, int xPos, int yPos) {
        this.npcTypeId = npcTypeId;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
