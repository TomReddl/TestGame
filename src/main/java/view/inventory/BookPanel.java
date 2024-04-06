package view.inventory;

import controller.CharactersController;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import model.entity.GameModeEnum;
import model.entity.WorldLangEnum;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import view.Game;
import game.GameParams;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Панель чтения книг/свитков/записок
 */
public class BookPanel {
    @Getter
    private static final Pane pane;
    private static final ImageView backgroundImage;
    private static final ScrollPane scrollPane = new ScrollPane();
    private static ImageView leftImage;
    private static ImageView rightImage;
    private static final int bookSizeX = 600; // ширина книги в пикселях
    private static final int bookSizeY = 400; // высота книги в пикселях
    private static final Label leftPageLabel = new Label();
    private static final Label scrollLabel = new Label();
    private static final Label rightPageLabel = new Label();
    private static final Label previousPageLabel = new Label(Game.getText("PREV_PAGE"));
    private static final Label nextPageLabel = new Label(Game.getText("NEXT_PAGE"));
    private static final Label closeLabel = new Label(Game.getText("CLOSE_BOOK"));
    private static Document document = null;
    private static int currentPageNumber = 1;

    static {
        Font bookFont = Font.font("Arial", FontWeight.BOLD, 12);
        InputStream fontStream = BookPanel.class.getResourceAsStream("/fonts/KingthingsPetrock.ttf");
        if (fontStream != null) {
            try {
                Font.loadFont(fontStream, 36);
                bookFont = Font.font("Kingthings Petrock", FontWeight.BOLD, 14);
                fontStream.close();
            } catch (IOException ex) {
                throw new RuntimeException("Не удалось загрузить шрифт для книг=%s" + ex.getMessage());
            }
        }

        pane = new Pane();
        pane.setPrefSize(bookSizeX, bookSizeY + 30);
        pane.setLayoutX(5);
        pane.setLayoutY(5);
        pane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        pane.setVisible(false);

        backgroundImage = new ImageView();
        pane.getChildren().add(backgroundImage);

        scrollPane.setLayoutX(50);
        scrollPane.setLayoutY(100);
        scrollPane.setContent(scrollLabel);
        scrollPane.setVisible(false);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        pane.getChildren().add(scrollPane);

        leftImage = new ImageView();
        leftImage.setX(35);
        leftImage.setY(35);
        pane.getChildren().add(leftImage);

        rightImage = new ImageView();
        rightImage.setX(335);
        rightImage.setY(35);
        pane.getChildren().add(rightImage);

        leftPageLabel.setFont(bookFont);
        leftPageLabel.setLayoutX(25);
        leftPageLabel.setLayoutY(25);
        leftPageLabel.setWrapText(true);
        leftPageLabel.setTextFill(Color.web("#4a3710"));
        pane.getChildren().add(leftPageLabel);

        rightPageLabel.setFont(bookFont);
        rightPageLabel.setLayoutX(bookSizeX / 2 + 15);
        rightPageLabel.setLayoutY(25);
        rightPageLabel.setWrapText(true);
        rightPageLabel.setTextFill(Color.web("#4a3710"));
        pane.getChildren().add(rightPageLabel);

        scrollLabel.setFont(bookFont);
        scrollLabel.setTextFill(Color.web("#4a3710"));
        scrollLabel.setWrapText(true);

        previousPageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        previousPageLabel.setLayoutX(25);
        previousPageLabel.setLayoutY(bookSizeY - 10);
        previousPageLabel.setOnMouseClicked(event -> showPreviousPage());
        previousPageLabel.setTextFill(Color.BLACK);
        pane.getChildren().add(previousPageLabel);

        nextPageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nextPageLabel.setLayoutX(bookSizeX / 2 + 15);
        nextPageLabel.setLayoutY(bookSizeY - 10);
        nextPageLabel.setOnMouseClicked(event -> showNextPage());
        nextPageLabel.setTextFill(Color.BLACK);
        pane.getChildren().add(nextPageLabel);

        closeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        closeLabel.setLayoutX(500);
        closeLabel.setLayoutY(bookSizeY - 10);
        closeLabel.setOnMouseClicked(event -> closeBookPanelAndShowInvent());
        closeLabel.setTextFill(Color.BLACK);
        pane.getChildren().add(closeLabel);

        Game.getRoot().getChildren().add(pane);
    }

