package com.regtrans.service;

import com.regtrans.dao.DaoInterface;
import com.regtrans.model.TimeSheet;
import com.regtrans.model.Trailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteException;

import java.util.List;

@Service("trailerService")
public class TrailerService {

    private DaoInterface<Trailer> trailerDao;

    @Autowired
    @Qualifier("trailerDao")
    public void setTrailerDao(DaoInterface<Trailer> trailerDao) {
        this.trailerDao = trailerDao;
    }

    public List<Trailer> getTrailers(){
        return trailerDao.findAll();
    }

    public Trailer save(Trailer trailer) throws SQLiteException {
        return trailerDao.save(trailer);
    }

    public Trailer update(Trailer trailer){
        return trailerDao.update(trailer);
    }

    public Trailer delete(Trailer trailer){
        return trailerDao.delete(trailer);
    }
}
