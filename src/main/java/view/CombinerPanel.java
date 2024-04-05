package view;

import controller.CharactersController;
import controller.ItemsController;
import controller.TimeController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;
import model.editor.TileTypeEnum;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectParams;
import model.entity.map.Items;
import model.entity.player.Character;
import view.inventory.InventoryPanel;

import java.util.*;
import java.util.stream.Collectors;

import static controller.ItemsController.biomassId;
import static controller.ItemsController.mutantIngredientId;

/**
 * Панель объединителя ингредиентов. Позволяет создать из двух ингредиентов один новый, обладающий свойствами обоих ингредиентов
 */
public class CombinerPanel {
    private final Random random = new Random();
    @Getter
    private final Pane pane;
    @Getter
    private final Label combinerNameLabel; // название объединителя
    private final Label infoLabel; // подсказка для игрока
    private final Label chanceLabel; // шанс объединения
    @Getter
    private final ImageView firstIngredientImage; // изображение первого выбранного ингредиента
    @Getter
    private final ImageView firstIngredientBackgroundImage; // фон для первого ингредиента
    @Getter
    private final ImageView secondIngredientImage; // изображение второго выбранного ингредиента
    @Getter
    private final ImageView secondIngredientBackgroundImage; // фон для второго ингредиента
    @Getter
    private final ImageView biomassImage; // изображение соединительного ингредиента (биомассы)
    @Getter
    private final ImageView biomassBackgroundImage; // фон для соединительного ингредиента (биомассы)
    @Getter
    private final Label biomassCountLabel; // количество биомассы
    @Getter
    private final Button combineButton; // кнопка "Объединить"
    private final Button closeButton;

    @Getter
    @Setter
    private Items firstIngredient = new Items(); // первый выбранный ингредиент
    @Getter
    @Setter
    private Items secondIngredient = new Items(); // второй выбранный ингредиент
    @Getter
    @Setter
    private Items biomass = new Items(); // биомасса
    @Getter
    private int index;
    @Getter
    private int chance; // шанс успешного комбинирования ингредиентов

    private final int timeToCombine = 30; // время объединения ингредиентов

