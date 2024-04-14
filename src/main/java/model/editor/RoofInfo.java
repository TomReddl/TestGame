package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Информация о крыше в редакторе
 */
@Getter
@Setter
public class RoofInfo implements Serializable {
    private int id;
    @JsonIgnore
    private ImageView image;
}
