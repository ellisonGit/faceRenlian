package main.java.com.ha.facecamera.configserver.pkgs;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.Face;

public class ModifyFaceRequest extends AddFaceRequest {

	public ModifyFaceRequest(Face _face) {
		super(_face);
		messageID = Constants.MESSAGE_ID_MODIFYFACE;
	}
	
}
