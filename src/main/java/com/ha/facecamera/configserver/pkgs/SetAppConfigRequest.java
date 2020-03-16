package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import main.java.com.ha.facecamera.configserver.ConfigServer;
import main.java.com.ha.facecamera.configserver.ConfigServerConfig;
import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.AppConfig;
import main.java.com.ha.tlv.SerializeAdapter;
import main.java.com.ha.tlv.Util;

public class SetAppConfigRequest extends SerializeAdapter {

	private AppConfig appConfig;
	
	public SetAppConfigRequest(AppConfig _appConfig) {
		super(Constants.MESSAGE_ID_APPCONFIG_SET);
		appConfig = _appConfig;
	}

	@Override
	public String sendTo(IoSession session, int sysType, int majorProtocol, int minorProtocol) throws Exception {
		//if(appConfig == null || session == null) return "POJO or Session is null";
		int len = 414;
		if(!appConfig.isCompareSwitch())
			len -= 4;
		if(appConfig.getDataUploadMethod() == 0)
			len -= 120;
		if(appConfig.getResv4() != null)
			len += appConfig.getResv4().length;
		IoBuffer buffer = IoBuffer.allocate(len + 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(Util.createType(sysType, majorProtocol, minorProtocol, messageID));
        buffer.putInt(len);
        byte[] temp = null;
        if(appConfig.getDeviceNo() == null || appConfig.getDeviceNo().isEmpty()) {
        	buffer.put(new byte[32]);
        } else {
	        temp = appConfig.getDeviceNo().getBytes("UTF-8");
	        if(temp.length > 31) return i18nMessage("cfg.deviceNoStrLenTooLong");
	        buffer.put(temp);
	        buffer.put(new byte[32 - temp.length]);
        }
        if(appConfig.getAddrNo() == null || appConfig.getAddrNo().isEmpty()) {
        	buffer.put(new byte[32]);
        } else {
        	temp = appConfig.getAddrNo().getBytes("UTF-8");
        	if(temp.length > 31) return i18nMessage("cfg.addrNoStrLenTooLong");
	        buffer.put(temp);
	        buffer.put(new byte[32 - temp.length]);
        }
        if(appConfig.getAddrName() == null || appConfig.getAddrName().isEmpty()) {
        	buffer.put(new byte[96]);
        } else {
        	temp = appConfig.getAddrName().getBytes("UTF-8");
        	if(temp.length > 95) return i18nMessage("cfg.addrNameStrLenTooLong");
        	buffer.put(temp);
        	buffer.put(new byte[96 - temp.length]);
        }
        buffer.put(appConfig.getResv1());
        buffer.put((byte)appConfig.getHeartBeatInterval());
        if(!appConfig.isCloundConfigEnable())
        	buffer.put(new byte[19]);
        else {
        	buffer.put((byte) 1);
        	buffer.putShort(appConfig.getCloundConfigPort());
        	temp = appConfig.getCloundConfigIp().getBytes("UTF-8");
        	if(temp.length > 15) return i18nMessage("cfg.cloudConfigServerIpStrLenTooLong");
        	buffer.put(temp);
        	buffer.put(new byte[16 - temp.length]);
        }
        if(!appConfig.isDoReg()) {
        	buffer.put(new byte[32]);
        } else {
        	buffer.put((byte) 1);
        	temp = appConfig.getRegUserName().getBytes("UTF-8");
        	if(temp.length > 15) return i18nMessage("cfg.regUsernameStrLenTooLong");
        	buffer.put(temp);
        	buffer.put(new byte[15 - temp.length]);
        	temp = appConfig.getRegPassword().getBytes("UTF-8");
        	if(temp.length > 16) return i18nMessage("cfg.regPasswordStrLenTooLong");
        	buffer.put(temp);
        	buffer.put(new byte[16 - temp.length]);
        }
    	buffer.put(appConfig.getResv2());
        buffer.putShort(appConfig.getDataUploadMethod());
        if(appConfig.getDataUploadMethod() != 0) {
        	buffer.putShort(appConfig.getDataUploadPort());
        	if(appConfig.getDataUploadServer() == null || appConfig.getDataUploadServer().isEmpty()) return i18nMessage("cfg.dataUploadServerIPNotSet");
        	temp = appConfig.getDataUploadServer().getBytes("UTF-8");
        	if(temp.length > 15) return i18nMessage("cfg.dataUploadServerIPStrLenTooLong");
        	buffer.put(temp);
        	buffer.put(new byte[16 - temp.length]);
        	if(appConfig.getDataUploadMethod() == 3) {
        		if(appConfig.getDataUploadUrl() == null || appConfig.getDataUploadUrl().isEmpty())
        			buffer.put(new byte[102]);
        		else {
        			temp = appConfig.getDataUploadUrl().getBytes("UTF-8");
        			if(temp.length > 101) return i18nMessage("cfg.dataUploadUrlStrLenTooLong");
        			buffer.put(temp);
        			buffer.put(new byte[102 - temp.length]);
        		}
        	} else if(appConfig.getDataUploadMethod() == 2) {
        		// FTP
        		temp = appConfig.getFtpUserName().getBytes("UTF-8");
    			if(temp.length > 14) return i18nMessage("cfg.ftpUsernameStrLenTooLong");
    			buffer.put(temp);
    			buffer.put(new byte[15 - temp.length]);
        		temp = appConfig.getFtpPassword().getBytes("UTF-8");
    			if(temp.length > 14) return i18nMessage("cfg.ftpPasswordStrLenTooLong");
    			buffer.put(temp);
    			buffer.put(new byte[15 - temp.length]);
    			temp = appConfig.getDataUploadUrl().getBytes("UTF-8");
    			if(temp.length > 69) return i18nMessage("cfg.dataUploadUrlStrLenTooLong");
    			buffer.put(temp);
    			buffer.put(new byte[70 - temp.length]);
    			buffer.put(new byte[2]);
        	} else {
        		buffer.put(new byte[102]);
        	}
        }
        buffer.put(appConfig.getWorkMode());
        buffer.put(appConfig.getResv3());
        int compareSwitcher = appConfig.getResv5();
        if(appConfig.isCompareSwitch())
        	compareSwitcher |= 0b1;
        else
        	compareSwitcher &= 0b1111_1111_1111_1111_1111_1111_1111_1110;
        if(appConfig.isAgeDetect())
        	compareSwitcher |= 0b10;
        else
        	compareSwitcher &= 0b1111_1111_1111_1111_1111_1111_1111_1101;
        if(appConfig.isSexDetect())
        	compareSwitcher |= 0b100;
        else
        	compareSwitcher &= 0b1111_1111_1111_1111_1111_1111_1111_1011;
        if(appConfig.isLivingDetect())
        	compareSwitcher |= 0b100000;
        else
        	compareSwitcher &= 0b1111_1111_1111_1111_1111_1111_1101_1111;
        if(appConfig.isSafetyHatDetect())
        	compareSwitcher |= 0b1000000;
        else
        	compareSwitcher &= 0b1111_1111_1111_1111_1111_1111_1011_1111;
        buffer.putInt(compareSwitcher);
        if(appConfig.isCompareSwitch())
        	buffer.putInt(appConfig.getEnsureThreshold());
        buffer.putInt(appConfig.isRepeatFilter() ? 1 : 0);
        buffer.putInt(appConfig.getRepeatFilterTime());
        short uploadMethod = appConfig.getResv6();
        if(appConfig.isUploadEnvironmentImage())
        	uploadMethod |= 0b1;
        else
        	uploadMethod &= 0b1111_1111_1111_1110;
        if(appConfig.isUploadFeatureImage())
        	uploadMethod |= 0b10;
	    else
	    	uploadMethod &= 0b1111_1111_1111_1101;
        if(appConfig.isUploadPhoto())
        	uploadMethod |= 0b100;
	    else
	    	uploadMethod &= 0b1111_1111_1111_1011;
        if(appConfig.isUploadFeature())
        	uploadMethod |= 0b1000;
	    else
	    	uploadMethod &= 0b1111_1111_1111_0111;
        buffer.putShort(uploadMethod);
        if(appConfig.getResv4() != null)
        	buffer.put(appConfig.getResv4());
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
