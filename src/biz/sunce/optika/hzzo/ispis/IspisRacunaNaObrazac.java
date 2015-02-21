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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.print.PrintService;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.RacuniUtil;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.02.27
 * 
 * @author asabo
 * 
 */
public final class IspisRacunaNaObrazac implements Printable {
	
	protected PageFormat pFormat;
	protected PrinterJob pJob;

	 
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
	Font italic = new Font("Times New Roman", Font.ITALIC, 9);
	
	int pocetakStraniceOdskokX = 0;
	int pocetakStraniceOdskokY = 0;// 15;

	float omjerSir = 1.0f;
	float omjerDuz = 1.0f;

	RacunVO racun = null;
	
	private final boolean ispisUDefaultPrinter = PostavkeBean
			.isIspisUGlavniPrinter();

	public IspisRacunaNaObrazac(RacunVO racun) {
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
		
		String printer = PostavkeBean.getPostavkaDB(GlavniFrame.ODABRANI_PRINTER, "");
		
		PrintService servis = null;
		
		if (!"".equals(printer))
		{
			PrintService[] servisi = PrinterJob.lookupPrintServices();
			
			for (PrintService ps: servisi)
			{
			if (printer.equals(ps.getName()))
			 { 
				servis = ps;
				try 
				{
					pJob.setPrintService(servis);
				} 
				catch (PrinterException e) 
				{
					Logger.warn("Nismo uspjeli postaviti printer za ispis: "+printer, e);
				}
				break;
			 }
			}
		}
		
		boolean rezultat = ispisUDefaultPrinter ? true: pJob.printDialog();
		
		if ( rezultat ) {
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
			pJob.setJobName("Hzzo ispis na obrazac");

			try {
				pJob.print();
				
				PrintService printService = pJob.getPrintService();
				String printerSvcName = printService==null?null:printService.getName();
				 
				if (printer!=null && printerSvcName!=null && !printer.equals(printerSvcName))
				   PostavkeBean.setPostavkaDB(GlavniFrame.ODABRANI_PRINTER, printerSvcName);

				
			} catch (PrinterException printerException) {

				Logger.fatal("Iznimka prilikom ispisa hzzo obrasca racuna: ",
						printerException);
			} 
			catch (Error greska) 
			{
				Logger.fatal(
						"Greska sustava prilikom ispisa hzzo obrasca racuna: ",
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
		
		// x ide desno, y dolje

		g.setFont(mono);

		String sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();

		//HZZO sifra isporucitelja
		int sifIspLen = sifIsporucitelja.length();
		for (int i = 0; i < sifIspLen; i++)
			g.drawString(sifIsporucitelja.charAt(i) + "", 
					(int) (racX + 98*omjerSir + i * 14.0d * omjerSir + 97 * mm * omjerSir),
					(int) (racY + ( - (16.0d * mm)) * omjerDuz + 0.5f)); 
 
		// OIB - maticni broj isporucitelja
		String mbIsporucitelja = p.getTvrtkaOIB();
		int mbIspLen = mbIsporucitelja.length();
		
		for (int i = 0; i < mbIspLen; i++)
			g.drawString(mbIsporucitelja.charAt(i) + "",
					(int) (racX + (365 ) * omjerSir + i * 14.0d * omjerSir + 0.5f), 
					(int) (racY + ( - (10 * mm)) * omjerDuz + 0.5f));
		
		//broj maloprodajnog racuna
		g.setFont(glavni);
		g.drawString(racun.getBrojOsobnogRacunaOsnovno(),
				(int) (racX + (375 ) * omjerSir + 0 * 14.0d * omjerSir + 0.5f), 
				(int) (racY + (79 - 30 * mm) * omjerDuz + 0.5f));

		g.setFont(glavni);
		
		// naziv tvrtke
		String nazivOptike = p.getTvrtkaNaziv();
		g.drawString(nazivOptike,
				(int) (racX + (50 - 5 * mm) * omjerSir + 0.5f), 
				(int) (racY + ((40.0d -2.0d * mm) + 60-66*mm) * omjerDuz + 0.5f));
		// adresa tvrtke
		String adresaOptike = p.getMjestoRada() + ", " + p.getTvrtkaAdresa();
		g.drawString(adresaOptike, 
				(int) (racX + (117+79*mm) * omjerSir + 0.5f),
				(int) (racY + ((40.0d -2.0d * mm) + 60-66*mm) * omjerDuz + 0.5f));

		// tvrtka racun
		g.drawString(p.getTvrtkaRacun(),
				(int) (racX + 75 * omjerSir + 0.5f),
				(int) (racY + (79 + (33*mm - 70*mm)) * omjerDuz + 0.5f));

		//poziv na broj
		String poziv1 = racun.getPozivNaBroj1(), poziv2 = racun.getPozivNaBroj2();
		if (poziv1 == null) poziv1 = "";
		if (poziv2 == null)	poziv2 = "";

		g.drawString(poziv1, 
				(int) (racX + 26 * mm * omjerSir + 0.5f),
				(int) (racY + (79 - 30 * mm) * omjerDuz + 0.5f));
		g.drawString(poziv2, 
				(int) (racX + 44 * mm * omjerSir + 0.5f),
				(int) (racY + (79 - 30 * mm) * omjerDuz + 0.5f));

		 
		ArrayList<StavkaRacunaVO> stavke = racun.getStavkeRacuna();

		int startcy = (int) (racY + (146 - 34.0d * mm) * omjerSir + 0.5f);
		
		double odskokRetka = 19.6d * omjerDuz;
		
		int suma = 0;
		int sumaPorezneOsnove = 0;
		int sumaPoreza = 0;
		PoreznaStopaVO stopa = null;
		int sirinaPoljaNazivPomagala = (int) (61 * mm * omjerSir + 0.5f);

		int stavkeSize = stavke.size();
		for (int sf = 0; sf < stavkeSize; sf++) 
		{
			StavkaRacunaVO st = (StavkaRacunaVO) stavke.get(sf);
			PomagaloVO pom = nadjiPomagalo(st.getSifArtikla());
			stopa = nadjiPoreznuSkupinu(st.getPoreznaStopa()); // 12.03.06.
																// -asabo-
																// dodano
			int cy = (int) (startcy + sf * odskokRetka);
			int lrb = (int) (16.0d * mm); // lijevi rub
			
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
				g.drawString(naziv, (int) (racX + (lrb+46*mm) * omjerSir + 0.5f), cy); // cy
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
				g.drawString(naziv.substring(0, mj), (int) (racX + (lrb+46*mm)
						* omjerSir + 0.5f), cy+2 - mali.getSize());

				int prvoMjesto = mj;
				dozSir = 0; // reset
				// drugi redak while
				while (mj < naziv.length()) {
					dozSir += fm.charWidth(naziv.charAt(mj));
					mj++;
					if (dozSir >= sirinaPoljaNazivPomagala)
						break;
				}// drugi while
				g.drawString(naziv.substring(prvoMjesto, mj), (int) (racX + (lrb+46.0d*mm)
						* omjerSir + 0.5d), cy+2);
			}// else
 
			int totalBezPDVa = RacuniUtil.getNettoIznosStavke(st);
			int total = RacuniUtil.getBruttoIznosStavke(st);

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
		
		int lrb = (int) (16.0d * mm);
		// ukupno prvi dio gore odskokRetka je vec pomnozen sa duzinskim
		// faktorom
		this.ispisiNovac(g, suma, 
				(int) (racX + (lrb + 166 * mm) * omjerSir + 0.5f),
				(int) (startcy + 3 * odskokRetka + 9*mm), glavni);

		
		int desniRub = (int) ((lrb + 165 * mm) * omjerSir + 0.5f);

		// u slucaju osnovnog osiguranja to je iznos koji placa stranka
		int iznosSudjelovanja = racun.getIznosSudjelovanja() != null ? racun
				.getIznosSudjelovanja().intValue() : 0;

		// ukupan iznos se sastoji od nekoliko proizvoda po nekoliko poreznih
		// skupina
		// i tocan iznos placenog poreza dijeli se u jednakom omjeru na stranku i
		// osiguranje
		// trenutno je to jedini nacin na koji znamo da se porez raspodjeljuje
		 
		float omjerZdr = (float) (suma - iznosSudjelovanja) / suma;
		sumaPoreza = (int) ((float) sumaPoreza * omjerZdr + 0.5f);
		
		
		//---------------------- totali --------------------------------
		int zbirnicy = (int) (-2 * omjerDuz + startcy + 5 * odskokRetka - 1 * mm);
		
		odskokRetka = 5.70d * mm;
		desniRub -= 5.0d*mm;

	 	// iznos za pomagala je isti kao i cijeli iznos
		this.ispisiNovac(g, suma, racX + desniRub,
				(int) (zbirnicy + 1 * odskokRetka-1*mm), glavni);

	 	// iznos sudjelovanja
		this.ispisiNovac(g, racun.getIznosSudjelovanja().intValue(), 
				(int) racX + desniRub,
				(int) (zbirnicy + 2 * odskokRetka-1*mm), glavni);
		
		// iznos na teret osn. osiguranja
		this.ispisiNovac(g, racun.getIznosOsnovnogOsiguranja().intValue(), racX
				+ desniRub, (int) (zbirnicy + 3 * odskokRetka), glavni);

		// iznos PDV-a
		this.ispisiNovac(g, sumaPoreza, racX + desniRub,
				(int) (zbirnicy + 4 * odskokRetka), glavni);

		Calendar c = racun.getDatumNarudzbe();

		//datum narudzbe 
		String datumNarudzbe = c.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c.get(Calendar.MONTH)];
		int godNar = c.get(Calendar.YEAR) - 2000;
		g.setFont(mali);
		g.drawString(
				datumNarudzbe,
				(int) (racX + (80 + 0 * mm) * omjerSir + 0.5f),
				(int) ( zbirnicy + 5 * odskokRetka + 0.5f + 2*mm));
		g.drawString("" + (godNar < 10 ? "0" + godNar : "" + godNar),
				(int) (racX + (145 - 2 * mm) * omjerSir), 
				(int) ( zbirnicy + 5 * odskokRetka + 0.5f + 2*mm));

		//------------------------mjesto i dat. izdavanja --------------
		Calendar c2 = racun.getDatumIzdavanja();

		String datumIzd = "" + c2.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c2.get(Calendar.MONTH)];
		int godIzd = c2.get(Calendar.YEAR) - 2000;

		//mjesto
		g.drawString(p.getMjestoRada(), 
				(int) (racX + (100 + 10 * mm) * omjerSir + 0.5f),
				(int) ( zbirnicy + 7 * odskokRetka + 1*mm +0.5f));
		
		g.drawString(datumIzd, 
				(int) (racX + (210 -4 * mm) * omjerSir + 0.5f),
				(int) ( zbirnicy + 7 * odskokRetka + 1*mm + 0.5f));
		g.drawString("" + (godIzd < 10 ? "0" + godIzd : "" + godIzd),
				(int) (racX + (262 -4 * mm) * omjerSir + 0.5f), 
				(int) ( zbirnicy + 7 * odskokRetka + 1*mm +0.5f));

		
		DjelatnikVO dvo = GlavniFrame.getDjelatnik();
		String odgOsoba = dvo != null ? dvo.getIme() + " " + dvo.getPrezime()
				: " ";
		g.setFont(italic);
		g.drawString(odgOsoba, 
				(int) (400 * omjerSir + 0.5f), 
				(int) ( zbirnicy + 9 * odskokRetka + 0.5f));

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
