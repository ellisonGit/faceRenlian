package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;

public class StreamDataResponse {

	private byte[] h264Segment;
	
	public byte[] getH264Segment() {
		return h264Segment;
	}

	public String readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 4) return i18nMessage("normal.dataLengthNotEnough");
		int type = buffer.getInt();
		if(type != 2) return i18nMessage("stream.typeIllegal");
		if(buffer.remaining() < 16) return i18nMessage("normal.dataLengthNotEnough");
		@SuppressWarnings("unused")
		int width = buffer.getUnsignedShort();
		@SuppressWarnings("unused")
		int height = buffer.getUnsignedShort();
		@SuppressWarnings("unused")
		int seq_no0 = buffer.getInt();
		@SuppressWarnings("unused")
		int seq_no1 = buffer.getInt();
		int bufferSize = (int)buffer.getUnsignedInt();
		if(buffer.remaining() < bufferSize) return i18nMessage("normal.dataLengthNotEnough");
		// 做个最大判定，不能太大了！
		if(bufferSize > 1024 * 1024) return i18nMessage("stream.dataTooLong");
		h264Segment = new byte[bufferSize];
		buffer.get(h264Segment);
		return Constants.NOERROR;
	}

}
