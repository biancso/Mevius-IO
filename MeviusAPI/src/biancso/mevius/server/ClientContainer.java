package biancso.mevius.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.server.ConnectionHandler.ConnectionType;

public class ClientContainer {
	private HashMap<UUID, ? extends MeviusClient> socketMap;
	private final MeviusServer server;

	protected ClientContainer(MeviusServer server) {
		socketMap = new HashMap<>();
		this.server = server;
	}

	public void disconnect(MeviusClient client) throws IOException {
		client.getSocket().shutdownInput();
		client.getSocket().shutdownOutput();
		client.getSocket().close();
		socketMap.remove(client.getUUID());
	}

	protected void clientJoin(MeviusClient client) {
		if (socketMap.containsValue(client))
			try {
				disconnect(client);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		server.connectionhandler.connection(ConnectionType.JOIN, client);
	}

	protected void clientExit(MeviusClient client) {
		if (!socketMap.containsValue(client))
			throw new RuntimeErrorException(new Error("Undefined socket has disconnected"));
		server.connectionhandler.connection(ConnectionType.EXIT, client);
		try {
			disconnect(client);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
