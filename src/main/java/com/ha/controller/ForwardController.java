package main.java.com.ha.controller;


import main.java.com.ha.pojo.*;
import main.java.com.ha.service.*;
import main.java.com.ha.util.Face;
import main.java.com.ha.util.MyConfig;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: Ellison
 * Date: 2019-11-8
 * Time: 14:39
 * Modified:
 */
@RestController
@RequestMapping(value = "/forward",produces = "application/json;charset=utf-8")
public class ForwardController {
    private final static Logger logger = LoggerFactory.getLogger(ForwardController.class);
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private  TimeRecordsService timeRecordsService;
    @Autowired
    private TimeRecordsPicService timeRecordsPicService;

    @Autowired
    private WhiteCardTaskService whiteCardTaskService;

    @Autowired
    private ClockWhiteCardService clockWhiteCardService;

    /**
     * 查询
     */

    @PostMapping(value = "/getimg")
    public List<Employee> showerweima() {

        Map<String, Object> params = new HashMap<String, Object>();
        List<Employee> resMap = employeeService.getQrcode(params);
        for (int i = 0; i < resMap.size(); i++) {
            Employee emp = resMap.get(i);
            String dirName=System.getProperty("user.dir") + "/"+emp.getEmpId()+".jpg";
            File file = new File(System.getProperty("user.dir") + "/"+emp.getEmpId()+".jpg");
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



        }
            return resMap;

        }

    /**
     * update
     */

    @GetMapping(value = "/updateState")
    public int updateState( String empId) {
      //  JSONObject jsonObject = JSONObject.fromObject(Data);
        //String empId = jsonObject.get("emp_id").toString();//员工编号
        if (empId!=null) {
            Employee emp=new Employee();
            emp.setEmpId(empId);
            emp.setBless(1);
            int resMap = employeeService.updateState(emp);
            if (resMap > 0) {

                return resMap;
            }
        }
        return -1;
    }

    /**
     * insert
     */

    @PostMapping(value = "/insertTimeRecords")
    public int insertTimeRecords(@RequestBody String Data) {
        JSONObject jsonObject = JSONObject.fromObject(Data);
        String emp_id = jsonObject.get("emp_id").toString();//员工编号
        String timestamp = jsonObject.get("Timestamp").toString(); //打卡时间
        TimeRecords timeR=new TimeRecords();
        timeR.setEmpId(emp_id);
        timeR.setSignTime(timestamp);
        timeR.setCardId("2");
        timeR.setClockId(2);
        if(timeR!=null){
            int res= timeRecordsService.insertTimeRecords(timeR);
            if(res>0){
                logger.info("打卡记录插入成功！");
                return 0;
            }
        }

        return -1;
    }


    @GetMapping(value = "/select")
    public int selectAll() {

        List<TimeRecords> res= timeRecordsService.selectAll();
            if(res!=null){
                logger.info("结果：",res.size());
                return 0;
            }
        return -1;
        }


    /**
     * insert pic_local插入人脸图片
     */
    @PostMapping(value = "/insertTimeRecordsPic")
    public int insertTimeRecordsPic(@RequestBody String Data) {
        JSONObject jsonObject = JSONObject.fromObject(Data);
        String emp_id = jsonObject.get("emp_id").toString();//员工编号
        String timestamp = jsonObject.get("Timestamp").toString(); //打卡时间
        String pic_local = jsonObject.get("pic_local").toString(); //人脸图片路径
        TimeRecordsPic timeR=new TimeRecordsPic();
        timeR.setEmpId(emp_id);
        timeR.setSignTime(timestamp);
        timeR.setFlag(0);
        timeR.setPicLocal(pic_local);
        if(timeR!=null){
            int res= timeRecordsPicService.insertTimeRecordsPic(timeR);
            if(res>0){
                logger.info("人脸图片记录信息插入成功！");
                return 0;
            }
        }

        return -1;
    }

    /**
     * 查白名单whitecardtask表
     */
    @PostMapping(value = "/getWhitecardtask")
    public List<WhiteCardTask> getWhitecardtask() {
        List<WhiteCardTask> resMap = whiteCardTaskService.selectNoWhite();
        if(resMap.size()>0){
            for (int i = 0; i < resMap.size(); i++) {
                WhiteCardTask emp = resMap.get(i);
                String dirName= MyConfig.lujing +"/Photo";
                Face. createDir(dirName);
                File file = new File( MyConfig.lujing +"/Photo/"+emp.getEmpId()+".jpg");
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
            }
            return resMap;
        }
       return resMap;
    }

