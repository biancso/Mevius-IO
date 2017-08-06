package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusResponsePacket;

public class ResponsePacketEvent extends PacketEvent {

	public ResponsePacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		super(packet, client, type);
	}

	public MeviusResponsePacket getPacket() {
		return getAlternativePacket();
	}
}
