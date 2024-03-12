package model.entity.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import controller.utils.generation.NPCGenerator;
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
    @JsonProperty("dialodId")
    private String dialodId; // идентификатор диалога
    @JsonProperty("dialogPhase")
    private String dialogPhase; // этап диалога. Используется для изменения диалога при повторных общениях с NPC
    @JsonProperty("name")
    private String name; // имя NPC

    public NPC(int npcTypeId, int id, int xPos, int yPos) {
        this.npcTypeId = npcTypeId;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isAlive = true;
        this.name = NPCGenerator.generateName(npcTypeId);
    }
}
