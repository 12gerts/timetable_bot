package org.bot;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public interface IWeek {
    List<String> today(String calendar, Date date);

    String day(String calendar, Date date);

    String week(String calendar, int amountOfDays);

    boolean isValid(String dateStr);

    Date parseDate(String report);

    Date parseDate(String report, DateFormat stf);

    boolean evenness();
}
