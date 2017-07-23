package biancso.mevius.server.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileUtils {

	///// File download methods
	public static boolean downloadFile(URL url, File path, boolean overwrite) throws IOException {
		InputStream is = url.openStream();
		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(path));
		byte[] buff = new byte[1024];
		int rb = 0;
		while ((rb = is.read(buff)) != -1) {
			fos.write(buff, 0, rb);
		}
		fos.flush();
		fos.close();
		return true;
	}

	public static boolean donwloadFile(URL url, File path) throws IOException {
		return downloadFile(url, path, true);
	}

	
}
