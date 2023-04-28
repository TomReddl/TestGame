package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

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
    private Map<String, String> organs; // органы
    private Integer health; // здоровье существа
    private String bloodType; // тип крови существа
}
