package model.entity.map;

import lombok.Getter;
import lombok.Setter;
import model.entity.GameCalendar;

import java.io.Serializable;

/**
 * Глобальная информация о мире игры
 */
@Getter
@Setter
public class WorldInfo implements Serializable {
    String worldName; // название мира
    @Getter
    private GameCalendar.GameDate currentDate = new GameCalendar.GameDate(); // текущая игровая дата
}
