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
import main.java.com.ha.facecamera.configserver.pojo.Time;
import main.java.com.ha.tlv.SerializeAdapter;
import main.java.com.ha.tlv.Util;

public class SetTimeRequest extends SerializeAdapter {	
	private Time pojoTime;

	public SetTimeRequest(Time time) {
		super(Constants.MESSAGE_ID_TIME_SET);
		this.pojoTime = time;
	}

	@Override
	public String sendTo(IoSession session, int sysType, int majorProtocol, int minorProtocol) throws Exception {
		//if(pojoTime == null || session == null) return "POJO or Session is null";
        if(pojoTime.getDate() == null || pojoTime.getDate().isEmpty()) return i18nMessage("time.dateNotSet");
        if(pojoTime.getTime() == null || pojoTime.getTime().isEmpty()) return i18nMessage("time.timeNotSet");
		IoBuffer buffer = IoBuffer.allocate(40);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(Util.createType(sysType, majorProtocol, minorProtocol, messageID));
        buffer.putInt(32);
        buffer.put(pojoTime.getTimeZone());
        byte[] temp = pojoTime.getDate().getBytes("UTF-8");
        if(temp.length > 10) return i18nMessage("time.dateStrLenTooLong");
        buffer.put(temp);
        buffer.put(new byte[11 - temp.length]);
        temp = pojoTime.getTime().getBytes("UTF-8");
        if(temp.length > 8) return i18nMessage("time.timeStrLenTooLong");
        buffer.put(temp);
        buffer.put(new byte[9 - temp.length]);
        buffer.put(new byte[11]);
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
