package biz.sunce.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import biz.sunce.optika.Logger;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGDecodeParam;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: ANSA
 * </p>
 * 
 * @author Ante Sabo
 * @version 1.0
 */

public final class PictureResizer {
	public PictureResizer() {
	}

	public byte[] preformatirajSlikuKaoJPEG(java.io.InputStream ins,
			int potrebnaSirina, int potrebnaDuzina, float kvaliteta) {
		int sirina, duzina;
		byte[] out;

		if (ins == null)
			return null;

		try {
			com.sun.image.codec.jpeg.JPEGImageDecoder jpegD;
			int velicina = ins.available();

			jpegD = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(ins);

			java.awt.image.BufferedImage slika;

			slika = jpegD.decodeAsBufferedImage();

			ins.close();

			sirina = slika.getWidth();
			duzina = slika.getHeight();

			double omjerD = 1.0d, omjerS = 1.0d;

			if (potrebnaSirina > 0) {
				omjerS = (double) potrebnaSirina / (double) sirina;
				omjerD = omjerS;
			}
			if (potrebnaDuzina > 0) {
				omjerD = (double) potrebnaDuzina / (double) duzina;

				if (potrebnaSirina <= 0)
					omjerS = omjerD;
			}

			sirina *= omjerS;
			duzina *= omjerD;

			// Get a graphics region, using the Frame
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
					sirina, duzina, java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image
					.getGraphics();
			graf.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
					java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
			graf.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
					java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjerS, omjerD);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			out = this.zakodirajUJpeg(image, kvaliteta);

			image = null;
			// izlaznaSlika=null;
			tx = null;
			slika = null;
			jpegD = null;
			graf = null;
		} catch (NullPointerException e) {
			Logger.log("Null pointer greska kod smanjivanja slike: " + e
					+ " Linija: " + e.getStackTrace()[0].getLineNumber());
			return null;
		} catch (java.io.IOException ioe) {
			Logger.log("IO greska kod smanjivanja slike: " + ioe + " Linija: "
					+ ioe.getStackTrace()[0].getLineNumber());
			return null;
		}

