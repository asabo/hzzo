/*
 * Project opticar
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.PomagaloDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.StringUtils;

/**
 * datum:2006.01.31
 * 
 * @author asabo
 * 
 */

public final class Pomagala implements PomagaloDAO {
	private static final String POMAGALO = ".Pomagalo";
	private static final int CACHE_SIZE = 2048;

	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica = "artikli";
	
	private String[] kolone = { "sifra", "naziv" };
	private final String select = "SELECT sifra, naziv,"
			+ "porezna_stopa, status, po_cijeni," // 20.03.06. -asabo-
			+ "ocno_pomagalo," // 12.04.06. -asabo- dodano
			+ " created,"
			+ " created_by,"
			+ " updated,"
			+ " updated_by"			
			+ " FROM " + tablica;

	public String narusavaLiObjektKonzistentnost(PomagaloVO objekt) {
		if (objekt==null) return null;

		String sifra = objekt.getSifraArtikla() ;
		String naziv = objekt.getNaziv() ;
				
		if ( sifra==null || sifra.trim().equals("") )
			return "Šifra artikla nije unesena!";

		if ( !sifra.trim().equals(sifra) || sifra.indexOf(' ')!=-1 )
			return "U šifri artikla postoji razmak, koji nije dopušten!";
		
		if (!StringUtils.imaSamoBrojeveISlova(sifra))
		   return "Šifra može sadržavati samo slova i brojeve!";

		if ( sifra.length()<7 )
			return "Šifra artikla mora imati barem 7 znakova!";

		if ( naziv==null || naziv.trim().equals("") )
			return "naziv artikla nije unešen!";
		
		if ( !naziv.trim().equals(naziv) )
			return "U nazivu artikla postoji razmak na kraju/poèetku, koji nije dopušten!";

		if ( naziv.length()<4 )
			return "Naziv artikla mora imati barem 4 znaka!";
		
		return null;
	}

	final String insertUpit = "INSERT INTO " + tablica + " "
			+ "(SIFRA,naziv,porezna_stopa,po_cijeni,ocno_pomagalo,created,created_by,updated,updated_by) " + // 12.04.06.
			" VALUES (?,?,?,?,?,?,?,?,?)"; // ovoj je tablici sifra string i sastavni je dio inserta

