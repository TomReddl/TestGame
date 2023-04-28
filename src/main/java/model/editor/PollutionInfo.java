package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Данные типа загрязнения в редакторе
 */
@Getter
@Setter
public class PollutionInfo implements Serializable {
    private int id;
    private Integer nextId; // id следующего уровня загрязнения того же типа
    @JsonIgnore
    private ImageView image;
}
