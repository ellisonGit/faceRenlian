package main.java.com.ha.facecamera.configserver.pojo;

import java.awt.Rectangle;
import java.util.Date;

/**
 * 抓拍对比数据
 * 
 * @author 林星
 */
public class CaptureCompareData extends PojoAdapter {

	private long sequenceID;
	private String cameraID;
	private String addrID;
	private String addrName;
	private Date captureTime;
	private boolean isRealtime;
	private boolean isPersonMatched;
	private int matchScore;
	private String personID;
	private String personName;
	private int personRole;
	private byte[] environmentImageData;
	private Rectangle faceRegionInEnvironment;
	private byte[] featureImageData;
	private Rectangle faceRegionInFeature;
	private byte[] videoData;
	private Date videoStartTime;
	private Date videoEndTime;
	private byte sex;
	private byte age;
	private byte qValue;
	private byte safetyHatColor;
	private float[] featureData;
	private byte[] modelImageData;
	private String sn;
	private String idCardNo;
	private String idCardName;
	private String idCardBirth;
	private byte idCardSex;
	private String idCardNation;
	private String idCardAddress;
	private String idCardOrg;
	private String idCardStartDate;
	private String idCardExpireDate;

	/** 获取数据包序列号 */
	public long getSequenceID() {
		return sequenceID;
	}

	/** 设置数据包序列号 */
	public void setSequenceID(long sequenceID) {
		this.sequenceID = sequenceID;
	}

	/** 获取设备编号 */
	public String getCameraID() {
		return cameraID;
	}

	/** 设置设备编号 */
	public void setCameraID(String cameraID) {
		this.cameraID = cameraID;
	}

	/** 获取地点编号 */
	public String getAddrID() {
		return addrID;
	}

	/** 设置地点编号 */
	public void setAddrID(String addrID) {
		this.addrID = addrID;
	}

	/** 获取地点名称 */
	public String getAddrName() {
		return addrName;
	}

	/** 设置地点名称 */
	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	/** 获取抓拍时间 */
	public Date getCaptureTime() {
		return captureTime;
	}

	/** 设置抓拍时间 */
	public void setCaptureTime(Date captureTime) {
		this.captureTime = captureTime;
	}

	/** 是否实时抓拍数据 */
	public boolean isRealtime() {
		return isRealtime;
	}

	/** 设置实时抓拍数据 */
	public void setRealtime(boolean isRealtime) {
		this.isRealtime = isRealtime;
	}

	/** 是否匹配的设备库中的人员 */
	public boolean isPersonMatched() {
		return isPersonMatched;
	}

	/** 设置是否匹配成功 */
	public void setPersonMatched(boolean isPersonMatched) {
		this.isPersonMatched = isPersonMatched;
	}

	/** 获取匹配度1~100 */
	public int getMatchScore() {
		return matchScore;
	}

	/** 设置匹配度 */
	public void setMatchScore(int matchScore) {
		this.matchScore = matchScore;
	}

	/** 获取人员编号；如果未匹配到可能为null或者空值 */
	public String getPersonID() {
		return personID;
	}

	/** 设置人员编号 */
	public void setPersonID(String personID) {
		this.personID = personID;
	}

	/** 获取人员姓名；如果未匹配到可能为null或者空值 */
	public String getPersonName() {
		return personName;
	}

	/** 设置人员姓名 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}

	/** 获取人员角色 */
	public int getPersonRole() {
		return personRole;
	}

	/** 设置人员角色 */
	public void setPersonRole(int personRole) {
		this.personRole = personRole;
	}

	/** 获取环境图（大图）图片；可能为null表示前端未回传大图 */
	public byte[] getEnvironmentImageData() {
		return environmentImageData;
	}

	/** 设置环境图（大图）图片 */
	public void setEnvironmentImageData(byte[] environmentImageData) {
		this.environmentImageData = environmentImageData;
	}

