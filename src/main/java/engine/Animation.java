package engine;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Animation {
  private final Image[] images;
  private final int duration;
  private final boolean staticImage;

  private Animation(Image[] images, int duration, boolean staticImage) {
    this.images = images;
    this.duration = duration;
    this.staticImage = staticImage;
  }

  public Image getCurrentImage(int time) {
    if (staticImage) {
      return images[0];
    }
    int index = (time % (images.length * duration)) / duration;
    if (index < 0) {
      index = 0;
    }

    return images[index];
  }

  public static class Builder {
    private List<Image> images = new ArrayList<>();
    private int duration = 200;

    public Builder addAnimation(String path) {
      this.images.add(new Image(path));
      return this;
    }

    public Builder addDuration(int duration) {
      this.duration = duration;
      return this;
    }

    public Animation buildStatic() {
      if (images.size() != 1) {
        throw new IllegalArgumentException("can not create static");
      }
      return new Animation(images.toArray(Image[]::new), 0, true);
    }

    public Animation build() {
      if (images.isEmpty()) {
        throw new IllegalArgumentException("can not create empty animation");
      }
      if (duration <=0) {
        throw new IllegalArgumentException("wrong duration " + duration);
      }
      return new Animation(images.toArray(Image[]::new), duration, false);
    }
  }
}
