package biancso.mevius.packet.events;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusPacket;

public abstract class PacketEvent {
	private final MeviusPacket packet;
	private final boolean isreceived;
	private final MeviusClient client;

	public PacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		this.packet = packet;
		this.client = client;
		this.isreceived = type.equals(PacketEventType.RECEIVE);
	}

	public MeviusPacket getPacket() {
		return packet;
	}

	public MeviusClient getClient() {
		return client;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAlternativePacket() {
		return (T) getPacketClass().cast(packet);
	}

	public boolean isReceivedPacket() {
		return this.isreceived;
	}

	public Class<? extends MeviusPacket> getPacketClass() {
		return packet.getClass();
	}
}
