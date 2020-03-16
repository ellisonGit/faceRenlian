package main.java.com.ha.tlv;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 可反序列化的数据
 * <br>
 * 即可从数据链路接收并解析的数据
 * 
 * @author 林星
 *
 */
public abstract class DeSerializeAdapter {

	protected int messageID;
	protected int ackCode;
	protected Object tag;
	
	public DeSerializeAdapter(int _ackCode) {
		ackCode = _ackCode;
	}
	
	/**
	 * 获取数据包消息类型
	 * 
	 * @return 消息类型
	 */
	public int getMessageID() {
		return messageID;
	}
	/**
	 * 获取数据包应答码
	 * 
	 * @return 应答码
	 */
	public int getACKCode() {
		return ackCode;
	}
	
	/**
	 * 获取附加数据
	 * 
	 * @return 附加数据
	 */
	public Object getTag() {
		return tag;
	}
	/**
	 * 设置附加数据
	 * 
	 * @param tag 附加数据
	 */
	public void setTag(Object tag) {
		this.tag = tag;
	}

	/**
	 * 从数据缓冲区读取一个结构化数据
	 * <br>
	 * 数据会填充到自身属性
	 * 
	 * @param buffer 缓冲区
	 * @return 读取过程发现的错误
	 * @throws Exception 
	 */
	public abstract String readFrom(IoBuffer buffer) throws Exception;
}
