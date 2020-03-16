package main.java.com.ha.sdk.inner;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : FaceRecoDef.h:81</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FaceImage extends Structure {
	/** \u9365\u5267\u5896\u7f02\u6827\u5f7f */
	public int img_seq;
	/** \u9365\u5267\u5896\u93cd\u714e\u7d21 0\u951b\u6b6bpg 1\u951b\u6b5cgr */
	public int img_fmt;
	/**
	 * \u7455\u4f79\u655e\u9350\u5c80\u6b91\u6d5c\u9e3f\u52af\u9365\u5267\u5896\u93c1\u7248\u5d41\u951b\u5c7e\u656e\u93b8\u4f77\u5bcc\u5a34\u4f78\u6d58\u934d\u5fd4\u7278\u5bee\ufffd<br>
	 * C type : unsigned char*
	 */
	public Pointer img;
	/** img\u9428\u52ef\u66b1\u6434\ufffd */
	public int img_len;
	/** \u9365\u60e7\u511a\u7039\u85c9\u5bb3\u951b\u5ba9pg\u9365\u60e7\u511a\u6d93\u5d85\uff5e\u59dd\u3089\u300d */
	public int width;
	/** \u9365\u60e7\u511a\u6942\u6a3a\u5bb3\u951b\u5ba9pg\u9365\u60e7\u511a\u6d93\u5d85\uff5e\u59dd\u3089\u300d */
	public int height;
	public FaceImage() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("img_seq", "img_fmt", "img", "img_len", "width", "height");
	}
	/**
	 * @param img_seq \u9365\u5267\u5896\u7f02\u6827\u5f7f<br>
	 * @param img_fmt \u9365\u5267\u5896\u93cd\u714e\u7d21 0\u951b\u6b6bpg 1\u951b\u6b5cgr<br>
	 * @param img \u7455\u4f79\u655e\u9350\u5c80\u6b91\u6d5c\u9e3f\u52af\u9365\u5267\u5896\u93c1\u7248\u5d41\u951b\u5c7e\u656e\u93b8\u4f77\u5bcc\u5a34\u4f78\u6d58\u934d\u5fd4\u7278\u5bee\ufffd<br>
	 * C type : unsigned char*<br>
	 * @param img_len img\u9428\u52ef\u66b1\u6434\ufffd<br>
	 * @param width \u9365\u60e7\u511a\u7039\u85c9\u5bb3\u951b\u5ba9pg\u9365\u60e7\u511a\u6d93\u5d85\uff5e\u59dd\u3089\u300d<br>
	 * @param height \u9365\u60e7\u511a\u6942\u6a3a\u5bb3\u951b\u5ba9pg\u9365\u60e7\u511a\u6d93\u5d85\uff5e\u59dd\u3089\u300d
	 */
	public FaceImage(int img_seq, int img_fmt, Pointer img, int img_len, int width, int height) {
		super();
		this.img_seq = img_seq;
		this.img_fmt = img_fmt;
		this.img = img;
		this.img_len = img_len;
		this.width = width;
		this.height = height;
	}
	public static class ByReference extends FaceImage implements Structure.ByReference {
		
	};
	public static class ByValue extends FaceImage implements Structure.ByValue {
		
	};
}