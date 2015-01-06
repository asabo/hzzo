/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import biz.sunce.dao.CacheabilniDAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.MjestoDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.DrzavaVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.optika.Logger;

/**
 * datum:2005.05.06
 * 
 * @author dstanic izmjenio 08.05.05. - asabo - dodana metoda find()
 * 
 */
public final class Mjesta extends CacheabilniDAO<MjestoVO> implements MjestoDAO {

	private static final String selectMjesta = " SELECT" + "			m.sifra,"
			+ "			m.naziv," + "			m.sifDrzave," + "			d.sifra," + "			d.naziv,"
			+ "			d.cc,m.zip," + "   m.sif_podr_ureda" + " FROM "
			+ "			mjesta m,drzave d" + "	WHERE " + "			m.sifDrzave=d.sifra";

	public void insert(MjestoVO obj) throws SQLException {
		// nema potreba za implementacijom Davor
	}

	public boolean update(MjestoVO obj) throws SQLException {
		// nema potreba za implementacijom Davor
		return false;
	}

	public void delete(Object obj) throws SQLException {
		// nema potreba za implementacijom Davor
	}

	public MjestoVO read(Object obj) throws SQLException {
		Integer ulaz = null;
		String naziv = null;
		MjestoVO mjesto = null;

		if (obj instanceof Integer) {
			ulaz = (Integer) obj;
			mjesto = super.povuciIzCachea(ulaz);
			if (mjesto != null)
				return mjesto;
		} else if (obj instanceof String)
			naziv = (String) obj;

		String upit = "SELECT " + "		m.sifra," + "		m.naziv,"
				+ "		m.sifDrzave," + "		d.sifra," + "		d.naziv," + "		d.cc,"
				+ "   m.zip," + "   m.sif_podr_ureda" + " FROM "
				+ "		mjesta m," + "		drzave d " + "WHERE d.sifra=m.sifDrzave ";

		if (ulaz != null)
			upit += "and m.sifra=?"; // 21.07.05. -asabo- dodano:
										// d.sifra=m.sifDrzave and
		else if (naziv != null)
			upit += "and lower(m.naziv)=?";

		Connection c = DAOFactory.getConnection();
		ResultSet rs = null;
		PreparedStatement pst = null;

		try {
			pst = c.prepareStatement(upit);

			if (ulaz != null)
				pst.setInt(1, ulaz.intValue());
			else if (naziv != null)
				pst.setString(1, naziv.toLowerCase());

			rs = pst.executeQuery();

			if (rs.next()) {
				mjesto = new MjestoVO();
				mjesto.setSifra(Integer.valueOf(rs.getInt(1)));
				mjesto.setNaziv(rs.getString(2));
				mjesto.setSifDrzave(Integer.valueOf(rs.getInt(3)));
				mjesto.setZip(Integer.valueOf(rs.getInt(7)));
				int sifPodr = rs.getInt(8);
				if (!rs.wasNull())
					mjesto.setSifPodruzniceHzzo(Integer.valueOf(sifPodr));

				DrzavaVO drzava = new DrzavaVO(Integer.valueOf(rs.getInt(4)),
						rs.getString(5), rs.getString(6));

				mjesto.setDrzava(drzava);

				super.ubaciUCache(mjesto.getSifra(), mjesto);

			}
		} catch (SQLException e) {
			Logger.fatal("Problem kod povlacenja mjesta iz baze", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sqle) {
			}
			try {
				if (pst != null)
					pst.close(); pst=null;
			} catch (SQLException sqle) {
			}
			DAOFactory.freeConnection(c);
		}
		return mjesto;
	}

	public List<MjestoVO> findAll(Object kljuc) throws SQLException {
		List<MjestoVO> lista = new ArrayList<MjestoVO>();
		String mj = null;
		SearchCriteria krit = null;

		if (kljuc != null && kljuc instanceof String)
			mj = (String) kljuc;
		else if (kljuc != null && kljuc instanceof SearchCriteria)
			krit = (SearchCriteria) kljuc;

		String upit = selectMjesta;

		// 28.07.05. -asabo- dodao likeanje za mjesta, problem ako korisnik hoce
		// Vukovar i napise 'vu', ne prolazi like '%vu%', mora ici 'vu%'...
		if (mj != null)
			upit += " and ((lower(m.naziv) like '%" + mj + "%') )";
		else if (krit != null) {
			List l = krit.getPodaci();
			mj = (String) l.get(0);
			upit += " and m.sif_podr_ureda is not null and ((lower(m.naziv) like '%"
					+ mj
					+ "%') or (lower(m.naziv) like '"
					+ mj
					+ "%') or (lower(m.naziv) like '%" + mj + "'))";
		}

		// 28.07.05. -asabo- dodao
		upit += " order by m.naziv";

		Connection c = DAOFactory.getConnection();
		ResultSet rs = null;
		Statement st = null;

		try {
			st = c.createStatement();
			rs = st.executeQuery(upit);
			while (rs.next()) {
				MjestoVO mjesta = new MjestoVO();
				mjesta.setSifra(Integer.valueOf(rs.getInt(1)));
				mjesta.setNaziv(rs.getString(2));
				mjesta.setSifDrzave(Integer.valueOf(rs.getInt(3)));
				mjesta.setZip(Integer.valueOf(rs.getInt(7)));
				int sifPodr = rs.getInt(8);
				if (!rs.wasNull())
					mjesta.setSifPodruzniceHzzo(Integer.valueOf(sifPodr));

				DrzavaVO drzava = new DrzavaVO(Integer.valueOf(rs.getInt(4)),
						rs.getString(5), rs.getString(6));
				mjesta.setDrzava(drzava);
				
				lista.add(mjesta);
			}
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod Mjesta CSC findAll. Kljuc: " + kljuc
					+ " string kljuc: " + mj, e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sqle) {
			}
			try {
				if (st != null)
					st.close(); st=null;
			} catch (SQLException sqle) {
			}
			DAOFactory.freeConnection(c);
		}
		return lista;
	}

	// ------------------------------------------------ upiti-------------

	public Class getVOClass() throws ClassNotFoundException {
		return MjestoVO.class;
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

	public Object getValueAt(MjestoVO vo, int kolonas) {
		return null;
	}

	public boolean setValueAt(MjestoVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(MjestoVO vo, int kolona) {
		return false;
	}

	public int getRowCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#find(java.lang.Object)
	 */
	public List find(Object kriterij) throws SQLException {
		return null;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}

	public String narusavaLiObjektKonzistentnost(MjestoVO objekt) {

		return null;
	}

}
