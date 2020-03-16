package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : config_gw.h:1043</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FaceAppParam extends Structure {
	/**
	 * \u8bbe\u5907\u7f16\u53f7<br>
	 * C type : char[32]
	 */
	public byte[] dev_no = new byte[32];
	/**
	 * \u70b9\u4f4d\u7f16\u53f7<br>
	 * C type : char[32]
	 */
	public byte[] point_no = new byte[32];
	/**
	 * \u70b9\u4f4d\u540d\u79f0<br>
	 * C type : char[96]
	 */
	public byte[] point_name = new byte[96];
	/**
	 * \u4fdd\u7559\u5b57\u6bb51<br>
	 * C type : char[32]
	 */
	public byte[] resv1 = new byte[32];
	/** \u5fc3\u8df3\u65f6\u95f4\u95f4\u9694 */
	public byte heart_beat_interval;
	/** \u5916\u7f51\u63a7\u5236\u5f00\u5173 */
	public byte extranet_enale;
	/** \u5916\u7f51\u63a7\u5236\u670d\u52a1\u5668\u7aef\u53e3 */
	public short extranet_port;
	/**
	 * \u5916\u7f51\u63a7\u5236\u670d\u52a1\u5668ip<br>
	 * C type : char[16]
	 */
	public byte[] extranet_ip = new byte[16];
	/** \u5f00\u542f\u767b\u5f55\u9a8c\u8bc1\u540e\uff0c\u5ba2\u6237\u7aef\u5728\u4e0e\u670d\u52a1\u5668\u5efa\u7acb\u8fde\u63a5\u540e\u5c06\u81ea\u52a8\u53d1\u9001\u767b\u5f55\u9a8c\u8bc1\u8bf7\u6c42\u3002\u6ce8\uff1a\u6293\u62cd\u673a\u4f5c\u4e3a\u5ba2\u6237\u7aef\u30021\uff1a\u5f00\u542f\u767b\u5f55\u9a8c\u8bc1 0\uff1a\u5173\u95ed\u767b\u5f55\u9a8c\u8bc1 */
	public byte verify_enable;
	/**
	 * \u767b\u5f55\u9a8c\u8bc1\u7528\u6237\u540d<br>
	 * C type : char[15]
	 */
	public byte[] user_name = new byte[15];
	/**
	 * \u767b\u5f55\u9a8c\u8bc1\u5bc6\u7801<br>
	 * C type : char[16]
	 */
	public byte[] passwd = new byte[16];
	/**
	 * \u4fdd\u7559\u5b57\u6bb52<br>
	 * C type : unsigned char[12]
	 */
	public byte[] resv2 = new byte[12];
	/** \u6570\u636e\u4e0a\u4f20\u65b9\u5f0f0\uff1a\u5173\u95ed\u4e0a\u4f20 1\uff1aTCP\u65b9\u5f0f 2\uff1aFTP\u65b9\u5f0f 3\uff1aHTTP\u65b9\u5f0f */
	public short upload_mode;
	/** \u670d\u52a1\u5668\u7aef\u53e3 */
	public short upload_port;
	/**
	 * \u670d\u52a1\u5668IP<br>
	 * C type : char[16]
	 */
	public byte[] upload_ip = new byte[16];
	/** C type : upload_info_union */
	public upload_info_union upload_info;
	/**
	 * \u4fdd\u7559\u5b57\u6bb53<br>
	 * C type : char[18]
	 */
	public byte[] resv3 = new byte[18];
	/** \u6bd4\u5bf9\u5f00\u5173 */
	public int match_enable;
	/** \u6bd4\u5bf9\u786e\u4fe1\u5206\u6570 */
	public int match_score;
	/** \u91cd\u590d\u8fc7\u6ee4\u5f00\u5173 */
	public int dereplication_enable;
	/** \u91cd\u590d\u8d85\u65f6/\u8f93\u51fa\u95f4\u9694\u65f6\u95f4 */
	public int dereplication_interval;
	/** \u56fe\u50cf\u8f93\u51fa\u5f62\u5f0f */
	public short output_mode;
	/**
	 * \u4fdd\u7559\u5b57\u6bb54<br>
	 * C type : char[256]
	 */
	public byte[] resv4 = new byte[256];
	/** <i>native declaration : config_gw.h:1061</i> */
	public static class upload_info_union extends Union {
		/**
		 * \u6293\u62cd\u6570\u636e\u670d\u52a1URL(http\u4e0a\u4f20\u65b9\u5f0f\u65f6\u6709\u6548)<br>
		 * C type : char[102]
		 */
		public byte[] upload_url = new byte[102];
		/**
		 * ftp\u4e0a\u4f20\u53c2\u6570\u4fe1\u606f(ftp\u4e0a\u4f20\u65f6\u6709\u6548);<br>
		 * C type : FtpInfo
		 */
		public FtpInfo ftp_info;
		/**
		 * tcp\u4e0a\u4f20\u65b9\u5f0f\u7684\u4fdd\u7559\u5b57\u6bb5<br>
		 * C type : char[102]
		 */
		public byte[] tcp_resv = new byte[102];
		/**
		 * \u5176\u5b83\u4e0a\u4f20\u65b9\u5f0f\u4fdd\u7559\u5b57\u6bb5<br>
		 * C type : char[102]
		 */
		public byte[] other_resv = new byte[102];
		public upload_info_union() {
			super();
		}
		/**
		 * @param upload_url_or_tcp_resv_or_other_resv \u6293\u62cd\u6570\u636e\u670d\u52a1URL(http\u4e0a\u4f20\u65b9\u5f0f\u65f6\u6709\u6548)<br>
		 * C type : char[102], or tcp\u4e0a\u4f20\u65b9\u5f0f\u7684\u4fdd\u7559\u5b57\u6bb5<br>
		 * C type : char[102], or \u5176\u5b83\u4e0a\u4f20\u65b9\u5f0f\u4fdd\u7559\u5b57\u6bb5<br>
		 * C type : char[102]
		 */
		public upload_info_union(byte upload_url_or_tcp_resv_or_other_resv[]) {
			super();
			if ((upload_url_or_tcp_resv_or_other_resv.length != this.upload_url.length)) 
				throw new IllegalArgumentException("Wrong array size !");
			this.other_resv = this.tcp_resv = this.upload_url = upload_url_or_tcp_resv_or_other_resv;
			setType(byte[].class);
		}
		/**
		 * @param ftp_info ftp\u4e0a\u4f20\u53c2\u6570\u4fe1\u606f(ftp\u4e0a\u4f20\u65f6\u6709\u6548);<br>
		 * C type : FtpInfo
		 */
		public upload_info_union(FtpInfo ftp_info) {
			super();
			this.ftp_info = ftp_info;
			setType(FtpInfo.class);
		}
		public static class ByReference extends upload_info_union implements Structure.ByReference {
			
		};
		public static class ByValue extends upload_info_union implements Structure.ByValue {
			
		};
	};
	public FaceAppParam() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("dev_no", "point_no", "point_name", "resv1", "heart_beat_interval", "extranet_enale", "extranet_port", "extranet_ip", "verify_enable", "user_name", "passwd", "resv2", "upload_mode", "upload_port", "upload_ip", "upload_info", "resv3", "match_enable", "match_score", "dereplication_enable", "dereplication_interval", "output_mode", "resv4");
	}
	public static class ByReference extends FaceAppParam implements Structure.ByReference {
		
	};
	public static class ByValue extends FaceAppParam implements Structure.ByValue {
		
	};
}
