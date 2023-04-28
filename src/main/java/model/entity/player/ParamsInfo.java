package model.entity.player;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
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

        legacy.get(0).setStrId("HAN");
        legacy.get(1).setStrId("RISA");
        legacy.get(2).setStrId("ULKOR");
        legacy.get(3).setStrId("MHAA");
        legacy.get(4).setStrId("WURTUS");
        legacy.get(5).setStrId("SHI_DOL");

        characteristics.get(0).setStrId("POWER");
        characteristics.get(1).setStrId("STAMINA");
        characteristics.get(2).setStrId("AGILITY");
        characteristics.get(3).setStrId("CHARISMA");
        characteristics.get(4).setStrId("INTELLIGENCE");
        characteristics.get(5).setStrId("PERCEPTION");

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
            skill.setExperience(0);
            skills.add(skill);
        }

        skills.get(0).setStrId("HEAVY_WEAPON");
        skills.get(1).setStrId("HEAVY_ARMOR");
        skills.get(2).setStrId("FARMING");
        skills.get(3).setStrId("CARPENTRY");
        skills.get(4).setStrId("BLOCKING");
        skills.get(5).setStrId("HAND_COMBAT");
        skills.get(6).setStrId("BLACKSMITHING");
        skills.get(7).setStrId("CONSTRUCTION");
        skills.get(8).setStrId("LIGHT_WEAPON");
        skills.get(9).setStrId("LIGHT_ARMOR");
        skills.get(10).setStrId("ARMORLESS");
        skills.get(11).setStrId("LOCKPICKING");
        skills.get(12).setStrId("SPEECH");
        skills.get(13).setStrId("TRADE");
        skills.get(14).setStrId("COMMAND");
        skills.get(15).setStrId("LOVE");
        skills.get(16).setStrId("TRAINING");
        skills.get(17).setStrId("POTIONS");
        skills.get(18).setStrId("MEDICINE");
        skills.get(19).setStrId("ENGINEERING");
        skills.get(20).setStrId("MARKSMANSHIP");
        skills.get(21).setStrId("PICK_POCKET");
        skills.get(22).setStrId("SNEAK");
        skills.get(23).setStrId("ANIMAL_HANDLING");
    }
}
