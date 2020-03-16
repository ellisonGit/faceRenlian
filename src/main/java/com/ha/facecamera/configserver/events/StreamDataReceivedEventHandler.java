package main.java.com.ha.facecamera.configserver.events;

/**
 * 收到设备视频数据事件
 * 
 * @author 林星
 *
 */
@FunctionalInterface
public interface StreamDataReceivedEventHandler {

	/**
	 * 收到设备视频数据事件处理函数
	 * 
	 * @param deviceID 设备标识符
	 * @param h264 h264数据段
	 */
	void onStreamDataReceived(String deviceID, byte[] h264);
}
