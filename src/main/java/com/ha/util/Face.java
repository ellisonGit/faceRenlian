package main.java.com.ha.util;

import com.alibaba.fastjson.JSON;
import main.java.com.ha.facecamera.configserver.*;
import main.java.com.ha.service.EmployeeService;
import main.java.com.ha.service.TimeRecordsService;
import main.java.com.ha.service.impl.EmployeeServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Face {

	private static final Logger logger = LoggerFactory.getLogger(Face.class);

	public static void main(String[] args) throws IOException {
		testfance();
	}
	

	
	public static void testListFace(int port) throws IOException {

		final ConfigServer cs = new ConfigServer();

		ConfigServerConfig csc = new ConfigServerConfig();

	    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cs.onCameraConnected(sn->{
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					String zada= zadan.zha();
					if (zada=="0") {
						logger.info("key过期！");

					}else{
						String url =   MyConfig.comUrl+"/forward/getWhitecardtask";
						System.out.println("请求地址："+url);
						String result= httpPostJson.doPost(url,"");
						logger.info("查白名单表结果："+result);
						JSONArray json = JSONArray.fromObject(result);
						if (json.size()>0) {
							//2循环遍历这个数组
							for (int i = 0; i < json.size(); i++) {
								//3、把里面的对象转化为JSONObject
								JSONObject job = json.getJSONObject(i);
								String id = job.get("id").toString();//编号
								String empId = job.get("empId").toString();//员工 编号
								String empFname = job.get("empFname").toString();//姓名
								String shebeiNo = job.get("remark").toString();//设备号

								String clockId = job.get("clockId").toString();
								String cardId = job.get("cardId").toString();
								String cardSn = job.get("cardSn").toString();
								String cardtype = job.get("cardtype").toString();
								String cardtypecode = job.get("cardtypecode").toString();
								String areacode = job.get("areacode").toString();

								final String filePath = MyConfig.lujing +"/Photo/"+ empId + ".jpg";
								main.java.com.ha.facecamera.configserver.pojo.Face f = new main.java.com.ha.facecamera.configserver.pojo.Face();
								f.setId(empId);
								f.setName(empFname);
								f.setRole(1);
								f.setJpgFilePath(new String[]{filePath});
								String[] a = f.getJpgFilePath();
								boolean b = cs.getCameraOnlineState(sn);
								boolean c = cs.addFace(shebeiNo, f, 500);
								if (c == true) {
									JSONObject signMap = new JSONObject();
									signMap.put("empId", empId);
									signMap.put("empFname", empFname);
									signMap.put("clockId", clockId);
									signMap.put("cardId", cardId);
									signMap.put("cardSn", cardSn);

									signMap.put("cardtype", cardtype);
									signMap.put("cardtypecode", cardtypecode);
									signMap.put("areacode", areacode);
									String param = JSON.toJSONString(signMap);

									//查询是否存在考勤机设备
									String urls =   MyConfig.comUrl+"/forward/getIsexist";
									result= httpPostJson.doPost(urls,param);
									JSONArray json2 = JSONArray.fromObject(result);
									if (json2.size()==0) {
										url =   MyConfig.comUrl+"/forward/insertWhite";
										result= httpPostJson.doPost(url,param);
										if("success".equals(result)){
											logger.info(empId+"工号成功下发白名单！");
											JSONObject idMap = new JSONObject();
											idMap.put("id",id);
											idMap.put("cardtypecode",cardtypecode);
											idMap.put("areacode",areacode);
											idMap.put("cardId",cardId);
											String  urlUpdate =   MyConfig.comUrl+"/forward/updateWhite";
											param = JSON.toJSONString(idMap);
											String res= httpPostJson.doPost(urlUpdate,param);
											if("success".equals(res)){
												logger.info(id+"成功更新白名单信息！");
											}
										}
									}else{
										JSONObject idMap = new JSONObject();
										idMap.put("id",id);
										idMap.put("cardtypecode",cardtypecode);
										idMap.put("areacode",areacode);
										idMap.put("cardId",cardId);
										String  urlUpdate =   MyConfig.comUrl+"/forward/updateWhite";
										param = JSON.toJSONString(idMap);
										String res= httpPostJson.doPost(urlUpdate,param);
										if("success".equals(res)){
											logger.info(id+"成功更新白名单信息！");
										}
									}
								}
								System.out.println("人脸数据是否添加成功:" + c);
								System.out.println("设备是否在线：" + b);
							}
						}

						url =   MyConfig.comUrl+"/forward/getWhitecardtaskDel";
						result= httpPostJson.doPost(url,"");
						json = JSONArray.fromObject(result);
						if (json.size()>0) {
							//循环遍历这个数组
							for (int i = 0; i < json.size(); i++) {
								//3、把里面的对象转化为JSONObject
								JSONObject job = json.getJSONObject(i);
								String empId = job.get("empId").toString();//员工 编号
								String clockId = job.get("clockId").toString();//一卡通设备编号
								String id = job.get("id").toString();//编号
								String cardtypecode = job.get("cardtypecode").toString();
								String areacode = job.get("areacode").toString();
								String cardId = job.get("cardId").toString();
								String shebeiNo = job.get("remark").toString();//设备号
								boolean d = cs.deleteFaceBySn(shebeiNo,empId , 500);//删除这个员工的人脸机的图片
								if (d == true) {
									JSONObject idMap = new JSONObject();
									idMap.put("id",id);
									idMap.put("cardtypecode",cardtypecode);
									idMap.put("areacode",areacode);
									idMap.put("cardId",cardId);
									idMap.put("clockId",clockId);
									idMap.put("empId",empId);
									//删除白名单
									String  urlDel =   MyConfig.comUrl+"/forward/delClockWhite";
									String paramDel = JSON.toJSONString(idMap);
									String re= httpPostJson.doPost(urlDel,paramDel);
									if("success".equals(re)){
										logger.info(id+"成功删除白名单信息！");
										String  urlUpdate =   MyConfig.comUrl+"/forward/updateWhite";
										String param = JSON.toJSONString(idMap);
										String res= httpPostJson.doPost(urlUpdate,param);
										if("success".equals(res)){
											logger.info(id+"成功更新删除白名单信息！");
										}
									}
								}
							}
						}
					}
					}

			});
		}).start(port, csc);

		Face.facePic();//抓拍
		System.in.read();
	}




	public static void testfance() {

		DataServer dataServer = new DataServer();
		DataServerConfig dsc = new DataServerConfig();
		dsc.connectStateInvokeCondition = ConnectStateInvokeCondition.DeviceNoKnown;
		boolean ret = dataServer.start(10002, dsc);
		if (ret) {
			logger.info("启动数据服务器成功！");

			dataServer.onCameraConnected((val) -> {
				try {
					EventQueue.invokeAndWait(() -> {
						logger.info("设备已连接-数据端口 ");
						logger.info(val);

					});
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			dataServer.onCameraDisconnected((val) -> {
				try {
					EventQueue.invokeAndWait(() -> {
						logger.info("设备已断开-数据端口 ");
						logger.info(val);

					});
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			ExecutorService cachedThreadPool;
			cachedThreadPool = Executors.newCachedThreadPool();
			dataServer.onCaptureCompareDataReceived((data) -> {
				try {
					EventQueue.invokeAndWait(() -> {

						logger.info("收到抓拍对比数据\n\t");
						logger.info(data.toJson());
						String emp_id=data.getPersonID();
						Date signTime=data.getCaptureTime();
						String  sn=data.getCameraID();

						if(emp_id!=null){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String requestTimestamp=sdf.format(signTime);//请求时间
							System.out.println("时间："+requestTimestamp);
							System.out.println("员工编号："+emp_id);
							logger.info("设备号：",sn);
							JSONObject signMap = new JSONObject();
							signMap.put("emp_id",emp_id);
							signMap.put("Timestamp",requestTimestamp);
							String  jsonString= JSON.toJSONString(signMap);
							String url = MyConfig.comUrl+"/forward/insertTimeRecords";
							String result= httpPostJson.doPost(url,jsonString);
							logger.info("成功："+result);
							SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
							String requestTimes=sd.format(signTime);//请求时间
							byte[] resByte =data.getFeatureImageData();
							try {
								ByteToFile(resByte,requestTimes,emp_id,requestTimestamp,sn);//保存人脸图片地址打卡记录
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

					});
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
		}
	}
//将获取Bytes转图片
	static void ByteToFile(byte[] bytes,String DateTime,String emp_id,String requestTimestamp,String sn)throws Exception{
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		BufferedImage bi1 = ImageIO.read(bais);
		try {
			String year = DateTime.substring(0, 4);//截取年份
			String month = DateTime.substring(4, 6);//截取月份
            String day = DateTime.substring(6, 8);//截取月份
			//创建目录
			String dirName =MyConfig.lujing+"/Pic/"+year+"/"+month+"/"+day+"/"+sn+"";//设定图片存放路径
			createDir(dirName);
			File w2 = new File(MyConfig.lujing+"/Pic/"+year+"/"+month+"/"+day+"/"+sn+"/"+DateTime+"_"+emp_id+".jpg");//可以是jpg,png,gif格式
            String pic_local=MyConfig.lujing+"/Pic/"+year+"/"+month+"/"+day+"/"+sn+"/"+DateTime.trim()+"_"+emp_id+".jpg";//图片存放路径
			//logger.info(pic_local);
			//System.out.println("时间："+DateTime);
			//System.out.println("员工编号："+emp_id);
			JSONObject signMap = new JSONObject();
			signMap.put("emp_id",emp_id);
			signMap.put("Timestamp",requestTimestamp);
			signMap.put("pic_local",pic_local);
			String  jsonString= JSON.toJSONString(signMap);
			String url =  MyConfig.comUrl+"/forward/insertTimeRecordsPic";
			String result= httpPostJson.doPost(url,jsonString);
			//System.out.println("成功："+result);
			ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			bais.close();
		}
	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			//logger.warn("创建目录" + destDirName + "失败，目标目录已经存在");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
//创建目录
		if (dir.mkdirs()) {
			logger.warn("创建目录" + destDirName + "成功！");
			return true;
		} else {
			logger.warn("创建目录" + destDirName + "失败！");
			return false;
		}
	}


	// 人脸抓拍
	public static void facePic() throws IOException {
		DataServer dataServer = new DataServer();
		DataServerConfig dsc = new DataServerConfig();
		dsc.connectStateInvokeCondition = ConnectStateInvokeCondition.DeviceNoKnown;
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
						logger.info("启动数据服务器成功！");
						dataServer.onCameraConnected((val) -> {
							try {
								EventQueue.invokeAndWait(() -> {
									logger.info("设备已连接-数据端口 ");
									logger.info(val);
								});
							} catch (InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						});
						dataServer.onCameraDisconnected((val) -> {
							try {
								EventQueue.invokeAndWait(() -> {
									logger.info("设备已断开-数据端口 ");
									logger.info(val);

								});
							} catch (InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						});
						dataServer.onCaptureCompareDataReceived((data) -> {
							try {
								EventQueue.invokeAndWait(() -> {
									String zada = zadan.zha();
									if (zada == "0") {
										logger.info("key过期！");

									} else{
										logger.info("收到抓拍对比数据\n\t");
									//logger.info(data.toJson());
									String emp_id = data.getPersonID();
									Date signTime = data.getCaptureTime();
									String sn = data.getCameraID();
									if (emp_id != null) {
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										String requestTimestamp = sdf.format(signTime);//请求时间
										logger.info("抓拍员工编号：" + emp_id);
										//logger.info("设备号：",sn);
										JSONObject signMap = new JSONObject();
										signMap.put("emp_id", emp_id);
										signMap.put("Timestamp", requestTimestamp);
										String jsonString = JSON.toJSONString(signMap);
										String url = MyConfig.comUrl + "/forward/insertTimeRecords";
										String result = httpPostJson.doPost(url, jsonString);
										logger.info("成功>>>>>>>：" + result);
										SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
										String requestTimes = sd.format(signTime);//请求时间
										byte[] resByte = data.getFeatureImageData();
										try {
											ByteToFile(resByte, requestTimes, emp_id, requestTimestamp, sn);//保存人脸图片地址打卡记录
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
								}
								});

							} catch (InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						});
					}
			//	}

			});
		dataServer.start(10002,dsc);
		System.in.read();
	}
}
