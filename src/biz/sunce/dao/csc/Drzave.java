/*
 * Created on 2005.04.23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package biz.sunce.dao.csc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biz.sunce.dao.CacheabilniDAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DrzavaDAO;
import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.DrzavaVO;
import biz.sunce.optika.Logger;

/**
 * @author asabo izmjenio 08.05.05. - asabo - dodana metoda find()
 */
public class Drzave extends CacheabilniDAO<DrzavaVO> implements DrzavaDAO {
	private static String[] zaglavlja = { "sifra", "naziv" };

	public boolean isCellEditable(DrzavaVO vo, int kolona) {
		if (kolona > 0)
			return true;
		else
			return false;
	}

	public Class<?> getColumnClass(int columnIndex) {

		switch (columnIndex) {
		case 0:
			return INTEGER_CLASS;
		case 1:
			return STRING_CLASS;
		default:
			return null;
		}// switch

	}// getColumnClass

	public boolean setValueAt(DrzavaVO drzava, Object vrijednost, int kolona) {
		boolean rez = true;
		if (drzava == null)
			return false;

		try {

			switch (kolona) {
			case 1:
				drzava.setNaziv((String) vrijednost);
				rez = this.update(drzava);
				break;
			default:
			}// switch
		} catch (Exception cnfe) {
			return false;
		}
		return rez;
	}

	/*
	 * 25.04.05. - asabo - za odredjeni redak u tablici TableModel poslat ce
	 * ValueObject koji se nalazi u tom retku, a metodi je zadatak vratiti
	 * vrijednost za zadanu kolonu
	 * 
	 * @see biz.sunce.dao.DAO#getValueAt(biz.sunce.opticar.vo.ValueObject, int)
	 */
	public Object getValueAt(DrzavaVO vo, int columnIndex) {
		if (vo == null)
			return null;

		DrzavaVO drzava = (DrzavaVO) vo;

		switch (columnIndex) {
		case 0:
			return drzava.getSifra();
		case 1:
			return drzava.getNaziv();
		default:
			return null;
		}
	}// getValueAt

