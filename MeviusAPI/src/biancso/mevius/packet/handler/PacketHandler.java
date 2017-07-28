package biancso.mevius.packet.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.handler.exceptions.ListenerAlreadyRegisteredException;

public class PacketHandler {
	private final ArrayList<PacketListener> listeners;

	public PacketHandler() {
		listeners = new ArrayList<>();
	}

	public void registerListener(PacketListener listener) throws ListenerAlreadyRegisteredException {
		if (listeners.contains(listener))
			throw new ListenerAlreadyRegisteredException(listener.getClass().getName() + " is exist!");
		listeners.add(listener);
	}

	public final void callEvent(PacketEvent event) {
		for (PacketListener listener : listeners) {
			for (Method m : listener.getClass().getMethods()) {
				if (m.getParameterTypes().length != 1)
					continue;
				if (!m.getParameterTypes()[0].equals(event.getClass()))
					continue;
				if (!m.isAnnotationPresent(EventMethod.class))
					continue;
				try {
					m.invoke(listener, event);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
