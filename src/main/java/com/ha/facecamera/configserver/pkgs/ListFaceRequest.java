package main.java.com.ha.facecamera.configserver.pkgs;

import static main.java.com.ha.facecamera.configserver.Constants.i18nMessage;

import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import main.java.com.ha.facecamera.configserver.ConfigServer;
import main.java.com.ha.facecamera.configserver.ConfigServerConfig;
import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.ListFaceCriteria;
import main.java.com.ha.tlv.SerializeAdapter;
import main.java.com.ha.tlv.Util;

public class ListFaceRequest extends SerializeAdapter {
	private ListFaceCriteria listFaceCriteria;
	
	public ListFaceRequest(ListFaceCriteria _listFaceCriteria) {
		super(Constants.MESSAGE_ID_LISTFACE);
		this.listFaceCriteria = _listFaceCriteria;
	}

	@Override
	public String sendTo(IoSession session, int sysType, int majorProtocol, int minorProtocol) throws Exception {
		//if(listFaceCriteria == null || session == null) return "POJO or Session is null";
		if(listFaceCriteria.getRole() != -1 && listFaceCriteria.getRole() != 0 && listFaceCriteria.getRole() != 1 && listFaceCriteria.getRole() != 2) return i18nMessage("face.roleIllegal");
		if(listFaceCriteria.getPageNo() < 1) return i18nMessage("face.list.pageNoIllegal");
		if(listFaceCriteria.getPageSize() < 1 || listFaceCriteria.getPageSize() > 100) return i18nMessage("face.list.pageSizeIllegal");
		int xtraLen = 4;
		if(listFaceCriteria.getConditionFlag() > 0) {
			if((listFaceCriteria.getConditionFlag() & 0x1) == 0x1) {
				if(listFaceCriteria.getId() == null || listFaceCriteria.getId().isEmpty())
					return i18nMessage("face.idNotSet");
				else if(listFaceCriteria.getId().getBytes("UTF-8").length > 19)
					return i18nMessage("face.idStrLenTooLong");
				xtraLen += 20;
			}
			if((listFaceCriteria.getConditionFlag() & 0x2) == 0x2) {
				if(listFaceCriteria.getName() == null || listFaceCriteria.getName().isEmpty())
					return i18nMessage("face.list.nameNotSet");
				else if(listFaceCriteria.getName().getBytes("UTF-8").length > 19)
					return i18nMessage("face.nameStrLenTooLong");
				xtraLen += 16;
			}
			if((listFaceCriteria.getConditionFlag() & 0x4) == 0x4) {
				xtraLen += 4;
			}
			if((listFaceCriteria.getConditionFlag() & 0x10) == 0x10) {
				if(listFaceCriteria.getStartDateBegin() == null || listFaceCriteria.getStartDateEnd() == null)
					return i18nMessage("face.list.startDateNotSet");
				xtraLen += 8;
			}
			if((listFaceCriteria.getConditionFlag() & 0x8) == 0x8) {
				if(listFaceCriteria.getExpireDateBegin() == null || listFaceCriteria.getExpireDateEnd() == null)
					return i18nMessage("face.list.expireDateNotSet");
				xtraLen += 8;
			}
		}
		IoBuffer buffer = IoBuffer.allocate(28 + xtraLen);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(Util.createType(sysType, majorProtocol, minorProtocol, messageID));
        buffer.putInt(20 + xtraLen);
		buffer.putInt(listFaceCriteria.getRole());
		buffer.putInt(listFaceCriteria.getPageNo());
		buffer.putInt(listFaceCriteria.getPageSize());
		buffer.put((byte)(listFaceCriteria.isGetFeatureData()?1:0));
		buffer.put((byte)(listFaceCriteria.isGetImageData()?1:0));
		buffer.put(new byte[6]);
		buffer.putUnsignedShort(listFaceCriteria.getConditionFlag());
		buffer.putShort((short) (listFaceCriteria.isFuzzyMode()?1:0));
		if(xtraLen > 4) {
			if((listFaceCriteria.getConditionFlag() & 0x1) == 0x1) {
				byte[] idBytes = listFaceCriteria.getId().getBytes("UTF-8");
				buffer.put(idBytes);
				buffer.put(new byte[20 - idBytes.length]);
			}
			if((listFaceCriteria.getConditionFlag() & 0x2) == 0x2) {
				byte[] nameBytes = listFaceCriteria.getName().getBytes("UTF-8");
				buffer.put(nameBytes);
				buffer.put(new byte[16 - nameBytes.length]);
			}
			if((listFaceCriteria.getConditionFlag() & 0x4) == 0x4) {
				buffer.putUnsignedInt(listFaceCriteria.getWiegandNo());
			}
			if((listFaceCriteria.getConditionFlag() & 0x8) == 0x8) {
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC-8:00"));
				cal.set(1970, 0, 1, 0, 0, 0);
				buffer.putUnsignedInt((listFaceCriteria.getExpireDateBegin().getTime() - cal.getTimeInMillis()) / 1000);
				cal.set(1970, 0, 1, 0, 0, 0);
				buffer.putUnsignedInt((listFaceCriteria.getExpireDateEnd().getTime() - cal.getTimeInMillis()) / 1000);
			}
			if((listFaceCriteria.getConditionFlag() & 0x10) == 0x10) {
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC-8:00"));
				cal.set(1970, 0, 1, 0, 0, 0);
				buffer.putUnsignedInt((listFaceCriteria.getStartDateBegin().getTime() - cal.getTimeInMillis()) / 1000);
				cal.set(1970, 0, 1, 0, 0, 0);
				buffer.putUnsignedInt((listFaceCriteria.getStartDateEnd().getTime() - cal.getTimeInMillis()) / 1000);
			}
		}
        buffer.flip();
		//System.out.print(messageID + "=>");
		//System.out.println(bytesToHexFun3(buffer.array()));

		WriteFuture wf = session.write(buffer);
		if(wf.await(buffer.capacity() / ((ConfigServerConfig) session.getAttribute(ConfigServer.SESATTR_KEY_CFG)).expectBandWidth + 1, TimeUnit.SECONDS) &&
				wf.isWritten())
			return Constants.NOERROR;
		else
			return Constants.SENDTIMEOUT;
	}
}
