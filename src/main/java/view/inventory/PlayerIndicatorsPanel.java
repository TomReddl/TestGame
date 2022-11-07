package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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

import java.util.*;

/**
 * Панель жизненных параметров персонажа
 */
public class PlayerIndicatorsPanel {
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
    private static final Label styleLabel = new Label(Game.getText("STYLE")); // стиль одежды

    @Getter
    private static final Label healthValueLabel = new Label("50/100"); // здоровье
    @Getter
    private static final Label staminaValueLabel = new Label("50/100"); // выносливость
    @Getter
    private static final Label hungerValueLabel = new Label("50/100"); // голод
    @Getter
    private static final Label thirstValueLabel = new Label("50/100"); // жажда
    @Getter
    private static final Label cleannessValueLabel = new Label("50/100"); // чистота

    @Getter
    private static final ProgressBar healthPB = new ProgressBar(0.75);
    @Getter
    private static final ProgressBar staminaPB = new ProgressBar(0.6);
    @Getter
    private static final ProgressBar hungerPB = new ProgressBar(0.8);
    @Getter
    private static final ProgressBar thirstPB = new ProgressBar(0.5);
    @Getter
    private static final ProgressBar cleannessPB = new ProgressBar(0.5);

    private static final List<Pair<Label, ProgressBar>> indicatorControls = new ArrayList<>();

    static {
        pane = new Pane();
        pane.setPrefSize(200, 200);
        pane.setLayoutX(5);
        pane.setLayoutY(5);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        heroImage = new ImageView("/graphics/characters/31.png");
        heroImage.setLayoutX(5);
        heroImage.setLayoutY(5);
        pane.getChildren().add(heroImage);

        nameLabel.setLayoutX(5);
        nameLabel.setLayoutY(50);
        pane.getChildren().add(nameLabel);

        healthPB.setLayoutX(5);
        healthPB.setLayoutY(65);
        healthPB.setPrefWidth(190);
        healthPB.setPrefHeight(20);
        healthPB.setStyle("-fx-accent: #18c314;");
        pane.getChildren().add(healthPB);

        healthLabel.setLayoutX(10);
        healthLabel.setLayoutY(65);
        pane.getChildren().add(healthLabel);

        staminaPB.setLayoutX(5);
        staminaPB.setLayoutY(85);
        staminaPB.setPrefWidth(190);
        staminaPB.setPrefHeight(20);
        staminaPB.setStyle("-fx-accent: #18c314;");
        pane.getChildren().add(staminaPB);

        staminaLabel.setLayoutX(10);
        staminaLabel.setLayoutY(85);
        pane.getChildren().add(staminaLabel);

        hungerPB.setLayoutX(5);
        hungerPB.setLayoutY(105);
        hungerPB.setPrefWidth(190);
        hungerPB.setPrefHeight(20);
        hungerPB.setStyle("-fx-accent: #18c314;");
        pane.getChildren().add(hungerPB);

        hungerLabel.setLayoutX(10);
        hungerLabel.setLayoutY(105);
        pane.getChildren().add(hungerLabel);

        thirstPB.setLayoutX(5);
        thirstPB.setLayoutY(125);
        thirstPB.setPrefWidth(190);
        thirstPB.setPrefHeight(20);
        thirstPB.setStyle("-fx-accent: #18c314;");
        pane.getChildren().add(thirstPB);

        thirstLabel.setLayoutX(10);
        thirstLabel.setLayoutY(125);
        pane.getChildren().add(thirstLabel);

        cleannessPB.setLayoutX(5);
        cleannessPB.setLayoutY(145);
        cleannessPB.setPrefWidth(190);
        cleannessPB.setPrefHeight(20);
        cleannessPB.setStyle("-fx-accent: #18c314;");
        pane.getChildren().add(cleannessPB);

        cleannessLabel.setLayoutX(10);
        cleannessLabel.setLayoutY(145);
        pane.getChildren().add(cleannessLabel);

        styleLabel.setLayoutX(10);
        styleLabel.setLayoutY(165);
        pane.getChildren().add(styleLabel);

        healthValueLabel.setLayoutX(150);
        healthValueLabel.setLayoutY(65);
        pane.getChildren().add(healthValueLabel);

        staminaValueLabel.setLayoutX(150);
        staminaValueLabel.setLayoutY(85);
        pane.getChildren().add(staminaValueLabel);

        hungerValueLabel.setLayoutX(150);
        hungerValueLabel.setLayoutY(105);
        pane.getChildren().add(hungerValueLabel);

        thirstValueLabel.setLayoutX(150);
        thirstValueLabel.setLayoutY(125);
        pane.getChildren().add(thirstValueLabel);

        cleannessValueLabel.setLayoutX(150);
        cleannessValueLabel.setLayoutY(145);
        pane.getChildren().add(cleannessValueLabel);

        indicatorControls.add(new Pair<>(healthValueLabel, healthPB));
        indicatorControls.add(new Pair<>(staminaValueLabel, staminaPB));
        indicatorControls.add(new Pair<>(hungerValueLabel, hungerPB));
        indicatorControls.add(new Pair<>(thirstValueLabel, thirstPB));
        indicatorControls.add(new Pair<>(cleannessValueLabel, cleannessPB));

        Game.getRoot().getChildren().add(pane);
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

        styleLabel.setText(String.format(Game.getText("STYLE"), Game.getMap().getPlayer().getStyle().getDesc()));
    }

