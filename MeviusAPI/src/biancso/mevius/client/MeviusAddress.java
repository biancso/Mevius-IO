package biancso.mevius.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MeviusAddress {
	private final InetAddress addr;
	private final int port;

	private MeviusAddress(String host, int port) throws UnknownHostException {
		this.addr = InetAddress.getByName(host);
		this.port = port;
	}

	public static MeviusAddress newInstance(String host, int port) throws UnknownHostException {
		return new MeviusAddress(host, port);
	}

	protected InetAddress getIp() throws UnknownHostException {
		return addr;
	}

	protected int getPort() {
		return port;
	}
}
