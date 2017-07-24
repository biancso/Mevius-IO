package biancso.mevius.utils.cipher;

import java.util.Random;

import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipherKey {
	private Object key;
	private MeviusCipherType type;

	public MeviusCipherKey(Object key, MeviusCipherType type) {
		this.key = key;
		this.type = type;
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
		return new MeviusCipherKey(sb.toString(), MeviusCipherType.AES256);
	}

	public MeviusCipherType getKeyType() throws UnsupportedMeviusKeyException {
		return type;
	}

	@SuppressWarnings("unchecked")
	public <T> T getKey() {
		return (T) type.getKeyType().cast(key);
	} // !!!! MeviusCipherType.RSA will return KeyPair
}
