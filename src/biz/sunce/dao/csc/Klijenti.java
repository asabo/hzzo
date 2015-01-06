/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.CacheabilniDAO;
import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.MjestoDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.DrzavaVO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.PictureUtil;
import biz.sunce.util.Util;

/**
 * datum:2005.04.26
 * 
 * @author dstanic izmjenio 08.05.05. - asabo - dodana metoda find() dorada
 *         14.05.05. - dstanic -
 */
public final class Klijenti extends CacheabilniDAO<KlijentVO> implements KlijentDAO {
	private static String[] zaglavlja = { "ime", "prezime", "mjesto", "dat.roð" };
	private MjestoDAO mjestoDAO = null;
	private String tablica = "KLIJENTI";

	public MjestoDAO getMjestoDAO() {
		if (this.mjestoDAO == null)
			this.mjestoDAO = DAOFactory.getInstance().getMjesta();
		return this.mjestoDAO;
	}

	public void delete(Object obj) throws SQLException {
		KlijentVO klijentVO = null;

		if (obj instanceof Integer) {
			klijentVO = (KlijentVO) this.read(obj);//
		}
		if (obj instanceof KlijentVO) {
			klijentVO = (KlijentVO) obj;
		} else
			throw new SQLException("Æaæu zajebaji");
		
		klijentVO.setStatus(STATUS_DELETED.charAt(1));
		this.update(klijentVO);
		
		super.izbaciIzCachea(klijentVO.getSifra());
	}// delete
		// -------------------------------- METODE VEZANE UZ
		// PRIKAZ----------------------------------

