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
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DAOObjekt;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.PomagaloDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
import biz.sunce.util.RacuniUtil;
import biz.sunce.util.StringUtils;
import biz.sunce.util.Util;

/**
 * datum:2006.02.23
 * 
 * @author asabo
 * 
 */
public final class StavkeRacuna implements StavkaRacunaDAO {
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica = "stavke_racuna";
	private String[] kolone = { "šifra", "naziv", "kolièina", "kn" };

	private PomagaloDAO pomagala = null;

	final String select = "select sr.sif_racuna,sr.sif_artikla,sr.kolicina,sr.po_cijeni,sr.porezna_stopa,sr.sif_proizvodjaca,sr.tvrtka_sifra_art";
	final String from = " from " + tablica + " sr";

	public String narusavaLiObjektKonzistentnost(StavkaRacunaVO objekt) {
		StavkaRacunaVO s = (StavkaRacunaVO) objekt;
		boolean iso9999 = false;
		
		if (s == null)
			return "ispravnost praznog objekta se ne može provjeravati";

		String sifArtikla = s.getSifArtikla();
		int sifArtLen = sifArtikla == null ? -1 : sifArtikla.length();

		if (sifArtikla == null || (sifArtLen != 12 && sifArtLen > 9))
			return "šifra artikla u stavci raèuna nije ispravna!";

		if (sifArtLen == 12 && !StringUtils.imaSamoBrojeve(sifArtikla)) {
			return "šifra artikla " + sifArtikla
					+ " nije po ISO 9999 standardu";
		} else if (sifArtLen == 12 && StringUtils.imaSamoBrojeve(sifArtikla))
			iso9999 = true;

		if (s.getKolicina() == null || s.getKolicina().intValue() <= 0)
			return "kolièina u stavci raèuna nije ispravna!";

		if (s.getPoCijeni() == null || s.getPoCijeni().intValue() <= 0)
			return "cijena u stavci raèuna nije ispravna!";

		if (s.getSifRacuna() == null)
			return "stavka nije povezana na raèun!";

		// 15.09.06. -asabo- dodano
		if (!iso9999 && s.getEkstraSifProizvoda() != null
				&& s.getEkstraSifProizvoda().length() > 16)
			return "Vaša šifra nestandardnog proizvoda može biti dugaèka maks. 16 znakova";

		if (!iso9999
				&& (s.getSifProizvodjaca() == null || (s.getSifProizvodjaca()
						.intValue() <= 0)))
			return "Šifra proizoðaèa nije unešena ili nije ispravna!";

		return null;
	}// jeliObjektNarusavaKonzistentnost

	// treba metodi getValueAt()...
	private PomagaloDAO getPomagalaDAO() {
		if (this.pomagala == null)
			this.pomagala = DAOFactory.getInstance().getPomagala();
		return this.pomagala;
	}// getPomagala

	
	final String insUpit 
	= "INSERT INTO "
			+ tablica
			+ " "
			+ // INSERT da se ne zabunujemo vise
			"(sif_racuna,sif_artikla,kolicina,po_cijeni,porezna_stopa,sif_proizvodjaca,tvrtka_sifra_art)"
			+ " VALUES (?,?,?,?,?,?,?)";
	
