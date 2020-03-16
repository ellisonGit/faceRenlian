package main.java.com.ha.sdk.inner;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.FloatByReference;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : FaceRecoDef.h:4</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FaceRecoInfo extends Structure {
	/** \u93b6\u64b4\u5abf\u6434\u5fd3\u5f7f\u951b\u5c7c\u7ca01\u5bee\ufffd\u6fee\u5b36\u7d1d\u59e3\u5fce\u9a87\u9422\u71b6\u7af4\u7f01\u52ec\u59c4\u93b7\u5d86\u669f\u93b9\ue1bc\ue583\u9354\ufffd1\u9286\ufffd */
	public int sequence;
	/**
	 * \u9429\u544a\u6e80\u7f02\u6827\u5f7f<br>
	 * C type : char[32]
	 */
	public byte[] camId = new byte[32];
	/**
	 * \u9410\u901b\u7d85\u7f02\u6827\u5f7f<br>
	 * C type : char[32]
	 */
	public byte[] posId = new byte[32];
	/**
	 * \u9410\u901b\u7d85\u935a\u5d87\u041e<br>
	 * C type : char[96]
	 */
	public byte[] posName = new byte[96];
	/** \u93b6\u64b4\u5abf\u93c3\u5815\u68ff\u7ec9\u6393\u669f\u951b\u5c7c\u7ca01970\u9a9e\ufffd01\u93c8\ufffd01\u93c3\ufffd00\u93c3\ufffd00\u9352\ufffd00\u7ec9\u6395\u56a6\u93b6\u64b4\u5abf\u93c3\u5815\u68ff\u7f01\u5fda\u7e43\u9428\u52ed\ue757\u93c1\u822c\ufffd\ufffd */
	public int tvSec;
	/** \u93b6\u64b4\u5abf\u93c3\u5815\u68ff\u5bf0\ue1be\ue757\u93c1\u5e2e\u7d1dtvSec\u9428\u52eb\u71ac\u93c1\ufffd */
	public int tvUsec;
	/** \u7039\u70b4\u6902\u93b6\u64b4\u5abf\u93cd\u56e7\u7e54\u951b\ufffd0\u951b\u6c36\u6f6a\u7039\u70b4\u6902\u93b6\u64b4\u5abf\u93c1\u7248\u5d41\u9286\u509e\u6f6a0\u951b\u6c2c\u7584\u93c3\u8235\u59c4\u93b7\u5d86\u669f\u93b9\ue1ba\ufffd\ufffd */
	public short isRealtimeData;
	/** \u59e3\u65bf\ue1ee\u7f01\u64b4\u7049\u951b\ufffd0\u951b\u6c2d\u6e6d\u59e3\u65bf\ue1ee\u9286\ufffd-1\u951b\u6c2d\u762e\u7035\u7470\u3051\u7490\u30e3\ufffd\u509a\u3047\u6d5c\ufffd0\u9428\u52eb\u5f47\u934a\u7877\u7d30\u59e3\u65bf\ue1ee\u93b4\u612c\u59db\u93c3\u5241\u6b91\u7ead\ue1bb\u4fca\u6434\ufe40\u578e\u93c1\u5e2e\u7d19100\u9352\u55d7\u57d7\u951b\u5908\ufffd\ufffd */
	public short matched;
	/**
	 * \u6d5c\u54c4\u61b3ID<br>
	 * C type : char[20]
	 */
	public byte[] matchPersonId = new byte[20];
	/**
	 * \u6d5c\u54c4\u61b3\u6fee\u64b3\u6095<br>
	 * C type : char[16]
	 */
	public byte[] matchPersonName = new byte[16];
	/** \u6d5c\u54c4\u61b3\u7459\u6395\u58ca\u951b\ufffd0\u951b\u6c2d\u6ad8\u95ab\u6c2b\u6c49\u935b\u6a38\ufffd\ufffd 1\u951b\u6c31\u6ae7\u935a\u5d85\u5d1f\u6d5c\u54c4\u61b3\u9286\ufffd 2\u951b\u6c36\u7ca6\u935a\u5d85\u5d1f\u6d5c\u54c4\u61b3 */
	public int matchRole;
	/** \u934f\u3126\u6ad9\u9365\u6485\u7d1d\u93c4\ue21a\u60c1\u9356\u546d\u60c8\u934f\u3126\u6ad9\u9365\u60e7\u511a\u9286\ufffd0\u951b\u6c2b\u7b09\u9356\u546d\u60c8\u934f\u3126\u6ad9\u9365\u60e7\u511a\u9286\u509e\u6f6a0\u951b\u6c2c\u5bd8\u935a\ue0a2\u53cf\u93c5\ue21a\u6d58\u934d\u5fcb\ufffd\ufffd */
	public int existImg;
	/**
	 * \u934f\u3126\u6ad9\u9365\u60e7\u511a\u93cd\u714e\u7d21<br>
	 * C type : char[4]
	 */
	public byte[] imgFormat = new byte[4];
	/** \u934f\u3126\u6ad9\u9365\u60e7\u511a\u6fb6\u0443\u76ac */
	public int imgLen;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u934f\u3126\u6ad9\u9365\u60e7\u511a\u9428\u520b\u9367\u612d\u7223\u9286\ufffd */
	public short faceXInImg;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u934f\u3126\u6ad9\u9365\u60e7\u511a\u9428\u524f\u9367\u612d\u7223 */
	public short faceYInImg;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u934f\u3126\u6ad9\u9365\u60e7\u511a\u7039\u85c9\u5bb3 */
	public short faceWInImg;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u934f\u3126\u6ad9\u9365\u60e7\u511a\u6942\u6a3a\u5bb3 */
	public short faceHInImg;
	/** \u6d5c\u9e3f\u52af\u9365\u6485\u7d1d\u9417\u7470\u5553\u9365\u60e7\u511a\u93cd\u56e7\u7e54\u951b\u5c7e\u69f8\u935a\ufe40\u5bd8\u935a\ue0a4\u58d2\u9350\u6b0f\u6d58\u934d\u5fcb\ufffd\ufffd0\u951b\u6c2b\u7b09\u9356\u546d\u60c8\u9417\u7470\u5553\u9365\u60e7\u511a\u9286\u509e\u6f6a0\u951b\u6c2c\u5bd8\u935a\ue0a4\u58d2\u9350\u6b0f\u6d58\u934d\u5fcb\ufffd\ufffd */
	public int existFaceImg;
	/**
	 * \u6d5c\u9e3f\u52af\u9365\u60e7\u511a\u704f\u4f7d\ue5ca\u93cd\u714e\u7d21\u9286\ufffd<br>
	 * C type : char[4]
	 */
	public byte[] faceImgFormat = new byte[4];
	/** \u9417\u7470\u5553\u9365\u60e7\u511a\u6fb6\u0443\u76ac */
	public int faceImgLen;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u9417\u7470\u5553\u9365\u60e7\u511a\u9428\u520b\u9367\u612d\u7223\u9286\ufffd */
	public short faceXInFaceImg;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u9417\u7470\u5553\u9365\u60e7\u511a\u9428\u524f\u9367\u612d\u7223\u9286\ufffd */
	public short faceYInFaceImg;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u9417\u7470\u5553\u9365\u60e7\u511a\u9428\u52eb\ue194\u6434\ufffd */
	public short faceWInFaceImg;
	/** \u6d5c\u9e3f\u52af\u6d63\u5d84\u7c2c\u9417\u7470\u5553\u9365\u60e7\u511a\u9428\u52ef\u73ee\u6434\ufffd */
	public short faceHInFaceImg;
	/** \u93c4\ue21a\u60c1\u9356\u546d\u60c8\u7459\u55db\ue576\u9286\ufffd0\u951b\u6c2b\u7b09\u9356\u546d\u60c8\u7459\u55db\ue576\u9286\u509e\u6f6a0\u951b\u6c2c\u5bd8\u935a\ue0a5\ue74b\u68f0\u6219\ufffd\ufffd */
	public int existVideo;
	/** \u7459\u55db\ue576\u74a7\u5cf0\ue750\u93c3\u5815\u68ff\u951b\u5822\ue757\u951b\ufffd */
	public int videoStartSec;
	/** videoStartSec\u704f\u70ac\u669f\u951b\u5c7d\u4e95\u6fe1\ufffd */
	public int videoStartUsec;
	/** \u7459\u55db\ue576\u7f01\u64b4\u6f6b\u93c3\u5815\u68ff\u951b\u5822\ue757\u951b\ufffd */
	public int videoEndSec;
	/** videoEndSec\u704f\u70ac\u669f\u951b\u5c7d\u4e95\u6fe1\ufffd */
	public int videoEndUsec;
	/**
	 * \u7459\u55db\ue576\u704f\u4f7d\ue5ca\u93cd\u714e\u7d21\u9286\ufffd<br>
	 * C type : char[4]
	 */
	public byte[] videoFormat = new byte[4];
	/** \u7459\u55db\ue576\u6fb6\u0443\u76ac */
	public int videoLen;
	/** \u93ac\u0443\u57c6 0: \u93c3\u72b3\ue11d\u6dc7\u2103\u4f05 1\u951b\u6c31\u657a 2\u951b\u6c2c\u30b3 */
	public byte sex;
	/** \u9a9e\u64ae\u7dde 0: \u93c3\u72b3\ue11d\u6dc7\u2103\u4f05 \u934f\u8dfa\u7560\u934a\u7877\u7d30\u9a9e\u64ae\u7dde */
	public byte age;
	/** \u741b\u3126\u510f 0: \u93c3\u72b3\ue11d\u6dc7\u2103\u4f05 \u934f\u8dfa\u7560\u934a\u7877\u7d30\u93c6\u509b\u6e6d\u7039\u6c2b\u7b9f */
	public byte expression;
	/** \u9472\u3088\u58ca 0: \u93c3\u72b3\ue11d\u6dc7\u2103\u4f05 \u934f\u8dfa\u7560\u934a\u7877\u7d30\u93c6\u509b\u6e6d\u7039\u6c2b\u7b9f */
	public byte skinColour;
	/** \u5a09\u3125\u553d\u93cd\u56e7\u566f\u9352\u55d8\u669f\u951b\u5c7d\u578e\u93c1\u62cc\u79fa\u6942\u6a3f\u79fa\u95ab\u509a\u608e\u9422\u3126\u6f75\u5a09\u3125\u553d */
	public byte qValue;
	/**
	 * \u6dc7\u6fc8\u6680<br>
	 * C type : char[123]
	 */
	public byte[] resv = new byte[123];
	/**
	 * \u934f\u3126\u6ad9\u9365\u60e7\u511a\u93c1\u7248\u5d41<br>
	 * C type : unsigned char*
	 */
	public Pointer img;
	/**
	 * \u9417\u7470\u5553\u9365\u60e7\u511a\u93c1\u7248\u5d41<br>
	 * C type : unsigned char*
	 */
	public Pointer faceImg;
	/**
	 * \u7459\u55db\ue576\u93c1\u7248\u5d41<br>
	 * C type : unsigned char*
	 */
	public Pointer video;
	/** \u8930\u64b3\u58a0\u93b6\u64b4\u5abf\u6d5c\u9e3f\u52af\u9428\u52ed\u58d2\u5bf0\u4f79\u669f\u93b9\ue1bc\u3047\u704f\ufffd */
	public int feature_size;
	/**
	 * \u8930\u64b3\u58a0\u93b6\u64b4\u5abf\u6d5c\u9e3f\u52af\u9428\u52ed\u58d2\u5bf0\u4f79\u669f\u93b9\ufffd<br>
	 * C type : float*
	 */
	public FloatByReference feature;
	/** \u59af\u2103\u6f98\u6d5c\u9e3f\u52af\u9365\u60e7\u511a\u95c0\u57ae\u5bb3 */
	public int modelFaceImgLen;
	/**
	 * \u59af\u2103\u6f98\u6d5c\u9e3f\u52af\u9365\u60e7\u511a\u7eeb\u8bf2\u7037<br>
	 * C type : char[4]
	 */
	public byte[] modelFaceImgFmt = new byte[4];
	/**
	 * \u59af\u2103\u6f98\u6d5c\u9e3f\u52af\u9365\u60e7\u511a\u93c1\u7248\u5d41<br>
	 * C type : unsigned char*
	 */
	public Pointer modelFaceImg;
	public FaceRecoInfo() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("sequence", "camId", "posId", "posName", "tvSec", "tvUsec", "isRealtimeData", "matched", "matchPersonId", "matchPersonName", "matchRole", "existImg", "imgFormat", "imgLen", "faceXInImg", "faceYInImg", "faceWInImg", "faceHInImg", "existFaceImg", "faceImgFormat", "faceImgLen", "faceXInFaceImg", "faceYInFaceImg", "faceWInFaceImg", "faceHInFaceImg", "existVideo", "videoStartSec", "videoStartUsec", "videoEndSec", "videoEndUsec", "videoFormat", "videoLen", "sex", "age", "expression", "skinColour", "qValue", "resv", "img", "faceImg", "video", "feature_size", "feature", "modelFaceImgLen", "modelFaceImgFmt", "modelFaceImg");
	}
	public static class ByReference extends FaceRecoInfo implements Structure.ByReference {
		
	};
	public static class ByValue extends FaceRecoInfo implements Structure.ByValue {
		
	};
}