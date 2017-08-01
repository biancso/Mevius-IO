package biancso.mevius.connection;

import java.lang.annotation.Annotation;

public enum ConnectionType {
	CLIENT_CONNECT_TO_SERVER(0), CLIENT_DISCONNECT_FROM_SERVER(0), SERVER_CONNECT_TO_CLIENT(
			1), SERVER_DISCONNECT_FROM_CLIENT(1);

	private enum ConnectionObject {
		SERVER, CLIENT;
	};

	final Class<? extends Annotation> annotation;

	private final ConnectionObject connobj;

	private ConnectionType(int id) {
		annotation = id == 0 ? ClientConnection.class : ServerConnection.class;
		this.connobj = id == 0 ? ConnectionObject.CLIENT : ConnectionObject.SERVER;
	}

	public ConnectionObject getConnectionObject() {
		return this.connobj;
	}
}
