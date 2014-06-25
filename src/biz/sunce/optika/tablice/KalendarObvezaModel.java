/*
 * Project opticari
 *
 */
package biz.sunce.optika.tablice;

import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.PregledDAO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.PregledVO;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2005.07.12
 * 
 * @author asabo
 * 
 */
public final class KalendarObvezaModel extends TableModel {
	java.util.Calendar startniDatum, temp;
	int kolona = 7;
	int vremInterval = 15;
	int startSat = 8, startMin = 0;
	int krajSat = 20, krajMin = 0;
	JComboBox lijecnici = null;
	JXTable tablica = null;
	int KONTROLA_ZA_MJESECI = PostavkeBean.getIntPostavkaSustava(
			"KONTROLA_ZA_MJESECI", 12);

	TableCellRenderer iscrtavacVrijemeKolone = null;
	private PregledVO prazanObjekt = new PregledVO();

	Hashtable podaci = new Hashtable();

	PregledDAO pregledi = DAOFactory.getInstance().getPregledi();

	public void setPocetak(int sat, int min) {
		this.startSat = sat;
		this.startMin = min;
		fireTableDataChanged();
	}

	public void setKraj(int sat, int min) {
		this.krajSat = sat;
		this.krajMin = min;
		fireTableDataChanged();
	}

	public void setInterval(int interval) {
		this.vremInterval = interval;
		fireTableDataChanged();
	}

	public KalendarObvezaModel(DAO objekt, JXTable tablica,
			JComboBox lijecnici, TableCellRenderer iscrtavacVrijemeKolone) {
		super(objekt, tablica);
		startniDatum = java.util.Calendar.getInstance();
		temp = java.util.Calendar.getInstance();
		this.lijecnici = lijecnici;
		this.tablica = tablica;
		this.iscrtavacVrijemeKolone = iscrtavacVrijemeKolone;
	}

	@Override
	public int getColumnCount() {
		return kolona + 1;

	}