	public void insert(Object objekt) throws SQLException {
		 
		PomagaloVO ul = (PomagaloVO) objekt;

		if (ul == null)
			throw new SQLException("Insert " + tablica
					+ ", ulazna vrijednost je null!");
   	
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement( insertUpit );

			ps.setString(1, ul.getSifraArtikla());
			ps.setString(2, ul.getNaziv());
			ps.setInt(3, ul.getPoreznaSkupina().intValue());

			if (ul.getCijenaSPDVom() == null
					|| ul.getCijenaSPDVom().intValue() <= 0)
				ps.setNull(4, Types.INTEGER);
			else
				ps.setInt(4, ul.getCijenaSPDVom().intValue());

			String op = ul.getOptickoPomagalo() != null
					&& ul.getOptickoPomagalo().booleanValue() ? DAO.DA : DAO.NE;

			ps.setString(5, op);
			
			ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			ps.setInt(7, GlavniFrame.getSifDjelatnika());
			ps.setTimestamp(8, null);
			ps.setNull(9, Types.INTEGER);

			int kom = ps.executeUpdate();

			if (kom == 1) {
				ul.setSifra(Integer.valueOf(0)); // po tome i pozivac zna da je
													// insert uspio...
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
					ps.close(); ps=null;
			} catch (SQLException e1) {
			}
			DAOFactory.freeConnection(conn);
		}// finally

	}// insert

	final String updateUpit = " update " + tablica + " set "
			+ "		  naziv=?," // 1
			+ " porezna_stopa=?, " // 2
			+ " status=" + DAO.STATUS_UPDATED + "," 
			+ " po_cijeni=?,"
			+ "ocno_pomagalo=?," // 12.04.06. -asabo- dodano
			+ "updated=?,"
			+ "updated_by=?"
			+ " where sifra=?"; // primary key ...
	
	// 07.01.06. -asabo- kreirano
	public boolean update(Object objekt) throws SQLException {
		PomagaloVO ul = (PomagaloVO) objekt;

		if (ul == null)
			throw new SQLException("Update " + tablica
					+ ", ulazna vrijednost je null!");


		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement( updateUpit );

			ps.setString(1, ul.getNaziv());
			ps.setInt(2, ul.getPoreznaSkupina().intValue());

			// 20.03.06. -asabo- dodano
			if (ul.getCijenaSPDVom() == null
					|| ul.getCijenaSPDVom().intValue() <= 0)
				ps.setNull(3, Types.INTEGER);
			else
				ps.setInt(3, ul.getCijenaSPDVom().intValue());

			String op = ul.getOptickoPomagalo() != null
					&& ul.getOptickoPomagalo().booleanValue() ? DAO.DA : DAO.NE;

			ps.setString(4, op);

			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			ps.setInt(6, GlavniFrame.getSifDjelatnika());
			
			ps.setString(7, ul.getSifraArtikla());

			int kom = ps.executeUpdate();

			boolean updated = kom == 1;
			if (!updated)
				Logger.log(
						tablica
								+ " update nije uspio promjeniti podatke u retku tablice "
								+ tablica + " sifra: " + ul.getSifraArtikla()
								+ " ?!?", null);
			else
				clearFromCache(ul.getSifraArtikla());

			return updated;
		}
		// -asabo- NEMA CATCH-anja! - sve ide pozivatelju...
		finally {
			try {
				if (ps != null)
					ps.close(); ps=null;
			} catch (SQLException e1) {
			}
			DAOFactory.freeConnection(conn); conn=null;
		}// finally
	}// update

	public void delete(Object kljuc) throws SQLException {
		PomagaloVO ul = (PomagaloVO) kljuc;

		if (ul == null)
			throw new SQLException("delete " + tablica
					+ ", ulazna vrijednost je null!");

		String sifra = ul.getSifraArtikla();

		String upit = " update " + tablica + " set status="
				+ DAO.STATUS_DELETED + " where sifra=" + sifra;

		try {
			int kom = 0;

			kom = DAOFactory.performUpdate(upit);

			if (kom == 0)
				Logger.log(tablica
						+ " delete nije uspio izbrisati redak tablice "
						+ tablica + " ?!? sifra:" + sifra, null);
		}
		// -asabo- NEMA CATCH-anja! - sve ide pozivatelju...
		finally {
		}// finally
	}// delete

	Hashtable<String, PomagaloVO> pomagalaCache = new Hashtable<String, PomagaloVO>(
			CACHE_SIZE);

	// 08.01.06. -asabo- kreirano
	public PomagaloVO read(Object kljuc) throws SQLException {
		String sifra = null;
		if (kljuc instanceof String) {
			sifra = (String) kljuc;
		}

		if (sifra != null && pomagalaCache.containsKey(sifra))
			return pomagalaCache.get(sifra);

		String upit = select;

		if (sifra != null)
			upit += " where sifra ='" + sifra + "'";

		if (sifra == null)
			upit += " order by sifra";

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		PomagaloVO pom = null;

		try {
			if (rs.next()) {
				pom = constructPomagalo(rs);
			}// if
			else
				pom = null;
		}
		// -asabo- nema CATCH-anja ...
		finally {
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

		if (pomagalaCache.size() > CACHE_SIZE)
			pomagalaCache.clear();

		if (sifra != null && pom != null)
			pomagalaCache.put(sifra, pom);

		return pom;
	}// read

	static final String ocnoPomagaloUpit = " and ocno_pomagalo='" + DAO.DA
			+ "' ";

	// 08.01.06. -asabo- kreirano
	public List<PomagaloVO> findAll(Object kljuc) throws SQLException {
		ArrayList<PomagaloVO> list = new ArrayList<PomagaloVO>(16);

		String sKljuc = null;
		SearchCriteria krit = null;

		if (kljuc instanceof String) {
			sKljuc = (String) kljuc;
		} else if (kljuc instanceof SearchCriteria) {
			krit = (SearchCriteria) kljuc;
		}

		String upit = select + " where status<>" + DAO.STATUS_DELETED;

		String ocnoPomagaloDodatak = ocnoPomagaloUpit;

		if (krit != null && krit.getKriterij() != null
				&& krit.getKriterij().equals(KRITERIJ_KORISTIMO_SVA_POMAGALA)) {
			ocnoPomagaloDodatak = "";
			sKljuc = (String) krit.getPodaci().get(0);
		}

		if (sKljuc != null)
			upit += " and (lower(naziv) like '%" + sKljuc.toLowerCase() + "%'"
					+ "    or sifra like '%" + sKljuc.toUpperCase() + "%'"
					+ "    ) " + ocnoPomagaloDodatak;

		upit += " order by sifra";

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		PomagaloVO pom = null;

		try {
			if (rs != null)
				while (rs.next()) {
					pom = constructPomagalo(rs);
					list.add(pom);
				}// while
		}
		// -asabo- nema CATCH-anja ...
		finally {
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

		return list;
	}// findAll

	public Class<biz.sunce.opticar.vo.PomagaloVO> getVOClass()
			throws ClassNotFoundException {
		return biz.sunce.opticar.vo.PomagaloVO.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GUIEditor<PomagaloVO> getGUIEditor() {
		try {
			return (GUIEditor) Class.forName(DAO.GUI_DAO_ROOT + POMAGALO).newInstance();

		} catch (InstantiationException ie) {
			Logger.log(
					"InstantiationException kod povlacenja GUIEditora klase Pomagalo",
					ie);
			return null;
		} catch (IllegalAccessException iae) {
			Logger.log(
					"IllegalAccessException kod povlacenja GUIEditora klase Pomagalo",
					iae);
			return null;
		} catch (ClassNotFoundException e) {
			Logger.log("Nema klase Pomagalo?!?", e);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int columnIndex) {
		try {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			default:
				return null;
			}
		} catch (Exception cnfe) {
			Logger.fatal("Problem kod tablice " + tablica
					+ " CSC - neke klase ne postoje?!?", cnfe);
			return null;
		}
	}// getColumnClass

	public Object getValueAt(PomagaloVO vo, int kolonas) {
		if (vo == null)
			return null;
		PomagaloVO pom = (PomagaloVO) vo;
		switch (kolonas) {
		case 0:
			return pom.getSifraArtikla();
		case 1:
			return pom.getNaziv();
		default:
			return null;
		}
	}// getValueAt

	public boolean setValueAt(PomagaloVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(PomagaloVO vo, int kolona) {
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
	private PomagaloVO constructPomagalo(ResultSet rs) throws SQLException {
		PomagaloVO pom = new PomagaloVO();

		pom.setSifraArtikla(rs.getString("sifra"));
		pom.setNaziv(rs.getString("naziv"));
		pom.setPoreznaSkupina(Integer.valueOf(rs.getInt("porezna_stopa")));
		pom.setStatus(rs.getString("status").charAt(0));
		pom.setCijenaSPDVom(Integer.valueOf(rs.getInt("po_cijeni")));
		pom.setOptickoPomagalo(Boolean.valueOf(rs.getString("ocno_pomagalo")
				.equals(DAO.DA) ? true : false));
		pom.setCreated(rs.getTimestamp("created").getTime());
		pom.setCreatedBy(rs.getInt("created_by"));
		Timestamp updated = rs.getTimestamp("updated");
		if (updated!=null)
		pom.setLastUpdated(updated.getTime());
		else 
			pom.setLastUpdated(0L);
		pom.setLastUpdatedBy(rs.getInt("updated_by"));
		
		return pom;
	}// constructPredlozak

	public void clearFromCache(ValueObject vo) {
		if (vo == null)
			return;
		String kljuc = "" + vo.getSifra();
		clearFromCache(kljuc);
	}

	public void clearFromCache(String kljuc) {
		if (kljuc == null)
			return;

		if (this.pomagalaCache.containsKey(kljuc)) {
			this.pomagalaCache.remove(kljuc);
		}
	}

}// Pomagala
