package main.java.com.ha.facecamera.configserver;

import static main.java.com.ha.facecamera.configserver.Constants.*;
import main.java.com.ha.facecamera.configserver.events.*;
import main.java.com.ha.facecamera.configserver.pkgs.*;
import main.java.com.ha.facecamera.configserver.pojo.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*import main.java.com.ha.facecamera.configserver.events.CameraAuthingEventHandler;
import main.java.com.ha.facecamera.configserver.events.CameraConnectedEventHandler;
import main.java.com.ha.facecamera.configserver.events.CameraDisconnectedEventHandler;
import main.java.com.ha.facecamera.configserver.events.FaceDeleteProgressInfoEventHandler;
import main.java.com.ha.facecamera.configserver.events.StreamDataReceivedEventHandler;
import main.java.com.ha.facecamera.configserver.pkgs.AddFaceRequest;
import main.java.com.ha.facecamera.configserver.pkgs.DeleteFaceRequest;
import main.java.com.ha.facecamera.configserver.pkgs.FaceDeleteProgressInfoRequest;
import main.java.com.ha.facecamera.configserver.pkgs.GetAllPersonIDResponse;
import main.java.com.ha.facecamera.configserver.pkgs.ListFaceRequest;
import main.java.com.ha.facecamera.configserver.pkgs.ListFaceResponse;
import main.java.com.ha.facecamera.configserver.pkgs.ModifyFaceRequest;
import main.java.com.ha.facecamera.configserver.pkgs.ReadAppConfigResponse;
import main.java.com.ha.facecamera.configserver.pkgs.ReadNetConfigResponse;
import main.java.com.ha.facecamera.configserver.pkgs.ReadTimeResponse;
import main.java.com.ha.facecamera.configserver.pkgs.ReadVersionResponse;
import main.java.com.ha.facecamera.configserver.pkgs.RegRequest;
import main.java.com.ha.facecamera.configserver.pkgs.SetAppConfigRequest;
import main.java.com.ha.facecamera.configserver.pkgs.SetTimeRequest;
import main.java.com.ha.facecamera.configserver.pkgs.StreamDataResponse;
import main.java.com.ha.facecamera.configserver.pkgs.ToggleStreamRequest;
import main.java.com.ha.facecamera.configserver.pojo.AppConfig;
import main.java.com.ha.facecamera.configserver.pojo.Face;
import main.java.com.ha.facecamera.configserver.pojo.FacePage;
import main.java.com.ha.facecamera.configserver.pojo.ListFaceCriteria;
import main.java.com.ha.facecamera.configserver.pojo.NetConfig;
import main.java.com.ha.facecamera.configserver.pojo.PojoAdapter;
import main.java.com.ha.facecamera.configserver.pojo.Time;
import main.java.com.ha.facecamera.configserver.pojo.Version;*/
import main.java.com.ha.sdk.HaCamera;
import main.java.com.ha.sdk.util.JSON;
import main.java.com.ha.tlv.DeSerializeAdapter;
import main.java.com.ha.tlv.DummyDeSerializable;
import main.java.com.ha.tlv.DummySerializable;
import main.java.com.ha.tlv.SerializeAdapter;
import main.java.com.ha.tlv.TLVCodecFactory;

/**
 * 配置服务端
 * 
 * @author 林星
 *
 */
