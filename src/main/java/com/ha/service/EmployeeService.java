package main.java.com.ha.service;

import main.java.com.ha.pojo.Employee;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:43
 * Modified:
 */
public interface EmployeeService {

    int updateState(Employee empId);

    int getMaxNRecSeq();

    String selectOpenid(String empId);
    List<Employee> getQrcode(Map<String, Object> params);
}
