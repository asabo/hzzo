/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo.ispis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.print.PrintService;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.RacuniUtil;
import biz.sunce.util.StringUtils;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.02.27
 * 
 * @author asabo
 * 
 */
public final class IspisRacunaOsnovnoOsiguranje implements Printable {
	
	private static final String FOOTER_FIRMA = "www.sunce.biz/hzzo";
	protected PageFormat pFormat;
	
 
	public static double A4_SIRINA_PX = 595.2756d;
	public static double A4_VISINA_PX = 841.8898d;

	PostavkeBean p = new PostavkeBean();

	int zaglX = 0, zaglY = 0;
	int racX = 0, racY = 0;
	int napX = 0, napY = 0;
	Font glavni = new Font("Arial", Font.PLAIN, 12);
	Font mono = new Font("Courier New", Font.BOLD, 16);
	Font mali = new Font("Times New Roman", Font.PLAIN, 9);
	Font nazivPomagalaMaliFont = new Font("Arial", Font.PLAIN, 7);

	Font centuryGothicVeliki = new Font("Century Gothic", Font.PLAIN, 12);
	Font centuryGothicVelikiBold = new Font("Century Gothic", Font.BOLD, 12);
	Font centuryGothicSrednji = new Font("Century Gothic", Font.PLAIN, 9);
	Font centuryGothicSrednjiBold = new Font("Century Gothic", Font.BOLD, 9);
	Font centuryGothicMali = new Font("Century Gothic", Font.TRUETYPE_FONT, 6);
	Font centuryGothicExtraMali = new Font("Century Gothic",
			Font.TRUETYPE_FONT, 4);
	Font centuryGothicMaliItalic = new Font("Century Gothic", Font.ITALIC, 6);

	float dash[] = { 2, 1 };
	BasicStroke crtice = new BasicStroke(0.7f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);

	int pocetakStraniceOdskokX = 0;
	int pocetakStraniceOdskokY = 0;// 15;

	float omjerSir = 1.0f;
	float omjerDuz = 1.0f;
	int ddY = 0; // donji dio Y, iznacava horizontalnu koordinatu pocetka donjeg
					// dijela racuna

	RacunVO racun = null;

	public IspisRacunaOsnovnoOsiguranje(RacunVO racun) {
		this.racun = racun;
		pFormat = new PageFormat();
		
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

		napX = (int) (zaglX * omjerSir + 0.5f);
		napY = (int) (zaglY + 500 * omjerDuz + 0.5f);

	}// konstruktor

	public void printDialog() {
		printaj(true);
	}

	static int brojKopija = 1;

	public final void printaj(boolean prikaziDialog){
		PrinterJob pJob = PrinterJob.getPrinterJob();
	
		printaj(prikaziDialog,pJob);
	}
	
	public final void printaj(boolean prikaziDialog,PrinterJob pJob) {
		// stavi koliko je zadnji puta bilo kopija...
		
		pJob.setCopies(brojKopija);
		
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
		
		//otvori print job ako treba	
		boolean printanjesaDialogom = prikaziDialog && pJob.printDialog();

		if (printanjesaDialogom || (!prikaziDialog)) {
			PageFormat pf = this.pFormat;

			zaglX = (int) ((pf.getImageableX() + 10) * omjerSir); // 10 tockica
																	// je cca 3
																	// mm
			zaglY = (int) (pf.getImageableY() * omjerDuz);

			zaglX += pocetakStraniceOdskokX;
			zaglY += pocetakStraniceOdskokY;

			racX = zaglX;
			racY = (int) (zaglY + 210 * omjerSir + 0.5f);
			napX = zaglX;
			napY = (int) (zaglY + 675 * omjerDuz + 0.5f);

			pJob.setPrintable(this, pf);
			String naslov = "hzzo_racun_osn_osig_br_"
					+ (this.racun != null ? this.racun
							.getBrojOsobnogRacunaOsnovno() : "nepoznat_broj") ;
			pJob.setJobName(naslov);

			try {
				
				pJob.print();
				 
				PrintService printService = pJob.getPrintService();
				String printerSvcName = printService==null?null:printService.getName();
				 
				if (printer!=null && printerSvcName!=null && !printer.equals(printerSvcName))
				   PostavkeBean.setPostavkaDB(GlavniFrame.ODABRANI_PRINTER, printerSvcName);
				
				brojKopija = pJob.getCopies(); // zapamti koliko je bilo kopija
												// za iduci put
			} 
			catch (PrinterException printerException) 
			{
				Logger.fatal("Iznimka prilikom ispisa hzzo raèuna: ",
						printerException);
			} 
			catch (Error greska) 
			{
				Logger.fatal("Greška sustava prilikom ispisa hzzo raèuna: ",
						greska);
			}

		}
	}// printDialog

