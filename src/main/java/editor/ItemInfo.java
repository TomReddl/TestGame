package editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entity.ItemTypeEnum;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
* Данные типа предмета в редакторе
* */
@Setter
@Getter
public class ItemInfo {
    private int id;
    private String name;
    private String desc;
    private Long weight;
    private List<ItemTypeEnum> types;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private ImageView icon;
}
