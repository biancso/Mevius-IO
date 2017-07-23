package biancso.mevius.utils.cipher;

import biancso.mevius.utils.cipher.exceptions.IllegalMeviusKeyException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipher {
	private byte[] bytedata;
	private String plaindata;

	public MeviusCipher(MeviusCipherType type, MeviusCipherAction action, MeviusCipherKey key, Object toEncrypt)
			throws UnsupportedMeviusKeyException, IllegalMeviusKeyException {
		if (!key.getKeyType().equals(type))
			throw new IllegalMeviusKeyException(key.getKeyType().name() + " is not matched with " + type.name());
		switch (action.getAction()) {
		case 0: // on encrypt
			switch (type.getType()) {
			case "rsa":
				rsaencrypt(key, toEncrypt);
				break;
			}
			break;
		case 1: // on decrypt

			break;
		}
	}

	private void rsaencrypt(MeviusCipherKey key, Object toEncrypt) {

	}

	private boolean isSupportedTarget(Object toEncrypt) {
		return true;
	}
}
