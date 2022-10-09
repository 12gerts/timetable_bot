package org.bot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Week {

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

    public int getToday() {
        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("u");
        return Integer.parseInt(formatDate.format(date));
    }

    public String today() {
        Schedule schedule = new Schedule();
        int numberOfDay = getToday();
        return nameOfDay(numberOfDay) + "\n" + schedule.getSchedule(numberOfDay);
    }

    public String tomorrow() {
        Schedule schedule = new Schedule();
        int numberOfDay = getToday()% 7 + 1;
        return nameOfDay(numberOfDay) + "\n" + schedule.getSchedule(numberOfDay);
    }

    public String week() {
        Schedule schedule = new Schedule();
        StringBuilder fullSchedule = new StringBuilder();
        for (int numberOfDay = 1; numberOfDay < 7; numberOfDay++) {
            fullSchedule.append(nameOfDay(numberOfDay)).append("\n");
            fullSchedule.append(schedule.getSchedule(numberOfDay)).append("\n");
        }
        return fullSchedule.toString();
    }
}
