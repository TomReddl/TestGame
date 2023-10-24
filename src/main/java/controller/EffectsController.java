package controller;

import javafx.scene.image.Image;
import lombok.Getter;
import model.editor.items.ItemInfo;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectInfo;
import model.entity.effects.EffectParams;
import model.entity.effects.EffectTypeEnum;
import model.entity.map.Items;
import model.entity.player.Player;
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

        effects.put("INSECT_DAMAGE_ADD", new EffectInfo(Game.getEffectText("INSECT_DAMAGE_ADD"), "GREEN"));
        effects.put("REPTILE_DAMAGE_ADD", new EffectInfo(Game.getEffectText("REPTILE_DAMAGE_ADD"), "GREEN"));
        effects.put("MOLD_DAMAGE_ADD", new EffectInfo(Game.getEffectText("MOLD_DAMAGE_ADD"), "GREEN"));
        effects.put("HUMANOID_DAMAGE_ADD", new EffectInfo(Game.getEffectText("HUMANOID_DAMAGE_ADD"), "GREEN"));
        effects.put("ABERRATION_DAMAGE_ADD", new EffectInfo(Game.getEffectText("ABERRATION_DAMAGE_ADD"), "GREEN"));

        effects.put("WATER_WALK", new EffectInfo(Game.getEffectText("WATER_WALK"), "BLUE"));
        effects.put("MOLD_STRIKE", new EffectInfo(Game.getEffectText("MOLD_STRIKE"), "GREEN"));

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
     * @param player - персонаж
     */
    public static void applyEffects(Items items, Player player) {
        List<EffectParams> effectParams = items.getEffects();
        if (effectParams != null) {
            player.getAppliedEffects().addAll(effectParams);
            for (EffectParams effect : effectParams) {
                effect.setBaseItem(items);
                setEffectType(effect, items);
                executeEffect(player, effect, true);
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
     * @param player- персонаж
     */
    public static void executeEffects(Player player) {
        List<EffectParams> removedEffects = new ArrayList<>();
        for (EffectParams effect : player.getAppliedEffects()) {
            if (effect.getBaseItem().getInfo().getTypes().contains(ItemTypeEnum.POTION)) {
                if (effect.getDurability() < 0) {
                    removedEffects.add(effect);
                    Game.getParams().refreshParamsValueViews(); // перерисовывает параметры персонажа
                    Game.getEffectsPanel().refreshEffectsPanel();
                } else {
                    executeEffect(player, effect, false);
                    effect.setDurability(effect.getDurability() - 1);
                }
            }
        }
        removedEffects.forEach(effect -> removeEffect(player, effect));
    }

    /**
     * Выполнить действие эффекта
     *
     * @param player      - персонаж
     * @param effect      - эффект
     * @param isFirstTime - действие выполняется первый раз. Для многих эффектов не нужно ничего делать после первого применения
     */
    private static void executeEffect(Player player, EffectParams effect, boolean isFirstTime) {
        switch (effect.getStrId()) {
            case "HEALTH_RESTORE": {
                player.getParams().getIndicators().get(0).setCurrentValue(
                        player.getParams().getIndicators().get(0).getCurrentValue() + effect.getPower()
                );
                break;
            }
            // увеличение характеристик
            case "POWER_INC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(0).setCurrentValue(
                            player.getParams().getCharacteristics().get(0).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "STAMINA_INC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(1).setCurrentValue(
                            player.getParams().getCharacteristics().get(1).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "AGILITY_INC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(2).setCurrentValue(
                            player.getParams().getCharacteristics().get(2).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "CHARISMA_INC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(3).setCurrentValue(
                            player.getParams().getCharacteristics().get(3).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "INTELLIGENCE_INC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(4).setCurrentValue(
                            player.getParams().getCharacteristics().get(4).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "PERCEPTION_INC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(5).setCurrentValue(
                            player.getParams().getCharacteristics().get(5).getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            // уменьшение характеристик
            case "POWER_DEC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(0).setCurrentValue(
                            player.getParams().getCharacteristics().get(0).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "STAMINA_DEC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(1).setCurrentValue(
                            player.getParams().getCharacteristics().get(1).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "AGILITY_DEC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(2).setCurrentValue(
                            player.getParams().getCharacteristics().get(2).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "CHARISMA_DEC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(3).setCurrentValue(
                            player.getParams().getCharacteristics().get(3).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "INTELLIGENCE_DEC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(4).setCurrentValue(
                            player.getParams().getCharacteristics().get(4).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "PERCEPTION_DEC": {
                if (isFirstTime) {
                    player.getParams().getCharacteristics().get(5).setCurrentValue(
                            player.getParams().getCharacteristics().get(5).getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }

            // увеличение навыков
            case "HEAVY_WEAPON_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                            player.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "HEAVY_ARMOR_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                            player.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "FARMING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("FARMING").setCurrentValue(
                            player.getParams().getSkills().get("FARMING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "CARPENTRY_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                            player.getParams().getSkills().get("CARPENTRY").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "BLOCKING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("BLOCKING").setCurrentValue(
                            player.getParams().getSkills().get("BLOCKING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "HAND_COMBAT_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                            player.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "BLACKSMITHING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                            player.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "CONSTRUCTION_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                            player.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_WEAPON_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                            player.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_ARMOR_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                            player.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ARMORLESS_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                            player.getParams().getSkills().get("ARMORLESS").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LOCKPICKING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                            player.getParams().getSkills().get("LOCKPICKING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "SPEECH_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("SPEECH").setCurrentValue(
                            player.getParams().getSkills().get("SPEECH").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "TRADE_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("TRADE").setCurrentValue(
                            player.getParams().getSkills().get("TRADE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "COMMAND_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("COMMAND").setCurrentValue(
                            player.getParams().getSkills().get("COMMAND").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "LOVE_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LOVE").setCurrentValue(
                            player.getParams().getSkills().get("LOVE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "TRAINING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("TRAINING").setCurrentValue(
                            player.getParams().getSkills().get("TRAINING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "POTIONS_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("POTIONS").setCurrentValue(
                            player.getParams().getSkills().get("POTIONS").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "MEDICINE_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("MEDICINE").setCurrentValue(
                            player.getParams().getSkills().get("MEDICINE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ENGINEERING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                            player.getParams().getSkills().get("ENGINEERING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "MARKSMANSHIP_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                            player.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "PICK_POCKET_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                            player.getParams().getSkills().get("PICK_POCKET").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "SNEAK_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("SNEAK").setCurrentValue(
                            player.getParams().getSkills().get("SNEAK").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ANIMAL_HANDLING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                            player.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "STONE_ART_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("STONE_ART").setCurrentValue(
                            player.getParams().getSkills().get("STONE_ART").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "SEWING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("SEWING").setCurrentValue(
                            player.getParams().getSkills().get("SEWING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "COOKING_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("COOKING").setCurrentValue(
                            player.getParams().getSkills().get("COOKING").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "PERFORMANCE_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("PERFORMANCE").setCurrentValue(
                            player.getParams().getSkills().get("PERFORMANCE").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "ENCHANTMENT_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("ENCHANTMENT").setCurrentValue(
                            player.getParams().getSkills().get("ENCHANTMENT").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }
            case "DEDUCTION_INC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("DEDUCTION").setCurrentValue(
                            player.getParams().getSkills().get("DEDUCTION").getCurrentValue() + effect.getPower()
                    );
                }
                break;
            }

            // уменьшение навыков
            case "HEAVY_WEAPON_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                            player.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "HEAVY_ARMOR_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                            player.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "FARMING_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("FARMING").setCurrentValue(
                            player.getParams().getSkills().get("FARMING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "CARPENTRY_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                            player.getParams().getSkills().get("CARPENTRY").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "BLOCKING_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("BLOCKING").setCurrentValue(
                            player.getParams().getSkills().get("BLOCKING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "HAND_COMBAT_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                            player.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "BLACKSMITHING_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                            player.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "CONSTRUCTION_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                            player.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_WEAPON_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                            player.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LIGHT_ARMOR_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                            player.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "ARMORLESS_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                            player.getParams().getSkills().get("ARMORLESS").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LOCKPICKING_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                            player.getParams().getSkills().get("LOCKPICKING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "SPEECH_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("SPEECH").setCurrentValue(
                            player.getParams().getSkills().get("SPEECH").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "TRADE_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("TRADE").setCurrentValue(
                            player.getParams().getSkills().get("TRADE").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "COMMAND_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("COMMAND").setCurrentValue(
                            player.getParams().getSkills().get("COMMAND").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "LOVE_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("LOVE").setCurrentValue(
                            player.getParams().getSkills().get("LOVE").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "TRAINING_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("TRAINING").setCurrentValue(
                            player.getParams().getSkills().get("TRAINING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "POTIONS_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("POTIONS").setCurrentValue(
                            player.getParams().getSkills().get("POTIONS").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "MEDICINE_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("MEDICINE").setCurrentValue(
                            player.getParams().getSkills().get("MEDICINE").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "ENGINEERING_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                            player.getParams().getSkills().get("ENGINEERING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "MARKSMANSHIP_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                            player.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "PICK_POCKET_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                            player.getParams().getSkills().get("PICK_POCKET").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "SNEAK_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("SNEAK").setCurrentValue(
                            player.getParams().getSkills().get("SNEAK").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }
            case "ANIMAL_HANDLING_DEC": {
                if (isFirstTime) {
                    player.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                            player.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() - effect.getPower()
                    );
                }
                break;
            }

            case "EPIPHANY":
            case "BLIND": {
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
     * @param player - персонаж
     */
    public static void removeEffects(Items items, Player player) {
        List<EffectParams> removedEffects = new ArrayList<>();
        for (EffectParams effect : player.getAppliedEffects()) {
            if (effect.getBaseItem().equals(items)) {
                removedEffects.add(effect);
            }
        }
        removedEffects.forEach(effect -> removeEffect(player, effect));
        Game.getParams().refreshParamsValueViews(); // перерисовывает параметры персонажа
        Game.getEffectsPanel().refreshEffectsPanel();
    }

    /**
     * Удалить наложенные на персонажа эффекты
     *
     * @param player
     * @param effect
     */
    public static void removeEffect(Player player, EffectParams effect) {
        player.getAppliedEffects().remove(effect);
        switch (effect.getStrId()) {
            // увеличение характеристик
            case "POWER_INC": {
                player.getParams().getCharacteristics().get(0).setCurrentValue(
                        player.getParams().getCharacteristics().get(0).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "STAMINA_INC": {
                player.getParams().getCharacteristics().get(1).setCurrentValue(
                        player.getParams().getCharacteristics().get(1).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "AGILITY_INC": {
                player.getParams().getCharacteristics().get(2).setCurrentValue(
                        player.getParams().getCharacteristics().get(2).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "CHARISMA_INC": {
                player.getParams().getCharacteristics().get(3).setCurrentValue(
                        player.getParams().getCharacteristics().get(3).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "INTELLIGENCE_INC": {
                player.getParams().getCharacteristics().get(4).setCurrentValue(
                        player.getParams().getCharacteristics().get(4).getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "PERCEPTION_INC": {
                player.getParams().getCharacteristics().get(5).setCurrentValue(
                        player.getParams().getCharacteristics().get(5).getCurrentValue() - effect.getPower()
                );
                break;
            }
            // уменьшение характеристик
            case "POWER_DEC": {
                player.getParams().getCharacteristics().get(0).setCurrentValue(
                        player.getParams().getCharacteristics().get(0).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "STAMINA_DEC": {
                player.getParams().getCharacteristics().get(1).setCurrentValue(
                        player.getParams().getCharacteristics().get(1).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "AGILITY_DEC": {
                player.getParams().getCharacteristics().get(2).setCurrentValue(
                        player.getParams().getCharacteristics().get(2).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "CHARISMA_DEC": {
                player.getParams().getCharacteristics().get(3).setCurrentValue(
                        player.getParams().getCharacteristics().get(3).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "INTELLIGENCE_DEC": {
                player.getParams().getCharacteristics().get(4).setCurrentValue(
                        player.getParams().getCharacteristics().get(4).getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "PERCEPTION_DEC": {
                player.getParams().getCharacteristics().get(5).setCurrentValue(
                        player.getParams().getCharacteristics().get(5).getCurrentValue() + effect.getPower()
                );
                break;
            }

            // увеличение навыков
            case "HEAVY_WEAPON_INC": {
                player.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                        player.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "HEAVY_ARMOR_INC": {
                player.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                        player.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "FARMING_INC": {
                player.getParams().getSkills().get("FARMING").setCurrentValue(
                        player.getParams().getSkills().get("FARMING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "CARPENTRY_INC": {
                player.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                        player.getParams().getSkills().get("CARPENTRY").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "BLOCKING_INC": {
                player.getParams().getSkills().get("BLOCKING").setCurrentValue(
                        player.getParams().getSkills().get("BLOCKING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "HAND_COMBAT_INC": {
                player.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                        player.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "BLACKSMITHING_INC": {
                player.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                        player.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "CONSTRUCTION_INC": {
                player.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                        player.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LIGHT_WEAPON_INC": {
                player.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                        player.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LIGHT_ARMOR_INC": {
                player.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                        player.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ARMORLESS_INC": {
                player.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                        player.getParams().getSkills().get("ARMORLESS").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LOCKPICKING_INC": {
                player.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                        player.getParams().getSkills().get("LOCKPICKING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "SPEECH_INC": {
                player.getParams().getSkills().get("SPEECH").setCurrentValue(
                        player.getParams().getSkills().get("SPEECH").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "TRADE_INC": {
                player.getParams().getSkills().get("TRADE").setCurrentValue(
                        player.getParams().getSkills().get("TRADE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "COMMAND_INC": {
                player.getParams().getSkills().get("COMMAND").setCurrentValue(
                        player.getParams().getSkills().get("COMMAND").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "LOVE_INC": {
                player.getParams().getSkills().get("LOVE").setCurrentValue(
                        player.getParams().getSkills().get("LOVE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "TRAINING_INC": {
                player.getParams().getSkills().get("TRAINING").setCurrentValue(
                        player.getParams().getSkills().get("TRAINING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "POTIONS_INC": {
                player.getParams().getSkills().get("POTIONS").setCurrentValue(
                        player.getParams().getSkills().get("POTIONS").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "MEDICINE_INC": {
                player.getParams().getSkills().get("MEDICINE").setCurrentValue(
                        player.getParams().getSkills().get("MEDICINE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ENGINEERING_INC": {
                player.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                        player.getParams().getSkills().get("ENGINEERING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "MARKSMANSHIP_INC": {
                player.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                        player.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "PICK_POCKET_INC": {
                player.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                        player.getParams().getSkills().get("PICK_POCKET").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "SNEAK_INC": {
                player.getParams().getSkills().get("SNEAK").setCurrentValue(
                        player.getParams().getSkills().get("SNEAK").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ANIMAL_HANDLING_INC": {
                player.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                        player.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "STONE_ART_INC": {
                player.getParams().getSkills().get("STONE_ART").setCurrentValue(
                        player.getParams().getSkills().get("STONE_ART").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "SEWING_INC": {
                player.getParams().getSkills().get("SEWING").setCurrentValue(
                        player.getParams().getSkills().get("SEWING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "COOKING_INC": {
                player.getParams().getSkills().get("COOKING").setCurrentValue(
                        player.getParams().getSkills().get("COOKING").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "PERFORMANCE_INC": {
                player.getParams().getSkills().get("PERFORMANCE").setCurrentValue(
                        player.getParams().getSkills().get("PERFORMANCE").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "ENCHANTMENT_INC": {
                player.getParams().getSkills().get("ENCHANTMENT").setCurrentValue(
                        player.getParams().getSkills().get("ENCHANTMENT").getCurrentValue() - effect.getPower()
                );
                break;
            }
            case "DEDUCTION_INC": {
                player.getParams().getSkills().get("DEDUCTION").setCurrentValue(
                        player.getParams().getSkills().get("DEDUCTION").getCurrentValue() - effect.getPower()
                );
                break;
            }

            // уменьшение навыков
            case "HEAVY_WEAPON_DEC": {
                player.getParams().getSkills().get("HEAVY_WEAPON").setCurrentValue(
                        player.getParams().getSkills().get("HEAVY_WEAPON").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "HEAVY_ARMOR_DEC": {
                player.getParams().getSkills().get("HEAVY_ARMOR").setCurrentValue(
                        player.getParams().getSkills().get("HEAVY_ARMOR").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "FARMING_DEC": {
                player.getParams().getSkills().get("FARMING").setCurrentValue(
                        player.getParams().getSkills().get("FARMING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "CARPENTRY_DEC": {
                player.getParams().getSkills().get("CARPENTRY").setCurrentValue(
                        player.getParams().getSkills().get("CARPENTRY").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "BLOCKING_DEC": {
                player.getParams().getSkills().get("BLOCKING").setCurrentValue(
                        player.getParams().getSkills().get("BLOCKING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "HAND_COMBAT_DEC": {
                player.getParams().getSkills().get("HAND_COMBAT").setCurrentValue(
                        player.getParams().getSkills().get("HAND_COMBAT").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "BLACKSMITHING_DEC": {
                player.getParams().getSkills().get("BLACKSMITHING").setCurrentValue(
                        player.getParams().getSkills().get("BLACKSMITHING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "CONSTRUCTION_DEC": {
                player.getParams().getSkills().get("CONSTRUCTION").setCurrentValue(
                        player.getParams().getSkills().get("CONSTRUCTION").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LIGHT_WEAPON_DEC": {
                player.getParams().getSkills().get("LIGHT_WEAPON").setCurrentValue(
                        player.getParams().getSkills().get("LIGHT_WEAPON").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LIGHT_ARMOR_DEC": {
                player.getParams().getSkills().get("LIGHT_ARMOR").setCurrentValue(
                        player.getParams().getSkills().get("LIGHT_ARMOR").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "ARMORLESS_DEC": {
                player.getParams().getSkills().get("ARMORLESS").setCurrentValue(
                        player.getParams().getSkills().get("ARMORLESS").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LOCKPICKING_DEC": {
                player.getParams().getSkills().get("LOCKPICKING").setCurrentValue(
                        player.getParams().getSkills().get("LOCKPICKING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "SPEECH_DEC": {
                player.getParams().getSkills().get("SPEECH").setCurrentValue(
                        player.getParams().getSkills().get("SPEECH").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "TRADE_DEC": {
                player.getParams().getSkills().get("TRADE").setCurrentValue(
                        player.getParams().getSkills().get("TRADE").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "COMMAND_DEC": {
                player.getParams().getSkills().get("COMMAND").setCurrentValue(
                        player.getParams().getSkills().get("COMMAND").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "LOVE_DEC": {
                player.getParams().getSkills().get("LOVE").setCurrentValue(
                        player.getParams().getSkills().get("LOVE").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "TRAINING_DEC": {
                player.getParams().getSkills().get("TRAINING").setCurrentValue(
                        player.getParams().getSkills().get("TRAINING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "POTIONS_DEC": {
                player.getParams().getSkills().get("POTIONS").setCurrentValue(
                        player.getParams().getSkills().get("POTIONS").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "MEDICINE_DEC": {
                player.getParams().getSkills().get("MEDICINE").setCurrentValue(
                        player.getParams().getSkills().get("MEDICINE").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "ENGINEERING_DEC": {
                player.getParams().getSkills().get("ENGINEERING").setCurrentValue(
                        player.getParams().getSkills().get("ENGINEERING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "MARKSMANSHIP_DEC": {
                player.getParams().getSkills().get("MARKSMANSHIP").setCurrentValue(
                        player.getParams().getSkills().get("MARKSMANSHIP").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "PICK_POCKET_DEC": {
                player.getParams().getSkills().get("PICK_POCKET").setCurrentValue(
                        player.getParams().getSkills().get("PICK_POCKET").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "SNEAK_DEC": {
                player.getParams().getSkills().get("SNEAK").setCurrentValue(
                        player.getParams().getSkills().get("SNEAK").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "ANIMAL_HANDLING_DEC": {
                player.getParams().getSkills().get("ANIMAL_HANDLING").setCurrentValue(
                        player.getParams().getSkills().get("ANIMAL_HANDLING").getCurrentValue() + effect.getPower()
                );
                break;
            }
            case "EPIPHANY":
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
