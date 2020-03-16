package main.java.com.ha.facecamera.configserver.pkgs;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

public class FaceDeleteProgressInfoRequest {

	private int _total;
	public int getTotal() {
		return _total;
	}
	private int _cur;
	public int getCurrent() {
		return _cur;
	}
	private String _pid;
	public String getPersonID() {
		return _pid;
	}
	
	public boolean readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 28) return false;
		_total = buffer.getInt();
		_cur = buffer.getInt();
		byte[] temp = new byte[20];
		buffer.get(temp);
		_pid = new String(temp, "UTF-8").trim();
		return true;
	}
}