	public void insert(Object objekt) throws SQLException 
	{
	 
		StavkaRacunaVO ul = (StavkaRacunaVO) objekt;

		if (ul == null)
			throw new SQLException("Insert " + tablica
					+ ", ulazna vrijednost je null!");
	
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();

			ps = conn.prepareStatement(insUpit);

			ps.setInt(1, ul.getSifRacuna().intValue());
			ps.setString(2, ul.getSifArtikla());
			ps.setInt(3, ul.getKolicina().intValue());
			ps.setInt(4, ul.getPoCijeni().intValue());
			ps.setInt(5, ul.getPoreznaStopa().intValue()); // 12.03.06. -asabo-
															// dodano
			Integer sfp = ul.getSifProizvodjaca();
			if (sfp == null)
				ps.setNull(6, Types.INTEGER);
			else
				ps.setInt(6, sfp.intValue()); // 28.06.06. -asabo- dodano

			ps.setString(7, ul.getEkstraSifProizvoda()); // 15.09.06. -asabo-
															// dodano

			int kom = ps.executeUpdate();
			// status updated ce se samo postaviti po defaultu...

			if (kom == 1) {
				ul.setSifra(ul.getSifRacuna()); // samo da se zna da je insert
												// uspjesno ubacen

				// ubaciti statisticku evidenciju o proizvodjacu, ne smije
				// utjecati na neuspjeh poziva ove metode
				this.azurirajStatistikuProizvodjacaZaArtikl(ul, true);

			}// if kom==1
			else {
				Logger.fatal("neuspio insert zapisa stavke racuna u tablicu "
						+ tablica, null);
				ul.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
				return;
			}

		}
		// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
		finally {
			try {
				if (ps != null)
					ps.close();
				 ps = null;
			} catch (SQLException e1) {
			}
			if (conn != null)
				DAOFactory.freeConnection(conn);
			  conn = null;
		}// finally

	}// insert

	@SuppressWarnings("unchecked")
	private final void azurirajStatistikuProizvodjacaZaArtikl(
			StavkaRacunaVO ul, boolean insertirano) {
		if (ul.getSifProizvodjaca() == null)
			return;

		String sifraArtikla = ul.getSifArtikla();
		boolean iso9999 = sifraArtikla != null && sifraArtikla.length() == 12
				&& StringUtils.imaSamoBrojeve(sifraArtikla);

		if (iso9999)
			return;

		Hashtable<String, Object> kriterij = new Hashtable<String, Object>(2);
		kriterij.put("SIF_ARTIKLA", ul.getSifArtikla());
		kriterij.put("SIF_PROIZVODJACA", ul.getSifProizvodjaca());
		ArrayList<ValueObject> l = null;

		DAOObjekt dao = DAOFactory.getInstance().getProizvodjaciProizvoda();

		try {
			l = (ArrayList<ValueObject>) dao.findAll(kriterij);
			ValueObject v = null;
			Integer brojac = null;
			// prvi artikl sa popisa je nas.. treba ga ucitati, povecati mu
			// brojac za jedan i spremiti ga nazad u db
			if (l != null && l.size() > 0) {
				v = (ValueObject) l.get(0);
				if (v != null) {
					brojac = (Integer) v.getValue("BROJAC");

					brojac = Integer.valueOf((brojac != null ? brojac
							.intValue() : 0)
							+ (insertirano ? ul.getKolicina().intValue() : 0));

					try {
						v.setValue("BROJAC", brojac);
					} catch (Exception e) {
					}

					dao.updateOrInsertObject(v);
					kriterij.clear();
					kriterij = null;
					l.clear();
					l = null;
					v = null;
				}// osigurac if postoji zapis
			}// if ima nesto
			else {
				v = new ValueObject();
				try {
					v.setValue("SIF_ARTIKLA", ul.getSifArtikla());
					v.setValue("SIF_PROIZVODJACA", ul.getSifProizvodjaca());
					v.setValue("BROJAC", Integer.valueOf(1));
				} catch (Exception e) {
					Logger.debug(
							"Kreiranje statistike proizvodjaca proizvoda iznimka kod kreiranja ValueObjecta",
							e);
				}

				dao.insert(v);

				kriterij.clear();
				kriterij = null;
				v = null;
			}// ako ne postoji
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod azuriranja statistike proizvodjaca za proizvod",
					e);
		} finally {
			if (kriterij != null) {
				kriterij.clear();
				kriterij = null;
			}
			if (l != null) {
				l.clear();
				l = null;
			}
		}
	}// azurirajStatistikuProizvodjacaZaArtikl
	
	final String updUpit=
			        "update "
					+ tablica
					+ " set "
					+ " kolicina=?,po_cijeni=?,porezna_stopa=?, sif_proizvodjaca=?, tvrtka_sifra_art=? "
					+ // 19.09.06 -asabo- dodana tvrtka_sifra_art
					" where sif_racuna=? and sif_artikla=?";
	
	// 23.02.06. -asabo- kreirano ali mislim da se nece koristiti ...
	public boolean update(Object objekt) throws SQLException
	{
		StavkaRacunaVO ul = (StavkaRacunaVO) objekt;

		if (ul == null)
			throw new SQLException("Update " + tablica
					+ ", ulazna vrijednost je null!");

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DAOFactory.getConnection();
			if (conn == null)
				throw new SQLException(
						"Veza prema bazi podataka je prazna (CSC StavkeRacuna)!");

			ps = conn.prepareStatement(updUpit);

			ps.setInt(1, ul.getKolicina().intValue());
			ps.setInt(2, ul.getPoCijeni().intValue());
			ps.setInt(3, ul.getPoreznaStopa().intValue());

			if (ul.getSifProizvodjaca() != null) // 28.06.06. -asabo- dodano
				ps.setInt(4, ul.getSifProizvodjaca().intValue());
			else
				ps.setNull(4, Types.INTEGER);

			ps.setString(5, ul.getEkstraSifProizvoda()); // 15.09.06 -asabo-
															// dodano
			ps.setInt(6, ul.getSifRacuna().intValue());
			ps.setString(7, ul.getSifArtikla());

			int kom = ps.executeUpdate();

			if (kom == 1) {
				// ubaciti statisticku evidenciju o proizvodjacu, ne smije
				// utjecati na neuspjeh poziva ove metode
				this.azurirajStatistikuProizvodjacaZaArtikl(ul, false);
			}

			return kom == 1;
		}
		// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
		finally {
			try {
				if (ps != null)
					ps.close(); ps=null;
			} catch (SQLException e1) {
			}
			if (conn != null)
				DAOFactory.freeConnection(conn); conn=null;
		}// finally
	}// update

	public void delete(Object kljuc) throws SQLException {
		final String upit;
		StavkaRacunaVO ul = (StavkaRacunaVO) kljuc;

		if (ul == null)
			throw new SQLException("delete from " + tablica
					+ ", ulazna vrijednost je null!");

		upit = "delete from " + tablica + " where " + " sif_racuna="
				+ ul.getSifRacuna().intValue() + " and sif_artikla='"
				+ ul.getSifArtikla() + "'";

		try {
			DAOFactory.performUpdate(upit);
			return;
		}
		// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
		finally {
		}// finally
	}// delete

	// 23.02.06. -asabo- kreirano
	public StavkaRacunaVO read(Object kljuc) throws SQLException {
		// za ovu metodu nema logike da se ikada pozove, ali eto...
		Integer sifra = null;

		if (kljuc instanceof Integer)
			sifra = (Integer) kljuc;

		if (sifra == null)
			throw new SQLException("sifra parametar je null kod " + tablica
					+ " read");

		final String upit = select + from + " where sif_racuna=" + sifra.intValue();

		ResultSet rs = null;

		rs = DAOFactory.performQuery(upit);

		try {
			if (rs != null && rs.next())
				return this.constructStavkaRacuna(rs);
		} finally {
			// try{if (rs!=null && rs.getStatement()!=null)
			// rs.getStatement().close();}catch(SQLException sqle){}
			try {
				if (rs != null)
					rs.close();
				rs=null;
			} catch (SQLException sqle) {
			}
		}
		// konkretan jedan objekt ne moze se dobiti
		return null;
	}// read

	// 08.01.06. -asabo- kreirano
	public final List<StavkaRacunaVO> findAll(Object kljuc) throws SQLException {
		ArrayList<StavkaRacunaVO> list = new ArrayList<StavkaRacunaVO>(32);
		String status = "";

		RacunVO racun = null;
		Integer sifRacuna = null;
		SearchCriteria krit = null; // 16.05.06. -asabo- dodano

		if (kljuc instanceof RacunVO)
			racun = (RacunVO) kljuc;
		else if (kljuc instanceof Integer)
			sifRacuna = (Integer) kljuc;
		else if (kljuc instanceof SearchCriteria)
			krit = (SearchCriteria) kljuc;

		String upit = select;

		if (racun != null)
			upit += from + " where sif_racuna=" + racun.getSifra().intValue();
		else if (sifRacuna != null)
			upit += from + " where sif_racuna=" + sifRacuna.intValue();
		else if (krit != null && krit.getKriterij() != null
				&& krit.getKriterij().equalsIgnoreCase("dnevnik")) {
			@SuppressWarnings("rawtypes")
			ArrayList l = (ArrayList) krit.getPodaci();
			Calendar dOd, dDo;
			dOd = (Calendar) l.get(0);
			dDo = (Calendar) l.get(1);

			upit = "select r.sifra,r.datum_izdavanja,sr.sif_artikla,sr.sif_proizvodjaca as srsfp,sr.kolicina,a.naziv, k.ime, k.prezime, r.broj_potvrde1, r.broj_potvrde2,r.BROJ_OSOBNOG_RACUNA_OSN,r.osnovno_osiguranje,r.iznos_sudjelovanja,sr.po_cijeni,r.sif_lijecnika,0 as tvrtka_sifra_art"
					+ " from stavke_racuna sr,racuni r,artikli a,klijenti k"
					+ " where r.sifra=sr.sif_racuna and k.sifra=r.sif_klijenta"
					+ " and a.sifra=sr.sif_artikla and r.datum_izdavanja>='"
					+ Util.convertCalendarToStringForSQLQuery(dOd)
					+ "'"
					+ " and r.datum_izdavanja<='"
					+ Util.convertCalendarToStringForSQLQuery(dDo)
					+ "'"
					+ " and r.status!='D' and r.roba_isporucena='D' order by datum_izdavanja desc";
		}
		if (krit != null) {
			status = "P"; // po statusu cemo znati kako ispisivati van podatke..
			upit = "select min(sr.sif_racuna) as sif_racuna,sr.sif_artikla,sum(sr.kolicina) as kolicina,sum(sr.kolicina*sr.po_cijeni) "
					+ "as po_cijeni,0 as tvrtka_sifra_art,min(sr.porezna_stopa) as porezna_stopa,0 as sif_proizvodjaca ";
			upit += from + ",racuni r where sr.sif_racuna=r.sifra";
			@SuppressWarnings("rawtypes")
			ArrayList l = (ArrayList) krit.getPodaci();
			Calendar dOd, dDo;
			dOd = (Calendar) l.get(0);
			dDo = (Calendar) l.get(1);
			LijecnikVO lvo = (LijecnikVO) l.get(2);
			MjestoVO mvo = (MjestoVO) l.get(3);
			String art = (String) l.get(4);

			upit += " and r.datum_izdavanja>='"
					+ Util.convertCalendarToStringForSQLQuery(dOd) + "'"
					+ " and r.datum_izdavanja<='"
					+ Util.convertCalendarToStringForSQLQuery(dDo) + "'";

			if (lvo != null)
				upit += " and r.sif_lijecnika=" + lvo.getSifra().intValue();

			if (mvo != null)
				upit += " and r.sif_podruznice=" + mvo.getSifra().intValue();

			if (art != null && art.length() > 0)
				upit += " and sr.sif_artikla like '" + art + "%'";

			upit += " group by sr.sif_artikla order by sr.sif_artikla";

			/*
			 * l.add(dOd.getDatum()); l.add(dDo.getDatum());
			 * l.add(this.oznaceniLijecnik); // bio null, ne bio..
			 * l.add(this.oznacenoMjesto);
			 * l.add(this.jtArtikl.getText().trim().toUpperCase());
			 */
		}// if kriterij postoji

		ResultSet rs = null;

		try {
			rs = DAOFactory.performQuery(upit);

			try {

				if (rs != null)
					while (rs.next()) {
						StavkaRacunaVO svo = constructStavkaRacuna(rs);
						if (!status.equals(""))
							svo.setStatus(status.charAt(0));
						list.add(svo);
					}// while

				return list;
			} finally {
				// try{if (rs!=null && rs.getStatement()!=null)
				// rs.getStatement().close();}catch(SQLException sqle){}
				try {
					if (rs != null)
						rs.close();
					rs = null;
				} catch (SQLException sqle) {
				}
			}
		} catch (Exception e) {
			if (!(e instanceof SQLException))
				Logger.fatal(
						"Opæa iznimka kod traženja stavki raèuna za raèun: "
								+ (sifRacuna != null ? sifRacuna.intValue()
										: -1) + " ili " + racun != null ? racun
								.getBrojOsobnogRacunaOsnovno() : "null", e);
			else
				throw (SQLException) e;
		}
		
		return null;
	}// findAll

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Class getVOClass() throws ClassNotFoundException {
		return StavkaRacunaVO.class;
	}

	@SuppressWarnings("unchecked")
	public GUIEditor<StavkaRacunaVO> getGUIEditor() {
		try {
			return (GUIEditor<StavkaRacunaVO>) Class
					.forName(DAO.GUI_DAO_ROOT + ".StavkaRacuna").newInstance();

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
			Logger.log("Nema GUI klase Racun?!?", e);
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

	public final int getColumnCount() {
		return kolone.length;
	}

	static Class<?> stringClass;
	static Class<?> intClass;

	static {
		try {
			stringClass = Class.forName("java.lang.String");
			intClass = Class.forName("java.lang.Integer");
		} catch (ClassNotFoundException cnfe) {
			Logger.fatal(
					tablica
							+ " CSC - String, Integer ili Float kao klasa ne postoji?!?",
					cnfe);
			stringClass = null;
			intClass = null;
		}
	}

	public final Class<?> getColumnClass(int columnIndex) {

		switch (columnIndex) {
		case 0:
		case 1:
			return stringClass;
		case 2:
			return intClass;
		case 3:
			return stringClass;
		default:
			return stringClass;
		}

	}// getColumnClass

	public final Object getValueAt(StavkaRacunaVO vo, int kolonas) {
		if (vo == null)
			return null;
		StavkaRacunaVO s = (StavkaRacunaVO) vo;

		switch (kolonas) {
		case 0:
			return s.getSifArtikla();
		case 1:
			try {
				PomagaloVO pvo = (PomagaloVO) this.getPomagalaDAO().read(
						s.getSifArtikla());
				return pvo != null ? pvo.getNaziv() : "?!?";
			} catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod traženja pomagala za šifru:"
								+ s.getSifArtikla(), e);
			}
		case 2:
			return s.getKolicina();
		case 3:
			if (s.getStatus() == 'P')
				return srediIznos(s.getPoCijeni().intValue());
			else
				return srediIznos(RacuniUtil.getBruttoIznosStavke(s));

		default:
			return null;
		}
	}// getValueAt

	private String srediIznos(int iznos) {
		int kn, lp;
		kn = iznos / 100;
		lp = iznos % 100;
		StringBuilder sb = new StringBuilder(16);

		sb.append(kn);
		sb.append(".");
		if (lp < 10) {
			sb.append("0");
			sb.append(lp);
		} else
			sb.append(lp);

		String fiz = sb.toString();
		sb = null;
		return fiz;
	}// srediIznos

	public boolean setValueAt(StavkaRacunaVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(StavkaRacunaVO vo, int kolona) {
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
	private final StavkaRacunaVO constructStavkaRacuna(ResultSet rs)
			throws SQLException {
		StavkaRacunaVO svo = new StavkaRacunaVO();

		svo.setSifRacuna(Integer.valueOf(rs.getInt("sif_racuna")));
		svo.setSifArtikla(rs.getString("sif_artikla"));
		svo.setKolicina(Integer.valueOf(rs.getInt("kolicina")));
		svo.setPoCijeni(Integer.valueOf(rs.getInt("po_cijeni")));
		svo.setPoreznaStopa(Integer.valueOf(rs.getInt("porezna_stopa")));
		svo.setEkstraSifProizvoda(rs.getString("tvrtka_sifra_art")); // 15.09.06.
																		// -asabo-
																		// dodano
		svo.setSifraVelicineObloge(null);

		int sfp = rs.getInt("sif_proizvodjaca");

		if (!rs.wasNull())
			svo.setSifProizvodjaca(Integer.valueOf(sfp));
		else
			svo.setSifProizvodjaca(null);

		return svo;
	}// constructRacun

}// StavkeRacuna
