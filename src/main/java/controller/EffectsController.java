package controller;

import javafx.scene.image.Image;
import lombok.Getter;
import model.editor.items.ItemInfo;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectInfo;
import model.entity.effects.EffectParams;
import model.entity.effects.EffectTypeEnum;
import model.entity.map.Items;
import model.entity.character.Character;
import view.Editor;
import view.Game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для хранения и работы с эффектами
 */
public class EffectsController {
    @Getter
    private static final Map<String, EffectInfo> effects = new HashMap<>();
    @Getter
    private static final Map<String, Image> potionImages = new HashMap<>(); // цвета зелий
    @Getter
    private static final List<EffectParams> additionalEffects = new ArrayList<>(); // дополнительные параметры ингредиентов, распределяющиеся случайно в начале новой игры

    static {
        potionImages.put("AQUA", new Image("/graphics/items/icons/potions/aqua.png"));
        potionImages.put("BLUE", new Image("/graphics/items/icons/potions/blue.png"));
        potionImages.put("DARK_GREEN", new Image("/graphics/items/icons/potions/darkGreen.png"));
        potionImages.put("GREEN", new Image("/graphics/items/icons/potions/green.png"));
        potionImages.put("PURPLE", new Image("/graphics/items/icons/potions/purple.png"));
        potionImages.put("RED", new Image("/graphics/items/icons/potions/red.png"));
        potionImages.put("VIOLET", new Image("/graphics/items/icons/potions/violet.png"));
        potionImages.put("YELLOW", new Image("/graphics/items/icons/potions/yellow.png"));

        effects.put("HEALTH_RESTORE", new EffectInfo(Game.getEffectText("HEALTH_RESTORE")));

        effects.put("POWER_INC", new EffectInfo(Game.getEffectText("POWER_INC"), "GREEN"));
        effects.put("STAMINA_INC", new EffectInfo(Game.getEffectText("STAMINA_INC"), "GREEN"));
        effects.put("AGILITY_INC", new EffectInfo(Game.getEffectText("AGILITY_INC"), "GREEN"));
        effects.put("CHARISMA_INC", new EffectInfo(Game.getEffectText("CHARISMA_INC"), "GREEN"));
        effects.put("INTELLIGENCE_INC", new EffectInfo(Game.getEffectText("INTELLIGENCE_INC"), "GREEN"));
        effects.put("PERCEPTION_INC", new EffectInfo(Game.getEffectText("PERCEPTION_INC"), "GREEN"));

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
        effects.put("STONE_ART_INC", new EffectInfo(Game.getEffectText("STONE_ART_INC")));
        effects.put("SEWING_INC", new EffectInfo(Game.getEffectText("SEWING_INC")));
        effects.put("COOKING_INC", new EffectInfo(Game.getEffectText("COOKING_INC")));
        effects.put("PERFORMANCE_INC", new EffectInfo(Game.getEffectText("PERFORMANCE_INC")));
        effects.put("ENCHANTMENT_INC", new EffectInfo(Game.getEffectText("ENCHANTMENT_INC")));
        effects.put("DEDUCTION_INC", new EffectInfo(Game.getEffectText("DEDUCTION_INC")));

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

        effects.put("EPIPHANY", new EffectInfo(Game.getEffectText("EPIPHANY"), "VIOLET"));  // прозрение
        effects.put("FIRE_STEPS", new EffectInfo(Game.getEffectText("FIRE_STEPS"), "RED")); // огненная поступь
        effects.put("ADD_EXP", new EffectInfo(Game.getEffectText("ADD_EXP"), "BLUE"));      // добавить опыт
        effects.put("BLIND", new EffectInfo(Game.getEffectText("BLIND"), "GREEN"));         // слепота

        effects.put("FIRE_DAMAGE_RESIST", new EffectInfo(Game.getEffectText("FIRE_DAMAGE_RESIST"), "RED"));
        effects.put("ELECTRIC_DAMAGE_RESIST", new EffectInfo(Game.getEffectText("ELECTRIC_DAMAGE_RESIST"), "BLUE"));
        effects.put("FROST_DAMAGE_RESIST", new EffectInfo(Game.getEffectText("FROST_DAMAGE_RESIST"), "BLUE"));
        effects.put("ACID_DAMAGE_RESIST", new EffectInfo(Game.getEffectText("ACID_DAMAGE_RESIST"), "GREEN"));
        effects.put("ROT_DAMAGE_RESIST", new EffectInfo(Game.getEffectText("ROT_DAMAGE_RESIST"), "RED"));

        effects.put("INSECT_DAMAGE_ADD", new EffectInfo(Game.getEffectText("INSECT_DAMAGE_ADD"), "GREEN"));
        effects.put("REPTILE_DAMAGE_ADD", new EffectInfo(Game.getEffectText("REPTILE_DAMAGE_ADD"), "GREEN"));
        effects.put("MOLD_DAMAGE_ADD", new EffectInfo(Game.getEffectText("MOLD_DAMAGE_ADD"), "GREEN"));
        effects.put("HUMANOID_DAMAGE_ADD", new EffectInfo(Game.getEffectText("HUMANOID_DAMAGE_ADD"), "GREEN"));
        effects.put("ABERRATION_DAMAGE_ADD", new EffectInfo(Game.getEffectText("ABERRATION_DAMAGE_ADD"), "GREEN"));

        effects.put("WATER_WALK", new EffectInfo(Game.getEffectText("WATER_WALK"), "BLUE"));
        effects.put("MOLD_STRIKE", new EffectInfo(Game.getEffectText("MOLD_STRIKE"), "GREEN"));
        effects.put("LIFE_DETECTION", new EffectInfo(Game.getEffectText("LIFE_DETECTION"), "BLUE"));

        effects.put("PIERCING_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("PIERCING_DAMAGE_IMMUNITY"), "BLUE"));
        effects.put("CUTTING_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("CUTTING_DAMAGE_IMMUNITY"), "BLUE"));
        effects.put("CRUSHING_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("CRUSHING_DAMAGE_IMMUNITY"), "GREEN"));
        effects.put("EXPLOSIVE_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("EXPLOSIVE_DAMAGE_IMMUNITY"), "GREEN"));
        effects.put("FIRE_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("FIRE_DAMAGE_IMMUNITY"), "RED"));
        effects.put("ELECTRIC_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("ELECTRIC_DAMAGE_IMMUNITY"), "BLUE"));
        effects.put("FROST_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("FROST_DAMAGE_IMMUNITY"), "BLUE"));
        effects.put("ACID_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("ACID_DAMAGE_IMMUNITY"), "GREEN"));
        effects.put("ROT_DAMAGE_IMMUNITY", new EffectInfo(Game.getEffectText("ROT_DAMAGE_IMMUNITY"), "RED"));

        effects.put("MOLD_SPORE_IMMUNITY", new EffectInfo(Game.getEffectText("MOLD_SPORE_IMMUNITY"), "BLUE"));

        effects.put("PIERCING_DAMAGE", new EffectInfo(Game.getEffectText("PIERCING_DAMAGE"), "BLUE"));
        effects.put("CUTTING_DAMAGE", new EffectInfo(Game.getEffectText("CUTTING_DAMAGE"), "BLUE"));
        effects.put("CRUSHING_DAMAGE", new EffectInfo(Game.getEffectText("CRUSHING_DAMAGE"), "GREEN"));
        effects.put("EXPLOSIVE_DAMAGE", new EffectInfo(Game.getEffectText("EXPLOSIVE_DAMAGE"), "GREEN"));
        effects.put("FIRE_DAMAGE", new EffectInfo(Game.getEffectText("FIRE_DAMAGE"), "RED"));
        effects.put("ELECTRIC_DAMAGE", new EffectInfo(Game.getEffectText("ELECTRIC_DAMAGE"), "BLUE"));
        effects.put("FROST_DAMAGE", new EffectInfo(Game.getEffectText("FROST_DAMAGE"), "BLUE"));
        effects.put("ACID_DAMAGE", new EffectInfo(Game.getEffectText("ACID_DAMAGE"), "GREEN"));
        effects.put("ROT_DAMAGE", new EffectInfo(Game.getEffectText("ROT_DAMAGE"), "RED"));

        effects.put("PHASING", new EffectInfo(Game.getEffectText("PHASING"), "RED"));

        setAdditionalEffects();
    }

    private static void setAdditionalEffects() {
        additionalEffects.add(new EffectParams("EPIPHANY"));
        additionalEffects.add(new EffectParams("HEALTH_RESTORE"));
        additionalEffects.add(new EffectParams("POWER_INC"));
        Collections.shuffle(additionalEffects);
    }

    /**
     * Применить новые эффекты к персонажу
     *
     * @param items  - предмет, эффекты которого применяются
     * @param character - персонаж
     */
    public static void applyEffects(Items items, Character character) {
        List<EffectParams> effectParams = items.getEffects();
        if (effectParams != null) {
            character.getAppliedEffects().addAll(effectParams);
            for (EffectParams effect : effectParams) {
                effect.setBaseItem(items);
                setEffectType(effect, items);
                executeEffect(character, effect, true);
            }
            Game.getParams().refreshParamsValueViews(); // перерисовывает параметры персонажа
            Game.getEffectsPanel().refreshEffectsPanel();
        }
    }

    /**
     * Установить тип эффекта
     *
     * @param effect - эффект
     * @param item   - предмет
     */
    private static void setEffectType(EffectParams effect, Items item) {
        if (item != null) {
            if (item.getInfo().getTypes().contains(ItemTypeEnum.POTION)) {
                effect.setType(EffectTypeEnum.POTION);
            } else {
                effect.setType(EffectTypeEnum.ITEM);
            }
        }
    }

    /**
     * Выполнить действие наложенных на персонажа эффектов
     *
     * @param character- персонаж
     */
    public static void executeEffects(Character character) {
        List<EffectParams> removedEffects = new ArrayList<>();
        for (EffectParams effect : character.getAppliedEffects()) {
                if (effect.getDurability() != null && effect.getDurability() < 0) {
                    removedEffects.add(effect);
                    Game.getParams().refreshParamsValueViews(); // перерисовывает параметры персонажа
                    Game.getEffectsPanel().refreshEffectsPanel();
                } else {
                    executeEffect(character, effect, false);
                    if (effect.getDurability() != null) {
                        effect.setDurability(effect.getDurability() - 1);
                    }
                }
        }
        removedEffects.forEach(effect -> removeEffect(character, effect));
    }

    /**
     * Выполнить действие эффекта
     *
     * @param character      - персонаж
     * @param effect      - эффект
     * @param isFirstTime - действие выполняется первый раз. Для многих эффектов не нужно ничего делать после первого применения
     */
    private static void executeEffect(Character character, EffectParams effect, boolean isFirstTime) {
        switch (effect.getStrId()) {
            case "HEALTH_RESTORE": {
                if (character.getParams().getIndicators().get(0).getCurrentValue() < character.getParams().getIndicators().get(0).getMaxValue()) {
                    character.getParams().getIndicators().get(0).setCurrentValue(
                            character.getParams().getIndicators().get(0).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            // увеличение характеристик
            case "POWER_INC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(0).setCurrentValue(
                            character.getParams().getCharacteristics().get(0).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "STAMINA_INC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(1).setCurrentValue(
                            character.getParams().getCharacteristics().get(1).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "AGILITY_INC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(2).setCurrentValue(
                            character.getParams().getCharacteristics().get(2).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "CHARISMA_INC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(3).setCurrentValue(
                            character.getParams().getCharacteristics().get(3).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "INTELLIGENCE_INC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(4).setCurrentValue(
                            character.getParams().getCharacteristics().get(4).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "PERCEPTION_INC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(5).setCurrentValue(
                            character.getParams().getCharacteristics().get(5).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            // уменьшение характеристик
            case "POWER_DEC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(0).setCurrentValue(
                            character.getParams().getCharacteristics().get(0).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "STAMINA_DEC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(1).setCurrentValue(
                            character.getParams().getCharacteristics().get(1).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "AGILITY_DEC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(2).setCurrentValue(
                            character.getParams().getCharacteristics().get(2).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "CHARISMA_DEC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(3).setCurrentValue(
                            character.getParams().getCharacteristics().get(3).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "INTELLIGENCE_DEC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(4).setCurrentValue(
                            character.getParams().getCharacteristics().get(4).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "PERCEPTION_DEC": {
                if (isFirstTime) {
                    character.getParams().getCharacteristics().get(5).setCurrentValue(
                            character.getParams().getCharacteristics().get(5).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }

            // увеличение навыков
            case "HEAVY_WEAPON_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                            character.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "HEAVY_ARMOR_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                            character.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "FARMING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("FARMING").setCurrentValue(
                            character.getParams().getSkills().get("FARMING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "CARPENTRY_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                            character.getParams().getSkills().get("CARPENTRY").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "BLOCKING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("BLOCKING").setCurrentValue(
                            character.getParams().getSkills().get("BLOCKING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "HAND_COMBAT_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                            character.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "BLACKSMITHING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                            character.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "CONSTRUCTION_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                            character.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_WEAPON_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                            character.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_ARMOR_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                            character.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ARMORLESS_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                            character.getParams().getSkills().get("ARMORLESS").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LOCKPICKING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                            character.getParams().getSkills().get("LOCKPICKING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "SPEECH_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("SPEECH").setCurrentValue(
                            character.getParams().getSkills().get("SPEECH").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "TRADE_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("TRADE").setCurrentValue(
                            character.getParams().getSkills().get("TRADE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "COMMAND_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("COMMAND").setCurrentValue(
                            character.getParams().getSkills().get("COMMAND").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LOVE_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LOVE").setCurrentValue(
                            character.getParams().getSkills().get("LOVE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "TRAINING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("TRAINING").setCurrentValue(
                            character.getParams().getSkills().get("TRAINING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "POTIONS_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("POTIONS").setCurrentValue(
                            character.getParams().getSkills().get("POTIONS").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "MEDICINE_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("MEDICINE").setCurrentValue(
                            character.getParams().getSkills().get("MEDICINE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ENGINEERING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                            character.getParams().getSkills().get("ENGINEERING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "MARKSMANSHIP_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                            character.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "PICK_POCKET_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                            character.getParams().getSkills().get("PICK_POCKET").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "SNEAK_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("SNEAK").setCurrentValue(
                            character.getParams().getSkills().get("SNEAK").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ANIMAL_HANDLING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                            character.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "STONE_ART_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("STONE_ART").setCurrentValue(
                            character.getParams().getSkills().get("STONE_ART").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "SEWING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("SEWING").setCurrentValue(
                            character.getParams().getSkills().get("SEWING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "COOKING_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("COOKING").setCurrentValue(
                            character.getParams().getSkills().get("COOKING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "PERFORMANCE_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("PERFORMANCE").setCurrentValue(
                            character.getParams().getSkills().get("PERFORMANCE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ENCHANTMENT_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("ENCHANTMENT").setCurrentValue(
                            character.getParams().getSkills().get("ENCHANTMENT").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "DEDUCTION_INC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("DEDUCTION").setCurrentValue(
                            character.getParams().getSkills().get("DEDUCTION").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }

            // уменьшение навыков
            case "HEAVY_WEAPON_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                            character.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "HEAVY_ARMOR_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                            character.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "FARMING_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("FARMING").setCurrentValue(
                            character.getParams().getSkills().get("FARMING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "CARPENTRY_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                            character.getParams().getSkills().get("CARPENTRY").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "BLOCKING_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("BLOCKING").setCurrentValue(
                            character.getParams().getSkills().get("BLOCKING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "HAND_COMBAT_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                            character.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "BLACKSMITHING_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                            character.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "CONSTRUCTION_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                            character.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_WEAPON_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                            character.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_ARMOR_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                            character.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "ARMORLESS_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                            character.getParams().getSkills().get("ARMORLESS").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LOCKPICKING_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                            character.getParams().getSkills().get("LOCKPICKING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "SPEECH_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("SPEECH").setCurrentValue(
                            character.getParams().getSkills().get("SPEECH").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "TRADE_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("TRADE").setCurrentValue(
                            character.getParams().getSkills().get("TRADE").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "COMMAND_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("COMMAND").setCurrentValue(
                            character.getParams().getSkills().get("COMMAND").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LOVE_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("LOVE").setCurrentValue(
                            character.getParams().getSkills().get("LOVE").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "TRAINING_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("TRAINING").setCurrentValue(
                            character.getParams().getSkills().get("TRAINING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "POTIONS_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("POTIONS").setCurrentValue(
                            character.getParams().getSkills().get("POTIONS").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "MEDICINE_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("MEDICINE").setCurrentValue(
                            character.getParams().getSkills().get("MEDICINE").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "ENGINEERING_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                            character.getParams().getSkills().get("ENGINEERING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "MARKSMANSHIP_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                            character.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "PICK_POCKET_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                            character.getParams().getSkills().get("PICK_POCKET").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "SNEAK_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("SNEAK").setCurrentValue(
                            character.getParams().getSkills().get("SNEAK").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "ANIMAL_HANDLING_DEC": {
                if (isFirstTime) {
                    character.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                            character.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }

            case "EPIPHANY":
            case "LIFE_DETECTION":
            case "BLIND":
            case "PHASING":{
                if (isFirstTime) {
                    MapController.drawCurrentMap();
                }
                break;
            }
        }
    }

    /**
     * Удалить все эффекты, наложенные предметом
     *
     * @param items  - предмет, эффекты которого нужно удалить
     * @param character - персонаж
     */
    public static void removeEffects(Items items, Character character) {
        List<EffectParams> removedEffects = new ArrayList<>();
        if (character.getAppliedEffects() != null) {
            for (EffectParams effect : character.getAppliedEffects()) {
                if (effect.getBaseItem().equals(items)) {
                    removedEffects.add(effect);
                }
            }
            removedEffects.forEach(effect -> removeEffect(character, effect));
            Game.getParams().refreshParamsValueViews(); // перерисовывает параметры персонажа
            Game.getEffectsPanel().refreshEffectsPanel();
        }
    }

    /**
     * Удалить наложенные на персонажа эффекты
     *
     * @param character
     * @param effect
     */
    public static void removeEffect(Character character, EffectParams effect) {
        character.getAppliedEffects().remove(effect);
        switch (effect.getStrId()) {
            // увеличение характеристик
            case "POWER_INC": {
                character.getParams().getCharacteristics().get(0).setCurrentValue(
                        character.getParams().getCharacteristics().get(0).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "STAMINA_INC": {
                character.getParams().getCharacteristics().get(1).setCurrentValue(
                        character.getParams().getCharacteristics().get(1).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "AGILITY_INC": {
                character.getParams().getCharacteristics().get(2).setCurrentValue(
                        character.getParams().getCharacteristics().get(2).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "CHARISMA_INC": {
                character.getParams().getCharacteristics().get(3).setCurrentValue(
                        character.getParams().getCharacteristics().get(3).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "INTELLIGENCE_INC": {
                character.getParams().getCharacteristics().get(4).setCurrentValue(
                        character.getParams().getCharacteristics().get(4).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "PERCEPTION_INC": {
                character.getParams().getCharacteristics().get(5).setCurrentValue(
                        character.getParams().getCharacteristics().get(5).getCurrentValue() - effect.getPower()
                );
                break;
            }
            // уменьшение характеристик
            case "POWER_DEC": {
                character.getParams().getCharacteristics().get(0).setCurrentValue(
                        character.getParams().getCharacteristics().get(0).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "STAMINA_DEC": {
                character.getParams().getCharacteristics().get(1).setCurrentValue(
                        character.getParams().getCharacteristics().get(1).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "AGILITY_DEC": {
                character.getParams().getCharacteristics().get(2).setCurrentValue(
                        character.getParams().getCharacteristics().get(2).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "CHARISMA_DEC": {
                character.getParams().getCharacteristics().get(3).setCurrentValue(
                        character.getParams().getCharacteristics().get(3).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "INTELLIGENCE_DEC": {
                character.getParams().getCharacteristics().get(4).setCurrentValue(
                        character.getParams().getCharacteristics().get(4).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "PERCEPTION_DEC": {
                character.getParams().getCharacteristics().get(5).setCurrentValue(
                        character.getParams().getCharacteristics().get(5).getCurrentValue() + effect.getPower()
                );
                break;
            }

            // увеличение навыков
            case "HEAVY_WEAPON_INC": {
                character.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                        character.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "HEAVY_ARMOR_INC": {
                character.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                        character.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "FARMING_INC": {
                character.getParams().getSkills().get("FARMING").setCurrentValue(
                        character.getParams().getSkills().get("FARMING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "CARPENTRY_INC": {
                character.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                        character.getParams().getSkills().get("CARPENTRY").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "BLOCKING_INC": {
                character.getParams().getSkills().get("BLOCKING").setCurrentValue(
                        character.getParams().getSkills().get("BLOCKING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "HAND_COMBAT_INC": {
                character.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                        character.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "BLACKSMITHING_INC": {
                character.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                        character.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "CONSTRUCTION_INC": {
                character.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                        character.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LIGHT_WEAPON_INC": {
                character.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                        character.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LIGHT_ARMOR_INC": {
                character.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                        character.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ARMORLESS_INC": {
                character.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                        character.getParams().getSkills().get("ARMORLESS").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LOCKPICKING_INC": {
                character.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                        character.getParams().getSkills().get("LOCKPICKING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "SPEECH_INC": {
                character.getParams().getSkills().get("SPEECH").setCurrentValue(
                        character.getParams().getSkills().get("SPEECH").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "TRADE_INC": {
                character.getParams().getSkills().get("TRADE").setCurrentValue(
                        character.getParams().getSkills().get("TRADE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "COMMAND_INC": {
                character.getParams().getSkills().get("COMMAND").setCurrentValue(
                        character.getParams().getSkills().get("COMMAND").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LOVE_INC": {
                character.getParams().getSkills().get("LOVE").setCurrentValue(
                        character.getParams().getSkills().get("LOVE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "TRAINING_INC": {
                character.getParams().getSkills().get("TRAINING").setCurrentValue(
                        character.getParams().getSkills().get("TRAINING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "POTIONS_INC": {
                character.getParams().getSkills().get("POTIONS").setCurrentValue(
                        character.getParams().getSkills().get("POTIONS").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "MEDICINE_INC": {
                character.getParams().getSkills().get("MEDICINE").setCurrentValue(
                        character.getParams().getSkills().get("MEDICINE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ENGINEERING_INC": {
                character.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                        character.getParams().getSkills().get("ENGINEERING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "MARKSMANSHIP_INC": {
                character.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                        character.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "PICK_POCKET_INC": {
                character.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                        character.getParams().getSkills().get("PICK_POCKET").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "SNEAK_INC": {
                character.getParams().getSkills().get("SNEAK").setCurrentValue(
                        character.getParams().getSkills().get("SNEAK").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ANIMAL_HANDLING_INC": {
                character.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                        character.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "STONE_ART_INC": {
                character.getParams().getSkills().get("STONE_ART").setCurrentValue(
                        character.getParams().getSkills().get("STONE_ART").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "SEWING_INC": {
                character.getParams().getSkills().get("SEWING").setCurrentValue(
                        character.getParams().getSkills().get("SEWING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "COOKING_INC": {
                character.getParams().getSkills().get("COOKING").setCurrentValue(
                        character.getParams().getSkills().get("COOKING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "PERFORMANCE_INC": {
                character.getParams().getSkills().get("PERFORMANCE").setCurrentValue(
                        character.getParams().getSkills().get("PERFORMANCE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ENCHANTMENT_INC": {
                character.getParams().getSkills().get("ENCHANTMENT").setCurrentValue(
                        character.getParams().getSkills().get("ENCHANTMENT").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "DEDUCTION_INC": {
                character.getParams().getSkills().get("DEDUCTION").setCurrentValue(
                        character.getParams().getSkills().get("DEDUCTION").getCurrentValue() - effect.getPower()
                );
                break;
            }

            // уменьшение навыков
            case "HEAVY_WEAPON_DEC": {
                character.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                        character.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "HEAVY_ARMOR_DEC": {
                character.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                        character.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "FARMING_DEC": {
                character.getParams().getSkills().get("FARMING").setCurrentValue(
                        character.getParams().getSkills().get("FARMING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "CARPENTRY_DEC": {
                character.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                        character.getParams().getSkills().get("CARPENTRY").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "BLOCKING_DEC": {
                character.getParams().getSkills().get("BLOCKING").setCurrentValue(
                        character.getParams().getSkills().get("BLOCKING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "HAND_COMBAT_DEC": {
                character.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                        character.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "BLACKSMITHING_DEC": {
                character.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                        character.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "CONSTRUCTION_DEC": {
                character.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                        character.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LIGHT_WEAPON_DEC": {
                character.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                        character.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LIGHT_ARMOR_DEC": {
                character.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                        character.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "ARMORLESS_DEC": {
                character.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                        character.getParams().getSkills().get("ARMORLESS").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LOCKPICKING_DEC": {
                character.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                        character.getParams().getSkills().get("LOCKPICKING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "SPEECH_DEC": {
                character.getParams().getSkills().get("SPEECH").setCurrentValue(
                        character.getParams().getSkills().get("SPEECH").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "TRADE_DEC": {
                character.getParams().getSkills().get("TRADE").setCurrentValue(
                        character.getParams().getSkills().get("TRADE").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "COMMAND_DEC": {
                character.getParams().getSkills().get("COMMAND").setCurrentValue(
                        character.getParams().getSkills().get("COMMAND").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LOVE_DEC": {
                character.getParams().getSkills().get("LOVE").setCurrentValue(
                        character.getParams().getSkills().get("LOVE").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "TRAINING_DEC": {
                character.getParams().getSkills().get("TRAINING").setCurrentValue(
                        character.getParams().getSkills().get("TRAINING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "POTIONS_DEC": {
                character.getParams().getSkills().get("POTIONS").setCurrentValue(
                        character.getParams().getSkills().get("POTIONS").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "MEDICINE_DEC": {
                character.getParams().getSkills().get("MEDICINE").setCurrentValue(
                        character.getParams().getSkills().get("MEDICINE").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "ENGINEERING_DEC": {
                character.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                        character.getParams().getSkills().get("ENGINEERING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "MARKSMANSHIP_DEC": {
                character.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                        character.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "PICK_POCKET_DEC": {
                character.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                        character.getParams().getSkills().get("PICK_POCKET").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "SNEAK_DEC": {
                character.getParams().getSkills().get("SNEAK").setCurrentValue(
                        character.getParams().getSkills().get("SNEAK").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "ANIMAL_HANDLING_DEC": {
                character.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                        character.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "EPIPHANY":
            case "LIFE_DETECTION":
            case "BLIND": {
                MapController.drawCurrentMap();
                break;
            }
        }
    }

    /**
     * Распределяем дополнительные эффекты предметов по ингредиентам
     *
     * @param additionalEffect - мапа, хранящая пары <id ингредиента, доп. эффект>
     */
    public static void putAdditionalEffects(Map<Integer, EffectParams> additionalEffect) {
        int i = 0;
        List<EffectParams> additionalEffects = EffectsController.getAdditionalEffects();
        for (ItemInfo itemInfo : Editor.getItems()) {
            if (itemInfo.getTypes() != null && itemInfo.getTypes().contains(ItemTypeEnum.INGREDIENT) && itemInfo.getBaseEffects() != null) {
                while (itemInfo.getBaseEffects().stream()
                        .map(EffectParams::getStrId)
                        .collect(Collectors.toList()).contains(additionalEffects.get(i).getStrId())) {
                    i = (i + 1) % additionalEffects.size();
                }
                additionalEffect.put(itemInfo.getId(), additionalEffects.get(i));
                i = (i + 1) % additionalEffects.size();
            }
        }
    }
}
