/*
 * Created on 2005.04.23
 *
 */
package biz.sunce.dao;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.beans.PostavkeBean;

/**
 * @author asabo updated by dstanic 30-04-2005
 */
public final class DAOFactory {
	//private static String DAO_DB_ADR = "dao_db_adr";
	private static String DAODriverClass;
	private static String connString = null;
	private static String username;
	private static String password;
	private static ConnectionBroker broker;
	private static boolean alive = false;
	private static DAOFactory instance;

	// ------------- DAO Klase
	private DrzavaDAO drzavaDAO;
	private KlijentDAO klijentiDAO;
	private MjestoDAO mjestaDAO;
	// 15.05.05. -asabo-
	private DjelatnikDAO djelatniciDAO;
	private LijecnikDAO lijecniciDAO;
	// 22.05.05. -asabo
	private PregledDAO preglediDAO;
	// 23.05.05. -asabo-
	private KeratometrijaDAO keratomaterijaDAO;
	private SkiaskopijaDAO skiaskopijaDAO;
	private RefraktometarDAO refraktometarDAO;

	// 21.06.05. -asabo-
	private NaocaleDAO naocaleDAO;

	// 30.06.05. -asabo-
	private LeceDAO leceDAO;

	// 21.07.05. -asabo-
	private ArtiklDAO artikliDAO;

	// 29.08.05. -asabo-
	private SlikeDAO slikeDAO;

	// 06.10.05. -asabo- vrste leca
	private VrsteLecaDAO vrsteLecaDAO;

	// 09.10.05. -asabo- proizvodjaci
	private ProizvodjaciDAO proizvodjaci;

	// 30.08.05. -asabo- modul za sinkronizaciju baze podataka, preko njega ide
	// replikacija...
	private SynchModul synchModul;

	// 21.10.05. -asabo- dodane transakcije
	private TransakcijeDAO transakcijeDAO;

	// 08.01.06. -asabo- dodani predlosci
	private PredlosciDAO predlosciDAO;
	// 18.01.06. -asabo- dodane poruke
	private PorukaDAO porukeDAO;

	// 31.01.06. -asabo- dodano
	private TipoviTransakcijaDAO tipoviTransakcija;
	private VrsteRezervnihDijelovaDAO vrsteRezervnihDijelova;

	// 23.02.06. -asabo- dodano
	private PorukeSustavaDAO porukeSustava;

	// 24.02.06. -asabo- dodano
	private PostavkeDAO postavke;

	// 04.03.06. -asabo- dodano
	private PomagaloDAO pomagala;

	// 06.03.06. -asabo- dodano
	private RacunDAO racuni;
	private StavkaRacunaDAO stavke;

	// 08.03.06. -asabo- dodano
	private PoreznaStopaDAO porezneStope;

	// 12.03.06. -asabo- dodano
	private LogiranjeDAO logiranja;

	// 28.03.06. -asabo- dodano
	private HzzoObracunDAO hzzoObracuni;

	// 14.05.06. -asabo- dodano
	private VrstePomagalaDAO vrstePomagala;

	// 28.06.06. -asabo- dodano
	private HzzoIzvjesce hzzoIzvjesce;

	private Hashtable<String, DAOObjekt> DAOObjekti = null;

	// ------------- DAO Klase

	public static String NOT_ALIVE_MESSAGE = "Factory not operational";
	private static String daoSource = "biz.sunce.dao.csc";

	private static void podigniKonekcije() {
		if (daoSource.equals("biz.sunce.dao.csc")) {
			connString = getDAODBAdress();
			// connString+=";create=true";
			if (!connString.startsWith("jdbc:derby://"))
				DAODriverClass = "org.apache.derby.jdbc.EmbeddedDriver";
			else
				DAODriverClass = "org.apache.derby.jdbc.ClientDriver";

			username = "opticar";
			password = "pass";
			try {

				
				// connString+=";create=true";
				broker = new ConnectionBroker(DAODriverClass, connString,
						username, password, 10, 100, null, 60000.0d);
				alive = true;
			} catch (Exception e) {
				System.err.println("Iznimka pri podizanju konekcija: "+e);
				alive = false;
			} catch (Throwable t) {
				alive = false;
				System.err.println("Problem pri podizanju konekcija: "+t);
				System.exit(-1);
			}
		} // if DAOSource je CSC
	}

