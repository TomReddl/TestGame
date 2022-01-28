package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import entity.NPCInfo;
import entity.Tile;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public static List<Tile> getTiles1() {
        try {
            var path = "/" + JsonUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "objects/tiles1.json";
            return objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException("can not read 'tiles1.json', cause=%s" + ex.getMessage());
        }
    }

    public static List<Tile> getTiles2() {
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
}
