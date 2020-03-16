import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import main.java.com.ha.facecamera.configserver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.ha.facecamera.configserver.pojo.Face;
import main.java.com.ha.sdk.HaCamera;
import main.java.com.ha.sdk.util.IDGenerater;

public class Tests {
	private final static Logger logger = LoggerFactory.getLogger("HaSdk");


	public static void main(String[] args) throws IOException {
		//testDeletePersonById(10001, "LinXing", "456789");
		testListFace(10001,0);
		//testDeletePersonBatch(10001, 3);
		//testGetAllPersonIDs(10004);
		//testDownloadFaceBatch(10001);


	}
	
	private static void testDeletePersonById(int port, String... ids) throws IOException {
		final ConfigServer cs = new ConfigServer();
		ConfigServerConfig csc = new ConfigServerConfig();
	    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cs.onCameraConnected(sn->{
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					for(String id : ids)
						if(cs.deleteFace(sn, id, 500))
							logger.debug("删除人员成功！{} {}", sn, id);
						else
							logger.error("删除人员失败 {} {} {}", new Object[] {sn, cs.getLastErrorCode(), cs.getLastErrorMsg()});
				}
			});
			
		}).start(port, csc);
		System.in.read();
	}
	
	public static void testListFace(int port,int i) throws IOException {

		final ConfigServer cs = new ConfigServer();
		ConfigServerConfig csc = new ConfigServerConfig();
	    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cs.onCameraConnected(sn->{
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					for (int i=0;i<2;i++){
						final String filePath ="D:/face/2019/12/04/012310-F71A0E-1769EE/20191204154329_2019120415420500721.jpg";
						Face f = new Face();
						f.setId("lihao"+i);
						f.setName("测试人员"+i);
						f.setRole(1);
						f.setJpgFilePath(new String[]{filePath});
						String[] a = f.getJpgFilePath();
						boolean b = cs.getCameraOnlineState(sn);
						boolean c = cs.addFace(sn, f, 1000);
						System.out.println("输出c:" + c);
						System.out.println("输出b："+b);
					}

						/*if (fp != null)
							logger.debug("查询人员返回列表 {} {}", sn, fp);*/
					}

			});

		}).start(port, csc);


		System.in.read();


	}
	
	private static void testDeletePersonBatch(int port, int role) throws IOException {
		final ConfigServer cs = new ConfigServer();
		ConfigServerConfig csc = new ConfigServerConfig();
	    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cs.onCameraConnected(sn->{
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					if(!cs.deleteFaceSync(sn, role, 5000)) {
						logger.error("删除人员失败 {} {} {}", new Object[] {sn, cs.getLastErrorCode(), cs.getLastErrorMsg()});
					}
				}
				
			});
		}).onFaceDeleteProgressInfoReceived((sn, t, c, id) -> logger.debug("sn为{}删除进度上报，总计{}条，当前{}条，当前删除的人员是{}", new Object[] {sn, t, c, id})).start(port, csc);
		System.in.read();
	}

	private static void testGetAllPersonIDs(int port) throws IOException {
		final ConfigServer cs = new ConfigServer();
		ConfigServerConfig csc = new ConfigServerConfig();
	    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cs.onCameraConnected(sn->{
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					String[] personIds = cs.getAllPersonID(sn, 10000);
					if(personIds == null) {
						logger.error("获取所有人员编号失败，sn{} 错误码{} 错误信息{}", new Object[] {sn, cs.getLastErrorCode(), cs.getLastErrorMsg()});
					} else if(personIds.length == 0) {
						logger.warn("sn为{}的设备没有人员", sn);
					} else {
						logger.debug("sn为{}的设备全部人员编号(第一个)为{}", sn, personIds[0]);
					}
				}
				
			});
		}).start(port, csc);
		System.in.read();
	}
	
	private static void testDownloadFaceBatch(int port) throws IOException {
		final ConfigServer cs = new ConfigServer();
		ConfigServerConfig csc = new ConfigServerConfig();
	    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cs.onCameraConnected(sn->{
			File dir = new File("C:\\Users\\林星\\Desktop\\batchDown");
			Stream.of(dir.listFiles())
			.parallel()
			.forEachOrdered(f -> {
				cachedThreadPool.execute(new Runnable() {

					@Override
					public void run() {
						Face face = new Face();
						face.setId(IDGenerater.generateShortID());
						face.setJpgFilePath(new String[] {f.getAbsolutePath()});
						face.setName(f.getName());
						Date d = Calendar.getInstance().getTime();
						logger.error(String.format("%tF %tT.%tL sn=>%s tid=>%d add face", d, d, d, sn, Thread.currentThread().getId()));
						cs.addFace(sn, face, 5000);
						d = Calendar.getInstance().getTime();
						logger.error(String.format("%tF %tT.%tL sn=>%s tid=>%d add face ret=>code:%d msg:%s", d, d, d, sn, Thread.currentThread().getId(), cs.getLastErrorCode(), cs.getLastErrorMsg()));
					}
					
				});
			});
		}).start(port, csc);
		System.in.read();
	}
	
	public static void main_1(String[] args) throws IOException {
		HaCamera.init();
		File dir = new File("C:\\Users\\林星\\Desktop\\授权失败");
		Stream.of(dir.listFiles())
		//.parallel()

		.forEachOrdered(f -> {
			System.out.println(f.getName());
			try {
				testNormalizeImage(f.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println();
		});
		//testNormalizeImage("C:\\Users\\林星\\Desktop\\1.jpg");
	}
	
	private static void testNormalizeImage(String filePath) throws IOException {
		byte[] imgContent = Files.readAllBytes(new File(filePath).toPath());
		Object[] rets = HaCamera.twistImage(imgContent);
		int ret = (int)rets[0];
		if(ret != 0) {
			switch(ret) {
			case -14:
				System.out.println("提取人脸特征失败，须保证图像中有且仅有一张人脸");
				break;
			case -38:
				System.out.println("归一化图像失败");
				break;
			case -39:
				System.out.println("提取特征失败");
				break;
			case -40:
				System.out.println("人脸尺寸太小，人脸轮廓必须大于96*96");
				break;
			case -41:
				System.out.println("人像质量太差不满足注册条件");
				break;
			case -46:
				System.out.println("图像中人脸数不为1");
				break;
			case -47:
				System.out.println("图像中人脸不完整");
				break;
			case -48:
				System.out.println("人脸俯仰角太大");
				break;
			case -49:
				System.out.println("人脸侧偏角太大");
				break;
			case -50:
				System.out.println("人脸不正");
				break;
			case -51:
				System.out.println("张嘴幅度过大");
				break;
			case -52:
				System.out.println("光照不均匀");
				break;
			}
			return;
		}
		Encoder encoder = Base64.getEncoder();
		System.out.printf("人脸归一化成功，人脸特写缩略图Base64=>%s 人脸特写归一化图Base64=>%s", encoder.encodeToString((byte[]) rets[1]), encoder.encodeToString((byte[]) rets[2]));
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
			dataServer.onCaptureCompareDataReceived((data) -> {
				try {
					EventQueue.invokeAndWait(() -> {
						logger.info("收到抓拍对比数据\n\t");
						logger.info(data.toJson());
						if(data.getPersonID()!=null){
							System.out.println("员工编号："+data.getPersonID());
						}
						if(data.getPersonID()!=null){
							System.out.println("打卡时间："+data.getCaptureTime());
						}


						/*textArea.append("\n");
						dataViewDialog.showCaptureData(data);*/
							/*try {
								Files.write(new File("C:\\Users\\肖何\\Desktop\\data.json").toPath(), data.toJson().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
								Files.write(new File("C:\\Users\\肖何\\Desktop\\data.json").toPath(), System.lineSeparator().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}*/
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
}
