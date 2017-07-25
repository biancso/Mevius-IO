package biancso.mevius.packet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public class MeviusObjectPacket extends MeviusPacket {
	private final Object obj;

	public MeviusObjectPacket(Serializable serilizableObject) {
		obj = serilizableObject;
	}

	public void writeToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
	}

	@SuppressWarnings("unchecked")
	public <T> T castTo(Class<?> clazz) {
		return (T) clazz.cast(obj);
	}

}
