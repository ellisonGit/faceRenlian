package main.java.com.ha.service;


import main.java.com.ha.pojo.TimeRecordsPic;

import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:43
 * Modified:
 */
public interface TimeRecordsPicService {
    int insertTimeRecordsPic(TimeRecordsPic timeRecordsPic);
    int updateFlag(String empId);
    int updateUrl(String picLocal,String picExtranet);
    List<TimeRecordsPic> selectList();
}
