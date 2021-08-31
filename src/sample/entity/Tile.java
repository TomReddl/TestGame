package sample.entity;

import javafx.scene.image.ImageView;

public class Tile {
    private int id;
    private boolean passability;
    private ImageView image;

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

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
