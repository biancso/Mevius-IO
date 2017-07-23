package biancso.mevius.utils.cipher;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipherKey {
	private final Key key;

	public MeviusCipherKey(Key key) {
		this.key = key;
	}

	public MeviusCipherType getKeyType() throws UnsupportedMeviusKeyException {
		if (key instanceof PrivateKey || key instanceof PublicKey)
			return MeviusCipherType.RSA;
		throw new UnsupportedMeviusKeyException(key.getClass().getName() + " is not supported!");
	}

	public PrivateKey getRSAPrivateKey() {
		return (PrivateKey) key;
	}

	public PublicKey getRSAPublicKey() {
		return (PublicKey) key;
	}
}
