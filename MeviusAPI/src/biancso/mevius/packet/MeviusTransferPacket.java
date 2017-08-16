package biancso.mevius.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import biancso.mevius.nio.exceptions.MeviusCipherException;
import biancso.mevius.utils.cipher.MeviusCipherKey;

@SuppressWarnings("serial")
public class MeviusTransferPacket implements Serializable {
	private final byte[] key;
	private final byte[] obj;

	private MeviusTransferPacket(byte[] key, byte[] obj) {
		this.key = key;
		this.obj = obj;
	}

	public static MeviusTransferPacket getInstance(PublicKey publickey, MeviusPacket packet)
			throws MeviusCipherException {
		try {
			DESedeKeySpec desKeySpec = new DESedeKeySpec(((String) MeviusCipherKey.randomDESKey().getKey()).getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			Key key = keyFactory.generateSecret(desKeySpec);
			Cipher c = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
			c.init(Cipher.ENCRYPT_MODE, publickey);
			byte[] bkey = convertObj(key, c);
			c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] bobj = convertObj(packet, c);
			return new MeviusTransferPacket(bkey, bobj);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IOException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new MeviusCipherException(e.getLocalizedMessage());
		}
	}

	public Key getKey(PrivateKey privatekey) throws MeviusCipherException {
		try {
			Cipher c = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
			c.init(Cipher.DECRYPT_MODE, privatekey);
			/*
			 * DESedeKeySpec desKeySpec = new DESedeKeySpec((byte[]) (convertByte(key, c)));
			 * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede"); return
			 * keyFactory.generateSecret(desKeySpec);
			 */
			return (Key) convertByte(key, c);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| ClassNotFoundException | IOException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new MeviusCipherException(e.getMessage());
		}
	}

	public MeviusPacket getPacket(Key key) throws MeviusCipherException {
		try {
			Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, key);
			return (MeviusPacket) convertByte(obj, c);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | ClassNotFoundException
				| IOException | IllegalBlockSizeException | BadPaddingException e) {
			throw new MeviusCipherException(e.getMessage());

		}
	}

	private static byte[] convertObj(Object obj, Cipher c)
			throws IOException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.flush();
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		return c.doFinal(baos.toByteArray());
	}

	private static Object convertByte(byte[] buff, Cipher c)
			throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayInputStream bais = new ByteArrayInputStream(c.doFinal(buff));
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		ois.close();
		return o;
	}
}
