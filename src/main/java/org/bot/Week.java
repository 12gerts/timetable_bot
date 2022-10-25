package org.bot;

import org.bot.Parser.Schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Класс, обрабатывающий дни недели для вывода расписания
 */
public class Week implements IWeek{
    /**
     * Поле, хранящее сегодняшнюю дату или то, что фиксированную дату, с которой мы начинаем отсчет
     */
    private final Date today;

    private final Schedule schedule = new Schedule();

    public Week() {
        this.today = new Date();
    }
    public Week(Date date) {
        this.today = date;
    }
    /**
     * Метод, переводящий номер дня недели в его название
     *
     * @param numberOfDay номер дня недели
     * @return название дня недели
     */
    public String nameOfDay(int numberOfDay) {
        return switch (numberOfDay) {
            case 1 -> "Понедельник";
            case 2 -> "Вторник";
            case 3 -> "Среда";
            case 4 -> "Четверг";
            case 5 -> "Пятница";
            case 6 -> "Суббота";
            case 7 -> "Воскресенье";
            default -> throw new IllegalStateException("Unexpected value: " + numberOfDay);
        };
    }

    @Override
    public boolean evenness() {
        String weekNumber = new SimpleDateFormat("w").format(today);
        return Integer.parseInt(weekNumber) % 2 == 0;
    }

    public String shortNameOfDay(int numberOfDay) {
        return switch (numberOfDay) {
            case 1 -> "(пн)";
            case 2 -> "(вт)";
            case 3 -> "(ср)";
            case 4 -> "(чт)";
            case 5 -> "(пт)";
            case 6 -> "(сб)";
            case 7 -> "(вс)";
            default -> throw new IllegalStateException("Unexpected value: " + numberOfDay);
        };
    }


    /**
     * Метод, возвращающий следующий день от заданного
     *
     * @param date заданный день
     * @return следующий день
     */
    public Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
    }

    public Date parseDate(String dateStr) {
        try {
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean isValid(String dateStr) {
        return parseDate(dateStr) != null;
    }

    /**
     * Проверка на пустую строку - отсутствующее расписание
     *
     * @param schedule строку с расписанием
     * @return сообщение об отсутствии расписания/расписание
     */
    private String checkMissingSchedule(String schedule) {
        if (schedule.length() == 0) {
            return Report.NO_SCHEDULE + "\n";
        }
        return schedule;
    }

    /**
     * Метод, возвращающий номер дня недели от даты
     *
     * @param date заданная дата
     * @return порядковый номер дня недели
     */
    public int getNumberOfWeekDay(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("u");
        return Integer.parseInt(formatDate.format(date));
    }

    /**
     * Метод, возвращающий расписание на сегодняшнюю дату
     *
     * @param calendar ICalendar с расписанием на 2 недели
     * @return расписание на сегодняшний день
     */
    @Override
    public List<String> today(String calendar, Date date) {
        List<String> list = new ArrayList<>();
        return schedule.parseCalendar(calendar, date, list);
    }

    @Override
    public String day(String calendar, Date date) {
        int numberOfDay = getNumberOfWeekDay(date);
        String tomorrowSchedule = schedule.parseCalendar(calendar, date);
        return nameOfDay(numberOfDay) + "\n" + checkMissingSchedule(tomorrowSchedule);
    }

    /**
     * Метод, возвращающий расписание на заданное количество дней (до 14)
     *
     * @param calendar     ICalendar с расписанием на 2 недели
     * @param amountOfDays количество дней, на которое необходимо расписание
     * @return расписание на [amountOfDays] дней
     */
    @Override
    public String week(String calendar, int amountOfDays) {
        StringBuilder fullSchedule = new StringBuilder();
        Date day = today;
        int numberOfWeekDay = getNumberOfWeekDay(day);

        for (int i = 0; i < amountOfDays; i++) {
            String answer = schedule.parseCalendar(calendar, day);
            fullSchedule.append(nameOfDay(numberOfWeekDay)).append("\n");
            fullSchedule.append(checkMissingSchedule(answer)).append("\n");
            day = getNextDay(day);
            numberOfWeekDay = getNumberOfWeekDay(day);
        }
        return fullSchedule.toString();
    }
}