	static {
		getInstance();

	}


	// vraca link prema bazi podataka
	public final static String getDAODBAdress() {
		if (connString == null) {
			Properties p = System.getProperties();

			String sep = p.getProperty("file.separator");

			String adr = GlavniFrame.getWorkingHomeLocation();

			connString = "jdbc:derby:" + adr + sep + "opticardb" + sep
					+ "optika";

			return connString;
		}
		return connString;
	}// getDAODBAdress

	private DAOFactory() {
		try {

			this.lijecniciDAO = (LijecnikDAO) Class.forName(
					daoSource + ".Lijecnici").newInstance();
			// 22.05.05 -asabo-
			this.preglediDAO = (PregledDAO) Class.forName(
					daoSource + ".Pregledi").newInstance();
			// 23.05.05 -asabo-
			this.keratomaterijaDAO = (KeratometrijaDAO) Class.forName(
					daoSource + ".Keratometrija").newInstance();
			this.skiaskopijaDAO = (SkiaskopijaDAO) Class.forName(
					daoSource + ".Skiaskopija").newInstance();
			this.refraktometarDAO = (RefraktometarDAO) Class.forName(
					daoSource + ".Refraktometar").newInstance();

			// 27.06.05. -asabo-
			this.naocaleDAO = (NaocaleDAO) Class
					.forName(daoSource + ".Naocale").newInstance();
			// 30.06.05. -asabo-
			this.leceDAO = (LeceDAO) Class.forName(daoSource + ".Lece")
					.newInstance();

			this.artikliDAO = (ArtiklDAO) Class.forName(daoSource + ".Artikli")
					.newInstance();

			// 29.08.05. -asabo-
			this.slikeDAO = (SlikeDAO) Class.forName(daoSource + ".Slike")
					.newInstance();

			// 30.08.05. -asabo-
			this.synchModul = (SynchModul) Class.forName(
					daoSource + ".SynchModul").newInstance();

			// 06.10.05. -asabo-
			this.vrsteLecaDAO = (VrsteLecaDAO) Class.forName(
					daoSource + ".VrsteLeca").newInstance();

			// 09.10.05. -asabo-
			this.proizvodjaci = (ProizvodjaciDAO) Class.forName(
					daoSource + ".Proizvodjaci").newInstance();
			// 21.10.05. -asabo-
			this.transakcijeDAO = (TransakcijeDAO) Class.forName(
					daoSource + ".Transakcije").newInstance();

			// 08.01.06. -asabo-
			this.predlosciDAO = (PredlosciDAO) Class.forName(
					daoSource + ".Predlosci").newInstance();

			// 18.01.06. -asabo-
			this.porukeDAO = (PorukaDAO) Class.forName(daoSource + ".Poruke")
					.newInstance();
			DAOFactory.alive = true;
		} catch (ClassNotFoundException cnfe) {
			alive = false;
			cnfe.printStackTrace();
		} catch (IllegalAccessException iae) {
			alive = false;
			iae.printStackTrace();
		} catch (InstantiationException ie) {
			alive = false;
			ie.printStackTrace();
		}
	}// konstruktor

	public static final DAOFactory getInstance() {
		if (instance == null) {
			 synchronized(DAOFactory.class)
			 {
			   if (instance!=null) return instance;
			   
			 podigniKonekcije();
			 instance = new DAOFactory();
			 }			
			}
		return instance;
	}// getInstance

