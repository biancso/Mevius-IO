package biancso.mevius.packet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("serial")
public class MeviusFilePacket extends MeviusPacket {
	private final byte[] filedata;

	public MeviusFilePacket(File file) throws IOException {
		filedata = Files.readAllBytes(file.toPath());
	}

	public void write(File file) throws IOException {
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(filedata, 0, filedata.length);
		fos.flush();
		fos.close();
	}
}
