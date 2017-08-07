package biancso.mevius.packet;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("serial")
public class MeviusResponsePacket extends MeviusPacket {
	private final String requestadata;
	private final String responsedata;
	private Date timeout;
	private final UUID uuid;

	protected MeviusResponsePacket(MeviusRequestPacket mrp, String responsedata) {
		Field f;
		try {
			f = mrp.getClass().getSuperclass().getDeclaredField("timeout");
			f.setAccessible(true);
			timeout = (Date) f.get(mrp);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.uuid = mrp.getUUId();
		this.requestadata = mrp.getRequestData();
		this.responsedata = responsedata;
	}

	public final String getResponseData() {
		return this.responsedata;
	}

	public final String getRequestData() {
		return this.requestadata;
	}

	public final boolean isOutDated() {
		return timeout.before(new Date());
	}

	public final UUID getUUId() {
		return this.uuid;
	}
}
