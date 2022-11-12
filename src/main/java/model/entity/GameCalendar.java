package model.entity;

import lombok.Getter;
import lombok.Setter;
import view.Game;

/**
 * Календарь внутриигрового времени
 */
public class GameCalendar {
    @Getter
    private static final int startYear = 1872; // стартовый год игры
    @Getter
    private static final int ticsInHour = 60; // количество тиков в 1 часе
    @Getter
    private static GameDate currentDate = new GameDate(); // текущая игровая дата

    /**
     * Игровая дата
     */
    @Getter
    @Setter
    public static class GameDate {
        private int year; // год
        private int month; // месяц
        private int day; // день месяца
        private int dayOfWeek; // день недели
        private int hour; // час (диапазон значений: 0-23)
        private int tic; // тик (единица времени в игре, время, за которое персонаж делает один шаг по карте, диапазон значений: 1-ticsInHour)

        public GameDate() {
            year = startYear;
            month = 1;
            day = 1;
            dayOfWeek = 0;
            hour = 0;
            tic = 1;
        }
    }

    // Месяцы в игровом календаре
    public enum MonthEnum {
        TARDAS(Game.getGameText("TARDAS"), 33),
        FELDAS(Game.getGameText("FELDAS"), 30),
        MEETDAS(Game.getGameText("MEETDAS"), 31),
        JARDAS(Game.getGameText("JARDAS"), 30),
        JAMADAS(Game.getGameText("JAMADAS"), 32),
        PAAMDAS(Game.getGameText("PAAMDAS"), 30),
        NORIDAS(Game.getGameText("NORIDAS"), 31),
        DERDAS(Game.getGameText("DERDAS"), 30);

        @Getter
        private final String name; // название месяца
        @Getter
        private final int days; // количество дней в месяце

        MonthEnum(String name, int days) {
            this.name = name;
            this.days = days;
        }

        /**
         * Получить месяц по индексу
         *
         * @param index - индекс месяца
         * @return MonthEnum текущий месяц
         */
        public static MonthEnum getMonth(int index) {
            for (MonthEnum value : MonthEnum.values()) {
                if (value.ordinal() == index) {
                    return value;
                }
            }
            return null;
        }
    }

    // Дни недели в игровом календаре
    public enum DayOfWeekEnum {
        NULLUS(Game.getGameText("NULLUS")), // первый день года всегда нулевой день недели (Нуллус), все остальные недели начинаются с первого дня (Монос)
        MONOS(Game.getGameText("MONOS")),
        DITRIS(Game.getGameText("DITRIS")),
        TREES(Game.getGameText("TREES")),
        TETRAS(Game.getGameText("TETRAS")),
        PENTAS(Game.getGameText("PENTAS")),
        GECKSAS(Game.getGameText("GECKSAS")),
        GEPTAS(Game.getGameText("GEPTAS")),
        OKTAS(Game.getGameText("OKTAS")),
        NONAS(Game.getGameText("NONAS")),
        DEKAS(Game.getGameText("DEKAS"));

        private final String name; // название дня недели

        DayOfWeekEnum(String name) {
            this.name = name;
        }
    }
}
