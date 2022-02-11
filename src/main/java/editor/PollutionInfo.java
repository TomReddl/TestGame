package editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
* Данные типа загрязнения в редакторе
* */
@Getter
@Setter
public class PollutionInfo implements Serializable {
    private int id;
    @JsonIgnore
    private ImageView image;
}
