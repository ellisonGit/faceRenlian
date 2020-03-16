package main.java.com.ha.service.impl;


import main.java.com.ha.constant.WXConfig;
import main.java.com.ha.enums.InfoEnum;
import main.java.com.ha.pojo.TemplateJson;
import main.java.com.ha.pojo.WXWebToken;
import main.java.com.ha.service.WxAuthService;
import main.java.com.ha.util.GsonUtil;
import main.java.com.ha.util.MyConfig;
import main.java.com.ha.util.TemplateMsgUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Description:
 * User: by ellison
 * Date: 2018-09-25
 * Time: 20:39
 * Modified:
 */
@Service
public class WXAuthServiceImpl implements WxAuthService {

    @Override
    public WXWebToken getWXAccessToken(String appId, String secret, String code) {

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL localURL = new URL(String.format(WXConfig.getWebTokenUrl, appId, secret, code));
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                }

                WXWebToken wxAccessToken = (WXWebToken) GsonUtil.jsonToObject(resultBuffer.toString(), WXWebToken.class);
                return wxAccessToken;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /**
     * 推送订餐消息
     * @param openid
     * @return
     */
    public static String pushMsgDingC(String openid,String empname,String requestTime){

        if(openid == null || ("").equals(openid)){
            return InfoEnum.NO_OPENID.toString();
        }
          //openid="olvAC0_zpG7K03tJ4MjOmD8k0jD8";
        TemplateJson templateJson = new TemplateJson();
        templateJson.setTouser(openid);
        templateJson.setTemplate_id("YckO2II7jl94Q5kR8WdT73c5ledriS2aHlxrm3WCJPQ");
        templateJson.setUrl(MyConfig.comUrl+"/dingcindex.html");
        templateJson.setDataFirstValue("亲爱的用户，您已成功订餐，请按时去用餐。");
        templateJson.setDataKeyWord1Value(empname);
        templateJson.setDataKeyWord2Value(requestTime);
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


    /**
     * 推送退餐消息
     * @param openid
     * @return
     */
    public static String pushMsgTuiC(String openid){

        if(openid == null || ("").equals(openid)){
            return InfoEnum.NO_OPENID.toString();
        }
        //openid="olvAC0_zpG7K03tJ4MjOmD8k0jD8";
        TemplateJson templateJson = new TemplateJson();
        templateJson.setTouser(openid);
        templateJson.setTemplate_id("D8aZwDWImm6A-UhCg-zpwOIR3TzRbtlNwFx49ZLOpFc");
        templateJson.setUrl(MyConfig.comUrl+"/tuidingc.html");
        templateJson.setDataFirstValue("您的订餐已退餐！ ");
        templateJson.setDataKeyWord1Value("");
        templateJson.setDataKeyWord2Value("");
        templateJson.setDataKeyWord3Value("");
        templateJson.setDataKeyWord4Value("");
        templateJson.setDataRemarkValue("点击详情可查看更多信息");

        boolean result = TemplateMsgUtil.sendTemplateMsg(templateJson);
        if(result){
            return "SUCCESS";
        }else{
            return "template message send failed !";
        }
    }
}
