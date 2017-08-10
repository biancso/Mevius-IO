package biancso.mevius.nio.exceptions;

import java.io.IOException;

@SuppressWarnings("serial")
public class MeviusException extends IOException {

	public MeviusException(String str) {
		super(str);
	}
}
