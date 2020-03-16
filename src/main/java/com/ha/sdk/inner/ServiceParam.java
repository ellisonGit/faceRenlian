package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * \u670d\u52a1\u53c2\u6570\uff088\u5b57\u8282\uff09\u3002<br>
 * <i>native declaration : config_gw.h:651</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ServiceParam extends Structure {
	/** \u670d\u52a1\u7aef\u53e3(\u53ea\u8bfb )\u3002 */
	public short port;
	/** \u6700\u5927\u5ba2\u6237\u7aef\u8fde\u63a5\u6570\uff0c\u5f53\u524d\u4e0d\u751f\u6548\u3002 */
	public byte max_clients;
	/** \u5fc3\u8df3\u8d85\u65f6\u65f6\u95f4\uff0c\u5f53\u524d\u4e0d\u751f\u6548\u3002 */
	public byte heart_timeout;
	/**
	 * \u4fdd\u7559\u5b57\u6bb5\u3002<br>
	 * C type : unsigned char[4]
	 */
	public byte[] resv = new byte[4];
	public ServiceParam() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("port", "max_clients", "heart_timeout", "resv");
	}
	/**
	 * @param port \u670d\u52a1\u7aef\u53e3(\u53ea\u8bfb )\u3002<br>
	 * @param max_clients \u6700\u5927\u5ba2\u6237\u7aef\u8fde\u63a5\u6570\uff0c\u5f53\u524d\u4e0d\u751f\u6548\u3002<br>
	 * @param heart_timeout \u5fc3\u8df3\u8d85\u65f6\u65f6\u95f4\uff0c\u5f53\u524d\u4e0d\u751f\u6548\u3002<br>
	 * @param resv \u4fdd\u7559\u5b57\u6bb5\u3002<br>
	 * C type : unsigned char[4]
	 */
	public ServiceParam(short port, byte max_clients, byte heart_timeout, byte resv[]) {
		super();
		this.port = port;
		this.max_clients = max_clients;
		this.heart_timeout = heart_timeout;
		if ((resv.length != this.resv.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.resv = resv;
	}
	public static class ByReference extends ServiceParam implements Structure.ByReference {
		
	};
	public static class ByValue extends ServiceParam implements Structure.ByValue {
		
	};
}
