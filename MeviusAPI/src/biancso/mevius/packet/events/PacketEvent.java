package biancso.mevius.packet.events;

import java.util.Date;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusPacket;

public abstract class PacketEvent {
	private final MeviusPacket packet;
	private final boolean isreceived;
	private final MeviusClient client;
	private final Date date;

	public PacketEvent(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		this.packet = packet;
		this.client = client;
		this.isreceived = type.equals(PacketEventType.RECEIVE);
		this.date = new Date();
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

	public Date getWhen() {
		return date;
	}
}
