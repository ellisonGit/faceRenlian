package main.java.com.ha.facecamera.configserver.pojo;

/**
 * 版本信息
 * 
 * @author 林星
 *
 */
public class Version extends PojoAdapter {

	private String sn;
	private String protocolVersion;
	private String firewareVersion;
	private String codeVersion;
	private String buildDate;
	private String sysType;
	private String hardwarePlatform;
	private String sensorModel;
	private String algorithmVersion;
	private String sdkMinVersion;
	private long faceDemoMinVersion;

	/**
	 * 设置设备序列号
	 * 
	 * @param sn
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	/**
	 * 设置协议版本号
	 * 
	 * @param protocolVersion 协议版本号
	 */
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	/**
	 * 设置固件版本号
	 * 
	 * @param firewareVersion 固件版本号
	 */
	public void setFirewareVersion(String firewareVersion) {
		this.firewareVersion = firewareVersion;
	}
	
	/**
	 * 设置应用版本号
	 * 
	 * @param codeVersion 应用版本号
	 */
	public void setCodeVersion(String codeVersion) {
		this.codeVersion = codeVersion;
	}
	
	/**
	 * 设置构建日期
	 * 
	 * @param buildDate 构建日期
	 */
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	/**
	 * 获取设备序列号
	 * 
	 * @return 序列号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 获取设备协议版本
	 * 
	 * @return 协议版本
	 */
	public String getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * 获取设备固件版本
	 * 
	 * @return 固件版本
	 */
	public String getFirewareVersion() {
		return firewareVersion;
	}

	/**
	 * 获取设备应用版本
	 * 
	 * @return 应用版本
	 */
	public String getCodeVersion() {
		return codeVersion;
	}

	/**
	 * 获取设备应用构建时间
	 * 
	 * @return 应用构建时间
	 */
	public String getBuildDate() {
		return buildDate;
	}
	
	/**
	 * 获取系统类型
	 * 
	 * @return 系统类型
	 */
	public String getSysType() {
		return sysType;
	}
	/**
	 * 设置系统类型
	 * 
	 * @param sysType 系统类型
	 */
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	/**
	 * 获取硬件平台
	 * 
	 * @return 硬件平台
	 */
	public String getHardwarePlatform() {
		return hardwarePlatform;
	}
	/**
	 * 设置硬件平台
	 * 
	 * @param hardwarePlatform 硬件平台信息
	 */
	public void setHardwarePlatform(String hardwarePlatform) {
		this.hardwarePlatform = hardwarePlatform;
	}

	/**
	 * 获取传感器类型
	 * 
	 * @return 传感器类型
	 */
	public String getSensorModel() {
		return sensorModel;
	}
	/**
	 * 设置传感器类型
	 * 
	 * @param sensorModel 传感器类型信息
	 */
	public void setSensorModel(String sensorModel) {
		this.sensorModel = sensorModel;
	}

	/**
	 * 获取算法版本信息
	 * 
	 * @return 算法版本信息
	 */
	public String getAlgorithmVersion() {
		return algorithmVersion;
	}
	/**
	 * 设置算法版本信息
	 * 
	 * @param algorithmVersion 算法版本信息
	 */
	public void setAlgorithmVersion(String algorithmVersion) {
		this.algorithmVersion = algorithmVersion;
	}

	/**
	 * 获取最低sdk版本号
	 * 
	 * @return 最低sdk版本号
	 */
	public String getSdkMinVersion() {
		return sdkMinVersion;
	}
	/**
	 * 设置最低sdk版本号
	 * 
	 * @param sdkMinVersion 最低sdk版本号
	 */
	public void setSdkMinVersion(String sdkMinVersion) {
		this.sdkMinVersion = sdkMinVersion;
	}

	/**
	 * 获取最低客户端版本号
	 * 
	 * @return 最低客户端版本号
	 */
	public long getFaceDemoMinVersion() {
		return faceDemoMinVersion;
	}
	/**
	 * 设置最低客户端版本号
	 * 
	 * @param faceDemoMinVersion 最低客户端版本号
	 */
	public void setFaceDemoMinVersion(long faceDemoMinVersion) {
		this.faceDemoMinVersion = faceDemoMinVersion;
	}

}
