package biancso.mevius.packet.events;

import biancso.mevius.nio.MeviusClient;
import biancso.mevius.packet.MeviusEncryptedPacket;
import biancso.mevius.packet.MeviusPacket;

public class EncryptedPacketEvent extends PacketEvent {

	public EncryptedPacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		super(packet, client, type);
		// TODO Auto-generated constructor stub
	}

	public MeviusEncryptedPacket getPacket() {
		return getAlternativePacket();
	}
}
