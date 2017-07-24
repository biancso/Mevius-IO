package biancso.mevius.utils.cipher;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import biancso.mevius.utils.cipher.exceptions.IllegalMeviusKeyException;
import biancso.mevius.utils.cipher.exceptions.InvalidCipherTypeException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedEncryptTargetException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipher {
	private String stringdata;
	private byte[] bytedata;
	private MeviusCipherAction action;

	public MeviusCipher(MeviusCipherKey key, MeviusCipherAction action, Object toEncrypt)
			throws IllegalMeviusKeyException, UnsupportedMeviusKeyException, UnsupportedEncryptTargetException,
			InvalidKeyException, IllegalBlockSizeException, InvalidCipherTypeException {
		if (!isSupportedTarget(toEncrypt))
			throw new UnsupportedEncryptTargetException(toEncrypt.getClass().getSimpleName() + " is not supported!");
		this.action = action;
		switch (key.getKeyType().getType()) {
		case "rsa":
			rsaaction(key, toEncrypt);
			break;
		case "aes256":
			aes256action(key, toEncrypt);
			break;
		default:
			throw new InvalidCipherTypeException(key.getKeyType().name() + " is not valid !");
		}
	}

	public MeviusCipher(MeviusCipherType type, String toEncrypt) throws InvalidCipherTypeException {
		switch (type.getType()) {
		case "md5":
			md5action(toEncrypt);
			break;
		case "sha1":
			sha1action(toEncrypt);
			break;
		case "sha256":
			sha256action(toEncrypt);
			break;
		default:
			throw new InvalidCipherTypeException(type.name() + " is not valid !");
		}
	}

	protected byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	protected String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	private void md5action(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte[] buff = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < buff.length; i++) {
				sb.append(Integer.toString((buff[i] & 0xff) + 0x100, 16).substring(1));
			}
			stringdata = sb.toString();
			bytedata = new byte[] {};
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sha1action(String s) {
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(s.getBytes());
			stringdata = new BigInteger(1, crypt.digest()).toString(16);
			bytedata = new byte[] {};
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void sha256action(String s) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] h = digest.digest(s.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < h.length; i++) {
				String hex = Integer.toHexString(0xff & h[i]);
				if (hex.length() == 1)
					sb.append('0');
				sb.append(hex);
			}
			stringdata = sb.toString();
			bytedata = new byte[] {};
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void aes256action(MeviusCipherKey k, Object o) throws InvalidKeyException, IllegalBlockSizeException {
		try {
			String iv = ((String) k.getKey()).substring(0, 16);
			byte[] keyBytes = new byte[16];
			byte[] b = ((String) k.getKey()).getBytes();
			int len = b.length;
			if (len > keyBytes.length)
				len = keyBytes.length;
			System.arraycopy(b, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			if (action.getAction() == 0) {
				c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
				byte[] encrypted, toencrypt;
				if (o instanceof String) {
					toencrypt = ((String) o).getBytes();
				} else {
					toencrypt = (byte[]) o;
				}
				encrypted = c.doFinal(toencrypt);
				stringdata = new String(Base64.getEncoder().encode(encrypted));
				bytedata = encrypted;
			} else {
				c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
				byte[] decrypted, todecrypt;
				if (o instanceof String) {
					todecrypt = Base64.getDecoder().decode(((String) o).getBytes());
				} else {
					todecrypt = (byte[]) o;
				}
				decrypted = c.doFinal(todecrypt);
				stringdata = new String(decrypted);
				bytedata = decrypted;
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void rsaaction(MeviusCipherKey k, Object o) throws InvalidKeyException, IllegalBlockSizeException {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
			KeyPair kp = k.getKey();
			if (action.getAction() == 0) {
				byte[] strb; // need to fix it more comfortable
				if (o instanceof String) {
					strb = ((String) o).getBytes();
				} else {
					strb = (byte[]) o;
				}
				cipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
				byte[] cf = cipher.doFinal(strb);
				stringdata = byteArrayToHex(cf);
				bytedata = cf;
			} else {
				cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
				byte[] strb; // need to fix it more comfortable
				if (o instanceof String) {
					strb = hexToByteArray(((String) o));
				} else {
					strb = (byte[]) o;
				}
				byte[] cf = cipher.doFinal(strb);
				stringdata = new String(cf);
				bytedata = cf;
			}
		} catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | BadPaddingException e) {
			e.printStackTrace();
		}
	}

	private boolean isSupportedTarget(Object toEncrypt) {
		return true;
	}

	public byte[] toBytes() {
		return bytedata;
	}

	public String toString() {
		return stringdata;
	}
}
