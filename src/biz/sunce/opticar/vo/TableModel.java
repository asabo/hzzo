package biz.sunce.opticar.vo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAO;
import biz.sunce.optika.Logger;
import biz.sunce.util.tablice.sort.SabotovSortModel;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, SlusacModelaTablice
//ne moze biti final
public class TableModel<VO extends ValueObject> extends SabotovSortModel<VO> {

	private static final long serialVersionUID = 1L;

	public TableModel(DAO<VO> objekt, JXTable tablica) {
		super(false);
		filter = null;
		this.objekt = objekt;
		this.tablica = tablica;
		if (this.tablica != null && super.getSlusac() != null)
			this.tablica.addMouseListener(super.getSlusac());
	}

	public void setFilter(Object filter) {
		this.filter = filter;
		try {
			podaci = objekt == null ? podaci : objekt.findAll(this.filter);
			fireTableStructureChanged();
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod TableModel.setFilter(). Filter:"
					+ this.filter + " DAO:" + objekt.getClass().toString(), e);
		}
	}

	public void reload() {
		updatePodataka();
	}

	public void updatePodataka() {
		try {

			podaci = objekt == null ? podaci : objekt.findAll(filter);
			fireTableDataChanged();
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod punjenja podataka u TableModel. Objekt: "
							+ objekt, e);
		}
	}

	@Override
	public int getColumnCount() {
		return objekt == null ? super.getColumnCount() : objekt
				.getColumnCount();
	}

	public void dodajSlusaca(SlusacModelaTablice slusac) {
		if (slusac == null)
			return;
		if (slusaci == null)
			slusaci = new ArrayList<SlusacModelaTablice>();
		slusaci.add(slusac);
	}

	@Override
	public int getRowCount() {
		return podaci == null ? 0 : podaci.size();
	}

	@Override
	public boolean isCellEditable(int redak, int kolona) {
		VO vo = null;
		if (podaci != null)
			vo =  podaci.get(redak);
		return objekt == null ? super.isCellEditable(redak, kolona) : objekt
				.isCellEditable(vo, kolona);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return objekt == null ? super.getColumnClass(arg0) : objekt
				.getColumnClass(arg0);
	}

	@Override
	public Object getValueAt(int redak, int kolona) {
		VO vo = null;
		if (podaci != null && podaci.size() > redak)
			vo =  podaci.get(redak);
		return objekt == null ? vo.getValue(kolona) : objekt.getValueAt(vo,
				kolona);
	}

	@Override
	public void setValueAt(Object vrijednost, int redak, int kolona) {
		VO vo = null;
		if (podaci == null) {
			return;
		} else {
			vo =  podaci.get(redak);
			if (objekt == null)
				super.setValueAt(vrijednost, redak, kolona);
			else
				objekt.setValueAt(vo, vrijednost, kolona);
			return;
		}
	}

	@Override
	public String getColumnName(int kolona) {
		return objekt == null ? super.getColumnName(kolona) : objekt
				.getColumnName(kolona);
	}

	@Override
	public void removeTableModelListener(TableModelListener tablemodellistener) {
	}

	public void setData(List<VO> podaci) {
		if (this.podaci != null) {
			int kom = this.podaci.size();
			fireTableRowsDeleted(0, kom);
			this.podaci = null;
		}

		this.podaci = podaci;
		if (this.podaci != null)
			fireTableRowsInserted(0, this.podaci.size());
		else
			fireTableDataChanged();
	}

	@Override
	public List<VO> getData() {
		return podaci;
	}

	@Override
	public void updateRows() {
	}

	@Override
	public void misKliknut(java.awt.event.MouseEvent me) {
		if (slusaci != null) {
			int size = slusaci.size();
			for (int i = 0; i < size; i++) {
				SlusacModelaTablice sl = (SlusacModelaTablice) slusaci.get(i);
				sl.redakOznacen(tablica.getSelectedRow(), me, this);
			}

		}
	}

	@Override
	public void tablicaIzmjenjena(TableModelEvent tableEvent) {
		if (slusaci != null) {
			int size = slusaci.size();
			for (int i = 0; i < size; i++) {
				SlusacModelaTablice sl = (SlusacModelaTablice) slusaci.get(i);
				sl.redakIzmjenjen(tablica.getSelectedRow(), tableEvent, this);
			}

		}
	}

	@Override
	public DAO<VO> getDAO() {
		return objekt;
	}

	public void clearData() {
		if (podaci != null) {
			podaci.clear();
			podaci = null;
		}
	}

	DAO<VO> objekt;
	List<VO> podaci;
	List<SlusacModelaTablice> slusaci;
	JXTable tablica;
	private Object filter;
}