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
import java.awt.image.BufferedImage;
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
public final class IspisRacunaDopunskoOsiguranje implements Printable {
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

	Font centuryGothicVeliki = new Font("Century Gothic", Font.PLAIN, 12);
	Font centuryGothicSrednji = new Font("Century Gothic", Font.PLAIN, 9);
	Font centuryGothicSrednjiBold = new Font("Century Gothic", Font.BOLD, 9);
	Font centuryGothicMali = new Font("Century Gothic", Font.TRUETYPE_FONT, 6);
	Font centuryGothicExtraMali = new Font("Century Gothic",
			Font.TRUETYPE_FONT, 4);
	Font nazivPomagalaMaliFont = new Font("Arial", Font.PLAIN, 7);
	Font centuryGothicMaliItalic = new Font("Century Gothic", Font.ITALIC, 6);
	Font arialSrednjiBold = new Font("Arial", Font.BOLD, 9);
	Font centuryGothic12Bold = new Font("Century Gothic", Font.BOLD, 12);

	float dash[] = { 2, 1 };
	BasicStroke crtice = new BasicStroke(0.6f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);

	int pocetakStraniceOdskokX = 0;
	int pocetakStraniceOdskokY = 0;// 15;

	float omjerSir = 1.0f;
	float omjerDuz = 1.0f;

	int ddY = 0;

	BufferedImage pozadina;

	RacunVO racun = null;

	public IspisRacunaDopunskoOsiguranje(RacunVO racun) {
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

		// pozadina=HtmlPrintParser.ucitajSliku("biz/sunce/obrasci/RacDopZO.jpg");

	}// konstruktor

	public void printDialog() {
		printaj(true);
	}

	static int brojKopija = 1;

	public void printaj(boolean prikaziDialog) {
		PrinterJob pJob = PrinterJob.getPrinterJob();
		printaj(prikaziDialog, pJob);
	}

