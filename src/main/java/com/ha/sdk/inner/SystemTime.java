package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * \u9429\u544a\u6e80\u7eef\u8364\u7cba\u93c3\u5815\u68ff\u6dc7\u2103\u4f05<br>
 * <i>native declaration : FaceRecoDef.h:63</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class SystemTime extends Structure {
	/** \u93c3\u8dfa\u5c2f */
	public byte time_zone;
	/**
	 * \u93c3\u30e6\u6e61 2017/12/6\u951b\ufffd2017\u9a9e\ufffd12\u93c8\ufffd6\u93c3\u30ef\u7d1a<br>
	 * C type : char[11]
	 */
	public byte[] date = new byte[11];
	/**
	 * \u93c3\u5815\u68ff   17:37:05<br>
	 * C type : char[9]
	 */
	public byte[] time = new byte[9];
	/**
	 * \u6dc7\u6fc8\u6680<br>
	 * C type : char[11]
	 */
	public byte[] resv = new byte[11];
	public SystemTime() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("time_zone", "date", "time", "resv");
	}
	/**
	 * @param time_zone \u93c3\u8dfa\u5c2f<br>
	 * @param date \u93c3\u30e6\u6e61 2017/12/6\u951b\ufffd2017\u9a9e\ufffd12\u93c8\ufffd6\u93c3\u30ef\u7d1a<br>
	 * C type : char[11]<br>
	 * @param time \u93c3\u5815\u68ff   17:37:05<br>
	 * C type : char[9]<br>
	 * @param resv \u6dc7\u6fc8\u6680<br>
	 * C type : char[11]
	 */
	public SystemTime(byte time_zone, byte date[], byte time[], byte resv[]) {
		super();
		this.time_zone = time_zone;
		if ((date.length != this.date.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.date = date;
		if ((time.length != this.time.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.time = time;
		if ((resv.length != this.resv.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.resv = resv;
	}
	public static class ByReference extends SystemTime implements Structure.ByReference {
		
	};
	public static class ByValue extends SystemTime implements Structure.ByValue {
		
	};
}