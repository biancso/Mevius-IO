package biancso.mevius.packet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.ObjectPacketEvent;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.server.exceptions.PacketUnsupportedException;

public class MeviusObjectPacket extends MeviusPacket {

	private static final long serialVersionUID = 7496634581155147728L;
	
	private final Object obj;
	private final String objSignedData;

	// USAGE
	// MeviusObjectPacket packet = new MeviusObjectPacket(YOUR_SERIALIZABLE_OBJECT);
	public MeviusObjectPacket(Serializable serializable) {
		obj = serializable;
		objSignedData = serializable.getClass().getName();
	}

	public void writeToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
	}

	@SuppressWarnings("unchecked")
	public <T> T castTo(Class<?> clazz) throws PacketUnsupportedException {
		if (!isOBJSupported())
			throw new PacketUnsupportedException(objSignedData + " is not exist!");
		return (T) clazz.cast(obj);
	}

	private boolean isOBJSupported() {
		try {
			return Class.forName(objSignedData) != null;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public Class<?> getObjectClass() throws PacketUnsupportedException {
		try {
			return Class.forName(objSignedData);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new PacketUnsupportedException(objSignedData + " is not exist!");
	}

	@Override
	public PacketEvent createEvent(MeviusClient client, PacketEventType type) {
		return new ObjectPacketEvent(this, client, type);
	}

}
