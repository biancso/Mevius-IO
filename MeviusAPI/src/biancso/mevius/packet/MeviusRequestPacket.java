package biancso.mevius.packet;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.packet.events.RequestPacketEvent;

public class MeviusRequestPacket extends MeviusResponsablePacket implements Cloneable {

	private static final long serialVersionUID = 2898929739377309481L;

	private final String requestdata;

	public MeviusRequestPacket(String requestdata) {
		this.requestdata = requestdata;
	}

	public final String getRequestData() {
		return this.requestdata;
	}

	public MeviusResponsePacket response(String data) {
		return new MeviusResponsePacket(this, data);
	}

	@Override
	public PacketEvent createEvent(MeviusClient client, PacketEventType type) {
		return new RequestPacketEvent(this, client, type);
	}

}
