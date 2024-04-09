package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import model.entity.effects.EffectParams;

/**
 * Строка с информацией о действующем на персонажа эффекте для отображения на панели эффектов
 */
public class EffectRecord {
    @Getter
    private Pane pane;
    private Label effectNameLabel; // название эффекта
    private Label effectSourceLabel; // источник эффекта
    private Label timeLabel; // время эффекта
    private Label powerLabel; // сила эффекта

    public EffectRecord(EffectParams effect) {
        pane = new Pane();
        pane.setPrefSize(550, 40);
        pane.setBackground(new Background(new BackgroundFill((Color.WHITESMOKE), CornerRadii.EMPTY, Insets.EMPTY)));

        effectNameLabel = new Label(Game.getEffectText(effect.getStrId()));
        effectNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        effectNameLabel.setLayoutX(Game.getEffectsPanel().getEffectNameLabel().getLayoutX());
        effectNameLabel.setLayoutY(10);
        pane.getChildren().add(effectNameLabel);

        String baseItemName = effect.getBaseItem().getName();
        baseItemName = baseItemName.length() > 16 ? baseItemName.substring(0, 16).concat(".") : baseItemName;
        effectSourceLabel = new Label(baseItemName);
        effectSourceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        effectSourceLabel.setLayoutX(Game.getEffectsPanel().getEffectSourceLabel().getLayoutX());
        effectSourceLabel.setLayoutY(10);
        pane.getChildren().add(effectSourceLabel);

        timeLabel = new Label(effect.getDurability() != null ? effect.getDurability().toString() : "");
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        timeLabel.setLayoutX(Game.getEffectsPanel().getTimeLabel().getLayoutX());
        timeLabel.setLayoutY(10);
        pane.getChildren().add(timeLabel);

        powerLabel = new Label(effect.getPower() != null ? effect.getPower().toString() : "");
        powerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        powerLabel.setLayoutX(Game.getEffectsPanel().getPowerLabel().getLayoutX());
        powerLabel.setLayoutY(10);
        pane.getChildren().add(powerLabel);
    }
}
