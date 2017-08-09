package biancso.mevius.handler;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.UUID;

import biancso.mevius.nio.MeviusClient;

public class ClientHandler {
	private final HashMap<UUID, MeviusClient> uuidmap = new HashMap<>();
	private final HashMap<String, MeviusClient> ipmap = new HashMap<>();
	private final HashMap<MeviusClient, PublicKey> publickeymap = new HashMap<>();

	protected ClientHandler() {

	}

	public boolean isOnline(UUID uuid) {
		return uuidmap.containsKey(uuid);
	}

	public boolean join(MeviusClient client) {
		if (uuidmap.containsKey(client.getUUID()))
			return false;
		uuidmap.put(client.getUUID(), client);
		ipmap.put(client.getInetAddress().getHostAddress(), client);
		return true;
	}

	public boolean exit(MeviusClient client) {
		if (!uuidmap.containsKey(client.getUUID()))
			return false;
		uuidmap.remove(client.getUUID());
		ipmap.remove(client.getInetAddress().getHostAddress());
		return true;
	}

	public MeviusClient getClient(String host) {
		return ipmap.get(host);
	}

	public MeviusClient getClient(UUID uuid) {
		return uuidmap.get(uuid);
	}
	
	public PublicKey getPublicKey(MeviusClient client) {
		return publickeymap.get(client);
	}

	public void setPublicKey(MeviusClient client, PublicKey publickey) {
		if (!isOnline(client.getUUID()))
			return;
		publickeymap.put(client, publickey);
	}
}
