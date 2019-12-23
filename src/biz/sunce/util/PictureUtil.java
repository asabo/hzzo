/*
 * Project opticari
 *
 */
package biz.sunce.util;

import java.io.*;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.*;

/**
 * @author Ante Sabo
 * @version 1.0
 * 08.05.05. -asabo- sve sto bi trebalo za rad sa slikama, preformatravanje, zakodiravanje...
 */

public class PictureUtil {
	public static byte[] preformatirajSlikuKaoJPEG(
		java.io.InputStream ins,
		int potrebnaSirina,
		int potrebnaDuzina,
		float kvaliteta) {
		int sirina, duzina;
		byte[] out;

		if (ins == null)
			return null;

		try {
			 
			java.awt.image.BufferedImage slika;

			slika = ImageIO.read(ins);

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
			java.awt.image.BufferedImage image =
				new java.awt.image.BufferedImage(
					sirina,
					duzina,
					java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image.getGraphics();
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_INTERPOLATION,
				java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjerS, omjerD);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			out = zakodirajUJpeg(image, kvaliteta);

			image = null;
			//izlaznaSlika=null;
			tx = null;
			slika = null;
			 
			graf = null;
		} catch (NullPointerException e) {
			System.out.println(
				"Null pointer greska kod smanjivanja slike: "
					+ e
					+ " Linija: "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		} catch (java.io.IOException ioe) {
			System.out.println(
				"IO greska kod smanjivanja slike: "
					+ ioe
					+ " Linija: "
					+ ioe.getStackTrace()[0].getLineNumber());
			return null;
		}

		return out;
	} // preformatirajSlikuKaoJPEG

	public static byte[] preformatirajSlikuKaoJPEG(
		java.awt.image.BufferedImage slika,
		int potrebnaSirina,
		int potrebnaDuzina,
		float kvaliteta) {
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
			java.awt.image.BufferedImage image =
				new java.awt.image.BufferedImage(
					sirina,
					duzina,
					java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image.getGraphics();
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,
				java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjerS, omjerD);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			out = zakodirajUJpeg(image, kvaliteta);

			image = null;
			//izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			System.out.println(
				"Null pointer greska kod smanjivanja slike: "
					+ e
					+ " Linija: "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return out;
	} // preformatirajSlikuKaoJPEG

	public static final byte[] preformatirajSlikuKaoJPEG(
		java.awt.image.BufferedImage slika,
		float kvaliteta) {
		int sirina, duzina;
		byte[] out;

		if (slika == null)
			return null;

		try {
			out = zakodirajUJpeg(slika, kvaliteta);
		} catch (NullPointerException e) {
			System.out.println(
				"Null pointer greska kod smanjivanja slike: "
					+ e
					+ " Linija: "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		}
		return out;
	} // preformatirajSlikuKaoJPEG

	public final static java.awt.image.BufferedImage preformatirajSliku(
		java.awt.image.BufferedImage slika,
		int potrebnaSirina) {
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
			image = new java.awt.image.BufferedImage(sirina, duzina, slika.getType());

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image.getGraphics();
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			//izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			System.out.println(
				"Null pointer greska kod smanjivanja slike: "
					+ e
					+ " Linija: "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public final static java.awt.image.BufferedImage preformatirajSlikuBezAliasa(
		java.awt.image.BufferedImage slika,
		int potrebnaSirina) {
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
			image = new java.awt.image.BufferedImage(sirina, duzina, slika.getType());

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image.getGraphics();
			//graf.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
			//graf.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,java.awt.RenderingHints.VALUE_RENDER_QUALITY);
			//graf.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			//graf.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL,java.awt.RenderingHints.VALUE_STROKE_PURE);
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_FRACTIONALMETRICS,
				java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			//izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			System.out.println(
				"Null pointer greska kod smanjivanja slike: "
					+ e
					+ " Linija: "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public final static java.awt.image.BufferedImage preformatirajSlikuPoDuzini(
		java.awt.image.BufferedImage slika,
		int potrebnaDuzina) {
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
			image =
				new java.awt.image.BufferedImage(
					sirina,
					duzina,
					java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image.getGraphics();
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			//izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			System.out.println(
				"Null pointer greska kod smanjivanja slike: "
					+ e
					+ " Linija: "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public final static java.awt.image.BufferedImage preformatirajSliku(
		java.io.InputStream ins,
		int potrebnaSirina) {
		int sirina, duzina;

		java.awt.image.BufferedImage image = null, slika = null;

		byte[] out, in;

		if (ins == null)
			return null;

		try {
	
			ins.close();

			slika = ImageIO.read(ins);
			sirina = slika.getWidth();
			duzina = slika.getHeight();

			double omjer = 1.0d;

			if (potrebnaSirina > 0)
				omjer = (double) potrebnaSirina / (double) sirina;

			sirina *= omjer;
			duzina *= omjer;

			// Get a graphics region, using the Frame
			image =
				new java.awt.image.BufferedImage(
					sirina,
					duzina,
					java.awt.image.BufferedImage.TYPE_INT_RGB);

			java.awt.Graphics2D graf = (java.awt.Graphics2D) image.getGraphics();
			graf.setRenderingHint(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

			// Set the scale.
			java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

			tx.scale(omjer, omjer);

			graf.drawImage(slika, tx, null);
			graf.dispose();

			//izlaznaSlika=null;
			tx = null;
			slika = null;
			graf = null;
		} catch (NullPointerException e) {
			System.out.println(
				"Null pointer greska kod smanjivanja slike: "
					+ e
					+ " Linija: "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		} catch (java.io.IOException ioe) {
			System.out.println(
				"IO greska kod smanjivanja slike: "
					+ ioe
					+ " Linija: "
					+ ioe.getStackTrace()[0].getLineNumber());
			return null;
		}

		return image;
	} // preformatirajSliku

	public final static java.awt.image.BufferedImage vratiKaoBufferedImage(
		java.io.InputStream ins) {
		java.awt.image.BufferedImage slika = null;

		if (ins == null)
			return null;

		try {
 
			int velicina = ins.available();

			if (velicina < 1)
				return null;

			 

			//24.08.2004. iz nekog razloga zna se dogoditi da dekoder ne moze dekodirati sliku iz baze
			// pa smo ugradili try/catch i stavili da je slika=null
			try {
				slika = ImageIO.read(ins);
			} catch (Exception ex1) {
				slika = null;
			}

		} catch (IOException ex) {
		}  finally 
		{
			try {if (ins != null)ins.close();} catch (IOException ioe) {}
		}

		return slika;
	} // vratiKaoBufferedImage

	private final static byte[] zakodirajUJpeg(
		java.awt.image.BufferedImage slika,
		float kvaliteta) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream(4096);

			ImageIO.write(slika, "jpeg", out);
		} catch (IOException ex) {
			System.out.println("IO greska kod spremanja slike u Blob:" + ex);
			return null;
		}

		return out.toByteArray();
	} // zakodirajUJpeg

} //PictureResizer
