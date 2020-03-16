package main.java.com.ha.tlv;

/**
 * 
 * 使用本类可发送一个无Value字段的封包，一般用于发送心跳包等
 * 
 * @author 林星
 *
 */
public class DummySerializable extends SerializeAdapter {

	public DummySerializable(int _messageID) {
		super(_messageID);
	}

}
