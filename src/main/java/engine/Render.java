package engine;

import entity.GameEntity;
import entity.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Render {
  public static int WIDTH = 1000;
  public static int HEIGHT = 700;
  private Canvas canvas = new Canvas(WIDTH, HEIGHT);
  private GraphicsContext gc = canvas.getGraphicsContext2D();

  private World world;

  public void prepareWorld(World world) {
    this.world = world;
  }

  public void drawImage(int x, int y, Image image) {
    gc.drawImage(image.getImage(), x, y);
  }

  public void drawEntity(GameEntity entity, long lastFrameTime) {
    gc.drawImage(entity.getCurrentImage((int) lastFrameTime).getImage(), entity.getX(), entity.getY());
  }

  public void drawWorld(long lastFrameTime) {
    for (var entity : world.getEntities()) {
      drawEntity(entity, lastFrameTime);
    }
  }
}
