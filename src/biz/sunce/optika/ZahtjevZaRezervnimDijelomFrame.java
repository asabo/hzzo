/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.TransakcijeDAO;
import biz.sunce.opticar.vo.ArtiklVO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.TipTransakcijeVO;
import biz.sunce.opticar.vo.TransakcijaVO;
import biz.sunce.opticar.vo.VrstaRezervnogDijelaVO;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;

/**
 * datum:2006.01.31
 * @author asabo
 *
 */
public class ZahtjevZaRezervnimDijelomFrame extends JFrame implements SlusacOznaceneLabelePretrazivanja 
 {

	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jlKlijentNaziv = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtBrojKartice = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtModelNaocala = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JComboBox jcPotrebanDio = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextArea jtaNapomena = null;
	private javax.swing.JButton jbPosalji = null;
	private javax.swing.JButton jbOdustani = null;
	
	private KlijentVO klijent=null;
	private VrstaRezervnogDijelaVO vrstaRezervnogDijela=null;
	private TipTransakcijeVO tipTranskacije=null;
	PretrazivanjeProzor modeliNaocalaPretrazivanje=null;
	ArtiklVO artikl=null;
	
	/**
	 * This is the default constructor
	 */
	public ZahtjevZaRezervnimDijelomFrame() {
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
		this.setTitle("Zahtjev za rezervnim dijelovima");
		this.addWindowListener(new java.awt.event.WindowAdapter() { 
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) 
			{
				odustani();    
			}
		});
	}
	
	public void napuniPodatke(TransakcijaVO tvo)
	{
		if (tvo.getBrojKartice()!=null)
		this.jtBrojKartice.setText(tvo.getBrojKartice());
   KlijentVO kvo=null;
   try {
		kvo=(KlijentVO) DAOFactory.getInstance().getKlijenti().read(Integer.valueOf(tvo.getSifKlijenta()));
	} catch (SQLException e) {
		Logger.log("Problem kod trazenja klijenta za zahtjev. sfk: "+tvo.getSifKlijenta(),e);
	}		
	this.jlKlijentNaziv.setText(kvo.toString());
	
	if (tvo.getSifBoje()==0)
	this.jtModelNaocala.setText(tvo.getArtikl());
	else
	{
   try {
		this.artikl=(ArtiklVO)DAOFactory.getInstance().getArtikli().read(Integer.valueOf(tvo.getSifBoje()));
	   this.jtModelNaocala.setText(
	   this.artikl!=null?this.artikl.toString():"?!?");
	} catch (SQLException e1) {
		Logger.log("problem kod citanja artikala: ",e1);
	}
	
	}//else
	
	this.podesiJcPotrebanDio(tvo.getVrsta());
	this.jtaNapomena.setText(tvo.getNapomena());	 	
	}//napuniPodatke

	public void disableForm()
	{
		this.jtBrojKartice.setEnabled(false);
		this.jtModelNaocala.setEnabled(false);
		this.jcPotrebanDio.setEnabled(false);
		this.jtaNapomena.setEnabled(false);
		this.jbPosalji.setEnabled(false);
	}
	
	private void podesiJcPotrebanDio(int sifDijela)
	{
		for (int i=0; i<this.jcPotrebanDio.getItemCount(); i++)
		{
			VrstaRezervnogDijelaVO vrsta=(VrstaRezervnogDijelaVO)this.jcPotrebanDio.getItemAt(i);
			if (vrsta.getSifra().intValue()==sifDijela)
			{
				this.jcPotrebanDio.setSelectedIndex(i);
				break;
			}
		}//for i 
	}
	
	private void odustani()
	{
		this.dispose();
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
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
			consGridBagConstraints11.gridy = 5;
			consGridBagConstraints11.gridx = 1;
			consGridBagConstraints12.gridy = 5;
			consGridBagConstraints12.gridx = 2;
			consGridBagConstraints9.gridy = 4;
			consGridBagConstraints9.gridx = 0;
			consGridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints10.weighty = 1.0;
			consGridBagConstraints10.weightx = 1.0;
			consGridBagConstraints10.gridy = 4;
			consGridBagConstraints10.gridx = 1;
			consGridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints8.weightx = 1.0;
			consGridBagConstraints8.gridy = 3;
			consGridBagConstraints8.gridx = 1;
			consGridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints7.gridy = 3;
			consGridBagConstraints7.gridx = 0;
			consGridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints6.weightx = 1.0;
			consGridBagConstraints6.gridy = 2;
			consGridBagConstraints6.gridx = 1;
			consGridBagConstraints6.gridwidth = 2;
			consGridBagConstraints5.gridy = 2;
			consGridBagConstraints5.gridx = 0;
			consGridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints4.weightx = 1.0;
			consGridBagConstraints4.gridy = 1;
			consGridBagConstraints4.gridx = 1;
			consGridBagConstraints3.gridy = 1;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints2.gridy = 0;
			consGridBagConstraints2.gridx = 1;
			consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints4.gridwidth = 2;
			consGridBagConstraints10.gridwidth = 2;
			consGridBagConstraints8.gridwidth = 2;
			consGridBagConstraints2.gridwidth = 2;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints1);
			jContentPane.add(getJlKlijentNaziv(), consGridBagConstraints2);
			jContentPane.add(getJLabel1(), consGridBagConstraints3);
			jContentPane.add(getJtBrojKartice(), consGridBagConstraints4);
			jContentPane.add(getJLabel2(), consGridBagConstraints5);
			jContentPane.add(getJtModelNaocala(), consGridBagConstraints6);
			jContentPane.add(getJLabel3(), consGridBagConstraints7);
			jContentPane.add(getJcPotrebanDio(), consGridBagConstraints8);
			jContentPane.add(getJLabel4(), consGridBagConstraints9);
			jContentPane.add(getJtaNapomena(), consGridBagConstraints10);
			jContentPane.add(getJbPosalji(), consGridBagConstraints11);
			jContentPane.add(getJbOdustani(), consGridBagConstraints12);
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
			jLabel.setText("Klijent: ");
		}
		return jLabel;
	}
	/**
	 * This method initializes jlKlijentNaziv
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlKlijentNaziv() {
		if(jlKlijentNaziv == null) {
			jlKlijentNaziv = new javax.swing.JLabel();
			jlKlijentNaziv.setText("__");
		}
		return jlKlijentNaziv;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Broj kartice: ");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jtBrojKartice
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojKartice() {
		if(jtBrojKartice == null) {
			jtBrojKartice = new javax.swing.JTextField();
		}
		return jtBrojKartice;
	}
	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Model naoèala: ");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jtModelNaocala
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtModelNaocala() {
		if(jtModelNaocala == null) {
			jtModelNaocala = new javax.swing.JTextField();
			this.modeliNaocalaPretrazivanje=new PretrazivanjeProzor(this,DAOFactory.getInstance().getArtikli(),10,20,150,120,this.jtModelNaocala);
			this.modeliNaocalaPretrazivanje.dodajSlusaca(this);
		 

		}
		return jtModelNaocala;
	}
	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Potreban dio: ");
		}
		return jLabel3;
	}
	/**
	 * This method initializes jcPotrebanDio
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getJcPotrebanDio() {
		if(jcPotrebanDio == null) {
			jcPotrebanDio = new javax.swing.JComboBox();
			jcPotrebanDio.setPreferredSize(new java.awt.Dimension(200,25));
			jcPotrebanDio.setMinimumSize(new java.awt.Dimension(200,25));

			List dijelovi=null;
			
			try {
				dijelovi=DAOFactory.getInstance().getVrsteRezervnihDijelova().findAll(null);
			} catch (SQLException e) 
			{	Logger.log("SQL iznimka kod pokusaja povlacenja rezervnih dijelova - Zahtjev za rezervnim dijelovima",e); dijelovi=null;}
				if (dijelovi!=null)
				for (int i=0; i<dijelovi.size(); i++)
				{
					VrstaRezervnogDijelaVO dio=(VrstaRezervnogDijelaVO)dijelovi.get(i);	
					jcPotrebanDio.addItem(dio);	
				}//for i 
		}//if
		return jcPotrebanDio;
	}
	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Napomena: ");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jtaNapomena
	 * 
	 * @return javax.swing.JTextArea
	 */
	private javax.swing.JTextArea getJtaNapomena() {
		if(jtaNapomena == null) {
			jtaNapomena = new javax.swing.JTextArea();
			jtaNapomena.setRows(10);
			jtaNapomena.setColumns(25);
		}
		return jtaNapomena;
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
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{    
					if (podaciIspravni())
					spremiZahtjev();
				} 			
			});
		}
		return jbPosalji;
	}//getJbPosalji
	
	private boolean podaciIspravni()
	{
		boolean rez=true;
		
		if( (this.artikl!=null && this.artikl.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA) || 
		(this.jtModelNaocala.getText().equals("") && this.artikl==null ))
		{
			alert("Neispravan model naoèala");
			return false;
		}
		return rez;
	}//podaciIspravni
	
	private void alert(String poruka)
	{
		JOptionPane.showMessageDialog(this,poruka,"Upozorenje",JOptionPane.WARNING_MESSAGE);
	}
	
	private void info(String poruka)
	{
			JOptionPane.showMessageDialog(this,poruka,"Obavijest",JOptionPane.INFORMATION_MESSAGE);
  }
	private void spremiZahtjev()
	{
		TransakcijeDAO tdao=DAOFactory.getInstance().getTransakcije();
	
	  TransakcijaVO tvo=new TransakcijaVO();
	  if(this.artikl!=null)
	  tvo.setSifBoje(this.artikl.getSifra().intValue());
	  else
	  tvo.setArtikl(this.jtModelNaocala.getText());
	  
	  tvo.setDatVrijeme(java.util.Calendar.getInstance().getTimeInMillis());
	  tvo.setNapomena(this.jtaNapomena.getText());
	  tvo.setOdlazna(true);
	  tvo.setSifKlijenta(this.klijent.getSifra().intValue());
	  
	  VrstaRezervnogDijelaVO vrsta=(VrstaRezervnogDijelaVO)this.jcPotrebanDio.getSelectedItem();
	  
	  tvo.setTip(TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_REZERVNIM_DIJELOVIMA);
	  tvo.setVrsta((short)vrsta.getSifra().intValue());
	  tvo.setBrojKartice(this.jtBrojKartice.getText());
	
		try
		{
		tdao.insert(tvo);
		info("Zahtjev zabilježen, obavite sinkronizaciju što prije\nkako bi zahtjev što prije stigao na odredište!");
		this.dispose();
		}
		catch(SQLException sqle)
		{
			Logger.fatal("SQL iznimka kod zapisivanja zahtjeva za rezervnim dijelom",sqle);
			alert("Zahtjev nije zabiljezen, molimo kontaktirajte administratora sustava!");
		}
	}//spremiZahtjev
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
	 			odustani();
	 			}
			});
		}
		return jbOdustani;
	}
	public KlijentVO getKlijent() {
		return klijent;
	}

	public VrstaRezervnogDijelaVO getVrstaRezervnogDijela() {
		return vrstaRezervnogDijela;
	}

	public void setKlijent(KlijentVO klijentVO) {
		klijent = klijentVO;
		this.jlKlijentNaziv.setText(klijent.toString());
	}

	public void setVrstaRezervnogDijela(VrstaRezervnogDijelaVO dijelaVO) {
		vrstaRezervnogDijela = dijelaVO;
	}

	public TipTransakcijeVO getTipTranskacije() {
		return tipTranskacije;
	}

	public void setTipTranskacije(TipTransakcijeVO transakcijeVO) {
		tipTranskacije = transakcijeVO;
	}
	public void labelaOznacena(Labela labela) 
	{
		Object izv=labela.getIzvornik();
				if (izv!=null && izv instanceof ArtiklVO)
				{
				 	this.artikl=(ArtiklVO)izv;
					if (this.artikl!=null && this.artikl.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
					this.jtModelNaocala.setText(this.artikl.toString());
 	 
				}//if
	}//labelaOznacena

}  //  @jve:visual-info  decl-index=0 visual-constraint="13,13"
