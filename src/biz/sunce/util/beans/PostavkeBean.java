/*
 * Project opticari
 *
 */
package biz.sunce.util.beans;

import java.awt.Font;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.prefs.Preferences;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.PostavkeDAO;
import biz.sunce.opticar.vo.PostavkaVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;

/**
 * sistemske postavke, default username, naziv, adresa tvrtke i sl.
 * 
 * @author Ante Sabo
 * @version 1.0
 */

public final class PostavkeBean {
	private static final String PRAZAN_STRING = "";

	private static String LINK_NA_POSTAVKE = "Opticar_postavke";
	// private File datoteka;

	public static String TVRTKA_NAZIV = "tvrtkaNaziv";
	public static String TVRTKA_MB = "tvrtkaMb";
	public static String TVRTKA_ADRESA = "tvrtkaAdresa";
	public static String TVRTKA_BANKA = "tvrtkaBanka";
	public static String TVRTKA_RACUN = "tvrtkaRacun";
	public static String TVRTKA_TELEFON = "tvrtkaTelefon";
	public static String TVRTKA_FAX = "tvrtka_fax";
	public static String TVRTKA_EMAIL = "tvrtkaEmail";
	public static String TVRTKA_SIFRA = "tvrtkaSifra";
	public static String TVRTKA_SIFRA_POSLOVNICE = "tvrtkaSifraPoslovnice";

	public static String TIP_RACUNA = "tipRacuna";
	
	public static String FONT_NAME = "fontName";
	public static String FONT_STYLE= "fontStyle";
	public static String FONT_SIZE = "fontSize";
	
	public static String TVRTKA_HZZO_SIFRA_ISPORUCITELJA = "hzzo_sif_isporucitelja";
	public static String TVRTKA_HZZO_RACUN_ODSKOK_X = "hzzo_racun_odskok_x";
	public static String TVRTKA_HZZO_RACUN_ODSKOK_Y = "hzzo_racun_odskok_y";
	public static String TVRTKA_HZZO_RECEPT_POTOC_X = "hzzo_recept_potoc_odskok_x";
	public static String TVRTKA_HZZO_RECEPT_POTOC_Y = "hzzo_recept_potoc_odskok_y";
	public static String TVRTKA_HZZO_RACUN_OMJER_SIRINA = "hzzo_racun_omjer_sirina";
	public static String TVRTKA_HZZO_RACUN_OMJER_DUZINA = "hzzo_racun_omjer_duzina";
	public static String TVRTKA_HZZO_RACUN_AUTOM_POZIV_BROJ = "hzzo_racun_autom_poziv";
	public static String TVRTKA_HZZO_RACUN_ZA_DOPUNSKO = "hzzo_racun_za_dopunsko"; // broj
																					// racuna
																					// za
																					// dopunsko
																					// osiguranje
	public static String TVRTKA_HZZO_BROJACI_OVISNI_O_GODINI = "hzzo_brojaci_ovisni_o_godini";

	public static String ISPIS_U_GLAVNI_PRINTER= "ispis_u_glavni_printer";
	public static String AUTOMATSKO_RACUNANJE_SUDJELOVANJA= "automatsko_rac_sudjelovanja";

	public static String TVRTKA_MJESTO_RADA = "tvrtkaMjestoRada";

	public static String KONTROLA_OSOBNIH_RACUNA = "hzzo_kontrola_osobnih_racuna";

	private String tvrtkaNaziv = PRAZAN_STRING;
	private String tvrtkaMB = PRAZAN_STRING;
	private String tvrtkaAdresa = PRAZAN_STRING;
	private String tvrtkaBanka = PRAZAN_STRING;
	private String tvrtkaRacun = PRAZAN_STRING;
	private String tvrtkaTelefon = PRAZAN_STRING;
	private String tvrtkaFax = PRAZAN_STRING;
	private String tvrtkaEmail = PRAZAN_STRING;
	private int sifraTvrtke = 0;
	private int sifraPoslovnice = 0;
	private static String hzzoSifraIsporucitelja = null;
	private static Date datumValjanosti=null;
	
