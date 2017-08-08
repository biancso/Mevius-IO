package biancso.mevius.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

import biancso.mevius.handler.ConnectionType;
import biancso.mevius.handler.MeviusHandler;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusResponsablePacket;
import biancso.mevius.packet.events.PacketEventType;

public class MeviusClient {
	private final SocketChannel sc;
	private final MeviusHandler handler; // Handler for control packet and connection events
	private final UUID uuid;
	private final boolean self;

	public MeviusClient(InetSocketAddress addr, MeviusHandler handler) throws IOException {
		this.sc = SocketChannel.open(addr);
		sc.configureBlocking(false);
		this.handler = handler;
		uuid = UUID.randomUUID();
		self = true;
	}

	public MeviusClient(SocketChannel channel, MeviusHandler handler) {
		this.sc = channel;
		this.handler = handler;
		uuid = UUID.randomUUID();
		self = false;
	}

	public boolean isClosed() {
		return !sc.isOpen(); // Configure out is socket closed
	}

	public final UUID getUUID() {
		return this.uuid; // get UniqueId
	}

	public void disconnect() throws IOException {
		sc.close();
		if (!self)
			handler.getClientHandler().exit(this);
		handler.connection(ConnectionType.CLIENT_DISCONNECT_FROM_SERVER, this);
	}

	public void sendPacket(MeviusPacket packet) throws IOException {
		if (!packet.isSigned())
			throw new IllegalStateException(new Throwable("Packet is not signed!")); // Check packet's sign data
		if (packet instanceof MeviusResponsablePacket) { // If ResponsablePacket
			MeviusResponsablePacket responsablePacket = (MeviusResponsablePacket) packet; // VAR ResponsablePacket
			Class<? extends MeviusResponsablePacket> packetClass = responsablePacket.getClass(); // get packet class
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
			sc.write(convert(packet));
			handler.callEvent(MeviusHandler.getPacketEventInstance(responsablePacket, this, PacketEventType.SEND));
		} else {
			sc.write(convert(packet));
			handler.callEvent(MeviusHandler.getPacketEventInstance(packet, this, PacketEventType.SEND));
		}
	}

	private ByteBuffer convert(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.flush();
		oos.writeObject(obj);
		oos.flush();
		return ByteBuffer.wrap(baos.toByteArray());
	}

	public MeviusHandler getHandler() {
		return handler; // return handler
	}

	public InetAddress getInetAddress() {
		return sc.socket().getInetAddress();
	}
}
