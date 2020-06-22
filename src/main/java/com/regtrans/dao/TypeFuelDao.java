package com.regtrans.dao;

import com.regtrans.model.Driver;
import com.regtrans.model.TypeFuel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository("typeFuelDao")
public class TypeFuelDao implements DaoInterface<TypeFuel> {

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
    public List<TypeFuel> findAll() {
        Session session = sessionFactory.openSession();
        List<TypeFuel> typeFuels = session.createQuery("from TypeFuel tf").list();
        session.close();
        return typeFuels;
    }

    @Override
    public TypeFuel findById(int id) {
        return null;
    }

    @Override
    public TypeFuel save(TypeFuel entity) {
        return null;
    }

    @Override
    public TypeFuel delete(TypeFuel entity) {
        return null;
    }

    @Override
    public TypeFuel update(TypeFuel entity) {
        return null;
    }
}
