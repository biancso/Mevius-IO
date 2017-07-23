package biancso.mevius.utils;

public class StringKeyPair {
	private final String publicKey;
	private final String privateKey;

	protected StringKeyPair(String publickey, String privatekey) {
		this.publicKey = publickey;
		this.privateKey = privatekey;
	}

	public StringKeyPair(String keypairstring) {
		String[] k = keypairstring.split("|=|");
		publicKey = k[0];
		privateKey = k[1];
	}

	public String getPublicKeyString() {
		return publicKey;
	}

	public String getPrivateKeyString() {
		return privateKey;
	}

	public String toString() {
		return publicKey + "|=|" + privateKey;
	}
}
