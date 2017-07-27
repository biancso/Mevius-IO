package biancso.mevius.packet.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.packet.handler.exceptions.ListenerAlreadyRegisteredException;
import biancso.mevius.server.exceptions.PacketUnsupportedException;

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

	public final void callEvent(PacketEvent event, PacketEventType type) {
		boolean receive = type == PacketEventType.RECEIVE ? true : false;
		for (PacketListener listener : listeners) {
			for (Method m : listener.getClass().getMethods()) {
				if (m.getParameterTypes().length != 1)
					continue;
				if (!m.getParameterTypes()[0].isAssignableFrom(PacketEvent.class))
					continue;
				if (!m.isAnnotationPresent(EventMethod.class))
					continue;
				String packet = event.getPacketClass().getSimpleName();
				switch (packet) {
				
				default:
					try {
						throw new PacketUnsupportedException(event.getPacket());
					} catch (PacketUnsupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
}
