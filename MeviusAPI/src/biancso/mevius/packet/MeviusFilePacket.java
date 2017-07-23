package biancso.mevius.packet;

import java.io.File;
import java.io.IOException;

import biancso.mevius.packet.file.PacketFile;

@SuppressWarnings("serial")
public class MeviusFilePacket extends MeviusPacket {
	private final PacketFile file;

	@Deprecated
	public MeviusFilePacket(File file) throws IOException {
		this.file = new PacketFile(file);
	}

	public MeviusFilePacket(PacketFile file) {
		this.file = file;
	}

}
