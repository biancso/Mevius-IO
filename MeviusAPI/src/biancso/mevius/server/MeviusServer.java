package biancso.mevius.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.handler.ConnectionType;
import biancso.mevius.handler.MeviusHandler;
import biancso.mevius.server.exceptions.ServerCreateFailException;

public class MeviusServer extends Thread {
	private final ServerSocket serversocket;
	private boolean running = false;
	protected MeviusHandler handler;

	// MEVIUS ALPHA
	public MeviusServer(int port) throws ServerCreateFailException {
		try {
			this.serversocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new ServerCreateFailException(e.getMessage());
		}
		handler = new MeviusHandler();

	}

	public void run() {
		while (running && !isInterrupted()) {
			try {
				MeviusClient client = new MeviusClient(serversocket.accept(), handler);
				client.start();
				handler.connection(ConnectionType.CLIENT_CONNECT_TO_SERVER, client);
			} catch (IOException e) {
				continue;
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

	public void setTimeout(int time) {
		try {
			serversocket.setSoTimeout(time);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
