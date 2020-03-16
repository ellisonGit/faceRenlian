package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * \u8f66\u4f4d\u76f8\u673a\u7b97\u6cd5\u914d\u7f6e<br>
 * <i>native declaration : config_gw.h:518</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class config_carbinet_vdetect extends Structure {
	/**
	 * \u865a\u62df\u7ebf\u5708<br>
	 * C type : config_point[(4)][4]
	 */
	public config_point[] inventedRoi = new config_point[((4) * (4))];
	public config_carbinet_vdetect() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("inventedRoi");
	}
	/**
	 * @param inventedRoi \u865a\u62df\u7ebf\u5708<br>
	 * C type : config_point[(4)][4]
	 */
	public config_carbinet_vdetect(config_point inventedRoi[]) {
		super();
		if ((inventedRoi.length != this.inventedRoi.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.inventedRoi = inventedRoi;
	}
	public static class ByReference extends config_carbinet_vdetect implements Structure.ByReference {
		
	};
	public static class ByValue extends config_carbinet_vdetect implements Structure.ByValue {
		
	};
}