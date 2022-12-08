package org.bot.Services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bot.Entity.Ntf;
import org.bot.Entity.SendMessage;
import org.bot.ORM.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Класс для работы с базой данных с помощью hibernate
 */
public class NtfServices {
    private final SessionFactory sessionFactory;

    /**
     * Констурктор по умолчанию
     */
    public NtfServices() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     *
     * @return список всех оповещений
     */
    public List<Ntf> getNtfList() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Ntf.class);
        Root<Ntf> root = criteriaQuery.from(Ntf.class);
        criteriaQuery.select(root);
        Query query = session.createQuery(criteriaQuery);
        List<Ntf> ntfList = query.getResultList();
        session.close();
        return ntfList;
    }

    /**
     *
     * @return список статуса всех оповещений
     */
    public List<SendMessage> getSendMessageList() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(SendMessage.class);
        Root<SendMessage> root = criteriaQuery.from(SendMessage.class);
        criteriaQuery.select(root);
        Query query = session.createQuery(criteriaQuery);
        List<SendMessage> sendMessageList = query.getResultList();
        session.close();
        return sendMessageList;
    }

    /**
     * Сохраняет переданое сообщение в базу данных
     * @param ntf
     */
    public void buildTransaction(Ntf ntf) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(ntf);
        tx.commit();
        session.close();
    }

    /**
     * Обновляет текст сообщения по id
     * @param id транзакции
     * @param content содержание сообщения
     */
    public void updateTransactionContent(Long id, String content) {
        //save example - existing row in table
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Ntf ntf = (Ntf) session.get(Ntf.class, id);
        //update some data
        ntf.setContent(content);
        tx.commit();
        sessionFactory.close();
    }

    /**
     * Обновляет статус сообщения на отправленный
     * @param id транзакции
     */
    public void updateSendStatus(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Ntf ntf = (Ntf) session.get(Ntf.class, id);
        ntf.getSendMessage().setSend(true);
        tx.commit();
        sessionFactory.close();
    }

    /**
     * Создает элемент для базы данных
     * @param content текст сообщения
     * @param chatId id пользователя
     * @param date дата когда нужно отправить сообщения
     * @return элемент для базы данных
     */
    public Ntf createNotification(String content, Long chatId, Date date) {
        Ntf ntf = new Ntf();
        ntf.setContent(content);
        ntf.setChatId(chatId);
        ntf.setDate(date);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setSend(false);
        ntf.setSendMessage(sendMessage);
        sendMessage.setNtf(ntf);
        return ntf;
    }

    /**
     * Выводит все элементы из датабазы
     */
    public void printAllSendMessageDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        for (SendMessage sendMessage : new NtfServices().getSendMessageList()) {
            System.out.println(sendMessage.toString());
        }
        session.close();
    }

    public void printAllNtfDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        for (Ntf ntf : new NtfServices().getNtfList()) {
            System.out.println(ntf.toString());
        }
        session.close();
    }

    /**
     * С помощью SQL запроса запрашивает все не отправленные сообщения
     * @return Список всех не отправленных сообщений
     */
    public List<SendMessage> getAllNotSendMessage() {
        Transaction transaction = null;
        List<SendMessage> result;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        result = new ArrayList<>(session.createQuery("select id from SendMessage where isSend = false", SendMessage.class).getResultList());
        transaction.commit();
        return result;
    }

    /**
     * Метод, который по id ищет строчку в нем
     * @param searchUserId Id который нужно найти в базе данных
     * @return объект класса ntf которому присвоен такой id
     */
    public Ntf findById(String searchUserId) {
        Transaction transaction = null;
        Ntf result;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        Query<Ntf> query = session.createQuery("SELECT b FROM Ntf b WHERE b.id = :id", Ntf.class);
        query.setParameter("id", searchUserId);
        result = query.uniqueResult();
        transaction.commit();
        return result;
    }
}