package main.java.com.ha.facecamera.configserver.pojo;

import java.util.Date;

/**
 * 特征数据批量查询条件
 * <br>
 * 除了分页条件外，还支持使用编号、姓名等精确或模糊匹配，参考{@link #getRestrictions()}
 * 
 * @author 林星
 * @see Restrictions
 */
public class ListFaceCriteria extends PojoAdapter {
	
	private int role;
	private int pageNo;
	private int pageSize;
	private boolean getFeatureData;
	private boolean getImageData;
	private boolean fuzzyMode;
	private Restrictions restrictions;
	
	private int conditionFlag;
	private String id;
	private String name;
	private long wiegandNo;
	private Date startDateBegin;
	private Date startDateEnd;
	private Date expireDateBegin;
	private Date expireDateEnd;
	
	public ListFaceCriteria() {
		role = -1;
		pageNo = 1;
		pageSize = 20;
		getFeatureData = false;
		getImageData = false;
		conditionFlag = 0;
		restrictions = new Restrictions();
	}
	
	/**
	 * 获取需要获取的人员角色
	 * 
	 * @return 人员角色 0：普通人员。 1：白名单人员。 2：黑名单人员。 -1：所有人员。
	 */
	public int getRole() {
		return role;
	}
	/**
	 * 设置需要获取的人员角色
	 * 
	 * @param role 人员角色 0：普通人员。 1：白名单人员。 2：黑名单人员。 -1：所有人员。
	 */
	public void setRole(int role) {
		this.role = role;
	}
	/**
	 * 获取页码
	 * 
	 * @return 页码，从1开始
	 */
	public int getPageNo() {
		return pageNo;
	}
	/**
	 * 设置页码
	 * 
	 * @param pageNo 页码，从1开始
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	/**
	 * 获取页大小
	 * 
	 * @return 页大小，需小于100
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * 设置页大小
	 * 
	 * @param pageSize 页大小，需小于100
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 获取是否回传特征数据
	 * 
	 * @return 是否回传特征数据
	 */
	public boolean isGetFeatureData() {
		return getFeatureData;
	}
	/**
	 * 设置是否回传特征数据
	 * 
	 * @param getFeatureData 回传特征数据
	 */
	public void setGetFeatureData(boolean getFeatureData) {
		this.getFeatureData = getFeatureData;
	}
	/**
	 * 获取是否回传特片数据
	 * 
	 * @return 是否回传特片数据
	 */
	public boolean isGetImageData() {
		return getImageData;
	}
	/**
	 * 设置是否回传图片数据
	 * 
	 * @param getImageData 是否回传图片数据
	 */
	public void setGetImageData(boolean getImageData) {
		this.getImageData = getImageData;
	}

	/**
	 * 获取是否模糊匹配（针对编号和姓名有效）
	 * 
	 * @return 是否模糊匹配
	 */
	public boolean isFuzzyMode() {
		return fuzzyMode;
	}
	
	/**
	 * 获取查询条件组合标志位
	 * 
	 * @return 查询条件组合标志位
	 */
	public int getConditionFlag() {
		return conditionFlag;
	}
	/**
	 * 获取要查询的人员编号
	 * 
	 * @return 待查人员编号
	 */
	public String getId() {
		return id;
	}
	/**
	 * 获取待查询的人员姓名
	 * 
	 * @return 待查人员姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 获取待查人员韦根卡号
	 * 
	 * @return 待查人员韦根卡号
	 */
	public long getWiegandNo() {
		return wiegandNo;
	}
	/**
	 * 获取有效期起始时间起点
	 * 
	 * @return 待查人员有效期起始时间起点
	 */
	public Date getStartDateBegin() {
		return startDateBegin;
	}
	/**
	 * 获取有效期起始时间终点
	 * 
	 * @return 待查人员有效期起始时间终点
	 */
	public Date getStartDateEnd() {
		return startDateEnd;
	}
	/**
	 * 获取有效期截止时间起点
	 * 
	 * @return 待查人员有效期截止时间起点
	 */
	public Date getExpireDateBegin() {
		return expireDateBegin;
	}
	/**
	 * 获取有效期截止时间起点
	 * 
	 * @return 待查人员有效期截止时间终点
	 */
	public Date getExpireDateEnd() {
		return expireDateEnd;
	}

