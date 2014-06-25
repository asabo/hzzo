/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.PorukaVO;
import biz.sunce.opticar.vo.PredlozakVO;
import biz.sunce.opticar.vo.PregledVO;
import biz.sunce.util.GUI;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.01.18
 * @author asabo
 *
 */
public class SMSFrame extends JFrame 
{
  private KlijentVO klijent=null;
  private PredlozakVO predlozak=null;
  private PregledVO pregled=null;
  private int preostaloZnakova=160;
  private int maxZnakova=160;
  final SMSFrame ja=this;
  boolean disableCaret=false; // kad udje u obradu careta jedna dretva da druge ne ulaze
	
	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jlPrimatelj = null;
	private javax.swing.JTextField jtSMSBroj = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jlPreostalo = null;
	private javax.swing.JButton jbSalji = null;
	private javax.swing.JButton jbOdustani = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextPane jtpPoruka = null;
	private javax.swing.JLabel jLabel5 = null;
	/**
	 * This is the default constructor
	 */
	public SMSFrame() {
		super();
		initialize();
	}
	
	public void napuniPodatke(PorukaVO poruka)
	{
		if (poruka==null) return;
		
		this.jtpPoruka.setText(poruka.getPoruka());
		this.jtSMSBroj.setText(poruka.getAdresa());
	}
	
