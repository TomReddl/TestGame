package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tile {
    private int id;
    private String name;
    private String desc;
    private Integer hp; // прочность тайла, если 0, он неразрушаем
    private boolean passability; // проходимость тайла
    @JsonIgnore
    private ImageView image;
}
