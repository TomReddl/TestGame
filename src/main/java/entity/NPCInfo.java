package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
* Данные типа NPC в редакторе
* */
@Getter
@Setter
public class NPCInfo implements Serializable {
    private int imageId;
    @JsonIgnore
    private ImageView image;
}
