package model.editor.items;

import lombok.Getter;
import lombok.Setter;
import model.entity.effects.EffectParams;

import java.util.List;

/*
 * Информация об одежде
 * */
@Setter
@Getter
public class ClothesInfo extends ItemInfo {
    private Integer armor; // базовая броня
    private Integer skill; // навык, отвечающий за ношение данной одежды
    private String bodyPart; // часть тела, на которую надевается одежда
    private Integer covering; // уровень покрытия тела
    private String style; // стиль одежды (нужен для маскировки путем переодевания в одежду определенного стиля)
    private String gender; // для какого пола подходит этот элемент одежды
    private List<EffectParams> effects; // эффекты
}
