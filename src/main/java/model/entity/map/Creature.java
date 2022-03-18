package model.entity.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/*
 * * Информация о существе
 */
@Getter
@Setter
@NoArgsConstructor
public class Creature implements Serializable {
    @JsonProperty("id")
    private int id; // идентификатор данного конкретного существа
    @JsonProperty("creatureTypeId")
    private int creatureTypeId; // идентификатор типа существа
    @JsonProperty("xPos")
    private int xPos;
    @JsonProperty("yPos")
    private int yPos;
    @JsonProperty("isAlive")
    private boolean isAlive;

    public Creature(int creatureTypeId, int id, int xPos, int yPos) {
        this.creatureTypeId = creatureTypeId;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