	public void printaj(boolean prikaziDialog, PrinterJob pJob) {
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
					Logger.warn("Nismo uspjeli postaviti printer za ispis (do): "+printer, e);
				}
				break;
			 }
			}
		}
		
		//izbaci dialog za odabir printera ako je potrebno
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
			racY = (int) (zaglY + 158 * omjerSir + 0.5f);
			napX = zaglX;
			napY = (int) (zaglY + 675 * omjerDuz + 0.5f);

			pJob.setPrintable(this, pf);
			String naslov = "hzzo_racun_dop_osig_"
					+ (this.racun != null ? this.racun
							.getBrojOsobnogRacunaDopunsko() : "nepoznat_broj");
			pJob.setJobName(naslov);

			try {
				pJob.print();
				brojKopija = pJob.getCopies(); // da se upamti za slijedeci put
			
				//ako se zadnje koristeni printer promjenio, pohrani informaciju u postavkama
				PrintService printService = pJob.getPrintService();
				String printerSvcName = printService==null?null:printService.getName();
				 
				if (printer!=null && printerSvcName!=null && !printer.equals(printerSvcName))
				   PostavkeBean.setPostavkaDB(GlavniFrame.ODABRANI_PRINTER, printerSvcName);
				
			} 
			catch (PrinterException printerException) 
			{
				Logger.fatal("Iznimka prilikom ispisa dopunskog hzzo raèuna: ",
						printerException);
			} 
			catch (Error greska) 
			{
				Logger.fatal("Greška sustava prilikom ispisa dopunskog hzzo raèuna: ",
						greska);
			} 
			finally 
			{
				pJob = null;
				pf = null;
			}
		}
	}// printDialog

	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		if (pageIndex > 0)
			return NO_SUCH_PAGE;

		// prvo iscrati obrazac na pozadini...
		this.iscrtajObrazac(g);

		g.setFont(glavni);

		MjestoVO podrUred = racun == null ? null : racun.getPodrucniUred();

		if (podrUred == null) {
			try {
				podrUred = (MjestoVO) DAOFactory.getInstance().getMjesta()
						.read(racun.getSifPodrucnogUreda());
			} catch (SQLException e) {
				Logger.log(
						"Iznimka kod traženja mjesta podruènog ureda za raèun (ispisDop) osn: "
								+ racun.getBrojOsobnogRacunaOsnovno(), e);
			}
		}// if podrucni ured je null

		String pnaziv = podrUred != null ? podrUred.getNaziv() : "";

		if (this.pozadina != null)
			g.drawImage(this.pozadina, zaglX, zaglY, pozadina.getWidth(),
					pozadina.getHeight(), Color.white, null);

		g.drawString(pnaziv, (int) (zaglX + 75 * omjerSir),
				(int) (zaglY + 64 * omjerDuz));

		g.drawString(pnaziv, (int) (zaglX + 50 * omjerSir),
				(int) (zaglY + 85 * omjerDuz));

		String ulica = "";

		if (podrUred != null) {
			ulica = PostavkeBean.getPostavkaDB("hzzo_adr_"
					+ podrUred.getSifra().intValue(), "");
		}

		g.drawString(ulica, (int) (zaglX + 62 * omjerSir),
				(int) (zaglY + 115 * omjerDuz));

		String tipRacuna = PostavkeBean.getTipRacuna();

		String brojOsobnogRacuna = racun.getBrojOsobnogRacunaDopunsko() != null
				&& racun.getBrojOsobnogRacunaDopunsko().trim().length() > 0 ? racun
				.getBrojOsobnogRacunaDopunsko() : "";

		// broj osobnog racuna OK
		g.drawString(brojOsobnogRacuna, (int) (racX + 380 * omjerSir),
				(int) (ddY + 116 * omjerDuz));

		g.setFont(mono);

		String sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();

		// HZZO sifra isporucitelja
		int sifIspLen = sifIsporucitelja.length();
		for (int i = 0; i < sifIspLen; i++)
			g.drawString(sifIsporucitelja.charAt(i) + "", (int) (racX + 99
					* omjerSir + i * 13.5d * omjerSir),
					(int) (racY + 38 * omjerDuz));

		String brojPotvrde = racun.getBrojPotvrde1(), brojRacuna = racun
				.getBrojPotvrde2();

		// da ne pametujemo previse, moze biti 1,2 ili 3 znaka
		int brojPotvLen = brojPotvrde.length();
		if (brojPotvLen == 2)
			brojPotvrde = " " + brojPotvrde;
		else if (brojPotvLen == 1)
			brojPotvrde = "  " + brojPotvrde;

		brojPotvLen = brojPotvrde.length();

		for (int i = 0; i < brojPotvLen; i++)
			g.drawString(brojPotvrde.charAt(i) + "", (int) (racX + 331
					* omjerSir + i * 11.9d * omjerSir + 0.5f), (int) (racY + 38
					* omjerDuz + 0.5f));

		int brRacLen = brojRacuna.length();
		for (int i = 0; i < brRacLen; i++)
			g.drawString(brojRacuna.charAt(i) + "", (int) (racX + 375
					* omjerSir + i * 12.0d * omjerSir + 0.5f), (int) (racY + 38
					* omjerDuz + 0.5f));

		// broj police dopunskog osiguranja OK
		String policaDopunsko = racun.getBrojPoliceDopunsko();
		int polDopLen = policaDopunsko.length();
		for (int i = 0; i < polDopLen; i++)
			g.drawString(policaDopunsko.charAt(i) + "", (int) (racX + 370
					* omjerSir + i * 13.5d * omjerSir + 0.5f), (int) (racY
					+ 145 * omjerDuz + 0.5f));

		// pravi maticni broj isporucitelja
		String oibIsporucitelja = p.getTvrtkaOIB();
		int oibILen = oibIsporucitelja != null ? oibIsporucitelja.length() : -1;

		int sfpi = GlavniFrame.getSifraPoslovniceZaSynch();
		if (sfpi > 0) {
			g.setFont(centuryGothicMaliItalic);
			g.drawString("Poslovnica br.: " + sfpi, (int) (racX + 324
					* omjerSir + 0 * 13.5d * omjerSir + 0.5f), (int) (ddY + 170
					* omjerDuz + 0.5f));
			g.setFont(mono);
		}

		int y = (int) (ddY + 190 * omjerDuz + 0.5f);
		if (oibIsporucitelja != null && oibILen <= 8) {
			for (int i = 0; i < oibILen; i++)
				g.drawString(oibIsporucitelja.charAt(i) + "", (int) (racX + 371
						* omjerSir + i * 13.5d * omjerSir + 0.5f), y);
		} else // ako je mb veci od 9 znakova samo ga ispisujemo
		if (oibIsporucitelja != null && oibILen > 8) {
			g.setFont(glavni);
			int x = (int) (racX + 371 * omjerSir + 0.5f);
			g.setColor(Color.white);
			g.fillRect(x - 1, y - 11, (int) (omjerSir * 8 * 13.4f + 0.5f), 11);
			g.setColor(Color.black);
			g.drawString(oibIsporucitelja, x, y);
		}

		g.setFont(glavni);
		// naziv tvrtke OK
		String nazivOptike = p.getTvrtkaNaziv();
		g.drawString(nazivOptike, (int) (racX + 50 * omjerSir + 0.5f),
				(int) (racY + 69 * omjerDuz + 0.5f));
		// adresa tvrtke OK
		String adresaOptike = p.getMjestoRada() + ", " + p.getTvrtkaAdresa();
		g.drawString(adresaOptike, (int) (racX + 117 * omjerSir + 0.5f),
				(int) (racY + 106 * omjerDuz + 0.5f));

		// tvrtka racun OK
		g.drawString(p.getTvrtkaRacun(), (int) (racX + 75 * omjerSir + 0.5f),
				(int) (racY + 147 * omjerDuz + 0.5f));

		String poziv1 = racun.getPozivNaBroj1(), poziv2 = racun
				.getPozivNaBroj2();
		if (poziv1 == null)
			poziv1 = "";
		if (poziv2 == null)
			poziv2 = "";
		// poziv na broj
		g.drawString(poziv1, (int) (this.zaglX + 67 * omjerSir + 0.5f), y);
		g.drawString(poziv2, (int) (zaglX + 84 * omjerSir + 0.5f), y);

		g.setFont(mono);
		// sifra proizvodjaca
		// String proizvodjac=racun.getSifProizvodjaca();
		// if (proizvodjac==null) proizvodjac="";

		// ako je sifra proizvodjaca manja od 9 znakova trebamo 'desno'
		// poravnati broj
		// int razl=9-proizvodjac.length(); if (razl<0) razl=0;

		// for (int i=0; i<proizvodjac.length(); i++)
		// g.drawString(proizvodjac.charAt(i)+"",(int)(racX+99*omjerSir+(i+razl)*13.5d*omjerSir+0.5f),(int)(racY+126*omjerDuz+0.5f));

		List stavke = racun.getStavkeRacuna();

		// ako se ne nalaze u samom racunu, treba ih 'rucno' pokupiti iz baze
		if (stavke == null) {
			try {
				stavke = (ArrayList) DAOFactory.getInstance().getStavkeRacuna()
						.findAll(racun);
			} catch (SQLException ex) {
				Logger.log(
						"SQL iznimka kod trazenja stavki racuna za racun br.oso.dop.:"
								+ (racun != null ? racun
										.getBrojOsobnogRacunaDopunsko() : "?!?"),
						ex);
			}
		}// if stavke == null

		int startcy = (int) (ddY + 236.5f * omjerSir + 0.5f);
		double odskokRetka = 21.1d * omjerDuz;
		int suma = 0;
		int sumaPorezneOsnove = 0;
		int sumaPoreza = 0;
		PoreznaStopaVO stopa = null;
		int sirinaPoljaNazivPomagala = (int) (96 * omjerSir + 0.5f);
		String sfp;

		int stSize = stavke.size();

		for (int sf = 0; sf < stSize; sf++) {
			StavkaRacunaVO st = (StavkaRacunaVO) stavke.get(sf);
			PomagaloVO pom = nadjiPomagalo(st.getSifArtikla());
			stopa = nadjiPoreznuSkupinu(st.getPoreznaStopa()); // 12.03.06.
																// -asabo-
																// dodano

			g.setFont(mono);
			String sif = st.getSifArtikla();

			// zbog starih racuna moramo upisivati prazan string ako nema sifre
			// proizvodjaca, umjesto da blokiramo proces..
			sfp = (st.getSifProizvodjaca() != null ? ""
					+ st.getSifProizvodjaca().intValue() : "");

			boolean iso9999 = sif != null && (sif.length() == 12 || sif.length() == 13)
					&& StringUtils.imaSamoBrojeve(sif);

			// sifra pomagala
			int cy = (int) (startcy + sf * odskokRetka);

			if (!iso9999) {
				g.setFont(mono);
				// sifra pomagala
				int sfl = sif.length();
				for (int i = 0; i < sfl; i++)
					g.drawString(sif.charAt(i) + "", (int) (racX + 135
							* omjerSir + i * 13.5d * omjerSir + 0.5f), cy);
			} else {
				g.setColor(Color.white);
				g.fillRect((int) (racX + 140 * omjerSir + 0.5f), cy - 11,
						(int) (omjerSir * 6 * 13.4f + 0.5f), 11);
				g.setColor(Color.black);

				g.setFont(glavni);
				g.drawString(sif, (int) (racX + 134 * omjerSir + 0.5f), cy);
				g.setFont(mono);
			}

			// sifra proizvodjaca
			if (!iso9999 && sfp != null && sfp.trim().length() > 0) {
				sfp = sfp.trim(); // ocistiti za svaki slucaj od visaka sa
									// strane...
				int razlika = 7 - sfp.length();
				for (int i = 0; i < razlika; i++)
					sfp = " " + sfp;

				for (int i = 0; i < sfp.length(); i++)
					g.drawString(sfp.charAt(i) + "", (int) (racX + 237
							* omjerSir + i * 13.5d * omjerSir + 0.5f), cy);
			}

			g.setFont(nazivPomagalaMaliFont);
			String naziv = (pom != null ? pom.getNaziv() : "?!?");

			FontMetrics fm = g.getFontMetrics(nazivPomagalaMaliFont);
			int sir = (int) fm.stringWidth(naziv);
			int lijeviRubNazivPomagala = (int) (racX + 35.0f * omjerSir + 0.5f);

			// ako tekst stane unutar predvidjenog prostora trpamo u jedan redak
			if (sir <= sirinaPoljaNazivPomagala) {
				g.drawString(naziv, lijeviRubNazivPomagala, cy); // cy je vec
																	// podesen
																	// sa
																	// faktorom
																	// duzine
			} else {
				int mj = 0;
				int dozSir = 0;

				while (mj < naziv.length()) {
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
				while (mj < naziv.length()) {
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
					* omjerSir + 0.5f), cy, glavni); // bilo 345 mjesto 360
			ispisiNovac(g, st.getPoCijeni().intValue(), (int) (racX + 417
					* omjerSir + 0.5f), cy, glavni);
			ispisiNovac(g, total, (int) (racX + 498 * omjerSir + 0.5f), cy,
					glavni);

			suma += total;
			sumaPorezneOsnove += poreznaOsnova;
			sumaPoreza += porez;
		}// for sf

		boolean nemaPoreza = (sumaPoreza == 0);

		// ukupno prvi dio gore odskokRetka je vec pomnozen sa duzinskim
		// faktorom
		this.ispisiNovac(g, suma, (int) (racX + 498 * omjerSir + 0.5f),
				(int) (startcy + 7 * odskokRetka), glavni);
		int desniRub = (int) (480 * omjerSir + 0.5f);

		// u slucaju osnovnog osiguranja to je iznos koji placa stranka
		int iznosSudjelovanja = racun.getIznosSudjelovanja() != null ? racun
				.getIznosSudjelovanja().intValue() : 0;

		// ukupan iznos se sastoji od nekoliko proizvoda po nekoliko poreznih
		// skupina
		// i tocan iznos placenog poreza djeli se u jednakom omjeru na stranku i
		// osiguranje
		// trenutno je to jedini nacin na koji znamo da se porez raspodjeljuje
		float omjerZdr = (float) (iznosSudjelovanja) / suma;

		sumaPoreza = (int) ((float) sumaPoreza * omjerZdr + 0.5f);

		int pocetakY = (racY + 71 + 119);
		// startcy=(int)(-2*omjerDuz+startcy+9*odskokRetka-5); odskokRetka=17;
		odskokRetka = 17;
		int pocetakObrY = (int) (((float) pocetakY + 21 * 11 - 8) * 1.0f);
		startcy = pocetakObrY;
		desniRub += 20;

		// ukupni iznos s pdv-om
		this.ispisiNovac(g, suma, racX + desniRub,
				(int) (startcy + 0 * odskokRetka), glavni);

		// iznos za pomagala
		this.ispisiNovac(g, suma, racX + desniRub,
				(int) (startcy + 1 * odskokRetka), glavni);

		// iznos za postupke u ljekarni - 0 kn
		this.ispisiNovac(g, 0, racX + desniRub,
				(int) (startcy + 2 * odskokRetka), glavni);

		// iznos na teret obveznog zdr. osiguranja
		this.ispisiNovac(g, racun.getIznosOsnovnogOsiguranja().intValue(), racX
				+ desniRub, (int) (startcy + 3 * odskokRetka), glavni);

		// iznos na teret dopunskog osiguranja
		this.ispisiNovac(g, iznosSudjelovanja, racX + desniRub,
				(int) (startcy + 4 * odskokRetka), glavni);

		// iznos obracunatog PDV-a u tocki 5. (iznos na teret dop. osig.)
		this.ispisiNovac(g, sumaPoreza, racX + desniRub,
				(int) (startcy + 5 * odskokRetka), glavni);

		g.setFont(this.mali);
		if (nemaPoreza)
			g.drawString(
					"Osloboðeno plaæanja PDV-a po èl. 10A Zakona o PDV-u.",
					desniRub - 160, (int) (startcy + 7 * odskokRetka));

		g.setFont(glavni);
		// ukupni iznos s pdv-om
		// this.ispisiNovac(g,suma,racX+desniRub,(int)(startcy+0*odskokRetka),glavni);

		// this.ispisiNovac(g,racun.getIznosOsnovnogOsiguranja().intValue(),racX+desniRub,(int)(startcy+1*odskokRetka),glavni);
		// this.ispisiNovac(g,iznosSudjelovanja,racX+desniRub,
		// (int)(startcy+2*odskokRetka),glavni);

		// this.ispisiNovac(g,sumaPoreza,racX+desniRub,
		// (int)(startcy+3*odskokRetka),glavni);

		Calendar c = racun.getDatumNarudzbe();

		String datum = "" + c.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c.get(Calendar.MONTH)];
		int god = c.get(Calendar.YEAR) - 2000;
		g.setFont(mali);
		g.drawString(datum, (int) (racX + 70 * omjerSir + 0.5f), (int) (1
				* omjerDuz + startcy + 4 * odskokRetka + 8.5f));
		g.drawString("" + (god < 10 ? "0" + god : "" + god), racX + 137,
				(int) (1 * omjerDuz + startcy + 4 * odskokRetka + 8.5f));

		Calendar c2 = racun.getDatumIzdavanja();

		datum = "" + c2.get(Calendar.DAY_OF_MONTH) + ". "
				+ biz.sunce.util.Util.mjeseci[c2.get(Calendar.MONTH)];
		god = c2.get(Calendar.YEAR) - 2000;

		g.drawString(p.getMjestoRada(), (int) (racX + 60 * omjerSir + 0.5f),
				(int) (5 * omjerDuz + startcy + 6 * odskokRetka + 10.5f));
		g.drawString(datum, (int) (racX + 125 * omjerSir + 0.5f), (int) (5
				* omjerDuz + startcy + 6 * odskokRetka + 10.5f));
		g.drawString("" + (god < 10 ? "0" + god : "" + god), (int) (racX + 190
				* omjerSir + 0.5f), (int) (5 * omjerDuz + startcy + 6
				* odskokRetka + 10.5f));

		DjelatnikVO dvo = GlavniFrame.getDjelatnik();
		String odgOsoba = dvo != null ? dvo.getIme() + " " + dvo.getPrezime()
				: "?!?";
		g.drawString(odgOsoba, (int) (460 * omjerSir + 0.5f), (int) (2
				* omjerDuz + startcy + 8 * odskokRetka + 15f));

		// String napomena=racun.getNapomena()!=null?racun.getNapomena():"";
		// g.drawString(napomena,napX,napY);

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
	}// nadjiPomagalo

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

	// iscrtava na 'papiru' obrazac za osnovno zdr. osiguranje
	private void iscrtajObrazac(Graphics g) {
		// ********************************** ZAGLAVLJE RACUNA
		// *************************
		Graphics2D g2 = (Graphics2D) g;
		int x = (int) this.zaglX;
		int y = (int) this.zaglY;
		int lijeviRub = x + 23;
		int dpStart = 153;

		ddY = y + 148;

		g2.setColor(Color.black);

		g2.setStroke(crtice);
		// g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_MITER,tocke,1.0f));
		g2.drawRect(x, y, 520, 150); // 202 stari

		g2.setFont(this.centuryGothicSrednjiBold);
		g2.drawString("HRVATSKI ZAVOD ZA ZDRAVSTVENO OSIGURANJE", lijeviRub,
				y + 37);

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

		g2.setFont(this.arialSrednjiBold);
		g2.drawString("RAÈUN ZA DOPUNSKO", x + 349, y + 58);
		g2.drawString("ZDRAVSTVENO OSIGURANJE", x + 339, y + 67);

		// ********************************************* RACUN
		// ***************************

		g2.drawRect(x, y + dpStart, 520, 593); // 205 stari
		g2.setFont(this.centuryGothicSrednjiBold);
		g2.drawString("I. IZDAVANJE POMAGALA, DIJELA POMAGALA", x + 8, y
				+ dpStart + 12);
		g2.setFont(this.centuryGothicMali);
		g2.drawString("(ispunjava ugovorni isporuèitelj)", x + 210, y + dpStart
				+ 12);

		g2.setFont(this.centuryGothicMali);
		g2.drawString("  Matièni broj HZZO:     0 3 5 8 0 2 6 1 ", x + 374, y
				+ dpStart + 12);

		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("Ugovorni isporuèitelj: ", lijeviRub, y + dpStart + 37);
		g2.drawString("(šifra) ", x + 70, y + dpStart + 44);

		String mbIsporucitelja = p.getTvrtkaOIB(); // mb isporucitelja
		if (mbIsporucitelja != null && mbIsporucitelja.length() <= 8)
			nacrtajKockice(g2, lijeviRub + 74, y + dpStart + 31, 9); // 11up
		else if (mbIsporucitelja != null && mbIsporucitelja.length() > 8)
			this.nacrtajOkvir(g2, lijeviRub + 74, y + dpStart + 31, 115);

		int linijaNaziv = racY + 71;

		g2.drawString("Naziv: ", lijeviRub, linijaNaziv);
		g2.drawLine(x + 45, linijaNaziv, x + 250, linijaNaziv);

		String tipRacuna = PostavkeBean.getTipRacuna();
		g2.setFont(this.centuryGothicSrednjiBold);
		g2.drawString(tipRacuna, (racX + 318), linijaNaziv);

		g2.setFont(this.centuryGothicMali);
		g2.drawString("Adresa (mjesto, ulica i broj) ", lijeviRub,
				linijaNaziv + 34);
		g2.drawLine(x + 110, linijaNaziv + 37, x + 309, linijaNaziv + 37);

		g2.setFont(this.centuryGothicMali);
		g2.drawString("Broj raèuna", (racX + 318), linijaNaziv + 30);
		g2.drawString("isporuèitelja", (racX + 318), linijaNaziv + 37);
		g2.setColor(Color.GRAY);
		g2.drawLine(racX + 369, linijaNaziv + 37, racX + 369 + 116,
				linijaNaziv + 37);

		g2.setColor(Color.BLACK);
		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("Broj žiro-raèuna: ", lijeviRub, linijaNaziv + 78);
		g2.drawLine(x + 74, linijaNaziv + 78, x + 230, linijaNaziv + 78);

		g2.setFont(this.centuryGothicMali);
		g2.drawString("Br. police dop.", (racX + 318), linijaNaziv + 68);
		g2.drawString("zdr. osig. HZZO", (racX + 318), linijaNaziv + 75);
		nacrtajKockice(g2, (racX + 369), (linijaNaziv + 63), 9);

		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("Poziv na broj: ", lijeviRub, linijaNaziv + 110);
		g2.setColor(Color.GRAY);
		nacrtajOkvir(g2, x + 66, linijaNaziv + 98, 14); // poziv na broj 1
		nacrtajOkvir(g2, x + 83, linijaNaziv + 98, 115); // poziv na broj 2

		g2.setColor(Color.BLACK);
		g2.setFont(this.centuryGothicMaliItalic);
		g2.drawString("Matièni broj", racX + 318, linijaNaziv + 103);
		g2.drawString("isporuèitelja:", racX + 318, linijaNaziv + 110);
		nacrtajKockice(g2, racX + 369, linijaNaziv + 98, 8); // ug. isporucitelj

		g2.drawString("Broj potvrde / raèuna", x + 374, y + dpStart + 30);
		// broj racuna, prve tri kockice
		nacrtajKockice(g2, racX + 330, y + dpStart + 32, 3, 12.0f); // broj
																	// potvrde
																	// 11 komada
																	// dizemo
																	// gore
		// /
		g2.setFont(this.centuryGothicVeliki);
		g2.drawString("/", x + 368, y + dpStart + 43);
		// broj racuna
		nacrtajKockice(g2, racX + 374, y + dpStart + 32, 9, 12.0f); // 11 gore

		nacrtajKockice(g2, racX + 486, y + dpStart + 32, 1, 12.0f); // kockica
																	// za D
																	// slovo
		g2.setFont(this.centuryGothicSrednjiBold);
		g2.drawString(" D", racX + 486, y + dpStart + 42); // slovo D

		// proizvodjac
		// nacrtajKockice(g2,racX+98,linijaNaziv+43,9); //

		// *************** iscrtavanje mreze za stavke racuna ******************
		int visinaRetka = 21, redaka = 8;
		int visina1 = visinaRetka * redaka, visina2 = visina1 + visinaRetka;
		int pocetakY = (linijaNaziv + 119);

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

		int marginaObracun = lijeviRub + 244;
		int crtaStart = marginaObracun + 161;
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

		g2.drawString("4. Iznos na teret obveznog zdravstvenog osiguranja",
				marginaObracun, pocetakObrY + visinaRetka * 3);
		g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 3, crtaStart + 95,
				pocetakObrY + visinaRetka * 3);
		g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 3);

		g2.drawString("5. Iznos na teret dopunskog zdravstvenog osiguranja",
				marginaObracun, pocetakObrY + visinaRetka * 4);
		g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 4, crtaStart + 95,
				pocetakObrY + visinaRetka * 4);
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

		// ************************ napomena dio
		// **********************************
		// g2.drawRect(x,y+205+455+3,520,83);
		g2.setFont(this.centuryGothicMali);
		g2.drawString("HZZO-Direkcija, Zagreb", x + 17, y + 205 + 455 + 3 + 6
				+ 83);
		g2.drawString("RacDopZO V1, 19/12/06", x + 17, y + 205 + 455 + 3 + 12
				+ 83);

		g2.drawString("* - zaokružiti odgovarajuæe", x + 105, y + 205 + 455 + 3
				+ 6 + 83);

		g2.setFont(this.centuryGothicExtraMali);
		g2.drawString(FOOTER_FIRMA, x + 580, y + 205 + 455 + 8 + 83);
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
