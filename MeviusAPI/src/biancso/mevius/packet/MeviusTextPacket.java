package biancso.mevius.packet;

import java.nio.charset.Charset;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.packet.events.TextPacketEvent;

public class MeviusTextPacket extends MeviusPacket {

	private static final long serialVersionUID = 4718733364936917047L;

	private final byte[] data;

	public MeviusTextPacket(String data) {
		this.data = data.getBytes();
	}

	public int getLength() {
		return data.length;
	}

	public final String toString() {
		return new String(data);
	}

	public final String toString(Charset charset) {
		return new String(data, charset);
	}

	public final byte[] getBytes() {
		return data;
	}

	@Override
	public PacketEvent createEvent(MeviusClient client, PacketEventType type) {
		return new TextPacketEvent(this, client, type);
	}
}
