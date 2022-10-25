package org.bot;

import java.util.Date;
import java.util.List;

public interface IWeek {
    List<String> today(String calendar, Date date);
    String day(String calendar, Date date);
    String week(String calendar, int amountOfDays);
    boolean isValid(String dateStr);

    Date parseDate(String report);

    boolean evenness();
}
