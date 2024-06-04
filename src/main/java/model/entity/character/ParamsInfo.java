package model.entity.character;

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
    private final Map<String, Parameter> legacy = new HashMap<>(); // наследия персонажа
    @Getter
    private final Map<String, Parameter> characteristics = new HashMap<>(); // характеристики персонажа
    @Getter
    private final Map<String, Parameter> skills = new HashMap<>(); // навыки персонажа
    @Getter
    private final List<Indicator> indicators = new ArrayList<>(); // показатели персонажа

    public ParamsInfo() {
        List<Parameter> legacyList = new ArrayList<>();
        List<Parameter> characteristicsList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Parameter legacyParam = new Parameter();
            legacyParam.setCurrentValue(5);
            legacyParam.setRealValue(5);
            legacyParam.setExperience(0);
            legacyList.add(legacyParam);

            Parameter characteristic = new Parameter();
            characteristic.setCurrentValue(5);
            characteristic.setRealValue(5);
            characteristic.setExperience(0);
            characteristicsList.add(characteristic);
        }

        legacy.put("HAN", legacyList.get(0));
        legacy.put("RISA", legacyList.get(1));
        legacy.put("ULKOR", legacyList.get(2));
        legacy.put("MHAA", legacyList.get(3));
        legacy.put("WURTUS", legacyList.get(4));
        legacy.put("SHI_DOL", legacyList.get(5));

        characteristics.put("POWER", characteristicsList.get(0));
        characteristics.put("STAMINA", characteristicsList.get(1));
        characteristics.put("AGILITY", characteristicsList.get(2));
        characteristics.put("CHARISMA", characteristicsList.get(3));
        characteristics.put("INTELLIGENCE", characteristicsList.get(4));
        characteristics.put("PERCEPTION", characteristicsList.get(5));

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
