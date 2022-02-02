package entity;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private ImageView image;
    private int xPosition;
    private int yPosition;
    private int xMapPos; // сдвиг области отрисовки карты от начала координат
    private int yMapPos;
    private DirectionEnum direction; // направление движения персонажа

    public Player() {
        xPosition = 0;
        yPosition = 0;
        xMapPos = 0;
        yMapPos = 0;
        image = new ImageView("/graphics/characters/31.png");
        direction = DirectionEnum.RIGHT;
    }
}
