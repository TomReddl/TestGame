import engine.Render;
import entity.World;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stub.StubEntityGenerator;

public class Game extends Application {
  private final Render render = new Render();
  private final World world = new World();
  private final StubEntityGenerator entityGenerator = new StubEntityGenerator();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    world.setEntities(entityGenerator.generate(10000));
    render.prepareWorld(world);

    AnimationTimer animationTimer = new AnimationTimer() {
      long delta;
      long lastFrameTime;
      @Override
      public void handle(long now) {
        delta = now - lastFrameTime;
        lastFrameTime = now;
        render.drawWorld(lastFrameTime);
      }
    };
    animationTimer.start();

    stage.setTitle("Game");
    Group root = new Group();
    root.getChildren().add(render.getCanvas());

    stage.setScene(new Scene(root));
    stage.show();
  }

}
