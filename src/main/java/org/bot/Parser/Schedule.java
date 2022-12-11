package org.bot.Parser;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;
import biweekly.property.DateStart;
import biweekly.property.Summary;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Класс, формирующий расписание на заданный день
 */
public class Schedule {
    private final DateFormat dfEqual;
    private final DateFormat dfPrint;

    public Schedule() {
        this.dfEqual = new SimpleDateFormat("dd/MM/yyyy");
        this.dfPrint = new SimpleDateFormat("HH:mm");
    }

    /**
     * Метод, парсящий ICalendar и возвращающий все занятия за заданный день
     *
     * @param calendar ICalendar, содержащий все занятие за 2 недели с текущей даты
     * @param date     дата
     * @return расписание на заданную дату
     */
    public String parseCalendar(String calendar, Date date) {
        StringBuilder sb = new StringBuilder();
        try (ICalReader reader = new ICalReader(calendar)) {
            ICalendar icalendar;
            while ((icalendar = reader.readNext()) != null) {
                for (VEvent event : icalendar.getEvents()) {
                    DateStart dateStart = event.getDateStart();

                    if (!dfEqual.format(dateStart.getValue()).equals(dfEqual.format(date))) {
                        continue;
                    }

                    String dateStartStr = dfPrint.format(dateStart.getValue());
                    Summary summary = event.getSummary();
                    String summaryStr = summary.getValue();

                    sb.append(dateStartStr).append(": ").append(summaryStr).append("\n");
                }
            }
        } catch (IOException error) {
            return null;
        }
        return sb.toString();
    }

    public List<String> parseCalendar(String calendar, Date date, List<String> list) {
        try (ICalReader reader = new ICalReader(calendar)) {
            ICalendar icalendar;
            while ((icalendar = reader.readNext()) != null) {
                for (VEvent event : icalendar.getEvents()) {
                    DateStart dateStart = event.getDateStart();

                    if (!dfEqual.format(dateStart.getValue()).equals(dfEqual.format(date))) {
                        continue;
                    }
                    String dateStartStr = dfPrint.format(dateStart.getValue());
                    Summary summary = event.getSummary();
                    String summaryStr = summary.getValue();
                    try {
                        list.add(lessonNumber(dateStartStr) + ": " + summaryStr.substring(0, 24));
                    } catch (StringIndexOutOfBoundsException e) {
                        list.add(lessonNumber(dateStartStr) + ": " + summaryStr);
                    }
                }
            }
        } catch (IOException error) {
            return null;
        }
        return list;
    }

    public String lessonNumber(String time) {
        return switch (time) {
            case "08:30", "09:00" -> "1";
            case "10:15", "10:40" -> "2";
            case "12:00", "12:50" -> "3";
            case "14:15", "14:30" -> "4";
            case "16:00", "16:10" -> "5";
            case "17:40", "17:50" -> "6";
            case "19:15", "19:30" -> "7";
            default -> "0";
        };
    }
}


