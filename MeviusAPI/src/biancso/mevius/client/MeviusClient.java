package biancso.mevius.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import biancso.mevius.handler.ConnectionType;
import biancso.mevius.handler.MeviusHandler;
import biancso.mevius.packet.MeviusPacket;
import biancso.mevius.packet.MeviusRawObjectPacket;
import biancso.mevius.packet.events.PacketEventType;
import biancso.mevius.pipeline.Pipeline;

public class MeviusClient extends Thread {
	private final Socket socket; // Socket
	private final UUID uuid; // UniqueId
	private final ObjectInputStream ois; // ObjectInputStream for transfer o-o-t packet
	private final ObjectOutputStream oos; // ObjectOutputStream for transfer o-o-t packet
	private final MeviusHandler handler; // Handler for control packet and connection events
	private final Pipeline sendPipeline; // The sending pipeline
	private final Pipeline recievePipeline;
	
	
	// USAGE
	// MeviusClient client = new MeviusClient(InetAddress.getByname("localhost") , 3303, new MeviusHandler());
	// client.start();
	public MeviusClient(InetAddress addr, int port, MeviusHandler handler, Pipeline sendPipeline, Pipeline recievePipeline) throws IOException {
		socket = new Socket(addr, port); // Create new socket for addr:port
		uuid = UUID.randomUUID(); // Generate new random UniqueId
		oos = new ObjectOutputStream(socket.getOutputStream()); // Create OutputStream from socket outputStream
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream()); // Create InputStream from socket inputStream
		this.handler = handler; // Init handler
		this.handler.connection(ConnectionType.CLIENT_CONNECT_TO_SERVER, this); // Call ConnectionEvent 
		this.sendPipeline = (sendPipeline != null) ? sendPipeline : Pipeline.DEFAULT_PIPELINE.get();
		this.recievePipeline = (recievePipeline != null) ? recievePipeline : Pipeline.DEFAULT_PIPELINE.get();
	}

	// Constructor for MeviusServer
	public MeviusClient(Socket socket, MeviusHandler handler, Pipeline sendPipeline, Pipeline recievePipeline) throws IOException { // ** WARN ** Not for user
		this(socket.getInetAddress(), socket.getPort(), new MeviusHandler(), sendPipeline, recievePipeline);
	}

	public void close() throws IOException {
		this.socket.close(); // Close
	}

	public boolean isClosed() {
		return this.socket.isClosed(); // Configure out is socket closed
	}

	public final Socket getSocket() {
		return this.socket; // get Socket 
	}

	public final UUID getUUID() {
		return this.uuid; // get UniqueId
	}

	public void run() {
		while (!this.isInterrupted()) {
			try {
				Object obj = recievePipeline.process(ois.readObject());
				if (!(obj instanceof MeviusPacket))
					continue; // If Object isn't MeviusPacket, jump to next Object
				MeviusPacket packet = (MeviusPacket) obj; // VAR PAcket
				handler.callEvent(MeviusHandler.getPacketEventInstance(packet, this, PacketEventType.RECEIVE)); // Call Packet Receive Event
			} catch (ClassNotFoundException | IOException e) {
				try {
					disconnect(); // Disconnect on error
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void disconnect() throws IOException {
		socket.shutdownInput();
		socket.shutdownOutput();
		socket.close();
		handler.connection(ConnectionType.CLIENT_DISCONNECT_FROM_SERVER, this);
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

	public void sendPacket(Serializable packet) throws IOException {
		if(packet instanceof MeviusPacket) {
			try {
				Method m = MeviusPacket.class.getDeclaredMethod("onSend");
				m.setAccessible(true);
				m.invoke(packet);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			
			oos.writeObject(packet);
			handler.callEvent(MeviusHandler.getPacketEventInstance((MeviusPacket) packet, this, PacketEventType.SEND));
			oos.flush();
		}
		
		Object toSend = sendPipeline.process(packet);
		
		try {
			sendPacket(new MeviusRawObjectPacket((Serializable)toSend));
		} catch (ClassCastException e) {
			throw new RuntimeException("Processed Object not ready for sending (Serialization)", e);
		}
	}

	public MeviusHandler getHandler() {
		return handler;
	}
}
