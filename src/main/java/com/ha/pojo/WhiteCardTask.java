package main.java.com.ha.pojo;

import lombok.Data;

import java.util.Date;

/**
 * Description:白名单任务表
 * User:  Ellison
 * Date: 2019-08-13
 * Time: 11:56
 * Modified:
 */
@Data
public class WhiteCardTask {
    private  String id;

    private String cardId ;

    private String cardSn ;

    private String empId ;

    private String empFname ;

    //标识（0：新增，1：已删除）
    private int flag;

    private int clockId;

    private int cardtype;

    private String cardtypecode;

    private String areacode;
    //时间
    private Date opdate;

    private String operator;

    private String ExecuteDate;

    private String timebound;

    private String RealCardNo;

    private String deptName;

    private String remark;

    private byte[] photo;




}
