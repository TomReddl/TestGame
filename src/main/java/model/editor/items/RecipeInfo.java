package model.editor.items;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Данные о рецепте крафта предмета
 */
@Setter
@Getter
public class RecipeInfo {
    private Integer recipeId; // id рецепта
    private Integer itemId; // id создаваемого предмета
    private String craftingPlace; // название стола для крафта
    private Integer level; // требуемый уровень стола для крафта
    private Integer timeToCraft; // время на создание предмета
    private Map<String, Integer> elements; // мапа, хранящая пары элемент для крафта - требуемое количество
}
