package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : config_gw.h:380</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class config_net extends Structure {
	/**
	 * @see UPLOAD_MODE<br>
	 * C type : UPLOAD_MODE
	 */
	public int upload_mode;
	/** C type : char[16] */
	public byte[] ftp_server = new byte[16];
	/** C type : char[16] */
	public byte[] tcp_server = new byte[16];
	/** C type : char[16] */
	public byte[] ntp_server = new byte[16];
	public int ntp_interval;
	public short ftp_port;
	public short tcp_port;
	/** C type : char[32] */
	public byte[] ftp_user = new byte[32];
	/** C type : char[32] */
	public byte[] ftp_passwd = new byte[32];
	/** C type : char[16] */
	public byte[] led_server = new byte[16];
	public short led_port;
	public short http_port;
	/** C type : char[16] */
	public byte[] http_server = new byte[16];
	public byte tcp_enable;
	public byte ftp_enable;
	public byte led_enable;
	public byte http_enable;
	/** C type : char[88] */
	public byte[] resv = new byte[88];
	public config_net() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("upload_mode", "ftp_server", "tcp_server", "ntp_server", "ntp_interval", "ftp_port", "tcp_port", "ftp_user", "ftp_passwd", "led_server", "led_port", "http_port", "http_server", "tcp_enable", "ftp_enable", "led_enable", "http_enable", "resv");
	}
	public static class ByReference extends config_net implements Structure.ByReference {
		
	};
	public static class ByValue extends config_net implements Structure.ByValue {
		
	};
}
