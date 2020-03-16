package main.java.com.ha.facecamera.configserver.events;

/**
 * 与设备连接断开事件
 * 
 * @author 林星
 *
 */
@FunctionalInterface
public interface CameraDisconnectedEventHandler {

	/**
	 * 与设备断开连接之后的处理函数
	 * 
	 * @param val 设备sn或者设备DeviceNo<br><b>在数据上传链路中，设备断开连接触发时入参为设备（或最近一层网络设备）的ip</b>
	 */
	void onCameraDisconnected(String val);
}
