package org.bot;

public interface IWeek {
    String today(String calendar);
    String tomorrow(String calendar);
    String week(String calendar, int amountOfDays);
}
