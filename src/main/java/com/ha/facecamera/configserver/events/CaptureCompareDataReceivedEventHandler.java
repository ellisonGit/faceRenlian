package main.java.com.ha.facecamera.configserver.events;

import main.java.com.ha.facecamera.configserver.pojo.CaptureCompareData;

/**
 * 收到设备抓拍对比数据事件
 * 
 * @author 林星
 *
 */
@FunctionalInterface
public interface CaptureCompareDataReceivedEventHandler {

	/**
	 * 收到设备抓拍对比数据事件处理函数
	 * 
	 * @param data
	 *            抓拍对比数据
	 */
	void onCaptureCompareDataReceived(CaptureCompareData data);
}
