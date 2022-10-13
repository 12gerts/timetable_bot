package org.bot;

import org.bot.Telegram.Telegram;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс, реализующий базовую логику бота
 */
public class Logic {
    private String lastMessage = null;
    private final String[] massStringA = {"/today", "/tomorrow", "/week"};
    Group group = new Group();

    public String parseMessage(String textMsg, String chatId) {

        String response;
        //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
        if (textMsg.equals("/start")) {
            response = getHello();
        } else if (textMsg.equals("/change")) {
            lastMessage = textMsg;
            return Report.AUTHORIZATION_REPORT;
        } else if (Objects.equals(lastMessage, "/change")) {
            lastMessage = textMsg;
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            return group.checkGroupChange(chatId);
        } else if (Arrays.asList(massStringA).contains(lastMessage) && Telegram.map.get(chatId) == null) {
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            if (Telegram.map.get(chatId) == null) {
                lastMessage = textMsg;
                return Report.REQUEST_ERROR;
            }
            response = getReport(lastMessage, Telegram.map.get(chatId));
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
     * @param group  экземпляр класса Group, хранящий текущую учебную группу
     * @return ответ пользователю: расписание или предупреждающее сообщение
     */
    public String getReport(String report, String group) {
        Week week = new Week();
        return switch (report) {
            case "/today" -> week.today(group);
            case "/tomorrow" -> week.tomorrow(group);
            case "/week" -> week.week(group, 7);
            case "/weeks" -> week.week(group, 14);
            case "/help" -> Report.HELP_REPORT;
            default -> Report.DEFAULT_REPORT;
        };
    }
}

