package biz.sunce.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.optika.net.Kolona;

/**
 * <p>
 * Title: Projekt Optièar
 * </p>
 *
 * <p>
 * Description: aplikacija za voðenje optièke radnje
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 *
 * @author Ante Sabo; Davor Staniæ
 * @version 0.8
 */
public class DAOObjekt implements DAOSaKontrolomKonzistencije<ValueObject> {

	boolean imaCreated, imaUpdated;
	private int[] kljucevi; // redni brojevi kolona
	private String[] kolone;
	List listaKljuceva = null;
	List listaKolona = null;
	private String naziv;
	private String select, insert, update, read;
	private int[] tipoviKolona; // java.sql.Types za svaku kolonu

	public class OrderByZapis {
		private String kolona;
		private boolean asc = true;

		public String getKolona() {
			return this.kolona;
		}

		public void setKolona(String kolona) {
			this.kolona = kolona;
		}

		public boolean isAsc() {
			return this.asc;
		}

		public void setAsc(boolean asc) {
			this.asc = asc;
		}
	}

	private java.util.Vector orderByKolone = null;

	public DAOObjekt(String naziv) throws Exception {
		this.setNaziv(naziv);
		this.listaKolona = (ArrayList) this.getTableColumns(naziv);
		this.listaKljuceva = (ArrayList) this.getTablePrimaryKey(naziv);
		this.podesiRedneBrojeveKljucevaKolonama(this.listaKolona,
				this.listaKljuceva); // da se zna ko je kome sta

		int vel = ((ArrayList) this.listaKolona).size();

		String[] kolone = new String[vel];
		int[] tipoviKolona = new int[vel];
		int[] kljucevi = new int[this.listaKljuceva.size()]; //
		int brojacKljuceva = 0;
		for (int i = 0; i < vel; i++) {
			Kolona kolona = (Kolona) listaKolona.get(i);
			kolone[i] = kolona.getNaziv();
			tipoviKolona[i] = kolona.getTip();
			if (kolona.getRedniBrojUKljucu() != 0)
				kljucevi[brojacKljuceva++] = i;
		}// for i

		this.setKolone(kolone);
		this.setTipoviKolona(tipoviKolona);
		this.setKljucevi(kljucevi);

		this.sloziUpite();
	}// konstruktor

	// moze se koristiti samo na tablicama koje imaju kolone:
	// status char(1) not null, created timestamp not null, updated timestamp
	// default null
	// created_by integer not null, updated_by integer default null
	public DAOObjekt(String naziv, String[] kolone, int[] tipoviKolona,
			int[] kljucevi) throws Exception {
		this.setNaziv(naziv);
		this.setKolone(kolone);
		this.setKljucevi(kljucevi);
		this.setTipoviKolona(tipoviKolona);

		this.sloziUpite();
	}

	public void delete(Object kljuc) throws SQLException {
	}

	public boolean updateOrInsertObject(ValueObject vo) throws SQLException {
		boolean updated = this.update(vo);
		boolean inserted = false;

		if (!updated) {
			this.insert(vo);
			inserted = vo.getStatus() == 'I'; // jeli insertiran objekt
		}
		return updated | inserted;
	}// updateOrInsertObject

	// za kriterij poslan kao Hashtable kreira where upit za findAll i read
	private final String napraviWhere(Hashtable kriterij) {
		String where = " where ";
		boolean prvi = true;

		Enumeration k = kriterij.keys();
		while (k.hasMoreElements()) {
			String krit = (String) k.nextElement();
			boolean intKolona = false;

			if (kriterij.get(krit) instanceof Integer)
				intKolona = true;

			for (int i = 0; i < this.kolone.length; i++) {

				int tmpBroj = -1; // testiramo da vidimo jeli brojcana
									// vrijednost radi '' znakova

				if (this.kolone[i].equalsIgnoreCase(krit)) {
					where += (prvi ? " " : " and ")
							+ krit
							+ "="
							+ ((!intKolona) ? "'" + (kriterij.get(krit)) + "'"
									: ""
											+ ((Integer) (kriterij.get(krit)))
													.intValue());
					prvi = false;
					break; // nasli smo kolonu, nema smisla dalje vrtjeti
				}
			}// for i
		}// while

		return where;
	}// napraviWhere

