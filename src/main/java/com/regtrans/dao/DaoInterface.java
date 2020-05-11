package com.regtrans.dao;

import org.hibernate.SessionFactory;

import java.util.List;

public interface DaoInterface<T> {

    public void setSessionFactory(SessionFactory sessionFactory);
    public SessionFactory getSessionFactory();
    public List<T> findAll();
    public T findById(int id);
    public T save(T entity);
    public T delete(T entity);
}
