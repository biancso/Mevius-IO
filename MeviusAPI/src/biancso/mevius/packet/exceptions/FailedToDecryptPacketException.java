package biancso.mevius.packet.exceptions;

@SuppressWarnings("serial")
public class FailedToDecryptPacketException extends Exception {

	public FailedToDecryptPacketException(String msg) {
		super(msg);
	}
}
