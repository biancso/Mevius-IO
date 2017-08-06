package biancso.mevius.packet;

import java.io.Serializable;

public class MeviusRawObjectPacket
extends MeviusPacket {

	private static final long serialVersionUID = -8617889841248224121L;

	private final Serializable data;
	
	public MeviusRawObjectPacket(Serializable data) {
		this.data = data;
	}
	
	public Serializable getData() {
		return data;
	}
}
