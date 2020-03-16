package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : discover_def.h:25</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ipset_t extends Structure {
	/** C type : char[20] */
	public byte[] mac = new byte[20];
	/** C type : char[20] */
	public byte[] ip = new byte[20];
	/** C type : char[20] */
	public byte[] netmask = new byte[20];
	/** C type : char[20] */
	public byte[] gateway = new byte[20];
	public ipset_t() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("mac", "ip", "netmask", "gateway");
	}
	/**
	 * @param mac C type : char[20]<br>
	 * @param ip C type : char[20]<br>
	 * @param netmask C type : char[20]<br>
	 * @param gateway C type : char[20]
	 */
	public ipset_t(byte mac[], byte ip[], byte netmask[], byte gateway[]) {
		super();
		if ((mac.length != this.mac.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.mac = mac;
		if ((ip.length != this.ip.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.ip = ip;
		if ((netmask.length != this.netmask.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.netmask = netmask;
		if ((gateway.length != this.gateway.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.gateway = gateway;
	}
	public static class ByReference extends ipset_t implements Structure.ByReference {
		
	};
	public static class ByValue extends ipset_t implements Structure.ByValue {
		
	};
}