package org.bot;

import org.bot.Telegram.Telegram;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        Notification mThing;
        try {
            mThing = new Notification();

            Thread myThready = new Thread(mThing);
            //myThready.start();

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}