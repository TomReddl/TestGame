package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import model.entity.ItemTypeEnum;
import model.entity.map.ClosableCellInfo;
import model.entity.map.MapCellInfo;

/**
 * Панель настройки параметров тайла
 */
public class TileEditPanel {
    @Getter
    private static final Pane pane;

    private static final Label descLabel = new Label(Game.getText("DESCRIPTION"));
    private static final TextField descTF = new TextField();

    private static final Label isLockedLabel = new Label(Game.getText("IS_LOCKED"));
    private static final CheckBox isLockedCB = new CheckBox();

    private static final Label isCodeLockLabel = new Label(Game.getText("IS_CODE_LOCK"));
    private static final CheckBox isCodeLockCB = new CheckBox();

    private static final Label keyIdLabel = new Label(Game.getText("KEY_ID"));
    private static final TextField keyIdTF = new TextField();
    private static final ImageView keyImage = new ImageView();

    private static final Label lockLevelLabel = new Label(Game.getText("LOCK_LEVEL"));
    private static final TextField lockLevelTF = new TextField();

    private static final Label codeLockHintLabel = new Label(Game.getText("CODE_LOCK_HINT"));
    private static final TextField codeLockHintTF = new TextField();

    private static final Label isTrapLabel = new Label(Game.getText("IS_TRAP"));
    private static final CheckBox isTrapCB = new CheckBox();

    private static final Label trapLevelLabel = new Label(Game.getText("TRAP_LEVEL"));
    private static final TextField trapLevelTF = new TextField();

    private static final Button saveButton = new Button(Game.getText("SAVE"));
    private static final Button backButton = new Button(Game.getText("BACK"));
    private static MapCellInfo currentCellInfo = null;

    static {
        pane = new Pane();
        pane.setPrefSize(300, 300);
        pane.setLayoutX(150);
        pane.setLayoutY(150);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);

        descLabel.setLayoutX(20);
        descLabel.setLayoutY(5);
        pane.getChildren().add(descLabel);

        descTF.setLayoutX(120);
        descTF.setLayoutY(5);
        pane.getChildren().add(descTF);

        isLockedLabel.setLayoutX(20);
        isLockedLabel.setLayoutY(35);
        pane.getChildren().add(isLockedLabel);

        isLockedCB.setLayoutX(120);
        isLockedCB.setLayoutY(35);
        pane.getChildren().add(isLockedCB);

        isCodeLockLabel.setLayoutX(20);
        isCodeLockLabel.setLayoutY(65);
        pane.getChildren().add(isCodeLockLabel);

        isCodeLockCB.setLayoutX(120);
        isCodeLockCB.setLayoutY(65);
        isCodeLockCB.selectedProperty().addListener((observable, oldValue, newValue) -> setCodeLockVisible());
        pane.getChildren().add(isCodeLockCB);

        keyIdLabel.setLayoutX(20);
        keyIdLabel.setLayoutY(95);
        pane.getChildren().add(keyIdLabel);

        keyIdTF.setLayoutX(120);
        keyIdTF.setLayoutY(95);
        keyIdTF.textProperty().addListener((observable, oldValue, newValue) -> filterChars(keyIdTF, newValue));
        keyIdTF.setOnKeyTyped(event -> findKeyImage());
        pane.getChildren().add(keyIdTF);

        keyImage.setLayoutX(260);
        keyImage.setLayoutY(90);
        pane.getChildren().add(keyImage);

        lockLevelLabel.setLayoutX(20);
        lockLevelLabel.setLayoutY(125);
        pane.getChildren().add(lockLevelLabel);

        lockLevelTF.setLayoutX(120);
        lockLevelTF.setLayoutY(125);
        lockLevelTF.textProperty().addListener((observable, oldValue, newValue) -> filterChars(lockLevelTF, newValue));
        pane.getChildren().add(lockLevelTF);

        codeLockHintLabel.setLayoutX(20);
        codeLockHintLabel.setLayoutY(160);
        pane.getChildren().add(codeLockHintLabel);

        codeLockHintTF.setLayoutX(120);
        codeLockHintTF.setLayoutY(160);
        pane.getChildren().add(codeLockHintTF);

        isTrapLabel.setLayoutX(20);
        isTrapLabel.setLayoutY(190);
        pane.getChildren().add(isTrapLabel);

        isTrapCB.setLayoutX(120);
        isTrapCB.setLayoutY(190);
        pane.getChildren().add(isTrapCB);

        trapLevelLabel.setLayoutX(20);
        trapLevelLabel.setLayoutY(230);
        pane.getChildren().add(trapLevelLabel);

        trapLevelTF.setLayoutX(120);
        trapLevelTF.setLayoutY(230);
        trapLevelTF.textProperty().addListener((observable, oldValue, newValue) -> filterChars(trapLevelTF, newValue));
        pane.getChildren().add(trapLevelTF);

        saveButton.setLayoutX(15);
        saveButton.setLayoutY(260);
        saveButton.setPrefWidth(100);
        saveButton.setOnAction(event -> saveTile());
        pane.getChildren().add(saveButton);

        backButton.setLayoutX(120);
        backButton.setLayoutY(260);
        backButton.setPrefWidth(100);
        backButton.setOnAction(event -> hidePanel());
        pane.getChildren().add(backButton);

