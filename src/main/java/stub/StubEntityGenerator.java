package stub;

import engine.Animation;
import engine.Render;
import entity.EntityState;
import entity.GameEntity;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class StubEntityGenerator {
  private ThreadLocalRandom random = ThreadLocalRandom.current();


  public List<GameEntity> generateSingleton() {
    var entity = new GameEntity();
    entity.setId(UUID.randomUUID());
    entity.setX(random.nextInt(1, Render.WIDTH));
    entity.setY(random.nextInt(1, Render.HEIGHT));
    entity.setCurrentState(EntityState.NORMAL);
    entity.setAnimations(generateAnimations1(EntityState.NORMAL));
    return List.of(entity);
  }

  public List<GameEntity> generate(int count) {
    var entities = new ArrayList<GameEntity>();
    for (int i = 0; i < count; i++) {
      var entity = new GameEntity();
      entity.setId(UUID.randomUUID());
      entity.setX(random.nextInt(1, Render.WIDTH - 50));
      entity.setY(random.nextInt(1, Render.HEIGHT - 50));

      var result = i % 3;

      switch (result) {
        case 0: {
          entity.setCurrentState(EntityState.NORMAL);
          entity.setAnimations(generateAnimations1(EntityState.NORMAL));
          break;
        }
        case 1: {
          entity.setCurrentState(EntityState.DEAD);
          entity.setAnimations(generateAnimations2(EntityState.DEAD));
          break;
        }
        default: {
          entity.setCurrentState(EntityState.RUN);
          entity.setAnimations(generateStatic(EntityState.RUN));
        }
      }

      entities.add(entity);
    }
    return entities;
  }

  private Map<EntityState, Animation> generateAnimations1(EntityState state) {
    var animations = new HashMap<EntityState, Animation>();
    animations.put(state, new Animation.Builder()
            .addAnimation("/Graphics/Animations/1/10.png")
            .addAnimation("/Graphics/Animations/1/11.png")
            .addAnimation("/Graphics/Animations/1/12.png")
            .addAnimation("/Graphics/Animations/1/13.png")
            .addDuration(50)
            .build()
    );
    return animations;
  }

  private Map<EntityState, Animation> generateAnimations2(EntityState state) {
    var animations = new HashMap<EntityState, Animation>();
    animations.put(state, new Animation.Builder()
            .addAnimation("/Graphics/Animations/2/15.png")
            .addAnimation("/Graphics/Animations/2/16.png")
            .addAnimation("/Graphics/Animations/2/17.png")
            .addAnimation("/Graphics/Animations/2/18.png")
            .addAnimation("/Graphics/Animations/2/19.png")
            .addAnimation("/Graphics/Animations/2/20.png")
            .addDuration(30)
            .build()
    );
    return animations;
  }

  private Map<EntityState, Animation> generateStatic(EntityState state) {
    var animations = new HashMap<EntityState, Animation>();
    animations.put(state, new Animation.Builder()
            .addAnimation("/Graphics/Characters/hero.png")
            .buildStatic()
    );
    return animations;
  }

}
