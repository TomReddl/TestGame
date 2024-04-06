package model.entity.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.editor.CreatureInfo;
import view.Editor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация о существе
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
    private boolean isButchering; // признак разделанной туши (для убитых существ)
    private Integer health;
    private List<Items> inventory; // инвентарь существа

    public Creature(int creatureTypeId, int id, int xPos, int yPos) {
        this.creatureTypeId = creatureTypeId;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
        this.health = Editor.getCreatures().get(creatureTypeId).getHealth();
        if (health == null) { // если здоровье не задано, установим стандартное значение
            health = 100;
        }
        this.isAlive = true;
        this.inventory = new ArrayList<>();
    }

    @JsonIgnore
    public CreatureInfo getInfo() {
        return Editor.getCreatures().get(creatureTypeId);
    }
}
