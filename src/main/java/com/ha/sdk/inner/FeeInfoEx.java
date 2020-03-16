package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : HASdkDef.h:170</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FeeInfoEx extends Structure {
	/**
	 * \u8f66\u724c\u53f7\u7801<br>
	 * C type : char[16]
	 */
	public byte[] plate = new byte[16];
	public short year_in;
	public byte mon_in;
	public byte day_in;
	public byte hour_in;
	public byte min_in;
	public short sec_in;
	public short year_out;
	public byte mon_out;
	public byte day_out;
	public byte hour_out;
	public byte min_out;
	public short sec_out;
	public float park_fee;
	public short fee_mode;
	public byte platecolor;
	public byte InOut_type;
	public FeeInfoEx() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("plate", "year_in", "mon_in", "day_in", "hour_in", "min_in", "sec_in", "year_out", "mon_out", "day_out", "hour_out", "min_out", "sec_out", "park_fee", "fee_mode", "platecolor", "InOut_type");
	}
	public static class ByReference extends FeeInfoEx implements Structure.ByReference {
		
	};
	public static class ByValue extends FeeInfoEx implements Structure.ByValue {
		
	};
}