package biancso.mevius.packet.file;

@SuppressWarnings("serial")
public class PacketRSAFile implements MeviusTransferableFile {
	private final byte[] filearr;

	protected PacketRSAFile(byte[] filearr) {
		this.filearr = filearr;
	}

	@Override
	public byte[] getFileByte() {
		return filearr;
	}

}
