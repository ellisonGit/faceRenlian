package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.NetConfig;
import main.java.com.ha.tlv.DeSerializeAdapter;

public class ReadNetConfigResponse extends DeSerializeAdapter {

	private NetConfig netConfig;
	
	public NetConfig getNetConfig() {
		return netConfig;
	}
	
	public ReadNetConfigResponse(int _ackCode) {
		super(_ackCode);
	}

	@Override
	public String readFrom(IoBuffer buffer) throws Exception {
		NetConfig ret = new NetConfig();
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 66) return i18nMessage("normal.dataLengthNotEnough");
		byte[] temp = new byte[18];
		buffer.get(temp);
		ret.setMac(new String(temp, "UTF-8").trim());
		temp = new byte[16];
		buffer.get(temp);
		ret.setIp(new String(temp, "UTF-8").trim());
		buffer.get(temp);
		ret.setNetmask(new String(temp, "UTF-8").trim());
		buffer.get(temp);
		ret.setGateway(new String(temp, "UTF-8").trim());
		netConfig = ret;
		return Constants.NOERROR;
	}

}
