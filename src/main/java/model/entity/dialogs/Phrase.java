package model.entity.dialogs;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Фраза NPC в диалоге
 */
@Getter
@Setter
public class Phrase implements Serializable {
    String id; // идентификатор фразы
    String text; // текст фразы
    String script; // скрипт, срабатывающий при выводе фразы
    List<Answer> answers = new ArrayList<>(); // ответы на фразу
}
