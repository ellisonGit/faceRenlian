package main.java.com.ha.util;


import main.java.com.ha.constant.WechatAccount;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Description:
 * User: Ellison
 * Date: 2019-03-26
 * Time: 15:37
 * Modified:
 */
public class AccessTokenUtil {

    public static WxAccessToken accessToken = new WxAccessToken();

    //文件地址
    public static final String filePath=System.getProperty("user.dir")+"/src/resources/access_token.text";

    /**
     * 获取上次access_token时间
     * @param str
     *
     */
    public static Date getTime(String str){
      //获取=后面的字符串
        String get_time=str.substring(str.indexOf("=")+1,str.length());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date=format.parse(get_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 读取access_token文件
     * @param
     *
     */
    public static String readText() {
        //文件内容变量
        String sTimg = "";
        String str = "";
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);

            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            //读取内容
            String lineText = null;
            while ((lineText = bufferedReader.readLine()) != null) {
                sTimg = bufferedReader.readLine();
                if(sTimg!=null){
                    str = lineText;
                    //获取上次   access_token时间
                    Date getTime = getTime(sTimg);
                    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    long m = new Date().getTime() - getTime.getTime();
                    if (m / 1000 > 7200) {
                        str=rebuildText();
                        System.out.println("超时！");
                    }

                }
                }

            read.close();
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String token=str.substring(str.indexOf("=")+1,str.length());
        System.out.println("内存里的access_token："+token);
        return token;
    }

    /**
     * 重新生成文本内容
     */
    public static String rebuildText() throws FileNotFoundException {
        String token=  WxServerUtil.getAccessToken(WechatAccount.HNJCA.getAppId(), WechatAccount.HNJCA.getSecret());
        File file=new File(filePath);
        try {
            FileWriter fw=new FileWriter(file);
            fw.write("");
            //获取系统时间
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now=new Date();
            String now_str=format.format(now);
            //更新内容
            StringBuilder strNew= new StringBuilder();
            strNew.append("access_token="+token+"\n");
            strNew.append("get_time="+now_str);
            //重新写入文件
            fw.write(strNew.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
          System.out.println("重新请求access_token："+token);
        return token;
    }

}