        Game.getRoot().getChildren().add(pane);
    }

    private static void filterChars(TextField textField, String newValue) {
        if (!isCodeLockCB.isSelected()) {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }
    }

    private static void setCodeLockVisible() {
        if (isCodeLockCB.isSelected()) {
            keyIdLabel.setText(Game.getText("CODE_FOR_LOCK"));
            lockLevelLabel.setText(Game.getText("CHARS_FOR_LOCK"));
        } else {
            keyIdLabel.setText(Game.getText("KEY_ID"));
            lockLevelLabel.setText(Game.getText("LOCK_LEVEL"));
        }
        codeLockHintLabel.setVisible(isCodeLockCB.isSelected());
        codeLockHintTF.setVisible(isCodeLockCB.isSelected());
    }

    private static void findKeyImage() {
        var imageFind = false;
        if (!isCodeLockCB.isSelected() && !keyIdTF.getText().isEmpty() && !keyIdTF.getText().equals("0")) {
            var itemId = Integer.parseInt(keyIdTF.getText());
            if (itemId < Editor.getItems().size()) {
                var item = Editor.getItems().get(itemId);
                if (item != null && item.getTypes().contains(ItemTypeEnum.KEY)) {
                    keyImage.setImage(item.getImage().getImage());
                    imageFind = true;
                }
            }
        }
        if (!imageFind) {
            keyImage.setImage(null);
        }
    }

    public static void showPanel(MapCellInfo cellInfo) {
        currentCellInfo = cellInfo;
        pane.setVisible(true);
        descTF.setText(currentCellInfo.getDesc());
        var type = currentCellInfo.getTile2Info().getType();

        var keyVisible = "CONTAINER".equals(type) || "DOOR".equals(type);
        isLockedLabel.setVisible(keyVisible);
        isLockedCB.setVisible(keyVisible);
        isCodeLockLabel.setVisible(keyVisible);
        isCodeLockCB.setVisible(keyVisible);
        keyIdLabel.setVisible(keyVisible);
        keyIdTF.setVisible(keyVisible);
        lockLevelLabel.setVisible(keyVisible);
        lockLevelTF.setVisible(keyVisible);

        isTrapLabel.setVisible(keyVisible);
        isTrapCB.setVisible(keyVisible);
        trapLevelLabel.setVisible(keyVisible);
        trapLevelTF.setVisible(keyVisible);

        if (keyVisible) {
            ClosableCellInfo closableCellInfo = (ClosableCellInfo) cellInfo;
            isLockedCB.setSelected(closableCellInfo.isLocked());
            lockLevelTF.setText(String.valueOf(closableCellInfo.getLockLevel()));
            keyIdTF.setText(String.valueOf(closableCellInfo.getKeyId()));

            isCodeLockCB.setSelected(closableCellInfo.isCodeLock());
            if (isCodeLockCB.isSelected()) {
                lockLevelTF.setText(String.valueOf(closableCellInfo.getCharsForLock()).toUpperCase());
                keyIdTF.setText(String.valueOf(closableCellInfo.getCodeForLock()).toUpperCase());
                codeLockHintTF.setText(String.valueOf(closableCellInfo.getCodeHint()).toUpperCase());

                codeLockHintLabel.setVisible(true);
                codeLockHintTF.setVisible(true);
            } else {
                lockLevelTF.setText(String.valueOf(closableCellInfo.getLockLevel()));
                keyIdTF.setText(String.valueOf(closableCellInfo.getKeyId()));

                codeLockHintLabel.setVisible(false);
                codeLockHintTF.setVisible(false);
            }

            isTrapCB.setSelected(closableCellInfo.isTrap());
            trapLevelTF.setText(String.valueOf(closableCellInfo.getTrapLevel()));
        }
    }

    private static void saveTile() {
        currentCellInfo.setDesc(descTF.getText());
        var type = currentCellInfo.getTile2Info().getType();
        if ("CONTAINER".equals(type) || "DOOR".equals(type)) {
            ClosableCellInfo closableCellInfo = (ClosableCellInfo) currentCellInfo;
            closableCellInfo.setLocked(isLockedCB.isSelected());
            closableCellInfo.setCodeLock(isCodeLockCB.isSelected());
            if (isCodeLockCB.isSelected()) {
                closableCellInfo.setCodeForLock(keyIdTF.getText().isEmpty() ? null : keyIdTF.getText().toUpperCase());
                closableCellInfo.setCharsForLock(lockLevelTF.getText().isEmpty() ? null : lockLevelTF.getText().toUpperCase());
                closableCellInfo.setCodeHint(codeLockHintTF.getText().isEmpty() ? null : codeLockHintTF.getText().toUpperCase());
            } else {
                closableCellInfo.setKeyId(keyIdTF.getText().isEmpty() ? null : Integer.parseInt(keyIdTF.getText()));
                closableCellInfo.setLockLevel(lockLevelTF.getText().isEmpty() ? null : Integer.parseInt(lockLevelTF.getText()));
            }

            closableCellInfo.setTrap(isTrapCB.isSelected());
            if (isTrapCB.isSelected()) {
                closableCellInfo.setTrapLevel(trapLevelTF.getText().isEmpty() ? null : Integer.parseInt(trapLevelTF.getText()));
            }

            currentCellInfo = closableCellInfo;
        }
        hidePanel();
    }

    private static void hidePanel() {
        pane.setVisible(false);
    }
}
