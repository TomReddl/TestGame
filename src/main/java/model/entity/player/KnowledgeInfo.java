package model.entity.player;

import lombok.Getter;
import model.entity.WorldLangEnum;

import java.io.Serializable;
import java.util.*;

/**
 * Известная персонажу информация
 */
public class KnowledgeInfo implements Serializable {
    @Getter
    private final Set<Integer> readBooks = new HashSet<>(); // прочитанные книги

    @Getter
    private final List<WorldLangEnum> langs = new ArrayList<>(); // языки, которыми владеет персонаж

    @Getter
    private final Map<Integer, List<String>> knowEffects = new HashMap<>();

    public KnowledgeInfo() {
        langs.add(WorldLangEnum.NALHAIM); // герой знает налхеймский язык
        langs.add(WorldLangEnum.CATTGAR); // герой знает каттгарский язык
    }
}
