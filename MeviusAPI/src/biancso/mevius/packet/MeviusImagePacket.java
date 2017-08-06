package biancso.mevius.packet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import biancso.mevius.client.MeviusClient;
import biancso.mevius.packet.events.ImagePacketEvent;
import biancso.mevius.packet.events.PacketEvent;
import biancso.mevius.packet.events.PacketEventType;

public class MeviusImagePacket extends MeviusPacket {

	private static final long serialVersionUID = -2943150285073054863L;

	private final byte[] imageBuffer;

	// USAGE
	// MeviusImagePacket packet = new MeviusImagePacket(BufferedImage);
	public MeviusImagePacket(BufferedImage image) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(image.getHeight() * image.getWidth());
		ImageIO.setUseCache(false);
		ImageIO.write(image, "jpg", baos);
		imageBuffer = baos.toByteArray();
		baos.flush();
		baos.close();
	}

	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new ByteArrayInputStream(imageBuffer));
	}

	@Override
	public PacketEvent createEvent(MeviusClient client, PacketEventType type) {
		return new ImagePacketEvent(this, client, type);
	}
}
