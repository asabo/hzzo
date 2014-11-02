/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo.ispis;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.optika.Logger;
import biz.sunce.util.beans.PostavkeBean;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.optika.GlavniFrame;

/**
 * datum:2006.02.27
 * 
 * @author asabo
 * 
 */
public final class IspisRacunaPotOrt implements Printable {
	protected PageFormat pFormat;
	protected PrinterJob pJob;

	private java.awt.Font fontPodnozje;
	private int visinaPodnozja = 0;
	public static double A4_SIRINA_PX = 595.2756d;
	public static double A4_VISINA_PX = 841.8898d;
	public static double MM_SIRINA_PX = A4_SIRINA_PX / 210.0d; // 2.83
	public static double MM_VISINA_PX = A4_VISINA_PX / 296.0d; // 2.844

	private final PostavkeBean p = new PostavkeBean();

	int zaglX = 0, zaglY = 0;
	int racX = 0, racY = 0;
	int napX = 0, napY = 0;
	Font glavni = new Font("Arial", Font.PLAIN, 12);
	Font mono = new Font("Courier New", Font.BOLD, 16);
	Font mali = new Font("Times New Roman", Font.PLAIN, 9);

	int pocetakStraniceOdskokX = 0;
	int pocetakStraniceOdskokY = 0;// 15;

	float omjerSir = 1.0f;
	float omjerDuz = 1.0f;

	RacunVO racun = null;

	public IspisRacunaPotOrt(RacunVO racun) {
		this.racun = racun;
		pFormat = new PageFormat();
		pJob = PrinterJob.getPrinterJob();
		// this.pFormat=pJob.defaultPage();
		Paper papir = new Paper();
		int marginaT = 30, marginaL = 28;

		papir.setSize(A4_SIRINA_PX, A4_VISINA_PX);
		papir.setImageableArea(marginaL, marginaT, papir.getWidth() - marginaL
				* 2, papir.getHeight() - marginaT * 2);

		pFormat.setPaper(papir);

		String osX = PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_X, "0");
		String osY = PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_Y, "0");

		String omSir = PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_SIRINA, "1.0");
		String omDuz = PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_DUZINA, "1.0");

		try {
			pocetakStraniceOdskokX = Integer.parseInt(osX);
			pocetakStraniceOdskokY = Integer.parseInt(osY);
		} catch (NumberFormatException nfe) {
			Logger.log(
					"Neispravne vrijednosti za parametre x i y kod ispisa x:"
							+ osX + " y:" + osY, null);
		}

		try {
			omjerSir = Float.parseFloat(omSir);
			omjerDuz = Float.parseFloat(omDuz);
		} catch (NumberFormatException nfe) {
			Logger.log(
					"Neispravne vrijednosti za parametre sir i duz kod ispisa sir:"
							+ omSir + " duz:" + omDuz, null);
			omjerSir = 1.0f;
			omjerDuz = 1.0f;
		}

		int zaglX = 0, zaglY = 0;

		int racX = (int) (zaglX * omjerSir + 0.5f), racY = (int) (zaglY
				* omjerDuz + 210 * omjerDuz + 0.5f);

		napX = (int) (zaglX * omjerSir + 0.5f);
		napY = (int) (zaglY + 500 * omjerDuz + 0.5f);

	}// konstruktor

	public void printDialog() {
		if (pJob.printDialog()) {
			PageFormat pf = this.pFormat;

			zaglX = 39; // 10 tockica je cca 3 mm
			zaglY = 37; // ovdje margine ne igraju toliko jer se ispisuje nisko
						// dolje i na stvarne obrasce..
						// pa je puno bolji stvarni odskok u milimetrima nego
						// printerove sposobnosti

			zaglX += pocetakStraniceOdskokX;
			zaglY += pocetakStraniceOdskokY;

			racX = zaglX;
			racY = (int) (zaglY + 300 * omjerSir + 0.5f);
			napX = zaglX;
			napY = (int) (zaglY + 690 * omjerDuz + 0.5f);

			pJob.setPrintable(this, pf);
			pJob.setJobName("Hzzo ispis PotOrt");

			try {
				pJob.print();
			} catch (PrinterException printerException) {

				Logger.fatal("Iznimka prilikom ispisa hzzo potOrt racuna: ",
						printerException);
			} catch (Error greska) {
				Logger.fatal(
						"Greska sustava prilikom ispisa hzzo potOrt racuna: ",
						greska);
			}

		}
	}// printDialog

	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		if (pageIndex > 0)
			return NO_SUCH_PAGE;

		g.setFont(glavni);
		double mm = MM_SIRINA_PX; // koliko px je jedan mm
		racY += mm;

		g.setFont(mono);

		String sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();

