package entity;

import engine.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Tile {
    private UUID id;
    private String name;
    private Image image;
}
