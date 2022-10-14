package org.bot;

import org.bot.Http.HttpRequest;
import org.bot.Telegram.Telegram;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс, реализующий базовую логику бота
 */
public class Logic {
    private String lastMessage = null;
    private final String[] commandSchedule = {"/today", "/tomorrow", "/week", "/weeks"};
    private final String[] allCommand = {"/today", "/tomorrow", "/week", "/change", "/weeks"};
    Group group = new Group();

    public String parseMessage(String textMsg, String chatId) {

        String response;
        //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
        if (Arrays.asList(allCommand).contains(lastMessage) && Arrays.asList(allCommand).contains(textMsg)) {
            lastMessage = null;
        }

        if (textMsg.equals("/start")) {
            response = getHello();
        } else if (textMsg.equals("/help")) {
            response = Report.HELP_REPORT;
        } else if (textMsg.equals("/change")) {
            response = Report.AUTHORIZATION_REPORT;
        } else if (Objects.equals(lastMessage, "/change")) {
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            response = group.checkGroupChange(chatId);
            //вводится номер группы
        } else if (Arrays.asList(commandSchedule).contains(lastMessage) && Telegram.map.get(chatId) == null) {
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            if (Telegram.map.get(chatId) == null) {
                response = Report.REQUEST_ERROR;
            } else {
                response = getReport(lastMessage, Telegram.map.get(chatId));
            }
        } else {
            response = getReport(textMsg, Telegram.map.get(chatId));
        }
        lastMessage = textMsg;
        return response;
    }

    /**
     * Метод, вызывающий приветственное сообщение
     *
     * @return приветственное сообщение
     */
    public String getHello() {
        return Report.START_REPORT;
    }

    /**
     * Метод, обрабатывающий команды, полученные от пользователя
     *
     * @param report команда пользавателя
     * @return ответ пользователю: расписание или предупреждающее сообщение
     */
    public String getReport(String report, String innerGroup) {
        Week week = new Week();
        HttpRequest request = new HttpRequest();

        if (innerGroup == null && Arrays.asList(commandSchedule).contains(report)) {
            return Report.AUTHORIZATION_REPORT;
        }
        String calendar = request.getSchedule(innerGroup);
        return switch (report) {
            case "/today" -> week.today(calendar);
            case "/tomorrow" -> week.tomorrow(calendar);
            case "/week" -> week.week(calendar, 7);
            case "/weeks" -> week.week(calendar, 14);
            default -> Report.DEFAULT_REPORT;
        };
    }
}

