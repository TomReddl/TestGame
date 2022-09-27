package view.params;

import javafx.geometry.Insets;
import javafx.scene.Group;
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
import model.entity.player.Parameter;
import view.Game;

import java.util.ArrayList;
import java.util.List;

/*
 * Панель параметров персонажа
 */
public class ParamPanel {
    @Getter
    private static final Pane pane = new Pane();
    @Getter
    private static final ScrollPane scrollPane = new ScrollPane();
    @Getter
    private static final Pane skillsPane = new Pane();
    @Getter
    private final Label legacyLabel = new Label(Game.getText("LEGACY"));
    @Getter
    private final Label characteristicsLabel = new Label(Game.getText("CHARACTERISTICS"));
    @Getter
    private final Label skillsLabel = new Label(Game.getText("SKILLS"));

    @Getter
    private static final String legacy = "legacy";
    @Getter
    private static final String characteristic = "characteristic";
    @Getter
    private static final String skill = "skill";

    @Getter
    private static final List<String> legacyNames = new ArrayList<>();
    @Getter
    private static final List<String> characteristicsNames = new ArrayList<>();
    @Getter
    private static final List<String> skillsNames = new ArrayList<>();

    static {
        legacyNames.add("HAN");
        legacyNames.add("RISA");
        legacyNames.add("ULKOR");
        legacyNames.add("MHAA");
        legacyNames.add("WURTUS");
        legacyNames.add("SHI_DOL");

        characteristicsNames.add("POWER");
        characteristicsNames.add("STAMINA");
        characteristicsNames.add("DEXTERITY");
        characteristicsNames.add("CHARISMA");
        characteristicsNames.add("INTELLIGENCE");
        characteristicsNames.add("PERCEPTION");

        skillsNames.add("HEAVY_WEAPON");
        skillsNames.add("HEAVY_ARMOR");
        skillsNames.add("FARMING");
        skillsNames.add("CARPENTRY");
        skillsNames.add("BLOCKING");
        skillsNames.add("HAND_TO_HAND_COMBAT");
        skillsNames.add("BLACKSMITH");
        skillsNames.add("CONSTRUCTION");
        skillsNames.add("LIGHT_WEAPON");
        skillsNames.add("LIGHT_ARMOR");
        skillsNames.add("UNARMORED_FIGHT");
        skillsNames.add("BREAKING_LOCKS");
        skillsNames.add("SPEECHCRAFT");
        skillsNames.add("TRADE");
        skillsNames.add("COMMAND");
        skillsNames.add("LOVE");
        skillsNames.add("EDUCATION");
        skillsNames.add("POTIONS");
        skillsNames.add("MEDICINE");
        skillsNames.add("ENGINEERING");
        skillsNames.add("SHOOTING");
        skillsNames.add("PICKPOCKETING");
        skillsNames.add("SNEAKING");
        skillsNames.add("ANIMAL_HANDLING");
    }

    public ParamPanel(Group root) {
        pane.setLayoutX(40);
        pane.setLayoutY(40);
        pane.setPrefSize(400, 270);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        legacyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        legacyLabel.setLayoutX(5);
        legacyLabel.setLayoutY(5);
        pane.getChildren().add(legacyLabel);

        var i = 0;
        for (Parameter legacyParam : Game.getMap().getPlayer().getParams().getLegacy()) {
            var paramRecord = new ParamRecord(legacyParam, i, legacy);
            paramRecord.getBox().setLayoutY(20 + (i++) * 18);
            pane.getChildren().add(paramRecord.getBox());
        }

        characteristicsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        characteristicsLabel.setLayoutX(5);
        characteristicsLabel.setLayoutY(135);
        pane.getChildren().add(characteristicsLabel);

        i = 0;
        for (Parameter getCharacteristicParam : Game.getMap().getPlayer().getParams().getCharacteristics()) {
            var paramRecord = new ParamRecord(getCharacteristicParam, i, characteristic);
            paramRecord.getBox().setLayoutY(150 + (i++) * 18);
            pane.getChildren().add(paramRecord.getBox());
        }

        skillsPane.setLayoutX(190);
        skillsPane.setLayoutY(20);
        skillsPane.setPrefSize(180, 550);

        scrollPane.setLayoutX(190);
        scrollPane.setLayoutY(20);
        scrollPane.setPrefSize(200, 240);
        scrollPane.setVmin(0);
        scrollPane.setVmax(240);
        scrollPane.setContent(skillsPane);

        skillsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        skillsLabel.setLayoutX(190);
        skillsLabel.setLayoutY(5);
        pane.getChildren().add(skillsLabel);

        i = 0;
        var j = 0;
        for (Parameter skillParam : Game.getMap().getPlayer().getParams().getSkills()) {
            if (i % 4 == 0) {
                var label = new Label(Game.getText(ParamPanel.getCharacteristicsNames().get(i / 4) + "_PARAM_NAME"));
                label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                label.setTextFill(Color.web("#999999"));
                label.setLayoutY(i * 18 + j);
                label.setLayoutX(5);
                skillsPane.getChildren().add(label);
                j += 18;
            }
            var paramRecord = new ParamRecord(skillParam, i, skill);
            paramRecord.getBox().setLayoutY((i++) * 18 + j);
            skillsPane.getChildren().add(paramRecord.getBox());
        }

        pane.getChildren().add(scrollPane);
        root.getChildren().add(pane);
    }

    public void show(Boolean show) {
        //TODO обновлять отображение параметров
    }
}
