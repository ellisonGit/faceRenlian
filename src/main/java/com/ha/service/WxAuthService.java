package main.java.com.ha.service;


import main.java.com.ha.pojo.WXWebToken;

/**
 * Description:
 * User: by yangyong
 * Date: 2018-09-25
 * Time: 20:28
 * Modified:
 */
public interface WxAuthService {

    WXWebToken getWXAccessToken(String appId, String secret, String code);


}
