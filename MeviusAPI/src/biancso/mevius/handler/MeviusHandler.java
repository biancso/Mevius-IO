package biancso.mevius.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.handler.ramidzkh.ConnectionListener;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;

/**
 * Handles packets incoming from {@link MeviusClient}
 * 
 * @author biancso
 *
 */
public class MeviusHandler {

	private List<PacketListener> packetlisteners = new ArrayList<>();
	private List<ConnectionListener> connectionlisteners = new ArrayList<>();
	private List<Object> ramidzkhListeners = new ArrayList<>();
	
	// USAGE
	// MeviusHandler handler = new MeviusHandler();
	// handler.register...;
	
	public void registerPacketListener(PacketListener... listener) {
		for (PacketListener l : listener) {
			packetlisteners.add(l);
		}
	}

	public void registerListeners(Object... listeners) {
		for(Object o : listeners)
			ramidzkhListeners.add(o);
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
				if (!m.isAnnotationPresent(ConnectionHandler.class))
					continue;
				if (!((ConnectionHandler) m.getAnnotation(ConnectionHandler.class)).value().equals(type))
					continue;
				if (!m.getParameterTypes()[0].equals(MeviusClient.class))
					continue;
				try {
					m.setAccessible(true);
					m.invoke(listener, client);
				} catch (IllegalAccessException e) {
					exception(e);
				} catch (IllegalArgumentException e) {
					exception(e);
				} catch (InvocationTargetException e) {
					exception(e);
				}
			}
		}
		
		ramidzkhListeners.forEach(o -> {
			if(o instanceof ConnectionListener)
				((ConnectionListener)o).onConnection(type, client);
		});
	}
	
	public void exception(Throwable exception) {
		exception.printStackTrace();
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
					exception(e);
				}
			}
		}
		
		ramidzkhListeners.forEach(o -> {
			Method[] methods = (Method[]) Arrays.<Method>asList(o.getClass().getMethods())
					.stream()
					.filter(m -> {
						Annotation[] as = m.getAnnotations();
						return Arrays.asList(as).stream().filter(a -> a.getClass() == biancso.mevius.handler.ramidzkh.PacketListener.class).toArray().length > 0;
					}) // Is annotated with biancso.mevius.handler.ramidzkh.PacketListener
					.filter(m -> {
						Class<?>[] argT = m.getParameterTypes();
						if(argT.length != 1) return false;
						
						if(hasSuper(argT[0], event.getClass())) return true;
						return false;
					}) // Only one type that extends MeviusPacket
					.filter(m -> m.getExceptionTypes().length == 0) // No exceptions
					.toArray();
			
			for(Method m : methods) {
				try {
					m.invoke(o, event);
				} catch (InvocationTargetException e) {
					// Only RuntimeException and Error
					exception(e);
				} catch (IllegalAccessException e) {
					// Impossible
					exception(e);
				}
			}
		});
	}

	private static boolean hasSuper(Class<?> clazz, Class<?> checkTo) {
		if(clazz == checkTo) return true;
		
		while(clazz != null) {
			if((clazz = clazz.getSuperclass()) == checkTo) return true;
		}
		
		return false;
	}

	public static PacketEvent getPacketEventInstance(MeviusPacket packet, MeviusClient client, PacketEventType type) {
		return packet.createEvent(client, type);
	}

}
