package biancso.mevius.packet.handler.exceptions;

@SuppressWarnings("serial")
public class ListenerAlreadyRegisteredException extends Exception {

	public ListenerAlreadyRegisteredException(String msg) {
		super(msg);
	}
}