	private Font font = null;

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public static String getHzzoSifraIsporucitelja() {
		//pri podizanju programa sifra hzzo isporucitelja postavit ce se iz .key datoteke
		//ako ne bude postavljena, probat ce ju procitati iz baze
		if (hzzoSifraIsporucitelja == null)
			hzzoSifraIsporucitelja = PostavkeBean.getPostavkaDB(
					TVRTKA_HZZO_SIFRA_ISPORUCITELJA, PRAZAN_STRING);
		return hzzoSifraIsporucitelja;
	}
	
	public static Date getDatumValjanosti(){
		return datumValjanosti;
	}

	public static void setHzzoSifraIsporucitelja(String hzzoSifraIsporucitelja) {
		PostavkeBean.hzzoSifraIsporucitelja = hzzoSifraIsporucitelja;
		if (hzzoSifraIsporucitelja!=null && 
			(hzzoSifraIsporucitelja.equals("546854680") || hzzoSifraIsporucitelja.equals("500054681") ))
		{
		GlavniFrame.alert("Nastala je greska u aplikaciji. Aplikacija ce prekinuti rad. Molimo kontaktirajte administratora!");
		System.exit(-1);
		}
	}

	public static void setDatumValjanosti(Date datumValjanosti) {
		PostavkeBean.datumValjanosti= datumValjanosti;
	}
	
	private String mjestoRada = PRAZAN_STRING;

	private static PostavkeDAO postavke = null;

	public PostavkeBean() {
		this.loadData();
	}// konstruktor

	// 16.07.05. -asabo- metode za spremanje postavki u registry
	public static boolean setKorisnickaPostavka(String naziv, String vrijednost) {
		return setPostavka(naziv, vrijednost, true);
	}

	public static boolean setPostavkaSustava(String naziv, String vrijednost) {
		return setPostavka(naziv, vrijednost, false);
	}

	public static boolean setIntKorisnickaPostavka(String naziv, int vrijednost) {
		return setIntKorisnickaPostavka(naziv, vrijednost, true);
	}

	public static boolean setIntPostavkaSustava(String naziv, int vrijednost) {
		return setIntKorisnickaPostavka(naziv, vrijednost, false);
	}

	// 16.07.05. -asabo- metode za spremanje postavki u registry
	public static String getKorisnickaPostavka(String naziv,
			String defaultVrijednost) {
		return getPostavka(naziv, defaultVrijednost, true);
	}

	public static String getPostavkaSustava(String naziv,
			String defaultVrijednost) {
		return getPostavka(naziv, defaultVrijednost, false);
	}

	public static int getIntKorisnickaPostavka(String naziv,
			int defaultVrijednost) {
		return getIntPostavka(naziv, defaultVrijednost, true);
	}

	public static int getIntPostavkaSustava(String naziv, int defaultVrijednost) {
		return getIntPostavka(naziv, defaultVrijednost, false);
	}

	public static boolean setIntKorisnickaPostavka(String naziv, int vrijednost,
			boolean userRelated) throws IllegalArgumentException {
		return setPostavka(naziv, PRAZAN_STRING + vrijednost, userRelated);
	}

	public static boolean setPostavka(String naziv, String vrijednost,
			boolean userRelated) throws IllegalArgumentException {
		boolean rez = true;
		Preferences prefs = null;

		if (userRelated)
			prefs = Preferences.userRoot().node(LINK_NA_POSTAVKE);
		else
			prefs = Preferences.systemRoot().node(LINK_NA_POSTAVKE);

		if (prefs == null)
			throw new IllegalArgumentException("Ne mozemo pristupiti podacima");

		if (naziv == null || naziv.trim().equals(PRAZAN_STRING))
			throw new IllegalArgumentException("Naziv postavke nije ispravan");

		prefs.put(naziv, vrijednost);

		return rez;
	}// setPostavka

