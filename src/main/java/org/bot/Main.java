package org.bot;

import org.bot.Services.NtfServices;


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
        System.out.println(ntfServices.findById("1"));

    }
}
