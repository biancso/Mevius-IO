package biancso.mevius.server.exceptions;

import biancso.mevius.packet.MeviusPacket;

@SuppressWarnings("serial")
public class PacketUnsupportedException extends Exception {

	public PacketUnsupportedException(MeviusPacket packet) {
		super(packet.getSignedData() + " is not exists");
	}
}
