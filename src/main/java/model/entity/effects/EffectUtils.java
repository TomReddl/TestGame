package model.entity.effects;

import lombok.Getter;
import lombok.Setter;
import view.Game;

import java.util.HashMap;
import java.util.Map;

/*
 * Класс для хранения и работы с эффектами
 */
public class EffectUtils {
    @Getter
    @Setter
    private Map<String, EffectInfo> effects = new HashMap<>();

    public EffectUtils() {
        effects.put("HEALTH_RESTORE", new EffectInfo(Game.getEffectText("HEALTH_RESTORE")));

        effects.put("POWER_INC", new EffectInfo(Game.getEffectText("POWER_INC")));
        effects.put("STAMINA_INC", new EffectInfo(Game.getEffectText("STAMINA_INC")));
        effects.put("AGILITY_INC", new EffectInfo(Game.getEffectText("AGILITY_INC")));
        effects.put("CHARISMA_INC", new EffectInfo(Game.getEffectText("CHARISMA_INC")));
        effects.put("INTELLIGENCE_INC", new EffectInfo(Game.getEffectText("INTELLIGENCE_INC")));
        effects.put("PERCEPTION_INC", new EffectInfo(Game.getEffectText("PERCEPTION_INC")));

        effects.put("POWER_DEC", new EffectInfo(Game.getEffectText("POWER_DEC")));
        effects.put("STAMINA_DEC", new EffectInfo(Game.getEffectText("STAMINA_DEC")));
        effects.put("AGILITY_DEC", new EffectInfo(Game.getEffectText("AGILITY_DEC")));
        effects.put("CHARISMA_DEC", new EffectInfo(Game.getEffectText("CHARISMA_DEC")));
        effects.put("INTELLIGENCE_DEC", new EffectInfo(Game.getEffectText("INTELLIGENCE_DEC")));
        effects.put("PERCEPTION_DEC", new EffectInfo(Game.getEffectText("PERCEPTION_DEC")));

        effects.put("HEAVY_WEAPON_INC", new EffectInfo(Game.getEffectText("HEAVY_WEAPON_INC")));
        effects.put("HEAVY_ARMOR_INC", new EffectInfo(Game.getEffectText("HEAVY_ARMOR_INC")));
        effects.put("FARMING_INC", new EffectInfo(Game.getEffectText("FARMING_INC")));
        effects.put("CARPENTRY_INC", new EffectInfo(Game.getEffectText("CARPENTRY_INC")));
        effects.put("BLOCKING_INC", new EffectInfo(Game.getEffectText("BLOCKING_INC")));
        effects.put("HAND_COMBAT_INC", new EffectInfo(Game.getEffectText("HAND_COMBAT_INC")));
        effects.put("BLACKSMITHING_INC", new EffectInfo(Game.getEffectText("BLACKSMITHING_INC")));
        effects.put("CONSTRUCTION_INC", new EffectInfo(Game.getEffectText("CONSTRUCTION_INC")));
        effects.put("LIGHT_WEAPON_INC", new EffectInfo(Game.getEffectText("LIGHT_WEAPON_INC")));
        effects.put("LIGHT_ARMOR_INC", new EffectInfo(Game.getEffectText("LIGHT_ARMOR_INC")));
        effects.put("ARMORLESS_INC", new EffectInfo(Game.getEffectText("ARMORLESS_INC")));
        effects.put("LOCKPICKING_INC", new EffectInfo(Game.getEffectText("LOCKPICKING_INC")));
        effects.put("SPEECH_INC", new EffectInfo(Game.getEffectText("SPEECH_INC")));
        effects.put("TRADE_INC", new EffectInfo(Game.getEffectText("TRADE_INC")));
        effects.put("COMMAND_INC", new EffectInfo(Game.getEffectText("COMMAND_INC")));
        effects.put("LOVE_INC", new EffectInfo(Game.getEffectText("LOVE_INC")));
        effects.put("TRAINING_INC", new EffectInfo(Game.getEffectText("TRAINING_INC")));
        effects.put("POTIONS_INC", new EffectInfo(Game.getEffectText("POTIONS_INC")));
        effects.put("MEDICINE_INC", new EffectInfo(Game.getEffectText("MEDICINE_INC")));
        effects.put("ENGINEERING_INC", new EffectInfo(Game.getEffectText("ENGINEERING_INC")));
        effects.put("MARKSMANSHIP_INC", new EffectInfo(Game.getEffectText("MARKSMANSHIP_INC")));
        effects.put("PICK_POCKET_INC", new EffectInfo(Game.getEffectText("PICK_POCKET_INC")));
        effects.put("SNEAK_INC", new EffectInfo(Game.getEffectText("SNEAK_INC")));
        effects.put("ANIMAL_HANDLING_INC", new EffectInfo(Game.getEffectText("ANIMAL_HANDLING_INC")));

        effects.put("HEAVY_WEAPON_DEC", new EffectInfo(Game.getEffectText("HEAVY_WEAPON_DEC")));
        effects.put("HEAVY_ARMOR_DEC", new EffectInfo(Game.getEffectText("HEAVY_ARMOR_DEC")));
        effects.put("FARMING_DEC", new EffectInfo(Game.getEffectText("FARMING_DEC")));
        effects.put("CARPENTRY_DEC", new EffectInfo(Game.getEffectText("CARPENTRY_DEC")));
        effects.put("BLOCKING_DEC", new EffectInfo(Game.getEffectText("BLOCKING_DEC")));
        effects.put("HAND_COMBAT_DEC", new EffectInfo(Game.getEffectText("HAND_COMBAT_DEC")));
        effects.put("BLACKSMITHING_DEC", new EffectInfo(Game.getEffectText("BLACKSMITHING_DEC")));
        effects.put("CONSTRUCTION_DEC", new EffectInfo(Game.getEffectText("CONSTRUCTION_DEC")));
        effects.put("LIGHT_WEAPON_DEC", new EffectInfo(Game.getEffectText("LIGHT_WEAPON_DEC")));
        effects.put("LIGHT_ARMOR_DEC", new EffectInfo(Game.getEffectText("LIGHT_ARMOR_DEC")));
        effects.put("ARMORLESS_DEC", new EffectInfo(Game.getEffectText("ARMORLESS_DEC")));
        effects.put("LOCKPICKING_DEC", new EffectInfo(Game.getEffectText("LOCKPICKING_DEC")));
        effects.put("SPEECH_DEC", new EffectInfo(Game.getEffectText("SPEECH_DEC")));
        effects.put("TRADE_DEC", new EffectInfo(Game.getEffectText("TRADE_DEC")));
        effects.put("COMMAND_DEC", new EffectInfo(Game.getEffectText("COMMAND_DEC")));
        effects.put("LOVE_DEC", new EffectInfo(Game.getEffectText("LOVE_DEC")));
        effects.put("TRAINING_DEC", new EffectInfo(Game.getEffectText("TRAINING_DEC")));
        effects.put("POTIONS_DEC", new EffectInfo(Game.getEffectText("POTIONS_DEC")));
        effects.put("MEDICINE_DEC", new EffectInfo(Game.getEffectText("MEDICINE_DEC")));
        effects.put("ENGINEERING_DEC", new EffectInfo(Game.getEffectText("ENGINEERING_DEC")));
        effects.put("MARKSMANSHIP_DEC", new EffectInfo(Game.getEffectText("MARKSMANSHIP_DEC")));
        effects.put("PICK_POCKET_DEC", new EffectInfo(Game.getEffectText("PICK_POCKET_DEC")));
        effects.put("SNEAK_DEC", new EffectInfo(Game.getEffectText("SNEAK_DEC")));
        effects.put("ANIMAL_HANDLING_DEC", new EffectInfo(Game.getEffectText("ANIMAL_HANDLING_DEC")));
    }
}
