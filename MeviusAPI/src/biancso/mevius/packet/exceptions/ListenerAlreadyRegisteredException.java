package biancso.mevius.packet.exceptions;

public class ListenerAlreadyRegisteredException extends Exception {

	private static final long serialVersionUID = -8207295730878082733L;

	public ListenerAlreadyRegisteredException(String msg) {
		super(msg);
	}
}
