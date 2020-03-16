package main.java.com.ha.dao;

import main.java.com.ha.pojo.Employee;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:37
 * Modified:
 */
public interface EmployeeDao {


    int getMaxNRecSeq();

    int updateState(Employee empId);

    String selectOpenid(String empId);

   List<Employee> getQrcode(Map<String, Object> params);



}
