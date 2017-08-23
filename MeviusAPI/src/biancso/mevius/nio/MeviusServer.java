package biancso.mevius.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Iterator;

import biancso.mevius.handler.ConnectionType;
import biancso.mevius.handler.MeviusHandler;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.utils.cipher.MeviusCipherKey;

public class MeviusServer extends Thread {
	private boolean running = false;
	private final Selector selector;
	protected MeviusHandler handler;
	private final ServerSocketChannel ssc;
	private final KeyPair keypair;
	private long timeout = 10000;

	// MEVIUS ALPHA
	public MeviusServer(int port) throws IOException {
		this.ssc = ServerSocketChannel.open();
		this.selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(port));
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		handler = new MeviusHandler();
		keypair = MeviusCipherKey.randomRSAKeyPair(512).getKey();
	}

	public void setConnectionTimeout(int time) {
		timeout = time * 1000;
	}

	public void setConnectionTimeout(long time) {
		timeout = time;
	}

	public long getConnectionTimeout() {
		return timeout;
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
					if (k.isAcceptable()) {
						accept(k);
					} else if (k.isReadable()) {
						read(k);
					}
					continue;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void start() {
		running = true;
		super.start();
	}

	public void interrupt() {
		if (!running)
			return;
		running = false;
		super.interrupt();
	}

	public MeviusHandler getHandler() {
		return handler;
	}

	private void accept(SelectionKey k) {
		try {
			ServerSocketChannel sc = (ServerSocketChannel) k.channel();
			SocketChannel channel = sc.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
			MeviusClient mc = new MeviusClient(channel, keypair.getPublic(), handler);
			handler.getClientHandler().join(mc);
			handler.connection(ConnectionType.CLIENT_CONNECT_TO_SERVER, mc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private void read(SelectionKey k) {
		try {
			SocketChannel channel = (SocketChannel) k.channel();
			ByteBuffer data = ByteBuffer.allocate(100000);
			data.clear();
			channel.read(data);
			ByteArrayInputStream bais = new ByteArrayInputStream(data.array());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			MeviusClient client = handler.getClientHandler()
					.getClient(channel.socket().getInetAddress().getHostAddress());
			if (obj instanceof PublicKey) {
				handler.getClientHandler().setPublicKey(client, ((PublicKey) obj));
				channel.write(convert(keypair.getPublic()));
				return;
			}
			if (!(obj instanceof MeviusPacket))
				return;
			MeviusPacket packet = (MeviusPacket) obj;
			handler.callEvent(MeviusHandler.getPacketEventInstance(packet, client, PacketEventType.RECEIVE));
		} catch (IOException | ClassNotFoundException e) {
			if (e.getClass().equals(StreamCorruptedException.class)) {
				k.cancel();
				MeviusClient mc = handler.getClientHandler()
						.getClient(((SocketChannel) k.channel()).socket().getInetAddress().getHostAddress());
				try {
					mc.disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
			k.cancel();
			e.printStackTrace();
		}
	}

}
