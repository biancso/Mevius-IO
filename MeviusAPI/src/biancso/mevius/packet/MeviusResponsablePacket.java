package biancso.mevius.packet;

import java.util.Date;
import java.util.UUID;

@SuppressWarnings("serial")
public abstract class MeviusResponsablePacket extends MeviusPacket implements Cloneable {
	private final UUID packetUUId;
	private Date timeout;
	long time = 10000;

	public MeviusResponsablePacket() {
		packetUUId = UUID.randomUUID();
	}

	public final UUID getUUId() {
		return packetUUId;
	}

	public final void setTimeOut(long time) {
		if (time < 1000)
			throw new IllegalArgumentException(new Throwable("Time must be longer then 1000"));
		this.time = time;
	}

	public final void setTimeOut(int second) {
		if (second < 1)
			throw new IllegalArgumentException(new Throwable("Second must be longer then 1"));
		this.time = second * 1000;
	}

	public final long getTimeOut() {
		return time;
	}

	public final boolean isSent() {
		return timeout != null;
	}

	public final boolean isOutDated() {
		return timeout.before(new Date());
	}

	@SuppressWarnings("unused")
	private final void sent() {
		Date date = new Date();
		date.setTime(date.getTime() + time);
		timeout = date;
	}

}