	public static final void setKontrolaOsobnogRacuna(boolean kontrola) {
		if (kontrola)
			setPostavkaDB(KONTROLA_OSOBNIH_RACUNA, "true");
		else
			setPostavkaDB(KONTROLA_OSOBNIH_RACUNA, "false");
	}

	public static final boolean isKontrolaOsobnogRacuna() {
		return getPostavkaDB(KONTROLA_OSOBNIH_RACUNA, "true").equals("true");
	}

	public static final boolean isBrojaciOvisniOGodini() {
		return getPostavkaDB(TVRTKA_HZZO_BROJACI_OVISNI_O_GODINI, "N").equals(
				"D");
	}
	
	public static final boolean isIspisUGlavniPrinter() {
		return getPostavkaDB(ISPIS_U_GLAVNI_PRINTER, "N").equals(
				"D");
	}
	
	public static final boolean isAutomatskoRacunanjeSudjelovanja() {
		return getPostavkaDB(AUTOMATSKO_RACUNANJE_SUDJELOVANJA, "N").equals(
				"D");
	}
	
	public static final void setIspisUGlavniPrinter(boolean ispis) {
		if (ispis)
			setPostavkaDB(ISPIS_U_GLAVNI_PRINTER, "D");
		else
			setPostavkaDB(ISPIS_U_GLAVNI_PRINTER, "N");
	}

	public static final void setAutomatskoRacunanjeSudjelovanja(boolean vrijednost) {
		if (vrijednost)
			setPostavkaDB(AUTOMATSKO_RACUNANJE_SUDJELOVANJA, "D");
		else
			setPostavkaDB(AUTOMATSKO_RACUNANJE_SUDJELOVANJA, "N");
	}

	// naziv - naziv postavke, defaultVrijednost - vrijednost koju ce metoda
	// vratiti ako podatka nema u registryju - userRelated - jeli korisnicki ili
	// sistemski root
	private static String getPostavka(String naziv, String defaultVrijednost,
			boolean userRelated) throws IllegalArgumentException {
		Preferences prefs = null;

		if (userRelated)
			prefs = Preferences.userRoot().node(LINK_NA_POSTAVKE);
		else
			prefs = Preferences.systemRoot().node(LINK_NA_POSTAVKE);

		if (prefs == null)
			throw new IllegalArgumentException("Ne mozemo pristupiti podacima");

		if (naziv == null || naziv.trim().equals(PRAZAN_STRING))
			throw new IllegalArgumentException("Naziv postavke nije ispravan");

		String vrijednost = prefs.get(naziv, defaultVrijednost);

		return vrijednost;
	}// getPostavka

	private static int getIntPostavka(String naziv, int defaultVrijednost,
			boolean userRelated) throws IllegalArgumentException {
		String vr = getPostavka(naziv, "(nema)", userRelated);
		int tmp = -1;
		try {
			tmp = Integer.parseInt(vr);
		} catch (NumberFormatException nfe) {
			return defaultVrijednost;
		}
		return tmp;
	}// getIntPostavka
	
	 
	public static int getIntPostavkaDb(String naziv, int defaultVrijednost)
			throws IllegalArgumentException 
		{		
		 String vr;	
		 
		 vr = getPostavkaDB(naziv, "(nema)");
		 
		 int tmp = -1;
		 
		 try {
			tmp = Integer.parseInt(vr);
		} catch (NumberFormatException nfe) {
			return defaultVrijednost;
		}
		return tmp;
	}// getIntPostavkaDb

