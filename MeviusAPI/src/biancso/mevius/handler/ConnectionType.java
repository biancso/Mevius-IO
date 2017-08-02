package biancso.mevius.handler;

import java.lang.annotation.Annotation;

public enum ConnectionType {
	CLIENT_CONNECT_TO_SERVER, CLIENT_DISCONNECT_FROM_SERVER;

	final Class<? extends Annotation> annotation = Connection.class;

	public Class<? extends Annotation> getAnnotation() {
		return this.annotation;
	}
}
