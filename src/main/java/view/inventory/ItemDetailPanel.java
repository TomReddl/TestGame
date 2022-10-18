package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import model.editor.items.*;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectParams;
import model.entity.map.Items;
import view.Game;
import view.params.ParamPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
 * Панель детальной информации о предмете
 */
public class ItemDetailPanel {
    @Getter
    private static final Pane pane;
    @Getter
    private static final Label descLabel;
    @Getter
    private static Items selectItem;

    static {
        pane = new Pane();
        pane.setPrefSize(300, 300);
        pane.setLayoutX(765);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
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
        descLabel.setText(item.getInfo().getDesc());
        if (item.getInfo() instanceof WeaponInfo) {
            var weaponInfo = (WeaponInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(Game.getText("DAMAGE"), weaponInfo.getDamage()) + "\n" +
                    String.format(Game.getText("STRENGTH"),item.getCurrentStrength(), weaponInfo.getMaxStrength()) + "\n" +
                    (weaponInfo.getOneHand() ? Game.getText("ONE_HAND") : Game.getText("TWO_HAND")) + "\n" +
                    Game.getText(ParamPanel.getSkillsNames().get(weaponInfo.getSkill()) + "_PARAM_NAME"));
            if (weaponInfo.getEffects() != null) {
                for (EffectParams effect : weaponInfo.getEffects()) {
                    descLabel.setText(descLabel.getText() + Game.getEffectText(effect.getStrId()) + " " +
                            effect.getPower() + Game.getText("UNITS") + " " +
                            (effect.getDurability() > 0 ? (Game.getText("ON") + " " + effect.getDurability() + " " + Game.getText("TURNS")) : ""));
                }
            }
        } else if (item.getInfo() instanceof ClothesInfo) {
            var clothesInfo = (ClothesInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(Game.getText("ARMOR"), clothesInfo.getArmor()) + "\n" +
                    String.format(Game.getText("COVERING"), clothesInfo.getCovering()) + "\n" +

                    ((clothesInfo.getBodyPart().equals(BodyPartEnum.BACKPACK.name()) ||
                            clothesInfo.getBodyPart().equals(BodyPartEnum.BELT.name()))?
                            Game.getText("ADD_VOLUME") + " " +
                                    BigDecimal.valueOf(Long.parseLong(clothesInfo.getParams().get(0))).
                                            divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP)
                            : "") + "\n" +

                    String.format(Game.getText("STRENGTH"), item.getCurrentStrength(), clothesInfo.getMaxStrength()) + "\n" +
                    Game.getText(ParamPanel.getSkillsNames().get(clothesInfo.getSkill()) + "_PARAM_NAME") + "\n" + "\n" +

                    String.format(Game.getText("STYLE"), ClothesStyleEnum.valueOf(clothesInfo.getStyle()).getDesc()) + "\n" +
                    ClothesGenderEnum.valueOf(clothesInfo.getGender()).getDesc());
            if (clothesInfo.getEffects() != null) {
                for (EffectParams effect : clothesInfo.getEffects()) {
                    descLabel.setText(descLabel.getText() + Game.getEffectText(effect.getStrId()) + " " +
                            effect.getPower() + Game.getText("UNITS") + " " +
                            (effect.getDurability() > 0 ? (Game.getText("ON") + " " + effect.getDurability() + " " + Game.getText("TURNS")) : ""));
                }
            }
        } else if (item.getInfo() instanceof EdibleInfo) {
            var edibleInfo = (EdibleInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(Game.getText("TASTE"), edibleInfo.getTaste()) + "\n" +
                    String.format(Game.getText("HUNGER_RESTORE"), edibleInfo.getHunger()) + "\n" +
                            String.format(Game.getText("THIRST_RESTORE"), edibleInfo.getThirst()) + "\n" + "\n");
            if (edibleInfo.getEffects() != null) {
                for (EffectParams effect : edibleInfo.getEffects()) {
                    descLabel.setText(descLabel.getText() + Game.getEffectText(effect.getStrId()) + " " +
                            effect.getPower() + Game.getText("UNITS") + " " +
                            (effect.getDurability() > 0 ? (Game.getText("ON") + " " + effect.getDurability() + " " + Game.getText("TURNS")) : ""));
                }
            }
        } else if (item.getInfo().getTypes().contains(ItemTypeEnum.TOOL)) {
            var toolSkill = "";
            if (item.getInfo().getTypes().contains(ItemTypeEnum.PICKLOCK) ||
                    item.getInfo().getTypes().contains(ItemTypeEnum.SAPPER_TOOL)) {
                toolSkill = Game.getText("BREAKING_LOCKS_PARAM_NAME");
            } else if (item.getInfo().getTypes().contains(ItemTypeEnum.BUILDING_TOOL)) {
                toolSkill = Game.getText("CONSTRUCTION_PARAM_NAME");
            }
            descLabel.setText(descLabel.getText() + "\n\n" +
                    String.format(
                            Game.getText("CHARGES"),
                            item.getCurrentStrength(),
                            item.getInfo().getMaxStrength(),
                            item.getInfo().getParams().get(0),
                            toolSkill));
        }
        pane.setLayoutY(y + 65);
        pane.setVisible(true);
    }

    public static void hideDetailPanel() {
        selectItem = null;
        pane.setVisible(false);
    }
}
