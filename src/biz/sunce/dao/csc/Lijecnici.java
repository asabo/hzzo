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
import java.util.Hashtable;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.LijecnikDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;

/**
 * datum:2005.05.15
 * 
 * @author dstanic
 * 
 */
public final class Lijecnici implements LijecnikDAO {

	private static String[] zaglavlja = { "sifra", "ime", "prezime", "titula" };
	String tablica = "LIJECNICI";

	public void insert(Object objekt) throws SQLException {
		LijecnikVO ulaz = (LijecnikVO) objekt;

		if (objekt == null)
			throw new SQLException(
					"Insert lijecnik: ulazna vrijednost je null!");
		int sifra = DAO.NEPOSTOJECA_SIFRA;

		String upit = " INSERT INTO "
				+ tablica
				+ "	(sifra," // ne postavlja se preko preperd st.
				+ "	ime," // 1
				+ "	prezime," // 2
				+ "	titula," // 3
				+ "	created,"
				+ "  created_by" // 07.12.05. -asabo- dodano, treba imati
									// podatak o tome tko je kreirao lijecnika
				+ ") VALUES ("
				+ (sifra = DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(
						tablica, "sifra"));

		upit += ",?,?,?,current_timestamp,?)";// ,"+GlavniFrame.getSifDjelatnika()+")";
												// //4 komada

		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();

		ps = conn.prepareStatement(upit);

		ps.setString(1, ulaz.getIme());
		ps.setString(2, ulaz.getPrezime());
		ps.setString(3, ulaz.getTitula());
		ps.setInt(4, GlavniFrame.getSifDjelatnika());

		try {

			int kom = ps.executeUpdate();
			if (kom == 1) // da se nekako zna da je insert uspio
				ulaz.setSifra(Integer.valueOf(sifra));

		} catch (SQLException e) {
			Logger.fatal("Greska kod inserta lijecnika", e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			DAOFactory.freeConnection(conn);
		}
	}// insert

	public boolean update(Object objekt) throws SQLException {
		LijecnikVO ulaz = (LijecnikVO) objekt;

		if (ulaz == null)
			throw new SQLException("Update lijecnik ulazna vrijednost je null!");

		String upit = " UPDATE lijecnici SET "
				+ "		ime=?," // 1
				+ "		prezime=?," // 2
				+ "		titula=?," // 3
				+ "		status=?," // 4
				+ "		updated=current_timestamp," + "    updated_by="
				+ GlavniFrame.getSifDjelatnika() // 07.12.05. -asabo- dodana i
													// ta kolona - 28.05.05.
													// -asabo- a tko je
													// promjenio podatak?!?
				+ " WHERE sifra=?"; // 5

		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();
		ps = conn.prepareStatement(upit);

		ps.setString(1, ulaz.getIme());
		ps.setString(2, ulaz.getPrezime());
		ps.setString(3, ulaz.getTitula());
		ps.setString(4, "" + ulaz.getStatus());

		ps.setInt(5, ulaz.getSifra().intValue()); // ne smije biti null

		try {

			ps.executeUpdate();

			lijecniciCache.remove(ulaz.getSifra());
		} catch (SQLException e) {
			Logger.fatal("Greska kod update lijecnika", e);
			return false;
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			ps = null;
			DAOFactory.freeConnection(conn);
		}

		return true;
	}

	Hashtable<Integer, LijecnikVO> lijecniciCache = new Hashtable<Integer, LijecnikVO>(
			128);
	
	final String readLjecnici = "SELECT " + "			sifra," + "			ime," + "			prezime,"
			+ "			titula," + "			status," + "			created,"
			+ "			updated" // 28.05.05. -asabo- opet zarez viska...
			+ " FROM " + "			lijecnici" + " WHERE status<>"
			+ STATUS_DELETED + " and ";

	public LijecnikVO read(Object kljuc) throws SQLException {

		if (kljuc != null && kljuc instanceof Integer
				&& lijecniciCache.containsKey((Integer) kljuc))
			return lijecniciCache.get(kljuc);

		LijecnikVO lijecnik = null;
		Integer ulazSifra = null;
		String ulazIme = null;
		SearchCriteria krit = null;

		if (kljuc instanceof Integer) {
			ulazSifra = (Integer) kljuc;
		} else if (kljuc instanceof String) {
			ulazIme = (String) kljuc;
		} else if (kljuc instanceof SearchCriteria) {
			krit = (SearchCriteria) kljuc;
		} else {
			Logger.log("Lijecnici.read pozvani sa ulaznim parametrom klase: "
					+ (ulazSifra != null ? ulazSifra.getClass().toString()
							: "(null obj)"), null);
			return null;
		}

		String upit = readLjecnici;

		if (ulazSifra != null) {
			upit += " sifra = " + ulazSifra.intValue();
		} else // 28.05.05. -asabo- ne mogu biti oba postavljena
		if (ulazIme != null) {
			upit += " ime like '%" + ulazIme + "%'";
		} else if (krit != null
				&& krit.getKriterij().equals(KRITERIJ_IME_PREZIME)) {
			@SuppressWarnings("rawtypes")
			ArrayList l = (ArrayList) krit.getPodaci();
			String ki = (String) l.get(0);
			String kp = (String) l.get(1);
			if (ki != null)
				ki = ki.replaceAll("\\'", "").toLowerCase();
			if (kp != null)
				kp = kp.replaceAll("\\'", "").toLowerCase();

			upit += " lower(ime)='" + ki + "' and lower(prezime)='" + kp + "'";
		}// if krit!=null

		ResultSet rs = null;

		try {
			rs = DAOFactory.performQuery(upit);
			if (rs.next()) {
				lijecnik = (constructLijecnik(rs));
				lijecniciCache.put(lijecnik.getSifra(), lijecnik);
			}
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod lijecnici.read, upit:" + upit, e);
		} finally {
			try {
				if (rs != null)
					rs.close(); 
				rs = null;
			} catch (SQLException sql) {
			}
		}

		return lijecnik;
	}// read

	final String upitLjecnici = "SELECT " + "			sifra," + "			ime,"
			+ "			prezime," + "			titula," + "			status," + "			created,"
			+ "			updated" // maknut zarez!
			+ " FROM " + "			lijecnici" // 15.05.05. -asabo- bilo je djelatnici,
										// promjenio u lijecnici
			+ " WHERE status<>" + STATUS_DELETED;

	public List<LijecnikVO> findAll(Object kljuc) throws SQLException {

		String upit = upitLjecnici;

		String sKljuc = null;
		if (kljuc != null && kljuc instanceof String) {
			sKljuc = (String) kljuc;
			String filter = sKljuc;
			filter = filter.replaceAll("\'", "");

			if (filter != null) {
				filter = filter.toLowerCase().trim();
				// ako u filteru ima razmak onda se vjerojatno trazi kombinacija
				// ime-prezime...
				String[] fspl = filter.split(" ");
				if (fspl != null && fspl.length > 1) {
					upit += " and (lower(ime) like '%" + fspl[0]
							+ "%' and lower(prezime) like '%" + fspl[1]
							+ "%') order by prezime,ime";
				} else
					upit += " and (lower(ime) like '%" + filter
							+ "%' or lower(prezime) like '%" + filter
							+ "%') order by prezime,ime";
			}// if
		}// if kljuc != null

		ResultSet rs = DAOFactory.performQuery(upit);
		List<LijecnikVO> lista = new ArrayList<LijecnikVO>();
		try {
			if (rs != null)
				while (rs.next()) {
					lista.add(constructLijecnik(rs));
				}
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod lijecnici.findAll", e);
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
		LijecnikVO lijecnikVO = null;

		if (kljuc instanceof Integer) {
			lijecnikVO = (LijecnikVO) this.read(kljuc);//
		}
		if (kljuc instanceof LijecnikVO) {
			lijecnikVO = (LijecnikVO) kljuc;
		} else
			throw new SQLException("Æaæu zajebaje lijecnik");

		lijecnikVO.setStatus(STATUS_DELETED.charAt(1));
		this.update(lijecnikVO);
	}// delete

	// ----------------------------Metode vezae uz
	// prikaz----------------------------
	public Class getColumnClass(int columnIndex) {
		try {
			switch (columnIndex) {
			case 0:
				return Class.forName("java.lang.Integer");
			case 1:
			case 2:
			case 3:
				return Class.forName("java.lang.String");// Oba case-a vracaju
															// String
			default:
				return null;
			}// switch
		} catch (ClassNotFoundException cnfe) {
			return null;
		}
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

	public Object getValueAt(LijecnikVO vo, int kolonas) {
		if (vo == null)
			return null;
		LijecnikVO lijecnik = (LijecnikVO) vo;
		switch (kolonas) {
		case 0:
			return lijecnik.getSifra();
		case 1:
			return lijecnik.getIme();
		case 2:
			return lijecnik.getPrezime();
		case 3:
			return lijecnik.getTitula();
		default:
			return null;
		}
	}// getValueAt

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.LijecnikVO");
	}

	public boolean isCellEditable(LijecnikVO vo, int kolona) {
		return false;
	}

	public boolean setValueAt(LijecnikVO vo, Object vrijednost, int kolona) {
		return false;
	}

	private LijecnikVO constructLijecnik(ResultSet rs) throws SQLException {
		LijecnikVO l = new LijecnikVO();

		l.setSifra(Integer.valueOf(rs.getInt("SIFRA")));// ne smije biti null !
		l.setIme(rs.getString("IME"));
		l.setPrezime(rs.getString("PREZIME"));
		l.setTitula(rs.getString("TITULA"));
		l.setStatus(rs.getString("STATUS").charAt(0));

		l.setCreated(rs.getTimestamp("CREATED").getTime());
		l.setLastUpdated(rs.getTimestamp("UPDATED") != null ? rs.getTimestamp(
				"UPDATED").getTime() : 0L);

		l.setLastUpdatedBy(null);
		return l;
	}

	public GUIEditor getGUIEditor() {
		try {
			return (GUIEditor) Class.forName(DAO.GUI_DAO_ROOT + ".Lijecnik")
					.newInstance();
		} catch (InstantiationException ie) {
			Logger.log(
					"InstantiationException kod povlacenja GUIEditora klase Lijecnik",
					ie);
			return null;
		} catch (IllegalAccessException iae) {
			Logger.log(
					"IllegalAccessException kod povlacenja GUIEditora klase Lijecnik",
					iae);
			return null;
		} catch (ClassNotFoundException e) {
			Logger.log("Nema klase lijecnik?!?", e);
			return null;
		}
	}// getGUIEditor

	public String narusavaLiObjektKonzistentnost(LijecnikVO objekt) {
		return null;
	}// getGUIEditor
}
