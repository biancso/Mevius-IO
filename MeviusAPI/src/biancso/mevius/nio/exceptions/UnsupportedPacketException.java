package biancso.mevius.nio.exceptions;

import biancso.mevius.packet.MeviusPacket;

@SuppressWarnings("serial")
public class UnsupportedPacketException extends MeviusException {

	public UnsupportedPacketException(String str) {
		super(str);
		// TODO Auto-generated constructor stub
	}

	public UnsupportedPacketException(MeviusPacket packet) {
		// TODO Auto-generated constructor stub
		super(packet.getSignedData() + " is not exist!");
	}

}
