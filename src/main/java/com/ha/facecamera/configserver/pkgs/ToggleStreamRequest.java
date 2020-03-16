package main.java.com.ha.facecamera.configserver.pkgs;

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

public class ToggleStreamRequest extends SerializeAdapter {
	private boolean pushOpen;
	
	public ToggleStreamRequest(boolean pushOpen) {
		super(Constants.MESSAGE_ID_STREAM);
		this.pushOpen = pushOpen;
	}

	@Override
	public String sendTo(IoSession session, int sysType, int majorProtocol, int minorProtocol) throws Exception {
		IoBuffer buffer = IoBuffer.allocate(9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(Util.createType(sysType, majorProtocol, minorProtocol, messageID));
        buffer.putInt(1);
        buffer.put((byte) (pushOpen?1:0));
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
