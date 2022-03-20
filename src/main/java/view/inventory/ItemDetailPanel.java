package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import model.editor.items.ClothesGenderEnum;
import model.editor.items.ClothesInfo;
import model.editor.items.ClothesStyleEnum;
import model.editor.items.WeaponInfo;
import model.entity.map.Items;
import view.Game;
import view.params.ParamPanel;

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
                    Game.getText("STRENGTH") + ": " + weaponInfo.getMaxStrength() + "\n" +
                    (weaponInfo.getOneHand() ? Game.getText("ONE_HAND") : Game.getText("TWO_HAND")) + "\n" +
                    Game.getText(ParamPanel.getSkillsNames().get(weaponInfo.getSkill()) + "_PARAM_NAME"));
        } else if (item.getInfo() instanceof ClothesInfo) {
            var clothesInfo = (ClothesInfo) item.getInfo();
            descLabel.setText(descLabel.getText() + "\n\n" +
                    Game.getText("ARMOR") + ": " + clothesInfo.getArmor() + "\n" +
                    Game.getText("COVERING") + ": " + clothesInfo.getCovering() + "\n" +
                    Game.getText("STRENGTH") + ": " + clothesInfo.getMaxStrength() + "\n" +
                    Game.getText(ParamPanel.getSkillsNames().get(clothesInfo.getSkill()) + "_PARAM_NAME") + "\n" + "\n" +

                    Game.getText("STYLE") + ": " + ClothesStyleEnum.valueOf(clothesInfo.getStyle()).getDesc() + "\n" +
                    ClothesGenderEnum.valueOf(clothesInfo.getGender()).getDesc());
        }
        pane.setLayoutY(y + 65);
        pane.setVisible(Boolean.TRUE);
    }

    public static void hideDetailPanel() {
        selectItem = null;
        pane.setVisible(Boolean.FALSE);
    }
}
