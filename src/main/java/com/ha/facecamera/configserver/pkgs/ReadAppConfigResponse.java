package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.AppConfig;
import main.java.com.ha.tlv.DeSerializeAdapter;

public class ReadAppConfigResponse extends DeSerializeAdapter {
	
	private AppConfig appConfig;
	
	public AppConfig getAppConfig() {
		return appConfig;
	}

	public ReadAppConfigResponse(int _ackCode) {
		super(_ackCode);
	}

	@Override
	public String readFrom(IoBuffer buffer) throws Exception {
		Constructor<AppConfig> ctor = AppConfig.class.getDeclaredConstructor();
		ctor.setAccessible(true);
		AppConfig ret = ctor.newInstance();
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if(buffer.remaining() < 400) return i18nMessage("normal.dataLengthNotEnough");
		byte[] temp = new byte[32];
		buffer.get(temp);
		ret.setDeviceNo(new String(temp, "UTF-8").trim());
		buffer.get(temp);
		ret.setAddrNo(new String(temp, "UTF-8").trim());
		temp = new byte[96];
		buffer.get(temp);
		ret.setAddrName(new String(temp, "UTF-8").trim());
		temp = new byte[32];
		buffer.get(temp);
		ret.setResv1(temp);
		ret.setHeartBeatInterval(buffer.get());
		byte cloundConfigEnable = buffer.get();		
		if(cloundConfigEnable == 0) {
			ret.setCloundConfigEnable(false);
			buffer.get(new byte[18]);
		} else {
			ret.setCloundConfigEnable(true);
			ret.setCloundConfigPort(buffer.getShort());
			temp = new byte[16];
			buffer.get(temp);
			ret.setCloundConfigIp(new String(temp, "UTF-8").trim());
		}
		byte doReg = buffer.get();
		if(doReg == 0) {
			ret.setDoReg(false);
			buffer.get(new byte[31]);
		} else {
			ret.setDoReg(true);
			temp = new byte[16];
			buffer.get(temp, 0, 15);
			ret.setRegUserName(new String(temp, "UTF-8").trim());
			temp = new byte[17];
			buffer.get(temp, 0, 16);
			ret.setRegPassword(new String(temp, "UTF-8").trim());
		}
		temp = new byte[12];
		buffer.get(temp);
		ret.setResv2(temp);
		ret.setDataUploadMethod(buffer.getShort());
		if(ret.getDataUploadMethod() != 0) {
			ret.setDataUploadPort(buffer.getShort());
			temp = new byte[16];
			buffer.get(temp);
			ret.setDataUploadServer(new String(temp, "UTF-8").trim());
			temp = new byte[102];
			buffer.get(temp);
			if(ret.getDataUploadMethod() == 3)
				ret.setDataUploadUrl(new String(temp, "UTF-8").trim());
			else if(ret.getDataUploadMethod() == 2) {
				// FTP
				byte[] temp1 = new byte[15];
				System.arraycopy(temp, 0, temp1, 0, 15);
				ret.setFtpUserName(new String(temp1, "UTF-8").trim());
				System.arraycopy(temp, 15, temp1, 0, 15);
				ret.setFtpPassword(new String(temp1, "UTF-8").trim());
				ret.setDataUploadUrl(new String(temp, 30, 70, "UTF-8").trim());
			}
		}
		byte workMode = buffer.get();
		if(workMode == 1 || workMode == 2 || workMode == 3)
			ret.setWorkMode(workMode);
		temp = new byte[17];
		buffer.get(temp);
		ret.setResv3(temp);
		int compareSwitcher = buffer.getInt();
		ret.setResv5(compareSwitcher);
		ret.setCompareSwitch((compareSwitcher & 1) == 1);
		ret.setAgeDetect((compareSwitcher & 2) == 2);
		ret.setSexDetect((compareSwitcher & 4) == 4);
		ret.setLivingDetect((compareSwitcher & 32) == 32);
		ret.setSafetyHatDetect((compareSwitcher & 64) == 64);
		if(ret.isCompareSwitch()) {
			if(buffer.remaining() < 4) return i18nMessage("normal.dataLengthNotEnough");
			ret.setEnsureThreshold(buffer.getInt());
		}
		if(buffer.remaining() < 10) return i18nMessage("normal.dataLengthNotEnough");
		ret.setRepeatFilter(buffer.getInt() == 1);
		ret.setRepeatFilterTime(buffer.getInt());
		short uploadMethod = buffer.getShort();
		ret.setResv6(uploadMethod);
		ret.setUploadEnvironmentImage((uploadMethod & 0b1) == 0b1);
		ret.setUploadFeatureImage((uploadMethod & 0b10) == 0b10);
		ret.setUploadPhoto((uploadMethod & 4) == 4);
		ret.setUploadFeature((uploadMethod & 8) == 8);
		if(buffer.remaining() > 0) {
			temp = new byte[buffer.remaining()];
			buffer.get(temp);
			ret.setResv4(temp);
		}
		appConfig = ret;
		return Constants.NOERROR;
	}
}
