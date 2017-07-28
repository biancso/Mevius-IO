package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusTextPacket;

public class TextPacketEvent extends PacketEvent {

	public TextPacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		super(packet, client, type);
		// TODO Auto-generated constructor stub
	}

	public MeviusTextPacket getPacket() {
		return getAlternativePacket();
	}

}
