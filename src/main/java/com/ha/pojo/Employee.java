package main.java.com.ha.pojo;

import lombok.Data;

import java.util.Date;

/**
 * Description:员工表
 * User:  Ellison
 * Date: 2019-08-13
 * Time: 11:56
 * Modified:
 */
@Data
public class Employee {

    private String empId ;

    private String cardId ;

    private String empFname ;

    private String departId ;

    //标识（0：未同步，1：已同步）
    private int Bless;

    private String openId;

    //通过时间
    private Date passtime;
    //通过地点
    private String doorname;
    //进去方向
    private String inoutflag;

    private byte[] photo;



}
