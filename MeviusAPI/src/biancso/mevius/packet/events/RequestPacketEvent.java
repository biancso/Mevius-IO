package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusRequestPacket;

public class RequestPacketEvent extends PacketEvent {

	public RequestPacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		super(packet, client, type);
	}

	public MeviusRequestPacket getPacket() {
		return getAlternativePacket();
	}
}