	/**
	 * 获取条件构造器
	 * 
	 * @return 获取内置的查询条件构造器
	 */
	public Restrictions getRestrictions() {
		return restrictions;
	}

	/**
	 * 一个类似JPA的工具类，使用一系列函数构造查询条件
	 * <br>
	 * 注意，对姓名和编号的查询中是否模糊匹配是对两者都有效的；例如先调用{@link #idEq(String)}后调用{@link #nameLike(String)}，则id的匹配模式也变为了模糊匹配
	 * 
	 * @author 林星
	 * @see ListFaceCriteria#isFuzzyMode()
	 */
	public class Restrictions {
		/**
		 * 以编号查询人员
		 * 
		 * @param id 要查询的人员编号
		 * @return 分页查询对象
		 */
		public ListFaceCriteria idEq(String id) {
			ListFaceCriteria.this.id = id;
			ListFaceCriteria.this.conditionFlag |= 0x1;
			ListFaceCriteria.this.fuzzyMode = false;
			return ListFaceCriteria.this;
		}
		/**
		 * 以编号查询人员（模糊匹配）
		 * 
		 * @param id 要查询的人员编号
		 * @return 分页查询对象
		 */
		public ListFaceCriteria idLike(String id) {
			ListFaceCriteria.this.id = id;
			ListFaceCriteria.this.conditionFlag |= 0x1;
			ListFaceCriteria.this.fuzzyMode = true;
			return ListFaceCriteria.this;
		}
		/**
		 * 以姓名查询人员
		 * 
		 * @param name 人员姓名
		 * @return 分页查询对象
		 */
		public ListFaceCriteria nameEq(String name) {
			ListFaceCriteria.this.name = name;
			ListFaceCriteria.this.conditionFlag |= 0x2;
			ListFaceCriteria.this.fuzzyMode = false;
			return ListFaceCriteria.this;
		}
		/**
		 * 以姓名查询人员（模糊匹配）
		 * 
		 * @param name 人员姓名
		 * @return 分页查询对象
		 */
		public ListFaceCriteria nameLike(String name) {
			ListFaceCriteria.this.name = name;
			ListFaceCriteria.this.conditionFlag |= 0x2;
			ListFaceCriteria.this.fuzzyMode = true;
			return ListFaceCriteria.this;
		}
		/**
		 * 以韦根卡号查询人员
		 * 
		 * @param wiegandNo 人员韦根卡号
		 * @return 分页查询对象
		 */
		public ListFaceCriteria wiegandNoEq(long wiegandNo) {
			ListFaceCriteria.this.wiegandNo = wiegandNo;
			ListFaceCriteria.this.conditionFlag |= 0x4;
			return ListFaceCriteria.this;
		}
		/**
		 * 以有效期起始区间查询人员
		 * 
		 * @param startDateBegin 有效期开始时间起点
		 * @param startDateEnd 有效期开始时间终点
		 * @return 分页查询对象
		 */
		public ListFaceCriteria startDateBetween(Date startDateBegin, Date startDateEnd) {
			ListFaceCriteria.this.startDateBegin = startDateBegin;
			ListFaceCriteria.this.startDateEnd = startDateEnd;
			ListFaceCriteria.this.conditionFlag |= 0x10;
			return ListFaceCriteria.this;			
		}
		/**
		 * 以有效期截止区间查询人员
		 * 
		 * @param expireDateBegin 有效期截止时间起点
		 * @param expireDateEnd 有效期截止时间终点
		 * @return 分页查询对象
		 */
		public ListFaceCriteria expireDateBetween(Date expireDateBegin, Date expireDateEnd) {
			ListFaceCriteria.this.expireDateBegin = expireDateBegin;
			ListFaceCriteria.this.expireDateEnd = expireDateEnd;
			ListFaceCriteria.this.conditionFlag |= 0x8;
			return ListFaceCriteria.this;			
		}
	}
}
