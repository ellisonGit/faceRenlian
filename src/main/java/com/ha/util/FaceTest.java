package main.java.com.ha.util;

import com.alibaba.fastjson.JSON;
import main.java.com.ha.facecamera.configserver.*;
import main.java.com.ha.facecamera.configserver.pojo.Face;
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
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FaceTest {
	@Autowired
	private static TimeRecordsService timeRecordsService;
	@Autowired
	private static EmployeeServiceImpl employeeServiceImpl;
	@Autowired
	private static EmployeeService employeeService;
	private final static Logger logger = LoggerFactory.getLogger(FaceTest.class);

	public static void main(String[] args) throws IOException {
		//testDeletePersonById(10001, "LinXing", "456789");
		//testListFace(10001);
		testfance();
		//testDeletePersonBatch(10001, 3);
		//testGetAllPersonIDs(10004);
		//testDownloadFaceBatch(10001);
	}
	

	
	public static void testListFace(int port) throws IOException {

		final ConfigServer cs = new ConfigServer();

		ConfigServerConfig csc = new ConfigServerConfig();

	    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cs.onCameraConnected(sn->{
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					String url =   MyConfig.comUrl+"/forward/getimg";
					String result= httpPostJson.doPost(url,"");
					//System.out.println("成功："+result);
					JSONArray json = JSONArray.fromObject(result);
					if (json.size()>0) {
						//2循环遍历这个数组
						for (int i = 0; i < json.size(); i++) {
							//3、把里面的对象转化为JSONObject
							JSONObject job = json.getJSONObject(i);
							String empId = job.get("empId").toString();//编号
							String empFname = job.get("empFname").toString();//姓名
							final String filePath = System.getProperty("user.dir") + "/" + empId + ".jpg";
							Face f = new Face();
							f.setId(empId);
							f.setName(empFname);
							f.setRole(1);
							f.setJpgFilePath(new String[]{filePath});
							String[] a = f.getJpgFilePath();
							boolean b = cs.getCameraOnlineState(sn);
							boolean c = cs.addFace(sn, f, 500);
							if (c == true) {
								/*JSONObject signMap = new JSONObject();
								signMap.put("emp_id",empId);
								String  jsonString= JSON.toJSONString(signMap);*/
								url =  MyConfig.comUrl+"/forward/updateState";
								MyRequestUtil.sendGet(url,"empId="+empId);
							}

							System.out.println("人脸数据是否添加成功:" + c);

							System.out.println("设备是否在线：" + b);
						}
					}
					}


			});

		}).start(port, csc);


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
			String dirName ="D:/face/"+year+"/"+month+"/"+day+"/"+sn+"";//设定图片存放路径
			createDir(dirName);
			File w2 = new File("D:/face/"+year+"/"+month+"/"+day+"/"+sn+"/"+DateTime+"_"+emp_id+".jpg");//可以是jpg,png,gif格式
            String pic_local="D:/face/"+year+"/"+month+"/"+day+"/"+sn+"/"+DateTime.trim()+"_"+emp_id+".jpg";//图片存放路径
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
	public static boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if(file.exists()) {
			logger.warn("创建单个文件" + destFileName + "失败，目标文件已存在！");
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			logger.warn("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
			return false;
		}
//判断目标文件所在的目录是否存在
		if(!file.getParentFile().exists()) {
//如果目标文件所在的目录不存在，则创建父目录
			logger.warn("目标文件所在目录不存在，准备创建它！");
			if(!file.getParentFile().mkdirs()) {
				logger.warn("创建目标文件所在目录失败！");
				return false;
			}
		}

//创建目标文件
		try {
			if (file.createNewFile()) {
				logger.warn("创建单个文件" + destFileName + "成功！");
				return true;
			} else {
				logger.warn("创建单个文件" + destFileName + "失败！");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("创建单个文件" + destFileName + "失败！" + e.getMessage());
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


					//boolean ret = dataServer.start(10002, dsc);
					//if (ret) {
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
									logger.info("收到抓拍对比数据\n\t");
									logger.info(data.toJson());
									String emp_id=data.getPersonID();
									Date signTime=data.getCaptureTime();
									String  sn=data.getCameraID();
									if(emp_id!=null){
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										String requestTimestamp=sdf.format(signTime);//请求时间
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
			//	}

			});
		dataServer.start(10002,dsc);
		System.in.read();
	}
}
