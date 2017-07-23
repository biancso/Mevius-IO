package biancso.mevius.packet;

@SuppressWarnings("serial")
public class MeviusTextPacket extends MeviusPacket {
	private final String data;

	public MeviusTextPacket(String data) {
		this.data = data;
	}

	public int getLength() {
		return data.length();
	}

	public void trim() {
		data.trim();
	}

	public int indexOf(String str) {
		return data.indexOf(str);
	}

	public boolean startsWith(String str) {
		return data.startsWith(str);
	}

	public boolean endsWith(String str) {
		return data.endsWith(str);
	}

	public final String toString() {
		return data;
	}
}
