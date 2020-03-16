package main.java.com.ha.facecamera.configserver;

/**
 * 配置服务器启动配置参数
 * 
 * @author 林星
 *
 */
public final class ConfigServerConfig {
	/**
	 * 设备上线/下线触发条件
	 */
	public ConnectStateInvokeCondition connectStateInvokeCondition = ConnectStateInvokeCondition.SnKnown;
	
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
	 * 心跳间隔，单位为秒，这个参数有两个作用
	 * <br>
	 * 一是用作连接保持时间，设备超过这个时间*3没有发过来数据则判断设备离线了；服务器会每隔这个时间*2向设备发送心跳包
	 * <br>
	 * 二是当一个新的套接字连接进来，经过这个时间还没有被服务端认可（服务端向其请求的信息没响应）
	 */
	public int heartBeatInterval = 5;
	/**
	 * 强制刷新应用参数到设备
	 * <br>
	 * 当上层应用请求设置应用参数到设备时，SDK首先会判断目标参数是否未修改(因为SDK缓存了设备应用参数)
	 * <br>
	 * 如果要设置的参数与SDK缓存的对应设备参数完全一致，且为开启此配置，则设置不生效
	 */
	public boolean forceSetAppConfig = true;
	/**
	 * 是否在设备重连时自动重新打开视频数据拉取
	 * <br>
	 * 如果设备已经支持，就可以关闭
	 */
	public boolean autoRestartStream = true;
	/**
	 * 预计设备单路带宽，单位为Byte/S（字节每秒）
	 * <br>
	 * 在服务端向设备发送数据时，可能遭遇未知的网络耗时，处理不当会造成整个程序阻塞；因此使用此参数约束预期设备带宽，当数据小于此值，则允许1秒超时，数据超出但没超过两秒的量则允许2秒超时，以此类推
	 * <br>
	 * 如果希望超时较快，则将此值设置大一些；如果网络状况不好，则可将值设置小一些多等待一下
	 * <br>
	 * 实际使用中可能遭遇数据队列发送，因此此值可以比预期小一点
	 */
	public int expectBandWidth = 200 * 1024;
	/**
	 * 关闭JNI库的调用（不使用JNA去加载人脸特征值相关功能函数）
	 * <br>
	 * 启用之后人脸图片的预处理功能全部不可用，受影响的功能有校验图片和从全新图片添加、修改人脸
	 */
	public boolean noNativeMode = false;
}
