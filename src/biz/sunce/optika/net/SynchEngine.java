package biz.sunce.optika.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Konstante;
import biz.sunce.optika.Logger;
import biz.sunce.optika.PrikazivacSinkronizacijskihPoruka;
import biz.sunce.util.EncryptionUtils;
import biz.sunce.util.PKIUtils;
import biz.sunce.util.beans.PostavkeBean;

/**
 * <p>
 * Company: ANSA
 * </p>
 * 
 * @author Ante Sabo
 * @version 1.0
 */

public final class SynchEngine {
	private static String SYNCH_DATABASE = "opticar";
	private static PKIUtils pki, pki2;
	private static final String LATIN1_CHAR_ENC = "iso-8859-1";
	private static int SYNCH_SERVER_PORT = 64735;
	private static String ADRESA_SYNCH_SERVERA = "servlet/SynchServer";

	private static int PROT_OPTOCLUB_PORUKA_KRAJ_KOMUNIKACIJE = -1;

	// streamovi preko kojih ce se obavljati trenutna sinkronizacija
	private static java.io.InputStream in;
	private static java.io.OutputStream out;

	private static java.net.URLConnection con;

	public static boolean zipanje = true;

	private static PrikazivacSinkronizacijskihPoruka prikazivac = null;

	private static String serverAdr = null;

	// pri kreiranju popisa paziti na raspored tablica, prvo nosive, onda
	// 'ovisnici'...
	private static String[] osnovniPopisTablica = { "DJELATNICI", "LIJECNICI",
			"KLIJENTI", "IZDANE_LECE", "IZDANE_NAOCALE", "PREGLEDI", "PORUKE",
			"TRANSAKCIJE", // razni zahtjevi za karticama, rezervnim dijelovima
							// i sl.
			"SYSTEM_LOG" };

	// dodatni popis tablica koje se trebaju sinkronizirati u slucaju kad treba
	// prebacivati podatke po poslovnicama
	private static String[] preglediPopisTablica = { "DIOPTRIJA_NAOCALA",
			"DIOPTRIJA_LECA", "KERATOMERIJA", "SKIASKOPIJA", "REFRAKTOMETAR", };

	private static String[] tablice;

	static {
		if (!PostavkeBean.getPostavkaDB(
				Konstante.RAZMJENA_PODATAKA_O_REGLEDIMA_POSTAVKA, DAO.NE)
				.equals(DAO.DA))
			tablice = osnovniPopisTablica;
		else {
			// spajanje dvaju arraya u jedno
			tablice = new String[osnovniPopisTablica.length
					+ preglediPopisTablica.length];
			for (int i = 0; i < osnovniPopisTablica.length; i++)
				tablice[i] = osnovniPopisTablica[i];

			for (int i = 0; i < preglediPopisTablica.length; i++)
				tablice[i + osnovniPopisTablica.length] = preglediPopisTablica[i];
		}// else
	}// static

	// popis tablica koje se spremaju u poseban paket sa zahtjevima, te se
	// tablice trebaju moci
	// odmah raspakirati na serveru i one ce se tamo parsirati. Glavni
	// predstavnik je tablica PORUKE
	public static final String[] zahtjevi_tablice = { "PORUKE" };

	private static int PROTOKOL_OPTOCLUB = 201; // to je sifra optoclub
												// protokola

	static int postotak; // koliko je posto sinkronizirano

	static class Tmp {
		private boolean rez;

		public boolean isRez() {
			return rez;
		}

		public void setRez(boolean r) {
			rez = r;
		}
	}

	// zasada samo jedan prikazivac prikazuje stanje, za buduce potrebe metoda
	// se zove dodaj...
	public static void dodajPrikazivacaPoruka(
			PrikazivacSinkronizacijskihPoruka prikazivac) {
		SynchEngine.prikazivac = prikazivac;
	}

	public static final synchronized boolean performSynchronization(
			final String[] synchTablice, final String[] zahtjeviTablice,
			final String username, final String password) {
		return performSynchronization(synchTablice, zahtjeviTablice, username,
				password, null);
	}

	public static final synchronized boolean performSynchronization(
			final String[] synchTablice, final String[] zahtjeviTablice,
			final String username, final String password,
			final java.io.OutputStream stream) {
		boolean rez = true;
		final Tmp lock = new Tmp();

		new Thread() {
			@Override
			public void run() {
				Thread.currentThread().setName("PerformSynchThread");
				boolean rez = false;
				String[] st = null, zt = null;

				if (synchTablice == null)
					st = tablice;
				else
					st = synchTablice;
				if (zahtjeviTablice == null)
					zt = zahtjevi_tablice;
				else
					zt = zahtjeviTablice;

				if (stream == null)
					rez = performSynch(st, zt, username, password);
				else
					rez = performSynchToFile(st, zt, username, password, stream);

				if (!rez)
					Logger.log("Nastao je problem u sinkronizaciji podataka!");

				synchronized (lock) {
					lock.setRez(rez);
					lock.notify();
				}// synch
			}// run
		}.start();

		// synchronized(lock){try{lock.wait();}catch(Exception e){}}

		return lock.isRez();
	}// performSynchronization

