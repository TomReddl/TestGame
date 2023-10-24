package model.entity.player;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Все параметры персонажа
 */
public class ParamsInfo implements Serializable {
    @Getter
    private final List<Parameter> legacy = new ArrayList<>(); // наследия персонажа
    @Getter
    private final List<Parameter> characteristics = new ArrayList<>(); // характеристики персонажа
    @Getter
    private final Map<String, Parameter> skills = new HashMap<>(); // навыки персонажа
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
        List<Parameter> parameterList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Parameter skill = new Parameter();
            skill.setCurrentValue(10);
            skill.setRealValue(10);
            skill.setExperience(0);
            parameterList.add(skill);
        }

        //сила
        skills.put("HEAVY_WEAPON", parameterList.get(0)); // тяжелое оружие
        skills.put("HEAVY_ARMOR", parameterList.get(1)); // тяжелая броня
        skills.put("FARMING", parameterList.get(2)); // фермерство
        skills.put("CARPENTRY", parameterList.get(3)); // плотницкое дело
        skills.put("STONE_ART", parameterList.get(4)); // работа с камнем

        // выносливость
        skills.put("BLOCKING", parameterList.get(5)); // блокирование
        skills.put("HAND_COMBAT", parameterList.get(6)); // рукопашный бой
        skills.put("BLACKSMITHING", parameterList.get(7)); // кузнечное дело
        skills.put("CONSTRUCTION", parameterList.get(8)); // строительство
        skills.put("SEWING", parameterList.get(9)); // шитье

        // ловкость
        skills.put("LIGHT_WEAPON", parameterList.get(10)); // легкое оружие
        skills.put("LIGHT_ARMOR", parameterList.get(11)); // легка броня
        skills.put("ARMORLESS", parameterList.get(12)); // бездоспешный бой
        skills.put("LOCKPICKING", parameterList.get(13)); // взлом замков
        skills.put("COOKING", parameterList.get(14)); // кулинария

        // харизма
        skills.put("SPEECH", parameterList.get(15)); // убеждение
        skills.put("TRADE", parameterList.get(16)); // торговля
        skills.put("COMMAND", parameterList.get(17)); // командование
        skills.put("LOVE", parameterList.get(18)); // любовное мастерство
        skills.put("PERFORMANCE", parameterList.get(19)); // выступление

        // интеллект
        skills.put("TRAINING", parameterList.get(20)); // обучение
        skills.put("POTIONS", parameterList.get(21)); // зельеварение
        skills.put("MEDICINE", parameterList.get(22)); // медицина
        skills.put("ENGINEERING", parameterList.get(23)); // инженерия
        skills.put("ENCHANTMENT", parameterList.get(24)); // зачарование

        // восприятие
        skills.put("MARKSMANSHIP", parameterList.get(25)); // стрельба из лука
        skills.put("PICK_POCKET", parameterList.get(26)); // карманная кража
        skills.put("SNEAK", parameterList.get(27)); // подкрадывание
        skills.put("ANIMAL_HANDLING", parameterList.get(28)); // обращение с животными
        skills.put("DEDUCTION", parameterList.get(29)); // дедукция
    }
}
