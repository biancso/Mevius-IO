package biancso.mevius.packet.exceptions;

public class FailedToDecryptPacketException extends Exception {

	private static final long serialVersionUID = -6406058337599721381L;

	public FailedToDecryptPacketException(String msg) {
		super(msg);
	}
}