	public DrzavaDAO getDrzava() {
		if (this.drzavaDAO == null) {
			try {
				this.drzavaDAO = (DrzavaDAO) Class.forName(
						daoSource + ".Drzave").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}

		return this.drzavaDAO;
	}

	public DAOObjekt getProizvodjaciProizvoda() {
		final String nazivObjekta = "PROIZVODJACI_PROIZVODA";
		return this.getDAOObjekt(nazivObjekta);
	}

	public ProizvodjaciDAO getProizvodjaci() {
		return this.proizvodjaci;
	}

	public KlijentDAO getKlijenti() {
		if (this.klijentiDAO == null) {
			try {
				this.klijentiDAO = (KlijentDAO) Class.forName(
						daoSource + ".Klijenti").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return this.klijentiDAO;
	}

	// 21.10.05. -asabo- dodano
	public TransakcijeDAO getTransakcije() {
		return this.transakcijeDAO;
	}

	// 18.01.06. -asabo- dodano
	public PorukaDAO getPoruke() {
		return this.porukeDAO;
	}

	// 30.08.05. -asabo- modul za sinkronizaciju podataka
	public SynchModul getSynchModul() {
		return this.synchModul;
	}

	public static final java.sql.ResultSet performQuery(String upit) {
		return performQuery(upit, 0);
	}

	// 08.05.05. -asabo- korisnik je sam zaduzen zatvoriti ResultSet nakon
	// citanja podataka
	public static final java.sql.ResultSet performQuery(String upit, int limit) {
		java.sql.Connection con = null;
		java.sql.ResultSet rs = null;
		java.sql.Statement st = null;
		try {
			con = getConnection();
			int stariLimit = 0;

			st = con.createStatement();
			if (limit > 0) {
				stariLimit = st.getMaxRows();
				st.setMaxRows(limit);
			}

			rs = st.executeQuery(upit);

			// vratiti nazad na staro...
			st.setMaxRows(stariLimit);

		} catch (SQLException e) {
			e.printStackTrace();
			Logger.fatal("Iznimka kod DAOFactory.performQuery. Upit:" + upit, e);
		} catch (Throwable t) {
			System.err.println("Greska sustava pri DAOFactory.performQuery: "
					+ t);
			System.exit(-1);
		} finally {
			// ako zatvorimo statement, zatvorili smo i result set...
			// try{if (st!=null) st.close();}catch(SQLException e){}
			try {
				if (con != null)
					freeConnection(con); 
					
			} catch (SQLException e) {
		 }			
		}
		
		return rs;
	}// performQuery

	public static final void killFactory() 
	{
		instance.destroy();
		instance = null;
	}

	// 21.06.05. -asabo- dodao final (brze pozivanje, nema nadjacavanja)
	public static final java.sql.Connection getConnection() throws SQLException {
		if (!alive)
			throw new SQLException(NOT_ALIVE_MESSAGE);
		java.sql.Connection con = null;

		/*
		 * try{ System.out.println("Idemo po driver: "+DAODriverClass);
		 * Class.forName(DAODriverClass).newInstance(); } catch(Exception e){
		 * System.out.println("Iznimka dao driver: "+e); alive=false;}
		 * 
		 * con=alive?DriverManager.getConnection(connString,username,password):null
		 * ;
		 */
		con = alive && broker != null ? broker.getConnection() : null;

		if (con == null)
			throw new SQLException("Veza prema bazi podataka je null!!!");

		return con;
	}// getConnection

	// 21.06.05. -asabo- dodao final (brze pozivanje)
	public static final void freeConnection(java.sql.Connection c)
			throws SQLException {
		if (!alive)
			throw new SQLException(NOT_ALIVE_MESSAGE);

		if (broker != null && c != null)
			broker.freeConnection(c);
		else if (c != null)
			c.close();
	} // freeConnection

	public static final int performUpdate(String upd) {
		java.sql.Connection con = null;
		java.sql.Statement stmt = null;

		int rez = 0;
		// za slucaj hakiranja...
		if (upd == null || upd.trim().equals(""))
			return rez;

		try {
			con = getConnection();
			stmt = con != null ? con.createStatement() : null;
		} catch (SQLException ex) {
			return 0;
		}

		if (stmt == null)
			return 0;

		try {
			rez = stmt.executeUpdate(upd);
		} catch (SQLException ex) {

			Logger.fatal(
					"DAOFactory.performUpdate() Pogreska pri izvrsavanju update-a: "
							+ ex + " Upit: " + upd, ex);
			return 0;
		} finally {
			try {
				if (stmt != null)
					stmt.close(); stmt=null;
			} catch (SQLException e) {
			}
			try {
				if (con != null)
					DAOFactory.freeConnection(con); con=null;
			} catch (SQLException sqle) {
			}
		}
		return rez;
	} // performUpdate

	// posao se obavlja u dretvi kako bi se sto prije vratilo iz poziva destroy
	// metode
	public void destroy() {
		alive = false;
		new Thread() {
			@Override
			public void run() {
				this.setName("CistacDbBrokera");
				this.setPriority(Thread.MIN_PRIORITY);

				yield();
				if (broker != null)
					try {
						broker.destroy(10);
						yield();
					} catch (SQLException e) {

						e.printStackTrace();
					}
				broker = null;
			}// run
		}.start();
	}// destroy

	public static boolean isAlive() {
		return alive;
	}

	/**
	 * @return
	 */
	public MjestoDAO getMjesta() {
		if (mjestaDAO == null) {
			try {
				this.mjestaDAO = (MjestoDAO) Class.forName(
						daoSource + ".Mjesta").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return mjestaDAO;
	}

	// 15.05.05. -asabo-
	public DjelatnikDAO getDjelatnici() {
		if (this.djelatniciDAO == null) {
			// 15.05.05. -asabo-
			try {
				this.djelatniciDAO = (DjelatnikDAO) Class.forName(
						daoSource + ".Djelatnici").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}

		}
		return this.djelatniciDAO;
	}

	// 15.05.05. -asabo-
	public LijecnikDAO getLijecnici() {
		return lijecniciDAO;
	}

	// 22.05.05. -asabo-
	public PregledDAO getPregledi() {
		return preglediDAO;
	}

	// 08.01.06. -asabo-
	public PredlosciDAO getPredlosci() {
		return predlosciDAO;
	}

	// 23.05.05. -asabo-
	public KeratometrijaDAO getKeratometrija() {
		return keratomaterijaDAO;
	}

	// 23.05.05. -asabo-
	public SkiaskopijaDAO getSkiaskopija() {
		return skiaskopijaDAO;
	}

	public RefraktometarDAO getRefraktometar() {
		return refraktometarDAO;
	}

	// 21.06.05. -asabo-
	public NaocaleDAO getNaocale() {
		return naocaleDAO;
	}

	// 30.06.05. -asabo-
	public LeceDAO getLece() {
		return leceDAO;
	}

	// 21.07.05. -asabo-
	public ArtiklDAO getArtikli() {
		return artikliDAO;
	}

	// 28.08.05. -asabo-
	public SlikeDAO getSlike() {
		return slikeDAO;
	}

	// 06.10.05. -asabo-
	public VrsteLecaDAO getVrsteLeca() {
		return vrsteLecaDAO;
	}

	// 21.06.05. -asabo- dodao final (brze pozivanje)
	public final static boolean isTableEmpty(String tablica)
			throws SQLException {
		String upit = "select * from " + tablica;
		boolean rez = true;
		java.sql.ResultSet rs = null;
		try {
			rs = performQuery(upit);
			if (rs != null && rs.next())
				rez = false; // 07.12.05. -asabo- promjena, bilo je true...
			else
				rez = true; // takodjer - bilo je false...
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
		}
		return rez;
	}// isTableEmpty

	private static PostavkeBean postavkeBean;
	// milijun zapisa po poslovnici trebao bi sprijeciti preklapanje sifara
	// firefighting jest, nije se prije mislilo na poslovnice i replikaciju
	// i rjesenje je lose, ali ne mogu vise necu niakd zavrsiti
	// - bit ce ugradjena kontrola 'prelijevanja' i bit ce logirani svi
	// slucajevi
	private static final int odskokMedjuPoslovnicama = 1000000;

	// za koliko odksace prva sifra kod poslovnice
	public static final int getOdskokMedjuPoslovnicama() {
		return odskokMedjuPoslovnicama;
	}

	private static PostavkeBean getPostavkeBean() {
		if (postavkeBean == null)
			postavkeBean = new PostavkeBean();
		return postavkeBean;

	}

	// 27.12.05. -asabo- s obzirom da je 'sifra' najcesce ime kolone koja drzi
	// sifru
	// naocala postoji metoda koja samo zove dalje sa tim podatkom... da
	// pozivacu bude 'lakse'
	public final static synchronized int vratiSlijedecuSlobodnuSifruZaTablicu(
			String tablica) throws SQLException {
		return vratiSlijedecuSlobodnuSifruZaTablicu(tablica, "sifra");
	}

	// 27.12.05. -asabo- metoda zaduzena vratiti prvu slobodnu sifru za doticnu
	// tablicu
	// tu ce se moci ugraditi logika za kontrolu pristupa, pravila vezana uz
	// politiku poslovnica i slicno
	// zato je metoda automatski proglasena sinkroniziranom, iako u velikoj
	// vecini slucajeva to nije potrebno
	// ali zbog buducnosti odmah je u pocetku tako postavljena
	// - napomena: ako je tablica prazna, vratit ce se nazad broj 1 ! (ne 0... )
	public final static synchronized int vratiSlijedecuSlobodnuSifruZaTablicu(
			String tablica, String kljuc) throws SQLException {

		if (tablica == null || tablica.trim().equals(""))
			throw new SQLException(
					"neispravan naziv tablice pri trazenju slobodne sifre");

		if (kljuc == null || kljuc.trim().equals(""))
			throw new SQLException(
					"neispravan naziv kljuca tablice pri trazenju slobodne sifre");

		int poslovnica = getPostavkeBean().getSifraPoslovnice();

		if (isTableEmpty(tablica))
			return odskokMedjuPoslovnicama * poslovnica + 1;

		final String upit = "select max(" + kljuc + ")+1 from " + tablica + " where "
				+ kljuc + ">" + (odskokMedjuPoslovnicama * poslovnica)
				+ " and " + kljuc + "<"
				+ ((odskokMedjuPoslovnicama) * (poslovnica + 1));
		
		java.sql.ResultSet rs = null;
		int sifra = -1;

		try {
			rs = performQuery(upit);
			if (rs != null && rs.next())
				sifra = rs.getInt(1);
			else
				throw new SQLException(
						"vratiSlijedecuSlobodnuSifruZaTablicu ne vraca nazad ispravnu vrijednost kljuca, iako tablica nije prazna! Upit:\n"
								+ upit);

			if (rs.wasNull())
				sifra = odskokMedjuPoslovnicama * poslovnica + 1; // pocetna
																	// sifra

			// uff ovo se nece teoretski moci dogoditi kroz desetak godina, ali
			// kad se dogodi bit ce problem...
			if (sifra == -1
					|| sifra >= ((odskokMedjuPoslovnicama) * (poslovnica + 1) - 1)) {
				Logger.fatal(
						"DAOFactory.vratiSlijedecuSlobodnuSifru ne moze vratiti ispravnu sifru za tablicu "
								+ tablica
								+ " kljuc:"
								+ kljuc
								+ " sifra:"
								+ sifra, null);
			}// if
		} finally {
			try {
				//Statement st = rs.getStatement(); 
				
				if (rs != null)
					rs.close();
				rs=null;
				
				//if (st!=null) st.close(); st=null;
			} catch (SQLException sqle) {
			}
		}
		return sifra;
	}// vratiSlijedecuSlobodnuSifruZaTablicu

	public TipoviTransakcijaDAO getTipoviTransakcija() {
		if (tipoviTransakcija == null) {
			try {
				this.tipoviTransakcija = (TipoviTransakcijaDAO) Class.forName(
						daoSource + ".TipoviTransakcija").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}

		return tipoviTransakcija;
	}// getTipoviTransakcija

	public VrsteRezervnihDijelovaDAO getVrsteRezervnihDijelova() {
		if (vrsteRezervnihDijelova == null) {
			try {
				this.vrsteRezervnihDijelova = (VrsteRezervnihDijelovaDAO) Class
						.forName(daoSource + ".VrsteRezervnihDijelova")
						.newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return vrsteRezervnihDijelova;
	}// getVrsteRezervnihDijelova

	// 23.02.06. -asabo- dodano
	public PorukeSustavaDAO getPorukeSustava() {
		if (porukeSustava == null) {
			try {
				this.porukeSustava = (PorukeSustavaDAO) Class.forName(
						daoSource + ".PorukeSustava").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return porukeSustava;
	}// getPorukeSustava

	// 24.02.06. -asabo- dodano
	public PostavkeDAO getPostavke() {
		if (postavke == null) {
			try {
				this.postavke = (PostavkeDAO) Class.forName(
						daoSource + ".Postavke").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return postavke;
	}// getPostavke

	// 24.02.06. -asabo- dodano
	public final PomagaloDAO getPomagala() {
		if (pomagala == null) {
			try {
				this.pomagala = (PomagaloDAO) Class.forName(
						daoSource + ".Pomagala").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return pomagala;
	}// getPomagala

	// 06.03.06. -asabo- dodano
	public final RacunDAO getRacuni() {
		if (racuni == null) {
			try {
				this.racuni = (RacunDAO) Class.forName(daoSource + ".Racuni")
						.newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return racuni;
	}// getRacuni

	// 06.03.06. -asabo- dodano
	public StavkaRacunaDAO getStavkeRacuna() {
		if (stavke == null) {
			try {
				this.stavke = (StavkaRacunaDAO) Class.forName(
						daoSource + ".StavkeRacuna").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return stavke;
	}// getStavke

	// 06.03.06. -asabo- dodano
	public final PoreznaStopaDAO getPorezneStope() {
		if (porezneStope == null) {
			try {
				this.porezneStope = (PoreznaStopaDAO) Class.forName(
						daoSource + ".PorezneStope").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return porezneStope;
	}// getStavke

	// 12.03.06. -asabo- dodano
	public LogiranjeDAO getLogiranja() {
		if (logiranja == null) {
			try {
				this.logiranja = (LogiranjeDAO) Class.forName(
						daoSource + ".Logiranja").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return logiranja;
	}// getLogiranja

	// 12.03.06. -asabo- dodano
	public HzzoObracunDAO getHzzoObracuni() {
		if (hzzoObracuni == null) {
			try {
				this.hzzoObracuni = (HzzoObracunDAO) Class.forName(
						daoSource + ".HzzoObracuni").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return hzzoObracuni;
	}// getHzzoObracuni

	// 12.03.06. -asabo- dodano
	public final VrstePomagalaDAO getVrstePomagala() {
		if (vrstePomagala == null) {
			try {
				this.vrstePomagala = (VrstePomagalaDAO) Class.forName(
						daoSource + ".VrstePomagala").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}
		return vrstePomagala;
	}// getVrstePomagala

	// 28.06.06. -asabo- dodano
	public final HzzoIzvjesce getHzzoIzvjesca() {
		if (hzzoIzvjesce == null) {
			try {
				this.hzzoIzvjesce = (HzzoIzvjesce) Class.forName(
						daoSource + ".HzzoIzvjesce").newInstance();
			} catch (ClassNotFoundException cnfe) {
				alive = false;
				cnfe.printStackTrace();
			} catch (IllegalAccessException iae) {
				alive = false;
				iae.printStackTrace();
			} catch (InstantiationException ie) {
				alive = false;
				ie.printStackTrace();
			}
		}// if
		return hzzoIzvjesce;
	}// getHzzoIzvjesca

	// 13.01.07. -asabo- dodatak

	public DAOObjekt getDAOObjekt(String naziv) {
		if (this.DAOObjekti == null)
			this.DAOObjekti = new Hashtable<String,DAOObjekt>(5, 1);

		if (naziv == null)
			return null;

		DAOObjekt daoObjekt = null;

		daoObjekt = this.DAOObjekti.get(naziv);

		if (daoObjekt == null) {
			try {
				daoObjekt = new DAOObjekt(naziv);
				this.DAOObjekti.put(naziv, daoObjekt);
				return daoObjekt;
			} catch (Exception ex) {
				Logger.log("Nastala greska kod instanciranja DAOObjekta '"
						+ naziv + "'", ex);
			}
		}// if

		return daoObjekt;
	}// getDAOObjekt

} // klasa
