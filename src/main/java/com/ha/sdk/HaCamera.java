package main.java.com.ha.sdk;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.Arrays;

import main.java.com.ha.sdk.inner.ComHaSdkLibrary;
import main.java.com.ha.sdk.inner.ComHaSdkLibrary.discover_ipscan_cb_t;
import main.java.com.ha.sdk.inner.ipscan_t;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

public final class HaCamera {

	static {
		try {
			int count;
			byte[] buf = new byte[1024];
			if (Platform.isWindows()) {
				String[] dlls = Platform.is64Bit()
						? new String[] { "avcodec-57", "avutil-55", "hi_h264dec_w64", "libgcc_s_seh-1",
								"libgfortran-3", "libHasdk", "libnet_cpp", "libopenblas", "libquadmath-0",
								"libRtspClient", "libwinpthread-1", "msvcp90", "msvcr100", "msvcr90", "opencv_core249",
								"opencv_highgui249", "opencv_imgproc249", "opencv_video249", "pthreadVC2",
								"swresample-2", "swscale-4", "turbojpeg", "vcomp90" }
						: new String[] { "avcodec-57", "avutil-55", "hi_h264dec_w", "ijl15", "libgcc_s_sjlj-1",
								"libgfortran-3", "libHasdk", "libnet_cpp", "libopenblas", "libquadmath-0",
								"libRtspClient", "msvcp90", "msvcr100", "msvcr90", "opencv_core249",
								"opencv_highgui249", "opencv_imgproc249", "opencv_video249", "pthreadVC2",
								"swresample-2", "swscale-4", "vcomp90" };
				File tmpDir = Files.createTempDirectory(null).toFile();
				String platformPrefix = Platform.RESOURCE_PREFIX;
				for (int i = 0; i < dlls.length; ++i) {
					String dllName = dlls[i] + ".dll";
					File fdll = new File(tmpDir, dllName);
					try (FileOutputStream fos = new FileOutputStream(fdll)) {
						try (InputStream is = HaCamera.class.getClassLoader()
								.getResourceAsStream(platformPrefix + "/" + dllName)) {
							while ((count = is.read(buf, 0, buf.length)) > 0) {
								fos.write(buf, 0, count);
							}
						}
					}
					fdll.deleteOnExit();
					NativeLibrary.addSearchPath(dlls[i], tmpDir.getAbsolutePath());
				}
				String[] manifests = { "Microsoft.VC90.CRT.manifest", "Microsoft.VC90.OpenMP.manifest", "libHasdk.pdb" };
				for (int i = 0; i < manifests.length; ++i) {
					File fmanifest = new File(tmpDir, manifests[i]);
					try (FileOutputStream fos = new FileOutputStream(fmanifest)) {
						try (InputStream is = HaCamera.class.getClassLoader()
								.getResourceAsStream(platformPrefix + "/" + manifests[i])) {
							while ((count = is.read(buf, 0, buf.length)) > 0) {
								fos.write(buf, 0, count);
							}
						}
					}
					fmanifest.deleteOnExit();
				}
				tmpDir.deleteOnExit();
			} else if (Platform.isLinux()) {
				File tmpDir = Files.createTempDirectory(null).toFile();
				String platformPrefix = Platform.RESOURCE_PREFIX;
				File fdll = new File(tmpDir, "liblibHasdk.so");
				try (FileOutputStream fos = new FileOutputStream(fdll)) {
					try (InputStream is = HaCamera.class.getClassLoader()
							.getResourceAsStream(platformPrefix + "/liblibHasdk.so")) {
						while ((count = is.read(buf, 0, buf.length)) > 0) {
							fos.write(buf, 0, count);
						}
					}
				}
				fdll.deleteOnExit();
				NativeLibrary.addSearchPath("libHasdk", tmpDir.getAbsolutePath());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static class discover_ipscan_cb_impl implements discover_ipscan_cb_t {

		@Override
		public void apply(ipscan_t ipscan, int usr_param) {
			if (g_discover_eventHandler != null) {
				ipscan.read();
				try {
					g_discover_eventHandler.onDeviceDiscovered(new String(ipscan.mac, "UTF-8"),
							new String(ipscan.ip, "UTF-8"), new String(ipscan.netmask, "UTF-8"),
							new String(ipscan.manufacturer, "UTF-8"), new String(ipscan.platform, "UTF-8"),
							new String(ipscan.system, "UTF-8"), new String(ipscan.version, "UTF-8"));
				} catch (UnsupportedEncodingException e) {

				}
			}
		}

	}

	private static ComHaSdkLibrary.discover_ipscan_cb_t g_discover_ipscan_cb = new discover_ipscan_cb_impl();
	private static DeviceDiscoveredEventHandler g_discover_eventHandler;

	/**
	 * 初始化设备连接环境 <br>
	 * 在进行任何对设备操作之前，必须先行调用此函数 <br>
	 * 只需调用一次 <br>
	 * 如果确定不再使用任何与设备相关的操作，可调用{@link #deInit()}以释放资源
	 * 
	 * @see #deInit()
	 */
	public static void init() {
		ComHaSdkLibrary.INSTANCE.HA_Init();
		ComHaSdkLibrary.INSTANCE.HA_SetCharEncode(ComHaSdkLibrary.CHAR_ENCODE.CHAR_ENCODE_UTF8);
		ComHaSdkLibrary.INSTANCE.HA_SetNotifyConnected(1);
		ComHaSdkLibrary.INSTANCE.HA_InitFaceModel((String) null);
		ComHaSdkLibrary.INSTANCE.HA_RegDiscoverIpscanCb(g_discover_ipscan_cb, 0);
	}

	/**
	 * 反初始化设备连接环境 <br>
	 * 确定完全不需再操作设备时方可调用此函数，在下次执行{@link #init()}之前无法再对设备操作进行
	 * 
	 * @see #init()
	 */
	public static void deInit() {
		ComHaSdkLibrary.INSTANCE.HA_ClearAllCallbacksEx();
		ComHaSdkLibrary.INSTANCE.HA_DeInit();
	}

	/**
	 * 设置设备搜索回调
	 * 
	 * @param discover_eventHandler
	 *            搜索到设备之后的事件处理函数
	 */
	public static void onDeviceDiscovered(DeviceDiscoveredEventHandler discover_eventHandler) {
		g_discover_eventHandler = discover_eventHandler;
	}

	/**
	 * 搜索设备 <br>
	 * 在当前网络环境（同一交换机）下搜索所有设备
	 */
	public static void discoverDevice() {
		ComHaSdkLibrary.INSTANCE.HA_DiscoverIpscan();
	}

	/**
	 * 通过设备硬件地址设置其网络参数
	 * 
	 * @param mac
	 *            设备硬件地址
	 * @param ip
	 *            要为设备分配的ip
	 * @param netmask
	 *            要为设备设置的默认网关掩码
	 * @param gateway
	 *            要为设备设置的默认网关
	 */
	public static void setNetInfoByMac(String mac, String ip, String netmask, String gateway) {
		ComHaSdkLibrary.INSTANCE.HA_SetIpBymac(mac, ip, netmask, gateway);
	}

	/**
	 * 从给定图片中检测人脸并提取缩略图和归一化图
	 * 
	 * @param origin
	 *            要检测提取人脸的图片
	 * @return 一个对象数组<br>
	 *         数组的第一个元素是错误码，第二个元素是人脸的缩略图（宽高均小于130像素，包括人员头肩部分；是一个byte[]，内容是一张jpg图），数组的第三个元素是人脸的归一化图(是bgr分量的rawdata，不是图片格式)<br>
	 *         如果检测到人脸且人脸可用于注册，第一个元素为0<br>
	 *         如果未检测到人脸或人脸质量不够，则第一个元素不为0，且不存在第二三个元素
	 * @deprecated 速度很慢，不建议使用，请使用{@link #twistImage(byte[])}替代
	 * @see #twistImage(byte[])
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public static Object[] twistImage(BufferedImage origin) {
		byte[] bgr = getMatrixBGR(origin);
		byte[] twist_image = new byte[1024 * 1024];
        byte[] thumb_image = new byte[1024 * 1024];
        IntBuffer twist_size = IntBuffer.allocate(1);
        IntBuffer thumb_size = IntBuffer.allocate(1);
		IntBuffer feature_size = IntBuffer.allocate(1);
		IntBuffer faceJpgLen = IntBuffer.allocate(1);
		IntBuffer twist_w = IntBuffer.allocate(1);
		IntBuffer twist_h = IntBuffer.allocate(1);
        int _ret =  ComHaSdkLibrary.INSTANCE.HA_GetJpgFeatureImage(bgr, bgr.length, twist_image, twist_size, twist_w, twist_h, thumb_image, thumb_size);
        if (_ret != 0)
			return new Object[] { _ret };
        byte[] faceImg = new byte[thumb_size.get()];
        byte[] twistImg = new byte[twist_size.get()];
        System.arraycopy(thumb_image, 0, faceImg, 0, faceImg.length);
        System.arraycopy(twist_image, 0, twistImg, 0, twistImg.length);
		return new Object[] { 0, faceImg, twistImg };
	}

	/**
	 * 从给定图片数据中检测人脸并提取缩略图和归一化图
	 * 
	 * @param imageData
	 *            要检测提取人脸的图片数据（它是图片完整内容，通过Files.readAllBytes读出来的）
	 * @return 一个对象数组<br>
	 *         数组的第一个元素是错误码，第二个元素是人脸的缩略图（宽高均小于130像素，包括人员头肩部分；是一个byte[]，内容是一张jpg图），数组的第三个元素是人脸的归一化图(是bgr分量的rawdata，不是图片格式)<br>
	 *         如果检测到人脸且人脸可用于注册，第一个元素为0<br>
	 *         如果未检测到人脸或人脸质量不够，则第一个元素不为0，且不存在第二三个元素
	 */
	@SuppressWarnings("unused")
	public static Object[] twistImage(byte[] imageData) {
		byte[] twist_image = new byte[1024 * 1024];
        byte[] thumb_image = new byte[1024 * 1024];
        IntBuffer twist_size = IntBuffer.allocate(1);
        IntBuffer thumb_size = IntBuffer.allocate(1);
		IntBuffer feature_size = IntBuffer.allocate(1);
		IntBuffer faceJpgLen = IntBuffer.allocate(1);
		IntBuffer twist_w = IntBuffer.allocate(1);
		IntBuffer twist_h = IntBuffer.allocate(1);        
        int _ret =  ComHaSdkLibrary.INSTANCE.HA_GetJpgFeatureImage(imageData, imageData.length, twist_image, twist_size, twist_w, twist_h, thumb_image, thumb_size);
        if (_ret != 0)
			return new Object[] { _ret };
        byte[] faceImg = new byte[thumb_size.get()];
        byte[] twistImg = new byte[twist_size.get()];
        System.arraycopy(thumb_image, 0, faceImg, 0, faceImg.length);
        System.arraycopy(twist_image, 0, twistImg, 0, twistImg.length);
		return new Object[] { 0, faceImg, twistImg };
	}

	/**
	 * 计算两个特征值的相似度
	 * 
	 * @param feature_left
	 *            第一个特征值
	 * @param feature_right
	 *            第二个特征值
	 * @return 相似度，0~100
	 */
	public static int calcScore(float[] feature_left, float[] feature_right) {
		return ComHaSdkLibrary.INSTANCE.HA_GetMatchScores(feature_left, feature_right, 512);
	}
	
	/**
	 * 启用或关闭人脸有效性检测
	 * <br>
	 * 与设备无关，是整个服务端共享
	 * <br>
	 * 这个数据没有序列化到磁盘，每次进程启动都需要重新设置，支持热切换
	 * 
	 * @param enableFaceCheck true表示需要校验人脸 false表示不检验人脸有效性 默认为true
	 */
	public static void switchFaceCheckEnable(boolean enableFaceCheck) {
		ComHaSdkLibrary.INSTANCE.HA_SetFaceCheckEnable(enableFaceCheck?1:0);
	}
	
	/**
	 * 检测给定图片中人脸是否合格
	 * <br>
	 * 在实际使用中一般不直接返回成功失败，而是给客户以详细错误信息（使用{@link #twistImage(byte[])}来实现）其返回值第一个元素对应的错误信息如下
	 * <table border>
	 * <thead>
	 * <th>错误码</th>
	 * <th>错误描述</th>
	 * </thead>
	 * <tbody>
	 * <tr>
	 * <td>0</td>
	 * <td>成功，人脸合格</td>
	 * </tr>
	 * <tr>
	 * <td>-14</td>
	 * <td>提取人脸特征失败，须保证图像中有且仅有一张人脸</td>
	 * </tr>
	 * <tr>
	 * <td>-38</td>
	 * <td>归一化图像失败</td>
	 * </tr>
	 * <tr>
	 * <td>-39</td>
	 * <td>提取特征失败</td>
	 * </tr>
	 * <tr>
	 * <td>-40</td>
	 * <td>人脸尺寸太小，人脸轮廓必须大于96*96</td>
	 * </tr>
	 * <tr>
	 * <td>-41</td>
	 * <td>人像质量太差不满足注册条件</td>
	 * </tr>
	 * <tr>
	 * <td>-46</td>
	 * <td>图像中人脸数不为1</td>
	 * </tr>
	 * <tr>
	 * <td>-47</td>
	 * <td>图像中人脸不完整</td>
	 * </tr>
	 * <tr>
	 * <td>-48</td>
	 * <td>人脸俯仰角太大</td>
	 * </tr>
	 * <tr>
	 * <td>-49</td>
	 * <td>人脸侧偏角太大</td>
	 * </tr>
	 * <tr>
	 * <td>-50</td>
	 * <td>人脸不正</td>
	 * </tr>
	 * <tr>
	 * <td>-51</td>
	 * <td>张嘴幅度过大</td>
	 * </tr>
	 * <tr>
	 * <td>-52</td>
	 * <td>光照不均匀</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @param imageContent 图片数据，一般使用{@link Files#readAllBytes(java.nio.file.Path)}读出
	 * @return true表示人脸合格 false表示人脸不合格
	 */
	public static boolean validImage(byte[] imageContent) {
		return (int)twistImage(imageContent)[0] == 0;
	}

	private static boolean equalBandOffsetWith3Byte(BufferedImage image, int[] bandOffset) {
		if (image.getType() == BufferedImage.TYPE_3BYTE_BGR) {
			if (image.getData().getSampleModel() instanceof ComponentSampleModel) {
				ComponentSampleModel sampleModel = (ComponentSampleModel) image.getData().getSampleModel();
				if (Arrays.equals(sampleModel.getBandOffsets(), bandOffset)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isBGR3Byte(BufferedImage image) {
		return equalBandOffsetWith3Byte(image, new int[] { 0, 1, 2 });
	}

	private static byte[] getMatrixBGR(BufferedImage image) {
		if (null == image)
			throw new NullPointerException();
		byte[] matrixBGR;
		if (isBGR3Byte(image)) {
			matrixBGR = (byte[]) image.getData().getDataElements(0, 0, image.getWidth(), image.getHeight(), null);
		} else {
			// ARGB格式图像数据
			int intrgb[] = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			matrixBGR = new byte[image.getWidth() * image.getHeight() * 3];
			// ARGB转BGR格式
			for (int i = 0, j = 0; i < intrgb.length; ++i, j += 3) {
				matrixBGR[j] = (byte) (intrgb[i] & 0xff);
				matrixBGR[j + 1] = (byte) ((intrgb[i] >> 8) & 0xff);
				matrixBGR[j + 2] = (byte) ((intrgb[i] >> 16) & 0xff);
			}
		}
		return matrixBGR;
	}
}