	// uzima popis tablica, sinkronizira doticne tablice sa bazom podataka,
	// vraca nazad spakovane podatke kao polje bajtova
	// spremno za prijenos preko neta...
	private static final byte[] spakirajTabliceUPoljeBajtova(String[] tablice)
			throws IOException {
		// ##################### TRPANJE ZAHTJEVA U PAKET
		// ############################
		ByteArrayOutputStream tabBout = new ByteArrayOutputStream();
		GZIPOutputStream tabZout = new GZIPOutputStream(tabBout);
		ObjectOutputStream tabOout = new ObjectOutputStream(tabZout);
		int tablica = tablice.length;
		int brojac = 0;
		try {
			for (int i = 0; i < tablica; i++) {
				int postotak = ((i + 1) * 100) / tablica;
				setPostotak(postotak);

				postaviPoruku("prijenos: " + postotak + "% ->");

				// novi podaci od zadnje sinkronizacije
				List retci = DAOFactory.getInstance().getSynchModul()
						.collectNewData(tablice[i]);

				// kreiranje tablice... samo ako ima smisla sinkati podatke, tj.
				// ako u retcima ima nekakvih podataka
				if (retci != null && retci.size() > 0) {
					Tablica tab = new Tablica(tablice[i]);
					tab.setPodaci(retci);

					// dakle paket ce u sebi nositi zakriptirani niz bajtova u
					// kojem ce biti zipani Object stream
					// u kojem ce se nalaziti niz od X tablica
					tabOout.writeObject(tab);
					tab = null;
				}// ako ima nesto u retcima ...

				retci = null;// sto prije otkaci ga da se memorija sto prije
								// oslobodi

				brojac++;
			}// for i
				// #########################################################
				// KRAJ TRPANJA TABLICA U PAKET #################
			tabOout.flush();
			tabZout.finish();
		} finally {
			try {
				if (tabOout != null)
					tabOout.close();
			} catch (Exception e) {
			}
			try {
				if (tabZout != null)
					tabZout.close();
			} catch (Exception e) {
			}
			try {
				if (tabBout != null)
					tabBout.close();
			} catch (Exception e) {
			}
		}

		return tabBout.toByteArray();
	}// spakirajTabliceUPoljeBajtova