    /**
     * 添加下发白名单记录
     */
    @PostMapping(value = "/insertWhite")
    public String insertWhite(@RequestBody String paraData) {
        JSONObject jsonObject = JSONObject.fromObject(paraData);
        ClockWhiteCard clock=new ClockWhiteCard();
        String clockId = jsonObject.get("clockId").toString();
        String cardId = jsonObject.get("cardId").toString();
        String cardSn = jsonObject.get("cardSn").toString();
        String empId = jsonObject.get("empId").toString();
        String empFname = jsonObject.get("empFname").toString();
        String cardtype = jsonObject.get("cardtype").toString();
        String cardtypecode = jsonObject.get("cardtypecode").toString();
        String areacode = jsonObject.get("areacode").toString();

        clock.setAreacode(areacode);
        clock.setCardId(cardId);
        clock.setCardSn(cardSn);
        clock.setCardtype(Integer.parseInt(cardtype));
        clock.setCardtypecode(cardtypecode);
        clock.setClockId(clockId);
        clock.setEmpFname(empFname);
        clock.setEmpId(empId);
        int res = clockWhiteCardService.insertClockWhite(clock);
       if(res>0){
           return "success";
       }
        return "fail";

    }

    /**
     * 下发白名单之后更新白名单标记信息
     */
    @PostMapping(value = "/updateWhite")
    public String updateWhite(@RequestBody String paraData) {
        WhiteCardTask white=new WhiteCardTask();
        JSONObject jsonObject = JSONObject.fromObject(paraData);
        String id = jsonObject.get("id").toString();
        String cardtypecode = jsonObject.get("cardtypecode").toString();
        String areacode = jsonObject.get("areacode").toString();
        String cardId = jsonObject.get("cardId").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String requestTimestamp = sdf.format(new Date());//时间戳
        white.setExecuteDate(requestTimestamp);
        white.setRealCardNo(cardtypecode+areacode+cardId);
        white.setId(id);
        int res = whiteCardTaskService.updateState(white);
        if(res>0){
            return "success";
        }
        return "fail";

    }


    /**
     * 查删除的白名单
     */
    @PostMapping(value = "/getWhitecardtaskDel")
    public List<WhiteCardTask> getWhitecardtaskDel() {
        List<WhiteCardTask> resMap = whiteCardTaskService.selectNoWhiteDel();
        return resMap;
    }

    /**
     * 查设备是否存在
     */
    @PostMapping(value = "/getIsexist")
    public List<ClockWhiteCard> getIsexist(@RequestBody String paraData) {
        JSONObject jsonObject = JSONObject.fromObject(paraData);
        ClockWhiteCard clock=new ClockWhiteCard();
        String clockId = jsonObject.get("clockId").toString();
        String empId = jsonObject.get("empId").toString();
        clock.setClockId(clockId);
        clock.setEmpId(empId);
        List<ClockWhiteCard> resMap = clockWhiteCardService.selectIsexist(clock);
        return resMap;
    }

    /**
     * 查已删除的白名单
     */
    @PostMapping(value = "/getWhiteDel")
    public List<WhiteCardTask> getWhiteDel() {
        List<WhiteCardTask> resMap = whiteCardTaskService.selectWhiteDel();
        return resMap;
    }

    /**
     * 删除白名单
     */
    @PostMapping(value = "/delClockWhite")
    public String delClockWhite(@RequestBody String paraData) {
        JSONObject jsonObject = JSONObject.fromObject(paraData);
        ClockWhiteCard clock=new ClockWhiteCard();
        String clockId = jsonObject.get("clockId").toString();
        String empId = jsonObject.get("empId").toString();
        String cardId=jsonObject.get("cardId").toString();
        clock.setCardId(cardId);
        clock.setClockId(clockId);
        clock.setEmpId(empId);
        int resMap = clockWhiteCardService.delClockWhite(clock);
        if(resMap>0){
            return "success";
        }
        return "fail";
    }

}