    public CombinerPanel() {
        pane = new Pane();
        pane.setPrefSize(230, 210);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        combinerNameLabel = new Label();
        combinerNameLabel.setLayoutX(10);
        combinerNameLabel.setLayoutY(10);
        combinerNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        combinerNameLabel.setText(Editor.getTiles2().get(251).getName());
        pane.getChildren().add(combinerNameLabel);

        infoLabel = new Label();
        infoLabel.setLayoutX(10);
        infoLabel.setLayoutY(30);
        infoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        infoLabel.setText(Game.getText("COMBINER_HINT"));
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(210);
        pane.getChildren().add(infoLabel);

        firstIngredientBackgroundImage = new ImageView("/graphics/gui/ingredientBackground.png");
        firstIngredientBackgroundImage.setLayoutX(40);
        firstIngredientBackgroundImage.setLayoutY(106);
        firstIngredientBackgroundImage.setOnMouseClicked(event -> addIngredient(1, event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(firstIngredientBackgroundImage);

        firstIngredientImage = new ImageView();
        firstIngredientImage.setLayoutX(44);
        firstIngredientImage.setLayoutY(110);
        firstIngredientImage.setOnMouseClicked(event -> addIngredient(1, event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(firstIngredientImage);

        biomassBackgroundImage = new ImageView("/graphics/gui/ingredientBackground.png");
        biomassBackgroundImage.setLayoutX(90);
        biomassBackgroundImage.setLayoutY(106);
        pane.getChildren().add(biomassBackgroundImage);

        biomassImage = new ImageView();
        biomassImage.setLayoutX(94);
        biomassImage.setLayoutY(110);
        pane.getChildren().add(biomassImage);

        biomassCountLabel = new Label();
        biomassCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        biomassCountLabel.setLayoutX(95);
        biomassCountLabel.setLayoutY(137);
        pane.getChildren().add(biomassCountLabel);

        secondIngredientBackgroundImage = new ImageView("/graphics/gui/ingredientBackground.png");
        secondIngredientBackgroundImage.setLayoutX(140);
        secondIngredientBackgroundImage.setLayoutY(106);
        secondIngredientBackgroundImage.setOnMouseClicked(event -> addIngredient(2, event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(secondIngredientBackgroundImage);

        secondIngredientImage = new ImageView();
        secondIngredientImage.setLayoutX(144);
        secondIngredientImage.setLayoutY(110);
        secondIngredientImage.setOnMouseClicked(event -> addIngredient(2, event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(secondIngredientImage);

        chanceLabel = new Label();
        chanceLabel.setLayoutX(40);
        chanceLabel.setLayoutY(160);
        chanceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        chanceLabel.setMaxWidth(210);
        pane.getChildren().add(chanceLabel);

        combineButton = new Button(Game.getText("CONNECT_INGREDIENT"));
        combineButton.setLayoutX(10);
        combineButton.setLayoutY(180);
        combineButton.setPrefWidth(100);
        combineButton.setOnAction(event -> combineIngredients());
        combineButton.setDisable(true);
        pane.getChildren().add(combineButton);

        closeButton = new Button(Game.getText("CLOSE"));
        closeButton.setLayoutX(120);
        closeButton.setLayoutY(180);
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeButton);

        Game.getRoot().getChildren().add(pane);
    }

    private void closePanel() {
        firstIngredient = new Items();
        firstIngredientImage.setImage(null);
        combineButton.setDisable(true);
        chanceLabel.setText("");
        pane.setVisible(false);
        biomassCountLabel.setText("");
        Game.getMap().getSelecterCharacter().setInteractMapPoint(null);
    }

    /**
     * Объединить ингредиенты
     */
    private void combineIngredients() {
        if (firstIngredient.getTypeId() != 0 && secondIngredient.getTypeId() != 0) {
            List<String> knowEffects1 = Game.getMap().getSelecterCharacter().getKnowledgeInfo().getKnowEffects().get(firstIngredient.getTypeId());
            List<String> effects1 = firstIngredient.getEffects().stream().map(EffectParams::getStrId).collect(Collectors.toList());
            List<String> knowEffects2 = Game.getMap().getSelecterCharacter().getKnowledgeInfo().getKnowEffects().get(secondIngredient.getTypeId());
            List<String> effects2 = secondIngredient.getEffects().stream().map(EffectParams::getStrId).collect(Collectors.toList());
            if (effects1.equals(knowEffects1) && effects2.equals(knowEffects2)) {
                Character character = Game.getMap().getSelecterCharacter();
                TimeController.tic(timeToCombine);
                // проверяем, на месте ли наш стол, а то за время объединения он мог куда-то деться
                String tileType = character.getInteractMapPoint().getTile2Info().getType();
                if (tileType == null || !TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.INGREDIENTS_COMBINER)) {
                    Game.showMessage(character.getInteractMapPoint().getTile2Info().getName() + Game.getText("DESTROYED"));
                    closePanel();
                } else {
                    if (random.nextInt(100) < chance) {
                        Items mutantIngredient = new Items(mutantIngredientId, 1);
                        Set<EffectParams> effects = new HashSet<>(firstIngredient.getEffects());
                        effects.addAll(secondIngredient.getEffects());
                        mutantIngredient.setEffects(new ArrayList<>(effects));
                        ItemsController.addItem(mutantIngredient, character.getInventory(), character);
                    } else {
                        Game.showMessage(Game.getText("COMBINING_FAILED"));
                    }

                    ItemsController.deleteItem(biomass, 1, character.getInventory(), character);
                    if (biomass.getCount() <= 0) {
                        biomass = new Items();
                        biomassImage.setImage(null);
                        biomassCountLabel.setText("");
                        setCombineButtonDisabled();
                    } else {
                        biomassCountLabel.setText(String.valueOf(biomass.getCount()));
                    }
                    ItemsController.deleteItem(firstIngredient, 1, character.getInventory(), character);
                    firstIngredient = new Items();
                    firstIngredientImage.setImage(null);
                    ItemsController.deleteItem(secondIngredient, 1, character.getInventory(), character);
                    secondIngredient = new Items();
                    secondIngredientImage.setImage(null);
                    combineButton.setDisable(true);
                    CharactersController.addSkillExp("POTIONS", 20);
                }
            } else {
                Game.showMessage(Game.getText("COMBINE_IMPOSSIBLE"));
            }
        }
    }

    private void addIngredient(int index, boolean isRightMouse) {
        this.index = index;
        if (isRightMouse) { // правой кнопкой стираем предмет
            firstIngredient = new Items();
            firstIngredientImage.setImage(null);
            combineButton.setDisable(true);
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.INGREDIENT.getDesc(), InventoryPanel.ShowModeEnum.SELECT_FOR_COMBINER);
        }
    }

    public void showPanel() {
        pane.setVisible(true);
        biomass = ItemsController.findItemInInventory(biomassId, Game.getMap().getSelecterCharacter().getInventory());
        if (biomass != null) {
            biomassImage.setImage(biomass.getInfo().getIcon().getImage());
            biomassCountLabel.setText(String.valueOf(biomass.getCount()));
        }
        setCombineButtonDisabled();
    }

    /**
     * Установить доступность кнопки "Объединить"
     */
    public void setCombineButtonDisabled() {
        combineButton.setDisable(biomass == null || firstIngredient.getTypeId() == 0 || secondIngredient.getTypeId() == 0);
    }

    /**
     * Установить шанс успешного комбинирования ингредиентов
     */
    public void setCombineChance() {
        if (firstIngredient.getTypeId() != 0 && secondIngredient.getTypeId() != 0) {
            int count = 0;
            for (String str1 : firstIngredient.getEffects().stream().map(EffectParams::getStrId).collect(Collectors.toList())) {
                for (String str2 : secondIngredient.getEffects().stream().map(EffectParams::getStrId).collect(Collectors.toList())) {
                    if (str1.equals(str2)) {
                        count++;
                        break;
                    }
                }
            }
            int alchemySkill = Game.getMap().getSelecterCharacter().getParams().getSkills().get("POTIONS").getCurrentValue();
            if (count == 0) {
                chance = 10 + alchemySkill / 2;
            } else if (count == 1) {
                chance = 25 + alchemySkill / 2;
            }
            if (count >= 2) {
                chance = 50 + alchemySkill / 2;
            }
            chanceLabel.setText(Game.getText("COMBINE_CHANCE") + ": " + chance + "%");
        }
    }
}