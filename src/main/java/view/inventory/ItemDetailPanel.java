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
        pane.setVisible(Boolean.FALSE);

        descLabel = new Label();
        descLabel.setLayoutX(5);
        descLabel.setLayoutY(5);
        descLabel.setMaxWidth(290);
        descLabel.setWrapText(Boolean.TRUE);
        pane.getChildren().add(descLabel);
    }

    public static void showDetailPanel(Items item, double y) {
        selectItem = item;
        descLabel.setText(item.getInfo().getDesc());
        if (item.getInfo() instanceof WeaponInfo) {
            var weaponInfo = (WeaponInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    Game.getText("DAMAGE") + ": " + weaponInfo.getDamage() + "\n" +
                    Game.getText("STRENGTH") + ": " + item.getCurrentStrength() + "/" + weaponInfo.getMaxStrength() + "\n" +
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
                    Game.getText("ARMOR") + ": " + clothesInfo.getArmor() + "\n" +
                    Game.getText("COVERING") + ": " + clothesInfo.getCovering() + "\n" +

                    ((clothesInfo.getBodyPart().equals(BodyPartEnum.BACKPACK.name()) ||
                            clothesInfo.getBodyPart().equals(BodyPartEnum.BELT.name()))?
                            Game.getText("ADD_VOLUME") + " " +
                                    BigDecimal.valueOf(Long.parseLong(clothesInfo.getParams().get(0))).
                                            divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP)
                            : "") + "\n" +

                    Game.getText("STRENGTH") + ": " + item.getCurrentStrength() + "/" + clothesInfo.getMaxStrength() + "\n" +
                    Game.getText(ParamPanel.getSkillsNames().get(clothesInfo.getSkill()) + "_PARAM_NAME") + "\n" + "\n" +

                    Game.getText("STYLE") + ": " + ClothesStyleEnum.valueOf(clothesInfo.getStyle()).getDesc() + "\n" +
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
                    Game.getText("TASTE") + ": " + edibleInfo.getTaste() + "\n" +
                    Game.getText("HUNGER_RESTORE") + ": " + edibleInfo.getHunger() + "\n" +
                    Game.getText("THIRST_RESTORE") + ": " + edibleInfo.getThirst() + "\n" + "\n");
            if (edibleInfo.getEffects() != null) {
                for (EffectParams effect : edibleInfo.getEffects()) {
                    descLabel.setText(descLabel.getText() + Game.getEffectText(effect.getStrId()) + " " +
                            effect.getPower() + Game.getText("UNITS") + " " +
                            (effect.getDurability() > 0 ? (Game.getText("ON") + " " + effect.getDurability() + " " + Game.getText("TURNS")) : ""));
                }
            }
        }
        pane.setLayoutY(y + 65);
        pane.setVisible(Boolean.TRUE);
    }

    public static void hideDetailPanel() {
        selectItem = null;
        pane.setVisible(Boolean.FALSE);
    }
}
