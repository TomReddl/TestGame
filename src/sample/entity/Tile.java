package sample.entity;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tile {
    private int id;
    private boolean passability;
    private ImageView image;
}
