package org.bot.Repository;

import org.bot.Entity.Notification;
import org.bot.Entity.SendMessageFlag;
import org.bot.Services.INotificationService;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class NotificationRepository implements INotificationRepository {
    private final INotificationService notificationService;
    private final TreeMap<Date, Long> DateToId = new TreeMap<>();

    public NotificationRepository(INotificationService notificationService) {
        this.notificationService = notificationService;

    }

    @Override
    public Long getIdByDate(Date date) {
        return DateToId.get(date);
    }

    @Override
    public void put(Date date, Long id) {
        DateToId.put(date, id);
    }

    @Override
    public Date earliestDate() {
        return DateToId.firstKey();
    }

    @Override
    public void remove(Date date) {
        DateToId.remove(date);
    }

    public void fillWithNotSentMessages() {
        List<SendMessageFlag> notSendMessage = notificationService.getAllNotSentMessages();
        for (int i = 0; i < notSendMessage.size(); i++) {
            Notification notification = notificationService.findById(String.valueOf(notSendMessage.get(i)));
            DateToId.put(notification.getDate(), notification.getId());
        }
    }
}
