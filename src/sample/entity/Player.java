package sample.entity;

import javafx.scene.image.ImageView;

public class Player {
    private ImageView image;
    private int xPosition;
    private int yPosition;
    private int xMapPos; // сдвиг области отрисовки карты от начала координат
    private int yMapPos;

    public Player() {
        xPosition = 0;
        yPosition = 0;
        xMapPos = 0;
        yMapPos = 0;
        image = new ImageView("hero.png");
    }

    public int getxMapPos() {
        return xMapPos;
    }

    public void setxMapPos(int xMapPos) {
        this.xMapPos = xMapPos;
    }

    public int getyMapPos() {
        return yMapPos;
    }

    public void setyMapPos(int yMapPos) {
        this.yMapPos = yMapPos;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public ImageView getImage() {
        return image;
    }
}
