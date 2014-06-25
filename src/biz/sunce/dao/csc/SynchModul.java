package biz.sunce.dao.csc;

import java.io.CharArrayReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.optika.Logger;
import biz.sunce.optika.net.Kolona;
import biz.sunce.optika.net.Tablica;
import biz.sunce.util.Util;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public final class SynchModul implements biz.sunce.dao.SynchModul {
	final String countUpit = " select count(*) from ";
	final String selectUpit = " select * from ";
	final String whereUpit = null; // napravit ce se unutar same procedure
	int komKolona;
	boolean imaUpdated = false; // privremena var. u koju getTebleColumns
								// upisuje obavijest ima li
	boolean imaCreated = false; // trenutna tablica updated i created kolonu ili
								// ne
	static java.io.FileWriter fw = null;
	private static SynchModul instanca = null;

	public SynchModul() {
		instanca = this;
	}

	// vraca nazad listu Object[] elemenata koji predstavljaju nove retke
	// tablice
	public List collectNewData(String table) {
		List result = new ArrayList();
		try {
			Collection listaKolona = getTableColumns(table);
			Collection kljucevi = getTablePrimaryKey(table);
			komKolona = listaKolona.size();

			// one kolone koje sudjeluju u kljucu dobit ce unutar sebe
			// informaciju o tome koji su redni broj
			// sto je potrebno kasnije pri gradnji select i update upita...
			podesiRedneBrojeveKljucevaKolonama((List) listaKolona,
					(List) kljucevi);

			result.add(listaKolona); // Na prvom mjestu se nalazi opis tablice
			result.add(kljucevi); // a na drugom popis kolona koje tvore kljuc

			Connection con = null;
			ResultSet rs = null;
			String upit = selectUpit + table;

			openLogger();

			if (!this.imaCreated) {
				Logger.log("Tablica " + table
						+ " se treba sinkronizirati, a nema created kolonu!");
				return null;
			}

			String tmp = "select max(datVrijeme) from SYNCH where uspjeh='"
					+ biz.sunce.dao.DAO.DA + "'";
			java.sql.ResultSet rsd = DAOFactory.performQuery(tmp);
			Timestamp t = null;
			try {
				if (rsd != null && rsd.next()) {
					t = rsd.getTimestamp(1);
				}
			} catch (SQLException sqle) {
				Logger.fatal(
						"SQL iznimka kod citanja zadnjeg datuma sinkronizacije.",
						sqle);
				this.log("SQL izn kod citanja zadnjeg datuma sink." + sqle);
				return null;
			} finally {
				try {
					if (rsd != null) {
						rsd.close();
					}
				} catch (SQLException e) {
				}
			}

			if (t != null) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(t.getTime());
				upit += " where created>='"
						+ Util.convertCalendarToStringForSQLQuery(c, true)
						+ "'";

				if (this.imaUpdated) {
					upit += " or updated>='"
							+ Util.convertCalendarToStringForSQLQuery(c, true)
							+ "'";
				}

			} // if t nije null

			rs = DAOFactory.performQuery(upit);

			try {
				while (rs.next()) {
					result.add(constructRedak(rs, (List) listaKolona));
				}
			} catch (SQLException e) {
				Logger.fatal("Sql iznimka kod sync modula", e);
				log("SQL izn kod sync modula" + e + "\n" + upit);
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
		} catch (Error e) {
			log("Greška sustava kod collectNewData " + e + "\n");
			throw e;
		}

		return result;
	} // collectNewData

	// zapisuje podatake o obavljenoj sinkronizaciji i javlja nazad jeli
	// uspjesno zapisano
	public boolean registerSuccesfulSynchronization(Calendar date) {

		String upit = "insert into SYNCH(datVrijeme, uspjeh) values (?,'"
				+ DAO.DA + "')";
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DAOFactory.getConnection();

			if (con == null) {
				return false;
			}

			ps = con.prepareStatement(upit);

			if (ps != null) {
				Timestamp t = new Timestamp(date.getTimeInMillis());
				ps.setTimestamp(1, t);
				int kom = ps.executeUpdate();
				return kom == 1;
			} else { // if ps ispravan
				Logger.log("Prepared statement null u CSC SynchModul registerSuccesfulSynchronization ?!?");
			}
		} catch (SQLException sqle) {
			Logger.fatal(
					"SQL iznimka kod CSC SynchModul.registerSuccessfulSynchronization",
					sqle);
			log("SQL iznimka kod CSC SynchModul.registerSuccessfulSynchronization"
					+ sqle);
		} catch (Error err) {
			log("Greska sustava kod registerSuccesfulSynchronization" + err);
			throw err;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException sqle) {
			}
			if (con != null) {
				try {
					DAOFactory.freeConnection(con);
				} catch (SQLException e) {
					log("Problem kod zatvaranja veze");
					Logger.fatal(
							"SQL iznimka kod pokusaja vracanja sql veze brokeru",
							e);
				}
			}
		}
		return false;
	} // registerSuccesfulSynchronization

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
			Logger.fatal("Sql izimka kod [SynchModul] [getTableColums] modula",
					e);
			log("SQL izn kod [SynchModul] [getTableColums] modula" + e);
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			log("Opæa izn kod [SynchModul] [getTableColums] modula" + e);
		} catch (Error e) {
			log("Greška sustava kod [SynchModul] [getTableColums] modula" + e);
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
					"Sql izimka kod [SynchModul] [getTablePrimaryKey] modula",
					e);
			e.printStackTrace();
			return null;
		} catch (Error e) {
			log("Greška sustava kod [SynchModul] [getTablePrimaryKey] modula"
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

	// ima za zadatak upisati svim elementima liste kolone redne brojeve unutar
	// liste kljucevi kako bi se znalo koja kolona sudjeluje u kljucu
	public void podesiRedneBrojeveKljucevaKolonama(List kolone, List kljucevi) {
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

	public void log(String poruka) {
		if (fw != null) {
			try {
				fw.write(poruka + "\n");
			} catch (IOException ex) {
				Logger.log("IO iznimka kod zapisivanja u synch logger", ex);
			} catch (Exception e) {
				System.out.println("Iznimka kod SynchModul.log()" + e);
				e.printStackTrace();
			} // ako pukne pri zapisivanju u datoteku da ne srusi cijeli
				// sustav...
		} // if
	}// log

	private boolean updateRetkaTablice(List listaKolona, Object[] zapis,
			String nazivTablice) {
		boolean rez = true;
		int velicina = listaKolona.size();
		String upit = "(nema)";
		String insert = "insert into " + nazivTablice;
		String update = "update " + nazivTablice + " set ";
		String select = "select ";
		String insKolone = "(";
		String updKolone = "";
		String valuesInsKolone = " values (";

		try {
			Connection con = null;
			PreparedStatement psIns = null, psUpd = null, psSel = null;
			int rbk = 0, kolLen = 0;

			// prvo urediti upite
			for (int i = 0; i < velicina; i++) {
				Kolona k = (Kolona) listaKolona.get(i);
				if (k == null) {
					Logger.log("K je null do update retka tablice?!?");
					this.log("problem upd table k je null. Sel: " + select
							+ "\n" + insert + "\n" + update);
				}
				String colName = k.getNaziv();
				int tipKolone = k.getTip();
				// String typeName = k.getNazivTipa();

				if (i > 0) {
					insKolone += "," + colName;
				} else {
					insKolone += colName;
				}

				if (i > 0) {
					valuesInsKolone += ",?";
				} else {
					valuesInsKolone += "?";
				}

				if (i > 0) {
					updKolone = "," + colName + "=?";
				} else {
					updKolone = "" + colName + "=?";
				}
			} // prvi for i

			insKolone += ") ";
			valuesInsKolone += ") ";

			insert += " " + insKolone + " " + valuesInsKolone;
			// podesiti psIns i psUpd

			try {
				con = DAOFactory.getConnection();
				psIns = con.prepareStatement(insert);
			} catch (SQLException sqle) {
				Logger.fatal(
						"SQL iznimka kod SynchModul.updateTablice - priprema upita",
						sqle);
				try {
					if (psIns != null) {
						psIns.close();
					}
				} catch (SQLException ex) {
				}
				try {
					if (con != null) {
						DAOFactory.freeConnection(con);
					}
				} catch (SQLException ex) {
				}
				return false;
			}

			// trpanje podataka u ps-eve (ins,upd,sel)... idemo sve kolone jedan
			// po jedan
			for (int i = 1; i <= velicina; i++) {
				try {
					List t = (List) listaKolona.get(i - 1);

					String colName = (String) t.get(0);
					Integer dT = (Integer) t.get(1);
					String typeName = (String) t.get(2);

					Integer itmp = null;

					int tipKolone = dT.intValue();

					switch (tipKolone) {
					case Types.INTEGER:
					case Types.DECIMAL:
						Integer tmp = (Integer) zapis[i - 1];
						psIns.setInt(i, tmp.intValue());
						if (psUpd != null) {
							psUpd.setInt(i, tmp.intValue());
							// if (tc.isKey())
							// psUpd.setInt(rbk+kolLen,tmp.intValue());
						}
						if (psSel != null) { // try{if (tc.isKey())
												// psSel.setInt(i,tmp.intValue());
												// }finally
							{
								Logger.log("Tablica " + nazivTablice
										+ " ima problem i:" + i + " upit: "
										+ upit + "sel: " + select + " upd:"
										+ update);
							}
						}
						break;

					case Types.BIGINT:
						Long ltmp = (Long) zapis[i - 1];
						psIns.setLong(i, ltmp.longValue());
						if (psUpd != null) {
							psUpd.setLong(i, ltmp.longValue());
						}

						// if (psUpd!=null && tc.isKey())
						// psUpd.setLong(rbk+kolLen,ltmp.longValue());
						// if (psSel!=null && tc.isKey())
						// psSel.setLong(i,ltmp.longValue());

						break;

					case Types.SMALLINT:
					case Types.TINYINT:
						Short tmpS = (Short) zapis[i - 1];
						psIns.setShort(i, tmpS.shortValue());
						if (psUpd != null) {
							psUpd.setShort(i, tmpS.shortValue());
						}

						// if (psUpd!=null && tc.isKey())
						// psUpd.setShort(rbk+kolLen,tmpS.shortValue());
						// if (psSel!=null && tc.isKey())
						// psSel.setShort(i,tmpS.shortValue());
						break;

					case Types.VARCHAR:
					case Types.CHAR:
						String sttmp = (String) zapis[i - 1];

						// svako polje koje je izmjenjeno treba imati oznaku da
						// se je informacija prenijela.
						// zasada vrijedi oznaka 'S'
						// if (indVal) sttmp=Table.IND_VAL_UNESEN_PODATAK; // S

						psIns.setString(i, sttmp);
						if (psUpd != null) {
							psUpd.setString(i, sttmp);
						}

						// if (psUpd!=null && tc.isKey())
						// psUpd.setString(rbk+kolLen,sttmp);
						// if (psSel!=null && tc.isKey())
						// psSel.setString(i,sttmp);
						break;

					case Types.DATE:
						java.sql.Date dtmp = (java.sql.Date) zapis[i - 1];
						psIns.setDate(i, dtmp);
						if (psUpd != null) {
							psUpd.setDate(i, dtmp);
						}

						// if (psUpd!=null && tc.isKey())
						// psUpd.setDate(rbk+kolLen,dtmp);
						// if (psSel!=null && tc.isKey()) psSel.setDate(i,dtmp);
						break;

					case Types.TIMESTAMP:
						Timestamp tim = (Timestamp) zapis[i - 1];
						psIns.setTimestamp(i, tim);
						if (psUpd != null) {
							psUpd.setTimestamp(i, tim);
						}

						// if (psUpd!=null && tc.isKey())
						// psUpd.setTimestamp(rbk+kolLen,tim);
						// if (psSel!=null && tc.isKey())
						// psSel.setTimestamp(i,tim);
						break;

					case Types.FLOAT:
						Float ftmp = (Float) zapis[i - 1];
						psIns.setFloat(i, ftmp.floatValue());
						if (psUpd != null) {
							psUpd.setFloat(i, ftmp.floatValue());
						}

						// if (psUpd!=null && tc.isKey())
						// psUpd.setFloat(rbk+kolLen,ftmp.floatValue());
						// if (psSel!=null && tc.isKey())
						// psSel.setFloat(i,ftmp.floatValue());
						break;

					case Types.DOUBLE:
					case Types.REAL:
						Double dotmp = (Double) zapis[i - 1];
						psIns.setDouble(i, dotmp.doubleValue());
						if (psUpd != null) {
							psUpd.setDouble(i, dotmp.doubleValue());
						}

						// if (psUpd!=null && tc.isKey())
						// psUpd.setDouble(rbk+kolLen,dotmp.doubleValue());
						// if (psSel!=null && tc.isKey())
						// psSel.setDouble(i,dotmp.doubleValue());
						break;

					case Types.LONGVARCHAR:
					case Types.BLOB:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
						byte pod[];

						java.io.ByteArrayInputStream bin = null;

						int vel = 0;
						try {
							pod = (byte[]) zapis[i - 1];
							int pl = pod != null ? pod.length : 0;

							if (pod != null) {
								bin = new java.io.ByteArrayInputStream(pod);
							}

							if (pod != null) {
								psIns.setBinaryStream(i, bin, pl);
							} else {
								psIns.setNull(i, tipKolone);
							}

							if (psUpd != null) {
								if (pod != null) {
									psUpd.setBinaryStream(i, bin, pl);
								} else {
									psUpd.setNull(i, Types.LONGVARCHAR);
								}

								// if (tc.isKey())
								// if (pod!=null)
								// psUpd.setBinaryStream(rbk+kolLen,bin,pl);
								// else
								// psUpd.setNull(rbk+kolLen,tc.getTipKolone());
							} // if psUpd!=null

							if (psSel != null) {
								// if (tc.isKey())
								if (pod != null) {
									psSel.setBinaryStream(i, bin, pl);
								} else {
									psSel.setNull(i, Types.LONGVARCHAR);
								}
							} // if psSel!=null

						} catch (Exception ex2) {
							Logger.log(
									"Exc kod citanja binary streama (CSC SynchModul.updateTable) tablica: "
											+ nazivTablice, ex2);
							return false;
						} finally {
							try {
								if (bin != null) {
									bin.close();
								}
							} catch (java.io.IOException ioe) {
							}
						}
						break;

					case Types.CLOB:
						java.io.CharArrayReader cr = null;
						try {
							char[] ctmp = (char[]) zapis[i - 1];

							cr = new java.io.CharArrayReader(ctmp);
							int kom = 0;

							if (ctmp != null) {
								psIns.setCharacterStream(i, cr, ctmp.length);
							} else {
								psIns.setNull(i, Types.CLOB);
							}

							if (psUpd != null) {
								if (ctmp != null) {
									psUpd.setCharacterStream(i, cr, ctmp.length);
								} else {
									psUpd.setNull(i, Types.CLOB);
								}

								// if (tc.isKey())
								if (ctmp != null) {
									psUpd.setCharacterStream(rbk + kolLen, cr,
											ctmp.length);
								} else {
									psUpd.setNull(rbk + kolLen, Types.CLOB);
								}
							} // if psUpd

							if (psSel != null) {
								// if (tc.isKey())
								if (ctmp != null) {
									psSel.setCharacterStream(i, cr, ctmp.length);
								} else {
									psSel.setNull(i, Types.CLOB);
								}
							} // if psSel

						} catch (Exception ex1) {
							Logger.log(
									"CLOB pisanje podataka nije uspjesno obavljeno \nEx: ",
									ex1);
							return false;
						} finally {
							try {
								if (cr != null) {
									cr.close();
								}
							} catch (Exception e) {
							}
						}
						break;
					default:
						Logger.log("Nepoznata situacija?!? Ne mozemo prepoznati tip kolone: "
								+ tipKolone);
						this.log("Nepoznata situacija?!? Ne mozemo prepoznati tip kolone: "
								+ tipKolone);
					} // switch
				} catch (SQLException sqle) { // try
					Logger.log(
							"SQL Iznimka kod  postavljanja vrijednosti u kolone CSC SynchModul.updateRetkaTablice: ",
							sqle);
					log("SQL izn. kod postavljanja vrijednosti u kolone. Sel: "
							+ select + " ins:" + insert + " upd: " + update);
				} catch (Exception e) {
					Logger.log(
							"Opæa iznimka kod  postavljanja vrijednosti u kolone CSC SynchModul.updateRetkaTablice: ",
							e);
					log("Opæa iznimka kod postavljanja vrijednosti u kolone. Sel: "
							+ select + " ins:" + insert + " upd: " + update);
				} finally {

				}
			} // kraj for petlje
		} catch (Error e) {
			Logger.log(
					"Greška sustava kod  postavljanja vrijednosti u kolone CSC SynchModul.updateRetkaTablice: ",
					e);
			log("Greška sustava kod postavljanja vrijednosti u kolone. Sel: "
					+ select + " ins:" + insert + " upd: " + update);
			throw e;
		}

		return rez;
	} // updateRetkaTablice

	public static boolean updateRetkaTablice(List listaKolona, List kljucevi,
			Object[] zapis, String nazivTablice, Connection con) {
		boolean rez = true;
		int velicina = listaKolona != null ? listaKolona.size() : -1;
		String upit = "(nema)";
		String insert = "insert into " + nazivTablice;
		String update = "update " + nazivTablice + " set ";
		String select = "select * from " + nazivTablice;
		String insKolone = "(";
		String updKolone = "";
		String valuesInsKolone = " values (";
		String where = " where ";

		PreparedStatement psIns = null, psUpd = null, psSel = null;
		ResultSet rs = null;
		int rbk = 0, kolLen = 0;

		if (listaKolona == null) {
			Logger.log("Lista kolona je null kod updateRetkaTablice CSCS SynchModul..");
			return false;
		}
		if (kljucevi == null) {
			Logger.log("kljucevi su null kod updateRetkaTablice CSCS SynchModul..");
			return false;
		}

		// urediti where pomocu kljuceva.. prikacit cemo ga na update i select
		for (int i = 0; i < kljucevi.size(); i++) {
			Kolona k = (Kolona) kljucevi.get(i);

			if (k == null) {
				Logger.log("kljuc je null kod updateRetkaTablice CSCS SynchModul..");
				return false;
			}

			if (i == 0) {
				where += k.getNaziv() + "=?";
			} else {
				where += " and " + k.getNaziv() + "=?";
			}
		} // for i

		// urediti upite
		for (int i = 0; i < velicina; i++) {
			Kolona lt = (Kolona) listaKolona.get(i);

			if (lt == null) {
				Logger.log("kolona je null kod updateRetkaTablice CSCS SynchModul..");
				return false;
			}

			String colName = lt.getNaziv();
			int tipKolone = lt.getTip();
			String typeName = lt.getNazivTipa();

			if (i > 0) {
				insKolone += "," + colName;
			} else {
				insKolone += colName;
			}

			if (i > 0) {
				valuesInsKolone += ",?";
			} else {
				valuesInsKolone += "?";
			}

			if (i > 0) {
				updKolone += "," + colName + "=?";
			} else {
				updKolone += "" + colName + "=?";
			}

		} // prvi for i

		insKolone += ") ";
		valuesInsKolone += ") ";

		insert += " " + insKolone + " " + valuesInsKolone;
		// podesiti psIns i psUpd

		if (where != null && where.trim().equalsIgnoreCase("where"))
			where = ""; // nema where...

		update += " " + updKolone + " " + where;

		select += " " + where + " ";

		try {
			psIns = con.prepareStatement(insert);
			psUpd = con.prepareStatement(update);
			psSel = con.prepareStatement(select);
		} catch (Exception sqle) {
			if (instanca != null)
				instanca.log("loši upiti!?! ins: " + insert + " upd: " + update
						+ " sel: " + select);
			Logger.fatal(
					"SQL iznimka kod SynchModul.updateTablice - priprema upita",
					sqle);
			try {
				if (psIns != null) {
					psIns.close();
				}
			} catch (SQLException ex) {
			}
			try {
				if (con != null) {
					DAOFactory.freeConnection(con);
				}
			} catch (SQLException ex) {
			}
			return false;
		}

		int brojac = 0;
		// trpanje podataka u ps-eve (ins,upd,sel)... idemo sve kolone jedan po
		// jedan
		for (int i = 1; i <= velicina; i++) {
			try {
				Kolona k = (Kolona) listaKolona.get(i - 1);

				String colName = k.getNaziv();
				int tipKolone = k.getTip();
				String typeName = k.getNazivTipa();
				int redniKljuc = k.getRedniBrojUKljucu();
				int vrk = velicina + redniKljuc;

				Integer itmp = null;

				switch (tipKolone) {
				case Types.INTEGER:
				case Types.DECIMAL:
					Integer tmp = (Integer) zapis[i - 1];
					if (tmp == null) {
						psIns.setNull(i, Types.INTEGER);
						if (psUpd != null) {
							psUpd.setNull(i, Types.INTEGER);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, Types.INTEGER);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, Types.INTEGER);
						}
						break;
					} else {
						psIns.setInt(i, tmp.intValue());
						if (psUpd != null) {
							psUpd.setInt(i, tmp.intValue());
							if (redniKljuc > 0) {
								psUpd.setInt(vrk, tmp.intValue()); // ako kolona
																	// tvori i
																	// kljuc,
																	// upisati..
							}
						}
						if (psSel != null) {
							if (redniKljuc > 0) {
								psSel.setInt(redniKljuc, tmp.intValue()); // ako
																			// kolona
																			// tvori
																			// i
																			// kljuc,
																			// upisati..
							}
						}
					}// else
					break;

				case Types.BIGINT:
					Long ltmp = (Long) zapis[i - 1];
					if (ltmp == null) {
						psIns.setNull(i, Types.BIGINT);
						if (psUpd != null) {
							psUpd.setNull(i, Types.BIGINT);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, Types.INTEGER);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, Types.BIGINT);
						}
						break;
					} else {
						psIns.setLong(i, ltmp.longValue());
						if (psUpd != null) {
							psUpd.setLong(i, ltmp.longValue());
						}
						if (psUpd != null && redniKljuc > 0) {
							psUpd.setLong(vrk, ltmp.longValue());
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setLong(redniKljuc, ltmp.longValue());
						}
					}// else
					break;

				case Types.SMALLINT:
				case Types.TINYINT:
					Short tmpS = (Short) zapis[i - 1];

					if (tmpS == null) {
						psIns.setNull(i, tipKolone);
						if (psUpd != null) {
							psUpd.setNull(i, tipKolone);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, tipKolone);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, tipKolone);
						}
						break;
					} else {
						psIns.setShort(i, tmpS.shortValue());
						if (psUpd != null) {
							psUpd.setShort(i, tmpS.shortValue());
						}
						if (psUpd != null && redniKljuc > 0) {
							psUpd.setShort(vrk, tmpS.shortValue());
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setShort(redniKljuc, tmpS.shortValue());
						}
					}// else
					break;

				case Types.VARCHAR:
				case Types.CHAR:
				case Types.NUMERIC:
					String sttmp = (String) zapis[i - 1];

					if (sttmp == null) {
						psIns.setNull(i, tipKolone);
						if (psUpd != null) {
							psUpd.setNull(i, tipKolone);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, tipKolone);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, tipKolone);
						}
						break;
					} else {
						psIns.setString(i, sttmp);
						if (psUpd != null) {
							psUpd.setString(i, sttmp);
						}
						if (psUpd != null && redniKljuc > 0) {
							psUpd.setString(vrk, sttmp);
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setString(redniKljuc, sttmp);
						}
					}// else
					break;

				case Types.DATE:
					java.sql.Date dtmp = (java.sql.Date) zapis[i - 1];

					if (dtmp == null) {
						psIns.setNull(i, tipKolone);
						if (psUpd != null) {
							psUpd.setNull(i, tipKolone);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, tipKolone);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, tipKolone);
						}
						break;
					} else {
						psIns.setDate(i, dtmp);
						if (psUpd != null) {
							psUpd.setDate(i, dtmp);
						}
						if (psUpd != null && redniKljuc > 0) {
							psUpd.setDate(vrk, dtmp);
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setDate(redniKljuc, dtmp);
						}
					}// else
					break;

				case Types.TIMESTAMP:
					Timestamp tim = (Timestamp) zapis[i - 1];

					if (tim == null) {
						psIns.setNull(i, tipKolone);
						if (psUpd != null) {
							psUpd.setNull(i, tipKolone);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, tipKolone);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, tipKolone);
						}
						break;
					} else {
						psIns.setTimestamp(i, tim);
						if (psUpd != null) {
							psUpd.setTimestamp(i, tim);
						}
						if (psUpd != null && redniKljuc > 0) {
							psUpd.setTimestamp(vrk, tim);
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setTimestamp(redniKljuc, tim);
						}
					}// else
					break;

				case Types.FLOAT:
					Float ftmp = (Float) zapis[i - 1];

					if (ftmp == null) {
						psIns.setNull(i, tipKolone);
						if (psUpd != null) {
							psUpd.setNull(i, tipKolone);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, tipKolone);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, tipKolone);
						}
						break;
					} else {
						psIns.setFloat(i, ftmp.floatValue());
						if (psUpd != null) {
							psUpd.setFloat(i, ftmp.floatValue());
						}
						if (psUpd != null && redniKljuc > 0) {
							psUpd.setFloat(vrk, ftmp.floatValue());
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setFloat(redniKljuc, ftmp.floatValue());
						}
					}// else
					break;

				case Types.DOUBLE:
				case Types.REAL:
					Double dotmp = (Double) zapis[i - 1];

					if (dotmp == null) {
						psIns.setNull(i, tipKolone);
						if (psUpd != null) {
							psUpd.setNull(i, tipKolone);
							if (redniKljuc > 0) {
								psUpd.setNull(vrk, tipKolone);
							}
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setNull(redniKljuc, tipKolone);
						}
						break;
					} else {
						psIns.setDouble(i, dotmp.doubleValue());
						if (psUpd != null) {
							psUpd.setDouble(i, dotmp.doubleValue());
						}
						if (psUpd != null && redniKljuc > 0) {
							psUpd.setDouble(vrk, dotmp.doubleValue());
						}
						if (psSel != null && redniKljuc > 0) {
							psSel.setDouble(redniKljuc, dotmp.doubleValue());
						}
					}// else
					break;

				case Types.LONGVARCHAR:
				case Types.BLOB:
				case Types.VARBINARY:
				case Types.LONGVARBINARY:
					byte pod[];

					java.io.ByteArrayInputStream bin = null;

					int vel = 0;
					try {
						pod = (byte[]) zapis[i - 1];

						if (pod == null) {
							psIns.setNull(i, tipKolone);
							if (psUpd != null) {
								psUpd.setNull(i, tipKolone);
								if (redniKljuc > 0) {
									psUpd.setNull(vrk, tipKolone);
								}
							}
							if (psSel != null && redniKljuc > 0) {
								psSel.setNull(redniKljuc, tipKolone);
							}
							break;
						} else {
							int pl = pod != null ? pod.length : 0;

							if (pod != null) {
								bin = new java.io.ByteArrayInputStream(pod);
							}

							if (pod != null) {
								psIns.setBinaryStream(i, bin, pl);
							} else {
								psIns.setNull(i, tipKolone);
							}

							if (psUpd != null) {
								if (pod != null) {
									psUpd.setBinaryStream(i, bin, pl);
								} else {
									psUpd.setNull(i, Types.LONGVARCHAR);
								}

								if (redniKljuc > 0) {
									if (pod != null) {
										psUpd.setBinaryStream(vrk, bin, pl);
									} else {
										psUpd.setNull(vrk, Types.LONGVARCHAR);
									}
								}
							} // if psUpd!=null

							if (psSel != null) {
								if (redniKljuc > 0) {
									if (pod != null) {
										psSel.setBinaryStream(redniKljuc, bin,
												pl);
									} else {
										psSel.setNull(redniKljuc,
												Types.LONGVARCHAR);
									}
								}
							} // if psSel!=null
						}// else
					} catch (Exception ex2) {
						Logger.log(
								"Exc kod citanja binary streama (CSC SynchModul.updateTable) tablica: "
										+ nazivTablice, ex2);
						return false;
					} finally {
						try {
							if (bin != null) {
								bin.close();
							}
						} catch (java.io.IOException ioe) {
						}
					}
					break;

				case Types.CLOB:

					try {
						String s = (String) zapis[i - 1];

						if (s == null) {
							psIns.setNull(i, tipKolone);
							if (psUpd != null) {
								psUpd.setNull(i, tipKolone);
								if (redniKljuc > 0) {
									psUpd.setNull(vrk, tipKolone);
								}
							}
							if (psSel != null && redniKljuc > 0) {
								psSel.setNull(redniKljuc, tipKolone);
							}
							break;
						} else {
							Reader r = null;
							char[] arr = null;

							if (s != null && !s.equals("")) {
								arr = s.toCharArray();
								r = new CharArrayReader(arr);
							}
							if (r != null) {
								psIns.setCharacterStream(i, r, arr.length);
							} else {
								psIns.setNull(i, Types.CLOB);
							}

							if (psUpd != null) {
								if (r != null) {
									psUpd.setCharacterStream(i, r, arr.length);
								} else {
									psUpd.setNull(i, Types.CLOB);
								}

								if (redniKljuc > 0) {
									if (r != null) {
										psUpd.setCharacterStream(vrk, r,
												arr.length);
									} else {
										psUpd.setNull(vrk, Types.CLOB);
									}
								}
							} // if psUpd

							if (psSel != null) {
								if (redniKljuc > 0) {
									if (r != null) {
										psSel.setCharacterStream(redniKljuc, r,
												arr.length);
									} else {
										psSel.setNull(redniKljuc, Types.CLOB);
									}
								}
							} // if psSel
						}// else
					} catch (Exception ex1) {
						Logger.log(
								"CLOB pisanje podataka nije uspjesno obavljeno \nEx: ",
								ex1);
						return false;
					} finally {
					}
					break;
				default:
					if (instanca != null)
						instanca.log("Nepoznata situacija, ne mozemo prepoznati tip kolone: "
								+ tipKolone);
					Logger.log("Nepoznata situacija?!? Ne mozemo prepoznati tip kolone: "
							+ tipKolone);
				} // switch

				// ######################### kraj switcha
				// #################################
			} catch (SQLException sqle) { // try
				if (instanca != null)
					instanca.log("SQL Iznimka kod  postavljanja vrijednosti u kolone CSC SynchModul.updateRetkaTablice:"
							+ sqle);

				Logger.log(
						"SQL Iznimka kod  postavljanja vrijednosti u kolone CSC SynchModul.updateRetkaTablice: ",
						sqle);

			} catch (Exception e) {
				Logger.log(
						"Iznimka kod  postavljanja vrijednosti u kolone CSC SynchModul.updateRetkaTablice: ",
						e);
				if (instanca != null)
					instanca.log("Ex kod updateRetka: " + e);
			} finally {

			}

		} // kraj for petlje

		try {
			rs = psSel.executeQuery();
			int komada = 0;

			if (rs.next()) {
				komada = psUpd.executeUpdate();
			} else
				komada = psIns.executeUpdate();

			rez = (komada == 1);
			// moramo vidjeti gdje updateovi ne rade izmjene u tablicama, iako
			// ne mozemo znati zasto...
			if (!rez) {
				if (instanca != null)
					instanca.log("Neuspio update retka tablice: "
							+ nazivTablice + " ins: " + insert + " upd: "
							+ update);
			}
		} catch (SQLException sqle) {
			if (instanca != null) {
				instanca.log("SQL izn" + sqle + " ins: " + insert);
				String zp = "";
				if (zapis != null)
					for (int i = 0; i < zapis.length; i++)
						zp += (zapis[i] != null ? zapis[i] : "NULL") + ":";
				else
					zp = "zapis je null!";
				instanca.log("Zapis: " + zp);
			}// if instanca!=null

			Logger.log(
					"SQL iznimka kod CSC updateTablice naziv tablice:"
							+ nazivTablice + " insert: " + insert + " razlog:"
							+ sqle != null ? sqle.getMessage() : "SQLE null",
					sqle);
		} catch (Exception e) {
			if (instanca != null)
				instanca.log("Iznimka kod CSC updateTablice naziv tablice:"
						+ nazivTablice + " insert: " + insert + " upd: "
						+ update);
			Logger.log("Iznimka kod CSC updateTablice naziv tablice:"
					+ nazivTablice + "\n insert: " + insert + "\n update: "
					+ update + ispisiRedak(zapis), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException sqle) {
			}
			try {
				if (psUpd != null) {
					psUpd.close();
				}
			} catch (SQLException sqle) {
			}
			try {
				if (psIns != null) {
					psIns.close();
				}
			} catch (SQLException sqle) {
			}
		}

		return rez;
	} // updateRetkaTablice

	private static String ispisiRedak(Object[] zapis) {
		if (zapis == null)
			return "NULL?!?";
		String tmp = "";
		for (int i = 0; i < zapis.length; i++) {
			if (zapis[1] instanceof Number) {
				Number n = (Number) zapis[1];
				tmp += n.longValue();
			} else
				tmp += zapis[i];
		}// for i
		return tmp;
	}

	private Object[] constructRedak(ResultSet rs, List listaKolona) {
		int velicina = listaKolona.size();

		Object[] zapis = new Object[velicina];

		for (int i = 0; i < velicina; i++) {

			Kolona k = (Kolona) listaKolona.get(i);

			String colName = k.getNaziv();
			int tipKolone = k.getTip();
			String typeName = k.getNazivTipa();

			try {
				switch (tipKolone) {
				case Types.INTEGER:
				case Types.DECIMAL:
					zapis[i] = Integer.valueOf(rs.getInt(i + 1));
					if (rs.wasNull()) {
						zapis[i] = null;
					}

					// zapis.add(Integer.valueOf(rs.getInt(i)));
					break;

				case Types.BIGINT:
					zapis[i] = Long.valueOf(rs.getLong(i + 1));
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.SMALLINT:
				case Types.TINYINT:
					zapis[i] = Short.valueOf(rs.getShort(i + 1));
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.VARCHAR:
				case Types.CHAR:
				case Types.NUMERIC:
					zapis[i] = rs.getString(i + 1);
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.DATE:
					zapis[i] = rs.getDate(i + 1);
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.TIMESTAMP:
					zapis[i] = rs.getTimestamp(i + 1);
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.FLOAT:
					zapis[i] = new Float(rs.getFloat(i + 1));
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.DOUBLE:
					zapis[i] = Double.valueOf(rs.getDouble(i + 1));
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.REAL:
					zapis[i] = Double.valueOf(rs.getDouble(i + 1));
					if (rs.wasNull()) {
						zapis[i] = null;
					}
					break;

				case Types.LONGVARCHAR:
					InputStream inp = rs.getBinaryStream(i + 1);
					if (rs.wasNull()) {
						zapis[i] = null;
						break;
					}
					int vel = 0;
					try {
						byte[] pod;
						if (inp != null) {
							vel = inp.available();
							pod = new byte[vel];
							inp.read(pod);
						} else {
							pod = null;
						}

						zapis[i] = pod;
					} catch (IOException ex2) {
						Logger.fatal(
								"IOExc kod citanja binary streama (Table.napuniPodatke) ",
								ex2);
					} finally {
						try {
							if (inp != null) {
								inp.close();
							}
						} catch (java.io.IOException ioe) {
						}
					}

				case Types.BLOB:
				case Types.VARBINARY:
				case Types.LONGVARBINARY: // mysql-ov BLOB nije standardan tip
											// bloba, vec je ogranicen na 64k i
											// nema klasicnu oznaku tipa 2004
											// (Blob) vec -3 (VarBinary)
					java.sql.Blob b;
					b = rs.getBlob(i + 1);
					if (rs.wasNull()) {
						zapis[i] = null;
						break;
					}
					byte[] pd = null;

					if (b != null) {
						pd = b.getBytes(1L, (int) b.length()); // DA, prvi bajt
																// nije na
																// poziciji 0
																// !!!
					}
					zapis[i] = pd;
					break;
				case Types.CLOB:
					java.sql.Clob c;
					c = rs.getClob(i + 1);
					if (rs.wasNull()) {
						zapis[i] = null;
						break;
					}

					java.io.Reader in = null;
					java.io.InputStream ins = null;
					int kom = 0;

					Reader r = null;
					r = c != null ? c.getCharacterStream() : null;
					String str = null;
					if (r != null) {
						str = "";

						int rez = 0, off = 0, l = 512;
						char[] buf = null;
						try {
							buf = new char[l];
							while ((rez = r.read(buf, 0, l)) != -1) {
								str += new String(buf, 0, rez);
								off += rez;
							} // while

						} catch (IOException ioe) {
							Logger.fatal(
									"CLOB citanje podataka nije uspjesno obavljeno - CSC SynchModul.constructRedak",
									ioe);
						} finally {
							buf = null;
							try {
								if (r != null) {
									r.close();
								}
							} catch (IOException ioe) {
							}
							c = null;
						}
					} else { // if r!=null
						str = null;
					}

					zapis[i] = str;
					break;

				default:
					Logger.log("Nepoznata situacija?!? " + tipKolone
							+ " naziv kolone: " + colName);
				} // switch
			} catch (SQLException sqle) {
				Logger.fatal("Sql iznimka kod CSC SynchModul.constructRedak",
						sqle);
			}

		} // kraj for petlje

		return zapis;
	} // constructRedak

	private final void openLogger() {
		String f = biz.sunce.optika.GlavniFrame
				.vratiKonfiguracijskiDirektorijKorisnika();
		String sep = System.getProperty("file.separator");

		// za svaki slucaj provjeriti i zatvoriti stari stream
		if (fw != null) {
			try {
				fw.close();
			} catch (IOException ioe) {
			}
		}

		try {
			fw = new FileWriter(f + sep + "Synch_logger.txt", true);
		} catch (IOException ioe) {
			Logger.log("CSC SynchModul.updateSystemTable problem kod otvaranja Synch loggera");
			fw = null;
		}
	} // openLogger

	// metoda zaduzena updateati sistemske tablice poslane sa weba. Update se
	// sastoji od pokusaja
	// inserta, ako insert ne prodje, onda pokusaj updatea
	public boolean updateSystemTable(Tablica tablica) {
		boolean rez = true;

		openLogger();

		if (tablica == null) {
			Logger.log("CSC SynchModul.updateSystemTable - tablica je null?!?");
			return false;
		}
		Collection r = (Collection) tablica.getPodaci();
		List l = (List) r;

		// sistemske tablice ce uvijek u sebi nositi u prva dva zapisa podatke o
		// kolonama i kljucevima
		// to je zato sto ce se neke sistemske tablice updateati samo na nekim
		// kolonama, ne na svima
		// pa ce se na osnovu skracenog popisa kolona i kljuceva graditi
		// drugaciji update upiti (nadam se ne i insert...)
		// List kolone=(List)getTableColumns(tablica.getNaziv());
		// List kljucevi=(List)getTablePrimaryKey(tablica.getNaziv());
		List kolone = null, kljucevi = null;

		if (r == null) {
			Logger.log("CSC SynchModul.updateSystemTable - kolekcija tablice je null?!?");
			return false;
		}
		Object[] retci = r.toArray(), redak = null;

		if (retci == null) {
			Logger.log("CSC SynchModul.updateSystemTable - retci su null?!?");
			return false;
		}
		int vel = retci.length;

		if (vel < 3) {

			Logger.log("CSC SynchModul.updateSystemTable - broj zapisa u tablici manji od 3?!? vel:"
					+ vel);
			return false;
		}

		kolone = (List) retci[0];
		kljucevi = (List) retci[1];

		this.podesiRedneBrojeveKljucevaKolonama(kolone, kljucevi); // treba
																	// uvijek
																	// prvo
																	// podesiti

		java.sql.Connection con = null;

		try {
			con = DAOFactory.getConnection();
			for (int i = 2; i < vel; i++) {
				Object[] zapis = (Object[]) retci[i];

				updateRetkaTablice(kolone, kljucevi, zapis, tablica.getNaziv(),
						con);
			}
		} catch (SQLException sqle) {
			Logger.fatal("SQL iznimka kod SynchModul.updateSystemTable", sqle);
		} catch (Exception e) {
			log("Opæa iznimka kod update system table: " + e);
		} finally {
			try {
				if (con != null) {
					DAOFactory.freeConnection(con);
				}
			} catch (SQLException sqle) {
			}
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException ioe) {
			}
		}
		return rez;
	} // updateSystemTable
} // klasa
