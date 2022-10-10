package org.bot;

import com.google.gson.Gson;
import org.bot.Http.HttpRequest;
import org.bot.Http.ParserJson;

import java.util.Scanner;

public class Group {
    /**
     * Поле, хранящее номер внутренний номер учебной группы
     */
    private String numberOfGroup = null;

    /**
     * Меторд, обрабатывающий номер группы и возвращающий внутренний номер
     *
     * @return Внутренний номер группы/null
     */
    public String getNumberOfGroup() {
        if (numberOfGroup == null) {
            System.out.println(Report.AUTHORIZATION_REPORT);
            Scanner input = new Scanner(System.in);
            String command = input.nextLine();

            HttpRequest request = new HttpRequest();
            String response = request.getInnerNumber(command);

            if (response.length() > 6) {
                ParserJson responseJson = new Gson().fromJson(response, ParserJson.class);
                if (responseJson.suggestions.length != 0) {
                    numberOfGroup = String.valueOf(responseJson.suggestions[0].data);
                }
            }
        }
        return numberOfGroup;
    }

    /**
     * Метод, сбрасывающий или меняющий внутренний номер группы
     *
     * @return Сообщение об ошибке/об успешном исходе
     */
    public String changeGroup() {
        numberOfGroup = null;
        if (getNumberOfGroup() == null) {
            return Report.AUTHORIZATION_ERROR;
        } else {
            return Report.GROUP_CHANGE;
        }
    }
}

