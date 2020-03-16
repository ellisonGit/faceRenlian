package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : FaceRecoDef.h:159</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ha_rect extends Structure {
	/** \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u5bb8\ufe3f\u7b02\u7459\u62b6\u9367\u612d\u7223 */
	public short x;
	/** \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u5bb8\ufe3f\u7b02\u7459\u62b7\u9367\u612d\u7223 */
	public short y;
	/** \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u7039\u85c9\u5bb3 */
	public short width;
	/** \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u6942\u6a3a\u5bb3 */
	public short height;
	public ha_rect() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("x", "y", "width", "height");
	}
	/**
	 * @param x \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u5bb8\ufe3f\u7b02\u7459\u62b6\u9367\u612d\u7223<br>
	 * @param y \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u5bb8\ufe3f\u7b02\u7459\u62b7\u9367\u612d\u7223<br>
	 * @param width \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u7039\u85c9\u5bb3<br>
	 * @param height \u6d5c\u9e3f\u52af\u942d\u2541\u8230\u9356\u54c4\u7159\u6942\u6a3a\u5bb3
	 */
	public ha_rect(short x, short y, short width, short height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public static class ByReference extends ha_rect implements Structure.ByReference {
		
	};
	public static class ByValue extends ha_rect implements Structure.ByValue {
		
	};
}