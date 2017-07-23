package biancso.mevius.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusResponsablePacket;

public class MeviusOutputStream {
	private final ObjectOutputStream oos;

	public MeviusOutputStream(OutputStream outputstream) throws IOException {
		this.oos = new ObjectOutputStream(outputstream);
	}

	public final void close() throws IOException {
		oos.close();
	}

	public final void sendPacket(MeviusPacket packet) throws IOException {
		if (!packet.isSigned())
			throw new IllegalStateException(new Throwable("Packet is not signed!"));
		if (packet instanceof MeviusResponsablePacket) {
			MeviusResponsablePacket responsablePacket = (MeviusResponsablePacket) packet;
			Class<? extends MeviusResponsablePacket> packetClass = responsablePacket.getClass();
			try {
				Method m = packetClass.getSuperclass().getDeclaredMethod("sent", new Class[] {});
				m.setAccessible(true);
				try {
					m.invoke(responsablePacket);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			oos.writeObject(responsablePacket);
			oos.flush();
		} else {
			oos.writeObject(packet);
			oos.flush();
		}
	}

	public final void flush() throws IOException {
		oos.flush();
	}
}
