package main.java.com.ha.facecamera.configserver.events;

import java.util.Date;

import main.java.com.ha.facecamera.configserver.ConfigServer;

/**
 * 设备请求鉴权的事件
 * 
 * @author 林星
 * 
 */
@FunctionalInterface
public interface CameraAuthingEventHandler {

	/**
	 * 设备请求鉴权的事件
	 * <br>
	 * 设备连接之后，会自行发送(在设备开启了注册功能之后)
	 * <br>
	 * <b>
	 * 注意：由于当前版本没有做强验证开关（必须验证通过才进行连接），因此可能出现一种情况，即设备已经成功接入{@link ConfigServer#onCameraConnected(CameraConnectedEventHandler) cameraConnected}之后才会触发鉴权；不过理论上不会出现这个问题，因为设备先行连接和发送数据
	 * </b>
	 * 
	 * @param time 设备时间
	 * @param username 登陆用户名
	 * @param password 登陆密码
	 * @return 是否鉴权成功，如果返回false，服务端会立即断开设备
	 */
	boolean onCameraAuthing(Date time, String username, byte[] password);
}
