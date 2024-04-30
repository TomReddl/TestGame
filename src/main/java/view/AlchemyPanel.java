package view;

import controller.CharactersController;
import controller.EffectsController;
import controller.ItemsController;
import controller.TimeController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import model.editor.items.QualityGradationEnum;
import model.entity.ItemTypeEnum;
import model.entity.effects.EffectParams;
import model.entity.map.Items;
import model.entity.player.Character;
import view.inventory.InventoryPanel;

import java.util.*;
import java.util.stream.Collectors;

import static controller.ItemsController.bottleOfWaterId;

/**
 * Панель алхимического стола
 */
public class AlchemyPanel {
    private static final Random random = new Random();

    @Getter
    private final Pane pane;

    private final List<ImageView> ingredientBackgroundsImages = new ArrayList<>(); // фоны для инредиентов зелья
    @Getter
    private final List<ImageView> ingredientImages = new ArrayList<>(); // изображения выбранных инредиентов зелья
    @Getter
    private List<Label> selectedIngredientsCount = new ArrayList<>(); // лэйблы для количества выбранных ингредиентов
    private final ImageView bottleBackgroundImage; // фон для бутылки зелья
    @Getter
    private final ImageView bottleImage; // изображение выбранной бутылки
    @Getter
    private Label selectedBottlesCount = new Label(); // лэйбл для количества выбранных для зелья бутылок
    private final Label tableLabel; // название стола
    @Getter
    private List<Items> selectedIngredients = new ArrayList<>(); // выбранные для зелья предметы
    @Getter
    @Setter
    private Items selectedBottle = new Items(); // выбранная для зелья бутылка
    @Getter
    private int index;
    @Getter
    private final Button cookButton = new Button(Game.getText("COOK")); // кнопка "сварить"
    private final Button cookAllButton = new Button(Game.getText("COOK_ALL")); // кнопка "сварить всё"
    private final Button closeButton = new Button(Game.getText("CLOSE"));
    private final Label potionEffectsLabel = new Label();
    private final TextField potionNameEdit = new TextField();

    private final int timeToCook = 10; // время варки зелья

    private int durability = 0;
    private int power = 0;
    private QualityGradationEnum quality = null;

