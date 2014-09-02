/*
 * Created on 2005.04.20
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package biz.sunce.optika;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.DrzavaVO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.NaocaleVO;
import biz.sunce.opticar.vo.PredlozakVO;
import biz.sunce.opticar.vo.PregledVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.opticar.vo.TipTransakcijeVO;
import biz.sunce.opticar.vo.TransakcijaVO;
import biz.sunce.toedter.calendar.JDateChooser;
import biz.sunce.util.GUI;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;
import biz.sunce.util.gui.DaNeUpit;
import biz.sunce.util.gui.SlikaPanel;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class KlijentFrame extends JFrame implements SlusacOznaceneLabelePretrazivanja,SlusacModelaTablice,PopupMenuListener,ActionListener
 {
 	final KlijentFrame ja=this;
	List predlosci=null;
	List tipoviTransakcija=null;
	//kada korisnik klikne na salji SMS, salji mail ili print
	// tu ce sjediti pokazivac na button koji je kliknut, tako da kad
	// odabere neku opciju iz popup menija, da znamo sta hoce slati...
	JButton odabraniButton=null;
	boolean prikazatiUpozorenja=true;

	private javax.swing.JTabbedPane glavniPanel = null;
	private javax.swing.JPanel jpOpciPodaci = null;
	private javax.swing.JLabel jLabel = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-332,540"
	private javax.swing.JTextField jtIme = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-305,539"
	private javax.swing.JLabel jLabel1 = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-358,762"
	private javax.swing.JTextField jtPrezime = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-305,761"
	private javax.swing.JLabel jLabel2 = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-352,781"
	private javax.swing.JTextField jtAdresa = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-305,780"
	private javax.swing.JLabel jLabel3 = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-349,800"
	private javax.swing.JTextField jtMjesto = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-305,799"
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JToggleButton jtbSpolMusko = null;
	private javax.swing.JToggleButton jtbSpolZensko = null;
	private javax.swing.JPanel jpPregledi = null;
	private javax.swing.JLabel jlDrzava = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-350,820"
	private javax.swing.JTextField jtDrzava = null;  //  @jve:visual-info  decl-index=0 visual-constraint="-305,818"
	private PretrazivanjeProzor drzavePretrazivanje=null;
	private PretrazivanjeProzor mjestaPretrazivanje=null;
	private PretrazivanjeProzor klijentiPretrazivanje=null;
	private KlijentVO oznaceni=null,preporucio=null;
	private MjestoVO mjesto=null;
	private DrzavaVO drzava=null;

	private javax.swing.JLabel jLabel5 = null;
	private SlikaPanel slika = null;
	private javax.swing.JPanel jpPrvaTrecina = null;
	private javax.swing.JTextField jtZip = null;
	private javax.swing.JPanel jpDrugaTrecina = null;
	private javax.swing.JLabel jLabel6 = null;
	private JDateChooser jtDatRodjenja=null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JTextField jtJMBG = null;
	private javax.swing.JLabel jLabel9 = null;
	private javax.swing.JLabel jLabel10 = null;
	private javax.swing.JTextField jtTelefon = null;
	private JDateChooser jtDatUpisa = null;
	private javax.swing.JLabel jLabel11 = null;
	private javax.swing.JTextField jtHZZO = null;
	private javax.swing.JLabel jLabel12 = null;
	private javax.swing.JTextField jtZanimanje = null;
	private javax.swing.JLabel jLabel13 = null;
	private javax.swing.JTextField jtEmail = null;
	private javax.swing.JLabel jLabel14 = null;
	private javax.swing.JTextField jtGSM = null;
	private javax.swing.JCheckBox jcPreminuo = null;
	private javax.swing.JLabel jLabel15 = null;
	private javax.swing.JTextField jtPreporucio = null;
	private javax.swing.JButton jbSpremi = null;
	private javax.swing.JLabel jLabel16 = null;
	private JDateChooser jtSlijedeciPregled = null;

	private Vector slusaci=new Vector(1,1);
	private Vector poruke=new Vector(1,1);


	private javax.swing.JPanel jpSuncaneNaocale = null;
	private javax.swing.JCheckBox jcNeZeliPromo = null;
	private javax.swing.JLabel jLabel17 = null;
	private OsobineNaocalaPanel cvikaPanel = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private JXTable izdano = null;
	TableModel izdaneSuncaneNaocale=null;

	private javax.swing.JButton jbNoveSuncaneNaocale = null;
	private javax.swing.JTextPane jtPodsjetnik = null;
	private javax.swing.JLabel jLabel18 = null;
	private javax.swing.JButton jbPosaljiSMS = null;
	private javax.swing.JButton jbPosaljiMail = null;
	private javax.swing.JButton jbIspisiPoruku = null;
	private javax.swing.JPopupMenu jPopUpPredlosci = null;
	private javax.swing.JButton jbNoviZahtjev = null;
	private javax.swing.JPopupMenu jPopUpTipoviZahtjeva = null;
	private javax.swing.JTextPane jtfNapomena = null;
	private javax.swing.JLabel jLabel19 = null;

	public void dodajSlusaca(SlusacKlijentiFramea sl)
	{
		this.slusaci.add(sl);
	}

	private boolean jeliObjektIzmjenjen()
	{
		try
		{
		if (this.oznaceni==null) return false;
		if (this.oznaceni.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA)
		{
			if (!this.jtIme.getText().equals("")) return true;
			if (!this.jtPrezime.getText().equals("")) return true;
			if (!this.jtAdresa.getText().equals("")) return true;
			if (this.mjesto!=null) return true;
			if (!this.jtJMBG.getText().equals("")) return true;
			//if (this.jtDatRodjenja.getDatum()!=null) return true; // uvijek je podesen
			//if (this.jtDatUpisa.getDatum()!=null) return true;
			if (!this.jtTelefon.getText().equals("")) return true;
			if (!this.jtHZZO.getText().equals("")) return true;
			if (!this.jtZanimanje.getText().equals("")) return true;
			if (!this.jtEmail.getText().equals("")) return true;
			if (!this.jtfNapomena.getText().equals("")) return true;
			//if (this.jtSlijedeciPregled.getDatum()!=null) return true;
		}//ako je oznaceni objekt bez sifre, tj. jos nije isao u bazu podataka

		//dosli smo do tu, oznaceni.sifra je nepostojeca, znaci nije se diralo po korisniku, treba vratiti false i to je to
		if (this.oznaceni!=null && this.oznaceni.getSifra()!=null && this.oznaceni.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA)
		return false;

		if (this.oznaceni.getIme()!=null && !this.oznaceni.getIme().equals(this.jtIme.getText())) return true;
		if (this.oznaceni.getPrezime()!=null && !this.oznaceni.getPrezime().equals(this.jtPrezime.getText())) return true;
		if (this.oznaceni.getAdresa()!=null && !this.oznaceni.getAdresa().equals(this.jtAdresa.getText())) return true;
		if (this.mjesto!=null && this.oznaceni.getMjesto().getSifra().intValue()!=this.mjesto.getSifra().intValue()) return true;
		if (this.jtDatRodjenja!=null && this.oznaceni.getDatRodjenja()!=null && !Util.convertCalendarToString(this.jtDatRodjenja.getDatum(),false).equals(Util.convertCalendarToString(this.oznaceni.getDatRodjenja(),false)) ) return true;
		if (this.jtDatUpisa!=null && this.oznaceni.getDatUpisa()!=null && !Util.convertCalendarToString(this.jtDatUpisa.getDatum(),false).equals(Util.convertCalendarToString(this.oznaceni.getDatUpisa(),false)) ) return true;
    if (this.oznaceni.getJmbg()!=null && !this.jtJMBG.getText().trim().equals(this.oznaceni.getJmbg())) return true;
    if (this.oznaceni.getTel()!=null && !this.jtTelefon.getText().trim().equals(this.oznaceni.getTel())) return true;
		if (this.oznaceni.getHzzo()!=null && !this.jtHZZO.getText().trim().equals(this.oznaceni.getHzzo())) return true;
		if (this.oznaceni.getZanimanje()!=null && !this.jtZanimanje.getText().trim().equals(this.oznaceni.getZanimanje())) return true;
		if (this.oznaceni.getNapomena()!=null && !this.jtfNapomena.getText().trim().equals(this.oznaceni.getNapomena())) return true;

		if (this.oznaceni.getEmail()!=null && !this.jtTelefon.getText().trim().equals(this.oznaceni.getEmail())) return true;
		if (this.oznaceni.getPreporucio()!=null && this.preporucio!=null && this.preporucio.getSifra().intValue()!=this.oznaceni.getPreporucio().getSifra().intValue()) return true;
		if ((this.oznaceni.getPreporucio()==null)^(this.preporucio==null)) return true; // ako su razliciti po pitanju da je jedan null a drugi nije

		if (this.jtSlijedeciPregled!=null && this.oznaceni.getSlijedeciPregled()!=null && !Util.convertCalendarToString(this.jtSlijedeciPregled.getDatum(),false).equals(Util.convertCalendarToString(this.oznaceni.getSlijedeciPregled(),false)) ) return true;

	  if (this.oznaceni.getUmro()!=null && (this.jcPreminuo.isSelected()^this.oznaceni.getUmro().booleanValue())) return true;
		if (this.jcNeZeliPromo.isSelected()==this.oznaceni.isPrimaInfo()) return true; // izmjenjeno je SAMO ako su isti, to se negira pri pohranjivanju..

		}
		catch(NullPointerException npe)
		{
			Logger.fatal("Nulti pokazivaè iznimka kod provjeravanja jeli objekt klijent izmjenjen",npe);
		}

		return false;
	}
 //obavjestava sve registrirane slusace da su podaci na klijent frameu promjenjeni
	private void obavijestiSlusace()
	{
		if (this.slusaci!=null)
		for (int i=0; i<this.slusaci.size(); i++)
		{
			SlusacKlijentiFramea sl=(SlusacKlijentiFramea)this.slusaci.get(i);
			if (sl!=null) sl.klijentIzmjenjen(this);
		}//for i
	}//obavijestSlusace

 		public KlijentFrame(KlijentVO klijent)
 		{
 			this();
 			this.setOznaceni(klijent);
 		}

	public KlijentFrame()
        {
		super();
		initialize();
                GUI.centrirajFrame(this);
		this.getOznaceni(); //da se za svaki slucaj inicijalizira
	}

  public void dodajPoruku(String poruka)
  {
  	this.poruke.add(poruka);
  	this.napuniPodsjetnik();
  }
  private void napuniPodsjetnik()
  {
  	String poruka="";
  	if (this.poruke!=null)
  	for (int i=0; i<this.poruke.size(); i++)
  	{
  		poruka+=" - "+this.poruke.get(i)+"\n";
  	}
  	this.jtPodsjetnik.setText(poruka);
  }//napuniPodsjetnik

  public void izbrisiPoruke()
  {
  	this.poruke.removeAllElements();
  	this.napuniPodsjetnik();
  }

	private void initialize() {
		int faktor = GlavniFrame.getFaktor();
		this.setSize(785*faktor, 564*faktor);
		this.setContentPane(this.getGlavniPanel());
		this.setName("KlijentFrame");
		this.setTitle("Kartica klijenta ");

		this.setJMenuBar(null);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				prikazatiUpozorenja=false;
				//ako ime i prezime,datum,mjesto,adresa nisu uneseni onda nema potrebe pokusavati zapisati podatke..
				if (!( getJtIme().getText().equals("") &&  getJtPrezime().getText().equals("") && getJtAdresa().getText().equals("") && mjesto==null && getJtDatRodjenja().getDatum()==null) )
				{
				if (getOznaceni().isModified() && DaNeUpit.upit("Izmjenili ste podatke o klijentu, \nželite li ih pokušati pohraniti?","Klijent izmjenjen", ja) )
				{ prikazatiUpozorenja=true; if (provjeriElementeNaFormi()) spremiKlijenta();}
				}//if


				if(oznaceni!=null && oznaceni.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
				{

				}

			}//windowClosing
		});
		this.cvikaPanel.onemoguci(); // zasada nema niti jednog oznacenog proizvoda pa se po defaultu onemogucen..
		this.izdaneSuncaneNaocale=new TableModel(DAOFactory.getInstance().getNaocale(),this.getIzdano());

    this.getIzdano().setModel(this.izdaneSuncaneNaocale);
    this.izdaneSuncaneNaocale.dodajSlusaca(this);

	}//initialize

	//15.11.05. -asabo- postavlja filter na table model kako bi se ucitao ispravan popis suncanih naocala
	private void ucitajIzdaneSuncaneNaocaleZaKlijenta()
	{
		SearchCriteria krit=new SearchCriteria();
		ArrayList l=new ArrayList(1);
		krit.setKriterij(SearchCriteria.KRITERIJ_KLJUC);
		l.add(this.getOznaceni().getSifra());
		krit.setPodaci(l);

	  this.izdaneSuncaneNaocale.setFilter(krit);
	}//ucitajIzdaneSuncaneNaocaleZaKlijenta

	private javax.swing.JTabbedPane getGlavniPanel() {
		if(glavniPanel == null) {
			glavniPanel = new javax.swing.JTabbedPane();
			glavniPanel.setOpaque(true);
			glavniPanel.addTab("opæi podaci", null, getJpOpciPodaci(), "ovdje možete pronaæi opæe podatke o klijentu (ime, prezime, adresa, fotografija...)");
			glavniPanel.addTab("Pregledi", null, getJpPregledi(), null);
			glavniPanel.addTab("sunèane naoèale", null, getJpSuncaneNaocale(), "sve sunèane naoèale koje je klijent kupio ");
		}
		return glavniPanel;
	}

	private javax.swing.JPanel getJpOpciPodaci() {
		if(jpOpciPodaci == null) {
			jpOpciPodaci = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints41 = new java.awt.GridBagConstraints();
			consGridBagConstraints41.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints41.weighty = 1.0;
			consGridBagConstraints41.weightx = 1.0;
			consGridBagConstraints41.gridx = 2;
			consGridBagConstraints41.gridy = 0;
			consGridBagConstraints13.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints13.weighty = 1.0;
			consGridBagConstraints13.weightx = 1.0;
			consGridBagConstraints13.gridx = 1;
			consGridBagConstraints13.gridy = 0;
			consGridBagConstraints13.anchor = java.awt.GridBagConstraints.NORTH;
			jpOpciPodaci.setLayout(new java.awt.GridBagLayout());
			jpOpciPodaci.add(getJpPrvaTrecina(), consGridBagConstraints13);
			jpOpciPodaci.add(getJpDrugaTrecina(), consGridBagConstraints41);
			jpOpciPodaci.setName("opæi podaci");
			jpOpciPodaci.setToolTipText("opæi podaci o klijentu");
		}
		return jpOpciPodaci;
	}

	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Ime: ");
		}
		return jLabel;
	}

	private javax.swing.JTextField getJtIme() {
		if(jtIme == null) {
			jtIme = new javax.swing.JTextField();
			jtIme.setNextFocusableComponent(getJtPrezime());
			jtIme.setPreferredSize(new java.awt.Dimension(100,19));
			jtIme.setToolTipText("ime klijenta");
			jtIme.setMinimumSize(new java.awt.Dimension(100,19));
		}
		return jtIme;
	}

	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Prezime: ");
		}
		return jLabel1;
	}

	private javax.swing.JTextField getJtPrezime() {
		if(jtPrezime == null) {
			jtPrezime = new javax.swing.JTextField();
			jtPrezime.setNextFocusableComponent(getJtAdresa());
			jtPrezime.setMinimumSize(new java.awt.Dimension(100,19));
			jtPrezime.setPreferredSize(new java.awt.Dimension(100,19));
		}
		return jtPrezime;
	}

	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Adresa: ");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jtAdresa
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAdresa() {
		if(jtAdresa == null) {
			jtAdresa = new javax.swing.JTextField();
			jtAdresa.setMinimumSize(new java.awt.Dimension(100,19));
			jtAdresa.setPreferredSize(new java.awt.Dimension(110,19));
			jtAdresa.setToolTipText("ulica... ");
			jtAdresa.setText("(nema)");
			jtAdresa.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(java.awt.event.FocusEvent e)
				{ jtAdresa.selectAll();	}
			});
		}
		return jtAdresa;
	}
	/**
	 * This method initializes jLabel3
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Mjesto: ");
		}
		return jLabel3;
	}
	/**
	 * This method initializes jtMjesto
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtMjesto() {
		if(jtMjesto == null) {
			jtMjesto = new javax.swing.JTextField();
			jtMjesto.setMinimumSize(new java.awt.Dimension(65,19));
			jtMjesto.setPreferredSize(new java.awt.Dimension(100,19));

			jtMjesto.setText("(nema)");
			this.mjestaPretrazivanje=new PretrazivanjeProzor(this,DAOFactory.getInstance().getMjesta(),10,20,170,120,this.jtMjesto);
			this.mjestaPretrazivanje.dodajSlusaca(this);
                        this.mjestaPretrazivanje.setMinimumZnakovaZaPretrazivanje(2);
			this.mjestaPretrazivanje.setMaksimumZaPretrazivanje(12);

			jtMjesto.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e)
				{
				// ako je polje prazno pobrisi mjesto bean
				if (jtMjesto.getText().equals("")) mjesto=null;

				//29.01.06. -asabo- nema potrebe prijavljivati dogadjaje slusacu, on sam 'slusa' . . .
				//if (!e.isActionKey())
				//mjestaPretrazivanje.setFilter(jtMjesto.getText()+e.getKeyChar());
				}//keyTyped

			});
			jtMjesto.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(java.awt.event.FocusEvent e) {
					jtMjesto.selectAll();
				}
				public void focusLost(java.awt.event.FocusEvent e)
				{
				if (jtMjesto.getText().equals(""))
				 mjesto=null;

				}
			});
		}
		return jtMjesto;
	}
	/**
	 * This method initializes jLabel4
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Spol: ");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jtbSpolMusko
	 *
	 * @return javax.swing.JToggleButton
	 */
	private javax.swing.JToggleButton getJtbSpolMusko() {
		if(jtbSpolMusko == null) {
			jtbSpolMusko = new javax.swing.JToggleButton();
			jtbSpolMusko.setText("M");
			jtbSpolMusko.setSelected(true);
			jtbSpolMusko.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (jtbSpolMusko.isSelected())
					jtbSpolZensko.setSelected(false);
					else
					jtbSpolZensko.setSelected(true);
				}
			});
		}
		return jtbSpolMusko;
	}
	/**
	 * This method initializes jtbSpolZensko
	 *
	 * @return javax.swing.JToggleButton
	 */
	private javax.swing.JToggleButton getJtbSpolZensko() {
		if(jtbSpolZensko == null) {
			jtbSpolZensko = new javax.swing.JToggleButton();
			jtbSpolZensko.setText("Ž");
			jtbSpolZensko.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					if (jtbSpolZensko.isSelected())
					jtbSpolMusko.setSelected(false);
					else jtbSpolMusko.setSelected(true);
				}
			});
		}
		return jtbSpolZensko;
	}
	/**
	 * This method initializes jpPregledi
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpPregledi() {
		if(jpPregledi == null) {
			jpPregledi = new PreglediPanel(this);

		}
		return jpPregledi;
	}
	/**
	 * This method initializes jlDrzava
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlDrzava() {
		if(jlDrzava == null) {
			jlDrzava = new javax.swing.JLabel();
			jlDrzava.setText("Država: ");
		}
		return jlDrzava;
	}
	/**
	 * This method initializes jtDrzava
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtDrzava() {
		if(jtDrzava == null) {
			jtDrzava = new javax.swing.JTextField();
			jtDrzava.setPreferredSize(new java.awt.Dimension(100,20));
			jtDrzava.setMinimumSize(new java.awt.Dimension(100,20));
			this.drzavePretrazivanje=new PretrazivanjeProzor(this,DAOFactory.getInstance().getDrzava(),10,72,150,100,this.getJtDrzava());
			this.drzavePretrazivanje.dodajSlusaca(this);
			 	jtDrzava.addKeyListener(new java.awt.event.KeyAdapter()
				{
				public void keyTyped(java.awt.event.KeyEvent e)
				{
					if (jtDrzava.getText().equals("")) drzava=null;

					//if (!e.isActionKey())
					//drzavePretrazivanje.setFilter(jtDrzava.getText()+e.getKeyChar());
				}
				});
			jtDrzava.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e)
				{
					if (jtDrzava.getText().equals(""))
					drzava=null;
				}
			});
		}
		return jtDrzava;
	}
	/* (non-Javadoc)
	 * @see biz.sunce.util.SlusacOznaceneLabelePretrazivanja#labelaOznacena(biz.sunce.util.Labela)
	 */
	public void labelaOznacena(Labela labela) {
		Object izv=labela.getIzvornik();
		if (izv instanceof DrzavaVO)
		{
		 if (this.mjesto==null || this.mjesto.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA) // samo ako mjesto ne postoji
		 {
		 	this.drzava=(DrzavaVO)izv;
			this.jtDrzava.setText(labela.getText());
			}
			else
			this.jtDrzava.setText(mjesto.getDrzava().getNaziv());
		}
		else
		if (izv instanceof MjestoVO)
		{
	   MjestoVO mvo=(MjestoVO)izv;
			this.jtMjesto.setText(mvo.getNaziv());
			this.jtDrzava.setText(mvo.getDrzava().getNaziv());
			this.jtZip.setText(""+mvo.getZip());
			this.mjesto=mvo;
			this.drzava=mvo.getDrzava();
		}
		else if (izv instanceof KlijentVO)
		{
			KlijentVO kvo=(KlijentVO)izv;
			// obavezno provjeriti da klijent sam sebe ne preporuci
			if (kvo==null) return;
			if (this.oznaceni.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA)
			{
				JOptionPane.showMessageDialog(this,"Ne možete klijentu unijeti osobu koja ga je preporuèila\ndok prvo ne pohranite klijenta","Upozorenje!",JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (kvo.getSifra().intValue()==this.oznaceni.getSifra().intValue() )
			{
				JOptionPane.showMessageDialog(this,"Osoba ne može samu sebe preporuèiti!","Upozorenje!",JOptionPane.WARNING_MESSAGE);
				return;
			}

			this.jtPreporucio.setText(kvo.getIme()+" "+kvo.getPrezime());
			this.preporucio=kvo;
		}

	}
	/**
	 * This method initializes jLabel5
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("ZIP: ");
			jLabel5.setPreferredSize(new java.awt.Dimension(25,16));
			jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		}
		return jLabel5;
	}
	/**
	 * This method initializes slika
	 *
	 * @return javax.swing.JPanel
	 */
	private SlikaPanel getSlika() {
		if(slika == null) {
			slika = new SlikaPanel();
			slika.setLayout(null);
			int faktor = GlavniFrame.getFaktor();
			slika.setSize(100*faktor,200*faktor);
			slika.setToolTipText("Slika Vašeg klijenta, desnim klikom prebacujete je iz \nmeðuspremnika");
			slika.setPreferredSize(new java.awt.Dimension(150,200));
			slika.setBackground(java.awt.Color.WHITE);
			slika.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		}
		
		return slika;
	}
	/**
	 * This method initializes jpPrvaTrecina
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpPrvaTrecina() {
		if(jpPrvaTrecina == null) {
			jpPrvaTrecina = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints91 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints111 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints131 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints101 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints71 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints81 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints121 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints132 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints17 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints18 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints19 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints20 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints221 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints23 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints211 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints24 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints27 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints112 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints26 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints25 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints113 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints116 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints29 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints117 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints210 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints118 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints119 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints212 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints32 = new java.awt.GridBagConstraints();
			consGridBagConstraints119.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints119.weighty = 1.0;
			consGridBagConstraints119.weightx = 1.0;
			consGridBagConstraints119.gridy = 15;
			consGridBagConstraints119.gridx = 1;
			consGridBagConstraints119.gridwidth = 3;
			consGridBagConstraints212.gridy = 15;
			consGridBagConstraints212.gridx = 0;
			consGridBagConstraints212.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints118.gridy = 17;
			consGridBagConstraints118.gridx = 4;
			consGridBagConstraints117.gridy = 17;
			consGridBagConstraints117.gridx = 1;
			consGridBagConstraints210.gridy = 17;
			consGridBagConstraints210.gridx = 2;
			consGridBagConstraints32.gridy = 17;
			consGridBagConstraints32.gridx = 3;
			consGridBagConstraints116.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints116.weighty = 1.0;
			consGridBagConstraints116.weightx = 1.0;
			consGridBagConstraints116.gridy = 16;
			consGridBagConstraints116.gridx = 1;
			consGridBagConstraints116.gridwidth = 5;
			consGridBagConstraints29.gridy = 16;
			consGridBagConstraints29.gridx = 0;
			consGridBagConstraints29.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints113.gridy = 14;
			consGridBagConstraints113.gridx = 4;
			consGridBagConstraints113.gridwidth = 2;
			consGridBagConstraints26.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints26.weightx = 1.0;
			consGridBagConstraints26.gridy = 14;
			consGridBagConstraints26.gridx = 1;
			consGridBagConstraints26.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints112.gridy = 14;
			consGridBagConstraints112.gridx = 0;
			consGridBagConstraints112.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints27.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints27.weightx = 1.0;
			consGridBagConstraints27.gridy = 13;
			consGridBagConstraints27.gridx = 1;
			consGridBagConstraints25.gridy = 13;
			consGridBagConstraints25.gridx = 0;
			consGridBagConstraints25.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints24.gridy = 13;
			consGridBagConstraints24.gridx = 4;
			consGridBagConstraints24.gridwidth = 2;
			consGridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints23.weightx = 1.0;
			consGridBagConstraints23.gridy = 9;
			consGridBagConstraints23.gridx = 4;
			consGridBagConstraints23.gridwidth = 2;
			consGridBagConstraints221.gridx = 3;
			consGridBagConstraints221.gridy = 9;
			consGridBagConstraints20.gridy = 12;
			consGridBagConstraints20.gridx = 0;
			consGridBagConstraints20.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints211.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints211.weightx = 1.0;
			consGridBagConstraints211.gridy = 12;
			consGridBagConstraints211.gridx = 1;
			consGridBagConstraints19.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints19.weightx = 1.0;
			consGridBagConstraints19.gridy = 11;
			consGridBagConstraints19.gridx = 1;
			consGridBagConstraints18.gridx = 0;
			consGridBagConstraints18.gridy = 11;
			consGridBagConstraints18.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints16.gridx = 0;
			consGridBagConstraints16.gridy = 10;
			consGridBagConstraints16.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints17.weightx = 1.0;
			consGridBagConstraints17.gridx = 1;
			consGridBagConstraints17.gridy = 10;
			consGridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints15.weightx = 1.0;
			consGridBagConstraints15.gridx = 1;
			consGridBagConstraints15.gridy = 8;
			consGridBagConstraints132.gridx = 0;
			consGridBagConstraints132.gridy = 9;
			consGridBagConstraints132.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints121.gridx = 0;
			consGridBagConstraints121.gridy = 8;
			consGridBagConstraints121.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints14.weightx = 1.0;
			consGridBagConstraints14.gridy = 9;
			consGridBagConstraints14.gridx = 1;
			consGridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints11.weightx = 1.0;
			consGridBagConstraints11.gridy = 6;
			consGridBagConstraints11.gridx = 1;
			consGridBagConstraints10.gridx = 0;
			consGridBagConstraints10.gridy = 6;
			consGridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints81.gridy = 7;
			consGridBagConstraints81.gridx = 0;
			consGridBagConstraints71.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints71.weightx = 1.0;
			consGridBagConstraints71.gridy = 7;
			consGridBagConstraints71.gridx = 1;
			consGridBagConstraints71.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 3;
			consGridBagConstraints31.gridy = 0;
			consGridBagConstraints31.gridx = 5;
			consGridBagConstraints22.gridy = 0;
			consGridBagConstraints22.gridx = 4;
			consGridBagConstraints5.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints5.weightx = 1.0;
			consGridBagConstraints6.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints6.weightx = 1.0;
			consGridBagConstraints7.gridx = 0;
			consGridBagConstraints7.gridy = 0;
			consGridBagConstraints8.gridx = 0;
			consGridBagConstraints8.gridy = 1;
			consGridBagConstraints3.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints3.weightx = 1.0;
			consGridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints4.weightx = 1.0;
			consGridBagConstraints4.gridx = 1;
			consGridBagConstraints4.gridy = 3;
			consGridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints111.gridx = 0;
			consGridBagConstraints111.gridy = 5;
			consGridBagConstraints111.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints12.gridy = 4;
			consGridBagConstraints12.gridx = 2;
			consGridBagConstraints131.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints131.weightx = 1.0;
			consGridBagConstraints131.gridy = 4;
			consGridBagConstraints131.gridx = 3;
			consGridBagConstraints3.gridx = 1;
			consGridBagConstraints3.gridy = 0;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints2.weightx = 1.0;
			consGridBagConstraints2.gridx = 1;
			consGridBagConstraints2.gridy = 1;
			consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints6.gridx = 1;
			consGridBagConstraints6.gridy = 5;
			consGridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints91.gridx = 0;
			consGridBagConstraints91.gridy = 3;
			consGridBagConstraints91.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints221.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints101.gridx = 0;
			consGridBagConstraints101.gridy = 4;
			consGridBagConstraints101.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints5.gridx = 1;
			consGridBagConstraints5.gridy = 4;
			consGridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints131.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints113.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
			jpPrvaTrecina.setLayout(new java.awt.GridBagLayout());
			jpPrvaTrecina.add(getJLabel1(), consGridBagConstraints8);
			jpPrvaTrecina.add(getJtPrezime(), consGridBagConstraints2);
			jpPrvaTrecina.add(getJtIme(), consGridBagConstraints3);
			jpPrvaTrecina.add(getJLabel(), consGridBagConstraints7);
			jpPrvaTrecina.add(getJLabel2(), consGridBagConstraints91);
			jpPrvaTrecina.add(getJtAdresa(), consGridBagConstraints4);
			jpPrvaTrecina.add(getJLabel3(), consGridBagConstraints101);
			jpPrvaTrecina.add(getJtMjesto(), consGridBagConstraints5);
			jpPrvaTrecina.add(getJlDrzava(), consGridBagConstraints111);
			jpPrvaTrecina.add(getJtDrzava(), consGridBagConstraints6);
			jpPrvaTrecina.add(getJLabel5(), consGridBagConstraints12);
			jpPrvaTrecina.add(getJtZip(), consGridBagConstraints131);
			jpPrvaTrecina.add(getJLabel4(), consGridBagConstraints1);
			jpPrvaTrecina.add(getJtbSpolMusko(), consGridBagConstraints22);
			jpPrvaTrecina.add(getJtbSpolZensko(), consGridBagConstraints31);
			jpPrvaTrecina.add(getJtDatRodjenja(), consGridBagConstraints71);
			jpPrvaTrecina.add(getJLabel7(), consGridBagConstraints81);
			jpPrvaTrecina.add(getJLabel8(), consGridBagConstraints10);
			jpPrvaTrecina.add(getJtJMBG(), consGridBagConstraints11);
			jpPrvaTrecina.add(getJLabel9(), consGridBagConstraints121);
			jpPrvaTrecina.add(getJLabel10(), consGridBagConstraints132);
			jpPrvaTrecina.add(getJtTelefon(), consGridBagConstraints14);
			jpPrvaTrecina.add(getJtDatUpisa(), consGridBagConstraints15);
			jpPrvaTrecina.add(getJLabel11(), consGridBagConstraints16);
			jpPrvaTrecina.add(getJtHZZO(), consGridBagConstraints17);
			jpPrvaTrecina.add(getJLabel12(), consGridBagConstraints18);
			jpPrvaTrecina.add(getJtZanimanje(), consGridBagConstraints19);
			jpPrvaTrecina.add(getJLabel13(), consGridBagConstraints20);
			jpPrvaTrecina.add(getJtEmail(), consGridBagConstraints211);
			jpPrvaTrecina.add(getJLabel14(), consGridBagConstraints221);
			jpPrvaTrecina.add(getJtGSM(), consGridBagConstraints23);
			jpPrvaTrecina.add(getJcPreminuo(), consGridBagConstraints24);
			jpPrvaTrecina.add(getJLabel15(), consGridBagConstraints25);
			jpPrvaTrecina.add(getJtPreporucio(), consGridBagConstraints27);
			jpPrvaTrecina.add(getJLabel16(), consGridBagConstraints112);
			jpPrvaTrecina.add(getJtSlijedeciPregled(), consGridBagConstraints26);
			jpPrvaTrecina.add(getJcNeZeliPromo(), consGridBagConstraints113);
			jpPrvaTrecina.add(getJtPodsjetnik(), consGridBagConstraints116);
			jpPrvaTrecina.add(getJLabel18(), consGridBagConstraints29);
			jpPrvaTrecina.add(getJbPosaljiSMS(), consGridBagConstraints117);
			jpPrvaTrecina.add(getJbPosaljiMail(), consGridBagConstraints210);
			jpPrvaTrecina.add(getJbIspisiPoruku(), consGridBagConstraints32);
			jpPrvaTrecina.add(getJpopUpPredlosci(), new java.awt.GridBagConstraints());
			jpPrvaTrecina.add(getJbNoviZahtjev(), consGridBagConstraints118);
			jpPrvaTrecina.add(getJPopUpTipoviZahtjeva(), new java.awt.GridBagConstraints());
			jpPrvaTrecina.add(getJtfNapomena(), consGridBagConstraints119);
			jpPrvaTrecina.add(getJLabel19(), consGridBagConstraints212);
		}
		return jpPrvaTrecina;
	}
	/**
	 * This method initializes jtZip
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtZip() {
		if(jtZip == null) {
			jtZip = new javax.swing.JTextField();
			jtZip.setPreferredSize(new java.awt.Dimension(55,20));
			jtZip.setMinimumSize(new java.awt.Dimension(55,20));
		}
		return jtZip;
	}

	// 10.05.05. - asabo -
	// puni formu sa podacim koji se nalaze u objektu this.oznaceni
	// a predstavljaju oznacenog klijenta za kojeg se podaci trebaju promjeniti
	private void napuniFormu()
	{
		//zakljucava odredjene elemenete ako mora..
		podesiElementeNaFormi();

		if (this.oznaceni==null) return;

		this.jtIme.setText(this.oznaceni.getIme());
		this.jtPrezime.setText(this.oznaceni.getPrezime());
		this.jtAdresa.setText(this.oznaceni.getAdresa());
		this.jtMjesto.setText(this.oznaceni.getNazivMjesta());
		this.mjesto=this.oznaceni.getMjesto();
		this.jtZip.setText((this.oznaceni.getMjesto().getZip()!=null?""+this.oznaceni.getMjesto().getZip().intValue():""));
		this.drzava=this.oznaceni.getMjesto().getDrzava(); // drzava treba biti odvojeni objekt zbog upisa neke egzotike...
		this.jtDrzava.setText(this.drzava.getNaziv());
		this.jtJMBG.setText(this.oznaceni.getJmbg());
		this.jtDatRodjenja.setDatum(this.oznaceni.getDatRodjenja());
		this.jtDatUpisa.setDatum(this.oznaceni.getDatUpisa());
		this.jtTelefon.setText(this.oznaceni.getTel());
		this.jtGSM.setText(this.oznaceni.getGsm());
		this.jtHZZO.setText(this.oznaceni.getHzzo());
		this.jtZanimanje.setText(this.oznaceni.getZanimanje());
		this.jtEmail.setText(this.oznaceni.getEmail());

		if (this.oznaceni.getNapomena()!=null)
		this.jtfNapomena.setText(this.oznaceni.getNapomena());

		if (this.oznaceni.getPreporucio()!=null)
		this.jtPreporucio.setText(this.oznaceni.getPreporucio().getIme());
		else
		this.jtPreporucio.setText("");
		this.jcPreminuo.setSelected(this.oznaceni.getUmro().booleanValue());
		if (this.oznaceni.getSlijedeciPregled()!=null)
		this.jtSlijedeciPregled.setDatum(this.oznaceni.getSlijedeciPregled());
		this.slika.setSlika(this.oznaceni.getSlika());

		if (this.oznaceni.getSpol().charValue()==KlijentVO.SPOL_MUSKO)
		{
			this.jtbSpolMusko.setSelected(true);
		}
		else
		this.jtbSpolZensko.setSelected(true);

		//18.10.05. -asabo- dodano
		this.jcNeZeliPromo.setSelected(!this.oznaceni.isPrimaInfo());

	PreglediPanel pp=(PreglediPanel)this.jpPregledi;
	pp.getJlImePrezime().setText(this.oznaceni.getIme()+" "+this.oznaceni.getPrezime());
	pp.getJlAdresa().setText(this.oznaceni.getAdresa());
	pp.getJlMjesto().setText(this.oznaceni.getNazivMjesta());

	//15.11.05. -asabo- dodano
	this.ucitajIzdaneSuncaneNaocaleZaKlijenta();
	//23.12.05. -asabo- dodano
	this.napuniPodsjetnik();

	}//napuniFormu

	// koji trebaju biti ukljuceni, koji iskljuceni...
	private void podesiElementeNaFormi()
	{
		//ako se radi o novom klijentu, necemo dopustiti slanje nikakvih poruka
		if (this.oznaceni==null || this.oznaceni.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA)
		{
			this.getJbPosaljiMail().setEnabled(false);
			this.getJbPosaljiSMS().setEnabled(false);
			this.getJbIspisiPoruku().setEnabled(false);
			this.getJbNoviZahtjev().setEnabled(false);
		}
		else
		{
			this.getJbPosaljiMail().setEnabled(true);
			this.getJbPosaljiSMS().setEnabled(true);
			this.getJbIspisiPoruku().setEnabled(true);
			this.getJbNoviZahtjev().setEnabled(true);
		}
	}

	//public int getSifDjelatnika(){return GlavniFrame.getSifDjelatnika();}

	// podatke na formi trpa u objekt koji predstavlja klijenta
	// ako objekt ne postoji, kreira ga sa nepostojecom sifrom i
	private void napuniObjektPodacimaIzForme()
	{
		// ako je neki od tri objekta null, treba ga instancirati sa -1 sifrom

		if (this.oznaceni.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
		this.oznaceni.setLastUpdatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));

		if (this.drzava==null)
		{
			this.drzava=new DrzavaVO();
			this.drzava.setSifra(DAO.NEPOSTOJECA_SIFRA);
			this.drzava.setNaziv(this.jtDrzava.getText());
		}
		// ako mjesto nije odabrano klikom na ponudjena ili se naziv na formi razlikuje od onog koje je oznaceno
		if (this.mjesto==null || !this.mjesto.getNaziv().equals(this.jtMjesto.getText().trim()))
		{
			int zip=0;
			//pokusat cemo prvo pronaci mjesto na temelju naziva
			String ms=this.jtMjesto.getText().trim();
			try {
				this.mjesto=(MjestoVO)DAOFactory.getInstance().getMjesta().read(ms);
			} catch (SQLException e) {
				Logger.log("Neuspješno traženje podataka o mjestu naziva "+ms,e);
			}
			//ako ga nije ni do sada uspio pronaci, e onda 'izmisljamo' mjesto
			if (this.mjesto==null)
			{
			this.mjesto=new MjestoVO();
			this.mjesto.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
			this.mjesto.setNaziv(this.jtMjesto.getText().trim());
			try{zip=Integer.parseInt(this.jtZip.getText());}
			catch(NumberFormatException nfe)
			{zip=DAO.NEPOSTOJECA_SIFRA;}
			if (zip==DAO.NEPOSTOJECA_SIFRA)
			this.mjesto.setZip(null);
			else
			this.mjesto.setZip(Integer.valueOf(zip));

			this.mjesto.setDrzava(this.drzava);
			}//if mjesto jos uvijek null
		}//if mjesto null

		this.oznaceni.setIme(this.jtIme.getText().trim());
		this.oznaceni.setPrezime(this.jtPrezime.getText().trim());
		this.oznaceni.setAdresa(this.jtAdresa.getText().trim());
		this.oznaceni.setMjesto(this.mjesto);
		this.oznaceni.setJmbg(this.jtJMBG.getText().trim());
		this.oznaceni.setDatRodjenja(this.jtDatRodjenja.getDatum());
		this.oznaceni.setDatUpisa(this.jtDatUpisa.getDatum());
		this.oznaceni.setTel(this.jtTelefon.getText().trim());
		this.oznaceni.setGsm(this.jtGSM.getText().trim());
		this.oznaceni.setHzzo(this.jtHZZO.getText().trim());
		this.oznaceni.setZanimanje(this.jtZanimanje.getText().trim());
		this.oznaceni.setEmail(this.jtEmail.getText().trim());
		this.oznaceni.setPreporucio(this.preporucio);
		this.oznaceni.setUmro(Boolean.valueOf(this.jcPreminuo.isSelected()));
		this.oznaceni.setSpol(new Character(this.jtbSpolMusko.isSelected()?KlijentVO.SPOL_MUSKO:KlijentVO.SPOL_ZENSKO));
                this.oznaceni.setSlijedeciPregled(this.jtSlijedeciPregled.getDatum());
		if (this.slika.getSlika()!=null)
                    this.oznaceni.setSlika(this.slika.getSlika());

                //18.10.05. -asabo- dodani podaci za promo
                this.oznaceni.setPrimaInfo(!this.jcNeZeliPromo.isSelected());

		//24.02.06. -asabo- dodana napomena za klijenta
		if (this.jtfNapomena.getText().trim().equals(""))
		this.oznaceni.setNapomena(null);
		else
		this.oznaceni.setNapomena(this.jtfNapomena.getText().trim());
	}//napuniObjektPodacimaIzForme

	// 11.05.05. ako neki element na formi nije u ispravnom stanju baca alert
	// a nazad vraca false. Prvi los element prekida dalje ispitivanje ostalih elemenata
	private boolean provjeriElementeNaFormi()
	{
		Calendar datRodj=this.jtDatRodjenja.getDatum();
		Calendar datUpisa=this.jtDatUpisa.getDatum();
		String ime,prezime,adresa,mjesto,zip,jmbg,hzzo,mj,drz,email;
		ime=this.jtIme.getText().trim();
		prezime=this.jtPrezime.getText().trim();
		adresa=this.jtAdresa.getText().trim();
		zip=this.jtZip.getText().trim();
		jmbg=this.jtJMBG.getText().trim();
		mj=this.jtMjesto.getText().trim();
		drz=this.jtDrzava.getText().trim();
		email=this.jtEmail.getText().trim();
		hzzo=this.jtHZZO.getText().trim();

		if (datRodj==null && prikazatiUpozorenja) {alert ("Datum rodjenja nije ispravan!"); return false;}
		if (datUpisa==null && prikazatiUpozorenja) {alert ("Datum upisa nije ispravan!"); return false;}

		if (ime==null || ime.length()<3 && prikazatiUpozorenja)
		{alert("Ime nije ispravno!"); return false;		}

		if (prezime==null || prezime.length()<3 && prikazatiUpozorenja)
		{alert("Prezime nije ispravno!"); return false;	}

		if (adresa==null || adresa.length()<3 && prikazatiUpozorenja)
		{alert("adresa nije ispravna!"); return false;	}

		if (this.mjesto==null && (mj==null || mj.length()<3 || drz==null || drz.length()<3) && prikazatiUpozorenja)
		{alert("Mjesto nije ispravno"); return false;}


    //31.08.05. -asabo- dogradjena provjera JMBG broja
		if (jmbg!=null && !jmbg.equals("") &&
                    !Util.checkJMBG(jmbg) && prikazatiUpozorenja)
		{alert("JMBG nije ispravan"); return false;	}

		//03.03.06. -asabo- provjera ispravnost hzzo broja (ako je unesen)
		//hzzo broj mora imati 8 znakova i mi moramo pri slanju hzzo-u dodati devetog...
		if (hzzo!=null && !hzzo.equals("") &&
				hzzo.length()>17 && prikazatiUpozorenja)
		{alert("Broj hzzo iskaznice nije ispravan"); return false;	}


		if (email!=null && !email.equals("") &&
												!Util.checkEmailAddress(email) && prikazatiUpozorenja)
				{alert("Email adresa nije ispravna"); return false;	}

		return true;
	}//provjeriElementeNaFormi

	private void alert(String poruka)
	{
		JOptionPane.showMessageDialog(this,poruka);
	}

	private javax.swing.JPanel getJpDrugaTrecina() {
		if(jpDrugaTrecina == null) {
			jpDrugaTrecina = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints61 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints51 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints110 = new java.awt.GridBagConstraints();
			consGridBagConstraints110.gridy = 2;
			consGridBagConstraints110.gridx = 2;
			consGridBagConstraints110.anchor = java.awt.GridBagConstraints.SOUTHEAST;
			consGridBagConstraints51.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints51.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints51.weighty = 1.0;
			consGridBagConstraints51.weightx = 1.0;
			consGridBagConstraints51.gridy = 1;
			consGridBagConstraints51.gridx = 1;
			consGridBagConstraints51.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints61.gridy = 1;
			consGridBagConstraints61.gridx = 2;
			consGridBagConstraints61.anchor = java.awt.GridBagConstraints.NORTHWEST;
			jpDrugaTrecina.setLayout(new java.awt.GridBagLayout());
			jpDrugaTrecina.add(getSlika(), consGridBagConstraints51);
			jpDrugaTrecina.add(getJLabel6(), consGridBagConstraints61);
			jpDrugaTrecina.add(getJbSpremi(), consGridBagConstraints110);
		}
		return jpDrugaTrecina;
	}
	/**
	 * This method initializes jLabel6
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("slika");
		}
		return jLabel6;
	}
	/**
	 * This method initializes jtDatRodjenja
	 *
	 * @return javax.swing.JTextField
	 */
	private JDateChooser getJtDatRodjenja() {

		if(jtDatRodjenja == null) {
			jtDatRodjenja = new JDateChooser();
			jtDatRodjenja.setPreferredSize(new java.awt.Dimension(145,20));
		}
		return jtDatRodjenja;
	}
	/**
	 * This method initializes jLabel7
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Datum roðenja: ");
		}
		return jLabel7;
	}
	/**
	 * This method initializes jLabel8
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if(jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText("JMBG:");
		}
		return jLabel8;
	}
	/**
	 * This method initializes jtJMBG
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtJMBG() {
		if(jtJMBG == null) {
			jtJMBG = new javax.swing.JTextField();
			jtJMBG.setToolTipText("ako ne znate toèan broj, nemojte unositi ništa...");
		}
		return jtJMBG;
	}
	/**
	 * This method initializes jLabel9
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if(jLabel9 == null) {
			jLabel9 = new javax.swing.JLabel();
			jLabel9.setText("Datum upisa: ");
		}
		return jLabel9;
	}
	/**
	 * This method initializes jLabel10
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel10() {
		if(jLabel10 == null) {
			jLabel10 = new javax.swing.JLabel();
			jLabel10.setText("Telefon: ");
		}
		return jLabel10;
	}
	/**
	 * This method initializes jtTelefon
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtTelefon() {
		if(jtTelefon == null) {
			jtTelefon = new javax.swing.JTextField();
			jtTelefon.setToolTipText("broj fiksnog telefona, ako stranka dade broj GSM ureðaja, stavite ga u GSM polje");
		}
		return jtTelefon;
	}
	/**
	 * This method initializes jtDatUpisa
	 *
	 * @return javax.swing.JTextField
	 */
	private JDateChooser getJtDatUpisa() {
		if(jtDatUpisa == null) {
			jtDatUpisa = new JDateChooser();
                        jtDatUpisa.setDatum(Calendar.getInstance());
			if (this.oznaceni==null)
			jtDatUpisa.setDatum(Calendar.getInstance());
		}
		return jtDatUpisa;
	}
	/**
	 * This method initializes jLabel11
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel11() {
		if(jLabel11 == null) {
			jLabel11 = new javax.swing.JLabel();
			jLabel11.setText("HZZO broj: ");
		}
		return jLabel11;
	}
	/**
	 * This method initializes jtHZZO
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtHZZO() {
		if(jtHZZO == null) {
			jtHZZO = new javax.swing.JTextField();
			jtHZZO.setToolTipText("broj iskaznice osnovnog zdravstvenog osiguranja klijenta, pisati u formatu NNN/NNNNNNNN");
		}
		return jtHZZO;
	}
	/**
	 * This method initializes jLabel12
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel12() {
		if(jLabel12 == null) {
			jLabel12 = new javax.swing.JLabel();
			jLabel12.setText("Zanimanje: ");
		}
		return jLabel12;
	}
	/**
	 * This method initializes jtZanimanje
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtZanimanje() {
		if(jtZanimanje == null) {
			jtZanimanje = new javax.swing.JTextField();
		}
		return jtZanimanje;
	}
	/**
	 * This method initializes jLabel13
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel13() {
		if(jLabel13 == null) {
			jLabel13 = new javax.swing.JLabel();
			jLabel13.setText("E-mail: ");
		}
		return jLabel13;
	}
	/**
	 * This method initializes jtEmail
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtEmail() {
		if(jtEmail == null) {
			jtEmail = new javax.swing.JTextField();
			jtEmail.setToolTipText("email adresa klijenta");
		}
		return jtEmail;
	}
	/**
	 * This method initializes jLabel14
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel14() {
		if(jLabel14 == null) {
			jLabel14 = new javax.swing.JLabel();
			jLabel14.setText("GSM: ");
		}
		return jLabel14;
	}
	/**
	 * This method initializes jtGSM
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtGSM() {
		if(jtGSM == null) {
			jtGSM = new javax.swing.JTextField();
			jtGSM.setToolTipText("broj mobilnog telefona... ");
		}
		return jtGSM;
	}
	/**
	 * This method initializes jcPreminuo
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcPreminuo() {
		if(jcPreminuo == null) {
			jcPreminuo = new javax.swing.JCheckBox();
			jcPreminuo.setText("preminuo");
		}
		return jcPreminuo;
	}
	/**
	 * This method initializes jLabel15
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel15() {
		if(jLabel15 == null) {
			jLabel15 = new javax.swing.JLabel();
			jLabel15.setText("preporuèio: ");
		}
		return jLabel15;
	}
	/**
	 * This method initializes jtPreporucio
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPreporucio() {
		if(jtPreporucio == null) {
			jtPreporucio = new javax.swing.JTextField();

                    klijentiPretrazivanje=new PretrazivanjeProzor(this,DAOFactory.getInstance().getKlijenti(),10,20,120,100,jtPreporucio);

			jtPreporucio.setToolTipText("klijent koji je preporuèio ovu stranku ");
			klijentiPretrazivanje.dodajSlusaca(this);
                        klijentiPretrazivanje.setMaksimumZaPretrazivanje(11);
                        klijentiPretrazivanje.setMinimumZnakovaZaPretrazivanje(3);
                        SearchCriteria krit=new SearchCriteria();
                        krit.setKriterij(biz.sunce.dao.DAO.KRITERIJ_KLIJENT_LIMIT_1000);
                        klijentiPretrazivanje.setKriterij(krit);
			jtPreporucio.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					//if (!e.isActionKey())
					//klijentiPretrazivanje.setFilter(jtPreporucio.getText()+e.getKeyChar());
				}
			});
			jtPreporucio.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e)
				{
				klijentiPretrazivanje.setFilter("");
				}
			});
		}
		return jtPreporucio;
	}
	/**
	 * This method initializes jbSpremi
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbSpremi() {
		if(jbSpremi == null)
		{
			jbSpremi = new javax.swing.JButton();
			jbSpremi.setText("Spremi");
			jbSpremi.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					boolean rez=spremiKlijenta();
					// 13.05.05.relativno nezogdna situacija ako forma za unos klijenta ne bude frame, vec panel unutar
					// nekog drugog framea
					// ako se dogodi kakav exception dispose se nece pozivati
					if (rez)
				  dispose();
				}//mouseClicked
			});
		}//if
		return jbSpremi;
	}

	public boolean spremiKlijenta()
	{
		// prvo sve sa forme u objekt
			napuniObjektPodacimaIzForme();

			  // zatim tjeraj u bazu podataka
			  try
			  {
				if (!provjeriElementeNaFormi()) return false;

				if (oznaceni.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA)
				  DAOFactory.getInstance().getKlijenti().insert(oznaceni);
					 else
					  DAOFactory.getInstance().getKlijenti().update(oznaceni);

				// ako neki objekt prati jeli se klijent izmjenio ili nije, treba to prijaviti..
 				obavijestiSlusace();
			 }
			 catch (SQLException e1)
			 {
				Logger.fatal("Iznimka pri unosu podataka klijenta",e1);
				alert("Nastao je problem pri unosu podataka!");
				return false;
			}

    // za slucaj da je u onoj kartici kreirao suncanu naocalu ili mjenjao po postojecoj, a nije spremio
		spremiPodatkeOIzdanojSuncanojNaocali();

		return true;
	}//spremiKlijenta

	/**
	 * @return
	 */
	public KlijentVO getOznaceni()
	{
		// to je za slucaj da novi klijent dolazi u igru
		if (this.oznaceni==null)
		{
		this.oznaceni=new KlijentVO();
		this.oznaceni.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
		this.oznaceni.setCreated(Calendar.getInstance().getTimeInMillis());
		this.oznaceni.setCreatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
		podesiElementeNaFormi();
		}

		if (this.jeliObjektIzmjenjen()) this.oznaceni.setModified(true);

		return oznaceni;
	}//getOznaceni

	/**
	 * @param klijentVO
	 */
	public void setOznaceni(KlijentVO klijentVO) {
		oznaceni = klijentVO;
		this.napuniFormu();
	}

  // ako smo se prijavili da cemo slusati dogadjaje za odredjeni TableModel, on ce nam pozvati ovu metodu
  // ako korisnik oznaci neki redak U MouseEventu vidimo jeli jedan ili dva klika bilo nad retkom, te koji
  // posiljatelj je poslao poruku (ako se registriramo za vise njih odjednom da znamo koji je poslao poruku
	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj)
	{
		if (posiljatelj==this.izdaneSuncaneNaocale)
		{
		this.spremiPodatkeOIzdanojSuncanojNaocali();
		this.napuniFormuZaIzdanuSuncanuCviku((NaocaleVO)this.izdaneSuncaneNaocale.getData().get(redak));
                }//if
        }//redakOznacen

  private void napuniFormuZaIzdanuSuncanuCviku(NaocaleVO cvika)
  {
  	this.cvikaPanel.napuniFormu(cvika);
  }
	// kada se u nekom retku dogodi da se neki podatak izmjeni, model poziva ovu metodu ako se registriramo da cemo slusati promjene
	// tu bi se trebalo pokrenuti onda nekakvo zapisivanje u bazu podataka
	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj, TableModel posiljatelj)
	{

	}

	/**
	 * This method initializes jLabel16
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel16() {
		if(jLabel16 == null) {
			jLabel16 = new javax.swing.JLabel();
			jLabel16.setText("Slijedeæi pregled: ");
		}
		return jLabel16;
	}
	/**
	 * This method initializes jtSlijedeciPregled
	 *
	 * @return javax.swing.JTextField
	 */
	private JDateChooser getJtSlijedeciPregled() {
		if(jtSlijedeciPregled == null) {
			jtSlijedeciPregled = new JDateChooser();
			jtSlijedeciPregled.setPreferredSize(new java.awt.Dimension(175,20));
			Calendar c=Calendar.getInstance();
			int mjeseci=PostavkeBean.getIntPostavkaSustava("SLIJEDECI_PREGLED_MJESECI",12);
			c.set(Calendar.MONTH,c.get(Calendar.MONTH)+mjeseci);
			jtSlijedeciPregled.setDatum(c);
		}
		return jtSlijedeciPregled;
	}
	/**
	 * This method initializes jpSuncaneNaocale
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpSuncaneNaocale()
	{
		if(jpSuncaneNaocale == null) {
			jpSuncaneNaocale = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints114 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints115 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints28 = new java.awt.GridBagConstraints();
			consGridBagConstraints115.gridy = 1;
			consGridBagConstraints115.gridx = 1;
			consGridBagConstraints28.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints28.weighty = 1.0;
			consGridBagConstraints28.weightx = 1.0;
			consGridBagConstraints28.gridy = 2;
			consGridBagConstraints28.gridx = 0;
			consGridBagConstraints21.gridy = 1;
			consGridBagConstraints21.gridx = 0;
			consGridBagConstraints114.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints114.gridy = 0;
			consGridBagConstraints114.gridx = 0;
			consGridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints115.anchor = java.awt.GridBagConstraints.SOUTHWEST;
			consGridBagConstraints28.gridwidth = 2;
			jpSuncaneNaocale.setLayout(new java.awt.GridBagLayout());
			jpSuncaneNaocale.add(getJLabel17(), consGridBagConstraints114);
			jpSuncaneNaocale.add(getCvikaPanel(), consGridBagConstraints21);
			jpSuncaneNaocale.add(getJScrollPane(), consGridBagConstraints28);
			jpSuncaneNaocale.add(getJbNoveSuncaneNaocale(), consGridBagConstraints115);
			jpSuncaneNaocale.addComponentListener(new java.awt.event.ComponentAdapter() {
				public void componentHidden(java.awt.event.ComponentEvent e) {
					spremiPodatkeOIzdanojSuncanojNaocali(); // ako cvika postoji, trebaju se promjene zabiljeziti
					izdano.clearSelection();
				}
			});
		}
		return jpSuncaneNaocale;
	}//getJpSuncaneNaocale
	/**
	 * This method initializes jcNeZeliPromo
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcNeZeliPromo() {
		if(jcNeZeliPromo == null) {
			jcNeZeliPromo = new javax.swing.JCheckBox();
			jcNeZeliPromo.setText("ne želi promo");
		}
		return jcNeZeliPromo;
	}
	/**
	 * This method initializes jLabel17
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel17() {
		if(jLabel17 == null) {
			jLabel17 = new javax.swing.JLabel();
			jLabel17.setText("Izdane sunèane naoèale");
		}
		return jLabel17;
	}
	/**
	 * This method initializes cvikaPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private OsobineNaocalaPanel getCvikaPanel()
	{
		if(cvikaPanel == null) {
			cvikaPanel = new OsobineNaocalaPanel(this);
			cvikaPanel.setPreferredSize(new java.awt.Dimension(560,170));
			cvikaPanel.setMinimumSize(new java.awt.Dimension(560,168));

			cvikaPanel.setNaziv("sunèane naoèale");
		}
		return cvikaPanel;
	}
	/**
	 * This method initializes izdano
	 *
	 * @return javax.swing.JTable
	 */
	private JXTable getIzdano() {
		if(izdano == null) {
			izdano = new JXTable();
		}
		return izdano;
	}
	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane()
	{
		if(jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getIzdano());
		}
		return jScrollPane;
	}

	private List vratiPopisLijecnika()
		{
			List l=null;
			try
			{
				l=DAOFactory.getInstance().getLijecnici().findAll(null);
			}
			catch(SQLException sqle)
			{
				Logger.fatal("SQL iznimka kod KlijentiFrame.napuniLijecnike",sqle);
			}
			return l;
		}//vratiPopisLijecnika

	/**
	 * This method initializes jbNoveSuncaneNaocale
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbNoveSuncaneNaocale() {
		if(jbNoveSuncaneNaocale == null) {
			jbNoveSuncaneNaocale = new javax.swing.JButton();
			jbNoveSuncaneNaocale.setText("Nove naoèale");
			jbNoveSuncaneNaocale.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e)
				{
					if (oznaceni==null || (oznaceni!=null && oznaceni.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA))
					{
                                            if (biz.sunce.util.gui.DaNeUpit.upit("Da biste mogli upisati suèane naoèale\nprvo trebate pohraniti klijenta. Može?","Pohraniti klijenta?",ja))
                                            {
                                                boolean tmp=prikazatiUpozorenja;
                                                prikazatiUpozorenja=true;
                                                spremiKlijenta();
                                                prikazatiUpozorenja=tmp;
                                            }//if
                                            return;
					}//if oznaceni==null ...
				spremiPodatkeOIzdanojSuncanojNaocali();
				NaocaleVO nvo=cvikaPanel.getUlazni();

				nvo.setZaSunce(true);
				cvikaPanel.napuniFormu(nvo);

				}
			});
		}
		return jbNoveSuncaneNaocale;
	}

	// gleda ima li unesenih podataka u formi za izdanu suncanu naocalu, ako ima i ako je mjenjano onda ili insertira ili updatea podatke
	private void spremiPodatkeOIzdanojSuncanojNaocali()
	{
		NaocaleVO nvo=cvikaPanel.vratiPodatke();

		if (nvo!=null && nvo.isModified())
		{
			boolean update=false;
			try
			{
			if(nvo.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
			 {
			 update=true;
			 DAOFactory.getInstance().getNaocale().update(nvo);
			 }
			else
			 {

				DAOFactory.getInstance().getNaocale().insert(nvo);
				PregledVO pvo=new PregledVO();
				// lijecnik, klijent i datum pregleda obavezni su podaci o pregledu
			  pvo.setSifSuncanihNaocala(nvo.getSifra());
			  pvo.setDatVrijeme(Calendar.getInstance());
			  pvo.setCreated(Calendar.getInstance().getTimeInMillis());
			  pvo.setCreatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
			  LijecnikVO lvo=(LijecnikVO)this.vratiPopisLijecnika().get(0); // prvi lijecnik je 'nema lijecnika'
			  pvo.setSifLijecnika((lvo!=null?lvo.getSifra():Integer.valueOf(DAO.NEPOSTOJECA_SIFRA)));
			  pvo.setSifKlijenta(oznaceni.getSifra());
			  pvo.setKontrolaZaMjeseci(Integer.valueOf(DAO.KONTROLA_ZA_MJESECI));

			  DAOFactory.getInstance().getPregledi().insert(pvo); //insertiranje pregleda
			 }//else

			 //25.11.05. -asabo- jos treba dodati naredbu da se tablica sa izdanim naocalama popuni
			 // moramo malo 'otspavati' prvo, da se razdvoje dretve klika misem i spremanja podataka...
			 new Thread()
			 {
			 public void run()
			 {
			 try{Thread.sleep(5);}catch(Exception e){}
			 ucitajIzdaneSuncaneNaocaleZaKlijenta();
			 }
			 }.start();

			 // na kraju, ako je sve proslo ok, formu izbrisati
			 cvikaPanel.pobrisiFormu();
			}
			catch (SQLException e)
			{
			Logger.log("Nastala SQL iznimka kod pokusaja zapisivanja izdanih suncanih naocala u bazu - upd: "+update,e);
			}
		}//if nvo ispravan
		else
		// nije dirano nista po formi, ok, ali treba formu ipak izbrisati
	  cvikaPanel.pobrisiFormu();
	}//spremiPodatkeOIzdanojSuncanojNaocali
	/**
	 * This method initializes jtPodsjetnik
	 *
	 * @return javax.swing.JTextPane
	 */
	private javax.swing.JTextPane getJtPodsjetnik() {
		if(jtPodsjetnik == null) {
			jtPodsjetnik = new javax.swing.JTextPane();
			jtPodsjetnik.setPreferredSize(new java.awt.Dimension(400,100));
			jtPodsjetnik.setEditable(false);
			jtPodsjetnik.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 14));
			jtPodsjetnik.setForeground(java.awt.Color.red);
			jtPodsjetnik.setBackground(this.getBackground());
			jtPodsjetnik.setToolTipText("ovdje æe pisati odreðene poruke koje bi Vas trebale potsjetiti na potrebne radnje");
		}
		return jtPodsjetnik;
	}
	/**
	 * This method initializes jLabel18
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel18() {
		if(jLabel18 == null) {
			jLabel18 = new javax.swing.JLabel();
			jLabel18.setText("Podsjetnik: ");
		}
		return jLabel18;
	}
	/**
	 * This method initializes jbPosaljiSMS
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbPosaljiSMS() {
		if(jbPosaljiSMS == null) {
			jbPosaljiSMS = new javax.swing.JButton();
			jbPosaljiSMS.setText("Pošalji SMS");
			jbPosaljiSMS.add(this.getJpopUpPredlosci());
			jbPosaljiSMS.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					int vis=getJbPosaljiSMS().getHeight();
					odabraniButton=getJbPosaljiSMS(); //da znamo sta je htio ako sta odabere u meniju
  				//pokazi popup menu iznad dugmeta 'posalji SMS'
	  		  getJpopUpPredlosci().show(getJbPosaljiSMS(),0,-vis-getJbPosaljiSMS().getHeight());
				}//actionPerformed
			});
		}
		return jbPosaljiSMS;
	}
	/**
	 * This method initializes jbPosaljiMail
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbPosaljiMail() {
		if(jbPosaljiMail == null) {
			jbPosaljiMail = new javax.swing.JButton();
			jbPosaljiMail.setText("pošalji mail");
			jbPosaljiMail.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					int vis=getJbPosaljiMail().getHeight();

					odabraniButton=getJbPosaljiMail(); //da znamo sta je htio ako sta odabere u meniju
				  //pokazi popup menu iznad dugmeta 'posalji Mail'
				  getJpopUpPredlosci().show(getJbPosaljiMail(),0,-vis-getJbPosaljiMail().getHeight());
				}
			});
		}
		return jbPosaljiMail;
	}
	/**
	 * This method initializes jbIspisiPoruku
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbIspisiPoruku() {
		if(jbIspisiPoruku == null) {
			jbIspisiPoruku = new javax.swing.JButton();
			jbIspisiPoruku.setText("Ispiši poruku");
			jbIspisiPoruku.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					int vis=getJbIspisiPoruku().getHeight();
				  odabraniButton=getJbIspisiPoruku(); //da znamo sta je htio ako sta odabere u meniju
					//pokazi popup menu iznad dugmeta 'posalji SMS'
				  getJpopUpPredlosci().show(getJbIspisiPoruku(),0,-vis-getJbIspisiPoruku().getHeight());
				}
			});
		}
		return jbIspisiPoruku;
	}
	/**
	 * This method initializes jpSaljiSMS
	 *
	 * @return javax.swing.JPopupMenu
	 */
	private javax.swing.JPopupMenu getJpopUpPredlosci() {
		if(jPopUpPredlosci == null) {
			jPopUpPredlosci = new javax.swing.JPopupMenu();
			jPopUpPredlosci.addPopupMenuListener(this);

			try {
				predlosci=DAOFactory.getInstance().getPredlosci().findAll(null);
			} catch (SQLException e) {
				Logger.log("SQL iznimka kod KlijentFrame.getJpSaljiSMS",e);
				predlosci=null;
			}

		  if (predlosci!=null)
			for (int i=0; i<predlosci.size(); i++)
			{
				PredlozakVO pvo=(PredlozakVO)predlosci.get(i);
				JMenuItem jmeit=new JMenuItem(pvo.getNaziv());
				jmeit.addActionListener(this);
				jPopUpPredlosci.add(jmeit);

			}
			jPopUpPredlosci.setInvoker(getJbPosaljiSMS());
			jPopUpPredlosci.setLabel("Predlošci za SMS");
		}//ako je jpSaljiSMS null
		return jPopUpPredlosci;
	}//getJPopupPredlosci

	public void popupMenuCanceled(PopupMenuEvent e) {
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{

	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

  // menu itemsi predlozaka za sms / mail / print svi su registrirani ovdje prijaviti da su oznaceni
  // mozda ce kasnije trebati dograditi sa nekim drugim itemsima...
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JMenuItem)
		{
	  JMenuItem jmeit=(JMenuItem)e.getSource();

		if (predlosci!=null && this.odabraniButton!=this.jbNoviZahtjev)
			for (int i=0; i<predlosci.size(); i++)
			{
				PredlozakVO pvo=(PredlozakVO)predlosci.get(i);
				if (pvo.getNaziv().equals(jmeit.getText()))
         pokreniPisanjePoruke(pvo);
			}
		 else
	 	if (tipoviTransakcija!=null && this.odabraniButton==this.jbNoviZahtjev)
		for (int i=0; i<tipoviTransakcija.size(); i++)
			{
				TipTransakcijeVO ttvo=(TipTransakcijeVO)tipoviTransakcija.get(i);
				if (ttvo.getNaziv().equals(jmeit.getText()))
				 pokreniKreiranjeNovogZahtjeva(ttvo);
			}
		}//if JMenuItem
	}

	// gleda sta korisnik zeli napisati (SMS, mail, print) i
	// koji je predlozak otvoren te ovisno o tome otvara novi prozor sa porukom
	private void pokreniPisanjePoruke(PredlozakVO predlozak)
	{
		if (this.odabraniButton==this.jbPosaljiSMS)
		pokreniPisanjeSMSPoruke(predlozak);
		else
		if (this.odabraniButton==this.jbPosaljiMail)
		pokreniPisanjeMailPoruke(predlozak);
		else
		if (this.odabraniButton==this.jbIspisiPoruku)
		pokreniPisanjePorukeNaPapir(predlozak);

	}//pokreniPisanjePoruke

	private void pokreniKreiranjeNovogZahtjeva(TipTransakcijeVO ttvo)
	{

		if (ttvo.getSifra().intValue()==TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_REZERVNIM_DIJELOVIMA)
		{
		ZahtjevZaRezervnimDijelomFrame zahtjev=new ZahtjevZaRezervnimDijelomFrame();
		zahtjev.setKlijent(this.oznaceni);
		zahtjev.setTipTranskacije(ttvo);
    biz.sunce.util.GUI.centrirajFrame(zahtjev);
		zahtjev.show();
		}
		else
		if (ttvo.getSifra().intValue()==TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_INTERVENCIJOM )
				{
				ZahtjevZaIntervencijomFrame zahtjev=new ZahtjevZaIntervencijomFrame();
				biz.sunce.util.GUI.centrirajFrame(zahtjev);
				zahtjev.show();
				}
			else
				if (ttvo.getSifra().intValue()==TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_KARTICOM )
						{
					  kreirajZahtjevZaIzdavanjemKartice();
						}
	}//pokreniKreiranjeNovogZahtjeva

	private void kreirajZahtjevZaIzdavanjemKartice()
	{
		List transakcije=null;

		try {
			SearchCriteria krit=new SearchCriteria();
			krit.setKriterij("klijenti_zahtjevi");
			ArrayList l=new ArrayList(2);
			l.add( this.oznaceni.getSifra() ); //sifra klijenta
			l.add(Integer.valueOf(TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_KARTICOM)); //koju tip transakcije trazimo

			krit.setPodaci(l);
			transakcije=DAOFactory.getInstance().getTransakcije().findAll(krit);

			int komada=transakcije!=null?transakcije.size():-1;

			if (komada==-1)
			{
      alert ("nastao je problem pri ispitivanju uvjeta izdavanja kartice.\nMolimo kontaktirajte administratora sustava");
			Logger.log("nekakav problem sa brojem transakcija se pojavio pri trazenju svih zahtjeva za karticom klijenta ");
			return;
			}
			else
			if (komada>0)
			{
			int odgovor=JOptionPane.showConfirmDialog(this,"Veè ste za ovog klijenta zatražili izdavanje kartica: "+komada+" komada!\nŽelite li još jednu karticu za klijenta?","klijent veè posjeduje iskaznicu",
			JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE );

			if (odgovor==JOptionPane.NO_OPTION)
			{
				return;
			}//ako nece jos jednu karticu za korisnika
			}//ako je broj zahtjeva za karticom veci od nula

			// unos transakcije
			try {
			TransakcijaVO tvo=new TransakcijaVO();
			tvo.setOdlazna(true);
			tvo.setSifKlijenta(this.oznaceni.getSifra().intValue() );
			tvo.setTip(TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_KARTICOM);
			//tvo.setVrsta() bi se moglo koristiti kasnije za business,obicnu, platinum ili ne znam ni ja kakvu sve ne karticu, bude li trebalo
			tvo.setSadrzaj(this.oznaceni);// just in case..

			DAOFactory.getInstance().getTransakcije().insert(tvo);
			info("Zahtjev uspješno zabilježen\nObavite što prije sinkronizaciju podataka");
			}
			catch (Exception e1)
			{
				Logger.fatal("Iznimka kod zapisivanja zahtjeva za izdavanjem kartice",e1);
				alert("Nastao je problem pri zapisivanju transakcije. \nKontaktirajte administratora sustava!");
			}

		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod trazenja zahtjeva za izdavanjem kartice",e);
			alert("Nastao je problem pri pregledavanju obavljenih transakcija. \nKontaktirajte administratora sustava!");
		}
	}//kreirajZahtjevZaIzdavanjemKartice

	private void info(String poruka)
	{JOptionPane.showMessageDialog(this,poruka,"Obavijest",JOptionPane.INFORMATION_MESSAGE);}

	private void pokreniPisanjeSMSPoruke(PredlozakVO predlozak)
	{
		SMSFrame sms=new SMSFrame();
    // treba btiti PRVO klijent, onda predlozak, ne da mi se sad popravljati
		sms.setKlijent(this.oznaceni);
		sms.setPredlozak(predlozak);
		sms.pack();
		sms.show();
	}
	private void pokreniPisanjeMailPoruke(PredlozakVO predlozak)
	{
   MailFrame mail=new MailFrame();
   mail.setKlijent(this.oznaceni);
   mail.setPredlozak(predlozak);
   mail.pack();
   mail.show();
	}

	private void pokreniPisanjePorukeNaPapir(PredlozakVO predlozak)
	{

				HtmlPrintParser parser=new HtmlPrintParser();
				String ispis=parser.ucitajHtmlPredlozak(Konstante.PREDLOZAK_POZIV_KLIJENTU);
				ispis=ugradiUHtmlDokument(ispis,predlozak );

				HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis,"poziv_klijentu");
				//TODO ovdje bi trebalo otvoriti novi HtmlEditorPane gdje bi korisnik mogao dirati po tekstu
	}//pokreniPisanjePorukeNaPapir

	private String proparsirajPredlozak(String poruka, KlijentVO klijent)
	{
			PostavkeBean p=new PostavkeBean();
			try
			{
			poruka=poruka.replaceAll("\\n","<br>"); //
			poruka=poruka.replaceAll("\\[TVRTKA\\]",p.getTvrtkaNaziv());
			poruka=poruka.replaceAll("\\[NAZIV_KLIJENTA\\]",klijent!=null?klijent.getIme()+" "+klijent.getPrezime():"?!?");
			poruka=poruka.replaceAll("\\[DATUM_PREGLEDA\\]",klijent!=null&&klijent.getSlijedeciPregled()!=null?Util.convertCalendarToString(klijent.getSlijedeciPregled()):"?!?");
			poruka=poruka.replaceAll("\\[TEL\\]",p.getTvrtkaTelefon());
			poruka=poruka.replaceAll("\\[DATUM\\]",Util.convertCalendarToString(Calendar.getInstance()));
			poruka=poruka.replaceAll("\\[SPOL\\]",klijent!=null && klijent.getSpol().charValue()==KlijentVO.SPOL_MUSKO?"gdin":"gdja");
			poruka=poruka.replaceAll("\\[IME\\]",klijent!=null?klijent.getIme():"?!?");
			poruka=poruka.replaceAll("\\[PREZIME\\]",klijent!=null?klijent.getPrezime():"?!?");
			poruka=poruka.replaceAll("\\[MJESTO\\]",p.getMjestoRada());
			}
			catch(Exception e)
			{
				Logger.fatal("Iznimka kod parsiranja predloska; klijent:"+klijent+" postavke:"+p,e);
			}
			return poruka;
		}//proparsirajPredlozak

	private String ugradiUHtmlDokument(String ispis, PredlozakVO predlozak)
	{
		String isp=ispis;
		String proparsiraniPRedlozakTekst=proparsirajPredlozak(predlozak.getTekst(),this.oznaceni);
		isp=isp.replaceFirst("<!--poziv_tekst-->",proparsiraniPRedlozakTekst);
		isp=isp.replaceFirst("<!--klijent_podaci-->",this.oznaceni!=null?this.oznaceni.toHtmlForEnvelope():"?!?");
		isp=isp.replaceFirst("<!--poziv_predmet-->",predlozak!=null?predlozak.getNaziv():"?!?");

	 return isp;
	//
	}

	/**
	 * This method initializes jbNoviZahtjev
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbNoviZahtjev() {
		if(jbNoviZahtjev == null) {
			jbNoviZahtjev = new javax.swing.JButton();
			jbNoviZahtjev.setText("Novi zahtjev");
			jbNoviZahtjev.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					int vis=0;
					odabraniButton=getJbNoviZahtjev(); //da znamo sta je htio ako sta odabere u meniju
					//pokazi popup menu iznad dugmeta 'posalji SMS'
					getJPopUpTipoviZahtjeva().show(getJbNoviZahtjev(),0,0);
					vis=getJPopUpTipoviZahtjeva().getHeight();
					Point p=getJPopUpTipoviZahtjeva().getLocationOnScreen();
					getJPopUpTipoviZahtjeva().setLocation((int)p.getX(),(int)p.getY()-vis);
				}
			});
		}
		return jbNoviZahtjev;
	}
	/**
	 * This method initializes jPopUpTipoviZahtjeva
	 *
	 * @return javax.swing.JPopupMenu
	 */
	private javax.swing.JPopupMenu getJPopUpTipoviZahtjeva() {
		if(jPopUpTipoviZahtjeva == null)
		{
			jPopUpTipoviZahtjeva = new javax.swing.JPopupMenu();
			try
			{
				tipoviTransakcija=DAOFactory.getInstance().getTipoviTransakcija().findAll(null);
			}
			catch (SQLException e)
			{
			Logger.log("SQL iznimka kod KlijentFrame.getJPopUpTipoviZahtjeva()",e);
			tipoviTransakcija=null;
			}

					if (tipoviTransakcija!=null)
					for (int i=0; i<tipoviTransakcija.size(); i++)
					{
						TipTransakcijeVO ttvo=(TipTransakcijeVO)tipoviTransakcija.get(i);
						JMenuItem jmeit=new JMenuItem(ttvo.getNaziv());
						jmeit.addActionListener(this);
						jPopUpTipoviZahtjeva.add(jmeit);
					}//for i
					jPopUpPredlosci.setInvoker(getJbNoviZahtjev());
		}//if
		return jPopUpTipoviZahtjeva;
	}
	/**
	 * This method initializes jtfNapomena
	 *
	 * @return javax.swing.JTextPane
	 */
	private javax.swing.JTextPane getJtfNapomena() {
		if(jtfNapomena == null) {
			jtfNapomena = new javax.swing.JTextPane();
			jtfNapomena.setMaximumSize(new Dimension(300,100));
			int faktor = GlavniFrame.getFaktor();
			jtfNapomena.setSize(new Dimension(300*faktor,100*faktor));
			jtfNapomena.setMinimumSize(new Dimension(300,50));
		}
		return jtfNapomena;
	}
	/**
	 * This method initializes jLabel19
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel19() {
		if(jLabel19 == null) {
			jLabel19 = new javax.swing.JLabel();
			jLabel19.setText("Napomena: ");
		}
		return jLabel19;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,-23"
