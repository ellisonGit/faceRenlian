package main.java.com.ha.facecamera.configserver.pojo;

public class FacePage extends PojoAdapter {

	private int total;
	private ListFaceCriteria criteria;
	private Face[] faces;

	/**
	 * 获取总共数量
	 * 
	 * @return 总数
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置总数
	 * 
	 * @param total
	 *            总数
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 获取当前分页数据对应的查询条件
	 * 
	 * @return 关联的查询条件
	 */
	public ListFaceCriteria getCriteria() {
		return criteria;
	}

	/**
	 * 设置当前分页数据对应的查询条件
	 * 
	 * @param criteria
	 *            关联的查询条件
	 */
	public void setCriteria(ListFaceCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * 获取特征值数组
	 * 
	 * @return 当前页特征值数组
	 */
	public Face[] getFaces() {
		return faces;
	}

	/**
	 * 设置当前页特征值数组
	 * 
	 * @param faces
	 *            特征值数组
	 */
	public void setFaces(Face[] faces) {
		this.faces = faces;
	}

}
