package model.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Данные тайла в редакторе
 */
@Getter
@Setter
public class TileInfo {
    private int id;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String desc;
    private Integer strength; // прочность тайла, если 0, он неразрушаем
    private boolean passability; // проходимость тайла
    private boolean twoLayer; // у тайла есть второй уровень, отрисовывающийся поверх персонажа
    private String type; // тип тайла
    private Map<String, String> params; // дополнительные параметры тайла, мапа формата <Название параметра, значение>
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private ImageView upLayerImage;
}
