package biancso.mevius.client;

import java.io.Closeable;
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

/**
 * The Mevius Client.<br>
 * 
 * This is the entrypoint of IO with the Mevius library.
 * 
 * @author biancso
 *
 */
public class MeviusClient
extends Thread
implements Closeable {

	protected final Socket socket;
	protected final UUID uuid;
	protected final MeviusHandler handler;

	protected ObjectOutputStream oos;
	protected ObjectInputStream ois;

	protected final Pipeline sendPipeline;
	protected final Pipeline receivePipeline;
	
	protected int timeout;

	/**
	 * Constructs a new instance of {@link MeviusClient} with all arguments
	 * 
	 * @param addr The address to connect to
	 * @param port The port to use
	 * @param handler The MeviusHandler to use.
	 * @param sendPipeline The sending pipeline (May be <code>null</code>)
	 * @param receivePipeline The receiving pipeline (May be <code>null</code>)
	 * @throws IOException If any IOException occurs
	 */
	public MeviusClient(InetAddress addr, int port, MeviusHandler handler, Pipeline sendPipeline, Pipeline receivePipeline, int timeout) throws IOException {
		socket = new Socket(addr, port);
		uuid = UUID.randomUUID();
		this.handler = handler;
		this.handler.connection(ConnectionType.CLIENT_CONNECT_TO_SERVER, this);
		this.sendPipeline = (sendPipeline != null) ? sendPipeline : Pipeline.DEFAULT_PIPELINE.get();
		this.receivePipeline = (receivePipeline != null) ? receivePipeline : Pipeline.DEFAULT_PIPELINE.get();
		this.timeout = timeout;
	}

	/**
	 * A simple constructor of {@link MeviusClient} with minimal arguments
	 * 
	 * @param socket The socket to connect to
	 * @param handler The handler to use
	 * @param sendPipeline The sending pipeline (May be <code>null</code>)
	 * @param receivePipeline The receiving pipeline (May be <code>null</code>)
	 * @throws IOException If any IO exception occurs
	 */
	public MeviusClient(Socket socket, MeviusHandler handler, Pipeline sendPipeline, Pipeline recievePipeline) throws IOException { // ** WARN ** Not for user
		this(socket.getInetAddress(), socket.getPort(), new MeviusHandler(), sendPipeline, recievePipeline, 1000);
	}

	/**
	 * Connects to the given address
	 * 
	 * @throws IOException If any IO Exception happens when connecting
	 */
	public void connect() throws IOException {
		if(oos != null) oos.close();
		if(ois != null) ois.close();
		
		if(socket != null)
			if(!socket.isClosed())
				socket.close();
		
		socket.connect(socket.getRemoteSocketAddress(), timeout);
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	/**
	 * Sets the connection timeout when using {@link MeviusClient#connect()}
	 * 
	 * @param timeout The timeout to use
	 */
	public void setConnectionTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * @return Gets the current timeout value
	 */
	public int getConnectionTimeout() {
		return timeout;
	}

	/**
	 * Closes the client and underlying Objects
	 * 
	 * @throws IOException If any IO Exception occurs when closing
	 */
	@Override
	public void close() throws IOException {
		oos.close();
		ois.close();
		this.socket.close();
	}

	/**
	 * If the underlying connection has been closed
	 * 
	 * @return Has the underlying connection been closed
	 */
	public boolean isClosed() {
		return this.socket.isClosed();
	}

	/**
	 * Gets the remote {@link Socket socket}
	 * 
	 * @return The remote {@link Socket}
	 */
	public final Socket getSocket() {
		return this.socket;
	}

	/**
	 * Gets the {@link UUID} of this
	 * 
	 * @return The {@link UUID} of this
	 */
	public final UUID getUUID() {
		return this.uuid;
	}

	/**
	 * Starts running the client.<br>
	 * This automatically connects to the remote server if not already connected.
	 */
	public void run() {
		try {
			connect();
		} catch (IOException e2) {
			handler.exception(e2);
			return;
		}
		
		while (!this.isInterrupted()) {
			try {
				Object obj = receivePipeline.process(ois.readObject());
				if (!(obj instanceof MeviusPacket))
					continue;
				MeviusPacket packet = (MeviusPacket) obj;
				handler.callEvent(MeviusHandler.getPacketEventInstance(packet, this, PacketEventType.RECEIVE)); // Call Packet Receive Event
			} catch (ClassNotFoundException | IOException e) {
				try {
					disconnect();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Disconnects from the remote server
	 * 
	 * @throws IOException If any IO exception occurs
	 */
	public void disconnect() throws IOException {
		socket.shutdownInput();
		socket.shutdownOutput();
		socket.close();
		handler.connection(ConnectionType.CLIENT_DISCONNECT_FROM_SERVER, this);
	}

	/**
	 * Gets the hash code of the client
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result + ((ois == null) ? 0 : ois.hashCode());
		result = prime * result + ((oos == null) ? 0 : oos.hashCode());
		result = prime * result + ((receivePipeline == null) ? 0 : receivePipeline.hashCode());
		result = prime * result + ((sendPipeline == null) ? 0 : sendPipeline.hashCode());
		result = prime * result + ((socket == null) ? 0 : socket.hashCode());
		result = prime * result + timeout;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/**
	 * Returns true only if the other object <i>is</i> this object
	 */
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	/**
	 * Sends a packet, running it through the pipeline if it's not a<br>
	 * {@link MeviusPacket} and turning that into a {@link MeviusRawObjectPacket}<br>
	 * if the return is still not a {@link MeviusPacket}
	 * 
	 * @param packet The packet to send
	 * @throws IOException If any I/O exception occurs
	 */
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

	/**
	 * Gets the current {@link MeviusHandler} being used
	 * 
	 * @return The current {@link MeviusHandler} being used
	 */
	public MeviusHandler getHandler() {
		return handler;
	}
}
