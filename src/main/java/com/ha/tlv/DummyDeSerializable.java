package main.java.com.ha.tlv;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;

public class DummyDeSerializable extends DeSerializeAdapter {

	public DummyDeSerializable(int _ackCode) {
		super(_ackCode);
	}

	@Override
	public String readFrom(IoBuffer buffer) throws Exception {
		return Constants.NOERROR;
	}

}
