package biancso.mevius.utils;

public class ThreadTimer extends Thread {
	private final Thread thread;
	private long time = 10000;

	public ThreadTimer(Thread thread) {
		this.thread = thread;
	}

	public ThreadTimer setTime(int time) {
		this.time = time * 1000;
		return this;
	}

	public ThreadTimer setTime(long time) {
		this.time = time;
		return this;
	}

	public void run() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (thread.isAlive())
			thread.interrupt();
		interrupt();
	}

}
