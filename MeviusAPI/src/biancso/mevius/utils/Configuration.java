package biancso.mevius.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

import javax.net.ssl.X509KeyManager;

public class Configuration {
	private Properties prop = new Properties();
	private File propfile;

	public Configuration(File file) {
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		propfile = file;
		try {
			prop.load(new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Configuration(Properties prop, File file) {
		this.prop = prop;
		this.propfile = file;
	}

	public Configuration(File file, Charset charset) {
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		propfile = file;
		try {
			prop.load(new InputStreamReader(new FileInputStream(file), charset));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Configuration defaultProperties(Properties prop, File file) {
		return new Configuration(prop, file);
	}

	public void save() {
		try {
			FileWriter fw = new FileWriter(propfile);
			prop.store(fw, "THIS FILE HAS BEEN WRITEN IN MEVIUS");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void save(Charset charset) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(propfile), charset);
			prop.store(osw, "THIS FILE HAS BEEN WRITEN IN MEVIUS");
			osw.flush();
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getString(String key) {
		return prop.getProperty(key);
	}

	public String getString(String key, String defaultvalue) {
		return prop.getProperty(key, defaultvalue);
	}

	public void setString(String key, String value) {
		prop.setProperty(key, value);
	}

	public int getInt(String key) {
		try {
			return Integer.parseInt(prop.getProperty(key));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void setKeyPair(String key, KeyPair keypair) {
		String privateKey = Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded());
		String publicKey = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
		setString(key + "_PUBLIC", publicKey);
		setString(key + "_PRIVATE", privateKey);
	}

	public KeyPair getKeyPair(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				Base64.getDecoder().decode(getString(key + "_PUBLIC")));
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				Base64.getDecoder().decode(getString(key + "_PRIVATE")));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return new KeyPair(kf.generatePublic(publicKeySpec), kf.generatePrivate(privateKeySpec));
	}

	public int getInt(String key, int defaultvalue) {
		try {
			return Integer.parseInt(prop.getProperty(key));
		} catch (NumberFormatException e) {
			return defaultvalue;
		}
	}

	public void setInt(String key, int value) {
		prop.setProperty(key, String.valueOf(value));
	}

	public boolean getBoolean(String key) {
		return Boolean.valueOf(prop.getProperty(key));
	}

	public boolean getBoolean(String key, boolean defaultvalue) {
		return Boolean.valueOf(prop.getProperty(key, String.valueOf(defaultvalue)));
	}

	public void setBoolean(String key, boolean value) {
		prop.setProperty(key, String.valueOf(value));
	}
}
