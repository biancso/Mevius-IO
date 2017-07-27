package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusFilePacket;
import biancso.mevius.packet.MeviusPacket;

public class FilePacketEvent extends PacketEvent {

	public FilePacketEvent(MeviusPacket packet, MeviusClient client, boolean receive) {
		super(packet, client, receive);
		// TODO Auto-generated constructor stub
	}

	public MeviusFilePacket getPacket() {
		return getAlternativePacket();
	}
}
