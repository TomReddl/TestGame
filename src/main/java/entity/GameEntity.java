package entity;

import engine.Animation;
import engine.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class GameEntity {
  private UUID id;
  private String name;
  private String desc;
  private int x;
  private int y;
  private EntityState currentState;
  private Map<EntityState, Animation> animations;

  public Image getCurrentImage(int time) {
    return animations.get(currentState).getCurrentImage(time);
  }
}
