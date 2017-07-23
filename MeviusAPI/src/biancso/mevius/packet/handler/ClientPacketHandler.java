package biancso.mevius.packet.handler;

import java.io.IOException;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.client.MeviusInputStream;
import biancso.mevius.client.MeviusOutputStream;

public class ClientPacketHandler extends Thread {
	private final MeviusInputStream mis;
	private final MeviusOutputStream mos;
	
	public ClientPacketHandler(MeviusClient client) throws IOException {
		mis = client.getInputStream();
		mos = client.getOutputStream();
	}
	
	
}
