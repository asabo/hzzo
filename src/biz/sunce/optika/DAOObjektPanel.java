/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAO;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.JakiDAO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.opticar.vo.ValueObject;

/**
 * datum:2005.11.30
 * 
 * @author asabo
 * 
 */
public class DAOObjektPanel extends JPanel implements SlusacModelaTablice {

	private javax.swing.JPanel objektPanel = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private JXTable podaci = null;
	private javax.swing.JButton jbBrisi = null;
	private javax.swing.JButton jbNovi = null;
	private JakiDAO dao = null;
	TableModel model = null;
	GUIEditor guiPanel = null;
	// neki objektima se nece smjeti pobrisati 1 element (kao lijecnicima npr.)
	boolean sviElementiSeMoguBrisati = true;
	private javax.swing.JButton jbSpremi = null;

	// ako je true, NISTA se ne moze brisati
	boolean nemaBrisanja = false;
	// ako je true, nista se ne moze mjenjati, samo dodavati ili brisati (ako
	// nije iskljuceno i to, naravno)
	boolean nemaMjenjanja = false;
	boolean nemaDodavanja = false;

	/**
	 * This is the default constructor
	 */
	public DAOObjektPanel() {
		super();
		initialize();
	}

	// 05.12.05. -asabo- metoda postavlja sve potrebno za rad nad nekim DAO
	// objektom
	public void setDAOObjekt(JakiDAO dao) {
		this.dao = dao;

		this.model = new TableModel(this.dao, this.podaci);
		this.model.dodajSlusaca(this);
		this.podaci.setModel(this.model);
		this.setObjektPanel(this.dao.getGUIEditor());

		this.model.reload();
		postaviStatuseGumbica();
	}// setDAOObjekt

	private ArrayList<SlusacDaoObjektPanela> slusaci = new ArrayList<SlusacDaoObjektPanela>(
			1);

