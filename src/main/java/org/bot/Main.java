package org.bot;

import org.bot.ORM.HibernateUtil;
import org.bot.Telegram.Telegram;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    public static void main(String[] args) {

//        Session session = HibernateUtil.getSessionFactory().openSession();
//        for (Ntf ntf : new NtfServices().getNtfList()) {
//            System.out.println(ntf.getContent());
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

