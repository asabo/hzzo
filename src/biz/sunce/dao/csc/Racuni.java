/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DjelatnikDAO;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.HzzoObracunDAO;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.LijecnikDAO;
import biz.sunce.dao.MjestoDAO;
import biz.sunce.dao.RacunDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

import com.modp.checkdigits.CheckDigit;
import com.modp.checkdigits.CheckISO7064Mod11_10;

/**
 * datum:2006.02.23
 * 
 * @author asabo
 * 
 */
public final class Racuni implements RacunDAO {
	private static final Class<String> STRING_CLASS = String.class;
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica = "racuni";
	private final String kolone[] = { "br.oso.rn.osn.", "datum nar.",
			"datum izd.", "klijent", "lije\u010Dnik", "podr. ured", "akt. ZZR",
			"status" };
	// 31.03.06. -asabo- dodano 6 novih kolona:
	// sif_drzave,broj_iskaznice1,broj_iskaznice2,ino_broj_lista1,ino_broj_lista2
	// 09.04.06. -asabo- dodane 2 nove kolone:
	// broj_osobnog_racuna_osn,broj_osobnog_racuna_dop
	final String select = "select distinct r.sifra,r.dopunsko_osiguranje,r.osnovno_osiguranje,r.iznos_sudjelovanja,r.iznos_sudjelovanja_osnovno_osig,r.sif_klijenta,r.datum_narudzbe,r.datum_izdavanja,r.created,r.created_by,r.status,r.updated,r.updated_by,r.sif_podruznice,r.poziv_na_br1,r.poziv_na_br2,r.napomena,r.sif_proizvodjaca,r.broj_potvrde1,r.broj_potvrde2,r.broj_police_dopunsko,r.sif_drzave,r.broj_iskaznice1,r.broj_iskaznice2,r.ino_broj_lista1,r.ino_broj_lista2,r.sif_lijecnika,r.broj_osobnog_racuna_osn,r.broj_osobnog_racuna_dop,r.uzet_skuplji_model,r.vrsta_pomagala,r.roba_isporucena,r.sif_preporucio,r.datum_slijedeceg_prava,r.aktivnost_zzr,r.aktivnost_dop,r.broj_potvrde_lijecnika from "
			+ tablica + " r";

	private KlijentDAO klijenti = null;
	private DjelatnikDAO djelatnici = null;
	private LijecnikDAO lijecnici = null;
	private MjestoDAO mjesta = null;

	private final KlijentDAO getKlijenti() {
		if (this.klijenti == null)
			this.klijenti = DAOFactory.getInstance().getKlijenti();
		return this.klijenti;
	}

	private final LijecnikDAO getLijecnici() {
		if (this.lijecnici == null)
			this.lijecnici = DAOFactory.getInstance().getLijecnici();
		return this.lijecnici;
	}

	private final MjestoDAO getMjesta() {
		if (this.mjesta == null)
			this.mjesta = DAOFactory.getInstance().getMjesta();
		
		return this.mjesta;
	}

	private final DjelatnikDAO getDjelatnici() {
		if (this.djelatnici == null)
			this.djelatnici = DAOFactory.getInstance().getDjelatnici();
		return this.djelatnici;
	}

