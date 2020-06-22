package com.regtrans.service;

import com.regtrans.dao.DaoInterface;
import com.regtrans.dao.DriverDao;
import com.regtrans.model.Driver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service("driverService")
public class DriverService{

    private DaoInterface<Driver> driverDao;

    @Autowired
    @Qualifier("driverDao")
    public void setDriverDao(DaoInterface<Driver> driverDao) {
        this.driverDao = driverDao;
    }

    public List<Driver> getAllDrivers(){
        return driverDao.findAll();
    }

    public List<Driver> getAllDrivers(String from, String to){
        DriverDao driverDao = (DriverDao) this.driverDao;
        return driverDao.getWithSomeMonthTimeSheet(from, to);
    }
    public Driver saveDriver(Driver driver) throws SQLiteException {
        return driverDao.save(driver);
    }

    public Driver updateDriver(Driver driver){
        return driverDao.update(driver);
    }

    public Driver deleteDriver(Driver driver){
        return driverDao.delete(driver);
    }

}
