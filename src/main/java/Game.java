import engine.Render;
import entity.World;
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
    world.setEntities(entityGenerator.generate(100));
//    world.setEntities(entityGenerator.generateSingleton());

    stage.setTitle("Game");
    Group root = new Group();
    root.getChildren().add(render.getCanvas());

    render.start(world);

    stage.setScene(new Scene(root));
    stage.show();
  }

  @Override
  public void stop(){
    render.stop();
  }

}
