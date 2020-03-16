package main.java.com.ha.facecamera.configserver.pojo;

/**
 * 设备时间
 * 
 * @author 林星
 *
 */
public class Time extends PojoAdapter {

	private byte timeZone;
	private String date;
	private String time;
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Time)) return false;
		Time that = (Time) obj;
		if(timeZone != that.timeZone) return false;
		if(date == null && that.date != null) return false;
		if(date != null && that.date == null) return false;
		if(!date.equals(that.date)) return false;
		if(time == null && that.time != null) return false;
		if(time != null && that.time == null) return false;
		if(!time.equals(that.time)) return false;
		return true;
	}
	
	/**
	 * 获取时区
	 * 
	 * @return 时区
	 */
	public byte getTimeZone() {
		return timeZone;
	}
	/**
	 * 获取日期
	 * 
	 * @return 日期
	 */
	public String getDate() {
		return date;
	}
	/**
	 * 获取时间
	 * 
	 * @return 时间
	 */
	public String getTime() {
		return time;
	}
	/**
	 * 设置时区
	 * 
	 * @param timeZone 时区
	 */
	public void setTimeZone(byte timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * 设置日期
	 * <br>
	 * 例如2017/12/06
	 * 
	 * @param date 日期
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * 设置时间
	 * <br>
	 * 例如17:37:05
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

}
