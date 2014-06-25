/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biz.sunce.dao.CacheabilniDAO;
import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DjelatnikDAO;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;

/**
 * datum:2005.05.14
 * 
 * @author dstanic
 * 
 */
public class Djelatnici extends CacheabilniDAO<DjelatnikVO> implements
		DjelatnikDAO {

	private static final Class STRING_CLASS = String.class;
	private static String[] zaglavlja = { "username", "ime", "prezime",
			"administrator" };
	String tablica = "DJELATNICI";

	public void insert(Object objekt) throws SQLException {
		DjelatnikVO ulaz = (DjelatnikVO) objekt;

		if (ulaz == null)
			throw new SQLException(
					"Insert djelatnik ulazna vrijednost je null!");

		// boolean praznaTablica=DAOFactory.isTableEmpty("djelatnici");
		int sifra = DAO.NEPOSTOJECA_SIFRA;

		// 23.05.05. preuredjen upit da bi bilo sve ok sa unosom novih podataka
		// u tablicu
		String upit = " INSERT INTO "
				+ tablica
				+ "	(sifra," // ne postavlja se preko preperd st.
				+ "	ime," // 1
				+ "	prezime," // 2
				+ "	status," // 3
				+ "	lozinka," // 4
				+ "	administrator," // 5
				+ "	created,"
				+ "	created_by," // 6
				+ "  username)" // 7 13.12.05. -asabo- dodano
				+ " VALUES ("
				+ (sifra = DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(
						tablica, "sifra"))
				+ ",?,?,?,?,?,current_timestamp,?,?)"; // 7 komada

		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();

		ps = conn.prepareStatement(upit);

		ps.setString(1, ulaz.getIme());
		ps.setString(2, ulaz.getPrezime());
		ps.setString(3, "" + ulaz.getStatus());
		ps.setString(4, ulaz.getLozinka());
		ps.setString(5, ulaz.getAdministrator().booleanValue() ? DA : NE);

		ps.setInt(6, ulaz.getCreatedBy().intValue());// Ne smije biti null
		ps.setString(7, ulaz.getUsername()); // 13.12.05. -asabo- dodano
		try {

			int kom = ps.executeUpdate();
			if (kom == 1)
				ulaz.setSifra(Integer.valueOf(sifra)); // 01.03.06. -asabo-
														// dodano

		} catch (SQLException e) {
			Logger.fatal("Greska kod inserta djelatnika", e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			DAOFactory.freeConnection(conn);
		}
	}

	public boolean update(Object objekt) throws SQLException {
		DjelatnikVO ulaz = (DjelatnikVO) objekt;

		if (ulaz == null)
			throw new SQLException(
					"Update djelatnik ulazna vrijednost je null!");

		String upit = " UPDATE DJELATNICI SET " + "		ime=?," // 1
				+ "		prezime=?," // 2
				+ "		status=?," // 3
				+ "		lozinka=?," // 4
				+ "		administrator=?," // 5
				+ "		   updated=current_timestamp," + "		updated_by=?, " // 6
				+ "      username=? " // 7 13.12.05. -asabo- dodano
				+ " WHERE sifra=?"; // 8

		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();
		ps = conn.prepareStatement(upit);

		ps.setString(1, ulaz.getIme());
		ps.setString(2, ulaz.getPrezime());
		ps.setString(4, ulaz.getLozinka());
		ps.setString(3, "" + ulaz.getStatus());
		ps.setString(5, ulaz.getAdministrator().booleanValue() ? DA : NE);

		ps.setInt(6, ulaz.getLastUpdatedBy().intValue()); // TODO Ovo ne smije
															// biti null NIKAD
															// !!
		ps.setString(7, ulaz.getUsername()); // 13.12.05. -asabo- dodano
		ps.setInt(8, ulaz.getSifra().intValue()); // Isto ne smije biti null

		try {

			ps.executeUpdate();
			izbaciIzCachea(ulaz.getSifra());

		} catch (SQLException e) {
			Logger.fatal("Greska kod update djelatnika", e);
			return false;
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			DAOFactory.freeConnection(conn);
		}
		return true;
	}

	public DjelatnikVO read(Object kljuc) throws SQLException {

		DjelatnikVO djelatnik = null;
		Integer ulazSifra = null;
		String ulazIme = null;
		SearchCriteria kriterij = null; // 27.02.06. -asabo-

		if (kljuc instanceof Integer) {
			ulazSifra = (Integer) kljuc;
			djelatnik = povuciIzCachea(ulazSifra);

			if (djelatnik != null)
				return djelatnik;

		} else if (kljuc instanceof String) {
			ulazIme = (String) kljuc;
		} else if (kljuc instanceof SearchCriteria) {
			kriterij = (SearchCriteria) kljuc;
		}

		String upit = "SELECT " + "			sifra," + "			ime," + "			prezime,"
				+ "			status," + "			lozinka," + "			administrator,"
				+ "			created," + "			updated," + "			created_by,"
				+ "			updated_by," + "     username " // 13.12.05. -asabo-
				// dodano
				+ " FROM " + "			djelatnici" + " WHERE ";

		String ime = null, lozinka = null;

		if (ulazSifra != null) {
			upit += " sifra = ?";
		}
		if (ulazIme != null) {
			upit += " ime like ?";
		}
		if (kriterij != null) {

			ime = (String) kriterij.getPodaci().get(0);
			lozinka = (String) kriterij.getPodaci().get(1);
			if (ime == null || ime.trim().equals("")) {
				Logger.fatal("Djelatnici.read neispravan ulazni parametar ime:"
						+ ime, null);
				throw new SQLException("Neispravno korisnièko ime: " + ime);
			}

			if (lozinka == null) {
				Logger.fatal(
						"Djelatnici.read neispravan ulazni parametar lozinka",
						null);
				throw new SQLException("Neispravna lozinka, korisnièko ime: "
						+ ime);
			}
			ime = ime.trim();
			lozinka = lozinka.trim();
			upit += " username=? and lozinka=?";
		}// if kriterij!=null

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DAOFactory.getConnection();
		ps = conn.prepareStatement(upit);

		if (ulazSifra != null) {
			ps.setInt(1, ulazSifra.intValue());
		}

		if (ulazIme != null) {
			ps.setString(1, "%" + ulazIme + "%");
		}

		if (kriterij != null) {
			ps.setString(1, ime);
			ps.setString(2, lozinka);
		}

		try {
			rs = ps.executeQuery();

			if (rs.next()) {

				djelatnik = (constructDjelatnik(rs));
				ubaciUCache(djelatnik.getSifra(), djelatnik);
			}
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod djelatnik.read", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
			} catch (SQLException sql) {
			}
			try {
				if (ps != null)
					ps.close();
				ps = null;
			} catch (SQLException sql1) {
			}
			DAOFactory.freeConnection(conn);
		}
		return djelatnik;
	}

	public List<DjelatnikVO> findAll(Object kljuc) throws SQLException {
		String upit = "SELECT " + "			sifra," + "			ime," + "			prezime,"
				+ "			status," + "			lozinka," + "			administrator,"
				+ "			created," + "			updated," + "			created_by,"
				+ "			updated_by,"
				+ "      username " // 13.12.05. -asabo- dodano
				+ " FROM " + "			djelatnici" + " WHERE status<>"
				+ STATUS_DELETED;

		ResultSet rs = DAOFactory.performQuery(upit);
		List<DjelatnikVO> lista = new ArrayList<DjelatnikVO>();
		try {
			if (rs != null)
				while (rs.next()) {
					lista.add(constructDjelatnik(rs));
				}
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod djelatnici.findAll", e);
		} finally {
			try {
				if (rs != null)
					rs.getStatement().close();
			} catch (SQLException e) {
			}
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
		}
		return lista;
	}// findAll

	public void delete(Object kljuc) throws SQLException {
		DjelatnikVO djelatnikVO = null;

		if (kljuc instanceof Integer) {
			djelatnikVO = (DjelatnikVO) this.read(kljuc);//
		}
		if (kljuc instanceof DjelatnikVO) {
			djelatnikVO = (DjelatnikVO) kljuc;
		} else
			throw new SQLException("Æaæu zajebaji");

		djelatnikVO.setStatus(STATUS_DELETED.charAt(1));
		this.update(djelatnikVO);
		this.izbaciIzCachea(djelatnikVO.getSifra());
	}// delete

	// ----------------------------Metode vezae uz
	// prikaz----------------------------
	public Class getColumnClass(int columnIndex) {

		switch (columnIndex) {
		case 0:
			return STRING_CLASS;
		case 1:
		case 2:
			return STRING_CLASS;// Oba case-a vracaju
								// String
		case 3:
			return Boolean.class; // 12.12.05. -asabo-
									// dodano
		default:
			return null;
		}// switch

	}// getColumnClass

	public int getColumnCount() {
		return zaglavlja.length;
	}

	public String getColumnName(int rb) {
		return zaglavlja[rb];
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

	public Object getValueAt(DjelatnikVO djelatnik, int kolonas) {
		if (djelatnik == null)
			return null;
		;
		switch (kolonas) {
		case 0:
			return djelatnik.getUsername();
		case 1:
			return djelatnik.getIme();
		case 2:
			return djelatnik.getPrezime();
		case 3:
			return djelatnik.getAdministrator();
		default:
			return null;
		}
	}// getValueAt

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.DjelatnikVO");
	}

	public boolean isCellEditable(DjelatnikVO vo, int kolona) {
		return false;// Zasad necemo imati editabile tablice
	}

	public boolean setValueAt(DjelatnikVO vo, Object vrijednost, int kolona) {
		return false;// metoda iznad nece dopustiti dolazak progrma do tu
	}

	private DjelatnikVO constructDjelatnik(ResultSet rs) throws SQLException {
		DjelatnikVO d = new DjelatnikVO();

		d.setSifra(Integer.valueOf(rs.getInt("SIFRA")));// ne smije biti null !
		d.setIme(rs.getString("IME"));
		d.setPrezime(rs.getString("PREZIME"));
		d.setStatus(rs.getString("STATUS").charAt(0));
		d.setLozinka(rs.getString("LOZINKA"));
		d.setAdministrator(Boolean.valueOf(rs.getString("ADMINISTRATOR")
				.equals(DA)));

		d.setCreated(rs.getDate("created").getTime()); // 08.12.05. -asabo-
														// promjenjeno
		java.sql.Date dt = rs.getDate("updated"); // 12.12.05. -asabo- dodano
		d.setLastUpdated(dt != null ? dt.getTime() : 0L); // 08.12.05. -asabo-
															// promjenjeno

		d.setCreatedBy(Integer.valueOf(rs.getInt("CREATED_BY")));// ne smije
																	// biti null
																	// !
		d.setLastUpdatedBy(Integer.valueOf(rs.getInt("UPDATED_BY")));

		d.setUsername(rs.getString("username")); // 13.12.05. -asabo- dodano

		if (rs.wasNull())
			d.setLastUpdatedBy(null);

		return d;
	}// constructDjelatnik

	public List<DjelatnikVO> find(Object kljuc) throws SQLException {

		return null;
	}

	public GUIEditor getGUIEditor() {
		try {
			return (GUIEditor) Class.forName(DAO.GUI_DAO_ROOT + ".Djelatnik")
					.newInstance();
		} catch (InstantiationException ie) {
			Logger.log(
					"InstantiationException kod povlacenja GUIEditora klase "
							+ this.getClass().toString(), ie);
			return null;
		} catch (IllegalAccessException iae) {
			Logger.log(
					"IllegalAccessException kod povlacenja GUIEditora klase "
							+ this.getClass().toString(), iae);
			return null;
		} catch (ClassNotFoundException e) {
			Logger.log("Nema GUI klase kod " + this.getClass().toString(), e);
			return null;
		}
	}// getGUIEditor

	public String narusavaLiObjektKonzistentnost(ValueObject objekt) {
		DjelatnikVO dvo = (DjelatnikVO) objekt;
		//String username = dvo.getUsername();
		String upit = "select sifra,username from djelatnici where not(sifra="
				+ dvo.getSifra().intValue() + ") and username=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		if (dvo.getUsername() == null || dvo.getUsername().equals(""))
			return "morate unijeti korisnièko ime";

		if (!dvo.getUsername()
				.equals(Util.zaravnajNasaSlova(dvo.getUsername())))
			return "korisnièko ime ne može sadržavati hrvatske dijakritièke znakove!";

		try {
			con = DAOFactory.getConnection();
			ps = con.prepareStatement(upit);
			ps.setString(1, dvo.getUsername());

			rs = ps.executeQuery();

			if (rs != null && rs.next())
				return "korisnièko ime veè postoji. Promjenite ga!";

		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod djelatnici.narusavaLiObjektKonzistentnost",
					e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException sqle) {
			}
			try {
				if (con != null)
					DAOFactory.freeConnection(con);
			} catch (SQLException sqle) {
			}
		}// finally

		return null;
	}// narusava li objekt konzistentnost

}// klasa
