package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.Time;
import main.java.com.ha.tlv.DeSerializeAdapter;

/**
 * 读取设备时间响应包
 * 
 * @author 林星
 *
 */
public class ReadTimeResponse extends DeSerializeAdapter {

	private Time pojoTime;
	
	public Time getPojoTime() {
		return pojoTime;
	}
	
	public ReadTimeResponse(int _ackCode) {
		super(_ackCode);
	}

	@Override
	public String readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		Time ret = new Time();
		if(buffer.remaining() < 32) return i18nMessage("normal.dataLengthNotEnough");
		ret.setTimeZone(buffer.get());
		byte[] temp = new byte[11];
		buffer.get(temp);
		ret.setDate(new String(temp, "UTF-8").trim());
		temp = new byte[9];
		buffer.get(temp);
		ret.setTime(new String(temp, "UTF-8").trim());
		pojoTime = ret;
		return Constants.NOERROR;
	}

}
