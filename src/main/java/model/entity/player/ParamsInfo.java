package model.entity.player;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
 * Все параметры персонажа
 */

public class ParamsInfo {
    @Getter
    private List<Parameter> legacy = new ArrayList<>(); // наследия персонажа
    @Getter
    private List<Parameter> characteristics = new ArrayList<>(); // характеристики персонажа
    @Getter
    private List<Parameter> skills = new ArrayList<>(); // навыки персонажа

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

    }
}
