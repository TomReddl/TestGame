package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import model.entity.creatures.CreatureSizeEnum;
import model.entity.creatures.CreatureTypeEnum;

import java.util.List;
import java.util.Map;
/**
 * Данные типа существа в редакторе
 */
@Setter
@Getter
public class CreatureInfo {
    private int imageId;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String desc;
    private List<CreatureTypeEnum> types; // типы существа. Существо может принадлежать более чем к одному типу
    private Map<String, String> organs; // органы существа
    private Integer health; // здоровье существа
    private Integer level; // уровень существа (агрегированный показатель силы, используется для определения получаемого опыта, лута с существа и т.д.)
    private String bloodType; // тип крови существа
    private CreatureSizeEnum size; // размер существа
}
