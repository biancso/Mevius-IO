package biancso.mevius.utils.cipher;

public enum MeviusCipherAction {
	ENCRYPT(0), DECRYPT(1);

	private final int action;

	private MeviusCipherAction(int action) {
		this.action = action;
	}

	public int getAction() {
		return this.action;
	}
}
