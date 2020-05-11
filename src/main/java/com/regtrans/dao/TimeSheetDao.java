package com.regtrans.dao;

import com.regtrans.model.TimeSheet;
import com.regtrans.model.Transport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository("timeSheetDao")
public class TimeSheetDao implements DaoInterface<TimeSheet>{

    private SessionFactory sessionFactory;

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TimeSheet> findAll() {
        Session session = sessionFactory.openSession();
        List<TimeSheet> transports =  session.createQuery("From TimeSheet").list();
        session.close();
        return transports;
    }


    @Transactional(readOnly = true)
    public TimeSheet findById(int id) {
        Session session = sessionFactory.openSession();
        TimeSheet timeSheet = session.get(TimeSheet.class, id);
        session.close();
        return timeSheet;
    }

    @Override
    public TimeSheet save(TimeSheet entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
        return entity;
    }

    @Override
    public TimeSheet delete(TimeSheet entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
        return entity;
    }

}