	@Override
	public int getRowCount() {
		int kom = ((this.krajSat * 60 + this.krajMin) - (this.startSat * 60 + this.startMin));

		if (this.vremInterval > 0)
			kom = (kom) / this.vremInterval;
		else
			kom = 0;

		return kom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int redak, int kolona) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	@Override
	public Class getColumnClass(int arg0) {
		try {
			return Class.forName("java.lang.String");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private final int izracunajVrijemeZaCeliju(int redak, int kolona) {
		return this.startSat * 60 + this.startMin + redak * this.vremInterval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public final Object getValueAt(int redak, int kolona) {
		int tmp = this.izracunajVrijemeZaCeliju(redak, kolona);
		int sat = tmp / 60;
		int min = tmp % 60;

		if (kolona == 0) {
			return (sat < 10 ? "0" + sat : "" + sat) + ":"
					+ (min < 10 ? "0" + min : "" + min);
		} else {
			// datum pregleda
			temp.setTimeInMillis(startniDatum.getTimeInMillis()); // za svaki
																	// slucaj
																	// podesiti
																	// ih da
																	// krenu od
																	// iste
																	// startne
																	// tocke
			temp.set(Calendar.DAY_OF_MONTH,
					startniDatum.get(Calendar.DAY_OF_MONTH) + kolona - 1);
			temp.set(Calendar.HOUR_OF_DAY, sat);
			temp.set(Calendar.MINUTE, min);
			temp.set(Calendar.SECOND, 0);
			temp.set(Calendar.MILLISECOND, 0);

			PregledVO pvo = null;

			// prvo pogledati iz Hashmapa (brze je)
			pvo = (PregledVO) this.podaci.get(temp);

			if (pvo == null)
				try {
					pvo = (PregledVO) pregledi.read(temp);

					if (pvo != null)
						this.podaci.put(temp.clone(), pvo);
					else
						this.podaci.put(temp.clone(), this.prazanObjekt);
				} catch (SQLException e) {
					Logger.fatal(
							"SQL iznimka kod KalendarObvezaModel.getValueAt ",
							e);
					pvo = null;
				}

			if (pvo != null && pvo != this.prazanObjekt)
				return pvo;

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object vrijednost, int redak, int kolona) {
		int tmp = this.izracunajVrijemeZaCeliju(redak, kolona);
		int sat = tmp / 60;
		int min = tmp % 60;

		// datum pregleda
		temp.setTimeInMillis(startniDatum.getTimeInMillis()); // za svaki slucaj
																// podesiti ih
																// da krenu od
																// iste tocke
		temp.set(Calendar.DAY_OF_MONTH, startniDatum.get(Calendar.DAY_OF_MONTH)
				+ kolona - 1); // X dana naprijed
		temp.set(Calendar.HOUR_OF_DAY, sat);
		temp.set(Calendar.MINUTE, min);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);

		KlijentVO kvo = null;

		if (vrijednost instanceof KlijentVO)
			kvo = (KlijentVO) vrijednost;

		PregledVO pvo = null;
		LijecnikVO lvo = null;

		// prvo vidjeti da ga nema mozda u hashmapu
		pvo = (PregledVO) this.podaci.get(temp);

		if (pvo == null || pvo == this.prazanObjekt)
			try {
				pvo = (PregledVO) pregledi.read(temp);

			} catch (SQLException e) {
				Logger.fatal("SQL iznimka kod KalendarObvezaModel.getValueAt ",
						e);
				pvo = null;
			}

		// nasli smo objekt, dakle postoji zapis o njemu, ali dolazna vrijednost
		// je null, znaci
		// korisnik hoce pobrisati postojeci objekt...
		if (pvo != null && pvo != this.prazanObjekt && vrijednost == null)
			try {
				// hackon - ako postavimo sifru lijecnika da je null, onda ce ga
				// fizicki izbrisati iz baze
				pvo.setSifLijecnika(null);
				DAOFactory.getInstance().getPregledi().delete(pvo);
				this.podaci.put(temp.clone(), this.prazanObjekt); // brisemo ga
																	// i sa
																	// liste
																	// tako da
																	// se zna
				return; // prekid daljeg posla...
			} catch (SQLException sqle) {
				String pvs = pvo != null && pvo.getSifra() != null ? ""
						+ pvo.getSifra().intValue() : "(nema sifre)";

				Logger.fatal(
						"SQL iznimka kod KalendarObvezaModel.setValueAt() - brisanje objekta sifra: "
								+ pvs, sqle);
			}

		if (kvo == null) // moze se i to dogoditi, ako korisnik nije unio
							// nijednog klijenta...
			return; // tada u biti ne trebamo raditi nista, ali provjera nije
					// mogla biti ranije napravljena zbog deletanja

		// ako ne postoji pvo za zadani datum/vrijeme, kreirat cemo novog
		if (pvo == null) {
			pvo = new PregledVO();
			pvo.setCreated(Calendar.getInstance().getTimeInMillis());
			pvo.setCreatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
			pvo.setKontrolaZaMjeseci(Integer.valueOf(KONTROLA_ZA_MJESECI));
		} else {
			pvo.setLastUpdated(Calendar.getInstance().getTimeInMillis());
			pvo.setLastUpdatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
		}

		pvo.setSifKlijenta(kvo.getSifra());
		pvo.setDatVrijeme(temp);

		lvo = (LijecnikVO) this.lijecnici.getSelectedItem();
		pvo.setSifLijecnika(lvo.getSifra());

		try {

			if (pvo.getSifra().intValue() == DAO.NEPOSTOJECA_SIFRA)
				DAOFactory.getInstance().getPregledi().insert(pvo);
			else
				DAOFactory.getInstance().getPregledi().update(pvo);
		} catch (SQLException sqle) {
			Logger.fatal("SQL iznimka kod KalendarObvezaModel.setValueAt. ",
					sqle);
		}

		if (pvo != null)
			this.podaci.put(temp.clone(), pvo);
		else
			this.podaci.put(temp.clone(), this.prazanObjekt);
	}// setValueAt

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int kolona) {

		if (kolona == 0)
			return "vrijeme";

		temp.setTimeInMillis(startniDatum.getTimeInMillis());
		temp.set(Calendar.DAY_OF_MONTH,
				this.startniDatum.get(Calendar.DAY_OF_MONTH) + kolona - 1);

		return this.convertCalendarToString(temp);
	}

	private String convertCalendarToString(Calendar c) {
		int dan, mj, god, dt;
		if (c == null)
			return null;
		dan = c.get(Calendar.DAY_OF_MONTH);
		mj = c.get(Calendar.MONTH) + 1;
		god = c.get(Calendar.YEAR);
		dt = c.get(Calendar.DAY_OF_WEEK) - 1;

		return Util.daniUTjednuSkraceni[dt] + " - "
				+ (dan < 10 ? "0" + dan : "" + dan) + "."
				+ (mj < 10 ? "0" + mj : "" + mj) + "." + god + "\n";
	}

	@Override
	public void updateRows() {
	}

	@Override
	public List getData() {
		return new ArrayList(); // zasada nista, nemamo sta sortirati...
	}

	@Override
	public DAO getDAO() {
		return null;
	}

	@Override
	public void misKliknut(MouseEvent me) {
	}

	@Override
	public void tablicaIzmjenjena(TableModelEvent tableEvent) {
	}

	public java.util.Calendar getStartniDatum() {
		return startniDatum;
	}

	public void setStartniDatum(java.util.Calendar calendar) {
		startniDatum = calendar;
		this.podaci.clear(); // neka se malo memorija oslobodi...
		fireTableStructureChanged();

		TableColumn vrijemeKolona = this.tablica.getColumn("vrijeme");
		vrijemeKolona.setCellRenderer(this.iscrtavacVrijemeKolone);

	}

}
