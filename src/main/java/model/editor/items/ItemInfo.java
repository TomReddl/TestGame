package model.editor.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import model.entity.ItemTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Данные типа предмета в редакторе
 */
@Setter
@Getter
public class ItemInfo {
    private Integer id;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String desc;
    private Long weight;
    private Long volume;
    private Long price;
    protected Integer maxStrength; // Максимальная прочность
    private Boolean stackable; // предмет может храниться в стеках
    private Boolean fragile; // хрупкий предмет, его нельзя починить Если его прочность упадет до 0, предмет уничтожится
    private List<ItemTypeEnum> types;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private ImageView icon;
    private Map<String, String> params; // дополнительные параметры предмета, мапа формата <Название параметра, значение>
}
