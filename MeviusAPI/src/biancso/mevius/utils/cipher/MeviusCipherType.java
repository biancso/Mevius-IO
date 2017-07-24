package biancso.mevius.utils.cipher;

import java.security.KeyPair;

public enum MeviusCipherType {
	RSA("rsa", KeyPair.class), AES256("aes256", String.class);
	
	private final String type;
	private final Class<?> keytype;
	private MeviusCipherType(String type, Class<?> keytype) {
		this.type = type;
		this.keytype = keytype;
	}

	public String getType() {
		return type;
	}
	
	public final Class<?> getKeyType() {
		return keytype;
	}
	
}
