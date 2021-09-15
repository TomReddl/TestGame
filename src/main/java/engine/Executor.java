package engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class Executor {
  private final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
  private AtomicInteger tick = new AtomicInteger(0);

  public void stop() {
    scheduledThreadPool.shutdown();
    while (!scheduledThreadPool.isShutdown()) {
      scheduledThreadPool.shutdown();
    }
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
//      for (GameEntity entity : world.getEntities()) {
//        drawEntity(entity);
//      }
    }
  }

}
