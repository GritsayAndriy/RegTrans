package com.regtrans.service;

import com.regtrans.dao.DaoInterface;
import com.regtrans.dao.DriverDao;
import com.regtrans.model.Driver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

    public Driver saveDriver(Driver driver){
        return driverDao.save(driver);
    }

}