		return out;
	} // preformatirajSlikuKaoJPEG

	public byte[] preformatirajSlikuKaoJPEG(java.awt.image.BufferedImage slika,
			int potrebnaSirina, int potrebnaDuzina, float kvaliteta) {
		int sirina, duzina;
		byte[] out;

		if (slika == null)
			return null;

		try {

			sirina = slika.getWidth();
			duzina = slika.getHeight();

			double omjerD = 1.0d, omjerS = 1.0d;

			if (potrebnaSirina > 0) {
				omjerS = (double) potrebnaSirina / (double) sirina;
				omjerD = omjerS;
			}
			if (potrebnaDuzina > 0) {
				omjerD = (double) potrebnaDuzina / (double) duzina;

				if (potrebnaSirina <= 0)
					omjerS = omjerD;
			}

			sirina *= omjerS;
			duzina *= omjerD;

			// Get a graphics region, using the Frame
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
					sirina, duzina, java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image
					.getGraphics();
			graf.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
					java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
			graf.setRenderingHint(
					java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,
					java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjerS, omjerD);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			out = this.zakodirajUJpeg(image, kvaliteta);

			image = null;
			// izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			Logger.log("Null pointer greska kod smanjivanja slike: " + e
					+ " Linija: " + e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return out;
	} // preformatirajSlikuKaoJPEG

	public byte[] preformatirajSlikuKaoJPEG(java.awt.image.BufferedImage slika,
			float kvaliteta) {
		int sirina, duzina;
		byte[] out;

		if (slika == null)
			return null;

		try {
			out = this.zakodirajUJpeg(slika, kvaliteta);
		} catch (NullPointerException e) {
			Logger.log("Null pointer greska kod smanjivanja slike: " + e
					+ " Linija: " + e.getStackTrace()[0].getLineNumber());
			return null;
		}
		return out;
	} // preformatirajSlikuKaoJPEG

	public java.awt.image.BufferedImage preformatirajSliku(
			java.awt.image.BufferedImage slika, int potrebnaSirina) {
		int sirina, duzina;
		java.awt.image.BufferedImage image;
		byte[] out;

		if (slika == null)
			return null;

		try {
			sirina = slika.getWidth();
			duzina = slika.getHeight();

			double omjer = 1.0d;

			if (potrebnaSirina > 0)
				omjer = (double) potrebnaSirina / (double) sirina;

			sirina *= omjer;
			duzina *= omjer;

			// Get a graphics region, using the Frame
			image = new java.awt.image.BufferedImage(sirina, duzina,
					slika.getType());

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image
					.getGraphics();
			graf.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
					java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			// izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			Logger.log("Null pointer greska kod smanjivanja slike: " + e
					+ " Linija: " + e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public java.awt.image.BufferedImage preformatirajSlikuBezAliasa(
			java.awt.image.BufferedImage slika, int potrebnaSirina) {
		int sirina, duzina;
		java.awt.image.BufferedImage image;
		byte[] out;

		if (slika == null)
			return null;

		try {
			sirina = slika.getWidth();
			duzina = slika.getHeight();

			double omjer = 1.0d;

			if (potrebnaSirina > 0)
				omjer = (double) potrebnaSirina / (double) sirina;

			sirina *= omjer;
			duzina *= omjer;

			// Get a graphics region, using the Frame
			image = new java.awt.image.BufferedImage(sirina, duzina,
					slika.getType());

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image
					.getGraphics();
			// graf.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
			// graf.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,java.awt.RenderingHints.VALUE_RENDER_QUALITY);
			// graf.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			// graf.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL,java.awt.RenderingHints.VALUE_STROKE_PURE);
			graf.setRenderingHint(
					java.awt.RenderingHints.KEY_FRACTIONALMETRICS,
					java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			// izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			Logger.log("Null pointer greska kod smanjivanja slike: " + e
					+ " Linija: " + e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public java.awt.image.BufferedImage preformatirajSlikuPoDuzini(
			java.awt.image.BufferedImage slika, int potrebnaDuzina) {
		int sirina, duzina;
		java.awt.image.BufferedImage image;
		byte[] out;

		if (slika == null)
			return null;

		try {
			sirina = slika.getWidth();
			duzina = slika.getHeight();

			if (duzina < potrebnaDuzina)
				return slika;

			double omjer = 1.0d;

			if (potrebnaDuzina > 0)
				omjer = (double) potrebnaDuzina / (double) duzina;

			sirina *= omjer;
			duzina *= omjer;

			// Get a graphics region, using the Frame
			image = new java.awt.image.BufferedImage(sirina, duzina,
					java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image
					.getGraphics();
			graf.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
					java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			// izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			Logger.log("Null pointer greska kod smanjivanja slike: " + e
					+ " Linija: " + e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public java.awt.image.BufferedImage preformatirajSliku(
			java.io.InputStream ins, int potrebnaSirina) {
		int sirina, duzina;

		java.awt.image.BufferedImage image = null, slika = null;

		byte[] out, in;

		if (ins == null)
			return null;

		try {
			com.sun.image.codec.jpeg.JPEGImageDecoder jpegD;
			int velicina = ins.available();

			jpegD = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(ins);

			ins.close();

			slika = jpegD.decodeAsBufferedImage();
			sirina = slika.getWidth();
			duzina = slika.getHeight();

			double omjer = 1.0d;

			if (potrebnaSirina > 0)
				omjer = (double) potrebnaSirina / (double) sirina;

			sirina *= omjer;
			duzina *= omjer;

			// Get a graphics region, using the Frame
			image = new java.awt.image.BufferedImage(sirina, duzina,
					java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image
					.getGraphics();
			graf.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
					java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			// izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			System.out.println("Null pointer greska kod smanjivanja slike: "
					+ e + " Linija: " + e.getStackTrace()[0].getLineNumber());
			return null;
		} catch (java.io.IOException ioe) {
			Logger.log("IO greska kod smanjivanja slike: " + ioe + " Linija: "
					+ ioe.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public static java.awt.image.BufferedImage vratiKaoBufferedImage(
			java.io.InputStream ins) {
		java.awt.image.BufferedImage slika = null;

		if (ins == null)
			return null;

		try {
			com.sun.image.codec.jpeg.JPEGImageDecoder jpegD;
			int velicina = ins.available();

			if (velicina < 1)
				return null;

			jpegD = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(ins);

			// 24.08.2004. iz nekog razloga zna se dogoditi da dekoder ne moze
			// dekodirati sliku iz baze
			// pa smo ugradili try/catch i stavili da je slika=null
			try {
				// slika = jpegD.decodeAsBufferedImage();
				slika = ImageIO.read(ins);
			} catch (Exception ex1) {
				slika = null;
			}

		} catch (IOException ex) {
		} catch (ImageFormatException ex) {
			Logger.log("Greska u PictureResizer / metoda vratiKaoBufferedImage: "
					+ ex);
			return null;
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException ioe) {
			}
		}

		return slika;
	} // vratiKaoBufferedImage

	private byte[] zakodirajUJpeg(java.awt.image.BufferedImage slika,
			float kvaliteta) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream(4096);

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

			JPEGEncodeParam jpegParams = encoder
					.getDefaultJPEGEncodeParam(slika);
			jpegParams.setQuality(kvaliteta, false); // kvaliteta slike - 38%
			jpegParams.setXDensity(72);
			jpegParams.setYDensity(72);
			jpegParams.setDensityUnit(JPEGDecodeParam.DENSITY_UNIT_DOTS_INCH);
			encoder.setJPEGEncodeParam(jpegParams);
			encoder.encode(slika); // Encode image to JPEG and send to browser

			encoder = null;
		} catch (IOException ex) {
			Logger.log("IO greska kod spremanja slike u polje byteova:" + ex);
			return null;
		}

		return out != null ? out.toByteArray() : null;
	} // zakodirajUJpeg

}// PictureResizer