	// slaze jedan uvjet za neku kolonu, podaci o kojoj se nalaze u
	// SearchCriteriaObject objektu
	private final String napraviUvjet(SearchCriteriaObject so) {
		String uvjet = "";
		if (so == null)
			return null;

		uvjet = " " + so.getName();
		int dt = so.getDataType();
		String ap = // apostrof hoce li biti ili nece, neke baze podataka ga ne
					// toleriraju na brojcane vrijednosti
		dt == Types.VARCHAR || dt == Types.CHAR || dt == Types.LONGVARCHAR
				|| dt == Types.CLOB ? "'" : "";

		switch (so.getCriteria()) {
		case SearchCriteriaObject.CRITERIA_WORD_ANYWHERE:
			uvjet += " like " + ap + "%" + so.getValue() + "%" + ap;
			break;
		case SearchCriteriaObject.CRITERIA_WORD_AT_END:
			uvjet += " like " + ap + so.getValue() + "%" + ap;
			break;
		case SearchCriteriaObject.CRITERIA_WORD_AT_START:
			uvjet += " like " + ap + "%" + so.getValue() + ap;
			break;
		case SearchCriteriaObject.CRITERIA_WORD_STRICT:
			uvjet += "=" + ap + so.getValue() + ap;
		}

		return uvjet;
	}// sloziUvjet

	public List<ValueObject> findAll(Object kljuc) throws SQLException {
		Connection conn = null;
		Statement st = null;
		conn = DAOFactory.getConnection();
		ArrayList <ValueObject>podaci = new ArrayList<ValueObject>();
		ResultSet rs = null;

		String upit = select;

		// kako inkorporirati kljuc trazenja ?!?
		// ulazni objekt treba biti Hashtable sa popisom (kolona,vrijednost)
		// ako je ulazni parametar tipa String, provjerit cemo moze li se
		// doticni
		// pretvoriti u int i staviti prvu integer kolonu..
		// inace pod kljuc cemo staviti prvu varchar varijablu
		int kljucInt = -1;
		String kljucString = null;
		boolean imaKljucInt = false;

		if (kljuc instanceof String) {
			kljucString = (String) kljuc;

			try {
				kljucInt = Integer.parseInt(kljucString);
				imaKljucInt = true;
				kljucString = null;
			} catch (NumberFormatException nfe) {
				imaKljucInt = false;
			}

			if (kljucString != null) {
				for (int i = 0; i < this.tipoviKolona.length; i++)
					if (this.tipoviKolona[i] == Types.VARCHAR) {
						upit += " where " + this.kolone[i] + " like '%"
								+ kljucString + "%'";
						break;
					}
			} else if (imaKljucInt)
				for (int i = 0; i < this.tipoviKolona.length; i++)
					if (this.tipoviKolona[i] == Types.INTEGER) {
						upit += " where " + this.kolone[i] + "=" + kljucInt
								+ "";
						break;
					}// if
		}// if kljuc je string
		else if (kljuc instanceof Hashtable)
			upit += this.napraviWhere((Hashtable) kljuc);
		else if (kljuc instanceof SearchCriteriaObject) {
			SearchCriteriaObject so = (SearchCriteriaObject) kljuc;
			upit += " where " + this.napraviUvjet(so);
		}// if

		if (orderByKolone != null) {
			upit += " order by ";
			OrderByZapis z;
			for (int i = 0; i < orderByKolone.size(); i++) {
				if (i > 0)
					upit += ",";
				z = (OrderByZapis) orderByKolone.get(i);
				upit += z.getKolona() + " " + (z.isAsc() ? "asc " : "desc ");
			}// for i
		}// if postoji orderBy

		try {
			st = conn.createStatement();
			rs = st.executeQuery(upit);

			if (rs != null)
				while (rs.next())
					podaci.add(this.constructObject(rs));
		} catch (Exception e) {
			Logger.fatal("Greska kod DAOObjekt findAll ", e);
		} finally {
			try {
				if (rs != null)
					rs.close(); rs=null;
			} catch (SQLException e1) {
			}
			try {
				if (st != null)
					st.close(); st=null;
			} catch (SQLException e1) {
			}
			if (conn != null)
				DAOFactory.freeConnection(conn); conn=null;
		}
		return podaci;
	}// findAll

	private final ValueObject constructObject(ResultSet rs)
			throws SQLException, Exception {
		ValueObject vo = new ValueObject();

		try {

			for (int i = 0; i < this.kolone.length; i++) {
				vo.setValue(this.kolone[i], rs.getObject(this.kolone[i]));
			}
		} finally {
			vo.setKolone(this.kolone);
			vo.setTipoviKolona(this.tipoviKolona);
		}
		return vo;
	}// constructObject

