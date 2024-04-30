package model.entity.map;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Информация о фракции
 */
@Getter
@Setter
public class Fraction implements Serializable {
    String id; // идентификатор фракции
    String name; // название фракции
    Fraction parentFraction; // родительская фракция
    Map<String, Integer> reputations = new HashMap<>(); // мапа для хранения репутации у других фракий в виде id фракции- уровень отношения
}