	public void dodajSlusaca(SlusacDaoObjektPanela slusac) {
		this.slusaci.add(slusac);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		consGridBagConstraints11.gridy = 1;
		consGridBagConstraints11.gridx = 1;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints2.insets = new java.awt.Insets(5, 3, 76, 21);
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints2.weighty = 1.0;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 3;
		consGridBagConstraints2.gridx = 0;
		consGridBagConstraints3.gridy = 0;
		consGridBagConstraints3.gridx = 1;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		consGridBagConstraints2.gridwidth = 2;
		consGridBagConstraints4.gridy = 2;
		consGridBagConstraints4.gridx = 1;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
		consGridBagConstraints1.insets = new java.awt.Insets(40, 0, 0, 0);
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints1.gridheight = 3;
		consGridBagConstraints1.ipadx = 0;
		consGridBagConstraints1.ipady = 0;
		consGridBagConstraints3.insets = new java.awt.Insets(30, 0, 0, 0);
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getObjektPanel(), consGridBagConstraints1);
		this.add(getJScrollPane(), consGridBagConstraints2);
		this.add(getJbBrisi(), consGridBagConstraints3);
		this.add(getJbNovi(), consGridBagConstraints4);
		this.add(getJbSpremi(), consGridBagConstraints11);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(800*faktor, 600*faktor);
		this.setPreferredSize(new java.awt.Dimension(800, 600));
		this.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		this.setToolTipText("forma za ureðivanje postavki");

	}

	/**
	 * This method initializes objektPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getObjektPanel() {
		if (objektPanel == null) {
			objektPanel = new javax.swing.JPanel();
			objektPanel.setLayout(null);
			objektPanel.setMinimumSize(new java.awt.Dimension(200, 100));
			objektPanel.setPreferredSize(new java.awt.Dimension(400, 200));
			objektPanel.setSize(new java.awt.Dimension(400, 200));
		}
		return objektPanel;
	}

	private void setObjektPanel(GUIEditor panel) {
		this.remove(getObjektPanel());
		objektPanel = (JPanel) panel;
		this.guiPanel = panel; // da znamo i za njega da ne treba castati
								// kasnije
		this.guiPanel.napuniPodatke(null);

		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints1.gridheight = 2;
		consGridBagConstraints1.ipadx = 2;
		consGridBagConstraints1.ipady = 2;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.CENTER;
		consGridBagConstraints1.insets = new Insets(25, 5, 5, 5);
		objektPanel.setMinimumSize(new Dimension(300, 200));
		this.add(objektPanel, consGridBagConstraints1);

	}// setObjektPanel

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getPodaci() {
		if (podaci == null) {
			podaci = new JXTable();
			podaci.setSelectionBackground(new java.awt.Color(198, 180, 255));
		}
		return podaci;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getPodaci());
			jScrollPane.setPreferredSize(new java.awt.Dimension(453, 300));
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jbBrisi
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbBrisi() {
		if (jbBrisi == null) {
			jbBrisi = new javax.swing.JButton();
			jbBrisi.setText("Brisi");
			jbBrisi.setMnemonic(java.awt.event.KeyEvent.VK_B);
			jbBrisi.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseReleased(java.awt.event.MouseEvent e) {
					brisiObjekt();
				}
			});
		}
		return jbBrisi;
	}

	/**
	 * This method initializes jbNovi
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbNovi() {
		if (jbNovi == null) {
			jbNovi = new javax.swing.JButton();
			jbNovi.setText("Novi");
			jbNovi.setMnemonic(java.awt.event.KeyEvent.VK_N);
			jbNovi.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseReleased(java.awt.event.MouseEvent e) {
					try {
						ValueObject vo = (ValueObject) dao.getVOClass()
								.newInstance();
						guiPanel.napuniPodatke(vo);

						jbBrisi.setEnabled(false); // brisati se u ovoj
													// situaciji ne moze
						jbSpremi.setEnabled(true); // ali se spremiti moze
					} catch (InstantiationException e1) {
						Logger.fatal(
								"InstExc kod kreiranja novog objekta u DAOObjektPanel",
								e1);
					} catch (IllegalAccessException e1) {
						Logger.fatal(
								"IllegalAccessExc kod kreiranja novog objekta u DAOObjektPanel",
								e1);
					} catch (ClassNotFoundException e1) {
						Logger.fatal(
								"ClassNotFoundExc kod kreiranja novog objekta u DAOObjektPanel",
								e1);
					}
				}
			});
		}
		return jbNovi;
	}

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		this.spremiObjekt();

		if (posiljatelj == this.model) {
			List data = this.model.getData();
			if (data == null || data.size() <= redak)
				return;
			ValueObject obj = (ValueObject) data.get(redak);
			this.guiPanel.napuniPodatke(obj);
			this.postaviStatuseGumbica();
		}
	}// redakOznacen

	private boolean brisiObjekt() {
		boolean rez = true;
		ValueObject obj = this.guiPanel.vratiPodatke();

		if (obj == null)
			return false;

		if ((obj != null
		// moramo vidjeti jeli smije brisati sve elemente
				&& (!this.isSviElementiSeMoguBrisati() && obj != null
						&& obj.getSifra() != null && obj.getSifra().intValue() == 1))
				|| (obj != null && obj.getCreated() == 0L) // novi objekt?!? u
															// principu se ne
															// moze dogoditi
		) {
			JOptionPane.showInternalMessageDialog(this,
					"Ne možete izbrisati dotièni podatak!", "Upozorenje",
					JOptionPane.WARNING_MESSAGE);
			rez = false;
		} else if (obj.getCreated() == 0L) // novi je objekt?
		{
			JOptionPane.showInternalMessageDialog(this,
					"Ne možete izbrisati neupisani podatak!", "Upozorenje",
					JOptionPane.WARNING_MESSAGE);
			rez = false;
		} else {
			try {
				this.dao.delete(obj);
				this.guiPanel.pobrisiFormu();
				this.model.reload();
				this.postaviStatuseGumbica();
			} catch (SQLException e) {
				Logger.fatal("SQL iznimka kod pokušaja brisanja objekta klase "
						+ obj.getClass().toString(), e);
			}
		}
		return rez;
	}// brisiObjekt

	// sprema objekt i ako se to uspjesno obavi, briše formu za editiranje
	// objekta...
	private boolean spremiObjekt() {
		boolean rez = true;
		ValueObject obj = this.guiPanel.vratiPodatke();
		try {
			if (obj != null
					&& ((obj.getSifra() == null || obj.getSifra().intValue() == DAO.NEPOSTOJECA_SIFRA) || (obj
							.isModified() && obj.getSifra().intValue() != DAO.NEPOSTOJECA_SIFRA))) {
				// 01.03.06. -asabo- dodatak, svaki objekt koji se kreira mora
				// imati sifru DAO.NEPOSTOJECA_SIFRA
				if (obj != null && obj.getSifra() == null)
					obj.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));

				String poruka = this.dao.narusavaLiObjektKonzistentnost(obj);

				if (poruka != null) {
					boolean upozorenje = false;
					if (poruka.startsWith("@")) {
						poruka = poruka.substring(1);
						upozorenje = true;
					}

					JOptionPane.showMessageDialog(this.getParent(), poruka,
							"upozorenje!", JOptionPane.WARNING_MESSAGE);
					if (!upozorenje)
						return false;
				}

				// zakrpa, pomagala nemaju integer sifru, vec string, pa to
				// treba predvidjeti
				// i na osnovu tog podatka napraviti update.
				// ovo je privremena zakrpa, treba kvalitetno rijesiti problem
				// nebrojcanih sifara objekata
				if (obj != null && (obj instanceof PomagaloVO)) {
					PomagaloVO pvo = (PomagaloVO) obj;
					if (pvo.getCreated() == 1L) // pomagala ucitana iz baze
												// podataka imaju created kolonu
												// postavljenu
						obj.setSifra(null); // nema sifre, idemo na update...
				}

				// jeli novi?
				if (obj.getSifra() != null
						&& obj.getSifra().intValue() == DAO.NEPOSTOJECA_SIFRA 
						&& obj.getCreated()<=0 && obj.getCreatedBy()==null) {
					obj.setCreated(System.currentTimeMillis());
					obj.setCreatedBy(Integer.valueOf(GlavniFrame
							.getSifDjelatnika()));
					this.dao.insert(obj);
					if (obj.getSifra() == null
							|| obj.getSifra().intValue() == DAO.NEPOSTOJECA_SIFRA)
						JOptionPane
								.showMessageDialog(
										this.getParent(),
										"Nastala je greška pri pokušaju upisivanja podataka!",
										"Upozorenje!",
										JOptionPane.WARNING_MESSAGE);
				} else {
					obj.setLastUpdatedBy(Integer.valueOf(GlavniFrame
							.getSifDjelatnika()));

					boolean rezultat = this.dao.update(obj);
					if (!rezultat)
						JOptionPane
								.showMessageDialog(
										this.getParent(),
										"Nastala je greška pri pokušaju mijenjanja podataka!",
										"Upozorenje!",
										JOptionPane.WARNING_MESSAGE);
				}
				this.guiPanel.pobrisiFormu(); // spremili smo ga, vise ga ne
												// treba na formi
				this.model.reload();

				postaviStatuseGumbica();
			}// if obj ispravan
			else
			// objekt postoji, ali nije diran? Pobrisimo ga iz forme van, te
			// postavimo statuse gumbica shodno tome..
			if (obj != null && !obj.isModified()) {
				this.guiPanel.pobrisiFormu();
				this.postaviStatuseGumbica();
			}

		} catch (SQLException sqle) {
			Logger.fatal(
					"SQL Iznimka kod updateanja ValueObjekta kod DAOObjektPanel ",
					sqle);
			JOptionPane
					.showMessageDialog(
							GlavniFrame.getInstanca(),
							"Nastao je problem pri pohranjivanju novih podataka, pogledajte u poruke sustava",
							"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			rez = false;
		} catch (Exception e) {
			Logger.fatal(
					"Iznimka kod updateanja ValueObjekta kod DAOObjektPanel ",
					e);
			JOptionPane
					.showMessageDialog(
							GlavniFrame.getInstanca(),
							"Nastala je iznimka pri pohranjivanju novih podataka, pogledajte u poruke sustava",
							"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			rez = false;
		}
		if (!rez)
			for (SlusacDaoObjektPanela slusac : slusaci) {
				slusac.objektSpremljen(obj);
			}

		return rez;
	}// spremiObjekt

	private void postaviStatuseGumbica() {
		if (guiPanel.vratiPodatke() == null) {
			this.jbSpremi.setEnabled(false);
			this.jbNovi.setEnabled(true);
			this.jbBrisi.setEnabled(false);
		} else {
			this.jbSpremi.setEnabled(true);
			this.jbNovi.setEnabled(false);
			if (!nemaBrisanja)
				this.jbBrisi.setEnabled(true);
		}
	}// postaviStatuseGumbica

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {
	}

	public boolean isSviElementiSeMoguBrisati() {
		return sviElementiSeMoguBrisati;
	}

	public void setSviElementiSeMoguBrisati(boolean b) {
		sviElementiSeMoguBrisati = b;
	}

	/**
	 * This method initializes jbSpremi
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbSpremi() {
		if (jbSpremi == null) {
			jbSpremi = new javax.swing.JButton();
			jbSpremi.setText("Spremi");
			jbSpremi.setMnemonic(java.awt.event.KeyEvent.VK_S);
			jbSpremi.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseReleased(java.awt.event.MouseEvent e) {
					spremiObjekt();

				}
			});
		}
		return jbSpremi;
	}

	public boolean isNemaBrisanja() {
		return nemaBrisanja;
	}

	public void setNemaBrisanja(boolean b) {
		nemaBrisanja = b;

	}

	public boolean isNemaMjenjanja() {
		return nemaMjenjanja;
	}

	public void setNemaMjenjanja(boolean b) {
		nemaMjenjanja = b;
	}

	public boolean isNemaDodavanja() {
		return nemaDodavanja;
	}

	public void setNemaDodavanja(boolean b) {
		nemaDodavanja = b;
	}

}
