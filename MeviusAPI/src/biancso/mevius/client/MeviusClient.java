package biancso.mevius.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class MeviusClient {
	private final Socket socket;
	private final UUID uuid;
	private final MeviusInputStream mis;
	private final MeviusOutputStream mos;

	public MeviusClient(InetAddress addr, int port) throws IOException {
		socket = new Socket(addr, port);
		uuid = UUID.randomUUID();
		mos = new MeviusOutputStream(socket.getOutputStream());
		mos.flush();
		mis = new MeviusInputStream(socket.getInputStream());
	}

	public MeviusClient(Socket socket) throws IOException {
		this.socket = socket;
		uuid = UUID.randomUUID();
		mis = new MeviusInputStream(socket.getInputStream());
		mos = new MeviusOutputStream(socket.getOutputStream());
		mos.flush();
	}

	public void close() throws IOException {
		this.socket.close();
	}

	public boolean isClosed() {
		return this.socket.isClosed();
	}

	public MeviusInputStream getInputStream() throws IOException {
		return mis;
	}

	public MeviusOutputStream getOutputStream() throws IOException {
		return mos;
	}

	public final Socket getSocket() {
		return this.socket;
	}

	public final UUID getUUID() {
		return this.uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeviusClient other = (MeviusClient) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
