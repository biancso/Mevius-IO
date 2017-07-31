package biancso.mevius.packet.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;
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
				if (!m.isAnnotationPresent(EventMethod.class))
					continue;
				if (m.getParameterTypes().length != 1)
					continue;
				Class<?> clazz = m.getParameterTypes()[0];
				if (!clazz.equals(event.getClass()) && !event.getClass().isAssignableFrom(clazz))
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

	public static PacketEvent getPacketEventInstance(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		try {
			String eventclassname = "biancso.mevius.packet.events."
					+ packet.getClass().getSimpleName().replace("Mevius", "") + "Event";
			Class<?> clazz = Class.forName(eventclassname);
			Constructor<?> constructor = clazz.getConstructor(MeviusPacket.class, MeviusClient.class,
					PacketEventType.class);
			return (PacketEvent) constructor.newInstance(packet, client, type);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("");
	}
}
