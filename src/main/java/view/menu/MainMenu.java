package view.menu;

import model.entity.GameModeEnum;
import view.Game;
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

/*
 * Главное меню
 */
@Setter
@Getter
public class MainMenu {
    @Getter
    private static final Pane pane = new Pane();
    private final ImageView backgroundImage = new ImageView("/graphics/gui/Background.png");
    private final Button continueButton = new Button(Game.getText("CONTINUE"));
    private final Button newGameButton = new Button(Game.getText("NEW_GAME"));
    private final Button loadGameButton = new Button(Game.getText("LOAD_GAME"));
    private final Button editorButton = new Button(Game.getText("EDITOR"));
    private final Button settingsButton = new Button(Game.getText("SETTINGS"));
    private final Button exitButton = new Button(Game.getText("EXIT"));
    private final GameSettingsPanel settingsPanel = new GameSettingsPanel();

    private final int gameMenuPosX = 230;
    private final int gameMenuPosY = 200;

    public MainMenu(Group root) {
        pane.getChildren().add(backgroundImage);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        continueButton.setLayoutX(20);
        continueButton.setLayoutY(20);
        continueButton.setPrefWidth(100);
        pane.getChildren().add(continueButton);

        newGameButton.setLayoutX(20);
        newGameButton.setLayoutY(50);
        newGameButton.setPrefWidth(100);
        pane.getChildren().add(newGameButton);

        loadGameButton.setLayoutX(20);
        loadGameButton.setLayoutY(80);
        loadGameButton.setPrefWidth(100);
        pane.getChildren().add(loadGameButton);

        editorButton.setLayoutX(20);
        editorButton.setLayoutY(110);
        editorButton.setPrefWidth(100);
        editorButton.setOnAction(event -> Game.setGameMode(GameModeEnum.EDITOR));
        pane.getChildren().add(editorButton);

        settingsButton.setLayoutX(20);
        settingsButton.setLayoutY(140);
        settingsButton.setPrefWidth(100);
        settingsButton.setOnAction(event -> settingsPanel.viewSettingsPanel());
        pane.getChildren().add(settingsButton);

        exitButton.setLayoutX(20);
        exitButton.setLayoutY(170);
        exitButton.setPrefWidth(100);
        exitButton.setOnAction(event -> closeGame());
        pane.getChildren().add(exitButton);

        pane.setLayoutX(0);
        pane.setLayoutY(0);
        pane.setPrefSize(140, 210);

        root.getChildren().add(pane);
    }

    public void closeGame() {
        System.exit(0);
    }
}
