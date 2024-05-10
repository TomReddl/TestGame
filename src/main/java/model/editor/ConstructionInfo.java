package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import model.entity.map.Creature;
import model.entity.map.MapCellInfo;
import model.entity.character.Character;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Информация о конструкции
 */
@Getter
@Setter
public class ConstructionInfo implements Serializable {
    private String name; // название конструкции
    @JsonIgnore
    private ImageView image;
    private int id;
    private MapCellInfo[][] tiles; // тайлы конструкции
    Map<String, Character> characterList = new HashMap<>(); // персонажи
    Map<String, Creature> creaturesList = new HashMap<>(); // существа

    public ConstructionInfo(int x1, int y1, int x2, int y2) {
        tiles = new MapCellInfo[Math.abs(x2-x1) + 1][Math.abs(y2-y1) + 1];
    }

    public ConstructionInfo() {
    }
}