	public String narusavaLiObjektKonzistentnost(RacunVO objekt) {
		RacunVO rvo = (RacunVO) objekt;
		if (rvo == null)
			return "ispravnost praznog objekta se ne mo\u017Ee provjeravati";
		if (!rvo.getOsnovnoOsiguranje().booleanValue()
				&& (rvo.getBrojPoliceDopunsko() == null || rvo
						.getBrojPoliceDopunsko().trim().length() != 8)) {
			int vel = rvo.getBrojPoliceDopunsko() == null ? 0 : rvo
					.getBrojPoliceDopunsko().trim().length();
			return "Broj police dopunskog osiguranja nije ispravan. Duzina: "
					+ vel + " a treba biti 8 znakova";
		}
		long broj = -1L;

		if (!rvo.getOsnovnoOsiguranje().booleanValue()
				&& rvo.getBrojPoliceDopunsko() != null
				&& rvo.getBrojPoliceDopunsko().trim().length() == 8)
		{
			try {
				broj = Long.parseLong(rvo.getBrojPoliceDopunsko());
			} catch (NumberFormatException nfe) {
				return "Broj police dopunskog osiguranja nije ispravan jer sadrzi znakove koji nisu brojevi! "
						+ rvo.getBrojPoliceDopunsko();
			}
		
			if (broj==0L)
			{
				return "Broj police dopunskog osiguranja nije ispravan!!!";
			}
		}
		
		if (rvo.getOsnovnoOsiguranje().booleanValue()
				&& rvo.getDopunskoOsiguranje().booleanValue()) {
			return "Raèun je za dopunsko i osnovno osiguranje?!?";
		}

		if (!rvo.getOsnovnoOsiguranje().booleanValue()
				&& !rvo.getDopunskoOsiguranje().booleanValue()) {
			return "Raèun nije ni za dopunsko ni za  osnovno osiguranje?!?";
		}

		if (rvo.getPodrucniUred() == null)
			return "podrucni ured HZZO-a nije definiran!";

		// gledamo samo ako broj potvrde pomagala (lijecnika) nije postavljen...
		if (rvo.getBrojPotvrdePomagala() == null
				|| rvo.getBrojPotvrdePomagala().equals("")) {
			if (rvo.getBrojPotvrde1() == null || rvo.getBrojPotvrde1() != null
					&& rvo.getBrojPotvrde1().length() > 3
					&& !Util.jeliCijeliBroj(rvo.getBrojPotvrde1()))
				return "neispravan prvi dio broja potvrde/ra\u010Duna";
			if (rvo.getBrojPotvrde2() == null || rvo.getBrojPotvrde2() != null
					&& rvo.getBrojPotvrde2().length() > 10
					&& !Util.jeliCijeliBroj(rvo.getBrojPotvrde2()))
				return "neispravan drugi dio broja potvrde/ra\u010Duna";
			if (rvo.getBrojPotvrde1() != null
					&& rvo.getBrojPotvrde1().startsWith("0"))
				return "prvi dio broja potvrde ne smije imati vode\u0107u nulu!";
			if (rvo.getBrojPotvrde2() != null
					&& rvo.getBrojPotvrde2().startsWith("0"))
				return "drugi dio broja potvrde ne smije imati vode\u0107u nulu!";
		}
		if (rvo.getPozivNaBroj1() != null && rvo.getPozivNaBroj1().length() > 2)
			return "neispravan prvi dio poziva na broj";
		if (rvo.getPozivNaBroj2() != null
				&& rvo.getPozivNaBroj2().length() > 32)
			return "neispravan drugi dio poziva na broj";
		if (rvo.getNapomena() != null && rvo.getNapomena().length() > 64)
			return "napomena preduga\u010Dka!";
		if (!rvo.getOsnovnoOsiguranje().booleanValue()
				&& (rvo.getBrojOsobnogRacunaDopunsko() == null
						|| rvo.getBrojOsobnogRacunaDopunsko().trim().length() < 1 || rvo
						.getBrojOsobnogRacunaDopunsko().trim().length() > 22))
			return "Broj osobnog ra\u010Duna za dopunsko osiguranje nije ispravan!";
		if (rvo.getSifProizvodjaca() != null
				&& !rvo.getSifProizvodjaca().trim().equals("")) {
			int sfp = -1;
			try {
				sfp = Integer.parseInt(rvo.getSifProizvodjaca());
			} catch (NumberFormatException nfe) {
				sfp = -1;
			}
			if (sfp < 1)
				return "\u0160ifra proizvo\u0111a\u010Da nije ispravno une\u0161ena!";
		}
		if (rvo.getBrojIskaznice2() != null
				&& rvo.getBrojIskaznice2().length() > 8)
			return "drugi dio broja osiguranja korisnika mo\u017Ee biti maksimalno 8 znakova";
		if (rvo.getBrojOsobnogRacunaOsnovno() != null
				&& rvo.getBrojOsobnogRacunaOsnovno().trim().length() > 22)
			return "Broj osobnog ra\u010Duna za osnovno osiguranje je prevelik!";
		if (rvo.getBrojOsobnogRacunaDopunsko() != null
				&& rvo.getBrojOsobnogRacunaDopunsko().trim().length() > 22)
			return "Broj osobnog ra\u010Duna za dopunsko osiguranje je prevelik!";
		if (rvo.getBrojOsobnogRacunaOsnovno() != null
				&& rvo.getBrojOsobnogRacunaOsnovno().length() > 0
				&& !Util.tekstPodlijezeHzzoPravilimaANS(rvo
						.getBrojOsobnogRacunaOsnovno()))
			return "Broj osobnog ra\u010Duna za osnovno osiguranje ne podlije\u017Ee HZZO pravilima!";
		if (rvo.getBrojOsobnogRacunaDopunsko() != null
				&& rvo.getBrojOsobnogRacunaDopunsko().length() > 0
				&& !Util.tekstPodlijezeHzzoPravilimaANS(rvo
						.getBrojOsobnogRacunaDopunsko()))
			return "Broj osobnog ra\u010Duna za dopunsko osiguranje ne podlije\u017Ee HZZO pravilima!";
		if (rvo.getSifDrzave() != null) {
			if (rvo.getBrojInoBolesnickogLista1() == null
					|| rvo.getBrojInoBolesnickogLista1().length() > 3)
				return "Prvi dio ino bolesni\u010Dkog broja osigurane osobe treba biti dug do max. 3 znaka";
			if (rvo.getBrojInoBolesnickogLista2() == null
					|| rvo.getBrojInoBolesnickogLista2().length() > 8)
				return "Drugi dio ino bolesni\u010Dkog broja osigurane osobe treba biti dug do max. 8 znakova";
			int sfp = -1;
			try {
				sfp = Integer.parseInt(rvo.getBrojInoBolesnickogLista1());
			} catch (NumberFormatException nfe) {
				return "prvi dio ino bolesni\u010Dkog broja osigurane osobe nije ispravan";
			}
			try {
				sfp = Integer.parseInt(rvo.getBrojInoBolesnickogLista2());
			} catch (NumberFormatException nfe) {
				return "drugi dio ino bolesni\u010Dkog broja osigurane osobe nije ispravan";
			}
		}
		// zasada null lijecnik prolazi
		// if (rvo.getBrojPotvrdePomagala()==null ||
		// rvo.getBrojPotvrdePomagala().trim().equals("")){
		// return "Niste unijeli broj potvrde lijeènika za pomagalo";
		// }
		if (rvo.getBrojPotvrdePomagala() != null
				&& rvo.getBrojPotvrdePomagala().trim().length() == 14
				&& !Util.brojPotvrdeLijecnikaIspravna(rvo
						.getBrojPotvrdePomagala().trim())) {
			return "broj potvrde lijeènika za pomagalo nije ispravan";
		} else if (rvo.getBrojPotvrdePomagala() != null
				&& rvo.getBrojPotvrdePomagala().trim().length() > 0
				&& rvo.getBrojPotvrdePomagala().trim().length() < 13) {
			return "Potvrda lijeènika za pomagalo mora imati 13 ili 14 znakova!";
		}

		if (rvo.getBrojIskaznice1() == null
				|| rvo.getBrojIskaznice1().length() > 3)
			return "Prvi dio broja iskaznice osigurane osobe treba biti dug do max. 3 znaka";
		try {
			Integer.parseInt(rvo.getBrojIskaznice1());
		} catch (NumberFormatException nfe) {
			return "prvi dio broja iskaznice osigurane osobe nije ispravan";
		}
		try {
			Integer.parseInt(rvo.getBrojIskaznice2());
		} catch (NumberFormatException nfe) {
			return "drugi dio broja iskaznice osigurane osobe nije ispravan";
		}
		if (rvo.getBrojIskaznice2() == null
				|| rvo.getBrojIskaznice2().length() > 9)
			return "Drugi dio broja iskaznice osigurane osobe treba biti dug do max. 9 znakova";
		if (rvo.getSifKlijenta() == null)
			return "niste unijeli naziv klijenta za koga je ovaj ra\u010Dun izdan";
		if (!rvo.getOsnovnoOsiguranje().booleanValue()
				&& rvo.getBrojPoliceDopunsko() != null) {
			int brDop = -1;
			try {
				brDop = Integer.parseInt(rvo.getBrojPoliceDopunsko());
			} catch (NumberFormatException nfe) {
				return "Broj police dopunskog osiguranja ne sadr\u017Ei samo brojeve! - "
						+ rvo.getBrojPoliceDopunsko();
			}
		}
		if (rvo.getIznosSudjelovanja() == null
				|| rvo.getIznosSudjelovanja().intValue() < 0)
			return "iznos sudjelovanja nije ispravan!";
		if (rvo.getDatumIzdavanja().before(rvo.getDatumNarudzbe()))
			return "datum isporuke je raniji od datuma narud\u017Ebe!";
		if (PostavkeBean.isKontrolaOsobnogRacuna()
				&& rvo.getBrojOsobnogRacunaOsnovno() != null) {
			SearchCriteria sc = new SearchCriteria();
			sc.setKriterij("racun_sa_brojem_osobnog_racuna");
			ArrayList<Object> l = new ArrayList<Object>(2);
			l.add(rvo.getBrojOsobnogRacunaOsnovno());
			l.add(rvo.getSifra());
			sc.setPodaci(l);
			try {
				@SuppressWarnings("unchecked")
				ArrayList<RacunVO> rvol = (ArrayList<RacunVO>) findAll(sc);
				if (rvol != null && rvol.size() > 0) {
					RacunVO rvo2 = (RacunVO) rvol.get(0);
					return "Ra\u010Dun sa brojem osobnog ra\u010Duna '"
							+ rvo2.getBrojOsobnogRacunaOsnovno()
							+ "' ve\u010D postoji (ra\u010Dun br. "
							+ rvo2.getSifra().intValue() + ")";
				}
				rvol.clear();
				rvol = null;
			} catch (SQLException sqle) {
				Logger.fatal(
						"SQL iznimka kod trazenja racuna sa brojem osobnog racuna",
						sqle);
				return "Nastao je problem pri trazenju broj osobnog racuna. Kontaktirajte administratora";
			}
		}
		CheckDigit cd = new CheckISO7064Mod11_10();
		if (rvo.getBrojPotvrde2().length() > 8
				&& !cd.verify(rvo.getBrojPotvrde2()))
			return "@drugi dio broja potvrde/ra\u010Duna nije ispravan po ISO7064 standardu (samo upozorenje)";
		if (rvo.getAktivnostZZR() == null
				|| rvo.getAktivnostZZR().trim().equals(""))
			return "Morate unijeti \u0161ifru aktivnosti";
		if (rvo.getAktivnostZZR() != null
				&& rvo.getAktivnostZZR().length() > 0
				&& (!Util.tekstPodlijezeHzzoPravilimaANS(rvo.getAktivnostZZR())
						|| rvo.getAktivnostZZR().length() != 8
						|| !Util.jeliCijeliBroj(rvo.getAktivnostZZR()
								.substring(1)) || !Util
							.tekstPodlijezeHzzoPravilimaA(rvo.getAktivnostZZR()
									.substring(0, 1))))
			return "\u0160ifra aktivnosti za ZZR ne podlije\u017Ee HZZO pravilima!"
					+ (rvo.getAktivnostZZR().length() == 8 ? ""
							: "(aktivnost mora biti 8 znakova)");
		if (rvo.getAktivnostDop() != null
				&& rvo.getAktivnostDop().length() > 0
				&& (!Util.tekstPodlijezeHzzoPravilimaANS(rvo.getAktivnostDop())
						|| rvo.getAktivnostDop().length() != 8
						|| !Util.jeliCijeliBroj(rvo.getAktivnostDop()
								.substring(1)) || !Util
							.tekstPodlijezeHzzoPravilimaA(rvo.getAktivnostDop()
									.substring(0, 1))))
			return "\u0160ifra aktivnosti za dop. osiguranje ne podlije\u017Ee HZZO pravilima!"
					+ (rvo.getAktivnostDop().length() == 8 ? ""
							: "(aktivnost mora biti 8 znakova)");
		else
			return null;
		// TODO napraviti provjeru za broj potvrde lijecnika
		// broj_potvrde_lijecnika
	}// NarusavaLiKonzistentnost

