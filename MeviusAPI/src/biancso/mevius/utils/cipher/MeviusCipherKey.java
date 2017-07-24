package biancso.mevius.utils.cipher;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

import javax.management.RuntimeErrorException;

import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipherKey {
	private Object key;
	private MeviusCipherType type;

	public MeviusCipherKey(Object key, MeviusCipherType type) {
		this.key = key;
		this.type = type;
	}

	public static MeviusCipherKey randomRSAKeyPair(int keysize) {
		SecureRandom random = new SecureRandom();
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA", "SunJSSE");
			kpg.initialize(keysize, random);
			KeyPair kp = kpg.generateKeyPair();
			return new MeviusCipherKey(kp, MeviusCipherType.RSA);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeErrorException(new Error("Unhandled error while creating new RSAKeyPair"));
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