//		int sifIspLen = sifIsporucitelja.length();
//		for (int i = 0; i < sifIspLen; i++)
//			g.drawString(sifIsporucitelja.charAt(i) + "", (int) (racX + 98
//					* omjerSir + i * 14.0d * omjerSir + 2 * mm * omjerSir),
//					(int) (racY + (24 + 16 * mm) * omjerDuz));

		String brojPotvrde = racun.getBrojPotvrde1(), brojRacuna = racun
				.getBrojPotvrde2();

		// da ne pametujemo previse, moze biti 1,2 ili 3 znaka
		int brPotvLen = brojPotvrde.length();
		if (brPotvLen == 2)
			brojPotvrde = " " + brojPotvrde;
		else if (brPotvLen == 1)
			brojPotvrde = "  " + brojPotvrde;
		
		brPotvLen=brojPotvrde.length();

		for (int i = 0; i < brPotvLen; i++)
			g.drawString(brojPotvrde.charAt(i) + "", (int) (racX
					+ (363 - 14 * mm) * omjerSir + i * 13d * omjerSir + 0.5f),
					(int) (racY + (13 - 2 * mm) * omjerDuz + 0.5f));

		int brRacLen = brojRacuna.length();
		for (int i = 0; i < brRacLen; i++)
			g.drawString(brojRacuna.charAt(i) + "", (int) (racX
					+ (407 - 1 * mm) * omjerSir + i * 14.0d * omjerSir + 0.5f),
					(int) (racY + (13  -2 * mm) * omjerDuz + 0.5f));

		int sidroPrviRed = (int) (40.0d -2.0d * mm);

		// maticni broj isporucitelja
		String mbIsporucitelja = p.getTvrtkaOIB();
		int mbIspLen = mbIsporucitelja.length();
		
		for (int i = 0; i < mbIspLen; i++)
			g.drawString(mbIsporucitelja.charAt(i) + "",
					(int) (racX + (389 ) * omjerSir + i * 14.0d
							* omjerSir + 0.5f), (int) (racY
							+ ( + (1 * mm)) * omjerDuz + 0.5f));

		g.setFont(glavni);
		
		// naziv tvrtke
		String nazivOptike = p.getTvrtkaNaziv();
		g.drawString(nazivOptike,
				(int) (racX + (50 - 5 * mm) * omjerSir + 0.5f), (int) (racY
						+40 + (25*mm) * omjerDuz + 0.5f));
		// adresa tvrtke
		String adresaOptike = p.getMjestoRada() + ", " + p.getTvrtkaAdresa();
		g.drawString(adresaOptike, (int) (racX + (117+19*mm) * omjerSir + 0.5f),
				(int) (racY + ((40.0d -2.0d * mm) + 60-60*mm) * omjerDuz + 0.5f));

		// tvrtka racun
		g.drawString(p.getTvrtkaRacun(), (int) (racX + 75 * omjerSir + 0.5f),
				(int) (racY + (79 + (21 * mm - 70*mm)) * omjerDuz + 0.5f));

		String poziv1 = racun.getPozivNaBroj1(), poziv2 = racun
				.getPozivNaBroj2();
		if (poziv1 == null)
			poziv1 = "";
		if (poziv2 == null)
			poziv2 = "";

		g.drawString(poziv1, (int) (racX + 24 * mm * omjerSir + 0.5f),
				(int) (racY + (79 - 19 * mm) * omjerDuz + 0.5f));
		g.drawString(poziv2, (int) (racX + 32 * mm * omjerSir + 0.5f),
				(int) (racY + (79 - 19 * mm) * omjerDuz + 0.5f));

		g.setFont(mono);
		// sifra proizvodjaca

		List stavke = racun.getStavkeRacuna();

		int startcy = (int) (racY + (146 - 24 * mm) * omjerSir + 0.5f);
		
		double odskokRetka = 19.6d * omjerDuz;
		
		int suma = 0;
		int sumaPorezneOsnove = 0;
		int sumaPoreza = 0;
		PoreznaStopaVO stopa = null;
		int sirinaPoljaNazivPomagala = (int) (61 * mm * omjerSir + 0.5f);

		int stavkeSize = stavke.size();
		for (int sf = 0; sf < stavkeSize; sf++) {
			StavkaRacunaVO st = (StavkaRacunaVO) stavke.get(sf);
			PomagaloVO pom = nadjiPomagalo(st.getSifArtikla());
			stopa = nadjiPoreznuSkupinu(st.getPoreznaStopa()); // 12.03.06.
																// -asabo-
																// dodano

			int cy = (int) (startcy + sf * odskokRetka);
			int lrb = (int) (22.0d * mm); // lijevi rub
			
			g.setFont(mali);
			String sif = st.getSifArtikla();

			// sifra artikla
				g.drawString(sif, (int) (racX + (lrb -4 * mm)
						* omjerSir + 0 * 13.5d * omjerSir + 0.5f), cy);

			g.setFont(mali);
			String naziv = (pom != null ? pom.getNaziv() : "?!?");

			FontMetrics fm = g.getFontMetrics(mali);
			int sir = fm.stringWidth(naziv);

			
			// ako tekst stane unutar predvidjenog prostora trpamo u jedan redak
			if (sir <= sirinaPoljaNazivPomagala) {
				g.drawString(naziv, (int) (racX + (lrb+48*mm) * omjerSir + 0.5f), cy); // cy
																				// je
																				// vec
																				// podesen
																				// sa
																				// faktorom
																				// duzine
			} else {
				int mj = 0;
				int dozSir = 0;
				while (mj < naziv.length()) {
					dozSir += fm.charWidth(naziv.charAt(mj));
					mj++;
					if (dozSir >= sirinaPoljaNazivPomagala)
						break;
				}// prvi while
				g.drawString(naziv.substring(0, mj), (int) (racX + (lrb+48*mm)
						* omjerSir + 0.5f), cy - mali.getSize());

				int prvoMjesto = mj;
				dozSir = 0; // reset
				// drugi redak while
				while (mj < naziv.length()) {
					dozSir += fm.charWidth(naziv.charAt(mj));
					mj++;
					if (dozSir >= sirinaPoljaNazivPomagala)
						break;
				}// drugi while
				g.drawString(naziv.substring(prvoMjesto, mj), (int) (racX + (lrb+48*mm)
						* omjerSir + 0.5f), cy);
			}// else



			// sifra proizvodjaca
//			sif = st.getSifProizvodjaca() + "";
//			String razmak = "";
//			int razlika = 7 - sif.length();
//			for (int i = 0; i < razlika; i++)
//				razmak += " ";
//			sif = razmak + sif;
//
//			for (int i = 0; i < sif.length(); i++)
//				g.drawString(sif.charAt(i) + "", (int) (racX + (lrb + 75 * mm)
//						* omjerSir + i * 13.5d * omjerSir + 0.5f), cy);

			int cijenaBezPDVa = (int) (st.getPoCijeni().intValue()
					/ (1.0f + stopa.getStopa().intValue() / 100.0f) + 0.5f);
			int totalBezPDVa = st.getKolicina().intValue() * cijenaBezPDVa;
			int total = (int) (totalBezPDVa
					* (1.0f + stopa.getStopa().intValue() / 100.0f) + 0.5f);

			int poreznaOsnova = totalBezPDVa;
			int porez = total - totalBezPDVa;

			ispisiKolicinu(g, st.getKolicina().intValue(), (int) (racX
					+ (lrb + 116 * mm) * omjerSir + 0.5f), cy, glavni);
			ispisiNovac(g, st.getPoCijeni().intValue(), (int) (racX
					+ (lrb + 136 * mm) * omjerSir + 0.5f), cy, glavni);
			ispisiNovac(g, total,
					(int) (racX + (lrb + 166 * mm) * omjerSir + 0.5f), cy,
					glavni);
			suma += total;
			sumaPorezneOsnove += poreznaOsnova;
			sumaPoreza += porez;
		}// for sf
		
		int lrb = (int) (14.0d * mm);
		// ukupno prvi dio gore odskokRetka je vec pomnozen sa duzinskim
		// faktorom
		this.ispisiNovac(g, suma, (int) (racX + (lrb + 170 * mm) * omjerSir + 0.5f),
				(int) (startcy + 3 * odskokRetka - 3 * mm), glavni);

		
		int desniRub = (int) ((lrb + 165 * mm) * omjerSir + 0.5f);

		// u slucaju osnovnog osiguranja to je iznos koji placa stranka
		int iznosSudjelovanja = racun.getIznosSudjelovanja() != null ? racun
				.getIznosSudjelovanja().intValue() : 0;

		// ukupan iznos se sastoji od nekoliko proizvoda po nekoliko poreznih
		// skupina
		// i tocan iznos placenog poreza dijeli se u jednakom omjeru na stranku i
		// osiguranje
		// trenutno je to jedini nacin na koji znamo da se porez raspodjeljuje
		float omjerZdr = (float) (iznosSudjelovanja) / suma;
		sumaPoreza = (int) (sumaPoreza * omjerZdr + 0.5f);

		//
		int zbirnicy = (int) (-2 * omjerDuz + startcy + 5 * odskokRetka - 10 * mm);
		
		odskokRetka = 5.5 * mm;

		// ukupni iznos s pdv-om
		this.ispisiNovac(g, suma, racX + desniRub,
				(int) (zbirnicy + 0 * odskokRetka), glavni);
		// iznos za pomagala je isti kao i cijeli iznos
		this.ispisiNovac(g, suma, racX + desniRub,
				(int) (zbirnicy + 1 * odskokRetka), glavni);

		// iznos za postupke (ljekarne) - trenutno toga nema
		this.ispisiNovac(g, 0, racX + desniRub,
				(int) (zbirnicy + 2 * odskokRetka), glavni);

		// iznos doplate
		this.ispisiNovac(g, iznosSudjelovanja, racX + desniRub,
				(int) (zbirnicy + 3 * odskokRetka), glavni);

		// iznos na teret osn. osiguranja
		this.ispisiNovac(g, racun.getIznosOsnovnogOsiguranja().intValue(), racX
				+ desniRub, (int) (zbirnicy + 4 * odskokRetka), glavni);

		// iznos PDV-a
		this.ispisiNovac(g, sumaPoreza, racX + desniRub,
				(int) (zbirnicy + 5 * odskokRetka), glavni);

		Calendar c = racun.getDatumNarudzbe();

		String datum = "" + c.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c.get(Calendar.MONTH)];
		int god = c.get(Calendar.YEAR) - 2000;
		g.setFont(mali);
		g.drawString(
				datum,
				(int) (racX + (80 + 10 * mm) * omjerSir + 0.5f),
				(int) (3 * omjerDuz + zbirnicy - 2 * mm + 3 * odskokRetka + 0.5f));
		g.drawString("" + (god < 10 ? "0" + god : "" + god),
				(int) (racX + (145 + 10 * mm) * omjerSir), (int) (3 * omjerDuz
						+ zbirnicy - 2 * mm + 3 * odskokRetka + 0.5f));

		Calendar c2 = racun.getDatumIzdavanja();

		datum = "" + c2.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c2.get(Calendar.MONTH)];
		god = c2.get(Calendar.YEAR) - 2000;

		g.drawString(p.getMjestoRada(), (int) (racX + (130 + 10 * mm)
				* omjerSir + 0.5f), (int) (0 * omjerDuz + zbirnicy + 6
				* odskokRetka + 0.5f));
		g.drawString(datum, (int) (racX + (210 + 8 * mm) * omjerSir + 0.5f),
				(int) (0 * omjerDuz + zbirnicy + 6 * odskokRetka + 0.5f));
		g.drawString("" + (god < 10 ? "0" + god : "" + god), (int) (racX
				+ (262 + 8 * mm) * omjerSir + 0.5f), (int) (0 * omjerDuz
				+ zbirnicy + 6 * odskokRetka + 0.5f));

		DjelatnikVO dvo = GlavniFrame.getDjelatnik();
		String odgOsoba = dvo != null ? dvo.getIme() + " " + dvo.getPrezime()
				: "?!?";
		g.drawString(odgOsoba, (int) (420 * omjerSir + 0.5f), (int) (3
				* omjerDuz + zbirnicy + 3 * mm + 7 * odskokRetka + 0.5f));

		return PAGE_EXISTS;
	}// print

	private PomagaloVO nadjiPomagalo(String sifra) {
		PomagaloVO pom = null;

		try {
			if (sifra != null && !sifra.equals(""))
				pom = (PomagaloVO) DAOFactory.getInstance().getPomagala()
						.read(sifra);
			else
				pom = null;
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod Ispisa raèuna osn. osiguranje pri citanju odredjenog pomagala.. ",
					e);
		}

		return pom;
	}// nadjiPomagalo

	private PoreznaStopaVO nadjiPoreznuSkupinu(Integer sifra) {
		PoreznaStopaVO ps = null;

		try {
			if (sifra != null)
				ps = (PoreznaStopaVO) DAOFactory.getInstance()
						.getPorezneStope().read(sifra);
			else
				ps = null;
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod Ispisa raèuna osn. osiguranje pri citanju porezne stope.. ",
					e);
		}

		return ps;
	}// nadjiPoreznuSkupinu

	private void ispisiKolicinu(Graphics g, int kolicina, int x, int y, Font f) {
		int broj = kolicina;
		String str = "" + kolicina;
		double sir = g.getFontMetrics(f).stringWidth(str);
		g.setFont(f);
		g.drawString(str, (int) (x - sir), y);
	}

	// koordinata x predstavlja adresu zadnjeg znaka... dakle desna strana.
	// Iznos je u lipama
	private void ispisiNovac(Graphics g, int iznos, int x, int y, Font f) {
		int broj = iznos / 100, ostatak = iznos % 100;
		String str = "" + broj + ","
				+ (ostatak < 10 ? "0" + ostatak : "" + ostatak);
		double sir = g.getFontMetrics(f).stringWidth(str);
		g.setFont(f);
		g.drawString(str, (int) (x - sir), y);
	}// ispisiNovac

}
