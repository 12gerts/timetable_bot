package org.bot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Класс, обрабатывающий дни недели для вывода расписания
 */
public class Week {
    private final Date today = new Date();
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
     * Метод, возвращающий расписание на сегодня
     *
     * @param group экземпляр класса Group, хранящий текущую учебную группу
     * @return расписание на сегодняшний день
     */
    public String today(Group group) {
        String numberOfGroup = group.getNumberOfGroup();
        if (numberOfGroup == null) {
            return Report.AUTHORIZATION_REPORT;
        }

        Schedule schedule = new Schedule();
        int numberOfDay = getNumberOfWeekDay(today);
        String todaySchedule = schedule.getSchedule(today, numberOfGroup);
        return nameOfDay(numberOfDay) + "\n" + checkMissingSchedule(todaySchedule);
    }

    /**
     * Метод, возвращающий расписание на завтра
     *
     * @param group экземпляр класса Group, хранящий текущую учебную группу
     * @return расписание на завтрашний день
     */
    public String tomorrow(Group group) {
        String numberOfGroup = group.getNumberOfGroup();
        if (numberOfGroup == null) {
            return Report.AUTHORIZATION_REPORT;
        }

        Schedule schedule = new Schedule();
        Date tomorrowDate = getNextDay(today);
        int numberOfDay = getNumberOfWeekDay(tomorrowDate);
        String tomorrowSchedule = schedule.getSchedule(tomorrowDate, numberOfGroup);
        return nameOfDay(numberOfDay) + "\n" + checkMissingSchedule(tomorrowSchedule);
    }

    /**
     * Метод, возвращающий расписание на заданное количество дней (до 14)
     *
     * @param group        экземпляр класса Group, хранящий текущую учебную группу
     * @param amountOfDays количество дней, на которое необходимо расписание
     * @return расписание на [amountOfDays] дней
     */
    public String week(Group group, int amountOfDays) {
        String numberOfGroup = group.getNumberOfGroup();
        if (numberOfGroup == null) {
            return Report.AUTHORIZATION_REPORT;
        }


        Schedule schedule = new Schedule();
        StringBuilder fullSchedule = new StringBuilder();
        Date day = today;
        int numberOfWeekDay = getNumberOfWeekDay(day);

        for (int i = 0; i < amountOfDays; i++) {
            String answer = schedule.getSchedule(day, numberOfGroup);
            fullSchedule.append(nameOfDay(numberOfWeekDay)).append("\n");
            fullSchedule.append(checkMissingSchedule(answer)).append("\n");
            day = getNextDay(day);
            numberOfWeekDay = getNumberOfWeekDay(day);
        }
        return fullSchedule.toString();
    }
}