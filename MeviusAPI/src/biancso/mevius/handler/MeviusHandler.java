package biancso.mevius.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import biancso.mevius.nio.MeviusClient;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;

public class MeviusHandler {

	private ArrayList<PacketListener> packetlisteners = new ArrayList<>();
	private ArrayList<ConnectionListener> connectionlisteners = new ArrayList<>();
	private final ClientHandler ch = new ClientHandler();
	// USAGE
	// MeviusHandler handler = new MeviusHandler();
	// handler.register...;

	public void registerPacketListener(PacketListener... listener) {
		for (PacketListener l : listener) {
			packetlisteners.add(l);
		}
	}

	public void registerConnectionListener(ConnectionListener... listener) {
		for (ConnectionListener l : listener) {
			connectionlisteners.add(l);
		}
	}

	public void connection(ConnectionType type, MeviusClient client) {
		for (ConnectionListener listener : connectionlisteners) {
			for (Method m : listener.getClass().getMethods()) {
				if (m.getParameterTypes().length != 1)
					continue;
				if (!m.isAnnotationPresent(type.getAnnotation()))
					continue;
				if (!((ConnectionHandler) m.getAnnotation(ConnectionHandler.class)).value().equals(type))
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

	public final void callEvent(PacketEvent event) {
		for (PacketListener listener : packetlisteners) {
			for (Method m : listener.getClass().getMethods()) {
				if (!m.isAnnotationPresent(PacketHandler.class))
					continue;
				if (m.getParameterTypes().length != 1)
					continue;
				Class<?> clazz = m.getParameterTypes()[0];
				if (!clazz.equals(event.getClass()) && !clazz.equals(PacketEvent.class))
					continue;
				PacketHandler ph = m.getAnnotation(PacketHandler.class);
				if (!ph.value().equals(event.getEventType()) && !ph.value().equals(PacketEventType.ALL))
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

	public ClientHandler getClientHandler() {
		return ch;
	}
}
