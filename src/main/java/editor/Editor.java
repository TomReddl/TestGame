package editor;

import entity.ItemTypeEnum;
import game.Game;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;
import utils.JsonUtils;

import java.util.List;

import static params.GameParams.*;

@Setter
@Getter
public class Editor {
    private final TabPane tabPane = new TabPane();
    private final Pane buttonsPane = new Pane();
    private ImageView border;
    private final Pane pane1 = new Pane();
    private final Pane pane2 = new Pane();
    private final Pane pane3 = new Pane();
    private final Pane pane4 = new Pane();
    private final Pane pane5 = new Pane();
    private final Pane pane6 = new Pane();
    private final Pane pane7 = new Pane();
    private final Pane itemsPane = new Pane();
    private final TextField searchItemTF = new TextField();
    private final TabPane itemsTabPane = new TabPane();
    private final Label showZonesLabel = new Label(Game.getText("SHOW_ZONES"));
    private int selectTile = 0;
    private boolean showZones = false;
    private EditorObjectType selectedType = EditorObjectType.GROUND;
    private List<TileInfo> tiles1 = JsonUtils.getTiles1();
    private List<TileInfo> tiles2 = JsonUtils.getTiles2();
    private List<NPCInfo> npcList = JsonUtils.getNPC();
    private List<CreatureInfo> creatureList = JsonUtils.getCreatures();
    private List<ItemInfo> itemsList = JsonUtils.getItems();
    private List<PollutionInfo> pollutionList = JsonUtils.getPollutions();
    private List<ZoneInfo> zonesList = JsonUtils.getZones();
    private Canvas canvas = new Canvas(screenSizeX, screenSizeY); // размеры игрового окна

    public Editor(Group root) {
        root.getChildren().add(canvas);
        drawTiles(root);
    }


