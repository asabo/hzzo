/*
 * Project opticari
 *
 */
package biz.sunce.dao.gui;

import javax.swing.JPanel;

import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.ValueObject;

/**
 * datum:2005.11.30
 * @author asabo
 *
 */
public final class Klijent extends JPanel implements GUIEditor
 {
  KlijentVO objekt=null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtIme = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtPrezime = null;
	private javax.swing.JLabel jLabel2 = null;
	/**
	 * This is the default constructor
	 */
	public Klijent() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		consGridBagConstraints5.gridy = 2;
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints3.gridy = 1;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 1;
		consGridBagConstraints4.gridx = 1;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJtIme(), consGridBagConstraints2);
		this.add(getJLabel1(), consGridBagConstraints3);
		this.add(getJtPrezime(), consGridBagConstraints4);
		this.add(getJLabel2(), consGridBagConstraints5);
		this.setSize(300, 200);
	}
	public void napuniPodatke(ValueObject ulaz) {
	}
	public ValueObject vratiPodatke() {
		if (this.objekt==null) return null;
		
		this.objekt.setModified(this.jeliIzmjenjen());
		
		return this.objekt;
	}
	public void pobrisiFormu() {
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Ime: ");
		}
		return jLabel;
	}
	/**
	 * This method initializes jtIme
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtIme() {
		if(jtIme == null) {
			jtIme = new javax.swing.JTextField();
		}
		return jtIme;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Prezime: ");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jtPrezime
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPrezime() {
		if(jtPrezime == null) {
			jtPrezime = new javax.swing.JTextField();
		}
		return jtPrezime;
	}
	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("nije dovršeno!");
		}
		return jLabel2;
	}
	public boolean isFormaIspravna() {
		return false;
	}
	public void omoguci() {
	}
	public void onemoguci() {
	}
	public boolean jeliIzmjenjen() 
	{
	boolean rez=false;
	if (this.objekt==null) return false;
	if (this.objekt.getIme()!=null && !this.objekt.getIme().equals(this.jtIme.getText())) return true;
	if (this.objekt.getPrezime()!=null && !this.objekt.getPrezime().equals(this.jtPrezime.getText())) return true;
	return rez;
	}
	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
	}//jeliIzmjenjen
}
