package editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
* Данные зоны в редакторе
* */
@Getter
@Setter
public class ZoneInfo implements Serializable {
    private int id;
    @JsonIgnore
    private ImageView image;
}
