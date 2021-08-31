package sample.entity;

import javafx.scene.image.ImageView;

public class Tile {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPassability() {
        return passability;
    }

    public void setPassability(boolean passability) {
        this.passability = passability;
    }

    private int id;
    private boolean passability;

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    private ImageView image;
}
