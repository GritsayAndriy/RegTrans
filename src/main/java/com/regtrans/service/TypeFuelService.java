package com.regtrans.service;

import com.regtrans.dao.DaoInterface;
import com.regtrans.model.Driver;
import com.regtrans.model.TypeFuel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("typeFuelService")
public class TypeFuelService {

    private DaoInterface<TypeFuel> typeFuelDao;

    @Autowired
    @Qualifier("typeFuelDao")
    public void setTypeFuelDao(DaoInterface<TypeFuel> typeFuelDao) {
        this.typeFuelDao = typeFuelDao;
    }

    public List<TypeFuel> getAllTypeFuel(){
        return typeFuelDao.findAll();
    }
}
