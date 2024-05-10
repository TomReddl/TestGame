package model.entity.character;

import lombok.Getter;
import view.Game;

/**
 * Профессия или вид деятельности персонажа
 */
public enum ProfessionEnum {
    FARMER(Game.getGameText("FARMER")),                           // фермер
    SHEPHERD(Game.getGameText("SHEPHERD")),                       // пастух
    HUNTER(Game.getGameText("HUNTER")),                           // охотник
    MINER(Game.getGameText("MINER")),                             // шахтер
    DEALER(Game.getGameText("DEALER")),                           // торговец
    BLACKSMITH(Game.getGameText("BLACKSMITH")),                   // кузнец
    TAILOR(Game.getGameText("TAILOR")),                           // портной
    ENGINEER(Game.getGameText("ENGINEER")),                       // инженер
    COOK(Game.getGameText("COOK")),                               // повар
    TEACHER(Game.getGameText("TEACHER")),                         // преподаватель
    STUDENT(Game.getGameText("STUDENT")),                         // ученик
    PRIEST(Game.getGameText("PRIEST")),                           // священник
    GUARD(Game.getGameText("GUARD")),                             // стражник
    DOCTOR(Game.getGameText("DOCTOR")),                           // врач
    LUMBERJACK(Game.getGameText("LUMBERJACK")),                   // лесоруб
    CARPENTER(Game.getGameText("CARPENTER")),                     // плотник
    HOUSEWIFE(Game.getGameText("HOUSEWIFE")),                     // домохозяйка
    HOUSEKEEPER(Game.getGameText("HOUSEKEEPER")),                 // домработник
    GARDENER(Game.getGameText("GARDENER")),                       // садовник
    MAYOR(Game.getGameText("MAYOR")),                             // мэр
    HEADMAN(Game.getGameText("HEADMAN")),                         // староста
    NOBLE(Game.getGameText("NOBLE")),                             // знать
    SLAVE(Game.getGameText("SLAVE")),                             // раб
    SOLDIER(Game.getGameText("SOLDIER")),                         // солдат
    OVERSEER(Game.getGameText("OVERSEER")),                       // надзиратель
    JEWELER(Game.getGameText("JEWELER")),                         // ювелир
    PROSTITUTE(Game.getGameText("PROSTITUTE")),                   // проститутка
    MUSICIAN(Game.getGameText("MUSICIAN")),                       // музыкант
    ACTOR(Game.getGameText("ACTOR")),                             // актер
    WRITER(Game.getGameText("WRITER")),                           // писатель
    ARTIST(Game.getGameText("ARTIST")),                           // художник
    BEGGAR(Game.getGameText("BEGGAR")),                           // нищий
    DRUNKARD(Game.getGameText("DRUNKARD")),                       // пьяница
    REFUGEE(Game.getGameText("REFUGEE")),                         // беженец
    JOURNALIST(Game.getGameText("JOURNALIST")),                   // журналист
    MESSENGER(Game.getGameText("MESSENGER")),                     // посыльный
    MASON(Game.getGameText("MASON")),                             // каменщик
    CULINARY(Game.getGameText("CULINARY")),                       // кулинар
    MERCHANT(Game.getGameText("MERCHANT")),                       // купец
    FURNITURE_MAKER(Game.getGameText("FURNITURE_MAKER")),         // мебельщик
    SECURITY(Game.getGameText("SECURITY")),                       // охранник
    SCULPTOR(Game.getGameText("SCULPTOR")),                       // скульптор
    ADVENTURER(Game.getGameText("ADVENTURER")),                   // приключенец
    ALCHEMIST(Game.getGameText("ALCHEMIST")),                     // алхимик
    MERCENARY(Game.getGameText("MERCENARY"));                     // наемник

    @Getter
    private final String desc;

    ProfessionEnum(String desc) {
        this.desc = desc;
    }
}
