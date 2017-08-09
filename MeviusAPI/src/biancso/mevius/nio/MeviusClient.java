package biancso.mevius.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.UUID;

import biancso.mevius.handler.ConnectionType;
import biancso.mevius.handler.MeviusHandler;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusResponsablePacket;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.utils.ThreadTimer;
import biancso.mevius.utils.cipher.MeviusCipherKey;

public class MeviusClient {
	private final SocketChannel sc;
	private final MeviusHandler handler; // Handler for control packet and
											// connection events
	private final UUID uuid;
	private final boolean self;
	private EventListener el;
	private boolean ready = false;
	private PublicKey publickey;
	private PrivateKey privatekey;

	public MeviusClient(InetSocketAddress addr, MeviusHandler handler) throws IOException {
		this.sc = SocketChannel.open(addr);
		sc.configureBlocking(false);
		this.handler = handler;
		uuid = UUID.randomUUID();
		self = true;
		el = new EventListener(sc);
		el.start();
		KeyPair kp = MeviusCipherKey.randomRSAKeyPair(512).getKey();
		ObjectOutputStream oos = new ObjectOutputStream(sc.socket().getOutputStream());
		oos.flush();
		oos.writeObject(kp.getPublic());
		oos.flush();
		handler.connection(ConnectionType.CLIENT_CONNECT_TO_SERVER, this);
		new PublicKeyListener().start();
	}

	public MeviusClient(SocketChannel channel, PublicKey publickey, MeviusHandler handler) {
		this.sc = channel;
		this.handler = handler;
		uuid = UUID.randomUUID();
		self = false;
		this.publickey = publickey;
	}

	public SocketChannel getSocketChannel() {
		return sc;
	}

	public boolean isReady() {
		return publickey != null && handler.getClientHandler().getPublicKey(this) != null;
	}

	protected void setReady(boolean ready) {
		this.ready = ready;
	}

	protected void setPublicKey(PublicKey publickey) {
		this.publickey = publickey;
	}

	public final PublicKey getPublicKey() {
		return publickey;
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
		if (self && el != null)
			el.interrupt();
		sc.close();
		handler.connection(ConnectionType.CLIENT_DISCONNECT_FROM_SERVER, this);
	}

	public void sendPacket(MeviusPacket packet) throws IOException {
		if (!packet.isSigned())
			throw new IllegalStateException(new Throwable("Packet is not signed!")); // Check
																						// packet's
																						// sign
																						// data
		if (!ready)
			throw new IOException("Client is not ready!");
		if (packet instanceof MeviusResponsablePacket) { // If ResponsablePacket
			MeviusResponsablePacket responsablePacket = (MeviusResponsablePacket) packet; // VAR
																							// ResponsablePacket
			Class<? extends MeviusResponsablePacket> packetClass = responsablePacket.getClass(); // get
																									// packet
																									// class
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

	class PublicKeyListener extends Thread {

		public PublicKeyListener() {
			new ThreadTimer(this).setTime(10).start();
		}

		public void run() {
			while (true) {
				try {
					ObjectInputStream ois = new ObjectInputStream(sc.socket().getInputStream());
					Object obj = ois.readObject();
					if (!(obj instanceof PublicKey))
						continue;
					setPublicKey((PublicKey) obj);
					ready = true;
					break;
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			interrupt();
		}
	}

	class EventListener extends Thread {
		private final Selector selector;

		public EventListener(SocketChannel channel) throws IOException {
			selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ);
			channel.register(selector, SelectionKey.OP_WRITE);
		}

		public void run() {
			while (true) {
				try {
					selector.select();
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey k = it.next();
						it.remove();
						if (!k.isValid())
							continue;
						if (k.isReadable()) {
							read(k);
						} else if (k.isWritable()) {
							send(k);
						}
						continue;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void send(SelectionKey k) {
			Object obj = k.attachment();
			if (!(obj instanceof MeviusPacket))
				return;
			SocketChannel channel = (SocketChannel) k.channel();
			handler.callEvent(MeviusHandler.getPacketEventInstance((MeviusPacket) obj,
					handler.getClientHandler().getClient(channel.socket().getInetAddress().getHostAddress()),
					PacketEventType.SEND));
		}

		private void read(SelectionKey k) {
			try {
				SocketChannel channel = (SocketChannel) k.channel();
				ByteBuffer data = ByteBuffer.allocate(1024);
				data.clear();
				channel.read(data);
				ByteArrayInputStream bais = new ByteArrayInputStream(data.array());
				ObjectInputStream ois = new ObjectInputStream(bais);
				Object obj = ois.readObject();
				if (!(obj instanceof MeviusPacket))
					return;
				MeviusPacket packet = (MeviusPacket) obj;
				handler.callEvent(MeviusHandler.getPacketEventInstance(packet,
						handler.getClientHandler().getClient(channel.socket().getInetAddress().getHostAddress()),
						PacketEventType.RECEIVE));
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
