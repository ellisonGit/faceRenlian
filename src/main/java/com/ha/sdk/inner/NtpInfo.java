package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * NTP\u93cd\u2103\u6902\u6dc7\u2103\u4f05<br>
 * <i>native declaration : FaceRecoDef.h:124</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class NtpInfo extends Structure {
	/** Ntp\u5bee\ufffd\u934f\u5d07\u59f8\u93ac\u4f8a\u7d1d0\u93c4\ue21a\u53e7 \u95c8\ufffd0\u93c4\ue21a\u7d11 */
	public short enable_state;
	/** \u93c7\u5b58\u67ca\u935b\u3126\u6e61\u951b\ufffd60~600s */
	public short update_cycle;
	/**
	 * NTP\u93c8\u5d85\u59df\u9363\u2565p\u9366\u677f\u6f43<br>
	 * C type : char[16]
	 */
	public byte[] ntp_server = new byte[16];
	/**
	 * \u6dc7\u6fc8\u6680<br>
	 * C type : char[12]
	 */
	public byte[] resv = new byte[12];
	public NtpInfo() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("enable_state", "update_cycle", "ntp_server", "resv");
	}
	/**
	 * @param enable_state Ntp\u5bee\ufffd\u934f\u5d07\u59f8\u93ac\u4f8a\u7d1d0\u93c4\ue21a\u53e7 \u95c8\ufffd0\u93c4\ue21a\u7d11<br>
	 * @param update_cycle \u93c7\u5b58\u67ca\u935b\u3126\u6e61\u951b\ufffd60~600s<br>
	 * @param ntp_server NTP\u93c8\u5d85\u59df\u9363\u2565p\u9366\u677f\u6f43<br>
	 * C type : char[16]<br>
	 * @param resv \u6dc7\u6fc8\u6680<br>
	 * C type : char[12]
	 */
	public NtpInfo(short enable_state, short update_cycle, byte ntp_server[], byte resv[]) {
		super();
		this.enable_state = enable_state;
		this.update_cycle = update_cycle;
		if ((ntp_server.length != this.ntp_server.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.ntp_server = ntp_server;
		if ((resv.length != this.resv.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.resv = resv;
	}
	public static class ByReference extends NtpInfo implements Structure.ByReference {
		
	};
	public static class ByValue extends NtpInfo implements Structure.ByValue {
		
	};
}
