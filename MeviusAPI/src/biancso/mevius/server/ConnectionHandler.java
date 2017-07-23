package biancso.mevius.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.server.handler.ClientExit;
import biancso.mevius.server.handler.ClientJoin;
import biancso.mevius.server.handler.ConnectionListener;

public class ConnectionHandler {
	private ArrayList<ConnectionListener> listeners = new ArrayList<>();

	enum ConnectionType {
		JOIN(0), EXIT(1);

		final Class<? extends Annotation> annotation;

		private ConnectionType(int i) {
			annotation = i == 0 ? ClientJoin.class : ClientExit.class;
		}

	};

	protected ConnectionHandler() {

	}

	public void registerListener(ConnectionListener listener) {
		if (isAlreadyRegisted(listener))
			return;
		listeners.add(listener);
	}

	public boolean isAlreadyRegisted(ConnectionListener listener) {
		return listeners.contains(listener);
	}

	protected void connection(ConnectionType type, MeviusClient client) {
		for (ConnectionListener listener : listeners) {
			for (Method m : listener.getClass().getMethods()) {
				if (m.getParameterTypes().length != 1)
					continue;
				if (!m.getParameterTypes()[0].equals(MeviusClient.class))
					continue;
				if (!m.isAnnotationPresent(type.annotation))
					continue;
				try {
					m.setAccessible(true);
					m.invoke(listener, client);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
