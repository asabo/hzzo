/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import biz.sunce.optika.hzzo.HzzoPostavkeFrame;
import biz.sunce.util.GUI;
import biz.sunce.util.KontrolneZnamenkeUtils;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2005.05.01
 * 
 * @author asabo
 * 
 */
public final class PostavkeFrame extends JFrame {
 
	private static final long serialVersionUID = 3756670833568648782L;

	String[] tipoviRacuna = { "", "R-1", "R-2" };

	Font arial = new Font("Arial", Font.PLAIN, 11);
	
	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtNazivTvtrke = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtAdresa = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtMjesto = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtTelefon = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtFax = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtMaticniBroj = null;
	private javax.swing.JLabel jLabel6 = null;
	private javax.swing.JTextField jtBrojRacuna = null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JTextField jtBanka = null;

	private PostavkeBean postavke = null;
	private javax.swing.JPanel doljnjiPanel = null;
	private javax.swing.JButton jbSpremi = null;
	private JButton jbLogo;
	private javax.swing.JButton jbOdustani = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JTextField jtEmail = null;
	private javax.swing.JButton jbHzzo = null;
	private javax.swing.JLabel jLabel9 = null;
	private javax.swing.JComboBox jcTipRacuna = null;

	/**
	 * This is the default constructor
	 */
	public PostavkeFrame() {
		super("Postavke");
		initialize();
		this.napuniPodatke();
		// ne pokazujemo hzzo ako stranka nema pravo koristiti ga
		if (!GlavniFrame.isImaPravoNaHzzo())
			this.jbHzzo.setVisible(false);
	}

	public void centriraj() {
		final PostavkeFrame ja = this;
		Thread t = new Thread() {
			public void run() {
				biz.sunce.util.GUI.centrirajFrame(ja);
				try {
					sleep(500);
				} catch (InterruptedException inter) {
					return;
				}
				this.setPriority(Thread.MIN_PRIORITY);
				yield();
				
			}
		};
		SwingUtilities.invokeLater(t);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		// this.setSize(320, 300);
		this.setIconImage(GlavniFrame.getImageIcon().getImage());
		this.setContentPane(getJContentPane());

		this.setTitle("Postavke");
		biz.sunce.util.GUI.centrirajFrame(this);
		Thread t = new Thread() {
			public void run() {
				this.setPriority(Thread.MIN_PRIORITY);
				yield();
				validate();
				setSize(365, 315);
			}
		};

		SwingUtilities.invokeLater(t);
		centriraj();

	}

	public void napuniPodatke() {
		this.postavke = new PostavkeBean();
		this.jtNazivTvtrke.setText(this.postavke.getTvrtkaNaziv());
		this.jtAdresa.setText(this.postavke.getTvrtkaAdresa());
		this.jtBanka.setText(this.postavke.getTvrtkaBanka());
		this.jtBrojRacuna.setText(this.postavke.getTvrtkaRacun());
		this.jtFax.setText(this.postavke.getTvrtkaFax());
		this.jtMaticniBroj.setText(this.postavke.getTvrtkaOIB());
		this.jtMjesto.setText(this.postavke.getMjestoRada());
		this.jtTelefon.setText(this.postavke.getTvrtkaTelefon());
		this.jtEmail.setText(this.postavke.getTvrtkaEmail());
		this.jcTipRacuna.setSelectedItem(PostavkeBean.getTipRacuna());
	}// napuniPodatke

