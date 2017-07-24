package biancso.mevius.packet;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;

import biancso.mevius.packet.exceptions.FailedToDecryptPacketException;
import biancso.mevius.utils.cipher.MeviusCipherKey;
import biancso.mevius.utils.cipher.exceptions.UnsupportedMeviusKeyException;

@SuppressWarnings("serial")
public class MeviusEncryptedPacket extends MeviusPacket {
	private final SealedObject encryptedobj;

	public MeviusEncryptedPacket(MeviusCipherKey key, MeviusPacket packet)
			throws UnsupportedMeviusKeyException, IllegalBlockSizeException, IOException {
		switch (key.getKeyType().getType()) {
		case "aes256":
			encryptedobj = encryptaes256(key, packet);
			break;
		default:
			throw new UnsupportedMeviusKeyException(
					"Packet Encryption does not support with " + key.getKeyType().getType());
		}
	}

	private SealedObject encryptaes256(MeviusCipherKey k, MeviusPacket packet)
			throws IllegalBlockSizeException, IOException {
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
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			return new SealedObject(packet, c);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		throw new RuntimeErrorException(new Error("An error while encrypting packet"));
	}

	public MeviusPacket getDecryptedPacket(MeviusCipherKey key)
			throws UnsupportedMeviusKeyException, FailedToDecryptPacketException {
		switch (key.getKeyType().getType()) {
		case "aes256":
			return decryptedaes256(key);
		default:
			throw new UnsupportedMeviusKeyException(
					"Packet Encryption does not support with " + key.getKeyType().getType());
		}
	}

	private MeviusPacket decryptedaes256(MeviusCipherKey k) throws FailedToDecryptPacketException {
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
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			return (MeviusPacket) encryptedobj.getObject(c);
		} catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException
				| NoSuchPaddingException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException
				| IOException e) {
			e.printStackTrace();
		}
		throw new FailedToDecryptPacketException("");
	}
}