	/** 获取人脸在环境图（大图）中的坐标 */
	public Rectangle getFaceRegionInEnvironment() {
		return faceRegionInEnvironment;
	}

	/** 设置人脸在环境图（大图）中的坐标 */
	public void setFaceRegionInEnvironment(Rectangle faceRegionInEnvironment) {
		this.faceRegionInEnvironment = faceRegionInEnvironment;
	}

	/** 获取特写图数据；可能为null表示未回传特写图 */
	public byte[] getFeatureImageData() {
		return featureImageData;
	}

	/** 设置特写图数据 */
	public void setFeatureImageData(byte[] featureImageData) {
		this.featureImageData = featureImageData;
	}

	/** 获取人脸在特写图中的区域 */
	public Rectangle getFaceRegionInFeature() {
		return faceRegionInFeature;
	}

	/** 设置人脸在特写图中的区域 */
	public void setFaceRegionInFeature(Rectangle faceRegionInFeature) {
		this.faceRegionInFeature = faceRegionInFeature;
	}

	/** 获取视频数据 */
	public byte[] getVideoData() {
		return videoData;
	}

	/** 设置视频数据 */
	public void setVideoData(byte[] videoData) {
		this.videoData = videoData;
	}

	/** 获取视频开始时间 */
	public Date getVideoStartTime() {
		return videoStartTime;
	}

	/** 设置视频开始时间 */
	public void setVideoStartTime(Date videoStartTime) {
		this.videoStartTime = videoStartTime;
	}

	/** 获取视频结束时间 */
	public Date getVideoEndTime() {
		return videoEndTime;
	}

	/** 设置视频结束时间 */
	public void setVideoEndTime(Date videoEndTime) {
		this.videoEndTime = videoEndTime;
	}
	
	/**
	 * 获取性别
	 * 
	 * @return 0: 无此信息 1：男 2：女
	 */
	public byte getSex() {
		return sex;
	}
	/**
	 * 设置性别
	 * 
	 * @param sex 0: 无此信息 1：男 2：女
	 */
	public void setSex(byte sex) {
		this.sex = sex;
	}
	/**
	 * 获取年龄
	 * 
	 * @return 0: 无此信息 其它值：年龄
	 */
	public byte getAge() {
		return age;
	}
	/**
	 * 设置年龄
	 * 
	 * @param age 0: 无此信息 其它值：年龄
	 */
	public void setAge(byte age) {
		this.age = age;
	}
	
	/**
	 * 获取人脸标准度
	 * <br>
	 * 0：无此信息；1~100：有效值；值越大则人脸越标准。
	 * 
	 * @return 人脸标准度
	 */
	public byte getqValue() {
		return qValue;
	}
	/**
	 * 设置人脸标准度
	 * 
	 * @param qValue 人脸标准度
	 */
	public void setqValue(byte qValue) {
		this.qValue = qValue;
	}
	
	/**
	 * 获取安全帽颜色<br>
	 * 0：未戴安全帽 1：蓝色安全帽 2：橙色安全帽 3：红色安全帽 4：白色安全帽 5：黄色安全帽
	 * 
	 * @return 安全帽颜色
	 */
	public byte getSafetyHatColor() {
		return safetyHatColor;
	}
	/**
	 * 设置安全帽颜色<br>
	 * 0：未戴安全帽 1：蓝色安全帽 2：橙色安全帽 3：红色安全帽 4：白色安全帽 5：黄色安全帽
	 * 
	 * @param safetyHatColor 安全帽颜色
	 */
	public void setSafetyHatColor(byte safetyHatColor) {
		this.safetyHatColor = safetyHatColor;
	}

	/** 获取（抓拍到）人脸特征值数据 */
	public float[] getFeatureData() {
		return featureData;
	}

	/** 设置（抓拍到）人脸特征值数据 */
	public void setFeatureData(float[] featureData) {
		this.featureData = featureData;
	}

