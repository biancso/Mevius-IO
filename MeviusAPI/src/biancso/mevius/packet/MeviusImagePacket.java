package biancso.mevius.packet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class MeviusImagePacket extends MeviusPacket {

	private final byte[] imageBuffer;

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
}
