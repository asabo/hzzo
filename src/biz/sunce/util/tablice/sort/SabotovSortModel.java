package biz.sunce.util.tablice.sort;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelListener;

import biz.sunce.dao.DAO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.util.tablice.SlusacTablice;

public abstract class SabotovSortModel extends DefaultSortTableModel {

	private static final long serialVersionUID = -8412418959046397554L;
	public boolean sortiranje = false;
	private SlusacTablice slusac = null;
	private boolean imaKljuc = false;
	Collator croTextOrder = Collator.getInstance(new java.util.Locale("hr",
			"hr"));

	// 14.04.04. sortModeli koji nadjacavaju ovu klasu ako smatraju potrebnim
	// sebe slati columnComparatoru
	// da ih ovaj zove pri usporedjivanju redaka, onda te klase u kontruktorima
	// trebaju ovu varijablu postaviti na true
	// I NADJACATI metodu usporediDvaRetka. Tada ce ih ColumnComparator pozivati
	// za usporedbu kolona..
	private boolean slatiModelPriSortiranju = false;

	public SabotovSortModel(boolean imaKljuc) {
		super();
		this.imaKljuc = imaKljuc;
		this.slusac = new SlusacTablice(this);
		this.addTableModelListener(this.slusac);
		this.setSlatiModelPriSortiranju(true);
	}

	@Override
	public void addTableModelListener(TableModelListener listener) {
		super.addTableModelListener(listener);
	}

	public SlusacTablice getSlusac() {
		return this.slusac;
	}

	public abstract void updateRows();

	public abstract List getData(); // 01.05.05. - asabo -

	public abstract DAO getDAO(); // 01.05.05. - asabo -

	/**
	 * svaki sortModel koji nasljedjuje SabotovSortModel morat ce imati ovu
	 * metodu koja ce se pozivati kada korisnik klikne misem na neko polje
	 * tablice
	 * 
	 * @param me
	 */
	public abstract void misKliknut(java.awt.event.MouseEvent me);

	/**
	 * svaki sortModel koji naslijedjuje SabotovSortModel morat ce imati metodu
	 * tablicaIzmjena koja ce se automatski zvati ako se promjeni neki podatak
	 * unutar tablice
	 */
	public abstract void tablicaIzmjenjena(
			javax.swing.event.TableModelEvent tableEvent);

	// po defaultu ova metoda ne radi nista pametno, ne sortira nista konkretno,
	// trebao bi ju nadjacati
	// sortModel koji osjeca da ima potrebu to napraviti
	public int usporediDvaRetka(Object prviRedak, Object drugiRedak, int kolona) {
		int rez = 0;
		DAO d = this.getDAO();
		Object prvi, drugi;

		prvi = d.getValueAt((ValueObject) prviRedak, kolona);
		drugi = d.getValueAt((ValueObject) drugiRedak, kolona);

		if (prvi instanceof String && drugi instanceof String)
			return this.croTextOrder.compare(prvi, drugi);
		else if ((prvi instanceof Comparable) && (drugi instanceof Comparable)) {
			Comparable cp, cd;
			cp = (Comparable) prvi;
			cd = (Comparable) drugi;
			return cp.compareTo(cd);
		}

		return rez;
	}// usporediDvaRetka

	@Override
	public void sortColumn(int col, boolean ascending) {
		List v;
		sortiranje = true;
		v = this.getData();

		int di = col;

		if (this.imaKljuc)
			di++;

		ColumnComparator comparator;

		// ako se salje modem pri sortiranju, onda ce ColumnComparator uvijek
		// zvati model.usporediDvaRetka() da dobije rezultat van
		if (this.isSlatiModelPriSortiranju())
			comparator = new ColumnComparator(di, ascending, this);
		else
			comparator = new ColumnComparator(di, ascending);

		Collections.sort(v, comparator);

		sortiranje = false;

		this.updateRows();
	}

	public boolean isSlatiModelPriSortiranju() {
		return slatiModelPriSortiranju;
	}

	public void setSlatiModelPriSortiranju(boolean slatiModelPriSortiranju) {
		this.slatiModelPriSortiranju = slatiModelPriSortiranju;
	}

}// klasa
