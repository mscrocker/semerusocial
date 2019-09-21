package es.udc.fi.dc.fd.dtos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

public class PruebaImagen {
	public static void main(String args[]) throws Exception {
		BufferedImage bImage = ImageIO.read(new File("/home/alex/Imágenes/shingeki2.jpg"
				));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bImage, "jpg", bos);
		byte[] data = bos.toByteArray();
		
		String s = new String(data);
		System.out.println(s);
	}
}
