package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import model.entity.player.GenderEnum;

import java.io.Serializable;

/**
 * Данные типа NPC в редакторе
 */
@Getter
@Setter
public class NPCInfo implements Serializable {
    private int imageId;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String desc;
    private GenderEnum gender;
}
