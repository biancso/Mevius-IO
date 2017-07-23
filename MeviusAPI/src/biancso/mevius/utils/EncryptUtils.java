package biancso.mevius.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {

	public static class MD5 {
		public static String encode(String str) throws NoSuchAlgorithmException {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] buff = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < buff.length; i++) {
				sb.append(Integer.toString((buff[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		}
	}

	public static class SHA256 {
		public static String encode(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] h = digest.digest(str.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < h.length; i++) {
				String hex = Integer.toHexString(0xff & h[i]);
				if (hex.length() == 1)
					sb.append('0');
				sb.append(hex);
			}
			return sb.toString();
		}
	}

	public static class SHA1 {
		public static String encode(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));

			return new BigInteger(1, crypt.digest()).toString(16);
		}
	}

	public static class AES {

		public static String randomAESKey() {
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
			return sb.toString();
		}

		public static String Encode256(String key, String str) throws UnsupportedEncodingException,
				NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
				InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			String iv = key.substring(0, 16);

			byte[] keyBytes = new byte[16];
			byte[] b = key.getBytes("UTF-8");
			int len = b.length;
			if (len > keyBytes.length)
				len = keyBytes.length;
			System.arraycopy(b, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

			byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
			String enStr = new String(Base64.getEncoder().encode(encrypted));

			return enStr;
		}

		public static String Decode256(String key, String str) throws UnsupportedEncodingException,
				NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
				InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			String iv = key.substring(0, 16);
			
			byte[] keyBytes = new byte[16];
			byte[] b = key.getBytes("UTF-8");
			int len = b.length;
			if (len > keyBytes.length)
				len = keyBytes.length;
			System.arraycopy(b, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));

			byte[] byteStr = Base64.getDecoder().decode(str.getBytes());

			return new String(c.doFinal(byteStr), "UTF-8");
		}
	}
}