	public static final boolean setPostavkaDB(String naziv, String vrijednost)
			throws IllegalArgumentException {
		boolean rez = true;
		if (naziv == null || naziv.trim().equals(PRAZAN_STRING))
			throw new IllegalArgumentException(
					"naziv postavke ne moze biti null ili prazan string");

		PostavkaVO pvo = null;

		try {
			pvo = (PostavkaVO) getPostavke().read(naziv);
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka pri pokusaju citanja postavke", e);
			return false; // odmah vracaj false
		}

		if (pvo == null) {
			pvo = new PostavkaVO();
			pvo.setNaziv(naziv);
			pvo.setVrijednost(vrijednost);
			try {
				getPostavke().insert(pvo);
				
				if (dbPostavke.containsKey(naziv)) {
					dbPostavke.remove(naziv);
				}
				
			} catch (SQLException e1) {
				Logger.fatal(
						"SQL iznimka kod pokusaja zapisivanja postavke u db",
						e1);
				rez = false;
			}
		}// if pvo==null
		else {
			pvo.setVrijednost(vrijednost);
			try {
				
				getPostavke().update(pvo);
				
				if (dbPostavke.containsKey(naziv)) {
					dbPostavke.remove(naziv);
				}
			} catch (SQLException e1) {
				Logger.fatal(
						"SQL iznimka kod pokusaja updateanja postavke u db", e1);
				rez = false;
			}
		}// else od if pvo == null

		return rez;
	}// setPostavka

	private static final PostavkeDAO getPostavke() {
		if (postavke == null) {
			postavke = DAOFactory.getInstance().getPostavke();
		}
		return postavke;
	}// getPostavke

	static Hashtable<String, String> dbPostavke = new Hashtable<String, String>(
			32);

	// naziv - naziv postavke, defaultVrijednost - vrijednost koju ce metoda
	// vratiti ako podatka nema u bazi
	public static String getPostavkaDB(String naziv, String defaultVrijednost)
			throws IllegalArgumentException {
		if (naziv == null || naziv.trim().equals(PRAZAN_STRING))
			throw new IllegalArgumentException(
					"naziv postavke ne moze biti null ili prazan string");

		if (dbPostavke.containsKey(naziv)) {
			return dbPostavke.get(naziv);
		}

		String vrijednost = defaultVrijednost;

		PostavkaVO pvo = null;

		try {
			pvo = (PostavkaVO) getPostavke().read(naziv);
			vrijednost = pvo != null ? pvo.getVrijednost() : defaultVrijednost;
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka pri pokusaju citanja postavke", e);
		}

		if (pvo!=null)
		dbPostavke.put(naziv, vrijednost);

		return vrijednost;
	}// getPostavkaDB

	public void saveData() {

		try {
		
			PostavkeBean.setPostavkaDB(TVRTKA_NAZIV,
					this.getTvrtkaNaziv() != null ? this.getTvrtkaNaziv()
							: "?!?!");
			PostavkeBean.setPostavkaDB(TVRTKA_ADRESA,
					this.getTvrtkaAdresa() != null ? this.getTvrtkaAdresa()
							: PRAZAN_STRING);
			PostavkeBean.setPostavkaDB(TVRTKA_BANKA,
					this.getTvrtkaBanka() != null ? this.getTvrtkaBanka() : PRAZAN_STRING);
			PostavkeBean.setPostavkaDB(TVRTKA_RACUN,
					this.getTvrtkaRacun() != null ? this.getTvrtkaRacun() : PRAZAN_STRING);
			PostavkeBean.setPostavkaDB(TVRTKA_MB,
					this.getTvrtkaOIB() != null ? this.getTvrtkaOIB() : PRAZAN_STRING);
			PostavkeBean.setPostavkaDB(TVRTKA_MJESTO_RADA,
					this.getMjestoRada() != null ? this.getMjestoRada()
							: "?!?!");
			PostavkeBean.setPostavkaDB(TVRTKA_TELEFON,
					this.getTvrtkaTelefon() != null ? this.getTvrtkaTelefon()
							: PRAZAN_STRING);
			PostavkeBean.setPostavkaDB(TVRTKA_FAX,
					this.getTvrtkaFax() != null ? this.getTvrtkaFax() : PRAZAN_STRING);
			PostavkeBean.setPostavkaDB(TVRTKA_EMAIL,
					this.getTvrtkaEmail() != null ? this.getTvrtkaEmail() : PRAZAN_STRING);
			PostavkeBean.setPostavkaDB(TVRTKA_SIFRA, PRAZAN_STRING+this.getSifraTvrtke());
			PostavkeBean.setPostavkaDB(TVRTKA_SIFRA_POSLOVNICE, PRAZAN_STRING+this.getSifraPoslovnice());
			
			Font font = getFont();
			PostavkeBean.setPostavkaDB(FONT_NAME, font==null?PRAZAN_STRING:font.getName()); 
			PostavkeBean.setPostavkaDB(FONT_STYLE, font==null?PRAZAN_STRING:""+font.getStyle()); 
			PostavkeBean.setPostavkaDB(FONT_SIZE, font==null?PRAZAN_STRING:""+font.getSize()); 

		} catch (Exception ex) {
			Logger.fatal(
					"Ne ide zapisivanje podataka kod PostavkeBean.saveData(): ",
					ex);
			alert("Ne ide zapisivanje podataka!");
			return;
		} finally {

		}// finally

		// info ("Podaci uspješno zapisani u datoteku! ");
	}// saveData