	// metoda je zaduzena za obavljanje sinkronizacije. Vraca nazad poruku o
	// tome jeli sinkronizacija obavljena uspjesno ili ne
	// prima u sebe vektor sa popisom tablica koje se trebaju sinkronizirati
	private static synchronized boolean performSynch(String[] tablice,
			String[] zahtjeviTablice, String username, String pass) {
		boolean rez = true;
		Tablica t = null;
		int tablica = 0, podataka = 0;

		java.sql.Connection cnn = null;
		setPostotak(0);

		// kreiranje parova kljuceva za asinkronu / sinkronu enkripciju
		pki = new PKIUtils();
		pki.generateKeys();

		java.io.ObjectOutputStream oout = null;
		java.io.ObjectInputStream oin = null;

		java.io.ByteArrayInputStream bin = null;
		java.io.ByteArrayOutputStream bout = null;

		java.util.zip.GZIPOutputStream zout = null;
		java.util.zip.GZIPInputStream zin = null;

		java.io.DataOutputStream dout = null;
		java.io.DataInputStream din = null;

		ByteArrayOutputStream tabBout = null;
		java.io.ObjectOutputStream tabOout = null;
		java.util.zip.GZIPOutputStream tabZout = null;

		try {

			postaviPoruku("spajanje...");
			openStreams();

			// oin=new java.io.ObjectInputStream(in);

			int i;
			int brojac = 0;

			// posto GZIP stream, a niti ostali ZIP, Inflater & slicni streamovi
			// se ne mogu tako jednostavno nakaciti na stream koji
			// nije buferiran (citaj: nije datoteka, vec neki servlet na mrezi),
			// postoje odredjeni problemi oko kompresiranja podataka
			// prije slanja, pa se _mora_ napraviti prvo posao u neki byte
			// array, pa zatim slati preko mreze. Posto mi radimo sa 50-ak (do
			// 100)
			// tablica ukupne velicine oko 200MB, bilo bi prerizicno zipati
			// 200MB u memoriju, pa onda to slati preko mreze, tako da cemo
			// posao
			// rezati u 50-ak dijelova (svaka tablica ce imati svoj zip stream
			// koji ce biti uredno unisten poslije prijenosa) sto ce biti manji
			// memorijski sok za server. Najgore su tablice sa slikama naocala
			// koje su svaka velicine cca 50MB, i jos k tome ne daju se zipati
			// Vjerojatno cemo dodati opciju da za takve tablice nema kompresije
			// pa ce sve raditi u direkt modu bez memorije izmedju.

			if (out == null) {
				postaviPoruku("Spajanje nije moguæe!");
				Logger.log("Output stream je prazan kod pokusaja sinkronizacije podataka! ");
				return false;
			}

			dout = new java.io.DataOutputStream(out);

			oout = new java.io.ObjectOutputStream(dout);

			// try{oout.close();}catch(Exception e){}

			postaviPoruku("rukovanje...");

			// prvo serveru saljemo svoj javni kljuc. Serverov je zadatak
			// kreirati tajni (sinkroni) kljuc i zakodirati ga sa primljenim
			// javnim kljucem
			try {
				oout.writeObject(pki.getPublicKey());
			} catch (SocketException socke) {
				Logger.log(
						"Socket Exc kod prenosenja podataka u SynchEngineu ",
						socke);
				return false;
			}

			din = new java.io.DataInputStream(in);
			// oin=new java.io.ObjectInputStream(din);

			// saljemo serveru informaciju o nasem protokolu koji cemo koristiti

			dout.writeInt(PROTOKOL_OPTOCLUB);
			dout.writeInt(podataka); // zasada nebitan podatak

			dout.flush();

			int zl = din.readInt();
			byte[] zak = null;

			if (zl > 0) {
				zak = new byte[zl];
				din.read(zak, 0, zl);
			}

			byte[] kljuc = null;

			kljuc = pki.decrypt(zak);

			EncryptionUtils enc = new EncryptionUtils();

			// 04.10.05. -asabo- promjenio u static(trazilo se)
			// iz desifriranog niza bajtova generira se simetricni kljuc preko
			// kojega dalje ide sva komunikacija . . .
			EncryptionUtils.generateKey(kljuc);

			javax.crypto.SecretKey seckey = EncryptionUtils.getKey();

			EncryptionUtils.generateKey(kljuc);

			byte[] uname = EncryptionUtils.encrypt(username
					.getBytes(LATIN1_CHAR_ENC));
			byte[] passw = EncryptionUtils.encrypt(pass
					.getBytes(LATIN1_CHAR_ENC));
			int ln = uname != null ? uname.length : 0;
			int pln = passw != null ? passw.length : 0;

			postaviPoruku("logiranje...");

			// username / password... prvo ide password, malo da izmjenimo
			// standardnu proceduru
			dout.writeInt(pln);
			if (passw != null)
				dout.write(passw);

			dout.writeInt(ln);
			if (uname != null)
				dout.write(uname);

			byte[] tmpp = EncryptionUtils.encrypt(GlavniFrame.SOFTWARE_VERSION);
			// 16.01.06. -asabo- dodano za potrebe buduceg izmjenjivanja, da se
			// zna koja verzija softvera i koja verzija protokola igraju
			dout.writeInt(tmpp.length);
			dout.write(tmpp);

			tmpp = EncryptionUtils.encrypt(GlavniFrame.PROTOCOL_VERSION);
			dout.writeInt(tmpp.length);
			dout.write(tmpp);

			boolean logiran = false;

			try {
				logiran = din.readBoolean();
			} catch (EOFException eofe) {
				Logger.fatal(
						"EOF exception kod citanja informacije o tome jeli korisnik logiran u SynchEngineu",
						eofe);
				return false;
			}

			if (!logiran) {
				postaviPoruku("logiranje NEUSPJEŠNO!");
				return false;
			} else
				postaviPoruku("logiranje uspješno");

			tablica = tablice.length;

			Collection tablicePolje = new ArrayList(tablica);

			Paket transakcijePaket = new Paket(Paket.TIP_PAKETA_TRANSAKCIJE);
			// 27.02.06. -asabo- treba znati u kojem encodingu idu podaci...
			transakcijePaket
					.setCharacterEncoding(GlavniFrame.getCharEncoding());
			transakcijePaket.setJavaVersion(GlavniFrame.getJavaVersion());
			transakcijePaket.setOs(GlavniFrame.getOS());
			transakcijePaket.setUserLanguage(GlavniFrame.getUserLanguage());

			transakcijePaket.setDjelatnikPoslao(GlavniFrame.getSifDjelatnika());
			transakcijePaket.setSifPoslovnice(GlavniFrame
					.getSifraPoslovniceZaSynch());
			transakcijePaket.setSifTvrtke(GlavniFrame.getSifraTvrtkeZaSynch());

			Paket zahtjeviPaket = new Paket(Paket.TIP_PAKETA_ZAHTJEVI);

			zahtjeviPaket.setDjelatnikPoslao(GlavniFrame.getSifDjelatnika());
			zahtjeviPaket.setSifPoslovnice(GlavniFrame
					.getSifraPoslovniceZaSynch());
			zahtjeviPaket.setSifTvrtke(GlavniFrame.getSifraTvrtkeZaSynch());
			zahtjeviPaket.setCharacterEncoding(GlavniFrame.getCharEncoding());
			zahtjeviPaket.setJavaVersion(GlavniFrame.getJavaVersion());
			zahtjeviPaket.setOs(GlavniFrame.getOS());
			zahtjeviPaket.setUserLanguage(GlavniFrame.getUserLanguage());

			int zapisa = 0; // koliko sveukupno ima zapisa u svim tablicama...
							// ako je ==0, necemo sinkati paket, a svaka tablica
							// koja bude imala svoj broj zapisa ==0, necemo ju
							// staviti na listu

			tabBout = new ByteArrayOutputStream();
			tabZout = new GZIPOutputStream(tabBout);
			tabOout = new ObjectOutputStream(tabZout);

			byte[] tabliceBajtovi = null;

			if (logiran)
				tabliceBajtovi = spakirajTabliceUPoljeBajtova(tablice);

			pki2 = new PKIUtils();
			pki2.setOtherPublicKey(GlavniFrame.getPublicKey(), true); // false =
																		// nacin
																		// na
																		// koji
																		// se
																		// niz
																		// bajtova
																		// pretvara
																		// u
																		// pubKey,
																		// preko
																		// ObjectStreama,
																		// a
																		// true
																		// kroz
																		// javin
																		// mehanizam
			// posto ne mozemo javnim kljucem zakriptirati cijeli dokument,
			// kriptiramo samo simetricni kljuc i saljemo ga zajedno sa paketom
			// na odrediste
			// dok dokument kriptiramo sa simetricnim kljucem. Na taj nacin
			// dokument je zasticen koliko postoji mogucnost kradje simetricnog
			// kljuca..
			transakcijePaket.setKljucEnkripcije(pki2.encrypt(EncryptionUtils
					.getKey().getEncoded()));
			// iako je simetricna enkripcija, moze raspakovati samo vlasnik
			// privatnog kljuca sa kojim ce 'otkljucati' simetricni kljuc koji
			// se nalazi u Paketu
			// server koji zaprima pakete IMA mogucnost desifriranja podataka
			// unutar paketa, ali samo taj trenutak razmjene podataka, jer je u
			// posjedu simetricnog kljuca (u memoriji)
			// sto mozemo smatrati prednoscu (recimo mogucnost kriptiranja i
			// paketa sa porukama), ali i sigurnosnim propustom (valjda postoji
			// neka fora kako hacker moze ukrasti iz memorije kljuc)
			tabliceBajtovi = EncryptionUtils.encrypt(tabliceBajtovi);
			transakcijePaket.setPodaci(tabliceBajtovi);

			// ovi se paketi takodjer otpakiravaju na nekom racunalu koje
			// posjeduje private key...
			zahtjeviPaket.setKljucEnkripcije(pki2.encrypt(EncryptionUtils
					.getKey().getEncoded()));
			// sve odjednom :)
			zahtjeviPaket.setPodaci(EncryptionUtils
					.encrypt(spakirajTabliceUPoljeBajtova(zahtjeviTablice)));

			Calendar trenutakSinkronizacije = Calendar.getInstance();
			transakcijePaket.setTrenutakSlanja(trenutakSinkronizacije
					.getTimeInMillis());
			zahtjeviPaket.setTrenutakSlanja(trenutakSinkronizacije
					.getTimeInMillis());

			tabOout = null; // sve panicno odmah cistiti, bit ce problema sa
							// memorijom..
			tabZout = null;
			tabBout = null;
			tabliceBajtovi = null;

			// jos jedanput pakiranje Paketa u niz bajtova u svrhu kriptiranja,
			// ali ovaj puta simetricnog
			tabBout = new ByteArrayOutputStream();
			tabZout = new GZIPOutputStream(tabBout);
			tabOout = new ObjectOutputStream(tabZout);

			tabOout.writeObject(transakcijePaket);
			tabOout.writeObject(zahtjeviPaket);

			try {
				if (tabOout != null)
					tabOout.close();
			} catch (Exception e) {
			}
			try {
				if (tabZout != null)
					tabZout.close();
			} catch (Exception e) {
			}
			try {
				if (tabBout != null)
					tabBout.close();
			} catch (Exception e) {
			}

			byte podaci[] = tabBout != null ? tabBout.toByteArray() : null;

			tabOout = null; // sve panicno odmah cistiti, bit ce problema sa
							// memorijom..
			tabZout = null;
			tabBout = null;
			zahtjeviPaket = null;
			transakcijePaket = null; // teski objekti koje bi trebalo sto prije
										// otkaciti van iz memorije

			// podaci se zakodiravaju simetricnim tajnim kljucem prije slanja
			// serveru, a poslije zipanja
			// (kad bi se podaci prvo sifrirali, pa zipali, zipanje ne bi bilo
			// kvalitetno obavljeno zbog vece entropije kodne abecede)
			podaci = EncryptionUtils.encrypt(podaci);

			int komada = podaci != null ? podaci.length : 0;

			// gotovo je sa pakiranjem tablica, sada treba paket poslati preko
			// interneta...

			// zout=new GZIPOutputStream(out);
			// oout=new java.io.ObjectOutputStream(out);
			// oout.useProtocolVersion(java.io.ObjectStreamConstants.PROTOCOL_VERSION_2);

			setPostotak(0);
			// prvo koliko bajtova dolazi
			dout.writeInt(komada);

			// zatim i sami podaci
			dout.write(podaci, 0, komada);

			// System.out.println("Zapisani podaci velicine: "+podaci.length);
			podaci = null;
			transakcijePaket = null; // oslobodi se tereta...

			// server ce nam vratiti nazad jeli uspjesno obavljena
			// sinkronizacija
			boolean sinkronizirano = false;

			// zajebava nas server sa svojim slanjem boolean vrijednosti, pa
			// saljemo nesto sto je veliko 8 bajtova...
			sinkronizirano = din.readBoolean();
			long datVrijeme = din.readLong();
			postaviPoruku(sinkronizirano ? "uspješno poslano"
					: "neuspješno poslano");

			if (sinkronizirano) {
				PostavkeBean.setPostavkaSustava(
						Konstante.VRIJEME_ZADNJE_SINKRONIZACIJE, ""
								+ datVrijeme); // da se zna kad smo zadnji puta
												// sinkali uspjesno
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(datVrijeme);

				DAOFactory.getInstance().getSynchModul()
						.registerSuccesfulSynchronization(c);

			}// if uspjesna sinkronizacija

			// ovdje ce lezati informacija jesmo li uspjesno pohranili podatke
			// lokalno
			boolean pohranjeno = false;

			int komadaNazad = 0;

			komadaNazad = din.readInt();

			if (komadaNazad == PROT_OPTOCLUB_PORUKA_KRAJ_KOMUNIKACIJE)
				return true;

			podaci = null;
			// System.out.println("Komada nazad: "+komadaNazad+" uspjesno obavljena sinkronizacija? "+sinkronizirano);

			while (komadaNazad > 0) {
				podaci = new byte[komadaNazad];

				int procitano = 0;

				postaviPoruku("uèitavanje poruka");

				while (procitano < komadaNazad) {
					procitano += din.read(podaci, procitano,
							(komadaNazad - procitano));
				}

				podaci = EncryptionUtils.decrypt(podaci);

				bin = new java.io.ByteArrayInputStream(podaci);
				zin = new java.util.zip.GZIPInputStream(bin);
				oin = new java.io.ObjectInputStream(zin);

				try {
					while (oin.available() == 0) {
						transakcijePaket = (Paket) oin.readObject();

						// try{pohranjeno=t.pohraniPodatkeUBazu(cnn);}
						// finally{db.freeConnection(cnn);}
						if (transakcijePaket != null
								&& transakcijePaket.getTipPaketa() == Paket.TIP_PAKETA_SISTEMSKE_TABLICE) {
							postaviPoruku("spremanje podataka...");
							spremiTabliceUBazuPodataka(podaci); // 08.05.07.
																// -asabo-
																// trebalo je
																// doraditi da
																// to bude
																// posebna
																// metoda
							postaviPoruku("spremanje podataka gotovo!");
						}// if
						else
							pohranjeno = false;

					}// while oin.available()==0 //svi transakcijski paketi se
						// kroz petlju raspetljavaju...
				} catch (EOFException eofe) {
					// eof se mora dogoditi, ne znamo drugacije koliko podataka
					// ima...
				} finally {
					// System.out.println("Vratila se nazad tablica "+t.getNaziv());
					try {
						if (oin != null)
							oin.close();
					} catch (java.io.IOException ioe) {
					}
					try {
						if (zin != null)
							zin.close();
					} catch (java.io.IOException ioe) {
					}
					try {
						if (bin != null)
							bin.close();
					} catch (java.io.IOException ioe) {
					}
				}

				// tablica se uspjesno ucitala nazad, trebamo poslati poruku
				// nazad da je sve ok
				dout.writeBoolean(pohranjeno);

				komadaNazad = din.readInt(); // citamo koliko komada ima dalje
				if (komadaNazad == PROT_OPTOCLUB_PORUKA_KRAJ_KOMUNIKACIJE)
					break;
			}// while

			postaviPoruku("sinkronizacija uspješno obavljena");

			// ako je sve proslo ok, izravnavamo sve podatke
			// if (pohranjeno && sinkronizirano)
			// try{cnn=db.getConnection();
			// t.izravnajSveIndVal(cnn);}finally{db.freeConnection(cnn);}

			// SynchEngine.setPoruka("(besposlen)"); // prebacena tablica, pa
			// brisemo ju sa popisa...
			dout.flush();
			rez = true; // sve je proslo ok!!!!
			// if (napredak!=null) napredak.dispose();

			try {
				if (oout != null)
					oout.close();
			} catch (Exception e) {
			}
			try {
				if (zout != null)
					zout.close();
			} catch (Exception e) {
			}
			try {
				if (bout != null)
					bout.close();
			} catch (Exception e) {
			}
			oout = null;
			zout = null;
			bout = null;
			postaviPoruku("Sinkronizacija završena...");
		} catch (java.io.IOException ioe) {
			Logger.log("IOExc kod prenosenja podataka u SynchEngineu ", ioe);
			postaviPoruku("IO problem...");
			return false;
		} catch (java.lang.ClassNotFoundException cnfe) {
			Logger.log("CNFExc kod prenosenja podataka u SynchEngineu", cnfe);
			postaviPoruku("CNF problem! Kontaktirati službu!");
			return false;
		} catch (java.lang.Error err) {
			Logger.log("Greska sustava kod prenosenja podataka u SynchEngineu",
					err);
			postaviPoruku("Greška sustava! Kontaktirajte službu!");
			return false;
		} finally {
			closeStreams();
			try {
				if (dout != null)
					dout.close();
			} catch (Exception e) {
			}
			try {
				if (oout != null)
					oout.close();
			} catch (Exception e) {
			}
			try {
				if (zout != null)
					zout.close();
			} catch (Exception e) {
			}
			try {
				if (bout != null)
					bout.close();
			} catch (Exception e) {
			}

			try {
				if (tabOout != null)
					tabOout.close();
			} catch (Exception e) {
			}
			try {
				if (tabZout != null)
					tabZout.close();
			} catch (Exception e) {
			}
			try {
				if (tabBout != null)
					tabBout.close();
			} catch (Exception e) {
			}
		}
		return rez;
	}// performSynch