	/** 获取匹配到的人脸模板图 */
	public byte[] getModelImageData() {
		return modelImageData;
	}

	/** 设置匹配到的人脸模板图 */
	public void setModelImageData(byte[] modelImageData) {
		this.modelImageData = modelImageData;
	}

	/** 设置抓拍数据设备序列号 */
	public String getSn() {
		return sn;
	}

	/** 获取抓拍数据设备序列号 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取身份证号
	 * 
	 * @return 身份证号
	 */
	public String getIdCardNo() {
		return idCardNo;
	}
	/**
	 * 设置身份证号
	 * 
	 * @param idCardNo 身份证号
	 */
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	/**
	 * 获取身份证姓名信息
	 * 
	 * @return 身份证姓名信息
	 */
	public String getIdCardName() {
		return idCardName;
	}
	/**
	 * 设置身份证姓名信息
	 * 
	 * @param idCardName 身份证姓名信息
	 */
	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}
	
	/**
	 * 获取身份证出生日期信息
	 * 
	 * @return 身份证出生日期信息
	 */
	public String getIdCardBirth() {
		return idCardBirth;
	}
	/**
	 * 设置身份证出生日期信息
	 * 
	 * @param idCardBirth 份证出生日期信息
	 */
	public void setIdCardBirth(String idCardBirth) {
		this.idCardBirth = idCardBirth;
	}

	/**
	 * 获取身份证性别信息
	 * 
	 * @return 1:男 2:女
	 */
	public byte getIdCardSex() {
		return idCardSex;
	}
	/**
	 * 设置身份证性别信息
	 * 
	 * @param idCardSex 1:男 2:女
	 */
	public void setIdCardSex(byte idCardSex) {
		this.idCardSex = idCardSex;
	}

	/**
	 * 获取身份证民族信息
	 * 
	 * @return 身份证民族信息
	 */
	public String getIdCardNation() {
		return idCardNation;
	}
	/**
	 * 设置身份证民族信息
	 * 
	 * @param idCardNation 身份证民族信息
	 */
	public void setIdCardNation(String idCardNation) {
		this.idCardNation = idCardNation;
	}

	/**
	 * 获取身份证住址信息
	 * 
	 * @return 身份证住址信息
	 */
	public String getIdCardAddress() {
		return idCardAddress;
	}
	/**
	 * 设置身份证住址信息
	 * 
	 * @param idCardAddress 身份证住址信息
	 */
	public void setIdCardAddress(String idCardAddress) {
		this.idCardAddress = idCardAddress;
	}

	/**
	 * 获取身份证签发机关信息
	 * 
	 * @return 身份证签发机关信息
	 */
	public String getIdCardOrg() {
		return idCardOrg;
	}
	/**
	 * 设置身份证签发机关信息
	 * 
	 * @param idCardOrg 身份证签发机关信息
	 */
	public void setIdCardOrg(String idCardOrg) {
		this.idCardOrg = idCardOrg;
	}

	/**
	 * 获取身份证有效期起始时间信息
	 * 
	 * @return 身份证有效期起始时间信息
	 */
	public String getIdCardStartDate() {
		return idCardStartDate;
	}
	/**
	 * 设置身份证有效期起始时间信息
	 * 
	 * @param idCardStartDate 身份证有效期起始时间信息
	 */
	public void setIdCardStartDate(String idCardStartDate) {
		this.idCardStartDate = idCardStartDate;
	}

	/**
	 * 获取身份证有效期截止时间信息
	 * 
	 * @return 身份证有效期截止时间信息
	 */
	public String getIdCardExpireDate() {
		return idCardExpireDate;
	}
	/**
	 * 设置身份证有效期截止时间信息
	 * 
	 * @param idCardExpireDate 身份证有效期截止时间信息
	 */
	public void setIdCardExpireDate(String idCardExpireDate) {
		this.idCardExpireDate = idCardExpireDate;
	}
}
