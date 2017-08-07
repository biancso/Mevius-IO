package biancso.mevius.packet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("serial")
public class MeviusFilePacket extends MeviusPacket {
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
}
