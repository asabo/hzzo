/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import biz.sunce.dao.CacheabilniDAO;
import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.ProizvodjaciDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.ProizvodjacVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;

/**
 * datum:2005.07.21
 * 
 * @author asabo
 * 
 */
public final class Proizvodjaci extends CacheabilniDAO<ProizvodjacVO> implements
		ProizvodjaciDAO {
	String tablica = "proizvodjaci p";
	String kolone[] = { "Hzzo šifra", "Naziv" };

	public void insert(Object objekt) throws SQLException {
		String upit = "insert into proizvodjaci (sifra,naziv,hzzo_sifra) values(?,?,?)";

		if (objekt == null || !(objekt instanceof ProizvodjacVO))
			throw new SQLException(
					"Ulazni objekt nije klase ProizvodjacVO ili je null");

		ProizvodjacVO pvo = (ProizvodjacVO) objekt;
		PreparedStatement ps = null;
		Connection con = DAOFactory.getConnection();

		if (con != null)
			try {
				int sfp = DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(
						tablica, "sifra");
				Integer hz = pvo.getHzzoSifra();

				ps = con.prepareStatement(upit);
				ps.setInt(1, sfp);
				ps.setString(2, pvo.getNaziv());
				if (hz != null)
					ps.setInt(3, pvo.getHzzoSifra().intValue());
				else
					ps.setNull(3, Types.INTEGER);

				int kom = ps.executeUpdate();
				if (kom == 0) {
					Logger.log("Pokušaj upisivanja proizvoðaèa nije uspio. Naziv"
							+ pvo.getNaziv()
							+ " hzzo šifra:"
							+ (hz != null ? hz.intValue() : -1));
					throw new SQLException(
							"Nije uspio pokušaj zapisivanja podataka!");
				} else
					pvo.setSifra(Integer.valueOf(sfp)); // da se zna da je
														// insert prosao ok
			} finally {
				try {
					if (ps != null)
						ps.close();
				} catch (SQLException sqle) {
				}
				if (con != null)
					DAOFactory.freeConnection(con);
			}
		else
			throw new SQLException("Veza prema bazi podataka je null!");

	}// insert

	public boolean update(Object objekt) throws SQLException {
		String upit = "update proizvodjaci set naziv=?,hzzo_sifra=? where sifra=?";

		if (objekt == null || !(objekt instanceof ProizvodjacVO))
			throw new SQLException(
					"Ulazni objekt nije klase ProizvodjacVO ili je null");

		ProizvodjacVO pvo = (ProizvodjacVO) objekt;
		PreparedStatement ps = null;
		Connection con = DAOFactory.getConnection();

		if (con != null)
			try {
				Integer hz = pvo.getHzzoSifra();

				ps = con.prepareStatement(upit);

				ps.setString(1, pvo.getNaziv());
				if (hz != null)
					ps.setInt(2, pvo.getHzzoSifra().intValue());
				else
					ps.setNull(2, Types.INTEGER);

				ps.setInt(3, pvo.getSifra().intValue());

				int kom = ps.executeUpdate();
				if (kom == 0) {
					Logger.log("Pokušaj mjenjanja podataka o proizvoðaèu nije uspio. Naziv"
							+ pvo.getNaziv()
							+ " hzzo šifra:"
							+ (hz != null ? hz.intValue() : -1));
					throw new SQLException(
							"Nije uspio pokušaj mjenjanja podataka o proizvoðaèu!");
				} else
					super.izbaciIzCachea(pvo.getSifra());

				return kom == 1;
			} finally {
				try {
					if (ps != null)
						ps.close();
				} catch (SQLException sqle) {
				}
				ps = null;
				if (con != null)
					DAOFactory.freeConnection(con);
			}
		else
			throw new SQLException("Veza prema bazi podataka je null!");

	}// update

	public void delete(Object kljuc) throws SQLException {
	}

	public ProizvodjacVO read(Object obj) throws SQLException {
		Integer ulaz = null;
		SearchCriteria kriterij = null;

		if (obj == null)
			throw new SQLException(
					"Ulazni parametar prazan! CSC Proizvodjaci.read()");

		if (obj instanceof Integer)
			ulaz = (Integer) obj;
		else if (obj instanceof SearchCriteria) {
			kriterij = (SearchCriteria) obj;
		}

		String upit = "SELECT " + "		p.sifra," + "		p.naziv,"
				+ "		p.proizvodi_stakla," + "		p.proizvodi_lece,"
				+ "   p.hzzo_sifra" // 07.06.06. -asabo- dodano
				+ " FROM " + tablica + " where sifra=" + ulaz.intValue();

		ResultSet rs = null;

		ProizvodjacVO proizvodjac = null;
		try {
			rs = DAOFactory.performQuery(upit);

			if (rs != null && rs.next()) {
				proizvodjac = constructProizvodjac(rs, proizvodjac);
				super.ubaciUCache(proizvodjac.getSifra(), proizvodjac);
			}// if
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod CSC Proizvodjaci.read() sifra:"
					+ (ulaz != null ? ulaz.intValue() : -1), e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sqle) {
			}

		}
		return proizvodjac;
	}// read

	public List<ProizvodjacVO> findAll(Object kljuc) throws SQLException {
		Integer ulaz = null;
		String art = null;
		SearchCriteria kriterij = null;
		boolean lece = false, stakla = false, naocale = false;

		List<ProizvodjacVO> lista = new ArrayList<ProizvodjacVO>();

		if (kljuc instanceof Integer)
			ulaz = (Integer) kljuc;
		else if (kljuc instanceof String)
			art = (String) kljuc;
		else if (kljuc instanceof SearchCriteria)
			kriterij = (SearchCriteria) kljuc;

		String upit = "SELECT " + "		p.sifra," + "		p.naziv,"
				+ "		p.proizvodi_stakla," + "		p.proizvodi_lece,"
				+ "   p.hzzo_sifra" // 07.06.06. -asabo- dodano
				+ " FROM " + tablica;

		if (ulaz != null)
			upit += " where sifra=" + ulaz.intValue();
		else if (art != null)
			upit += " where lower(p.naziv) like '%" + art.toLowerCase() + "%'";
		else if (kriterij != null
				&& kriterij.getKriterij().equals(
						KRITERIJ_PROIZVODJACI_HZZO_SIFRA)) {
			Integer sifra = null;
			sifra = (Integer) kriterij.getPodaci().get(0);
			upit += " where p.hzzo_sifra="
					+ (sifra != null ? sifra.intValue() : DAO.NEPOSTOJECA_SIFRA);
		} else if (kriterij != null) {
			if (kriterij.getKriterij()
					.equals(KRITERIJ_PROIZVODJACI_SAMO_STAKLA))
				stakla = true;
			else if (kriterij.getKriterij().equals(
					KRITERIJ_PROIZVODJACI_SAMO_LECE))
				lece = true;
			else if (kriterij.getKriterij().equals(
					KRITERIJ_PROIZVODJACI_LECE_STAKLA)) {
				lece = true;
				stakla = true;
			}

			art = (String) kriterij.getPodaci().get(0); // prvi podatak moraju
														// biti i neka slova
			if (art == null)
				art = ""; // za svaki slucaj ...

			upit += " where lower(p.naziv) like '%" + art.toLowerCase() + "%'";

			if (lece)
				upit += " and p.proizvodi_lece='" + DAO.DA + "'";
			if (stakla)
				upit += " and p.proizvodi_stakla='" + DAO.DA + "'";

		}// if ima kriterij

		ResultSet rs = null;

		ProizvodjacVO proizvodjac = null;
		try {
			rs = DAOFactory.performQuery(upit);

			if (rs != null)
				while (rs.next()) {
					proizvodjac = constructProizvodjac(rs, proizvodjac);
					lista.add(proizvodjac);
					proizvodjac = null; // constructProizvodjac kreira novi
										// objekt ali samo ako primi null u
										// sebe...
				}// while
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod CSC Proizvodjaci.findAll() sifra:"
					+ (ulaz != null ? ulaz.intValue() : -1), e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sqle) {
			}
		}

		return lista;
	}// findAll

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.ProizvodjacVO");
	}

	public String getColumnName(int rb) {
		return kolone[rb];
	}

	public int getColumnCount() {
		return kolone != null ? kolone.length : 0;
	}

	public Class getColumnClass(int columnIndex) {
		 
			switch (columnIndex) {
			case 0:
				return STRING_CLASS;
			case 1:
			default:
				return STRING_CLASS;

			}
		 

	}// klasa

	public Object getValueAt(ProizvodjacVO vo, int kolonas) {
		ProizvodjacVO pvo = (ProizvodjacVO) vo;
		switch (kolonas) {
		case 0:
			return pvo.getHzzoSifra() != null ? ""
					+ pvo.getHzzoSifra().intValue() : "";
		case 1:
			return pvo.getNaziv();
		default:
			return "";
		}
	}

	public boolean setValueAt(ProizvodjacVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(ProizvodjacVO vo, int kolona) {
		return false;
	}

	public int getRowCount() {
		try {
			return this.findAll(null).size();
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod proizvodjaci.getRowCount", e);
			return 0;
		}
	}

	private ProizvodjacVO constructProizvodjac(ResultSet rs, ProizvodjacVO pvo)
			throws SQLException {
		if (rs == null)
			return null;
		if (pvo == null)
			pvo = new ProizvodjacVO();

		pvo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		pvo.setNaziv(rs.getString("naziv"));
		pvo.setProizvodiLece(rs.getString("proizvodi_lece").equals(DAO.DA));
		pvo.setProizvodiStakla(rs.getString("proizvodi_stakla").equals(DAO.DA));
		pvo.setHzzoSifra(Integer.valueOf(rs.getInt("hzzo_sifra")));
		if (rs.wasNull())
			pvo.setHzzoSifra(null);
		return pvo;
	}

	public GUIEditor getGUIEditor() {
		try {
			return (GUIEditor) Class.forName(DAO.GUI_DAO_ROOT + ".Proizvodjac")
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
	}

	public String narusavaLiObjektKonzistentnost(ValueObject objekt) {
		if (objekt == null)
			return "Ne mozemo provjeravati ispravnost praznog objekta";
		ProizvodjacVO pvo = (ProizvodjacVO) objekt;

		Integer hzsf = pvo.getHzzoSifra();

		// null sifra moze proci, ali ako nije null treba vidjeti ima li netko
		// registriran pod tom sifrom
		if (hzsf != null) {
			SearchCriteria sc = new SearchCriteria();
			sc.setKriterij(ProizvodjaciDAO.KRITERIJ_PROIZVODJACI_HZZO_SIFRA);
			ArrayList l = new ArrayList(1);
			l.add(hzsf);
			sc.setPodaci(l);
			try {
				l = (ArrayList) this.findAll(sc);
				if (l != null)
					for (int i = 0; i < l.size(); i++) {
						ProizvodjacVO pv = (ProizvodjacVO) l.get(i);
						if (pv.getSifra().intValue() != pvo.getSifra()
								.intValue())
							return "Proizvoðaè '"
									+ pv.getNaziv()
									+ "' veè je zauzeo HZZO šifru "
									+ hzsf.intValue()
									+ " - prvo je tamo promjenite da biste je mogli dodijeliti proizvoðaèu '"
									+ pvo.getNaziv() + "'";
					}
			} catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod trazenja proizvodjaca sa hzzo sifrom "
								+ hzsf.intValue(), e);
				return "Nastao je problem pri provjeravanju ispravnosti proizvoðaèa. Provjerite poruke sustava!";
			}
		}// if hzsf != null

		if (true) // nek if ostane ... za kasnije
		{
			try {
				ArrayList l = (ArrayList) this.findAll(pvo.getNaziv());
				if (l != null)
					for (int i = 0; i < l.size(); i++) {
						ProizvodjacVO pv = (ProizvodjacVO) l.get(i);
						if (pv.getSifra().intValue() != pvo.getSifra()
								.intValue())
							return "Proizvoðaè sa nazivom '" + pv.getNaziv()
									+ "' veè postoji!";
					}
			} catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod trazenja proizvodjaca sa nazivom "
								+ pvo.getNaziv(), e);
				return "Nastao je problem pri provjeravanju ispravnosti proizvoðaèa. Provjerite poruke sustava!";
			}
		}// if true

		return null;
	}// getGUIEditor

}// Artikli
