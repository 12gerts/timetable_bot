package org.bot.Notification;

import org.bot.Entity.Ntf;
import org.bot.Services.NtfServices;
import org.bot.Telegram.Telegram;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.NoSuchElementException;

import static org.bot.Notification.Handler.treeMap;

public class Notification
        implements Runnable
{
    NtfServices ntfServices = new NtfServices();
    public void run()
    {
        while (true) {
            try {
                Date date = treeMap.firstKey();
                if (date.before(new Date())) {
                    Ntf ntf = ntfServices.findById(String.valueOf(treeMap.get(date)));
                    new Telegram().sendMessage(
                            ntf.getChatId().toString(),
                            ntf.getSubject() + "\n" + ntf.getContent());
                    ntfServices.updateSendStatus(ntf.getId());
                    treeMap.remove(date);
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException e) {
                continue;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
