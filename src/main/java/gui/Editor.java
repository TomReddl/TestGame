package gui;

import entity.CreatureList;
import entity.NPCList;
import entity.TileType;
import entity.TilesList;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Editor {

  private ImageView border;
  private final Pane pane = new Pane();
  private final Pane pane2 = new Pane();
  private final Pane pane3 = new Pane();
  private final Pane pane4 = new Pane();
  private int selectTile = 0;
  private TileType selectedType = TileType.GROUND;
  private TilesList tilesList;
  private NPCList npcList;
  private CreatureList creatureList;

  public Editor() {
    tilesList = new TilesList();
    npcList = new NPCList();
    creatureList = new CreatureList();
  }


  public void drawTiles(Group root) {
    TabPane tabPane = new TabPane();
    tabPane.setLayoutX(630);
    tabPane.setPrefSize(370, 620);
    tabPane.getTabs().add(new Tab("Тайлы"));
    tabPane.getTabs().add(new Tab("Объекты"));
    tabPane.getTabs().add(new Tab("Персонажи"));
    tabPane.getTabs().add(new Tab("Существа"));
    tabPane.getTabs().get(0).setClosable(false);
    tabPane.getTabs().get(1).setClosable(false);
    tabPane.getTabs().get(2).setClosable(false);
    tabPane.getTabs().get(3).setClosable(false);

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setLayoutX(5);
    scrollPane.setPrefSize(180, 600);
    for (int i = 0; i < tilesList.getGroundCounts(); i++) {
      ImageView tile = new ImageView("/graphics/tiles/" + i + ".png");
      tile.setX(5 + (i / 13) * 45);
      tile.setY(5 + (i) * 45 - (i / 13) * 585);
      tile.setId(String.valueOf(i));
      tile.setOnMousePressed(event -> {
        if (pane2.getChildren().contains(border)) {
          pane2.getChildren().remove(border);
          pane.getChildren().add(border);
        } else if (pane3.getChildren().contains(border)) {
          pane3.getChildren().remove(border);
          pane.getChildren().add(border);
        } else if (pane4.getChildren().contains(border)) {
          pane4.getChildren().remove(border);
          pane.getChildren().add(border);
        }
        selectTile = tilesList.getGroundTiles().get(Integer.parseInt(tile.getId())).getId();
        selectedType = TileType.GROUND;
        border.setX(tilesList.getGroundTiles().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
        border.setY(tilesList.getGroundTiles().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
      });

      tilesList.getGroundTiles().get(i).setImage(tile);
      pane.getChildren().add(tile);
    }
    border = new javafx.scene.image.ImageView("/graphics/gui/Border.png");
    border.setX(tilesList.getGroundTiles().get(0).getImage().getX() - 1);
    border.setY(tilesList.getGroundTiles().get(0).getImage().getY() - 1);
    pane.getChildren().add(border);
    scrollPane.setContent(pane);
    tabPane.getTabs().get(0).setContent(scrollPane);

    ScrollPane scrollPane2 = new ScrollPane();
    scrollPane2.setLayoutX(190);
    scrollPane2.setPrefSize(180, 600);

    for (int i = 0; i < tilesList.getTile2Count(); i++) {
      ImageView tile = new ImageView("/graphics/tiles2/" + i + ".png");
      tile.setX(5 + (i / 13) * 45);
      tile.setY(5 + (i) * 45 - (i / 13) * 585);
      tile.setId(String.valueOf(i));
      tile.setOnMousePressed(event -> {
        if (pane.getChildren().contains(border)) {
          pane.getChildren().remove(border);
          pane2.getChildren().add(border);
        } else if (pane3.getChildren().contains(border)) {
          pane3.getChildren().remove(border);
          pane2.getChildren().add(border);
        } else if (pane4.getChildren().contains(border)) {
          pane4.getChildren().remove(border);
          pane2.getChildren().add(border);
        }
        selectTile = tilesList.getGameObjects().get(Integer.parseInt(tile.getId())).getId();
        selectedType = TileType.OBJECT;
        border.setX(tilesList.getGameObjects().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
        border.setY(tilesList.getGameObjects().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
      });

      tilesList.getGameObjects().get(i).setImage(tile);
      pane2.getChildren().add(tile);
    }
    scrollPane2.setContent(pane2);
    tabPane.getTabs().get(1).setContent(scrollPane2);

    ScrollPane scrollPane3 = new ScrollPane();
    scrollPane3.setLayoutX(190);
    scrollPane3.setPrefSize(180, 600);

    for (int i = 0; i < npcList.getNpcCount(); i++) {
      ImageView tile = new ImageView("/graphics/characters/" + i + ".png");
      tile.setX(5 + (i / 13) * 45);
      tile.setY(5 + (i) * 45 - (i / 13) * 585);
      tile.setId(String.valueOf(i));
      tile.setOnMousePressed(event -> {
        if (pane.getChildren().contains(border)) {
          pane.getChildren().remove(border);
          pane3.getChildren().add(border);
        } else if (pane2.getChildren().contains(border)) {
          pane2.getChildren().remove(border);
          pane3.getChildren().add(border);
        } else if (pane4.getChildren().contains(border)) {
          pane4.getChildren().remove(border);
          pane3.getChildren().add(border);
        }
        selectTile = npcList.getNpc().get(Integer.parseInt(tile.getId())).getImageId();
        selectedType = TileType.NPC;
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
      ImageView tile = new ImageView("/graphics/creatures/" + i + ".png");
      tile.setFitWidth(40);
      tile.setPreserveRatio(true);
      tile.setSmooth(true);
      tile.setCache(true);
      tile.setX(5 + (i / 13) * 45);
      tile.setY(5 + (i) * 45 - (i / 13) * 585);
      tile.setId(String.valueOf(i));
      tile.setOnMousePressed(event -> {
        if (pane.getChildren().contains(border)) {
          pane.getChildren().remove(border);
          pane4.getChildren().add(border);
        } else if (pane2.getChildren().contains(border)) {
          pane2.getChildren().remove(border);
          pane4.getChildren().add(border);
        } else if (pane3.getChildren().contains(border)) {
          pane3.getChildren().remove(border);
          pane4.getChildren().add(border);
        }
        selectTile = creatureList.getCreatures().get(Integer.parseInt(tile.getId())).getImageId();
        selectedType = TileType.CREATURE;
        border.setX(creatureList.getCreatures().get(Integer.parseInt(tile.getId())).getImage().getX() - 1);
        border.setY(creatureList.getCreatures().get(Integer.parseInt(tile.getId())).getImage().getY() - 1);
      });

      creatureList.getCreatures().get(i).setImage(tile);
      pane4.getChildren().add(tile);
    }
    scrollPane4.setContent(pane4);
    tabPane.getTabs().get(3).setContent(scrollPane4);

    root.getChildren().add(tabPane);
  }

}