	public void insert(Object objekt) throws SQLException {
		String upit;
		RacunVO ul = (RacunVO) objekt;
		int sifRacuna = DAO.NEPOSTOJECA_SIFRA;

		if (ul == null)
			throw new SQLException("Insert " + tablica
					+ ", ulazna vrijednost je null!");

		upit = "INSERT INTO "
				+ tablica
				+ " (sifra,dopunsko_osiguranje,osnovno_osiguranje,iznos_sudjelovanja,iznos_sudjelovanja_osnovno_osig,sif_klijenta,datum_narudzbe,"
				+ "datum_izdavanja,created,created_by,  SIF_PROIZVODJACA,broj_potvrde1,broj_potvrde2,broj_police_dopunsko,sif_podruznice,poziv_na_br1,"
				+ "poziv_na_br2,napomena, sif_drzave,broj_iskaznice1,broj_iskaznice2,ino_broj_lista1,ino_broj_lista2,sif_lijecnika,broj_osobnog_racuna_osn,"
				+ "broj_osobnog_racuna_dop,uzet_skuplji_model,vrsta_pomagala,roba_isporucena,status,sif_preporucio,datum_slijedeceg_prava,aktivnost_zzr,"
				+ "aktivnost_dop,broj_potvrde_lijecnika) VALUES ("
				+ (sifRacuna = DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(
						"racuni", "sifra")) + ","
				+ "?,?,?,?,?,?,?,current_timestamp,"
				+ GlavniFrame.getSifDjelatnika() + ",?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,'U',?,?,?,?,?)";

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn != null ? conn.prepareStatement(upit) : null;
			if (ps == null)
				throw new SQLException(
						"PreparedStatement je null kod inserta racuna!");

			ps.setString(1, ul.getDopunskoOsiguranje().booleanValue() ? DAO.DA
					: DAO.NE);
			ps.setString(2, ul.getOsnovnoOsiguranje().booleanValue() ? DAO.DA
					: DAO.NE);
			ps.setInt(3, ul.getIznosSudjelovanja() != null ? ul
					.getIznosSudjelovanja().intValue() : 0);
			ps.setInt(4, ul.getIznosOsnovnogOsiguranja() != null ? ul
					.getIznosOsnovnogOsiguranja().intValue() : 0);

			if (ul.getSifKlijenta() != null)
				ps.setInt(5, ul.getSifKlijenta().intValue());
			else
				// else je gadan problem, ne moze se unijeti null korisnik
				ps.setNull(5, Types.INTEGER);

			java.sql.Date datNar, datIzd;
			datNar = new java.sql.Date(ul.getDatumNarudzbe().getTimeInMillis());
			ps.setDate(6, datNar);

			if (ul.getDatumIzdavanja() == null) // u slucaju da datuma izdavanja
												// nema, stavlja se datum
												// narudzbe, (dva ista datuma),
												// ali ce se to kasnije
												// izmjeniti ...
				ps.setDate(7, datNar);
			else {
				datIzd = new java.sql.Date(ul.getDatumIzdavanja()
						.getTimeInMillis());
				ps.setDate(7, datIzd);
			}

			// 01.03.06. -asabo- dodano
			ps.setString(8, ul.getSifProizvodjaca());
			ps.setString(9, ul.getBrojPotvrde1());
			ps.setString(10, ul.getBrojPotvrde2());
			ps.setString(11, ul.getBrojPoliceDopunsko());

			// 08.03.06. -asabo- dodano
			ps.setInt(12, ul.getSifPodrucnogUreda().intValue());
			ps.setString(13, ul.getPozivNaBroj1());
			ps.setString(14, ul.getPozivNaBroj2());

			// 11.03.06. -asabo- dodano
			ps.setString(15, ul.getNapomena());

			// 31.03.06. -asabo- dodano
			if (ul.getSifDrzave() != null) {
				ps.setInt(16, ul.getSifDrzave().intValue());
				ps.setString(19, ul.getBrojInoBolesnickogLista1());
				ps.setString(20, ul.getBrojInoBolesnickogLista2());
			} else {
				ps.setNull(16, Types.INTEGER);
				ps.setNull(19, Types.VARCHAR);
				ps.setNull(20, Types.VARCHAR);
			}

			ps.setString(17, ul.getBrojIskaznice1());
			ps.setString(18, ul.getBrojIskaznice2());

			if (ul.getSifLijecnika() != null)
				ps.setInt(21, ul.getSifLijecnika().intValue());
			else
				ps.setNull(21, Types.INTEGER);

			// 09.04.06. -asabo- dodano
			ps.setString(22, ul.getBrojOsobnogRacunaOsnovno());
			ps.setString(23, ul.getBrojOsobnogRacunaDopunsko());

			// 07.05.06. -asabo- dodano
			boolean skupljiModel = ul.getKupljenSkupljiArtikl() != null ? ul
					.getKupljenSkupljiArtikl().booleanValue() : false;
			ps.setString(24, skupljiModel ? DAO.DA : DAO.NE);

			ps.setInt(25, ul.getVrstaPomagala().intValue());

			// po defaultu cemo staviti da je true...
			boolean robaIsporucena = ul.getRobaIsporucena() != null ? ul
					.getRobaIsporucena().booleanValue() : true;
			ps.setString(26, robaIsporucena ? DAO.DA : DAO.NE);

			// sifra_preporucio
			if (ul.getPreporucio() != null)
				ps.setInt(27, ul.getPreporucio().intValue());
			else
				ps.setNull(27, Types.INTEGER);

			Date dt = new Date(ul.getDatumSlijedecegPrava() == null ? 0L : ul
					.getDatumSlijedecegPrava().getTime());
			if (dt.getTime() == 0L)
				dt = null;
			ps.setDate(28, dt);
			ps.setString(29, ul.getAktivnostZZR());
			ps.setString(30, ul.getAktivnostDop());
			// -as- 2009-03-21
			ps.setString(31, ul.getBrojPotvrdePomagala());

			int kom = ps.executeUpdate();
			// status updated ce se samo postaviti po defaultu...

			if (kom == 1) {
				ul.setSifra(Integer.valueOf(sifRacuna));
				// ozbiljan propust koji bi mogao stvoriti probleme ako se baza
				// porta na mysql ili neko drugo serversko rjesenje
				// gdje vrijeme na jednom racunalu ne mora odgovarati vremenu na
				// drugom racunalu
				ul.setCreated(Calendar.getInstance().getTimeInMillis());

			}// if kom==1
			else {
				String sfkl = "";
				sfkl = ul != null && ul.getSifKlijenta() != null ? ""
						+ ul.getSifKlijenta().intValue() : "?!?";

				Logger.fatal("neuspio insert zapisa u tablicu " + tablica
						+ " sifKlijenta:" + sfkl, null);
				ul.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
				return;
			}// else

			try {
				if (ps != null)
					ps.close();
				ps = null;
			} catch (SQLException e) {
			}

		}
		// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
		finally {
			try {
				if (ps != null)
					ps.close();
				ps = null;
			} catch (SQLException e1) {
			}

			if (conn != null)
				DAOFactory.freeConnection(conn);
		}// finally

	}// insert

	// 23.02.06. -asabo- kreirano ali mislim da se nece koristiti ...
	public boolean update(Object objekt) throws SQLException {
		String upit;
		RacunVO ul = (RacunVO) objekt;

		if (ul == null)
			throw new SQLException("Update " + tablica
					+ ", ulazna vrijednost je null!");

		String updBy = "";

		// s obzirom da odmah nakon insera mora ici update (tako je
		// smajmunirano)
		// koje moze ici unutar 10 minuta vremena, necemo postavljati updated_by
		// ako je razmak izmedju created i updated manji od 10 minuta

		Calendar created = Calendar.getInstance();
		created.setTimeInMillis(ul.getCreated());
		created.set(Calendar.MILLISECOND, 0);
		created.set(Calendar.SECOND, 0);
		created.set(Calendar.MINUTE, 0);
		Calendar sad = Calendar.getInstance();
		sad.set(Calendar.MILLISECOND, 0);
		sad.set(Calendar.SECOND, 0);
		sad.set(Calendar.MINUTE, 0);

		if (!sad.equals(created))
			updBy = " updated_by=" + GlavniFrame.getSifDjelatnika()
					+ ", updated=current_timestamp,";
		else
			updBy = "";

		upit = "update "
				+ tablica
				+ " set "
				+ ""
				+ updBy
				+ "dopunsko_osiguranje=?,osnovno_osiguranje=?,iznos_sudjelovanja=?,iznos_sudjelovanja_osnovno_osig=?,sif_klijenta=?, "
				+ " status=?,datum_narudzbe=?,datum_izdavanja=?, "
				+ " sif_proizvodjaca=?,broj_potvrde1=?,broj_potvrde2=?,broj_police_dopunsko=?,sif_podruznice=?,poziv_na_br1=?,poziv_na_br2=?,napomena=?,"
				+ " sif_drzave=?,broj_iskaznice1=?,broj_iskaznice2=?,ino_broj_lista1=?,ino_broj_lista2=?,sif_lijecnika=?, "
				+ " broj_osobnog_racuna_osn=?,broj_osobnog_racuna_dop=?,uzet_skuplji_model=?,vrsta_pomagala=?,roba_isporucena=?,sif_preporucio=?,"
				+ " datum_slijedeceg_prava=?, aktivnost_zzr=?, aktivnost_dop=?, broj_potvrde_lijecnika=?"
				+ " where sifra=?";

		// String upd="update "+tablica+" set "+
		// "" + updBy;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement(upit);
			ps.clearParameters();

			ps.setString(1, ul.getDopunskoOsiguranje().booleanValue() ? DAO.DA
					: DAO.NE);
			ps.setString(2, ul.getOsnovnoOsiguranje().booleanValue() ? DAO.DA
					: DAO.NE);
			ps.setInt(3, ul.getIznosSudjelovanja() != null ? ul
					.getIznosSudjelovanja().intValue() : 0);
			ps.setInt(4, ul.getIznosOsnovnogOsiguranja() != null ? ul
					.getIznosOsnovnogOsiguranja().intValue() : 0);
			ps.setInt(5, ul.getSifKlijenta().intValue());

			char status = ul.getStatus();
			ps.setString(6, status == 0 ? DAO.STATUS_UPDATED_PS : "" + status);

			// upd+=",dopunsko_osiguranje='"+(ul.getDopunskoOsiguranje().booleanValue()?DAO.DA:DAO.NE)+"'";
			// upd+=",osnovno_osiguranje='"+(ul.getOsnovnoOsiguranje().booleanValue()?DAO.DA:DAO.NE)+"'"+
			// ",iznos_sudjelovanja="+(ul.getIznosSudjelovanja()!=null?ul.getIznosSudjelovanja().intValue():0);
			// upd+=",iznos_sudjelovanja_osnovno_osig="+(ul.getIznosOsnovnogOsiguranja()!=null?ul.getIznosOsnovnogOsiguranja().intValue():0);
			// upd+=",sif_klijenta="+ul.getSifKlijenta().intValue();

			java.sql.Date datNar, datIzd;

			datNar = new java.sql.Date(ul.getDatumNarudzbe().getTimeInMillis());
			ps.setDate(7, datNar);

			if (ul.getDatumIzdavanja() == null) // u slucaju da datuma izdavanja
												// nema, stavlja se datum
												// narudzbe, (dva ista datuma),
												// ali ce se to kasnije
												// izmjeniti ...
				ps.setDate(8, datNar);
			else {
				datIzd = new java.sql.Date(ul.getDatumIzdavanja()
						.getTimeInMillis());
				ps.setDate(8, datIzd);
			}

			// String
			// di=ul.getDatumIzdavanja()!=null?"'"+Util.convertCalendarToStringForSQLQuery(ul.getDatumIzdavanja())+"'":"null";
			//
			// upd+=",status='U',datum_narudzbe='"+Util.convertCalendarToStringForSQLQuery(ul.getDatumNarudzbe())+"'"+
			// ",datum_izdavanja="+di ;

			// 01.03.06 -asabo- dodano
			String sfp = ul.getSifProizvodjaca() != null
					&& !ul.getSifProizvodjaca().trim().equals("") ? ul
					.getSifProizvodjaca().trim() : null;
			if (sfp != null)
				ps.setString(9, sfp);
			else
				ps.setNull(9, Types.VARCHAR);

			ps.setString(10, ul.getBrojPotvrde1());
			ps.setString(11, ul.getBrojPotvrde2());
			ps.setString(12, ul.getBrojPoliceDopunsko());

			// String sp=sfp!=null?"'"+sfp+"'":"null";

			// upd+=",sif_proizvodjaca='"+sp+"'"+
			// ",broj_potvrde1='"+ul.getBrojPotvrde1()+"'"+
			// ",broj_potvrde2='"+ul.getBrojPotvrde2()+"'"+
			// ",broj_police_dopunsko='"+ul.getBrojPoliceDopunsko()+"'";

			// 08.03.06. -asabo- dodano
			ps.setInt(13, ul.getSifPodrucnogUreda().intValue());
			ps.setString(14, ul.getPozivNaBroj1());
			ps.setString(15, ul.getPozivNaBroj2());

			// String
			// nap=ul.getNapomena()==null?"null":"'"+ul.getNapomena()+"'";

			// upd+=",sif_podruznice="+ul.getSifPodrucnogUreda().intValue()+
			// ",poziv_na_br1='"+ul.getPozivNaBroj1()+"',poziv_na_br2='"+ul.getPozivNaBroj2()+"'"+
			// ",napomena="+nap.replaceAll("\\'", "")+"'";

			// 11.03.06. -asabo- dodano
			if (ul.getNapomena() == null)
				ps.setNull(16, Types.VARCHAR);
			else
				ps.setString(16, ul.getNapomena());

			// sif_drzave,broj_iskaznice1,broj_iskaznice2,ino_broj_lista1,ino_broj_lista2

			// 31.03.06. -asabo- dodano
			if (ul.getSifDrzave() != null) {
				ps.setInt(17, ul.getSifDrzave().intValue());
				ps.setString(20, ul.getBrojInoBolesnickogLista1());
				ps.setString(21, ul.getBrojInoBolesnickogLista2());
				// upd+=",sif_drzave="+ul.getSifDrzave().intValue()+",ino_broj_lista1='"+ul.getBrojInoBolesnickogLista1()+"',"+
				// "ino_broj_lista2='"+ul.getBrojInoBolesnickogLista2()+"'";
			} else {
				ps.setNull(17, Types.INTEGER);
				ps.setNull(20, Types.VARCHAR);
				ps.setNull(21, Types.VARCHAR);
				// upd+=",sif_drzave=null, ino_broj_lista1=null,"+
				// "ino_broj_lista2=null";
			}

			ps.setString(18, ul.getBrojIskaznice1());
			ps.setString(19, ul.getBrojIskaznice2());
			// String
			// bi1=ul.getBrojIskaznice1()==null?"null":"'"+ul.getBrojIskaznice1()+"'";
			// String
			// bi2=ul.getBrojIskaznice2()==null?"null":"'"+ul.getBrojIskaznice2()+"'";

			// upd+=",broj_iskaznice1="+bi1+",broj_iskaznice2="+bi2;

			if (ul.getSifLijecnika() != null)
				ps.setInt(22, ul.getSifLijecnika().intValue());
			else
				ps.setNull(22, Types.INTEGER);

			// upd+=",sif_lijecnika="+(ul.getSifLijecnika()!=null?""+ul.getSifLijecnika().intValue():"null")
			// ;

			ps.setString(23, ul.getBrojOsobnogRacunaOsnovno());

			// upd+=",broj_osobnog_racuna_osn='"+ul.getBrojOsobnogRacunaOsnovno()+"'";

			String brdop = ul.getBrojOsobnogRacunaDopunsko() != null
					&& !ul.getBrojOsobnogRacunaDopunsko().trim().equals("") ? ul
					.getBrojOsobnogRacunaDopunsko().trim() : null;
			if (brdop != null)
				ps.setString(24, brdop);
			else
				ps.setNull(24, Types.VARCHAR);

			// upd+=",broj_osobnog_racuna_dop="+(brdop!=null?"'"+brdop+"'":"null");

			// 07.05.06. -asabo- dodano
			boolean skupljiModel = ul.getKupljenSkupljiArtikl() != null ? ul
					.getKupljenSkupljiArtikl().booleanValue() : false;
			ps.setString(25, skupljiModel ? DAO.DA : DAO.NE);

			ps.setInt(26, ul.getVrstaPomagala().intValue());

			// upd+=",uzet_skuplji_model='"+(skupljiModel?DAO.DA:DAO.NE)+"'";
			// upd+=",vrsta_pomagala="+ul.getVrstaPomagala().intValue();

			// po defaultu cemo staviti da je true...
			boolean robaIsporucena = ul.getRobaIsporucena() != null ? ul
					.getRobaIsporucena().booleanValue() : true;
			ps.setString(27, robaIsporucena ? DAO.DA : DAO.NE);

			// upd+=",roba_isporucena='"+(robaIsporucena?DAO.DA:DAO.NE)+"'";

			if (ul.getPreporucio() != null)
				ps.setInt(28, ul.getPreporucio().intValue());
			else
				ps.setNull(28, Types.INTEGER);

			Date dt = new Date(ul.getDatumSlijedecegPrava() == null ? 0L : ul
					.getDatumSlijedecegPrava().getTime());
			if (dt.getTime() == 0L)
				dt = null;
			if (dt != null)
				ps.setDate(29, dt);
			else
				ps.setNull(29, Types.DATE);

			// ,sif_preporucio=?," +
			// " datum_slijedeceg_prava=?, aktivnost_zzr=?, aktivnost_dop=?, broj_potvrde_lijecnika=-bpl-"
			// + " where sifra=?";

			if (ul.getAktivnostZZR() == null
					|| ul.getAktivnostZZR().trim().equals(""))
				ps.setNull(30, Types.CHAR);
			else
				ps.setString(30, ul.getAktivnostZZR());

			if (ul.getAktivnostDop() == null
					|| ul.getAktivnostDop().trim().equals(""))
				ps.setNull(31, Types.CHAR);
			else
				ps.setString(31, ul.getAktivnostDop());

			String bpl;
			// -as- 2009-03-21
			if (ul.getBrojPotvrdePomagala() == null)
				bpl = null;
			else
				bpl = ul.getBrojPotvrdePomagala().trim();

			if (bpl == null)
				ps.setNull(32, Types.VARCHAR);
			else
				ps.setString(32, bpl);

			ps.setInt(33, ul.getSifra().intValue());

			int kom = ps.executeUpdate();
			ps.clearParameters();
			return kom == 1;
		}
		// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
		finally {
			try {
				if (ps != null)
					ps.close();
				ps = null;
			} catch (SQLException e1) {
			}

			if (conn != null)
				DAOFactory.freeConnection(conn);
		}// finally
	}// update

	public void delete(Object kljuc) throws SQLException {
		String upit;
		RacunVO ul = (RacunVO) kljuc;

		if (ul == null)
			throw new SQLException("delete from " + tablica
					+ ", ulazna vrijednost je null!");

		upit = "Update " + tablica + " set " + " status=" + DAO.STATUS_DELETED
				+ " where sifra=?";

		// ako mu je status postavljen na X, onda se stvarno racun fizicki brise
		// a to se dogadja samo sa racunima koji nemaju stavki i koji trebaju
		// biti maknuti
		// jer se stranka na formi odlucila unijeti jednu stavku, pa ju
		// pobrisala...
		if (ul.getStatus() == 'X')
			upit = "delete from " + tablica + " where sifra=?";

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();
			ps = conn.prepareStatement(upit);

			ps.setInt(1, ul.getSifra().intValue());

			int kom = ps.executeUpdate();

			if (kom == 1 && ul.getStatus() == 'X')
				ul.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA)); // da se
																		// zna
																		// da
																		// vise
																		// ne
																		// postoji

			return;
		}
		// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
		finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}

			if (conn != null)
				DAOFactory.freeConnection(conn);
		}// finally
	}// delete

	// 23.02.06. -asabo- kreirano
	public RacunVO read(Object kljuc) throws SQLException {
		Integer sifra = null;

		if (kljuc instanceof Integer)
			sifra = (Integer) kljuc;

		if (sifra == null)
			throw new SQLException("sifra parametar je null kod " + tablica
					+ " read");

		String upit = select + " where r.status<>" + DAO.STATUS_DELETED
				+ " and r.sifra=" + sifra.intValue();

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		try {
			if (rs != null && rs.next())
				return this.constructRacun(rs);
		} finally {
			// 28.03.06. -asabo- opasno je zatvarati pripadajuci statement..
			// try{if (rs!=null && rs.getStatement()!=null)
			// rs.getStatement().close();}catch(SQLException sqle){}
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
		}
		// konkretan jedan objekt ne moze se dobiti
		return null;
	}// read

	// 08.01.06. -asabo- kreirano
	public final List<RacunVO> findAll(Object kljuc) throws SQLException {
		ArrayList<RacunVO> list = new ArrayList<RacunVO>(32);

		KlijentVO klijentKljuc = null;
		Calendar datumKljuc = null;
		SearchCriteria kriterij = null;

		if (kljuc instanceof KlijentVO) {
			klijentKljuc = (KlijentVO) kljuc;
		} else if (kljuc instanceof Calendar) {
			datumKljuc = (Calendar) kljuc;
		} else if (kljuc instanceof SearchCriteria) {
			kriterij = (SearchCriteria) kljuc;
		}

		String where = " where r.status<>" + DAO.STATUS_DELETED + " ";
		String upit = select; // where mu jos ne dodajemo

		if (klijentKljuc != null)
			upit += where + " and r.sif_klijenta="
					+ klijentKljuc.getSifra().intValue();
		else if (datumKljuc != null)
			upit += where
					+ " and r.datum_narudzbe="
					+ Util.convertCalendarToStringForSQLQuery(datumKljuc, false);
		else if (kriterij != null
				&& kriterij.getKriterij().equals(
						RacunDAO.KRITERIJ_RACUNI_PO_VISE_KRITERIJA)) {
			Calendar datumOd = null, datumDo = null;
			KlijentVO korisnik = null;
			MjestoVO mjesto = null;
			LijecnikVO lijecnik = null;
			PomagaloVO pomagalo = null;
			String brojOsobnogRacuna = null;
			String strPomagalo = null;

			List l = kriterij.getPodaci();

			if (l.get(0) != null)
				datumOd = (Calendar) l.get(0);
			else
				datumOd = null;
			if (l.get(1) != null)
				datumDo = (Calendar) l.get(1);
			else
				datumDo = null;
			if (l.get(2) != null)
				korisnik = (KlijentVO) l.get(2);
			else
				korisnik = null;
			if (l.get(3) != null)
				mjesto = (MjestoVO) l.get(3);
			else
				mjesto = null;
			// 22.04.06. -asabo- dodano
			if (l.get(4) != null)
				lijecnik = (LijecnikVO) l.get(4);
			else
				lijecnik = null;

			Object obj = l.get(5);

			if (obj != null) {

				if (obj instanceof PomagaloVO)
					pomagalo = (PomagaloVO) obj;
				else {
					pomagalo = null;
					if (obj instanceof String)
						strPomagalo = (String) obj;
				}
			}// if

			if (l.get(6) != null)
				brojOsobnogRacuna = (String) l.get(6);
			else
				brojOsobnogRacuna = null;

			if (pomagalo != null) {
				upit += ", stavke_racuna sr " + where
						+ " and sr.sif_racuna=r.sifra and sr.sif_artikla='"
						+ pomagalo.getSifraArtikla() + "'";
			} else if (pomagalo == null && strPomagalo != null) {
				upit += ", stavke_racuna sr "
						+ where
						+ " and sr.sif_racuna=r.sifra and sr.sif_artikla like '%"
						+ strPomagalo + "%'";
			} else
				upit += where; // kod stavki racuna ide mala komplikacija, pa je
								// zato ovo ovako

			if (datumOd != null)
				upit += " and r.datum_izdavanja>='"
						+ Util.convertCalendarToStringForSQLQuery(datumOd)
						+ "'";

			if (datumDo != null)
				upit += " and r.datum_izdavanja<='"
						+ Util.convertCalendarToStringForSQLQuery(datumDo)
						+ "'";

			if (korisnik != null)
				upit += " and r.sif_klijenta=" + korisnik.getSifra().intValue();

			if (mjesto != null)
				upit += " and r.sif_podruznice=" + mjesto.getSifra().intValue();

			if (lijecnik != null)
				upit += " and r.sif_lijecnika="
						+ lijecnik.getSifra().intValue();

			if (brojOsobnogRacuna != null)
				upit += " and r.BROJ_OSOBNOG_RACUNA_OSN like '%"
						+ brojOsobnogRacuna.replaceAll("\'", "") + "%'";

		}// if vise kriterija postoji
		else if (kriterij != null
				&& kriterij.getKriterij().equals(
						HzzoObracunDAO.KRITERIJ_SVI_RACUNI_ZA_OBRACUN)) {

			Calendar datumObracunaDo = null, datumObracunaOd = null;
			Integer sifPodruznice = null, osiguranje = null;

			List l = kriterij.getPodaci();

			// datumi za trazenje racuna, prvi moze biti null, drugi ne smije
			// biti null
			// prvom se gledaju svi racuni > a drugom svi <=
			if (l.get(0) != null)
				datumObracunaOd = (Calendar) l.get(0);
			else
				datumObracunaOd = null;
			if (l.get(1) != null)
				datumObracunaDo = (Calendar) l.get(1);
			else
				datumObracunaDo = null;
			if (l.get(2) != null)
				sifPodruznice = (Integer) l.get(2);
			else
				sifPodruznice = null;
			if (l.get(3) != null)
				osiguranje = (Integer) l.get(3);
			else
				osiguranje = null;
			upit += where;

			upit += " and r.status!=" + DAO.STATUS_STORNIRAN;

			if (datumObracunaOd != null)
				upit += " and r.datum_izdavanja>'"
						+ Util.convertCalendarToStringForSQLQuery(datumObracunaOd)
						+ "'";

			if (datumObracunaDo != null)
				upit += " and r.datum_izdavanja<='"
						+ Util.convertCalendarToStringForSQLQuery(datumObracunaDo)
						+ "'";
			else {
				Logger.log(
						"Pretraživanje raèuna za kreiranje hzzo obraèuna, datum do je null?!?",
						null);
				return null;
			}

			if (sifPodruznice != null)
				upit += " and r.sif_podruznice=" + sifPodruznice.intValue();
			else {
				Logger.log(
						"Pretraživanje raèuna za kreiranje hzzo obraèuna, šifra podružnice je null?!?",
						null);
				return null;
			}
			if (osiguranje != null) {
				int osig = osiguranje.intValue();
				upit = upit + " and r.aktivnost_zzr like ";
				switch (osig) {
				case 1: // '\001'
					upit = upit + "'A6%'";
					break;

				case 2: // '\002'
					upit = upit + "'A8%'";
					break;
				}
			} else {
				Logger.log(
						"Pretraživanje raèuna za kreiranje hzzo obraèuna, osiguranje je null?!?",
						null);
				return null;
			}

		}// if kriterij KRITERIJ_SVI_RACUNI_ZA_OBRACUN postoji
		else if (kriterij != null
				&& kriterij.getKriterij().equals(
						RacunDAO.KRITERIJ_RACUNI_SA_BROJEM_OSOBNOG_RACUNA)) {
			String brojOsobnogRacuna = "";
			Integer sifRacuna;
			List l = kriterij.getPodaci();
			brojOsobnogRacuna = (String) l.get(0);
			sifRacuna = (Integer) l.get(1);
			String sfr = sifRacuna != null
					&& sifRacuna.intValue() != DAO.NEPOSTOJECA_SIFRA ? " and not(r.sifra="
					+ sifRacuna.intValue() + ") "
					: "";
			upit += where + sfr + " and r.broj_osobnog_racuna_osn='"
					+ brojOsobnogRacuna.replaceAll("\'", "").trim() + "'";
		}

		if (kriterij != null
				&& kriterij.getKriterij().equals(
						HzzoObracunDAO.KRITERIJ_SVI_RACUNI_ZA_OBRACUN)) {
			upit += " order by r.BROJ_OSOBNOG_RACUNA_OSN asc";	
		}
		else
		{
		// po datumu izdavanja ih sortiraj (ne datumu narudzbe)
		upit += " order by r.datum_izdavanja desc";
		}
		
		
		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);
		
		upit=null;

		try {
			if (rs != null)
				while (rs.next()) {
					list.add(constructRacun(rs));
				}// while

			return list;
		} finally {
			// try{if (rs!=null && rs.getStatement()!=null)
			// rs.getStatement().close();}catch(SQLException sqle){}
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sqle) {
			}
		}

	}// findAll

	public final Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.RacunVO");
	}

	public GUIEditor getGUIEditor() {
		try {
			return (GUIEditor) Class.forName(DAO.GUI_DAO_ROOT + ".Racun")
					.newInstance();

		} catch (InstantiationException ie) {
			Logger.log(
					"InstantiationException kod povlacenja GUIEditora za podatke "
							+ tablica, ie);
			return null;
		} catch (IllegalAccessException iae) {
			Logger.log(
					"IllegalAccessException kod povlacenja GUIEditora za podatke "
							+ tablica, iae);
			return null;
		} catch (ClassNotFoundException e) {
			Logger.log("Nema gui klase Racun?!?", e);
			return null;
		} finally {
		}
	}// getGUIEditor

	public String getColumnName(int rb) {
		if (rb >= 0 && rb < kolone.length)
			return kolone[rb];
		else
			return null;
	}

	public int getColumnCount() {
		return kolone.length;
	}

	public final Class getColumnClass(int columnIndex) {
		 
			switch (columnIndex) {
			// case 0: return Class.forName("java.lang.Integer");
			default:
				return STRING_CLASS;
			}
		 
	}// getColumnClass

	public final Object getValueAt(RacunVO vo, int kolonas) {
		if (vo == null)
			return null;
		RacunVO r = vo;
		// kolone={"sifra","datum nar.","datum izd.","klijent","lijeènik","podr. ured"};

		switch (kolonas) {
		case 0:
			return r.getBrojOsobnogRacunaOsnovno();
		case 1:
			return Util.convertCalendarToString(r.getDatumNarudzbe(), false);
		case 2:
			return r.getDatumIzdavanja() == null ? "nije isporuèen" : Util
					.convertCalendarToString(r.getDatumIzdavanja(), false);
		case 3:
			KlijentVO kvo = null;
			try {
				kvo = (KlijentVO) this.getKlijenti().read(r.getSifKlijenta());
			} catch (SQLException e) {
				Logger.fatal(
						"Iznimka kod CSC Racuni DAO getValueAt() - èitanje klijenta",
						e);
				return "(problem)";
			}
			return kvo != null ? kvo.getIme() + " " + kvo.getPrezime() : "?!?";

		case 4:
			LijecnikVO lvo = null;
			Integer sfl = r.getSifLijecnika();
			if (sfl != null)
				try 
			    {
					lvo = (LijecnikVO) this.getLijecnici().read(sfl);
				} 
			    catch (SQLException e) 
			    {
					Logger.fatal(
							"Iznimka kod CSC Racuni DAO getValueAt() - èitanje lijeènika",
							e);
					return "(problem)";
				}
			
			 return lvo != null ? lvo.toString() : "?!?";

		case 5:
			MjestoVO mvo = null;
			Integer sfm = r.getSifPodrucnogUreda();
			if (sfm != null)
				try {
					mvo = (MjestoVO) this.getMjesta().read(sfm);
				} catch (SQLException e) {
					Logger.fatal(
							"Iznimka kod CSC Racuni DAO getValueAt() - èitanje mjesta",
							e);
					return "(problem)";
				}
			return mvo != null ? mvo.getNaziv() : "?!?";

		case 6:
			return r.getAktivnostZZR();
		case 7:
			char st = r.getStatus();
			switch (st) {
			case 'S':
				return "storniran";
			case 'D':
				return "pobrisan";
			case 'U':
				return "";
			case 'A':
				return "arhiviran";
			default:
				return "";
			}

		default:
			return null;
		}
	}// getValueAt

	public boolean setValueAt(RacunVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(RacunVO vo, int kolona) {
		return false;
	}

	public int getRowCount() {
		int komada = 0;
		try {
			komada = this.findAll(null).size();
		} catch (SQLException e) {
			komada = 0;
		}
		return komada;
	}

	// 08.01.06. -asabo- kreirano
	private final RacunVO constructRacun(ResultSet rs) throws SQLException {
		RacunVO rvo = new RacunVO();

		rvo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		rvo.setDopunskoOsiguranje(Boolean.valueOf(rs.getString(
				"dopunsko_osiguranje").equals(DAO.DA)));
		rvo.setOsnovnoOsiguranje(Boolean.valueOf(rs.getString(
				"osnovno_osiguranje").equals(DAO.DA)));
		rvo.setIznosSudjelovanja(Integer.valueOf(rs
				.getInt("iznos_sudjelovanja")));
		rvo.setIznosOsnovnogOsiguranja(Integer.valueOf(rs
				.getInt("iznos_sudjelovanja_osnovno_osig")));
		rvo.setSifKlijenta(Integer.valueOf(rs.getInt("sif_klijenta")));

		// 01.03.06. -asabo- dodano
		rvo.setSifProizvodjaca(rs.getString("sif_proizvodjaca"));
		rvo.setBrojPotvrde1(rs.getString("broj_potvrde1"));
		rvo.setBrojPotvrde2(rs.getString("broj_potvrde2"));
		rvo.setBrojPoliceDopunsko(rs.getString("broj_police_dopunsko"));

		// 08.03.06. -asabo- dodano
		rvo.setSifPodrucnogUreda(Integer.valueOf(rs.getInt("sif_podruznice")));
		rvo.setPozivNaBroj1(rs.getString("poziv_na_br1"));
		rvo.setPozivNaBroj2(rs.getString("poziv_na_br2"));

		// 11.03.06. -asabo- dodano
		rvo.setNapomena(rs.getString("napomena"));

		// 31.03.06. -asabo- dodano
		rvo.setSifDrzave(Integer.valueOf(rs.getInt("sif_drzave")));
		if (rs.wasNull())
			rvo.setSifDrzave(null);

		// 03.04.06. -asabo- dodano
		rvo.setSifLijecnika(Integer.valueOf(rs.getInt("sif_lijecnika")));
		if (rs.wasNull())
			rvo.setSifLijecnika(null);

		// 09.04.06. -asabo- dodano
		rvo.setBrojOsobnogRacunaOsnovno(rs.getString("broj_osobnog_racuna_osn"));
		rvo.setBrojOsobnogRacunaDopunsko(rs
				.getString("broj_osobnog_racuna_dop"));

		// stringovi su bez provjere pa bio null-ne bio.. ok..
		rvo.setBrojIskaznice1(rs.getString("broj_iskaznice1"));
		rvo.setBrojIskaznice2(rs.getString("broj_iskaznice2"));
		rvo.setBrojInoBolesnickogLista1(rs.getString("ino_broj_lista1"));
		rvo.setBrojInoBolesnickogLista2(rs.getString("ino_broj_lista2"));

		// 07.05.06. -asabo- dodano
		rvo.setKupljenSkupljiArtikl(Boolean.valueOf(rs.getString(
				"uzet_skuplji_model").equals(DAO.DA) ? true : false));

		// 14.05.06. -asabo- dodano
		rvo.setVrstaPomagala(Integer.valueOf(rs.getInt("vrsta_pomagala")));

		// created, updated...
		rvo.setCreatedBy(Integer.valueOf(rs.getInt("created_by")));
		rvo.setCreated(rs.getTimestamp("created").getTime());

		int upby = rs.getInt("updated_by");
		if (rs.wasNull()) {
			rvo.setLastUpdatedBy(null);
			rvo.setLastUpdated(0L);
		} else {
			rvo.setLastUpdatedBy(Integer.valueOf(upby));
			rvo.setLastUpdated(rs.getTimestamp("updated").getTime());
		}

		rvo.setSifKlijenta(Integer.valueOf(rs.getInt("sif_klijenta")));
		Calendar datIzd, datNar;
		java.sql.Date ddatNar, ddatIzd;

		ddatNar = rs.getDate("datum_narudzbe");
		datNar = Calendar.getInstance();
		datNar.setTime(ddatNar);
		rvo.setDatumNarudzbe(datNar);

		ddatIzd = rs.getDate("datum_izdavanja");
		datIzd = Calendar.getInstance();
		datIzd.setTime(ddatIzd);
		rvo.setDatumIzdavanja(datIzd);

		// 02.07.06. -asabo- dodano
		rvo.setRobaIsporucena(Boolean.valueOf(rs.getString("roba_isporucena")
				.equals(DAO.DA) ? true : false));

		rvo.setDatumSlijedecegPrava(rs.getDate("datum_slijedeceg_prava"));

		int preporucio = rs.getInt("sif_preporucio");
		if (rs.wasNull())
			rvo.setPreporucio(null);
		else
			rvo.setPreporucio(Integer.valueOf(preporucio));

		rvo.setAktivnostZZR(rs.getString("aktivnost_zzr"));
		rvo.setAktivnostDop(rs.getString("aktivnost_dop"));

		// -as- 2009-03-21
		rvo.setBrojPotvrdePomagala(rs.getString("broj_potvrde_lijecnika"));
		String statusStr = rs.getString("status");
		rvo.setStatus(statusStr == null ? 0 : statusStr.charAt(0));

		return rvo;
	}// constructRacun

}// Racuni
