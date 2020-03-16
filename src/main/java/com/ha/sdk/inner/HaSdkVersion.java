package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : HASdkDef.h:236</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class HaSdkVersion extends Structure {
	/**
	 * sdk\u7248\u672c<br>
	 * C type : char[64]
	 */
	public byte[] sdk_version = new byte[64];
	/**
	 * \u534f\u8bae\u7c7b\u578b<br>
	 * C type : char[64]
	 */
	public byte[] protocl_version = new byte[64];
	/**
	 * sdk\u6e90\u4ee3\u7801\u7248\u672c<br>
	 * C type : char[64]
	 */
	public byte[] sdk_code_version = new byte[64];
	/**
	 * \u652f\u6301\u7684\u6700\u4f4e\u56fa\u4ef6\u7248\u672c<br>
	 * C type : char[64]
	 */
	public byte[] min_firmware_ver = new byte[64];
	/**
	 * sdk\u4f7f\u7528\u5f97\u7b97\u6cd5\u7248\u672c<br>
	 * C type : char[64]
	 */
	public byte[] algorithm_version = new byte[64];
	public HaSdkVersion() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("sdk_version", "protocl_version", "sdk_code_version", "min_firmware_ver", "algorithm_version");
	}
	/**
	 * @param sdk_version sdk\u7248\u672c<br>
	 * C type : char[64]<br>
	 * @param protocl_version \u534f\u8bae\u7c7b\u578b<br>
	 * C type : char[64]<br>
	 * @param sdk_code_version sdk\u6e90\u4ee3\u7801\u7248\u672c<br>
	 * C type : char[64]<br>
	 * @param min_firmware_ver \u652f\u6301\u7684\u6700\u4f4e\u56fa\u4ef6\u7248\u672c<br>
	 * C type : char[64]<br>
	 * @param algorithm_version sdk\u4f7f\u7528\u5f97\u7b97\u6cd5\u7248\u672c<br>
	 * C type : char[64]
	 */
	public HaSdkVersion(byte sdk_version[], byte protocl_version[], byte sdk_code_version[], byte min_firmware_ver[], byte algorithm_version[]) {
		super();
		if ((sdk_version.length != this.sdk_version.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.sdk_version = sdk_version;
		if ((protocl_version.length != this.protocl_version.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.protocl_version = protocl_version;
		if ((sdk_code_version.length != this.sdk_code_version.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.sdk_code_version = sdk_code_version;
		if ((min_firmware_ver.length != this.min_firmware_ver.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.min_firmware_ver = min_firmware_ver;
		if ((algorithm_version.length != this.algorithm_version.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.algorithm_version = algorithm_version;
	}
	public static class ByReference extends HaSdkVersion implements Structure.ByReference {
		
	};
	public static class ByValue extends HaSdkVersion implements Structure.ByValue {
		
	};
}
