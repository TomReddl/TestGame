package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import model.editor.items.*;
import model.entity.InlayerSizeEnum;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectParams;
import model.entity.map.Items;
import view.Game;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

/**
 * Панель детальной информации о предмете
 */
public class ItemDetailPanel {
    @Getter
    private static final Pane pane;
    @Getter
    private static final Label descLabel;
    @Getter
    @Setter
    private static Items selectItem;

    static {
        pane = new Pane();
        pane.setPrefSize(300, 300);
        pane.setLayoutX(765);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setStyle("-fx-border-color:gray;");
        pane.setVisible(false);

        descLabel = new Label();
        descLabel.setLayoutX(5);
        descLabel.setLayoutY(5);
        descLabel.setMaxWidth(290);
        descLabel.setWrapText(true);
        pane.getChildren().add(descLabel);

        Game.getRoot().getChildren().add(pane);
    }

    public static void showDetailPanel(Items item, double y) {
        selectItem = item;
        List<Items> containerInventory = Game.getInventory().getItems();
        pane.setLayoutX(containerInventory.contains(selectItem) ? 765 : 490);
        pane.setLayoutY(y + 65);
        pane.setVisible(true);
        descLabel.setText(item.getName());
        if (item.getInfo() instanceof WeaponInfo) {
            descLabel.setText(item.getInfo().getDesc());
            var weaponInfo = (WeaponInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(Game.getText("DAMAGE"), weaponInfo.getDamage()) + "\n" +
                    String.format(Game.getText("STRENGTH"), item.getCurrentStrength(), weaponInfo.getMaxStrength()) + "\n" +
                    (weaponInfo.getOneHand() ? Game.getText("ONE_HAND") : Game.getText("TWO_HAND")) + "\n" +
                    Game.getText(weaponInfo.getSkill() + "_PARAM_NAME"));
            addEffectsText(item.getEffects());
            addInlayerText(item);
        } else if (item.getInfo() instanceof ClothesInfo) {
            var clothesInfo = (ClothesInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(Game.getText("ARMOR"), clothesInfo.getArmor()) + "\n" +
                    String.format(Game.getText("COVERING"), clothesInfo.getCovering()) + "\n" +

                    ((clothesInfo.getBodyPart().equals(BodyPartEnum.BACKPACK.name()) ||
                            clothesInfo.getBodyPart().equals(BodyPartEnum.BELT.name())) ?
                            Game.getText("ADD_VOLUME") + " " +
                                    BigDecimal.valueOf(Long.parseLong(clothesInfo.getParams().get("addVolume"))).
                                            divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP)
                            : "") + "\n" +

                    String.format(Game.getText("STRENGTH"), item.getCurrentStrength(), clothesInfo.getMaxStrength()) + "\n" +
                    Game.getText(clothesInfo.getSkill() + "_PARAM_NAME") + "\n" + "\n" +

                    String.format(Game.getText("STYLE"), ClothesStyleEnum.valueOf(clothesInfo.getStyle()).getDesc()) + "\n" +
                    ClothesGenderEnum.valueOf(clothesInfo.getGender()).getDesc());
            addEffectsText(item.getEffects());
            addInlayerText(item);
        } else if (item.getInfo() instanceof EdibleInfo) {
            var edibleInfo = (EdibleInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(Game.getText("TASTE"), edibleInfo.getTaste()) + "\n" +
                    String.format(Game.getText("HUNGER_RESTORE"), edibleInfo.getHunger()) + "\n" +
                    String.format(Game.getText("THIRST_RESTORE"), edibleInfo.getThirst()) + "\n" + "\n");

            descLabel.setText(descLabel.getText() + Game.getText("EFFECTS") + "\n");
            boolean emptyShowEffects = true;
            if (item.getEffects() != null) {
                for (EffectParams effect : item.getEffects()) {
                    boolean showEffectInfo = true;
                    if (item.getInfo().getTypes().contains(ItemTypeEnum.INGREDIENT)) {
                        List<String> effects = Game.getMap().getSelecterCharacter().getKnowledgeInfo().getKnowEffects().get(item.getTypeId());
                        if (effects == null || !effects.contains(effect.getStrId())) {
                            showEffectInfo = false; // для ингредиентов не показывает эффекты, которые персонаж еще не открыл
                        }
                    }
                    if (showEffectInfo) {
                        emptyShowEffects = false;
                        descLabel.setText(descLabel.getText() + Game.getEffectText(effect.getStrId()) + " " +
                                (effect.getPower() != null ? effect.getPower() + Game.getText("UNITS") + " " : "") +
                                (effect.getDurability() != null ? (Game.getText("ON") + " " + effect.getDurability() + " " + Game.getText("TURNS")) : "")
                                + "\n");
                    }
                }
            }
            if (emptyShowEffects) {
                descLabel.setText(descLabel.getText() + Game.getText("UNKNOWN_EFFECTS"));
            }
        } else if (item.getInfo().getTypes().contains(ItemTypeEnum.TOOL)) {
            if (item.getInfo().getTypes().contains(ItemTypeEnum.WATERING_CAN)) {
                descLabel.setText(descLabel.getText() + "\n\n" +
                        String.format(
                                Game.getText("CAPACITY"),
                                item.getParams().get("currentCapacity"),
                                item.getParams().get("capacity")));
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.PICKLOCK) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.SAPPER_TOOL) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.BUILDING_TOOL)) {
                var toolSkill = "";
                if (item.getInfo().getTypes().contains(ItemTypeEnum.PICKLOCK) ||
                        item.getInfo().getTypes().contains(ItemTypeEnum.SAPPER_TOOL)) {
                    toolSkill = Game.getText("LOCKPICKING_PARAM_NAME");
                } else if (item.getInfo().getTypes().contains(ItemTypeEnum.BUILDING_TOOL)) {
                    toolSkill = Game.getText("CONSTRUCTION_PARAM_NAME");
                }
                descLabel.setText(descLabel.getText() + "\n\n" +
                        String.format(
                                Game.getText("CHARGES"),
                                item.getCurrentStrength(),
                                item.getInfo().getMaxStrength(),
                                item.getInfo().getParams().get("skillBonus"),
                                toolSkill));
            }
        } else if (item.getInfo().getTypes().contains(ItemTypeEnum.ENCHANTMENT)) {
            addEffectsText(item.getEffects());
            addInlayerText(item);
        }
    }

    /**
     * Добавить описание эффектов
     *
     * @param effectsList
     */
    private static void addEffectsText(List<EffectParams> effectsList) {
        if (effectsList != null && effectsList.size() > 0) {
            descLabel.setText(descLabel.getText() + "\n\n" + Game.getText("EFFECTS") + "\n");
            Iterator<EffectParams> iterator = effectsList.iterator();
            while (iterator.hasNext()) {
                EffectParams effect = iterator.next();
                descLabel.setText(descLabel.getText() + Game.getEffectText(effect.getStrId()) + " " +
                        ((effect.getPower() != null ? effect.getPower() + " " + Game.getText("UNITS") : "") + " ") +
                        ((effect.getDurability() != null && effect.getDurability() > 0) ?
                                (Game.getText("ON") + " " + effect.getDurability() + " " + Game.getText("TURNS")) :
                                ""));
                if (iterator.hasNext()) {
                    descLabel.setText(descLabel.getText() + "\n");
                }
            }
        }
    }

    /**
     * Добавить описание размера инкрустата (или слота для инкрустата)
     *
     * @param item
     */
    private static void addInlayerText(Items item) {
        if (item.getInlayerId() == null || item.getInlayerId() == 0) {
            String inlayerSize = "нет";
            if (item.getParams() != null && item.getParams().get("inlayerSize") != null) {
                inlayerSize = InlayerSizeEnum.valueOf(item.getParams().get("inlayerSize")).getDesc();
            }
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(
                            item.getInfo().getTypes().contains(ItemTypeEnum.ENCHANTMENT) ? Game.getText("INLAYER_SIZE") :
                                    Game.getText("INLAYER_SLOT_SIZE"), inlayerSize));
        }
    }

    public static void hideDetailPanel() {
        selectItem = null;
        pane.setVisible(false);
    }
}
