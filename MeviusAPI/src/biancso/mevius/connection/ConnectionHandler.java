package biancso.mevius.connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import biancso.mevius.client.MeviusClient;

public class ConnectionHandler {
	private ArrayList<ConnectionListener> listeners = new ArrayList<>();

	public void registerListener(ConnectionListener listener) {
		if (isAlreadyRegisted(listener))
			return;
		listeners.add(listener);
	}

	public boolean isAlreadyRegisted(ConnectionListener listener) {
		return listeners.contains(listener);
	}

	public void connection(ConnectionType type, MeviusClient client) {
		for (ConnectionListener listener : listeners) {
			for (Method m : listener.getClass().getMethods()) {
				if (m.getParameterTypes().length != 1)
					continue;
				if (!m.isAnnotationPresent(type.annotation))
					continue;
				if (!m.getParameterTypes()[0].equals(MeviusClient.class))
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