	// prima podatke iz kojih se citaju tablice i po odredjenom redosljedu
	// spremaju u bazu podataka
	private static final void spremiTabliceUBazuPodataka(byte[] pod) {
		Tablica tabl = null;

		java.io.ByteArrayInputStream bin2 = null;
		java.util.zip.GZIPInputStream zin2 = null;
		java.io.ObjectInputStream oin2 = null;

		Hashtable prebaceneTablice = new java.util.Hashtable();
		try {
			bin2 = new java.io.ByteArrayInputStream(pod);
			zin2 = new java.util.zip.GZIPInputStream(bin2);
			oin2 = new java.io.ObjectInputStream(zin2);

			// prvo pregledati tablice po prioritetu updatea, dakle sa osnovnog
			// popisa idemo
			// po redu updateati onako kako popis kaze.. na kraju preostale
			//dakle za svaku od nosivih tablica proci kroz sve zapise i kad se nadje tablica, azurirati ju
			for (int j = 0; j < osnovniPopisTablica.length; j++) {
				Object temp = null;
				try {
					while (oin2.available() == 0) {
						temp = oin2.readObject();
						tabl = (Tablica) temp;

						if (tabl != null
								&& tabl.getNaziv().equals(
										osnovniPopisTablica[j])) {
							DAOFactory.getInstance().getSynchModul()
									.updateSystemTable(tabl);
							System.out.println(j + ". Ide tabla: "
									+ tabl.getNaziv());
							prebaceneTablice.put(tabl.getNaziv(), tabl); // zabiljeziti
																			// da
																			// je
																			// isla..
						break; // nasli smo tablicu, ne treba dalje kroz podatke za ovaj prolaz
						}
					}// while kroz sve podatke
				} catch (ClassCastException cce) {
					StackTraceElement[] ste = cce.getStackTrace();
					String greska = ste != null && ste.length > 0 ? ste[0]
							.getFileName() + ":" + ste[0].getLineNumber()
							: "(nema strace)";
					Logger.fatal("ClassCast kod sinkronizacije obj:" + temp
							+ " stc:" + greska, cce);
				} catch (EOFException eof) {
				}

				oin2.close();
				zin2.close();
				bin2.close();
				bin2 = new java.io.ByteArrayInputStream(pod);
				zin2 = new java.util.zip.GZIPInputStream(bin2);
				oin2 = new java.io.ObjectInputStream(zin2);
			}// for j

			while (oin2.available() == 0) {
				tabl = (Tablica) oin2.readObject();
				if (tabl != null
						&& !prebaceneTablice.containsKey(tabl.getNaziv())) {
					DAOFactory.getInstance().getSynchModul()
							.updateSystemTable(tabl);
					System.out.println("naknadno " + tabl.getNaziv());
				}
				postaviPoruku("tablica  azurirana");

			}// while

		} catch (IOException ioe) {
			if (ioe != null && !(ioe instanceof EOFException))
				Logger.fatal(
						"IO iznimka kod spremanja tablica u bazu podataka (bajtovi)",
						ioe);
		} catch (ClassNotFoundException cnfe) {
			Logger.fatal(
					"ClassNotFound?!? ...kod spremanja tablica u bazu podataka (bajtovi)",
					cnfe);
		} finally {
			try {
				if (oin2 != null)
					oin2.close();
			} catch (java.io.IOException ioe) {
			}
			try {
				if (zin2 != null)
					zin2.close();
			} catch (java.io.IOException ioe) {
			}
			try {
				if (bin2 != null)
					bin2.close();
			} catch (java.io.IOException ioe) {
			}
			if (prebaceneTablice != null)
				prebaceneTablice.clear();
			prebaceneTablice = null;
		}

	}// spremiTabliceUBazu

