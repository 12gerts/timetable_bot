package org.bot.Services;

import org.bot.Entity.Notification;
import org.bot.Entity.SendMessageFlag;

import java.util.List;

public interface INotificationService {
    void saveNotification(Notification notification);

    void setMessageSent(Long id);

    List<SendMessageFlag> getAllNotSentMessages();

    Notification findById(String searchUserId);
}
