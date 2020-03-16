package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import java.util.Arrays;
import java.util.List;
/**
 * \u8fde\u63a5\u53c2\u6570\u3002<br>
 * <i>native declaration : config_gw.h:888</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ClientParam extends Structure {
	/**
	 * \u6a21\u5f0f@ref UploadMode \u3002<br>
	 * 0 \u672a\u521d\u59cb\u5316\u3002 1 TCP\u534f\u8bae\u4e0a\u4f20\u3002 2 FTP\u534f\u8bae\u4e0a\u4f20\u3002 3 HTTP\u534f\u8bae\u4e0a\u4f20\u3002
	 */
	public byte mode;
	/**
	 * \u4fdd\u7559\u3002<br>
	 * C type : char[3]
	 */
	public byte[] resv = new byte[3];
	/** C type : field1_union */
	public field1_union field1;
	/**
	 * \u670d\u52a1\u5668\u3002<br>
	 * <i>native declaration : config_gw.h:894</i>
	 */
	public static class field1_union extends Union {
		/** C type : FtpClientParam */
		public FtpClientParam ftp;
		/** C type : TcpClientParam */
		public TcpClientParam tcp;
		/** C type : HttpClientParam */
		public HttpClientParam http;
		public field1_union() {
			super();
		}
		/** @param ftp C type : FtpClientParam */
		public field1_union(FtpClientParam ftp) {
			super();
			this.ftp = ftp;
			setType(FtpClientParam.class);
		}
		/** @param tcp C type : TcpClientParam */
		public field1_union(TcpClientParam tcp) {
			super();
			this.tcp = tcp;
			setType(TcpClientParam.class);
		}
		/** @param http C type : HttpClientParam */
		public field1_union(HttpClientParam http) {
			super();
			this.http = http;
			setType(HttpClientParam.class);
		}
		public static class ByReference extends field1_union implements Structure.ByReference {
			
		};
		public static class ByValue extends field1_union implements Structure.ByValue {
			
		};
	};
	public ClientParam() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("mode", "resv", "field1");
	}
	/**
	 * @param mode \u6a21\u5f0f@ref UploadMode \u3002<br>
	 * 0 \u672a\u521d\u59cb\u5316\u3002 1 TCP\u534f\u8bae\u4e0a\u4f20\u3002 2 FTP\u534f\u8bae\u4e0a\u4f20\u3002 3 HTTP\u534f\u8bae\u4e0a\u4f20\u3002<br>
	 * @param resv \u4fdd\u7559\u3002<br>
	 * C type : char[3]<br>
	 * @param field1 C type : field1_union
	 */
	public ClientParam(byte mode, byte resv[], field1_union field1) {
		super();
		this.mode = mode;
		if ((resv.length != this.resv.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.resv = resv;
		this.field1 = field1;
	}
	public static class ByReference extends ClientParam implements Structure.ByReference {
		
	};
	public static class ByValue extends ClientParam implements Structure.ByValue {
		
	};
}