public final class ConfigServer extends IoHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(ConfigServer.class);

	private static final String SESATTR_KEY_CAMERASN = "KEY_CAMERA_SN";
	private static final String SESATTR_KEY_DEVICENO = "KEY_DEVICE_NO";
	private static final String SESATTR_KEY_LASTRECV = "KEY_LASTRECV_TIME";

	private static final String SESATTR_KEY_VERSION = "KEY_INFO_VERSION";
	private static final String SESATTR_KEY_APPCONFIG = "KEY_INFO_APPCONFIG";
	private static final String SESATTR_KEY_NETCONFIG = "KEY_INFO_NETCONFIG";

	private static final String SESATTR_KEY_LOCK_TIME = "KEY_LOCKER_TIME";
	private static final String SESATTR_KEY_RET_TIME = "KEY_RET_TIME";
	private static final String SESATTR_KEY_LOCK_FACE = "KEY_LOCKER_FACE";
	private static final String SESATTR_KEY_RET_FACE = "KEY_RET_FACE";
	private static final String SESATTR_KEY_RET_FACE0 = "KEY_TMP_FACE";
	private static final String SESATTR_KEY_LOCK_CFG = "KEY_LOCKER_CFG";
	private static final String SESATTR_KEY_RET_CFG = "KEY_RET_CFG";

	/**
	 * IoSession的特性之一，服务器配置信息
	 */
	public static final String SESATTR_KEY_CFG = "KEY_CFG";

	// 内部属性定义
	private CameraConnectedEventHandler cameraConnectedEventHandler;
	private CameraDisconnectedEventHandler cameraDisconnectedEventHandler;
	private StreamDataReceivedEventHandler streamDataReceivedEventHandler;
	private CameraAuthingEventHandler cameraAuthingEventHandler;
	private FaceDeleteProgressInfoEventHandler faceDeleteProgressInfoEventHandler;

	private NioSocketAcceptor acceptor;
	private ConfigServerConfig config;
	private ConcurrentMap<String, IoSession> sn2Session;
	private ConcurrentMap<String, IoSession> deviceNo2Session;
	private ConcurrentHashSet<String> playedDeviceIDs;
	private ExecutorService cachedThreadPool;
	private ScheduledExecutorService scheduledThreadPool;

	private Thread heartBeatSendThread = new Thread() {

		@Override
		public void run() {
			while(true) {
				Set<IoSession> sess = sn2Session.values().stream().collect(Collectors.toSet());
				sess.addAll(deviceNo2Session.values());
				sess.parallelStream().forEachOrdered(ses -> {
					cachedThreadPool.execute(() -> {
						try {
							if(sendDummy(ses, MESSAGE_ID_HEARTBEAT) == Constants.SENDTIMEOUT) {
								logger.error("发送心跳失败，向设备发送指令超时 {}", ses.getRemoteAddress());
							} else
								logger.debug("发送心跳成功 {}", ses.getRemoteAddress());
						} catch (Throwable e) {
							//setLastError(0x01FF, e.getMessage());
							logger.debug("发送心跳失败 " + ses.getRemoteAddress(), e);
						}
					});
				});
				try {
					Thread.sleep(config.heartBeatInterval * 5 * 1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	};
	{
		heartBeatSendThread.setDaemon(true);
	}

	private static ThreadLocal<Integer> tlLastErrorCode = ThreadLocal.<Integer>withInitial(()->0);
	private static ThreadLocal<String> tlLastErrorMsg = ThreadLocal.<String>withInitial(()->Constants.NOERROR);
	private void setLastError(int code, String msg) {
		tlLastErrorCode.set(code);
		tlLastErrorMsg.set(msg);
	}
	/**
	 * 获取特定设备上次操作错误代码
	 * <br>
	 * 因为通过网络进行远程配置，可能遭遇未知错误，因此如果发现某次操作失败，则可以检查此值
	 *
	 * @return 错误码
	 */
	public int getLastErrorCode() {
		return tlLastErrorCode.get();
	}
	/**
	 * 获取特定设备上次操作错误信息描述
	 * <br>
	 * 传入null或者需要获取的设备不存在时则获取的是环境错误代码(<b>如系统启动与退出发生的错误</b>)
	 *
	 * @return 错误信息描述
	 */
	public String getLastErrorMsg() {
		return tlLastErrorMsg.get();
	}

	// 外部函数
	/**
	 * 注册相机连接事件回调
	 * @param _cameraConnectedEventHandler 相机连接事件处理函数
	 * @return 当前对象
	 */
	public ConfigServer onCameraConnected(CameraConnectedEventHandler _cameraConnectedEventHandler) {
		this.cameraConnectedEventHandler = _cameraConnectedEventHandler;
		return this;
	}
	/**
	 * 注册相机断开事件回调
	 * @param _cameraDisconnectedEventHandler 相机断开事件处理函数
	 * @return 当前对象
	 */
	public ConfigServer onCameraDisconnected(CameraDisconnectedEventHandler _cameraDisconnectedEventHandler) {
		this.cameraDisconnectedEventHandler = _cameraDisconnectedEventHandler;
		return this;
	}
	/**
	 * 注册视频数据接收事件回调
	 * @param _streamDataReceivedEventHandler 视频数据接收事件处理函数
	 * @return 当前对象
	 */
	public ConfigServer onStreamDataReceived(StreamDataReceivedEventHandler _streamDataReceivedEventHandler) {
		this.streamDataReceivedEventHandler = _streamDataReceivedEventHandler;
		return this;
	}
	/**
	 * 注册设备鉴权事件回调
	 * @param _cameraAuthingEventHandler 设备鉴权事件处理函数
	 * @return 当前对象
	 */
	public ConfigServer onAuthing(CameraAuthingEventHandler _cameraAuthingEventHandler) {
		this.cameraAuthingEventHandler = _cameraAuthingEventHandler;
		return this;
	}
	/**
	 * 人员删除进度通知事件回调
	 * @param _faceDeleteProgressInfoEventHandler 人员删除进度通知事件处理函数
	 * @return 当前对象
	 */
	public ConfigServer onFaceDeleteProgressInfoReceived(FaceDeleteProgressInfoEventHandler _faceDeleteProgressInfoEventHandler ) {
		this.faceDeleteProgressInfoEventHandler = _faceDeleteProgressInfoEventHandler;
		return this;
	}

	public ConfigServer() {
		acceptor = new NioSocketAcceptor();
		acceptor.setHandler(this);
		acceptor.getFilterChain().addLast("tlv", new ProtocolCodecFilter(new TLVCodecFactory()));
		sn2Session = new ConcurrentHashMap<>();
		deviceNo2Session = new ConcurrentHashMap<>();
		playedDeviceIDs = new ConcurrentHashSet<>();
	}

	/**
	 * 启动服务器监听
	 *
	 * @param port 要监听的端口号
	 *
	 * @return 是否成功
	 */
	public boolean start(int port, ConfigServerConfig _config) {
		this.config = _config;
		if(!config.noNativeMode)
			HaCamera.init();
		try {
			logger.info("正在尝试启动配置服务器，预备使用端口{} 启动参数{}", port, JSON.format(_config));
		} catch (IllegalArgumentException | IllegalAccessException e1) {
		}
		if(config.heartBeatInterval > 0) {
			// 判断非法连接
			acceptor.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, config.heartBeatInterval);
			// 接收空闲判断连接是否已断开了
			acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, config.heartBeatInterval);
		}
		try {
			acceptor.setReuseAddress(true);
			acceptor.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			setLastError(0x00ff, e.getMessage());
			logger.error("配置服务器启动失败", e);
			return false;
		}
		cachedThreadPool = Executors.newCachedThreadPool();
		scheduledThreadPool = Executors.newScheduledThreadPool(10); // FIXME 可以调优
		heartBeatSendThread.start();
		setLastError(0, i18nMessage("global.noError"));
		logger.info("配置服务器启动成功");
		return true;
	}

	/**
	 * 停止服务器监听
	 */
	public void stop() {
		heartBeatSendThread.interrupt();
		cachedThreadPool.shutdown();
		scheduledThreadPool.shutdown();
		acceptor.unbind();
		acceptor.dispose(true);
		if(!config.noNativeMode)
			HaCamera.deInit();
		logger.info("配置服务器停止成功");
	}

	/**
	 * 拉取视频数据
	 *
	 * @param deviceID 设备标识符(设备编号或序列号，通过配置参数不同)
	 * @return 是否播放完成（此值只作为向设备拉取流的请求发送成功，并不代表设备开始传递或已经收取到流的标识）
	 */
	public boolean startStream(String deviceID) {
		if(deviceNo2Session.containsKey(deviceID))
			return startStreamCore(deviceNo2Session.get(deviceID));
		else if(sn2Session.containsKey(deviceID))
			return startStreamCore(sn2Session.get(deviceID));
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("拉取视频数据失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 拉取视频数据
	 *
	 * @param sn 设备序列号
	 * @return 是否播放完成（此值只作为向设备拉取流的请求发送成功，并不代表设备开始传递或已经收取到流的标识）
	 */
	public boolean startStreamBySn(String sn) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn播放设备视频，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试播放sn为{}的视频失败，该设备未连接", sn);
			return false;
		}
		if(startStreamCore(session)) {
			playedDeviceIDs.add(sn);
			logger.info("尝试播放sn为{}的设备视频完成", sn);
			return true;
		}
		return false;
	}
	/**
	 * 拉取视频数据
	 *
	 * @param deviceNo 设备编号
	 * @return 是否播放完成（此值只作为向设备拉取流的请求发送成功，并不代表设备开始传递或已经收取到流的标识）
	 */
	public boolean startStreamByDeviceNo(String deviceNo) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo播放设备视频，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试播放deviceNo为{}的视频失败，该设备未连接", deviceNo);
			return false;
		}
		if(startStreamCore(session)) {
			playedDeviceIDs.add(deviceNo);
			logger.info("尝试播放deviceNo为{}的设备视频完成", deviceNo);
			return true;
		}
		return false;
	}
	private boolean startStreamCore(IoSession session) {
		try {
			if(sendPkg(session, new ToggleStreamRequest(true)) == Constants.SENDTIMEOUT) {
				setLastError(0x0AFE, i18nMessage("normal.sendTimeout"));
				logger.error("向设备发出播放视频指令失败，向设备发送指令超时 {}", session.getRemoteAddress());
			}
			else {
				logger.info("成功向设备发出播放视频指令，设备IP及端口为{}", session.getRemoteAddress());
				return true;
			}
		} catch (Exception e) {
			setLastError(0x0AFF, e.getMessage());
			logger.error("向设备发出播放视频指令失败，设备IP及端口为" + session.getRemoteAddress(), e);
		}
		return false;
	}

	/**
	 * 停止视频数据
	 *
	 * @param deviceID 设备标识符(设备编号或序列号，通过配置参数不同)
	 * @return 是否停止播放完成（此值只作为向设备停止拉取流的请求发送成功，并不代表设备立即停止推流）
	 */
	public boolean stopStream(String deviceID) {
		if(deviceNo2Session.containsKey(deviceID))
			return stopStreamCore(deviceNo2Session.get(deviceID));
		else if(sn2Session.containsKey(deviceID))
			return stopStreamCore(sn2Session.get(deviceID));
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("停止视频数据失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 停止视频数据
	 *
	 * @param sn 设备序列号
	 * @return 是否停止播放完成（此值只作为向设备停止拉取流的请求发送成功，并不代表设备立即停止推流）
	 */
	public boolean stopStreamBySn(String sn) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn停止播放设备视频，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试停止播放sn为{}的视频失败，该设备未连接", sn);
			return false;
		}
		if(stopStreamCore(session)) {
			playedDeviceIDs.remove(sn);
			logger.info("尝试停止播放sn为{}的设备视频完成", sn);
			return true;
		}
		return false;
	}
	/**
	 * 停止视频数据
	 *
	 * @param deviceNo 设备编号
	 * @return 是否停止播放完成（此值只作为向设备停止拉取流的请求发送成功，并不代表设备立即停止推流）
	 */
	public boolean stopStreamByDeviceNo(String deviceNo) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo停止播放设备视频，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试停止播放deviceNo为{}的视频失败，该设备未连接", deviceNo);
			return false;
		}
		if(stopStreamCore(session)) {
			playedDeviceIDs.remove(deviceNo);
			logger.info("尝试停止播放deviceNo为{}的设备视频完成", deviceNo);
			return true;
		}
		return false;
	}
	private boolean stopStreamCore(IoSession session) {
		try {
			if(sendPkg(session, new ToggleStreamRequest(false))  == Constants.SENDTIMEOUT) {
				setLastError(0x0BFE, i18nMessage("normal.sendTimeout"));
				logger.error("向设备发出停止播放视频指令失败，向设备发送指令超时 {}", session.getRemoteAddress());
			}
			else {
				logger.info("成功向设备发出停止播放视频指令，设备IP及端口为{}", session.getRemoteAddress());
				return true;
			}
		} catch (Exception e) {
			setLastError(0x0BFF, e.getMessage());
			logger.error("向设备发出停止播放视频指令失败，设备IP及端口为" + session.getRemoteAddress(), e);
		}
		return false;
	}

	/**
	 * 获取设备视频播放状态
	 *
	 * @param deviceID 设备标识符(设备编号或序列号，通过配置参数不同)
	 * @return 设备视频是否处于被拉取状态
	 */
	public boolean getIsStreamStart(String deviceID) {
		return playedDeviceIDs.contains(deviceID);
	}
	/**
	 * 获取设备视频播放状态
	 *
	 * @param sn 设备序列号
	 * @return 设备视频是否处于被拉取状态
	 */
	public boolean getIsStreamStartBySn(String sn) {
		return playedDeviceIDs.contains(sn);
	}
	/**
	 * 获取设备视频播放状态
	 *
	 * @param deviceNo 设备编号
	 * @return 设备视频是否处于被拉取状态
	 */
	public boolean getIsStreamStartByDeviceNo(String deviceNo) {
		return playedDeviceIDs.contains(deviceNo);
	}

	/**
	 * 获取设备在线状态
	 *
	 * @param deviceID 设备标识符
	 * @return 设备是否在线
	 */
	public boolean getCameraOnlineState(String deviceID) {
		return getCameraOnlineStateByDeviceNo(deviceID) || getCameraOnlineStateBySn(deviceID);
	}
	/**
	 * 判断相机是否在线
	 * <br>
	 * 通过设备序列号进行查找
	 *
	 * @param sn 设备序列号
	 * @return 相机是否在线
	 */
	public boolean getCameraOnlineStateBySn(String sn) {
		return sn2Session.containsKey(sn);
	}
	/**
	 * 判断相机是否在线
	 * <br>
	 * 通过设备编号进行查找
	 *
	 * @param deviceNo 设备编号
	 * @return 相机是否在线
	 */
	public boolean getCameraOnlineStateByDeviceNo(String deviceNo) {
		return deviceNo2Session.containsKey(deviceNo);
	}

	/**
	 * 断开指定设备
	 *
	 * @param deviceID 设备标识符
	 * @param waitInMilli 等待断开操作完成的超时<br>传递大于0的值表示要等待，反之则立即返回，但不管是否等待，都会尝试断开操作
	 * @return 在等待一定时间之后是否断开成功<br>此值只在等待时有效，返回false只表示在等待一段时间后断开操作还未完全完成，但不表示断开失败
	 */
	public boolean disconnectCamera(String deviceID, long waitInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return disconnectCameraCore(deviceNo2Session.get(deviceID), waitInMilli);
		else if(sn2Session.containsKey(deviceID))
			return disconnectCameraCore(sn2Session.get(deviceID), waitInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("断开设备失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 通过设备编号断开设备
	 *
	 * @param deviceNo 设备编号
	 * @param waitInMilli 等待断开操作完成的超时<br>传递大于0的值表示要等待，反之则立即返回，但不管是否等待，都会尝试断开操作
	 * @return 在等待一定时间之后是否断开成功<br>此值只在等待时有效，返回false只表示在等待一段时间后断开操作还未完全完成，但不表示断开失败
	 */
	public boolean disconnectCameraByDeviceNo(String deviceNo, long waitInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo断开设备，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试断开deviceNo为{}的设备，该设备未连接", deviceNo);
			return false;
		}
		return disconnectCameraCore(session, waitInMilli);
	}
	/**
	 * 通过设备序列号断开设备
	 *
	 * @param sn 设备序列号
	 * @param waitInMilli 等待断开操作完成的超时<br>传递大于0的值表示要等待，反之则立即返回，但不管是否等待，都会尝试断开操作
	 * @return 在等待一定时间之后是否断开成功<br>此值只在等待时有效，返回false只表示在等待一段时间后断开操作还未完全完成，但不表示断开失败
	 */
	public boolean disconnectCameraBySn(String sn, long waitInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn断开设备，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试断开sn为{}的设备，该设备未连接", sn);
			return false;
		}
		return disconnectCameraCore(session, waitInMilli);
	}
	private boolean disconnectCameraCore(IoSession session, long waitInMilli) {
		CloseFuture cf = session.closeOnFlush();
		if(waitInMilli > 0) {
			try {
				logger.info("断开设备，延迟{}毫秒", waitInMilli);
				return cf.await(waitInMilli);
			} catch (InterruptedException e) {
				setLastError(0x01FF, e.getMessage());
				logger.error("断开设备失败", e);
			}
			return false;
		}
		logger.info("断开设备完成，未设置超时时间");
		return true;
	}

	/**
	 * 获取客户端应用参数
	 *
	 * @param deviceID 设备标识符
	 * @return 客户端应用参数
	 */
	public AppConfig getAppConfig(String deviceID) {
		if(deviceNo2Session.containsKey(deviceID))
			return getAppConfigCore(deviceNo2Session.get(deviceID));
		else if(sn2Session.containsKey(deviceID))
			return getAppConfigCore(sn2Session.get(deviceID));
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取客户端应用参数失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return null;
		}
	}
	/**
	 * 通过Sn获取客户端应用参数
	 *
	 * @param sn 序列号
	 * @return 应用参数
	 */
	public AppConfig getAppConfigBySn(String sn) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn获取设备应用参数，但传入null");
			return null;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取sn为{}的应用参数失败，设备未连接", sn);
			return null;
		}
		return getAppConfigCore(session);
	}
	/**
	 * 通过设备编号获取客户端应用参数
	 *
	 * @param deviceNo 设备编号
	 * @return 应用参数
	 */
	public AppConfig getAppConfigByDeviceNo(String deviceNo) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo获取设备应用参数，但传入null");
			return null;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取deviceNo为{}的应用参数失败，设备未连接", deviceNo);
			return null;
		}
		return getAppConfigCore(session);
	}
	private AppConfig getAppConfigCore(IoSession session) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_CFG);
		synchronized (locker) {
			try {
				logger.info("获取参数成功{}，值为{}", session.getRemoteAddress(), session.getAttribute(SESATTR_KEY_APPCONFIG));
			} catch (IllegalArgumentException e) {
			}
			return (AppConfig) ((PojoAdapter)session.getAttribute(SESATTR_KEY_APPCONFIG)).clone();
		}
	}

	/**
	 * 获取客户端网络参数
	 *
	 * @param deviceID 设备标识符
	 * @return 客户端网络参数
	 */
	public NetConfig getNetConfig(String deviceID) {
		if(deviceNo2Session.containsKey(deviceID))
			return (NetConfig) Optional.ofNullable(deviceNo2Session.get(deviceID).getAttribute(SESATTR_KEY_NETCONFIG)).map(o -> ((PojoAdapter)o).clone()).orElse(null);
		else if(sn2Session.containsKey(deviceID))
			return (NetConfig) Optional.ofNullable(sn2Session.get(deviceID).getAttribute(SESATTR_KEY_NETCONFIG)).map(o -> ((PojoAdapter)o).clone()).orElse(null);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取客户端网络参数失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return null;
		}
	}
	/**
	 * 通过Sn获取客户端网络参数
	 *
	 * @param sn 序列号
	 * @return 网络参数
	 */
	public NetConfig getNetConfigBySn(String sn) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn获取网络参数，但传入null");
			return null;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试获取sn为{}的网络参数失败，该设备未连接", sn);
			return null;
		}
		if(!session.containsAttribute(SESATTR_KEY_NETCONFIG)) {
			setLastError(0x0102, i18nMessage("global.contentNotFound"));
			logger.error("尝试获取sn为{}的网络参数失败，未取得该设备网络参数", sn);
			return null;
		}
		try {
			logger.info("尝试获取sn为{}的网络参数成功，值为{}", sn, session.getAttribute(SESATTR_KEY_NETCONFIG));
		} catch (IllegalArgumentException e) {
		}
		return (NetConfig) ((PojoAdapter)session.getAttribute(SESATTR_KEY_NETCONFIG)).clone();
	}
	/**
	 * 通过设备编号获取客户端网络参数
	 *
	 * @param deviceNo 设备编号
	 * @return 网络参数
	 */
	public NetConfig getNetConfigByDeviceNo(String deviceNo) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo获取网络参数，但传入null");
			return null;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试获取deviceNo为{}的网络参数失败，该设备未连接", deviceNo);
			return null;
		}
		if(!session.containsAttribute(SESATTR_KEY_NETCONFIG)) {
			setLastError(0x0102, i18nMessage("global.contentNotFound"));
			logger.error("尝试获取deviceNo为{}的网络参数失败，未取得该设备网络参数", deviceNo);
			return null;
		}
		try {
			logger.info("尝试获取deviceNo为{}的网络参数成功，值为{}", deviceNo, session.getAttribute(SESATTR_KEY_NETCONFIG));
		} catch (IllegalArgumentException e) {
		}
		return (NetConfig) ((PojoAdapter)session.getAttribute(SESATTR_KEY_NETCONFIG)).clone();
	}

	/**
	 * 获取设备上次发送数据的时间
	 * <br>
	 * 即上次收到该设备数据的时间(包括心跳)
	 *
	 * @param deviceID 设备标识符
	 * @return 上次发送数据的时间
	 */
	public Date getLastDataRecvTime(String deviceID) {
		if(deviceNo2Session.containsKey(deviceID))
			return ((Calendar) deviceNo2Session.get(deviceID).getAttribute(SESATTR_KEY_LASTRECV)).getTime();
		else if(sn2Session.containsKey(deviceID))
			return ((Calendar) sn2Session.get(deviceID).getAttribute(SESATTR_KEY_LASTRECV)).getTime();
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取设备上次发送数据的时间失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return null;
		}
	}
	/**
	 * 通过设备序列号获取设备上次发送数据的时间
	 * <br>
	 * 即上次收到该设备数据的时间
	 *
	 * @param sn 设备序列号
	 * @return 上次发送数据的时间
	 */
	public Date getLastDataRecvTimeBySn(String sn) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn获取上次数据接收时间失败，但传入null");
			return null;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试获取sn为{}的上次数据接收时间失败，该设备未连接", sn);
			return null;
		}
		if(!session.containsAttribute(SESATTR_KEY_LASTRECV)) {
			setLastError(0x0102, i18nMessage("global.contentNotFound"));
			logger.error("尝试获取sn为{}的上次数据接收时间失败，未取得该设备上次数据接收时间", sn);
			return null;
		}
		try {
			logger.info("尝试获取sn为{}的上次数据接收时间成功，值为{}", sn, session.getAttribute(SESATTR_KEY_LASTRECV));
		} catch (IllegalArgumentException e) {
		}
		return ((Calendar) session.getAttribute(SESATTR_KEY_LASTRECV)).getTime();
	}
	/**
	 * 通过设备编号获取设备上次发送数据的时间
	 * <br>
	 * 即上次收到该设备数据的时间
	 *
	 * @param deviceNo 设备编号
	 * @return 上次发送数据的时间
	 */
	public Date getLastDataRecvTimeByDeviceNo(String deviceNo) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo获取上次数据接收时间失败，但传入null");
			return null;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试获取deviceNo为{}的上次数据接收时间失败，该设备未连接", deviceNo);
			return null;
		}
		if(!session.containsAttribute(SESATTR_KEY_LASTRECV)) {
			setLastError(0x0102, i18nMessage("global.contentNotFound"));
			logger.error("尝试获取deviceNo为{}的上次数据接收时间失败，未取得该设备上次数据接收时间", deviceNo);
			return null;
		}
		try {
			logger.info("尝试获取deviceNo为{}的上次数据接收时间成功，值为{}", deviceNo, session.getAttribute(SESATTR_KEY_LASTRECV));
		} catch (IllegalArgumentException e) {
		}
		return ((Calendar) session.getAttribute(SESATTR_KEY_LASTRECV)).getTime();
	}

	/**
	 * 获取版本信息
	 *
	 * @param deviceID 设备标识符
	 * @return 设备版本信息
	 */
	public Version getVersion(String deviceID) {
		if(deviceNo2Session.containsKey(deviceID))
			return (Version) ((PojoAdapter)deviceNo2Session.get(deviceID).getAttribute(SESATTR_KEY_VERSION)).clone();
		else if(sn2Session.containsKey(deviceID))
			return (Version) ((PojoAdapter)sn2Session.get(deviceID).getAttribute(SESATTR_KEY_VERSION)).clone();
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取版本信息失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return null;
		}
	}
	/**
	 * 通过设备序列号获取版本信息
	 *
	 * @param sn 设备序列号
	 * @return 设备版本信息
	 */
	public Version getVersionBySn(String sn) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn获取版本信息失败，但传入null");
			return null;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试获取sn为{}的版本信息失败，该设备未连接", sn);
			return null;
		}
		try {
			logger.info("尝试获取sn为{}的版本信息成功，值为{}", sn, session.getAttribute(SESATTR_KEY_VERSION));
		} catch (IllegalArgumentException e) {
		}
		return (Version) ((PojoAdapter)session.getAttribute(SESATTR_KEY_VERSION)).clone();
	}
	/**
	 * 通过设备编号获取版本信息
	 *
	 * @param deviceNo 设备编号
	 * @return 设备版本信息
	 */
	public Version getVersionByDeviceNo(String deviceNo) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo获取版本信息失败，但传入null");
			return null;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试获取deviceNo为{}的版本信息失败，该设备未连接", deviceNo);
			return null;
		}
		try {
			logger.info("尝试获取deviceNo为{}的版本信息成功，值为{}", deviceNo, session.getAttribute(SESATTR_KEY_VERSION));
		} catch (IllegalArgumentException e) {
		}
		return (Version) ((PojoAdapter)session.getAttribute(SESATTR_KEY_VERSION)).clone();
	}

	/**
	 * 获取时间
	 *
	 * @param deviceID 设备标识符
	 * @param timeoutInMilli 超时时间
	 * @return 设备当前时间
	 */
	public Time getTime(String deviceID, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return getTimeCore(deviceNo2Session.get(deviceID), timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return getTimeCore(sn2Session.get(deviceID), timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取时间失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return null;
		}
	}
	/**
	 * 通过设备序列号获取时间
	 *
	 * @param sn 设备序列号
	 * @param timeoutInMilli 超时时间
	 * @return 时间
	 */
	public Time getTimeBySn(String sn, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn获取设备时间，但传入null");
			return null;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取sn为{}的时间失败，设备未连接", sn);
			return null;
		}
		return getTimeCore(session, timeoutInMilli);
	}
	/**
	 * 通过设备编号获取时间
	 *
	 * @param deviceNo 设备编号
	 * @param timeoutInMilli 超时时间
	 * @return 时间
	 */
	public Time getTimeByDeviceNo(String deviceNo, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo获取设备时间，但传入null");
			return null;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取deviceNo为{}的时间失败，设备未连接", deviceNo);
			return null;
		}
		return getTimeCore(session, timeoutInMilli);
	}
	private Time getTimeCore(IoSession session, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_TIME);
		synchronized (locker) {
			try {
				if(sendDummy(session, MESSAGE_ID_TIME_GET) == Constants.SENDTIMEOUT) {
					logger.error("获取设备时间失败，向设备发送指令超时 {}", session.getRemoteAddress());
					setLastError(0x02FE, i18nMessage("normal.sendTimeout"));
					return null;
				}
				session.removeAttribute(SESATTR_KEY_RET_TIME);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_TIME)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_TIME);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0204, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("获取设备时间失败，解析设备返回数据封包出现错误 {}", da.getTag());
						}
						else {
							setLastError(0x0203, i18nMessage("ack." + da.getACKCode()));
							logger.warn("获取设备时间失败，设备返回数据封包中响应码为{}", da.getACKCode());
						}
						return null;
					}
					Time time = (Time) attrValue;
					setLastError(0, i18nMessage("global.noError"));
					logger.info("获取设备时间成功，设备时间为{}", time);
					return time;
				}
			} catch (Exception e) {
				setLastError(0x02ff, e.getMessage());
				logger.error("获取设备时间发生异常 " + session.getRemoteAddress(), e);
				return null;
			}
			setLastError(0x0202, i18nMessage("normal.timeout"));
			logger.error("获取设备时间失败，等待网络返回超时 {}", session.getRemoteAddress());
			return null;
		}
	}

	/**
	 * 设置时间
	 *
	 * @param deviceID 设备标识符
	 * @param timeToSet 需要设置的时间
	 * @param timeoutInMilli 超时时间
	 * @return 是否设置成功
	 */
	public boolean setTime(String deviceID, Time timeToSet, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return setTimeCore(deviceNo2Session.get(deviceID), timeToSet, timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return setTimeCore(sn2Session.get(deviceID), timeToSet, timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("设置时间失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 通过设备序列号设置时间
	 *
	 * @param sn 序列号
	 * @param time 需要设置的时间
	 * @param timeoutInMilli 超时时间
	 * @return 是否设置成功
	 */
	public boolean setTimeBySn(String sn, Time time, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn设置设备时间，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("设置sn为{}的时间失败，设备未连接", sn);
			return false;
		}
		return setTimeCore(session, time, timeoutInMilli);
	}
	/**
	 * 通过设备编号号设置时间
	 *
	 * @param deviceNo 设备编号
	 * @param time 需要设置的时间
	 * @param timeoutInMilli 超时时间
	 * @return 是否设置成功
	 */
	public boolean setTimeByDeviceNo(String deviceNo, Time time, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo设置设备时间，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("设置deviceNo为{}的时间失败，设备未连接", deviceNo);
			return false;
		}
		return setTimeCore(session, time, timeoutInMilli);
	}
	private boolean setTimeCore(IoSession session, Time time, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_TIME);
		synchronized (locker) {
			try {
				SetTimeRequest pktTime = new SetTimeRequest(time);
				String sendRet = sendPkg(session, pktTime);
				if(sendRet != Constants.NOERROR) {
					if(sendRet == Constants.SENDTIMEOUT) {
						setLastError(0x03FE, i18nMessage("normal.sendTimeout"));
						logger.error("设置设备时间失败，向设备发送指令超时{}-{}", session.getRemoteAddress(), i18nMessage("normal.sendTimeout"));
					} else {
						setLastError(0x0301, i18nMessage("normal.sendPkgParseError", sendRet));
						logger.error("设置设备时间失败，解析发送数据包出现错误{}-{}", session.getRemoteAddress(), sendRet);
					}
					return false;
				}
				session.removeAttribute(SESATTR_KEY_RET_TIME);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_TIME)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_TIME);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0304, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("设置设备时间失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
						}
						else {
							setLastError(0x0303, i18nMessage("ack." + da.getACKCode()));
							logger.warn("设置设备时间失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
						}
						return false;
					}
					logger.info("设置设备时间成功");
					setLastError(0, i18nMessage("global.noError"));
					return true;
				}
			} catch (Exception e) {
				setLastError(0x03ff, e.getMessage());
				logger.error("设置设备时间发生异常 " + session.getRemoteAddress(), e);
				return false;
			}
			setLastError(0x0302, i18nMessage("normal.timeout"));
			logger.error("设置设备时间失败，等待网络返回超时 {}", session.getRemoteAddress());
			return false;
		}
	}

	/**
	 * 分页查询人脸特征值数据
	 *
	 * @param deviceID 设备标识编号
	 * @param criteria 分页参数
	 * @param timeoutInMilli 超时时间
	 * @return 查询到的特征值数据数组
	 */
	public FacePage listFace(String deviceID, ListFaceCriteria criteria, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return listFaceCore(deviceNo2Session.get(deviceID), criteria, timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return listFaceCore(sn2Session.get(deviceID), criteria, timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("分页查询人脸特征值数据失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return null;
		}
	}
	/**
	 * 分页查询人脸特征值数据
	 *
	 * @param sn 设备序列号
	 * @param criteria 分页参数
	 * @param timeoutInMilli 超时时间
	 * @return 查询到的特征值数据数组
	 */
	public FacePage listFaceBySn(String sn, ListFaceCriteria criteria, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn分页查询人员失败，但传入null");
			return null;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("分页查询sn为{}的人员失败，设备未连接", sn);
			return null;
		}
		return listFaceCore(session, criteria, timeoutInMilli);
	}
	/**
	 * 分页查询人脸特征值数据
	 *
	 * @param deviceNo 设备编号
	 * @param criteria 分页参数
	 * @param timeoutInMilli 超时时间
	 * @return 查询到的特征值数据数组
	 */
	public FacePage listFaceByDeviceNo(String deviceNo, ListFaceCriteria criteria, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo分页查询人员失败，但传入null");
			return null;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("分页查询deviceNo为{}的人员失败，设备未连接", deviceNo);
			return null;
		}
		return listFaceCore(session, criteria, timeoutInMilli);
	}
	@SuppressWarnings("unchecked")
	private FacePage listFaceCore(IoSession session, ListFaceCriteria criteria, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_FACE);
		synchronized (locker) {
			try {
				ListFaceRequest pkg = new ListFaceRequest(criteria);
				String sendRet = sendPkg(session, pkg);
				if(sendRet != Constants.NOERROR) {
					if(sendRet == Constants.SENDTIMEOUT) {
						setLastError(0x04FE, i18nMessage("normal.sendTimeout"));
						logger.error("分页查询设备人员信息失败，向设备发送指令超时{}-{}", session.getRemoteAddress(), i18nMessage("normal.sendTimeout"));
					} else {
						setLastError(0x0401, i18nMessage("normal.sendPkgParseError", sendRet));
						logger.error("分页查询设备人员信息失败，解析发送封包失败{}-{}", session.getRemoteAddress(), sendRet);
					}
					return null;
				}
				session.setAttribute(SESATTR_KEY_RET_FACE, new ArrayList<Face>());
				session.removeAttribute(SESATTR_KEY_RET_FACE0);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_FACE0)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_FACE0);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0404, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("分页查询设备人员信息失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
						}
						else {
							setLastError(0x0403, i18nMessage("ack." + da.getACKCode()));
							logger.warn("分页查询设备人员信息失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
						}
						return null;
					}
					List<Face> faces = (List<Face>)session.getAttribute(SESATTR_KEY_RET_FACE);
					int total = ((Integer)attrValue).intValue();
					FacePage facePage = new FacePage();
					facePage.setCriteria(criteria);
					if(faces.size() < 1 || total < 1) {
						setLastError(0, i18nMessage("global.noError"));
						logger.info("分页查询设备人员信息完成，本页无数据，页码{} {}", criteria.getPageNo(), session.getRemoteAddress());
						return facePage;
					}
					facePage.setTotal(total);
					facePage.setFaces(faces.toArray(new Face[0]));
					setLastError(0, i18nMessage("global.noError"));
					logger.info("分页查询设备人员信息完成，页码{}页内条目数{}符合条件记录数{} {}", new Object[] {criteria.getPageNo(), faces.size(),  total, session.getRemoteAddress()});
					return facePage;
				}
			} catch (Exception e) {
				setLastError(0x04FF, e.getMessage());
				logger.error("分页查询设备人员发生异常 " + session.getRemoteAddress(), e);
				return null;
			}
			setLastError(0x0402, i18nMessage("normal.timeout"));
			logger.error("分页查询设备人员失败，等待网络返回超时 {}", session.getRemoteAddress());
			return null;
		}
	}

	/**
	 * 添加一个人脸到设备模板库中
	 *
	 * @param deviceID 设备标识
	 * @param face 人脸数据
	 * @param timeoutInMilli 超时时间
	 * @return 是否添加成功
	 */

	public boolean addFace(String deviceID, Face face, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return addFaceCore(deviceNo2Session.get(deviceID), face, timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return addFaceCore(sn2Session.get(deviceID), face, timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("添加一个人脸到设备模板库中失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 添加一个人脸到设备模板库中
	 *
	 * @param sn 设备序列号
	 * @param face 人脸数据
	 * @param timeoutInMilli 超时时间
	 * @return 是否添加成功
	 */
	public boolean addFaceBySn(String sn, Face face, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn注册人脸，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("向sn为{}的设备注册人脸失败，设备未连接", sn);
			return false;
		}
		return addFaceCore(session, face, timeoutInMilli);
	}
	/**
	 * 添加一个人脸到设备模板库中
	 *
	 * @param deviceNo 设备编号
	 * @param face 人脸数据
	 * @param timeoutInMilli 超时时间
	 * @return 是否添加成功
	 */
	public boolean addFaceByDeviceNo(String deviceNo, Face face, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo注册人脸，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("向deviceNo为{}的设备注册人脸失败，设备未连接", deviceNo);
			return false;
		}
		return addFaceCore(session, face, timeoutInMilli);
	}
	private boolean addFaceCore(IoSession session, Face face, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_FACE);
		synchronized (locker) {
			try {
				if((face.getJpgFileContent() == null || face.getJpgFileContent().length < 1) &&
						(face.getJpgFilePath() == null || face.getJpgFilePath().length < 1) &&
						(face.getTwistImageData() == null || face.getTwistImageData().length < 1 ||
								face.getThumbImageData() == null || face.getThumbImageData().length < 1 ||
								face.getTwistImageData().length != face.getThumbImageData().length)) {
					setLastError(0x0501, i18nMessage("face.jpgDataNotSet"));
					logger.error("添加人员信息失败，人员头像的图片数据为空 {}", session.getRemoteAddress());
					return false;
				}
				if(face.getTwistImageData() != null) {
					for(int i = 0; i < face.getTwistImageData().length; ++i) {
						if(face.getTwistImageData()[i] == null) {
							setLastError(0x0501, i18nMessage("face.twisImage_i_Null", i));
							logger.error("添加人员信息失败，索引为{}的人脸归一化图数据为空 {}", i, session.getRemoteAddress());
							return false;
						}
						if(face.getThumbImageData()[i] == null) {
							setLastError(0x0501, i18nMessage("face.thumbImage_i_Null", i));
							logger.error("添加人员信息失败，索引为{}的人脸缩略图数据为空 {}", i, session.getRemoteAddress());
							return false;
						}
					}
				}
				else if(face.getJpgFileContent() != null) {
					face.setTwistImageData(new byte[face.getJpgFileContent().length][]);
					face.setThumbImageData(new byte[face.getJpgFileContent().length][]);
					for(int i = 0; i < face.getJpgFileContent().length; ++i) {
						Object[] rets = config.noNativeMode?new Object[] {-17}:HaCamera.twistImage(face.getJpgFileContent()[i]);
						if(rets.length == 1) {
							face.setTwistImageData(null);
							face.setThumbImageData(null);
							setLastError(0x0501, i18nMessage("face.twis.fail", i, rets[0], i18nMessage("face.twis.error." + rets[0])));
							logger.error("添加人员信息失败，从图片内容检测人脸提取归一化图失败。错误码{} {}", rets[0], session.getRemoteAddress());
							return false;
						}
						face.getTwistImageData()[i] = (byte[])rets[2];
						face.getThumbImageData()[i] = (byte[])rets[1];
					}
				} else if(face.getJpgFilePath() != null) {
					face.setTwistImageData(new byte[face.getJpgFilePath().length][]);
					face.setThumbImageData(new byte[face.getJpgFilePath().length][]);
					for(int i = 0; i < face.getJpgFilePath().length; ++i) {
						try {
							//System.out.println("shuchu::"+face.getJpgFilePath()[i]);
							Object[] rets = config.noNativeMode?new Object[] {-17}:HaCamera.twistImage(Files.readAllBytes(new File(face.getJpgFilePath()[i]).toPath()));
							if(rets.length == 1) {
								System.out.println("shu::"+rets[0]);
								face.setTwistImageData(null);
								face.setThumbImageData(null);
								setLastError(0x0501, i18nMessage("face.twis.failPath", face.getJpgFilePath()[i], rets[0], i18nMessage("face.twis.error." + rets[0])));
								logger.error("添加人员信息失败，从图片路径检测人脸提取归一化图失败。路径{} 错误码{} {}", new Object[] {face.getJpgFilePath()[i], rets[0], session.getRemoteAddress()});
								return false;
							}
							face.getTwistImageData()[i] = (byte[])rets[2];
							face.getThumbImageData()[i] = (byte[])rets[1];
						} catch (IOException e) {
							setLastError(0x05FF, e.getMessage());
							logger.error("添加人员信息失败，从人脸图片路径转换人脸数据错误 " + session.getRemoteAddress(), e);
							return false;
						}
					}
				}
				AddFaceRequest pkg = new AddFaceRequest(face);
				String sendRet = sendPkg(session, pkg);
				if(sendRet != Constants.NOERROR) {
					if(sendRet == Constants.SENDTIMEOUT) {
						setLastError(0x05FE, i18nMessage("normal.sendTimeout"));
						logger.error("添加人员信息失败，向设备发送指令超时{}-{}", session.getRemoteAddress(), i18nMessage("normal.sendTimeout"));
					} else {
						setLastError(0x0501, i18nMessage("normal.sendPkgParseError", sendRet));
						logger.error("添加人员信息失败，解析发送封包失败{}-{}", session.getRemoteAddress(), sendRet);
					}
					return false;
				}
				session.removeAttribute(SESATTR_KEY_RET_FACE0);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_FACE0)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_FACE0);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0504, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("添加人员信息失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
						}
						else {
							setLastError(0x0503, i18nMessage("ack." + da.getACKCode()));
							logger.warn("添加人员信息失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
						}
						return false;
					}
					setLastError(0, i18nMessage("global.noError"));
					logger.info("添加人员信息成功 {}", session.getRemoteAddress());
					return true;
				}
			} catch (Exception e) {
				setLastError(0x05FF, e.getMessage());
				logger.error("添加人员信息发生异常 " + session.getRemoteAddress(), e);
				return false;
			}
			setLastError(0x0502, i18nMessage("normal.timeout"));
			logger.error("添加人员信息失败，等待网络返回超时 {}", session.getRemoteAddress());
			return false;
		}
	}

	/**
	 * 更新设备模板库中已有人脸
	 *
	 * @param deviceID 设备标识
	 * @param face 人脸数据
	 * @param timeoutInMilli 超时时间
	 * @return 是否更新成功
	 */
	public boolean modifyFace(String deviceID, Face face, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return modifyFaceCore(deviceNo2Session.get(deviceID), face, timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return modifyFaceCore(sn2Session.get(deviceID), face, timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("更新设备模板库中已有人脸失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 更新设备模板库中已有人脸
	 *
	 * @param sn 设备序列号
	 * @param face 人脸数据
	 * @param timeoutInMilli 超时时间
	 * @return 是否更新成功
	 */
	public boolean modifyFaceBySn(String sn, Face face, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn更新人员信息，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("更新sn为{}的人员信息，设备未连接", sn);
			return false;
		}
		return modifyFaceCore(session, face, timeoutInMilli);
	}
	/**
	 * 更新设备模板库中已有人脸
	 *
	 * @param deviceNo 设备编号
	 * @param face 人脸数据
	 * @param timeoutInMilli 超时时间
	 * @return 是否更新成功
	 */
	public boolean modifyFaceByDeviceNo(String deviceNo, Face face, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo更新人员信息，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("更新deviceNo为{}的人员信息，设备未连接", deviceNo);
			return false;
		}
		return modifyFaceCore(session, face, timeoutInMilli);
	}
	private boolean modifyFaceCore(IoSession session, Face face, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_FACE);
		synchronized (locker) {
			try {
				if((face.getJpgFileContent() == null || face.getJpgFileContent().length < 1) &&
						(face.getJpgFilePath() == null || face.getJpgFilePath().length < 1) &&
						(face.getTwistImageData() == null || face.getTwistImageData().length < 1 ||
								face.getThumbImageData() == null || face.getThumbImageData().length < 1 ||
								face.getTwistImageData().length != face.getThumbImageData().length)) {
					setLastError(0x0601, i18nMessage("face.jpgDataNotSet"));
					logger.error("更新人员信息失败，人员头像的图片数据为空 {}", session.getRemoteAddress());
					return false;
				}
				if(face.getTwistImageData() != null) {
					for(int i = 0; i < face.getTwistImageData().length; ++i) {
						if(face.getTwistImageData()[i] == null) {
							setLastError(0x0601, i18nMessage("face.twisImage_i_Null", i));
							logger.error("更新人员信息失败，索引为{}的人脸归一化图数据为空 {}", i, session.getRemoteAddress());
							return false;
						}
						if(face.getThumbImageData()[i] == null) {
							setLastError(0x0601, i18nMessage("face.thumbImage_i_Null", i));
							logger.error("更新人员信息失败，索引为{}的人脸缩略图数据为空 {}", i, session.getRemoteAddress());
							return false;
						}
					}
				} else if(face.getJpgFileContent() != null) {
					face.setTwistImageData(new byte[face.getJpgFileContent().length][]);
					face.setThumbImageData(new byte[face.getJpgFileContent().length][]);
					for(int i = 0; i < face.getJpgFileContent().length; ++i) {
						Object[] rets = config.noNativeMode?new Object[] {-17}:HaCamera.twistImage(face.getJpgFileContent()[i]);
						if(rets.length == 1) {
							face.setTwistImageData(null);
							face.setThumbImageData(null);
							setLastError(0x0601, i18nMessage("face.twis.fail", i, rets[0], i18nMessage("face.twis.error." + rets[0])));
							logger.error("更新人员信息失败，从图片内容检测人脸提取归一化图失败。错误码{} {}", rets[0], session.getRemoteAddress());
							return false;
						}
						face.getTwistImageData()[i] = (byte[])rets[2];
						face.getThumbImageData()[i] = (byte[])rets[1];
					}
				} else if(face.getJpgFilePath() != null) {
					face.setTwistImageData(new byte[face.getJpgFilePath().length][]);
					face.setThumbImageData(new byte[face.getJpgFilePath().length][]);
					for(int i = 0; i < face.getJpgFilePath().length; ++i) {
						try {
							Object[] rets = config.noNativeMode?new Object[] {-17}:HaCamera.twistImage(Files.readAllBytes(new File(face.getJpgFilePath()[i]).toPath()));
							if(rets.length == 1) {
								face.setTwistImageData(null);
								face.setThumbImageData(null);
								setLastError(0x0601, i18nMessage("face.twis.failPath", face.getJpgFilePath()[i], rets[0], i18nMessage("face.twis.error." + rets[0])));
								logger.error("更新人员信息失败，从图片路径检测人脸提取归一化图失败。路径{} 错误码{} {}", new Object[] {face.getJpgFilePath()[i], rets[0], session.getRemoteAddress()});
								return false;
							}
							face.getTwistImageData()[i] = (byte[])rets[2];
							face.getThumbImageData()[i] = (byte[])rets[1];
						} catch (IOException e) {
							setLastError(0x06FF, e.getMessage());
							logger.error("更新人员信息失败，从人脸图片路径转换人脸数据错误 " + session.getRemoteAddress(), e);
							return false;
						}
					}
				}
				ModifyFaceRequest pkg = new ModifyFaceRequest(face);
				String sendRet = sendPkg(session, pkg);
				if(sendRet != Constants.NOERROR) {
					if(sendRet == Constants.SENDTIMEOUT) {
						setLastError(0x06FE, i18nMessage("normal.sendTimeout"));
						logger.error("更新人员信息失败，向设备发送指令超时{}-{}", session.getRemoteAddress(), i18nMessage("normal.sendTimeout"));
					} else {
						setLastError(0x0601, i18nMessage("normal.sendPkgParseError", sendRet));
						logger.error("更新人员信息失败，解析发送封包失败{}-{}", session.getRemoteAddress(), sendRet);
					}
					return false;
				}
				session.removeAttribute(SESATTR_KEY_RET_FACE0);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_FACE0)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_FACE0);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0604, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("更新人员信息失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
						}
						else {
							setLastError(0x0603, i18nMessage("ack." + da.getACKCode()));
							logger.warn("更新人员信息失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
						}
						return false;
					}
					setLastError(0, i18nMessage("global.noError"));
					logger.info("更新人员信息成功 {}", session.getRemoteAddress());
					return true;
				}
			} catch (Exception e) {
				setLastError(0x06FF, e.getMessage());
				logger.error("更新人员信息发生异常 " + session.getRemoteAddress(), e);
				return false;
			}
			setLastError(0x0602, i18nMessage("normal.timeout"));
			logger.error("更新人员信息失败，等待网络返回超时 {}", session.getRemoteAddress());
			return false;
		}
	}

	/**
	 * 通过人员编号删除模板库中数据
	 *
	 * @param deviceID 设备标识符
	 * @param id 人员编号
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFace(String deviceID, String id, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return deleteFaceCore(deviceNo2Session.get(deviceID), -1, id, timeoutInMilli, false);
		else if(sn2Session.containsKey(deviceID))
			return deleteFaceCore(sn2Session.get(deviceID), -1, id, timeoutInMilli, false);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("通过人员编号删除模板库中数据失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 通过人员角色删除模板库中数据
	 * <br>
	 * 阻塞等待设备删除完毕（请将<code>timeoutInMilli</code>的值稍微设置大一点）
	 *
	 * @param deviceID 设备标识符
	 * @param role 人员角色 0:普通人员 1:白名单 2:黑名单 3:所有人员
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFaceSync(String deviceID, int role, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return deleteFaceCore(deviceNo2Session.get(deviceID), role, null, timeoutInMilli, false);
		else if(sn2Session.containsKey(deviceID))
			return deleteFaceCore(sn2Session.get(deviceID), role, null, timeoutInMilli, false);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("通过人员角色删除模板库中数据失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 通过人员角色删除模板库中数据
	 *
	 * @param deviceID 设备标识符
	 * @param role 人员角色 0:普通人员 1:白名单 2:黑名单 3:所有人员
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFace(String deviceID, int role, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return deleteFaceCore(deviceNo2Session.get(deviceID), role, null, timeoutInMilli, false);
		else if(sn2Session.containsKey(deviceID))
			return deleteFaceCore(sn2Session.get(deviceID), role, null, timeoutInMilli, false);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("通过人员角色删除模板库中数据失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 通过人员角色删除模板库中数据
	 *
	 * @param sn 设备序列号
	 * @param role 人员角色 人员角色 0:普通人员 1:白名单 2:黑名单 3:所有人员
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFaceBySn(String sn, int role, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn删除人员，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("删除sn为{}的人员失败，设备未连接", sn);
			return false;
		}
		return deleteFaceCore(session, role, null, timeoutInMilli, false);
	}
	/**
	 * 通过人员角色删除模板库中数据
	 * <br>
	 * 阻塞等待设备删除完毕（请将<code>timeoutInMilli</code>的值稍微设置大一点）
	 *
	 * @param sn 设备序列号
	 * @param role 人员角色 人员角色 0:普通人员 1:白名单 2:黑名单 3:所有人员
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFaceSyncBySn(String sn, int role, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn删除人员，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("删除sn为{}的人员失败，设备未连接", sn);
			return false;
		}
		return deleteFaceCore(session, role, null, timeoutInMilli, true);
	}
	/**
	 * 通过人员编号删除模板库中数据
	 *
	 * @param sn 设备序列号
	 * @param id 人员编号
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFaceBySn(String sn, String id, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn删除人员，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("删除sn为{}的人员失败，设备未连接", sn);
			return false;
		}
		return deleteFaceCore(session, -1, id, timeoutInMilli, false);
	}
	/**
	 * 通过人员角色删除模板库中数据
	 *
	 * @param deviceNo 设备编号
	 * @param role 人员角色 人员角色 0:普通人员 1:白名单 2:黑名单 3:所有人员
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFaceByDeviceNo(String deviceNo, int role, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo删除人员，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("删除deviceNo为{}的人员失败，设备未连接", deviceNo);
			return false;
		}
		return deleteFaceCore(session, role, null, timeoutInMilli, false);
	}
	/**
	 * 通过人员角色删除模板库中数据
	 * <br>
	 * 阻塞等待设备删除完毕（请将<code>timeoutInMilli</code>的值稍微设置大一点）
	 *
	 * @param deviceNo 设备编号
	 * @param role 人员角色 人员角色 0:普通人员 1:白名单 2:黑名单 3:所有人员
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFaceSyncByDeviceNo(String deviceNo, int role, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo删除人员，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("删除deviceNo为{}的人员失败，设备未连接", deviceNo);
			return false;
		}
		return deleteFaceCore(session, role, null, timeoutInMilli, true);
	}
	/**
	 * 通过人员编号删除模板库中数据
	 *
	 * @param deviceNo 设备编号
	 * @param id 人员编号
	 * @param timeoutInMilli 超时时间
	 * @return 是否删除成功
	 */
	public boolean deleteFaceByDeviceNo(String deviceNo, String id, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo删除人员，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("删除deviceNo为{}的人员失败，设备未连接", deviceNo);
			return false;
		}
		return deleteFaceCore(session, -1, id, timeoutInMilli, false);
	}
	private boolean deleteFaceCore(IoSession session, int role, String id, long timeoutInMilli, boolean sync) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_FACE);
		synchronized (locker) {
			try {
				DeleteFaceRequest pkg = new DeleteFaceRequest();
				pkg.setID(id);
				pkg.setRole(role);
				String sendRet = sendPkg(session, pkg);
				if(sendRet != Constants.NOERROR) {
					if(sendRet == Constants.SENDTIMEOUT) {
						setLastError(0x07FE, i18nMessage("normal.sendTimeout"));
						logger.error("删除人员信息失败，向设备发送指令超时{}-{}", session.getRemoteAddress(), i18nMessage("normal.sendTimeout"));
					} else {
						setLastError(0x0701, i18nMessage("normal.sendPkgParseError", sendRet));
						logger.error("删除人员信息失败，解析发送封包失败{}-{}", session.getRemoteAddress(), sendRet);
					}
					return false;
				}
				if(sync) {
					session.removeAttribute(SESATTR_KEY_RET_FACE);
					((Semaphore)locker).tryAcquire(2, timeoutInMilli, TimeUnit.MILLISECONDS); // FIXME 需两次响应，第一次是开始删除，第二次是删除完。可能会造成死锁！！！
					if(session.containsAttribute(SESATTR_KEY_RET_FACE)) {
						Object attrValue = session.getAttribute(SESATTR_KEY_RET_FACE);
						if(attrValue instanceof DeSerializeAdapter) {
							DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
							if(da.getTag() != null) {
								setLastError(0x0704, i18nMessage("normal.parseAnswerError", da.getTag()));
								logger.error("删除人员信息失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
							}
							else {
								setLastError(0x0703, i18nMessage("ack." + da.getACKCode()));
								logger.warn("删除人员信息失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
							}
							return false;
						}
						setLastError(0, i18nMessage("global.noError"));
						logger.info("删除人员信息成功 {} 总计删除{}条", session.getRemoteAddress(), attrValue);
						return true;
					}
				} else {
					session.removeAttribute(SESATTR_KEY_RET_FACE0);
					((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
					if(session.containsAttribute(SESATTR_KEY_RET_FACE0)) {
						Object attrValue = session.getAttribute(SESATTR_KEY_RET_FACE0);
						if(attrValue instanceof DeSerializeAdapter) {
							DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
							if(da.getTag() != null) {
								setLastError(0x0704, i18nMessage("normal.parseAnswerError", da.getTag()));
								logger.error("删除人员信息失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
							}
							else {
								setLastError(0x0703, i18nMessage("ack." + da.getACKCode()));
								logger.warn("删除人员信息失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
							}
							return false;
						}
						setLastError(0, i18nMessage("global.noError"));
						logger.info("删除人员信息成功 {}", session.getRemoteAddress());
						return true;
					}
				}
			} catch (Exception e) {
				setLastError(0x07FF, e.getMessage());
				logger.error("删除人员信息发生异常 " + session.getRemoteAddress(), e);
				return false;
			}
			setLastError(0x0702, i18nMessage("normal.timeout"));
			logger.error("删除人员信息失败，等待网络返回超时 {}", session.getRemoteAddress());
			return false;
		}
	}

	/**
	 * 更新设备应用参数
	 *
	 * @param deviceID 设备标识符
	 * @param appConfig 新的应用参数
	 * @param timeoutInMilli 超时时间
	 * @return 是否设置成功
	 */
	public boolean setAppConfig(String deviceID, AppConfig appConfig, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return setAppConfigCore(deviceNo2Session.get(deviceID), appConfig, timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return setAppConfigCore(sn2Session.get(deviceID), appConfig, timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("更新设备应用参数失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 更新设备应用参数
	 *
	 * @param sn 设备序列号
	 * @param appConfig 新的应用参数
	 * @param timeoutInMilli 超时时间
	 * @return 是否设置成功
	 */
	public boolean setAppConfigBySn(String sn, AppConfig appConfig, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn设置设备应用参数，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("设置sn为{}的应用参数失败，设备未连接", sn);
			return false;
		}
		return setAppConfigCore(session, appConfig, timeoutInMilli);
	}
	/**
	 * 更新设备应用参数
	 *
	 * @param deviceNo 设备编号
	 * @param appConfig 新的应用参数
	 * @param timeoutInMilli 超时时间
	 * @return 是否设置成功
	 */
	public boolean setAppConfigByDeviceNo(String deviceNo, AppConfig appConfig, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo设置设备应用参数，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("设置deviceNo为{}的应用参数失败，设备未连接", deviceNo);
			return false;
		}
		return setAppConfigCore(session, appConfig, timeoutInMilli);
	}
	private boolean setAppConfigCore(IoSession session, AppConfig appConfig, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_CFG);
		synchronized (locker) {
			try {
				AppConfig config_old = (AppConfig)session.getAttribute(SESATTR_KEY_APPCONFIG);
				SetAppConfigRequest pkg = new SetAppConfigRequest(appConfig);
				if(appConfig.equals(config_old) && !config.forceSetAppConfig) {
					setLastError(0x0801, i18nMessage("cfg.notChange"));
					logger.warn("尝试设置设备应用参数失败，设备应用参数未做任何修改(如果需要强制同步，请设置ConfigServerConfig中forceSetAppConfig为true){} {}", session.getRemoteAddress(), appConfig);
					return false;
				}
				String sendRet = sendPkg(session, pkg);
				if(sendRet != Constants.NOERROR) {
					if(sendRet == Constants.SENDTIMEOUT) {
						setLastError(0x08FE, i18nMessage("normal.sendTimeout"));
						logger.error("设置应用参数失败，向设备发送指令超时{}-{}", session.getRemoteAddress(), i18nMessage("normal.sendTimeout"));
					} else {
						setLastError(0x0801, i18nMessage("normal.sendPkgParseError", sendRet));
						logger.error("设置应用参数失败，解析发送封包失败{}-{}", session.getRemoteAddress(), sendRet);
					}
					return false;
				}
				session.removeAttribute(SESATTR_KEY_RET_CFG);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_CFG)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_CFG);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0804, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("修改设备应用参数失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
						}
						else {
							setLastError(0x0803, i18nMessage("ack." + da.getACKCode()));
							logger.warn("修改设备应用参数失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
						}
						return false;
					}
					setLastError(0, i18nMessage("global.noError"));
					session.setAttribute(SESATTR_KEY_APPCONFIG, appConfig.clone());
					logger.info("修改设备应用参数成功 {}", session.getRemoteAddress());
					return true;
				}
			} catch (Exception e) {
				setLastError(0x08FF, e.getMessage());
				logger.error("修改设备应用参数发生异常 " + session.getRemoteAddress(), e);
				return false;
			}
			setLastError(0x0802, i18nMessage("normal.timeout"));
			logger.error("修改设备应用参数失败，等待网络返回超时 {}", session.getRemoteAddress());
			return false;
		}
	}

	/**
	 * 重启设备
	 *
	 * @param deviceID 设备标识符
	 * @param timeoutInMilli 超时时间
	 * @return 操作是否成功
	 */
	public boolean reboot(String deviceID, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return rebootCore(deviceNo2Session.get(deviceID), timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return rebootCore(sn2Session.get(deviceID), timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("重启设备失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return false;
		}
	}
	/**
	 * 通过设备序列号重启设备
	 *
	 * @param sn 设备序列号
	 * @param timeoutInMilli 超时时间
	 * @return 操作是否成功
	 */
	public boolean rebootBySn(String sn, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn重启设备，但传入null");
			return false;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("重启sn为{}的设备失败，设备未连接", sn);
			return false;
		}
		return rebootCore(session, timeoutInMilli);
	}
	/**
	 * 通过设备编号重启设备
	 *
	 * @param deviceNo 设备编号
	 * @param timeoutInMilli 超时时间
	 * @return 操作是否成功
	 */
	public boolean rebootByDeviceNo(String deviceNo, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo重启设备，但传入null");
			return false;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("重启deviceNo为{}的设备失败，设备未连接", deviceNo);
			return false;
		}
		return rebootCore(session, timeoutInMilli);
	}
	private boolean rebootCore(IoSession session, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_CFG);
		synchronized (locker) {
			try {
				if(sendDummy(session, MESSAGE_ID_REBOOT) == Constants.SENDTIMEOUT) {
					logger.error("重启设备失败，向设备发送指令超时 {}", session.getRemoteAddress());
					setLastError(0x09FE, i18nMessage("normal.sendTimeout"));
					return false;
				}
				session.removeAttribute(SESATTR_KEY_RET_CFG);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_CFG)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_CFG);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0904, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("重启设备失败，解析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
						}
						else {
							setLastError(0x0903, i18nMessage("ack." + da.getACKCode()));
							logger.warn("重启设备失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
						}
						return false;
					}
					setLastError(0, i18nMessage("global.noError"));
					logger.info("重启设备成功 {}", session.getRemoteAddress());
					return true;
				}
			} catch (Exception e) {
				setLastError(0x09ff, e.getMessage());
				logger.error("重启设备发生异常 " + session.getRemoteAddress(), e);
				return false;
			}
			setLastError(0x0902, i18nMessage("normal.timeout"));
			logger.error("重启设备失败，等待网络返回超时 {}", session.getRemoteAddress());
			return false;
		}
	}

	/**
	 * 获取设备所有人员编号
	 *
	 * @param deviceID 设备标识符
	 * @param timeoutInMilli 超时时间
	 * @return 人员编号数组，或空数组（设备没有人员）或null（中途出错）
	 */
	public String[] getAllPersonID(String deviceID, long timeoutInMilli) {
		if(deviceNo2Session.containsKey(deviceID))
			return getAllPersonIDCore(deviceNo2Session.get(deviceID), timeoutInMilli);
		else if(sn2Session.containsKey(deviceID))
			return getAllPersonIDCore(sn2Session.get(deviceID), timeoutInMilli);
		else {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取设备所有人员编号失败，传入的设备标识符{}不存在于已连接设备中", deviceID);
			return null;
		}
	}
	/**
	 * 通过设备序列号获取设备所有人员编号
	 *
	 * @param deviceID 设备标识符
	 * @param timeoutInMilli 超时时间
	 * @return 人员编号数组，或空数组（设备没有人员）或null（中途出错）
	 */
	public String[] getAllPersonIDBySn(String sn, long timeoutInMilli) {
		if(sn == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以sn获取所有人员编号，但传入null");
			return null;
		}
		IoSession session = sn2Session.get(sn);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取sn为{}的所有人员编号失败，设备未连接", sn);
			return null;
		}
		return getAllPersonIDCore(session, timeoutInMilli);
	}
	/**
	 * 通过设备编号获取设备所有人员编号
	 *
	 * @param deviceID 设备标识符
	 * @param timeoutInMilli 超时时间
	 * @return 人员编号数组，或空数组（设备没有人员）或null（中途出错）
	 */
	public String[] getAllPersonIDByDeviceNo(String deviceNo, long timeoutInMilli) {
		if(deviceNo == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("尝试以deviceNo获取所有人员编号，但传入null");
			return null;
		}
		IoSession session = deviceNo2Session.get(deviceNo);
		if(session == null) {
			setLastError(0x0101, i18nMessage("global.deviceNotFound"));
			logger.warn("获取deviceNo为{}的所有人员编号失败，设备未连接", deviceNo);
			return null;
		}
		return getAllPersonIDCore(session, timeoutInMilli);
	}
	private String[] getAllPersonIDCore(IoSession session, long timeoutInMilli) {
		Object locker = session.getAttribute(SESATTR_KEY_LOCK_FACE);
		synchronized (locker) {
			try {
				if(sendDummy(session, MESSAGE_ID_GETALLPERSONID) == Constants.SENDTIMEOUT) {
					logger.error("获取所有人员编号失败，向设备发送指令超时 {}", session.getRemoteAddress());
					setLastError(0x0EFE, i18nMessage("normal.sendTimeout"));
					return null;
				}
				session.removeAttribute(SESATTR_KEY_RET_FACE0);
				((Semaphore)locker).tryAcquire(timeoutInMilli, TimeUnit.MILLISECONDS);
				if(session.containsAttribute(SESATTR_KEY_RET_FACE0)) {
					Object attrValue = session.getAttribute(SESATTR_KEY_RET_FACE0);
					if(attrValue instanceof DeSerializeAdapter) {
						DeSerializeAdapter da = ((DeSerializeAdapter)attrValue);
						if(da.getTag() != null) {
							setLastError(0x0E04, i18nMessage("normal.parseAnswerError", da.getTag()));
							logger.error("获取所有人员编号失败，解日析设备返回数据封包出现错误 {}-{}", session.getRemoteAddress(), da.getTag());
						}
						else {
							setLastError(0x0E03, i18nMessage("ack." + da.getACKCode()));
							logger.warn("获取所有人员编号失败，设备返回数据封包中响应码为{}-{}", session.getRemoteAddress(), da.getACKCode());
						}
						return null;
					}
					String[] personIDs = (String[])attrValue;
					setLastError(0, i18nMessage("global.noError"));
					logger.info("获取所有人员编号成功 {} 共计{}条", session.getRemoteAddress(), personIDs.length);
					return personIDs;
				}
			} catch (Exception e) {
				setLastError(0x0EFF, e.getMessage());
				logger.error("获取所有人员编号发生异常 " + session.getRemoteAddress(), e);
				return null;
			}
			setLastError(0x0E02, i18nMessage("normal.timeout"));
			logger.error("获取所有人员编号失败，等待网络返回超时 {}", session.getRemoteAddress());
			return null;
		}
	}

    /*public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) {
            buf.append(String.format("%02X ", new Integer(b & 0xff)));
        }

        return buf.toString();
    }*/

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(final IoSession session, Object message) throws Exception {
		IoBuffer buffer = (IoBuffer) message;
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		//buffer.mark();
		int type = buffer.getInt();
		int messageID = (type & 0x3FF);
		int len = buffer.getInt();
        /*byte[] _data = new byte[len];
        buffer.get(_data);
        System.out.print("C " + messageID + "=>");
		System.out.println(bytesToHexFun3(_data));
        buffer.reset();
		buffer.skip(8);*/
		session.setAttribute(SESATTR_KEY_LASTRECV, Calendar.getInstance());
		if(messageID == MESSAGE_ID_HEARTBEAT) {
			logger.debug("收到心跳消息{}", session.getRemoteAddress());
			logger.info("收到心跳消息{}", session.getRemoteAddress());
			return;
		}
		if(messageID == MESSAGE_ID_STREAM) {
			StreamDataResponse sdr = new StreamDataResponse();
			if(streamDataReceivedEventHandler != null && sdr.readFrom(buffer) == Constants.NOERROR) {
				if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.DeviceNoKnown ||
						config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
						session.containsAttribute(SESATTR_KEY_DEVICENO)) {
					streamDataReceivedEventHandler.onStreamDataReceived((String)session.getAttribute(SESATTR_KEY_DEVICENO), sdr.getH264Segment());
				}
				if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.SnKnown ||
						config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
						session.containsAttribute(SESATTR_KEY_CAMERASN)) {
					streamDataReceivedEventHandler.onStreamDataReceived((String)session.getAttribute(SESATTR_KEY_CAMERASN), sdr.getH264Segment());
				}
			}
			return;
		}
		if(messageID == MESSAGE_ID_REG) {
			RegRequest rr = new RegRequest();
			if(rr.readFrom(buffer)) {
				if(cameraAuthingEventHandler != null) {
					logger.debug("收到设备登陆消息 {}", session.getRemoteAddress());
					if(!cameraAuthingEventHandler.onCameraAuthing(rr.getData().getTime(), rr.getData().getUserName(), rr.getData().getPassword())) {
						//disconnectCameraCore(session, 500);
						session.closeOnFlush();
						logger.info("设备登陆失败 {}", session.getRemoteAddress());
					}
				}
			}
			return;
		}
		if(messageID == MESSAGE_ID_FACEDELETEPROGRESSINFO) {
			FaceDeleteProgressInfoRequest fdpr = new FaceDeleteProgressInfoRequest();
			if(fdpr.readFrom(buffer)) {
				if(faceDeleteProgressInfoEventHandler != null) {
					if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.DeviceNoKnown ||
							config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
							session.containsAttribute(SESATTR_KEY_DEVICENO)) {
						faceDeleteProgressInfoEventHandler.onFaceDeleteProgressInfoReceived((String)session.getAttribute(SESATTR_KEY_DEVICENO), fdpr.getTotal(), fdpr.getCurrent(), fdpr.getPersonID());
					}
					if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.SnKnown ||
							config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
							session.containsAttribute(SESATTR_KEY_CAMERASN)) {
						faceDeleteProgressInfoEventHandler.onFaceDeleteProgressInfoReceived((String)session.getAttribute(SESATTR_KEY_CAMERASN), fdpr.getTotal(), fdpr.getCurrent(), fdpr.getPersonID());
					}
				}
				if(fdpr.getTotal() == fdpr.getCurrent()) {
					session.setAttribute(SESATTR_KEY_RET_FACE, fdpr.getCurrent());
					((Semaphore)session.getAttribute(SESATTR_KEY_LOCK_FACE)).release();
				}
			}
			return;
		}
		if(messageID == MESSAGE_ID_ACK) {
			if(len < 8) return;
			int respMessageID = buffer.getInt();
			int ackCode = buffer.getInt();
			switch(respMessageID) {
				case MESSAGE_ID_VERSION_GET:
					ReadVersionResponse rvr = new ReadVersionResponse(ackCode);
					String readRet_rvr = null;
					if(ackCode == ACK_CODE_SUCCESS && (readRet_rvr = rvr.readFrom(buffer)) == Constants.NOERROR) {
						Version version = rvr.getVersion();
						session.setAttribute(SESATTR_KEY_VERSION, version);
						//logger.info("收到设备版本号回应包 {} {}", session.getRemoteAddress(), version);
						if(version.getSn() != null && !version.getSn().isEmpty()) {
							if(sn2Session.containsKey(version.getSn())) {
								IoSession sessionOld = sn2Session.get(version.getSn());
								logger.info("已有sn相同的连接在案，先将其断开 {}", sessionOld.getRemoteAddress());
								sessionOld.removeAttribute(SESATTR_KEY_CAMERASN);
								sessionOld.closeOnFlush();
							}
							sn2Session.put(version.getSn(), session);
							session.setAttribute(SESATTR_KEY_CAMERASN, version.getSn());
							if(config.autoRestartStream && playedDeviceIDs.contains(version.getSn())) {
								startStreamBySn(version.getSn());
								logger.debug("设备连接后自动重新播放视频成功 {}", version.getSn());
							}
						}
					} else {
						logger.info("收到设备版本号回应包，但解析失败 {} {}", session.getRemoteAddress(), readRet_rvr);
					}
					break;
				case MESSAGE_ID_APPCONFIG_GET:
					ReadAppConfigResponse racr = new ReadAppConfigResponse(ackCode);
					String readRet_racr = null;
					if(ackCode == ACK_CODE_SUCCESS && (readRet_racr = racr.readFrom(buffer)) == Constants.NOERROR) {
						AppConfig appConfig = racr.getAppConfig();
						session.setAttribute(SESATTR_KEY_APPCONFIG, appConfig);
						//logger.info("收到设备应用参数回应包 {} {}", session.getRemoteAddress(), appConfig);
						if(appConfig.getDeviceNo() != null && !appConfig.getDeviceNo().isEmpty()) {
							if(deviceNo2Session.containsKey(appConfig.getDeviceNo())) {
								IoSession sessionOld = deviceNo2Session.get(appConfig.getDeviceNo());
								logger.info("已有deviceNo相同的连接在案，先将其断开 {}", sessionOld.getRemoteAddress());
								sessionOld.removeAttribute(SESATTR_KEY_DEVICENO);
								sessionOld.closeOnFlush();
							}
							deviceNo2Session.put(appConfig.getDeviceNo(), session);
							session.getAttribute(SESATTR_KEY_DEVICENO, appConfig.getDeviceNo());
							if(config.autoRestartStream && playedDeviceIDs.contains(appConfig.getDeviceNo())) {
								startStreamByDeviceNo(appConfig.getDeviceNo());
								logger.debug("设备连接后自动重新播放视频成功 {}", appConfig.getDeviceNo());
							}
						}
						scheduledThreadPool.schedule(() -> {
							if(session.containsAttribute(SESATTR_KEY_CAMERASN)) {
								if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.SnKnown ||
										config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
										cameraConnectedEventHandler != null)
									cameraConnectedEventHandler.onCameraConnected((String)session.getAttribute(SESATTR_KEY_CAMERASN));
							}
							if(session.containsAttribute(SESATTR_KEY_DEVICENO)) {
								if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.DeviceNoKnown ||
										config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
										cameraConnectedEventHandler != null)
									cameraConnectedEventHandler.onCameraConnected((String)session.getAttribute(SESATTR_KEY_DEVICENO));
							}
						}, 200, TimeUnit.MILLISECONDS);
					} else {
						logger.info("收到设备应用参数回应包，但解析失败 {} {}", session.getRemoteAddress(), readRet_racr);
					}
					break;
				case MESSAGE_ID_TIME_GET:
					ReadTimeResponse rtr = new ReadTimeResponse(ackCode);
					if(ackCode != ACK_CODE_SUCCESS) {
						session.setAttribute(SESATTR_KEY_RET_TIME, rtr);
					} else {
						String readRet = rtr.readFrom(buffer);
						if(readRet != Constants.NOERROR) {
							rtr.setTag(readRet);
							session.setAttribute(SESATTR_KEY_RET_TIME, rtr);
						} else
							session.setAttribute(SESATTR_KEY_RET_TIME, rtr.getPojoTime());
					}
					((Semaphore)session.getAttribute(SESATTR_KEY_LOCK_TIME)).release();
					break;
				case MESSAGE_ID_NETCFG_GET:
					ReadNetConfigResponse rncr = new ReadNetConfigResponse(ackCode);
					if(ackCode == ACK_CODE_SUCCESS && rncr.readFrom(buffer) == Constants.NOERROR) {
						NetConfig netConfig = rncr.getNetConfig();
						session.setAttribute(SESATTR_KEY_NETCONFIG, netConfig);
					}
					break;
				case MESSAGE_ID_TIME_SET:
					if(ackCode == ACK_CODE_SUCCESS) {
						session.setAttribute(SESATTR_KEY_RET_TIME);
					} else {
						session.setAttribute(SESATTR_KEY_RET_TIME, new DummyDeSerializable(ackCode));
					}
					((Semaphore)session.getAttribute(SESATTR_KEY_LOCK_TIME)).release();
					break;
				case MESSAGE_ID_LISTFACE:
					if(session.containsAttribute(SESATTR_KEY_LOCK_FACE)) {
						Object lock_listface = session.getAttribute(SESATTR_KEY_LOCK_FACE);
						ListFaceResponse lfr = new ListFaceResponse(ackCode);
						if(ackCode != ACK_CODE_SUCCESS) {
							session.setAttribute(SESATTR_KEY_RET_FACE0, new DummyDeSerializable(ackCode));
							((Semaphore)lock_listface).release();
							return;
						}
						String readRet = lfr.readFrom(buffer);
						if(readRet != Constants.NOERROR) {
							DeSerializeAdapter da = new DummyDeSerializable(ackCode);
							da.setTag(readRet);
							session.setAttribute(SESATTR_KEY_RET_FACE0, da);
							((Semaphore)lock_listface).release();
							return;
						}
						if(!session.containsAttribute(SESATTR_KEY_RET_FACE)) {
							session.removeAttribute(SESATTR_KEY_RET_FACE0);
							((Semaphore)lock_listface).release();
							return;
						}
						session.setAttribute(SESATTR_KEY_RET_FACE0, Integer.valueOf(lfr.getTotal()));
						if(lfr.getIndex() == 0) {
							((Semaphore)lock_listface).release();
							return;
						}
						List<Face> faces = (List<Face>)session.getAttribute(SESATTR_KEY_RET_FACE);
						faces.add(lfr.getFace());
					}
					break;
				case MESSAGE_ID_ADDFACE:
				case MESSAGE_ID_MODIFYFACE:
				case MESSAGE_ID_DELETEFACE:
					if(ackCode == ACK_CODE_SUCCESS) {
						session.setAttribute(SESATTR_KEY_RET_FACE0);
					} else {
						session.setAttribute(SESATTR_KEY_RET_FACE0, new DummyDeSerializable(ackCode));
					}
					((Semaphore)session.getAttribute(SESATTR_KEY_LOCK_FACE)).release();
					break;
				case MESSAGE_ID_APPCONFIG_SET:
				case MESSAGE_ID_REBOOT:
					if(ackCode == ACK_CODE_SUCCESS) {
						session.setAttribute(SESATTR_KEY_RET_CFG);
					} else {
						session.setAttribute(SESATTR_KEY_RET_CFG, new DummyDeSerializable(ackCode));
					}
					((Semaphore)session.getAttribute(SESATTR_KEY_LOCK_CFG)).release();
					break;
				case MESSAGE_ID_GETALLPERSONID:
					GetAllPersonIDResponse gapr = new GetAllPersonIDResponse(ackCode);
					if(ackCode != ACK_CODE_SUCCESS) {
						session.setAttribute(SESATTR_KEY_RET_FACE0, gapr);
					} else {
						String readRet = gapr.readFrom(buffer);
						if(readRet != Constants.NOERROR) {
							gapr.setTag(readRet);
							session.setAttribute(SESATTR_KEY_RET_FACE0, gapr);
						} else
							session.setAttribute(SESATTR_KEY_RET_FACE0, gapr.getPersonIDs());
					}
					((Semaphore)session.getAttribute(SESATTR_KEY_LOCK_FACE)).release();
					break;
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.debug("连接成功关闭 {}", session.getRemoteAddress());
		String sn = null;
		String deviceNo = null;
		if(session.containsAttribute(SESATTR_KEY_CAMERASN) || session.containsAttribute(SESATTR_KEY_DEVICENO)) {
			if(session.containsAttribute(SESATTR_KEY_CAMERASN)) {
				sn = (String) session.getAttribute(SESATTR_KEY_CAMERASN);
				logger.info("sn为{}", sn);
				if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.SnKnown ||
						config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown)
						&& cameraDisconnectedEventHandler != null)
					cameraDisconnectedEventHandler.onCameraDisconnected(sn);
			}
			if(session.containsAttribute(SESATTR_KEY_DEVICENO)) {
				deviceNo = (String) session.getAttribute(SESATTR_KEY_DEVICENO);
				//logger.info("deviceNo为{}", deviceNo);
				if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.DeviceNoKnown ||
						config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown)
						&& cameraDisconnectedEventHandler != null)
					cameraDisconnectedEventHandler.onCameraDisconnected(deviceNo);
			}
			if(sn != null)
				sn2Session.remove(sn);
			if(deviceNo != null)
				deviceNo2Session.remove(deviceNo);
		}else {
			logger.info("连接未绑定deviceNo或sn即被关闭，可能由于链路断开或有相同编号的设备连接进来 {}", session.getRemoteAddress());
		}
		session.removeAttribute(SESATTR_KEY_LOCK_TIME);
		session.removeAttribute(SESATTR_KEY_LOCK_FACE);
		session.removeAttribute(SESATTR_KEY_LOCK_CFG);
	}

	@Override
	public void sessionOpened(final IoSession session) throws Exception {
		logger.debug("连接成功建立 {}", session.getRemoteAddress());
		session.setAttribute(SESATTR_KEY_LOCK_TIME, new Semaphore(0));
		session.setAttribute(SESATTR_KEY_LOCK_FACE, new Semaphore(0));
		session.setAttribute(SESATTR_KEY_LOCK_CFG, new Semaphore(0));
		session.setAttribute(SESATTR_KEY_CFG, config);
		cachedThreadPool.execute(() -> {
			try {
				if(sendDummy(session, MESSAGE_ID_VERSION_GET) == Constants.SENDTIMEOUT) {
					logger.error("发送版本获取指令失败，向设备发送指令超时 {}", session.getRemoteAddress());
				} else
					logger.debug("发送版本获取指令成功 {}", session.getRemoteAddress());
				if(sendDummy(session, MESSAGE_ID_APPCONFIG_GET) == Constants.SENDTIMEOUT) {
					logger.error("发送应用参数获取指令失败，向设备发送指令超时 {}", session.getRemoteAddress());
				} else
					logger.debug("发送应用参数获取指令成功 {}", session.getRemoteAddress());
				if(sendDummy(session, MESSAGE_ID_NETCFG_GET) == Constants.SENDTIMEOUT) {
					logger.error("发送网络参数获取指令失败，向设备发送指令超时 {}", session.getRemoteAddress());
				} else
					logger.debug("发送网络参数获取指令成功 {}", session.getRemoteAddress());
			} catch (Throwable e) {
				//setLastError(0x01FF, e.getMessage());
				logger.error("获取设备信息失败", e);
			}
		});
	}

	@Override
	public void sessionIdle(final IoSession session, IdleStatus status) throws Exception {
		logger.debug("链路空闲 {} {}", status, session.getRemoteAddress());
		// TODO 可能要定时判断是否该断开视频，避免带宽泄漏
		if(status == IdleStatus.WRITER_IDLE) {
			if(!session.containsAttribute(SESATTR_KEY_APPCONFIG) || !session.containsAttribute(SESATTR_KEY_VERSION)) {
				// 可能由于网络问题造成获取设备信息失败，断开连接以待下次设备重连
				session.closeOnFlush();
				logger.error("由于设备长时间未回报应用参数或版本信息，断开它 {}", session.getRemoteAddress());
				return;
			}
		}
		else if(status == IdleStatus.READER_IDLE) {
			if(!session.containsAttribute(SESATTR_KEY_APPCONFIG)) return;
			AppConfig _config = (AppConfig) session.getAttribute(SESATTR_KEY_APPCONFIG);
			long usecBetween = Calendar.getInstance().getTime().getTime() - session.getLastReadTime();
			int secBetween = (int) (usecBetween / 1000);
			if(secBetween > 3 * _config.getHeartBeatInterval()) {
				// 设备很长时间没发过来数据了
				session.closeOnFlush();
				logger.error("由于设备长时间未发送数据，断开它 {}", session.getRemoteAddress());
				return;
			}
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error("网络交互中发生异常 " + session.getRemoteAddress(), cause);
	}

	private String sendDummy(IoSession session, int messsageID) throws Exception {
		return new DummySerializable(messsageID).sendTo(session, config.sysType, config.majorProtocol, config.minorProtocol);
	}
	private String sendPkg(IoSession session, SerializeAdapter pkg) throws Exception {
		return pkg.sendTo(session, config.sysType, config.majorProtocol, config.minorProtocol);
	}
}
