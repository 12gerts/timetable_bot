package org.bot.Telegram;

import org.bot.Logic;
import org.bot.Reader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;

public class Telegram extends TelegramLongPollingBot {
    private final Logic logic = new Logic();
    public static HashMap<String, String> map = new HashMap<>();
    Reader reader = new Reader();


    /**
     * Метод, который читает имя бота из файла и присваивает его в BOT_NAME
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        String BOT_NAME;
        try {
            BOT_NAME = reader.readFile("src/main/java/org/bot/Telegram/name.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return BOT_NAME;
    }

    /**
     * Метод, который читает токен бота из файла и присваивает его в BOT_TOKEN
     *
     * @return токен бота
     */
    @Override
    public String getBotToken() {
        String BOT_TOKEN;
        try {
            BOT_TOKEN = reader.readFile("src/main/java/org/bot/Telegram/token.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return BOT_TOKEN;
    }

    /**
     * Метод, который принимает и отправляет сообщения в телеграмм
     *
     * @param update входящее обновление
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMess = update.getMessage();
                String chatId = inMess.getChatId().toString();

                if (!map.containsKey(chatId)) {
                    map.put(chatId, null);
                }

                String response = logic.parseMessage(inMess.getText(), chatId);

                SendMessage outMess = new SendMessage();
                outMess.setChatId(chatId);
                outMess.setText(response);

                execute(outMess);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}