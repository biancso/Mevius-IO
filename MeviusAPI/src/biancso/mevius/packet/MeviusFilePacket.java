package biancso.mevius.packet;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("serial")
public class MeviusFilePacket extends MeviusPacket {
	private byte[] b;

	// USAGE
	// MeviusFilePacket packet = new MeviusFilePacket(new File("C:\\TESTFILE"));
	public MeviusFilePacket(File file) throws IOException {
		ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(file.toPath()));
		b = buffer.array();
	}

	public void write(File dest) throws IOException {
		Files.createDirectories(dest.toPath());
		FileChannel fc = FileChannel.open(dest.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		fc.write(ByteBuffer.wrap(b));
		fc.close();
	}
}
