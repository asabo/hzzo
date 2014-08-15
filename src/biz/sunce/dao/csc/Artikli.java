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

import biz.sunce.dao.ArtiklDAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.ArtiklVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
import biz.sunce.dao.SearchCriteria;

/**
 * datum:2005.07.21
 * 
 * @author asabo
 * 
 */
public final class Artikli implements ArtiklDAO {

	public void insert(Object objekt) throws SQLException {
	}

	public boolean update(Object objekt) throws SQLException {
		return false;
	}

	public void delete(Object kljuc) throws SQLException {
	}

	final String upitArtikli = "SELECT "
			+ "		b.sifra,"
			+ "		b.oznakaBoje,"
			+ "		b.sifNaocale,"
			+ "		n.katOznaka,"
			+ "		n.sifMarke,"
			+ "		m.naziv,"
			+ "   n.proizvedeno1, "
			+ "   n.proizvedeno2 "
			+ " FROM "
			+ "		boje b,"
			+ "		naocale n, "
			+ "   marke m "
			+ " WHERE b.sifNaocale=n.sifra and m.sifra=n.sifMarke and b.sifra=?";

	
	public ArtiklVO read(Object obj) throws SQLException {
		Integer ulaz = null;
		 
		if (obj == null)
			throw new SQLException(
					"Ulazni parametar prazan! CSC Artikli.read()");

		if (obj instanceof Integer)
			ulaz = (Integer) obj;
	 	
		Connection c = DAOFactory.getConnection();
		ResultSet rs = null;
		PreparedStatement pst = null;
		ArtiklVO artikl = null;
		try {
			pst = c.prepareStatement(upitArtikli);

			if (ulaz != null)
				pst.setInt(1, ulaz.intValue());

			rs = pst.executeQuery();

			if (rs.next()) {
				artikl = constructArtikl(rs, artikl);
			}// if
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod CSC artikli.read() sifra:"
					+ (ulaz != null ? ulaz.intValue() : -1), e);
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
		return artikl;
	}// read

	final String upitArtikliSvi = "SELECT " + "		b.sifra," + "		b.oznakaBoje,"
			+ "		b.sifNaocale," + "		n.katOznaka," + "		n.sifMarke,"
			+ "		m.naziv," + "   n.proizvedeno1, " + "   n.proizvedeno2 "
			+ " FROM " + "		boje b," + "		naocale n, " + "   marke m "
			+ " WHERE b.sifNaocale=n.sifra and m.sifra=n.sifMarke ";
	
	public List<ArtiklVO> findAll(Object kljuc) throws SQLException {
		Integer ulaz = null;
		String art = null;

		List<ArtiklVO> lista = new ArrayList<ArtiklVO>();

		if (kljuc instanceof Integer)
			ulaz = (Integer) kljuc;
		else if (kljuc instanceof String)
			art = (String) kljuc;
		
		String upit = upitArtikliSvi;

		Connection c = DAOFactory.getConnection();
		ResultSet rs = null;
		PreparedStatement pst = null;
		ArtiklVO artikl = null;
		try {
			pst = c.prepareStatement(upit);

			if (ulaz != null)
				upit += " and n.sifra=" + ulaz.intValue();
			else if (art != null)
				upit += " and n.katOznaka like '%" + art + "%'";

			rs = pst.executeQuery();

			if (rs != null)
				while (rs.next()) {
					artikl = constructArtikl(rs, artikl);
					lista.add(artikl);
					artikl = null; // constructArtikl kreira novi objekt ali
									// samo ako primi null u sebe...
				}// if
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod CSC artikli.findAll() sifra:"
					+ (ulaz != null ? ulaz.intValue() : -1), e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException sqle) {
			}
			DAOFactory.freeConnection(c);
		}
		return lista;
	}// findAll

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.AtriklVO");
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

	public Object getValueAt(ValueObject vo, int kolonas) {
		return null;
	}

	public boolean setValueAt(ValueObject vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(ValueObject vo, int kolona) {
		return false;
	}

	public int getRowCount() {
		return 0;
	}

	private ArtiklVO constructArtikl(ResultSet rs, ArtiklVO avo)
			throws SQLException {
		if (rs == null)
			return null;
		if (avo == null)
			avo = new ArtiklVO();

		avo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		avo.setOznakaBoje(rs.getString("oznakaBoje"));
		avo.setSifNaocale(Integer.valueOf(rs.getInt("sifNaocale")));
		avo.setKatOznaka(rs.getString("katOznaka"));
		avo.setSifMarke(Integer.valueOf(rs.getInt("sifMarke")));
		avo.setNazivMarke(rs.getString("naziv"));
		avo.setPrvaGodProizvodnje(Integer.valueOf(rs.getInt("proizvedeno1")));
		avo.setZadnjaGodProizvodnje(Integer.valueOf(rs.getInt("proizvedeno2")));

		return avo;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}

}// Artikli
