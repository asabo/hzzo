/*
 * Project opticari
 *
 */
package biz.sunce.util;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import javax.swing.text.BadLocationException;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Konstante;
import biz.sunce.optika.Logger;

/**
 * datum:2005.09.19
 * 
 * @author asabo ucitava dokument iz klase ili file sistema i
 */
public final class HtmlPrintParser {

	// ucitava predlozak, gleda ima li u predlosku linkova na druge predloske i
	// ugradjuje ih u predlozak
	public String ucitajHtmlPredlozak(String datoteka) {

		java.io.InputStream is = null;
		
		String[] isp = null;
		String ispis = null;
		Scanner scanner;

		if (datoteka == null)
			return null;

		URL url=null;
		 
		try {
			url = Thread.currentThread().getContextClassLoader().getResource(datoteka);
			//is = ClassLoader.getSystemClassLoader().getSystemResourceAsStream(datoteka);
			// is = resource.openStream();

			scanner = new Scanner(url.openStream());
			StringBuilder sb = new StringBuilder(8096);

			while (scanner.hasNext()) {
				sb.append(scanner.nextLine());
			}
			scanner.close(); 
			
			ispis = sb.toString();
			sb=null;
			int ind = 0;

			while (ind != -1) {
				ind = ispis.indexOf("<!--datoteka:ukljuci", ind);
				if (ind == -1)
					break; // nema vise?
				// tu ukljucujemo drugu datoteku
				// <!--datoteka:ukljuci naziv=pregled_zaglavlje.html-->
				int zadnjiInd = ispis.indexOf("-->", ind) + 3;
				String dat = ispis.substring(ind, zadnjiInd);
				int nazInd = dat.indexOf("naziv=");
				nazInd += 6;
				int krajNaz = nazInd;

				while (krajNaz < dat.length() && dat.charAt(krajNaz) != ' '
						&& dat.charAt(krajNaz) != '-')
					krajNaz++;
				String datNaziv = dat.substring(nazInd, krajNaz);

				if (!datNaziv.startsWith("/"))
					datNaziv = nadjiRootZaLink(datoteka) + "/" + datNaziv;

				if (!datoteka.equals(datNaziv)) // da sprijecimo eventualnu
												// beskonacnu rekurziju i slicne
												// hakeraje..
				{
					String dodatak = ucitajHtmlPredlozak(datNaziv);
					ispis = ispis.replaceFirst(dat, dodatak);
				}

				ind = zadnjiInd;
			}// while

		} catch (NullPointerException npe) {
			Logger.log(
					"NPE iznimka kod HtmlPrintParser.ucitajHtmlPredlozak za predl.: "
							+ datoteka + " url:" + url, npe);
		} catch (Exception ioe) {
			Logger.log(
					"IO iznimka kod HtmlPrintParser.ucitajHtmlPredlozak za predl.: "
							+ datoteka, ioe);
		} 
		finally {			
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
			}
		}
		return ispis;
	}// ucitajHtmlPredlozak

	// ucitava predlozak, gleda ima li u predlosku linkova na druge predloske i
	// ugradjuje ih u predlozak
	public static final BufferedImage ucitajSliku(String datoteka) {
		java.io.InputStream is = null;

		if (datoteka == null)
			return null;

		try {
			is = ClassLoader.getSystemResourceAsStream(datoteka);

			// ne postoji unutar nekog jar-a? mozda je fizicki link na neku
			// datoteku
			if (is == null) {
				try {
					is = new FileInputStream(datoteka);
				} catch (java.io.FileNotFoundException nfo) {
					is = null;
				}
			}// if is==null

			if (is == null)
				return null;

			return PictureResizer.vratiKaoBufferedImage(is);
		} catch (Exception ioe) {
			Logger.log("Iznimka kod HtmlPrintParser.ucitajSliku", ioe);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
			}
		}
		return null;
	}// ucitajSliku

	private String nadjiRootZaLink(String link) {
		String root = "";
		if (link == null)
			return null;
		root = link.substring(0, link.lastIndexOf("/"));
		return root;
	}// nadjiRootZaLink

	public static boolean ispisHTMLDokumentaNaStampac(String ispis,
			String nazivPosla) {
		return ispisHTMLDokumentaNaStampac(ispis, false, nazivPosla);
	}

	public static boolean ispisHTMLDokumentaNaStampac(String ispis,
			boolean landscape, String nazivPosla) {
		java.io.ByteArrayInputStream bains = new java.io.ByteArrayInputStream(
				ispis.getBytes());

		Charset cset = Charset.forName(Konstante.DEFAULT_CHARSET);

		java.io.InputStreamReader isr = new java.io.InputStreamReader(bains,
				cset);
		java.io.BufferedReader br = new java.io.BufferedReader(isr); // new
																		// StringReader(ispis)

		// Create empty HTMLDocument to read into
		HTMLEditorKit htmlKit = new HTMLEditorKit();

		HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();

		// Create parser (javax.swing.text.html.parser.ParserDelegator)
		//HTMLEditorKit.Parser parser = new ParserDelegator();
		// Get parser callback from document

		// javax.swing.text.Element
		// body=htmlDoc.getRootElements()[0].getElement(0);

		HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
		// Load it (true means to ignore character set)
		/*
		 * try { parser.parse(br, callback, true); } catch (IOException ex1) {
		 * System
		 * .out.println("IOException kod parsiranja html dokumenta: "+ex1);
		 * return false; }
		 */

		try {
			htmlKit.read(br, htmlDoc, 0);
		} catch (ChangedCharSetException ex1) {
			Logger.log(
					"CCSEXC kod HtmlPrintParser:" + ex1 + ":"
							+ ex1.getCharSetSpec(), ex1);

		} catch (IOException ex1) {
			Logger.log("IOEXC kod HtmlPrintParser:" + ex1, ex1);
		}

		catch (BadLocationException ex1) {
			Logger.log("badlocexc kod HtmlPrintParser:" + ex1, ex1);
		}

		DocumentRenderer dr = new DocumentRenderer();
		if (landscape)
			dr.setOrientationLandscape();

		try {
			dr.setZaglavlje(GlavniFrame.getZaglavljeDokumenata());
			dr.print(htmlDoc, nazivPosla);

		} catch (Exception ex) {
			System.out.println("Greska kod printanja dokumenta:" + ex);
			return false;
		}

		return true;
	}// ispisHTMLDokumentaNaStampac
}// klasa
