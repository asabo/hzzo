/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import javax.swing.JFrame;

import biz.sunce.optika.GlavniFrame;

/**
 * datum:2006.07.20
 * @author asabo
 *
 */
public final class KreiranjeHzzoIzvjescaNapredakFrame extends JFrame {

	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jlStatus = null;
	private javax.swing.JProgressBar jpNapredak = null;
	/**
	 * This is the default constructor
	 */
	public KreiranjeHzzoIzvjescaNapredakFrame() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		//this.setSize(350, 200);
		this.setIconImage(GlavniFrame.getImageIcon().getImage());
		this.setContentPane(getJContentPane());
		this.setTitle("Kreiranje HZZO izvješæa");		
	}
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
			consGridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints3.weightx = 1.0;
			consGridBagConstraints3.gridy = 2;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints2.gridy = 1;
			consGridBagConstraints2.gridx = 0;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints1.insets = new java.awt.Insets(20,20,20,20);
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints1);
			jContentPane.add(getJlStatus(), consGridBagConstraints2);
			jContentPane.add(getJpNapredak(), consGridBagConstraints3);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("HZZO izvješæe se kreira... ");
			jLabel.setFont(new java.awt.Font("Georgia", java.awt.Font.BOLD, 18));
		}
		return jLabel;
	}
	/**
	 * This method initializes jlStatus
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlStatus() {
		if(jlStatus == null) {
			jlStatus = new javax.swing.JLabel();
			jlStatus.setText("(besposlen)");
			jlStatus.setFont(new java.awt.Font("Century Gothic", java.awt.Font.BOLD, 18));
		}
		return jlStatus;
	}
	/**
	 * This method initializes jpNapredak
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private javax.swing.JProgressBar getJpNapredak() {
		if(jpNapredak == null) {
			jpNapredak = new javax.swing.JProgressBar();
			jpNapredak.setToolTipText("postotak obavljenog posla");
		}
		return jpNapredak;
	}
	public void setStatus(String status)
	{
		this.jlStatus.setText(status);
	}
	public void setVrijednost(int vrijednost)
	{this.jpNapredak.setValue(vrijednost);	}
	
	public void setMaksimum(int max)
	{this.jpNapredak.setMaximum(max);}
	
}
