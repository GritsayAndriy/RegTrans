package com.regtrans.service;

import com.regtrans.dao.DaoInterface;
import com.regtrans.model.Driver;
import com.regtrans.model.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteException;

import java.sql.SQLException;
import java.util.List;

@Service("transportService")
public class TransportService {

    private DaoInterface<Transport> transportDao;
    @Autowired
    @Qualifier("transportDao")
    public void setTransportDao(DaoInterface<Transport> transportDao) {
        this.transportDao = transportDao;
    }

    public void findAll(){
        transportDao.findAll().forEach(System.out::println);
    }

    public List<Transport> getAllTransports(){
        return transportDao.findAll();
    }

    public Transport save(Transport transport) throws SQLiteException {
        return transportDao.save(transport);
    }

    public Transport updateTransport(Transport transport){
        return transportDao.update(transport);
    }

    public Transport deleteTransport(Transport transport){
        return transportDao.delete(transport);
    }
}