	public void loadData() {

		try {
			Preferences rootPrefs = Preferences.systemRoot().node(
					LINK_NA_POSTAVKE);
			// Preferences userPrefs = Preferences.userRoot().node(
			// LINK_NA_POSTAVKE);

			String tvrtNaz = getProp(TVRTKA_NAZIV, rootPrefs);
			String tvrtAdr = getProp(TVRTKA_ADRESA, rootPrefs);
			String tvrtBan = getProp(TVRTKA_BANKA, rootPrefs);
			String tvrtRac = getProp(TVRTKA_RACUN, rootPrefs);
			String tvrtMB = getProp(TVRTKA_MB, rootPrefs);
			String mjProd = getProp(TVRTKA_MJESTO_RADA, rootPrefs);
			String tvrtTel = getProp(TVRTKA_TELEFON, rootPrefs);
			String tvrtFax = getProp(TVRTKA_FAX, rootPrefs);
			String tvrtEmail = getProp(TVRTKA_EMAIL, rootPrefs);

			this.setTvrtkaNaziv(tvrtNaz);
			this.setTvrtkaAdresa(tvrtAdr);
			this.setTvrtkaBanka(tvrtBan);
			this.setTvrtkaRacun(tvrtRac);
			this.setTvrtkaOIB(tvrtMB);
			this.setMjestoRada(mjProd);
			this.setTvrtkaTelefon(tvrtTel);
			this.setTvrtkaFax(tvrtFax);
			this.setTvrtkaEmail(tvrtEmail);
			int sifraTvrtke = -1;

			sifraTvrtke = PostavkeBean.getIntPostavkaDb(TVRTKA_SIFRA, -1);
			if (sifraTvrtke == -1) {
				sifraTvrtke = PostavkeBean.getIntPostavkaSustava(TVRTKA_SIFRA,
						0);
				PostavkeBean.setPostavkaDB(TVRTKA_SIFRA, PRAZAN_STRING + sifraTvrtke);
			}

			this.setSifraTvrtke(sifraTvrtke);

			int sifraPoslovnice = -1;

			sifraPoslovnice = PostavkeBean.getIntPostavkaDb(
					TVRTKA_SIFRA_POSLOVNICE, -1);
			if (sifraPoslovnice == -1) {
				sifraPoslovnice = PostavkeBean.getIntPostavkaSustava(
						TVRTKA_SIFRA_POSLOVNICE, 0);
				PostavkeBean.setPostavkaDB(TVRTKA_SIFRA_POSLOVNICE, PRAZAN_STRING
						+ sifraPoslovnice);
			}

			this.setSifraPoslovnice(sifraPoslovnice);
			
			String fontName= PostavkeBean.getPostavkaDB(FONT_NAME, PRAZAN_STRING);
			int fontStyle= PostavkeBean.getIntPostavkaDb(FONT_STYLE, -1);
			int fontSize= PostavkeBean.getIntPostavkaDb(FONT_SIZE,-1);

			
			if (fontName.equals(PRAZAN_STRING))
			{
				this.setFont(null);
			}
			else
			{
				Font fnt = new Font(fontName,fontStyle,fontSize);
				this.setFont(fnt);
			}
			

		} catch (Exception ex) {
			Logger.fatal("Ne ide citanje podataka kod PostavkeBean.load(): ",
					ex);
			alert("Ne ide citanje podataka!");
		} finally {
		}

	}// loadData

