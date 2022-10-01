package model.entity.player;

import javafx.scene.paint.Color;
import lombok.Getter;
import model.entity.WorldLangEnum;
import view.Game;
import view.params.ParamPanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Все параметры персонажа
 */
public class ParamsInfo implements Serializable {
    @Getter
    private final List<Parameter> legacy = new ArrayList<>(); // наследия персонажа
    @Getter
    private final List<Parameter> characteristics = new ArrayList<>(); // характеристики персонажа
    @Getter
    private final List<Parameter> skills = new ArrayList<>(); // навыки персонажа
    @Getter
    private final List<Indicator> indicators = new ArrayList<>(); // показатели персонажа

    public ParamsInfo() {
        for (int i = 0; i < 6; i++) {
            Parameter legacyParam = new Parameter();
            legacyParam.setCurrentValue(5);
            legacyParam.setRealValue(5);
            legacyParam.setExperience(0);
            legacy.add(legacyParam);

            Parameter characteristic = new Parameter();
            characteristic.setCurrentValue(5);
            characteristic.setRealValue(5);
            characteristic.setExperience(0);
            characteristics.add(characteristic);
        }
        for (int i = 0; i < 5; i++) {
            Indicator indicator = new Indicator();
            indicator.setCurrentValue(50);
            indicator.setMaxValue(100);
            indicator.setMinValue(0);
            indicators.add(indicator);
        }
        for (int i = 0; i < 24; i++) {
            Parameter skill = new Parameter();
            skill.setCurrentValue(10);
            skill.setRealValue(10);
            skills.add(skill);
        }
    }

    // увеличить навык
    public void increaseSkill(int skillId, int points) {
        skills.get(skillId).setCurrentValue(skills.get(skillId).getCurrentValue() + points);
        if (skills.get(skillId).getCurrentValue() > 100) {
            skills.get(skillId).setCurrentValue(100);
        }
        skills.get(skillId).setRealValue(skills.get(skillId).getRealValue() + points);
        if (skills.get(skillId).getRealValue() > 100) {
            skills.get(skillId).setRealValue(100);
        }
        ParamPanel.getSkillsLabels().get(skillId).setText(skills.get(skillId).getCurrentValue().toString());
        Game.showMessage(String.format(
                Game.getText("INCREASE_SKILL_MESSAGE"),
                Game.getText(ParamPanel.getSkillsNames().get(skillId) + "_PARAM_NAME"),
                skills.get(skillId).getCurrentValue().toString()),
                Color.GREEN);
    }
}
