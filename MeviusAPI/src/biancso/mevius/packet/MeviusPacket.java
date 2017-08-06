package biancso.mevius.packet;

import java.io.Serializable;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.server.exceptions.PacketUnsupportedException;

public abstract class MeviusPacket implements Serializable {

	private static final long serialVersionUID = -5761615955867476829L;

	private String classsrc;
	
	public MeviusPacket() {
		classsrc = this.getClass().getName();
	}

	@Deprecated
	public final void sign(Class<? extends MeviusPacket> packetClazz) {
		classsrc = packetClazz.getName();
	}

	public final boolean isSigned() {
		return !(classsrc == null);
	}

	public final String getSignedData() {
		if (!isSigned())
			throw new IllegalStateException("Packet has not signed!");
		return classsrc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classsrc == null) ? 0 : classsrc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!isSigned())
			throw new IllegalStateException("Packet has not signed!");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeviusPacket other = (MeviusPacket) obj;
		if (!other.isSigned())
			throw new IllegalStateException("Packet has not signed!");
		if (classsrc == null) {
			if (other.classsrc != null)
				return false;
		} else if (!classsrc.equals(other.classsrc))
			return false;
		return true;
	}

	public boolean isPacketSupported() {
		try {
			return Class.forName(classsrc) != null;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public static boolean isPacketEncrypted(MeviusPacket packet) {
		return packet instanceof MeviusEncryptedPacket;
	}

	public boolean isPacketEncrypted() {
		return this instanceof MeviusEncryptedPacket;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends MeviusPacket> getPacketClass() throws PacketUnsupportedException {
		try {
			return (Class<? extends MeviusPacket>) Class.forName(getSignedData());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		throw new PacketUnsupportedException(this);
	}
	
	protected void onSend() {}

	public abstract PacketEvent createEvent(MeviusClient client, PacketEventType type);
}
