package biancso.mevius.utils;

public enum MeviusCipherType {
	RSA("rsa"), AES256("aes256");

	private final String type;

	private MeviusCipherType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
