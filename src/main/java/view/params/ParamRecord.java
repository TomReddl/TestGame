package view.params;

import model.entity.player.Parameter;
import view.Game;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;

/*
 * Строка с информацией о параметре персонажа
 */
public class ParamRecord {
    @Getter
    private HBox box;
    private Label nameLabel;
    private Label valueLabel;

    public ParamRecord(Parameter parameter, int index, String paramType) {
        box = new HBox();
        box.setSpacing(10);
        box.setLayoutX(10);

        String labelText = "";
        if (ParamPanel.getLegacy().equals(paramType)) {
            labelText = Game.getText(ParamPanel.getLegacyNames().get(index) + "_PARAM_NAME");
        } else if (ParamPanel.getCharacteristic().equals(paramType)) {
            labelText = Game.getText(ParamPanel.getCharacteristicsNames().get(index) + "_PARAM_NAME");
        } else if (ParamPanel.getSkill().equals(paramType)) {
            labelText = Game.getText(ParamPanel.getSkillsNames().get(index) + "_PARAM_NAME");
        }

        nameLabel = new Label(labelText);
        nameLabel.setLayoutX(5);
        nameLabel.setLayoutY(5);
        nameLabel.setMinWidth(150);
        box.getChildren().add(nameLabel);

        valueLabel = new Label(parameter.getCurrentValue().toString());
        valueLabel.setLayoutX(150);
        valueLabel.setLayoutY(5);
        box.getChildren().add(valueLabel);
    }
}
