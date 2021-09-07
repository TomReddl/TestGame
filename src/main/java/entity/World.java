package entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class World {
  List<GameEntity> entities;
}
