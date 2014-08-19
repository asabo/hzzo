/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import biz.sunce.optika.GlavniFrame;
import biz.sunce.util.beans.PostavkeBean;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
/**
 * datum:2006.03.11
 * 
 * @author asabo
 * 
 */
public final class HzzoPostavkeFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507311512874491318L;

	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtHzzoBrojIsporucitelja = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtPomakTekstaUdesnoRacun = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtPomakTekstaDoljeRacun = null;
	private JCheckBox jcIspisUGlavniPrinter;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtFaktorPovecanjaSirinaRacun = null;
	private javax.swing.JLabel jLabel6 = null;
	private javax.swing.JTextField jtFaktorPovecanjaDuzinaRacun = null;
	private javax.swing.JButton jbPohrani = null;

	private PostavkeBean postavke = null;
	private javax.swing.JCheckBox jcAutomatskoUgradjivanjePozivaNaBroj = null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JTextField jtBrojRacunaZaDopunskoOsiguranje = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JTextField jtPomakTekstaDoljePotOc = null;
	private javax.swing.JLabel jLabel9 = null;
	private javax.swing.JTextField jtPomakTekstaDesnoPotOc = null;
	private javax.swing.JLabel jLabel10 = null;
	private javax.swing.JLabel jLabel11 = null;
	private javax.swing.JCheckBox jcKontrolaOsobnihRacuna = null;
	private javax.swing.JCheckBox jcBrojaciOvisniOGodini = null;
	
	Font font = new Font("Arial", Font.PLAIN, 11);

	/**
	 * This is the default constructor
	 */
	public HzzoPostavkeFrame(PostavkeBean postavke) {
		super();
		this.postavke = postavke;
		initialize();
		napuniFormu();
		Thread t = new Thread() {
			public void run() {
				this.setPriority(Thread.MIN_PRIORITY);
				yield();
				validate();
				pack();
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
		int faktor = GlavniFrame.getFaktor();		
		this.setSize(405*faktor, 318*faktor);
		this.setContentPane(getJContentPane());
		this.setTitle("Hzzo postavke");
		this.setIconImage(GlavniFrame.getImageIcon().getImage());
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.jtHzzoBrojIsporucitelja.setEditable(false);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints51 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints41 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints52 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints61 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints71 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
			consGridBagConstraints16.gridy = 10;
			consGridBagConstraints16.gridx = 0;
			consGridBagConstraints15.gridy = 9;
			consGridBagConstraints15.gridx = 0;
			consGridBagConstraints15.gridwidth = 2;
			consGridBagConstraints61.gridy = 6;
			consGridBagConstraints61.gridx = 3;
			consGridBagConstraints71.gridy = 7;
			consGridBagConstraints71.gridx = 3;
			consGridBagConstraints41.gridy = 7;
			consGridBagConstraints41.gridx = 0;
			consGridBagConstraints41.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints52.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints52.weightx = 1.0;
			consGridBagConstraints52.gridy = 7;
			consGridBagConstraints52.gridx = 2;
			consGridBagConstraints22.gridy = 6;
			consGridBagConstraints22.gridx = 0;
			consGridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints31.weightx = 1.0;
			consGridBagConstraints31.gridy = 6;
			consGridBagConstraints31.gridx = 2;
			consGridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints21.weightx = 1.0;
			consGridBagConstraints21.gridy = 5;
			consGridBagConstraints21.gridx = 2;
			consGridBagConstraints14.gridy = 5;
			consGridBagConstraints14.gridx = 0;
			consGridBagConstraints51.gridy = 8;
			consGridBagConstraints51.gridx = 0;
			consGridBagConstraints51.gridwidth = 4;
			consGridBagConstraints13.gridy = 10;
			consGridBagConstraints13.gridx = 2;
			consGridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints12.weightx = 1.0;
			consGridBagConstraints12.gridy = 4;
			consGridBagConstraints12.gridx = 2;
			consGridBagConstraints11.gridy = 4;
			consGridBagConstraints11.gridx = 0;
			consGridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints8.gridy = 2;
			consGridBagConstraints8.gridx = 3;
			consGridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints10.weightx = 1.0;
			consGridBagConstraints10.gridy = 3;
			consGridBagConstraints10.gridx = 2;
			consGridBagConstraints9.gridy = 3;
			consGridBagConstraints9.gridx = 0;
			consGridBagConstraints9.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints6.gridy = 2;
			consGridBagConstraints6.gridx = 0;
			consGridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints7.weightx = 1.0;
			consGridBagConstraints7.gridy = 2;
			consGridBagConstraints7.gridx = 2;
			consGridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints4.weightx = 1.0;
			consGridBagConstraints4.gridy = 1;
			consGridBagConstraints4.gridx = 2;
			consGridBagConstraints3.gridy = 1;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints5.gridy = 1;
			consGridBagConstraints5.gridx = 3;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints2.weightx = 1.0;
			consGridBagConstraints2.gridy = 0;
			consGridBagConstraints2.gridx = 1;
			consGridBagConstraints2.gridwidth = 3;
			consGridBagConstraints14.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints22.anchor = java.awt.GridBagConstraints.EAST;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints1);
			jContentPane.add(getJtHzzoBrojIsporucitelja(),
					consGridBagConstraints2);
			jContentPane.add(getJLabel1(), consGridBagConstraints3);
			jContentPane.add(getJtPomakTekstaUdesnoRacun(),
					consGridBagConstraints4);
			jContentPane.add(getJLabel2(), consGridBagConstraints5);
			jContentPane.add(getJLabel3(), consGridBagConstraints6);
			jContentPane.add(getJtPomakTekstaDoljeRacun(),
					consGridBagConstraints7);
			jContentPane.add(getJLabel4(), consGridBagConstraints8);
			jContentPane.add(getJLabel5(), consGridBagConstraints9);
			jContentPane.add(getJtFaktorPovecanjaSirinaRacun(),
					consGridBagConstraints10);
			jContentPane.add(getJLabel6(), consGridBagConstraints11);
			jContentPane.add(getJtFaktorPovecanjaDuzinaRacun(),
					consGridBagConstraints12);
			jContentPane.add(getJbPohrani(), new GridBagConstraints(2, 11, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jContentPane.add(getJcAutomatskoUgradjivanjePozivaNaBroj(),
					consGridBagConstraints51);
			jContentPane.add(getJLabel7(), consGridBagConstraints14);
			jContentPane.add(getJtBrojRacunaZaDopunskoOsiguranje(),
					consGridBagConstraints21);
			jContentPane.add(getJLabel8(), consGridBagConstraints22);
			jContentPane.add(getJtPomakTekstaDoljePotOc(),
					consGridBagConstraints31);
			jContentPane.add(getJLabel9(), consGridBagConstraints41);
			jContentPane.add(getJtPomakTekstaDesnoPotOc(),
					consGridBagConstraints52);
			jContentPane.add(getJLabel10(), consGridBagConstraints61);
			jContentPane.add(getJLabel11(), consGridBagConstraints71);
			jContentPane.add(getJcKontrolaOsobnihRacuna(),
					consGridBagConstraints15);
			jContentPane.add(getJcBrojaciOvisniOGodini(),
					consGridBagConstraints16);
			jContentPane.add(getJCheckBox1(), new GridBagConstraints(0, 11, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jContentPane.setToolTipText("Hzzo postavke");
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
			jLabel.setText("Hzzo šifra isporuèitelja: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes jtHzzoBrojIsporucitelja
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtHzzoBrojIsporucitelja() {
		if (jtHzzoBrojIsporucitelja == null) {
			jtHzzoBrojIsporucitelja = new javax.swing.JTextField();
			jtHzzoBrojIsporucitelja
					.setToolTipText("matièni broj koji Vam je HZZO dodijelio");
		}
		return jtHzzoBrojIsporucitelja;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Pomak udesno teksta: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jtPomakTekstaUdesnoRacun
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPomakTekstaUdesnoRacun() {
		if (jtPomakTekstaUdesnoRacun == null) {
			jtPomakTekstaUdesnoRacun = new javax.swing.JTextField();
			jtPomakTekstaUdesnoRacun
					.setToolTipText("ako tekst na raèunu 'bježi' previše ulijevo... ");
		}
		return jtPomakTekstaUdesnoRacun;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("px");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Pomak teksta dolje: ");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jtPomakTekstaDoljeRacun
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPomakTekstaDoljeRacun() {
		if (jtPomakTekstaDoljeRacun == null) {
			jtPomakTekstaDoljeRacun = new javax.swing.JTextField();
			jtPomakTekstaDoljeRacun
					.setToolTipText("ako tekst bježi prema gore i dolje");
		}
		return jtPomakTekstaDoljeRacun;
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("px");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Faktor poveæanja po širini: ");
		}
		return jLabel5;
	}

	/**
	 * This method initializes jtFaktorPovecanjaSirinaRacun
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtFaktorPovecanjaSirinaRacun() {
		if (jtFaktorPovecanjaSirinaRacun == null) {
			jtFaktorPovecanjaSirinaRacun = new javax.swing.JTextField();
			jtFaktorPovecanjaSirinaRacun
					.setToolTipText("ako je tekst po širini prekratak ili preširok ovime se korigira");
		}
		return jtFaktorPovecanjaSirinaRacun;
	}

	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if (jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Faktor poveæanja po dužini: ");
		}
		return jLabel6;
	}

	/**
	 * This method initializes jtFaktorPovecanjaDuzinaRacun
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtFaktorPovecanjaDuzinaRacun() {
		if (jtFaktorPovecanjaDuzinaRacun == null) {
			jtFaktorPovecanjaDuzinaRacun = new javax.swing.JTextField();
		}
		return jtFaktorPovecanjaDuzinaRacun;
	}

	/**
	 * This method initializes jbPohrani
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbPohrani() {
		if (jbPohrani == null) {
			jbPohrani = new javax.swing.JButton();
			jbPohrani.setText("Pohrani");
			jbPohrani.setMnemonic(java.awt.event.KeyEvent.VK_P);
			jbPohrani.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					pohraniHzzoPostavke();
				}
			});
		}
		return jbPohrani;
	}

	private void pohraniHzzoPostavke() {
		boolean rez = true;
		int sifIsporucitelja = -1, pomakDesno = -1, pomakDolje = -1, pomakDesnoPotOc = -1, pomakDoljePotOc = -1;
		float faktorSirina = -1.0f, faktorDuzina = -1.0f;

		String sifIsp = this.jtHzzoBrojIsporucitelja.getText().trim();
		String pomUd = this.jtPomakTekstaUdesnoRacun.getText().trim();
		String pomDo = this.jtPomakTekstaDoljeRacun.getText().trim();

		String pomUdPotOc = this.jtPomakTekstaDesnoPotOc.getText().trim();
		String pomDoPotOc = this.jtPomakTekstaDoljePotOc.getText().trim();

		String fakSir = this.jtFaktorPovecanjaSirinaRacun.getText().trim();
		String fakDuz = this.jtFaktorPovecanjaDuzinaRacun.getText().trim();
		String brojRacDop = this.jtBrojRacunaZaDopunskoOsiguranje.getText()
				.trim();

		boolean automPozivNaBroj = this.jcAutomatskoUgradjivanjePozivaNaBroj
				.isSelected();
		boolean brojaciOvisniOGodini = this.jcBrojaciOvisniOGodini.isSelected();
		boolean ispisUGlavniPrinter = this.jcIspisUGlavniPrinter.isSelected();

		if (pomUd.length() > 0)
			try {
				pomakDesno = Integer.parseInt(pomUd);
			} catch (NumberFormatException nfe) {
				alert("Vrijednost za pomak udesno nije ispravna!");
				rez = false;
			}

		if (pomDo.length() > 0)
			try {
				pomakDoljePotOc = Integer.parseInt(pomDo);
			} catch (NumberFormatException nfe) {
				alert("Vrijednost za pomak dolje nije ispravna!");
				rez = false;
			}

		if (pomUdPotOc.length() > 0)
			try {
				pomakDesnoPotOc = Integer.parseInt(pomUdPotOc);
			} catch (NumberFormatException nfe) {
				alert("Vrijednost za pomak udesno recepta nije ispravna!");
				rez = false;
			}

		if (pomDoPotOc.length() > 0)
			try {
				pomakDoljePotOc = Integer.parseInt(pomDoPotOc);
			} catch (NumberFormatException nfe) {
				alert("Vrijednost za pomak dolje recepta nije ispravna!");
				rez = false;
			}

		if (fakSir.length() > 0)
			try {
				faktorSirina = Float.parseFloat(fakSir);
			} catch (NumberFormatException nfe) {
				alert("Vrijednost za faktor širine nije ispravna!");
				rez = false;
			}

		if (fakDuz.length() > 0)
			try {
				faktorDuzina = Float.parseFloat(fakDuz);
			} catch (NumberFormatException nfe) {
				alert("Vrijednost za faktor dužine nije ispravna!");
				rez = false;
			}

		if (!rez)
			return;

		if (rez && pomakDesno != -1)
			PostavkeBean.setPostavkaDB(PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_X,
					"" + pomakDesno);

		if (rez && pomakDolje != -1)
			PostavkeBean.setPostavkaDB(PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_Y,
					"" + pomakDolje);

		if (rez && pomakDesnoPotOc != -1)
			PostavkeBean.setPostavkaDB(PostavkeBean.TVRTKA_HZZO_RECEPT_POTOC_X,
					"" + pomakDesnoPotOc);

		if (rez && pomakDoljePotOc != -1)
			PostavkeBean.setPostavkaDB(PostavkeBean.TVRTKA_HZZO_RECEPT_POTOC_Y,
					"" + pomakDoljePotOc);

		if (rez && faktorSirina != -1.0f)
			PostavkeBean.setPostavkaDB(
					PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_SIRINA, ""
							+ faktorSirina);

		if (rez && faktorDuzina != -1.0f)
			PostavkeBean.setPostavkaDB(
					PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_DUZINA, ""
							+ faktorDuzina);

		if (rez)
			PostavkeBean.setPostavkaDB(
					PostavkeBean.TVRTKA_HZZO_RACUN_AUTOM_POZIV_BROJ,
					(automPozivNaBroj ? "D" : "N"));

		if (rez)
			PostavkeBean.setPostavkaDB(
					PostavkeBean.TVRTKA_HZZO_RACUN_ZA_DOPUNSKO, brojRacDop);

		if (rez)
			PostavkeBean.setKontrolaOsobnogRacuna(this.jcKontrolaOsobnihRacuna
					.isSelected());

		if (rez)
			PostavkeBean.setPostavkaDB(
					PostavkeBean.TVRTKA_HZZO_BROJACI_OVISNI_O_GODINI,
					(brojaciOvisniOGodini ? "D" : "N"));

		if (rez)
			PostavkeBean.setIspisUGlavniPrinter(ispisUGlavniPrinter);

		// ako je sve proslo u redu, zatvaramo prozor...
		if (rez)
			this.dispose();
	}// pohraniHzzoPostavke

	private void napuniFormu() {
		this.jtHzzoBrojIsporucitelja.setText(PostavkeBean
				.getHzzoSifraIsporucitelja());

		this.jtPomakTekstaUdesnoRacun.setText(PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_X, "0"));

		this.jtPomakTekstaDoljeRacun.setText(PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_Y, "0"));

		this.jtPomakTekstaDesnoPotOc.setText(PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RECEPT_POTOC_X, "0"));

		this.jtPomakTekstaDoljePotOc.setText(PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RECEPT_POTOC_Y, "0"));

		this.jtFaktorPovecanjaSirinaRacun.setText(PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_SIRINA, "1.0"));

		this.jtFaktorPovecanjaDuzinaRacun.setText(PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_DUZINA, "1.0"));

		this.jcAutomatskoUgradjivanjePozivaNaBroj.setSelected(PostavkeBean
				.getPostavkaDB(PostavkeBean.TVRTKA_HZZO_RACUN_AUTOM_POZIV_BROJ,
						"D").equals("D"));

		this.jcBrojaciOvisniOGodini.setSelected(PostavkeBean
				.isBrojaciOvisniOGodini());

		this.jcKontrolaOsobnihRacuna.setSelected(PostavkeBean
				.isKontrolaOsobnogRacuna());

		this.jcIspisUGlavniPrinter.setSelected(PostavkeBean
				.isIspisUGlavniPrinter());

		String racDop = null;

		// idemo na to da sve postavke budu zapisane u DB, samo nuzno u
		// registrima
		racDop = PostavkeBean.getPostavkaDB(
				PostavkeBean.TVRTKA_HZZO_RACUN_ZA_DOPUNSKO, "");

		if (racDop.equals("")) {
			racDop = PostavkeBean.getPostavkaSustava(
					PostavkeBean.TVRTKA_HZZO_RACUN_ZA_DOPUNSKO, "");
			PostavkeBean.setPostavkaDB(
					PostavkeBean.TVRTKA_HZZO_RACUN_ZA_DOPUNSKO, racDop);
		}

		if (racDop.equals("")) {
			PostavkeBean p = new PostavkeBean();

			racDop = p.getTvrtkaRacun();
			PostavkeBean.setPostavkaDB(
					PostavkeBean.TVRTKA_HZZO_RACUN_ZA_DOPUNSKO, racDop);
		}

		this.jtBrojRacunaZaDopunskoOsiguranje.setText(racDop);
	}// napuniFormu

	private void alert(String poruka) {
		JOptionPane.showMessageDialog(this, poruka, "Upozorenje!",
				JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * This method initializes jcAutomatskoUgradjivanjePozivaNaBroj
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcAutomatskoUgradjivanjePozivaNaBroj() {
		if (jcAutomatskoUgradjivanjePozivaNaBroj == null) {
			jcAutomatskoUgradjivanjePozivaNaBroj = new javax.swing.JCheckBox();
			jcAutomatskoUgradjivanjePozivaNaBroj
					.setText("Automat. ugraðivanje poziva na broj u raèun");
			jcAutomatskoUgradjivanjePozivaNaBroj
					.setMnemonic(java.awt.event.KeyEvent.VK_A);
			jcAutomatskoUgradjivanjePozivaNaBroj.setFont(font);
		}
		return jcAutomatskoUgradjivanjePozivaNaBroj;
	}

	/**
	 * This method initializes jLabel7
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if (jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Broj Racuna za dop. osiguranje: ");
		}
		return jLabel7;
	}

	/**
	 * This method initializes jtBrojRacunaZaDopunskoOsiguranje
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojRacunaZaDopunskoOsiguranje() {
		if (jtBrojRacunaZaDopunskoOsiguranje == null) {
			jtBrojRacunaZaDopunskoOsiguranje = new javax.swing.JTextField();
		}
		return jtBrojRacunaZaDopunskoOsiguranje;
	}

	/**
	 * This method initializes jLabel8
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if (jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText("Pomak teksta dolje kod PotOc:");
		}
		return jLabel8;
	}

	/**
	 * This method initializes jtPomakTekstaDoljePotOc
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPomakTekstaDoljePotOc() {
		if (jtPomakTekstaDoljePotOc == null) {
			jtPomakTekstaDoljePotOc = new javax.swing.JTextField();
		}
		return jtPomakTekstaDoljePotOc;
	}

	/**
	 * This method initializes jLabel9
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if (jLabel9 == null) {
			jLabel9 = new javax.swing.JLabel();
			jLabel9.setText("Pomak teksta udesno kod PotOc:");
		}
		return jLabel9;
	}

	/**
	 * This method initializes jtPomakTekstaDesnoPotOc
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPomakTekstaDesnoPotOc() {
		if (jtPomakTekstaDesnoPotOc == null) {
			jtPomakTekstaDesnoPotOc = new javax.swing.JTextField();
		}
		return jtPomakTekstaDesnoPotOc;
	}

	/**
	 * This method initializes jLabel10
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel10() {
		if (jLabel10 == null) {
			jLabel10 = new javax.swing.JLabel();
			jLabel10.setText("px");
		}
		return jLabel10;
	}

	/**
	 * This method initializes jLabel11
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel11() {
		if (jLabel11 == null) {
			jLabel11 = new javax.swing.JLabel();
			jLabel11.setText("px");
		}
		return jLabel11;
	}

	/**
	 * This method initializes jcKontrolaOsobnihRacuna
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcKontrolaOsobnihRacuna() {
		if (jcKontrolaOsobnihRacuna == null) {
			jcKontrolaOsobnihRacuna = new javax.swing.JCheckBox();
			jcKontrolaOsobnihRacuna.setText("Kontrola osobnih raèuna");
			jcKontrolaOsobnihRacuna
					.setToolTipText("ako ne želite dozvoliti sluèajan unos dva raèuna sa istim brojem osobnog raèuna");
			jcKontrolaOsobnihRacuna.setMnemonic(java.awt.event.KeyEvent.VK_K);
			jcKontrolaOsobnihRacuna.setFont(font);
		}
		return jcKontrolaOsobnihRacuna;
	}

	/**
	 * This method initializes jcBrojaciOvisniOGodini
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcBrojaciOvisniOGodini() {
		if (jcBrojaciOvisniOGodini == null) {
			jcBrojaciOvisniOGodini = new javax.swing.JCheckBox();
			jcBrojaciOvisniOGodini.setName("");
			jcBrojaciOvisniOGodini.setText("Brojaèi ovisni o godini");
			jcBrojaciOvisniOGodini
					.setToolTipText("brojevi obraèuna podešeni da su u odnosu na trenutnu godinu");
			jcBrojaciOvisniOGodini.setMnemonic(java.awt.event.KeyEvent.VK_B);
			jcBrojaciOvisniOGodini.setFont(font);
		}
		return jcBrojaciOvisniOGodini;
	}

	private JCheckBox getJCheckBox1() {
		if (jcIspisUGlavniPrinter == null) {
			jcIspisUGlavniPrinter = new JCheckBox();
			jcIspisUGlavniPrinter.setText("Ispis u pisaè bez odabira");
			jcIspisUGlavniPrinter.setSelected(PostavkeBean
					.isBrojaciOvisniOGodini());
			jcIspisUGlavniPrinter.setMnemonic(java.awt.event.KeyEvent.VK_P);
			jcIspisUGlavniPrinter
					.setToolTipText("ako je ukljuèeno, program Vam neæe nuditi odabir pisaèa pri ispisu, veè æe ispisivati u zadnji odabrani pisaè");
			jcIspisUGlavniPrinter.setName("jcIspisUGlavniPrinter");
			jcIspisUGlavniPrinter.setFont(font);
		}
		return jcIspisUGlavniPrinter;
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"klasa
