package model.editor.items;

import lombok.Getter;
import lombok.Setter;
import model.entity.effects.EffectParams;

import java.util.List;

/**
 * Данные о съедобном предмете: ингридиенте, зелье или еде
 */
@Setter
@Getter
public class EdibleInfo extends ItemInfo {
    private Integer taste; // вкус
    private Integer hunger; // сколько голода восстанавливает
    private Integer thirst; // сколько жажды восстанавливает
    private Integer remainder; // остаток после съедения предмета
}
