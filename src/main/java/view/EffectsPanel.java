package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import model.entity.effects.EffectParams;

import static game.GameParams.tileSize;

/**
 * Панель эффектов, действующих на персонажа
 */
public class EffectsPanel {
    @Getter
    private final Pane pane;
    private final ScrollPane scrollPane;
    private final Pane innerPane;

    @Getter
    private final Label effectNameLabel; // название эффекта
    @Getter
    private final Label effectSourceLabel; // источник эффекта
    @Getter
    private final Label timeLabel; // время эффекта
    @Getter
    private final Label powerLabel; // сила эффекта

    public EffectsPanel() {
        pane = new Pane();
        pane.setPrefSize(340, 305);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        innerPane = new Pane();
        innerPane.setPrefSize(340, 305);
        innerPane.setLayoutX(150);
        innerPane.setLayoutY(150);
        innerPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        scrollPane = new ScrollPane();
        scrollPane.setPrefSize(400, 400);
        scrollPane.setMaxHeight(400);
        scrollPane.setMinWidth(550);
        scrollPane.setContent(innerPane);

        pane.getChildren().add(scrollPane);

        effectNameLabel = new Label(Game.getText("EFFECT_NAME"));
        effectNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        effectNameLabel.setLayoutX(10);
        effectNameLabel.setLayoutY(10);
        innerPane.getChildren().add(effectNameLabel);

        effectSourceLabel = new Label(Game.getText("EFFECT_SOURCE"));
        effectSourceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        effectSourceLabel.setLayoutX(260);
        effectSourceLabel.setLayoutY(10);
        innerPane.getChildren().add(effectSourceLabel);

        timeLabel = new Label(Game.getText("EFFECT_TIME"));
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        timeLabel.setLayoutX(370);
        timeLabel.setLayoutY(10);
        innerPane.getChildren().add(timeLabel);

        powerLabel = new Label(Game.getText("EFFECT_POWER"));
        powerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        powerLabel.setLayoutX(480);
        powerLabel.setLayoutY(10);
        innerPane.getChildren().add(powerLabel);
    }

    /**
     * Обновить отображение наложенных на персонажа эффектов
     */
    public void refreshEffectsPanel() {
        innerPane.getChildren().remove(innerPane.getChildren().indexOf(powerLabel) + 1, innerPane.getChildren().size());
        int i = 0;
        for (EffectParams effect : Game.getMap().getPlayer().getAppliedEffects()) {
            var itemRecord = new EffectRecord(effect);
            itemRecord.getPane().setLayoutY(++i * tileSize);
            innerPane.getChildren().add(itemRecord.getPane());
        }
        Game.getGameMenu().getEffectTab().setDisable(Game.getMap().getPlayer().getAppliedEffects().isEmpty());
    }
}
