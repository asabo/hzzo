/*
 * Project opticari
 *
 */
package biz.sunce.dao.gui;

import javax.swing.JPanel;

import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.optika.GlavniFrame;

/**
 * datum:2005.12.05
 * @author asabo
 *
 */
public class Djelatnik extends JPanel implements GUIEditor<DjelatnikVO>
{
	private static final long serialVersionUID = 5017398451483247714L;
	
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtIme = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtPrezime = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JPasswordField jtLozinka = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JCheckBox jcAdministrator = null;
	
	private DjelatnikVO objekt;
	
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtKorisnickoIme = null;
	
	int faktor = GlavniFrame.getFaktor();
	/**
	 * This is the default constructor
	 */
	public Djelatnik() {
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
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
		consGridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints31.weightx = 1.0;
		consGridBagConstraints31.gridy = 0;
		consGridBagConstraints31.gridx = 1;
		consGridBagConstraints11.gridy = 0;
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints10.gridy = 4;
		consGridBagConstraints10.gridx = 1;
		consGridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints8.gridy = 4;
		consGridBagConstraints8.gridx = 0;
		consGridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints7.weightx = 1.0;
		consGridBagConstraints7.gridy = 3;
		consGridBagConstraints7.gridx = 1;
		consGridBagConstraints5.gridy = 3;
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints3.gridy = 2;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 2;
		consGridBagConstraints4.gridx = 1;
		consGridBagConstraints1.gridy = 1;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 1;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJtIme(), consGridBagConstraints2);
		this.add(getJLabel1(), consGridBagConstraints3);
		this.add(getJtPrezime(), consGridBagConstraints4);
		this.add(getJLabel2(), consGridBagConstraints5);
		this.add(getJtLozinka(), consGridBagConstraints7);
		this.add(getJLabel3(), consGridBagConstraints8);
		this.add(getJcAdministrator(), consGridBagConstraints10);
		this.add(getJLabel4(), consGridBagConstraints11);
		this.add(getJtKorisnickoIme(), consGridBagConstraints31);
		this.setSize(294*faktor, 107*faktor);
		this.setPreferredSize(new java.awt.Dimension(294,107));
	}
	public void napuniPodatke(DjelatnikVO ulaz) 
	{
	if (ulaz==null) {this.pobrisiFormu(); return;}
	
	this.objekt=(DjelatnikVO)ulaz;
	this.jtIme.setText(this.objekt.getIme());
	this.jtPrezime.setText(this.objekt.getPrezime());
	this.jtLozinka.setText(this.objekt.getLozinka());
	this.jcAdministrator.setSelected(this.objekt.getAdministrator()!=null?this.objekt.getAdministrator().booleanValue():false);		
	this.jtKorisnickoIme.setText(this.objekt.getUsername());
	this.omoguci();
	}//napuniPodatke
	
	public DjelatnikVO vratiPodatke() 
	{
		if (this.objekt==null) return null;
		
		this.objekt.setModified(this.jeliIzmjenjen()); // to prvo uciniti... 
		
		this.objekt.setIme(this.jtIme.getText() );
		this.objekt.setPrezime(this.jtPrezime.getText());
		String pass=null;
		
		if (this.jtLozinka.getPassword()!=null)
		pass=new String(this.jtLozinka.getPassword()); 
		
		this.objekt.setLozinka(pass);
		this.objekt.setAdministrator(Boolean.valueOf(this.jcAdministrator.isSelected()));		
		this.objekt.setUsername(this.jtKorisnickoIme.getText());
		return this.objekt;
	}//vratiPodatke
	
	public void pobrisiFormu() 
	{
	 this.jtIme.setText("");
	 this.jtPrezime.setText("");
	 this.jtLozinka.setText("");
	 this.jcAdministrator.setSelected(false);
	 this.jtKorisnickoIme.setText("");
	 this.objekt=null;
	 this.onemoguci();
	}
	
	public void omoguci(){postaviStatuseElemenata(true);}
	public void onemoguci(){ postaviStatuseElemenata(false);}
	
	private void postaviStatuseElemenata(boolean st)
	{
		this.jtIme.setEnabled(st);
		this.jtPrezime.setEnabled(st);
		this.jtLozinka.setEnabled(st);
		this.jcAdministrator.setEnabled(st);
		this.jtKorisnickoIme.setEnabled(st);
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
			jLabel2.setText("Lozinka: ");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jtLozinka
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private javax.swing.JPasswordField getJtLozinka() {
		if(jtLozinka == null) {
			jtLozinka = new javax.swing.JPasswordField();
		}
		return jtLozinka;
	}
	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Administrator: ");
		}
		return jLabel3;
	}
	/**
	 * This method initializes jcAdministrator
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcAdministrator() {
		if(jcAdministrator == null) {
			jcAdministrator = new javax.swing.JCheckBox();
		}
		return jcAdministrator;
	}
	public boolean isFormaIspravna() {
		return false;
	}
	
	public boolean jeliIzmjenjen() 
	{
		boolean rez=false;
		String pass=null;
		if (this.jtLozinka.getPassword()!=null) pass=new String(this.jtLozinka.getPassword());
		else pass=null;
		
	if (this.objekt==null) return false;
	if (this.objekt.getIme()!=null && !this.objekt.getIme().equals(this.jtIme.getText())) return true;
	if (this.objekt.getPrezime()!=null && !this.objekt.getPrezime().equals(this.jtPrezime.getText())) return true;
	if (this.objekt.getLozinka()!=null && pass!=null && !this.objekt.getLozinka().equals(pass)) return true;
  
  if (this.objekt.getAdministrator()!=null && this.objekt.getAdministrator().booleanValue()!=this.jcAdministrator.isSelected())	
	return true;
	
	if (this.objekt.getUsername()!=null && !this.objekt.getUsername().equals(this.jtKorisnickoIme.getText())) return true;
  
	return rez;
	}//jeliIzmjenjen
	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Korisnièko ime: ");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jtKorisnickoIme
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtKorisnickoIme() {
		if(jtKorisnickoIme == null) {
			jtKorisnickoIme = new javax.swing.JTextField();
		}
		return jtKorisnickoIme;
	}
	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
