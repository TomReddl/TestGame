package view.params;

import model.entity.GameLangEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/*
 * Класс для хранения глобальных параметров игры
 */
public class GameParams {
    public static final int tileSize = 40; // размер тайла в пикселях
    public static final int headerSize = 45; // размер шапки окна в пикселях
    public static final int mapSize = 300; // размер карты в тайлах
    public static final int viewSize = 15; // размер отображаемой зоны карты в тайлах
    public static final int screenSizeX = 1020; // ширина игрового окна в пикселях
    public static final int screenSizeY = 680; // высота игрового окна в пикселях
    public static GameLangEnum lang = GameLangEnum.RU; // язык игры

    static {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("game.properties");
        Properties appProps = new Properties();
        try {
            InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(stream), StandardCharsets.UTF_8);
            appProps.load(reader);
            lang = GameLangEnum.valueOf(appProps.getProperty("lang"));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
