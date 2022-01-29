package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

/*
* Данные типа предмета в редакторе
* */
@Setter
@Getter
public class ItemInfo {
    private int id;
    private String name;
    private String desc;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private ImageView icon;
}
