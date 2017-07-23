package biancso.mevius.utils.cipher;

import java.security.PublicKey;

import biancso.mevius.utils.cipher.exceptions.IllegalMeviusKeyException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedEncryptTargetException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipher {
	private byte[] encodedbytedata;
	private byte[] plainbytedata;
	private String plainstringdata;
	private String encodedstringdata;
	private MeviusCipherAction action;

	public MeviusCipher(MeviusCipherType type, MeviusCipherAction action, MeviusCipherKey key, Object toEncrypt)
			throws IllegalMeviusKeyException, UnsupportedMeviusKeyException, UnsupportedEncryptTargetException {
		if (!key.getKeyType().equals(type))
			throw new IllegalMeviusKeyException(key.getKeyType().name() + " is not matched with " + type.name());
		if (!isSupportedTarget(toEncrypt))
			throw new UnsupportedEncryptTargetException(toEncrypt.getClass().getSimpleName() + " is not supported!");
		this.action = action;
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

	private void rsaencrypt(MeviusCipherKey k, Object o) {
		PublicKey key = k.getRSAPublicKey();
		if (o instanceof String) {

		}
	}

	private boolean isSupportedTarget(Object toEncrypt) {
		return true;
	}

	public byte[] toBytes() {
		return action == MeviusCipherAction.ENCRYPT ? encodedbytedata : plainbytedata;
	}

	public String toString() {
		return action == MeviusCipherAction.ENCRYPT ? encodedstringdata : plainstringdata;
	}
}
