package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import main.java.com.ha.facecamera.configserver.ConfigServer;
import main.java.com.ha.facecamera.configserver.ConfigServerConfig;
import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.Face;
import main.java.com.ha.tlv.SerializeAdapter;
import main.java.com.ha.tlv.Util;

public class AddFaceRequest extends SerializeAdapter {

	private Face face;

	public AddFaceRequest(Face _face) {
		super(Constants.MESSAGE_ID_ADDFACE);
		this.face = _face;
	}

	@Override
	public String sendTo(IoSession session, int sysType, int majorProtocol, int minorProtocol) throws Exception {
		/*if (session == null || face == null)
			return "POJO or Session is null";
		if(face.getTwistImageData() == null)
			return "field 'twisImageData' is null";
		if(face.getTwistImageData().length < 1)
			return "field 'twisImageData' is empty";*/
		for(int i = 0; i < face.getTwistImageData().length; ++i) {
			if(face.getTwistImageData()[i] == null || face.getTwistImageData()[i].length != Constants.TWIS_IMG_W * Constants.TWIS_IMG_H * 3)
				return i18nMessage("face.twisImage_i_WrongLength", i);
		}
		if (face.getId() == null || face.getId().isEmpty())
			return i18nMessage("face.idNotSet");
		if (face.getRole() != 0 && face.getRole() != 1 && face.getRole() != 2)
			return i18nMessage("face.roleIllegal");
		int imageDataLen = 4;
		if (face.getThumbImageData() != null && face.getThumbImageData().length > 0) {
			for (int i = 0; i < face.getThumbImageData().length; ++i) {
				if (face.getThumbImageData()[i] == null || face.getThumbImageData()[i].length == 0)
					return i18nMessage("face.thumbImage_i_Null", i);
				imageDataLen += 4;
				imageDataLen += 4;
				imageDataLen += face.getThumbImageData()[i].length;
			}
		}
		int twisDataLen = face.getTwistImageData().length * (Constants.TWIS_IMG_W * Constants.TWIS_IMG_W * 3 + 8);
		IoBuffer buffer = IoBuffer.allocate(80 + face.getFeatureCount() * face.getFeatureSize() * 4 + imageDataLen + twisDataLen);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(Util.createType(sysType, majorProtocol, minorProtocol, messageID));
		buffer.putInt(72 + face.getFeatureCount() * face.getFeatureSize() * 4 + imageDataLen + twisDataLen);
		byte[] temp = face.getId().getBytes("UTF-8");
		if (temp.length > 19)
			return i18nMessage("face.idStrLenTooLong");
		buffer.put(temp);
		buffer.put(new byte[20 - temp.length]);
		if (face.getName() == null || face.getName().isEmpty()) {
			buffer.put(new byte[16]);
		} else {
			temp = face.getName().getBytes("UTF-8");
			if (temp.length > 15)
				return i18nMessage("face.nameStrLenTooLong");
			buffer.put(temp);
			buffer.put(new byte[16 - temp.length]);
		}
		buffer.putInt(face.getRole());
		buffer.putShort(face.getFeatureCount());
		buffer.putShort(face.getFeatureSize());
		for (int i = 0; i < face.getFeatureCount(); ++i)
			for (int j = 0; j < face.getFeatureSize(); ++j)
				buffer.putFloat(face.getFeatureData()[i][j]);
		if(face.getStartDate() == Constants.DISABLED) {
			buffer.put(new byte[16]);
		} else {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC-8:00"));
			cal.set(1970, 0, 1, 0, 0, 0);
			buffer.putUnsignedInt((face.getStartDate().getTime() - cal.getTimeInMillis()) / 1000);
			buffer.put(new byte[12]);
		}
		if (imageDataLen == 4) {
			buffer.putInt(0);
		} else {
			buffer.putInt(face.getThumbImageData().length);
			for (int i = 0; i < face.getThumbImageData().length; ++i) {
				byte[] imageData = face.getThumbImageData()[i];
				buffer.putInt(imageData.length);
				buffer.put(new byte[] { 'j', 'p', 'g', 0 });
				buffer.put(imageData);
			}
		}
		buffer.putUnsignedInt(face.getWiegandNo());
		if(face.getExpireDate() == Constants.LONGLIVE) {
			buffer.putInt(-1);
		} else if(face.getExpireDate() == Constants.DISABLED) {
			buffer.putInt(0);
		} else {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC-8:00"));
			cal.set(1970, 0, 1, 0, 0, 0);
			buffer.putUnsignedInt((face.getExpireDate().getTime() - cal.getTimeInMillis()) / 1000);
		}
		buffer.putInt(face.getTwistImageData().length);
		for(int i = 0; i < face.getTwistImageData().length; ++i) {
			buffer.putInt(Constants.TWIS_IMG_W);
			buffer.putInt(Constants.TWIS_IMG_H);
			buffer.put(face.getTwistImageData()[i]);
		}
		buffer.flip();
		//System.out.print(messageID + "=>");
		//System.out.println(bytesToHexFun3(buffer.array()));

		WriteFuture wf = session.write(buffer);
		if(wf.await(buffer.capacity() / ((ConfigServerConfig) session.getAttribute(ConfigServer.SESATTR_KEY_CFG)).expectBandWidth + 1, TimeUnit.SECONDS) &&
				wf.isWritten())
			return Constants.NOERROR;
		else
			return Constants.SENDTIMEOUT;
	}
}
