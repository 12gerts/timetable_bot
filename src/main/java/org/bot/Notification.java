package org.bot;

import org.bot.Telegram.Telegram;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

class Notification            //Нечто, реализующее интерфейс Runnable
        implements Runnable        //(содержащее метод run())
{
    public void run()        //Этот метод будет выполняться в побочном потоке
    {
        while (true) {
            try {
                new Telegram().sendMessage("956982270", "notificationMessage");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