	public Class<?> getColumnClass(int columnIndex) {
		 
			switch (columnIndex) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				return STRING_CLASS;// Sva 4 case-a vracaju
															// String
			default:
				return null;
			}// switch
		 
	}// getColumnClass

	public int getColumnCount() {
		return zaglavlja.length;
	}

	public String getColumnName(int rb) {
		return zaglavlja[rb];
	}

	public Object getValueAt(KlijentVO vo, int column) {

		if (vo == null)
			return null;
		KlijentVO klijent =  vo;
		switch (column) {
		case 0:
			return klijent.getIme();
		case 1:
			return klijent.getPrezime();
		case 2:
			return klijent.getMjesto() != null ? klijent.getMjesto().toString()
					: null;
		case 3:			
			return klijent.getDatRodjenja()==null?null:Util.convertCalendarToString(klijent.getDatRodjenja());
		default:
			return null;
		}
	}// getValueAt

	public Class<KlijentVO> getVOClass() throws ClassNotFoundException {
		return biz.sunce.opticar.vo.KlijentVO.class;
	}

	public boolean setValueAt(KlijentVO vo, Object vrijednost, int kolona) {
		return false;// metoda se nece nikada pozvati jer isCellEditable vraca
						// false
	}

	public boolean isCellEditable(KlijentVO vo, int kolona) {
		return false; // Tablica ne moze biti mijenjatri read only
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

	// -------------------------------- METODE VEZANE UZ
	// PRIKAZ----------------------------------

	public void insert(KlijentVO obj) throws SQLException {
		KlijentVO ulaz = (KlijentVO) obj;
		if (ulaz == null)
			throw new SQLException("ulazna vrijednost je null!");

		int sifra = DAO.NEPOSTOJECA_SIFRA;

		final String upit = " INSERT INTO " + tablica + "	(sifra,"
				+ "	ime,"
				+ "	prezime,"
				+ "	adresa,"
				+ "	spol,"
				+ "	datrodjenja,"
				+ "	datupisa,"
				+ "	tel,"
				+ "	gsm,"
				+ "	email,"// 10 9
				+ "	zanimanje," // 10 za ?
				+ "	jmbg,"
				+ "	hzzo,"
				+ "	umro,"
				+ "	preporucio,"
				+ "	sifmjesta,"
				+ "	slijedecipregled,"
				+ "	created," // 17
				+ "	updated,"
				+ "	slika," // 20 19
				+ "	created_by," // 20 za ?
				+ "	updated_by,"
				+ "	mjesto,"
				+ "	drzava,"
				+ "	zip,"
				+ " prima_info," // 18.10.2005. -asabo- dodano
				+ " napomena)" // 23.02.2006. -asabo- dodano

				+ " VALUES ( "
				+ (sifra = DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(
						tablica, "sifra")) + "," + " ?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?," + " ?,?,?,?,?,?)";

		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();

		ps = conn.prepareStatement(upit);

		ps.setString(1, ulaz.getIme());
		ps.setString(2, ulaz.getPrezime());
		ps.setString(3, ulaz.getAdresa());
		ps.setString(4, ""
				+ (ulaz.getSpol() != null ? ulaz.getSpol().charValue() : 'M'));
		ps.setDate(5, ulaz.getDatRodjenja() == null ? null : new java.sql.Date(
				ulaz.getDatRodjenja().getTime().getTime()));
		ps.setDate(6, new java.sql.Date(ulaz.getDatUpisa().getTime().getTime()));
		ps.setString(7, ulaz.getTel());
		ps.setString(8, ulaz.getGsm());
		ps.setString(9, ulaz.getEmail());
		ps.setString(10, ulaz.getZanimanje());
		ps.setString(11, ulaz.getJmbg());
		ps.setString(12, ulaz.getHzzo());
		ps.setString(13, ulaz.getUmro() != null
				&& ulaz.getUmro().booleanValue() ? DA : NE); // 10.05.05.
																// -asabo-
																// DAO.DA i
																// DAO.NE
																// uvedeni

		if (ulaz.getPreporucio() != null)
			ps.setInt(14, ulaz.getPreporucio().getSifra().intValue()); // 10.05.05
																		// -asabo-
																		// getPreporucio()
																		// nije
																		// vise
																		// integer
		else
			ps.setNull(14, Types.INTEGER);

		// broj 15 (mjesto) se dolje malo nize unosi (kompliciraniji unos)

		if (ulaz.getSlijedeciPregled() != null)
			ps.setDate(16, new java.sql.Date(ulaz.getSlijedeciPregled()
					.getTime().getTime()));
		else
			ps.setNull(16, Types.DATE);

		ps.setDate(17, new java.sql.Date(ulaz.getCreated())); // created

		ps.setNull(18, Types.TIMESTAMP); // updated

		// slika
		BufferedImage im = ulaz.getSlika();
		if (im != null) {
			byte[] ins = PictureUtil.preformatirajSlikuKaoJPEG(im, 1.00f);
			ByteArrayInputStream bins = new ByteArrayInputStream(ins);
			ps.setBinaryStream(19, bins, bins.available());
			// nema potrebe zatvarati bins!
		} else
			ps.setNull(19, Types.BLOB);

		ps.setInt(20, GlavniFrame.getSifDjelatnika()); // created by

		// pri insertiranju nema updated_by, mora biti null...
		ps.setNull(21, Types.INTEGER);

		if (ulaz.getSifMjesta() != null
				&& ulaz.getSifMjesta().intValue() != NEPOSTOJECA_SIFRA) {// Mjesto
																			// postoji
																			// u
																			// bazi
																			// sve
																			// dalje
																			// ide
																			// ok
			ps.setInt(15, ulaz.getSifMjesta().intValue());
			ps.setNull(22, Types.VARCHAR);
			ps.setNull(23, Types.VARCHAR);
			ps.setNull(24, Types.INTEGER);
		} else {// Mjesto ne postoji u bazi....
			ps.setNull(15, Types.INTEGER);// Fk postavljamo na null ako,
											// izvrsavanje programa dodje do tu
											// mora biti null !!!!!
			ps.setString(22, ulaz.getMjesto().getNaziv()); // bubligen upisan
															// sabo :)
			ps.setString(23, ulaz.getMjesto().getDrzava().getNaziv()); // e
																		// jesmo
																		// ga tu
																		// zakomplicirali

			if (ulaz.getMjesto().getZip() != null
					&& ulaz.getMjesto().getZip().intValue() != NEPOSTOJECA_SIFRA)
				ps.setInt(24, ulaz.getMjesto().getZip().intValue());
			else
				ps.setNull(24, Types.INTEGER);

		}// else Mjesto ne postoji u bazi

		ps.setString(25, ulaz.isPrimaInfo() ? DA : NE); // 18.10.05. -asabo-
														// dodano

		ps.setString(26, ulaz.getNapomena()); // 23.02.06. -asabo- dodano

		try {
			int kom = ps.executeUpdate();
			if (kom == 1)
				ulaz.setSifra(Integer.valueOf(sifra)); // 28.02.06. -asabo- da se
													// zna da je objekt uspjesno
													// insertiran
		} catch (SQLException e) {
			Logger.fatal("Greska kod inserta klijenta sifra:" + sifra
					+ " naziv: " + ulaz.getIme() + " " + ulaz.getPrezime(), e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				ps=null;
			} catch (SQLException e1) {
			}
			if (conn != null)
				DAOFactory.freeConnection(conn);
		}
	}// insert


	final String upitRead = "SELECT " + "			sifra," + "			ime," + "			prezime,"
			+ "			adresa," + "			spol," + "			datrodjenja,"
			+ "			datupisa," + "			tel," + "			gsm," + "			email,"
			+ "			zanimanje," + "			jmbg," + "			hzzo," + "			umro,"
			+ "			preporucio," + "			sifmjesta," + "			slijedecipregled,"
			+ "			created," + "			updated," + "			slika,"
			+ "			created_by," + "			updated_by," + "			mjesto,"
			+ "			drzava," + "			zip," + "    prima_info," // 13.02.06.
															// -asabo-
															// dodano
			+ "    napomena" // 23.02.06. -asabo- dodano
			+ " FROM klijenti" + " WHERE ";
	
	public KlijentVO read(Object obj) throws SQLException {

		KlijentVO klijent = null;
		Integer ulazSifra = null;
		String ulazIme = null;

		if (obj instanceof Integer) {
			ulazSifra = (Integer) obj;
			klijent = super.povuciIzCachea(ulazSifra);
			if (klijent!=null)
				return klijent;			
		}
		if (obj instanceof String) {
			ulazIme = (String) obj;
		}
		
		String upit = upitRead;

		// nadograðivanje upita !!!! ocemo radit kombinacije ???? ostaje za
		// vidit
		if (ulazSifra != null) {
			upit += " sifra = ?";
		}
		if (ulazIme != null) {
			upit += " ime like ?";
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DAOFactory.getConnection();
		ps = conn.prepareStatement(upit);

		if (ulazSifra != null) {
			ps.setInt(1, ulazSifra.intValue());
		}

		if (ulazIme != null) {
			ps.setString(1, "%" + ulazIme + "%");
		}

		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				klijent = (constructKlijent(rs));
				super.ubaciUCache(klijent.getSifra(), klijent);
			}
		} catch (SQLException e) {
			Logger.warn("Problem kod povlacenja klijenta sa objektom: "+obj, e);
		} finally {
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sql) {
			}
			try {
				if (ps != null)
					ps.close(); ps=null;
			} catch (SQLException sql1) {
			}
			DAOFactory.freeConnection(conn); conn=null;
		}
		
		return klijent;
	}

	public boolean update(KlijentVO obj) throws SQLException {
		KlijentVO ulaz = (KlijentVO) obj;
		if (ulaz == null)
			throw new SQLException("ulazna vrijednost je null!");
		final String upit = " UPDATE KLIJENTI SET " + "		ime=?," + "		prezime=?,"
				+ "		adresa=?," + "		spol=?," + "		datrodjenja=?,"
				+ "		datupisa=?," + "		tel=?,"
				+ "		gsm=?,"
				+ "		email=?,"
				+ "		zanimanje=?," // 10
				+ "		jmbg=?," + "		hzzo=?," + "		umro=?," + "		preporucio=?,"
				+ "		sifmjesta=?," + "		slijedecipregled=?," + "		created=?,"
				+ "		updated=current_timestamp," // SAD je updateirano..
				+ "		slika=?," + "		" // created_by=? NECE SE MIJENJATI
				+ "		updated_by=?," + "		mjesto=?,"// 20
				+ "		drzava=?," + "		zip=?," + "   prima_info=?," // 16.12.05.
																	// -asabo-
																	// dodano
				+ "   napomena=?," // 23.02.06. -asabo- dodano
				+ "   status=?" // 16.03.06. -asabo- dodano
				+ "	WHERE sifra=?";

		// tODO dodaj u upit mjesto,drzava,zip za slucaj da mjestoVO ima sifru
		// DAO.NEPOSTOJECA_SIFRA (tj. -1)
		// u mjestoVO ces imati potrebne podatke o nazivu egzoticnog mjesta,
		// drzavi i ZIP-u (ako je zip 0 ili -1 upisujes null)
		// ako sifra mjesta postoji tada su ova tri podatka null
		// TODO Sabo Provjeri nadam se da je ovo sredeno !!!

		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();

		ps = conn.prepareStatement(upit);

		ps.setString(1, ulaz.getIme());
		ps.setString(2, ulaz.getPrezime());
		ps.setString(3, ulaz.getAdresa());
		ps.setString(4, "" + ulaz.getSpol().charValue());
		ps.setDate(5, new java.sql.Date(ulaz.getDatRodjenja().getTime()
				.getTime()));
		ps.setDate(6, new java.sql.Date(ulaz.getDatUpisa().getTime().getTime()));
		ps.setString(7, ulaz.getTel());
		ps.setString(8, ulaz.getGsm());
		ps.setString(9, ulaz.getEmail());
		ps.setString(10, ulaz.getZanimanje());
		ps.setString(11, ulaz.getJmbg());
		ps.setString(12, ulaz.getHzzo());
		ps.setString(13, ulaz.getUmro().booleanValue() ? DAO.DA : DAO.NE);

		if (ulaz.getPreporucio() != null)
			ps.setInt(14, ulaz.getPreporucio().getSifra().intValue()); // 10.05.05.
																		// -asabo-
																		// nema
																		// vise
																		// integera
		else
			ps.setNull(14, Types.INTEGER);

		// 12.05.05. -asabo- slijedeciPregled moze biti i null (tj. nema ga)
		if (ulaz.getSlijedeciPregled() != null)
			ps.setDate(16, new java.sql.Date(ulaz.getSlijedeciPregled()
					.getTime().getTime()));
		else
			ps.setNull(16, Types.DATE);

		ps.setDate(17, new java.sql.Date(ulaz.getCreated()));

		// slika
		BufferedImage im = ulaz.getSlika();
		if (im != null) {
			byte[] ins = PictureUtil.preformatirajSlikuKaoJPEG(im, 1.00f);
			ByteArrayInputStream bins = new ByteArrayInputStream(ins);
			ps.setBinaryStream(18, bins, bins.available());
			// nema potrebe zatvarati bins!
		} else
			ps.setNull(18, Types.BLOB);

		ps.setInt(19, GlavniFrame.getSifDjelatnika());

		if (ulaz.getSifMjesta() != null
				&& ulaz.getSifMjesta().intValue() != NEPOSTOJECA_SIFRA) {// Mjesto
																			// postoji
																			// u
																			// bazi
																			// sve
																			// dalje
																			// ide
																			// ok
			ps.setInt(15, ulaz.getSifMjesta().intValue());
			ps.setNull(20, Types.VARCHAR);// postavljaju se na null vrijednosti
			ps.setNull(21, Types.VARCHAR);// -||-
			ps.setNull(22, Types.INTEGER);// -||-
		} else {// Mjesto ne postoji u bazi....
			ps.setNull(15, Types.INTEGER);
			ps.setString(20, ulaz.getMjesto().getNaziv());
			ps.setString(21, ulaz.getMjesto().getDrzava().getNaziv());

			if (ulaz.getMjesto().getZip() != null
					&& ulaz.getMjesto().getZip().intValue() != NEPOSTOJECA_SIFRA) {
				ps.setInt(22, ulaz.getMjesto().getZip().intValue());
			} else {
				ps.setNull(22, Types.INTEGER);
			}
		}

		// 16.12.05. -asabo- dodana jos jedna kolona
		ps.setString(23, ulaz.isPrimaInfo() ? DAO.DA : DAO.NE);

		ps.setString(24, ulaz.getNapomena()); // 23.02.06. -asabo- dodano

		if (ulaz.getSifra() == null || ulaz.getSifra().intValue() == -1) {
			Logger.fatal(
					"Nemoguca situacija, sifra je null ili -1 kod updatea klijenta: sifra="
							+ ulaz.getSifra(), null);
			return false;
		}

		// 16.03.06. -asabo- dodano
		ps.setString(25, "" + ulaz.getStatus());

		// ne moze biti -1, mora biti sifra
		ps.setInt(26, ulaz.getSifra().intValue());// Where

		try {
			ps.executeUpdate();
			
			super.izbaciIzCachea(ulaz.getSifra());
			
		} 
		catch (SQLException e) {
			Logger.fatal("Greska kod update klijenta", e);
			return false;
		} finally {
			try {
				if (ps != null)
					ps.close(); ps=null;
			} catch (SQLException e1) {
			}
			DAOFactory.freeConnection(conn); conn=null;
		}
		return true;
	}// update

	final String upitFindAll = "SELECT " + "		klijenti.sifra," + "		ime," + "		prezime,"
			+ "		adresa," + "		spol," + "		datrodjenja," + "		datupisa,"
			+ "		tel," + "		gsm," + "		email," + "		zanimanje," + "		jmbg,"
			+ "		hzzo," + "		umro," + "		preporucio," + "		sifmjesta,"
			+ "		slijedeciPregled," + "		klijenti.created,"
			+ "		klijenti.updated," + "		slika," + "		klijenti.created_by,"
			+ "		klijenti.updated_by," + "	mjesto,"
			+ "	drzava,"
			+ " prima_info," // 16.12.05. -asabo- dodano
			+ "	zip,"
			+ " napomena" // 23.02.06. -asabo- dodano
			+ " FROM " + "		klijenti" + " WHERE klijenti.status<>"
			+ STATUS_DELETED;
	
	public List<KlijentVO> findAll(Object kljuc) throws SQLException {
		// 22.07.05. -asabo- dodan datum kao ulazni parametar. Ako postoji datum
		// kao ul.p.
		// onda se vraca nazad popis klijenata koji bi trebali u to vrijeme
		// zakazati pregled

		Calendar datum = null;
		 
		SearchCriteria kriterij = null;

		if (kljuc != null && kljuc instanceof Calendar) {
			datum = (Calendar) kljuc;

		} else if (kljuc != null && kljuc instanceof SearchCriteria) {
			kriterij = (SearchCriteria) kljuc;

		}

		String upit = upitFindAll;

		if (datum != null) {
			String d = Util.convertCalendarToStringForSQLQuery(datum);
			String ld, ld2;
			java.util.Calendar datum2 = Calendar.getInstance();
			// datum2.setTimeInMillis(datum.getTimeInMillis());
			datum2.setTime(datum.getTime());

			int dan = datum.get(Calendar.DAY_OF_MONTH);
			dan += DAO.PREGLEDI_DANA_ZA_ZAKAZATI_PREGLED_UNAPRIJED_SE_GLEDA;
			boolean l = datum.isLenient();
			datum.setLenient(true);
			datum.set(Calendar.DAY_OF_MONTH, dan);
			String d2 = Util.convertCalendarToStringForSQLQuery(datum);
			datum.setLenient(l); // vratiti nazad na staro...

			// Timestamp t1=new Timestamp(ld);
			// Timestamp t2=new Timestamp(ld2);

			ld = d + " 00:00:00.000000000";
			ld2 = d2 + " 00:00:00.000000000";

			// slijedeci pregled...
			upit += " and slijedeciPregled>='"
					+ d
					+ "' and slijedeciPregled<='"
					+ d2
					+ "' "
					+ " and klijenti.sifra not in ((select pregledi.sifKlijenta from pregledi where "
					+ " klijenti.sifra=pregledi.sifKlijenta and (date(pregledi.datVrijeme)>='"
					+ d + "' and date(pregledi.datVrijeme)<='" + d2 + "')) )";
		}// if datum!=null
		else
		// 17.08.05. -asabo-
		if (kriterij != null
				&& kriterij.getKriterij().equals(
						DAO.KRITERIJ_KLIJENT_DATUM_RODJENJA)) {
			List<?> pod = kriterij.getPodaci();
			if (pod == null)
				return null;
			datum = (Calendar) pod.get(0);

			Calendar d2 = Calendar.getInstance();
			// d2.setTimeInMillis(datum.getTimeInMillis());
			d2.setTime(datum.getTime());
			d2.setLenient(true);
			d2.set(Calendar.DAY_OF_MONTH, datum.get(Calendar.DAY_OF_MONTH)
					+ DAO.PREGLEDI_DANA_ZA_GLEDATI_RODJENDAN);

			upit += " and (datRodjenja is not null) and (day(datRodjenja)+month(datRodjenja)*31)>="
					+ (datum.get(Calendar.DAY_OF_MONTH) + (datum
							.get(Calendar.MONTH) + 1) * 31)
					+ " and not( (day(datRodjenja)+month(datRodjenja)*31)>"
					+ (d2.get(Calendar.DAY_OF_MONTH) + (d2.get(Calendar.MONTH) + 1) * 31)
					+ ")";

		}// if kriterij nije null
		else
		// 26.01.07. -asabo-
		if (kriterij != null
				&& kriterij.getKriterij().equals(
						DAO.KRITERIJ_KLIJENT_LIMIT_1000)) {
			List<?> pod = kriterij.getPodaci();
			String filter = null;
			if (pod == null || pod.size() < 1)
				return null;
			filter = (String) pod.get(0);

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
			} else {
				if (pod != null)
					pod.clear();
				return null;
			}
			if (pod != null)
				pod.clear();
		}// if kriterij nije null

		ResultSet rs = null;
		if (kriterij != null
				&& kriterij.getKriterij().equals(
						DAO.KRITERIJ_KLIJENT_LIMIT_1000))
			rs = DAOFactory.performQuery(upit, 1000);
		else
			rs = DAOFactory.performQuery(upit);

		List<KlijentVO> lista = new ArrayList<KlijentVO>();
		try {
			if (rs != null)
				while (rs.next()) {
					lista.add(constructKlijent(rs));
				}
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod klijenti.findAll", e);
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.getStatement().close();
			} catch (SQLException e) {
			}
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException sqle) {
			}
		}
		return lista;
	}// findAll

	private KlijentVO constructKlijent(ResultSet rs) throws SQLException {
		KlijentVO k = new KlijentVO();
		// Integer not null
		k.setSifra(rs.getInt("sifra"));
		k.setCreatedBy(Integer.valueOf(rs.getInt("created_by")));

		// Stringovi
		k.setIme(rs.getString("ime"));
		k.setPrezime(rs.getString("prezime"));
		k.setAdresa(rs.getString("adresa"));
		k.setTel(rs.getString("tel"));
		k.setGsm(rs.getString("gsm"));
		k.setEmail(rs.getString("email"));
		k.setZanimanje(rs.getString("zanimanje"));
		k.setJmbg(rs.getString("jmbg"));
		k.setHzzo(rs.getString("hzzo"));
		// 10.05.05. - asabo - preporucio ne moze biti integer broj, vec
		// KlijentVO objekt
		int prep = rs.getInt("preporucio");

		if (rs.wasNull())
			k.setPreporucio(null);
		else
			k.setPreporucio((KlijentVO) this.read(Integer.valueOf(prep))); // ili ce
																		// ovdje
																		// ici
																		// this.find()
																		// ?

		int sifMjesta = rs.getInt("sifmjesta");

		if (rs.wasNull()) {
			// TODO Stanicu pogledaj sta sam radio pa se buni
			// 10.05.05. - asabo - dodatak
			// ako sifra mjesta ne postoji, covjek je rucno unio nazive mjesta,
			// postanski broj i drzavu
			// i u tom slucaju podaci se nalaze u tablici klijenti, koje
			// ucitavamo, te instanciramo objekte
			// klase MjestoVO i DrzavaVO koje u sebi nose oznaku nepostojece
			// sifre (-1) te nazive mjesta/drzave
			// na taj nacin svi daljnji pozivi selektora prema tim objektima su
			// potpuno isti kao da postoji
			// normalno mjesto
			MjestoVO mvo = new MjestoVO();
			DrzavaVO dvo = new DrzavaVO();
			Integer minus1 = Integer.valueOf(NEPOSTOJECA_SIFRA);

			mvo.setSifra(minus1);
			mvo.setNaziv(rs.getString("mjesto"));
			mvo.setZip(Integer.valueOf(rs.getInt("zip")));
			if (rs.wasNull())
				mvo.setZip(null);
			dvo.setSifra(minus1);
			dvo.setNaziv(rs.getString("drzava"));
			mvo.setDrzava(dvo);
			k.setMjesto(mvo);
		} else {
			k.setMjesto((MjestoVO) getMjestoDAO().read(Integer.valueOf(sifMjesta)));
		}

		k.setLastUpdatedBy(Integer.valueOf(rs.getInt("updated_by")));
		if (rs.wasNull())
			k.setLastUpdatedBy(null);

		// timestamp
		k.setCreated(rs.getDate("created").getTime());
		java.sql.Date tmp = rs.getDate("updated");
		k.setLastUpdated(tmp != null ? tmp.getTime() : 0L);

		BufferedImage bi;
		InputStream ins = rs.getBinaryStream("slika");

		if (ins != null) {
			bi = PictureUtil.vratiKaoBufferedImage(ins);
			k.setSlika(bi);
		} else if (rs.wasNull())
			k.setSlika(null);

		Calendar cal = Calendar.getInstance();// Instanca kalendara
		Date date = rs.getDate("datrodjenja");
		if (date != null){
			cal.setTime(date);
		k.setDatRodjenja(cal);
		}
		else k.setDatRodjenja(null);
		cal = Calendar.getInstance();
		cal.setTime(rs.getDate("datupisa"));
		k.setDatUpisa(cal);

		// 06.10.05. -asabo- dodan datum slijedecg pregleda (nedostajao)
		java.sql.Date d = rs.getDate("slijedeciPregled");
		if (d != null) {
			cal = Calendar.getInstance();
			cal.setTime(d);
			k.setSlijedeciPregled(cal);
		}// ako datum postoji

		k.setSpol(new Character(rs.getString("spol").charAt(0)));
		// 16.12.05. -asabo- izmjenio, bilo je rs.getString("spol")
		k.setUmro(Boolean.valueOf(
				rs.getString("umro").equalsIgnoreCase(DAO.DA) ? true : false));

		// 16.12.05. -asabo- dodao, treba znati jeli covjek hoce primati promo
		// ili ne
		k.setPrimaInfo(rs.getString("prima_info").equalsIgnoreCase(DAO.DA) ? true
				: false);

		// 23.02.06. -asabo- dodano, korisnik mora moci zapisati i nekakvu
		// napomenu za svog klijenta
		k.setNapomena(rs.getString("napomena"));
		return k;
	}// constructKlijent

	public GUIEditor<KlijentVO> getGUIEditor() {
		return null;
	}

	public String narusavaLiObjektKonzistentnost(KlijentVO objekt) {
		// TODO Auto-generated method stub
		return null;
	}
 

}// Klijenti klasa
