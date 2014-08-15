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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DjelatnikDAO;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.HzzoObracunDAO;
import biz.sunce.dao.MjestoDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.HzzoObracunVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;

/**
 * datum:28.03.06.
 * 
 * @author asabo
 * 
 */

public final class HzzoObracuni implements HzzoObracunDAO {
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica = "obracuni_hzzo";

	private String[] kolone = { "sifra", "datum", "podružnica", "osiguranje",
			"kreiran", "kreirao", "pristup", "pristupio" };

	private String select = "SELECT " + " sifra," + " datum,"
			+ "sif_podruznice," + "osiguranje," + "created_by," + "  created,"
			+ "updated_by," + "updated" + " FROM " + "	 " + tablica;

	private DjelatnikDAO djelatnici = null;
	private MjestoDAO mjesta = null; // 09.04.06. -asabo- dodano

	public String narusavaLiObjektKonzistentnost(HzzoObracunVO hvo) {

		if (hvo == null)
			return "prazan objekt!";

		if (hvo.getDatum() == null)
			return "datum obraèuna nije definiran!";

		if (hvo.getSifPodruznice() == null)
			return "podružnica nije definirana!";

		try {
			SearchCriteria krit = new SearchCriteria();
			krit.setKriterij(KRITERIJ_OBRACUN_DATUM_SIFRA_PODRUZNICE);

			ArrayList<Object> l = new ArrayList<Object>();

			l.add(hvo.getDatum());
			l.add(hvo.getSifPodruznice());
			krit.setPodaci(l);

			Object o = this.read(krit);

			if (o != null)
				return "obraèun sa ovim datumom je veè napravljen! ";

		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod traženja hzzo obraèuna po datumu (narusavaLiObjektKonzistentnost)",
					e);
			return "iznimka pri pristupu podacima! Provjerite poruke sustava!";
		}

