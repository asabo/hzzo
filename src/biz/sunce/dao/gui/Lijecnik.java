/*
 * Project opticari
 *
 */
package biz.sunce.dao.gui;

import javax.swing.JPanel;

import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.optika.GlavniFrame;

/**
 * datum:2005.11.30
 * @author asabo
 *
 */
public class Lijecnik extends JPanel implements GUIEditor<LijecnikVO>
{
 
	private static final long serialVersionUID = -3282689237539924777L;
	LijecnikVO objekt;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtIme = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtPrezime = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtTitula = null;
	/**
	 * This is the default constructor
	 */
	public Lijecnik() {
		super();
		initialize();
		
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		consGridBagConstraints10.gridy = 2;
		consGridBagConstraints10.gridx = 0;
		consGridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints11.weightx = 1.0;
		consGridBagConstraints11.gridy = 2;
		consGridBagConstraints11.gridx = 1;
		consGridBagConstraints8.gridy = 1;
		consGridBagConstraints8.gridx = 0;
		consGridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints9.weightx = 1.0;
		consGridBagConstraints9.gridy = 1;
		consGridBagConstraints9.gridx = 1;
		consGridBagConstraints6.gridy = 0;
		consGridBagConstraints6.gridx = 0;
		consGridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints7.weightx = 1.0;
		consGridBagConstraints7.gridy = 0;
		consGridBagConstraints7.gridx = 1;
		consGridBagConstraints7.anchor = java.awt.GridBagConstraints.CENTER;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints6);
		this.add(getJtIme(), consGridBagConstraints7);
		this.add(getJLabel1(), consGridBagConstraints8);
		this.add(getJtPrezime(), consGridBagConstraints9);
		this.add(getJLabel2(), consGridBagConstraints10);
		this.add(getJtTitula(), consGridBagConstraints11);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(290*faktor, 68*faktor);
		this.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		this.setPreferredSize(new java.awt.Dimension(290,68));
		this.setMinimumSize(new java.awt.Dimension(290,68));
	}
	public void napuniPodatke(LijecnikVO ulaz) 
	{
	if (ulaz==null) {pobrisiFormu(); return;	}
	this.omoguci();
	this.objekt=(LijecnikVO)ulaz;
	this.jtIme.setText(this.objekt.getIme());
	this.jtPrezime.setText(this.objekt.getPrezime());
	this.jtTitula.setText(this.objekt.getTitula());
	}
	
	public LijecnikVO vratiPodatke() {
		if (this.objekt==null) return null;
		//ovo se mora prvo postaviti, nema smisla nakon postavljanja vrijednosti ga namjestati
		this.objekt.setModified(this.jeliIzmjenjen());
		
		this.objekt.setIme(this.jtIme.getText() );
		this.objekt.setPrezime(this.jtPrezime.getText());
		this.objekt.setTitula(this.jtTitula.getText());
		return this.objekt;
	}//vratiPodatke
	
	public void pobrisiFormu() {
		this.objekt=null;
		this.jtIme.setText("");
		this.jtPrezime.setText("");
		this.jtTitula.setText("");
		this.onemoguci();
	}
	public void omoguci()
	{this.postaviStatusElemenata(true);}
	
	public void onemoguci()
	{	this.postaviStatusElemenata(false);}
	
	private void postaviStatusElemenata(boolean status)
	{
		this.jtIme.setEnabled(status);
		this.jtPrezime.setEnabled(status);
		this.jtTitula.setEnabled(status);
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
			jtIme.addInputMethodListener(new java.awt.event.InputMethodListener() { 
				public void inputMethodTextChanged(java.awt.event.InputMethodEvent e) 
				{ }
				public void caretPositionChanged(java.awt.event.InputMethodEvent e) {} 
			});
		}
		return jtIme;
	}
	public boolean jeliIzmjenjen()
	{
		boolean rez=false;
		if (this.objekt==null) return false;
		if (this.objekt.getIme()!=null && !this.objekt.getIme().equals(this.jtIme.getText())) return true;
		if (this.objekt.getPrezime()!=null && !this.objekt.getPrezime().equals(this.jtPrezime.getText())) return true;
		if (this.objekt.getTitula()!=null && !this.objekt.getTitula().equals(this.jtTitula.getText())) return true;
		return rez;
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
			jLabel2.setText("Titula: ");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jtTitula
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtTitula() {
		if(jtTitula == null) {
			jtTitula = new javax.swing.JTextField();
		}
		return jtTitula;
	}
	public boolean isFormaIspravna() {
		return false;
	}
	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
	}
 
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
