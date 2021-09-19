package entity;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
// Класс с данными типа NPC в редакторе
public class NPCInfo implements Serializable {
    private int imageId;
    private ImageView image;
}
