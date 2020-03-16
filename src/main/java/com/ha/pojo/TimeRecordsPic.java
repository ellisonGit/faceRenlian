package main.java.com.ha.pojo;

import lombok.Data;


/**
 * Description:打卡图片记录表
 * User:  TimeRecords
 * Date: 2019-11-13
 * Time: 11:56
 * Modified:
 */
@Data
public class TimeRecordsPic {

    private String empId ;

    private String signTime ;

    private int flag ;

    private String picLocal ;

    private String picExtranet ;//外网图片地址






}