    public AlchemyPanel() {
        pane = new Pane();
        pane.setPrefSize(340, 305);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        tableLabel = new Label();
        tableLabel.setLayoutX(10);
        tableLabel.setLayoutY(10);
        tableLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(tableLabel);

        Label ingredientsLabel = new Label();
        ingredientsLabel.setLayoutX(10);
        ingredientsLabel.setLayoutY(40);
        ingredientsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ingredientsLabel.setText(Game.getText("INGREDIENTS"));
        pane.getChildren().add(ingredientsLabel);

        int x = 0;
        for (int i = 0; i < 5; i++) {
            selectedIngredients.add(new Items());
            ImageView image = new ImageView("/graphics/gui/ingredientBackground.png");
            image.setLayoutX(10 + x * 50);
            image.setLayoutY(60);
            image.setId(String.valueOf(i));
            int finalI = i;
            image.setOnMouseClicked(event -> addIngredient(finalI, event.getButton() == MouseButton.SECONDARY));
            ingredientBackgroundsImages.add(image);
            pane.getChildren().add(image);

            ImageView image2 = new ImageView();
            image2.setLayoutX(14 + x * 50);
            image2.setLayoutY(64);
            image2.setId(String.valueOf(i));
            image2.setOnMouseClicked(event -> addIngredient(finalI, event.getButton() == MouseButton.SECONDARY));
            ingredientImages.add(image2);

            Label label = new Label();
            label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            label.setLayoutX(14 + x * 50);
            label.setLayoutY(90);
            pane.getChildren().add(label);

            selectedIngredientsCount.add(label);

            x++;

            pane.getChildren().add(image2);
        }

        Label bottleLabel = new Label();
        bottleLabel.setLayoutX(280);
        bottleLabel.setLayoutY(40);
        bottleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        bottleLabel.setText(Game.getText("BOTTLE"));
        pane.getChildren().add(bottleLabel);

        bottleBackgroundImage = new ImageView("/graphics/gui/bottleBackground.png");
        bottleBackgroundImage.setLayoutX(280);
        bottleBackgroundImage.setLayoutY(60);
        bottleBackgroundImage.setOnMouseClicked(event -> addBottle(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(bottleBackgroundImage);

        bottleImage = new ImageView();
        bottleImage.setLayoutX(284);
        bottleImage.setLayoutY(64);
        bottleImage.setOnMouseClicked(event -> addBottle(event.getButton() == MouseButton.SECONDARY));
        pane.getChildren().add(bottleImage);

        selectedBottlesCount.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        selectedBottlesCount.setLayoutX(284);
        selectedBottlesCount.setLayoutY(90);
        pane.getChildren().add(selectedBottlesCount);

        Label effectsLabel = new Label();
        effectsLabel.setLayoutX(10);
        effectsLabel.setLayoutY(110);
        effectsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        effectsLabel.setText(Game.getText("EFFECTS"));
        pane.getChildren().add(effectsLabel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(130);
        scrollPane.setPrefSize(320, 100);

        potionEffectsLabel.setLayoutX(10);
        potionEffectsLabel.setLayoutY(10);
        potionEffectsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        scrollPane.setContent(potionEffectsLabel);

        pane.getChildren().add(scrollPane);

        Label potionNameLabel = new Label();
        potionNameLabel.setLayoutX(10);
        potionNameLabel.setLayoutY(240);
        potionNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        potionNameLabel.setText(Game.getText("POTION_NAME"));
        pane.getChildren().add(potionNameLabel);

        potionNameEdit.setLayoutX(120);
        potionNameEdit.setLayoutY(240);
        potionNameEdit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().add(potionNameEdit);

        cookButton.setLayoutX(10);
        cookButton.setLayoutY(270);
        cookButton.setPrefWidth(100);
        cookButton.setOnAction(event -> cookPotion());
        cookButton.setDisable(true);
        pane.getChildren().add(cookButton);

        cookAllButton.setLayoutX(120);
        cookAllButton.setLayoutY(270);
        cookAllButton.setPrefWidth(100);
        cookAllButton.setOnAction(event -> cookAllPotion());
        cookAllButton.setDisable(false);
        cookAllButton.setVisible(false);
        pane.getChildren().add(cookAllButton);

        closeButton.setLayoutX(120);
        closeButton.setLayoutY(270);
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(event -> closePanel());
        pane.getChildren().add(closeButton);

        Game.getRoot().getChildren().add(pane);
    }

    /**
     * Сварить зелья из всех ингредиентов
     */
    private void cookAllPotion() {
        while (canCook()) {
            cookPotion();
        }
    }

    /**
     * Можно ли сварить зелье
     * @return
     */
    private boolean canCook() {
        long ingredientsCount = selectedIngredients.stream()
                .filter(ingredient -> ingredient.getTypeId() != 0)
                .count();
        return selectedBottle.getTypeId() == bottleOfWaterId && ingredientsCount >= 3;
    }

    /**
     * Сварить зелье
     */
    private void cookPotion() {
        Character character = Game.getMap().getPlayersSquad().getSelectedCharacter();

        Map<String, Integer> effectsMap = new HashMap<>();
        for (Items item : selectedIngredients) {
            if (item.getTypeId() != 0 && item.getEffects() != null) {
                for (String effect : item.getEffects().stream()
                        .map(EffectParams::getStrId)
                        .collect(Collectors.toList())) {
                    effectsMap.merge(effect, 1, Integer::sum);
                }
            }
        }
        boolean potionCreate = false;
        boolean newEffectExplored = false; // были ли открыты новые эффекты ингредиентов при варке
        int expPoints = 1;
        List<EffectParams> effectParams = new ArrayList<>();
        long price = 0L;
        for (String key : effectsMap.keySet()) {
            if (effectsMap.get(key) >= 3) {
                // Ставим признак создания зелья только если минимум у трех ингредиентов есть одинаковый эффект
                potionCreate = true;
                expPoints += 10; // за каждый эффект в сваренном зелье добавляем 10 очков опыта
                var params = new EffectParams();
                params.setStrId(key);
                params.setDurability(durability);
                params.setPower(power);
                price += params.getPower() + params.getDurability();
                effectParams.add(params);
                for (Items ingredient : selectedIngredients) {
                    if (ingredient.getTypeId() != 0) {
                        if (ingredient.getEffects().stream()
                                .map(EffectParams::getStrId)
                                .collect(Collectors.toList()).contains(key)) {
                            List<String> effects = Game.getMap().getPlayersSquad().getSelectedCharacter().getKnowledgeInfo().getKnowEffects().get(ingredient.getTypeId());
                            if (effects == null) {
                                effects = new ArrayList<>();
                                effects.add(key);
                                newEffectExplored = true;
                            } else {
                                if (!effects.contains(key)) {
                                    effects.add(key);
                                    newEffectExplored = true;
                                }
                            }
                            Game.getMap().getPlayersSquad().getSelectedCharacter().getKnowledgeInfo().getKnowEffects().put(ingredient.getTypeId(), effects);
                        }
                    }
                }
            }
        }
        TimeController.tic(timeToCook); // варка зелья занимает время

        // проверяем, на месте ли наш стол, а то за время варки зелья он мог сгореть
        String tileType = character.getInteractMapPoint().getTile2Info().getType();
        if (tileType == null || !TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.ALCHEMY_TABLE)) {
            Game.showMessage(character.getInteractMapPoint().getTile2Info().getName() + Game.getText("DESTROYED"));
            closePanel();
        } else {
            if (potionCreate) {
                ItemsController.deleteItem(selectedBottle, 1, character.getInventory(), character);
                Items potion = new Items(13, 1);
                potion.setEffects(effectParams);
                potion.setPrice(price);
                setEffectsText();
                potion.setName(potionNameEdit.getText() + " (" + quality.getName() + ")"); // к названию предмета приписываем его качество
                ItemsController.addItem(potion, character.getInventory(), character);

                Game.showMessage(newEffectExplored ?
                                Game.getText("POTION_CRAFTED") + ". " + Game.getText("NEW_EFFECT_EXPLORED") :
                                Game.getText("POTION_CRAFTED"),
                        Color.GREEN);
            } else {
                Game.showMessage(Game.getText("POTION_CRAFT_FAIL"));
            }

            CharactersController.addSkillExp("POTIONS", expPoints);

            // удаляем потраченные предметы в любом случае, даже, если зелье не получилось.
            ItemsController.deleteItem(selectedIngredients.get(0), 1, character.getInventory(), character);
            ItemsController.deleteItem(selectedIngredients.get(1), 1, character.getInventory(), character);
            ItemsController.deleteItem(selectedIngredients.get(2), 1, character.getInventory(), character);
            ItemsController.deleteItem(selectedIngredients.get(3), 1, character.getInventory(), character);
            ItemsController.deleteItem(selectedIngredients.get(4), 1, character.getInventory(), character);

            for (int i = 0; i < 5; i++) {
                selectedIngredientsCount.get(i).setText(String.valueOf(selectedIngredients.get(i).getCount()));
                if (selectedIngredients.get(i).getCount() <= 0) {
                    selectedIngredients.set(i, new Items());
                    ingredientImages.get(i).setImage(null);
                    selectedIngredientsCount.get(i).setText("");
                }
            }
            selectedBottlesCount.setText(String.valueOf(selectedBottle.getCount()));
            if (selectedBottle.getCount() <= 0) {
                selectedBottle = new Items();
                bottleImage.setImage(null);
                selectedBottlesCount.setText("");
            }
            setCookButtonDisabled();
            setCookAllButtonVisibly();
        }
    }

    private void closePanel() {
        pane.setVisible(false);
        // стираем выбранные элементы
        for (int i = 0; i < 5; i++) {
            selectedIngredients.set(i, new Items());
            ingredientImages.get(i).setImage(null);
            selectedIngredientsCount.get(i).setText("");
        }
        selectedBottle = new Items();
        bottleImage.setImage(null);
        selectedBottlesCount.setText("");
        potionEffectsLabel.setText("");
        potionNameEdit.setText("");
        cookButton.setDisable(true);
        Game.getMap().getPlayersSquad().getSelectedCharacter().setInteractMapPoint(null);
    }

    private void addIngredient(int index, boolean isRightMouse) {
        this.index = index;
        if (isRightMouse) { // правой кнопкой стираем предмет
            selectedIngredients.set(index, new Items());
            ingredientImages.get(index).setImage(null);
            selectedIngredientsCount.get(index).setText("");
            setCookButtonDisabled();
            Game.getEditor().getAlchemyPanel().setEffectsText();
            setCookAllButtonVisibly();
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.INGREDIENT.getDesc(), InventoryPanel.ShowModeEnum.SELECT_FOR_POTION_CRAFT);
        }
    }

    private void addBottle(boolean isRightMouse) {
        if (isRightMouse) { // правой кнопкой стираем предмет
            selectedBottle = new Items();
            bottleImage.setImage(null);
            selectedBottlesCount.setText("");
            setCookButtonDisabled();
            setCookAllButtonVisibly();
        } else {
            Game.getGameMenu().showOnlyInventory(true, ItemTypeEnum.BOTTLE.getDesc(), InventoryPanel.ShowModeEnum.SELECT_FOR_POTION_CRAFT);
        }
    }

    /**
     * Показать/скрыть панель алхимического стола
     *
     * @param visible - признак видимости
     * @param name -    название стола для отображения игроку
     * @param level -   уровень стола, чем выше, тем больше ячеек под ингредиенты
     */
    public void showPanel(boolean visible, String name, int level) {
        tableLabel.setText(name);
        pane.setVisible(visible);
        ingredientBackgroundsImages.get(3).setVisible(level > 1); // четвертый ингредиент можно добавлять начиная со 2 уровня стола
        ingredientImages.get(3).setVisible(level > 1);
        ingredientBackgroundsImages.get(4).setVisible(level > 2); // пятый ингредиент можно добавлять начиная с 3 уровня стола
        ingredientImages.get(4).setVisible(level > 2);

        selectedBottle = ItemsController.findItemInInventory(bottleOfWaterId, Game.getMap().getPlayersSquad().getSelectedCharacter().getInventory());
        if (selectedBottle != null) {
            bottleImage.setImage(selectedBottle.getInfo().getIcon().getImage());
            selectedBottlesCount.setText(String.valueOf(selectedBottle.getCount()));
        }
    }

    /**
     * Установить доступность кнопки "Сварить"
     */
    public void setCookButtonDisabled() {
        cookButton.setDisable(!canCook());

        quality = ItemsController.getQuality(Game.getMap().getPlayersSquad().getSelectedCharacter().getParams().getSkills().get("POTIONS"));
        durability = (int) (1 + 5 * quality.getQualityLevel()) + random.nextInt(5);
        power = (int) (1 + 5 * quality.getQualityLevel()) + random.nextInt(5);
    }

    /**
     * Установить видимость кнопки "Сварить всё"
     */
    public void setCookAllButtonVisibly() {
        long ingredientsCount = selectedIngredients.stream()
                .filter(item -> item.getTypeId() != 0 && item.getCount() > 1)
                .count();

        boolean manyBottles = selectedBottle.getTypeId() != 0 && selectedBottle.getCount() > 1;

        cookAllButton.setVisible(ingredientsCount > 2 && manyBottles && !potionEffectsLabel.getText().equals(""));
        closeButton.setLayoutX(cookAllButton.isVisible() ? 230 : 120);
    }

    /**
     * Показать игроку текст эффектов зелья
     */
    public void setEffectsText() {
        Map<String, Integer> effectsMap = new HashMap<>();
        var knowEffects = Game.getMap().getPlayersSquad().getSelectedCharacter().getKnowledgeInfo().getKnowEffects();
        for (Items item : selectedIngredients) {
            if (item.getTypeId() != 0 && item.getEffects() != null) {
                for (String effect : item.getEffects().stream()
                        .map(EffectParams::getStrId)
                        .filter(effect -> knowEffects.get(item.getTypeId()) != null && knowEffects.get(item.getTypeId()).contains(effect))
                        .collect(Collectors.toList())) {
                    effectsMap.merge(effect, 1, Integer::sum);
                }
            }
        }
        String text = "";
        String potionName = "";
        for (String key : effectsMap.keySet()) {
            if (effectsMap.get(key) >= 3) {
                if (potionName.equals("")) {
                    potionName = EffectsController.getEffects().get(key).getName();
                }

                text = text.concat(EffectsController.getEffects().get(key).getName() + " " +
                        power + Game.getText("UNITS") + " " +
                        Game.getText("ON") + " " + durability) + " " + Game.getText("TURNS") + "\n";
            }
        }
        potionEffectsLabel.setText(text);
        potionNameEdit.setText(potionName);
    }
}
