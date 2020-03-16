package main.java.com.ha.task;


import main.java.com.ha.pojo.TimeRecordsPic;
import main.java.com.ha.qiniu.qiniuFile;
import main.java.com.ha.service.impl.EmployeeServiceImpl;
import main.java.com.ha.service.impl.TimeRecordsPicServiceImpl;
import main.java.com.ha.util.MyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-16
 * Time: 17:15
 * Modified:
 */
@Component
public class TimeRecordsPicTask {
    @Autowired
    private TimeRecordsPicServiceImpl timeRecordsPicServiceImpl;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;
    private final static Logger logger = LoggerFactory.getLogger(TimeRecordsPicTask.class);
   // @Scheduled(cron="0 */5 * * * ?")   //每1fen钟执行一次
   @Scheduled(cron="*/20 * * * * ?")   //每10秒钟执行一次
    public void syncTimeRecordsData() throws ParseException, IOException {
       List<TimeRecordsPic> list= timeRecordsPicServiceImpl.selectList();
       if(list.size()>0){
           for (int i = 0; i < list.size(); i++) {
               TimeRecordsPic timeR = list.get(i);
               String picUrl = timeR.getPicLocal();//获取图片地址

               String empId = timeR.getEmpId();//获取图片地址
              String openId= employeeServiceImpl.selectOpenid(empId);
              if(openId==null || openId.length()==0){
                  logger.info(empId+"此人员未绑定！"+openId);
              }
              else{
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  String requestTimestamp=sdf.format(new Date());//请求时间
                  int one = picUrl.lastIndexOf("/");//截取最后一个/的值
                  String picValue=picUrl.substring((one+1),picUrl.length());
                  new qiniuFile().upload(picUrl,picValue);
                  String url= qiniuFile.publicFile(picValue, MyConfig.qiniuUrl);//七牛返回的图片地址
                  String ret=TimeRecordsPicServiceImpl.pushMsgPic(openId,url,requestTimestamp);
                  if("SUCCESS".equals(ret)){
                      int flag=timeRecordsPicServiceImpl.updateFlag(empId);
                      if(flag>0){
                          logger.info("工号："+empId+"》》更新成功");
                      }
                      int picUr=timeRecordsPicServiceImpl.updateUrl(picUrl,url);
                      if(picUr>0){
                          logger.info("外网图片地址:》》更新成功");
                      }
                  }

              }
           }
       }

   }
}
