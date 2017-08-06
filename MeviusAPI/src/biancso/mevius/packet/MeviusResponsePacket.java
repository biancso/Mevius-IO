package biancso.mevius.packet;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.packet.events.RequestPacketEvent;

public class MeviusResponsePacket extends MeviusPacket {

	private static final long serialVersionUID = -7883845014219622263L;

	private final String requestadata;
	private final String responsedata;
	private Date timeout;
	private final UUID uuid;

	protected MeviusResponsePacket(MeviusRequestPacket mrp, String responsedata) {
		Field f;
		try {
			f = MeviusResponsePacket.class.getDeclaredField("timeout");
			timeout = (Date) f.get(mrp);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
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

	@Override
	public PacketEvent createEvent(MeviusClient client, PacketEventType type) {
		return new RequestPacketEvent(this, client, type);
	}
}
