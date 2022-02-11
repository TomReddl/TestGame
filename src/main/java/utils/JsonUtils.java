package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import editor.ItemInfo;
import editor.NPCInfo;
import editor.PollutionInfo;
import editor.TileInfo;
import entity.map.Map;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    public static List<NPCInfo> getNPC() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/characters.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'characters.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<NPCInfo> getCreatures() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/creatures.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'creatures.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<ItemInfo> getItems() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/items.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'items.json', cause=%s" + ex.getMessage());
        }
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

    public void saveMap(String mapName, Map map) {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "world/" + mapName + ".json";
            path = path.replaceAll("/", Matcher.quoteReplacement("\\"));
            objectMapper.writeValue(new File(path), map);
        } catch (Exception ex) {
            throw new RuntimeException("can not save map " + ex.getMessage());
        }
    }

    public static Map loadMap(String mapName) {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "world/" + mapName + ".json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read map, cause=%s" + ex.getMessage());
        }
    }
}
