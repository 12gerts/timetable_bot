package org.bot;

import org.bot.Notification.Handler;
import org.bot.Notification.Notification;
import org.bot.Telegram.Telegram;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;



public class Main {
    public static void main(String[] args) {

        Notification notification;
        Handler handler = new Handler();
        handler.createTreeMap();

        try {
            notification = new Notification();

            Thread threadNotification = new Thread(notification);
            threadNotification.start();

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
