package model.entity.map;

import javafx.scene.image.Image;
import lombok.Getter;
import view.Game;

/**
 * Погода
 */
public enum WeatherEnum {
    CLEAR(Game.getText("CLEAR_WEATHER"), null, null, null),                                                                                         // Безоблачно
    SMALL_RAIN(Game.getText("SMALL_RAIN_WEATHER"), new Image("/graphics/weather/rain3.1.png"), new Image("/graphics/weather/rain3.2.png"), null),           // Морось
    MEDIUM_RAIN(Game.getText("MEDIUM_RAIN_WEATHER"), new Image("/graphics/weather/rain1.1.png"), new Image("/graphics/weather/rain1.2.png"), null),         // Дождь
    HEAVY_RAIN(Game.getText("HEAVY_RAIN_WEATHER"), new Image("/graphics/weather/rain2.1.png"), new Image("/graphics/weather/rain2.2.png"), null),           // Ливень
    BLOOD_RAIN(Game.getText("BLOOD_RAIN_WEATHER"), new Image("/graphics/weather/bloodRain.1.png"), new Image("/graphics/weather/bloodRain.2.png"), null),   // Кровавый дождь
    ACID_RAIN(Game.getText("ACID_RAIN_WEATHER"), new Image("/graphics/weather/acidRain.1.png"), new Image("/graphics/weather/acidRain.2.png"), null),       // Кислотный дождь
    FOG(Game.getText("FOG"), new Image("/graphics/weather/fog1.png"), new Image("/graphics/weather/fog2.png"), new Image("/graphics/weather/fog3.png")),        // Туман
    SNOW(Game.getText("SNOW"), new Image("/graphics/weather/snow1.1.png"), new Image("/graphics/weather/snow1.2.png"), null);                               // Снег

    @Getter
    private final String desc;
    @Getter
    private final Image image1;
    @Getter
    private final Image image2;
    @Getter
    private final Image image3;

    WeatherEnum(String desc, Image image1, Image image2, Image image3) {
        this.desc = desc;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    @Override
    public String toString() {
        return desc;
    }
}
