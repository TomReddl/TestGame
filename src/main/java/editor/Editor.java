package editor;

import entity.ItemType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Editor {

    private ImageView border;
    private final Pane pane1 = new Pane();
    private final Pane pane2 = new Pane();
    private final Pane pane3 = new Pane();
    private final Pane pane4 = new Pane();
    private final Pane pane5 = new Pane();
    private final Pane itemsPane = new Pane();

    private int selectTile = 0;
    private EditorObjectType selectedType = EditorObjectType.GROUND;
    private TilesList tilesList;
    private NPCList npcList;
    private CreatureList creatureList;
    private ItemsList itemsList;

    public Editor() {
        tilesList = new TilesList();
        npcList = new NPCList();
        creatureList = new CreatureList();
        itemsList = new ItemsList();
    }


    public void drawTiles(Group root) {
        TabPane tabPane = new TabPane();
        tabPane.setLayoutX(630);
        tabPane.setPrefSize(370, 620);
        tabPane.getTabs().add(new Tab("Тайлы"));
        tabPane.getTabs().add(new Tab("Объекты"));
        tabPane.getTabs().add(new Tab("Персонажи"));
        tabPane.getTabs().add(new Tab("Существа"));
        tabPane.getTabs().add(new Tab("Предметы"));
        tabPane.getTabs().get(0).setClosable(false);
        tabPane.getTabs().get(1).setClosable(false);
        tabPane.getTabs().get(2).setClosable(false);
        tabPane.getTabs().get(3).setClosable(false);
        tabPane.getTabs().get(4).setClosable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(5);
        scrollPane.setPrefSize(180, 600);
        for (int i = 0; i < tilesList.getTiles1Count(); i++) {
            ImageView tile = new ImageView("/graphics/tiles/" + i + ".png");
            tile.setX(5 + (i / 13) * 45);
            tile.setY(5 + (i) * 45 - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                setBorder(pane1);
                selectTile = tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.GROUND;
                border.setX(tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tilesList.getTiles1().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tilesList.getTiles1().get(i).setImage(tile);
            pane1.getChildren().add(tile);
        }
        border = new javafx.scene.image.ImageView("/graphics/gui/Border.png");
        border.setX(tilesList.getTiles1().get(0).getImage().getX() - 1);
        border.setY(tilesList.getTiles1().get(0).getImage().getY() - 1);
        pane1.getChildren().add(border);
        scrollPane.setContent(pane1);
        tabPane.getTabs().get(0).setContent(scrollPane);

        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setLayoutX(190);
        scrollPane2.setPrefSize(180, 600);

        for (int i = 0; i < tilesList.getTiles2Count(); i++) {
            ImageView tile = new ImageView("/graphics/tiles2/" + i + ".png");
            tile.setX(5 + (i / 13) * 45);
            tile.setY(5 + (i) * 45 - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                setBorder(pane2);
                selectTile = tilesList.getTiles2().get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.OBJECT;
                border.setX(tilesList.getTiles2().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tilesList.getTiles2().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tilesList.getTiles2().get(i).setImage(tile);
            pane2.getChildren().add(tile);
        }
        scrollPane2.setContent(pane2);
        tabPane.getTabs().get(1).setContent(scrollPane2);

        ScrollPane scrollPane3 = new ScrollPane();
        scrollPane3.setLayoutX(190);
        scrollPane3.setPrefSize(180, 600);

        for (int i = 0; i < npcList.getNpcCount(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/characters/" + i + ".png");
            }
            tile.setX(5 + (i / 13) * 45);
            tile.setY(5 + (i) * 45 - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                setBorder(pane3);
                selectTile = npcList.getNpc().get(Integer.parseInt(tile.getId())).getImageId();
                selectedType = EditorObjectType.NPC;
                border.setX(npcList.getNpc().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(npcList.getNpc().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            npcList.getNpc().get(i).setImage(tile);
            pane3.getChildren().add(tile);
        }
        scrollPane3.setContent(pane3);
        tabPane.getTabs().get(2).setContent(scrollPane3);

        ScrollPane scrollPane4 = new ScrollPane();
        scrollPane4.setLayoutX(190);
        scrollPane4.setPrefSize(180, 600);

        for (int i = 0; i < creatureList.getCreaturesCount(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/creatures/" + i + ".png");
            }
            tile.setFitWidth(40);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * 45);
            tile.setY(5 + (i) * 45 - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                setBorder(pane4);
                selectTile = creatureList.getCreatures().get(Integer.parseInt(tile.getId())).getImageId();
                selectedType = EditorObjectType.CREATURE;
                border.setX(creatureList.getCreatures().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(creatureList.getCreatures().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            creatureList.getCreatures().get(i).setImage(tile);
            pane4.getChildren().add(tile);
        }
        scrollPane4.setContent(pane4);
        tabPane.getTabs().get(3).setContent(scrollPane4);

        ScrollPane scrollPane5 = new ScrollPane();
        scrollPane5.setLayoutX(190);
        scrollPane5.setPrefSize(180, 600);

        TextField searchItemTextField = new TextField();
        searchItemTextField.setLayoutX(5);
        searchItemTextField.setLayoutY(5);
        searchItemTextField.setPromptText("Название предмета");
        pane5.getChildren().add(searchItemTextField);

        TabPane itemsTabPane = new TabPane();
        itemsTabPane.setLayoutX(5);
        itemsTabPane.setLayoutY(35);
        itemsTabPane.setPrefSize(350, 620);
        for (ItemType itemType : ItemType.values()) {
            Tab tab = new Tab(itemType.getDesc());
            tab.setClosable(Boolean.FALSE);
            itemsTabPane.getTabs().add(tab);
        }
        itemsTabPane.getTabs().get(0).setContent(itemsPane);
        itemsTabPane.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, tab, t1) -> {
                    tab.setContent(null);
                    t1.setContent(itemsPane);
                    filterItems(t1.getText());
                }
        );
        pane5.getChildren().add(itemsTabPane);

        for (int i = 0; i < itemsList.getItemsCount(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/items/icons/" + i + ".png");
            }
            tile.setFitWidth(40);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * 45);
            tile.setY(5 + (i) * 45 - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMousePressed(event -> {
                setBorder(itemsPane);
                selectTile = itemsList.getItems().get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.ITEM;
                border.setX(itemsList.getItems().get(Integer.parseInt(tile.getId())).getIcon().getX() - 1);
                border.setY(itemsList.getItems().get(Integer.parseInt(tile.getId())).getIcon().getY() - 1);
            });

            itemsList.getItems().get(i).setIcon(tile);
            if (i > 0) {
                itemsList.getItems().get(i).setImage(new ImageView("/graphics/items/" + i + ".png"));
            }
            itemsPane.getChildren().add(tile);
        }
        scrollPane5.setContent(pane5);
        tabPane.getTabs().get(4).setContent(scrollPane5);

        root.getChildren().add(tabPane);
    }

    private void setBorder(Pane pane) {
        if (pane1.getChildren().contains(border)) {
            pane1.getChildren().remove(border);
        } else if (pane2.getChildren().contains(border)) {
            pane2.getChildren().remove(border);
        } else if (pane3.getChildren().contains(border)) {
            pane3.getChildren().remove(border);
        } else if (pane4.getChildren().contains(border)) {
            pane4.getChildren().remove(border);
        } else if (itemsPane.getChildren().contains(border)) {
            itemsPane.getChildren().remove(border);
        }
        pane.getChildren().add(border);
    }

    private void filterItems(String itemType) {
        ItemType type = ItemType.getItemTypeByCode(itemType);
        int i = 1;
        for (Node tile : itemsPane.getChildren()) {
            ImageView itemTile = (ImageView) tile;
            List<ItemType> types = itemsList.getItems().get(Integer.parseInt(itemTile.getId())).getTypes();
            if (types != null) {
                itemTile.setVisible(types.contains(type));
                if (itemTile.isVisible()) {
                    itemTile.setX(5 + (i / 13) * 45);
                    itemTile.setY(5 + (i) * 45 - (i / 13) * 585);
                    i++;
                }
            }
        }
    }

}
