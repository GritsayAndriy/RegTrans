package com.regtrans.dao;

import com.regtrans.model.TimeSheet;
import com.regtrans.model.Trailer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.sqlite.SQLiteException;

import java.util.List;

@Transactional
@Repository("trailerDao")
public class TrailerDao implements DaoInterface<Trailer> {

    private SessionFactory sessionFactory;

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Trailer> findAll() {
        Session session = sessionFactory.openSession();
        List<Trailer> trailers =  session.createQuery("From Trailer").list();
        session.close();
        return trailers;
    }

    @Override
    public Trailer findById(int id) {
        return null;
    }

    @Override
    public Trailer save(Trailer entity) throws SQLiteException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
        return entity;
    }

    @Override
    public Trailer delete(Trailer entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
        return entity;
    }

    @Override
    public Trailer update(Trailer entity) {
        Session session = sessionFactory.openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(entity);
        tx1.commit();
        session.close();
        return entity;
    }
}
