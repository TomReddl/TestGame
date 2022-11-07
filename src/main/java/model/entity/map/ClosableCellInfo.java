package model.entity.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Данные о запираемой точке на карте (контейнер или дверь)
 */
@Getter
@Setter
@NoArgsConstructor
public class ClosableCellInfo extends MapCellInfo {
    private boolean isLocked; // признак запертой двери или контейнера
    private Integer keyId; // идентификатор ключа (для дверей или контейнеров)
    private Integer lockLevel; // сложность замка (для дверей или контейнеров)

    private boolean isTrap; // признак наличия ловушки
    private Integer trapLevel; // уровень ловушки

    private boolean isCodeLock; // признак кодового змка
    private String codeForLock; // код для кодового замка
    private String charsForLock; // символы для кодового замка
    private String codeHint; // подсказка для кода

    public ClosableCellInfo(MapCellInfo oldInfo) {
        super(oldInfo);
    }
}
