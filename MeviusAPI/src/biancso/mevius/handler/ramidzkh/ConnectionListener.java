package biancso.mevius.handler.ramidzkh;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.handler.ConnectionType;

public interface ConnectionListener {

	public void onConnection(ConnectionType type, MeviusClient client);
}
