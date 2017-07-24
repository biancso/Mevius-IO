package biancso.mevius.utils.cipher;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
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
import biancso.mevius.utils.cipher.exceptions.UnsupportedEncryptTargetException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipher {
	private byte[] encodedbytedata;
	private byte[] plainbytedata;
	private String plainstringdata;
	private String encodedstringdata;
	private MeviusCipherAction action;

	public MeviusCipher(MeviusCipherKey key, MeviusCipherAction action,  Object toEncrypt)
			throws IllegalMeviusKeyException, UnsupportedMeviusKeyException, UnsupportedEncryptTargetException,
			InvalidKeyException, IllegalBlockSizeException {
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
				encodedstringdata = new String(Base64.getEncoder().encode(encrypted));
				encodedbytedata = encrypted;
			} else {
				c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
				byte[] decrypted, todecrypt;
				if (o instanceof String) {
					todecrypt = Base64.getDecoder().decode(((String) o).getBytes());
				} else {
					todecrypt = (byte[]) o;
				}
				decrypted = c.doFinal(todecrypt);
				plainstringdata = new String(decrypted);
				plainbytedata = decrypted;
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
				encodedstringdata = byteArrayToHex(cf);
				encodedbytedata = cf;
			} else {
				cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
				byte[] strb; // need to fix it more comfortable
				if (o instanceof String) {
					strb = hexToByteArray(((String) o));
				} else {
					strb = (byte[]) o;
				}
				byte[] cf = cipher.doFinal(strb);
				plainstringdata = new String(cf);
				plainbytedata = cf;
			}
		} catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | BadPaddingException e) {
			e.printStackTrace();
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
