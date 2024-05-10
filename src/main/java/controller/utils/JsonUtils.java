package controller.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import model.editor.ConstructionInfo;
import model.editor.*;
import model.editor.items.*;
import model.entity.dialogs.Dialog;
import model.entity.map.MapChunk;
import lombok.experimental.UtilityClass;
import model.entity.map.WorldInfo;
import view.Game;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

@UtilityClass
/*
 * Вспомогательный класс для работы с json-файлами
 * */
public class JsonUtils {
    private static final ObjectMapper objectMapper = createMapper();

    private static ObjectMapper createMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        javaTimeModule.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);

        var mapper = new ObjectMapper();
        mapper.registerModule(javaTimeModule);
        return mapper;
    }

    public static List<TileInfo> getTiles1() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/tiles1.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'tiles1.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<TileInfo> getTiles2() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/tiles2.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'tiles2.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<CharacterInfo> getCharacters() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/characters.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'characters.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<CreatureInfo> getCreatures() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/creatures.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'creatures.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<ItemInfo> getItems() {
        List<ItemInfo> items = new ArrayList<>();
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/items.json";
            items.addAll(objectMapper.readValue(new File(path), new TypeReference<>() {
            }));

            path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/weapons.json";
            items.addAll(objectMapper.readValue(new File(path), new TypeReference<List<WeaponInfo>>() {
            }));

            path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/clothes.json";
            items.addAll(objectMapper.readValue(new File(path), new TypeReference<List<ClothesInfo>>() {
            }));

            path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/edible.json";
            items.addAll(objectMapper.readValue(new File(path), new TypeReference<List<EdibleInfo>>() {
            }));
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'items.json', cause=%s" + ex.getMessage());
        }
        items.sort(Comparator.comparing(ItemInfo::getId));
        return items;
    }

    public static List<PollutionInfo> getPollutions() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/pollutions.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'pollutions.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<ZoneInfo> getZones() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/zones.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'zones.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<RoofInfo> getRoofs() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/roofs.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'roofs.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<ConstructionInfo> getConstructions() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/constructions.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'constructions.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<RecipeInfo> getRecipes() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/recipes.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'recipes.json', cause=%s" + ex.getMessage());
        }
    }

    public void saveMap(String mapName, MapChunk map) {
        String[] coordinates = mapName.split("\\.");
        map.setWorldPosY(Integer.parseInt(coordinates[0]));
        map.setWorldPosX(Integer.parseInt(coordinates[1]));
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "world/" + mapName + ".json";
            path = path.replaceAll("/", Matcher.quoteReplacement("\\"));
            objectMapper.writeValue(new File(path), map);
        } catch (Exception ex) {
            throw new RuntimeException("Не удалось сохранить чанк карты mapName: " + ex.getMessage());
        }
        saveWorldInfo(Game.getWorldInfo());
    }

    /**
     * Сохранить общую информацию о мире игры
     *
     * @param worldInfo
     */
    public void saveWorldInfo(WorldInfo worldInfo) {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "world/world.json";
            path = path.replaceAll("/", Matcher.quoteReplacement("\\"));
            objectMapper.writeValue(new File(path), worldInfo);
        } catch (Exception ex) {
            throw new RuntimeException("Не удалось сохранить информацию о мире игры: " + ex.getMessage());
        }
    }

    public static MapChunk loadMap(String mapName) {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "world/" + mapName + ".json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read map, cause=%s" + ex.getMessage());
        }
    }

    /**
     * Сохранить конструкции
     *
     * @param constructions
     */
    public void saveConstructions(List<ConstructionInfo> constructions) {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/constructions.json";
            path = path.replaceAll("/", Matcher.quoteReplacement("\\"));
            objectMapper.writeValue(new File(path), constructions);
        } catch (Exception ex) {
            throw new RuntimeException("Не удалось сохранить информацию о мире игры: " + ex.getMessage());
        }
    }

    /**
     * Загрузить пресохраненный диалог по имени
     * @param name
     * @return
     */
    public static Dialog getDialog(String name) {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/dialogs/" + name + ".json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read " + name + "'.json', cause=%s" + ex.getMessage());
        }
    }
}
