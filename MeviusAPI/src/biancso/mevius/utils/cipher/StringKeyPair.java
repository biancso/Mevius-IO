package biancso.mevius.utils.cipher;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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
	
	public static StringKeyPair toStringKeyPair(KeyPair keypair) {
		return new StringKeyPair(Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded()),
				Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded()));
	}
	
	public KeyPair toKeyPair(StringKeyPair keypair)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				Base64.getDecoder().decode(keypair.getPublicKeyString()));
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				Base64.getDecoder().decode(keypair.getPrivateKeyString()));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return new KeyPair(kf.generatePublic(publicKeySpec), kf.generatePrivate(privateKeySpec));
	}
}
