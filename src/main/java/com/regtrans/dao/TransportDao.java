package com.regtrans.dao;

import com.regtrans.model.Transport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository("transportDao")
public class TransportDao implements DaoInterface<Transport> {

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
    public Transport findById(int id) {
        Session session = sessionFactory.openSession();
        Transport transport = session.get(Transport.class, id);
        session.close();
        return transport;
    }

    @Override
    public Transport save(Transport entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
        return entity;
    }

    @Override
    public Transport delete(Transport entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
        return entity;
    }


    @Transactional(readOnly = true)
    @Override
    public List<Transport> findAll() {
        Session session = sessionFactory.openSession();
        List<Transport> transports =  session.createQuery("From Transport").list();
        session.close();
        return transports;
    }
}
