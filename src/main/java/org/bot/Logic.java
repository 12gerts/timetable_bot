package org.bot;

import org.bot.Http.HttpRequest;
import org.bot.Telegram.Telegram;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс, реализующий базовую логику бота
 */
public class Logic {
    /**
     * Поле, хранящее последнее сообщение пользователя
     */
    private String lastMessage = null;
    private final String[] COMMAND_SCHEDULE = {"/today", "/tomorrow", "/week", "/weeks"};
    private final String[] ALL_COMMAND = {"/today", "/tomorrow", "/week", "/change", "/weeks"};
    Group group = new Group();

    /**
     * Метод, обрабатывающий сообщения пользователя
     *
     * @param textMsg сообщение пользователя
     * @param chatId  внутренний номер чата
     * @return ответ пользователю
     */
    public String parseMessage(String textMsg, String chatId) {

        String response;
        // для случая, если несколько раз запрашиваем расписание, не вводя группу
        if (Arrays.asList(ALL_COMMAND).contains(lastMessage) && Arrays.asList(ALL_COMMAND).contains(textMsg)) {
            lastMessage = null;
        }

        if (textMsg.equals("/start")) {
            // обработка /start
            response = getHello();
        } else if (textMsg.equals("/help")) {
            // обработка /help
            response = Report.HELP_REPORT;
        } else if (textMsg.equals("/change")) {
            // просим ввести номер группы для смены
            response = Report.AUTHORIZATION_REPORT;
        } else if (Objects.equals(lastMessage, "/change")) {
            // обработка номера группы для смены
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            response = group.checkGroupChange(chatId);
        } else if (Arrays.asList(COMMAND_SCHEDULE).contains(lastMessage) && Telegram.map.get(chatId) == null) {
            // обработка номера группы для вывода расписания
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            if (Telegram.map.get(chatId) == null) {
                response = Report.REQUEST_ERROR;
            } else {
                response = getReport(lastMessage, Telegram.map.get(chatId));
            }
        } else {
            // обработка неверного ввода и вывод расписания при наличии группы
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
     * Метод, выдающий расписание и обрабатывающий неверный ввод
     *
     * @param innerGroup внутренний номер группы
     * @param report     команда пользователя
     * @return ответ пользователю: расписание или предупреждающее сообщение
     */
    public String getReport(String report, String innerGroup) {
        Week week = new Week();
        HttpRequest request = new HttpRequest();

        if (innerGroup == null && Arrays.asList(COMMAND_SCHEDULE).contains(report)) {
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

