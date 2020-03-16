package main.java.com.ha.pojo;

import lombok.Data;


/**
 * Description:设备当前白名单表
 * User:  Ellison
 * Date: 2019-08-13
 * Time: 11:56
 * Modified:
 */
@Data
public class ClockWhiteCard {

    private String clockId ;

    private String cardId ;

    private String cardSn ;

    private String empId ;

    private String empFname ;

    private int cardtype;

    private String cardtypecode;

    private String areacode;
    private String timebound;



}
