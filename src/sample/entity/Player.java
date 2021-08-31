package sample.entity;

import javafx.scene.image.ImageView;

public class Player {
    private ImageView image;

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

    private int xPosition;
    private int yPosition;

    public Player() {
        xPosition = 0;
        yPosition = 0;
        image = new ImageView("hero.png");
    }

    public ImageView getImage() {
        return image;
    }
}