    private void drawTiles(Group root) {
        buttonsPane.setLayoutX(5);
        buttonsPane.setLayoutY(610);
        root.getChildren().add(buttonsPane);

        tabPane.setLayoutX(630);
        tabPane.setPrefSize(460, 620);
        tabPane.getTabs().add(new Tab(Game.getText("TILES")));
        tabPane.getTabs().add(new Tab(Game.getText("OBJECTS")));
        tabPane.getTabs().add(new Tab(Game.getText("CHARACTERS")));
        tabPane.getTabs().add(new Tab(Game.getText("CREATURES")));
        tabPane.getTabs().add(new Tab(Game.getText("ITEMS")));
        tabPane.getTabs().add(new Tab(Game.getText("POLLUTIONS")));
        tabPane.getTabs().add(new Tab(Game.getText("ZONES")));
        tabPane.getTabs().get(0).setClosable(false);
        tabPane.getTabs().get(1).setClosable(false);
        tabPane.getTabs().get(2).setClosable(false);
        tabPane.getTabs().get(3).setClosable(false);
        tabPane.getTabs().get(4).setClosable(false);
        tabPane.getTabs().get(5).setClosable(false);
        tabPane.getTabs().get(6).setClosable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(5);
        scrollPane.setPrefSize(180, 600);
        for (int i = 0; i < tiles1.size(); i++) {
            ImageView tile = new ImageView("/graphics/tiles/" + i + ".png");
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane1);
                selectTile = tiles1.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.GROUND;
                border.setX(tiles1.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tiles1.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tiles1.get(i).setImage(tile);
            tiles1.get(i).setName(Game.getTiles1Text(i + "NAME"));
            tiles1.get(i).setDesc(Game.getTiles1Text(i + "DESC"));
            pane1.getChildren().add(tile);
        }
        border = new javafx.scene.image.ImageView("/graphics/gui/Border.png");
        border.setX(tiles1.get(0).getImage().getX() - 1);
        border.setY(tiles1.get(0).getImage().getY() - 1);
        pane1.getChildren().add(border);
        scrollPane.setContent(pane1);
        tabPane.getTabs().get(0).setContent(scrollPane);

        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setLayoutX(190);
        scrollPane2.setPrefSize(180, 600);

        for (int i = 0; i < tiles2.size(); i++) {
            ImageView tile = new ImageView("/graphics/tiles2/" + i + ".png");
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane2);
                selectTile = tiles2.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.OBJECT;
                border.setX(tiles2.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(tiles2.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            tiles2.get(i).setImage(tile);
            tiles2.get(i).setName(Game.getTiles2Text(i + "NAME"));
            tiles2.get(i).setDesc(Game.getTiles2Text(i + "DESC"));

            if (tiles2.get(i).isTwoLayer()) {
                ImageView upTile = new ImageView("/graphics/tiles2/" + i + ".up.png");
                tiles2.get(i).setUpLayerImage(upTile);
            }
            pane2.getChildren().add(tile);
        }
        scrollPane2.setContent(pane2);
        tabPane.getTabs().get(1).setContent(scrollPane2);

        ScrollPane scrollPane3 = new ScrollPane();
        scrollPane3.setLayoutX(190);
        scrollPane3.setPrefSize(180, 600);

        for (int i = 0; i < npcList.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/characters/" + i + ".png");
            }
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane3);
                selectTile = npcList.get(Integer.parseInt(tile.getId())).getImageId();
                selectedType = EditorObjectType.NPC;
                border.setX(npcList.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(npcList.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            npcList.get(i).setImage(tile);
            pane3.getChildren().add(tile);
        }
        scrollPane3.setContent(pane3);
        tabPane.getTabs().get(2).setContent(scrollPane3);

        ScrollPane scrollPane4 = new ScrollPane();
        scrollPane4.setLayoutX(190);
        scrollPane4.setPrefSize(180, 600);

        for (int i = 0; i < creatureList.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/creatures/" + i + ".png");
            }
            tile.setFitWidth(tileSize);
            tile.setFitHeight(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane4);
                selectTile = creatureList.get(Integer.parseInt(tile.getId())).getImageId();
                selectedType = EditorObjectType.CREATURE;
                border.setX(creatureList.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(creatureList.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            creatureList.get(i).setImage(tile);
            pane4.getChildren().add(tile);
        }
        scrollPane4.setContent(pane4);
        tabPane.getTabs().get(3).setContent(scrollPane4);

        ScrollPane scrollPane5 = new ScrollPane();
        scrollPane5.setLayoutX(190);
        scrollPane5.setPrefSize(180, 600);

        searchItemTF.setLayoutX(5);
        searchItemTF.setLayoutY(5);
        searchItemTF.setPromptText(Game.getText("ITEM_NAME"));
        searchItemTF.setOnAction(event -> filterItems(
                itemsTabPane.getSelectionModel().getSelectedItem().getText(),
                searchItemTF.getText()));
        pane5.getChildren().add(searchItemTF);


        itemsTabPane.setLayoutX(5);
        itemsTabPane.setLayoutY(35);
        itemsTabPane.setPrefSize(430, 620);
        for (ItemTypeEnum itemType : ItemTypeEnum.values()) {
            Tab tab = new Tab(itemType.getDesc());
            tab.setClosable(Boolean.FALSE);
            itemsTabPane.getTabs().add(tab);
        }
        itemsTabPane.getTabs().get(0).setContent(itemsPane);
        itemsTabPane.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, tab, t1) -> {
                    tab.setContent(null);
                    t1.setContent(itemsPane);
                    filterItems(t1.getText(), searchItemTF.getText());
                }
        );
        pane5.getChildren().add(itemsTabPane);

        for (int i = 0; i < itemsList.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/items/icons/" + i + ".png");
            }
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(itemsPane);
                selectTile = itemsList.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.ITEM;
                border.setX(itemsList.get(Integer.parseInt(tile.getId())).getIcon().getX() - 1);
                border.setY(itemsList.get(Integer.parseInt(tile.getId())).getIcon().getY() - 1);
            });

            itemsList.get(i).setIcon(tile);
            itemsList.get(i).setName(Game.getItemsText(i + "NAME"));
            itemsList.get(i).setDesc(Game.getItemsText(i + "DESC"));
            if (i > 0) {
                itemsList.get(i).setImage(new ImageView("/graphics/items/" + i + ".png"));
            }
            itemsPane.getChildren().add(itemsList.get(i).getIcon());

            var itemName = itemsList.get(i).getName();
            itemName = itemName.length() > 8 ? itemName.substring(0, 8) : itemName;
            Label itemNameLabel = new Label(itemName);
            itemNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 8));
            itemNameLabel.setLayoutX(tile.getX());
            itemNameLabel.setLayoutY(tile.getY() + tileSize - 8);
            itemNameLabel.setId(String.valueOf(i));
            itemsList.get(i).setNameLabel(itemNameLabel);
            itemsPane.getChildren().add(itemsList.get(i).getNameLabel());

        }
        scrollPane5.setContent(pane5);
        tabPane.getTabs().get(4).setContent(scrollPane5);

        ScrollPane scrollPane6 = new ScrollPane();
        scrollPane6.setLayoutX(190);
        scrollPane6.setPrefSize(180, 600);

        for (int i = 0; i < pollutionList.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/pollutions/" + i + ".png");
            }
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane6);
                selectTile = pollutionList.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.POLLUTION;
                border.setX(pollutionList.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(pollutionList.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            pollutionList.get(i).setImage(tile);
            pane6.getChildren().add(tile);
        }
        scrollPane6.setContent(pane6);
        tabPane.getTabs().get(5).setContent(scrollPane6);

        ScrollPane scrollPane7 = new ScrollPane();
        scrollPane7.setLayoutX(190);
        scrollPane7.setPrefSize(180, 600);

        CheckBox showZonesCheckBox = new CheckBox();
        showZonesCheckBox.setLayoutX(5);
        showZonesCheckBox.setLayoutY(5);
        showZonesCheckBox.setOnAction(event -> changeShowZones());

        HBox box = new HBox();
        box.setLayoutX(5);
        box.setLayoutY(5);
        box.getChildren().addAll(showZonesCheckBox, showZonesLabel);
        box.setSpacing(5);

        pane7.getChildren().add(box);

        for (int i = 0; i < zonesList.size(); i++) {
            ImageView tile;
            if (i == 0) {
                tile = new ImageView("/graphics/gui/Delete.png");
            } else {
                tile = new ImageView("/graphics/zones/" + i + ".png");
            }
            tile.setFitWidth(tileSize);
            tile.setPreserveRatio(true);
            tile.setSmooth(true);
            tile.setCache(true);
            tile.setX(5 + (i / 13) * (tileSize + 5));
            tile.setY(35 + (i) * (tileSize + 5) - (i / 13) * 585);
            tile.setId(String.valueOf(i));
            tile.setOnMouseClicked(event -> {
                setBorder(pane7);
                selectTile = zonesList.get(Integer.parseInt(tile.getId())).getId();
                selectedType = EditorObjectType.ZONE;
                border.setX(zonesList.get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
                border.setY(zonesList.get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
            });

            zonesList.get(i).setImage(tile);
            pane7.getChildren().add(tile);
        }
        scrollPane7.setContent(pane7);
        tabPane.getTabs().get(6).setContent(scrollPane7);

        root.getChildren().add(tabPane);
    }

    private void changeShowZones() {
        showZones = !showZones;
        Game.getMap().drawCurrentMap();
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
        } else if (pane6.getChildren().contains(border)) {
            pane6.getChildren().remove(border);
        } else if (pane7.getChildren().contains(border)) {
            pane7.getChildren().remove(border);
        }
        pane.getChildren().add(border);
    }

    private void filterItems(String itemType, String searchString) {
        ItemTypeEnum type = ItemTypeEnum.getItemTypeByCode(itemType);
        int i = 1;
        boolean visible = false;
        for (ItemInfo itemInfo : itemsList) {
            ImageView itemTile = itemInfo.getIcon();
            List<ItemTypeEnum> types = itemInfo.getTypes();
            if (types != null) {
                visible = (types.contains(type) || ItemTypeEnum.ALL.equals(type)) &&
                        ((itemInfo.getDesc().toLowerCase().contains(searchString.toLowerCase())) ||
                                (itemInfo.getName().toLowerCase().contains(searchString.toLowerCase())));
                itemTile.setVisible(visible);
                if (itemTile.isVisible()) {
                    itemTile.setX(5 + (i / 13) * (tileSize + 5));
                    itemTile.setY(5 + (i) * (tileSize + 5) - (i / 13) * 585);
                    i++;
                }
            }
            Label label = itemInfo.getNameLabel();
            label.setVisible(visible);
            label.setLayoutX(itemTile.getX());
            label.setLayoutY(itemTile.getY() + tileSize - 8);
        }
    }

}
