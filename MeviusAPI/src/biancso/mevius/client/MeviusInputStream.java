package biancso.mevius.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusRequestPacket;
import biancso.mevius.packet.MeviusResponsePacket;
import biancso.mevius.server.exceptions.PacketOutdatedException;
import biancso.mevius.server.exceptions.PacketUnsupportedException;

public class MeviusInputStream {
	private ObjectInputStream ois;

	public MeviusInputStream(InputStream is) throws IOException {
		this.ois = new ObjectInputStream(is);
	}

	public final MeviusPacket readPacket() throws IOException, PacketUnsupportedException, PacketOutdatedException {
		Object obj = null;
		try {
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (obj == null)
			return null;
		if (!(obj instanceof MeviusPacket))
			return null;
		MeviusPacket meviuspacket = (MeviusPacket) obj;
		if (!meviuspacket.isPacketSupported())
			throw new PacketUnsupportedException(meviuspacket);
		if (meviuspacket instanceof MeviusRequestPacket) {
			MeviusRequestPacket mrp = (MeviusRequestPacket) meviuspacket;
			if (mrp.isOutDated())
				throw new PacketOutdatedException(mrp);
		} else if (meviuspacket instanceof MeviusResponsePacket) {
			MeviusResponsePacket mrp = (MeviusResponsePacket) meviuspacket;
			if (mrp.isOutDated())
				throw new PacketOutdatedException(mrp);
		}
		return meviuspacket;
	}

	public final void close() throws IOException {
		ois.close();
		ois = null;
	}

	public final boolean isClosed() {
		return ois == null;
	}

}
