package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusObjectPacket;
import biancso.mevius.packet.MeviusPacket;

public class ObjectPacketEvent extends PacketEvent {

	public ObjectPacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		super(packet, client, type);
		// TODO Auto-generated constructor stub
	}

	public MeviusObjectPacket getPacket() {
		return getAlternativePacket();
	}
}
