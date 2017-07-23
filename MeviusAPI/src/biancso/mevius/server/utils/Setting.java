package biancso.mevius.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Properties;

public class Setting {
	private Properties prop = new Properties();
	private File propfile;

	public Setting(File file) {
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

	private Setting(Properties prop, File file) {
		this.prop = prop;
		this.propfile = file;
	}

	public Setting(File file, Charset charset) {
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

	public static Setting defaultProperties(Properties prop, File file) {
		return new Setting(prop, file);
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
