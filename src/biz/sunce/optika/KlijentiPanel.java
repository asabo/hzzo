/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.util.gui.DaNeUpit;

/**
 * datum:2005.05.13
 * 
 * @author asabo
 * 
 */
public class KlijentiPanel extends JPanel implements SlusacModelaTablice,
		SlusacKlijentiFramea {
	final KlijentiPanel ja = this;

	private class PretrazivacDretva extends Thread {
		private boolean ziv = false;

		@Override
		public void run() {
			ziv = true;
			try {
				// malo spavamo prije nego pocnemo pretrazivanje (mozda korisnik
				// jos nesto upise)
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				return;
			}

			String mjesto = jtMjesto.getText().toLowerCase().trim();
			String ime = jtIme.getText().toLowerCase().trim();
			String prezime = jtPrezime.getText().toLowerCase().trim();
			String zanimanje = jtZanimanje.getText().toLowerCase().trim();
			if (!ziv)
				return;
			if (mjesto.equals("") && ime.equals("") && prezime.equals("")
					&& zanimanje.equals("")) {
				model.setData(sviPodaci);
				return;
			}

			ArrayList l = new ArrayList();

			if (ziv) {
				int vel = sviPodaci != null ? sviPodaci.size() : 0;

				for (int i = 0; i < vel; i++) {
					if (!ziv)
						return;
					KlijentVO kvo = (KlijentVO) sviPodaci.get(i);
					// mjesto ne odgovara? idemo dalje
					if (!mjesto.equals("")
							&& kvo.getNazivMjesta().toLowerCase()
									.indexOf(mjesto) != -1) {
						l.add(kvo);
						continue;
					}
					if (!ziv)
						return;
					if (!ime.equals("")
							&& kvo.getIme().toLowerCase().indexOf(ime) != -1) {
						l.add(kvo);
						continue;
					}
					if (!ziv)
						return;
					if (!prezime.equals("")
							&& kvo.getPrezime().toLowerCase().indexOf(prezime) != -1) {
						l.add(kvo);
						continue;
					}
					if (!ziv)
						return;
					if (!zanimanje.equals("")
							&& kvo.getZanimanje().toLowerCase()
									.indexOf(zanimanje) != -1) {
						l.add(kvo);
						continue;
					}
					if (!ziv)
						return;
					// ako je dosao do tu, klijent zadovoljava sve zadane
					// parametre

				}// for i

				if (!ziv)
					return;
				model.setData(l);

				podaci.doLayout();
			}// if
		}// run

		public boolean isZiv() {
			return ziv;
		}

		public void setZiv(boolean b) {
			ziv = b;
		}

	}// PretrazivacDretva

	PretrazivacDretva pretrazivac = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JPanel jpFilteri = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtMjesto = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtIme = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtPrezime = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtZanimanje = null;
	private javax.swing.JScrollPane jspPodaci = null; // @jve:visual-info
														// decl-index=0
														// visual-constraint="106,383"
	private JXTable podaci = null;

	TableModel model = null;
	List sviPodaci = null;

	private javax.swing.JButton jbNoviKlijent = null;

	/**
	 * This is the default constructor
	 */
	public KlijentiPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		consGridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints3.weighty = 0.0D;
		consGridBagConstraints3.weightx = 0.0D;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints3.gridy = 2;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.weightx = 0.0D;
		consGridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
		consGridBagConstraints1.ipadx = 0;
		consGridBagConstraints1.ipady = 5;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints2.weighty = 0.0D;
		consGridBagConstraints2.weightx = 0.0D;
		consGridBagConstraints2.gridy = 1;
		consGridBagConstraints2.gridx = 0;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTH;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJpFilteri(), consGridBagConstraints2);
		this.add(getJspPodaci(), consGridBagConstraints3);
		this.setSize(800, 580);
		this.setPreferredSize(new java.awt.Dimension(800, 580));
		this.setMinimumSize(new java.awt.Dimension(800, 580));
		this.setLocation(0, 10);
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Klijenti");
			jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 24));
		}
		return jLabel;
	}

	/**
	 * This method initializes jpFilteri
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpFilteri() {
		if (jpFilteri == null) {
			jpFilteri = new javax.swing.JPanel();
			jpFilteri.add(getJbNoviKlijent(), null);
			jpFilteri.add(getJLabel1(), null);
			jpFilteri.add(getJtMjesto(), null);
			jpFilteri.add(getJLabel2(), null);
			jpFilteri.add(getJtIme(), null);
			jpFilteri.add(getJLabel3(), null);
			jpFilteri.add(getJtPrezime(), null);
			jpFilteri.add(getJLabel4(), null);
			jpFilteri.add(getJtZanimanje(), null);
			jpFilteri.setPreferredSize(new java.awt.Dimension(10, 30));
			jpFilteri.setBackground(new java.awt.Color(188, 187, 190));
		}
		return jpFilteri;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Mjesto: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jtMjesto
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtMjesto() {
		if (jtMjesto == null) {
			jtMjesto = new javax.swing.JTextField();
			jtMjesto.setPreferredSize(new java.awt.Dimension(75, 20));
			jtMjesto.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (!e.isActionKey())
						pokreniPretrazivanje();
				}
			});
		}
		return jtMjesto;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Ime: ");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jtIme
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtIme() {
		if (jtIme == null) {
			jtIme = new javax.swing.JTextField();
			jtIme.setPreferredSize(new java.awt.Dimension(75, 20));
			jtIme.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (!e.isActionKey())
						pokreniPretrazivanje();
				}
			});
		}
		return jtIme;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Prezime: ");
		}
		return jLabel3;
	}

	private javax.swing.JTextField getJtPrezime() {
		if (jtPrezime == null) {
			jtPrezime = new javax.swing.JTextField();
			jtPrezime.setPreferredSize(new java.awt.Dimension(75, 20));
			jtPrezime.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (!e.isActionKey())
						pokreniPretrazivanje();
				}
			});
		}
		return jtPrezime;
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Zanimanje: ");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jtZanimanje
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtZanimanje() {
		if (jtZanimanje == null) {
			jtZanimanje = new javax.swing.JTextField();
			jtZanimanje.setPreferredSize(new java.awt.Dimension(75, 20));
			jtZanimanje.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (!e.isActionKey())
						pokreniPretrazivanje();
				}
			});
		}
		return jtZanimanje;
	}

	private void pokreniPretrazivanje() {
		if (this.pretrazivac != null && this.pretrazivac.isAlive())
			this.pretrazivac.setZiv(false);

		this.pretrazivac = new PretrazivacDretva();
		this.pretrazivac.start();

	}// pokreniPretrazivanje

	private JXTable getPodaci() {
		if (podaci == null) {
			podaci = new JXTable();
			podaci.setToolTipText("dvostruki klik nad željenim klijentom...");
			this.model = new TableModel(DAOFactory.getInstance().getKlijenti(),
					this.podaci);

			this.podaci.setModel(this.model);
			this.model.updatePodataka(); // obavezno pozvati da se aktivira
											// povlacenje svjezih podataka iz db
			this.model.dodajSlusaca(this);
			this.sviPodaci = this.model.getData();
		}
		return podaci;
	}

	/**
	 * This method initializes jspPodaci
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJspPodaci() {
		if (jspPodaci == null) {
			jspPodaci = new javax.swing.JScrollPane();
			jspPodaci.setViewportView(getPodaci());
			jspPodaci
					.setComponentOrientation(java.awt.ComponentOrientation.UNKNOWN);
			jspPodaci.setPreferredSize(new java.awt.Dimension(800, 500));
			jspPodaci.setMinimumSize(new java.awt.Dimension(800, 500));
		}
		return jspPodaci;
	}

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		// ovako znamo koji imamo tableModel i koji ValueObject-i sjede unutra
		if (posiljatelj == this.model && event.getClickCount() == 2
				&& redak >= 0) {
			KlijentVO kvo = (KlijentVO) this.model.getData().get(redak);
			KlijentFrame kf = new KlijentFrame();
			kf.setOznaceni(kvo);
			kf.dodajSlusaca(ja);
			kf.setVisible(true);

		}
		// dvostruki desni klik brise klijenta, naravno pod uvjetom da klijent
		// kaze ok to hocu
		else if (posiljatelj == this.model
				&& redak >= 0
				&& event.getButton() == MouseEvent.BUTTON3
				&& event.getClickCount() == 2
				&& DaNeUpit.upit("Jeste sigurni da želite pobrisati klijenta?",
						"Brisati klijenta?", GlavniFrame.getInstanca())) {
			KlijentVO kvo = (KlijentVO) this.model.getData().get(redak);
			pobrisiKlijenta(kvo);
		}// else

	}

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {
	}

	/**
	 * This method initializes jbNoviKlijent
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbNoviKlijent() {
		if (jbNoviKlijent == null) {
			jbNoviKlijent = new javax.swing.JButton();
			jbNoviKlijent.setText("Novi");
			jbNoviKlijent.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					KlijentFrame kf = new KlijentFrame();
					kf.dodajSlusaca(ja);
					kf.show();
				}
			});
		}
		return jbNoviKlijent;
	}

	private void pobrisiKlijenta(KlijentVO kvo) {
		try {
			DAOFactory.getInstance().getKlijenti().delete(kvo);
			klijentIzmjenjen(null); // nek se osvjezi prikaz ako je sve proslo
									// ok
		} catch (SQLException e) {
			Logger.fatal("Greška prilikom brisanja klijenta kod KlijentiPanel",
					e);
			JOptionPane
					.showMessageDialog(
							GlavniFrame.getInstanca(),
							"Nastala je greška pri pokušaju brisanja klijenta. Molimo Vas da kontaktirate administratora sustava",
							"Upozorenje", JOptionPane.WARNING_MESSAGE);
		}
	}// pobrisiKlijenta

	public void klijentIzmjenjen(KlijentFrame pozivac) {
		this.model.updatePodataka(); // obavezno pozvati da se aktivira
										// povlacenje svjezih podataka iz db
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
