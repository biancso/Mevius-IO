package biancso.mevius.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.handler.ConnectionType;
import biancso.mevius.handler.MeviusHandler;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.events.PacketEventType;

public class MeviusServer extends Thread {
	private boolean running = false;
	private final Selector selector;
	protected MeviusHandler handler;
	private final ServerSocketChannel ssc;

	// MEVIUS ALPHA
	public MeviusServer(int port) throws IOException {
		this.ssc = ServerSocketChannel.open();
		this.selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(port));
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		handler = new MeviusHandler();
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

	private void send(SelectionKey k) {
		Object obj = k.attachment();
		if (!(obj instanceof MeviusPacket))
			return;
		SocketChannel channel = (SocketChannel) k.channel();
		handler.callEvent(MeviusHandler.getPacketEventInstance((MeviusPacket) obj,
				handler.getClientHandler().getClient(channel.socket().getInetAddress().getHostAddress()),
				PacketEventType.SEND));
	}

	private void accept(SelectionKey k) {
		try {
			ServerSocketChannel sc = (ServerSocketChannel) k.channel();
			SocketChannel channel = sc.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
			MeviusClient mc = new MeviusClient(channel, handler);
			handler.connection(ConnectionType.CLIENT_CONNECT_TO_SERVER, mc);
			handler.getClientHandler().join(mc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
