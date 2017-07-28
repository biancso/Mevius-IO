package biancso.mevius.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import biancso.mevius.packet.MeviusFilePacket;
import biancso.mevius.packet.MeviusImagePacket;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusResponsablePacket;
import biancso.mevius.packet.MeviusTextPacket;
import biancso.mevius.packet.events.FilePacketEvent;
import biancso.mevius.packet.events.ImagePacketEvent;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.packet.events.TextPacketEvent;
import biancso.mevius.packet.handler.PacketHandler;

public class MeviusClient extends Thread {
	private final Socket socket;
	private final UUID uuid;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;
	private final PacketHandler ph;

	// OutputStream flush
	public MeviusClient(InetAddress addr, int port, PacketHandler ph) throws IOException {
		socket = new Socket(addr, port);
		uuid = UUID.randomUUID();
		this.ph = ph;
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());
	}

	public MeviusClient(Socket socket, PacketHandler ph) throws IOException {
		this.socket = socket;
		uuid = UUID.randomUUID();
		this.ph = ph;
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());
	}

	public void close() throws IOException {
		this.socket.close();
	}

	public boolean isClosed() {
		return this.socket.isClosed();
	}

	public final Socket getSocket() {
		return this.socket;
	}

	public final UUID getUUID() {
		return this.uuid;
	}

	public void run() {
		while (!this.isInterrupted()) {
			try {
				Object obj = ois.readObject();
				if (!(obj instanceof MeviusPacket))
					continue;
				MeviusPacket packet = (MeviusPacket) obj;
				if (packet instanceof MeviusFilePacket) {
					ph.callEvent(new FilePacketEvent(packet, this, PacketEventType.RECEIVE));
				} else if (packet instanceof MeviusImagePacket) {
					ph.callEvent(new ImagePacketEvent(packet, this, PacketEventType.RECEIVE));
				} else if (packet instanceof MeviusTextPacket) {
					ph.callEvent(new TextPacketEvent(packet, this, PacketEventType.RECEIVE));
				} else {
					System.out.println("Under development");
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeviusClient other = (MeviusClient) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public void sendPacket(MeviusPacket packet) throws IOException {
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
}
