package model.entity.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import model.entity.map.Fraction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация об отряде
 */
@Getter
@Setter
public class Squad implements Serializable {
    String name; // название отряда
    List<Character> characters = new ArrayList<>(); // список членов отряда
    Character selectedCharacter; // выбранный в данный момент персонаж
    Character mainCharacter; // основной персонаж
    Fraction fraction; // фракция, к которой принадлежит отряд
    @JsonProperty("xMapPos")
    private int xMapPos; // сдвиг области отрисовки карты от начала координат
    @JsonProperty("yMapPos")
    private int yMapPos;
}
