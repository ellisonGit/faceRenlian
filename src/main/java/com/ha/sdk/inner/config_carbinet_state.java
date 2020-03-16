package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * \u8f66\u4f4d\u72b6\u6001\u4fe1\u606f<br>
 * <i>native declaration : config_gw.h:509</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class config_carbinet_state extends Structure {
	/** \u662f\u5426\u6709\u6548  0\u65e0\u6548 1\u6709\u6548 */
	public int isEnable;
	/** \u662f\u5426\u95ea\u70c1  0\u4e0d\u95ea 1\u95ea */
	public int isFlicker;
	/** \u706f\u7684\u989c\u8272 0\u706d 1\u7ea2 2\u7eff 3\u9ec4 4\u84dd\u8272 5\u54c1\u7ea2 6\u9752 7\u767d */
	public int lightColor;
	public config_carbinet_state() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("isEnable", "isFlicker", "lightColor");
	}
	/**
	 * @param isEnable \u662f\u5426\u6709\u6548  0\u65e0\u6548 1\u6709\u6548<br>
	 * @param isFlicker \u662f\u5426\u95ea\u70c1  0\u4e0d\u95ea 1\u95ea<br>
	 * @param lightColor \u706f\u7684\u989c\u8272 0\u706d 1\u7ea2 2\u7eff 3\u9ec4 4\u84dd\u8272 5\u54c1\u7ea2 6\u9752 7\u767d
	 */
	public config_carbinet_state(int isEnable, int isFlicker, int lightColor) {
		super();
		this.isEnable = isEnable;
		this.isFlicker = isFlicker;
		this.lightColor = lightColor;
	}
	public static class ByReference extends config_carbinet_state implements Structure.ByReference {
		
	};
	public static class ByValue extends config_carbinet_state implements Structure.ByValue {
		
	};
}
