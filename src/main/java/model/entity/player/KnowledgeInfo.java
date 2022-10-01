package model.entity.player;

import lombok.Getter;
import model.entity.WorldLangEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Известная персонажу информация
 */
public class KnowledgeInfo implements Serializable {
    @Getter
    private final Set<Integer> readBooks = new HashSet<>(); // прочитанные книги

    @Getter
    private final List<WorldLangEnum> langs = new ArrayList<>(); // языки, которыми владеет персонаж

    public KnowledgeInfo() {
        langs.add(WorldLangEnum.NALHAIM); // герой знает налхеймский язык
        langs.add(WorldLangEnum.CATTGAR); // герой знает каттгарский язык
    }
}
