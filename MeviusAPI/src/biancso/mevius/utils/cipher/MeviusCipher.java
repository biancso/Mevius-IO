package biancso.mevius.utils.cipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import biancso.mevius.utils.cipher.exceptions.IllegalMeviusKeyException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedEncryptTargetException;
import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

public class MeviusCipher {
	private byte[] encodedbytedata;
	private byte[] plainbytedata;
	private String plainstringdata;
	private String encodedstringdata;
	private MeviusCipherAction action;

	public MeviusCipher(MeviusCipherType type, MeviusCipherAction action, MeviusCipherKey key, Object toEncrypt)
			throws IllegalMeviusKeyException, UnsupportedMeviusKeyException, UnsupportedEncryptTargetException,
			InvalidKeyException, IllegalBlockSizeException {
		if (!key.getKeyType().equals(type))
			throw new IllegalMeviusKeyException(key.getKeyType().name() + " is not matched with " + type.name());
		if (!isSupportedTarget(toEncrypt))
			throw new UnsupportedEncryptTargetException(toEncrypt.getClass().getSimpleName() + " is not supported!");
		this.action = action;
		switch (type.getType()) {
		case "rsa":
			rsaaction(key, toEncrypt);
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

	private void rsaaction(MeviusCipherKey k, Object o) throws InvalidKeyException, IllegalBlockSizeException {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
			if (action.getAction() == 0) {
				byte[] strb = ((String) o).getBytes();
				cipher.init(Cipher.ENCRYPT_MODE, k.getRSAPublicKey());
				byte[] cf = cipher.doFinal(strb);
				encodedstringdata = byteArrayToHex(cf);
				encodedbytedata = cf;
			} else {
				cipher.init(Cipher.DECRYPT_MODE, k.getRSAPrivateKey());
				byte[] strb = hexToByteArray(((String) o));
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
