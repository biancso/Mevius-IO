package biancso.mevius.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.handler.ConnectionType;
import biancso.mevius.handler.MeviusHandler;

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

					if (k.isAcceptable()) {
						accept(k);
					} else if (k.isReadable()) {
						read(k);
					} else if (k.isWritable()) {

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
			System.out.println(channel.socket().getInetAddress().getHostAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