	public static synchronized boolean performSynchFromFile(java.io.File file) {
		boolean rez = true;
		Tablica t = null;
		int tablica = 0, podataka = 0;
		FileInputStream in = null;
		if (file == null)
			return false;

		setPostotak(0);

		java.io.ObjectInputStream tabOIn = null;
		java.util.zip.GZIPInputStream tabZin = null;

		try {
			int i = 0;
			int brojac = 0;
			in = new FileInputStream(file);

			// posto GZIP stream, a niti ostali ZIP, Inflater & slicni streamovi
			// se ne mogu tako jednostavno nakaciti na stream koji
			// nije buferiran (citaj: nije datoteka, vec neki servlet na mrezi),
			// postoje odredjeni problemi oko kompresiranja podataka
			// prije slanja, pa se _mora_ napraviti prvo posao u neki byte
			// array, pa zatim slati preko mreze. Posto mi radimo sa 50-ak (do
			// 100)
			// tablica ukupne velicine oko 200MB, bilo bi prerizicno zipati
			// 200MB u memoriju, pa onda to slati preko mreze, tako da cemo
			// posao
			// rezati u 50-ak dijelova (svaka tablica ce imati svoj zip stream
			// koji ce biti uredno unisten poslije prijenosa) sto ce biti manji
			// memorijski sok za server. Najgore su tablice sa slikama naocala
			// koje su svaka velicine cca 50MB, i jos k tome ne daju se zipati
			// Vjerojatno cemo dodati opciju da za takve tablice nema kompresije
			// pa ce sve raditi u direkt modu bez memorije izmedju.

			if (in == null) {
				postaviPoruku("Ulazni tok podataka nije ispravan!");
				Logger.log("Input stream je prazan kod pokusaja sinkronizacije podataka! ");
				return false;
			}

			tabZin = new GZIPInputStream(in);
			tabOIn = new ObjectInputStream(tabZin);

			Paket transakcijePaket = (Paket) tabOIn.readObject();
			Paket zahtjeviPaket = (Paket) tabOIn.readObject();

			try {
				if (tabOIn != null)
					tabOIn.close();
			} catch (Exception e) {
			}
			try {
				if (tabZin != null)
					tabZin.close();
			} catch (Exception e) {
			}

			tabOIn = null; // sve panicno odmah cistiti, bit ce problema sa
							// memorijom..
			tabZin = null;

			tablica = tablice.length;

			// Collection tablicePolje=new ArrayList(tablica);

			int zapisa = 0; // koliko sveukupno ima zapisa u svim tablicama...
							// ako je ==0, necemo sinkati paket, a svaka tablica
							// koja bude imala svoj broj zapisa ==0, necemo ju
							// staviti na listu

			byte[] tabliceBajtovi = null;

			tabliceBajtovi = transakcijePaket.getPodaci();
			spremiTabliceUBazuPodataka(tabliceBajtovi);
			transakcijePaket = null;

			tabliceBajtovi = zahtjeviPaket.getPodaci();
			spremiTabliceUBazuPodataka(tabliceBajtovi);

			tabliceBajtovi = null;
			zahtjeviPaket = null;

			postaviPoruku("Uèitavanje podataka završeno...");
			rez = true;
		} catch (Exception e) {
			Logger.fatal("Iznimka kod sinkronizacije podataka iz  datoteke", e);
			rez = false;
		} finally {
			try {
				if (tabOIn != null)
					tabOIn.close();
			} catch (Exception e) {
			}
			try {
				if (tabZin != null)
					tabZin.close();
			} catch (Exception e) {
			}
		}
		return rez;
	}// performSynchFromFile

