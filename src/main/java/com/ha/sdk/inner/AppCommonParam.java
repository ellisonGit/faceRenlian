package main.java.com.ha.sdk.inner;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * \u5e94\u7528\u7a0b\u5e8f\u901a\u7528\u53c2\u6570(512\u5b57\u8282)\u3002<br>
 * <i>native declaration : config_gw.h:794</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AppCommonParam extends Structure {
	/**
	 * --------|-32\u5b57\u8282--|<br>
	 * C type : AppServicesParam
	 */
	public AppServicesParam services;
	/**
	 * -----|-224\u5b57\u8282-|<br>
	 * C type : DescriptionParam
	 */
	public DescriptionParam description;
	/**
	 * -----------|-128\u5b57\u8282--|<br>
	 * C type : ExtranetParam
	 */
	public ExtranetParam extranet;
	/**
	 * \u4fdd\u7559\u5b57\u6bb5\u3002<br>
	 * --------------------------|-128\u5b57\u8282-|<br>
	 * C type : char[128]
	 */
	public byte[] resv = new byte[128];
	public AppCommonParam() {
		super();
	}
	protected List<String>getFieldOrder() {
		return Arrays.asList("services", "description", "extranet", "resv");
	}
	/**
	 * @param services --------|-32\u5b57\u8282--|<br>
	 * C type : AppServicesParam<br>
	 * @param description -----|-224\u5b57\u8282-|<br>
	 * C type : DescriptionParam<br>
	 * @param extranet -----------|-128\u5b57\u8282--|<br>
	 * C type : ExtranetParam<br>
	 * @param resv \u4fdd\u7559\u5b57\u6bb5\u3002<br>
	 * --------------------------|-128\u5b57\u8282-|<br>
	 * C type : char[128]
	 */
	public AppCommonParam(AppServicesParam services, DescriptionParam description, ExtranetParam extranet, byte resv[]) {
		super();
		this.services = services;
		this.description = description;
		this.extranet = extranet;
		if ((resv.length != this.resv.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.resv = resv;
	}
	public static class ByReference extends AppCommonParam implements Structure.ByReference {
		
	};
	public static class ByValue extends AppCommonParam implements Structure.ByValue {
		
	};
}