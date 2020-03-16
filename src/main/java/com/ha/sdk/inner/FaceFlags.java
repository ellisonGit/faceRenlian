package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : FaceRecoDef.h:71</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FaceFlags extends Structure {
	/**
	 * \u6d5c\u54c4\u61b3ID<br>
	 * C type : char[20]
	 */
	public byte[] faceID = new byte[20];
	/**
	 * \u6d5c\u54c4\u61b3\u6fee\u64b3\u6095<br>
	 * C type : char[16]
	 */
	public byte[] faceName = new byte[16];
	/** \u6d5c\u54c4\u61b3\u7459\u6395\u58ca\u951b\ufffd0\u951b\u6c2d\u6ad8\u95ab\u6c2b\u6c49\u935b\u6a38\ufffd\ufffd 1\u951b\u6c31\u6ae7\u935a\u5d85\u5d1f\u6d5c\u54c4\u61b3\u9286\ufffd 2\u951b\u6c36\u7ca6\u935a\u5d85\u5d1f\u6d5c\u54c4\u61b3\u9286\ufffd -1\u951b\u6c2d\u588d\u93c8\u5909\u6c49\u935b\u6a38\ufffd\ufffd */
	public int role;
	/** \u95ca\ufe3d\u7274\u9357\u5fda\ue185\u95c2\u3127\ue6e6\u9357\u2033\u5f7f */
	public int wgCardNO;
	/** \u93b4\ue045\ue11b\u93c3\u5815\u68ff\u951b\u5c83\ue1da\u6d5c\u54c4\u61b3\u6dc7\u2103\u4f05\u9366\u3128\ue1da\u93c3\u5815\u68ff\u9350\u546e\u6e41\u93c1\ufffd,\u6d60\ufffd1970\u9a9e\ufffd1\u93c8\ufffd1\u93c3\ufffd0\u93c3\ufffd0\u9352\ufffd0\u7ec9\u6391\u57cc\u93b4\ue045\ue11b\u93c3\u5815\u68ff\u9428\u52ed\ue757\u93c1\u5e2e\u7d190xFFFFFFFF\u741b\u3127\u305a\u59d8\u9550\u7b99\u93c8\u590b\u6665\u951b\ufffd0\u741b\u3127\u305a\u59d8\u9550\u7b99\u6fb6\u8fa8\u6665\u951b\ufffd */
	public int effectTime;
	/** C type : char[8188] */
	public byte[] resv = new byte[8188];
	public FaceFlags() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("faceID", "faceName", "role", "wgCardNO", "effectTime", "resv");
	}
	/**
	 * @param faceID \u6d5c\u54c4\u61b3ID<br>
	 * C type : char[20]<br>
	 * @param faceName \u6d5c\u54c4\u61b3\u6fee\u64b3\u6095<br>
	 * C type : char[16]<br>
	 * @param role \u6d5c\u54c4\u61b3\u7459\u6395\u58ca\u951b\ufffd0\u951b\u6c2d\u6ad8\u95ab\u6c2b\u6c49\u935b\u6a38\ufffd\ufffd 1\u951b\u6c31\u6ae7\u935a\u5d85\u5d1f\u6d5c\u54c4\u61b3\u9286\ufffd 2\u951b\u6c36\u7ca6\u935a\u5d85\u5d1f\u6d5c\u54c4\u61b3\u9286\ufffd -1\u951b\u6c2d\u588d\u93c8\u5909\u6c49\u935b\u6a38\ufffd\ufffd<br>
	 * @param wgCardNO \u95ca\ufe3d\u7274\u9357\u5fda\ue185\u95c2\u3127\ue6e6\u9357\u2033\u5f7f<br>
	 * @param effectTime \u93b4\ue045\ue11b\u93c3\u5815\u68ff\u951b\u5c83\ue1da\u6d5c\u54c4\u61b3\u6dc7\u2103\u4f05\u9366\u3128\ue1da\u93c3\u5815\u68ff\u9350\u546e\u6e41\u93c1\ufffd,\u6d60\ufffd1970\u9a9e\ufffd1\u93c8\ufffd1\u93c3\ufffd0\u93c3\ufffd0\u9352\ufffd0\u7ec9\u6391\u57cc\u93b4\ue045\ue11b\u93c3\u5815\u68ff\u9428\u52ed\ue757\u93c1\u5e2e\u7d190xFFFFFFFF\u741b\u3127\u305a\u59d8\u9550\u7b99\u93c8\u590b\u6665\u951b\ufffd0\u741b\u3127\u305a\u59d8\u9550\u7b99\u6fb6\u8fa8\u6665\u951b\ufffd<br>
	 * @param resv C type : char[8188]
	 */
	public FaceFlags(byte faceID[], byte faceName[], int role, int wgCardNO, int effectTime, byte resv[]) {
		super();
		if ((faceID.length != this.faceID.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.faceID = faceID;
		if ((faceName.length != this.faceName.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.faceName = faceName;
		this.role = role;
		this.wgCardNO = wgCardNO;
		this.effectTime = effectTime;
		if ((resv.length != this.resv.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.resv = resv;
	}
	public static class ByReference extends FaceFlags implements Structure.ByReference {
		
	};
	public static class ByValue extends FaceFlags implements Structure.ByValue {
		
	};
}