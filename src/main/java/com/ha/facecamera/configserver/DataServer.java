package main.java.com.ha.facecamera.configserver;

import static main.java.com.ha.facecamera.configserver.Constants.MESSAGE_ID_ACK;
import static main.java.com.ha.facecamera.configserver.Constants.MESSAGE_ID_DATA;
import static main.java.com.ha.facecamera.configserver.Constants.MESSAGE_ID_HEARTBEAT;
import static main.java.com.ha.facecamera.configserver.Constants.MESSAGE_ID_REG;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.util.ConcurrentHashSet;

import main.java.com.ha.facecamera.configserver.events.CameraAuthingEventHandler;
import main.java.com.ha.facecamera.configserver.events.CameraConnectedEventHandler;
import main.java.com.ha.facecamera.configserver.events.CameraDisconnectedEventHandler;
import main.java.com.ha.facecamera.configserver.events.CaptureCompareDataReceivedEventHandler;
import main.java.com.ha.facecamera.configserver.pkgs.CaptureCompareDataRequest;
import main.java.com.ha.facecamera.configserver.pkgs.RegRequest;
import main.java.com.ha.facecamera.configserver.pojo.CaptureCompareData;
import main.java.com.ha.tlv.DummySerializable;
import main.java.com.ha.tlv.TLVCodecFactory;
import main.java.com.ha.tlv.Util;

/**
 * 数据服务端
 * 
 * @author 林星
 *
 */
public final class DataServer extends IoHandlerAdapter {

    private static final String SESATTR_KEY_DEVICENO = "KEY_DEVICE_NO";
    private static final String SESATTR_KEY_SN = "KEY_SN";
    private static final String SESATTR_KEY_LASTRECV = "KEY_LASTRECV_TIME";

    // 内部属性定义
    private CameraConnectedEventHandler cameraConnectedEventHandler;
    private CameraDisconnectedEventHandler cameraDisconnectedEventHandler;
    private CaptureCompareDataReceivedEventHandler captureCompareDataReceivedEventHandler;
    private CameraAuthingEventHandler cameraAuthingEventHandler;
    
