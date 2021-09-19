package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// Класс с данными типа существа в редакторе
public class CreatureInfo {
    private int imageId;
    @JsonIgnore
    private ImageView image;
}
