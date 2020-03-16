package main.java.com.ha.facecamera.configserver.pkgs;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.pojo.RegData;
import main.java.com.ha.tlv.Util;

public class RegRequest {

	private RegData data;
	
	public RegData getData() {
		return data;
	}
	
	public boolean readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 4) return false;
		RegData _data = new RegData();
		_data.setTime(Util.resolveUTCTime(buffer.getUnsignedInt(), 0));
		if(buffer.remaining() < 15) return false;
		byte[] temp = new byte[16];
		buffer.get(temp, 0, 15);
		_data.setUserName(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 16) return false;
		temp = new byte[16];
		buffer.get(temp);
		_data.setPassword(temp);
		data = _data;
		return true;
	}
}
