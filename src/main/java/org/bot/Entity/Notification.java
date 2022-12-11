package org.bot.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long chatId;

    private String content;

    private String subject;

    private Date date;

    @OneToOne(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SendMessageFlag sendMessageFlag;

    /**
     * Создает элемент для базы данных
     *
     * @param content текст сообщения
     * @param chatId  id пользователя
     * @param date    дата когда нужно отправить сообщения
     * @return элемент для базы данных
     */
    public static Notification createNotification(String content, Long chatId, Date date, String subject) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setChatId(chatId);
        notification.setDate(date);
        notification.setSubject(subject);
        SendMessageFlag sendMessageFlag = new SendMessageFlag();
        sendMessageFlag.setSend(false);
        notification.setSendMessageFlag(sendMessageFlag);
        sendMessageFlag.setNotification(notification);
        return notification;
    }

    @Override
    public String toString() {
        return "Id = " + id + "\nchatId = " + chatId + "\ncontent = " + content + "\ndate = " + date;
    }
}
