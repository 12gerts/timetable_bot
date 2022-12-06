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

public class NtfServices {
    private final SessionFactory sessionFactory;

    public NtfServices() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

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

    public void BuildTransaction(Ntf ntf) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(ntf);
        tx.commit();
        session.close();
    }

    public void UpdateTransactionContennt(Long id, String content) {
        //save example - existing row in table
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Ntf ntf = (Ntf) session.get(Ntf.class, new Long(id));
        //update some data
        ntf.setContent(content);
        tx.commit();
        sessionFactory.close();
    }

    public Ntf CreateNotification(String content, Long chatId) {
        Ntf ntf = new Ntf();
        ntf.setContent(content);
        ntf.setChatId(chatId);
        ntf.setDate(new Date());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setSend(false);
        ntf.setSendMessage(sendMessage);
        sendMessage.setNtf(ntf);
        return ntf;
    }

    public void PrintAllSendMessageDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        for (SendMessage sendMessage : new NtfServices().getSendMessageList()) {
            System.out.println(sendMessage.toString());
        }
        session.close();
    }

    public void PrintAllNtfDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        for (Ntf ntf : new NtfServices().getNtfList()) {
            System.out.println(ntf.toString());
        }
        session.close();
    }
    public List<SendMessage> getAllNotSendMessage() {
        Transaction transaction = null;
        List<SendMessage> result;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = new ArrayList<>(session.createQuery("select id from SendMessage where isSend = false", SendMessage.class).getResultList());
            transaction.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
        return result;
    }
}