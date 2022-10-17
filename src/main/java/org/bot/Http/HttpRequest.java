package org.bot.Http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Класс, формирующий и отправляющий GET-запросы к API УрФУ
 */
public class HttpRequest implements IHttpRequest{
    /**
     * Метод, формирующий GET-запрос для получения внутреннего номера группы
     *
     * @param group общий номер группы
     * @return внутренний номер группы
     */
    @Override
    public String getInnerNumber(String group) {
        String query = "https://urfu.ru/api/schedule/groups/suggest/?query=" + group;
        return request(query);
    }

    /**
     * Метод, формирующий GET-запрос для получения ICalendar с расписанием занятий
     *
     * @param innerNumber внутренний номер группы
     * @return ICalendar с расписанием занятий
     */
    @Override
    public String getSchedule(String innerNumber) {
        String query = "https://urfu.ru/api/schedule/groups/calendar/" + innerNumber;
        return request(query);
    }

    /**
     * Метод, отправляющий GET-запрос к API УрФУ
     *
     * @param query GET-запрос
     * @return ответ от сервера
     */
    private String request(String query) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.connect();

            StringBuilder sb = new StringBuilder();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                return sb.toString();
            }
        } catch (IOException cause) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}