	private static synchronized boolean performSynchToFile(String[] tablice,
			String[] zahtjeviTablice, String username, String pass,
			java.io.OutputStream out) {
		boolean rez = true;
		Tablica t = null;
		int tablica = 0, podataka = 0;

		setPostotak(0);

		java.io.ObjectOutputStream tabOout = null;
		java.util.zip.GZIPOutputStream tabZout = null;
		try {
			int i;
			int brojac = 0;

			// posto GZIP stream, a niti ostali ZIP, Inflater & slicni streamovi
			// se ne mogu tako jednostavno nakaciti na stream koji
			// nije buferiran (citaj: nije datoteka, vec neki servlet na mrezi),
			// postoje odredjeni problemi oko kompresiranja podataka
			// prije slanja, pa se _mora_ napraviti prvo posao u neki byte
			// array, pa zatim slati preko mreze. Posto mi radimo sa 50-ak (do
			// 100)
			// tablica ukupne velicine oko 200MB, bilo bi prerizicno zipati
			// 200MB u memoriju, pa onda to slati preko mreze, tako da cemo
			// posao
			// rezati u 50-ak dijelova (svaka tablica ce imati svoj zip stream
			// koji ce biti uredno unisten poslije prijenosa) sto ce biti manji
			// memorijski sok za server. Najgore su tablice sa slikama naocala
			// koje su svaka velicine cca 50MB, i jos k tome ne daju se zipati
			// Vjerojatno cemo dodati opciju da za takve tablice nema kompresije
			// pa ce sve raditi u direkt modu bez memorije izmedju.

			if (out == null) {
				postaviPoruku("Izlazni tok podataka nije ispravan!");
				Logger.log("Output stream je prazan kod pokusaja sinkronizacije podataka! ");
				return false;
			}

			boolean logiran = false;

			tablica = tablice.length;

			// Collection tablicePolje=new ArrayList(tablica);

			Paket transakcijePaket = new Paket(Paket.TIP_PAKETA_TRANSAKCIJE);
			// 27.02.06. -asabo- treba znati u kojem encodingu idu podaci...
			transakcijePaket
					.setCharacterEncoding(GlavniFrame.getCharEncoding());
			transakcijePaket.setJavaVersion(GlavniFrame.getJavaVersion());
			transakcijePaket.setOs(GlavniFrame.getOS());
			transakcijePaket.setUserLanguage(GlavniFrame.getUserLanguage());

			transakcijePaket.setDjelatnikPoslao(GlavniFrame.getSifDjelatnika());
			transakcijePaket.setSifPoslovnice(GlavniFrame
					.getSifraPoslovniceZaSynch());
			transakcijePaket.setSifTvrtke(GlavniFrame.getSifraTvrtkeZaSynch());

			Paket zahtjeviPaket = new Paket(Paket.TIP_PAKETA_ZAHTJEVI);

			zahtjeviPaket.setDjelatnikPoslao(GlavniFrame.getSifDjelatnika());
			zahtjeviPaket.setSifPoslovnice(GlavniFrame
					.getSifraPoslovniceZaSynch());
			zahtjeviPaket.setSifTvrtke(GlavniFrame.getSifraTvrtkeZaSynch());
			zahtjeviPaket.setCharacterEncoding(GlavniFrame.getCharEncoding());
			zahtjeviPaket.setJavaVersion(GlavniFrame.getJavaVersion());
			zahtjeviPaket.setOs(GlavniFrame.getOS());
			zahtjeviPaket.setUserLanguage(GlavniFrame.getUserLanguage());

			int zapisa = 0; // koliko sveukupno ima zapisa u svim tablicama...
							// ako je ==0, necemo sinkati paket, a svaka tablica
							// koja bude imala svoj broj zapisa ==0, necemo ju
							// staviti na listu

			tabZout = new GZIPOutputStream(out);
			tabOout = new ObjectOutputStream(tabZout);

			byte[] tabliceBajtovi = null;

			tabliceBajtovi = spakirajTabliceUPoljeBajtova(tablice);
			transakcijePaket.setPodaci(tabliceBajtovi);

			zahtjeviPaket
					.setPodaci(spakirajTabliceUPoljeBajtova(zahtjeviTablice));

			Calendar trenutakSinkronizacije = Calendar.getInstance();
			transakcijePaket.setTrenutakSlanja(trenutakSinkronizacije
					.getTimeInMillis());
			zahtjeviPaket.setTrenutakSlanja(trenutakSinkronizacije
					.getTimeInMillis());

			tabOout.writeObject(transakcijePaket);
			tabOout.writeObject(zahtjeviPaket);

			try {
				if (tabOout != null)
					tabOout.close();
			} catch (Exception e) {
			}
			try {
				if (tabZout != null)
					tabZout.close();
			} catch (Exception e) {
			}

			tabOout = null; // sve panicno odmah cistiti, bit ce problema sa
							// memorijom..
			tabZout = null;
			zahtjeviPaket = null;
			transakcijePaket = null; // teski objekti koje bi trebalo sto prije
										// otkaciti van iz memorije

			// gotovo je sa pakiranjem tablica, sada treba paket poslati preko
			// interneta...

			// zout=new GZIPOutputStream(out);
			// oout=new java.io.ObjectOutputStream(out);
			// oout.useProtocolVersion(java.io.ObjectStreamConstants.PROTOCOL_VERSION_2);

			postaviPoruku("Spremanje podataka završeno...");
			rez = true;
		} catch (Exception e) {
			Logger.fatal("Iznimka kod sinkronizacije podataka u datoteku", e);
			rez = false;
		} finally {
			try {
				if (tabOout != null)
					tabOout.close();
			} catch (Exception e) {
			}
			try {
				if (tabZout != null)
					tabZout.close();
			} catch (Exception e) {
			}
		}
		return rez;
	}// performSynchToFile

