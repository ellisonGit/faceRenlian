package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : config_gw.h:490</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class MessageCarPortState extends Structure {
	/** \u72b6\u6001 0 \u65e0\u8f66 1\u6709\u8f66 2 \u538b\u7ebf 3 \u7279\u6b8a\u8f66\u4f4d */
	public int carPortState;
	/** \u989c\u8272 */
	public int lightColor;
	/** \u5f53\u524d\u6709\u51e0\u6839\u8f66\u9053 */
	public int laneNum;
	/**
	 * \u6bcf\u4e2a\u4f4d\u7f6e\u662f\u5426\u6709\u8f66<br>
	 * C type : int[(4)]
	 */
	public int[] hasCar = new int[4];
	/**
	 * \u662f\u5426\u6709\u8f66\u724c<br>
	 * C type : int[(4)]
	 */
	public int[] hasPlate = new int[4];
	/**
	 * \u8f66\u724c\u53f7\u7801<br>
	 * C type : char[(4)][16]
	 */
	public byte[] plate = new byte[((4) * (16))];
	public MessageCarPortState() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("carPortState", "lightColor", "laneNum", "hasCar", "hasPlate", "plate");
	}
	/**
	 * @param carPortState \u72b6\u6001 0 \u65e0\u8f66 1\u6709\u8f66 2 \u538b\u7ebf 3 \u7279\u6b8a\u8f66\u4f4d<br>
	 * @param lightColor \u989c\u8272<br>
	 * @param laneNum \u5f53\u524d\u6709\u51e0\u6839\u8f66\u9053<br>
	 * @param hasCar \u6bcf\u4e2a\u4f4d\u7f6e\u662f\u5426\u6709\u8f66<br>
	 * C type : int[(4)]<br>
	 * @param hasPlate \u662f\u5426\u6709\u8f66\u724c<br>
	 * C type : int[(4)]<br>
	 * @param plate \u8f66\u724c\u53f7\u7801<br>
	 * C type : char[(4)][16]
	 */
	public MessageCarPortState(int carPortState, int lightColor, int laneNum, int hasCar[], int hasPlate[], byte plate[]) {
		super();
		this.carPortState = carPortState;
		this.lightColor = lightColor;
		this.laneNum = laneNum;
		if ((hasCar.length != this.hasCar.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.hasCar = hasCar;
		if ((hasPlate.length != this.hasPlate.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.hasPlate = hasPlate;
		if ((plate.length != this.plate.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.plate = plate;
	}
	public static class ByReference extends MessageCarPortState implements Structure.ByReference {
		
	};
	public static class ByValue extends MessageCarPortState implements Structure.ByValue {
		
	};
}