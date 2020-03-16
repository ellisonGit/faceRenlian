package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.Version;
import main.java.com.ha.tlv.DeSerializeAdapter;

/**
 * 读取设备版本信息数据响应包
 * 
 * @author 林星
 *
 */
public final class ReadVersionResponse extends DeSerializeAdapter {

	private Version pojoVersion;
	
	public Version getVersion() {
		return pojoVersion;
	}
	
	public ReadVersionResponse(int _ackCode) {
		super(_ackCode);
	}

	@Override
	public String readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		Version ret = new Version();
		if(buffer.remaining() < 100) return i18nMessage("normal.dataLengthNotEnough");
		byte[] temp = new byte[32];
		buffer.get(temp);
		ret.setSn(new String(temp, "UTF-8").trim());
		temp = new byte[8];
		buffer.get(temp);
		ret.setProtocolVersion(new String(temp, "UTF-8").trim());
		temp = new byte[16];
		buffer.get(temp);
		ret.setFirewareVersion(new String(temp, "UTF-8").trim());
		temp = new byte[8];
		buffer.get(temp);
		ret.setCodeVersion(new String(temp, "UTF-8").trim());
		temp = new byte[20];
		buffer.get(temp);
		ret.setBuildDate(new String(temp, "UTF-8").trim());
		pojoVersion = ret;
		if(buffer.remaining() < 32) return Constants.NOERROR;
		buffer.skip(16); // 16字节保留字段
		temp = new byte[16];
		buffer.get(temp);
		ret.setSysType(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 16) return Constants.NOERROR;
		buffer.get(temp);
		ret.setHardwarePlatform(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 16) return Constants.NOERROR;
		buffer.get(temp);
		ret.setSensorModel(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 16) return Constants.NOERROR;
		buffer.get(temp);
		ret.setAlgorithmVersion(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 16) return Constants.NOERROR;
		buffer.get(temp);
		ret.setSdkMinVersion(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 4) return Constants.NOERROR;
		ret.setFaceDemoMinVersion(buffer.getUnsignedInt());
		return Constants.NOERROR;
	}

}
