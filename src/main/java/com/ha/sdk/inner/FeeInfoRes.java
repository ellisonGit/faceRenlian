package main.java.com.ha.sdk.inner;
import main.java.com.ha.sdk.inner.ComHaSdkLibrary.time_t;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : HASdkDef.h:137</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FeeInfoRes extends Structure {
	/**
	 * \u8f66\u724c<br>
	 * C type : char[16]
	 */
	public byte[] plate = new byte[16];
	/**
	 * \u5165\u573a\u65f6\u95f4<br>
	 * C type : time_t
	 */
	public time_t time_in;
	/**
	 * \u51fa\u573a\u65f6\u95f4<br>
	 * C type : time_t
	 */
	public time_t time_out;
	/** \u505c\u8f66\u8d39\u7528 */
	public float park_fee;
	/** \u6536\u8d39\u7c7b\u578b */
	public short fee_mode;
	/** \u8f66\u724c\u989c\u8272 */
	public byte platecolor;
	/** \u6708\u79df\u8f66\u8fd8\u662f\u4e34\u65f6\u8f66 */
	public byte InOut_type;
	public FeeInfoRes() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("plate", "time_in", "time_out", "park_fee", "fee_mode", "platecolor", "InOut_type");
	}
	/**
	 * @param plate \u8f66\u724c<br>
	 * C type : char[16]<br>
	 * @param time_in \u5165\u573a\u65f6\u95f4<br>
	 * C type : time_t<br>
	 * @param time_out \u51fa\u573a\u65f6\u95f4<br>
	 * C type : time_t<br>
	 * @param park_fee \u505c\u8f66\u8d39\u7528<br>
	 * @param fee_mode \u6536\u8d39\u7c7b\u578b<br>
	 * @param platecolor \u8f66\u724c\u989c\u8272<br>
	 * @param InOut_type \u6708\u79df\u8f66\u8fd8\u662f\u4e34\u65f6\u8f66
	 */
	public FeeInfoRes(byte plate[], time_t time_in, time_t time_out, float park_fee, short fee_mode, byte platecolor, byte InOut_type) {
		super();
		if ((plate.length != this.plate.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.plate = plate;
		this.time_in = time_in;
		this.time_out = time_out;
		this.park_fee = park_fee;
		this.fee_mode = fee_mode;
		this.platecolor = platecolor;
		this.InOut_type = InOut_type;
	}
	public static class ByReference extends FeeInfoRes implements Structure.ByReference {
		
	};
	public static class ByValue extends FeeInfoRes implements Structure.ByValue {
		
	};
}
