package view.params;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import model.entity.player.Parameter;
import view.Game;

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

        var labelText = "";
        var labelDesc = "";
        if (ParamPanel.getLegacy().equals(paramType)) {
            labelText = Game.getText(ParamPanel.getLegacyNames().get(index) + "_PARAM_NAME");
            labelDesc = Game.getText(ParamPanel.getLegacyNames().get(index) + "_PARAM_DESC");
        } else if (ParamPanel.getCharacteristic().equals(paramType)) {
            labelText = Game.getText(ParamPanel.getCharacteristicsNames().get(index) + "_PARAM_NAME");
            labelDesc = Game.getText(ParamPanel.getCharacteristicsNames().get(index) + "_PARAM_DESC");
        } else if (ParamPanel.getSkill().equals(paramType)) {
            labelText = Game.getText(ParamPanel.getSkillsNames().get(index) + "_PARAM_NAME");
            labelDesc = Game.getText(ParamPanel.getSkillsNames().get(index) + "_PARAM_DESC");
        }

        nameLabel = new Label(labelText);
        nameLabel.setLayoutX(5);
        nameLabel.setLayoutY(5);
        nameLabel.setMinWidth(150);
        var desc = labelDesc;
        nameLabel.setOnMouseEntered(event -> ParamDescPanel.showDetailPanel(desc, box));
        nameLabel.setOnMouseExited(event -> ParamDescPanel.hideDetailPanel());
        box.getChildren().add(nameLabel);

        valueLabel = new Label(parameter.getCurrentValue().toString());
        valueLabel.setLayoutX(150);
        valueLabel.setLayoutY(5);
        valueLabel.setOnMouseEntered(event -> ParamDescPanel.showDetailPanel(
                String.format(
                        Game.getText("EXP_FOR_SKILL"),
                        parameter.getExperience(),
                        parameter.getRealValue()*10),
                box));
        valueLabel.setOnMouseExited(event -> ParamDescPanel.hideDetailPanel());
        box.getChildren().add(valueLabel);
    }
}
