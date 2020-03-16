package main.java.com.ha.facecamera.configserver.pojo;

import java.util.Date;

public class RegData extends PojoAdapter {

	private Date time;
	private String userName;
	private byte[] password;
	
	/**
	 * 获取设备时间
	 * 
	 * @return 设备时间
	 */
	public Date getTime() {
		return time;
	}
	/**
	 * 设置设备时间
	 * 
	 * @param time 设备时间
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	/**
	 * 获取登陆用户名
	 * 
	 * @return 用户名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置登陆用户名
	 * 
	 * @param userName 用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取登陆密码
	 * 
	 * @return 密码(MD5加密过的)
	 */
	public byte[] getPassword() {
		return password;
	}
	/**
	 * 设置登陆密码
	 * 
	 * @param password 密码(MD5加密过的)
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}
}
