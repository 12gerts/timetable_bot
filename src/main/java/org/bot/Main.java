package org.bot;

import org.bot.Entity.Ntf;
import org.bot.Entity.SendMessage;
import org.bot.ORM.HibernateUtil;
import org.bot.Services.NtfServices;
import org.bot.Telegram.Telegram;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Date;
import java.util.Iterator;


public class Main {
    public static void main(String[] args) {
//        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//
//        Notification mThing;
//        try {
//            mThing = new Notification();
//
//            Thread myThready = new Thread(mThing);
//            //myThready.start();
//
//            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            telegramBotsApi.registerBot(new Telegram());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
        NtfServices ntfServices = new NtfServices();
//        Ntf ntf = ntfServices.CreateNotification("testDb", 123L);
//        ntfServices.UpdateTransactionContennt(302L, "cringe");
        System.out.println(ntfServices.getAllNotSendMessage());

    }
}
