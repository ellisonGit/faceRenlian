package main.java.com.ha.service.impl;

import main.java.com.ha.dao.EmployeeDao;
import main.java.com.ha.facecamera.configserver.ConfigServer;
import main.java.com.ha.facecamera.configserver.pojo.Face;
import main.java.com.ha.pojo.Employee;
import main.java.com.ha.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: Ellison
 * Date: 2019-11-07
 * Time: 14:47
 * Modified:
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public int getMaxNRecSeq() {
        return this.employeeDao.getMaxNRecSeq();
    }
    @Override
    public   int updateState(Employee empId) {
        return this.employeeDao. updateState(empId);
    }

    @Override
    public  List<Employee>  getQrcode(Map<String, Object> params) {
        return employeeDao.getQrcode(params);
    }


    @Override
    public  String selectOpenid(String empId) {
        return employeeDao.selectOpenid(empId);
    }


    public List<Employee>  showerweima(String sn) {
ConfigServer cs=null;
        Map<String, Object> params = new HashMap<String, Object>();
        List<Employee> resMap = employeeService.getQrcode(params);
        for (int i = 0; i < resMap.size(); i++) {
            Employee emp = resMap.get(i);
            File file = new File(System.getProperty("user.dir") + "/ligong"+i+".jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            try {
                bos.write(emp.getPhoto());
            } catch (IOException e) {
                e.printStackTrace();
            }
            final String filePath = System.getProperty("user.dir") + "/ligong"+i+".jpg";
            Face f = new Face();
            f.setId("lihao"+i);
            f.setName("李浩"+i);
            f.setRole(1);
            f.setJpgFilePath(new String[]{filePath});
            String[] a = f.getJpgFilePath();
            boolean b = cs.getCameraOnlineState(sn);
            boolean c = cs.addFace(sn, f, 500);
            System.out.println("输出c:" + c);
            System.out.println("输出b："+b);
        }

        return resMap ;

    }





}
