package biancso.mevius.nio.exceptions;

@SuppressWarnings("serial")
public class IllegalMeviusKeyException extends MeviusCipherException {

	public IllegalMeviusKeyException(String reason) {
		super(reason);
	}
}
