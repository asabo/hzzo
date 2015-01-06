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
import biz.sunce.dao.PoreznaStopaDAO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.optika.Logger;

/**
 * datum:2006.02.23
 * 
 * @author asabo
 * 
 */
public final class PorezneStope implements PoreznaStopaDAO {
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica = "porezne_stope";
	private String[] kolone = { "sifra", "naziv", "stopa" };

	private final String select = "select sifra,naziv,stopa from "
			+ tablica;

	Hashtable<Integer, PoreznaStopaVO> porezneStopeCache = new Hashtable<Integer, PoreznaStopaVO>(
			16);

	public String narusavaLiObjektKonzistentnost(PoreznaStopaVO objekt) {
		PoreznaStopaVO psvo = (PoreznaStopaVO) objekt;
		if (psvo == null)
			return "ispravnost praznog objekta se ne može provjeravati";

		if (psvo.getStopa() == null || psvo.getStopa().intValue() < 0
				|| psvo.getStopa().intValue() > 99)
			return "iznos porezne stope neispravan";

		return null;
	}// jeliObjektNarusavaKonzistentnost

	public void insert(PoreznaStopaVO objekt) throws SQLException {
		String upit;
		PoreznaStopaVO ul = (PoreznaStopaVO) objekt;
		int sifra = DAO.NEPOSTOJECA_SIFRA;

		if (ul == null)
			throw new SQLException("Insert " + tablica
					+ ", ulazna vrijednost je null!");

		upit = "INSERT INTO " + tablica
				+ " "
				+ // INSERT da se ne zabunujemo vise
				"(sifra,naziv,stopa)"
				+ " VALUES ("
				+ (sifra = DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(
						tablica, "sifra")) + ",?,?)";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rstemp = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement(upit);

			ps.setString(1, ul.getNaziv());
			ps.setInt(2, ul.getStopa().intValue());

			int kom = ps.executeUpdate();
			// status updated ce se samo postaviti po defaultu...

			if (kom == 1) {
				ul.setSifra(Integer.valueOf(sifra));

			}// if kom==1
			else {
				Logger.fatal("neuspio insert zapisa u tablicu " + tablica, null);
				ul.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
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
			try {
				if (rstemp != null)
					rstemp.close();
			} catch (SQLException sqle) {
			}
			if (conn != null)
				DAOFactory.freeConnection(conn);
		}// finally

	}// insert

	// 23.02.06. -asabo- kreirano ali mislim da se nece koristiti ...
	public boolean update(PoreznaStopaVO objekt) throws SQLException {
		String upit;
		PoreznaStopaVO ul = (PoreznaStopaVO) objekt;

		if (ul == null)
			throw new SQLException("Update " + tablica
					+ ", ulazna vrijednost je null!");

		upit = "Update " + tablica + " set " + "naziv=?,stopa=?"
				+ " where sifra=?";

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement(upit);

			ps.setString(1, ul.getNaziv());
			ps.setInt(2, ul.getStopa().intValue());
			ps.setInt(3, ul.getSifra().intValue());

			int kom = ps.executeUpdate();
			return kom == 1;
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
	}// update

	public void delete(Object kljuc) throws SQLException {

		PoreznaStopaVO ul = (PoreznaStopaVO) kljuc;

		if (ul == null)
			throw new SQLException("delete from " + tablica
					+ ", ulazna vrijednost je null!");

		String upit;
		upit = "Update " + tablica + " set " + " status=" + DAO.STATUS_DELETED
				+ " where sifra=?";

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			// ps.setInt(1,ul.getSifra().intValue());

			// nema brisanja - zasad
			// int kom=ps.executeUpdate();
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
	public PoreznaStopaVO read(Object kljuc) throws SQLException {
		Integer sifra = null;

		if (kljuc instanceof Integer)
			sifra = (Integer) kljuc;

		if (sifra == null)
			throw new SQLException("sifra parametar je null kod " + tablica
					+ " read");

		if (porezneStopeCache.containsKey(sifra)) {
			return porezneStopeCache.get(sifra);
		}

		String upit = select + " where sifra=" + sifra.intValue();

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		try {
			if (rs != null && rs.next()) {
				PoreznaStopaVO res = this.constructPoreznaStopa(rs);
				porezneStopeCache.put(sifra, res);
				return res;
			}
		} finally {
			try {
				if (rs != null && rs.getStatement() != null)
					rs.getStatement().close();
			} catch (SQLException sqle) {
			}
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sqle) {
			}
		}
		// konkretan jedan objekt ne moze se dobiti
		return null;
	}// read

	// 08.01.06. -asabo- kreirano
	public final List findAll(Object kljuc) throws SQLException {
		ArrayList list = new ArrayList(8); // nema vise od 8 poreznih stopa

		String upit = select + " ";

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		try {
			if (rs != null)
				while (rs.next()) {
					list.add(constructPoreznaStopa(rs));
				}// while

			return list;
		} finally {
			try {
				if (rs != null && rs.getStatement() != null)
					rs.getStatement().close();
			} catch (SQLException sqle) {
			}
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
			rs = null;
		}

	}// findAll

	public final Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.PoreznaStopaVO");
	}

	public GUIEditor getGUIEditor() {
		try {
			return (GUIEditor) Class
					.forName(DAO.GUI_DAO_ROOT + ".PoreznaStopa").newInstance();

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
		try {
			switch (columnIndex) {
			case 0:
			case 2:
				return Class.forName("java.lang.Integer");
			case 1:
			default:
				return Class.forName("java.lang.String");
			}
		} catch (ClassNotFoundException cnfe) {
			Logger.fatal(tablica
					+ " CSC -   String ili Integer kao klasa ne postoji?!?",
					cnfe);
			return null;
		}
	}// getColumnClass

	public final Object getValueAt(PoreznaStopaVO vo, int kolonas) {
		if (vo == null)
			return null;
		PoreznaStopaVO r = vo;

		switch (kolonas) {
		case 0:
			return r.getSifra();
		case 1:
			return r.getNaziv();
		case 2:
			return r.getStopa();
		default:
			return null;
		}
	}// getValueAt

	public boolean setValueAt(PoreznaStopaVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(PoreznaStopaVO vo, int kolona) {
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
	private final PoreznaStopaVO constructPoreznaStopa(ResultSet rs)
			throws SQLException {
		PoreznaStopaVO psvo = new PoreznaStopaVO();
		psvo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		psvo.setNaziv(rs.getString("naziv"));
		psvo.setStopa(Integer.valueOf(rs.getInt("stopa")));
		return psvo;
	}// constructRacun

}// TipoviZahtjeva
