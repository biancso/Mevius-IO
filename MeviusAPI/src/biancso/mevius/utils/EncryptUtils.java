package biancso.mevius.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {

	public static class RSA {
		public static class KEY {
			public static KeyPair generateKeyPair(int keysize)
					throws NoSuchAlgorithmException, NoSuchProviderException {
				SecureRandom random = new SecureRandom();
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "SunJSSE");
				kpg.initialize(keysize, random);
				KeyPair kp = kpg.generateKeyPair();
				return kp;
			}
		}

		public static class ENCRYPT {
			public static String encodeToString(PublicKey publickey, String str)
					throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
					NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
				byte[] strb = str.getBytes();
				cipher.init(Cipher.ENCRYPT_MODE, publickey);
				return byteArrayToHex(cipher.doFinal(strb));
			}

			public static byte[] encodeToByte(PublicKey publickey, String str)
					throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
					InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
				byte[] strb = str.getBytes();
				cipher.init(Cipher.ENCRYPT_MODE, publickey);
				return cipher.doFinal(strb);
			}

			public static String encodeToString(PublicKey publickey, byte[] str)
					throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
					InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
				cipher.init(Cipher.ENCRYPT_MODE, publickey);
				return byteArrayToHex(cipher.doFinal(str));
			}

			public static byte[] encodeToByte(PublicKey publickey, byte[] str)
					throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
					NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
				cipher.init(Cipher.ENCRYPT_MODE, publickey);
				return cipher.doFinal(str);
			}
		}

		public static class DECRYPT {
			public static String decodeToString(PrivateKey privatekey, String str)
					throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
					NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
				cipher.init(Cipher.DECRYPT_MODE, privatekey);
				return new String(cipher.doFinal(hexToByteArray(str)));
			}
		}

		protected static byte[] hexToByteArray(String hex) {
			if (hex == null || hex.length() == 0) {
				return null;
			}
			byte[] ba = new byte[hex.length() / 2];
			for (int i = 0; i < ba.length; i++) {
				ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
			}
			return ba;
		}

		protected static String byteArrayToHex(byte[] ba) {
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

	}

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
