package biancso.mevius.utils.cipher;

public class MeviusCipher {
	private byte[] bytedata;
	private String plaindata;

	public MeviusCipher(MeviusCipherType type, MeviusCipherAction action, Object toEncrypt) {
		switch (action.getAction()) {
		case 0: // on encrypt
			switch (type.getType()) {
			case "rsa":

				break;
			}
			break;
		case 1: // on decrypt

			break;
		}
	}

	private void rsaencrypt() {

	}

}
