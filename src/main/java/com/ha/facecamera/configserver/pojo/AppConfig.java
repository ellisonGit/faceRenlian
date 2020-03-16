package main.java.com.ha.facecamera.configserver.pojo;

import java.lang.reflect.Field;
import java.util.Arrays;

import main.java.com.ha.sdk.util.JSON;

/**
 * 设备应用参数
 * 
 * @author 林星
 *
 */
public class AppConfig extends PojoAdapter {
	
	private String deviceNo;
	private String addrNo;
	private String addrName;
	private byte heartBeatInterval;
	private byte cloundConfigEnable;
	private short cloundConfigPort;
	private String cloundConfigIp;
	private short dataUploadMethod;
	private short dataUploadPort;
	private String dataUploadServer;
	private String dataUploadUrl;
	private boolean compareSwitch;
	private byte workMode;
	private int ensureThreshold;
	private boolean repeatFilter;
	private int repeatFilterTime;
	private boolean uploadEnvironmentImage;
	private boolean uploadFeatureImage;
	private String ftpUserName;
	private String ftpPassword;
	private boolean ageDetect;
	private boolean sexDetect;
	private boolean livingDetect;
	private boolean safetyHatDetect;
	private boolean uploadPhoto;
	private boolean uploadFeature;
	private boolean doReg;
	private String regUserName;
	private String regPassword;
	private byte[] resv1; // “点位名称”和“心跳时间间隔”之间的保留字段
	private byte[] resv2; // “登录密码”和“数据上传方式”之间的保留字段
	private byte[] resv3; // “服务器其它信息（工作/对比模式）”和“功能控制选项”之间的保留字段
	private byte[] resv4; // 封包最后的保留字段
	private int resv5; // “功能控制选项”的原始数据，因为这个选项是Flags格式（即是用字节的不同bit位表示不同功能，害怕冲突，因此保存原始值做位运算）
	private short resv6; // “输出控制选项”的原始数据，这个选项也是Flags格式
	
