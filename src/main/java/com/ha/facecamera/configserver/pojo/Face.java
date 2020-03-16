package main.java.com.ha.facecamera.configserver.pojo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.sdk.util.IDGenerater;
import main.java.com.ha.sdk.util.JSON;

public class Face extends main.java.com.ha.facecamera.configserver.pojo.PojoAdapter implements Serializable {

	private static final long serialVersionUID = 7539019337709411150L;
	
	private String id;
	private String name;
	private int role;
	private long wiegandNo;
	private Date startDate = Constants.DISABLED;
	private Date expireDate = Constants.LONGLIVE;
	private short featureCount;
	private short featureSize;
	private String[] jpgFilePath;
	private byte[][] jpgFileContent;
	private float[][] featureData;
	private byte[][] thumbImageData;
	private byte[][] twistImageData;

	/**
	 * 获取人员编号
	 * 
	 * @return 编号
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置人员编号
	 * <br>
	 * 如果需要从UUID生成短号，请参考{@link IDGenerater#extractUUID(String)}
	 * 
	 * @param id 编号
	 * @see IDGenerater#extractUUID(String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取人员姓名
	 * 
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置人员姓名
	 * 
	 * @param name 姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取人员角色
	 * 
	 * @return 角色
	 */
	public int getRole() {
		return role;
	}
	/**
	 * 设置人员角色
	 * 
	 * @param role 角色
	 */
	public void setRole(int role) {
		this.role = role;
	}
	
	/**
	 * 获取人员韦根卡号
	 * 
	 * @return 韦根卡号，其实类型是unsigned int
	 */
	public long getWiegandNo() {
		return wiegandNo;
	}
	/**
	 * 设置人员韦根卡号
	 * 
	 * @param wiegandNo 韦根卡号，其实类型是unsigned int
	 */
	public void setWiegandNo(long wiegandNo) {
		this.wiegandNo = wiegandNo;
	}
	
	/**
	 * 获取生效(起始)时间
	 * 
	 * @return 生效时间
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * 设置生效(起始)时间
	 * 
	 * @param startDate 生效时间
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * 获取过期(截止)时间
	 * 
	 * @return 过期时间
	 */
	public Date getExpireDate() {
		return expireDate;
	}
	/**
	 * 设置过期(截止)时间
	 * <br>
	 * 可以使用{@link Constants#LONGLIVE}和{@link Constants#DISABLED}造成特殊效果
	 * 
	 * @param expireDate 过期时间
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	/**
	 * 获取特征值数量
	 * 
	 * @return 特征值数量
	 */
	public short getFeatureCount() {
		return featureCount;
	}
	/**
	 * 设置特征值数量
	 * 
	 * @param featureCount 特征值数量
	 */
	public void setFeatureCount(short featureCount) {
		this.featureCount = featureCount;
	}
	/**
	 * 获取每组特征值元素数量
	 * 
	 * @return 特征值组内元素个数
	 */
	public short getFeatureSize() {
		return featureSize;
	}
	/**
	 * 设置每组特征值元素数量
	 * 
	 * @param featureSize 特征值组内元素个数
	 */
	public void setFeatureSize(short featureSize) {
		this.featureSize = featureSize;
	}

	
	/**
	 * 获取图片路径
	 * 
	 * @return 图片路径
	 */
	public String[] getJpgFilePath() {
		return jpgFilePath;
	}
	/**
	 * 设置图片路径
	 * 
	 * @param jpgFilePath 图片路径
	 */
	public void setJpgFilePath(String[] jpgFilePath) {
		this.jpgFilePath = jpgFilePath;
	}
	
	/**
	 * 获取图片内容
	 * 
	 * @return 图片内容
	 */
	public byte[][] getJpgFileContent() {
		return jpgFileContent;
	}
	/**
	 * 设置图片内容
	 * 
	 * @param jpgFileContent 图片内容<br>必须是完整图片（通过Files.readAllBytes读出来的）<br>优先级比jpgfilepath高
	 */
	public void setJpgFileContent(byte[][] jpgFileContent) {
		this.jpgFileContent = jpgFileContent;
	}
	
	/**
	 * 获取特征值数据
	 * 
	 * @return 特征值数据
	 */
	public float[][] getFeatureData() {
		return featureData;
	}
	/**
	 * 设置特征值数据
	 * 
	 * @param featureData 特征值数据
	 */
	public void setFeatureData(float[][] featureData) {
		this.featureData = featureData;
	}
	/**
	 * 获取图像数据
	 * 
	 * @return 图像数据
	 */
	public byte[][] getThumbImageData() {
		return thumbImageData;
	}
	/**
	 * 设置图像数据
	 * 
	 * @param imageData 图像数据
	 */
	public void setThumbImageData(byte[][] imageData) {
		this.thumbImageData = imageData;
	}
		
	/**
	 * 获取归一化图数据
	 * 
	 * @return 归一化图数据
	 */
	public byte[][] getTwistImageData() {
		return twistImageData;
	}
	/**
	 * 设置归一化图数据
	 * 
	 * @param twistImageData 归一化图数据
	 */
	public void setTwistImageData(byte[][] twistImageData) {
		this.twistImageData = twistImageData;
	}

	@Override
	public String toJson() {
		try {
			return JSON.format(this, new JSON.Observer() {
				
				@Override
				public boolean intercept(Object bean, Field f, StringBuilder sb) throws IllegalArgumentException, IllegalAccessException {
					if(f.getName().equals("startDate") || f.getName().equals("expireDate")) {
						Date d = (Date) f.get(bean);
						if(d == null || (d != Constants.DISABLED && d != Constants.LONGLIVE)) return true;
						sb.append('"').append(f.getName()).append("\":");
						if(d == Constants.DISABLED)
							sb.append("DISABLED");
						else if(d == Constants.LONGLIVE)
							sb.append("LONGLIVE");
						sb.append(',');
						return false;
					}
					return true;
				}
			});
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return ((Object)this).toString();
	}
}
