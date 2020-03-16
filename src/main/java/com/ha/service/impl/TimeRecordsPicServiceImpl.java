package main.java.com.ha.service.impl;

import main.java.com.ha.dao.TimeRecordsPicDao;
import main.java.com.ha.enums.InfoEnum;
import main.java.com.ha.pojo.TemplateJson;
import main.java.com.ha.pojo.TimeRecordsPic;
import main.java.com.ha.service.TimeRecordsPicService;
import main.java.com.ha.util.TemplateMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * Description:
 * User: Ellison
 * Date: 2019-11-15
 * Time: 14:47
 * Modified:
 */
@Service
public class TimeRecordsPicServiceImpl implements TimeRecordsPicService {

    @Autowired
    private TimeRecordsPicDao timeRecordsPicDao;


    @Override
    public   int insertTimeRecordsPic(TimeRecordsPic timeRecordsPic) {
        return timeRecordsPicDao. insertTimeRecordsPic(timeRecordsPic);
    }

    @Override
    public   List<TimeRecordsPic> selectList() {
        return this. timeRecordsPicDao. selectList();
    }
    @Override
    public   int updateFlag(String empId) {
        return timeRecordsPicDao. updateFlag(empId);
    }

    @Override
    public   int updateUrl(String picLocal,String picExtranet) {
        return timeRecordsPicDao. updateUrl(picLocal,picExtranet);
    }
    /**
     * 推送ren消息
     * @param openid
     * @return
     */
    public static String pushMsgPic(String openid,String url,String dateTime){

        if(openid == null || ("").equals(openid)){
            return InfoEnum.NO_OPENID.toString();
        }
        openid="o5hcr6K994hNS8U9z-ACua6y4q0c";
        TemplateJson templateJson = new TemplateJson();
        templateJson.setTouser(openid);
        templateJson.setTemplate_id("CCHmBzIIFQKXWJWxOjE_WNzVSONSHaxn20Rn7wXu6YA");
        templateJson.setUrl(url);
        templateJson.setDataFirstValue("考勤通知");
        templateJson.setDataKeyWord1Value("");
        templateJson.setDataKeyWord2Value(dateTime);
        templateJson.setDataKeyWord3Value("");
        templateJson.setDataKeyWord4Value("");
        templateJson.setDataKeyWord5Value("订餐窗口");
        templateJson.setDataRemarkValue("点击详情可查看更多信息");

        boolean result = TemplateMsgUtil.sendTemplateMsg(templateJson);
        if(result){
            return "SUCCESS";
        }else{
            return "template message send failed !";
        }
    }


}
