package main.java.com.ha.tlv;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import main.java.com.ha.facecamera.configserver.ConfigServer;
import main.java.com.ha.facecamera.configserver.ConfigServerConfig;
import main.java.com.ha.facecamera.configserver.Constants;

/**
 * 默认可发送数据实现
 * 
 * @author 林星
 *
 */
public abstract class SerializeAdapter {

	protected int messageID;
	
	/**
	 * 数据消息类型
	 * 
	 * @param _messageID 消息类型
	 */
	protected SerializeAdapter(int _messageID) {
		this.messageID = _messageID;
	}
	
	public String sendTo(IoSession session, int sysType, int majorProtocol, int minorProtocol) throws Exception {
		IoBuffer buffer = IoBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(Util.createType(sysType, majorProtocol, minorProtocol, messageID));
        buffer.putInt(0);
        buffer.flip();
		//System.out.print(messageID + "=>");
		//System.out.println(bytesToHexFun3(buffer.array()));
		WriteFuture wf = session.write(buffer);
        if(session.containsAttribute(ConfigServer.SESATTR_KEY_CFG)) {
			if(wf.await(buffer.capacity() / ((ConfigServerConfig) session.getAttribute(ConfigServer.SESATTR_KEY_CFG)).expectBandWidth + 1, TimeUnit.SECONDS) &&
					wf.isWritten())
				return Constants.NOERROR;
			else
				return Constants.SENDTIMEOUT;
        }
		return Constants.NOERROR;
	}
	
	/*public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) {
            buf.append(String.format("%02X ", new Integer(b & 0xff)));
        }

        return buf.toString();
    }*/
}
