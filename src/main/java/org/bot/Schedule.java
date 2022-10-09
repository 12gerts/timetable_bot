package org.bot;

public class Schedule {
    public String getSchedule(int day) {
        return switch (day) {
            case 1, 2, 3, 4, 5, 6, 7 -> Report.NO_SCHEDULE;
            default -> Report.NO_IMPLEMENTATION;
        };
    }
}