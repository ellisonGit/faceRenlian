package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.tlv.DeSerializeAdapter;

/**
 * 获取所有人员编号响应包
 * 
 * @author 林星
 *
 */
public class GetAllPersonIDResponse extends DeSerializeAdapter {

	private String[] personIds;
	
	public String[] getPersonIDs() {
		return personIds;
	}
	
	public GetAllPersonIDResponse(int _ackCode) {
		super(_ackCode);
	}

	@Override
	public String readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 4) return i18nMessage("normal.dataLengthNotEnough");
		int count = buffer.getInt();
		if(count < 1) {
			personIds = new String[0];
			return Constants.NOERROR;
		}
		if(buffer.remaining() < count * 20) return i18nMessage("normal.dataLengthNotEnough");
		personIds = new String[count];
		byte[] temp = new byte[20];
		for(int i = 0; i < count; ++i) {
			buffer.get(temp);
			personIds[i] = new String(temp, "UTF-8").trim();
		}
		return Constants.NOERROR;
	}

}
