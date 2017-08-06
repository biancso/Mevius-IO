package biancso.mevius.packet;

import java.io.Serializable;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.ObjectPacketEvent;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;

public class MeviusRawObjectPacket
extends MeviusPacket {

	private static final long serialVersionUID = -8617889841248224121L;

	private final Serializable data;
	
	public MeviusRawObjectPacket(Serializable data) {
		this.data = data;
	}
	
	public Serializable getData() {
		return data;
	}

	@Override
	public PacketEvent createEvent(MeviusClient client, PacketEventType type) {
		return new ObjectPacketEvent(this, client, type);
	}
}
