package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : config_gw.h:936</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FacePrivateParam extends Structure {
	/** \u68c0\u6d4b\u8ddf\u8e2a\u533a\u57df\u8fb9\u6570\u3002 */
	public byte roi_border_num;
	/** \u6bd4\u5bf9\u5f00\u5173\u3002\u53d6\u503c\u4e3a0\u65f6\u5173\u95ed\uff0c\u5176\u5b83\u53d6\u503c\u6253\u5f00\u3002 */
	public byte enable_match;
	/** \u8c03\u8bd5\u5f00\u5173\u3002 */
	public byte enable_debug;
	/** \u6df1\u5ea6\u53bb\u91cd\u590d\u5f00\u5173\u30021\uff1a\u5f00 0\uff1a\u5173\u3002 */
	public byte enable_dereplication;
	/** \u91cd\u590d\u8d85\u65f6\u3002\u8d85\u65f6\u5c06\u4e0d\u518d\u89c6\u4e3a\u91cd\u590d\u3002\u4ec5\u5728\u5f00\u542f\u53bb\u91cd\u590d\u65f6\u6709\u6548\u3002\u5355\u4f4d\uff1a\u79d2\u3002\u53d6\u503c\u8303\u56f4\uff1a1~60\u3002 */
	public byte replication_timeout;
	/** \u91cd\u590d\u8f93\u51fa\u95f4\u9694\u3002\u5bf9\u4e8e\u540c\u4e00\u4eba\u5458\uff0c\u4e0a\u62a5\u4eba\u8138\u7ed3\u679c\u65f6\u95f4\u95f4\u9694\u3002\u5355\u4f4d\uff1a\u79d2\u3002\u53d6\u503c\u8303\u56f4\uff1a1~120\u3002 */
	public byte replication_interval;
	/** \u8f93\u51fa\u56fe\u50cf\u54c1\u8d28\u3002 */
	public byte quality;
	/** \u540d\u5355\u8fc7\u671f\u81ea\u52a8\u6e05\u7406\u529f\u80fd\u5f00\u5173\u3002 */
	public byte enable_auto_clean;
	/** \u8f93\u51fa\u63a7\u5236\u3002 */
	public int output_form;
	/** \u6bd4\u5bf9\u6ee1\u5206\u76f8\u4f3c\u5ea6\u503c\u3002\u53d6\u503c\u8303\u56f4\uff1a0.2~1.0\u3002\u76f8\u4f3c\u5ea6\u8fbe\u5230\u8be5\u503c\u65f6\u786e\u4fe1\u5ea6\u4e3a\u6ee1\u5206\u3002 */
	public float full_credit;
	/** \u76f8\u4f3c\u5ea6\u8fbe\u5230\u8be5\u503c\u65f6\u8ba4\u4e3a\u6bd4\u5bf9\u6210\u529f\uff08100\u5206\u5236\uff09\u3002 */
	public short match_score;
	/** \u767d\u540d\u5355GPIO\u8f93\u51fa\u5f00\u5173\uff08\u7aef\u53e31\uff09\u30021\uff1a\u5f00 0\uff1a\u5173 */
	public byte gpio_enable_white;
	/** \u9ed1\u540d\u5355GPIO\u8f93\u51fa\u5f00\u5173\uff08\u7aef\u53e32\uff09\u30021\uff1a\u5f00 0\uff1a\u5173 */
	public byte gpio_enable_black;
	/** \u6682\u672a\u5b9a\u4e49GPIO\u8f93\u51fa\u5f00\u5173\uff08\u7aef\u53e33\uff09\u30021\uff1a\u5f00 0\uff1a\u5173 */
	public byte gpio_enable_resv;
	public byte serial_send_flag;
	/**
	 * \u6570\u636e\u4f4d<br>
	 * C type : unsigned char[2]
	 */
	public byte[] serial_databit = new byte[2];
	/**
	 * \u662f\u5426\u5f00\u59cb\u6821\u9a8c<br>
	 * C type : unsigned char[2]
	 */
	public byte[] serial_parity = new byte[2];
	/**
	 * \u505c\u6b62\u4f4d<br>
	 * C type : unsigned char[2]
	 */
	public byte[] serial_stopbit = new byte[2];
	/**
	 * \u6ce2\u7279\u7387<br>
	 * C type : int[2]
	 */
	public int[] serial_baudrate = new int[2];
	/** \u8865\u5149\u706f\u5de5\u4f5c\u6a21\u5f0f\u30021\uff1a\u5e38\u4eae 2\uff1a\u81ea\u52a8\u63a7\u5236 3\uff1a\u5e38\u95ed\u3002 */
	public byte light_mode;
	/** \u8865\u5149\u706f\u4eae\u5ea6\u7b49\u7ea7(1~100)\u3002 */
	public byte light_level;
	/** \u4eae\u706f\u95e8\u9650\u3002\u753b\u9762\u4eae\u5ea6\u503c\u4f4e\u4e8e\u8be5\u503c\u65f6\u4eae\u706f\u3002\u4eae\u5ea6\u53d6\u503c\u8303\u56f4\uff1a1~255\u3002 */
	public byte light_threshold;
	/** \u97f3\u91cf\u53d6\u503c\u8303\u56f4\uff1a0~100\u3002 */
	public byte audio_volume;
	/**
	 * \u5b57\u8282\u5bf9\u9f50\uff0c\u4fdd\u7559\u3002<br>
	 * C type : char[2]
	 */
	public byte[] resv2 = new byte[2];
	/** \u95f8\u673a\u63a7\u5236\u7c7b\u578b\uff1a0\u7ee7\u7535\u5668\uff0c 1\u97e6\u6839\u3002 */
	public byte gateway_control_type;
	/** \u7ee7\u7535\u5668\u5e8f\u53f7\u3002 */
	public byte alarm_index;
	/** \u7ee7\u7535\u5668\u81ea\u52a8\u95ed\u5408\u6301\u7eed\u65f6\u95f4\uff0c\u5355\u4f4dms(500-5000)\u3002 */
	public short alarm_duration;
	/** \u97e6\u6839\u7c7b\u578b\uff0cWG26,WG34, WG36, WG44\u3002\u76ee\u524d\u53ea\u652f\u6301WG26, WG34\u3002 */
	public byte wiegand_type;
	/** \u53d1\u884c\u7801\uff0cwg36 wg44\u9700\u8981\u6b64\u5b57\u6bb5 */
	public byte wiegand_dcode;
	/** \u97e6\u6839\u95e8\u7981\u516c\u5171\u5361\u53f7\u3002 */
	public int wiegand_public_cardid;
	/** \u81ea\u52a8\u5347\u6210\u5361\u53f7\u65f6\uff0c\u97e6\u6839\u5361\u53f7\u8303\u56f4\uff0c\u6700\u5c0f\u503c\u3002 */
	public int wiegand_card_id_min;
	/** \u81ea\u52a8\u5347\u6210\u5361\u53f7\u65f6\uff0c\u97e6\u6839\u5361\u53f7\u8303\u56f4\uff0c\u6700\u5927\u503c\u3002 */
	public int wiegand_card_id_max;
	/**
	 * \u4fdd\u7559\u3002<br>
	 * C type : char[68]
	 */
	public byte[] resv3 = new byte[68];
	/**
	 * \u68c0\u6d4b\u8ddf\u8e2a\u533a\u57df\u5750\u6807\u70b9\u3002\u987a\u6b21\u8fde\u63a5\u6784\u6210\u68c0\u6d4b\u8ddf\u8e2a\u533a\u57df\u3002<br>
	 * C type : Point[6]
	 */
	public Point[] roi = new Point[6];
	/** \u4eba\u8138\u68c0\u6d4b\u6700\u5c0f\u4eba\u8138\u5c3a\u5bf8 */
	public short min_face_size;
	/**
	 * \u4fdd\u7559\u3002<br>
	 * C type : char[334]
	 */
	public byte[] resv = new byte[334];
	public FacePrivateParam() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("roi_border_num", "enable_match", "enable_debug", "enable_dereplication", "replication_timeout", "replication_interval", "quality", "enable_auto_clean", "output_form", "full_credit", "match_score", "gpio_enable_white", "gpio_enable_black", "gpio_enable_resv", "serial_send_flag", "serial_databit", "serial_parity", "serial_stopbit", "serial_baudrate", "light_mode", "light_level", "light_threshold", "audio_volume", "resv2", "gateway_control_type", "alarm_index", "alarm_duration", "wiegand_type", "wiegand_dcode", "wiegand_public_cardid", "wiegand_card_id_min", "wiegand_card_id_max", "resv3", "roi", "min_face_size", "resv");
	}
	public static class ByReference extends FacePrivateParam implements Structure.ByReference {
		
	};
	public static class ByValue extends FacePrivateParam implements Structure.ByValue {
		
	};
}
