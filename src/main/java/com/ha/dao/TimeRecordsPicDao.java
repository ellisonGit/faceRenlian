package main.java.com.ha.dao;

import main.java.com.ha.pojo.TimeRecordsPic;

import java.util.List;


/**
 * Description:
 * User: Ellison
 * Date: 2019-11-15
 * Time: 14:37
 * Modified:
 */
public interface TimeRecordsPicDao {

 int insertTimeRecordsPic(TimeRecordsPic timeRecordsPic);

 int updateFlag(String empId);

 int updateUrl(String picLocal,String picExtranet);

 List<TimeRecordsPic> selectList();



}
