package org.bot.Notification;

import org.bot.Entity.Notification;
import org.bot.Repository.INotificationRepository;
import org.bot.Services.INotificationService;
import org.bot.Telegram.IMessageSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.NoSuchElementException;

public class NotificationSender implements Runnable {
    private final INotificationService notificationService;
    private final INotificationRepository notificationRepository;
    private final IMessageSender messageSender;

    public NotificationSender(INotificationRepository notificationRepository, IMessageSender messageSender, INotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.messageSender = messageSender;
        this.notificationService = notificationService;
    }

    public void trySendNotification() throws TelegramApiException {
        Date date = notificationRepository.earliestDate();
        if (!date.before(new Date())) {
            return;
        }
        Notification notification = notificationService.findById(String.valueOf(notificationRepository.getIdByDate(date)));
        messageSender.sendMessage(
                notification.getChatId().toString(),
                notification.getSubject() + "\n" + notification.getContent());
        notificationService.setMessageSent(notification.getId());
        notificationRepository.remove(date);
    }

    public void run() {
        while (true) {
            try {
                trySendNotification();
            } catch (NoSuchElementException ignored) {
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            try {
                int sleepTime = 1000;
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
