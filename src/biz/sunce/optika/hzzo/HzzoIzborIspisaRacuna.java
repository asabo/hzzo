/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import javax.swing.JFrame;

import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.hzzo.ispis.IspisRacunaDopunskoOsiguranje;
import biz.sunce.optika.hzzo.ispis.IspisRacunaOsnovnoOsiguranje;
 
import biz.sunce.optika.hzzo.ispis.IspisRacunaNaObrazac;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.03.20
 * 
 * @author asabo
 * 
 */
public final class HzzoIzborIspisaRacuna extends JFrame {

	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JButton jbRacOsZO = null;
	private javax.swing.JButton jbRacDopZO = null;
 
	private javax.swing.JButton jbPotOc3 = null;
	private javax.swing.JButton jButton = null;

	private RacunVO racun = null;

	private final boolean ispisUDefaultPrinter = PostavkeBean
			.isIspisUGlavniPrinter();

	/**
	 * This is the default constructor
	 */
	public HzzoIzborIspisaRacuna() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Izbor ispisa raèuna");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconImage(GlavniFrame.getImageIcon().getImage());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				racun = null;
				dispose();
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			consGridBagConstraints7.gridy = 4;
			consGridBagConstraints7.gridx = 1;
			consGridBagConstraints4.gridy = 1;
			consGridBagConstraints4.gridx = 0;
			consGridBagConstraints5.gridy = 2;
			consGridBagConstraints5.gridx = 0;
			consGridBagConstraints3.gridy = 0;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints6.gridy = 3;
			consGridBagConstraints6.gridx = 0;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJbRacOsZO(), consGridBagConstraints3);
			jContentPane.add(getJbRacDopZO(), consGridBagConstraints4);
			 
			jContentPane.add(getJbPotOc3(), consGridBagConstraints6);
			jContentPane.add(getJButton(), consGridBagConstraints7);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jbRacOsZO
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbRacOsZO() {
		if (jbRacOsZO == null) {
			jbRacOsZO = new javax.swing.JButton();
			jbRacOsZO.setText("Obrazac za osn. osiguranje");
			jbRacOsZO.setToolTipText("obrazac RacOsZO");
			jbRacOsZO.setMnemonic(java.awt.event.KeyEvent.VK_O);
			jbRacOsZO.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getRacun() != null) {
						IspisRacunaOsnovnoOsiguranje ispis = new IspisRacunaOsnovnoOsiguranje(
								getRacun());
						ispis.printaj(!ispisUDefaultPrinter);
						ispis.finalize(); ispis = null;
						System.gc(); // nek se pocisti smece nastalo nakon
										// printanja
						requestFocus();
					}
				}// actionPerformed
			});
		}
		return jbRacOsZO;
	}

	/**
	 * This method initializes jbRacDopZO
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbRacDopZO() {
		if (jbRacDopZO == null) {
			jbRacDopZO = new javax.swing.JButton();
			jbRacDopZO.setText("Obrazac za dop. osiguranje");
			jbRacDopZO.setToolTipText("Obrazac RacDopZO");
			jbRacDopZO.setMnemonic(java.awt.event.KeyEvent.VK_D);
			jbRacDopZO.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					if (getRacun() != null
							&& !getRacun().getOsnovnoOsiguranje()
									.booleanValue()) {
						IspisRacunaDopunskoOsiguranje ispis = new IspisRacunaDopunskoOsiguranje(
								getRacun());

						ispis.printaj(!ispisUDefaultPrinter);
						// ispis.finalize();
						ispis = null;
						System.gc();
						requestFocus();
					}
				}
			});
		}
		return jbRacDopZO;
	}

 

	/**
	 * This method initializes jbPotOc3
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbPotOc3() {
		if (jbPotOc3 == null) {
			jbPotOc3 = new javax.swing.JButton();
			jbPotOc3.setText("Ispis na HZZO doznaku");
			jbPotOc3.setToolTipText("ispis na poleðinu HZZO-ve doznake za ortopedska/oèna pomagala");
			jbPotOc3.setMnemonic(java.awt.event.KeyEvent.VK_P);
			jbPotOc3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getRacun() != null) {
						IspisRacunaNaObrazac ispis = new IspisRacunaNaObrazac(
								getRacun());
						ispis.printDialog();
					}
				}
			});
		}
		return jbPotOc3;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton() {
		if (jButton == null) {
			jButton = new javax.swing.JButton();
			jButton.setText("Zatvori");
			jButton.setToolTipText("Zatvara ovaj prozor");
			jButton.setMnemonic(java.awt.event.KeyEvent.VK_Z);
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return jButton;
	}

	public RacunVO getRacun() {
		return racun;
	}

	public void setRacun(RacunVO racunVO) {
		racun = racunVO;
		if (racun != null && racun.getOsnovnoOsiguranje().booleanValue()) {
			// ako je racun za osnovno osiguranje, ne moze se printati na
			// obrascu za dodatno
			this.jbRacDopZO.setEnabled(false);
		}
	}

}