	// 不能new，因为设备有些字段是不能改的（是为了保证向后兼容性），请用ConfigServer#getAppConfig获取之后改变值再set回去
	private AppConfig() {
		ensureThreshold = 75; // 添加一个默认值，避免对比开关从关到开之后出现确信分数不合法的bug
		workMode = 1; // 工作模式默认为自动
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AppConfig))
			return false;
		AppConfig that = (AppConfig) obj;
		if(deviceNo != null || that.deviceNo != null) {
			if (deviceNo == null && that.deviceNo != null)
				return false;
			if (deviceNo != null && that.deviceNo == null)
				return false;
			if (!deviceNo.equals(that.deviceNo))
				return false;
		}
		if(addrNo != null || that.addrNo != null) {
			if (addrNo == null && that.addrNo != null)
				return false;
			if (addrNo != null && that.addrNo == null)
				return false;
			if (!addrNo.equals(that.addrNo))
				return false;
		}
		if (addrName != null || that.addrName != null) {
			if (addrName == null && that.addrName != null)
				return false;
			if (addrName != null && that.addrName == null)
				return false;
			if (!addrName.equals(that.addrName))
				return false;
		}
		if (heartBeatInterval != that.heartBeatInterval)
			return false;
		if (cloundConfigEnable != that.cloundConfigEnable)
			return false;
		if (cloundConfigPort != that.cloundConfigPort)
			return false;
		if (!cloundConfigIp.equals(that.cloundConfigIp))
			return false;
		if (dataUploadMethod != that.dataUploadMethod)
			return false;
		if (dataUploadPort != that.dataUploadPort)
			return false;
		if (dataUploadServer != null || that.dataUploadServer != null) {
			if (dataUploadServer == null && that.dataUploadServer != null)
				return false;
			if (dataUploadServer != null && that.dataUploadServer == null)
				return false;
			if (!dataUploadServer.equals(that.dataUploadServer))
				return false;
		}
		if (dataUploadUrl != null || that.dataUploadUrl != null) {
			if (dataUploadUrl == null && that.dataUploadUrl != null)
				return false;
			if (dataUploadUrl != null && that.dataUploadUrl == null)
				return false;
			if (!dataUploadUrl.equals(that.dataUploadUrl))
				return false;
		}
		if (ftpUserName != null || that.ftpUserName != null) {
			if (ftpUserName == null && that.ftpUserName != null)
				return false;
			if (ftpUserName != null && that.ftpUserName == null)
				return false;
			if (!ftpUserName.equals(that.ftpUserName))
				return false;
		}
		if (ftpPassword != null || that.ftpPassword != null) {
			if (ftpPassword == null && that.ftpPassword != null)
				return false;
			if (ftpPassword != null && that.ftpPassword == null)
				return false;
			if (!ftpPassword.equals(that.ftpPassword))
				return false;
		}
		if (compareSwitch != that.compareSwitch)
			return false;
		if (workMode != that.workMode)
			return false;
		if (ensureThreshold != that.ensureThreshold)
			return false;
		if (repeatFilter != that.repeatFilter)
			return false;
		if (repeatFilterTime != that.repeatFilterTime)
			return false;
		if (uploadEnvironmentImage != that.uploadEnvironmentImage)
			return false;
		if (uploadFeatureImage != that.uploadFeatureImage)
			return false;
		if (ageDetect != that.ageDetect)
			return false;
		if (sexDetect != that.sexDetect)
			return false;
		if (livingDetect != that.livingDetect)
			return false;
		if (safetyHatDetect != that.safetyHatDetect) 
			return false;
		if (uploadPhoto != that.uploadPhoto)
			return false;
		if (uploadFeature != that.uploadFeature)
			return false;
		if (doReg != that.doReg)
			return false;
		if (regUserName != null || that.regUserName != null) {
			if (regUserName == null && that.regUserName != null)
				return false;
			if (regUserName != null && that.regUserName == null)
				return false;
			if (!regUserName.equals(that.regUserName))
				return false;
		}
		if (regPassword != null || that.regPassword != null) {
			if (regPassword == null && that.regPassword != null)
				return false;
			if (regPassword != null && that.regPassword == null)
				return false;
			if (!regPassword.equals(that.regPassword))
				return false;
		}
		if (resv1 != null || that.resv1 != null) {
			if (resv1 == null && that.resv1 != null)
				return false;
			if (resv1 != null && that.resv1 == null)
				return false;
			if (!Arrays.equals(resv1, that.resv1))
				return false;
		}
		if (resv2 != null || that.resv2 != null) {
			if (resv2 == null && that.resv2 != null)
				return false;
			if (resv2 != null && that.resv2 == null)
				return false;
			if (!Arrays.equals(resv2, resv2))
				return false;
		}
		if (resv3 != null || that.resv3 != null) {
			if (resv3 == null && that.resv3 != null)
				return false;
			if (resv3 != null && that.resv3 == null)
				return false;
			if (!Arrays.equals(resv3, resv3))
				return false;
		}
		if (resv4 != null || that.resv4 != null) {
			if (resv4 == null && that.resv4 != null)
				return false;
			if (resv4 != null && that.resv4 == null)
				return false;
			if (!Arrays.equals(resv4, resv4))
				return false;
		}
		if (resv5 != that.resv5)
			return false;
		if (resv6 != that.resv6)
			return false;
		return true;
	}

	/**
	 * 获取设备编号
	 * 
	 * @return 设备编号
	 */
	public String getDeviceNo() {
		return deviceNo;
	}

	/**
	 * 设置设备编号
	 * 
	 * @param deviceNo
	 *            设置编号
	 */
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	/**
	 * 获取点位编号
	 * 
	 * @return 点位编号
	 */
	public String getAddrNo() {
		return addrNo;
	}

	/**
	 * 设置点位编号
	 * 
	 * @param addrNo
	 *            点位编号
	 */
	public void setAddrNo(String addrNo) {
		this.addrNo = addrNo;
	}

	/**
	 * 获取点位名称
	 * 
	 * @return 点位名称
	 */
	public String getAddrName() {
		return addrName;
	}

	/**
	 * 设置点位名称
	 * 
	 * @param addName
	 */
	public void setAddrName(String addName) {
		this.addrName = addName;
	}

	/**
	 * 获取心跳间隔
	 * 
	 * @return 心跳间隔 5~255
	 */
	public short getHeartBeatInterval() {
		return (short) (heartBeatInterval & 0xff);
	}

	/**
	 * 设置心跳间隔
	 * 
	 * @param heartBeatInterval
	 *            心跳间隔 5~255
	 */
	public void setHeartBeatInterval(short heartBeatInterval) {
		this.heartBeatInterval = (byte) heartBeatInterval;
	}

	/**
	 * 设置心跳间隔
	 * 
	 * @param heartBeatInterval
	 *            心跳间隔 5~255
	 */
	public void setHeartBeatInterval(byte heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
	}

	/**
	 * 是否启用了外网配置
	 * 
	 * @return 外网配置开关
	 */
	public boolean isCloundConfigEnable() {
		return this.cloundConfigEnable == 1;
	}

	/**
	 * 设置外网配置开关
	 * 
	 * @param cloundConfigEnable
	 *            是否启用外网配置
	 */
	public void setCloundConfigEnable(boolean cloundConfigEnable) {
		this.cloundConfigEnable = (byte) (cloundConfigEnable ? 1 : 0);
	}

	/**
	 * 获取外网配置端口
	 * 
	 * @return 外网配置端口
	 */
	public short getCloundConfigPort() {
		return cloundConfigPort;
	}

	/**
	 * 设置外网配置端口
	 * 
	 * @param cloundConfigPort
	 *            外网配置端口
	 */
	public void setCloundConfigPort(short cloundConfigPort) {
		this.cloundConfigPort = cloundConfigPort;
	}

	/**
	 * 获取外网配置IP
	 * 
	 * @return 外网配置IP
	 */
	public String getCloundConfigIp() {
		return cloundConfigIp;
	}

	/**
	 * 设置外网配置IP
	 * 
	 * @param cloundConfigIp
	 *            外网配置IP
	 */
	public void setCloundConfigIp(String cloundConfigIp) {
		this.cloundConfigIp = cloundConfigIp;
	}

	/**
	 * 获取上传方式
	 * 
	 * @return 上传方式 0关闭 1TCP 2HTTP
	 */
	public short getDataUploadMethod() {
		return dataUploadMethod;
	}

	/**
	 * 设置上传方式
	 * 
	 * @param dataUploadMethod
	 *            上传方式 0关闭 1TCP 2HTTP
	 */
	public void setDataUploadMethod(short dataUploadMethod) {
		this.dataUploadMethod = dataUploadMethod;
	}

	/**
	 * 设置上传服务器端口
	 * 
	 * @return 上传服务器端口
	 */
	public short getDataUploadPort() {
		return dataUploadPort;
	}

	/**
	 * 获取上传服务器端口
	 * 
	 * @param dataUploadPort
	 *            上传服务器端口
	 */
	public void setDataUploadPort(short dataUploadPort) {
		this.dataUploadPort = dataUploadPort;
	}

	/**
	 * 获取上传服务器IP
	 * 
	 * @return 上传服务器IP
	 */
	public String getDataUploadServer() {
		return dataUploadServer;
	}

	/**
	 * 设置上传服务器IP
	 * 
	 * @param dataUploadServer
	 *            上传服务器IP
	 */
	public void setDataUploadServer(String dataUploadServer) {
		this.dataUploadServer = dataUploadServer;
	}

	/**
	 * 获取数据上传Url <br>
	 * 在http和ftp上传时表示路径，tcp时无用
	 * 
	 * @return 数据上传Url
	 */
	public String getDataUploadUrl() {
		return dataUploadUrl;
	}

	/**
	 * 设置数据上传Url <br>
	 * 在http和ftp上传时表示路径，tcp时无用
	 * 
	 * @param dataUploadUrl
	 *            数据上传Url
	 */
	public void setDataUploadUrl(String dataUploadUrl) {
		this.dataUploadUrl = dataUploadUrl;
	}

	/**
	 * 获取对比开关
	 * 
	 * @return 是否开启了对比
	 */
	public boolean isCompareSwitch() {
		return compareSwitch;
	}

	/**
	 * 设置对比开关
	 * 
	 * @param compareSwitch
	 *            是否开启对比
	 */
	public void setCompareSwitch(boolean compareSwitch) {
		this.compareSwitch = compareSwitch;
	}

	/**
	 * 获取设备工作模式
	 * 
	 * @return 1：自动模式 2：在线模式 3：离线模式
	 */
	public byte getWorkMode() {
		return workMode;
	}
	/**
	 * 设置设备工作模式
	 * 
	 * @param workMode 1：自动模式 2：在线模式 3：离线模式
	 */
	public void setWorkMode(byte workMode) {
		this.workMode = workMode;
	}

	/**
	 * 获取确信分数
	 * 
	 * @return 确信分数 70~100
	 */
	public int getEnsureThreshold() {
		return ensureThreshold;
	}

	/**
	 * 设置确信分数
	 * 
	 * @param ensureThreshold
	 *            确信分数 70~100
	 */
	public void setEnsureThreshold(int ensureThreshold) {
		this.ensureThreshold = ensureThreshold;
	}

	/**
	 * 获取是否去重复
	 * 
	 * @return 是否去重
	 */
	public boolean isRepeatFilter() {
		return repeatFilter;
	}

	/**
	 * 设置是否去除重复
	 * 
	 * @param repeatFilter
	 *            去除重复开关
	 */
	public void setRepeatFilter(boolean repeatFilter) {
		this.repeatFilter = repeatFilter;
	}

	/**
	 * 获取重复超时
	 * 
	 * @return 重复超时时间 单位为秒
	 */
	public int getRepeatFilterTime() {
		return repeatFilterTime;
	}

	/**
	 * 设置重复超时
	 * 
	 * @param repeatFilterTime
	 *            重复超时时间 单位为秒
	 */
	public void setRepeatFilterTime(int repeatFilterTime) {
		this.repeatFilterTime = repeatFilterTime;
	}

	/**
	 * 获取图片输出形式
	 * 
	 * @return 图片输出形式 0：无图 1：全景 2：特写 3：全景+特写
	 * @deprecated 函数现已不建议使用，请使用拆分后的函数{@link #isUploadEnvironmentImage()} {@link #isUploadFeatureImage()}
	 * @see #isUploadEnvironmentImage()
	 * @see #isUploadFeatureImage()
	 */
	@Deprecated
	public short getImageUploadMethod() {
		return (short) ((uploadEnvironmentImage ? 1 : 0) + (uploadFeatureImage ? 2 : 0));
	}

	/**
	 * 设置图片输出形式
	 * 
	 * @param imageUploadMethod
	 *            图片输出形式 0：无图 1：全景 2：特写 3：全景+特写
	 * @deprecated 函数现已不建议使用，请使用拆分后的函数{@link #setUploadEnvironmentImage(boolean)} {@link #setUploadFeatureImage(boolean)}
	 * @see #setUploadEnvironmentImage(boolean)
	 * @see #setUploadFeatureImage(boolean)
	 */
	@Deprecated
	public void setImageUploadMethod(short imageUploadMethod) {
		uploadEnvironmentImage = (imageUploadMethod & 0b1) == 0b1;
		uploadFeatureImage = (imageUploadMethod & 0b10) == 0b10;
	}
	
	/**
	 * 获取全景图上传标记
	 * 
	 * @return 是否上传全景图
	 */
	public boolean isUploadEnvironmentImage() {
		return uploadEnvironmentImage;
	}
	/**
	 * 设置全景图上传标记
	 * 
	 * @param uploadEnvironmentImage 是否上传全景图
	 */
	public void setUploadEnvironmentImage(boolean uploadEnvironmentImage) {
		this.uploadEnvironmentImage = uploadEnvironmentImage;
	}
	/**
	 * 获取特写图上传标记
	 * 
	 * @return 是否上传特写图
	 */
	public boolean isUploadFeatureImage() {
		return uploadFeatureImage;
	}
	/**
	 * 设置特写图上传标记
	 * 
	 * @param uploadFeatureImage 是否上传特写图
	 */
	public void setUploadFeatureImage(boolean uploadFeatureImage) {
		this.uploadFeatureImage = uploadFeatureImage;
	}

	/**
	 * 获取ftp用户名
	 * 
	 * @return ftp上传用户名
	 */
	public String getFtpUserName() {
		return ftpUserName;
	}

	/**
	 * 设置ftp用户名
	 * 
	 * @param ftpUserName
	 *            ftp上传用户名
	 */
	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}

	/**
	 * 获取ftp密码
	 * 
	 * @return ftp上传密码
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}

	/**
	 * 设置ftp密码
	 * 
	 * @param ftpPassword
	 *            ftp上传密码
	 */
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	/**
	 * 获取是否检测年龄
	 * 
	 * @return 是否检测年龄
	 */
	public boolean isAgeDetect() {
		return ageDetect;
	}
	/**
	 * 设置是否检测年龄
	 * 
	 * @param ageDetect 是否检测年龄
	 */
	public void setAgeDetect(boolean ageDetect) {
		this.ageDetect = ageDetect;
	}
	/**
	 * 获取是否检测性别
	 * 
	 * @return 是否检测性别
	 */
	public boolean isSexDetect() {
		return sexDetect;
	}
	/**
	 * 设置是否检测性别
	 * 
	 * @param sexDetect 是否检测性别
	 */
	public void setSexDetect(boolean sexDetect) {
		this.sexDetect = sexDetect;
	}
	/**
	 * 获取是否开启了活体检测
	 * 
	 * @return 是否开启了活体检测
	 */
	public boolean isLivingDetect() {
		return livingDetect;
	}
	/**
	 * 设置是否开启了活体检测
	 * 
	 * @param livingDetect 是否开启了活体检测
	 */
	public void setLivingDetect(boolean livingDetect) {
		this.livingDetect = livingDetect;
	}
		
	/**
	 * 获取是否开启了安全帽检测
	 * 
	 * @return 是否开启了安全帽检测
	 */
	public boolean isSafetyHatDetect() {
		return safetyHatDetect;
	}
	/**
	 * 设置安全帽检测开关
	 * 
	 * @param safetyHatDetect 是否开启安全帽检测
	 */
	public void setSafetyHatDetect(boolean safetyHatDetect) {
		this.safetyHatDetect = safetyHatDetect;
	}

	/**
	 * 获取是否上传模板图片(在抓拍数据上传时)
	 * 
	 * @return 是否上传模板图片
	 */
	public boolean isUploadPhoto() {
		return uploadPhoto;
	}
	/**
	 * 设置是否上传模板图片(在抓拍数据上传时)
	 * 
	 * @param uploadPhoto 是否上传模板图片
	 */
	public void setUploadPhoto(boolean uploadPhoto) {
		this.uploadPhoto = uploadPhoto;
	}
	/**
	 * 获取是否上传特征值数据(在抓拍数据上传时)
	 * 
	 * @return 是否上传特征值数据
	 */
	public boolean isUploadFeature() {
		return uploadFeature;
	}
	/**
	 * 设置是否上传特征值数据(在抓拍数据上传时)
	 * 
	 * @param uploadFeature 是否上传特征值数据
	 */
	public void setUploadFeature(boolean uploadFeature) {
		this.uploadFeature = uploadFeature;
	}
	/**
	 * 获取是否主动向服务器注册开关
	 * 
	 * @return true表示设备会在连接上服务器之后立即发起注册请求（即鉴权） 
	 */
	public boolean isDoReg() {
		return doReg;
	}
	/**
	 * 设置是否主动向服务器注册开关
	 * 
	 * @param doReg true表示设备会在连接上服务器之后立即发起注册请求（即鉴权） 
	 */
	public void setDoReg(boolean doReg) {
		this.doReg = doReg;
	}
	/**
	 * 获取设备注册用户名
	 * 
	 * @return 设备登录名
	 */
	public String getRegUserName() {
		return regUserName;
	}
	/**
	 * 设置设备注册用户名
	 * 
	 * @param regUserName 设备登录名
	 */
	public void setRegUserName(String regUserName) {
		this.regUserName = regUserName;
	}
	/**
	 * 获取设备注册密码
	 * 
	 * @return 设备密码
	 */
	public String getRegPassword() {
		return regPassword;
	}
	/**
	 * 设置设备注册密码
	 * 
	 * @param regPassword 设备密码
	 */
	public void setRegPassword(String regPassword) {
		this.regPassword = regPassword;
	}
	
	public byte[] getResv1() {
		return resv1;
	}

	public void setResv1(byte[] resv1) {
		this.resv1 = resv1;
	}

	public byte[] getResv2() {
		return resv2;
	}

	public void setResv2(byte[] resv2) {
		this.resv2 = resv2;
	}

	public byte[] getResv3() {
		return resv3;
	}

	public void setResv3(byte[] resv3) {
		this.resv3 = resv3;
	}

	public byte[] getResv4() {
		return resv4;
	}

	public void setResv4(byte[] resv4) {
		this.resv4 = resv4;
	}

	public int getResv5() {
		return resv5;
	}

	public void setResv5(int resv5) {
		this.resv5 = resv5;
	}

	public short getResv6() {
		return resv6;
	}

	public void setResv6(short resv6) {
		this.resv6 = resv6;
	}

	@Override
	public String toJson() {
		try {
			return JSON.format(this, new JSON.Observer() {
				
				@Override
				public boolean intercept(Object bean, Field f, StringBuilder sb) throws IllegalArgumentException, IllegalAccessException {
					return !f.getName().startsWith("resv");
				}
			});
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return ((Object)this).toString();
	}
}
