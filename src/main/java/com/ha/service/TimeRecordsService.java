package main.java.com.ha.service;


import main.java.com.ha.pojo.TimeRecords;

import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:43
 * Modified:
 */
public interface TimeRecordsService {
    int insertTimeRecords(TimeRecords timeRecords);
    List<TimeRecords> selectAll();
}
