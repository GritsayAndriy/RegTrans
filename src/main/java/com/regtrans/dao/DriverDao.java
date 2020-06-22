package com.regtrans.dao;

import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
@Repository("driverDao")
public class DriverDao implements DaoInterface<Driver>{

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public Driver findById(int id) {
        Session session = sessionFactory.openSession();
        Driver driver = session.get(Driver.class, id);
        session.close();
        return driver;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Driver> findAll() {
        Session session = sessionFactory.openSession();
        List<Driver> drivers = session.createNamedQuery("Driver.getAllWithTimeSheet").list();
        session.close();
        return drivers;
    }

    @Transactional(readOnly = true)
    public List<Driver> getWithSomeMonthTimeSheet(String from, String to) {
        Session session = sessionFactory.openSession();
        List<Driver> drivers = session.createNamedQuery("Driver.getWithSomeMonthTimeSheet").list();
        for (Driver driver: drivers){
            driver.setTimeSheets((List<TimeSheet>)  session.createNamedQuery("TimeSheet.getTimeSheetSomeMonthAndDriver")
                    .setParameter("fromDay", from)
                    .setParameter("toDay", to)
                    .setParameter("driver", driver)
                    .list());
        }
        session.close();
        return drivers;
    }

    @Override
    public Driver save(Driver driver){
        Session session = sessionFactory.openSession();
        Transaction tx1 = session.beginTransaction();
        session.saveOrUpdate(driver);
        tx1.commit();
        session.close();
        return driver;
    }

    @Override
    public Driver delete(Driver entity) {
        Session session = sessionFactory.openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(entity);
        tx1.commit();
        session.close();
        return entity;
    }

    @Override
    public Driver update(Driver entity){
        Session session = sessionFactory.openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(entity);
        tx1.commit();
        session.close();
        return entity;
    }


}
