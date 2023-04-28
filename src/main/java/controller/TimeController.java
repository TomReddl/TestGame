package controller;

import model.editor.items.BodyPartEnum;
import model.entity.GameCalendar;
import model.entity.battle.DamageTypeEnum;
import model.entity.map.MapCellInfo;
import model.entity.map.WeatherEnum;
import view.Game;

import static controller.BattleController.*;

/**
 * Действия со временем
 */
public class TimeController {

    /**
     * Выполнить указанное количество тиков
     *
     * @param count - количество тиков
     */
    public static void tic(int count) {
        for (int i = 1; i <= count; i++) {
            tic(i == count);
        }
    }

    /**
     * Ход времени
     *
     * @param show - нужно ли отрисовывать изменения мира
     */
    public static void tic(boolean show) {
        incTime(show);
    }

    /**
     * Увеличить текущее время в календаре
     *
     * @param show - нужно ли показывать обновленное время игроку
     * @return true, если удалось увеличить время
     */
    private static boolean incTime(boolean show) {
        var currentDate = GameCalendar.getCurrentDate();
        currentDate.setTic(currentDate.getTic() + 1);

        MapCellInfo playerMapCell = Game.getMap().getTiles()[Game.getMap().getPlayer().getXPosition()][Game.getMap().getPlayer().getYPosition()];
        if (playerMapCell.getFireId() != 0) {
            // Если персонаж стоит на горящем тайле, урон огнем наносится ему каждый ход
            BattleController.applyDamageToPlayer(playerMapCell.getFireId() * baseFireDamage, DamageTypeEnum.FIRE_DAMAGE);
        }

        Integer pollutionId = playerMapCell.getPollutionId();
        if (pollutionId >= 13 && pollutionId <= 15) {
            // Если персонаж стоит на кислотном загрязнении без обуви, урон кислотой наносится ему каждый ход
            if (Game.getMap().getPlayer().getWearingItems().get(BodyPartEnum.SHOES.ordinal()).getValue() == null) {
                BattleController.applyDamageToPlayer((pollutionId - 12) * baseAcidPollutionDamage, DamageTypeEnum.ACID_DAMAGE);
            }
        }

        if (Game.getMap().getCurrentWeather().getKey().equals(WeatherEnum.ACID_RAIN)) {
            // Если идет кислотный дождь, он наносит урон персонажу
            BattleController.applyDamageToPlayer(baseAcidRainDamage, DamageTypeEnum.FIRE_DAMAGE);
        }

        // эффекты действуют каждый ход
        EffectsController.executeEffects(Game.getMap().getPlayer());

        if (currentDate.getTic() % 10 == 0) {
            MapController.fireSpread(); // распространение огня каждые 10 тиков
        }

        if (currentDate.getTic() > GameCalendar.getTicsInHour()) {
            currentDate.setTic(1);
            currentDate.setHour(currentDate.getHour() + 1);

            if (currentDate.getHour() % 4 == 0) {
                MapController.moldGrowth(); // рост плесени каждые 4 часа
            }

            CharactersController.changeHungerAndThirst(Game.getMap().getPlayer()); // жажда и голод убывают каждый час

            if (currentDate.getHour() > 23) {
                currentDate.setHour(0);

                currentDate.setDayOfWeek(currentDate.getDayOfWeek() + 1);
                if (currentDate.getDayOfWeek() > 10) {
                    currentDate.setDayOfWeek(1);

                    MapController.plantGrowth(); // рост растений раз в неделю
                    CharactersController.growingHair(); // рост волос персонажа раз в неделю
                }

                currentDate.setDay(currentDate.getDay() + 1);

                CharactersController.changeCleanness(Game.getMap().getPlayer()); // чистота убывает каждый день

                if (currentDate.getDay() > GameCalendar.MonthEnum.getMonth(currentDate.getMonth()).getDays()) {
                    currentDate.setDay(1);
                    currentDate.setMonth(currentDate.getMonth() + 1);
                    if (currentDate.getMonth() > 8) {
                        currentDate.setMonth(1);
                        currentDate.setYear(currentDate.getYear() + 1);
                        currentDate.setDayOfWeek(0); // первый день года всегда нулевой день недели
                    }
                }
            }
        }
        if (show) {
            Game.getTimeLabel().setText(getCurrentDataStr(false));
            MapController.drawCurrentMap();
        }
        return true;
    }

    /**
     * Получить текстовое представление текущей даты
     *
     * @param detailTime - показывать точное время (если персонаж смотрит на часы)
     *
     * @return текущая дата в читаемом виде
     */
    public static String getCurrentDataStr(boolean detailTime) {
        var currentDate = GameCalendar.getCurrentDate();
        String time;
        if (detailTime) {
            String hour = currentDate.getHour() < 10 ? "0" + currentDate.getHour() : String.valueOf(currentDate.getHour());
            String tic = currentDate.getTic() < 10 ? "0" + currentDate.getTic() : String.valueOf(currentDate.getTic());
            time = hour + ":" + tic;
        } else {
            time = getTimesOfDay(currentDate.getHour());
        }
        return time + ", " + currentDate.getDay() + " " +
                GameCalendar.MonthEnum.getMonth(currentDate.getMonth()).getName().toLowerCase() + " " +
                currentDate.getYear() + " " + Game.getGameText("YEAR");
    }

    /**
     * Получить время суток
     *
     * @param hour - текущий час
     *
     * @return название времени суток (утро/день/вечер/ночь)
     */
    private static String getTimesOfDay(int hour) {
        if (hour < 5) {
            return Game.getGameText("NIGHT");
        } else if (hour < 11) {
            return Game.getGameText("MORNING");
        } else if (hour < 17) {
            return Game.getGameText("AFTERNOON");
        } else if (hour < 22) {
            return Game.getGameText("EVENING");
        }
        return Game.getGameText("NIGHT");
    }

    /**
     * Получить текущее время года
     *
     * @return время года для текущей даты
     */
    public static GameCalendar.SeasonEnum getSeason() {
        return GameCalendar.MonthEnum.getMonth(GameCalendar.getCurrentDate().getMonth()).getSeason();
    }
}