    private NioSocketAcceptor acceptor;
    private DataServerConfig config;
    private ConcurrentMap<String, IoSession> deviceNo2Session;
    private ConcurrentMap<String, IoSession> sn2Session;
    private ConcurrentHashSet<IoSession> allSession;
    private ExecutorService cachedThreadPool;
    private Thread heartBeatSendThread = new Thread() {
    	
    	@Override
    	public void run() {
    		while(true) {
    			Set<IoSession> sess = new HashSet<>();
    			if(config.keepAliveOnIdleConnection) {
    				sess = allSession;
    			}
    			else {
    				sess = sn2Session.values().stream().collect(Collectors.toSet());
    				sess.addAll(deviceNo2Session.values());
    			}
    			sess.parallelStream().forEachOrdered(ses -> {
					cachedThreadPool.execute(() -> {
						try {
							sendDummy(ses, MESSAGE_ID_HEARTBEAT);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		});
				});
	    		try {
					Thread.sleep(config.heartBeatInterval * 1000);
				} catch (InterruptedException e) {
					break;
				}
    		}
    	}
    };
    {
    	heartBeatSendThread.setDaemon(true);
    }

    // 外部函数
    /**
     * 注册相机连接事件回调
     * <br>
     * 数据链路会触发两次上线（当链路建立时会以设备IP:Port方式触发一次，当第一次数据上来时会以DeviceNo或/和Sn触发第二次），如果15秒设备未发送数据，则设备端一般会自行断开链路以节约资源
     * 
     * @param _cameraConnectedEventHandler 相机连接事件处理函数
     * @return 当前对象
     */
    public DataServer onCameraConnected(CameraConnectedEventHandler _cameraConnectedEventHandler) {
        this.cameraConnectedEventHandler = _cameraConnectedEventHandler;
        return this;
    }
    /**
     * 注册相机断开事件回调
     * <br>
     * 数据链路下线事件只会触发一次，如果设备在15秒内未上传数据，此时的下线仍以IP:Port方式告知上层应用，如果设备在过程中上传过数据，则会以DeviceNo或/和Sn触发一次
     * 
     * @param _cameraDisconnectedEventHandler 相机断开事件处理函数
     * @return 当前对象
     */
    public DataServer onCameraDisconnected(CameraDisconnectedEventHandler _cameraDisconnectedEventHandler) {
        this.cameraDisconnectedEventHandler = _cameraDisconnectedEventHandler;
        return this;
    }
    /**
     * 注册对比抓拍数据接收事件回调
     * @param _captureCompareDataReceivedEventHandler 对比抓拍数据接收事件处理函数
     * @return 当前对象
     */
    public DataServer onCaptureCompareDataReceived(CaptureCompareDataReceivedEventHandler _captureCompareDataReceivedEventHandler) {
    	this.captureCompareDataReceivedEventHandler = _captureCompareDataReceivedEventHandler;
    	return this;
    }
    /**
     * 注册设备鉴权事件回调 
     * @param _cameraAuthingEventHandler 设备鉴权事件处理函数
     * @return 当前对象
     */
    public DataServer onAuthing(CameraAuthingEventHandler _cameraAuthingEventHandler) {
    	this.cameraAuthingEventHandler = _cameraAuthingEventHandler;
    	return this;
    }
    
    public DataServer() {
        acceptor = new NioSocketAcceptor();
        acceptor.setHandler(this);
        acceptor.getFilterChain().addLast("tlv", new ProtocolCodecFilter(new TLVCodecFactory()));
        deviceNo2Session = new ConcurrentHashMap<>();
        sn2Session = new ConcurrentHashMap<>();
        allSession = new ConcurrentHashSet<>();
    }
    
    /**
     * 启动数据接收服务器
     * 
     * @param port 要监听的端口
     * @param _config 启动参数
     * @return 是否启动成功
     */
    public boolean start(int port, DataServerConfig _config) {
    	this.config = _config;
        try {
        	acceptor.setReuseAddress(true);
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            return false;
        }
        cachedThreadPool = Executors.newCachedThreadPool();
        heartBeatSendThread.start();
        return true;
    }
    
    /**
     * 停止服务器监听
     */
    public void stop() {
    	heartBeatSendThread.interrupt();
    	cachedThreadPool.shutdown();
    	acceptor.unbind();
        acceptor.dispose(true);
    }
    
    /**
     * 获取设备在线状态
     * 
     * @param deviceID 设备编号或设备序列号
     * @return 设备是否在线
     */
    public boolean getCameraOnlineState(String deviceID) {
    	return deviceNo2Session.containsKey(deviceID) ||  sn2Session.containsKey(deviceID);
    }
    
    /**
     * 断开指定设备
     * 
     * @param deviceID 设备编号或设备序列号
     * @param waitInMilli 等待断开操作完成的超时<br>传递大于0的值表示要等待，反之则立即返回，但不管是否等待，都会尝试断开操作
     * @return 在等待一定时间之后是否断开成功<br>此值只在等待时有效，返回false只表示在等待一段时间后断开操作还未完全完成，但不表示断开失败
     */
    public boolean disconnectCamera(String deviceID, long waitInMilli) {
    	IoSession session = deviceNo2Session.get(deviceID);
    	if(session == null)
    		session = sn2Session.get(deviceID);
    	if(session == null) return false;
    	CloseFuture cf = session.closeOnFlush();
    	if(waitInMilli > 0)
			try {
				return cf.await(waitInMilli);
			} catch (InterruptedException e) {

			}
    	return false;
    }
    
    /**
     * 获取设备上次发送数据的时间
     * <br>
     * 即上次收到该设备数据的时间(不包括心跳)
     * 
     * @param deviceID 设备编号或设备序列号
     * @return 上次发送数据的时间
     */
    public Date getLastDataRecvTime(String deviceID) {
    	IoSession session = deviceNo2Session.get(deviceID);
    	if(session == null)
    		session = sn2Session.get(deviceID);
    	if(session == null) {
    		return null;
    	}
    	if(!session.containsAttribute(SESATTR_KEY_LASTRECV)) return null;
    	return ((Calendar) session.getAttribute(SESATTR_KEY_LASTRECV)).getTime();
    }
    
    @SuppressWarnings("unused")
	@Override
    public void messageReceived(final IoSession session, Object message) throws Exception {
    	IoBuffer buffer = (IoBuffer) message;
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int type = buffer.getInt();
        int messageID = (type & 0x3FF);
        int len = buffer.getInt();
        if(messageID == MESSAGE_ID_REG) {
        	RegRequest rr = new RegRequest();
        	if(rr.readFrom(buffer)) {
        		if(cameraAuthingEventHandler != null)
        			if(!cameraAuthingEventHandler.onCameraAuthing(rr.getData().getTime(), rr.getData().getUserName(), rr.getData().getPassword())) {
        				//disconnectCameraCore(session, 500);
        				session.closeOnFlush();
        			}
        	}
        }
    	if(messageID != MESSAGE_ID_DATA) return;
    	final CaptureCompareDataRequest ccdr = new CaptureCompareDataRequest();
    	if(ccdr.readFrom(buffer)) {
    		CaptureCompareData data = ccdr.getData();
    		boolean firstTimeReceive = false;
			if(data.getCameraID() != null && !data.getCameraID().isEmpty()) {
	    		if(!deviceNo2Session.containsKey(data.getCameraID())) {
	    			deviceNo2Session.put(data.getCameraID(), session);
	    			firstTimeReceive = true;
	    		}
	    		else if(!session.equals(deviceNo2Session.get(data.getCameraID()))) {
	    			deviceNo2Session.get(data.getCameraID()).closeOnFlush();
	    			deviceNo2Session.put(data.getCameraID(), session);
	    			firstTimeReceive = true;
	    		}
			}
			if(data.getSn() != null && !data.getSn().isEmpty()) {
    			if(!sn2Session.containsKey(data.getSn())) {
    				sn2Session.put(data.getSn(), session);
	    			firstTimeReceive = true;
    			}
    			else if(!session.equals(sn2Session.get(data.getSn()))) {
    				sn2Session.get(data.getSn()).closeOnFlush();
    				sn2Session.put(data.getSn(), session);
	    			firstTimeReceive = true;
    			}
			}
			if(firstTimeReceive) {
				if(data.getCameraID() != null && !data.getCameraID().isEmpty()) {
		    		session.setAttribute(SESATTR_KEY_DEVICENO, data.getCameraID());
		    		if(config.connectStateInvokeCondition == ConnectStateInvokeCondition.DeviceNoKnown ||
		    				config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) {
		    			if(cameraConnectedEventHandler != null)
		    				cameraConnectedEventHandler.onCameraConnected(data.getCameraID());
		    		}
				}
				if(data.getSn() != null && !data.getSn().isEmpty()) {
	    			session.setAttribute(SESATTR_KEY_SN, data.getSn());
		    		if(config.connectStateInvokeCondition == ConnectStateInvokeCondition.SnKnown ||
		    				config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) {
		    			if(cameraConnectedEventHandler != null)
		    				cameraConnectedEventHandler.onCameraConnected(data.getSn());
		    		}
				}
				cachedThreadPool.execute(() -> {	    			
					try {
						sendDummy(session, MESSAGE_ID_HEARTBEAT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		});
			}
    		if(captureCompareDataReceivedEventHandler != null)
    			captureCompareDataReceivedEventHandler.onCaptureCompareDataReceived(data);
    		cachedThreadPool.execute(() -> {
	    		try {
					sendDataAck(session, ccdr);
				} catch (Exception e) {

				}
			});
    	}
    }
    
    @Override
    public void sessionClosed(IoSession session) throws Exception {
    	allSession.remove(session);
    	if(!session.containsAttribute(SESATTR_KEY_DEVICENO) && !session.containsAttribute(SESATTR_KEY_SN)
    			&& config.invokeConnectEventOnAnonymous) {
            if(cameraDisconnectedEventHandler != null)
                cameraDisconnectedEventHandler.onCameraDisconnected(((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress());
        }
		if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.DeviceNoKnown ||
				config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
				session.containsAttribute(SESATTR_KEY_DEVICENO)) {
        	String deviceNo = (String) session.getAttribute(SESATTR_KEY_DEVICENO);
        	deviceNo2Session.remove(deviceNo, session);
            if(cameraDisconnectedEventHandler != null)
                cameraDisconnectedEventHandler.onCameraDisconnected(deviceNo);
		}
		else if((config.connectStateInvokeCondition == ConnectStateInvokeCondition.SnKnown ||
				config.connectStateInvokeCondition == ConnectStateInvokeCondition.DevicenoOrSnKnown) &&
				session.containsAttribute(SESATTR_KEY_SN)) {
        	String sn = (String) session.getAttribute(SESATTR_KEY_SN);
        	sn2Session.remove(sn, session);
            if(cameraDisconnectedEventHandler != null)
                cameraDisconnectedEventHandler.onCameraDisconnected(sn);
		}
    }
    
    @Override
    public void sessionOpened(IoSession session) throws Exception {
    	allSession.add(session);
    	if(cameraConnectedEventHandler != null && config.invokeConnectEventOnAnonymous)
    		cameraConnectedEventHandler.onCameraConnected(((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress());
    }
    
    private void sendDummy(IoSession session, int messsageID) throws Exception {
    	new DummySerializable(messsageID).sendTo(session, config.sysType, config.majorProtocol, config.minorProtocol);
    }
    private void sendDataAck(IoSession session, CaptureCompareDataRequest ccdr) throws Exception {
    	IoBuffer buffer = IoBuffer.allocate(28);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(Util.createType(config.sysType, config.majorProtocol, config.minorProtocol, MESSAGE_ID_ACK));
        buffer.putInt(20);
        buffer.putInt(MESSAGE_ID_DATA);
        buffer.putInt(0);
        buffer.putUnsignedInt(ccdr.getData().getSequenceID());
        buffer.putUnsignedInt(ccdr._$timeSec);
        buffer.putUnsignedInt(ccdr._$timeUSec);
        buffer.flip();
        session.write(buffer).await(200, TimeUnit.MILLISECONDS); // FIXME 发送超时设置为200ms
    }
}
