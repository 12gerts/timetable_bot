package org.bot.Services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bot.Entity.Ntf;
import org.bot.ORM.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

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
        List<Ntf> bookList = query.getResultList();
        session.close();
        return bookList;
    }

}