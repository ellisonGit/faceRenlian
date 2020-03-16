package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : config_gw.h:583</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class config_device_group extends Structure {
	public int enable_group;
	public int enable_group_assist;
	/**
	 * \u672c\u673a\u7ec4\u7f51\u7684\u76f8\u8f85ip<br>
	 * C type : char[20]
	 */
	public byte[] ip = new byte[20];
	public int gate_num;
	/** C type : device_group_gate_attr[8] */
	public device_group_gate_attr[] attr = new device_group_gate_attr[8];
	public config_device_group() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("enable_group", "enable_group_assist", "ip", "gate_num", "attr");
	}
	/**
	 * @param ip \u672c\u673a\u7ec4\u7f51\u7684\u76f8\u8f85ip<br>
	 * C type : char[20]<br>
	 * @param attr C type : device_group_gate_attr[8]
	 */
	public config_device_group(int enable_group, int enable_group_assist, byte ip[], int gate_num, device_group_gate_attr attr[]) {
		super();
		this.enable_group = enable_group;
		this.enable_group_assist = enable_group_assist;
		if ((ip.length != this.ip.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.ip = ip;
		this.gate_num = gate_num;
		if ((attr.length != this.attr.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.attr = attr;
	}
	public static class ByReference extends config_device_group implements Structure.ByReference {
		
	};
	public static class ByValue extends config_device_group implements Structure.ByValue {
		
	};
}
