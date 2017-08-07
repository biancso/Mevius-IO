package biancso.mevius.packet;

@SuppressWarnings("serial")
public class MeviusRequestPacket extends MeviusResponsablePacket implements Cloneable {
	private final String requestdata;

	public MeviusRequestPacket(String requestdata) {
		this.requestdata = requestdata;
	}

	public final String getRequestData() {
		return this.requestdata;
	}

	public MeviusResponsePacket response(String data) {
		return new MeviusResponsePacket(this, data);
	}

}
