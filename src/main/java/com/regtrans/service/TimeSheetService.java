package com.regtrans.service;

import com.regtrans.dao.DaoInterface;
import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteException;

import java.time.LocalDate;
import java.util.List;

@Service("timeSheetService")
public class TimeSheetService {

    private DaoInterface<TimeSheet> timeSheetDao;

    @Autowired
    @Qualifier("timeSheetDao")
    public void setTimeSheetDao(DaoInterface<TimeSheet> timeSheetDao) {
        this.timeSheetDao = timeSheetDao;
    }

    public List<TimeSheet> getAllTimeSheet(){
        return timeSheetDao.findAll();
    }

    public TimeSheet save(TimeSheet timeSheet) throws SQLiteException {
        return timeSheetDao.save(timeSheet);
    }

    public TimeSheet unselectTimeSheet(TimeSheet timeSheet){
        return timeSheetDao.delete(timeSheet);
    }

//    public void
}
