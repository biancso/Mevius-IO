package biancso.mevius.nio.exceptions;

@SuppressWarnings("serial")
public class UnsupportedMeviusKeyException extends MeviusCipherException {

	public UnsupportedMeviusKeyException(String reason) {
		super(reason);
	}
}
