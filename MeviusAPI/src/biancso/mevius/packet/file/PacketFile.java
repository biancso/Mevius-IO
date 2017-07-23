package biancso.mevius.packet.file;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

@SuppressWarnings("serial")
public class PacketFile implements Serializable {
	private byte[] filebytearr;

	public PacketFile(File file) throws IOException {
		filebytearr = Files.readAllBytes(file.toPath());
	}

	private PacketFile(byte[] filebytearr) {
		this.filebytearr = filebytearr;
	}

	public byte[] getFileByte() {
		// TODO Auto-generated method stub
		return filebytearr;
	}

	public PacketFile encrypt() {
		return new PacketFile(this.filebytearr);
	}

	// MEGABYTE
	public long getFileSize() {
		return filebytearr.length / 2048;
	}

}
