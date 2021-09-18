package entity;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NPCInfo implements Serializable {
    private int imageId;
    private ImageView image;
}
