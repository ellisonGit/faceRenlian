package main.java.com.ha.tlv;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by 林星 on 2017/12/19.
 */

public final class Util {

    /**
     * 从系统类型、主版本号、次版本号以及消息类型创建Type字段
     * @param sysType 系统类型
     * @param majorProtocol 主版本号
     * @param minorProtocol 次版本号
     * @param msgType 消息类型
     * @return Type
     */
    public static int createType(int sysType, int majorProtocol, int minorProtocol, int msgType) {
		int type = 0;

		type |= (sysType << 24);
		type |= (majorProtocol << 14);
		type |= (minorProtocol << 10);
		type |= msgType;

		return type;
	}
    
    // 从请求包TYPE字段创建回应包请求字段
    public static int createType(int originType, int messageID) {
        return (originType & -1024) | messageID;
    }
    
    public static Date resolveUTCTime(long secs, long usecs) {
    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC-8:00"));
        cal.set(1970, 0, 1, 0, 0, 0);
        cal.add(Calendar.SECOND, (int)secs);
        cal.add(Calendar.MILLISECOND, (int)(usecs / 1000));
        return cal.getTime();
    }
    
    /**
     * 判断注册密码是否正确
     * 
     * @param time 设备时间
     * @param password 设备密码（明文）
     * @param passwordMD5 设备密码（密文）
     * @return 密码是否正确。按照协议约定，判定密码是否正确的做法应该是使用time和password作为密钥，经过MD5加密后得到passwordMD5
     */
    public static boolean judgeRegPassword(Date time, String password, byte[] passwordMD5) {
    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC-8:00"));
    	cal.set(1970, 0, 1, 0, 0, 0);
    	int secDiff = (int) ((time.getTime() - cal.getTime().getTime()) / 1000);
    	byte[] _password = new byte[password.length() + 4];
    	_password[3] = (byte) ((secDiff & 0xFF000000) >> 24);
    	_password[2] = (byte) ((secDiff & 0xFF0000) >> 16);
    	_password[1] = (byte) ((secDiff & 0xFF00) >> 8);
    	_password[0] = (byte) (secDiff & 0xFF);
    	System.arraycopy(password.getBytes(), 0, password, 4, password.length());
    	try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] _passwordMD5 = md.digest(_password);
			return Arrays.equals(passwordMD5, _passwordMD5);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
}
