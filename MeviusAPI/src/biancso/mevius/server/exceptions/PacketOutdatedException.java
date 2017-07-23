package biancso.mevius.server.exceptions;

import java.lang.reflect.Field;
import java.util.Date;

import biancso.mevius.packet.MeviusRequestPacket;
import biancso.mevius.packet.MeviusResponsePacket;

@SuppressWarnings("serial")
public class PacketOutdatedException extends Exception {
	private static Date packetSentDate;

	public PacketOutdatedException(MeviusResponsePacket packet) {
		super("Packet outdated! " + packet.getUUId().toString());
		try {
			Class<?> clazz = packet.getClass().getSuperclass();
			Field f = clazz.getDeclaredField("timeout");
			f.setAccessible(true);
			Date date = (Date) f.get(packet);
			packetSentDate = date;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public PacketOutdatedException(MeviusRequestPacket packet) {
		super("Packet outdated! " + packet.getUUId().toString());
		try {
			Class<?> clazz = packet.getClass().getSuperclass();
			Field f = clazz.getDeclaredField("timeout");
			f.setAccessible(true);
			Date date = (Date) f.get(packet);
			packetSentDate = date;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Date getDate() {
		return packetSentDate;
	}
}
