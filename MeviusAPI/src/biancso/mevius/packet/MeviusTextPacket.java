package biancso.mevius.packet;

import java.nio.charset.Charset;

@SuppressWarnings("serial")
public class MeviusTextPacket extends MeviusPacket {
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
}
