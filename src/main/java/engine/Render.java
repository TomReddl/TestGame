package engine;

import entity.GameEntity;
import entity.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Render {
  public static int WIDTH = 1000;
  public static int HEIGHT = 700;
  private Canvas canvas = new Canvas(WIDTH, HEIGHT);
  private GraphicsContext gc = canvas.getGraphicsContext2D();
  private AtomicInteger tick = new AtomicInteger(0);
  private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
  private World world;

  public void start(World world) {
    this.world = world;
//    if (world.getEntities().size() > ) {
//    }
    scheduledThreadPool.scheduleAtFixedRate(new DrawingThread(), 500, 15, TimeUnit.MILLISECONDS);
  }

  public void stop() {
    scheduledThreadPool.shutdown();
    while (!scheduledThreadPool.isShutdown()) {
      scheduledThreadPool.shutdown();
    }
  }

  public void drawImage(int x, int y, Image image) {
    gc.drawImage(image.getImage(), x, y);
  }

  public void drawEntity(GameEntity entity) {
    gc.drawImage(entity.getCurrentImage(tick.get()).getImage(), entity.getX(), entity.getY());
  }

  private class DrawingThread implements Runnable {

    @Override
    public void run() {
      var value = tick.get();
      if (value!= 1000) {
        tick.incrementAndGet();
      } else {
        tick.set(0);
      }
      for (GameEntity entity : world.getEntities()) {
        drawEntity(entity);
      }
    }
  }

}
