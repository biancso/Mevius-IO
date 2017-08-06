package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusImagePacket;
import biancso.mevius.packet.MeviusPacket;

public class ImagePacketEvent extends PacketEvent {

	public ImagePacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType receive) {
		super(packet, client, receive);
	}

	public MeviusImagePacket getPacket() {
		return getAlternativePacket();
	}
}