    public static void showBookPanel(int bookTypeId) {
        currentPageNumber = 1;
        pane.setVisible(true);
        loadBook(bookTypeId);
    }

    public static void closeBookPanelAndShowInvent() {
        pane.setVisible(false);
        if (!Game.getGameMode().equals(GameModeEnum.EDITOR)) {
            Game.getGameMenu().showGameMenuPanel("0");
        }
        Game.hideMessage();
    }

    public static void closeBookPanel() {
        BookPanel.getPane().setVisible(false);
        Game.hideMessage();
    }

    public static void loadBook(int bookTypeId) {
        var path = "/" + BookPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/objects/books/" +
                bookTypeId + "_" + GameParams.lang.toString().toLowerCase() + ".xml";
        File file = new File(path);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            if (file.exists()) {
                document = documentBuilder.parse(file);

                if (Game.getMap().getSelecterCharacter().getKnowledgeInfo().getLangs().contains(WorldLangEnum.valueOf(document.getElementsByTagName("lang").item(0).getTextContent()))) {

                    /*  повышаем навык, если
                     *  1) персонаж знает язык книги
                     *  2) в книге есть знания для повышения навыка
                     *  3) персонаж еще не читал эту книгу
                     */
                    var increaseSkill = document.getElementsByTagName("increaseSkill").item(0);
                    if (increaseSkill != null &&
                            !Game.getMap().getSelecterCharacter().getKnowledgeInfo().getReadBooks().contains(bookTypeId)) {
                        CharactersController.increaseSkill(increaseSkill.getTextContent(), 1);
                    }

                    // добавляем книгу в список прочтенных
                    Game.getMap().getSelecterCharacter().getKnowledgeInfo().getReadBooks().add(bookTypeId);
                }

                var type = document.getElementsByTagName("type").item(0).getTextContent();
                backgroundImage.setImage(new Image("/graphics/gui/books/" + type + ".png"));
                pane.setPrefSize(backgroundImage.getImage().getWidth(), backgroundImage.getImage().getHeight());
                closeLabel.setLayoutX(backgroundImage.getImage().getWidth() - closeLabel.getWidth() - 25);
                if (type.startsWith("book")) { // книга
                    previousPageLabel.setVisible(true);
                    nextPageLabel.setVisible(true);
                    scrollPane.setVisible(false);
                    leftPageLabel.setLayoutY(25);
                    showPages(document, currentPageNumber);
                } else if (type.startsWith("note")) { // записка
                    showNote(document);
                } else if (type.startsWith("scroll")) { // свиток
                    showScroll(document);
                }
            } else {
                closeBookPanelAndShowInvent();
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void showNote(Document document) {
        var text = document.getElementsByTagName("pages").item(0).getChildNodes().item(1).getTextContent();

        if (text.startsWith("IMG_")) {
            leftImage.setVisible(true);
            var path = "/graphics/booksImages/" + text.replaceAll("IMG_", "") + ".png";
            var localized_path = "/graphics/booksImages/" + text.replaceAll("IMG_", "") + "_" +
                    GameParams.lang.toString().toLowerCase() + ".png";
            if (new File("/" + BookPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath() + localized_path).exists()) {
                leftImage.setImage(new Image(localized_path));
            } else {
                leftImage.setImage(new Image(path));
            }

            text = document.getElementsByTagName("pages").item(0).getChildNodes().item(3).getTextContent();

            leftPageLabel.setLayoutY(leftImage.getImage().getHeight() + 10);
        } else {
            leftImage.setVisible(false);
        }

        leftPageLabel.setVisible(true);
        rightPageLabel.setVisible(false);

        if (Game.getMap().getSelecterCharacter().getKnowledgeInfo().getLangs().contains(WorldLangEnum.valueOf(document.getElementsByTagName("lang").item(0).getTextContent()))) {
            leftPageLabel.setText(text);
        } else {
            leftPageLabel.setText(getForeignLanguageText(text));
        }

        leftPageLabel.setMaxWidth(250);
        rightImage.setVisible(false);
        previousPageLabel.setVisible(false);
        nextPageLabel.setVisible(false);
        scrollPane.setVisible(false);
    }

    public static void showScroll(Document document) {
        leftImage.setVisible(false);
        rightImage.setVisible(false);
        previousPageLabel.setVisible(false);
        nextPageLabel.setVisible(false);
        leftPageLabel.setVisible(false);
        rightPageLabel.setVisible(false);

        var text = document.getElementsByTagName("pages").item(0).getChildNodes().item(1).getTextContent();

        if (Game.getMap().getSelecterCharacter().getKnowledgeInfo().getLangs().contains(WorldLangEnum.valueOf(document.getElementsByTagName("lang").item(0).getTextContent()))) {
            scrollLabel.setText(text);
        } else {
            scrollLabel.setText(getForeignLanguageText(text));
        }

        scrollLabel.setMaxWidth(220);
        scrollLabel.setVisible(true);
        scrollPane.setVisible(true);
        scrollPane.setPrefSize(bookSizeX / 2 - 50, bookSizeY - 150);
    }

    public static void showNextPage() {
        showPages(document, ++currentPageNumber);
    }

    public static void showPreviousPage() {
        showPages(document, --currentPageNumber);
    }

    private static void showPages(Document document, int pageNumber) {
        if (document.getElementsByTagName("type").item(0).getTextContent().startsWith("book")) {
            var rightText = "";
            var leftText = "";
            var leftPage = document.getElementsByTagName("pages").item(0).getChildNodes().item(1 + (pageNumber - 1) * 4);
            var rightPage = document.getElementsByTagName("pages").item(0).getChildNodes().item(3 + (pageNumber - 1) * 4);
            if (rightPage != null && leftPage != null) {
                leftText = leftPage.getTextContent();
                rightText = rightPage.getTextContent();
            } else {
                pageNumber = pageNumber <= 1 ? ++currentPageNumber : --currentPageNumber;
                leftText = document.getElementsByTagName("pages").item(0).getChildNodes().item(1 + (pageNumber - 1) * 4).getTextContent();
                rightText = document.getElementsByTagName("pages").item(0).getChildNodes().item(3 + (pageNumber - 1) * 4).getTextContent();
            }

            if (!leftText.startsWith("IMG_")) {
                leftPageLabel.setVisible(true);
                if (Game.getMap().getSelecterCharacter().getKnowledgeInfo().getLangs().contains(WorldLangEnum.valueOf(document.getElementsByTagName("lang").item(0).getTextContent()))) {
                    leftPageLabel.setText(leftText);
                } else {
                    leftPageLabel.setText(getForeignLanguageText(leftText));
                }
                leftPageLabel.setMaxWidth(250);
                leftImage.setVisible(false);
            } else {
                leftImage.setVisible(true);
                var path = "/graphics/booksImages/" + leftText.replaceAll("IMG_", "") + ".png";
                var localized_path = "/graphics/booksImages/" + leftText.replaceAll("IMG_", "") + "_" +
                        GameParams.lang.toString().toLowerCase() + ".png";
                if (new File("/" + BookPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath() + localized_path).exists()) {
                    leftImage.setImage(new Image(localized_path));
                } else {
                    leftImage.setImage(new Image(path));
                }
                leftPageLabel.setVisible(false);
            }

            if (!rightText.startsWith("IMG_")) {
                rightPageLabel.setVisible(true);
                if (Game.getMap().getSelecterCharacter().getKnowledgeInfo().getLangs().contains(WorldLangEnum.valueOf(document.getElementsByTagName("lang").item(0).getTextContent()))) {
                    rightPageLabel.setText(rightText);
                } else {
                    rightPageLabel.setText(getForeignLanguageText(rightText));
                }
                rightPageLabel.setMaxWidth(250);
                rightImage.setVisible(false);
            } else {
                rightImage.setVisible(true);
                var path = "/graphics/booksImages/" + rightText.replaceAll("IMG_", "") + ".png";
                var localized_path = "/graphics/booksImages/" + rightText.replaceAll("IMG_", "") + "_" +
                        GameParams.lang.toString().toLowerCase() + ".png";
                if (new File("/" + BookPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath() + localized_path).exists()) {
                    rightImage.setImage(new Image(localized_path));
                } else {
                    rightImage.setImage(new Image(path));
                }
                rightPageLabel.setVisible(false);
            }
        }
    }

    // шифрует текст, если персонаж не знает языка, на котором он написан
    private static String getForeignLanguageText(String text) {
        Game.showMessage(Game.getText("ERROR_BOOK_FOREIGN_LANG"));
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(Character.toString(c + 15));
        }
        return sb.toString();
    }
}
