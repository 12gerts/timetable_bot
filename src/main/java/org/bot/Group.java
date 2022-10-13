package org.bot;

import com.google.gson.Gson;
import org.bot.Http.HttpRequest;
import org.bot.Http.ParserJson;
import org.bot.Telegram.Telegram;

public class Group {
    /**
     * Поле, хранящее номер внутренний номер учебной группы
     */
    private String numberOfGroup = null;

    /**
     * Метод, запрашивающий номер группы и возвращающий внутренний номер
     *
     * @return Внутренний номер группы/null
     */
    public String getNumberOfGroup() {
        return numberOfGroup;
    }

    /**
     * Метод, обрабатывающий номер группы и возвращающий внутренний номер
     *
     * @param group номер группы
     * @return внутренний номер группы
     */
    public String convertAndUpdateNumberOfGroup(String group, String chatId) {
        HttpRequest request = new HttpRequest();
        String response = request.getInnerNumber(group);

        if (response != null && response.length() > 6) {
            ParserJson responseJson = new Gson().fromJson(response, ParserJson.class);
            if (responseJson.suggestions.length != 0) {
                Telegram.map.replace(chatId, String.valueOf(responseJson.suggestions[0].data));
                return Telegram.map.get(chatId);
            }
        }
        Telegram.map.replace(chatId, null);
        return null;
    }

    /**
     * Метод, сбрасывающий или меняющий внутренний номер группы
     *
     * @return Сообщение об ошибке/об успешном исходе
     */

    public String checkGroupChange(String chatId) {
        if (Telegram.map.get(chatId) == null) {
            return Report.REQUEST_ERROR;
        } else {
            return Report.GROUP_CHANGE;
        }
    }
}
