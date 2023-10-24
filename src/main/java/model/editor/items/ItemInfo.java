package model.editor.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectParams;
import view.Game;

import java.util.ArrayList;
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
    protected int maxStrength; // Максимальная прочность
    private Boolean stackable; // предмет может храниться в стеках
    private Boolean fragile; // хрупкий предмет, его нельзя починить Если его прочность упадет до 0, предмет уничтожится
    private boolean burn; // может ли предмет сгореть
    private List<ItemTypeEnum> types;
    @JsonIgnore
    private ImageView image;
    @JsonIgnore
    private ImageView icon;
    private Map<String, String> params; // дополнительные параметры предмета, мапа формата <Название параметра, значение>
    private List<EffectParams> effects; // эффекты
    private Integer inlayerId; // идентификатор инкрустата (для зачаровываемых предметов)

    public List<EffectParams> getEffects() {
        // для ингредиентов добавляем случайные дополнительные эффекты
        if (this.getTypes().contains(ItemTypeEnum.INGREDIENT) && effects != null) {
            List<EffectParams> allEffects = new ArrayList<>(effects);
            allEffects.add(Game.getMap().getAdditionalEffect().get(id));
            return allEffects;
        } else {
            return effects;
        }
    }

    public List<EffectParams> getBaseEffects() {
        return effects;
    }
}
