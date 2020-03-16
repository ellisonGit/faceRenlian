package main.java.com.ha.facecamera.configserver.events;

/**
 * 人脸批量删除进度通知
 * 
 * @author 林星
 *
 */
@FunctionalInterface
public interface FaceDeleteProgressInfoEventHandler {

	/**
	 * 人脸批量删除进度消息通知处理函数
	 * 
	 * @param deviceID
	 * 		设备标识符
	 * @param delCount
	 * 		总共删除的条目数
	 * @param curDelNo
	 * 		当前删除记录索引
	 * @param faceId
	 * 		当前删除的人员编号
	 */
	void onFaceDeleteProgressInfoReceived(String deviceID, int delCount, int curDelNo, String faceId);
}
