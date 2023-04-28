package controller;

import javafx.scene.image.ImageView;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для работы с существами
 */
public class CreaturesController {
    @Getter
    private static final ImageView remains = new ImageView("/graphics/creatures/remains.png"); // останки существа
    public static final List<Integer> invisibleCreatures = new ArrayList<>(); // существа, видимые лишь под действием эффекта EPIPHANY (Прозрение)

    static {
        invisibleCreatures.add(6);
    }
}
