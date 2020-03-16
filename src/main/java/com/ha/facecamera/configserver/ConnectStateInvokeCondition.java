package main.java.com.ha.facecamera.configserver;

/**
 * 设备上线/下线触发条件枚举
 * <br>
 * 除了设备上下线，还有一些如删除进度等也是使用这个枚举控制
 * 
 * @author 林星
 *
 */
public enum ConnectStateInvokeCondition {
	/**
	 * 通过序列号
	 */
	SnKnown,
	/**
	 * 通过设备编号
	 */
	DeviceNoKnown,
	/**
	 * 通过设备编号或序列号
	 * <br>
	 * 设置此项时可能会回调两次（早期不支持数据封包中发送序列号的设备可能只会回调一次）
	 */
	DevicenoOrSnKnown
}