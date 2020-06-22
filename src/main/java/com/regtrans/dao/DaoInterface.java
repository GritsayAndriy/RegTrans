package com.regtrans.dao;

import org.hibernate.SessionFactory;
import org.sqlite.SQLiteException;

import java.sql.SQLException;
import java.util.List;

public interface DaoInterface<T> {

    public void setSessionFactory(SessionFactory sessionFactory);
    public SessionFactory getSessionFactory();
    public List<T> findAll();
    public T findById(int id);
    public T save(T entity) throws SQLiteException;
    public T delete(T entity);
    public T update(T entity);
}
