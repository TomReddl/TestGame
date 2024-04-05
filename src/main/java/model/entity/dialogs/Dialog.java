package model.entity.dialogs;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Диалог с неигровым персонажем
 */
@Getter
@Setter
public class Dialog implements Serializable {
    String characterId; // идентификатор персонажа
    Map<String, Phrase> phrases = new HashMap<>(); // мапа id фразы-фраза
    String firstPhraseCondition; // условие выбора первой фразы при начале диалога
}
