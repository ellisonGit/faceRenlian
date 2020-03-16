package main.java.com.ha.service.impl;


import main.java.com.ha.dao.TimeRecordsDao;

import main.java.com.ha.pojo.TimeRecords;

import main.java.com.ha.service.TimeRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Description:
 * User: Ellison
 * Date: 2019-11-07
 * Time: 14:47
 * Modified:
 */
@Service
public class TimeRecordsServiceImpl implements TimeRecordsService {

    @Autowired
    private TimeRecordsDao timeRecordsDao;


    @Override
    public   int insertTimeRecords(TimeRecords timeRecords) {
        return timeRecordsDao. insertTimeRecords(timeRecords);
    }

    @Override
    public  List<TimeRecords>selectAll( ) {
        return timeRecordsDao. selectAll();
    }




}