	public void disableForme()
	{
		this.jbSalji.setEnabled(false);
		this.jtpPoruka.setEditable(false);
		this.jtSMSBroj.setEditable(false);
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setName("SMSFrame");
		this.setTitle("SMS poruka klijentu");
		GUI.centrirajFrame(this);
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
			consGridBagConstraints13.gridy = 4;
			consGridBagConstraints13.gridx = 0;
			consGridBagConstraints13.gridwidth = 3;
			consGridBagConstraints11.gridy = 2;
			consGridBagConstraints11.gridx = 0;
			consGridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints12.weighty = 1.0;
			consGridBagConstraints12.weightx = 1.0;
			consGridBagConstraints12.gridy = 2;
			consGridBagConstraints12.gridx = 1;
			consGridBagConstraints12.gridwidth = 2;
			consGridBagConstraints7.gridy = 3;
			consGridBagConstraints7.gridx = 0;
			consGridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints8.gridy = 3;
			consGridBagConstraints8.gridx = 1;
			consGridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints10.gridy = 5;
			consGridBagConstraints10.gridx = 2;
			consGridBagConstraints9.gridy = 5;
			consGridBagConstraints9.gridx = 1;
			consGridBagConstraints5.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints5.weightx = 1.0;
			consGridBagConstraints5.gridy = 1;
			consGridBagConstraints5.gridx = 1;
			consGridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints3.gridy = 1;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints2.gridy = 0;
			consGridBagConstraints2.gridx = 0;
			consGridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints4.gridy = 0;
			consGridBagConstraints4.gridx = 1;
			consGridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints4.gridwidth = 2;
			consGridBagConstraints5.gridwidth = 2;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints2);
			jContentPane.add(getJLabel1(), consGridBagConstraints3);
			jContentPane.add(getJlPrimatelj(), consGridBagConstraints4);
			jContentPane.add(getJtSMSBroj(), consGridBagConstraints5);
			jContentPane.add(getJLabel2(), consGridBagConstraints7);
			jContentPane.add(getJlPreostalo(), consGridBagConstraints8);
			jContentPane.add(getJbSalji(), consGridBagConstraints9);
			jContentPane.add(getJbOdustani(), consGridBagConstraints10);
			jContentPane.add(getJLabel4(), consGridBagConstraints11);
			jContentPane.add(getJtpPoruka(), consGridBagConstraints12);
			jContentPane.add(getJLabel5(), consGridBagConstraints13);
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
			jLabel.setText("Primatelj: ");
			jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("broj: ");
			jLabel1.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
		}
		return jLabel1;
	}
	/**
	 * This method initializes jlPrimatelj
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlPrimatelj() {
		if(jlPrimatelj == null) {
			jlPrimatelj = new javax.swing.JLabel();
			jlPrimatelj.setText("_ _");
		}
		return jlPrimatelj;
	}
	/**
	 * This method initializes jtSMSBroj
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtSMSBroj() {
		if(jtSMSBroj == null) {
			jtSMSBroj = new javax.swing.JTextField();
			jtSMSBroj.setPreferredSize(new java.awt.Dimension(250,20));
			jtSMSBroj.setMinimumSize(new java.awt.Dimension(250,20));

			jtSMSBroj.setToolTipText("GSM broj stranke kojoj saljete SMS poruku");
		}
		return jtSMSBroj;
	}
	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Preostalo znakova: ");
			jLabel2.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
		}
		return jLabel2;
	}
	/**
	 * This method initializes jlPreostalo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlPreostalo() {
		if(jlPreostalo == null) {
			jlPreostalo = new javax.swing.JLabel();
			jlPreostalo.setText("160");
		}
		return jlPreostalo;
	}
	/**
	 * This method initializes jbSalji
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbSalji() {
		if(jbSalji == null) {
			jbSalji = new javax.swing.JButton();
			jbSalji.setText("Šalji");
			jbSalji.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{    
				if (!provjeriFormu()) return;
				
				PorukaVO poruka=new PorukaVO();
				urediNasaSlova(); // skini nasa slova ... 
				poruka.setTipPoruke(PorukaVO.TIP_PORUKE_SMS);
				poruka.setSifKlijenta(klijent.getSifra().intValue());
				poruka.setSifra(Integer.valueOf(biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA));
				poruka.setPoruka(jtpPoruka.getText());
				poruka.setVrstaPoruke(PorukaVO.VRSTA_PORUKE_POZIV_NA_PREGLED); // ovo je za
				poruka.setAdresa(jtSMSBroj.getText());
				try
				{ 
				DAOFactory.getInstance().getPoruke().insert(poruka);
				}
				catch(SQLException sqle)
				{
					Logger.fatal("Insert SMS poruke nije uspit",sqle);
					JOptionPane.showMessageDialog(ja,"Nastao je problem pri pohranjivanju poruke. Molimo Vas, obavite sinkronizaciju podataka ili kontaktirajte administratora sustava!","Upozorenje",JOptionPane.WARNING_MESSAGE);
					return;
				}
	
				// ako je sve ok zatvaramo frame
				if (poruka.getSifra()!=null && poruka.getSifra().intValue()!=biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA)
				dispose();
				}//actionPerformed
			});
		}
		return jbSalji;
	}
	//gleda jeli telefonski broj ispravan, te jeli poruka ispravna i sl. baca upozorenje sta nije ok i vraca false 
	private boolean provjeriFormu()
	{
	if (jtSMSBroj.getText().length()<9)
	{JOptionPane.showMessageDialog(this,"SMS broj ne izgleda ispravan!","Upozorenje",JOptionPane.WARNING_MESSAGE); return false;}
	
	if(jtpPoruka.getText().length()>maxZnakova)
	{JOptionPane.showMessageDialog(this,"SMS poruka je prevelika!","Upozorenje",JOptionPane.WARNING_MESSAGE); return false;} 
 
	return true;	
	}//provjeriFormu
	/**
	 * This method initializes jbOdustani
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbOdustani() {
		if(jbOdustani == null) {
			jbOdustani = new javax.swing.JButton();
			jbOdustani.setText("Odustani");
			jbOdustani.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{    
				dispose();				
				}
			});
		}
		return jbOdustani;
	}
	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("poruka: ");
			jLabel4.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
		}
		return jLabel4;
	}
	/**
	 * This method initializes jtpPoruka
	 * 
	 * @return javax.swing.JTextPane
	 */
	private javax.swing.JTextPane getJtpPoruka() {
		if(jtpPoruka == null) {
			jtpPoruka = new javax.swing.JTextPane();
			jtpPoruka.setToolTipText("Ovdje upisujete tekst poruke klijentu");
			jtpPoruka.setPreferredSize(new java.awt.Dimension(250,110));
			jtpPoruka.addCaretListener(new javax.swing.event.CaretListener() { 
				public void caretUpdate(javax.swing.event.CaretEvent e) 
				{
					if(disableCaret) return; // druge dretve odjeb
					disableCaret=true;
				new Thread()
				{
				@Override
				public void run(){
				try{								
				podesiPreostaloZnakova();
			
				jtpPoruka.revalidate();
				}
				finally{disableCaret=false;}
				}//run
				}.start();    
				}
			});
		}
		return jtpPoruka;
	}
	private void urediNasaSlova()
	{
		String txt=this.jtpPoruka.getText();
		String s=Util.zaravnajNasaSlova(txt,true);
		if (!s.equals(txt))
		{this.jtpPoruka.setText(s); this.jtpPoruka.requestFocusInWindow();} 
	}
	public KlijentVO getKlijent() {
		return klijent;
	}

	public PredlozakVO getPredlozak() {
		return predlozak;
	}

	public int getPreostaloZnakova() {
		return preostaloZnakova;
	}

	public void setKlijent(KlijentVO klijentVO) {
		klijent = klijentVO;
		this.jtSMSBroj.setText(klijent.getGsm());
		this.jlPrimatelj.setText(klijent.toString());
	}

	public void setPredlozak(PredlozakVO predlozakVO) 
	{
		predlozak = predlozakVO;
		 	 
		new Thread(){@Override
		public void run(){try{Thread.sleep(50);}catch(Exception e){}proparsirajPredlozak(predlozak.getTekst());}}.start();		
	}
	
	// mijenja neke elemente u predlosku u stvarne vrijednosti, npr. [tvrtka_naziv] mjenja u pravi naziv tvrtke
	private void proparsirajPredlozak(String poruka)
	{
		PostavkeBean p=new PostavkeBean();
		try{
		poruka=poruka.replaceAll("\\n",""); // enter bi pocistili?
		poruka=poruka.replaceAll("\\[TVRTKA\\]",p.getTvrtkaNaziv());		
		poruka=poruka.replaceAll("\\[NAZIV_KLIJENTA\\]",klijent!=null?klijent.getIme()+" "+klijent.getPrezime():"?!?");
		poruka=poruka.replaceAll("\\[DATUM_PREGLEDA\\]",klijent!=null&&klijent.getSlijedeciPregled()!=null?Util.convertCalendarToString(klijent.getSlijedeciPregled()):"?!?");
		poruka=poruka.replaceAll("\\[TEL\\]",p.getTvrtkaTelefon());		
		poruka=poruka.replaceAll("\\[DATUM\\]",Util.convertCalendarToString(Calendar.getInstance()));
		poruka=poruka.replaceAll("\\[SPOL\\]",klijent!=null && klijent.getSpol().charValue()==KlijentVO.SPOL_MUSKO?"gdin":"gdja");
		poruka=poruka.replaceAll("\\[IME\\]",klijent!=null?klijent.getIme():"?!?");		
		poruka=poruka.replaceAll("\\[PREZIME\\]",klijent!=null?klijent.getPrezime():"?!?");		
		poruka=poruka.replaceAll("\\[MJESTO\\]",p.getMjestoRada());		

		setPoruka(poruka);
		}
		catch(Exception e)
		{
			Logger.fatal("Iznimka kod punjenja SMS frame-a: klijent:"+klijent+" pregled:"+pregled+" predlozak:"+predlozak+" postavke:"+p,e);
		}
	}//proparsirajPredlozak
	
	public void setPoruka(String poruka)
	{
		this.jtpPoruka.setText(poruka);
		podesiPreostaloZnakova();
	}
	
	//ako je poruka manja od 160 znakova, pokazuje koliko preostaje
	//inace reze poruku na 160 znakova.. 
	private void podesiPreostaloZnakova()
	{
		String p=this.jtpPoruka.getText();
	if (p.length()>maxZnakova) 
	{
		p=p.substring(0,maxZnakova);
		 
		this.jtpPoruka.enableInputMethods(false);
		this.jtpPoruka.setText(p);
	 
		this.jtpPoruka.enableInputMethods(true);
	  this.jlPreostalo.setText("0");
	  this.jtpPoruka.requestFocusInWindow(); // vrati mu fokus
	}
	else
	{
		int preostalo=maxZnakova-p.length();
		this.jlPreostalo.setText(""+preostalo);
	}
	}//podesiPreostaloZnakova

	public void setPreostaloZnakova(int i) {
		preostaloZnakova = i;
	}

	public PregledVO getPregled() {
		return pregled;
	}

	public void setPregled(PregledVO pregledVO) {
		pregled = pregledVO;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Slova èæšðž nisu podržana i bit æe zamijenjena");
		}
		return jLabel5;
	}
}
