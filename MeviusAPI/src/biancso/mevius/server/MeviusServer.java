package biancso.mevius.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.connection.ConnectionHandler;
import biancso.mevius.packet.handler.PacketHandler;
import biancso.mevius.server.exceptions.ServerCreateFailException;

public class MeviusServer extends Thread {
	private final ServerSocket serversocket;
	private boolean running = false;
	protected ClientContainer clientcontainer;
	protected ConnectionHandler connectionhandler;
	protected PacketHandler ph;

	public MeviusServer(int port) throws ServerCreateFailException {
		try {
			this.serversocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new ServerCreateFailException(e.getMessage());
		}
		clientcontainer = new ClientContainer(this);
		connectionhandler = new ConnectionHandler();
		ph = new PacketHandler();

	}

	public void run() {
		while (running && !isInterrupted()) {
			try {
				MeviusClient client = new MeviusClient(serversocket.accept(), ph, connectionhandler);
				client.start();
				clientcontainer.clientJoin(client);
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

	public ClientContainer getClientContainer() {
		return this.clientcontainer;
	}

	public ConnectionHandler getConnectionHandler() {
		return connectionhandler;
	}

	public PacketHandler getPacketHandler() {
		return ph;
	}

	public void setDefaultPacketHandler(PacketHandler ph) {
		this.ph = ph;
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
