/*
 * Project opticari
 *
 */
package biz.sunce.dao.gui;

import javax.swing.JPanel;

import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.ProizvodjacVO;
import biz.sunce.optika.GlavniFrame;

/**
 * datum:2006.06.07
 * @author asabo
 *
 */
public final class Proizvodjac extends JPanel implements GUIEditor<ProizvodjacVO>
{

	private static final long serialVersionUID = 6458146553716879749L;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtNaziv = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtHzzoSifra = null;
	ProizvodjacVO oznaceni=null;
	
	/**
	 * This is the default constructor
	 */
	public Proizvodjac() {
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
		consGridBagConstraints3.gridy = 1;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 1;
		consGridBagConstraints4.gridx = 1;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJtNaziv(), consGridBagConstraints2);
		this.add(getJLabel1(), consGridBagConstraints3);
		this.add(getJtHzzoSifra(), consGridBagConstraints4);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(247*faktor, 80*faktor);
	}
	public void napuniPodatke(ProizvodjacVO ulaz) {
		if (ulaz!=null && ulaz instanceof ProizvodjacVO)
		this.oznaceni=(ProizvodjacVO)ulaz;
		else {this.pobrisiFormu(); return;} 
		
		this.jtNaziv.setText(this.oznaceni.getNaziv());
		this.jtHzzoSifra.setText(this.oznaceni.getHzzoSifra()!=null?""+this.oznaceni.getHzzoSifra().intValue():"");
		this.omoguci();
	}
	public ProizvodjacVO vratiPodatke() {
		if (this.oznaceni==null) return null;
		
		this.oznaceni.setModified(this.jeliIzmjenjen());
		
		this.oznaceni.setNaziv(this.jtNaziv.getText());
		int hzsif=-1;
		try
		{
			hzsif=Integer.parseInt(this.jtHzzoSifra.getText());
		}
		catch(NumberFormatException nfe){hzsif=-1;}
		
		if (hzsif>0)
		this.oznaceni.setHzzoSifra(Integer.valueOf(hzsif));
		else this.oznaceni.setHzzoSifra(null);
		
		return this.oznaceni;
	}//vratiPodatke
	
	public void pobrisiFormu() 
	{
		String p="";
		this.jtNaziv.setText(p);
		this.jtHzzoSifra.setText(p);
		this.oznaceni=null;
		this.onemoguci();
	}
	public boolean isFormaIspravna() {
		return false;
	}
	public void omoguci() { postaviStatuseElemenata(true);}
	public void onemoguci() {postaviStatuseElemenata(false);	}
	
	private void postaviStatuseElemenata(boolean s)
	{
		jtNaziv.setEnabled(s);
		jtHzzoSifra.setEnabled(s);
	}
	public boolean jeliIzmjenjen() 
	{
		return true;
	}
	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
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
			jtNaziv.setToolTipText("Naziv proizvoðaèa");
			jtNaziv.setPreferredSize(new java.awt.Dimension(120,20));
			jtNaziv.setMinimumSize(new java.awt.Dimension(120,20));
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
			jLabel1.setText("Hzzo šifra: ");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jtHzzoSifra
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtHzzoSifra() {
		if(jtHzzoSifra == null) {
			jtHzzoSifra = new javax.swing.JTextField();
			jtHzzoSifra.setToolTipText("peteroznamenkasti broj koji propisuje HZZO");
			jtHzzoSifra.setText("");
			jtHzzoSifra.setPreferredSize(new java.awt.Dimension(120,20));
			jtHzzoSifra.setMinimumSize(new java.awt.Dimension(120,20));
		}
		return jtHzzoSifra;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
