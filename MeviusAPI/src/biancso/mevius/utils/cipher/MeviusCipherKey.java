package biancso.mevius.utils.cipher;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;

import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipherKey {
	private Key key;
	private String skey;

	public MeviusCipherKey(Key key) {
		this.key = key;
	}

	public MeviusCipherKey(String key) {
		skey = key;
	}

	public static MeviusCipherKey randomAES256Key() {
		String bigChar = "QWERTYUIOPASDFGHJKLZXCVBNM";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			switch (rnd.nextInt(2) + 1) {
			case 1:
				sb.append(bigChar.charAt(rnd.nextInt(bigChar.length())));
				break;
			case 2:
				sb.append(bigChar.toLowerCase().charAt(rnd.nextInt(bigChar.length())));
				break;
			}
		}
		return new MeviusCipherKey(sb.toString());
	}

	public MeviusCipherType getKeyType() throws UnsupportedMeviusKeyException {
		if (key instanceof PrivateKey || key instanceof PublicKey)
			return MeviusCipherType.RSA;
		throw new UnsupportedMeviusKeyException(key.getClass().getName() + " is not supported!");
	}

	public String getAES256Key() {
		return skey;
	}

	public PrivateKey getRSAPrivateKey() {
		return (PrivateKey) key;
	}

	public PublicKey getRSAPublicKey() {
		return (PublicKey) key;
	}

}
