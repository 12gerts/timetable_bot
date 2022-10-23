package org.bot;

import java.util.List;

public interface IWeek {
    List<String> today(String calendar);
    String tomorrow(String calendar);
    String week(String calendar, int amountOfDays);
}
