package biancso.mevius.packet.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("serial")
public class PacketFile implements MeviusTransferableFile {
	private byte[] filebytearr;

	public PacketFile(File file) throws IOException {
		filebytearr = Files.readAllBytes(file.toPath());
	}

	@Override
	public byte[] getFileByte() {
		// TODO Auto-generated method stub
		return filebytearr;
	}
	
	
}
