package menu;

import entity.GameModeEnum;
import game.Main;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MainMenu {
    private final Pane menuPane = new Pane();
    private final ImageView backgroundImage = new ImageView("/graphics/gui/Background.png");
    private final Button continueButton = new Button("Продолжить");
    private final Button newGameButton = new Button("Новая игра");
    private final Button loadGameButton = new Button("Загрузить игру");
    private final Button editorButton = new Button("Редактор");
    private final Button settingsButton = new Button("Настройки");
    private final Button exitButton = new Button("Выход");

    private final int gameMenuPosX = 230;
    private final int gameMenuPosY = 200;

    public MainMenu(Group root) {
        menuPane.getChildren().add(backgroundImage);
        menuPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        continueButton.setLayoutX(20);
        continueButton.setLayoutY(20);
        continueButton.setPrefWidth(100);
        menuPane.getChildren().add(continueButton);

        newGameButton.setLayoutX(20);
        newGameButton.setLayoutY(50);
        newGameButton.setPrefWidth(100);
        menuPane.getChildren().add(newGameButton);

        loadGameButton.setLayoutX(20);
        loadGameButton.setLayoutY(80);
        loadGameButton.setPrefWidth(100);
        menuPane.getChildren().add(loadGameButton);

        editorButton.setLayoutX(20);
        editorButton.setLayoutY(110);
        editorButton.setPrefWidth(100);
        editorButton.setOnAction(event -> startEditor());
        menuPane.getChildren().add(editorButton);

        settingsButton.setLayoutX(20);
        settingsButton.setLayoutY(140);
        settingsButton.setPrefWidth(100);
        menuPane.getChildren().add(settingsButton);

        exitButton.setLayoutX(20);
        exitButton.setLayoutY(170);
        exitButton.setPrefWidth(100);
        exitButton.setOnAction(event -> closeGame());
        menuPane.getChildren().add(exitButton);

        menuPane.setLayoutX(0);
        menuPane.setLayoutY(0);
        menuPane.setPrefSize(140, 210);

        root.getChildren().add(menuPane);
    }

    public void setMenuMode(GameModeEnum gameMode) {
        if (gameMode.equals(GameModeEnum.MAIN_MENU)) {
            menuPane.setLayoutX(0);
            menuPane.setLayoutY(0);
            backgroundImage.setVisible(Boolean.TRUE);
        } else {
            menuPane.setLayoutX(gameMenuPosY);
            menuPane.setLayoutY(gameMenuPosY);
            backgroundImage.setVisible(Boolean.FALSE);
        }
    }

    public void startEditor() {
        setMenuMode(GameModeEnum.EDITOR);
        menuPane.setVisible(Boolean.FALSE);
    }

    public void closeGame() {
        System.exit(0);
    }
}
