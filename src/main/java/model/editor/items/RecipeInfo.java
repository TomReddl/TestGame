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
    private Integer itemCount; // количество создаваемых предметов
    private CraftingPlaceEnum craftingPlace; // тип стола для крафта
    private Integer skillId; // навык для крафта
    private Integer minRequiredSkillLevel; // минимальный требуемый уровень навыка для крафта. Не обязательное
    private Integer level; // требуемый уровень стола для крафта
    private Integer timeToCraft; // время на создание предмета. Если null, то берется фиксированное значение
    private Integer exp; // Опыт за создание предмета. Если null, то берется фиксированное значение
    private String group; // группа, к которой относится рецепт
    private Map<String, Integer> elements; // мапа, хранящая пары элемент для крафта - требуемое количество
}
