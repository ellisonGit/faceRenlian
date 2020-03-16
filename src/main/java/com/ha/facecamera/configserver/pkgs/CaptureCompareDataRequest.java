package main.java.com.ha.facecamera.configserver.pkgs;

import java.awt.Rectangle;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.pojo.CaptureCompareData;
import main.java.com.ha.tlv.Util;

/**
 * 抓拍对比数据包
 * 
 * @author 林星
 *
 */
public final class CaptureCompareDataRequest {

	private CaptureCompareData data;
	public long _$timeSec, _$timeUSec; // 为了给sdk内部使用而保存的变量，由于数据断点续传功能支持需要原样返回微秒，为避免经由程序运算转存后的精度损失，因此保存原数据
	
	public CaptureCompareData getData() {
		return data;
	}
	
	public boolean readFrom(IoBuffer buffer) throws Exception {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 4) return false;
		CaptureCompareData _data = new CaptureCompareData();
		_data.setSequenceID(buffer.getUnsignedInt());
		if(buffer.remaining() < 32) return false;
		byte[] temp = new byte[32];
		buffer.get(temp);
		_data.setCameraID(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 32) return false;
		buffer.get(temp);
		_data.setAddrID(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 96) return false;
		temp = new byte[96];
		buffer.get(temp);
		_data.setAddrName(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 8) return false;
		long captimeSec = buffer.getUnsignedInt();
		_$timeSec = captimeSec;
		long captimeUSec = buffer.getUnsignedInt();
		_$timeUSec = captimeUSec;
		_data.setCaptureTime(Util.resolveUTCTime(captimeSec, captimeUSec));
		if(buffer.remaining() < 2) return false;
		_data.setRealtime(buffer.getShort() != 0);
		if(buffer.remaining() < 2) return false;
		short matchScore = buffer.getShort();
		if(matchScore > 0) {
			_data.setPersonMatched(true);
			_data.setMatchScore(matchScore);
			if(buffer.remaining() < 40) return false;
			temp = new byte[20];
			buffer.get(temp);
			_data.setPersonID(new String(temp, "UTF-8").trim());
			temp = new byte[16];
			buffer.get(temp);
			_data.setPersonName(new String(temp, "UTF-8").trim());
			_data.setPersonRole(buffer.getInt());
		}
		if(buffer.remaining() < 4) return false;
		int environmentImgFlag = buffer.getInt();
		int environmentImgSize = 0;
		if(environmentImgFlag != 0) {
			if(buffer.remaining() < 16) return false;
			buffer.getInt(); // 跳过4个字节jpg
			environmentImgSize = buffer.getInt();
			_data.setFaceRegionInEnvironment(new Rectangle(buffer.getUnsignedShort(), buffer.getUnsignedShort(), buffer.getUnsignedShort(), buffer.getUnsignedShort()));
		}
		if(buffer.remaining() < 4) return false;
		int featureImgFlag = buffer.getInt();
		int featureImgSize = 0;
		if(featureImgFlag != 0) {
			if(buffer.remaining() < 16) return false;
			buffer.getInt(); // 跳过4个字节jpg
			featureImgSize = buffer.getInt();
			_data.setFaceRegionInFeature(new Rectangle(buffer.getUnsignedShort(), buffer.getUnsignedShort(), buffer.getUnsignedShort(), buffer.getUnsignedShort()));
		}
		if(buffer.remaining() < 4) return false;
		int videoFlag = buffer.getInt();
		int videoSize = 0;
		if(videoFlag != 0) {
			if(buffer.remaining() < 24) return false;
			long startTimeSec = buffer.getUnsignedInt();
			long startTimeUSec = buffer.getUnsignedInt();
			_data.setVideoStartTime(Util.resolveUTCTime(startTimeSec, startTimeUSec));
			long endTimeSec = buffer.getUnsignedInt();
			long endTimeUSec = buffer.getUnsignedInt();
			_data.setVideoEndTime(Util.resolveUTCTime(endTimeSec, endTimeUSec));
			buffer.getInt(); // 跳过4个字节mp4
			videoSize = buffer.getInt();
		}
		if(buffer.remaining() < 128) return false;
		_data.setSex(buffer.get());
		_data.setAge(buffer.get());
		buffer.get(new byte[2]);
		_data.setqValue(buffer.get());
		buffer.skip(3);
		_data.setSafetyHatColor(buffer.get());
		buffer.get(new byte[119]);
		if(environmentImgFlag != 0) {
			if(buffer.remaining() < environmentImgSize) return false;
			_data.setEnvironmentImageData(new byte[environmentImgSize]);
			buffer.get(_data.getEnvironmentImageData());
		}
		if(featureImgFlag != 0) {
			if(buffer.remaining() < featureImgSize) return false;
			_data.setFeatureImageData(new byte[featureImgSize]);
			buffer.get(_data.getFeatureImageData());
		}
		if(videoFlag != 0) {
			if(buffer.remaining() < videoSize) return false;
			_data.setVideoData(new byte[videoSize]);
			buffer.get(_data.getVideoData());
		}
		this.data = _data;
		if(buffer.remaining() < 4) return true; // 兼容旧协议，没有特征值回传
		int featureCount = buffer.getInt();
		if(featureCount > 0) {
			if(buffer.remaining() < featureCount * 4) return false;
			_data.setFeatureData(new float[featureCount]);
			for(int i = 0; i < featureCount; ++i) {
				_data.getFeatureData()[i] = buffer.getFloat();
			}
		}
		if(buffer.remaining() < 4) return true;
		int modelImgSize = buffer.getInt();
		if(modelImgSize > 0) {
			if(buffer.remaining() < 4 + modelImgSize) return false;
			buffer.getInt(); // 跳过4字节jpg
			_data.setModelImageData(new byte[modelImgSize]);
			buffer.get(_data.getModelImageData());
		}
		if(environmentImgFlag != 0) {
			if(buffer.remaining() < 20)
				return true;
			buffer.skip(20);
		}
		if(featureImgFlag != 0) {
			if(buffer.remaining() < 20)
				return true;
			buffer.skip(20);
		}
		if(buffer.remaining() < 32) return true;
		temp = new byte[32];
		buffer.get(temp);
		data.setSn(new String(temp, "UTF-8").trim());
		if(buffer.remaining() < 4) return true; // 旧版协议无身份证信息
		if(buffer.getInt() != 1) return true; // 无身份证信息
		if(buffer.remaining() < 296) return true;
		temp = new byte[36];
		buffer.get(temp);
		data.setIdCardNo(new String(temp, StandardCharsets.UTF_8).trim());
		temp = new byte[43];
		buffer.get(temp);
		data.setIdCardName(new String(temp, StandardCharsets.UTF_8).trim());
		temp = new byte[17];
		buffer.get(temp);
		data.setIdCardBirth(new String(temp, StandardCharsets.UTF_8).trim());
		data.setIdCardSex(buffer.get());
		temp = new byte[19];
		buffer.get(temp);
		data.setIdCardNation(new String(temp, StandardCharsets.UTF_8).trim());
		temp = new byte[103];
		buffer.get(temp);
		data.setIdCardAddress(new String(temp, StandardCharsets.UTF_8).trim());
		temp = new byte[43];
		buffer.get(temp);
		data.setIdCardOrg(new String(temp, StandardCharsets.UTF_8).trim());
		temp = new byte[17];
		buffer.get(temp);
		data.setIdCardStartDate(new String(temp, StandardCharsets.UTF_8).trim());
		temp = new byte[17];
		buffer.get(temp);
		data.setIdCardExpireDate(new String(temp, StandardCharsets.UTF_8).trim());
		return true;
	}
}
