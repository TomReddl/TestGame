package model.editor.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.entity.ItemTypeEnum;
import javafx.scene.control.Label;
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
    private Integer id;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String desc;
    private Long weight;
    private Long volume;
    private Long price;
    private Boolean stackable;
    private List<ItemTypeEnum> types;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private ImageView icon;
    @JsonIgnore
    private Label nameLabel;
    private List<String> params; // дополнительные параметры предмета
}
