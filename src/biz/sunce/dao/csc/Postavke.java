package biz.sunce.dao.csc;

/**
 * datum:2005.06.29
 * @author asabo
 *
 */

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
import biz.sunce.dao.PostavkeDAO;
import biz.sunce.opticar.vo.PostavkaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;

/**
 * datum:2005.06.21
 * 
 * @author asabo
 * 
 */
public final class Postavke implements PostavkeDAO {
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica = "POSTAVKE";

	private final Hashtable<String, PostavkaVO> cache = new Hashtable<String, PostavkaVO>();

	public void insert(Object objekt) throws SQLException {
		PostavkaVO ul = (PostavkaVO) objekt;

		if (ul == null) {
			Logger.warn(
					"Prilikom insertiranja postavke sustava doslo do ilegalnog stanja objekta, ulazni objekt je null, ",
					null);
			throw new SQLException(
					"Insert postavke, ulazna vrijednost je null!");
		}

		String upit = " INSERT INTO " + tablica + "		(" + "		 naziv," // 1
				+ "		 vrijednost" // 2
				+ " ) VALUES (";
		upit += "?,?)"; // 2 komada

		Connection conn = null;
		PreparedStatement ps = null;

		int sifra = DAO.NEPOSTOJECA_SIFRA; // sifra unesene naocale

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement(upit);

			if (ul.getNaziv() == null) {
				Logger.warn(
						"Prilikom pohranjivanja postavke sustava doslo do ilegalnosg stanja objekta, naziv je null, vrijednost:"
								+ ul.getVrijednost(), null);
				throw new SQLException("Ilegalna vrijednost ulaznog objekta");
			}

			ps.setString(1, ul.getNaziv());
			ps.setString(2, ul.getVrijednost());

			int kom = ps.executeUpdate();

			if (kom == 1) {
			}// if kom==1
			else {
				Logger.fatal("neuspio insert zapisa postavke u tablicu "
						+ tablica + " naziv ulaza:" + ul.getNaziv()
						+ " vrijednost ulaza: " + ul.getVrijednost(), null);
				return;
			}// else

		} // try
			// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe
			// ...
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

	public boolean update(Object objekt) throws SQLException {
		PostavkaVO ul = (PostavkaVO) objekt;

		if (ul == null) {
			Logger.warn(
					"Prilikom updateanja postavke sustava doslo do ilegalnog stanja objekta, ulazni objekt je null, ",
					null);
			throw new SQLException("Update " + tablica
					+ ", ulazna vrijednost je null!");
		}

		// iako se u principu ne bi trebalo moci nikako dogoditi, just in
		// case...
		if (ul.getNaziv() == null)
			throw new SQLException("Update " + tablica
					+ ", ulazna vrijednost naziva nije ispravna! vrijednost: "
					+ ul.getVrijednost());

		String upit = " update " + tablica + " set " + "	 vrijednost=?" // 1
				+ "  where naziv=?"; // primary key ...

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement(upit);

			ps.setString(1, ul.getVrijednost());
			ps.setString(2, ul.getNaziv());

			int kom = ps.executeUpdate();

			// moze biti kom==0 ako je vrijednost koja se unosi ista prethodnoj,
			// tako da to nije ozbiljna iznimka, ali zapisujemo u log...
			if (kom == 0)
				Logger.log(
						"CSC Postavke.update nije uspio promjeniti podatke u retku?!?",
						null);
			else
				cache.remove(ul.getNaziv());

			return kom == 1; // jeli, ispravnim updateanjem se smatra promjena
								// samo jednog retka u tablici
		}
		// 28.06.05. -asabo- NEMA CATCH-anja! - sve ide pozivatelju...
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
	}

	public PostavkaVO read(Object kljuc) throws SQLException {
		String naziv = null;
		if (kljuc instanceof String) {
			naziv = (String) kljuc;
			if (cache.containsKey(naziv))
				return cache.get(naziv);
		}

		String upit = "SELECT " + " naziv," + "vrijednost" + " FROM " + "	 "
				+ tablica;

		if (naziv != null)
			upit += " where naziv like '%" + naziv + "%'";

		upit += " order by naziv";

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		PostavkaVO pvo = null;

		try {
			if (rs != null && rs.next()) {
				pvo = constructPostavka(rs);
				cache.put(pvo.getNaziv(), pvo);
			}// if rs.next
		}
		// 28.06.05. -asabo- nema CATCH-anja ...
		finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
			} catch (SQLException sqle) {
			}
		}
		return pvo;
	}// read

	public List<PostavkaVO> findAll(Object kljuc) throws SQLException {
		String naziv = null;

		List<PostavkaVO> lista = new ArrayList<PostavkaVO>();

		if (kljuc instanceof String) {
			naziv = (String) kljuc;
		} else
			naziv = kljuc != null ? kljuc.toString() : null;
		// sve je ostalo pripremljeno za situaciju kad ce u buducnosti trebati
		// mozda pretrazivati po nekom kriteriju

		String upit = "SELECT " + "     naziv," + "  vrijednost" + " FROM "
				+ "	 " + tablica;

		if (naziv != null)
			upit += " where naziv like '%" + naziv + "%'";

		upit += " order by naziv";

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		PostavkaVO pvo = null;

		try {

			while (rs.next()) {
				pvo = constructPostavka(rs);

				lista.add(pvo);
			}// while
		}// try
			// 30.06.05. -asabo- nema CATCH-anja ...
		finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
			} catch (SQLException sqle) {
			}
		}

		return lista;
	}// findAll

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.PostavkaVO");
	}

	public String getColumnName(int rb) {
		return null;
	}

	public int getColumnCount() {
		return 0;
	}

	public Class getColumnClass(int columnIndex) {
		return null;
	}

	public int getRowCount() {
		return 0;
	}

	private PostavkaVO constructPostavka(ResultSet rs) throws SQLException {
		PostavkaVO pvo = new PostavkaVO();

		pvo.setNaziv(rs.getString("naziv"));
		pvo.setVrijednost(rs.getString("vrijednost"));

		return pvo;
	}// constructPostavka

	public GUIEditor getGUIEditor() {
		return null;
	}// constructLeca

	 

 

	public String narusavaLiObjektKonzistentnost(PostavkaVO objekt) {
		 
		return null;
	}

	public Object getValueAt(PostavkaVO vo, int kolonas) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean setValueAt(PostavkaVO vo, Object vrijednost, int kolona) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCellEditable(PostavkaVO vo, int kolona) {
		// TODO Auto-generated method stub
		return false;
	}
}// klasa

