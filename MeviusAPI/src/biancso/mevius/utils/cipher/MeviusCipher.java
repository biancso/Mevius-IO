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
		switch (type.getType()) {
		case "rsa":
			rsaaction(key, toEncrypt);
			break;
		}
	}

	private void rsaaction(MeviusCipherKey k, Object o) {
		if (action.getAction() == 0) {
			
		} else {

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