	/*
	 * 25.04.05. - asabo - broj kolona koje ce se prikazati u tablici
	 * 
	 * @see biz.sunce.dao.DAO#getColumnCount()
	 */
	public int getColumnCount() {
		return zaglavlja.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#getColumnName(int)
	 */
	public String getColumnName(int rb) {
		if (rb < 0 || rb > zaglavlja.length)
			return null;
		else
			return zaglavlja[rb];
	}

	/*
	 * @see biz.sunce.dao.DAO#findAll()
	 */
	public List<DrzavaVO> findAll(Object kljuc) throws SQLException {
		// 25.04.05. - asabo - u slucaju da nema podataka vratit ce se prazna
		// lista
		// kako se ne bi dogodio kakav NullPointerException iznad. Zato
		// bezuvjetno
		// instanciramo listu na pocetku. Princip za sve findAll metode
		List<DrzavaVO> lista = new ArrayList<DrzavaVO>();

		String upit = "select sifra,naziv,cc,cc3 from DRZAVE where status<>"
				+ STATUS_DELETED;
		java.sql.Connection c = DAOFactory.getConnection();
		java.sql.ResultSet rs = null;
		java.sql.Statement st = null;

		try {
			st = c.createStatement();
			rs = st.executeQuery(upit);
			while (rs.next()) {
				DrzavaVO drzava = new DrzavaVO();

				drzava.setSifra(rs.getInt(1));
				drzava.setNaziv(rs.getString(2));
				drzava.setCc(rs.getString(3));
				drzava.setCc3(rs.getString(4));

				lista.add(drzava);
			}

		} catch (SQLException e) {
			Logger.fatal("Iznimka kod Drzave.findAll", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
			rs = null;
			try {
				if (st != null)
					st.close();
			} catch (SQLException sqle) {
			}
			st = null;
			DAOFactory.freeConnection(c);
			c = null;
		}
		return lista;
	}// findAll

	public void delete(Object obj) throws SQLException {
		DrzavaVO ulaz = (DrzavaVO) obj;
		if (ulaz == null)
			throw new SQLException("ulazna vrijednost za drzavu je null!");

		String upit = "update drzave set status='D' where sifra="
				+ ulaz.getSifra();

		int komada = DAOFactory.performUpdate(upit);

		if (komada != 1)
			Logger.log("Neuspješno brisanje države sf:"
					+ ulaz.getSifra().intValue(), null);
		else
			izbaciIzCachea(ulaz.getSifra());
	}// delete

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#insert(java.lang.Object)
	 */
	public void insert(DrzavaVO obj) throws SQLException {

		DrzavaVO ulaz = (DrzavaVO) obj;
		if (ulaz == null)
			throw new SQLException("ulazna vrijednost je null!");

		boolean praznaTablica = DAOFactory.isTableEmpty("drzave");

		String upit = "insert into drzave (sifra,naziv,status,cc,cc3) values ("
				+ (praznaTablica ? "1," : "(select max(sifra)+1 from drzave),")
				+ // 07.12.05. -asabo- izmjenjeno
				" '" + ulaz.getNaziv() + "','U','" + ulaz.getCc() + "','"
				+ ulaz.getCc3() + "')";

		int komada = DAOFactory.performUpdate(upit);

		if (komada != 1)
			Logger.log(
					"Neuspješno unošenje države sf:"
							+ ulaz.getSifra().intValue() + " naziv: "
							+ ulaz.getNaziv(), null);

	}// insert

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#read(java.lang.Object) 25.11.05. asabo - ne znam
	 * koji je smisao ove metode, eto, pa neka ceka...
	 */
	public DrzavaVO read(Object obj) throws SQLException {

		DrzavaVO ulaz = null;
		Integer sifra = null;

		if (obj == null)
			throw new SQLException("ulazna vrijednost je null!");

		if (obj instanceof DrzavaVO) {
			ulaz = (DrzavaVO) obj;
			sifra = ulaz.getSifra();
		} else if (obj instanceof Integer) {
			sifra = (Integer) obj;
			ulaz = povuciIzCachea(sifra);
			if (ulaz != null)
				return ulaz;
		}

		String upit = "select sifra,naziv,cc,cc3 from DRZAVE where sifra="
				+ sifra.intValue();
		java.sql.Connection c = null;
		java.sql.ResultSet rs = null;
		java.sql.Statement st = null;
		DrzavaVO drzava = null;

		try {
			c = DAOFactory.getConnection();
			st = c.createStatement();
			rs = st.executeQuery(upit);
			if (rs != null && rs.next()) {
				drzava = new DrzavaVO();

				drzava.setSifra(rs.getInt(1));
				drzava.setNaziv(rs.getString(2));
				drzava.setCc(rs.getString(3));
				drzava.setCc3(rs.getString(4));

				ubaciUCache(drzava.getSifra(), drzava);
			}// if

		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod CSC Drzave.read()", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
			rs = null;
			try {
				if (st != null)
					st.close();
			} catch (SQLException sqle) {
			}
			st = null;
			if (c != null)
				DAOFactory.freeConnection(c);
			c = null;
		}
		return drzava;
	}// read

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#update(java.lang.Object) 25.04.05. asabo - nakon
	 * sto se updateira objekt u bazi podataka, njegovi se podaci mjenjaju,
	 * dakle ulazni objekt se MIJENJA, ne kreira se novi. (nema smisla drzati
	 * stari i novi)
	 */
	public boolean update(DrzavaVO obj) throws SQLException {
		DrzavaVO ulaz = (DrzavaVO) obj;
		if (ulaz == null)
			throw new SQLException("ulazna vrijednost je null!");

		String upit = "update drzave set naziv='" + ulaz.getNaziv()
				+ "', status='U', cc='" + ulaz.getCc() + "', cc3='"
				+ ulaz.getCc3() + "' where sifra=" + ulaz.getSifra();

		int komada = DAOFactory.performUpdate(upit);

		if (komada != 1) {
			Logger.log("Update CSC drzave sf:" + ulaz.getSifra().intValue()
					+ " nije promjenio vrijednost");
		} else
			povuciIzCachea(ulaz.getSifra());

		return komada == 1;
	}// update

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#getVOClass()
	 */
	public Class<DrzavaVO> getVOClass() throws ClassNotFoundException {
		return biz.sunce.opticar.vo.DrzavaVO.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#getRowCount()
	 */
	public int getRowCount() {
		int komada = 0;
		try {
			komada = this.findAll(null).size();
		} catch (SQLException e) {
			komada = 0;
		}

		return komada;
	}

	public GUIEditor<DrzavaVO> getGUIEditor() {
		return null;
	}

}// Drzave klasa
