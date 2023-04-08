package controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для работы с существами
 */
public class CreaturesController {
    public static final List<Integer> invisibleCreatures = new ArrayList<>(); // существа, видимые лишь под действием эффекта EPIPHANY (Прозрение)

    static {
        invisibleCreatures.add(6);
    }
}