	public void finalize() {
		
		this.mali = null;
		this.mono = null;
		this.pFormat = null;
		this.racun = null;
		this.p = null;
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		if (pageIndex > 0)
			return NO_SUCH_PAGE;
		int desnaKolona = racX + 350 - 23;

		try
		{
		
		iscrtajObrazac(g);

		g.setFont(glavni);
		g.setColor(Color.black);

		MjestoVO podrUred = racun.getPodrucniUred();

		if (podrUred == null) {
			try {
				podrUred = (MjestoVO) DAOFactory.getInstance().getMjesta()
						.read(racun.getSifPodrucnogUreda());
			} catch (SQLException e) {
				Logger.log(
						"Iznimka kod trazenja mjesta podrucnog ureda za racun (ispisDop) osn: "
								+ racun.getBrojOsobnogRacunaOsnovno(), e);
			}
		}// if podrucni ured je null

		String pnaziv = podrUred != null ? podrUred.getNaziv() : "";

		g.drawString(pnaziv, (int) (zaglX + 75 * omjerSir),
				(int) (zaglY + 64 * omjerDuz));

		g.drawString(pnaziv, (int) (zaglX + 50 * omjerSir),
				(int) (zaglY + 83 * omjerDuz));
		String ulica = "";

		if (podrUred != null) {
			ulica = PostavkeBean.getPostavkaDB("hzzo_adr_"
					+ podrUred.getSifra().intValue(), "");
		}

		g.drawString(ulica, (int) (zaglX + 62 * omjerSir),
				(int) (zaglY + 115 * omjerDuz));

		// String tipRacuna=PostavkeBean.getTipRacuna();

		String brojOsobnogRacuna = racun.getBrojOsobnogRacunaOsnovno() != null ? racun
				.getBrojOsobnogRacunaOsnovno() : ""; // OK
		g.drawString(brojOsobnogRacuna, (int) (racX + 380 * omjerSir),
				(int) (ddY + 136 * omjerDuz));

		g.setFont(mono);
		g.setColor(Color.black);

		// HZZO sifra isporucitelja
		String sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();
		
		int sifIspLen = sifIsporucitelja.length();
		for (int i = 0; i < sifIspLen; i++)
			g.drawString(sifIsporucitelja.charAt(i) + "", (int) (racX + 99
					* omjerSir + i * 13.5d * omjerSir),
					(int) (ddY + 37 * omjerDuz)); // OK

		String brojPotvrde = racun.getBrojPotvrde1(), brojRacuna = racun
				.getBrojPotvrde2();

		// da ne pametujemo previse, moze biti 1,2 ili 3 znaka
		int brojPotvLen = brojPotvrde.length();
		if (brojPotvLen == 2)
			brojPotvrde = " " + brojPotvrde; // OK
		else if (brojPotvLen == 1)
			brojPotvrde = "  " + brojPotvrde;
		
		brojPotvLen=brojPotvrde.length();

		for (int i = 0; i < brojPotvLen; i++)
			g.drawString(brojPotvrde.charAt(i) + "", (int) (racX + 340
					* omjerSir + i * 11.9d * omjerSir + 0.5f), (int) (ddY + 40
					* omjerDuz + 0.5f)); // OK

		int brRacLen = brojRacuna.length();
		for (int i = 0; i < brRacLen; i++)
			g.drawString(brojRacuna.charAt(i) + "", (int) (desnaKolona + 57
					* omjerSir + i * 12.0d * omjerSir + 0.5f), (int) (ddY + 40
					* omjerDuz + 0.5f)); // OK

		// pravi maticni firme
		String oibIsporucitelja = p.getTvrtkaOIB(); // OK

		int oibILen = oibIsporucitelja!=null?oibIsporucitelja.length():-1;
		int sfpi = GlavniFrame.getSifraPoslovniceZaSynch();
		if (sfpi>0){
			g.setFont(centuryGothicMaliItalic);
			g.drawString("Poslovnica br.: "+sfpi, (int) (racX + 320
					* omjerSir + 0 * 13.5d * omjerSir + 0.5f), (int) (ddY
					+ 150 * omjerDuz + 0.5f));
			g.setFont(mono);
		}
		
		
		if (oibIsporucitelja != null && oibILen <= 8) {
			for (int i = 0; i < oibILen; i++)
				g.drawString(oibIsporucitelja.charAt(i) + "", (int) (racX + 367
						* omjerSir + i * 13.5d * omjerSir + 0.5f), (int) (ddY
						+ 177 * omjerDuz + 0.5f));
		} else // ako je mb veci od 9 znakova samo ga ispisujemo
		if (oibIsporucitelja != null && oibILen > 8) {
			g.setFont(glavni);
			g.drawString(oibIsporucitelja, (int) (racX + 367 * omjerSir + 0.5f),
					(int) (ddY + 177 * omjerDuz + 0.5f));
		}

		/*
		 * String proizvodjac=racun.getSifProizvodjaca(); if (proizvodjac==null)
		 * proizvodjac=""; //ako je sifra proizvodjaca manja od 9 znakova
		 * trebamo 'desno' poravnati broj int razl=9-proizvodjac.length(); if
		 * (razl<0) razl=0; for (int i=0; i<proizvodjac.length(); i++)
		 * g.drawString
		 * (proizvodjac.charAt(i)+"",(int)(racX+97*omjerSir+(i+razl)*
		 * 13.5d*omjerSir+0.5f),(int)(ddY+37*omjerDuz+0.5f));
		 */

		g.setFont(glavni);
		String nazivOptike = p.getTvrtkaNaziv();
		g.drawString(nazivOptike, (int) (racX + 45 * omjerSir + 0.5f),
				(int) (ddY + 61 * omjerDuz + 0.5f));
		String adresaOptike = p.getMjestoRada() + ", " + p.getTvrtkaAdresa();
		g.drawString(adresaOptike, (int) (racX + 117 * omjerSir + 0.5f),
				(int) (ddY + 100 * omjerDuz + 0.5f));

		// broj ziro-racuna
		g.drawString(p.getTvrtkaRacun(), (int) (racX + 75 * omjerSir + 0.5f),
				(int) (ddY + 136 * omjerDuz + 0.5f));

		String poziv1 = racun.getPozivNaBroj1(), poziv2 = racun
				.getPozivNaBroj2();
		if (poziv1 == null)
			poziv1 = "";
		if (poziv2 == null)
			poziv2 = "";

		g.drawString(poziv1, (int) (this.zaglX + 66 * omjerSir + 0.5f),
				(int) (ddY + 176 * omjerDuz + 0.5f)); // OK
		g.drawString(poziv2, (int) (zaglX + 83 * omjerSir + 0.5f), (int) (ddY
				+ 176 * omjerDuz + 0.5f));

		 List<StavkaRacunaVO> stavke = racun.getStavkeRacuna();

		// ako se ne nalaze u samom racunu, treba ih 'rucno' pokupiti iz baze
		if (stavke == null) {
			try {
				stavke = DAOFactory.getInstance().getStavkeRacuna().findAll(racun);
			} 
			catch (SQLException ex) 
			{
				Logger.log(
						"SQL iznimka kod trazenja stavki racuna za racun br.oso.osn.:"
								+ (racun != null ? racun
										.getBrojOsobnogRacunaOsnovno() : "?!?"),
						ex);
			}
		}// if stavke == null

		int startcy = (int) (ddY + 226.5f * omjerSir + 0.5f);
		double odskokRetka = 21.1d * omjerDuz;
		int suma = 0;
		int sumaPorezneOsnove = 0;
		int sumaPoreza = 0;
		PoreznaStopaVO stopa = null;
		int sirinaPoljaNazivPomagala = (int) (96 * omjerSir + 0.5f);
		String sfps;

		int stavkeSize = stavke.size();
		StavkaRacunaVO st;
		PomagaloVO pom;
		
		for (int sf = 0; sf < stavkeSize; sf++) 
		{
			st = (StavkaRacunaVO) stavke.get(sf);
			pom = RacuniUtil.nadjiPomagalo(st.getSifArtikla());
			stopa = RacuniUtil.nadjiPoreznuSkupinu(st.getPoreznaStopa()); 

			// zbog starih racuna moramo upisivati prazan string ako nema sifre
			// proizvodjaca, umjesto da blokiramo proces..
			sfps = (st.getSifProizvodjaca() != null ? ""
					+ st.getSifProizvodjaca().intValue() : "");

			String sifraArtikla = st.getSifArtikla();

			boolean iso9999 = sifraArtikla != null
					&& (sifraArtikla.length() == 12 || sifraArtikla.length() == 13)
					&& StringUtils.imaSamoBrojeve(sifraArtikla);
			String sif = st.getSifArtikla();

			int cy = (int) (startcy + sf * odskokRetka);
			if (!iso9999) {
				g.setFont(mono);
				// sifra pomagala
				int sfl = sif.length();
				for (int i = 0; i < sfl; i++)
					g.drawString(sif.charAt(i) + "", (int) (racX + 135
							* omjerSir + i * 13.5d * omjerSir + 0.5f), cy);
			} else{
				g.setColor(Color.white);
				g.fillRect((int) (racX + 140 * omjerSir + 0.5f), cy-11, (int)(omjerSir*6*13.4f+0.5f), 11);
				g.setColor(Color.black);
				
				g.drawString(sif, (int) (racX + 134 * omjerSir + 0.5f), cy);
			}
			// sifra proizvodjaca
			if (!iso9999 && sfps != null && sfps.trim().length() > 0) {
				sfps = sfps.trim(); // ocistiti za svaki slucaj od visaka sa
									// strane...
				int sfpsLen = sfps.length();
				int razl = 7 - sfpsLen;
				for (int i = 0; i < razl; i++)
					sfps = " " + sfps;
				
				sfpsLen = sfps.length();

				for (int i = 0; i < sfpsLen; i++)
					g.drawString(sfps.charAt(i) + "", (int) (racX + 237
							* omjerSir + i * 13.5d * omjerSir + 0.5f), cy);
			}

			g.setFont(nazivPomagalaMaliFont);
			String naziv = (pom != null ? pom.getNaziv() : "?!?");

			FontMetrics fm = g.getFontMetrics(nazivPomagalaMaliFont);
			int sir = (int) fm.stringWidth(naziv);
			int lijeviRubNazivPomagala = (int) (racX + 36.0f * omjerSir + 0.5f);

			// ako tekst stane unutar predvidjenog prostora trpamo u jedan redak
			if (sir <= sirinaPoljaNazivPomagala) {
				g.drawString(naziv, lijeviRubNazivPomagala, cy); // cy je vec
																	// podesen
																	// sa
																	// faktorom
																	// duzine
			} 
			else 
			{
				int mj = 0;
				int dozSir = 0;

				int nazivLen = naziv.length();
				while (mj < nazivLen) 
				{
					dozSir += (int) fm.charWidth(naziv.charAt(mj));
					mj++;
					if (dozSir >= sirinaPoljaNazivPomagala)
						break;
				}// prvi while
				g.drawString(naziv.substring(0, mj), lijeviRubNazivPomagala, cy
						- nazivPomagalaMaliFont.getSize());

				int prvoMjesto = mj;
				dozSir = 0; // reset
				// drugi redak while
				while (mj < nazivLen) {
					dozSir += (int) fm.charWidth(naziv.charAt(mj));
					mj++;
					if (dozSir >= sirinaPoljaNazivPomagala)
						break;
				}// drugi while
				g.drawString(naziv.substring(prvoMjesto, mj),
						lijeviRubNazivPomagala, cy);
			}// else

			int totalBezPDVa = RacuniUtil.getNettoIznosStavke(st);
			int total = RacuniUtil.getBruttoIznosStavke(st);

			int poreznaOsnova = totalBezPDVa;
			int porez = total - totalBezPDVa;

			ispisiKolicinu(g, st.getKolicina().intValue(), (int) (racX + 360
					* omjerSir + 0.5f), cy, glavni); // 345 je bilo

			ispisiNovac(g, st.getPoCijeni().intValue(), (int) (racX + 417
					* omjerSir + 0.5f), cy, glavni);
			ispisiNovac(g, total, (int) (racX + 498 * omjerSir + 0.5f), cy,
					glavni);
		
			suma += total;
			sumaPorezneOsnove += poreznaOsnova;
			sumaPoreza += porez;
		}// for sf

		// ukupno prvi dio gore odskokRetka je vec pomnozen sa duzinskim
		// faktorom
		this.ispisiNovac(g, suma, (int) (racX + 498 * omjerSir + 0.5f),
				(int) (startcy + 7 * odskokRetka), glavni);
		int desniRub = (int) (480 * omjerSir + 0.5f);

		// u slucaju osnovnog osiguranja to je iznos koji placa stranka
		int iznosSudjelovanja = racun.getIznosSudjelovanja() != null ? racun
				.getIznosSudjelovanja().intValue() : 0;

		boolean nemaPoreza = (sumaPoreza == 0);

		// ukupan iznos se sastoji od nekoliko proizvoda po nekoliko poreznih
		// skupina
		// i tocan iznos placenog poreza djeli se u jednakom omjeru na stranku i
		// osiguranje
		// trenutno je to jedini nacin na koji znamo da se porez raspodjeljuje
		float omjerZdr = (float) (suma - iznosSudjelovanja) / suma;
		sumaPoreza = (int) ((float) sumaPoreza * omjerZdr + 0.5f);

		// ako je racun ipak za dopunsko osiguranje, znaci da je iznos na teret
		// stranke 0
		// ali prije ponistavanja smo izracunali sumu poreza koja ipak ovisi o
		// iznosu sudjelovanja
		if ( !racun.getOsnovnoOsiguranje().booleanValue() )
			iznosSudjelovanja = 0;

		startcy = (int) (startcy + 9 * odskokRetka - 3);
		odskokRetka = 17;
		// ukupni iznos s pdv-om
		this.ispisiNovac(g, suma, racX + desniRub,
				(int) (startcy + 0 * odskokRetka), glavni);

		// iznos za pomagala
		this.ispisiNovac(g, suma, racX + desniRub,
				(int) (startcy + 1 * odskokRetka), glavni);

		// iznos za postupke
		this.ispisiNovac(g, 0, racX + desniRub,
				(int) (startcy + 2 * odskokRetka), glavni);

		// iznos doplate
		this.ispisiNovac(g, iznosSudjelovanja, racX + desniRub,
				(int) (startcy + 3 * odskokRetka), glavni);

		// iznos na teret obveznog osiguranja
		this.ispisiNovac(g, racun.getIznosOsnovnogOsiguranja().intValue(), racX
				+ desniRub, (int) (startcy + 4 * odskokRetka), glavni);

		// iznos PDV- u tocki 5.
		this.ispisiNovac(g, sumaPoreza, racX + desniRub,
				(int) (startcy + 5 * odskokRetka), glavni);

		g.setFont(this.mali);
		if (nemaPoreza)
			g.drawString(
					"Osloboðeno plaæanja PDV-a po èl. 10A Zakona o PDV-u.",
					desniRub - 160, (int) (startcy + 7 * odskokRetka));
		g.setFont(this.glavni);

		Calendar c = racun.getDatumNarudzbe();

		String datum = "" + c.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c.get(Calendar.MONTH)];
		int god = c.get(Calendar.YEAR) - 2000;
		g.setFont(mali);
		g.drawString(datum, (int) (racX + 70 * omjerSir + 0.5f), (int) (1
				* omjerDuz + startcy + 4 * odskokRetka + 8.5f));
		g.drawString("" + (god < 10 ? "0" + god : "" + god), racX + 135,
				(int) (1 * omjerDuz + startcy + 4 * odskokRetka + 8.5f));

		Calendar c2 = racun.getDatumIzdavanja();

		datum = "" + c2.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c2.get(Calendar.MONTH)];
		god = c2.get(Calendar.YEAR) - 2000;

