package main.java.com.ha.dao;

import main.java.com.ha.pojo.TimeRecords;

import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-04-29
 * Time: 14:37
 * Modified:
 */
public interface TimeRecordsDao {

 int insertTimeRecords(TimeRecords timeRecords);

 List<TimeRecords>selectAll();

}
