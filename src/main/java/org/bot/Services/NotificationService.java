package org.bot.Services;

import org.bot.Entity.Notification;
import org.bot.Entity.SendMessageFlag;
import org.bot.ORM.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с базой данных с помощью hibernate
 */
public class NotificationService implements INotificationService {
    private final SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    /**
     * Конструктор по умолчанию
     */
    public NotificationService() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    private void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    private void closeSessionAndTransaction() {
        transaction.commit();
        session.close();
    }


    /**
     * Сохраняет переданное сообщение в базу данных
     */
    @Override
    public void saveNotification(Notification notification) {
        openSessionAndTransaction();
        session.persist(notification);
        closeSessionAndTransaction();
    }

    /**
     * Обновляет статус сообщения на отправленный
     *
     * @param id транзакции
     */
    @Override
    public void setMessageSent(Long id) {
        openSessionAndTransaction();
        Notification notification = session.get(Notification.class, id);
        notification.getSendMessageFlag().setSend(true);
        closeSessionAndTransaction();
    }

    /**
     * С помощью SQL запроса запрашивает все не отправленные сообщения
     *
     * @return Список всех не отправленных сообщений
     */
    @Override
    public List<SendMessageFlag> getAllNotSentMessages() {
        openSessionAndTransaction();
        List<SendMessageFlag> result;
        result = new ArrayList<>(session.createQuery("select id from SendMessageFlag where isSend = false", SendMessageFlag.class).getResultList());
        closeSessionAndTransaction();
        return result;
    }

    /**
     * Метод, который по id ищет строчку в нем
     *
     * @param searchUserId Id который нужно найти в базе данных
     * @return объект класса ntf которому присвоен такой id
     */
    @Override
    public Notification findById(String searchUserId) {
        openSessionAndTransaction();
        Notification result;
        Query<Notification> query = session.createQuery("select b from Notification b where b.id = :id", Notification.class);
        query.setParameter("id", searchUserId);
        result = query.uniqueResult();
        closeSessionAndTransaction();
        return result;
    }
}