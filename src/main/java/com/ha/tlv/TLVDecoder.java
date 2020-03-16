package main.java.com.ha.tlv;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.ByteOrder;

public final class TLVDecoder extends CumulativeProtocolDecoder {
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (in.remaining() >= 8) { // 前8字节是包头+长度
            // 标记当前position的快照标记mark，以便后继的reset操作能恢复position位置
            in.mark();

            in.order(ByteOrder.LITTLE_ENDIAN);
            in.skip(4);
            int len = in.getInt();
            if(len > 70 * 100 * 1024 || len < 0) { // 这个值是100个人员数据，理论上单个包的最大数据量
            	// 发现错误封包，丢掉所有数据
            	in.position(in.limit());
            	return false;
            }

            if (in.remaining() < len) {
                // 如果消息内容不够，则重置恢复position位置到操作前,进入下一轮, 接收新数据，以拼凑成完整数据
                in.reset();
                return false;
            } else {
                // 消息内容足够
                in.reset(); // 重置恢复position位置到操作前
                int totalLen = (int) (8 + len); // 总长 = 包头+包体

                byte[] packArr = new byte[totalLen];
                in.get(packArr, 0, totalLen);

                IoBuffer buffer = IoBuffer.allocate(totalLen);
                buffer.put(packArr);
                buffer.flip();
                out.write(buffer);
                buffer.free();

                if (in.remaining() > 0) { // 如果读取一个完整包内容后还粘了包，就让父类再调用一次，进行下一次解析
                    return true;
                }
            }
        }
        return false; // 处理成功，让父类进行接收下个包
    }
}
