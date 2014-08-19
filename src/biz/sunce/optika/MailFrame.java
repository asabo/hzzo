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
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.01.28
 * @author asabo
 *
 */
public final class MailFrame extends JFrame {
	
	KlijentVO klijent=null;
  PregledVO pregled=null;
  PredlozakVO predlozak=null;
  final MailFrame ja=this;
  
	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtEmail = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextArea jtaTekst = null;
	private javax.swing.JButton jbPosalji = null;
	private javax.swing.JButton jbOdustani = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jlKlijent = null;
	/**
	 * This is the default constructor
	 */
	public MailFrame() {
		super();
		initialize();
	}
	
	public void napuniFormu(PorukaVO pvo)
	{
		this.jtEmail.setText(pvo.getAdresa());
		this.jtaTekst.setText(pvo.getPoruka());		
	}
	
	public void disableForme()
	{
		this.jtEmail.setEditable(false);
		this.jtaTekst.setEditable(false);
		this.jbPosalji.setEnabled(false);
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		int faktor = GlavniFrame.getFaktor();
		this.setSize(300*faktor, 200*faktor);
		this.setContentPane(getJContentPane());
		this.setTitle("Mail poruka klijentu");
		this.setName("MailFrame");
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
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			consGridBagConstraints5.gridy = 3;
			consGridBagConstraints5.gridx = 1;
			consGridBagConstraints6.gridy = 3;
			consGridBagConstraints6.gridx = 2;
			consGridBagConstraints7.gridy = 0;
			consGridBagConstraints7.gridx = 0;
			consGridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints9.gridy = 0;
			consGridBagConstraints9.gridx = 1;
			consGridBagConstraints9.gridwidth = 2;
			consGridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints3.gridy = 2;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints4.weighty = 1.0;
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
			consGridBagConstraints2.gridwidth = 2;
			consGridBagConstraints4.gridwidth = 2;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints1);
			jContentPane.add(getJtEmail(), consGridBagConstraints2);
			jContentPane.add(getJLabel1(), consGridBagConstraints3);
			jContentPane.add(getJtaTekst(), consGridBagConstraints4);
			jContentPane.add(getJbPosalji(), consGridBagConstraints5);
			jContentPane.add(getJbOdustani(), consGridBagConstraints6);
			jContentPane.add(getJLabel2(), consGridBagConstraints7);
			jContentPane.add(getJlKlijent(), consGridBagConstraints9);
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
			jLabel.setText("Email adresa: ");
		}
		return jLabel;
	}
	/**
	 * This method initializes jtEmail
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtEmail() {
		if(jtEmail == null) {
			jtEmail = new javax.swing.JTextField();
			jtEmail.setToolTipText("email adresa Vašeg klijenta, mora biti u formatu ime@domena.xxx");
		}
		return jtEmail;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Poruka: ");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jtaTekst
	 * 
	 * @return javax.swing.JTextArea
	 */
	private javax.swing.JTextArea getJtaTekst() {
		if(jtaTekst == null) {
			jtaTekst = new javax.swing.JTextArea();
			jtaTekst.setPreferredSize(new java.awt.Dimension(350,300));
			jtaTekst.setMinimumSize(new java.awt.Dimension(350,300));
			jtaTekst.setToolTipText("poruka Vašem klijentu");
			jtaTekst.setLineWrap(true);
			jtaTekst.setRows(25);
			jtaTekst.setColumns(80);
			
		}
		return jtaTekst;
	}
	
	public void setPredlozak(PredlozakVO predlozakVO) 
		{
			predlozak = predlozakVO;
		 	 
			new Thread(){@Override
			public void run(){try{Thread.sleep(50);}catch(Exception e){}proparsirajPredlozak(predlozak.getTekst());}}.start();		
		}
	
//mijenja neke elemente u predlosku u stvarne vrijednosti, npr. [tvrtka_naziv] mjenja u pravi naziv tvrtke
 private void proparsirajPredlozak(String poruka)
 {
	 PostavkeBean p=new PostavkeBean();
	 try{
		
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
		 Logger.fatal("Iznimka kod punjenja Mail frame-a: klijent:"+klijent+" pregled:"+pregled+" predlozak:"+predlozak+" postavke:"+p,e);
	 }
 }//proparsirajPredlozak
 
//gleda jeli telefonski broj ispravan, te jeli poruka ispravna i sl. baca upozorenje sta nije ok i vraca false 
 private boolean provjeriFormu()
 {
 if (!biz.sunce.util.Util.checkEmailAddress(this.jtEmail.getText()))
 {JOptionPane.showMessageDialog(this,"Email adresa ne izgleda ispravna!","Upozorenje",JOptionPane.WARNING_MESSAGE); return false;}
 
 return true;	
 }//provjeriFormu
 
 private void setPoruka(String poruka)
 {
 	this.jtaTekst.setText(poruka);
 }
	/**
	 * This method initializes jbPosalji
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbPosalji() {
		if(jbPosalji == null) {
			jbPosalji = new javax.swing.JButton();
			jbPosalji.setText("Pošalji");
			jbPosalji.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					PorukaVO poruka=new PorukaVO();
							 
							poruka.setTipPoruke(PorukaVO.TIP_PORUKE_MAIL);
							poruka.setSifKlijenta(klijent.getSifra().intValue());
							poruka.setSifra(Integer.valueOf(biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA));
							poruka.setPoruka(jtaTekst.getText());
							poruka.setVrstaPoruke(PorukaVO.VRSTA_PORUKE_POZIV_NA_PREGLED); // ovo je za doraditi
							poruka.setAdresa(jtEmail.getText());
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
				}
			});
		}
		return jbPosalji;
	}
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
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					dispose();
				}
			});
		}
		return jbOdustani;
	}
	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Klijent: ");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jlKlijent
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlKlijent() {
		if(jlKlijent == null) {
			jlKlijent = new javax.swing.JLabel();
			jlKlijent.setText("__");
			jlKlijent.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
			jlKlijent.setToolTipText("naziv klijenta kojem upravo pišete email poruku");
		}
		return jlKlijent;
	}
	public void setKlijent(KlijentVO klijentVO) {
		klijent = klijentVO;
		
    this.jlKlijent.setText(klijent.toString());
    this.jtEmail.setText(klijent.getEmail());
	}

	public void setPregled(PregledVO pregledVO) {
		pregled = pregledVO;
	}

}
