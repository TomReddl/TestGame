package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import model.entity.creatures.CreatureSizeEnum;
import model.entity.character.GenderEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Данные типа персонажа в редакторе
 */
@Getter
@Setter
public class CharacterInfo implements Serializable {
    private int imageId;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String desc;
    private GenderEnum gender;
    private String bloodType; // тип крови персонажа
    private Map<String, String> items; // предметы
    private CreatureSizeEnum size; // размер персонажа
    private String tradableItems; // торгуемые предметы
    private String profession; // профессия
    private List<String> trainableSkills; // тренируемые навыки
    private Map<String, String> legacy; // наследия персонажа в формате название - значение
    private Map<String, String> characteristics; // характеристики персонажа в формате название - значение
    private Map<String, String> skills; // навыки персонажа в формате название - значение
    private BigDecimal cellCoefficient; // коэффициент цен при продаже предметов
    private BigDecimal buyCoefficient; // коэффициент цен при покупке предметов
}
