package main.java.com.ha.facecamera.configserver;

/**
 * 数据服务器启动配置参数
 * 
 * @author 林星
 *
 */
public final class DataServerConfig {
	
	/**
	 * 数据封包中系统类型
	 */
	public int sysType = 12;
	/**
	 * 数据封包中主版本号
	 */
	public int majorProtocol = 0;
	/**
	 * 数据封包中次版本号
	 */
	public int minorProtocol = 9;
	
	/**
	 * 心跳间隔，单位为秒
	 * <br>
	 * 由于数据链路中服务端从不主动发数据，为避免被设备判断离线而断开，需要定时发送心跳；这个值比超时小即可
	 */
	public int heartBeatInterval = 10;
	/**
	 * 设备上线/下线触发条件
	 */
	public ConnectStateInvokeCondition connectStateInvokeCondition = ConnectStateInvokeCondition.SnKnown;
	/**
	 * 是否维持空闲数据链接
	 * <br>
	 * 空闲链接的定义是还未传输过数据的链接，当链接第一次传输了数据，这个链接就会一直被维持。
	 * 在此之前，如果此值为false，则服务端不会主动维护（不主动断开，但设备理论上会在15秒后断开以节约资源），因此可能会出现重复上下线
	 */
	public boolean keepAliveOnIdleConnection = true;
	/**
	 * 是否在未知连接属性（设备编号，设备序列号；未知的条件一般是还未传输过数据）时触发上下线事件
	 */
	public boolean invokeConnectEventOnAnonymous = false; 

}