		g.drawString(p.getMjestoRada(), (int) (racX + 55 * omjerSir + 0.5f),
				(int) (5 * omjerDuz + startcy + 6 * odskokRetka + 9.5f));
		g.drawString(datum, (int) (racX + 130 * omjerSir + 0.5f), (int) (5
				* omjerDuz + startcy + 6 * odskokRetka + 9.5f));
		g.drawString("" + (god < 10 ? "0" + god : "" + god), (int) (racX + 190
				* omjerSir + 0.5f), (int) (5 * omjerDuz + startcy + 6
				* odskokRetka + 9.5f));

		DjelatnikVO dvo = GlavniFrame.getDjelatnik();
		String odgOsoba = dvo != null ? dvo.getIme() + " " + dvo.getPrezime()
				: "?!?";
		g.drawString(odgOsoba, (int) (435 * omjerSir + 0.5f), (int) (5
				* omjerDuz + startcy + 8 * odskokRetka + 11.5f));

		// String napomena=racun.getNapomena()!=null?racun.getNapomena():"";
		// g.drawString(napomena,napX,napY);

		}
		catch(Exception e){
			Logger.log("Iznimka kod ispisa dokumenta", e);
			if (e instanceof PrinterException){
				PrinterException pe = (PrinterException)e;
				Logger.log("Problem s printerom: "+pe, pe);
			throw pe;	
			}
		 }
		return PAGE_EXISTS;
	}// print

	private void ispisiKolicinu(Graphics g, int kolicina, int x, int y, Font f) 
	{		 
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

	// iscrtava na 'papiru' obrazac za osnovno zdr. osiguranje
	private void iscrtajObrazac(Graphics g) {
		// ********************************** ZAGLAVLJE RACUNA
		// *************************
		Graphics2D g2 = (Graphics2D) g;
		int x = (int) this.zaglX;
		int y = (int) this.zaglY;
		int lijeviRub = x + 23;
		int sirina = 510;

		g2.setColor(Color.black);

		g2.setStroke(crtice);
		// g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_MITER,tocke,1.0f));
		g2.drawRect(x, y, sirina, 146);

		g2.setFont(this.centuryGothicSrednjiBold);
		g2.drawString("HRVATSKI ZAVOD ZA ZDRAVSTVENO OSIGURANJE", lijeviRub,
				y + 27);
		g2.setFont(this.centuryGothicMali);

		g2.drawString(
				"Podruèni ured: . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ",
				lijeviRub, y + 67);
		g2.drawString(
				"Mjesto:  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ",
				lijeviRub, y + 86);
		g2.drawString(
				"Ulica i broj:  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ",
				lijeviRub, y + 118);

		g2.setFont(this.centuryGothicVelikiBold);
		g2.drawString("RAÈUN ZA OBVEZNO", x + 349, y + 56);
		g2.drawString("ZDRAVSTVENO OSIGURANJE", x + 339, y + 71);

		// ********************************************* RACUN
		// ***************************
		g2.drawRect(x, y + 148, sirina, 455 + 83 + 57);
		ddY = y + 148;
		g2.setFont(this.centuryGothicSrednjiBold);
		g2.drawString("I. IZDAVANJE POMAGALA, DIJELA POMAGALA", x + 8, ddY + 10);
		g2.setFont(this.centuryGothicMali);
		g2.drawString("(ispunjava ugovorni isporuèitelj)", x + 210, ddY + 10);

		g2.drawString("Matièni broj HZZO:   0 3 5 8 0 2 6 1 ", x + 380,
				ddY + 10);

		int linijaNaziv = ddY + 64;
		int desnaKolona = racX + 350 - 23;

		g2.setFont(this.centuryGothicMali);
		g2.drawString("Broj potvrde", x + 415, ddY + 25);
		nacrtajKockice(g2, desnaKolona + 13, ddY + 28, 3, 12.0f); // broj
																	// potvrde
																	// 11 komada
																	// dizemo
																	// gore
		// broj racuna
		nacrtajKockice(g2, desnaKolona + 57, ddY + 28, 9, 12.0f); // 11 gore

		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("Ugovorni isporuèitelj: ", lijeviRub, ddY + 28);
		g2.drawString("(šifra) ", x + 60, ddY + 35);
		nacrtajKockice(g2, racX + 97, ddY + 26, 9); // ug. isporucitelj

		g2.drawString("Naziv: ", lijeviRub, linijaNaziv);
		g2.drawLine(x + 45, linijaNaziv, x + 250, linijaNaziv);

		String tipRacuna = PostavkeBean.getTipRacuna();
		g2.setFont(this.centuryGothicVelikiBold);
		g2.drawString(tipRacuna, desnaKolona, linijaNaziv);

		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("Adresa (mjesto, ulica i broj) ", lijeviRub,
				linijaNaziv + 37);
		g2.drawLine(x + 110, linijaNaziv + 37, x + 320, linijaNaziv + 37);

		g2.drawString("Broj žiro-raèuna: ", lijeviRub, linijaNaziv + 73);
		g2.drawLine(x + 74, linijaNaziv + 73, x + 230, linijaNaziv + 73);

		g2.drawString("Broj raèuna", desnaKolona, linijaNaziv + 65);
		g2.drawString("isporuèitelja", desnaKolona, linijaNaziv + 73);
		g2.setColor(Color.GRAY);
		g2.drawLine(racX + 380, linijaNaziv + 73, desnaKolona + 150,
				linijaNaziv + 73);

		g2.setColor(Color.BLACK);
		g2.drawString("Poziv na broj: ", lijeviRub, linijaNaziv + 113);
		g2.setColor(Color.GRAY);
		nacrtajOkvir(g2, lijeviRub + 43, linijaNaziv + 101, 14); // poziv na
																	// broj 1
		nacrtajOkvir(g2, lijeviRub + 60, linijaNaziv + 101, 115); // poziv na
																	// broj 2

		g2.setColor(Color.BLACK);
		g2.drawString("Matièni broj", desnaKolona, linijaNaziv + 105);
		g2.drawString("isporuèitelja:", desnaKolona, linijaNaziv + 112);

		// mb
		String mbIsporucitelja = p.getTvrtkaOIB();
		// mb
		if (mbIsporucitelja != null && mbIsporucitelja.length() <= 8)
			nacrtajKockice(g2, desnaKolona + 40, linijaNaziv + 101, 8); // 11up
		else if (mbIsporucitelja != null && mbIsporucitelja.length() > 8)
			this.nacrtajOkvir(g2, desnaKolona + 40, linijaNaziv + 101, 115);

		// *************** iscrtavanje mreze za stavke racuna ******************
		int visinaRetka = 21, redaka = 8;
		int visina1 = visinaRetka * redaka, visina2 = visina1 + visinaRetka;
		int pocetakY = ddY + 190;

		// kolone
		lijeviRub -= 17; // 6 mm ulijevo
		g2.setColor(Color.BLACK);
		g2.drawLine(lijeviRub, pocetakY, lijeviRub, pocetakY + visina1); // 1 rb
		g2.drawLine(lijeviRub + 28, pocetakY, lijeviRub + 28, pocetakY
				+ visina1); // 2 naziv pomagala
		g2.drawLine(lijeviRub + 124, pocetakY, lijeviRub + 124, pocetakY
				+ visina1);// 3 sifra pomagala
		g2.drawLine(lijeviRub + 226, pocetakY, lijeviRub + 226, pocetakY
				+ visina1);// 4 sifra proizvodjaca

		g2.drawLine(lijeviRub + 325, pocetakY, lijeviRub + 325, pocetakY
				+ visina2);// 5 kolicina
		g2.drawLine(lijeviRub + 363, pocetakY, lijeviRub + 363, pocetakY
				+ visina2);// 6 jed. cj s PDV
		g2.drawLine(lijeviRub + 415, pocetakY, lijeviRub + 415, pocetakY
				+ visina2);// 7 iznos u kn s pdv
		g2.drawLine(lijeviRub + 497, pocetakY, lijeviRub + 497, pocetakY
				+ visina2);// 8 rub

		g2.setFont(this.centuryGothicMali);
		// retci
		for (int i = 0; i < redaka + 1; i++) {
			g2.drawLine(lijeviRub, pocetakY + (visinaRetka) * i,
					lijeviRub + 497, pocetakY + visinaRetka * i);

			if (i > 0 && i < 8) {
				this.nacrtajKockice(g2, lijeviRub + 127, (int) (pocetakY
						+ (visinaRetka) * i + 4), 7); // sifra artikla
				this.nacrtajKockice(g2, lijeviRub + 229, (int) (pocetakY
						+ (visinaRetka) * i + 4), 7); // sifra proizvodjaca
				g2.drawString("" + (i) + ".", lijeviRub + 7, pocetakY
						+ visinaRetka * i + 12);
			} else if (i == 8)
				g2.drawLine(lijeviRub + 325, pocetakY + visinaRetka * (i + 1),
						lijeviRub + 497, pocetakY + visinaRetka * (i + 1));
		}// for i

		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("Redni", lijeviRub + 2, pocetakY + 10);
		g2.drawString("broj", lijeviRub + 4, pocetakY + 15);

		g2.drawString("Naziv pomagala", lijeviRub + 45, pocetakY + 13);

		g2.drawString("Šifra pomagala", lijeviRub + 145, pocetakY + 13);

		g2.drawString("Šifra proizvoðaèa", lijeviRub + 250, pocetakY + 13);

		g2.drawString("kolièina", lijeviRub + 334, pocetakY + 13);

		g2.drawString("Jedinièna cijena", lijeviRub + 364, pocetakY + 10);
		g2.drawString("u kn (s PDV-om)", lijeviRub + 366, pocetakY + 15);

		g2.drawString("Iznos u kn", lijeviRub + 437, pocetakY + 10);
		g2.drawString("(s PDV-om)", lijeviRub + 436, pocetakY + 15);

		g2.drawString("Ukupno", lijeviRub + 331, pocetakY + visinaRetka * 9
				- visinaRetka / 2 + 2);

		int marginaObracun = lijeviRub + 234;
		int crtaStart = marginaObracun + 156;
		int pocetakObrY = (int) (((float) pocetakY + visinaRetka * 11 - 6) * 1.0f);
		visinaRetka = 17; // smanjujemo skok...

		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("1. Ukupan iznos (s PDV-om)", marginaObracun, pocetakObrY
				+ visinaRetka * 0);
		g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 0, crtaStart + 95,
				pocetakObrY + visinaRetka * 0);
		g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 0);

		g2.drawString("2. Iznos za pomagala", marginaObracun, pocetakObrY
				+ visinaRetka * 1);
		g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 1, crtaStart + 95,
				pocetakObrY + visinaRetka * 1);
		g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 1);

		g2.drawString("3. Iznos za postupke (u ljekarni)", marginaObracun,
				pocetakObrY + visinaRetka * 2);
		g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 2, crtaStart + 95,
				pocetakObrY + visinaRetka * 2);
		g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 2);

		g2.drawString("4. Iznos doplate", marginaObracun, pocetakObrY
				+ visinaRetka * 3);
		g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 3, crtaStart + 95,
				pocetakObrY + visinaRetka * 3);
		g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 3);

		g2.drawString("5. Iznos na teret obveznog zdravstvenog osiguranja",
				marginaObracun, pocetakObrY + visinaRetka * 4);
		g2.drawLine(crtaStart + 3, pocetakObrY + visinaRetka * 4,
				crtaStart + 95, pocetakObrY + visinaRetka * 4);
		g2.drawString("kn.", crtaStart + 97, pocetakObrY + visinaRetka * 4);

		g2.drawString("6. Iznos obraèunatog PDV-a u toèki 5.", marginaObracun,
				pocetakObrY + visinaRetka * 5);
		g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 5, crtaStart + 95,
				pocetakObrY + visinaRetka * 5);
		g2.drawString("kn.", crtaStart + 97, pocetakObrY + visinaRetka * 5);

		g2.drawString("Datum narudžbe ", lijeviRub + 11, pocetakObrY
				+ visinaRetka * 4 + 9);
		g2.drawLine(lijeviRub + 54 + 11, pocetakObrY + visinaRetka * 4 + 9,
				lijeviRub + 108 + 11, pocetakObrY + visinaRetka * 4 + 9);
		g2.drawString("/20", lijeviRub + 109 + 11, pocetakObrY + visinaRetka
				* 4 + 9);
		g2.drawLine(lijeviRub + 121 + 11, pocetakObrY + visinaRetka * 4 + 9,
				lijeviRub + 131 + 11, pocetakObrY + visinaRetka * 4 + 9);
		g2.drawString("g.", lijeviRub + 133 + 11, pocetakObrY + visinaRetka * 4
				+ 9);

		int razinaMjestoDatum = pocetakObrY + visinaRetka * 6 + 15;
		g2.drawString("Mjesto i datum ", lijeviRub, razinaMjestoDatum);
		g2.drawLine(lijeviRub + 44, razinaMjestoDatum, lijeviRub + 141,
				razinaMjestoDatum);
		g2.drawString(", ", lijeviRub + 144, razinaMjestoDatum);
		g2.drawLine(lijeviRub + 147, razinaMjestoDatum, lijeviRub + 175,
				razinaMjestoDatum);
		g2.drawString("20", lijeviRub + 176, razinaMjestoDatum);
		g2.drawLine(lijeviRub + 184, razinaMjestoDatum, lijeviRub + 194,
				razinaMjestoDatum);
		g2.drawString("g.", lijeviRub + 195, razinaMjestoDatum);

		int donjaRazina = pocetakObrY + visinaRetka * 9 + 1;
		// potpisi raznorazni MP ...
		g2.setFont(this.centuryGothicMali);
		g2.drawLine(lijeviRub, donjaRazina, lijeviRub + 176, donjaRazina);
		g2.drawString("Potpis osigurane osobe:", lijeviRub + 55,
				donjaRazina + 7);

		g2.drawString("M.P.", marginaObracun + 25, donjaRazina);

		g2.drawLine(crtaStart - 37, donjaRazina, crtaStart + 99, donjaRazina);
		g2.drawString("Ime, prezime i potpis odgovorne osobe", crtaStart - 28,
				donjaRazina + 7);

		g2.setFont(this.centuryGothicMali);
		g2.drawString("HZZO-Direkcija, Zagreb", x + 17, y + 205 + 455 + 3 + 6
				+ 83);
		g2.drawString("RacOsZO V1, 19/12/06", x + 17, y + 205 + 455 + 3 + 12
				+ 83);

		g2.drawString("* - zaokružiti odgovarajuæe", x + 105, y + 205 + 455 + 3
				+ 6 + 83);

		g2.setFont(this.centuryGothicExtraMali);
		g2.drawString(FOOTER_FIRMA, x + 465, y + 205 + 455 + 4 + 83);

	}// iscrtajObrazac

	private void nacrtajKockice(Graphics2D g, int x, int y, int brojKockica) {
		nacrtajKockice(g, x, y, brojKockica, 13.5f);
	}

	private void nacrtajKockice(Graphics2D g, int x, int y, int brojKockica,
			float sirKoc) {
		float visKoc = 13.5f;
		g.setStroke(crtice);
		int sirina = (int) (brojKockica * sirKoc);
		g.drawRect(x, y, sirina, (int) visKoc);
		for (int i = 0; i < brojKockica; i++)
			g.drawLine((int) (x + i * sirKoc), y, (int) (x + i * sirKoc),
					(int) (y + visKoc));
	}// nacrtajKockice

	private void nacrtajOkvir(Graphics2D g, int x, int y, int sirina) {
		float visKoc = 13.5f;
		g.setStroke(crtice);
		g.drawRect(x, y, sirina, (int) visKoc);
	}// nacrtajKockice

}// klasa
