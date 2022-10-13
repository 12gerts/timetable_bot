package org.bot.Telegram;


import org.bot.Group;
import org.bot.Logic;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Telegram extends TelegramLongPollingBot {
    Logic logic;
    public static HashMap<String, String> map = new HashMap<>();

    public String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public Telegram() {
        logic = new Logic();
    }

    @Override
    public String getBotUsername() {
        String BOT_NAME = null;
        try {
            BOT_NAME = readFile("src\\main\\java\\org\\bot\\Telegram\\name.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        //создаем две константы, присваиваем им значения токена и имя бота соответсвтенно
        //вместо звездочек подставляйте свои данные
        String BOT_TOKEN = null;
        try {
            BOT_TOKEN = readFile("src\\main\\java\\org\\bot\\Telegram\\token.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                //Извлекаем из объекта сообщение пользователя
                Message inMess = update.getMessage();
                //Достаем из inMess id чата пользователя
                String chatId = inMess.getChatId().toString();
                if(!map.containsKey(chatId)){
                    map.put(chatId, null);
                }
                //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
                String response = logic.parseMessage(inMess.getText(), chatId);
                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage outMess = new SendMessage();

                //Добавляем в наше сообщение id чата, а также наш ответ
                outMess.setChatId(chatId);
                outMess.setText(response);

                //Отправка в чат
                execute(outMess);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}