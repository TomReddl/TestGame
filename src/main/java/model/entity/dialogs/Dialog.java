package model.entity.dialogs;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Диалог с NPC
 */
@Getter
@Setter
public class Dialog implements Serializable {
    String characterId; // идентификатор NPC
    Map<String, Phrase> phrases = new HashMap<>(); // мапа id фразы-фраза
    String firstPhraseCondition; // условие выбора первой фразы при начале диалога
}
