package engine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Image {
  private final String path;
  private final javafx.scene.image.Image image;

  public Image(String path) {
    this.path = path;
    this.image = new javafx.scene.image.Image(path);
  }

  @Override
  public String toString() {
    return "Image{" +
            "path=" + path +
            '}';
  }
}