	public Class<?> getColumnClass(int columnIndex) {
		return null;
	}

	public int getColumnCount() {
		return 0;
	}

	public String getColumnName(int rb) {
		return this.kolone != null ? this.kolone[rb] : null;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}

	// accessories
	public int[] getKljucevi() {
		return kljucevi;
	}

	public String[] getKolone() {
		return kolone;
	}

	public String getNaziv() {
		return naziv;
	}

	public int getRowCount() {
		return 0;
	}

	private Collection getTableColumns(String tableName) {
		List listaKolona = new ArrayList();

		Connection con = null;
		ResultSet rs = null;

		try {
			this.imaCreated = false; // resetiramo varijable
			this.imaUpdated = false;

			con = DAOFactory.getConnection();
			rs = con.getMetaData().getColumns(null, null, tableName, "%");

			while (rs.next()) {
				String colName = rs.getString("COLUMN_NAME");
				int dataType = rs.getInt("DATA_TYPE");
				String typeName = rs.getString("TYPE_NAME");

				Kolona kolona = new Kolona();

				kolona.setNaziv(colName);
				kolona.setTip(dataType);
				kolona.setNazivTipa(typeName);
				kolona.setRedniBrojUKljucu(0); // za svaki slucaj

				// 31.08.05. -asabo- dodao provjeru za sql upit gore..
				if (colName != null && colName.equalsIgnoreCase("created")) {
					this.imaCreated = true;
				} else if (colName != null
						&& colName.equalsIgnoreCase("updated")) {
					this.imaUpdated = true;
				}

				listaKolona.add(kolona);
			} // kraj od While petlje
		} catch (SQLException e) {
			Logger.fatal("Sql izimka kod [DAOObjekt] [getTableColums] modula",
					e);
			Logger.log("SQL izn kod [DAOObjekt] [getTableColums] modula" + e);
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			Logger.log("Opæa izn kod [DAOObjekt] [getTableColums] modula" + e);
		} catch (Error e) {
			Logger.log("Greška sustava kod [DAOObjekt] [getTableColums] modula"
					+ e);
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException sqle) {
			}
			try {
				if (con != null) {
					DAOFactory.freeConnection(con);
				}
			} catch (SQLException sqle) {
			}
		}
		return listaKolona;
	} // getTableColumns

	// vraca nazad popis kolona koje cine kljuc (tj. jedinstveno opisuju jedan
	// redak)
	private Collection getTablePrimaryKey(String tableName) {
		List listaKolona = new ArrayList();
		Connection con = null;
		ResultSet rs = null;

		try {
			con = DAOFactory.getConnection();

			rs = con.getMetaData().getPrimaryKeys(null, null, tableName);

			if (rs == null || !rs.next()) {
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException sqle) {
				}
				rs = con.getMetaData().getBestRowIdentifier(null, null,
						tableName, DatabaseMetaData.bestRowSession, true);
			} else {// ponovno pripremi upit...
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException sqle) {
				}
				rs = con.getMetaData().getPrimaryKeys(null, null, tableName);
			}

			while (rs.next()) {
				String colName = rs.getString("COLUMN_NAME");
				// int dataType = rs.getInt("DATA_TYPE");
				// String typeName = rs.getString("TYPE_NAME");

				Kolona kolona = new Kolona();

				kolona.setNaziv(colName);
				// kolona.setTip(dataType);
				// kolona.setNazivTipa(typeName);
				kolona.setRedniBrojUKljucu(0); // nazalost sad mu ne mozemo
												// postaviti koji je redni broj
												// u kljucu, to ce jedna druga
												// procedura obaviti

				listaKolona.add(kolona);
			} // kraj od While petlje
		} catch (SQLException e) {
			Logger.fatal(
					"Sql izimka kod [DAOObjekt] [getTablePrimaryKey] modula", e);
			e.printStackTrace();
			return null;
		} catch (Error e) {
			Logger.log("Greška sustava kod [DAOObjekt] [getTablePrimaryKey] modula"
					+ e);
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException sqle) {
			}
			try {
				if (con != null) {
					DAOFactory.freeConnection(con);
				}
			} catch (SQLException sqle) {
			}
		}
		return listaKolona;
	} // getTablePrimaryKey

	public int[] getTipoviKolona() {
		return tipoviKolona;
	}

	public Object getValueAt(ValueObject vo, int kolonas) {
		return null;
	}

	public Class getVOClass() throws ClassNotFoundException {
		return null;
	}

	// ne koristi se
	public void insert(Object objekt) throws SQLException {
	}

	public void insert(ValueObject objekt) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();

		ps = conn.prepareStatement(this.insert);

		/*
		 * if (this.imaCreated && objekt!=null) { String
		 * sqlcal=biz.sunce.util.Util
		 * .convertCalendarToStringForSQLQuery(Calendar.getInstance(),true);
		 * java.sql.Timestamp t=java.sql.Timestamp.valueOf(sqlcal);
		 * try{objekt.setValue("CREATED",t);
		 * objekt.setValue("CREATED_BY",GlavniFrame.getDjelatnik().getSifra());
		 * }catch(Exception e){} }//if ima created
		 */

		for (int i = 0; i < this.kolone.length; i++) {
			Object val = objekt.getValue(this.kolone[i]);

			// poseban tretman za 'sistemske' kolone da se izbjegne varanje :)
			if (this.kolone[i].equals("CREATED")) {
				Timestamp tstmp = new Timestamp(System.currentTimeMillis());
				ps.setTimestamp(i + 1, tstmp);
				continue;
			} else if (this.kolone[i].equals("CREATED_BY")) {
				ps.setInt(i + 1, GlavniFrame.getSifDjelatnika());
				continue;
			} else if (this.kolone[i].equals("STATUS")) {
				ps.setString(i + 1, "U"); // updated
				continue;
			}

			if (val != null)
				ps.setObject(i + 1, val); // sve ce sama Java automatski
											// obaviti?!? :)
			else
				ps.setNull(i + 1, this.tipoviKolona[i]);

			/*
			 * switch(this.tipoviKolona[i]) { case Types.VARCHAR:
			 * ps.setString(i,(String)val); case Types.INTEGER:
			 * ps.setInt(i,((Integer)val).intValue()); case Types.FLOAT:
			 * ps.setFloat(i,((Float)val).floatValue()); // itd. .. doraditi s
			 * vremenom
			 * 
			 * }
			 */

		}// for i

		// broj 15 (mjesto) se dolje malo nize unosi (kompliciraniji unos)
		try {
			int kom = ps.executeUpdate();

			if (kom == 1)
				objekt.setStatus('I'); // inserted...

			// ulaz.setSifra(Integer.valueOf(sifra)); //28.02.06. -asabo- da se
			// zna da je objekt uspjesno insertiran
		} catch (SQLException e) {
			Logger.fatal("Greska kod DAOObjekt ", e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			if (conn != null)
				DAOFactory.freeConnection(conn);
		}
	}// insert

	public boolean isCellEditable(ValueObject vo, int kolona) {
		return false;
	}

	public String narusavaLiObjektKonzistentnost(ValueObject objekt) {
		return null;
	}

	// ima za zadatak upisati svim elementima liste kolone redne brojeve unutar
	// liste kljucevi kako bi se znalo koja kolona sudjeluje u kljucu
	private final void podesiRedneBrojeveKljucevaKolonama(List kolone,
			List kljucevi) {
		if (kolone != null && kljucevi != null) {
			for (int i = 0; i < kljucevi.size(); i++) {
				Kolona k = (Kolona) kljucevi.get(i);

				if (k != null) {
					for (int j = 0; j < kolone.size(); j++) {
						Kolona k2 = (Kolona) kolone.get(j);
						if (k2 != null && k2.getNaziv().equals(k.getNaziv())) {
							k2.setRedniBrojUKljucu(i + 1); // ne mozemo brojati
															// od nula, jer se
															// nula smatra
															// informacijom da
															// kolona ne tvori
															// kljuc
						}
					} // for j
				}// if k
			}// for i
		}// if kolone
	} // podesiRedneBrojeveKljucevaKolonama

	public ValueObject read(Object kljuc) throws SQLException {
		return null;
	}

	public void setKljucevi(int[] kljucevi) {
		this.kljucevi = kljucevi;
	}

	public void setKolone(String[] kolone) {
		this.kolone = kolone;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public void setTipoviKolona(int[] tipoviKolona) {
		this.tipoviKolona = tipoviKolona;
	}

	public boolean setValueAt(ValueObject vo, Object vrijednost, int kolona) {
		return false;
	}

	// slaze select, insert, update upite za kasniju upotrebu
	private void sloziUpite() {
		select = "select ";
		for (int i = 0; i < kolone.length; i++)
			select += (i > 0 ? "," : "") + kolone[i];
		select += " from " + this.getNaziv();

		insert = "insert into " + this.getNaziv() + " (";
		for (int i = 0; i < kolone.length; i++)
			insert += (i > 0 ? "," : "") + kolone[i];
		insert += ") values (";
		for (int i = 0; i < kolone.length; i++)
			insert += (i > 0 ? "," : "") + "?";
		insert += ")";

		update = "update " + this.getNaziv() + " set ";
		for (int i = 0; i < kolone.length; i++)
			update += (i > 0 ? "," : "") + kolone[i] + "=?";
		update += " where ";
		for (int i = 0; i < kljucevi.length; i++)
			update += (i > 0 ? " and " : "") + kolone[kljucevi[i]] + "=?";

		read = select + " where ";
		for (int i = 0; i < kljucevi.length; i++)
			read += (i > 0 ? " and " : "") + kolone[kljucevi[i]] + "=?";

	}// sloziUpite

	// vraca sve retke u tablici slozene u jednu html tablicu koja je vezana na
	// css klasu DAOTablica
	// tako da se doticnoj tablici moze 'izvana' izmjeniti izgled bez da se dira
	// i strukturu vracenog teksta
	// tablica vraca svoje zaglavlje, te podatke u tijelu
	public String toHtmlTable() {
		String html = "<table class='DAOTablica'>";
		html += "<thead><tr>";
		int kol = this.getColumnCount();
		int red = this.getRowCount();
		ArrayList l = null;

		for (int i = 0; i < kol; i++)
			html += "<td>" + this.getColumnName(i);
		html += "</tr></thead>";

		html += "</table>";
		return html;
	}

	public boolean update(ValueObject objekt) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DAOFactory.getConnection();

		ps = conn.prepareStatement(this.update);
		int kl = this.kolone.length;

		if (this.imaUpdated && objekt != null) {
			String sqlcal = biz.sunce.util.Util
					.convertCalendarToStringForSQLQuery(Calendar.getInstance(),
							true);
			java.sql.Timestamp t = java.sql.Timestamp.valueOf(sqlcal);
			try {
				objekt.setValue("UPDATED", t);
				objekt.setValue("UPDATED_BY", GlavniFrame.getDjelatnik()
						.getSifra());
				objekt.setValue("updated", t); // da budemo sigurni...
				objekt.setValue("updated_by", GlavniFrame.getDjelatnik()
						.getSifra());
			} catch (Exception e) {
			}
		}

		for (int i = 0; i < kl; i++) {
			String kol = this.kolone[i];
			Object val = objekt.getValue(kol);

			if (val != null)
				ps.setObject(i + 1, val); // sve ce sama Java automatski
											// obaviti?!? :)
			else
				ps.setNull(i + 1, this.tipoviKolona[i]);

			/*
			 * switch(this.tipoviKolona[i]) { case Types.VARCHAR:
			 * ps.setString(i,(String)val); case Types.INTEGER:
			 * ps.setInt(i,((Integer)val).intValue()); case Types.FLOAT:
			 * ps.setFloat(i,((Float)val).floatValue()); // itd. .. doraditi s
			 * vremenom
			 * 
			 * }
			 */

		}// for i

		for (int i = 0; i < this.kljucevi.length; i++) {
			Object val = objekt.getValue(this.kolone[this.kljucevi[i]]);

			ps.setObject(i + 1 + kl, val);
		}

		try {
			int kom = ps.executeUpdate();
			// if (kom==1)
			// ulaz.setSifra(Integer.valueOf(sifra)); //28.02.06. -asabo- da se
			// zna da je objekt uspjesno insertiran
			return kom == 1;
		} catch (SQLException e) {
			// GlavniFrame.fatal("Greska kod inserta klijenta sifra:"+sifra+" naziv: "+ulaz.getIme()+" "+ulaz.getPrezime(),e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e1) {
			}
			if (conn != null)
				DAOFactory.freeConnection(conn);
		}
		return false;
	}// update

	// nece se koristiti, vec update(ValueObject)
	public boolean update(Object objekt) throws SQLException {
		return false;
	}// update

	public java.util.Vector getOrderByKolone() {
		return orderByKolone;
	}

	public void addOrderByKolona(String string, boolean asc) {
		setOrderByKolona(string, asc);
	}

	public void setOrderByKolona(String string, boolean asc) {

		if (orderByKolone == null)
			orderByKolone = new java.util.Vector(4);

		OrderByZapis z = new OrderByZapis();
		z.setKolona(string);
		z.setAsc(asc);
		orderByKolone.add(z);
	}

}// klasa
