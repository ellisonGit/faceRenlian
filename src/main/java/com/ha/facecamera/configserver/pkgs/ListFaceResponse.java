package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.Face;
import main.java.com.ha.tlv.DeSerializeAdapter;
import main.java.com.ha.tlv.Util;

public class ListFaceResponse extends DeSerializeAdapter {

	private int total;
	private int index;
	private Face face;
	
	/**
	 * 获取总数
	 * 
	 * @return 总数
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * 获取当前记录索引
	 * 
	 * @return 记录索引
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * 获取当前传输的特征值数据
	 * 
	 * @return 特征值数据
	 */
	public Face getFace() {
		return face;
	}

	public ListFaceResponse(int _ackCode) {
		super(_ackCode);
	}

	@Override
	public String readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 8) return i18nMessage("normal.dataLengthNotEnough");
		total = buffer.getInt();
		index = buffer.getInt();
		if(index == 0) return Constants.NOERROR;
		if(buffer.remaining() < 48) return i18nMessage("normal.dataLengthNotEnough");
		byte[] temp = new byte[20];
		buffer.get(temp);
		Face face = new Face();
		face.setId(new String(temp, "UTF-8").trim());
		temp = new byte[16];
		buffer.get(temp);
		face.setName(new String(temp, "UTF-8").trim());
		face.setRole(buffer.getInt());
		face.setFeatureCount(buffer.getShort());
		face.setFeatureSize(buffer.getShort());
		this.face = face;
		if(face.getFeatureCount() > 0) {
			if(face.getFeatureSize() != 0) {
				if(buffer.remaining() < face.getFeatureCount() * face.getFeatureSize() * 4) return i18nMessage("normal.dataLengthNotEnough");
				face.setFeatureData(new float[face.getFeatureCount()][]);
				for(int i = 0; i < face.getFeatureCount(); ++i) {
					face.getFeatureData()[i] = new float[face.getFeatureSize()];
					for(int j = 0; j < face.getFeatureSize(); ++j)
						face.getFeatureData()[i][j] = buffer.getFloat();
				}
			}
		}
		if(buffer.remaining() < 4) return i18nMessage("normal.dataLengthNotEnough");
		int imageCount = buffer.getInt();
		if(imageCount > 0) {
			byte[][] imageData = new byte[imageCount][];
			for(int i = 0; i < imageCount; ++i) {
				if(buffer.remaining() < 4) return i18nMessage("normal.dataLengthNotEnough");
				int imageDataLen = buffer.getInt();
				if(imageDataLen < 0) return i18nMessage("face.list.response.thumbImageDataLen_i_Illegal", i);
				if(buffer.remaining() < imageDataLen) return i18nMessage("normal.dataLengthNotEnough");
				imageData[i] = new byte[imageDataLen];
				buffer.get(imageData[i]);
			}
			face.setThumbImageData(imageData);
		}
		if(buffer.remaining() < 24) return Constants.NOERROR; // 兼容旧协议，没有韦根和期效的固件版本
		buffer.get(new byte[16]);
		face.setWiegandNo(buffer.getUnsignedInt());
		int expireFlag = buffer.getInt();
		if(expireFlag == -1)
			face.setExpireDate(Constants.LONGLIVE);
		else if(expireFlag == 0)
			face.setExpireDate(Constants.DISABLED);
		else {
			face.setExpireDate(Util.resolveUTCTime(expireFlag, 0));
		}
		int startFlag = buffer.getInt();
		if(startFlag == -1)
			face.setStartDate(Constants.LONGLIVE);
		else if(startFlag == 0)
			face.setStartDate(Constants.DISABLED);
		else {
			face.setStartDate(Util.resolveUTCTime(startFlag, 0));
		}
		if(buffer.remaining() < 6) return Constants.NOERROR; // 兼容旧协议，没有归一化图
		buffer.get(new byte[4]);
		int twistImageLen = buffer.getInt();
		if(twistImageLen == 0) return Constants.NOERROR;
		if(twistImageLen < 1 || twistImageLen > 5) return i18nMessage("face.list.response.twistImageCountIllegal");
		byte[][] twistImageData = new byte[twistImageLen][];
		for(int i = 0; i < twistImageLen; ++i) {
			short tw = buffer.getShort();
			short th = buffer.getShort();
			// FIXME
			if(tw != Constants.TWIS_IMG_W)
				tw = Constants.TWIS_IMG_W;
			if(th != Constants.TWIS_IMG_H)
				th = Constants.TWIS_IMG_H;
			int tchannel = buffer.getInt();
			if(tchannel != 3) return i18nMessage("face.list.response.twistImageChannel_i_Illegal", i);
			twistImageData[i] = new byte[tw * th * tchannel];
			if(buffer.remaining() < tw * th * tchannel) return i18nMessage("normal.dataLengthNotEnough");
			buffer.get(twistImageData[i]);
		}
		face.setTwistImageData(twistImageData);
		return Constants.NOERROR;
	}

}
