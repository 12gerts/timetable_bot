package org.bot;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;
import biweekly.property.DateStart;
import biweekly.property.Summary;
import org.bot.Http.HttpRequest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс, формирующий расписание на заданный день
 */
public class Schedule {
    /**
     * Метод, делающий запрос к API УрФУ по внутреннему номеру группы и возвращающий расписание на заданный день
     *
     * @param date        дата
     * @param innerNumber внутренний номер группы
     * @return расписание на заданный день
     */
    public String getSchedule(Date date, String innerNumber) {
        HttpRequest request = new HttpRequest();
        String calendar = request.getSchedule(innerNumber);
        return parseCalendar(calendar, date);
    }

    /**
     * Метод, парсящий ICalendar и возвращающий все занятия за заданный день
     *
     * @param calendar ICalendar, содержающий все задания за 2 недели с текущей даты
     * @param date     дата
     * @return расписание за заданный
     */
    private String parseCalendar(String calendar, Date date) {
        DateFormat dfEqual = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfPrint = new SimpleDateFormat("HH:mm");
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
}
