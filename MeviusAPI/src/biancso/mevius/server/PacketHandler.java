package biancso.mevius.server;

import java.util.ArrayList;

import biancso.mevius.packet.handler.PacketListener;

public class PacketHandler {
	private final ArrayList<PacketListener> listeners;
	
	protected PacketHandler() {
		listeners = new ArrayList<>();
	}
	
	
}