	public void spremiPodatke() {
		this.postavke.setMjestoRada(this.jtMjesto.getText().trim());
		this.postavke.setTvrtkaAdresa(this.jtAdresa.getText().trim());
		this.postavke.setTvrtkaBanka(this.jtBanka.getText().trim());
		this.postavke.setTvrtkaEmail(this.jtEmail.getText().trim());
		this.postavke.setTvrtkaFax(this.jtFax.getText().trim());
		this.postavke.setTvrtkaOIB(this.jtMaticniBroj.getText().trim());
		this.postavke.setTvrtkaNaziv(this.jtNazivTvtrke.getText().trim());
		this.postavke.setTvrtkaRacun(this.jtBrojRacuna.getText().trim());
		this.postavke.setTvrtkaTelefon(this.jtTelefon.getText().trim());
		PostavkeBean.setTipRacuna((String) this.jcTipRacuna.getSelectedItem());
		this.postavke.saveData();
		
	}// spremiPodatke

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints17 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints19 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
			consGridBagConstraints31.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints31.weightx = 1.0;
			consGridBagConstraints31.gridy = 8;
			consGridBagConstraints31.gridx = 1;
			consGridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints14.gridy = 8;
			consGridBagConstraints14.gridx = 0;
			consGridBagConstraints14.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints22.weightx = 1.0;
			consGridBagConstraints22.gridy = 5;
			consGridBagConstraints22.gridx = 1;
			consGridBagConstraints21.gridy = 5;
			consGridBagConstraints21.gridx = 0;
			consGridBagConstraints21.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints19.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints19.weighty = 1.0;
			consGridBagConstraints19.weightx = 1.0;
			consGridBagConstraints19.gridy = 10;
			consGridBagConstraints19.gridx = 0;
			consGridBagConstraints19.gridwidth = 2;
			consGridBagConstraints16.gridy = 9;
			consGridBagConstraints16.gridx = 0;
			consGridBagConstraints16.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints17.weightx = 1.0;
			consGridBagConstraints17.gridy = 9;
			consGridBagConstraints17.gridx = 1;
			consGridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints15.weightx = 1.0;
			consGridBagConstraints15.gridy = 7;
			consGridBagConstraints15.gridx = 1;
			consGridBagConstraints13.gridy = 7;
			consGridBagConstraints13.gridx = 0;
			consGridBagConstraints13.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints11.gridy = 6;
			consGridBagConstraints11.gridx = 0;
			consGridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints12.weightx = 1.0;
			consGridBagConstraints12.gridy = 6;
			consGridBagConstraints12.gridx = 1;
			consGridBagConstraints9.gridy = 4;
			consGridBagConstraints9.gridx = 0;
			consGridBagConstraints9.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints10.weightx = 1.0;
			consGridBagConstraints10.gridy = 4;
			consGridBagConstraints10.gridx = 1;
			consGridBagConstraints7.gridy = 3;
			consGridBagConstraints7.gridx = 0;
			consGridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints8.weightx = 1.0;
			consGridBagConstraints8.gridy = 3;
			consGridBagConstraints8.gridx = 1;
			consGridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints6.weightx = 1.0;
			consGridBagConstraints6.gridy = 2;
			consGridBagConstraints6.gridx = 1;
			consGridBagConstraints5.gridy = 2;
			consGridBagConstraints5.gridx = 0;
			consGridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints3.gridy = 1;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints4.weightx = 1.0;
			consGridBagConstraints4.gridy = 1;
			consGridBagConstraints4.gridx = 1;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints2.weightx = 1.0;
			consGridBagConstraints2.gridy = 0;
			consGridBagConstraints2.gridx = 1;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints1);
			jContentPane.add(getJtNazivTvtrke(), consGridBagConstraints2);
			jContentPane.add(getJLabel1(), consGridBagConstraints3);
			jContentPane.add(getJtAdresa(), consGridBagConstraints4);
			jContentPane.add(getJLabel2(), consGridBagConstraints5);
			jContentPane.add(getJtMjesto(), consGridBagConstraints6);
			jContentPane.add(getJLabel3(), consGridBagConstraints7);
			jContentPane.add(getJtTelefon(), consGridBagConstraints8);
			jContentPane.add(getJLabel4(), consGridBagConstraints9);
			jContentPane.add(getJtFax(), consGridBagConstraints10);
			jContentPane.add(getJLabel5(), consGridBagConstraints11);
			jContentPane.add(getJtMaticniBroj(), consGridBagConstraints12);
			jContentPane.add(getJLabel6(), consGridBagConstraints13);
			jContentPane.add(getJtBrojRacuna(), consGridBagConstraints15);
			jContentPane.add(getJLabel7(), consGridBagConstraints16);
			jContentPane.add(getJtBanka(), consGridBagConstraints17);
			jContentPane.add(getDoljnjiPanel(), consGridBagConstraints19);
			jContentPane.add(getJLabel8(), consGridBagConstraints21);
			jContentPane.add(getJtEmail(), consGridBagConstraints22);
			jContentPane.add(getJLabel9(), consGridBagConstraints14);
			jContentPane.add(getJcTipRacuna(), new GridBagConstraints(1, 8, 1,
					1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(1, 0, 1, 0), 0, 0));

			jContentPane
					.setToolTipText("Naziv Vaše tvrtke, adresa, ostali podaci ");
			jContentPane.setPreferredSize(new java.awt.Dimension(320, 235));
		}
		return jContentPane;
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Naziv tvrtke: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes jtNazivTvtrke
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtNazivTvtrke() {
		if (jtNazivTvtrke == null) {
			jtNazivTvtrke = new javax.swing.JTextField();
			jtNazivTvtrke.setPreferredSize(new java.awt.Dimension(120, 20));
			jtNazivTvtrke.setFont(arial);
		}
		return jtNazivTvtrke;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Adresa: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jtAdresa
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAdresa() {
		if (jtAdresa == null) {
			jtAdresa = new javax.swing.JTextField();
			jtAdresa.setFont(arial);
		}
		return jtAdresa;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Mjesto: ");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jtMjesto
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtMjesto() {
		if (jtMjesto == null) {
			jtMjesto = new javax.swing.JTextField();
			jtMjesto.setFont(arial);
		}
		return jtMjesto;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("telefon: ");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jtTelefon
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtTelefon() {
		if (jtTelefon == null) {
			jtTelefon = new javax.swing.JTextField();
			jtTelefon.setFont(arial);
		}
		return jtTelefon;
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Fax: ");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jtFax
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtFax() {
		if (jtFax == null) {
			jtFax = new javax.swing.JTextField();
			jtFax.setFont(arial);
		}
		return jtFax;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("OIB:");
		}
		return jLabel5;
	}

	/**
	 * This method initializes jtMaticniBroj
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtMaticniBroj() {
		if (jtMaticniBroj == null) {
			jtMaticniBroj = new javax.swing.JTextField();
			jtMaticniBroj.setFont(arial);			
			jtMaticniBroj.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent evt) {
					jtMaticniBrojFocusLost(evt);
				}
			});
		}
		return jtMaticniBroj;
	}

	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if (jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Broj raèuna: ");
		}
		return jLabel6;
	}

	/**
	 * This method initializes jtBrojRacuna
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojRacuna() {
		if (jtBrojRacuna == null) {
			jtBrojRacuna = new javax.swing.JTextField();
			jtBrojRacuna.setFont(arial);
		}
		return jtBrojRacuna;
	}

	/**
	 * This method initializes jLabel7
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if (jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Banka: ");
		}
		return jLabel7;
	}

	/**
	 * This method initializes jtBanka
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBanka() {
		if (jtBanka == null) {
			jtBanka = new javax.swing.JTextField();
			jtBanka.setFont(arial);
		}
		return jtBanka;
	}

	/**
	 * This method initializes doljnjiPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getDoljnjiPanel() {
		if (doljnjiPanel == null) {
			doljnjiPanel = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout20 = new java.awt.FlowLayout();
			layFlowLayout20.setAlignment(java.awt.FlowLayout.RIGHT);
			doljnjiPanel.setLayout(layFlowLayout20);
			doljnjiPanel.add(getJbHzzo(), null);
			doljnjiPanel.add(getJbSpremi(), null);
			doljnjiPanel.add(getJbOdustani(), null);
			doljnjiPanel.add(getJbLogo());
		}
		return doljnjiPanel;
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
			jbSpremi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jbSpremi.setMnemonic(java.awt.event.KeyEvent.VK_S);
			jbSpremi.setFont(arial);
			jbSpremi.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					spremiPodatke();
					dispose();
				}
			});
		}
		return jbSpremi;
	}

	/**
	 * This method initializes jbOdustani
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbOdustani() {
		if (jbOdustani == null) {
			jbOdustani = new javax.swing.JButton();
			jbOdustani.setText("Odustani");
			jbOdustani.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jbOdustani.setMnemonic(java.awt.event.KeyEvent.VK_O);
			jbOdustani.setFont(arial);
			jbOdustani.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}

			});
		}
		return jbOdustani;
	}

	/**
	 * This method initializes jLabel8
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if (jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText("Email: ");
		}
		return jLabel8;
	}

	/**
	 * This method initializes jtEmail
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtEmail() {
		if (jtEmail == null) {
			jtEmail = new javax.swing.JTextField();
			jtEmail.setFont(arial);
			jtEmail.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent evt) {
					jtEmailFocusLost(evt);
				}
			});
		}
		return jtEmail;
	}

	/**
	 * This method initializes jbHzzo
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbHzzo() {
		if (jbHzzo == null) {
			jbHzzo = new javax.swing.JButton();
			jbHzzo.setText("Hzzo");
			jbHzzo.setToolTipText("Hzzo postavke");
			jbHzzo.setMnemonic(java.awt.event.KeyEvent.VK_H);
			jbHzzo.setFont(arial);
			jbHzzo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					HzzoPostavkeFrame hzzo = new HzzoPostavkeFrame(postavke);
					hzzo.setVisible(true);
					biz.sunce.util.GUI.centrirajFrame(hzzo);
				}
			});
		}
		return jbHzzo;
	}

	/**
	 * This method initializes jLabel9
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if (jLabel9 == null) {
			jLabel9 = new javax.swing.JLabel();
			jLabel9.setText("Tip raèuna:");
		}
		return jLabel9;
	}

	/**
	 * This method initializes jcTipRacuna
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getJcTipRacuna() {
		if (jcTipRacuna == null) {
			jcTipRacuna = new javax.swing.JComboBox();
			jcTipRacuna.setMinimumSize(new java.awt.Dimension(80, 25));
			jcTipRacuna.setPreferredSize(new java.awt.Dimension(85, 25));
			jcTipRacuna.setFont(arial);
			if (tipoviRacuna != null)
				for (int i = 0; i < tipoviRacuna.length; i++) {
					jcTipRacuna.addItem(tipoviRacuna[i]);
				}
		}// if tip racuna je null
		return jcTipRacuna;
	}
	
	private void jtMaticniBrojFocusLost(FocusEvent evt) {
		String txt = this.jtMaticniBroj.getText().trim();
		if (!KontrolneZnamenkeUtils.ispravanOIB(txt)){
			GlavniFrame.alert("OIB nije ispravan!");
			this.jtMaticniBroj.requestFocusInWindow();
		}
	}
	
	private void jtEmailFocusLost(FocusEvent evt) {
		String email = this.jtEmail.getText().trim();
		if (!email.equals("") && !Util.checkEmailAddress(email)){
			GlavniFrame.alert("Email adresa nije ispravna!");
			this.jtEmail.requestFocusInWindow();
		}
	}
	
	private JButton getJbLogo() {
		if(jbLogo == null) {
			jbLogo = new JButton();
			jbLogo.setText("logo tvrtke..");
			jbLogo.setFont(arial);
			jbLogo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					logoTvrtke();					
				}
			});
		}
		return jbLogo;
	}
	
	private void logoTvrtke() {
		LogoFrame lf=new LogoFrame();
		
		lf.setVisible(true);
		GUI.centrirajFrame(lf);
	}
} // @jve:visual-info decl-index=0 visual-constraint="18,2"