	// ako ce eventualno biti kakvo graficko sucelje preko kojega ce korisnik
	// moci pratiti proceduru
	// sinkronizacije, onda ce se tom sucelju poruke odavde proslijedjivati
	private static void postaviPoruku(String poruka) {
		System.out.println(poruka);
		if (prikazivac != null)
			prikazivac.postaviPoruku(poruka);
	}

	// ima za zadatak spojiti se sa serverom, obaviti sve potrebne pregovore,
	// zakriptirati i/ili zazipati
	// komunikaciju (ili pak nista od toga), te na kraju vratiti obican output
	// Stream na koji se moze nakaciti
	// ObjectOutputStream ili nesto trece po potrebi
	private static void openStreams() {
		String lokalniServer = "127.0.0.1";
		String police = "police.hr";

		serverAdr = PostavkeBean.getPostavkaDB(Konstante.POSTAVKE_SYNCH_SERVER,
				police);// da se softveri mogu lako preseliti na drugi server
		String adresa = serverAdr + "/" + ADRESA_SYNCH_SERVERA;

		java.net.URL url = null;
		java.net.Socket s = null;

		try {
			String adr = "http://" + adresa;

			// prije spajanja sa serverom treba ga cimnuti kako bi se podigao
			// potreban servis
			if (true) {
				try {
					String enc = "";

					if (false) {
						java.io.ByteArrayOutputStream baout = new java.io.ByteArrayOutputStream();
						java.io.ObjectOutputStream oout = new java.io.ObjectOutputStream(
								baout);

						oout.writeObject(SynchEngine.pki.getPublicKey());

						try {
							if (oout != null)
								oout.close();
						} catch (java.io.IOException ioe) {
						}
						try {
							if (baout != null)
								baout.close();
						} catch (java.io.IOException ioe) {
						}

						enc = URLEncoder.encode(
								new String(baout.toByteArray()),
								LATIN1_CHAR_ENC);

						adr += "?k=" + enc;
					}// if

					url = new java.net.URL(adr);

					java.net.URLConnection con = url.openConnection();

					postaviPoruku("Otvaranje veze...");

					if (con != null) {

						con.setUseCaches(false);
						con.setDefaultUseCaches(false);

						con.setRequestProperty("Content-type",
								"application/octet-stream");

						try {
							con.setDoOutput(true);
							con.setDoInput(true);

							con.connect();
						} catch (java.io.IOException ioe) {
							Logger.log(
									"Synch IOException kod otvaranja http veze: ",
									ioe);
						}

						try {
							if (con != null)
								con.getInputStream();
						} catch (Exception e) {
						}
					}// if

					// malo spavamo...
					// try{Thread.currentThread().sleep(1000);}catch(Exception
					// e){}
					// e, probaj sada se socketirati...
				} finally {
					con = null;
					url = null;
				}
			}// if true

			// poslije cimanja bi trebali moci otvoriti socket...
			s = new java.net.Socket(serverAdr, SYNCH_SERVER_PORT);

			// System.out.println("socket receive buffer: "+s.getReceiveBufferSize()+" keep: "+s.getKeepAlive());
			// s.setReceiveBufferSize(2048);

			out = s.getOutputStream();
			in = s.getInputStream();

		} catch (MalformedURLException ex) {
			Logger.log("Greska kod otvaranja out veze: " + ex);
			return;
		} catch (java.io.IOException ioe) {
			Logger.log("IOException kod otvaranja out veze: " + ioe);
			return;
		} catch (Exception e) {
			Logger.log("Exception kod otvaranja out veze: " + e);
			return;
		}
	}// openStreams

