package biancso.mevius.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class MeviusLogger {
	private String prefix;
	private Collection<String> logCollection;
	private SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd hh/mm/ss]");

	public MeviusLogger(String prefix) {
		this.prefix = "[" + prefix + "]";
	}

	public MeviusLogger(Class<?> clazz) {
		this.prefix = clazz.getSimpleName();
	}

	public void setPrefix(String prefix) {
		this.prefix = "[" + prefix + "]";
	}

	public void setDefaultDateFormat(SimpleDateFormat format) {
		this.sdf = format;
	}

	public void setDefaultDateFormat(String format) {
		this.sdf = new SimpleDateFormat(format);
	}

	public void log(String log, boolean sysout) {
		log = prefix + " " + sdf.format(new Date()) + " " + log;
		logCollection.add(log);
		if (sysout)
			System.out.println(log);
	}

	public void saveLog(File file) {
		LogSave ls = new LogSave(logCollection, file);
		ls.start();
	}

	private class LogSave extends Thread {
		private final Collection<String> logCollection;
		private File file;

		public LogSave(Collection<String> logCollection, File file) {
			this.logCollection = logCollection;
			this.file = file;
		}

		public void run() {
			try {
				FileWriter fw = new FileWriter(file);
				StringBuilder sb = new StringBuilder();
				for (String str : logCollection) {
					sb.append(str + "\n");
				}
				fw.write(sb.toString());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

	}
}
