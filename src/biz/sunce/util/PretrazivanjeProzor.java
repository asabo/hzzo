/*
 * Project opticari
 *
 */
package biz.sunce.util;

/**
 * datum:2005.05.04
 * @author asabo
 *
 */

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import biz.sunce.dao.DAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.SearchCriteriaObject;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;

/**
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: ANSA
 * </p>
 * 
 * @author Ante Sabo
 * @version 1.0 kada korisnik utipkava na nekom mjestu string za trazenje, ova
 *          klasa je zaduzena podici se i prikazati rezultate pretrazivanja na
 *          osnovu korisnikovih podataka. Takodjer, ako korisnik klikne na neki
 *          rezultat pretrazivanja ili udari Enter tipkom nad njime, zaduzena je
 *          obavijestiti o tome dogadjaju sve prijavljene slusace.
 */

@SuppressWarnings("serial")
public final class PretrazivanjeProzor extends javax.swing.JWindow implements
		java.awt.event.AdjustmentListener, MouseListener, KeyListener,
		TableCellEditor, FocusListener {
	javax.swing.JFrame frameNosac;
	Point mjesto;
	java.util.Set<SlusacOznaceneLabelePretrazivanja> slusaci;
	java.util.Set<Object> nadjeno;
	java.util.List<Labela> labele;
	String filter;
	int maksimumZaPretrazivanje = 5;

	boolean podaciUEditoruMjenjani; // jeli nesto tipkano...
	// 15.07.05. -asabo- koja je zadnja labela bila oznacena

	javax.swing.JScrollBar sb; // svaki prozor ima desno okomiti scrollbar
	int maks = 0;
	int sirina, visina, sirinaScrollbara;
	PretrazivacDretva pretrazivac = null;
	Component sidro = null;
	Labela oznacena = null;
	boolean trebaBarcode = false;
	DAO<ValueObject> objekt = null;
	// 14.07.05. -asabo- koji editor ce biti zaduzen za editiranje teksta u
	// formi
	private Component editor = null;
	// 15.07.05. -asabo- ako se dogadja kakva akcija pomicanja po editoru, da
	// stopEditing ne bi izasao van
	private boolean akcijaPomicanja = false;

	JTable tablica = null;

	// ako je postavljena ova vrijednost, onda pri prikazivanju labela program
	// nece koristiti
	// ValueObject.toString() poziv vec ValueObject.getValue(descriptorColumn)
	String descriptorColumn;

	// ako ne bude null, nega ce PretrazivanjeProzor proslijedjivati umjesto
	// praznog stringa
	SearchCriteria kriterij = null;
	List kr = null;

	int x, y;
	int redak, stupac;

	int rbOznaceneBoje = -1;
	int vis = 20;
	List podaci = null;
	// 26.07.05. -asabo- koliki broj znakova ce predstavljati minimum za
	// zapocinjanje pretrazivanja
	int minimumZnakovaZaPretrazivanje = 1;
	SearchCriteriaObject soKriterij = null;
	boolean enabled = true;
	boolean oznaciPrviElement = true;

	class PretrazivacDretva extends Thread {
		PretrazivanjeProzor prozor;

		boolean ziv = true;

		public PretrazivacDretva(PretrazivanjeProzor prozor) {
			this.prozor = prozor;
		}

		@Override
		public void run() {
			if (this == null || !enabled)
				return; // mhm?!?

			this.setName("PretrazivacDretva filter: "
					+ filter
					+ " frame: "
					+ (frameNosac != null ? frameNosac.getClass().toString()
							: "(nema)"));
			this.setPriority(Thread.MIN_PRIORITY);

			this.ziv = true;
			try {
				List temp = null;
				if (this.prozor != null && this.prozor.nadjeno != null)
					this.prozor.nadjeno.clear();

				yield();
				
				if (!this.ziv)
					return;
				// if (this.prozor.podaci==null)
				// this.prozor.podaci=this.prozor.pronadjiSveArtikle();

				// temp=this.prozor.podaci;

				String f;

				f = this.prozor.getFilter();

				if (f == null)
					return;
				f = f.toLowerCase().trim();
				if (f.equals("") || f.length() < minimumZnakovaZaPretrazivanje
						|| f.length() > maksimumZaPretrazivanje)
					return;

				if (!this.ziv)
					return;

				if (soKriterij != null && (sidro != null && sidro.hasFocus())) {
					soKriterij.setFilterValue(f);
					temp = this.prozor.objekt.findAll(soKriterij);
					yield();
				} 
				else
				// ako postoji kriterij, trebamo ugraditi filter u njega i
				// pozvati findAll metodu
				if (kriterij != null && (sidro != null && sidro.hasFocus())) {
					if (kr == null)
						kr = new ArrayList(1);
					kr.add(f);
					kriterij.setPodaci(kr);
					temp = this.prozor.objekt.findAll(kriterij);
					yield();
					kr = null;
				} 
				else 
				 if (enabled && (sidro != null && sidro.hasFocus()))
				 {
					temp = this.prozor.objekt.findAll(f);
					yield();
				 }
				// 26.07.05. -asabo- iako se pretrazivanje u bazi podataka
				// izvrsava sa filterom, nemaju sve klase ugradjen String kao
				// ulazni param, pa
				// vracaju nazad popis _svih_ elemenata, pa treba jos jednom
				// profiltrirati 'rucno'.. zasada je tako, poslije ce se ovo
				// tu ukinuti...

				int tmpSize = temp==null?0:temp.size();
				
				if (this.prozor.nadjeno == null && this.ziv && temp != null
						&& f != null)
					this.prozor.nadjeno = new HashSet<Object>(tmpSize);
				else if (this.prozor.nadjeno != null)
					this.prozor.nadjeno.clear(); // just in case

				if (!this.ziv) {
					if (temp != null)
						temp.clear();
					temp = null;
				}

				// pretrazujemo samo ako ovaj ima fokus
				if (this.ziv && temp != null && f != null
						&& (sidro != null && sidro.hasFocus()))
					for (int i = 0; i < tmpSize; i++) {
						if (!this.ziv)
							return;

						Object vo = temp != null && tmpSize > i ? temp.get(i)
								: null;

						if (vo == null)
							break;

						// String el = vo.toString().toLowerCase().trim();
						if (this.ziv && this.prozor.nadjeno != null
								&& (sidro != null && sidro.hasFocus()))
							this.prozor.nadjeno.add(vo);
						else
							return;
					}// for i
				else {
					if (temp != null)
						temp.clear();
					temp = null;
					if (this.prozor != null && this.prozor.nadjeno != null)
						this.prozor.nadjeno.clear();
					return;
				}

				if (this.prozor != null && this.prozor.podaci != null)
					this.prozor.podaci.clear();

				if (this.prozor != null && this.ziv
						&& (sidro != null && sidro.hasFocus()))
					this.prozor.razmjestiElemente();
			} 
			catch (Exception sqle) 
			{
				if (!(sqle instanceof InterruptedException))
					Logger.fatal(
							"Iznimka pri pretrazivanju elemenata (94) klasa:"
									+ this.getClass().toString(), sqle);
			}

			// if (this.ziv && this.prozor!=null) this.prozor.pokaziSe(); // ok
			// otkrijmo to sta ima unutra
		}// run

		public void zaustavi() {
			this.ziv = false;
		}

		public boolean isZiv() {
			return this.ziv;
		}

	}// PretrazivacDretva klasa

	public void setMinimumZnakovaZaPretrazivanje(int minimum) {
		this.minimumZnakovaZaPretrazivanje = minimum;
	}

	public PretrazivanjeProzor(javax.swing.JFrame frame, DAO objekt, int x,
			int y, int sirina, int visina, Component sidro) {
		super(frame);

		this.frameNosac = frame;
		this.objekt = objekt;

		this.setFocusable(true);
		this.setVisible(false);
		this.getContentPane().setLayout(null);

		this.sirina = sirina;
		this.visina = visina;
		this.sidro = sidro;

		if (x == 0 && y == 0) {
			this.x = this.sidro.getX() + 20;
			this.y = this.sidro.getY() + 20;
		} else {
			this.x = x;
			this.y = y;
		}

		if (this.sidro != null) {
			this.sidro.addMouseListener(this);
			this.sidro.addKeyListener(this);
			this.sidro.addFocusListener(this);
		}

		sirinaScrollbara = 15;

		this.setSize(sirina, visina);
		this.getContentPane().setBackground(java.awt.Color.lightGray);
		// this.setLocationRelativeTo(sidro);

		if (this.getContentPane() instanceof JPanel) {
			JPanel rp = (JPanel) this.getContentPane();
			rp.setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
		}

		// this.pozicionirajSe();
		// this.setLocation(x,y);

		this.slusaci = new java.util.HashSet<SlusacOznaceneLabelePretrazivanja>();
		this.nadjeno = new java.util.HashSet<Object>();

		this.sb = new javax.swing.JScrollBar();
		sb.setBounds(sirina - sirinaScrollbara - 1, 2, sirinaScrollbara,
				visina - 3);
		sb.setSize(sirinaScrollbara, visina - 3);
		sb.setOrientation(Adjustable.VERTICAL);
		sb.setEnabled(true);
		sb.setVisible(true);
		this.getContentPane().add(sb);
		this.setFocusableWindowState(false);
		this.pretrazivac = new PretrazivacDretva(this);
		this.sb.addAdjustmentListener(this);
	}// Konstruktor

	public void dodajSlusaca(SlusacOznaceneLabelePretrazivanja slusac) {
		this.slusaci.add(slusac);
	}

	public void pozicionirajSe() {
		// ne bi smjeli zvati getEditor() osim ako nam stvarno ne treba...
		if (this.editor != null && this.editor.isShowing()) {
			Point p = this.editor.getLocationOnScreen();
			this.x = p.x + this.x;
			this.y = p.y + this.y;
			this.setMjesto(new Point(this.x, this.y), false);
		} else
			this.setMjesto(new Point(this.x, this.y), true);

	}

	public void setMjesto(Point mjesto) {
		this.setMjesto(mjesto, true);
	}

	public void setMjesto(Point mjesto, boolean relativno) {

		if (mjesto == null)
			return;

		this.mjesto = mjesto;

		double x, y;

		if (relativno) {

			x = mjesto.getX();
			y = mjesto.getY();

			if (this.sidro != null && this.sidro.isShowing()) {
				x += this.sidro.getLocationOnScreen().getX();
				y += this.sidro.getLocationOnScreen().getY();
			}

			/*
			 * JFrame tata=(JFrame)this.frameNosac;
			 * 
			 * double sirFrame=tata!=null?tata.getSize().getWidth():0; double
			 * visFrame=tata!=null?tata.getSize().getHeight():0; double
			 * sirProz=tata!=null?this.getSize().getWidth():0; double
			 * visProz=tata!=null?this.getSize().getHeight():0;
			 * 
			 * if ((x+sirProz)>sirFrame) x=sirFrame-sirProz; if
			 * ((y+visProz)>visFrame) y=visFrame-visProz;
			 */
			mjesto = new Point((int) x, (int) y);
		} // if relativno

		// this.setLocation(mjesto);
		this.setBounds(mjesto.x, mjesto.y, this.sirina, this.visina);
		// this.x=mjesto.x;
		// this.y=mjesto.y;
	}// setMjesto

	public String getFilter() {
		return this.filter;
	}

	public void setFilter(String filter) {
		if (filter != null) {
			filter = filter.toLowerCase().trim();
			if (this.filter == null)
				this.filter = filter;
			else
				synchronized (this.filter) {
					this.filter = filter;
				}
		}// if filter != null
			// NE provjeravati jeli filter null pa shodno tome pokretati
			// pretrazivanje..
		if (!enabled || !(this.sidro != null && this.sidro.hasFocus())) {
			this.sakrijSe();
			return;
		}

		if (this.filter != null)
			synchronized (this.filter) {
				this.pokreniPretrazivanje();
			}
	}// setFilter

	protected List pronadjiSveArtikle() throws SQLException {
		if (!enabled) {
			this.sakrijSe();
			return new ArrayList();
		}
		return this.objekt != null ? this.objekt.findAll(null)
				: new ArrayList();
	}

	private void pokreniPretrazivanje() {
		if (!enabled) {
			this.sakrijSe();
			return;
		}
		// za svaki slucaj pobrisati sve prije nadjeno
		if (this.pretrazivac != null && this.pretrazivac.isZiv()) {
			this.pretrazivac.zaustavi();
		}

		if (this.pretrazivac != null)
			synchronized (this.pretrazivac) {
				int ponavljanja = 10;
				// ako je prethodna dretva ziva pricekat cemo deset milisekundi
				// dok ne stane, bude li i dalje radila odustajemo
				while (this.pretrazivac.isAlive() && ponavljanja > 0) {
					ponavljanja--;
					try {
						Thread.yield();
						Thread.sleep(10);
					} catch (InterruptedException inte) {
						return;
					}
				}// while
			}// synch

		if (this.filter != null
				&& this.filter.trim().length() <= maksimumZaPretrazivanje
				&& this.filter.trim().length() >= minimumZnakovaZaPretrazivanje) {

			this.pretrazivac = new PretrazivacDretva(this);

			this.sakrijSe();
			this.pobrisiSveElemente();

			try {
				if (this.pretrazivac != null)
					SwingUtilities.invokeLater(this.pretrazivac);
			}
			// nekada se zna dogoditi, ne znam sta cu s tim, nema ozbiljnih
			// posljedica
			catch (IllegalThreadStateException ilet) {
			}

		} else
			this.sakrijSe();
	}// pokreniPretrazivanje

	public void pobrisiSveElemente() {
		if (this.oznacena != null)
			this.oznacena.setOznacena(false);
		this.oznacena = null;
		this.otpustiResurse();
		this.getContentPane().removeAll(); // brisemo sa popisa sve elemente,
											// dolaze novi
		this.rbOznaceneBoje = -1;
	}

	public void pokaziSe() {
		if (!enabled || (this.sidro != null && !this.sidro.hasFocus())) {
			this.sakrijSe();
			return;
		}
		this.pozicionirajSe();
		if (this.frameNosac != null && this.frameNosac.isShowing())
			this.setVisible(true);

		// ako treba zacrveniti prvi element tako da korisnik enterom potvrdi
		// unos
		if (this.oznaciPrviElement)
			this.pomakDolje();
	}// pokaziSe

	public void sakrijSe() {
		if (this.isVisible())
			this.setVisible(false);
		this.otpustiResurse();
	}

	public void otpustiResurse() {
		if (this.podaci != null)
			this.podaci.clear();
		this.podaci = null;
		if (this.nadjeno != null)
			this.nadjeno.clear();
		this.nadjeno = null;
		if (this.labele != null)
			this.labele.clear();
		this.labele = null;
		if (this.kr != null)
			this.kr.clear();
		this.kr = null;
	}// otpustiResurse

	public void misKliknut(Labela labela) {
		SlusacOznaceneLabelePretrazivanja sl;

		// 15.07.05. -asabo- da se zna koja je labela bila oznacena zadnja
		this.setOznacena(labela);

		// 20.07.05. -asabo- ako je editor postavljen, trebalo bi nekakvu
		// vrijednost i njemu postaviti..
		if (this.editor != null && labela != null) {
			if (this.editor instanceof JTextField) {
				JTextField jtf = (JTextField) this.editor;
				jtf.setText(labela.getText());
			}
		}

		Iterator<SlusacOznaceneLabelePretrazivanja> it = this.slusaci.iterator();
		while (it.hasNext())
		{
			sl = (SlusacOznaceneLabelePretrazivanja) it.next();
			if (sl != null && labela != null)
				sl.labelaOznacena(labela);
		}
		this.sakrijSe(); // vise ne trebas...
		this.registrirajDogadjajOznacivanjaLabeleUModeluTablice();
	}// misKliknut

	public void pomakDolje() {
		this.akcijaPomicanja = true;

		if (this.oznacena != null)
			this.oznacena.setOznacena(false);

		if (this.nadjeno == null)
			return;

		int zadnji = this.nadjeno != null ? this.nadjeno.size() - 1 : 0;

		if (this.rbOznaceneBoje == -1)
			this.rbOznaceneBoje = 0;
		else if (this.rbOznaceneBoje >= zadnji)
			this.rbOznaceneBoje = 0;
		else
			this.rbOznaceneBoje++;

		if (this.rbOznaceneBoje >= 0 && this.rbOznaceneBoje <= zadnji) {
			try { // 24.09.2004. privremena zakrpa dok ne shvatim sta je na
					// stvari
				this.oznacena = (Labela) this.labele.get(this.rbOznaceneBoje);
			} catch (Exception e) {
				System.err.println("Problem kod pomakDolje: "+e+" rb: "+this.rbOznaceneBoje);
				return;
			}

			this.sb.setValue(this.rbOznaceneBoje*this.vis);		
			
//			if (this.oznacena.getY() > (this.visina - this.vis))
//			{
//				this.sb.setValue(this.sb.getValue() + vis);				
//			}
			oznacena.setOznacena(true);
		}
	}// pomakDolje

	public void pomakGore() {
		this.akcijaPomicanja = true;

		if (this.oznacena != null)
			this.oznacena.setOznacena(false);
		int zadnji;

		if (this.nadjeno != null)
			zadnji = this.nadjeno.size() - 1;
		else
			return;

		if (this.rbOznaceneBoje == -1)
			this.rbOznaceneBoje = zadnji;
		else if (this.rbOznaceneBoje <= 0)
			this.rbOznaceneBoje = zadnji;
		else
			this.rbOznaceneBoje--;

		if (this.rbOznaceneBoje >= 0 && this.rbOznaceneBoje <= zadnji) {
			try {
				this.oznacena = (Labela) this.labele.get(this.rbOznaceneBoje);
			} catch (Exception e) {
				return;
			}

			if (this.oznacena.getY() < 0)
				this.sb.setValue(this.sb.getValue() - vis);

			oznacena.setOznacena(true);
		}
	}

	// ubacuje na listu sve elemente
	public void razmjestiElemente() {
		Labela labela = null;
		Container cp = this.getContentPane();
		int sir = this.sirina;
		int x = 5, y = 0;

		if (this.labele != null) {
			this.labele.clear();
			this.labele = null;
		}

		int nkom = 0;

		if (this.nadjeno != null)
			synchronized (this.nadjeno) {
				nkom = this.nadjeno != null ? this.nadjeno.size() : 0;
			}

		if (nkom > 1000)
			nkom = 1000; // zastitni mehanizam, ne ici vise od tog broja
							// elemenata

		this.labele = new java.util.ArrayList<Labela>(nkom);

		Iterator<Object> it = this.nadjeno!=null?this.nadjeno.iterator():null;
		int brojac = 0;
		
		while (brojac++<nkom && it.hasNext()){
			try {
				if (this.nadjeno != null)
					synchronized (this.nadjeno) {
						if (this.nadjeno == null)
							break;
						labela = new Labela(this);
						// isti k, bio descriptorColumn null ili ne...
						labela.setIzvornik(
								this.nadjeno != null ? it.next()
										: null, descriptorColumn);
					}

				if (this.labele != null && labela != null
						&& this.nadjeno != null)
					this.labele.add(labela);
				labela.setToolTipText(labela.getText());
			} catch (IndexOutOfBoundsException iobe) {
				break;
			}

			cp.add(labela);

			int tmp = sir - this.sb.getWidth() - x;
			if (labela != null) {
				labela.setBounds(x, y, tmp, vis);
				labela.setOriginalY(y);
				labela.setSize(tmp, vis);
				y += vis;
			}
		}// for i
		this.maks = y;

		// podesiti jos scrollbar
		if (this.maks > this.visina) {
			this.getContentPane().add(sb);
			sb.setMinimum(0);
			sb.setMaximum(maks - visina + vis);
			sb.setUnitIncrement(vis);
			sb.setValue(0);
			sb.setVisible(true);
		} else
			this.sb.setVisible(false);

		// nema elemenata, nema se sta ni prozor pojavljivati
		if (this.labele == null || this.labele.size() == 0)
			this.sakrijSe();
		else {
			this.pokaziSe();
			this.repaint();
		}

	}// razmjestiElemente

	/**
	 * adjustmentValueChanged
	 * 
	 * @param e
	 *            AdjustmentEvent
	 */
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Labela l;
		int x = 5;
		int v = this.sb.getValue();
		int size = this.labele != null ? this.labele.size() : 0;

		for (int i = 0; i < size; i++) {
			if (this.labele == null || i >= this.labele.size())
				break;
			try {
				l = (Labela) this.labele.get(i);
			} catch (IndexOutOfBoundsException iobe) {
				break;
			}

			l.setLocation(x, l.getOriginalY() - v);
		}// for i
	}// adjustmentValueChanged

	public Labela getOznacena() {
		return oznacena;
	}

	public void setOznacena(Labela oznacena) {
		this.oznacena = oznacena;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {

	}// keyPressed

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (!enabled) {
			this.sakrijSe();
			return;
		}
		// zasada samo ovako...
		podaciUEditoruMjenjani = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.pomakDolje();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.pomakGore();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.misKliknut(this.getOznacena());
		} else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			this.setFilter("");
			this.sakrijSe();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(final KeyEvent e) {
		if (!enabled) {
			this.sakrijSe();
			return;
		}

		// ignorirat cemo Enter tipku, nju aktivira keyPressed....
		if (!e.isActionKey() && e.getKeyChar() != KeyEvent.VK_BACK_SPACE
				&& e.getKeyChar() != KeyEvent.VK_ENTER) {
			String stariF = this.getFilter();

			if (e.getSource() instanceof JTextField) {
				final JTextField tf = (JTextField) e.getSource();
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.currentThread().setPriority(Thread.MIN_PRIORITY);							
							Thread.sleep(4);
							Thread.yield();
						} catch (Exception e) {
							return;
						}
						setFilter(tf.getText());
					}
				}.start();
			}// if
			else
				this.setFilter((stariF != null ? stariF : "") + e.getKeyChar());
		} else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
			String f = this.getFilter();
			if (f == null)
				return;
			try {
				f = f.substring(0, f.length() - 1);
			} catch (StringIndexOutOfBoundsException oube) {
				return;
			}

			this.setFilter(f);

			if (this.editor != null && this.editor.isShowing())
				this.setMjesto(this.getEditor().getLocationOnScreen(), false);
		}

		if (this.editor != null && this.editor.isShowing()) {
			Point p = this.getEditor().getLocationOnScreen();
			this.x = p.x + 10;
			this.y = p.y + 25;
		}
	}// keyTyped

	private Component getEditor() {
		if (this.editor == null) {
			this.editor = new TextFieldEditorPolje(this);
			this.editor.addKeyListener(this);
		}
		return this.editor;
	}// getEditor

	public Component getTableCellEditorComponent(JTable tablica, Object ulaz,
			boolean selektirano, int redak, int stupac) {
		this.sidro = this.getEditor(); // tu je ok imati editor...
		this.tablica = tablica;
		this.redak = redak;
		this.stupac = stupac;

		if (this.getEditor() instanceof JTextField) {
			JTextField tf = (JTextField) this.getEditor();
			tf.setText("");
			podaciUEditoruMjenjani = false;
		}

		this.sidro = this.getEditor();
		this.x = 10;
		this.y = 10;

		return this.getEditor();
	}// getTableCellEditorComponent

	private void registrirajDogadjajOznacivanjaLabeleUModeluTablice() {
		if (this.tablica != null && this.getOznacena() != null)
			this.tablica.getModel().setValueAt(
					this.getOznacena().getIzvornik(), redak, stupac);
	}

	public void cancelCellEditing() {
		this.sakrijSe();
	}

	public boolean stopCellEditing() {
		boolean rez = true;
		if (this.akcijaPomicanja)
			rez = false;

		this.akcijaPomicanja = false; // brisemo staru akciju pomicanja

		// ako se ide van trebaju se obaviti jos neke stvari...
		if (rez) {

			// ako je korisnik polje ostavio praznim, treba vidjeti ima li kakav
			// pregled na zadanoj koordinati
			// i ako ima - pobrisati ga (ali fizicki, treba bas delete from ...
			// hm, tu nas design zaj... )
			if (this.getEditor() instanceof JTextField) {
				JTextField tf = (JTextField) this.getEditor();
				if (tf.getText().trim().equals("") && podaciUEditoruMjenjani) {
					// brisanje podataka... samo ako je korisnik nesto utipkao u
					// polju...
					if (this.tablica != null)
						this.tablica.getModel().setValueAt(null, this.redak,
								this.stupac);

				}// if tf==""
			}// if editor==textField

			this.setFilter("");
			this.tablica = null; // izlazimo van iz editora, dakle nema vise
									// tablice kojoj bi mogli prijavljivati
									// eventualne dogadjaje... just in case
		}

		return rez;
	}// stopCellEditing

	public Object getCellEditorValue() {
		// nazalost tablica nikada ne zove ovu metodu kojoj je smisao dati nazad
		// vrijednost u editoru..
		return null;
	}// getCellEditorValue

	public boolean isCellEditable(EventObject arg0) {
		return true;
	}

	public boolean shouldSelectCell(EventObject arg0) {
		return true;
	}

	@Override
	public String toString() {
		return this.getFilter();
	}

	public void addCellEditorListener(CellEditorListener arg0) {
	}

	public void removeCellEditorListener(CellEditorListener arg0) {
	}

	public void setKriterij(SearchCriteria criteria) {
		kriterij = criteria;
		kr = new ArrayList(1);
	}

	public int getMaksimumZaPretrazivanje() {
		return maksimumZaPretrazivanje;
	}

	public void setMaksimumZaPretrazivanje(int i) {
		maksimumZaPretrazivanje = i;
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		this.sakrijSe(); // just in case
	}

	public SearchCriteriaObject getSoKriterij() {
		return soKriterij;
	}

	// searchObject kriterij postavlja se kao sredstvo 'objasnjavanja'
	// DAOObjektu
	// kako ce pretrazivati bazu. S obzirom da uvijek ulazi nekakav string
	// objekt
	// za pretrazivanje, on ce se ugradjivati u taj objekt i onda se zvati
	// findAll
	// sa njime
	public void setSoKriterij(SearchCriteriaObject object) {
		soKriterij = object;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public String getDescriptorColumn() {
		return descriptorColumn;
	}

	public boolean isOznaciPrviElement() {
		return oznaciPrviElement;
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
	}

	public void setDescriptorColumn(String descriptorColumn) {
		this.descriptorColumn = descriptorColumn;
	}

	public void setOznaciPrviElement(boolean oznaciPrviElement) {
		this.oznaciPrviElement = oznaciPrviElement;
	}

}// klasa
