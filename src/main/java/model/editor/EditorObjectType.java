package model.editor;

/**
 * Тип выбранного в редакторе тайла
 */
public enum EditorObjectType {
  GROUND,                         // нижний тайл
  OBJECT,                         // верхний тайл
  CHARACTER,                      // персонаж
  CREATURE,                       // существо
  ITEM,                           // предмет
  POLLUTION,                      // загрязнение
  ZONE,                           // зона
  ROOF,                           // крыша
  CONSTRUCTION_COORDINATES,       // Координаты для сохранения конструкции
  CONSTRUCTION                    // Конструкция
}