	private String getProp(String property, Preferences rootPrefs) {

		String dbp = PostavkeBean.getPostavkaDB(property, PRAZAN_STRING);

		if (dbp.equals(PRAZAN_STRING)) {
			String rp = rootPrefs.get(property, PRAZAN_STRING);

			PostavkeBean.setPostavkaDB(property, rp);

			dbp = rp;
		}

		return dbp;
	}

	private void alert(String poruka) {
		javax.swing.JOptionPane.showMessageDialog(null, poruka, "Upozorenje",
				javax.swing.JOptionPane.ERROR_MESSAGE);
	}

 
	public void setTvrtkaNaziv(String tvrtkaNaziv) {
		this.tvrtkaNaziv = tvrtkaNaziv;
		dbPostavke.clear();
	}

	public void setTvrtkaAdresa(String adresa) {
		this.tvrtkaAdresa = adresa;
		dbPostavke.clear();
	}

	public void setTvrtkaBanka(String banka) {
		this.tvrtkaBanka = banka;
		dbPostavke.clear();
	}

	public void setTvrtkaOIB(String MB) {
		this.tvrtkaMB = MB;
		dbPostavke.clear();
	}

	public void setTvrtkaRacun(String brojRacuna) {
		this.tvrtkaRacun = brojRacuna;
		dbPostavke.clear();
	}

	public void setMjestoRada(String mjesto) {
		this.mjestoRada = mjesto;
		dbPostavke.clear();
	}

	// ---- GETOVI ---

	public String getTvrtkaNaziv() {
		return this.tvrtkaNaziv;
	}

	public String getTvrtkaAdresa() {
		return this.tvrtkaAdresa;
	}

	public String getTvrtkaBanka() {
		return this.tvrtkaBanka;
	}

	public String getTvrtkaOIB() {
		return this.tvrtkaMB;
	}

	public String getTvrtkaRacun() {
		return this.tvrtkaRacun;
	}

	public String getMjestoRada() {
		return this.mjestoRada;
	}

	/**
	 * @return
	 */
	public String getTvrtkaEmail() {
		return tvrtkaEmail;
	}

	/**
	 * @return
	 */
	public String getTvrtkaFax() {
		return tvrtkaFax;
	}

	/**
	 * @return
	 */
	public String getTvrtkaTelefon() {
		return tvrtkaTelefon;
	}

	/**
	 * @param string
	 */
	public void setTvrtkaEmail(String string) {
		tvrtkaEmail = string;
	}

	/**
	 * @param string
	 */
	public void setTvrtkaFax(String string) {
		tvrtkaFax = string;
	}

	/**
	 * @param string
	 */
	public void setTvrtkaTelefon(String string) {
		tvrtkaTelefon = string;
	}

	public int getSifraPoslovnice() {
		return sifraPoslovnice;
	}

	public int getSifraTvrtke() {
		return sifraTvrtke;
	}

	public void setSifraPoslovnice(int i) {
		sifraPoslovnice = i;
	}

	public void setSifraTvrtke(int i) {
		sifraTvrtke = i;
	}

	public static String getTipRacuna() {
		return getPostavkaDB(TIP_RACUNA, PRAZAN_STRING);
	}

	public static void setTipRacuna(String tip) {
		setPostavkaDB(TIP_RACUNA, tip);	
	}

} // klasa PostavkeBean
