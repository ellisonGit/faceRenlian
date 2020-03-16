package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import main.java.com.ha.facecamera.configserver.ConfigServer;
import main.java.com.ha.facecamera.configserver.ConfigServerConfig;
import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.tlv.SerializeAdapter;
import main.java.com.ha.tlv.Util;

public class DeleteFaceRequest extends SerializeAdapter {

	private int role = -1;
	private String id;
	
	public DeleteFaceRequest() {
		super(Constants.MESSAGE_ID_DELETEFACE);
	}

	public void setRole(int _role) {
		this.role = _role;
	}
	public void setID(String _id) {
		this.id = _id;
	}
	
	@Override
	public String sendTo(IoSession session, int sysType, int majorProtocol, int minorProtocol) throws Exception {
		if(role != -1 && role != 0 && role != 1 && role != 2 && role != 3) return i18nMessage("face.roleIllegal");
		if((id == null || id.isEmpty()) && role == -1) return i18nMessage("face.delete.idAndRoleCombineIllegal");
		IoBuffer buffer = IoBuffer.allocate(32);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(Util.createType(sysType, majorProtocol, minorProtocol, messageID));
        buffer.putInt(24);
        buffer.putInt(role);
        if(id == null || id.isEmpty())
        	buffer.put(new byte[20]);
        else {
        	byte[] temp = id.getBytes("UTF-8");
        	if(temp.length > 19) return i18nMessage("face.idStrLenTooLong");
        	buffer.put(temp);
        	buffer.put(new byte[20 - temp.length]);
        }
        buffer.flip();
		//System.out.print(messageID + "=>");
		//System.out.println(bytesToHexFun3(buffer.array()));

		WriteFuture wf = session.write(buffer);
		if(wf.await(buffer.capacity() / ((ConfigServerConfig) session.getAttribute(ConfigServer.SESATTR_KEY_CFG)).expectBandWidth + 1, TimeUnit.SECONDS) &&
				wf.isWritten())
			return Constants.NOERROR;
		else
			return Constants.SENDTIMEOUT;
	}
}
