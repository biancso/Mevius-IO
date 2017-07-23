package biancso.mevius.utils.cipher.exceptions;

@SuppressWarnings("serial")
public class UnsupportedMeviusKeyException extends MeviusCipherException {

	public UnsupportedMeviusKeyException(String reason) {
		super(reason);
	}
}
