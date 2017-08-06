package biancso.mevius.packet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.FilePacketEvent;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;

public class MeviusFilePacket extends MeviusPacket {

	private static final long serialVersionUID = -5974370856104052770L;

	private byte[] filebytearr;

	// USAGE
	// MeviusFilePacket packet = new MeviusFilePacket(new File("C:\\TESTFILE"));
	public MeviusFilePacket(File file) throws IOException {
		filebytearr = Files.readAllBytes(file.toPath());
	}

	public void write(File dest) throws IOException {
		FileOutputStream fos = new FileOutputStream(dest);
		fos.write(filebytearr);
		fos.flush();
		fos.close();
	}

	@Override
	public PacketEvent createEvent(MeviusClient client, PacketEventType type) {
		return new FilePacketEvent(this, client, type);
	}
}
