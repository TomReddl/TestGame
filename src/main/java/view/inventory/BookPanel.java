package view.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import view.Game;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/*
 * Панель чтения книг/записок
 */
public class BookPanel {
    @Getter
    private static final Pane pane;
    private static final ImageView backgroundImage;
    private static ImageView leftImage;
    private static ImageView rightImage;
    private static final int bookSizeX = 600; // ширина книги в пикселях
    private static final int bookSizeY = 400; // высота книги в пикселях
    private static final Label leftPageLabel = new Label();
    private static final Label rightPageLabel = new Label();
    private static final Label previousPageLabel = new Label(Game.getText("PREV_PAGE"));
    private static final Label nextPageLabel = new Label(Game.getText("NEXT_PAGE"));
    private static final Label closeLabel = new Label(Game.getText("CLOSE"));
    private static Document document = null;
    private static int currentPageNumber = 1;

    static {
        pane = new Pane();
        pane.setPrefSize(bookSizeX, bookSizeY + 30);
        pane.setLayoutX(5);
        pane.setLayoutY(5);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(Boolean.FALSE);

        backgroundImage = new ImageView("/graphics/gui/books/background1.png");
        backgroundImage.setFitWidth(bookSizeX);
        backgroundImage.setFitHeight(bookSizeY);
        pane.getChildren().add(backgroundImage);

        leftImage = new ImageView();
        leftImage.setX(25);
        leftImage.setY(25);
        pane.getChildren().add(leftImage);

        rightImage = new ImageView();
        rightImage.setX(325);
        rightImage.setY(25);
        pane.getChildren().add(rightImage);

        leftPageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        leftPageLabel.setLayoutX(25);
        leftPageLabel.setLayoutY(25);
        pane.getChildren().add(leftPageLabel);

        rightPageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        rightPageLabel.setLayoutX(bookSizeX / 2 + 15);
        rightPageLabel.setLayoutY(25);
        pane.getChildren().add(rightPageLabel);

        previousPageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        previousPageLabel.setLayoutX(25);
        previousPageLabel.setLayoutY(bookSizeY + 5);
        previousPageLabel.setOnMouseClicked(event -> showPages(document, --currentPageNumber));
        pane.getChildren().add(previousPageLabel);

        nextPageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nextPageLabel.setLayoutX(bookSizeX / 2);
        nextPageLabel.setLayoutY(bookSizeY + 5);
        nextPageLabel.setOnMouseClicked(event -> showPages(document, ++currentPageNumber));
        pane.getChildren().add(nextPageLabel);

        closeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        closeLabel.setLayoutX(500);
        closeLabel.setLayoutY(bookSizeY + 5);
        closeLabel.setOnMouseClicked(event -> closeBookPanel());
        pane.getChildren().add(closeLabel);
    }

    public static void showBookPanel(int bookTypeId) {
        currentPageNumber = 1;
        BookPanel.getPane().setVisible(true);
        Game.getGameMenu().showGameMenuPanel("0");
        loadBook(bookTypeId);
    }

    public static void closeBookPanel() {
        BookPanel.getPane().setVisible(false);
        Game.getGameMenu().showGameMenuPanel("0");
    }

    public static void loadBook(int bookTypeId) {
        var path = "/" + BookPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/objects/books/" + bookTypeId + ".xml";
        File file = new File(path);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            if (file.exists()) {
                document = documentBuilder.parse(file);
                showPages(document, currentPageNumber);
            } else {
                closeBookPanel();
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void showPages(Document document, int pageNumber) {
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
            leftPageLabel.setText(leftText);
            leftImage.setVisible(false);
        } else {
            leftImage.setVisible(true);
            leftImage.setImage(new Image("/graphics/booksImages/" + leftText.replaceAll("IMG_", "") + ".png"));
            leftPageLabel.setVisible(false);
        }

        if (!rightText.startsWith("IMG_")) {
            rightPageLabel.setVisible(true);
            rightPageLabel.setText(rightText);
            rightImage.setVisible(false);
        } else {
            rightImage.setVisible(true);
            rightImage.setImage(new Image("/graphics/booksImages/" + rightText.replaceAll("IMG_", "") + ".png"));
            rightPageLabel.setVisible(false);
        }
    }
}
