/*
 * Project opticari
 *
 */
package biz.sunce.dao.gui;

import javax.swing.JPanel;

import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.PredlozakVO;
import biz.sunce.optika.GlavniFrame;

/**
 * datum:2006.01.08
 * @author asabo
 *
 */
public class Predlozak extends JPanel implements GUIEditor<PredlozakVO>
{
 
	private static final long serialVersionUID = 4021174022603291559L;

	PredlozakVO objekt=null;
  
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtNaziv = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JEditorPane jtaTekst = null;
	/**
	 * This is the default constructor
	 */
	public Predlozak() {
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
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		consGridBagConstraints5.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints5.weighty = 1.0;
		consGridBagConstraints5.weightx = 1.0;
		consGridBagConstraints5.gridy = 1;
		consGridBagConstraints5.gridx = 1;
		consGridBagConstraints3.gridy = 1;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJtNaziv(), consGridBagConstraints2);
		this.add(getJLabel1(), consGridBagConstraints3);
		this.add(getJtaTekst(), consGridBagConstraints5);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(315*faktor, 179*faktor);
	}
	public void napuniPodatke(PredlozakVO ulaz) {
	if (ulaz==null) {pobrisiFormu(); return;	}
 	
	this.objekt=(PredlozakVO)ulaz;

	if (this.objekt.getTipPodataka()==null) 
	 this.objekt.setTipPodataka(PredlozakVO.DEFAULT_TIP_PODATAKA);
	
	this.omoguci();
	this.jtNaziv.setText(this.objekt.getNaziv());
	this.jtaTekst.setText(this.objekt.getTekst());
	}
	public PredlozakVO vratiPodatke() 
	{
		if (this.objekt==null) return null;
		
		//ovo se mora prvo postaviti, nema smisla nakon postavljanja vrijednosti ga namjestati
		this.objekt.setModified(this.jeliIzmjenjen());
	
		
		this.objekt.setNaziv(this.jtNaziv.getText());
		this.objekt.setTekst(this.jtaTekst.getText());
			
	return this.objekt;
	}//vratiPodatke
	public void pobrisiFormu() 
	{
		this.jtaTekst.setText("");
		this.jtNaziv.setText("");
		this.objekt=null;
		this.onemoguci();
	}
	public boolean isFormaIspravna() {
		return jtNaziv.getText().length()>0;
	}
	public void omoguci() {
		this.jtNaziv.setEnabled(true);
		this.jtaTekst.setEnabled(true);
	}
	public void onemoguci() {
		this.jtNaziv.setEnabled(false);
		this.jtaTekst.setEnabled(false);
	}
	public boolean jeliIzmjenjen() 
	{
		if (this.objekt==null) return false;
		if (this.objekt.getNaziv()!=null && !this.objekt.getNaziv().equals(this.jtNaziv.getText())) return true;
		if (this.objekt.getTekst()!=null && !this.objekt.getTekst().equals(this.jtaTekst.getText())) return true;
  	return false;
 	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Naziv: ");
		}
		return jLabel;
	}
	/**
	 * This method initializes jtNaziv
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtNaziv() {
		if(jtNaziv == null) {
			jtNaziv = new javax.swing.JTextField();
		}
		return jtNaziv;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Tekst: ");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jtaTekst
	 * 
	 * @return javax.swing.JEditorPane
	 */
	private javax.swing.JEditorPane getJtaTekst() {
		if(jtaTekst == null) {
			jtaTekst = new javax.swing.JEditorPane();
			jtaTekst.setToolTipText("tekst poruke");
			jtaTekst.setPreferredSize(new java.awt.Dimension(270,150));
			jtaTekst.setMinimumSize(new java.awt.Dimension(270,150));			
		}
		return jtaTekst;
	}
	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