		// na kraju ako nijedan if nije bio true objekt je ispravan i vracamo
		// null
		return null;
	}// narusavaLiObjektKonzistentnost

	public void insert(Object objekt) throws SQLException {
		String upit;
		HzzoObracunVO ul = (HzzoObracunVO) objekt;

		if (ul == null)
			throw new SQLException("Insert " + tablica
					+ ", ulazna vrijednost je null!");

		int sifra = DAO.NEPOSTOJECA_SIFRA; // sifra unesenog retka

		upit = "INSERT INTO " + tablica
				+ " "
				+ "(SIFRA,datum,created_by,created,sif_podruznice,osiguranje) "
				+ // 09.04.06. -asabo- dodana sif_podruznice
				" VALUES ("
				+ (sifra = DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(
						tablica, "sifra")) + ",?,"
				+ GlavniFrame.getSifDjelatnika() + ",current_timestamp,?,?)";

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement(upit);

			Date dat = new java.sql.Date(ul.getDatum().getTimeInMillis());

			if (ul.getSifPodruznice() == null)
				throw new SQLException("šifra podružnice nije postavljena");

			ps.setDate(1, dat);
			ps.setInt(2, ul.getSifPodruznice().intValue());
			ps.setInt(3, ul.getSifOsiguranja().intValue());

			int kom = ps.executeUpdate();

			if (kom == 1) {
				ul.setSifra(Integer.valueOf(sifra)); // po tome i pozivac zna da
														// je insert uspio...
			}// if kom==1
			else {
				Logger.fatal("neuspio insert zapisa u tablicu " + tablica, null);
				return;
			}

		}
		// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
		finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			ps = null;
			if (conn != null)
				DAOFactory.freeConnection(conn);
			conn = null;
		}// finally

	}// insert

	final String upitUpdate = " update " + tablica + " set " + "		  datum=?, " // 1
			+ "sif_podruznice=?," // 2 09.04.06. -asabo- dodano
			+ "osiguranje=?" + " where sifra=?"; // primary key ...

	
	// 28.03.06. -asabo- kreirano
	public boolean update(Object objekt) throws SQLException {
		HzzoObracunVO ul = (HzzoObracunVO) objekt;

		if (ul == null)
			throw new SQLException("Update " + tablica
					+ ", ulazna vrijednost je null!");

		
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			if (ul.getSifPodruznice() == null)
				throw new SQLException(
						"šifra podružnice kod updatea nije postavljena");

			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement( upitUpdate );

			Date dat = new java.sql.Date(ul.getDatum().getTimeInMillis());

			ps.setDate(1, dat);

			ps.setInt(2, ul.getSifPodruznice().intValue());

			ps.setInt(3, ul.getSifOsiguranja().intValue());

			ps.setInt(4, ul.getSifra().intValue());

			int kom = ps.executeUpdate();

			if (kom == 0)
				Logger.log(
						tablica
								+ " update nije uspio promjeniti podatke u retku tablice "
								+ tablica
								+ " sifra: "
								+ (ul.getSifra() != null ? ul.getSifra()
										.intValue() : -1) + "  datum: " + dat
								+ "?!?", null);

			return kom == 1;
		}
		// -asabo- NEMA CATCH-anja! - sve ide pozivatelju...
		finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			ps = null;
			if (conn != null)
				DAOFactory.freeConnection(conn);
			conn = null;
		}// finally
	}// update

	public void delete(Object kljuc) throws SQLException {
		HzzoObracunVO ul = (HzzoObracunVO) kljuc;

		if (ul == null)
			throw new SQLException("delete " + tablica
					+ ", ulazna vrijednost je null!");

		Integer sifra = ul.getSifra();

		if (sifra == null)
			throw new SQLException("delete " + tablica
					+ ", sifra parametar je null!");

		Logger.log("Hzzo obraèun br. "
				+ sifra.intValue()
				+ " se fizièki briše iz db. sfpodr: "
				+ (ul.getSifPodruznice() != null ? ""
						+ ul.getSifPodruznice().intValue() : "null")
				+ " Djelatnik: " + GlavniFrame.getSifDjelatnika());

		// da, fizicki se brise...
		String upit = " delete from " + tablica + " where sifra="
				+ sifra.intValue();

		try {
			int kom = 0;

			kom = DAOFactory.performUpdate(upit);

			if (kom == 0)
				Logger.log(
						tablica + " delete nije uspio izbrisati redak tablice "
								+ tablica + " ?!? sifra:"
								+ (sifra != null ? sifra.intValue() : -1), null);
		}
		// -asabo- NEMA CATCH-anja! - sve ide pozivatelju...
		finally {
		}// finally
	}// delete

	// 28.03.06. -asabo- kreirano
	public HzzoObracunVO read(Object kljuc) throws SQLException {
		Integer sifra = null;
		Calendar cKljuc = null;
		SearchCriteria krit = null;

		String upit = select;

		if (kljuc == null)
			return null; // trebalo bi baciti exception... zasada ovako

		if (kljuc instanceof Integer) {
			sifra = (Integer) kljuc;
		} else // iako ne treba u biti, neka ostane...
		if (kljuc instanceof HzzoObracunVO) {
			HzzoObracunVO hvo = (HzzoObracunVO) kljuc;
			sifra = hvo.getSifra();
		}// if hzzo..
		else // ako udje datum u metodu read, objekt trazi postoji li kakav
				// obracun konkretnog datuma
		if (kljuc instanceof Calendar) {
			cKljuc = (Calendar) kljuc;
		} else if (kljuc instanceof SearchCriteria) {
			krit = (SearchCriteria) kljuc;
		}

		if (sifra != null)
			upit += " where sifra =" + sifra.intValue() + "";
		else if (cKljuc != null)
			upit += " where datum='"
					+ Util.convertCalendarToStringForSQLQuery(cKljuc) + "'";
		else if (krit != null
				&& krit.getKriterij().equals(
						HzzoObracunDAO.KRITERIJ_OBRACUN_DATUM_SIFRA_PODRUZNICE)) {
			krit = (SearchCriteria) kljuc;
			ArrayList<Object> l = (ArrayList<Object>) krit.getPodaci();
			Calendar c = (Calendar) l.get(0);
			Integer sifp = (Integer) l.get(1);

			upit += " where datum='"
					+ Util.convertCalendarToStringForSQLQuery(c) + "'";
			upit += " and sif_podruznice=" + sifp.intValue();
		}

		upit += " order by sifra"; // ne dirati ovo!

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		HzzoObracunVO pom = null;

		try {
			if (rs != null && rs.next()) {
				pom = constructObracun(rs);
			}// if
			else
				pom = null;
		}
		// -asabo- nema CATCH-anja ...
		finally {
			// try{if (rs!=null && rs.getStatement()!=null)
			// rs.getStatement().close();}catch(SQLException sqle){}
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
		}
		return pom;
	}// read

	// 08.01.06. -asabo- kreirano
	public List<HzzoObracunVO> findAll(Object kljuc) throws SQLException {
		ArrayList<HzzoObracunVO> list = new ArrayList<HzzoObracunVO>(10);

		// String sKljuc = null;
		SearchCriteria krit = null;
		Calendar cKljuc = null;

		if (kljuc instanceof Calendar) {
			cKljuc = (Calendar) kljuc;
		} else if (kljuc instanceof SearchCriteria) {
			krit = (SearchCriteria) kljuc;
		}

		String upit = select;

		if (krit != null
				&& krit.getKriterij().equals(
						KRITERIJ_SVI_OBRACUNI_ZA_PODRUZNICU_HZZO)) {
			Calendar datum = null;
			Integer sifPodruznice = null, godina = null, osiguranje = null;
			ArrayList<Object> l = (ArrayList<Object>) krit.getPodaci();
			Object o = l.get(0);
			if (o != null)
				datum = (Calendar) o;
			sifPodruznice = (Integer) l.get(1);

			// neki pozivatelji mogu definirati i godinu za koju trazimo
			// obracune
			if (l.size() > 2)
				godina = (Integer) l.get(2);

			if (l.size() > 3)
				osiguranje = (Integer) l.get(3);

			// datum moze biti null, sto ce reci da se traze svi obracuni
			// napravljeni za odr. poslovnicu
			upit += " where sif_podruznice=" + sifPodruznice.intValue();

			// a ako nije, onda se traze svi prethodni obracuni za zadani datum
			if (datum != null)
				upit += " and datum<'"
						+ Util.convertCalendarToStringForSQLQuery(datum) + "'";

			// ako je godina postavljena...
			if (godina != null)
				upit += " and year(datum)=" + godina.intValue();

			if (osiguranje != null)
				upit += " and osiguranje=" + osiguranje.intValue();

		}// if
		else if (cKljuc != null)
			upit += " where datum<'"
					+ Util.convertCalendarToStringForSQLQuery(cKljuc) + "'";

		// vazna napomena, UVIJEK popis pregleda iz baze podataka mora izaci
		// sortiran padajuci po datumu
		// cijeli sustav ce racunati na to i sve metode koje ce zvati findAll
		// znaju da datumi dolaze od najnovijeg prema dolje
		upit += " order by datum desc";

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		HzzoObracunVO obr = null;

		try {
			if (rs != null)
				while (rs.next()) {
					obr = constructObracun(rs);
					list.add(obr);
				}// while
		}
		// -asabo- nema CATCH-anja ...
		finally {
			// 28.03.06. -asabo- zasada necemo zatvarati statement, nije
			// pouzdano..
			// try{if (rs!=null && rs.getStatement()!=null)
			// rs.getStatement().close();}catch(SQLException sqle){}
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
		}

		return list;
	}// findAll

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.HzzoObracunVO");
	}

	public GUIEditor<HzzoObracunVO> getGUIEditor() {
		try {
			return (GUIEditor<HzzoObracunVO>) Class.forName(DAO.GUI_DAO_ROOT + ".HzzoObracun")
					.newInstance();

		} catch (InstantiationException ie) {
			Logger.log(
					"InstantiationException kod povlacenja GUIEditora klase HzzoObracun",
					ie);
			return null;
		} catch (IllegalAccessException iae) {
			Logger.log(
					"IllegalAccessException kod povlacenja GUIEditora klase HzzoObracun",
					iae);
			return null;
		} catch (ClassNotFoundException e) {
			Logger.log("Nema klase HzzoObracun?!?", e);
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
		return kolone != null ? kolone.length : 0;
	}

	Class<Integer> integerClass = Integer.class;
	Class<String> stringClass = String.class;
	
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int columnIndex) {
		try {

			switch (columnIndex) {
			case 0:
				return integerClass;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				return stringClass;
			default:
				return null;
			}
		} catch (Exception cnfe) {
			Logger.fatal("Problem kod tablice " + tablica
					+ " CSC - nesto se dogadja: ", cnfe);
			return null;
		}
	}// getColumnClass

	public Object getValueAt(HzzoObracunVO vo, int kolonas) {
		if (vo == null)
			return null;
		HzzoObracunVO obr = vo;

		// sifra","datum","podružnica","osiguranje","kreiran","kreirao","pristup","pristupio"};

		switch (kolonas) {
		case 0:
			return obr.getSifra();
		case 1:
			return Util.convertCalendarToString(obr.getDatum());
		case 2:
			MjestoVO mvo = null;
			try {
				mvo = (MjestoVO) this.getMjesta().read(obr.getSifPodruznice());
			} catch (SQLException e1) {
				Logger.fatal(
						"Iznimka kod CSC HzzoObracuni DAO getValueAt() - èitanje podružnice obraèuna ",
						e1);
				return "(problem)";
			}
			return mvo != null ? mvo.getNaziv() : "?!?";
		case 3:
			Integer osig = obr != null ? obr.getSifOsiguranja() : null;
			return osig == null || osig.intValue() != 1 ? "HZZOZZR" : "HZZO";
		case 4:
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(obr.getCreated());
			return Util.convertCalendarToString(c, true); // vrijeme

		case 5:
			DjelatnikVO dvo = null;
			try {
				dvo = (DjelatnikVO) this.getDjelatnici().read(
						obr.getCreatedBy());
			} catch (SQLException e) {
				Logger.fatal(
						"Iznimka kod CSC HzzoObracuni DAO getValueAt() - èitanje djelatnika koji je kreirao obraèun",
						e);
				return "(problem)";
			}
			return dvo != null ? dvo.getIme() + " " + dvo.getPrezime() : "?!?";

		case 6:
			if (obr.getLastUpdated() == 0L)
				return "(nema)";
			Calendar u = Calendar.getInstance();
			u.setTimeInMillis(obr.getLastUpdated());
			return Util.convertCalendarToString(u, true); // vrijeme

		case 7:
			dvo = null;
			if (obr.getLastUpdatedBy() == null)
				return "";
			try {
				dvo = (DjelatnikVO) this.getDjelatnici().read(
						obr.getLastUpdatedBy());
			} catch (SQLException e) {
				Logger.fatal(
						"Iznimka kod CSC HzzoObracuni DAO getValueAt() - èitanje djelatnika koji je updateao obraèun",
						e);
				return "(problem)";
			}
			return dvo != null ? dvo.getIme() + " " + dvo.getPrezime() : "?!?";
		default:
			return null;
		}
	}// getValueAt

	public boolean setValueAt(HzzoObracunVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(HzzoObracunVO vo, int kolona) {
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

	// 04.03.06. -asabo- kreirano
	private HzzoObracunVO constructObracun(ResultSet rs) throws SQLException {
		HzzoObracunVO obr = new HzzoObracunVO();

		obr.setSifra(Integer.valueOf(rs.getInt("sifra")));

		Calendar c = Calendar.getInstance();
		Date t = rs.getDate("datum");
		c.setTimeInMillis(t.getTime());
		obr.setDatum(c);

		// created, updated...
		obr.setCreatedBy(Integer.valueOf(rs.getInt("created_by")));
		obr.setCreated(rs.getTimestamp("created").getTime());

		int upby = rs.getInt("updated_by");
		if (rs.wasNull()) {
			obr.setLastUpdatedBy(null);
			obr.setLastUpdated(0L);
		} else {
			obr.setLastUpdatedBy(Integer.valueOf(upby));
			obr.setLastUpdated(rs.getTimestamp("updated").getTime());
		}

		// 09.04.06. -asabo- dodano
		obr.setSifPodruznice(Integer.valueOf(rs.getInt("sif_podruznice")));
		obr.setSifOsiguranja(Integer.valueOf(rs.getInt("osiguranje")));

		return obr;
	}// constructObracun

	private DjelatnikDAO getDjelatnici() {
		if (this.djelatnici == null)
			this.djelatnici = DAOFactory.getInstance().getDjelatnici();
		return this.djelatnici;
	}

	private MjestoDAO getMjesta() {
		if (this.mjesta == null)
			this.mjesta = DAOFactory.getInstance().getMjesta();
		return this.mjesta;
	}

}// HzzoObracuni
