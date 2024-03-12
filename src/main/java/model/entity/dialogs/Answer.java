package model.entity.dialogs;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Ответ персонажа на фразу NPC
 */
@Getter
@Setter
public class Answer implements Serializable {
    String text; // текст ответа
    String visiblyCondition; // условие видимости ответа
    String nextPhraseCondition; // id или условие выбора следующей фразы
}
