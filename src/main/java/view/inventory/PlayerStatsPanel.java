package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import lombok.Getter;
import model.editor.items.BodyPartEnum;
import model.editor.items.ClothesInfo;
import model.editor.items.ClothesStyleEnum;
import model.entity.map.Items;
import view.Game;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Панель жизненных параметров персонажа
 */
public class PlayerStatsPanel {
    @Getter
    private static final Pane pane;
    private static final ImageView heroImage;
    private static final Label nameLabel = new Label(Game.getText("HERO_NAME"));
    private static final Label healthLabel = new Label(Game.getText("HEALTH")); // здоровье
    private static final Label staminaLabel = new Label(Game.getText("STAMINA")); // выносливость
    private static final Label hungerLabel = new Label(Game.getText("HUNGER")); // голод
    private static final Label thirstLabel = new Label(Game.getText("THIRST")); // жажда
    private static final Label cleannessLabel = new Label(Game.getText("CLEANNESS")); // чистота
    @Getter
    private static final Label styleLabel = new Label(Game.getText("STYLE")); // чистота

    @Getter
    private static final Label healthValueLabel = new Label("100/100"); // здоровье
    @Getter
    private static final Label staminaValueLabel = new Label("100/100"); // выносливость
    @Getter
    private static final Label hungerValueLabel = new Label("100/100"); // голод
    @Getter
    private static final Label thirstValueLabel = new Label("100/100"); // жажда
    @Getter
    private static final Label cleannessValueLabel = new Label("100/100"); // чистота

    static {
        pane = new Pane();
        pane.setPrefSize(200, 200);
        pane.setLayoutX(5);
        pane.setLayoutY(5);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(Boolean.FALSE);

        heroImage = new ImageView("/graphics/characters/31.png");
        heroImage.setLayoutX(5);
        heroImage.setLayoutY(5);
        pane.getChildren().add(heroImage);

        nameLabel.setLayoutX(5);
        nameLabel.setLayoutY(50);
        pane.getChildren().add(nameLabel);

        healthLabel.setLayoutX(5);
        healthLabel.setLayoutY(65);
        pane.getChildren().add(healthLabel);

        staminaLabel.setLayoutX(5);
        staminaLabel.setLayoutY(80);
        pane.getChildren().add(staminaLabel);

        hungerLabel.setLayoutX(5);
        hungerLabel.setLayoutY(95);
        pane.getChildren().add(hungerLabel);

        thirstLabel.setLayoutX(5);
        thirstLabel.setLayoutY(110);
        pane.getChildren().add(thirstLabel);

        cleannessLabel.setLayoutX(5);
        cleannessLabel.setLayoutY(125);
        pane.getChildren().add(cleannessLabel);

        styleLabel.setLayoutX(5);
        styleLabel.setLayoutY(140);
        pane.getChildren().add(styleLabel);

        healthValueLabel.setLayoutX(150);
        healthValueLabel.setLayoutY(65);
        pane.getChildren().add(healthValueLabel);

        staminaValueLabel.setLayoutX(150);
        staminaValueLabel.setLayoutY(80);
        pane.getChildren().add(staminaValueLabel);

        hungerValueLabel.setLayoutX(150);
        hungerValueLabel.setLayoutY(95);
        pane.getChildren().add(hungerValueLabel);

        thirstValueLabel.setLayoutX(150);
        thirstValueLabel.setLayoutY(110);
        pane.getChildren().add(thirstValueLabel);

        cleannessValueLabel.setLayoutX(150);
        cleannessValueLabel.setLayoutY(125);
        pane.getChildren().add(cleannessValueLabel);
    }

    /*
     * Получить стиль надетой одежды
     */
    public static void setClothesStyle(List<Pair<BodyPartEnum, Items>> wearingItems) {
        var styles = new HashMap<String, Integer>();

        for (Pair<BodyPartEnum, Items> wearingItem : wearingItems) {
            if (wearingItem.getValue() != null) {
                var style = ((ClothesInfo) wearingItem.getValue().getInfo()).getStyle();
                switch (wearingItem.getKey()) {
                    case HEAD:
                    case SHIRT:
                    case FACE:
                    case GLOVES:
                    case SHOES: {
                        styles.put(style,
                                5 + (styles.get(style) != null ? styles.get(style) : 0));
                        break;
                    }
                    case TORSO:
                    case LEGS: {
                        styles.put(style,
                                20 + (styles.get(style) != null ? styles.get(style) : 0));
                        break;
                    }
                }
            }
        }

        var key = Collections.max(styles.entrySet(), Map.Entry.comparingByValue()).getKey();
        key = styles.get(key) > 30 ? key : ClothesStyleEnum.COMMON.name();
        Game.getMap().getPlayer().setStyle(ClothesStyleEnum.valueOf(key));

        styleLabel.setText(Game.getText("STYLE") + ": " + Game.getMap().getPlayer().getStyle().getDesc());
    }
}