	private static void closeStreams() {
		try {
			if (in != null)
				in.close();
		} catch (java.io.IOException ioe) {
		}
		try {
			if (out != null)
				out.close();
		} catch (java.io.IOException ioe) {
		}

		// javljamo serveru da si moze spustiti servis, za slucaj da je doticni
		// zapeo po bilo kakvoj osnovi
		if (true) {
			String police = "police.hr";
			String serverAdr = PostavkeBean.getPostavkaDB(
					Konstante.POSTAVKE_SYNCH_SERVER, police);// da se softveri
																// mogu lako
																// preseliti na
																// drugi server

			String adresa = "http://" + serverAdr + "/" + ADRESA_SYNCH_SERVERA;
			final String adr = adresa + "?stat=gotov";

			new Thread() {
				@Override
				public void run() {
					try {
						java.net.URL url = new java.net.URL(adr);

						java.net.URLConnection con = url != null ? url
								.openConnection() : null;

						if (con != null) {
							con.setUseCaches(false);
							con.setDefaultUseCaches(false);

							// con.setRequestMethod("POST");
							con.setRequestProperty("Content-type",
									"application/octet-stream");
							con.connect();
							try {
								con.getInputStream();
							} catch (Exception e) {
							} // samo da potegne nekakve podatke...
						}// if
					} catch (Exception e) {
						Logger.log("Iznimka kod pokusaja spustanja servera: ",
								e);
					}
				}// run
			}.start();
		}// if
	}// closeStreams

	// 10.10.05. -asabo- raspakira paket i sve tablice u paketu posalje na
	// azuriranje lokalnog sustava
	private void parsirajSistemskiPaket(Paket paket) {
		try {
			ByteArrayInputStream bin = new java.io.ByteArrayInputStream(
					paket.getPodaci());
			GZIPInputStream zin = new java.util.zip.GZIPInputStream(bin);
			ObjectInputStream oin = new java.io.ObjectInputStream(zin);

			// ovo zna biti komad, pa da pomognemo ostalim dretvama doci do daha
			// Thread.currentThread().yield();
			Tablica t = (Tablica) oin.readObject();
			// Thread.currentThread().yield();

			try {
				if (oin != null)
					oin.close();
			} catch (Exception e) {
			}
			try {
				if (zin != null)
					zin.close();
			} catch (Exception e) {
			}
			try {
				if (bin != null)
					bin.close();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			Logger.fatal("Iznimka kod SynchEngine.pasrirajSistemskiPaket", e);
		} finally {

		}
	}// parsirajSistemskiPaket

	public int getPostotak() {
		return postotak;
	}

	private static void setPostotak(int p) {
		postotak = p;
		if (prikazivac != null)
			prikazivac.postaviTrenutnuVrijednost(postotak);
	}
}// klasa
