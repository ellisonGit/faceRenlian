package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * \u6d41\u53c2\u6570(64\u5b57\u8282)\u3002<br>
 * <i>native declaration : config_gw.h:838</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class StreamParam extends Structure {
	/** \u89c6\u9891\u6d41\u6216JPEG\u6d41@ref ImageFormat \u3002 */
	public int video_format;
	/**
	 * \u4fdd\u7559\u3002<br>
	 * C type : char[60]
	 */
	public byte[] resv = new byte[60];
	public StreamParam() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("video_format", "resv");
	}
	/**
	 * @param video_format \u89c6\u9891\u6d41\u6216JPEG\u6d41@ref ImageFormat \u3002<br>
	 * @param resv \u4fdd\u7559\u3002<br>
	 * C type : char[60]
	 */
	public StreamParam(int video_format, byte resv[]) {
		super();
		this.video_format = video_format;
		if ((resv.length != this.resv.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.resv = resv;
	}
	public static class ByReference extends StreamParam implements Structure.ByReference {
		
	};
	public static class ByValue extends StreamParam implements Structure.ByValue {
		
	};
}