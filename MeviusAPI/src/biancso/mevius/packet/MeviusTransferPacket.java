package biancso.mevius.packet;

import java.io.IOException;
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
import javax.crypto.SealedObject;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import biancso.mevius.nio.exceptions.MeviusCipherException;
import biancso.mevius.utils.cipher.MeviusCipherKey;

@SuppressWarnings("serial")
public class MeviusTransferPacket implements Serializable {
	private final SealedObject key;
	private final SealedObject obj;

	private MeviusTransferPacket(SealedObject key, SealedObject obj) {
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
			SealedObject okey = new SealedObject(key.getEncoded(), c);
			c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, key);
			SealedObject oobj = new SealedObject(packet, c);
			return new MeviusTransferPacket(okey, oobj);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | IOException | InvalidKeySpecException e) {
			e.printStackTrace();
			throw new MeviusCipherException(e.getLocalizedMessage());
		}
	}

	public Key getKey(PrivateKey privatekey) throws MeviusCipherException {
		try {
			Cipher c = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "SunJCE");
			c.init(Cipher.DECRYPT_MODE, privatekey);
			DESedeKeySpec desKeySpec = new DESedeKeySpec((byte[]) (key.getObject(c)));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			return keyFactory.generateSecret(desKeySpec);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| ClassNotFoundException | IllegalBlockSizeException | BadPaddingException | IOException
				| InvalidKeySpecException e) {
			throw new MeviusCipherException(e.getMessage());
		}
	}

	public MeviusPacket getPacket(Key key) throws MeviusCipherException {
		try {
			Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, key);
			return (MeviusPacket) obj.getObject(c);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | ClassNotFoundException
				| IllegalBlockSizeException | BadPaddingException | IOException e) {
			throw new MeviusCipherException(e.getMessage());

		}
	}
}
