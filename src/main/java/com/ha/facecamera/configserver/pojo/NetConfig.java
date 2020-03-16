package main.java.com.ha.facecamera.configserver.pojo;

public class NetConfig extends PojoAdapter {

	private String mac;
	private String ip;
	private String netmask;
	private String gateway;
	
	/**
	 * 获取设备MAC地址
	 * 
	 * @return 设备网卡物理编号
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * 设置设备MAC地址
	 * 
	 * @param mac 新的物理地址
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * 获取设备内网IP地址
	 * 
	 * @return 设备内网IPV4地址
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置设备内网IP地址
	 * 
	 * @param ip 设备内网IPV4地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取设备子网掩码
	 * 
	 * @return 设备子网掩码
	 */
	public String getNetmask() {
		return netmask;
	}

	/**
	 * 设置设备子网掩码
	 * 
	 * @param netmask 设备子网掩码
	 */
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	/**
	 * 获取设备默认网关
	 * 
	 * @return 设备默认网关
	 */
	public String getGateway() {
		return gateway;
	}

	/**
	 * 设置设备默认网关
	 * 
	 * @param gateway 设备默认网关
	 */
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
}
