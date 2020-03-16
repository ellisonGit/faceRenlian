package main.java.com.ha.facecamera.configserver;

import java.text.MessageFormat;
import java.util.Date;
import java.util.ResourceBundle;

public final class Constants {

	/**
	 * 预设值<br>将发送数据到设备的操作返回值置为此值表示数据校验成功，数据已写入网卡<br>将从设备接收数据的操作返回值置为此值表示数据校验成功，已拼接成实体
	 */
	public static final String NOERROR = "no error";
	/**
	 * 预设值<br>将数据发送到设备发生了网络超时，超时发生在数据发送阶段
	 */
	public static final String SENDTIMEOUT = "send data to remote-device timeout";
	
	/**
	 * 预设值，将对象的过期时间设置为此值时对象永不过期
	 */
	public static final Date LONGLIVE = new Date();
	/**
	 * 预设值，将对象的过期时间设置为此值时对象处于未启用状态
	 */
	public static final Date DISABLED = new Date(LONGLIVE.getTime()+1);
	
	public static final int MESSAGE_ID_REBOOT = 0; 						// 重启设备
	public static final int MESSAGE_ID_HEARTBEAT = 2; 					// 心跳包
    public static final int MESSAGE_ID_ACK = 4; 						// 响应包
    public static final int MESSAGE_ID_DATA = 5; 						// 数据包
    public static final int MESSAGE_ID_VERSION_GET = 6; 				// 查询设备版本信息
    public static final int MESSAGE_ID_REG = 7; 						// 登陆验证
    public static final int MESSAGE_ID_TIME_GET = 11; 					// 查询设备时间
    public static final int MESSAGE_ID_TIME_SET = 12; 					// 设置设备时间
    public static final int MESSAGE_ID_NETCFG_GET = 15; 				// 获取设备网络配置
    public static final int MESSAGE_ID_STREAM = 103; 					// 视频数据
    public static final int MESSAGE_ID_FACEDELETEPROGRESSINFO = 302;	// 人脸删除进度
    public static final int MESSAGE_ID_GETALLPERSONID = 400; 			// 获取所有人员编号
    public static final int MESSAGE_ID_LISTFACE = 401; 					// 浏览人脸模板库
    public static final int MESSAGE_ID_ADDFACE = 402; 					// 添加人像对比数据
    public static final int MESSAGE_ID_MODIFYFACE = 403; 				// 修改人像数据
    public static final int MESSAGE_ID_DELETEFACE = 404; 				// 删除人像数据
    public static final int MESSAGE_ID_APPCONFIG_GET = 804; 			// 查询应用参数
    public static final int MESSAGE_ID_APPCONFIG_SET = 805; 			// 设置应用参数
    public static final int MESSAGE_ID_APPCONFIG_RESET = 806; 			// 重置应用参数
    
    public static final int ACK_CODE_SUCCESS = 0;
    
    public static final int TWIS_IMG_W = 150; // 归一化图像宽度
    public static final int TWIS_IMG_H = 150; // 归一化图像高度
    
    
    private static ResourceBundle i18ns = ResourceBundle.getBundle("HaSdkmessage"); // 错误或提示信息国际化内容    
    /**
     * 获取错误或提示信息的国际化表示
     * 
     * @param msg 信息键值
     * @param params 需要格式化的参数
     * @return 信息的国际化表示
     */
    public static String i18nMessage(String msg, Object... params) {
    	if(params != null && params.length > 0)
    		return MessageFormat.format(i18ns.getString(msg), params);
    	return i18ns.getString(msg);
    }
}
