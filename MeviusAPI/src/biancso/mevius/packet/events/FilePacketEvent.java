package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusFilePacket;
import biancso.mevius.packet.MeviusPacket;

public class FilePacketEvent extends PacketEvent {

	public FilePacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType receive) {
		super(packet, client, receive);
	}

	public MeviusFilePacket getPacket() {
		return getAlternativePacket();
	}
}
