package editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

/*
* Данные типа существа в редакторе
* */
@Setter
@Getter
public class CreatureInfo {
    private int imageId;
    @JsonIgnore
    private ImageView image;
}