    public static void showPanel(Boolean show) {
        PlayerIndicatorsPanel.getPane().setVisible(show);

        if (show) {
            var indicators = Game.getMap().getPlayer().getParams().getIndicators();

            healthValueLabel.setText(indicators.get(0).getCurrentValue() + "/" + indicators.get(0).getMaxValue());
            staminaValueLabel.setText(indicators.get(1).getCurrentValue() + "/" + indicators.get(1).getMaxValue());
            hungerValueLabel.setText(indicators.get(2).getCurrentValue() + "/" + indicators.get(2).getMaxValue());
            thirstValueLabel.setText(indicators.get(3).getCurrentValue() + "/" + indicators.get(3).getMaxValue());
            cleannessValueLabel.setText(indicators.get(4).getCurrentValue() + "/" + indicators.get(4).getMaxValue());

            healthPB.setProgress((double) indicators.get(0).getCurrentValue() / indicators.get(0).getMaxValue());
            staminaPB.setProgress((double) indicators.get(1).getCurrentValue() / indicators.get(1).getMaxValue());
            hungerPB.setProgress((double) indicators.get(2).getCurrentValue() / indicators.get(2).getMaxValue());
            thirstPB.setProgress((double) indicators.get(3).getCurrentValue() / indicators.get(3).getMaxValue());
            cleannessPB.setProgress((double) indicators.get(4).getCurrentValue() / indicators.get(4).getMaxValue());
        }
    }

    /**
     * обновить значение индикатора
     *
     * @param indicatorId - идентификатор индикатора
     * @param value       - новое текущее значение
     */
    public static void setIndicatorValue(int indicatorId, int value) {
        var params = Game.getMap().getPlayer().getParams();
        params.getIndicators().get(indicatorId).setCurrentValue(value);
        if (params.getIndicators().get(indicatorId).getCurrentValue() > params.getIndicators().get(indicatorId).getMaxValue()) {
            params.getIndicators().get(indicatorId).setCurrentValue(params.getIndicators().get(indicatorId).getMaxValue());
        }
        var maxValue = Game.getMap().getPlayer().getParams().getIndicators().get(indicatorId).getMaxValue();
        indicatorControls.get(indicatorId).getKey().setText(value + "/" + maxValue);
        indicatorControls.get(indicatorId).getValue().setProgress((double) value / maxValue);
    }
}
