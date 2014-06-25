/*
 * Created on 2005.04.23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package biz.sunce.dao.csc;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.SlikeDAO;
import biz.sunce.opticar.vo.ArtiklVO;
import biz.sunce.opticar.vo.SlikaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
import biz.sunce.util.PictureResizer;

/**
 * @author asabo izmjenio 08.05.05. - asabo - dodana metoda find()
 */
public final class Slike implements SlikeDAO {
	private static String[] zaglavlja = { "sifra" };

	public boolean isCellEditable(SlikaVO vo, int kolona) {
		if (kolona > 0)
			return true;
		else
			return false;
	}

	public Class getColumnClass(int columnIndex) {
		try {

			switch (columnIndex) {
			case 0:
				return Class.forName("java.lang.Integer");
			case 1:
				return Class.forName("java.lang.String");
			default:
				return null;
			}// switch
		} catch (ClassNotFoundException cnfe) {
			return null;
		}
	}// getColumnClass

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
	 * @see biz.sunce.dao.DAO#findAll() samo read metoda ce imati funkciju, ne i
	 * findAll (zasada)
	 */
	public List findAll(Object kljuc) throws SQLException {
		return null;
	}// findAll

	public void delete(Object obj) throws SQLException {
	}// delete

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#insert(java.lang.Object)
	 */
	public void insert(Object obj) throws SQLException {
	}// insert

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#read(java.lang.Object) 25.11.05. asabo - ne znam
	 * koji je smisao ove metode, eto, pa neka ceka...
	 */
	public SlikaVO read(Object obj) throws SQLException {
		SearchCriteria kriterij = null;
		Object objekt = null;
		SlikaVO vo = null;

		if (obj == null)
			return null;

		// samo SearchCriteria moze uci kao upit
		if (obj instanceof SearchCriteria) {
			kriterij = (SearchCriteria) obj;
			objekt = kriterij.getPodaci().get(0);
		} else
			return null;

		if (objekt == null)
			return null;

		String upit = null;
		java.sql.Connection c = null;
		java.sql.ResultSet rs = null;
		java.sql.Statement st = null;
		BufferedImage slika = null;

		if (objekt instanceof ArtiklVO) {
			ArtiklVO avo = (ArtiklVO) objekt;
			if (kriterij.getKriterij().equals(KRITERIJ_SLIKA_NAOCALE))
				upit = "select slika from slikeNaocala where sifNaocale="
						+ avo.getSifNaocale().intValue();
			else if (kriterij.getKriterij().equals(KRITERIJ_SLIKA_BOJE))
				upit = "select slika from slikeBoja where sifBoje="
						+ avo.getSifra().intValue();
		}// if objekt instanceof
		else if (objekt instanceof Integer) {
			Integer i = (Integer) objekt;
			if (kriterij.getKriterij().equals(KRITERIJ_SLIKA_NAOCALE))
				upit = "select slika from slikeNaocala where sifNaocale="
						+ i.intValue();
			else if (kriterij.getKriterij().equals(KRITERIJ_SLIKA_BOJE))
				upit = "select slika from slikeBoja where sifBoje="
						+ i.intValue();
		}

		BufferedInputStream ins = null;

		try {
			c = DAOFactory.getConnection();
			st = c.createStatement();
			rs = st.executeQuery(upit);
			if (rs.next()) {
				ins = new BufferedInputStream(rs.getBinaryStream(1));

				if (ins != null)
					slika = PictureResizer.vratiKaoBufferedImage(ins);

			} // if
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod Slike CSC read", e);
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException sqle) {
			}
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
			try {
				if (st != null)
					st.close();
			} catch (SQLException sqle) {
			}
			if (c != null)
				DAOFactory.freeConnection(c);
		}

		vo = new SlikaVO();
		vo.setSlika(slika);

		return vo;
	}// read

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#update(java.lang.Object) 25.04.05. asabo - nakon
	 * sto se updateira objekt u bazi podataka, njegovi se podaci mjenjaju,
	 * dakle ulazni objekt se MIJENJA, ne kreira se novi. (nema smisla drzati
	 * stari i novi)
	 */
	public boolean update(Object obj) throws SQLException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#getVOClass()
	 */
	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("BufferedImage");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#getRowCount()
	 */
	public int getRowCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.dao.DAO#find(java.lang.Object)
	 */
	public List find(Object kriterij) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}

 

	@Override
	public Object getValueAt(SlikaVO vo, int kolonas) {
		 
		return null;
	}

	@Override
	public boolean setValueAt(SlikaVO vo, Object vrijednost, int kolona) {
		 
		return false;
	}

	public String narusavaLiObjektKonzistentnost(ValueObject objekt) {
	 
		return null;
	}

}// Drzava klasa
